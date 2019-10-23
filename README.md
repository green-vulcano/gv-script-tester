## GREEN VULCANO SCRIPT TESTER

### Overview

**GV Script Tester** allows you to quickly run and debug a *Javascript/Groovy* script using the [***gv-engine***](https://github.com/green-vulcano/gv-engine) java classes by setting up gvbuffers with the related properties (including many execution options), running the script showing its output and all changes in the related gvbuffers.
You can also set up multiple gvbuffers using the environment system. 

### Installation

The installation is very simple, just clone ***gv-script-tester***, open the project with your favourite IDE and than import [***gv-base***](https://github.com/green-vulcano/gv-engine) project in the *gv-script-tester* build path configuration.

### How to use

To run a ***Javascript script***, write into *JavaScript.js* the script that you want to execute, set *IS_JAVASCRIPT = true* in *Constants.java*, than run GVScriptTester.java as a java application. (Try to run example 1: Javascript)

```
GVScriptTester          ->  tester/execution/ [GVScriptTester.java]
JavaScript body         ->  scriptTester/ [JavaScript.js]
Javascript abilitation  ->  src/tester/settings/ [Constants.java]
```

To run a ***Groovy script***, insert the script into the appropriate area in *GroovyScript.java*, set *IS_JAVASCRIPT = false*, than run GVScriptTester.java as a java application. (Try to run example 2 - Groovy script)

```
GVScriptTester          ->  tester/execution/ [GVScriptTester.java]
Groovy Script body      ->  src/tester/groovy/ [GroovyScript.java]
Groovy abilitation      ->  src/tester/settings/ [Constants.java]
```

    Note: If your script is a function with a boolean return, set IS_CONDITION = true 
    (Try to run example 3 - Groovy script with boolean return)

To edit current (input) ***GVBuffer*** object or its properties, you have to edit the file *DATA-Buffer.xml*.
```
Current buffer content      ->  scriptTester/ [DATA-Buffer.xml]
```

To add an ***additional buffer***, you have to add an additional *ENV-[buffer number]-Buffer.xml* file for the buffer object and the related properties. The changes of additional GVBuffers during the execution are showed in the file *environmentLog.txt*. You can also change the property *SHOW_ALL_BUFFER_IN_OUTPUT* to always show all the buffers.

```
Current buffer object         ->  scriptTester/ [DATA-Buffer.xml]
Additional buffer object      ->  scriptTester/ [ENV-1-Buffer.xml]
Additional buffer object      ->  scriptTester/ [ENV-2-Buffer.xml]
           . . .                             . . .
Environment buffers log       ->  scriptTester/ [environmentLog.txt]
SHOW_ALL_BUFFER_IN_OUTPUT     ->  src/tester/settings/ [Constants.java]
```

To edit execution ***GV-SriptTester*** settings, edit the file *Constants.java*.

```
GV-SriptTester settings     ->  src/tester/settings/ [Constants.java]
```

### Examples
#### Example 1: Javascript
***Script*** -> *scriptTester/JavaScript.js*:
```
// example 1: Javascript

var input = JSON.parse(new java.lang.String(data.getObject()));

inputPlate = input.plate
inputColor = input.color

if(inputPlate.equals(data.getProperty("PRIVATE_CAR_PLATE"))){
	name = data.getProperty("FIRST_NAME")
	surname = data.getProperty("LAST_NAME")
	var output = name + " " + surname + "'s car is " + inputColor + "!"
	data.setObject(output)
} else {
	data.setObject("Car not found!")
}
```
***Data buffer*** -> *scriptTester/DATA-Buffer.xml*:
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE note SYSTEM "buffer.dtd">
<buffer name="data">
	<object>
		<value><![CDATA[{
"plate":"AD123CD",
"color":"red"
}]]></value>
	</object>
	<propertyList>
		<property>
			<name>FIRST_NAME</name>
			<value><![CDATA[Gino]]></value>
		</property>
		<property>
			<name>LAST_NAME</name>
			<value><![CDATA[Ginotti]]></value>
		</property>
		<property>
			<name>PRIVATE_CAR_PLATE</name>
			<value><![CDATA[AD123CD]]></value>
		</property>
	</propertyList>
</buffer>
```
***Execution output:***
```
---------------- GV BUFFER --------------------

> Object (multiline view):
{
"plate":"AD123CD",
"color":"red"
}

> Properties:
    > FIRST_NAME = Gino
    > LAST_NAME = Ginotti
    > PRIVATE_CAR_PLATE = AD123CD
-----------------------------------------------

         SCRIPT EXECUTION (Javascript)

---------------- GV BUFFER --------------------

> Object = Gino Ginotti's car is red!

> Properties:
    > FIRST_NAME = Gino
    > LAST_NAME = Ginotti
    > PRIVATE_CAR_PLATE = AD123CD
-----------------------------------------------
```
#### Example 2 and 3: Groovy
Have fun making these two examples work!

***Scripts*** -> *src/tester/groovy/GroovyScript.java*:
```
public void executeGroovyScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

	///////////////////////////////  SCRIPT AREA  ///////////////////////////////

	// example 2 - Groovy script
	org.json.JSONObject inputJson = new org.json.JSONObject(data.getObject());
	String city = inputJson.getString("city").toUpperCase();
    	data.setProperty("CITY", city);

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
```
