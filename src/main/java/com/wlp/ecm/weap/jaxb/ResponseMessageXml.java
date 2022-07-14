package com.wlp.ecm.weap.jaxb;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.data.WeapMetadata;

public class ResponseMessageXml {
	
	public static String createSuccessResponse(WeapMetadata metadata) throws JAXBException {
		LoggingUtil.LogTraceStartMsg();
		
		WeapResponseMessage reponseMessage = new WeapResponseMessage();
		AcnElement acn = new AcnElement();
		acn.setSource("IMAGING");
		acn.setAcnValue(metadata.getAcn());
		reponseMessage.setAcn(acn);
		reponseMessage.setApplicationStatus(metadata.getApplicationStatus());
		reponseMessage.setWorkLoc("");
		ApplicationElement application = new ApplicationElement();
		PrimaryApplicantElement primaryApplicant = new PrimaryApplicantElement();
		primaryApplicant.setFirstName(metadata.getApplicantFirstName());
		primaryApplicant.setLastName(metadata.getApplicantLastName());
		primaryApplicant.setMiddleInitital("");
		application.setPrimaryApplicant(primaryApplicant);
		reponseMessage.setApplication(application);
		
		String xml = generateXml(reponseMessage);
		
		LoggingUtil.LogTraceEndMsg();
		return xml;
	}

	public static String createFailedResponse(WeapMetadata metadata, String error) throws JAXBException {
		LoggingUtil.LogTraceStartMsg();

		WeapResponseMessage reponseMessage = new WeapResponseMessage();
		AcnElement acn = new AcnElement();
		acn.setSource("IMAGING");
		acn.setAcnValue(metadata.getAcn());
		reponseMessage.setAcn(acn);
		reponseMessage.setApplicationStatus(metadata.getApplicationStatus());
		reponseMessage.setWorkLoc("");
		ApplicationElement application = new ApplicationElement();
		PrimaryApplicantElement primaryApplicant = new PrimaryApplicantElement();
		primaryApplicant.setFirstName(metadata.getApplicantFirstName());
		primaryApplicant.setLastName(metadata.getApplicantLastName());
		primaryApplicant.setMiddleInitital("");
		application.setPrimaryApplicant(primaryApplicant);
		reponseMessage.setApplication(application);
		
		ErrorMessagesElement errorMessages = new ErrorMessagesElement();
		errorMessages.setErrorMessages(new String[] {error});
		reponseMessage.setErrorMessages(errorMessages);
		
		String xml = generateXml(reponseMessage);
		
		LoggingUtil.LogTraceEndMsg();
		return xml;
	}

	public static String createFailedResponse(WeapMetadata metadata, String[] errors) throws JAXBException {
		LoggingUtil.LogTraceStartMsg();

		WeapResponseMessage reponseMessage = new WeapResponseMessage();
		AcnElement acn = new AcnElement();
		acn.setSource("IMAGING");
		acn.setAcnValue(metadata.getAcn());
		reponseMessage.setAcn(acn);
		reponseMessage.setApplicationStatus(metadata.getApplicationStatus());
		reponseMessage.setWorkLoc("");
		ApplicationElement application = new ApplicationElement();
		PrimaryApplicantElement primaryApplicant = new PrimaryApplicantElement();
		primaryApplicant.setFirstName(metadata.getApplicantFirstName());
		primaryApplicant.setLastName(metadata.getApplicantLastName());
		primaryApplicant.setMiddleInitital("");
		application.setPrimaryApplicant(primaryApplicant);
		reponseMessage.setApplication(application);
		
		ErrorMessagesElement errorMessages = new ErrorMessagesElement();
		errorMessages.setErrorMessages(errors);
		reponseMessage.setErrorMessages(errorMessages);
		
		String xml = generateXml(reponseMessage);
		
		LoggingUtil.LogTraceEndMsg();
		return xml;
	}
	
	protected static String generateXml(WeapResponseMessage reponseMessage) throws JAXBException{
		LoggingUtil.LogTraceStartMsg();

		JAXBContext jaxbContext = JAXBContext.newInstance(WeapResponseMessage.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		jaxbMarshaller.marshal(reponseMessage, baos);
		String xml = new String(baos.toByteArray(), Charset.defaultCharset());

		LoggingUtil.LogTraceEndMsg();
		return xml;
	}
}
