package com.wlp.ecm.weap.data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.DocumentClassPropertyMapping;
import com.wlp.ecm.weap.config.UserCredentials;
import com.wlp.ecm.weap.config.CESettings;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.config.WeapDocumentClass;
import com.wlp.ecm.weap.config.WeapProperty;
import com.wlp.ecm.weap.document.WeapDocument;
import com.wlp.ecm.weap.document.WeapDocumentType;
import com.wlp.ecm.weap.document.WeapImagePage;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.reporting.MessageData;

public class WeapRepositoryCE extends WeapRepository 
{

	@Override
	@SuppressWarnings("unchecked")
	public String storeDocument(WeapDocument weapDocument, WeapMetadata metadata, int version, 
								MessageData msgData, String docSection) throws WeapException 
	{
		String docId = "";
		String docPageCnt = "";
		String docFmt = "";
		String docStat = ""; 
		String docMsg = "";
		
		WeapCEConnector wc = new WeapCEConnector();
		
		int pageCnt = 0;
		LoggingUtil.LogTraceStartMsg();

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		
		WeapConfigSettings config = ctx.getBean(WeapConfigSettings.class);
		CESettings ceConfig = ctx.getBean(CESettings.class);
		
		DocumentClassPropertyMapping classMapping = (DocumentClassPropertyMapping) ctx.getBean("documentClassPropertyMapping", "CE");
		//UserCredentials creds = ctx.getBean("contentEngineUserCredentials", UserCredentials.class);

		metadata.setRepository("CE");
		
		if (version == 2)
		{
			WriteLog.WriteStatus("Storing Document in CE 5.2");
			msgData.setRepoName("CE 5.2");
		}
		else
		{
			WriteLog.WriteStatus("Storing Document in CE 5.1");
			msgData.setRepoName("CE 5.1");
		}
				
		if (weapDocument == null)
		{
			docStat = "FAIL";
			docMsg = "E1002 - Document is NULL.";
			
			throw new WeapException("E1002");
		}
		
		if (metadata == null) 
		{
			docStat = "FAIL";
			docMsg = "E1003 - MetaData is NULL.";
			
			throw new WeapException("E1003");
		}
		
		if (weapDocument.getDocumentType() == WeapDocumentType.XFDF) 
		{
			// expected to convert to WeapPdfDocument
			weapDocument = weapDocument.convert();
			
			docFmt = "PDF";
			pageCnt = weapDocument.getPageCount();
		}
		
		if (metadata.getConversionRequired() && weapDocument.getDocumentType() == WeapDocumentType.PDF) 
		{
			// expected to convert to WeapImageDocument
			weapDocument = weapDocument.convert();
			pageCnt = weapDocument.getPages().size();
		}
		else if (weapDocument.getDocumentType() == WeapDocumentType.PDF)
		{
			docFmt = "PDF";
			pageCnt = weapDocument.getPageCount();
		}
		else
		{
			docFmt = weapDocument.getDocumentType().toString();
			pageCnt = 1;
		}
		
		
		WriteLog.WriteToLog("Document class: " + metadata.getDocumentClass());
		
		WeapDocumentClass weapDocClass = classMapping.getDocumentClasses().get(metadata.getDocumentClass());
		
		if (weapDocClass == null) 
		{
			docStat = "FAIL";
			docMsg = "E1004 - Document Class is NULL.";
			
			throw new WeapException("E1004");
		}

		ObjectStore os = null;
		
		if (version == 1)
		{
			ceConfig.setCEVersion(ceConfig.get51Ver());
			WriteLog.WriteToLog("Connecting to version 5.1...");
			
			os = wc.CELogon(ceConfig);
			
			if (os != null)
			{
				WriteLog.WriteToLog("Connected.");
			}
			else
			{
				docStat = "FAIL";
				docMsg = "Connecton to CE 5.1 failed.";
				
				WriteLog.WriteToLog("Connection failed!");
			}
		}
		else
		{
			ceConfig.setCEVersion(ceConfig.get52Ver());
			WriteLog.WriteToLog("Connecting to version 5.2...");
			os = wc.CELogon(ceConfig);
			
			if (os != null)
			{
				WriteLog.WriteToLog("Connected.");
			}
			else
			{
				docStat = "FAIL";
				docMsg = "Connecton to CE 5.2 failed.";
				
				WriteLog.WriteToLog("Connection failed!");
			}
		}
		
		if (os != null)
		{
			// Create a document instance.
			WriteLog.WriteToLog("Creating a document instance...");
			Document ceDoc = Factory.Document.createInstance(os, metadata.getDocumentClass());
			
			// Set document properties.
			WriteLog.WriteToLog("Setting document properties...");
			ceDoc.getProperties().putValue("DocumentTitle", metadata.getDcn());
			
			WriteLog.WriteToLog("Setting document mimetype to " + weapDocument.getMimeType() + "."); 
			ceDoc.set_MimeType(weapDocument.getMimeType());
	
			WriteLog.WriteToLog("Assigning properties...");
			assignProperties(weapDocClass, metadata, ceDoc);
	
			ContentElementList contentList = Factory.ContentElement.createList();
			
			if (weapDocument.isMultiPageDocument()) 
			{
				WriteLog.WriteToLog("Committing multiple page document...");
				
				Set<WeapImagePage> pages = weapDocument.getPages();
				pageCnt = pages.size();				
				
				for (WeapImagePage page : pages) 
				{
					ContentTransfer ct = Factory.ContentTransfer.createInstance();
					ct.setCaptureSource(page.getInputStream());
					ct.set_RetrievalName(metadata.getDcn());
					ct.set_ContentType(weapDocument.getMimeType());
					contentList.add(ct);
				}
			} 
			else 
			{
				WriteLog.WriteToLog("Committing single page document...");

				pageCnt = 1;
				
				ContentTransfer ct = Factory.ContentTransfer.createInstance();
				ct.setCaptureSource(weapDocument.getInputStream());
				ct.set_RetrievalName(metadata.getDcn());
				ct.set_ContentType(weapDocument.getMimeType());
				contentList.add(ct);
			}
			
			docPageCnt = Integer.toString(pageCnt);
			
			WriteLog.WriteToLog("Setting document properties...");
			
			ceDoc.set_ContentElements(contentList);
			
			WriteLog.WriteToLog("Checking in document...");
			ceDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			
			WriteLog.WriteToLog("Saving document...");
			ceDoc.save(RefreshMode.REFRESH);
	  	
			WriteLog.WriteToLog("Getting document number...");
			docId = ceDoc.get_Id().toString();
			
			WriteLog.WriteToLog("Adding document id to data...");
			metadata.addDocumentID(docId);
			
	  		WriteLog.WriteToLog("Created CE document with ID " + docId);
	  		LoggingUtil.LogInfoMsg("Created CE document with ID " + docId);
		}
		else
		{
			WriteLog.WriteToLog("Unable to create CE document!");
		}
		
		LoggingUtil.LogTraceEndMsg();
		
		if (docSection.equalsIgnoreCase("APP"))
		{
			msgData.setAppDocNum(docId);
			msgData.setAppFmt(docFmt);
			msgData.setAppPageCnt(docPageCnt);
			msgData.setAppStatus(docStat);
			msgData.setAppMessage(docMsg);
		}
		else if (docSection.equalsIgnoreCase("COV"))
		{
			msgData.setCoverDocNum(docId);
			msgData.setCoverFmt(docFmt);
			msgData.setCoverPageCnt(docPageCnt);
			msgData.setCoverStatus(docStat);
			msgData.setCoverMessage(docMsg);
		}
		
		return docId;
	}

