package tester.execution;

import static tester.execution.configuration.Paths.ENV_PROPERTIES_PATH_FIRST_PART;
import static tester.execution.configuration.Paths.ENV_PROPERTIES_PATH_LAST_PART;
import static tester.execution.configuration.Paths.GV_DATA_BUFFER_PROPERTIES_NAME;
import static tester.execution.configuration.Paths.GV_OUTPUT_DATA_BUFFER_PATH;
import static tester.execution.configuration.Paths.LOG_FILE_PATH;
import static tester.settings.Constants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map.Entry;

import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.buffer.GVException;
import it.greenvulcano.gvesb.identity.GVIdentityHelper;
import it.greenvulcano.gvesb.identity.IdentityInfo;
import it.greenvulcano.gvesb.identity.impl.DummyIdentityInfo;
import tester.execution.buffer.BufferHandler;
import tester.execution.engine.ScriptPerformer;
import tester.execution.output.Visualizer;

public class GVScriptTester{

	private static String inputBufferName = "input";

	public static void main(String[] args) throws Exception {
		GVBuffer data = new GVBuffer();
		HashMap<String,GVBuffer> environment = new HashMap<String,GVBuffer>();
		Files.write(Paths.get(LOG_FILE_PATH), "".getBytes());
		initializeTest(data, environment);
		initializeRoles();
		
		boolean conditionReturn;
		try {
			conditionReturn = executeTest(data, environment);
			showBuffers(data, environment);
			logBuffers(data, environment);
			saveBuffers(environment);
			if(IS_FUNCTION) {
				System.out.println("> Returned value = " + conditionReturn);
				System.out.println();
			}
		} catch (Exception e) {
			String error = "> SCRIPT EXECUTION ERROR!\n> Error: ";
			System.out.print(error);
			e.printStackTrace(); 
			logBuffers(data, environment);
			Files.write(Paths.get(LOG_FILE_PATH), (error + e.getMessage()).getBytes(), StandardOpenOption.APPEND);

			System.out.println();
			System.out.println("> Output buffers in " + LOG_FILE_PATH);
		}

	}

	////////////////////////////////////////////////

	private static void initializeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws GVException, IOException {

		String dataBufferDefinitionPath = GV_DATA_BUFFER_PROPERTIES_NAME;

		try {
			inputBufferName = BufferHandler.readBufferFromFile(data, dataBufferDefinitionPath);
			environment.put(inputBufferName, data);
		} catch (Exception e1) {
			System.out.println("ERROR: unable to initialize 'data' gvbuffer");
			e1.printStackTrace();
		}

		boolean bufferFileExists = true;
		int count = 1;
		while(bufferFileExists) {
			try {
				String environmentBufferDefinitionPath = ENV_PROPERTIES_PATH_FIRST_PART + count + ENV_PROPERTIES_PATH_LAST_PART;
				GVBuffer envBuffer = new GVBuffer();
				String bufferName = BufferHandler.readBufferFromFile(envBuffer, environmentBufferDefinitionPath);
				if (bufferName != null && !bufferName.equals("")) {
					environment.put(bufferName,envBuffer);
				}
			} catch (Exception e) {
				bufferFileExists = false;
			}
			count++;
		}

	}

	private static void saveBuffers(HashMap<String,GVBuffer> environment) throws GVException, IOException {

		for(Entry<String,GVBuffer> temp: environment.entrySet()) {
			String tempName = temp.getKey();
			GVBuffer tempBuffer = temp.getValue();
			try {
				String path = GV_OUTPUT_DATA_BUFFER_PATH + tempName + "-Buffer.xml";
				BufferHandler.saveBufferOnFile(tempName,tempBuffer,path);
			} catch (Exception e) {
				System.out.println("ERROR: unable to save output Buffers.");
				e.printStackTrace();
			}
		}

	}

	private static boolean executeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws Exception {
		showBuffers(data, environment);
		logBuffers(data, environment);
		ScriptPerformer.Language lang;
		if(isJavaScript()) {
			lang = ScriptPerformer.Language.JavaScript;
		} else if(isGroovy()) {
			lang = ScriptPerformer.Language.Groovy;
		} else if(isPython()) {
			lang = ScriptPerformer.Language.python;
		} else {
			throw new IllegalArgumentException("Undefined script language!");
		}
		String function = "";
		if(IS_FUNCTION) {
			function = " function";
		}
		String message = "###############################################\n\n" + 
				"         SCRIPT EXECUTION (" + lang.name() + function + ")\n" + 
				"\n###############################################\n\n";
		System.out.print(message);
		Files.write(Paths.get(LOG_FILE_PATH), (message).getBytes(), StandardOpenOption.APPEND);
		ScriptPerformer scriptPerformer = null;
		boolean conditionReturn = false;
			
		scriptPerformer = new ScriptPerformer(lang);
		if(IS_FUNCTION) {
			conditionReturn = scriptPerformer.executeScript(data, environment);
		} else {
			scriptPerformer.executeScript(data, environment);
		}

		return conditionReturn;
	}

	private static boolean isJavaScript() {
		return LANGUAGE.toLowerCase().equals(ScriptPerformer.Language.JavaScript.name().toLowerCase());
	}

	private static boolean isGroovy() {
		return LANGUAGE.toLowerCase().equals(ScriptPerformer.Language.Groovy.name().toLowerCase());
	}
	
	private static boolean isPython() {
		return LANGUAGE.toLowerCase().equals(ScriptPerformer.Language.python.name().toLowerCase());
	}

	private static void showBuffers(GVBuffer data, HashMap<String, GVBuffer> environment) throws IOException {

		for(String bufferName: environment.keySet()) {
			Visualizer.printGVBuffer(environment.get(bufferName), bufferName);
		}

	}

	private static void logBuffers(GVBuffer data, HashMap<String, GVBuffer> environment) throws IOException {
		for(String bufferName: environment.keySet()) {
			Visualizer.writeInTheLog(environment.get(bufferName),bufferName);
		}
	}

	public static String getInputBufferName() {
		return inputBufferName;
	}

	public static void setInputBufferName(String inputBufferName) {
		GVScriptTester.inputBufferName = inputBufferName;
	}
	
	private static void initializeRoles() {
		IdentityInfo newIdentity = new DummyIdentityInfo(NAME, ROLES, ADDRESSES);
		newIdentity.getAttributes().put("id", ENTITY_ID);
		GVIdentityHelper.push(newIdentity);
	}

}
