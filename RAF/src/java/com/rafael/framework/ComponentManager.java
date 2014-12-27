package com.rafael.framework;

import java.util.HashMap;
import java.util.Map;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class ComponentManager.
 */
public class ComponentManager{
	
	/** The components. */
	private Map<String, Component> components;

	/**
	 * Instantiates a new component manager.
	 */
	public ComponentManager() {
		components = new HashMap<String, Component>();
	}	
	
	/**
	 * Gets the component by class name.
	 *
	 * @param className the class name
	 * @return the component by class name
	 */
	private Component getComponentByClassName(String className) {		
		if ( components.containsKey(className) ) {
			return components.get(className); 
		}
		
		return null;
	}
	
	/**
	 * Start component.
	 *
	 * @param className the class name
	 */
	public void startComponent(String className) {
		Component component = getComponentByClassName(className);
		if(component != null) {
			component.start();
		}
	}
	
	/**
	 * Creates the component.
	 *
	 * @param className the class name
	 * @return the component
	 */
	public Component createComponent(String className) {
		Component component = getComponentByClassName(className);

		// check if the component was already added to the of devices, do not add it again
		if(null == component) {
			try {
				Class<?> appClass = Class.forName(className);
				component = (Component) appClass.newInstance();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return component;
	}

	/**
	 * Adds the component.
	 *
	 * @param className the class name
	 * @param component the component
	 */
	public void addComponent(String className, Component component) {
		components.put(className, component);
	}
}