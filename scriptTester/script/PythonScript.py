# example 4: ToUpper

text = data.getObject()
data.setProperty("input",text)
outputText = text.upper()
data.setProperty("output",outputText)
data.setObject(outputText)