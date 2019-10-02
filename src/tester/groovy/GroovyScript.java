package tester.groovy;

import java.util.HashMap;



public class GroovyScript {

	public void testScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		///////////////////////////////  SCRIPT AREA  ///////////////////////////////

		// example 2 - Groovy script
		org.json.JSONObject inputJson = new org.json.JSONObject(data.getObject());
		String city = inputJson.getString("city").toUpperCase();
	    data.setProperty("CITY", city);

		/////////////////////////////////////////////////////////////////////////////

	}

	public boolean testCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		/////////////////////////// CONDITION SCRIPT AREA  ///////////////////////////

		// example 3 - Groovy script with boolean return
		if(environment.get("test-buffer").getProperty("example").equals("hello!")) {
			return true;
		} else {
			return false;
		}		

		/////////////////////////////////////////////////////////////////////////////

	}
	
	

}
