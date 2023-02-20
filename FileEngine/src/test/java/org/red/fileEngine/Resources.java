package org.red.fileEngine;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Resources {
	public final static Path RESOURCE_DIR = Paths.get("src","test","resources");
	public final static String TEST_FOLDER_NAME = "TestFileTree";
	
	public final static Path CORE = Resources.RESOURCE_DIR.resolve(Resources.TEST_FOLDER_NAME);
	
}
