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
import com.rafael.sdk.middleware.mix.MixMiddleware;
import com.rafael.sdk.util.NormalThreadFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class FrameworkManager.
 */
public class FrameworkManager implements FrameworkManagerMBean{
	
	/** The Constant XML_ELEMENT_SERVICES. */
	private static final String XML_ELEMENT_SERVICES 			 = "Services";	
	
	/** The Constant XML_ELEMENT_SERVICE. */
	private static final String XML_ELEMENT_SERVICE 			 = "Service";
	
	/** The Constant XML_ELEMENT_DEVICES. */
	private static final String XML_ELEMENT_DEVICES 			 = "Devices";
	
	/** The Constant XML_ELEMENT_DEVICE. */
	private static final String XML_ELEMENT_DEVICE 				 = "Device";
	
	/** The Constant XML_ELEMENT_CONNECTION. */
	private static final String XML_ELEMENT_CONNECTION			 = "Connection";
	
	/** The Constant XML_ELEMENT_APPLICATIONS. */
	private static final String XML_ELEMENT_APPLICATIONS		 = "Applications";
	
	/** The Constant XML_ELEMENT_CONFIGURATION_FILE. */
	private static final String XML_ELEMENT_CONFIGURATION_FILE 	 = "ConfigurationFile";

	/** The Constant XML_ATTRIBUTE_CLASS_NAME. */
	private static final String XML_ATTRIBUTE_CLASS_NAME		 = "className";
	
	/** The Constant XML_ATTRIBUTE_UP_CONNECTION. */
	private static final String XML_ATTRIBUTE_UP_CONNECTION		 = "upConnection";
	
	/** The Constant XML_ATTRIBUTE_DOWN_CONNECTION. */
	private static final String XML_ATTRIBUTE_DOWN_CONNECTION	 = "downConnection";
	
	/** The Constant XML_ATTRIBUTE_DIR. */
	private static final String XML_ATTRIBUTE_DIR				 = "dir";
	
	/** The Constant XML_ATTRIBUTE_DIR_UP. */
	private static final String XML_ATTRIBUTE_DIR_UP			 = "up";
	
	/** The Constant XML_ATTRIBUTE_DIR_DOWN. */
	private static final String XML_ATTRIBUTE_DIR_DOWN			 = "down";	
	
	/** The Constant XML_ATTRIBUTE_LOGGER_FILENAME. */
	private static final String XML_ATTRIBUTE_LOGGER_FILENAME	 = "loggerFilename";	
	
	/** The Constant XML_ATTRIBUTE_LOGGER_FILE_SIZE. */
	private static final String XML_ATTRIBUTE_LOGGER_FILE_SIZE	 = "loggerFileSize";	
	
	/** The Constant XML_ATTRIBUTE_LOGGER_FILE_COUNT. */
	private static final String XML_ATTRIBUTE_LOGGER_FILE_COUNT	 = "loggerFileCount";

	/** The Constant XML_ATTRIBUTE_LOGGER_SOCKET_HOST. */
	private static final String XML_ATTRIBUTE_LOGGER_SOCKET_HOST = "loggerSocketHost";	
	
	/** The Constant XML_ATTRIBUTE_LOGGER_SOCKET_PORT. */
	private static final String XML_ATTRIBUTE_LOGGER_SOCKET_PORT = "loggerSocketPort";	

	/** The Constant XML_ATTRIBUTE_LOGGER_PRIORITY. */
	private static final String XML_ATTRIBUTE_LOGGER_PRIORITY	 = "loggerPriority";	

	/** The Constant XML_ATTRIBUTE_APPLICATIONS_FILENAME. */
	private static final String XML_ATTRIBUTE_APPLICATIONS_FILENAME = "name";	

	/** The Constant XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_COUNT. */
	private static final String XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_COUNT  			= "upAsyncHandlerThreadCount";
	
	/** The Constant XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_COUNT. */
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_COUNT  			= "downAsyncHandlerThreadCount";
	
	/** The Constant XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_POOL_PRIORITY. */
	private static final String XML_ATTRIBUTE_UP_ASYNC_HANDLER_THREAD_POOL_PRIORITY 	= "upAsyncHandlerThreadPoolPriority";
	
	/** The Constant XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_POOL_PRIORITY. */
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_HANDLER_THREAD_POOL_PRIORITY  	= "downAsyncHandlerThreadPoolPriority";
	
