package com.prateek.eskores;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * DebugLogger class; used to get the log from the customers for debugging
 */
public class DebugLogger {
	private static String METHODS	= "methods";
	private static String DATA  = "data";
	private static String ERRORS	= "errors";
	private static String MESSAGES	= "messages";
	
	private static File logFile		= null;
	
	// instance for DebugLogger 
	private static DebugLogger instanceLogger = null;
	
	public static boolean enabled	= false;
	
	/*
	 * Method to get the instance for DebugLogger class
	 */
	public static DebugLogger sharedDebugLog() {
		if (instanceLogger == null) {
			instanceLogger = new DebugLogger();
			instanceLogger.initialize();
		}
		return instanceLogger;
	}
	
	/*
	 * Method to initialize debug logger
	 */
	public void initialize() {
		
	}
	
	/*
	 * Method to print the log into file for debugging
	 */
	private static void debugLog(String log) {
		System.out.println(log);
	}
	
	public static void method(Object log) {
		debugLog(METHODS, log);
	}
	
	public static void message(Object log) {
		debugLog(MESSAGES, log);
	}
	
	public static void data(Object log) {
		debugLog(DATA, log);
	}
	
	public static void error(Object log) {
		debugLog(ERRORS, log);
	}

	public static void debugLog(String id, Object log) {
		if (id == null) {
			id = "";
		}
		debugLog(id + " : " + log);
	}
}
