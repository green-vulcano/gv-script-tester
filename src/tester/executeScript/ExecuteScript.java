package tester.executeScript;

import static tester.settings.Constants.*;

import java.util.HashMap;

import LukePack.LP;
import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.buffer.GVException;
import tester.script.GroovyScript;
import tester.script.JavaScript;

public class ExecuteScript{

	public static void main(String[] args) throws GVException {
		GVBuffer data = new GVBuffer();
		HashMap<String,GVBuffer> environment = new HashMap<String,GVBuffer>();
		LP.writeNewFile(LOG_FILE_PATH, "");
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
			System.out.println("> SCRIPT EXECUTION ERROR!");
			System.out.print("> Error: ");
			e.printStackTrace();
			LP.addToFileln(LOG_FILE_PATH, "> SCRIPT EXECUTION ERROR! \n > Error: " + e.getMessage()); 
		}
	}

	////////////////////////////////////////////////

	private static void initializeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws GVException {
		data.setObject(LP.readFile(GV_FILE_BUFFER_NAME));

		if(PROPERTY_NAME_1.length()!=0) data.setProperty(PROPERTY_NAME_1, PROPERTY_VALUE_1);
		if(PROPERTY_NAME_2.length()!=0) data.setProperty(PROPERTY_NAME_2, PROPERTY_VALUE_2);
		if(PROPERTY_NAME_3.length()!=0) data.setProperty(PROPERTY_NAME_3, PROPERTY_VALUE_3);
		if(PROPERTY_NAME_4.length()!=0) data.setProperty(PROPERTY_NAME_4, PROPERTY_VALUE_4);
		if(PROPERTY_NAME_5.length()!=0) data.setProperty(PROPERTY_NAME_5, PROPERTY_VALUE_5);

		GVBuffer prevData = new GVBuffer();
		prevData.setObject(LP.readFile(ENVIRONMENT_FILE_BUFFER_NAME));
		if(ENV_PROPERTY_NAME_1.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_1, ENV_PROPERTY_VALUE_1);
		if(ENV_PROPERTY_NAME_2.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_2, ENV_PROPERTY_VALUE_2);
		if(ENV_PROPERTY_NAME_3.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_3, ENV_PROPERTY_VALUE_3);
		if(ENV_PROPERTY_NAME_4.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_4, ENV_PROPERTY_VALUE_4);
		if(ENV_PROPERTY_NAME_5.length()!=0) prevData.setProperty(ENV_PROPERTY_NAME_5, ENV_PROPERTY_VALUE_5);
		environment.put(ENVIRONMENT_GVBUFFER_NAME,prevData);

		GVBuffer prevData2 = new GVBuffer();
		prevData2.setObject(LP.readFile(ENVIRONMENT_2_FILE_BUFFER_NAME));
		if(ENV_2_PROPERTY_NAME_1.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_1, ENV_2_PROPERTY_VALUE_1);
		if(ENV_2_PROPERTY_NAME_2.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_2, ENV_2_PROPERTY_VALUE_2);
		if(ENV_2_PROPERTY_NAME_3.length()!=0) prevData2.setProperty(ENV_2_PROPERTY_NAME_3, ENV_2_PROPERTY_VALUE_3);
		environment.put(ENVIRONMENT_2_GVBUFFER_NAME,prevData2);
	}

	private static boolean executeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws Exception {
		printGVBuffer(data);
		for(String bufferName: environment.keySet()) {
			writeInTheLog(environment.get(bufferName),bufferName);
		}
		System.out.println("              SCRIPT EXECUTION");
		LP.addToFileln(LOG_FILE_PATH, ">>>>>>>>>>>> SCRIPT EXECUTION <<<<<<<<<<<< \n");
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

	private static void showScriptResults(GVBuffer data, HashMap<String, GVBuffer> environment) {
		if(ENABLE_ROLE_VIEW) {
			System.out.println("> Role = " + CURRENT_ROLE);
		}
		if(ENABLE_IDENTITY_KEY_VIEW) {
			System.out.println("> "+ IDENTITY_KEY + " = " + IDENTITY_VALUE);
		}
		System.out.println();
		printGVBuffer(data);	
		for(String bufferName: environment.keySet()) {
			writeInTheLog(environment.get(bufferName),bufferName);
		}
	}

	public static void printGVBuffer(GVBuffer gvbuffer) {
		System.out.println("---------------- GV BUFFER --------------------");
		System.out.println();
		System.out.println("> GVBuffer = " + gvbuffer.getObject());
		System.out.println();
		System.out.println("> PropertyMap:");
		for(String key:gvbuffer.getPropertyNames()) {
			System.out.println("        > " + key + " = " + gvbuffer.getProperty(key));
		}
		System.out.println("-----------------------------------------------");
	}

	public static void writeInTheLog(GVBuffer gvbuffer, String bufferName) {
		String output = "";
		output += "---------------- GV BUFFER -------------------- \n";
		output += "\n";
		output += "> GVBuffer name = " + bufferName + "\n";
		output += "\n";
		output += "> GVBuffer = " + gvbuffer.getObject() + "\n";
		output += "\n";
		output += "> PropertyMap:" + "\n";
		for(String key:gvbuffer.getPropertyNames()) {
			output += "        > " + key + " = " + gvbuffer.getProperty(key) + "\n";
		}
		output += "-----------------------------------------------" + "\n";
		LP.addToFileln(LOG_FILE_PATH, output);
	}

}
