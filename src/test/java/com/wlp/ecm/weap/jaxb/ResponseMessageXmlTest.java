package com.wlp.ecm.weap.jaxb;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;

import com.wlp.ecm.weap.data.WeapMetadata;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.message.WeapMessageHeader;

public class ResponseMessageXmlTest {

	@Test
	public void testCreateSuccessResponse() throws WeapException {
		
		String response = null;
		
		WeapMessageHeader msgHeader = new WeapMessageHeader();
		msgHeader.setAcn("TESTACN");
		msgHeader.setAppStatus("X");
		msgHeader.setApplicantFirstName("firstname");
		msgHeader.setApplicantLastName("lastname");
		
		WeapMetadata metadata = WeapMetadata.createMetadata(msgHeader);
		
		try {
			response = ResponseMessageXml.createSuccessResponse(metadata);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull("Success response message is not null", response);
	}
	
	@Test
	public void testCreateFailedResponse() throws WeapException {
		
		String response = null;
		
		WeapMessageHeader msgHeader = new WeapMessageHeader();
		msgHeader.setAcn("TESTACN");
		msgHeader.setAppStatus("X");
		msgHeader.setApplicantFirstName("firstname");
		msgHeader.setApplicantLastName("lastname");
		
		WeapMetadata metadata = WeapMetadata.createMetadata(msgHeader);
		
		try {
			response = ResponseMessageXml.createFailedResponse(metadata, "SAMPLE FAILURE MESSAGE");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		Assert.assertNotNull("Failed response message is not null", response);
	}
}
