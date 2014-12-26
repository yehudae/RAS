package com.rafael.framework;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rafael.appmanager.ApplicationManager;
import com.rafael.framework.factory.ActivityFactory;
import com.rafael.framework.factory.DynamicActivityFactory;
import com.rafael.sdk.SystemManager;
import com.rafael.sdk.component.Component;
import com.rafael.sdk.component.Device;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MiddlewareManager;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.middleware.jmq.JmqMiddleware;
import com.rafael.sdk.util.NormalThreadFactory;

public class FrameworkManager implements FrameworkManagerMBean{
	
	private static final String XML_ELEMENT_SERVICES 			 = "Services";	
	private static final String XML_ELEMENT_SERVICE 			 = "Service";
	private static final String XML_ELEMENT_DEVICES 			 = "Devices";
	private static final String XML_ELEMENT_DEVICE 				 = "Device";
	private static final String XML_ELEMENT_CONNECTION			 = "Connection";
	private static final String XML_ELEMENT_APPLICATIONS		 = "Applications";
	private static final String XML_ELEMENT_CONFIGURATION_FILE 	 = "ConfigurationFile";

	private static final String XML_ATTRIBUTE_CLASS_NAME		 = "className";
	private static final String XML_ATTRIBUTE_UP_CONNECTION		 = "upConnection";
	private static final String XML_ATTRIBUTE_DOWN_CONNECTION	 = "downConnection";
	private static final String XML_ATTRIBUTE_DIR				 = "dir";
	private static final String XML_ATTRIBUTE_DIR_UP			 = "up";
	private static final String XML_ATTRIBUTE_DIR_DOWN			 = "down";	
	
	private static final String XML_ATTRIBUTE_LOGGER_FILENAME	 = "loggerFilename";	
	private static final String XML_ATTRIBUTE_LOGGER_FILE_SIZE	 = "loggerFileSize";	
	private static final String XML_ATTRIBUTE_LOGGER_FILE_COUNT	 = "loggerFileCount";

	private static final String XML_ATTRIBUTE_LOGGER_SOCKET_HOST = "loggerSocketHost";	
	private static final String XML_ATTRIBUTE_LOGGER_SOCKET_PORT = "loggerSocketPort";	

	private static final String XML_ATTRIBUTE_LOGGER_PRIORITY	 = "loggerPriority";	

	private static final String XML_ATTRIBUTE_APPLICATIONS_FILENAME = "name";	

	private static final String XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_COUNT  			= "upAsyncHandlerThreadCount";
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_COUNT  			= "downAsyncHandlerThreadCount";
	private static final String XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_POOL_PRIORITY 	= "upAsyncHandlerThreadPoolPriority";
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_POOL_PRIORITY  	= "downAsyncHandlerThreadPoolPriority";
	private static final String XML_ATTRIBUTE_UP_ASYNC_THREAD_PRIORITY 					= "upAsyncThreadPriority";
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_THREAD_PRIORITY		 		= "downAsyncThreadPriority";
	private static final String XML_ATTRIBUTE_UP_SYNC_THREAD_PRIORITY 					= "upSyncThreadPriority";
	private static final String XML_ATTRIBUTE_UP_SYNC_REALTIME 							= "upSyncRealtime";
	private static final String XML_ATTRIBUTE_MIDDLEWARE 								= "middleware";

	private ComponentManager deviceManager 	= null;
	private ComponentManager serviceManager = null;
	ApplicationManager applicationManager 	= null;
	private HashMap<String, Component> componentDescriptors = null;	
	private ActivityFactory activityFactory = new DynamicActivityFactory();
	private NodeList fwNodeList;
	private Element docElement;
	
	public FrameworkManager(String configurationFile) {
		deviceManager = new ComponentManager();
		serviceManager = new ComponentManager();		
		componentDescriptors = new HashMap<String, Component>();
		open(configurationFile);
	}		

	public void init(ActivityFactory activityFactory) {
		this.activityFactory = activityFactory;
		init();
	}
	
