package tester.execution.engine;

import static tester.execution.configuration.Paths.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.*;

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

}