package com.wlp.ecm.weap.data;

import javax.naming.*;
import javax.resource.ResourceException;
import javax.resource.cci.*;
import java.util.*;
import java.io.*;
import java.sql.ResultSet;
import com.filenet.is.ra.sa.util.*;
import com.filenet.is.ra.spi.FN_IS_SpiConnectionManager;
import com.filenet.is.ra.spi.FN_IS_SpiManagedConnectionFactory;
import com.filenet.is.ra.cci.*;
import com.wlp.ecm.weap.common.WriteLog;

public class WeapISConnector 
{

    String userName;
    String password;
    String libraryName;
    String raVersion;
    String raResourceWarning;
    String appServer;

    String loginMode = WebConstants.USER_MODE;
    
    Integer loginType;
    
    private Connection isConnection;
    private ConnectionFactory isConnectionFactory;
    private RecordFactory isRecordFactory;

    private GetMessage getMessage = null;
    
    boolean m_jaasFlag = false;
	
	public void initialize(String userName, String password,String libraryName, Integer loginType, Locale pCurrentLocale) 
	{
        this.userName = userName;
        this.password = password;
        this.libraryName = libraryName;
        this.loginType = loginType;

        /* authenticating the user by making an connection to IS through RA */

        try
        {
        	login(userName, password, libraryName, loginType, pCurrentLocale);
            
            //getting the RA version and setting it
            ResourceAdapterMetaData raMetaData = isConnectionFactory.getMetaData();
            
            this.raVersion = raMetaData.getAdapterVersion().toUpperCase();

            if (this.raVersion.indexOf(WebConstants.VERSION_VIEW) == -1)
            {
            	this.raVersion = WebConstants.VERSION_ENTERPRISE;
            }
            else
            {
            	this.raVersion = WebConstants.VERSION_VIEW;
            }
        }
        catch(ResourceException re)
        {
        	WriteLog.WriteToLog("ERROR: " + re.getMessage());
            WriteLog.writeTraceMsg(re);   
        }
        catch(Exception e)
        {
        	WriteLog.WriteToLog("ERROR: " + e.getMessage());
            WriteLog.writeTraceMsg(e);
        }
    }
	
    public Connection createFNConnection(ConnectionFactory factory, String userName, String passWord)
	{
	       Connection ccConnection = null;
	       
	       try
	       {
                WriteLog.WriteToLog("Getting connection...");
                
                FN_IS_CciConnectionSpec connSpec = new FN_IS_CciConnectionSpec(userName, passWord, 0);
                ccConnection = factory.getConnection(connSpec);
                
                WriteLog.WriteToLog("FileNET Connection is established.");
	                	                
	        } 
	       	catch (ResourceException e) 
	       	{
	       		WriteLog.WriteToLog("ERROR: " + e.getMessage());
	            WriteLog.writeTraceMsg(e);
	        }
       		
	       	return ccConnection;
	}

	
    public void closeIS() 
	{
		try
		{
			if (this.isConnection != null)
			{
				this.isConnection.close();
				this.isConnection = null;
			}
			
			if (this.isConnectionFactory != null)
			{
				this.isConnectionFactory = null;
			}
		}
		catch(ResourceException re)
		{
			WriteLog.WriteToLog("ERROR: " + re.getMessage());
            WriteLog.writeTraceMsg(re);
		}
	}
	
    public void login(String userName, String password, String libraryName, Integer loginType, Locale pCurrentLocale) 
    {
    	this.getMessage = GetMessageUtil.getInstance(pCurrentLocale);
        
        try
        {
        	WriteLog.WriteToLog("Creating connection factory for: " + libraryName);
        	this.isConnectionFactory = createConnFactory(libraryName);
        	
        	WriteLog.WriteToLog("Creating record factory for: " + libraryName);
        	this.isRecordFactory = this.isConnectionFactory.getRecordFactory();
            
           	WriteLog.WriteToLog("Creating connection...");
           	this.isConnection = createFNConnection(this.isConnectionFactory, userName, password);
            
            if(this.isConnection == null)
            {
            	WriteLog.WriteToLog("Error: Connection object returned is null");
            }

        }
        catch(ResourceException re)
        {
        	  WriteLog.WriteToLog("Error:  " + re.getMessage());
        	  WriteLog.writeTraceMsg(re);
        }
        catch(Exception e)
        {
        	  WriteLog.WriteToLog("Error:  " + e.getMessage());
        	  WriteLog.writeTraceMsg(e);
        }
    }
    
