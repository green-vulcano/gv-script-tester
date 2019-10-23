package tester.execution;

import static tester.execution.configuration.Paths.*;
import static tester.settings.Constants.IS_FUNCTION;
import static tester.settings.Constants.IS_JAVASCRIPT;
import static tester.settings.Constants.SHOW_ALL_BUFFERS_IN_OUTPUT;

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
import tester.execution.engine.JavaScriptPerformer;
import tester.execution.mapping.Buffer;
import tester.execution.mapping.Property;
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
			inputBufferName = readBufferFromFile(data, dataBufferDefinitionPath);
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
				String bufferName = readBufferFromFile(envBuffer, environmentBufferDefinitionPath);
				if (bufferName != null && !bufferName.equals("")) {
					environment.put(bufferName,envBuffer);
				}
			} catch (Exception e) {
				bufferFileExists = false;
			}
			count++;
		}

	}

	private static String readBufferFromFile(GVBuffer gvbuffer, String propertyPath)
			throws Exception {

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
			gvbuffer.setObject(element.getObject().getValue().getvalue());
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
			Visualizer.printGVBuffer(data, getInputBufferName());	
			for(String bufferName: environment.keySet()) {
				Visualizer.printGVBuffer(environment.get(bufferName), bufferName);
			}
		}
		
		Visualizer.writeInTheLog(data, getInputBufferName());	
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
