package tester.execution;

import static tester.execution.Paths.*;
import static tester.settings.Constants.IS_FUNCTION;
import static tester.settings.Constants.IS_JAVASCRIPT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.buffer.GVException;
import tester.execution.mapping.Buffer;
import tester.execution.mapping.Property;
import tester.groovy.GroovyScript;

public class GVScriptTester{

	public static void main(String[] args) throws Exception {
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
			String error = "> SCRIPT EXECUTION ERROR!\n> Error: ";
			System.out.print(error);
			e.printStackTrace(); 
			Files.write(Paths.get(LOG_FILE_PATH), (error + e.getMessage()).getBytes(), StandardOpenOption.APPEND);
		}
	}

	////////////////////////////////////////////////

	private static void initializeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws GVException, IOException {

		String objectPath = GV_FILE_BUFFER_NAME;
		String propertyPath = GV_DATA_BUFFER_PROPERTIES_NAME;

		try {
			readBufferFromFile(data, objectPath, propertyPath);
		} catch (Exception e1) {
			System.out.println("ERROR: unable to initialize 'data' gvbuffer");
			e1.printStackTrace();
		}

		boolean bufferFileExists = true;
		int count = 1;
		while(bufferFileExists) {
			try {
				String envObjectPath = ENV_OBJECT_PATH_FIRST_PART + count + ENV_OBJECT_PATH_LAST_PART;
				String envPropertiesPath = ENV_PROPERTIES_PATH_FIRST_PART + count + ENV_PROPERTIES_PATH_LAST_PART;
				GVBuffer envBuffer = new GVBuffer();
				String bufferName = readBufferFromFile(envBuffer, envObjectPath, envPropertiesPath);
				if (bufferName != null && !bufferName.equals("")) {
					environment.put(bufferName,envBuffer);
				}
			} catch (Exception e) {
				bufferFileExists = false;
			}
			count++;
		}

	}

	private static String readBufferFromFile(GVBuffer gvbuffer, String objectPath, String propertyPath)
			throws Exception {

		gvbuffer.setObject(new String(Files.readAllBytes(Paths.get(objectPath))));

		File source = new File(propertyPath);
		JAXBContext jaxbContext = JAXBContext.newInstance(Buffer.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		System.setProperty("javax.xml.accessExternalDTD", "all"); 
		Buffer element = (Buffer) jaxbUnmarshaller.unmarshal(source);

		try {
			if(element.getName()==null) {
				System.out.println("ERROR: missing mandatory field 'bufferName' in " + propertyPath);
				return null;
			}
			for(Property propertyPair : element.getPropertyList().getProperty()) {
				gvbuffer.setProperty(propertyPair.getName().getvalue(), propertyPair.getValue().getvalue());
			}

		} catch (Exception e) {
			System.out.println("ERROR: unable to read gv-script-tester configuration file: " + propertyPath);
			e.printStackTrace();
		}

		return element.getName();

	}

	private static boolean executeTest(GVBuffer data, HashMap<String,GVBuffer> environment) throws Exception {
		Visualizer.printGVBuffer(data);
		for(String bufferName: environment.keySet()) {
			Visualizer.writeInTheLog(environment.get(bufferName),bufferName);
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
		String message = "         SCRIPT EXECUTION (" + lang + function + ")\n\n";
		System.out.print(message);
		Files.write(Paths.get(LOG_FILE_PATH), (message).getBytes(), StandardOpenOption.APPEND);
		GroovyScript gs = null;
		JavaScriptPerformer javascript= null;
		boolean conditionReturn = false;
		if(!IS_JAVASCRIPT) {
			gs = new GroovyScript();
			if(IS_FUNCTION) {
				conditionReturn = gs.testGroovyCondition(data, environment);
			} else {
				gs.testGroovyScript(data, environment);
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

	private static void showScriptResults(GVBuffer data, HashMap<String, GVBuffer> environment) throws IOException {
		Visualizer.printGVBuffer(data);	
		for(String bufferName: environment.keySet()) {
			Visualizer.writeInTheLog(environment.get(bufferName),bufferName);
		}
		
	}

}
