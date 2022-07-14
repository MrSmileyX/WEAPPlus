package com.wlp.ecm.weap.config;

import org.w3c.dom.Element;

import com.wlp.ecm.weap.common.LoggingUtil;

public class WeapProperty {
	private String symbolicName = null;
	private String weapPropertyName = null;
	private WeapPropertyDataType dataType = null;

	public WeapProperty(Element propertyElement) {
		LoggingUtil.LogTraceStartMsg();

		symbolicName = propertyElement.getAttribute("symbolic-name");
		weapPropertyName = propertyElement.getAttribute("weap-property");
		String dataTypeStr = propertyElement.getAttribute("data-type"); 
		
		if (dataTypeStr.equalsIgnoreCase("string")) 
		{
			dataType = WeapPropertyDataType.STRING; 
		}
		else if (dataTypeStr.equalsIgnoreCase("date")) 
		{
			dataType = WeapPropertyDataType.DATE;
		}
		else if (dataTypeStr.equalsIgnoreCase("time")) 
		{
			dataType = WeapPropertyDataType.TIME;
		} 
		else if (dataTypeStr.equalsIgnoreCase("id")) 
		{
			dataType = WeapPropertyDataType.ID;
		}
		else if (dataTypeStr.equalsIgnoreCase("double")) 
		{
			dataType = WeapPropertyDataType.DOUBLE; 
		}
		else if (dataTypeStr.equalsIgnoreCase("integer")) 
		{
			dataType = WeapPropertyDataType.INTEGER;
		}
		else if (dataTypeStr.equalsIgnoreCase("boolean")) 
		{
			dataType = WeapPropertyDataType.BOOLEAN;
		}
		else if (dataTypeStr.equalsIgnoreCase("long")) 
		{
			dataType = WeapPropertyDataType.LONG;
		}
		else if (dataTypeStr.equalsIgnoreCase("short")) 
		{
			dataType = WeapPropertyDataType.SHORT;
		}
		else if (dataTypeStr.equalsIgnoreCase("byte")) 
		{
			dataType = WeapPropertyDataType.BYTE;
		}
		else 
		{
			dataType = WeapPropertyDataType.UNKNOWN;
		}

		LoggingUtil.LogTraceEndMsg();
	}
	
	public String getSymbolicName() {
		return symbolicName;
	}
	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}
	public String getWeapPropertyName() {
		return weapPropertyName;
	}
	public void setWeapPropertyName(String weapProperty) {
		this.weapPropertyName = weapProperty;
	}
	public WeapPropertyDataType getDataType() {
		return dataType;
	}
	public void setDataType(WeapPropertyDataType dataType) {
		this.dataType = dataType;
	}
}
