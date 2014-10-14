package pluginforlibgdx.wizards;

public class Message {
	protected static String PROJECT_NAME_IS_NULL = "Project name must be specified!";
	protected static String PROJECT_NAME_CONTAINS_INVALID_CHARACTER = "Project name must not contain '.','`',' ','?',';',':'";
	
	protected static String PROJECT_MAIN_PACKAGE_IS_NULL = "Package must be specified!";
	protected static String PROJECT_MAIN_PACKAGE_CONTAINS_INVALID_CHARACTER = "Package must not contain ',', '?', ':', ';'";
	protected static String PROJECT_MAIN_PACKAGE_ENDS_BY_DOT_CHACRACTER = "Package must not end by '.'";
	
	protected static String PROJECT_MAIN_CLASS_IS_NULL = "Game class must be specified!";
	protected static String PROJECT_MAIN_CLASS_CONTAINS_INVALID_CHARACTER = "Game class must not contain ',','.','?',';'";
	protected static String PROJECT_MAIN_CLASS_BEGINS_WITH_LOWERCASE_CHARACTER = "Game class must begin with uppercase letter!";
	
	protected static String PROJECT_DESTINATION_FOLDER_IS_NULL = "Destination folder must be specified!!";
	protected static String PROJECT_DESTINATION_FOLDER_IS_NOT_EXIST = "Destination folder is not exist!";
}
