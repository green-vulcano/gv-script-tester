package tester.execution;

import static tester.execution.Paths.*;
import static tester.settings.Constants.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

import org.json.JSONObject;

import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.buffer.GVException;
import tester.groovy.GroovyScript;

public class ExecuteScript{

	public static void main(String[] args) throws GVException, IOException {
		GVBuffer data = new GVBuffer();
		HashMap<String,GVBuffer> environment = new HashMap<String,GVBuffer>();
		Files.write(Paths.get(LOG_FILE_PATH), "".getBytes());
		initializeTest(data, environment);
		boolean conditionReturn;
		try {
			conditionReturn = executeTest(data, environment);
			showScriptResults(data, environment);
			if(IS_FUNCTION) {
				System.out.println();
				System.out.println("> Returned value = " + conditionReturn);
			}
		} catch (Exception e) {
			String error = "> SCRIPT EXECUTION ERROR! \n> Error: ";
			System.out.print(error);
			e.printStackTrace(); 
			Files.write(Paths.get(LOG_FILE_PATH), (error + e.getMessage()).getBytes(), StandardOpenOption.APPEND);
		}
	}

	////////////////////////////////////////////////

	private static void initializeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws GVException, IOException {

		String objectPath = GV_FILE_BUFFER_NAME;
		String propertyPath = GV_DATA_BUFFER_PROPERTIES_NAME;
		readBufferFromFile(data, objectPath, propertyPath);

		boolean bufferFileExists = true;
		int count = 1;
		while(bufferFileExists) {
			try {
				String envObjectPath = "scriptTester/ENV-" + count + "-GVBuffer.txt";
				String envPropertiesPath = "scriptTester/ENV-" + count + "-Properties.json";
				GVBuffer envBuffer = new GVBuffer();
				String bufferName = readBufferFromFile(envBuffer, envObjectPath, envPropertiesPath);
				if (bufferName != null && !bufferName.equals("")) {
					environment.put(bufferName,envBuffer);
				}
			} catch (IOException e) {
				bufferFileExists = false;
			}
			count++;
		}

	}

	private static String readBufferFromFile(GVBuffer gvbuffer, String objectPath, String propertyPath)
			throws GVException, IOException {

		gvbuffer.setObject(new String(Files.readAllBytes(Paths.get(objectPath))));
		String fileContent = new String(Files.readAllBytes(Paths.get(propertyPath)));
		JSONObject json = null;
		try {
			json = new JSONObject(fileContent);
			if(!json.has("bufferName")) {
				System.out.println("ERROR: missing mandatory field 'bufferName' in " + propertyPath);
				return null;
			}
			if(json.has("properties")) {
				for(Object obj : json.getJSONArray("properties")) {
					JSONObject propertyPair = (JSONObject) obj;
					gvbuffer.setProperty(propertyPair.getString("name"), propertyPair.getString("value"));
				}
			} else {
				System.out.println("ERROR: missing mandatory field 'properties' in " + propertyPath);
			}
		} catch (Exception e) {
			System.out.println("ERROR: unable to read gv-script-tester configuration file: " + propertyPath);
			e.printStackTrace();
		}

		return json.getString("bufferName");

	}

