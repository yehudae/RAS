package com.rafael.framework;

import java.util.HashMap;
import java.util.Map;

import com.rafael.sdk.component.Component;

public class ComponentManager{
	
	private Map<String, Component> components;

	public ComponentManager() {
		components = new HashMap<String, Component>();
	}	
	
	private Component getComponentByClassName(String className) {		
		if ( components.containsKey(className) ) {
			return components.get(className); 
		}
		
		return null;
	}
	
	public void startComponent(String className) {
		Component component = getComponentByClassName(className);
		if(component != null) {
			component.start();
		}
	}
	
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

	public void addComponent(String className, Component component) {
		components.put(className, component);
	}
}