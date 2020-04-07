package tester.settings;

import java.util.HashSet;
import java.util.Set;

public interface Constants {
	
	//// Execution settings
	public static final String LANGUAGE = "Javascript";  // Groovy script  |  Javascript | Python
	public static final boolean IS_FUNCTION = false;  // false -> script  |  true -> function
	
	
	//// Role settings	
	public static final String NAME = "gvadmin";
	public static final String ROLES = "admin";
	public static final String ADDRESSES = "gvadmin@greenvulcano.com";
	public static final String ENTITY_ID = "100";
	
	
	/// External JS library
	public static final Set<String> libName = new HashSet<String>();//"gvadmin";
	public static enum LibraryNames {
		mustache // '.js' will be added
	}
	
	//// Visual settings
	public static final boolean IMPROVE_JSON_VISUALIZATION = true;
	
}
