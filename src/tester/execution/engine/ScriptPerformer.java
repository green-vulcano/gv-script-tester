package tester.execution.engine;

import static tester.execution.configuration.Paths.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ScriptPerformer {

	public enum Language {
		Groovy, JavaScript, Undefined
	}

	private ScriptPerformer.Language lang;

	public ScriptPerformer(ScriptPerformer.Language lang) {
		this.lang = lang;
	}

	public boolean executeScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		ScriptEngine engine = setupJsEngine(data, environment);

		// evaluate JavaScript code from given file
		String path = null;
		if(this.lang.name().equals(Language.Groovy.name())) {
			path = GROOVY_FILE_PATH;
		} else if (this.lang.name().equals(Language.JavaScript.name())){
			path = JAVASCRIPT_FILE_PATH;
		}
		String fileBody = new String(Files.readAllBytes(Paths.get(path)));
		fileBody = addPropertyDeclaration(fileBody);
		fileBody = addXmlpDeclaration(fileBody);
		Object r = engine.eval(fileBody);
		if(r!=null) {
			return (boolean)r;
		} else {
			return true;
		}

	}

	private ScriptEngine setupJsEngine(it.greenvulcano.gvesb.buffer.GVBuffer data,
			HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();

		// create JavaScript engine
		ScriptEngine engine = factory.getEngineByName(this.lang.name());

		// setup the engine
		engine.put("data", data);
		engine.put("environment", environment);
		return engine;
	}

	private String addPropertyDeclaration(String fileBody) {

		List<String> allMatches = new ArrayList<String>();
		// \Q@{{\E.*\Q}}\E
		String pattern = Pattern.quote("@{{") + ".*" + Pattern.quote("}}");
		Matcher m = Pattern.compile(pattern)
				.matcher(fileBody);
		while (m.find()) {
			allMatches.add(m.group());
		}

		for(String s:allMatches) {
			String newString = "data.getProperty(\"" + s.substring(3, s.length()-2) + "\")";
			fileBody = fileBody.replace(s,newString);
		}
		return fileBody;

	}

	private String addXmlpDeclaration(String fileBody) {

		List<String> allMatches = new ArrayList<String>();
		// \Q@{{\E.*\Q}}\E
		String pattern = Pattern.quote("xmlp{{") + ".*" + Pattern.quote("}}");
		Matcher m = Pattern.compile(pattern)
				.matcher(fileBody);
		while (m.find()) {
			allMatches.add(m.group());
		}
		
		HashMap<String,String> xmlp = readXMLP();
		
		if(xmlp==null) return fileBody;

		int count = 0;
		
		for(String s:allMatches) {
			String key = s.substring(6, s.length()-2);
			String newString = xmlp.get(key);
			if(newString!=null) {
				if(count==0) {
					System.out.println("> XMLP Properties replaced:");
				}
				System.out.println("    > " + key + " = " + newString);
				fileBody = fileBody.replace(s,newString);
				count ++;
			}
		}
		
		if(count>0) {
			System.out.println();
		}
		
		return fileBody;

	}

	private HashMap<String,String> readXMLP(){
		try {
			String fileBody = new String(Files.readAllBytes(Paths.get(XMLP_PROPERTIES)));
			return new ObjectMapper().readValue(fileBody, HashMap.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}