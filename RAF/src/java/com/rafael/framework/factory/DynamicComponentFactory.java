package com.rafael.framework.factory;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating DynamicComponent objects.
 */
public class DynamicComponentFactory  extends ComponentFactory{

	/** The Constant XML_CONFIGURATION_FILE. */
	private static final String XML_CONFIGURATION_FILE = "framework.xml";
	
	/** The Constant XML_ELEMENT_COMPONENTS. */
	private static final String XML_ELEMENT_COMPONENTS = "Components";	
	
	/** The Constant XML_ELEMENT_COMPONENT. */
	private static final String XML_ELEMENT_COMPONENT = "Component";
	
	/** The Constant XML_ELEMENT_CLASS_NAME. */
	private static final String XML_ELEMENT_CLASS_NAME = "className";
	
	/** The components map. */
	private Map<String, Component> componentsMap = null;
	
	/* (non-Javadoc)
	 * @see com.rafael.framework.factory.ComponentFactory#createComponents(java.util.Map)
	 */
	@Override
	public void createComponents(Map<String, Component> componentsMap) {
		try	{
			this.componentsMap = componentsMap;

			// loop over the sysConfig.xml file and create the components
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(XML_CONFIGURATION_FILE);
			
			// get the document element
			Element docElement = dom.getDocumentElement();
				
			// get the list of elements of the file 
			NodeList fwNodeList = docElement.getChildNodes();
				
			// create the components
			createComponents(fwNodeList);
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new DynamicComponent object.
	 *
	 * @param fwNodeList the fw node list
	 */
	private void createComponents(NodeList fwNodeList) {
		if((fwNodeList != null) && (fwNodeList.getLength() > 0)){
			for (int i = 0; i < fwNodeList.getLength(); i++){			
				Node fwElement = fwNodeList.item(i);
				
				if(true == fwElement.getNodeName().equals(XML_ELEMENT_COMPONENTS))	{
					NodeList componentsNodeList = fwElement.getChildNodes();
					
					for (int index = 0; index < componentsNodeList.getLength(); index++)	{			
						Node compElement = componentsNodeList.item(index);
						
						// a component was found
						if (XML_ELEMENT_COMPONENT.equals(compElement.getNodeName())){
							Element componentElement = (Element)compElement;
							String className = componentElement.getAttribute(XML_ELEMENT_CLASS_NAME);
							
							// create the component with its threads
							createComponent(className);
						}
					}
				}
			}
		}
	}

	/**
	 * Creates a new DynamicComponent object.
	 *
	 * @param className the class name
	 */
	private void createComponent(String className) {
		// check if the component was already created
		if (!componentsMap.containsKey(className)){
			try{
				Class<?> appClass = Class.forName(className);
				Component component = (Component) appClass.newInstance();
				
				// add the new component to the map of components
				componentsMap.put(className, component);
			} 
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