	protected void assignProperties(WeapDocumentClass docClass, WeapMetadata metadata, Document ceDoc) 
	{
		LoggingUtil.LogTraceStartMsg();

		Properties docProperties = ceDoc.getProperties();

		Map<String, WeapProperty> weapProperties = docClass.getProperties();
		
		for (WeapProperty prop : weapProperties.values()) 
		{
				
			switch (prop.getDataType()) 
			{
				case STRING:
					String str = evaluateProperty(metadata, prop.getWeapPropertyName(), String.class);
					docProperties.putValue(prop.getSymbolicName(), str);
					break;
				case DATE:
					Date dt = evaluateProperty(metadata, prop.getWeapPropertyName(), Date.class);
					docProperties.putValue(prop.getSymbolicName(), dt);
					break;
				case ID:
					Id id = evaluateProperty(metadata, prop.getWeapPropertyName(), Id.class);
					docProperties.putValue(prop.getSymbolicName(), id);
					break;
				case INTEGER:
					Integer integer = evaluateProperty(metadata, prop.getWeapPropertyName(), Integer.class);
					docProperties.putValue(prop.getSymbolicName(), integer);
					break;
				case DOUBLE:
					Double dbl = evaluateProperty(metadata, prop.getWeapPropertyName(), Double.class);
					docProperties.putValue(prop.getSymbolicName(), dbl);
					break;
				case BOOLEAN:
					Boolean bool = evaluateProperty(metadata, prop.getWeapPropertyName(), Boolean.class);
					docProperties.putValue(prop.getSymbolicName(), bool);
					break;
				case UNKNOWN:
					// attempt to deal with it like a string... best effort
					String unknown = evaluateProperty(metadata, prop.getWeapPropertyName(), String.class);
					docProperties.putValue(prop.getSymbolicName(), unknown);
					break;
			}
		}

		docProperties.putValue("SRCID", "IES");
		
		LoggingUtil.LogTraceEndMsg();

	}
}
