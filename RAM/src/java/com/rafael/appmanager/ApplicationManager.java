package com.rafael.appmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rafael.sdk.SystemManager;
import com.rafael.sdk.component.Application;
import com.rafael.sdk.component.Manager;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MiddlewareManager;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.middleware.jmq.JmqMiddleware;
import com.rafael.sdk.util.NormalThreadFactory;
import com.rafael.sdk.util.RealTimeThreadFactory;

public class ApplicationManager implements ApplicationManagerMBean {
	private static final String XML_ELEMENT_MANAGERS = "Managers";
	private static final String XML_ELEMENT_MANAGER = "Manager";	
	private static final String XML_ELEMENT_APPLICATIONS = "Applications";	
	private static final String XML_ELEMENT_APPLICATION = "Application";
	private static final String XML_ATTRIBUTE_CLASS_NAME = "className";
	private static final String XML_ATTRIBUTE_DOWN_CONNECTION = "downConnection";		
	private static final String XML_ATTRIBUTE_SERVICE = "service";
	
	private static final String XML_ATTRIBUTE_ASYNC_HANDLER_THREAD_COUNT  = "downAsyncHandlerThreadCount";
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_POOL_PRIORITY  = "downAsyncHandlerThreadPoolPriority";
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_THREAD_PRIORITY = "downAsyncThreadPriority";

	private static final String XML_ATTRIBUTE_MAIN_THREAD_REALTIME  = "mainThreadRealtime";
	private static final String XML_ATTRIBUTE_MAIN_THREAD_PRIORITY  = "mainThreadPriority";

	private static final String XML_ATTRIBUTE_LOGGER_FILENAME	 = "loggerFilename";	
	private static final String XML_ATTRIBUTE_LOGGER_FILE_SIZE	 = "loggerFileSize";	
	private static final String XML_ATTRIBUTE_LOGGER_FILE_COUNT	 = "loggerFileCount";	
	private static final String XML_ATTRIBUTE_LOGGER_SOCKET_HOST = "loggerSocketHost";	
	private static final String XML_ATTRIBUTE_LOGGER_SOCKET_PORT = "loggerSocketPort";
	private static final String XML_ATTRIBUTE_LOGGER_PRIORITY	 = "loggerPriority";	
	private static final String XML_ATTRIBUTE_MIDDLEWARE 		 = "middleware";
	
	private Map<String, Application> applicationsMap;
	private SystemManager systemManager;
	private ArrayList<ManagerDescriptor> managerDescriptors = null;
	private NodeList fwNodeList;
	private Element docElement;
	
	private class ManagerDescriptor{
		Manager manager;
	}

	public ApplicationManager(String configurationFile)	{
		// a map of all the applications
		applicationsMap = new HashMap<String, Application>();
		
		managerDescriptors  = new ArrayList<ApplicationManager.ManagerDescriptor>();
		
		// get the system manager to save all the managers
		systemManager = SystemManager.getInstance();
		
		open(configurationFile);
	}
	
	private Element open(String configurationFile) {
		try {
			// load configuration from XML file
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();			
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(getClass().getClassLoader().getResourceAsStream(configurationFile));			
			docElement = dom.getDocumentElement();
			// get the list of elements of the file 
			fwNodeList = docElement.getChildNodes();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return docElement;
	}
	
	public void init() {
		configure();
		createManagers();
		bindManagers();
		createApplications();
	}

	private void setSystemLogger() {
		String loggerPriority = docElement.getAttribute(XML_ATTRIBUTE_LOGGER_PRIORITY);
		if (!loggerPriority.isEmpty()) {
			SystemManager.getInstance().setLoggerPriority(Integer.valueOf(loggerPriority));
		}
		
		String loggerFilename = docElement.getAttribute(XML_ATTRIBUTE_LOGGER_FILENAME);
		String loggerFileSize = docElement.getAttribute(XML_ATTRIBUTE_LOGGER_FILE_SIZE);
		String loggerFileCount = docElement.getAttribute(XML_ATTRIBUTE_LOGGER_FILE_COUNT);
		if (!loggerFilename.isEmpty()) {
			SystemManager.getInstance().logRasToFile(loggerFilename, Integer.valueOf(loggerFileSize), Integer.valueOf(loggerFileCount));
		}
		
		String loggerSocketHost = docElement.getAttribute(XML_ATTRIBUTE_LOGGER_SOCKET_HOST);
		String loggerSocketPort = docElement.getAttribute(XML_ATTRIBUTE_LOGGER_SOCKET_PORT);
		if (!loggerSocketHost.isEmpty()) {
			SystemManager.getInstance().logRasToSocket(loggerSocketHost, Integer.valueOf(loggerSocketPort));
		}
	}
	
	private void setSystemMiddleware() {
		String middlewareType = docElement.getAttribute(XML_ATTRIBUTE_MIDDLEWARE);
		if (!middlewareType.isEmpty()) {
			Class<?> appClass = null;
			Middleware middleware = null;
			try {
				appClass = Class.forName(middlewareType);
				middleware = (Middleware) appClass.newInstance();
				MiddlewareManager.instance().setMiddleware(middleware);
			}
			catch (Exception e) {
				MiddlewareManager.instance().setMiddleware(new JmqMiddleware());
			}
		}
		else {
			MiddlewareManager.instance().setMiddleware(new JmqMiddleware());
		}
	}
	
	private void configure() {
		setSystemLogger();
		setSystemMiddleware();
	}
	
	/**
	 * 
	 * @param inConfigurationFile
	 */
	public void createManagers() {
		if( (fwNodeList != null) && (fwNodeList.getLength() > 0) ) {
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
					
				// create the managers
				if( true == fwElement.getNodeName().equals(XML_ELEMENT_MANAGERS) ){
					createManagers(fwElement);
				}
			}
		}
	}
	
