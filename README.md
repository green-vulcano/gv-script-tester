## GREEN VULCANO SCRIPT TESTER

### Overview

**GV Script Tester** allows you to quickly run and debug a *Javascript/Groovy* script using the [***gv-engine***](https://github.com/green-vulcano/gv-engine) java classes, by setting up gvbuffers with the related properties (including many execution options), running the script by showing its output and all the changes in the involved gvbuffers.

![alt text](https://raw.githubusercontent.com/Luke460/gv-script-tester/master/gv-script-tester-eclipse.png)

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

To add an ***additional buffer***, you have to add an additional *ENV-[buffer number]-Buffer.xml* file for the buffer object and the related properties. To hide environment buffers on the output you can set *SHOW_ALL_BUFFERS_IN_OUTPUT = false*. 
After any execution, buffers are saved in xml format in the *output* folder.

TIPS: you can exclude a buffer setting its name as a void string (buffer name="").

```
Current buffer object         ->  scriptTester/ [DATA-Buffer.xml]
Additional buffer object      ->  scriptTester/ [ENV-1-Buffer.xml]
Additional buffer object      ->  scriptTester/ [ENV-2-Buffer.xml]
           . . .                             . . .
Environment buffers log       ->  scriptTester/ [environmentLog.txt]
Buffers after execution (xml) ->  output/ [BufferName.xml]
SHOW_ALL_BUFFERS_IN_OUTPUT    ->  src/tester/settings/ [Constants.java]
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

if(input.plate.equals(data.getProperty("PRIVATE_CAR_PLATE"))){
	
	name = data.getProperty("FIRST_NAME");
	surname = data.getProperty("LAST_NAME");
	var description = name + " " + surname + "'s car is " + input.color + ".";
	
	data.setObject(description);
	
	var cost = parseInt(input.cost);
	var tax = parseInt(environment.get("test").getProperty("FLAT_TAX"));
	var total = cost + tax;
	
	data.setProperty("TOTAL", total);
	
} else {
	
	data.setObject("Car not found!");
	data.setProperty("TOTAL", "0");
	
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
"color":"red",
"cost":"100"
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
***Test buffer*** *(just another buffer)* -> *scriptTester/ENV-1-Buffer.xml*:
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE note SYSTEM "buffer.dtd">
<buffer name="test">
	<object>
	</object>
	<propertyList>
		<property>
			<name>FLAT_TAX</name>
			<value><![CDATA[10]]></value>
		</property>
	</propertyList>
</buffer>
```
***Execution output:***
```
---------------- GV BUFFER --------------------

> Name = data

> Object (multiline view):
{
    "cost": "100",
    "color": "red",
    "plate": "AD123CD"
}

> Properties:
    > FIRST_NAME = Gino
    > LAST_NAME = Ginotti
    > PRIVATE_CAR_PLATE = AD123CD
-----------------------------------------------

---------------- GV BUFFER --------------------

> Name = test

> Object is null

> Properties:
    > FLAT_TAX = 10
-----------------------------------------------

###############################################

         SCRIPT EXECUTION (Javascript)

###############################################

---------------- GV BUFFER --------------------

> Name = data

> Object = Gino Ginotti's car is red.

> Properties:
    > FIRST_NAME = Gino
    > LAST_NAME = Ginotti
    > PRIVATE_CAR_PLATE = AD123CD
    > TOTAL = 110
-----------------------------------------------

---------------- GV BUFFER --------------------

> Name = test

> Object is null

> Properties:
    > FLAT_TAX = 10
-----------------------------------------------
```

#### Example 2: Groovy

***TIPS***: remember to set *IS_JAVASCRIPT = false*

***Scripts*** -> *src/tester/groovy/GroovyScript.java*:
```
public void executeGroovyScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

	///////////////////////////////  SCRIPT AREA  ///////////////////////////////

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
```
***Data buffer*** -> *scriptTester/DATA-Buffer.xml*:
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE note SYSTEM "buffer.dtd">
<buffer name="data">
	<object>
		<value><![CDATA[{
	"player": {
		"name": "Pino",
		"surname": "Pinotti",
		"number": 10
	}
}]]></value>
	</object>
	<propertyList></propertyList>
</buffer>
```
***Execution output:***
```
---------------- GV BUFFER --------------------

> Name = data

> Object (multiline view):
{"player": {
    "number": 10,
    "surname": "Pinotti",
    "name": "Pino"
}}

> No Properties
-----------------------------------------------

###############################################

         SCRIPT EXECUTION (Groovy)

###############################################

---------------- GV BUFFER --------------------

> Name = data

> Object (multiline view):
{"player": {
    "number": 10,
    "surname": "Pinotti",
    "name": "Pino"
}}

> Properties:
    > OUTPUT_JSON (multiline view):
{
    "number": 10,
    "code": "PINOPINOTTI10"
}
-----------------------------------------------
```
#### Example 3: Groovy (with return value)

Have fun making this simple example work!

***TIPS***: remember to set *IS_FUNCTION = true*

```
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