    public ConnectionFactory createConnFactory(String domainName)
	{
		WriteLog.WriteToLog("Creating connection factory...");
    	ConnectionFactory factory = createFactory(domainName, "FileNet");
    	return factory;
	}
    
    public ConnectionFactory createFactory(String domainName, String orgName)
	{
	   
	   FN_IS_SpiManagedConnectionFactory mcf = null; 

	   try
	   {
		   mcf = new FN_IS_SpiManagedConnectionFactory();
		   
		   mcf.setDomainName(domainName);
		   mcf.setOrganizationName(orgName);
		   mcf.setProductName("IBM FileNet Image Services Resource Adapter");
		   mcf.setPageBufferSize(64);
	   }
	   catch(Exception fnise)
	   {
		   WriteLog.WriteToLog(fnise.getMessage());
	   }

	   try 
	   {
		   return (ConnectionFactory) mcf.createConnectionFactory(new FN_IS_SpiConnectionManager());
	   } 
	   catch (ResourceException e) 
	   {
		   WriteLog.WriteToLog("ERROR: " + "CreateFactory in WeapISConnector Failed!");
		   WriteLog.WriteToLog("Error: " + e.getMessage());
		   WriteLog.writeTraceMsg(e);
		   
		   return null;
	   }
	}

    public long AddDoc(String docClassName, Short docType, String cacheName, List docProperties, InputStream[] pageStreams, Set folderSet, String docFamilyName,
            		   boolean is_Duplication_OK, Long sec_Read, Long sec_Write, Long sec_Append_Execute, Boolean enableChecksum, Map InteractionParmsMap) 
    {

    	Interaction interaction = null;
    	long lresultDocID = 0;
    	
    	try
    	{
    		this.raResourceWarning = "";
			interaction = this.isConnection.createInteraction();
			
			FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();
			
			/* Assigning Interaction Parameters to CciInteractionSpec */
			updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);
			
			if(interactionSpec.getFunctionName() == null)
			interactionSpec.setFunctionName(AppConstants.ADD_DOC_FUNCTION_NAME);
			
			Collection colDocProperties = (Collection) docProperties;
			
			/* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
			MappedRecord mappedRecord =  this.isRecordFactory.createMappedRecord(AppConstants.ADD_DOCUMENT);
			
			mappedRecord.put(AppConstants.DOC_CLASS_NAME, docClassName);
			mappedRecord.put(AppConstants.DOC_TYPE, docType);
			mappedRecord.put(AppConstants.CACHE_NAME, cacheName);
			mappedRecord.put(AppConstants.DOC_PAGE_STREAMS, pageStreams);
			mappedRecord.put(AppConstants.DOC_FAMILIY_NAME, docFamilyName);
			mappedRecord.put(AppConstants.IS_DUPLICATION_OK, new Boolean(is_Duplication_OK));
			mappedRecord.put(AppConstants.SEC_READ , sec_Read);
			mappedRecord.put(AppConstants.SEC_WRITE, sec_Write);
			mappedRecord.put(AppConstants.SEC_APPEND_EXECUTE, sec_Append_Execute);

			// creating a Indexed record containing foldername string
			IndexedRecord folderSetRecord=  this.isRecordFactory.createIndexedRecord(AppConstants.ADD_DOCUMENT_FOLDER_SET);
			
			if (folderSet!=null)
			{
				Iterator iteratorFolder = folderSet.iterator();
				
				while(iteratorFolder.hasNext())
				{
					folderSetRecord.add (iteratorFolder.next());
				}
			}
			
			mappedRecord.put(AppConstants.FOLDER_SET, folderSetRecord);
		
			// creating a Indexed record "DocProperties" of Mapped Record "DocProperty"
			IndexedRecord docPropertiesRecord = this.isRecordFactory.createIndexedRecord(AppConstants.ADD_DOCUMENT_DOC_PROPERTIES);
			
			ListIterator iter = docProperties.listIterator();
			
			while (iter.hasNext())
			{
				Map docProp = (Map)iter.next();
				MappedRecord docPropertyRecord = this.isRecordFactory.createMappedRecord(AppConstants.ADD_DOCUMENT_DOC_PROPERTY);
				docPropertyRecord.putAll(docProp);
				docPropertiesRecord.add(docPropertyRecord);
			}
			
			mappedRecord.put(AppConstants.INDEX_RECORDS, docPropertiesRecord);
			mappedRecord.put(AppConstants.ENABLE_CHECKSUM, enableChecksum);
			
			boolean bexecute3 = false;
			Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
			
			if(execute3Obj != null)
			{
				bexecute3 = execute3Obj.booleanValue();
			}
			
			MappedRecord resultMappedRecord = null;
			
			/* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
			// System.out.println("Add Doc: Before entering interaction.execute method.");
			if(bexecute3)
			{
				MappedRecord outputRecord = this.isRecordFactory.createMappedRecord(AppConstants.ADD_DOCUMENT_RESULT);
				interaction.execute(interactionSpec,mappedRecord,outputRecord);
				resultMappedRecord = outputRecord;
			}
			else
			{
				resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
			}
		
			// System.out.println("Add Doc: After exiting interaction.execute method.");
			this.raResourceWarning = getWarningFromInteraction(interaction);
			
			lresultDocID = new Long((resultMappedRecord.get(AppConstants.DOC_ID)).toString()).longValue();
		}
	    catch(ResourceException re)
    	{
			WriteLog.WriteToLog("Error: " + re.getMessage());
			WriteLog.writeTraceMsg(re);
		}
    	catch(Exception e)
    	{
    		WriteLog.WriteToLog("Error: " + e.getMessage());
    		WriteLog.writeTraceMsg(e);
		}
    	finally
    	{
    		if(interaction != null)
			{
				try 
				{
					interaction.close();
				}
				catch (ResourceException ie) 
				{
					WriteLog.WriteToLog("Error: " + ie.getMessage());
					WriteLog.writeTraceMsg(ie);
				}
			}
		}
    	
    	return lresultDocID;
    }
    
    private String getWarningFromInteraction(Interaction interaction)
    {
    	ResourceWarning resourceWarning;
    	String warningMsg = "";
    	
    	try 
    	{
			resourceWarning = interaction.getWarnings();
			
			while(resourceWarning != null)
			{
				warningMsg += resourceWarning.getMessage();
				resourceWarning = resourceWarning.getLinkedWarning();
			}
    	} 
    	catch (ResourceException e) 
    	{
    		WriteLog.WriteToLog("Error: " + e.getMessage());
			WriteLog.writeTraceMsg(e);
		}
    	
    	return (warningMsg.trim());
    }

    private void updateISpecWithISpecParams(FN_IS_CciInteractionSpec interactionSpec,Map paramsMap)
    {

    	if(paramsMap.containsKey(AppConstants.FUNCTION_NAME))
    	{
          interactionSpec.setFunctionName((String)paramsMap.get(AppConstants.FUNCTION_NAME));
    	}
    	else if(paramsMap.containsKey(AppConstants.INTERACTION_VERB))
    	{
    		interactionSpec.setInteractionVerb(((Integer)paramsMap.get(AppConstants.INTERACTION_VERB)).intValue());
    	}
    	else if(paramsMap.containsKey(AppConstants.EXECUTION_TIMEOUT))
        {
            interactionSpec.setExecutionTimeout(((Long)paramsMap.get(AppConstants.EXECUTION_TIMEOUT)).intValue());
        }
    }
}
