package com.wlp.ecm.weap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.data.WeapCEConnector;
import com.wlp.ecm.weap.data.WeapMetadata;
import com.wlp.ecm.weap.data.WeapRepository;
import com.wlp.ecm.weap.document.WeapDocument;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.message.WeapRequestMessage;
import com.wlp.ecm.weap.reporting.Database;
import com.wlp.ecm.weap.reporting.MessageData;
import com.wlp.ecm.weap.reporting.Reporting;

public class WeapMessageProcessor 
{
	@Autowired
	WeapConfigSettings config;
	
	public void processMessage(BytesMessage msg, Queue responseQueue, QueueConnectionFactory responseQcf, boolean inMain) throws WeapException, JMSException, JAXBException 
	{
		String repoType = "";
		String appDocId = "0";
		String covPgDocId = "0";
		String messageID = msg.getJMSMessageID();
		String messageTS = "";
		
		WriteLog.WriteStatus("Processing message...");
		
		System.setProperty("org.jpedal.jai", "true"); 
		
		Date ts = new Date(msg.getJMSTimestamp());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS aa (z)");
		messageTS = df.format(ts);
		
		LoggingUtil.LogDebugMsg("Received message, MsgID: " + messageID + ", Msg TimeStamp: " + messageTS);
		
		WriteLog.WriteStatus("**************************************************");
		WriteLog.WriteStatus("Received message.");
		WriteLog.WriteStatus("");
		WriteLog.WriteStatus("Message ID   : " + messageID);
		WriteLog.WriteStatus("Msg TimeStamp: " + messageTS);
		WriteLog.WriteStatus("");
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();		
		
		WeapRequestMessage weapMsg = new WeapRequestMessage();
		weapMsg.setBytesMessage((BytesMessage)msg);

		String filePath = config.getDebugDataDirectory() + weapMsg.getWeapMsgHdr().getAcn() + ".msgdat";
		String bkupName = weapMsg.getWeapMsgHdr().getAcn() + ".msgdat";
		LoggingUtil.LogDebugMsg(weapMsg.getMessageData(), filePath);
		LoggingUtil.LogDebugMsg(weapMsg.getWeapMsgHdr());
		
		WeapDocument applicationDoc = weapMsg.getApplicationDocument();
		WeapDocument coverPageDoc = weapMsg.getCoverPageDocument();
		
		WeapMetadata metadata = WeapMetadata.createMetadata(weapMsg.getWeapMsgHdr());
		MessageData currMsg = new MessageData(metadata, messageID);
		
		currMsg.setMsgTimeStamp(messageTS);
		currMsg.setBkupFileNm(bkupName);
		
		WriteLog.WriteStatus("Current Message Info:");
		WriteLog.WriteStatus("");
		WriteLog.WriteStatus("Member Number:       " + metadata.getMemberNumber());
		WriteLog.WriteStatus("Member Name:         " + metadata.getApplicantFirstName() + " " + metadata.getApplicantMiddleInitial() + " " + metadata.getApplicantLastName());
		WriteLog.WriteStatus("DCN:                 " + metadata.getDcn());
		WriteLog.WriteStatus("ACN:                 " + metadata.getAcn());
		WriteLog.WriteStatus("Batch ID:            " + metadata.getBatchId());
		WriteLog.WriteStatus("MBU:                 " + metadata.getMbu());
		WriteLog.WriteStatus("Brand:               " + metadata.getBrand());
		WriteLog.WriteStatus("EID:                 " + metadata.getEid());
		WriteLog.WriteStatus("HCID:                " + metadata.getHcid());
		WriteLog.WriteStatus("State:               " + metadata.getStateCd());
		WriteLog.WriteStatus("Region:              " + metadata.getWeapRegion());
		WriteLog.WriteStatus("SYSID:               " + metadata.getAltSysId());
		WriteLog.WriteStatus("Tran Code:           " + metadata.getTranCode());
		WriteLog.WriteStatus("Full Name:           " + metadata.getFullName());
		WriteLog.WriteStatus("Medicare ID:         " + metadata.getMedicareID());
		WriteLog.WriteStatus("Partner ID:          " + metadata.getPartnerId());
		WriteLog.WriteStatus("Product Code:        " + metadata.getProductType());
		WriteLog.WriteStatus("Document Class:      " + metadata.getDocumentClass());
		WriteLog.WriteStatus("Repository:          " + metadata.getRepository());
		
		WriteLog.WriteToLog("Initializing committal steps..." + repoType);
		
		String newMBU = metadata.getMbu();
		WriteLog.WriteToLog("MBU:                  " + newMBU);
	
		repoType = metadata.getRepository();
		WriteLog.WriteToLog("Repository Type:      " + repoType);
		
		String docClass = metadata.getDocumentClass();
		WriteLog.WriteToLog("Document Class:       " + docClass);
		
		currMsg.setRepoName(repoType);
		currMsg.setDocClass(docClass);
		
		WriteLog.WriteToLog("Initializing " + repoType +  " repository...");
		
		WeapRepository weapRepo = (WeapRepository) ctx.getBean("WeapRepository", repoType);
		
		WriteLog.WriteStatus("");
		WriteLog.WriteStatus("Documents for ACN: " + metadata.getAcn());
		WriteLog.WriteStatus("");

		if (applicationDoc == null)
		{
			throw new WeapException("E1019");
		}
		else 
		{
			WriteLog.WriteStatus("Committing Application...");
			
			appDocId = weapRepo.storeDocument(applicationDoc, metadata, 1, currMsg, "APP");
			
			WriteLog.WriteStatus("*************************************************************************");
			WriteLog.WriteStatus("* " + metadata.getAcn() + " -  Application:     " + appDocId);
			WriteLog.WriteStatus("*************************************************************************");
		}
				
		if (coverPageDoc != null) 
		{
			WriteLog.WriteStatus("Committing Cover Page...");
			
			if (newMBU.equalsIgnoreCase("MSMEDSYS"))
			{
				WriteLog.WriteToLog("Setting coverpage application type to COVERPAGE for MSMEDSYS...");
				metadata.setApplicationType("COVERPAGE");
								
				String prodType = metadata.getProductType();
				
				if (prodType.equalsIgnoreCase("ANTHEXTR"))
				{
					WriteLog.WriteToLog("Clearing coverpage product type for Anthem Extra application...");
					metadata.setProductType("");
				}
				
				covPgDocId = weapRepo.storeDocument(coverPageDoc, metadata, 1, currMsg, "COV");
				WriteLog.WriteStatus("*************************************************************************");
				WriteLog.WriteStatus("* " + metadata.getAcn() + " -  Cover Page:      " + covPgDocId);
				WriteLog.WriteStatus("*************************************************************************");
			}
			else
			{
				covPgDocId = weapRepo.storeDocument(coverPageDoc, metadata, 1, currMsg, "COV");
				WriteLog.WriteStatus("*************************************************************************");
				WriteLog.WriteStatus("* " + metadata.getAcn() + " -  Cover Page:      " + covPgDocId);	
				WriteLog.WriteStatus("*************************************************************************");
			}
			 
		}
		else
		{
			WriteLog.WriteStatus("No Cover Page found.");	
		}
		
		WriteLog.WriteStatus("");
		WriteLog.WriteStatus("**************************************************");
		WriteLog.WriteStatus("");
		
		WriteLog.writeReport(metadata);
		
		WeapJmsResponder responder = new WeapJmsResponder();
	
		if (responseQcf != null) 
		{
			responder.setQueueConnectionFactory(responseQcf);
			responder.setResponseQueue(responseQueue);
			
			if (metadata != null) 
			{
				String sentTo = responder.SendSuccessResponse(metadata);
				LoggingUtil.LogInfoMsg("Response for " + metadata.getAcn() + " send to " + sentTo + ".");
			}
			
			Logger logger = Logger.getLogger(LoggingUtil.class);
			
			if (logger.isDebugEnabled()) 
			{
				try 
				{
					responder.WriteDebugResponse(metadata, config.getDebugDataDirectory());
				}
				catch (JAXBException e) 
				{
					LoggingUtil.LogWarningMsg("WARN: Could not log the response!");
				}
			}
		} 
		else 
		{
			LoggingUtil.LogWarningMsg("Reply message not sent for " + metadata.getAcn() + ", no ReplyTo is defined.");
		}

		Date pts = new Date(msg.getJMSTimestamp());
		DateFormat pdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS aa (z)");
		String procTS = pdf.format(pts);
		
		currMsg.setProcTimeStamp(procTS);
		
		LoggingUtil.LogDebugMsg("Finished processing message, ID: " + messageID + ", TimeStamp: " + procTS);
		String logInfo =  String.format("WEAP message completed, DCN: %s, ACN: %s, batchId: %s, state: %s, region: %s", metadata.getDcn(), metadata.getAcn(), metadata.getBatchId(), metadata.getStateCd(), metadata.getWeapRegion().toString());
		
		LoggingUtil.LogInfoMsg(logInfo);
		Reporting.logMessage(currMsg);
				
		if (inMain)
		{
			WeapMessageRetry.checkForRetries(responseQcf, responseQueue);
		}
	}
}
