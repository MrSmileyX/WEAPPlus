package com.wlp.ecm.weap.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.resource.ResourceException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.filenet.is.ra.sa.bean.ClientBean;
import com.filenet.is.ra.sa.util.AppConstants;
import com.filenet.is.ra.sa.util.WebConstants;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.DocumentClassPropertyMapping;
import com.wlp.ecm.weap.config.UserCredentials;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.config.WeapDocumentClass;
import com.wlp.ecm.weap.config.WeapProperty;
import com.wlp.ecm.weap.document.WeapDocument;
import com.wlp.ecm.weap.document.WeapDocumentType;
import com.wlp.ecm.weap.document.WeapImagePage;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.message.WeapRegion;
import com.wlp.ecm.weap.reporting.MessageData;

public class WeapRepositoryISRA extends WeapRepository {

	@Override
	public String storeDocument(WeapDocument weapDocument, WeapMetadata metadata, int version, 
								MessageData msgData, String docSection)	throws WeapException 
	{
		String docId = "";
		String docPageCnt = "";
		String docFmt = "";
		String docStat = ""; 
		String docMsg = "";
		
		int pageCnt = 0;
		
		LoggingUtil.LogTraceStartMsg();

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		WeapConfigSettings config = ctx.getBean(WeapConfigSettings.class);
		DocumentClassPropertyMapping classMapping = (DocumentClassPropertyMapping) ctx.getBean("documentClassPropertyMapping", "IS");
		UserCredentials creds = null;

		metadata.setRepository("IS");
		WriteLog.WriteStatus("Storing Document in IS");
		
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

		String libraryName = null;
		
		if (metadata.getWeapRegion() == WeapRegion.EAST) 
		{
			creds = ctx.getBean("israCredentialsEast", UserCredentials.class);
			libraryName = config.getIsraLibraryNameEast();	
		} 
		else if (metadata.getWeapRegion() == WeapRegion.WEST) 
		{
			creds = ctx.getBean("israCredentialsWest", UserCredentials.class);
			libraryName = config.getIsraLibraryNameWest();
		} 
		else if (metadata.getWeapRegion() == WeapRegion.CENTRAL) 
		{
			creds = ctx.getBean("israCredentialsCentral", UserCredentials.class);
			libraryName = config.getIsraLibraryNameCentral();
		}
		else
		{
			creds = ctx.getBean("israCredentialsWest", UserCredentials.class);
			libraryName = config.getIsraLibraryNameWest();
		}
		
		msgData.setRepoName(libraryName);
		
		WriteLog.WriteToLog("Committing document to: " + libraryName);
		
		if (weapDocument.getDocumentType() == WeapDocumentType.XFDF) 
		{
			// expected to convert to WeapPdfDocument
			WriteLog.WriteToLog("Converting XFDF to PDF...");
			weapDocument = weapDocument.convert();
			
			docFmt = "PDF";
			pageCnt = weapDocument.getPageCount();			
		}
		
		if (metadata.getConversionRequired() && weapDocument.getDocumentType() == WeapDocumentType.PDF) 
		{
			// expected to convert to WeapImageDocument
			WriteLog.WriteToLog("Converting PDF to TIFF");
			weapDocument = weapDocument.convert();
			
			docFmt = "TIFF";
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
		
		docPageCnt = Integer.toString(pageCnt);
		
		WeapDocumentClass weapDocClass = classMapping.getDocumentClasses().get(metadata.getDocumentClass());
		
		if (weapDocClass == null) 
		{
			docStat = "FAIL";
			docMsg = "E1004 - Document Class is NULL.";

			throw new WeapException("E1004");
		}
		
		/* ISRA */
		long newDocId = 0;
		WeapISConnector cb = new WeapISConnector();  //ClientBean();
		ByteArrayInputStream[] pageStreams = null;
		
		try 
		{
			WriteLog.WriteToLog("Preparing to connect to IS Library: " + libraryName);
			cb.initialize(creds.getUser(), creds.getPswd(), libraryName, 0, Locale.getDefault());
		
			String docClassName = weapDocClass.getName();
			short docType = 0;
			String cacheName = null;
			List<Map<String, Object>> docIndexes = new ArrayList<Map<String, Object>>();
				
			assignProperties(weapDocClass, metadata, docIndexes);
			
			if (weapDocument.isMultiPageDocument()) 
			{
				Set<WeapImagePage> pages = weapDocument.getPages();
				pageStreams = new ByteArrayInputStream[pages.size()];
				
				int pageNbr = 0;
				
				for (WeapImagePage page : pages) 
				{
					pageStreams[pageNbr] = page.getInputStream();
					pageNbr++;
				}
			} 
			else 
			{
				pageStreams = new ByteArrayInputStream[1];
				pageStreams[0] = weapDocument.getInputStream();
			}
			
			Set<Object> folderSet = new HashSet<Object>();;
			String docFamilyName = null;
			boolean is_Duplication_OK = false;
			boolean enableChecksum = false;
		    Long secRead = null;
		    Long secWrite = null;
		    Long secAppendExecute = null;
		    Map<String, Object> paramsMap = new HashMap<String, Object>();
		    
		    paramsMap.put(AppConstants.FUNCTION_NAME, AppConstants.ADD_DOC_FUNCTION_NAME);
		    paramsMap.put(AppConstants.INTERACTION_VERB, new Integer(AppConstants.SYNC_SEND_RECEIVE));
		    paramsMap.put(AppConstants.EXECUTION_TIMEOUT, new Long(WebConstants.NUM_10000));
	  		paramsMap.put(WebConstants.INTERACTION_EXECUTE3, new Boolean(false));
			
	  		Map<String, Object> docPropDocFormat = new HashMap<String, Object>();
	  		String docFormatValue = weapDocument.getMimeType() + WebConstants.NAME_VALUE + "\"" + metadata.getDcn() + "\""; 
	        
	  		WriteLog.WriteToLog("Adding Index values...");
	  		
	  		docPropDocFormat.put(AppConstants.INDEX_NAME, new String(WebConstants.F_DOCFORMAT));
	        docPropDocFormat.put(AppConstants.INDEX_TYPE, new Short(WebConstants.NUM_50));
	        docPropDocFormat.put(AppConstants.INDEX_VALUE, docFormatValue);
	        docIndexes.add(docPropDocFormat);

	        WriteLog.WriteToLog("Adding document...");
	        
	  		newDocId = cb.AddDoc(docClassName, docType, cacheName, docIndexes, pageStreams, folderSet, docFamilyName, is_Duplication_OK, secRead, secWrite, secAppendExecute, enableChecksum, paramsMap);
	  		
	  		docId = Long.toString(newDocId);
	  		metadata.addDocumentID(docId);
	  		docStat = "PASS";
	  		
	  		WriteLog.WriteToLog("Document ID Created: " + docId);
	  		LoggingUtil.LogInfoMsg("Created ISRA document with docid " + docId);
	  		
	  		
		}
		catch (Exception e) 
		{
			LoggingUtil.LogErrorMsg(e);
			WriteLog.WriteToLog("ERROR: E1024 - " + e.getMessage());
			
			docStat = "FAIL";
			
			if (e.getMessage().length() >= 254) 
			{
				docMsg = e.getMessage().substring(0, 254);
			}
			else
			{
				docMsg = e.getMessage();
			}
			
			WriteLog.writeTraceMsg(e);
			throw new WeapException("E1024", e);
		} 
		finally 
		{
				//try 
				//{
			
			cb.closeIS();
				
				//cb.logoff();
				//} 
				//catch (ResourceException e) 
				//{
				//	LoggingUtil.LogErrorMsg(e);
				//}
			
			if (pageStreams != null) 
			{
				try {
					for (InputStream is : pageStreams) 
					{
						is.close();
					}
				} 
				catch (IOException e) 
				{
					LoggingUtil.LogErrorMsg(e);
				}
			}
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
	
	protected void assignProperties(WeapDocumentClass docClass, WeapMetadata metadata, List<Map<String, Object>> docIndexes) {
		LoggingUtil.LogTraceStartMsg();

		Map<String, WeapProperty> weapProperties = docClass.getProperties();
		for (WeapProperty prop : weapProperties.values()) {
			Map<String, Object> index = new HashMap<String, Object>();
			switch (prop.getDataType()) {
			case STRING:
				String str = evaluateProperty(metadata, prop.getWeapPropertyName(), String.class);
				index.put(AppConstants.INDEX_TYPE, new Short("50"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, str);
				docIndexes.add(index);
				break;
			case DATE:
				Date dtDate = evaluateProperty(metadata, prop.getWeapPropertyName(), Date.class);
				index.put(AppConstants.INDEX_TYPE, new Short("56"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, dtDate);
				docIndexes.add(index);
				break;
			case TIME:
				Date dtTime = evaluateProperty(metadata, prop.getWeapPropertyName(), Date.class);
				index.put(AppConstants.INDEX_TYPE, new Short("51"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, dtTime);
				docIndexes.add(index);
				break;
			case INTEGER:
				Integer integer = evaluateProperty(metadata, prop.getWeapPropertyName(), Integer.class);
				index.put(AppConstants.INDEX_TYPE, new Short("70"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, integer);
				break;
			case LONG:
				Long long1 = evaluateProperty(metadata, prop.getWeapPropertyName(), Long.class);
				index.put(AppConstants.INDEX_TYPE, new Short("71"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, long1);
				break;
			case SHORT:
				Short short1 = evaluateProperty(metadata, prop.getWeapPropertyName(), Short.class);
				index.put(AppConstants.INDEX_TYPE, new Short("68"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, short1);
				break;
			case BOOLEAN:
				Boolean bool = evaluateProperty(metadata, prop.getWeapPropertyName(), Boolean.class);
				index.put(AppConstants.INDEX_TYPE, new Short("65"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, bool);
				break;
			case BYTE:
				Byte byte1 = evaluateProperty(metadata, prop.getWeapPropertyName(), Byte.class);
				index.put(AppConstants.INDEX_TYPE, new Short("66"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, byte1);
				break;
			case UNKNOWN:
				// attempt to deal with it like a string... best effort
				String unknown = evaluateProperty(metadata, prop.getWeapPropertyName(), String.class);
				index.put(AppConstants.INDEX_TYPE, new Short("50"));
				index.put(AppConstants.INDEX_NAME, prop.getSymbolicName());
				index.put(AppConstants.INDEX_VALUE, unknown);
				break;
			}
		}

		LoggingUtil.LogTraceEndMsg();
	}
	
}
