package com.wlp.ecm.weap.message;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.document.WeapDocumentType;
import com.wlp.ecm.weap.document.WeapDocument;
import com.wlp.ecm.weap.document.WeapPdfDocument;
import com.wlp.ecm.weap.document.WeapXfdfDocument;
import com.wlp.ecm.weap.exception.WeapException;
public class WeapRequestMessage {
	byte[] messageData = null;
	WeapDocument coverPageDocument = null;
	WeapDocument billingPageDocument = null;
	WeapDocument applicationDocument = null;
	WeapMessageHeader weapMsgHdr = null;
	
	public void setMessageData(byte[] message) {
		LoggingUtil.LogTraceStartMsg();

		messageData = message;
		init();

		LoggingUtil.LogTraceEndMsg();
	}

	public byte[] getMessageData() {
		return messageData;
	}

	public void setBytesMessage(BytesMessage message) throws JMSException, WeapException {
		LoggingUtil.LogTraceStartMsg();

		if (message == null) {
			throw new WeapException("E1015"); 
		}
		
		if (message.getBodyLength() == 0) {
			throw new WeapException("E1016"); 
		}

		messageData = new byte[(int)message.getBodyLength()];
		int byteRead = message.readBytes(messageData);
		if (byteRead == 0) {
			throw new WeapException("E1017"); 
		}
		init();

		LoggingUtil.LogTraceEndMsg();
	}

	public WeapDocument getCoverPageDocument() 
	{
		return coverPageDocument;
	}

	public WeapDocument getBillingPageDocument() 
	{
		return billingPageDocument;
	}

	public WeapDocument getApplicationDocument() 
	{
		return applicationDocument;
	}

	public WeapMessageHeader getWeapMsgHdr() 
	{
		return weapMsgHdr;
	}
	
	protected void init() {
		LoggingUtil.LogTraceStartMsg();

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		WeapConfigSettings config = ctx.getBean(WeapConfigSettings.class);

		//String textMsg = null;
		Charset charset = Charset.forName(config.getMessageCharacterSet());
		LoggingUtil.LogDebugMsg("Characterset used: " + charset.displayName());
		String textMsg = new String(messageData, charset);
		LoggingUtil.LogDebugMsg("Message binary length: " + Integer.toString(messageData.length) + ", string length: " + Integer.toString(textMsg.length()));
		
		if (textMsg.length()!= messageData.length) 
		{
			//something is likely going to go wrong... likely the wrong charset
			LoggingUtil.LogWarningMsg("When converting the binary message to string the converted string length does not match the binary data");
		}
			
		// cover pages never come as PDF
		coverPageDocument = getXfdfDocFromMessage(textMsg, "coverpage");
		
		byte[] billingPagePdfDoc = getPDFFromMessage(messageData, textMsg, "billingpage");
		if (billingPagePdfDoc == null) 
		{
			billingPageDocument = getXfdfDocFromMessage(textMsg, "billingpage");
		} 
		else 
		{
			billingPageDocument = new WeapPdfDocument(billingPagePdfDoc);
		}
		
		byte[] applicationPdfDoc = getPDFFromMessage(messageData, textMsg, "application");
		if (applicationPdfDoc == null) 
		{
			applicationDocument = getXfdfDocFromMessage(textMsg, "application");
		} 
		else 
		{
			applicationDocument = new WeapPdfDocument(applicationPdfDoc);
		}
		
		weapMsgHdr = new WeapMessageHeader();
		
		// Its possible to use SAX or whatever to parse XML but these elements the XML is not valid
		weapMsgHdr.setTranCode(getXmlFromMessage(textMsg, "trancode"));
		weapMsgHdr.setAcn(getXmlFromMessage(textMsg, "acn"));
		weapMsgHdr.setBrand(getXmlFromMessage(textMsg, "brand"));
		weapMsgHdr.setStateCd(getXmlFromMessage(textMsg, "state"));
		weapMsgHdr.setMbu(getXmlFromMessage(textMsg, "MBU"));
		weapMsgHdr.setEid(getXmlFromMessage(textMsg, "eid"));
		weapMsgHdr.setHcid(getXmlFromMessage(textMsg, "HCID"));
		weapMsgHdr.setPartnerId(getXmlFromMessage(textMsg, "PartnerId"));
		weapMsgHdr.setAppType(getXmlFromMessage(textMsg, "apptype"));
		weapMsgHdr.setAppStatus(getXmlFromMessage(textMsg, "appstatus"));
		
		try 
		{
			weapMsgHdr.setReqEffDate( convertToDate(getXmlFromMessage(textMsg, "reqEffectiveDate")) );
		} 
		catch (ParseException e) 
		{
			LoggingUtil.LogWarningMsg("Parse exception occurred converting reqEffectiveDate");
			weapMsgHdr.setReqEffDate(null);
		}

		try 
		{
			weapMsgHdr.setAppRecDate(convertToDate(getXmlFromMessage(textMsg, "appReceivedDate")));
		}
		catch (ParseException e) 
		{
			LoggingUtil.LogWarningMsg("Parse exception occurred converting appReceivedDate");
			weapMsgHdr.setAppRecDate(null);
		}

		weapMsgHdr.setBrokerTin(getXmlFromMessage(textMsg, "brokertin"));
		weapMsgHdr.setApplicantLastName(getXmlFromMessage(textMsg, "ApplicantLName"));
		weapMsgHdr.setApplicantFirstName(getXmlFromMessage(textMsg, "ApplicantFName"));
		weapMsgHdr.setApplicantMi(getXmlFromMessage(textMsg, "ApplicantMI"));
		
		try 
		{
			weapMsgHdr.setApplicantDob(convertToDate(getXmlFromMessage(textMsg, "ApplicantDOB")) );
		} 
		catch (ParseException e) 
		{
			LoggingUtil.LogWarningMsg("Parse exception occurred converting ApplicantDOB");
			weapMsgHdr.setAppRecDate(null);
		}
		
		weapMsgHdr.setApplicantSsn(getXmlFromMessage(textMsg, "ApplicantSSN"));
		weapMsgHdr.setSpouseSsn(getXmlFromMessage(textMsg, "SpouseSSN"));
		weapMsgHdr.setTrackingNumber(getXmlFromMessage(textMsg, "trackingnum"));
		weapMsgHdr.setGeneralAgency(getXmlFromMessage(textMsg, "GeneralAgency"));
		weapMsgHdr.setProductType(getXmlFromMessage(textMsg, "ProductType"));
		weapMsgHdr.setMedicareID(getXmlFromMessage(textMsg, "MedicareID"));
		weapMsgHdr.setFullName(getXmlFromMessage(textMsg, "ApplicantLName") + " " + getXmlFromMessage(textMsg, "ApplicantFName"));
		weapMsgHdr.setAltSysId(getXmlFromMessage(textMsg, "MBU") + "_" + getXmlFromMessage(textMsg, "state"));
		
		LoggingUtil.LogTraceEndMsg();
	}
	
