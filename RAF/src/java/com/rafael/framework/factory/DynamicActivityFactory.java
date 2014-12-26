package com.rafael.framework.factory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rafael.sdk.activity.Activity;
import com.rafael.sdk.component.Component;

public class DynamicActivityFactory  extends ActivityFactory {

	private static final String XML_ELEMENT_ACTIVITY = "Activity";
	private static final String XML_ATTRIBUTE_CLASS_NAME = "className";
	private static final String XML_ATTRIBUTE_TOPIC = "topic";
	private static final String XML_ATTRIBUTE_TYPE = "type";
	private static final String XML_ATTRIBUTE_DIR = "dir"; 
	private static final String XML_ATTRIBUTE_PRIORITY = "priority"; 
	private static final String XML_ATTRIBUTE_UP = "up";
	private static final String XML_ATTRIBUTE_DOWN = "down";
	private static final String XML_ATTRIBUTE_ASYNC = "async";
	private static final String XML_ATTRIBUTE_SYNC = "sync";
				
	@Override
	public void createActivities(Component component) {
		try	{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			String name = component.getClass().getSimpleName() + ".xml";
			Document dom = db.parse(component.getClass().getClassLoader().getResourceAsStream(name));
			// get the document element
			Element docElement = dom.getDocumentElement();
			
			// get the list of elements of the file 
			NodeList activities = docElement.getChildNodes();
			
			// create all the activities and attach them to the component
			createActivities(activities, component);
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}

	private void createActivities(NodeList activities, Component component) {
		for (int index = 0; index < activities.getLength(); index++) {
			Node compElement = activities.item(index);
			if (XML_ELEMENT_ACTIVITY.equals(compElement.getNodeName())) {
				Element activityElement = (Element)compElement;
							
				String className = activityElement.getAttribute(XML_ATTRIBUTE_CLASS_NAME);
				String type = activityElement.getAttribute(XML_ATTRIBUTE_TYPE);
				String dir = activityElement.getAttribute(XML_ATTRIBUTE_DIR);
				String topic = activityElement.getAttribute(XML_ATTRIBUTE_TOPIC); 
				String priority = activityElement.getAttribute(XML_ATTRIBUTE_PRIORITY); 
							
				Activity activity = createActivity(className);
				
				
				if (priority.isEmpty()) {
					//TODO
				}
				else {
					activity.setPriority(Integer.valueOf(priority));
				}
				
				if (topic.isEmpty()) {
					topic = activity.getClass().getSimpleName();
				}
							
				if (XML_ATTRIBUTE_ASYNC.equals(type)) { 
					if (XML_ATTRIBUTE_UP.equals(dir)){
						component.putUpAsyncActivity(topic, activity, true);
					}
					else if (XML_ATTRIBUTE_DOWN.equals(dir)){
						component.putDownAsyncActivity(topic, activity, true);
					}
				}
				else if (XML_ATTRIBUTE_SYNC.equals(type)){
					component.putSyncActivity(topic, activity, true);
				}
			}
		}
	}

	private Activity createActivity(String className) {
		
		Class<?> appClass = null;
		Activity activity = null;
		try {
			appClass = Class.forName(className);
			activity = (Activity) appClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return activity;
	}
}
