package tester.execution.engine;

import static tester.execution.configuration.Paths.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.greenvulcano.gvesb.identity.GVIdentityHelper;
import tester.settings.Constants.LibraryNames;

import static tester.settings.Constants.IS_FUNCTION;
import static tester.execution.configuration.Paths.LIB_BASE_PATH;

public class ScriptPerformer {

	public enum Language {
		Groovy, JavaScript, python, Undefined
	}

	public enum PropertyType {
		GV, XMLP
	}

	private ScriptPerformer.Language lang;

	public ScriptPerformer(ScriptPerformer.Language lang) {
		this.lang = lang;
	}

	public boolean executeScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		ScriptEngine engine = setupEngine(data, environment);

		// evaluate JavaScript code from given file
		String path = null;
		if(this.lang.name().equals(Language.Groovy.name())) {
			path = GROOVY_FILE_PATH;
		} else if (this.lang.name().equals(Language.JavaScript.name())){
			path = JAVASCRIPT_FILE_PATH;
			// add external js library
			for(LibraryNames lib:LibraryNames.values()) {
				String libPath = LIB_BASE_PATH + lib.name() + ".js";
				System.out.println("> Importing " + libPath + "\n");
				String mustacheLib = new String(Files.readAllBytes(Paths.get(libPath)));
				engine.eval(mustacheLib);
			}
		} else if (this.lang.name().equals(Language.python.name())){
			path = PYTHON_FILE_PATH;
		}
		String fileBody = new String(Files.readAllBytes(Paths.get(path)));
		fileBody = injectProperties(fileBody, data, ScriptPerformer.PropertyType.GV);
		fileBody = injectProperties(fileBody, data, ScriptPerformer.PropertyType.XMLP);
		
		Object r = engine.eval(fileBody);
		if(IS_FUNCTION) {
			return (boolean)r;
		} else {
			return true;
		}

	}

	private ScriptEngine setupEngine(it.greenvulcano.gvesb.buffer.GVBuffer data,
			HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();

		// create script engine
		ScriptEngine engine = factory.getEngineByName(this.lang.name());

		// setup the engine
		engine.put("data", data);
		engine.put("environment", environment);
		engine.put("it.greenvulcano.gvesb.identity.GVIdentityHelper", GVIdentityHelper.class);
		return engine;
	}

	private String injectProperties(String fileBody, it.greenvulcano.gvesb.buffer.GVBuffer data, ScriptPerformer.PropertyType propertyType) {

		List<String> allMatches = new ArrayList<String>();

		String pattern = null;
		HashMap<String,String> xmlp = null;
		if(propertyType.equals(ScriptPerformer.PropertyType.XMLP)) {
			pattern = Pattern.quote("xmlp{{") + ".*" + Pattern.quote("}}");
			xmlp = readXMLP();
		} else if(propertyType.equals(ScriptPerformer.PropertyType.GV)) {
			pattern = Pattern.quote("@{{") + ".*" + Pattern.quote("}}");
		}

		Matcher m = Pattern.compile(pattern)
				.matcher(fileBody);
		while (m.find()) {
			allMatches.add(m.group());
		}

		int count = 0;
		HashSet<String> keys = new HashSet<>();

		for(String s:allMatches) {
			String key = null;
			String value = null;
			if(propertyType.equals(ScriptPerformer.PropertyType.XMLP)) {
				key = s.substring(6, s.length()-2);
				value = xmlp.get(key);
			} else if(propertyType.equals(ScriptPerformer.PropertyType.GV)) {
				key = s.substring(3, s.length()-2);
				value = data.getProperty(key);
			}

			if(!keys.contains(key)) {
				if(value!=null) {
					printReplacement(count, s, value, propertyType);
					fileBody = fileBody.replace(s,value);
				} else {
					printReplacementError(count, s, value, propertyType);
				}
				keys.add(key);
				count ++;
			}
		}

		if(count>0) {
			System.out.println();
		}

		return fileBody;

	}

	private void printReplacement(int count, String replacedString, String value, ScriptPerformer.PropertyType propertyType) {
		if(count==0) {
			System.out.println("> " + propertyType + " Properties replaced:");
		}
		System.out.println("    > " + replacedString + " = " + value);
	}

	private void printReplacementError(int count, String replacedString, String value, ScriptPerformer.PropertyType propertyType) {
		if(count==0) {
			System.out.println("> " + propertyType + " Properties replaced:");
		}
		System.out.println("    > WARNING: unable to replace " + replacedString);
	}

	@SuppressWarnings("unchecked")
	private HashMap<String,String> readXMLP(){
		try {
			String fileBody = new String(Files.readAllBytes(Paths.get(XMLP_PROPERTIES)));
			return new ObjectMapper().readValue(fileBody, HashMap.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}