	public WeapDocumentType getCoverPageDocType() {
		if (coverPageDocument != null) {
			return coverPageDocument.getDocumentType();
		} else {
			return WeapDocumentType.UNKNOWN;
		}
	}

	public WeapDocumentType getBillingPageDocType() {
		if (billingPageDocument != null) {
			return billingPageDocument.getDocumentType();
		} else {
			return WeapDocumentType.UNKNOWN;
		}
	}

	public WeapDocumentType getApplicationDocType() {
		if (applicationDocument != null) {
			return applicationDocument.getDocumentType();
		} else {
			return WeapDocumentType.UNKNOWN;
		}
	}
	
	protected String getXmlFromMessage(String msg, String element) {
		LoggingUtil.LogTraceStartMsg();

		String startTag = "<" + element + ">";
		String endTag = "</" + element + ">";
		String xml = "";
		
		int startPos = msg.indexOf(startTag);
		if (startPos >= 0) {
			int endPos = msg.indexOf(endTag);
			if (endPos >= 0) {
				xml = msg.substring(startPos+startTag.length(), endPos);
			}
		}
		
		LoggingUtil.LogTraceEndMsg();
		return xml;
	}

	protected WeapXfdfDocument getXfdfDocFromMessage(String msg, String element) {
		LoggingUtil.LogTraceStartMsg();

		String startTag = "<" + element + " type=\"XFDF\">";
		String endTag = "</" + element + ">";
		WeapXfdfDocument xfdfDoc = null;

		int startPos = msg.indexOf(startTag);
		String xml = "";
		if (startPos >= 0) {
			int endPos = msg.indexOf(endTag);
			if (endPos >= 0) {
				xml = msg.substring(startPos+startTag.length(), endPos);
				xfdfDoc = new WeapXfdfDocument(xml); 
			}
		}

		LoggingUtil.LogTraceEndMsg();
		return xfdfDoc;
	}

	protected byte[] getPDFFromMessage(byte[] binaryMsg, String textMsg, String element) {
		LoggingUtil.LogTraceStartMsg();

		String startTag = "<" + element + " type=\"PDF\">";
		String endTag = "</" + element + ">";
		
		byte[] binary = null;
		
		int startPos = textMsg.indexOf(startTag);
		if (startPos >= 0) {
			int endPos = textMsg.indexOf(endTag);
			startPos += startTag.length();
			if (endPos >= 0 && endPos > startPos) {
				binary = new byte[endPos-startPos];
				System.arraycopy(binaryMsg, startPos, binary, 0, endPos-startPos);
			}
		}

		LoggingUtil.LogTraceEndMsg();
		return binary;
	}

	protected Date convertToDate(String str) throws ParseException {
		LoggingUtil.LogTraceStartMsg();

		Date date = null;
		if (str != null && str.length() > 0) {
			date = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(str);
		}
		LoggingUtil.LogTraceEndMsg();
		return date;
	}
   
}