	private static void initializeTest_old(GVBuffer data, HashMap<String,GVBuffer> environment) throws GVException, IOException {

		////////////////////////////////

		data.setObject(new String(Files.readAllBytes(Paths.get(GV_FILE_BUFFER_NAME))));
		if(PROPERTY_NAME_1.length()!=0) data.setProperty(PROPERTY_NAME_1, PROPERTY_VALUE_1);
		if(PROPERTY_NAME_2.length()!=0) data.setProperty(PROPERTY_NAME_2, PROPERTY_VALUE_2);
		if(PROPERTY_NAME_3.length()!=0) data.setProperty(PROPERTY_NAME_3, PROPERTY_VALUE_3);
		if(PROPERTY_NAME_4.length()!=0) data.setProperty(PROPERTY_NAME_4, PROPERTY_VALUE_4);
		if(PROPERTY_NAME_5.length()!=0) data.setProperty(PROPERTY_NAME_5, PROPERTY_VALUE_5);
		if(PROPERTY_NAME_6.length()!=0) data.setProperty(PROPERTY_NAME_6, PROPERTY_VALUE_6);
		if(PROPERTY_NAME_7.length()!=0) data.setProperty(PROPERTY_NAME_7, PROPERTY_VALUE_7);
		if(PROPERTY_NAME_8.length()!=0) data.setProperty(PROPERTY_NAME_8, PROPERTY_VALUE_8);
		if(PROPERTY_NAME_9.length()!=0) data.setProperty(PROPERTY_NAME_9, PROPERTY_VALUE_9);
		if(PROPERTY_NAME_10.length()!=0) data.setProperty(PROPERTY_NAME_10, PROPERTY_VALUE_10);
		if(PROPERTY_NAME_11.length()!=0) data.setProperty(PROPERTY_NAME_11, PROPERTY_VALUE_11);
		if(PROPERTY_NAME_12.length()!=0) data.setProperty(PROPERTY_NAME_12, PROPERTY_VALUE_12);

		if(ENVIRONMENT_1_GVBUFFER_NAME!=null&&!ENVIRONMENT_1_GVBUFFER_NAME.equals("")) {
			GVBuffer prevData = new GVBuffer();
			prevData.setObject(new String(Files.readAllBytes(Paths.get(ENVIRONMENT_1_FILE_BUFFER_NAME))));
			if(ENV_PROPERTY_NAME_1.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_1, ENV_PROPERTY_VALUE_1);
			if(ENV_PROPERTY_NAME_2.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_2, ENV_PROPERTY_VALUE_2);
			if(ENV_PROPERTY_NAME_3.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_3, ENV_PROPERTY_VALUE_3);
			if(ENV_PROPERTY_NAME_4.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_4, ENV_PROPERTY_VALUE_4);
			if(ENV_PROPERTY_NAME_5.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_5, ENV_PROPERTY_VALUE_5);
			if(ENV_PROPERTY_NAME_6.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_6, ENV_PROPERTY_VALUE_6);
			if(ENV_PROPERTY_NAME_7.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_7, ENV_PROPERTY_VALUE_7);
			if(ENV_PROPERTY_NAME_8.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_8, ENV_PROPERTY_VALUE_8);
			environment.put(ENVIRONMENT_1_GVBUFFER_NAME,prevData);
		}

		if(ENVIRONMENT_2_GVBUFFER_NAME!=null&&!ENVIRONMENT_2_GVBUFFER_NAME.equals("")) {
			GVBuffer prevData2 = new GVBuffer();
			prevData2.setObject(new String(Files.readAllBytes(Paths.get(ENVIRONMENT_2_FILE_BUFFER_NAME))));
			if(ENV_2_PROPERTY_NAME_1.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_1, ENV_2_PROPERTY_VALUE_1);
			if(ENV_2_PROPERTY_NAME_2.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_2, ENV_2_PROPERTY_VALUE_2);
			if(ENV_2_PROPERTY_NAME_3.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_3, ENV_2_PROPERTY_VALUE_3);
			if(ENV_2_PROPERTY_NAME_4.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_4, ENV_2_PROPERTY_VALUE_4);
			if(ENV_2_PROPERTY_NAME_5.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_5, ENV_2_PROPERTY_VALUE_5);
			if(ENV_2_PROPERTY_NAME_6.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_6, ENV_2_PROPERTY_VALUE_6);
			if(ENV_2_PROPERTY_NAME_7.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_7, ENV_2_PROPERTY_VALUE_7);
			if(ENV_2_PROPERTY_NAME_8.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_8, ENV_2_PROPERTY_VALUE_8);
			environment.put(ENVIRONMENT_2_GVBUFFER_NAME,prevData2);
		}

		if(ENVIRONMENT_3_GVBUFFER_NAME!=null&&!ENVIRONMENT_3_GVBUFFER_NAME.equals("")) {
			GVBuffer prevData3 = new GVBuffer();
			prevData3.setObject(new String(Files.readAllBytes(Paths.get(ENVIRONMENT_3_FILE_BUFFER_NAME))));
			if(ENV_3_PROPERTY_NAME_1.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_1, ENV_3_PROPERTY_VALUE_1);
			if(ENV_3_PROPERTY_NAME_2.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_2, ENV_3_PROPERTY_VALUE_2);
			if(ENV_3_PROPERTY_NAME_3.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_3, ENV_3_PROPERTY_VALUE_3);
			if(ENV_3_PROPERTY_NAME_4.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_4, ENV_3_PROPERTY_VALUE_4);
			if(ENV_3_PROPERTY_NAME_5.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_5, ENV_3_PROPERTY_VALUE_5);
			if(ENV_3_PROPERTY_NAME_6.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_6, ENV_3_PROPERTY_VALUE_6);
			if(ENV_3_PROPERTY_NAME_7.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_7, ENV_3_PROPERTY_VALUE_7);
			if(ENV_3_PROPERTY_NAME_8.length()!=0) prevData3.setProperty(ENV_3_PROPERTY_NAME_8, ENV_3_PROPERTY_VALUE_8);
			environment.put(ENVIRONMENT_3_GVBUFFER_NAME,prevData3);
		}
	}

