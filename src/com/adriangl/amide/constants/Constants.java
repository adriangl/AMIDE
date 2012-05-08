package com.adriangl.amide.constants;

public class Constants {
	
	public static String SERVER_HOST = "localhost";
	public static final int SERVER_PORT = 1234;
	
	public static final int asteroidFields = 8;
	// 4 = size (in bytes) of a float value.
	public static final int asteroidBytes = 4*asteroidFields;
	public static final int asteroidNumber = 2;	
	public static final int bufferSize = 4+(asteroidBytes*asteroidNumber);
	
	public static final int BULLET_TIMEOUT = 500;

}