	/** The Constant XML_ATTRIBUTE_UP_ASYNC_THREAD_PRIORITY. */
	private static final String XML_ATTRIBUTE_UP_ASYNC_THREAD_PRIORITY 					= "upAsyncThreadPriority";
	
	/** The Constant XML_ATTRIBUTE_DOWN_ASYNC_THREAD_PRIORITY. */
	private static final String XML_ATTRIBUTE_DOWN_ASYNC_THREAD_PRIORITY		 		= "downAsyncThreadPriority";
	
	/** The Constant XML_ATTRIBUTE_UP_SYNC_THREAD_PRIORITY. */
	private static final String XML_ATTRIBUTE_UP_SYNC_THREAD_PRIORITY 					= "upSyncThreadPriority";
	
	/** The Constant XML_ATTRIBUTE_UP_SYNC_REALTIME. */
	private static final String XML_ATTRIBUTE_UP_SYNC_REALTIME 							= "upSyncRealtime";
	
	/** The Constant XML_ATTRIBUTE_MIDDLEWARE. */
	private static final String XML_ATTRIBUTE_MIDDLEWARE 								= "middleware";

	/** The device manager. */
	private ComponentManager deviceManager 	= null;
	
	/** The service manager. */
	private ComponentManager serviceManager = null;
	
	/** The application manager. */
	ApplicationManager applicationManager 	= null;
	
	/** The component descriptors. */
	private HashMap<String, Component> componentDescriptors = null;	
	
	/** The activity factory. */
	private ActivityFactory activityFactory = new DynamicActivityFactory();
	
	/** The fw node list. */
	private NodeList fwNodeList;
	
	/** The doc element. */
	private Element docElement;
	
	/**
	 * Instantiates a new framework manager.
	 *
	 * @param configurationFile the configuration file
	 */
	public FrameworkManager(String configurationFile) {
		deviceManager = new ComponentManager();
		serviceManager = new ComponentManager();		
		componentDescriptors = new HashMap<String, Component>();
		open(configurationFile);
	}		

	/**
	 * Inits the.
	 *
	 * @param activityFactory the activity factory
	 */
	public void init(ActivityFactory activityFactory) {
		this.activityFactory = activityFactory;
		init();
	}
	
	/**
	 * Open.
	 *
	 * @param configurationFile the configuration file
	 */
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
	
	/**
	 * Inits the.
	 */
	public void init() {		
		configure();

		// create the components and their publishers
		createComponents();
	
		// bind the components
		bindComponents();
			
		// create applications
		createApplications();
	}
	
	/**
	 * Sets the system logger.
	 */
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
	
	/**
	 * Sets the system middleware.
	 */
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
				MiddlewareManager.instance().setMiddleware(new MixMiddleware());
			}
		}
		else {
			MiddlewareManager.instance().setMiddleware(new MixMiddleware());
		}
	}

	/**
	 * Configure.
	 */
	private void configure() {
		setSystemLogger();
		setSystemMiddleware();
	}

	/**
	 * Creates the applications.
	 */
	private void createApplications() {
		if (null != applicationManager) {
			applicationManager.createApplications();
		}
	}

	/**
	 * Create the components and their publishers.
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
	 * Bind the components.
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
	 * Create Devices and bind them the connections.
	 *
	 * @param fwElement the fw element
	 * @param componentType the component type
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
	
	/**
	 * Bind components.
	 *
	 * @param fwElement the fw element
	 * @param componentType the component type
	 * @param componentManager the component manager
	 */
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
	
	/**
	 * Creates the manager.
	 *
	 * @param fwElement the fw element
	 */
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
	
	/**
	 * Bind applications.
	 *
	 * @param fwElement the fw element
	 */
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

	/* (non-Javadoc)
	 * @see com.rafael.framework.FrameworkManagerMBean#startDevice(java.lang.String)
	 */
	@Override
	public void startDevice(String deviceClassName){
		deviceManager.startComponent(deviceClassName);
	}


	/* (non-Javadoc)
	 * @see com.rafael.framework.FrameworkManagerMBean#stopDevice(java.lang.String)
	 */
	@Override
	public void stopDevice(String deviceClassName) {		
	}

	/* (non-Javadoc)
	 * @see com.rafael.framework.FrameworkManagerMBean#loadDevice(java.lang.String, int)
	 */
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