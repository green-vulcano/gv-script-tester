package tester.execution;

import static tester.execution.configuration.Paths.*;
import static tester.settings.Constants.IS_FUNCTION;
import static tester.settings.Constants.IS_JAVASCRIPT;
import static tester.settings.Constants.SHOW_ALL_BUFFERS_IN_OUTPUT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.buffer.GVException;
import tester.execution.buffer.BufferHandler;
import tester.execution.engine.JavaScriptPerformer;
import tester.execution.mapping.Buffer;
import tester.execution.mapping.Name;
import tester.execution.mapping.Property;
import tester.execution.mapping.PropertyList;
import tester.execution.mapping.Value;
import tester.execution.output.Visualizer;
import tester.groovy.GroovyScript;

public class GVScriptTester{
	
	private static String inputBufferName = "input";

	public static void main(String[] args) throws Exception {
		GVBuffer data = new GVBuffer();
		HashMap<String,GVBuffer> environment = new HashMap<String,GVBuffer>();
		Files.write(Paths.get(LOG_FILE_PATH), "".getBytes());
		initializeTest(data, environment);
		boolean conditionReturn;
		try {
			conditionReturn = executeTest(data, environment);
			saveBuffers(environment);
			showBuffers(data, environment);
			if(IS_FUNCTION) {
				System.out.println();
				System.out.println("> Returned value = " + conditionReturn);
			}
		} catch (Exception e) {
			String error = "> SCRIPT EXECUTION ERROR!\n> Error: ";
			System.out.print(error);
			e.printStackTrace(); 
			Files.write(Paths.get(LOG_FILE_PATH), (error + e.getMessage()).getBytes(), StandardOpenOption.APPEND);
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
		String message = "###############################################\n\n" + 
				"         SCRIPT EXECUTION (" + lang + function + ")\n" + 
				"\n###############################################\n\n";
		System.out.print(message);
		Files.write(Paths.get(LOG_FILE_PATH), (message).getBytes(), StandardOpenOption.APPEND);
		GroovyScript gs = null;
		JavaScriptPerformer javascript= null;
		boolean conditionReturn = false;
		if(!IS_JAVASCRIPT) {
			gs = new GroovyScript();
			if(IS_FUNCTION) {
				conditionReturn = gs.executeGroovyCondition(data, environment);
			} else {
				gs.executeGroovyScript(data, environment);
			}
		} else {
			javascript = new JavaScriptPerformer();
			if(IS_FUNCTION) {
				conditionReturn = javascript.executeJavaScriptCondition(data, environment);
			} else {
				javascript.executeJavaScript(data, environment);
			}
		}

		return conditionReturn;
	}

	private static void showBuffers(GVBuffer data, HashMap<String, GVBuffer> environment) throws IOException {
		
		if(!SHOW_ALL_BUFFERS_IN_OUTPUT) {
			Visualizer.printGVBuffer(data, null);	
		} else {	
			for(String bufferName: environment.keySet()) {
				Visualizer.printGVBuffer(environment.get(bufferName), bufferName);
			}
		}
		
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

}
