package com.wlp.ecm.weap.data;

import junit.framework.Assert;

import org.junit.Test;

import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.message.WeapMessageHeader;

public class WeapMetadataTest {

	@Test
	public void testCreateMetadataEast() throws WeapException {
		WeapMessageHeader msgHeader = new WeapMessageHeader();
		msgHeader.setAcn("TESTACN");
		msgHeader.setAppStatus("X");
		msgHeader.setApplicantFirstName("firstname");
		msgHeader.setApplicantLastName("lastname");
		msgHeader.setStateCd("CT");
		
		WeapMetadata metadata = WeapMetadata.createMetadata(msgHeader);
		Assert.assertTrue("WeapMetadataEast class is correct", metadata instanceof WeapMetadataEast );
	}
	
	@Test
	public void testCreateMetadataWest() throws WeapException {
		WeapMessageHeader msgHeader = new WeapMessageHeader();
		msgHeader.setAcn("TESTACN");
		msgHeader.setAppStatus("X");
		msgHeader.setApplicantFirstName("firstname");
		msgHeader.setApplicantLastName("lastname");
		msgHeader.setStateCd("AZ");
		msgHeader.setBrand("BCC");
		
		WeapMetadata metadata = WeapMetadata.createMetadata(msgHeader);
		Assert.assertTrue("WeapMetadataWest class is correct", metadata instanceof WeapMetadataWest );
	}

	@Test
	public void testCreateMetadataCentral() throws WeapException {
		WeapMessageHeader msgHeader = new WeapMessageHeader();
		msgHeader.setAcn("TESTACN");
		msgHeader.setAppStatus("X");
		msgHeader.setApplicantFirstName("firstname");
		msgHeader.setApplicantLastName("lastname");
		msgHeader.setStateCd("AZ");
		msgHeader.setBrand("ANTHEM");
		
		WeapMetadata metadata = WeapMetadata.createMetadata(msgHeader);
		Assert.assertTrue("WeapMetadataCentral class is correct", metadata instanceof WeapMetadataCentral );
	}
}
