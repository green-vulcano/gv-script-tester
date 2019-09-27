## GREEN VULCANO SCRIPT TESTER

### Overview

**GV Script Tester** allows you to quickly run and debug a *Javascript/Groovy* script by setting up gvbuffers with the related properties (including many execution options), running the script using the **gv-engine** java classes and showing all changes in the related gvbuffers on the Eclipse console.
You can also set up multiple gvbuffers using the environment system. The changes of additional GVBuffers are showed in the file "environmentLog.txt" located in "scriptTester" folder.

### Installation

The installation is very simple, just clone ***gv-script-tester***, open the project with your favourite IDE and than import [***gv-engine***](https://github.com/green-vulcano/gv-engine) project in the *gv-script-tester* build path configuration.

### How to use

To run a Javascript script, edit the file "JavaScript.js" located in "scriptTester" folder and set ***IS_JAVASCRIPT*** constant as **true**. (Try to run example 1: Javascript)

```
JavaScript body         ->  /scriptTester/ [JavaScript.js]
Javascript abilitation  ->  tester.settings [Constants.java]
```

To run a Groovy script, edit the script area in the file "GroovyScript.java" located in "tester.script" package and set ***IS_JAVASCRIPT*** constant as **false**. (Try to run example 2 - Groovy script)

```
Groovy Script body      ->  tester.script [GroovyScript.java]
Groovy abilitation      ->  tester.settings [Constants.java]
```

    Note: If your script is a function with a boolean return, set ***IS_CONDITION*** constants as **true**. 
    (Try to run example 3 - Groovy script with boolean return)

To edit current (input) GVBuffer object, you have to edit the file "DATA-GVBuffer.txt" located in "scriptTester" folder. If you need to add/modify properties of the current GVBuffer, you have to edit the file "Constants.java" in "tester.settings" package.

```
Current buffer content      ->  /scriptTester/ [DATA-GVBuffer.txt]
Current buffer properties   ->  tester.settings [Constants.java]
```

To add an additional buffer, use "ENV-X-GVBuffer.txt" file (located in "scriptTester" folder) and setup buffer name and its properties in the Constants file (tester.settings [Constants.java] ). You can use up to 2 additional GVBuffers.

```
Current buffer              ->  /scriptTester/ [DATA-GVBuffer.txt]
Additional buffer 1 object  ->  /scriptTester/ [ENV-1-GVBuffer.txt]
Additional buffer 2 object  ->  /scriptTester/ [ENV-2-GVBuffer.txt]
buffers properties          ->  tester.settings [Constants.java]
```

To edit miscellaneous GV-SriptTester options and set up properties or buffers settings, edit the file "Constants.java" in "tester.settings" package.

```
GV-SriptTester settings     ->  tester.settings [Constants.java] 
```