	public void createApplications()	{
		if( (fwNodeList != null) && (fwNodeList.getLength() > 0) ){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
					
				// create the managers
				if( true == fwElement.getNodeName().equals(XML_ELEMENT_APPLICATIONS) ){
					NodeList applicationsNodeList = fwElement.getChildNodes();
					
					for (int applicationIndex = 0; applicationIndex < applicationsNodeList.getLength(); applicationIndex++)	{			
						Node applicationElement = applicationsNodeList.item(applicationIndex);
						
						// an application was found
						if (XML_ELEMENT_APPLICATION.equals(applicationElement.getNodeName())) {
							Element appElement = (Element)applicationElement;
							String className = appElement.getAttribute(XML_ATTRIBUTE_CLASS_NAME);
							String mainThreadRealtime = appElement.getAttribute(XML_ATTRIBUTE_MAIN_THREAD_REALTIME);
							String mainThreadPriority = appElement.getAttribute(XML_ATTRIBUTE_MAIN_THREAD_PRIORITY);

							Application apllication = createApplication(className);
							apllication.onCreate();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							if (mainThreadRealtime.equals("true")) {
								Executors.newSingleThreadExecutor(new RealTimeThreadFactory(Integer.valueOf(mainThreadPriority))).execute(apllication);								
							}
							else {
								Executors.newSingleThreadExecutor(new NormalThreadFactory(Integer.valueOf(mainThreadPriority))).execute(apllication);								
							}
						}
					}
				}
			}
		}
	}

	
	private void createManagers(Node fwElement) {
		NodeList managersNodeList = fwElement.getChildNodes();
		
		for (int managerIndex = 0; managerIndex < managersNodeList.getLength(); managerIndex++)	{			
			Node managerElement = managersNodeList.item(managerIndex);
			
			// a manager was found
			if (XML_ELEMENT_MANAGER.equals(managerElement.getNodeName())){
				Element mngrElement = (Element)managerElement;
				String className = mngrElement.getAttribute(XML_ATTRIBUTE_CLASS_NAME);
				
				// create the manager
				Manager manager = createManager(className);
				
				// get the number of threads that handle the requests  
				String downAsyncHandlerThreadCount = mngrElement.getAttribute(XML_ATTRIBUTE_ASYNC_HANDLER_THREAD_COUNT);

				if (!downAsyncHandlerThreadCount.isEmpty()) {
					// set the number of threads for the manager
					manager.setDownAsyncThreadCount(new Integer(downAsyncHandlerThreadCount));
				}
				
				String downAsyncHandlerThreadPoolPriority = mngrElement.getAttribute(XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_POOL_PRIORITY);
				manager.setDownAsyncThreadPoolFactory(new NormalThreadFactory(Integer.valueOf(downAsyncHandlerThreadPoolPriority)));

				// get down connection type (ipc or inproc)
				String downConnection = mngrElement.getAttribute(XML_ATTRIBUTE_DOWN_CONNECTION);
				String downConnectionType = downConnection;
				String downConnectionString = "pub.down." + className;

				if (downConnection.contains("tcp")) {
					downConnectionType = downConnection.substring(0, 6);
					String[] strings = downConnection.split(":");
					downConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(downConnectionString).hashCode());
				}

				// create the publisher
				MwPublisher publisher = MiddlewareManager.instance().createPublisher(downConnectionType, downConnectionString, manager);
				
				// put the publisher to the service
				manager.setDownPublisher(publisher);
				
				ManagerDescriptor managerDescriptor = new ManagerDescriptor();
				
				managerDescriptor.manager = manager;
				
				managerDescriptors.add(managerDescriptor);
			}
		}
	}
	
	
	
	/**
	 * Create managers 
	 */
	public void bindManagers() {
		if( (fwNodeList != null) && (fwNodeList.getLength() > 0) ){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);

				// create the managers
				if( true == fwElement.getNodeName().equals(XML_ELEMENT_MANAGERS) ){
					NodeList managersNodeList = fwElement.getChildNodes();

					int index = 0;

					for (int managerIndex = 0; managerIndex < managersNodeList.getLength(); managerIndex++)	{			
						Node managerElement = managersNodeList.item(managerIndex);

						// a manager was found
						if (XML_ELEMENT_MANAGER.equals(managerElement.getNodeName())){
							Element mngrElement = (Element)managerElement;

							// create the manager
							ManagerDescriptor managerDescriptor = managerDescriptors.get(index++);

							Manager manager = ((Manager)managerDescriptor.manager); 

							// get the subscriber connection string
							String serviceName = mngrElement.getAttribute(XML_ATTRIBUTE_SERVICE);
							
							// get down connection type (ipc or inproc)
							String downConnection = mngrElement.getAttribute(XML_ATTRIBUTE_DOWN_CONNECTION);
							String downConnectionType = downConnection;
							String downConnectionString = "pub.up." + serviceName;
							
							if (downConnection.contains("tcp")) {
								downConnectionType = downConnection.substring(0, 6);
								String[] strings = downConnection.split(":");
								downConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(downConnectionString).hashCode());
							}
							
							// create the subscriber
							MwSubscriber subscriber = MiddlewareManager.instance().createSubscriber(downConnectionType, downConnectionString, manager);

							subscriber.connect(downConnectionType + downConnectionString);

							// put the subscriber to the service
							manager.setDownSubscriber(subscriber);
							
							downConnectionString = "rep.down." + serviceName;
							
							if (downConnection.contains("tcp")) {
								String[] strings = downConnection.split(":");
								downConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(downConnectionString).hashCode());
							}
							
							// put the requester to the service
							manager.createRequesters(downConnectionType, downConnectionString, 10, true, true);
							
							// get down async thread priority
							String downAsyncThreadPriority = mngrElement.getAttribute(XML_ATTRIBUTE_DOWN_ASYNC_THREAD_PRIORITY);

							manager.init(Integer.valueOf(downAsyncThreadPriority), 0, 0, false);
							manager.start();
						}
					}
				}
			}
		}
	}
	
	

	/**
	 * 
	 * @param className
	 * @return
	 */
	private Manager createManager(String className)	{
		Manager manager = null;
		
		if (null == systemManager.getManager(className))
		{
			try
			{
				Class<?> appClass = Class.forName(className);
				manager = (Manager) appClass.newInstance();
				
				systemManager.addManager(manager.getClass().getSimpleName(),manager);

			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return manager;
	}	
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	private Application createApplication(String className)	{
		Application application = null;
		
		if (applicationsMap.containsKey(className) == false)
		{
			try
			{
				Class<?> appClass = Class.forName(className);
				application = (Application) appClass.newInstance();

			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return application;
	}

	/**
	 * 
	 * @param appClassName
	 * @return
	 */
	public Application getApplication(String appClassName){
		if (this.applicationsMap.containsKey(appClassName) == true)	{
			Application application = this.applicationsMap.get(appClassName);
			if (null != application)
				return application;
		}
		return null;
	}

	/**
	 * 
	 * @param appName
	 */
	public void runApplication(String appName){
		try	{
			System.out.println("Executing application " + appName);
			ProcessBuilder pb = new ProcessBuilder(appName);
			//pb.redirectOutput(Redirect.INHERIT);
			pb.start();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void startApplication(String appClassName){
		Application application = getApplication(appClassName);
		if (null != application){
			System.out.println("Starting application " + appClassName);
			application.onStart();
		}
	}

	@Override
	public void stopApplication(String appClassName){
		Application application = getApplication(appClassName);
		if (null != application){
			application.onStop();
		}
	}
}