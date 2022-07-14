package com.wlp.ecm.weap.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.NONE)
public class AcnElement {
	@XmlAttribute(name="source", required=true)
	private String source = null;
	
	@XmlValue
	private String acnValue = null;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAcnValue() {
		return acnValue;
	}

	public void setAcnValue(String acnValue) {
		this.acnValue = acnValue;
	}
}
