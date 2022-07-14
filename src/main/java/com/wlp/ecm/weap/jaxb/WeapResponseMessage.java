package com.wlp.ecm.weap.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * Used for producing XML.  JAXB is used for mashalling java objects to XML
 * Below is the expected xml layout that this class is marshalled to.
 *   
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <INDVENROLLMENTUNDERWRITINGVALIDATION>
 *     <ACN source="IMAGING">ACN</ACN>
 *     <APPLICATIONSTATUS>I</APPLICATIONSTATUS>
 *     <WORKLOC></WORKLOC>
 *     <APPLICATION>
 *         <PRIMARYAPPLICANT>
 *             <FIRST>FIRSTNAME</FIRST>
 *             <LAST>LASTNAME</LAST>
 *             <MI></MI>
 *         </PRIMARYAPPLICANT>
 *     </APPLICATION>
 *     <UWERRORMESSAGES>
 *         <ERRORMESSAGE></ERRORMESSAGE>
 *     </UWERRORMESSAGES>
 * </INDVENROLLMENTUNDERWRITINGVALIDATION>
 * 
*/

@XmlRootElement(name="INDVENROLLMENTUNDERWRITINGVALIDATION")
@XmlAccessorType(XmlAccessType.NONE)
public class WeapResponseMessage {

	@XmlElement(name="ACN", required=true, defaultValue="IMAGING")
	private AcnElement acn = null;
	
	@XmlElement(name="APPLICATIONSTATUS", required=true, defaultValue="A")
	private String applicationStatus = null;

	@XmlElement(name="WORKLOC", required=true)
	private String workLoc = null;;

	@XmlElement(name="APPLICATION", required=true, type=ApplicationElement.class)
	private ApplicationElement application = null;

	@XmlElement(name="UWERRORMESSAGES", required=true, type=ErrorMessagesElement.class, nillable=false)
	private ErrorMessagesElement errorMessages = null;

	public void setAcn(AcnElement acn) {
		this.acn = acn;
	}
	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	public void setWorkLoc(String workLoc) {
		this.workLoc = workLoc;
	}
	public void setApplication(ApplicationElement application) {
		this.application = application;
	}
	public void setErrorMessages(ErrorMessagesElement errorMessages) {
		this.errorMessages = errorMessages;
	}
	public AcnElement getAcn() {
		return acn;
	}
	public String getApplicationStatus() {
		return applicationStatus;
	}
	public String getWorkLoc() {
		return workLoc;
	}
	public ApplicationElement getApplication() {
		return application;
	}
	public ErrorMessagesElement getErrorMessages() {
		return errorMessages;
	}
}
