package tester.execution;

import java.util.HashMap;

import tester.groovy.GroovyScript;

public class Script {

	public void testScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, GroovyScript gv) throws Exception {

		gv.testScript(data, environment);
	}

	public boolean testCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, GroovyScript gv) throws Exception {

		return gv.testCondition(data, environment);

	}
	
	public void testJavaScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, JavaScript javascript) throws Exception {
		
		javascript.executeJavaScript(data, environment);
		
	}
	
	public void testJavaScriptCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, JavaScript javascript) throws Exception {
		
		javascript.executeJavaScriptCondition(data, environment);
		
	}
	

}
