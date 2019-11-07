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