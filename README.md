## GREEN VULCANO SCRIPT TESTER

### Overview

**GV Script Tester** allows you to quickly run and debug a *Javascript/Groovy* script using the [***gv-engine***](https://github.com/green-vulcano/gv-engine) java classes by setting up gvbuffers with the related properties (including many execution options), running the script showing its output and all changes in the related gvbuffers.
You can also set up multiple gvbuffers using the environment system. 

### Installation

The installation is very simple, just clone ***gv-script-tester***, open the project with your favourite IDE and than import [***gv-base***](https://github.com/green-vulcano/gv-engine) project in the *gv-script-tester* build path configuration.

### How to use

To run a ***Javascript script***, write into *JavaScript.js* the script that you want to execute and set *IS_JAVASCRIPT = true* in *Constants.java*. (Try to run example 1: Javascript)

```
JavaScript body         ->  scriptTester/ [JavaScript.js]
Javascript abilitation  ->  src/tester/settings/ [Constants.java]
```

To run a ***Groovy script***, insert the script into the appropriate area in *GroovyScript.java* and set *IS_JAVASCRIPT = false*. (Try to run example 2 - Groovy script)

```
Groovy Script body      ->  src/tester/groovy/ [GroovyScript.java]
Groovy abilitation      ->  src/tester/settings/ [Constants.java]
```

    Note: If your script is a function with a boolean return, set IS_CONDITION = true 
    (Try to run example 3 - Groovy script with boolean return)

To edit current (input) ***GVBuffer*** object, you have to edit the file *DATA-GVBuffer.txt*. If you need to add/modify properties of the current GVBuffer, you have to edit the file *DATA-Properties.xml*.

```
Current buffer content      ->  scriptTester/ [DATA-GVBuffer.txt]
Current buffer properties   ->  scriptTester/ [DATA-Properties.xml]
```

To add an ***additional buffer***, you have to add an additional *ENV-[buffer number]-GVBuffer.txt* and *ENV-[buffer number]-Properties.xml* file for the buffer object and the related properties. The changes of additional GVBuffers during the execution are showed in the file *environmentLog.txt*.

```
Current buffer object         ->  scriptTester/ [DATA-GVBuffer.txt]
Current buffer properties     ->  scriptTester/ [DATA-Properties.xml]
Additional buffer object      ->  scriptTester/ [ENV-1-GVBuffer.txt]
Additional buffer properties  ->  scriptTester/ [ENV-1-Properties.xml]
Additional buffer object      ->  scriptTester/ [ENV-2-GVBuffer.txt]
Additional buffer properties  ->  scriptTester/ [ENV-2-Properties.xml]
           . . .                             . . .
Environment buffers log       ->  scriptTester/ [environmentLog.txt]
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
***Data buffer object*** -> *scriptTester/DATA-GVBuffer.txt*:
```
{
"plate":"AD123CD",
"color":"red"
}
```
***Data buffer properties*** -> *scriptTester/DATA-Properties.xml*:
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE note SYSTEM "properties.dtd">
<buffer name="data">
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

***Scripts:***
```
public void testGroovyScript(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

	///////////////////////////////  SCRIPT AREA  ///////////////////////////////

	// example 2 - Groovy script
	org.json.JSONObject inputJson = new org.json.JSONObject(data.getObject());
	String city = inputJson.getString("city").toUpperCase();
    	data.setProperty("CITY", city);

	/////////////////////////////////////////////////////////////////////////////

}

public boolean testGroovyCondition(it.greenvulcano.gvesb.buffer.GVBuffer data, HashMap<String, it.greenvulcano.gvesb.buffer.GVBuffer> environment) throws Exception {

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
