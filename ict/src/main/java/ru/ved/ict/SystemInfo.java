package ru.ved.ict;

public class SystemInfo {
	
	private SystemInfo() {}

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}