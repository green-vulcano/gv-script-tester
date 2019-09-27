package tester.settings;

public interface Constants {
	
	//// Miscellanous ////
	
	public static final boolean ENABLE_ROLE_VIEW = false;
	public static final String CURRENT_ROLE = "hurry_manager";
	public static final boolean ENABLE_IDENTITY_KEY_VIEW = false;
	public static final String IDENTITY_KEY = "id";
	public static final String IDENTITY_VALUE = "666";
	
	
	//// Execution settings

	public static final boolean IS_JAVASCRIPT = true;  // false -> Groovy script  |  true -> Javascript
	public static final boolean IS_FUNCTION = false;  // false -> script  |  true -> function
	
	
	//// ScriptTester property ////
	
	public static final String JAVASCRIPT_FILE_PATH = "scriptTester/JavaScript.js";
	public static final String LOG_FILE_PATH = "scriptTester/environmentLog.txt";
	public static final String GV_FILE_BUFFER_NAME = "scriptTester/DATA-GVBuffer.txt";	
	public static final String ENVIRONMENT_1_FILE_BUFFER_NAME = "scriptTester/ENV-1-GVBuffer.txt";		
	public static final String ENVIRONMENT_2_FILE_BUFFER_NAME = "scriptTester/ENV-2-GVBuffer.txt";		
	
	
	//// Property in the current buffer (Object -> scriptTester/GVBuffer.txt) ////
	
	public static final String PROPERTY_NAME_1 = "FIRST_NAME";
	public static final String PROPERTY_VALUE_1 = "Gino";
	
	public static final String PROPERTY_NAME_2 = "LAST_NAME";
	public static final String PROPERTY_VALUE_2 = "Ginotti";
	
	public static final String PROPERTY_NAME_3 = "PRIVATE_CAR_PLATE";
	public static final String PROPERTY_VALUE_3 = "AD123CD";
	
	public static final String PROPERTY_NAME_4 = "";
	public static final String PROPERTY_VALUE_4 = "";
	
	public static final String PROPERTY_NAME_5 = "";
	public static final String PROPERTY_VALUE_5 = "";

	
	//// Property in buffer X (Object -> scriptTester/ENV-GVBuffer.txt) ////
	
	public static final String ENVIRONMENT_1_GVBUFFER_NAME = "buffer-x";
	
	public static final String ENV_PROPERTY_NAME_1 = "TAX";
	public static final String ENV_PROPERTY_VALUE_1 = "10";
	
	public static final String ENV_PROPERTY_NAME_2 = "";
	public static final String ENV_PROPERTY_VALUE_2 = "";

	public static final String ENV_PROPERTY_NAME_3 = "";
	public static final String ENV_PROPERTY_VALUE_3 = "";
	
	public static final String ENV_PROPERTY_NAME_4 = "";
	public static final String ENV_PROPERTY_VALUE_4 = "";
	
	public static final String ENV_PROPERTY_NAME_5 = "";
	public static final String ENV_PROPERTY_VALUE_5 = "";
	
	
	//// Property in buffer Y (Object -> scriptTester/ENV-2-GVBuffer.txt) ////
	
	public static final String ENVIRONMENT_2_GVBUFFER_NAME = "buffer-y";
	
	public static final String ENV_2_PROPERTY_NAME_1 = "";
	public static final String ENV_2_PROPERTY_VALUE_1 = "";
	
	public static final String ENV_2_PROPERTY_NAME_2 = "";
	public static final String ENV_2_PROPERTY_VALUE_2 = "";

	public static final String ENV_2_PROPERTY_NAME_3 = "";
	public static final String ENV_2_PROPERTY_VALUE_3 = "";
	
}
