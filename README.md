## GREEN VULCANO SCRIPT TESTER

### Overview

**GV Script Tester** allows you to quickly run and debug a *Javascript/Groovy* script using the [***gv-engine***](https://github.com/green-vulcano/gv-engine) java classes by setting up gvbuffers with the related properties (including many execution options), running the script showing its output and all changes in the related gvbuffers.
You can also set up multiple gvbuffers using the environment system. 

### Installation

The installation is very simple, just clone ***gv-script-tester***, open the project with your favourite IDE and than import [***gv-engine***](https://github.com/green-vulcano/gv-engine) project in the *gv-script-tester* build path configuration.

### How to use

To run a ***Javascript script***, edit the file *JavaScript.js* located in *scriptTester* folder and set *IS_JAVASCRIPT* constant as *true* in *Constants.java* located in *tester.settings* package. (Try to run example 1: Javascript)

```
JavaScript body         ->  /scriptTester/ [JavaScript.js]
Javascript abilitation  ->  tester.settings [Constants.java]
```

To run a ***Groovy script***, edit the script area in the file *GroovyScript.java* located in *tester.script* package and set *IS_JAVASCRIPT* constant as *false*. (Try to run example 2 - Groovy script)

```
Groovy Script body      ->  tester.script [GroovyScript.java]
Groovy abilitation      ->  tester.settings [Constants.java]
```

    Note: If your script is a function with a boolean return, set ***IS_CONDITION*** constants as **true**. 
    (Try to run example 3 - Groovy script with boolean return)

To edit current (input) ***GVBuffer*** object, you have to edit the file *DATA-GVBuffer.txt* located in *scriptTester* folder. If you need to add/modify properties of the current GVBuffer, you have to edit the file *Constants.java* in *tester.settings* package.

```
Current buffer content      ->  /scriptTester/ [DATA-GVBuffer.txt]
Current buffer properties   ->  tester.settings [Constants.java]
```

To add an ***additional buffer***, use *ENV-X-GVBuffer.txt* file (located in *scriptTester* folder) and setup buffer name and its properties in the Constants file (tester.settings [Constants.java] ). You can use up to 3 additional GVBuffers. The changes of additional GVBuffers are showed in the file *environmentLog.txt* located in *scriptTester* folder.

```
Current buffer              ->  /scriptTester/ [DATA-GVBuffer.txt]
Additional buffer 1 object  ->  /scriptTester/ [ENV-1-GVBuffer.txt]
Additional buffer 2 object  ->  /scriptTester/ [ENV-2-GVBuffer.txt]
Additional buffer 3 object  ->  /scriptTester/ [ENV-3-GVBuffer.txt]
Environment buffers log     ->  /scriptTester/ [environmentLog.txt]
Buffers properties          ->  tester.settings [Constants.java]
```

To edit miscellaneous ***GV-SriptTester options*** and set up ***properties*** or ***buffers settings***, edit the file *Constants.java* in *tester.settings* package.

```
GV-SriptTester settings     ->  tester.settings [Constants.java] 
```

### Examples
#### Example 1: Javascript
Script:
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
Execution output:
```
---------------- GV BUFFER -------------------- 

> GVBuffer (multiline view): 
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

> GVBuffer = Gino Ginotti's car is red!

> Properties:
    > FIRST_NAME = Gino
    > LAST_NAME = Ginotti
    > PRIVATE_CAR_PLATE = AD123CD
-----------------------------------------------
```
#### Example 2 and 3: Groovy
Have fun making these two examples work!

Scripts:
```
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
```
