package com.wlp.ecm.weap.config;

import java.util.Hashtable;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wlp.ecm.weap.common.LoggingUtil;

public class WeapDocumentClass {
	private String name = null;
	private Map<String, WeapProperty> properties = null;
	
	public WeapDocumentClass(Element classElement) {
		LoggingUtil.LogTraceStartMsg();

		NodeList propertyNodes = classElement.getChildNodes();
		properties = new Hashtable<String, WeapProperty>();
		name = classElement.getAttribute("name");
		for (int iter = 0; iter < propertyNodes.getLength(); iter ++){
			Node propertyNode = propertyNodes.item(iter);
			if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
				WeapProperty property = new WeapProperty((Element) propertyNode);
				properties.put(property.getSymbolicName(), property);
			}
		}

		LoggingUtil.LogTraceEndMsg();
	}

	public String getName() {
		return name;
	}

	public Map<String, WeapProperty> getProperties() {
		return properties;
	}

}
