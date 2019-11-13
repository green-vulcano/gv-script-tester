package tester.execution.output;

import static tester.execution.configuration.Paths.LOG_FILE_PATH;
import static tester.settings.Constants.IMPROVE_JSON_VISUALIZATION;
import static tester.settings.Constants.IMPROVE_XML_VISUALIZATION;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import it.greenvulcano.gvesb.buffer.GVBuffer;

public class Visualizer {

	public static void printGVBuffer(GVBuffer gvbuffer, String bufferName) {
		String output = generateBufferInfo(gvbuffer, bufferName);
		System.out.print(output);
	}

	public static void writeInTheLog(GVBuffer gvbuffer, String bufferName) throws IOException {
		String output = generateBufferInfo(gvbuffer, bufferName);
		Files.write(Paths.get(LOG_FILE_PATH), output.getBytes(), StandardOpenOption.APPEND);
	}

	private static String generateBufferInfo(GVBuffer gvbuffer, String bufferName) {
		String output = "";
		output += "---------------- GV BUFFER --------------------\n";
		output += "\n";
		if(bufferName!=null) {
			output += "> Name = " + bufferName + "\n";
			output += "\n";
		}
		String bufferObject = (String) gvbuffer.getObject();
		output = printElement(output, bufferObject, "Object");
		output += "\n";

		if(gvbuffer.getPropertyNames().length<1) {
			output += "> No Properties" + "\n";
		} else {
			output += "> Properties:" + "\n";
			for(String key:gvbuffer.getPropertyNames()) {
				String propertyValue = gvbuffer.getProperty(key);
				output += "    ";
				output = printElement(output, propertyValue, key);
			}
		}
		output += "-----------------------------------------------" + "\n\n";
		return output;
	}

	private static String printElement(String output, String element, String elementName) {
		if(IMPROVE_JSON_VISUALIZATION) {
			element = formatJson(element);
		}
		if(IMPROVE_XML_VISUALIZATION) {
			element = toPrettyXML(element, 2);
		}
		if(element!=null && element.contains("\n")) {
			output += "> " + elementName + " (multiline view):\n" + element + "\n";
		} else {
			String info = null;
			if(element==null) {
				info = "is null";
			} else if (element.equals("")){
				info = "is empty";
			}
			if(info==null) {
				output += "> " + elementName + " = " + element + "\n";
			} else {
				output += "> " + elementName + " " + info + "\n";
			}	
		}
		return output;
	}

	private static String formatJson(String s) {
		if(s!=null && s!="" && s.contains("{") && s.contains("}")) {
			try {
				JSONObject jsonObject = new JSONObject(s);
				return (jsonObject.toString(4));
			} catch (Exception e) {
				// is not a json :(
			}
		}
		return s;
	}
	
	public static String toPrettyXML(String xml, int indent) {
	    try {
	        // Turn xml string into a document
	        Document document = DocumentBuilderFactory.newInstance()
	                .newDocumentBuilder()
	                .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

	        // Remove whitespaces outside tags
	        document.normalize();
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
	                                                      document,
	                                                      XPathConstants.NODESET);

	        for (int i = 0; i < nodeList.getLength(); ++i) {
	            Node node = nodeList.item(i);
	            node.getParentNode().removeChild(node);
	        }

	        // Setup pretty print options
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        // Return pretty print xml string
	        StringWriter stringWriter = new StringWriter();
	        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
	        return stringWriter.toString();
	    } catch (Exception e) {
	    	// is not an XML :(
	    }
	    return xml;
	}
}
