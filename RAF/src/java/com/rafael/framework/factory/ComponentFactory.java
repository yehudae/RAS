package com.rafael.framework.factory;

import java.util.Map;
import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Component objects.
 */
public abstract class ComponentFactory {
	
	/**
	 * Creates a new Component object.
	 *
	 * @param container the container
	 */
	public abstract void createComponents(Map<String, Component> container);
}
