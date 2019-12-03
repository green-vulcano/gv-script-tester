package tester.groovy;

import java.util.HashMap;



public class GroovyScript {

	public void executeGroovyScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

		///////////////////////////////  SCRIPT AREA  ///////////////////////////////

		// example 2 - Groovy script

		String inputJsonString = data.getObject().toString();
		
		org.json.JSONObject inputJson = new org.json.JSONObject(inputJsonString);
		
		int number = inputJson.getJSONObject("player").getInt("number");
		String name = inputJson.getJSONObject("player").getString("name");
		String surname = inputJson.getJSONObject("player").getString("surname");
		
		String code = (name + surname + number).toUpperCase();
	
		org.json.JSONObject outputJson = new org.json.JSONObject();
		outputJson.put("number", number);
		outputJson.put("code", code);
		
		data.setProperty("OUTPUT_JSON", outputJson.toString());		         

		/////////////////////////////////////////////////////////////////////////////

	}

	public boolean executeGroovyCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

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
