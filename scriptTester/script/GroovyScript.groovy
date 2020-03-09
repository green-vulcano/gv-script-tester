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