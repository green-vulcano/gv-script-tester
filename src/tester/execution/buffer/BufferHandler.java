package tester.execution.buffer;

import static tester.execution.configuration.Paths.GV_OUTPUT_DATA_BUFFER_PATH;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.greenvulcano.gvesb.buffer.GVBuffer;
import it.greenvulcano.gvesb.buffer.GVException;
import tester.execution.mapping.Buffer;
import tester.execution.mapping.Name;
import tester.execution.mapping.Property;
import tester.execution.mapping.PropertyList;
import tester.execution.mapping.Value;

public class BufferHandler {

	public static String readBufferFromFile(GVBuffer gvbuffer, String propertyPath)
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
			if(element.getObject()!=null && element.getObject().getValue()!=null)
				gvbuffer.setObject(element.getObject().getValue().getvalue());
			for(Property propertyPair : element.getPropertyList().getProperty()) {
				String propertyName = propertyPair.getName().getvalue();
				if(propertyName!=null && !propertyName.equals("")) {
					if(propertyPair.getValue()!=null) {
						gvbuffer.setProperty(propertyName, propertyPair.getValue().getvalue());
					} else {
						throw new it.greenvulcano.gvesb.buffer.GVException(
								"GV Property cannot be null: buffer = " + element.getName() + 
								", property name = " + propertyName +
								", value = null\n");
					}
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR: unable to read gv-script-tester configuration file: " + propertyPath);
			e.printStackTrace();
		}

		return element.getName();

	}

	public static void saveBufferOnFile(String name, GVBuffer gvbuffer, String propertyPath)
			throws Exception {

		JAXBContext jaxbContext = JAXBContext.newInstance(Buffer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		System.setProperty("javax.xml.accessExternalDTD", "all"); 
	    jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", 
	    		  "\n<!DOCTYPE note SYSTEM \"buffer.dtd\">");
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		OutputStream os = new FileOutputStream(propertyPath);
		
		Buffer element = new Buffer();
		element.setName(name);
		tester.execution.mapping.Object obj = new tester.execution.mapping.Object();
		Value value = new Value();
		//value.setvalue("<![CDATA[" + (String)gvbuffer.getObject() + "]]>");
		value.setvalue((String)gvbuffer.getObject());
		obj.setValue(value);
		element.setObject(obj);
		
		PropertyList propertyList = new PropertyList();	
		ArrayList<Property> propertyArray = new ArrayList<>();
		propertyList.setProperty(propertyArray);
		element.setPropertyList(propertyList);
		
		for(String key : gvbuffer.getPropertyNamesSet()) {
			String property = gvbuffer.getProperty(key);
			Property gvproperty = new Property();
			Name pName = new Name();
			pName.setvalue(key);
			gvproperty.setName(pName);
			
			Value pValue = new Value();
			//pValue.setvalue("<![CDATA[" + property + "]]>");
			pValue.setvalue(property);
			gvproperty.setValue(pValue);			
			
			element.getPropertyList().getProperty().add(gvproperty);
			
		}
		jaxbMarshaller.marshal(element, os);
	}
	
}