	private void open(String configurationFile) {
		try	{
			// load configuration from XML file
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();			
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(getClass().getClassLoader().getResourceAsStream(configurationFile));			
			docElement = dom.getDocumentElement();
			// get the list of elements of the file 
			fwNodeList = docElement.getChildNodes();
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	public void init() {		
		configure();

		// create the components and their publishers
		createComponents();
	
		// bind the components
		bindComponents();
			
		// create applications
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

	private void createApplications() {
		if (null != applicationManager) {
			applicationManager.createApplications();
		}
	}

	/**
	 * Create the components and their publishers
	 * @param fwNodeList
	 */
	private void createComponents() {
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
				
				// create the devices
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_DEVICES))	{
					createComponents(fwElement, XML_ELEMENT_DEVICE);
				}
			}
		}
		
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
				
				// create the services
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_SERVICES)){
					createComponents(fwElement, XML_ELEMENT_SERVICE);
				}
			}
		}
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
			
				// create the application manager
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_APPLICATIONS)){
					createManager(fwElement);
				}
			}
		}
	}

	/**
	 * Bind the components
	 * @param fwNodeList
	 */
	private void bindComponents() {
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
				
				// create the devices
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_DEVICES))	{
					bindComponents(fwElement, XML_ELEMENT_DEVICE, deviceManager);
				}
			}
		}
		
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
				
				// create the services
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_SERVICES)){
					bindComponents(fwElement, XML_ELEMENT_SERVICE, serviceManager);
				}
			}
		}
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
			
				// create the application manager
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_APPLICATIONS)){
					bindApplications(fwElement);
				}
			}
		}
	}


	/**
	 * Create Devices and bind them the connections 
	 */
	private void createComponents(Node fwElement, String componentType) {
		NodeList componentNodeList = fwElement.getChildNodes();
		
		for (int componentIndex = 0; componentIndex<componentNodeList.getLength(); componentIndex++)	{			
			Node componentNode = componentNodeList.item(componentIndex);
			
			// a device was found
			if (componentType.equals(componentNode.getNodeName())){
				Element componentElement = (Element)componentNode;
				String className = componentElement.getAttribute(XML_ATTRIBUTE_CLASS_NAME);
							
				// create the device
				Component component = deviceManager.createComponent(className);
				
				// get the number of threads that handle the requests  
				String upAsyncHandlerThreadCount = componentElement.getAttribute(XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_COUNT);
				String downAsyncHandlerThreadCount = componentElement.getAttribute(XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_COUNT);
				
				if (!upAsyncHandlerThreadCount.isEmpty()) {
					component.setUpAsyncThreadCount((Integer.valueOf(upAsyncHandlerThreadCount)));
				}

				if (!downAsyncHandlerThreadCount.isEmpty()) {
					component.setDownAsyncThreadCount((Integer.valueOf(downAsyncHandlerThreadCount)));
				}

				String upAsyncHandlerThreadPoolPriority = componentElement.getAttribute(XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_POOL_PRIORITY);
				component.setUpAsyncThreadPoolFactory(new NormalThreadFactory(Integer.valueOf(upAsyncHandlerThreadPoolPriority)));
				
				String downAsyncHandlerThreadPoolPriority = componentElement.getAttribute(XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_POOL_PRIORITY);
				component.setDownAsyncThreadPoolFactory(new NormalThreadFactory(Integer.valueOf(downAsyncHandlerThreadPoolPriority)));
				
				////////////UP PUBLISHER//////////////////////////
				// get connection type (TCP/IPC/INPROC)
				String upConnection = componentElement.getAttribute(XML_ATTRIBUTE_UP_CONNECTION);
				String upConnectionType = upConnection;
				String upConnectionString = "pub.up." + className;
								
				if (upConnection.contains("tcp")) {
					upConnectionType = upConnection.substring(0, 6);
					String[] strings = upConnection.split(":");
					upConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(upConnectionString).hashCode());
				}
				
				MwPublisher upPublisher = MiddlewareManager.instance().createPublisher(upConnectionType, upConnectionString, component);

				// put the publisher to the device
				component.setUpPublisher(upPublisher);
				//////////////////////////////////////////////////
				
				////////////DOWN PUBLISHER////////////////////////
				// get connection type (TCP/IPC/INPROC)
				String downConnection = componentElement.getAttribute(XML_ATTRIBUTE_DOWN_CONNECTION);
				String downConnectionType = downConnection;
				String downConnectionString = "pub.down." + className;
				
				if (downConnection.contains("tcp")) {
					downConnectionType = downConnection.substring(0, 6);
					String[] strings = downConnection.split(":");
					downConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(downConnectionString).hashCode());
				}

				// create the publisher
				MwPublisher downPublisher = MiddlewareManager.instance().createPublisher(downConnectionType, downConnectionString, component);
								
				// put the publisher to the device
				component.setDownPublisher(downPublisher);
				//////////////////////////////////////////////////
				
				////////////DOWN REPLIER////////////////////////
				// get connection type (TCP/IPC/INPROC)
				String repConnectionType = upConnection;
				String repConnectionString = "rep.down." + className;
				
				if (upConnection.contains("tcp")) {
					repConnectionType = upConnection.substring(0, 6);
					String[] strings = upConnection.split(":");
					repConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(repConnectionString).hashCode());
				}
				
				// create the replier
				MwReplier replier = MiddlewareManager.instance().createReplier(repConnectionType, repConnectionString, component);

				// put the replier to the device
				component.setReplier(replier);
				//////////////////////////////////////////////////
				
				// create a component descriptor
				// add it to the vector of components descriptors
				componentDescriptors.put(className, component);
			}
		}
	}
	
	private void bindComponents(Node fwElement, String componentType, ComponentManager componentManager) {
		NodeList componentNodeList = fwElement.getChildNodes();
		
		for (int componentIndex = 0; componentIndex<componentNodeList.getLength(); componentIndex++) {			
			Node componentNode = componentNodeList.item(componentIndex);
			
			// a device was found
			if (componentType.equals(componentNode.getNodeName())) {
				Element componentElement = (Element)componentNode;
				
				// the name of the class for creating the device
				String className = componentElement.getAttribute(XML_ATTRIBUTE_CLASS_NAME);
				
				// get up connection type (ipc or inproc)
				String upConnection = componentElement.getAttribute(XML_ATTRIBUTE_UP_CONNECTION);
				
				// get down connection type (ipc or inproc)
				String downConnection = componentElement.getAttribute(XML_ATTRIBUTE_DOWN_CONNECTION);

				String upConnectionType = upConnection;
				if (upConnection.contains("tcp")) {
					upConnectionType = upConnection.substring(0, 6);
				}
				
				String downConnectionType = downConnection;
				if (downConnection.contains("tcp")) {
					downConnectionType = downConnection.substring(0, 6);
				}

				// put the subscriber to the device
				Component component = componentDescriptors.get(className); 

				// create up subscriber
				MwSubscriber subUp = MiddlewareManager.instance().createSubscriber(upConnectionType, className, component);
				
				// create down subscriber
				MwSubscriber subDown = MiddlewareManager.instance().createSubscriber(downConnectionType, className, component);

				NodeList connNodeList = componentNode.getChildNodes();

				for (int connIndex = 0; connIndex<connNodeList.getLength(); connIndex++) {			
					Node connNode = connNodeList.item(connIndex);

					// a connection was found
					if (XML_ELEMENT_CONNECTION.equals(connNode.getNodeName())) {
						Element connElement = (Element)connNode;
						String connectionClassName = connElement.getAttribute(XML_ATTRIBUTE_CLASS_NAME);
						String dir = connElement.getAttribute(XML_ATTRIBUTE_DIR);
						if (dir.equals(XML_ATTRIBUTE_DIR_UP)) {
							String upConnectionString = "pub.down." + connectionClassName;
							if (upConnection.contains("tcp")) {
								String[] strings = upConnection.split(":");
								upConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(upConnectionString).hashCode());
							}
							subUp.connect(upConnectionType + upConnectionString);
						}
						else if (dir.equals(XML_ATTRIBUTE_DIR_DOWN)) {
							String downConnectionString = "pub.up." + connectionClassName;
							if (downConnection.contains("tcp")) {
								String[] strings = downConnection.split(":");
								downConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(downConnectionString).hashCode());
							}
							subDown.connect(downConnectionType + downConnectionString);
						}
					}
				}
				
				// set the up subscriber
				component.setUpSubscriber(subUp);
				
				// set the down subscriber
				component.setDownSubscriber(subDown);
				
				String reqConnectionType = downConnection;
				String reqConnectionString = "rep.down." + className;
				
				if (downConnection.contains("tcp")) {
					reqConnectionType = downConnection.substring(0, 6);
					String[] strings = downConnection.split(":");
					reqConnectionString = strings[1].substring(2) + ":" + Math.abs((short)(reqConnectionString).hashCode());
				}
				
				// set the down requester
				component.createRequesters(reqConnectionType, reqConnectionString, 10, true, false);

				// get down async thread priority
				String downAsyncThreadPriority = componentElement.getAttribute(XML_ATTRIBUTE_DOWN_ASYNC_THREAD_PRIORITY);

				// get up async thread priority
				String upAsyncThreadPriority = componentElement.getAttribute(XML_ATTRIBUTE_UP_ASYNC_THREAD_PRIORITY);
				
				// get up sync thread priority
				String upSyncThreadPriority = componentElement.getAttribute(XML_ATTRIBUTE_UP_SYNC_THREAD_PRIORITY);

				// get up sync real-time
				String upSyncRealtime = componentElement.getAttribute(XML_ATTRIBUTE_UP_SYNC_REALTIME);

				component.init(Integer.valueOf(downAsyncThreadPriority), Integer.valueOf(upAsyncThreadPriority),
							   Integer.valueOf(upSyncThreadPriority), upSyncRealtime.equals("true"));
				
				activityFactory.createActivities(component);

				// add the device to the device manager
				deviceManager.addComponent(className, component);
				
				// start the device
				component.start();
			}
		}
	}
	
	private void createManager(Node fwElement)	{
		NodeList applicationsNodeList = fwElement.getChildNodes();
		
		for (int i=0; i<applicationsNodeList.getLength(); i++){			
			Node node = applicationsNodeList.item(i);
			
			// a configuration file was found
			if (XML_ELEMENT_CONFIGURATION_FILE.equals(node.getNodeName())){
				Element configurationFileElement = (Element)node;
				applicationManager = new ApplicationManager(configurationFileElement.getAttribute(XML_ATTRIBUTE_APPLICATIONS_FILENAME));
				applicationManager.createManagers();
			}
		}
	}
	private void bindApplications(Node fwElement)	{
		NodeList configurationFileNodeList = fwElement.getChildNodes();
		
		for (int configurationFileIndex = 0; configurationFileIndex < configurationFileNodeList.getLength(); configurationFileIndex++){			
			Node servElement = configurationFileNodeList.item(configurationFileIndex);
			
			// a configuration file was found
			if (XML_ELEMENT_CONFIGURATION_FILE.equals(servElement.getNodeName())){
				if (null != applicationManager){
					applicationManager.bindManagers();
				}
			}
		}
	}

	@Override
	public void startDevice(String deviceClassName){
		deviceManager.startComponent(deviceClassName);
	}


	@Override
	public void stopDevice(String deviceClassName) {		
	}

	@Override
	public void loadDevice(String deviceClassName, int numOfThreads) {
		try	{
			Device device = null;						
			device = (Device)deviceManager.createComponent(deviceClassName);
			device.setDownAsyncThreadCount((new Integer(numOfThreads)));
			device.setUpAsyncThreadCount((new Integer(numOfThreads)));
			deviceManager.addComponent(deviceClassName, device);
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
}