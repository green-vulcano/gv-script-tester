package tester.execution.engine;

import static tester.settings.Paths.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.script.*;

public class JavaScriptPerformer {

	public void executeJavaScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		ScriptEngine engine = setupJsEngine(data, environment);

		// evaluate JavaScript code from given file
		String fileBody = new String(Files.readAllBytes(Paths.get(JAVASCRIPT_FILE_PATH)));
		engine.eval(fileBody);

	}

	public boolean executeJavaScriptCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		ScriptEngine engine = setupJsEngine(data, environment);

		// evaluate JavaScript code from given file
		String fileBody = new String(Files.readAllBytes(Paths.get(JAVASCRIPT_FILE_PATH)));
		return (boolean) engine.eval(fileBody);

	}

	private ScriptEngine setupJsEngine(it.greenvulcano.gvesb.buffer.GVBuffer data,
			HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();

		// create JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");

		// setup the engine
		engine.put("data", data);
		engine.put("environment", environment);
		return engine;
	}
}