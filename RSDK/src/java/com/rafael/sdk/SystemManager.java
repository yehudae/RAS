package com.rafael.sdk;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.SocketHandler;

import com.rafael.sdk.component.Manager;

public class SystemManager {
	
	private static SystemManager instance = new SystemManager();
	private Logger rasLogger;
	private LogManager logManager;
	private Map<String, Manager> managersMap = null;
	private LinkedBlockingQueue<LogRecord> logRecords = new LinkedBlockingQueue<LogRecord>();
	private FileHandler rasFileHandler = null;
	private SocketHandler rasSocketHandler = null;
	private int loggerPriority = 5;
		
	private SystemManager() {
		managersMap = new HashMap<String, Manager>();		
		rasLogger = Logger.getLogger(SystemManager.class.getName());
		logManager = LogManager.getLogManager();
		handleLogRecords(loggerPriority);
	}
	
	static public SystemManager getInstance() {
		return instance;
	}
	
	private void handleLogRecords(final int loggerPriority) {
		Executors.newSingleThreadExecutor().execute(new Runnable(){
			
			@Override
			public void run() {
				Thread.currentThread().setPriority(loggerPriority);
				while (true) {
					try {
						LogRecord logRecord = logRecords.take();
						rasLogger.log(logRecord);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void logRasToFile(String filename, int fileSize, int numOfFiles) {
		try {
			if (null != rasFileHandler) return;
			rasFileHandler = new FileHandler(filename, fileSize, numOfFiles, true);
	        rasLogger.addHandler(rasFileHandler);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        rasFileHandler.setFormatter(formatter);  
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logRasToSocket(String host, int port) {
		try {
			if (null != rasSocketHandler) return;
			rasSocketHandler = new SocketHandler(host, port);
	        rasLogger.addHandler(rasSocketHandler);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        rasSocketHandler.setFormatter(formatter);  
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setRasLoggerLevel(Level level) {
		rasLogger.setLevel(level);
	}
	
	public void addManager(String managerName, Manager manager) {
		managersMap.put(managerName, manager);
	}
	
	public Manager getManager(String managerName) {
		return managersMap.get(managerName);
	}
	
	public void setLoggerPriority(int priority) {
		this.loggerPriority = priority;
	}
	
	// Log a message, with no arguments.
	public void rasLog(Level level, String sourceClass, String sourceMethod, int threadId, String msg) {
		LogRecord logRecord = new LogRecord(level, msg);
		logRecord.setSourceClassName(sourceClass);
		logRecord.setSourceMethodName(sourceMethod);
		logRecord.setThreadID(threadId);
		try {
			logRecords.put(logRecord);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	 
	public Logger getApplicationLogger(String application) {
		Logger logger = logManager.getLogger(application);
		return logger;
	}
}
