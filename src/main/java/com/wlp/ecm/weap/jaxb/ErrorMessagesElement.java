package com.wlp.ecm.weap.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class ErrorMessagesElement {
	@XmlElement(name="ERRORMESSAGE", required=true)
	private String[] errorMessages;
	
	public void setErrorMessages(String[] errorMessages) {
		this.errorMessages = errorMessages;
	}
	public String[] getErrorMessages() {
		return errorMessages;
	}

}
