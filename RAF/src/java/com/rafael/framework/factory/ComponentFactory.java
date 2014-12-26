package com.rafael.framework.factory;

import java.util.Map;
import com.rafael.sdk.component.Component;

public abstract class ComponentFactory {
	public abstract void createComponents(Map<String, Component> container);
}
