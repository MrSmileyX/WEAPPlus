package com.wlp.ecm.weap.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class ApplicationElement {
	@XmlElement(name="PRIMARYAPPLICANT", required=true, type=PrimaryApplicantElement.class)
	private PrimaryApplicantElement primaryApplicant = null;

	public void setPrimaryApplicant(PrimaryApplicantElement primaryApplicant) {
		this.primaryApplicant = primaryApplicant;
	}
	public PrimaryApplicantElement getPrimaryApplicant() {
		return primaryApplicant;
	}
}
