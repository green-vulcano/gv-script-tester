package tester.execution;

import java.util.HashMap;

import tester.groovy.GroovyScript;

public class ScriptSelector {

	public void testScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, GroovyScript gv) throws Exception {

		gv.testGroovyScript(data, environment);
	}

	public boolean testCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, GroovyScript gv) throws Exception {

		return gv.testGroovyCondition(data, environment);

	}
	
	public void testJavaScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, JavaScriptPerformer javascript) throws Exception {
		
		javascript.executeJavaScript(data, environment);
		
	}
	
	public void testJavaScriptCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment, JavaScriptPerformer javascript) throws Exception {
		
		javascript.executeJavaScriptCondition(data, environment);
		
	}
	

}
