package com.wlp.ecm.weap.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class PrimaryApplicantElement {
	@XmlElement(name="FIRST", required=true)
	private String firstName = null;

	@XmlElement(name="LAST", required=true)
	private String lastName = null;

	@XmlElement(name="MI", required=true)
	private String middleInitital = null;
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setMiddleInitital(String middleInitital) {
		this.middleInitital = middleInitital;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getMiddleInitital() {
		return middleInitital;
	}

}
