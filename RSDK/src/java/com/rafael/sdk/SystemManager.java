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

// TODO: Auto-generated Javadoc
/**
 * The Class SystemManager.
 */
public class SystemManager {
	
	/** The instance. */
	private static SystemManager instance = new SystemManager();
	
	/** The ras logger. */
	private Logger rasLogger;
	
	/** The log manager. */
	private LogManager logManager;
	
	/** The managers map. */
	private Map<String, Manager> managersMap = null;
	
	/** The log records. */
	private LinkedBlockingQueue<LogRecord> logRecords = new LinkedBlockingQueue<LogRecord>();
	
	/** The ras file handler. */
	private FileHandler rasFileHandler = null;
	
	/** The ras socket handler. */
	private SocketHandler rasSocketHandler = null;
	
	/** The logger priority. */
	private int loggerPriority = 5;
		
	/**
	 * Instantiates a new system manager.
	 */
	private SystemManager() {
		managersMap = new HashMap<String, Manager>();		
		rasLogger = Logger.getLogger(SystemManager.class.getName());
		logManager = LogManager.getLogManager();
		handleLogRecords(loggerPriority);
	}
	
	/**
	 * Gets the single instance of SystemManager.
	 *
	 * @return single instance of SystemManager
	 */
	static public SystemManager getInstance() {
		return instance;
	}
	
	/**
	 * Handle log records.
	 *
	 * @param loggerPriority the logger priority
	 */
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
	
	/**
	 * Log ras to file.
	 *
	 * @param filename the filename
	 * @param fileSize the file size
	 * @param numOfFiles the num of files
	 */
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
	
	/**
	 * Log ras to socket.
	 *
	 * @param host the host
	 * @param port the port
	 */
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
	
	/**
	 * Sets the ras logger level.
	 *
	 * @param level the new ras logger level
	 */
	public void setRasLoggerLevel(Level level) {
		rasLogger.setLevel(level);
	}
	
	/**
	 * Adds the manager.
	 *
	 * @param managerName the manager name
	 * @param manager the manager
	 */
	public void addManager(String managerName, Manager manager) {
		managersMap.put(managerName, manager);
	}
	
	/**
	 * Gets the manager.
	 *
	 * @param managerName the manager name
	 * @return the manager
	 */
	public Manager getManager(String managerName) {
		return managersMap.get(managerName);
	}
	
	/**
	 * Sets the logger priority.
	 *
	 * @param priority the new logger priority
	 */
	public void setLoggerPriority(int priority) {
		this.loggerPriority = priority;
	}
	
	// Log a message, with no arguments.
	/**
	 * Ras log.
	 *
	 * @param level the level
	 * @param sourceClass the source class
	 * @param sourceMethod the source method
	 * @param threadId the thread id
	 * @param msg the msg
	 */
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
	 
	/**
	 * Gets the application logger.
	 *
	 * @param application the application
	 * @return the application logger
	 */
	public Logger getApplicationLogger(String application) {
		Logger logger = logManager.getLogger(application);
		return logger;
	}
}