	private static boolean executeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws Exception {
		printGVBuffer(data);
		for(String bufferName: environment.keySet()) {
			writeInTheLog(environment.get(bufferName),bufferName);
		}
		String lang;
		if(IS_JAVASCRIPT) {
			lang = "Javascript";
		} else {
			lang = "Groovy";
		}
		String function = "";
		if(IS_FUNCTION) {
			function = " function";
		}
		String message = "         SCRIPT EXECUTION (" + lang + function + ") \n \n";
		System.out.print(message);
		Files.write(Paths.get(LOG_FILE_PATH), (message).getBytes(), StandardOpenOption.APPEND);
		GroovyScript gs = null;
		JavaScript javascript= null;
		boolean conditionReturn = false;
		if(!IS_JAVASCRIPT) {
			gs = new GroovyScript();
			if(IS_FUNCTION) {
				conditionReturn = gs.testCondition(data, environment);
			} else {
				gs.testScript(data, environment);
			}
		} else {
			javascript = new JavaScript();
			if(IS_FUNCTION) {
				conditionReturn = javascript.executeJavaScriptCondition(data, environment);
			} else {
				javascript.executeJavaScript(data, environment);
			}
		}

		return conditionReturn;
	}

	private static void showScriptResults(GVBuffer data, HashMap<String, GVBuffer> environment) throws IOException {
		printGVBuffer(data);	
		for(String bufferName: environment.keySet()) {
			writeInTheLog(environment.get(bufferName),bufferName);
		}
	}

	public static void printGVBuffer(GVBuffer gvbuffer) {
		String output = generateBufferInfo(gvbuffer, null);
		System.out.print(output);
	}

	public static void writeInTheLog(GVBuffer gvbuffer, String bufferName) throws IOException {
		String output = generateBufferInfo(gvbuffer, bufferName);
		Files.write(Paths.get(LOG_FILE_PATH), output.getBytes(), StandardOpenOption.APPEND);
	}

	public static String generateBufferInfo(GVBuffer gvbuffer, String bufferName) {
		String output = "";
		output += "---------------- GV BUFFER -------------------- \n";
		output += "\n";
		if(bufferName!=null) {
			output += "> GVBuffer name = " + bufferName + "\n";
			output += "\n";
		}
		String bufferObject = (String) gvbuffer.getObject();
		if(bufferObject!=null && bufferObject.contains("\n")) {
			output += "> GVBuffer (multiline view): \n" + bufferObject + "\n";
		} else {
			output += "> GVBuffer = " + bufferObject + "\n";
		}
		output += "\n";

		if(gvbuffer.getPropertyNames().length<1) {
			output += "> No Properties" + "\n";
		} else {
			output += "> Properties:" + "\n";
			for(String key:gvbuffer.getPropertyNames()) {
				String propertyValue = gvbuffer.getProperty(key);
				if(propertyValue!=null && propertyValue.contains("\n")) {
					output += "    > " + key + " (multiline view): \n" + propertyValue + "\n";
				} else {
					output += "    > " + key + " = " + propertyValue + "\n";
				}
			}
		}
		output += "-----------------------------------------------" + "\n \n";
		return output;
	}

}
