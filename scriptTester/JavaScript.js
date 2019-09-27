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