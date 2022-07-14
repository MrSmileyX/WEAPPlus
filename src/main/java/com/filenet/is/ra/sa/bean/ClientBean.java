/*
 *
 * Copyright (c) 2002-2003  FileNet Corporation.
 *
 * REVISION HISTORY:
 *	DATE 		NAME			PURPOSE
 *
 *
 *
 *
 *
 */
package com.filenet.is.ra.sa.bean;
import javax.naming.*;
import javax.resource.ResourceException;
import javax.resource.cci.*;
import java.util.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.sql.SQLException;
import com.filenet.is.ra.sa.util.*;
import com.filenet.is.ra.spi.FN_IS_SpiConnectionManager;
import com.filenet.is.ra.spi.FN_IS_SpiManagedConnectionFactory;
import com.filenet.is.ra.cci.*;
import com.wlp.ecm.weap.common.WriteLog;

import java.text.DecimalFormat;

/**
 * <code>ClientBean</code> is a business object which abstract the functionality
 * of the Sample Application which is serializable. This is the bean
 * implementation class which implements all the business methods defined in the
 * Remote Interface of IS Resource Adapter and the necessary callback methods.
 * All the remote calls within Sample Application will be delegated to this class.
 *
 * @author Ashu Govil, Kiran Kumar, Neha Pandey, Nikhil Bhatia, Inderjit Singh
 * @version 3.2.1
 *
 * implements - Serialiazable
 */

public class ClientBean implements Serializable
{
    String m_UserName;
    String m_Password;
    String m_LibraryName;
    String m_RAVersion;
    Integer m_LoginType;
    String m_LoginMode = WebConstants.USER_MODE;
    transient String m_RAResourceWarning;
    transient String m_AppServer;
	//Code Added for DTS 180479 - JAAS Authentication Realm
	boolean m_jaasFlag = false;
	//Code Added for DTS 180479 - JAAS Authentication Realm Ends

    /*Commented by Sachin for WAS6 changes
      boolean m_isWebSphere5 = false;
    */
    //Code added for WAS6 changes*/
     transient boolean m_isWebSphere6 = false;
    //Code added for WAS6 changes


   transient boolean m_isWebLogic = false;

   transient boolean m_ConnectionKeepAlive = false;
   transient boolean m_ConnectionActive = false;
    /*---------*/

    private transient Connection m_Connection;
    private transient ConnectionFactory m_ConnectionFactory;
    private transient RecordFactory m_RecordFactory;
    private transient Interaction m_GetDocContentInteraction;
    private transient Interaction m_GetDocContentInteraction2;
    private transient Interaction m_getDocumentPropInteraction;
    private transient Interaction m_findDocInteraction;
    private transient ResultSet m_findDocResultSet;
    private transient InputStream m_InputStream;
    private transient boolean isISNull = false;
    //code added for DTS 169459
    private Boolean cpwlFlag = new Boolean("false");
    //code added for DTS 169459 ends
    private transient GetMessage m_GetMessage = null;

    private static final int ONE_KB = 1024;
	//Code modified for DTS-201757 Begin
    //private static final int m_WindowSize = 64 * ONE_KB;
	private static int m_WindowSize = 64 * ONE_KB;
	private Locale m_pCurrentLocale = null;
	//Code modified for DTS-201757 -End
	//private  List resultPrinterAttributesList = new ArrayList();
/**
**/
     //code added for DTS 169459
    /* Default Constructor */
    public ClientBean(){
    }

    /* Constructor for ChangePasswordWithoutLogin*/
    public ClientBean(Boolean cpwlFlag){
      this.cpwlFlag = cpwlFlag;
    }
    
    //code added for DTS 169459 ends here

    public void initialize(String userName,String password,String libraryName,Integer loginType,String loginMode, Locale pCurrentLocale)
                                                        throws ResourceException, Exception {
        initialize(userName,password,libraryName,loginType, pCurrentLocale);
        m_LoginMode = loginMode;
    }

/**This is a private method which called to open an connection. IN case of Weblogic
 * and WAS 4.2 login will be called only from initialize function of ClientBean.
 * But in case of WAS 5.0, login() will be call before executing an interaction.
**/
    public void login(Locale pCurrentLocale) throws ResourceException {
      m_GetMessage = GetMessageUtil.getInstance(pCurrentLocale);
      try{
          Context ctx = new InitialContext();
          Object obj = ctx.lookup(m_LibraryName);

          //Creating ConnectionFactory, RecordFactory & Connection and store it in class level objects
          m_ConnectionFactory = (ConnectionFactory)obj;

          m_RecordFactory = m_ConnectionFactory.getRecordFactory();
          //code added for DTS 169459
          //Added for ChangePasswordWithoutLogin
          ConnectionSpec cSpec = null;
         if(cpwlFlag.booleanValue() == true){
             cSpec = new FN_IS_CciConnectionSpec(m_UserName,
                  m_Password,cpwlFlag);
          }else{
              cSpec = new FN_IS_CciConnectionSpec(m_UserName,
                  m_Password, m_LoginType);
          }
          //End for ChangePasswordWithoutLogin
          //code added for DTS 169459 ends here

		//Code commented for DTS 180479 - JAAS Authentication Realm
          //m_Connection = m_ConnectionFactory.getConnection(cSpec);

		 //Code Added for DTS 180479 - JAAS Authentication Realm
		  if(m_jaasFlag){
			m_Connection = m_ConnectionFactory.getConnection();
		  } else {
			m_Connection = m_ConnectionFactory.getConnection(cSpec);
		  }
		 //Code Added for DTS 180479 - JAAS Authentication Realm Ends

          if(m_Connection == null){
              throw new Exception("Connection object returned is null");
          }

        }
        catch(NamingException ne)
        {
    	  ne.printStackTrace();
          throw new ResourceException("Library Name is invalid or not defined.");
        }
        catch(ResourceException re)
        {
          re.printStackTrace();
          throw re;
        }
        catch(Exception e)
        {
          e.printStackTrace();
          throw new ResourceException(e.getMessage());
        }
    }
    
    private Connection connectIS(String repository, String userName, String passWord, ConnectionFactory myFactory)
	{
		Connection fnConnection = null;
		fnConnection = createFNConnection(myFactory, userName, passWord);
		return fnConnection;
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
	       		e.printStackTrace();
	        }
       		
	       	return ccConnection;
	}

	
	private void closeIS(Connection fnConnection, ConnectionFactory myFactory) throws ResourceException 
	{
		try
		{
			if (fnConnection != null)
			{
				fnConnection.close();
				fnConnection = null;
			}
			
			if (myFactory != null)
			{
				myFactory = null;
			}
		}
		catch(ResourceException re)
		{
			 WriteLog.WriteToLog("Error:  " + re.getMessage());
	            
             re.printStackTrace();
             throw re;
		}
	}
	
    public void login(String userName, String password, String libraryName, Integer loginType, Locale pCurrentLocale) throws ResourceException 
    {
        m_GetMessage = GetMessageUtil.getInstance(pCurrentLocale);
        
        try
        {
            //Context ctx = new InitialContext();
            //Object obj = ctx.lookup(m_LibraryName);

            //Creating ConnectionFactory, RecordFactory & Connection and store it in class level objects
            //m_ConnectionFactory = (ConnectionFactory)obj;
        	
        	WriteLog.WriteToLog("Creating connection factory for: " + libraryName);
        	m_ConnectionFactory = createConnFactory(libraryName);
        	
        	WriteLog.WriteToLog("Creating record factory for: " + libraryName);
        	m_RecordFactory = m_ConnectionFactory.getRecordFactory();
            
            //ConnectionSpec cSpec = null;
            //
            //if(cpwlFlag.booleanValue() == true)
            //{
            //   WriteLog.WriteToLog("Creating connection specifictation for: " + libraryName + ":" + userName + ":" + password + ":" + cpwlFlag.toString());
            //   cSpec = new FN_IS_CciConnectionSpec(userName, password, cpwlFlag);
            //}
            //else
            //{
            //	WriteLog.WriteToLog("Creating connection specifictation for: " + libraryName + ":" + userName + ":" + password + ":" + loginType);
            //  cSpec = new FN_IS_CciConnectionSpec(userName, password, loginType);
            //}
            
            //if(m_jaasFlag)
  		  	//{
            //	WriteLog.WriteToLog("Creating connection!");
            //	m_Connection = m_ConnectionFactory.getConnection();
  		  	//}
  		  	//else 
  		  	//{
  		  		//if (cSpec != null)
  		  		//{
  		  		//	WriteLog.WriteToLog("Creating connection based on specification!");
  		  		//	m_Connection = m_ConnectionFactory.getConnection(cSpec);	
  		  		//}
  		  		//else
  		  		//{
  		           	WriteLog.WriteToLog("Creating connection...");
  	            	m_Connection = createFNConnection(m_ConnectionFactory, userName, password);
  	            	//m_ConnectionFactory.getConnection();
  	 	  		//}
  		  	//}
            
            if(m_Connection == null)
            {
            	throw new Exception("Connection object returned is null");
            }

        }
        catch(NamingException ne)
        {
              WriteLog.WriteToLog("Error:  " + ne.getMessage());
              
              ne.printStackTrace();
              throw new ResourceException("Library Name is invalid or not defined.");
        }
        catch(ResourceException re)
        {
        	  WriteLog.WriteToLog("Error:  " + re.getMessage());
            
              re.printStackTrace();
              throw re;
        }
        catch(Exception e)
        {
        	  WriteLog.WriteToLog("Error:  " + e.getMessage());
            
              e.printStackTrace();
              throw new ResourceException(e.getMessage());
        }
    }
    
    public ConnectionFactory createConnFactory(String domainName)
	{
		final HashMap<String, String> parameters = new HashMap<String, String>(3);
	    
		parameters.put("ISRA_Domain", domainName);
	    parameters.put("ISRA_Org",  "FileNet");
	    
	    WriteLog.WriteToLog("Creating connection factory...");
	       
	    try 
	    {
	    	ConnectionFactory factory = createFactory(parameters);
	    	return factory;
	    }
	    catch (ResourceException e) 
	    {
      		e.printStackTrace();
	    }
	    
	    return null;
	}
    
    public ConnectionFactory createFactory(Map<String, String> parameters) throws ResourceException 
	{
	   final String domainName = parameters.get("ISRA_Domain");
	   final String organizationName = parameters.get("ISRA_Org");
	   
	   FN_IS_SpiManagedConnectionFactory mcf = null; 

	   try
	   {
		   mcf = new FN_IS_SpiManagedConnectionFactory();
		   
		   mcf.setDomainName(domainName);
		   mcf.setOrganizationName(organizationName);
		   mcf.setProductName("IBM FileNet Image Services Resource Adapter");
		   mcf.setPageBufferSize(64);
	   }
	   catch(Exception fnise)
	   {
		   WriteLog.WriteToLog(fnise.getMessage());
	   }

	   return (ConnectionFactory) mcf.createConnectionFactory(new FN_IS_SpiConnectionManager());
	}
    
/**
 *  Gets called by the client requests, it authenticates the user at the starting
 *  of user session. This method tries to establish a connection to the RA using
 *  the passed in user name,password and keeps a reference to that new connection.
 *  If the userName,password are null, then this method calls the ConnectionFactory.getConnection().
 *  Else it calls ConnectionFactory.getConnection(ConnectionSpec). The first one
 *  is called as 'Container Managed Sign on' which should have appropriate settings in the
 *  application server. This is possible only in Managed environment.
 *  @param java.lang.String userName
 *					userName using which the clients wants to login.
 *  @param java.lang.String password
 *					password associated with the userName using which the clients wants to login.
 *  @param java.lang.String libraryName
 *					libraryName clients wants to login.
 *  @param java.lang.String loginMode
 *					Mode in which user has logs into Sample Application
 *  @return void
 *  @throws NamingException, ResourceException, Exception
 */
    public void initialize(String userName, String password,String libraryName, Integer loginType, Locale pCurrentLocale) throws ResourceException, Exception 
    {
        m_UserName = userName;
        m_Password = password;
        m_LibraryName = libraryName;
        m_LoginType = loginType;

        /* authenticating the user by making an connection to IS through RA */

        try
        {
        	login(userName, password, libraryName, loginType, pCurrentLocale);
            
            //getting the RA version and setting it
            ResourceAdapterMetaData raMetaData = m_ConnectionFactory.getMetaData();
            
            m_RAVersion = raMetaData.getAdapterVersion().toUpperCase();

            if (m_RAVersion.indexOf(WebConstants.VERSION_VIEW) == -1)
            {
            	m_RAVersion = WebConstants.VERSION_ENTERPRISE;
            }
            else
            {
            	m_RAVersion = WebConstants.VERSION_VIEW;
            }
        }
        catch(ResourceException re)
        {
            re.printStackTrace();
            throw re;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }
    }

/*
    This method sets InteractionSpecs parameter in interactionSpec (FN_IS_CciInteraction)
*/
    private void updateISpecWithISpecParams(FN_IS_CciInteractionSpec interactionSpec,Map paramsMap) throws ResourceException{

        /*  @param String fucntionName An String representing the name of Interaction. */
        if( paramsMap.containsKey(AppConstants.FUNCTION_NAME))
          interactionSpec.setFunctionName((String)paramsMap.get(AppConstants.FUNCTION_NAME));

        /*  @param int interactionVerb An integer representing the mode of interaction with an EIS instance
            as specified by the InteractionSpec. The values of interactionverb may be one of SYNC_SEND,
            SYNC_RECEIVE,SYNC_SEND_RECEIVE whose values are 0,1,2 respectively. */
	if( paramsMap.containsKey(AppConstants.INTERACTION_VERB))
            interactionSpec.setInteractionVerb(((Integer)paramsMap.get(AppConstants.INTERACTION_VERB)).intValue());

        /*  @param int executionTimeOut An integer representing the execution timeout for performing
            Interaction. */
        if( paramsMap.containsKey(AppConstants.EXECUTION_TIMEOUT))
            interactionSpec.setExecutionTimeout(((Long)paramsMap.get(AppConstants.EXECUTION_TIMEOUT)).intValue());

    }

/*
    This method sets ResultSet parameter in interactionSpec (FN_IS_CciInteraction)
*/
    private void updateISpecWithResultSetParams(FN_IS_CciInteractionSpec interactionSpec,Map paramsMap){

        /*@ param int fetchSize The constant denotes number of rows that should be fetched from the database when
            more rows are needed for this result set.*/
        if( paramsMap.containsKey(AppConstants.FETCH_SIZE))
	    interactionSpec.setFetchSize(((Integer)paramsMap.get(AppConstants.FETCH_SIZE)).intValue());

        /*@param int fetchDirection The constant indicating the direction in which the rows in a result set
          will be processed. The valid values are ResultSet.FETCH_FORWARD,ResultSet.FETCH_REVERSE,
          ResultSet.FETCH_UNKNOWN */
        if( paramsMap.containsKey(AppConstants.FETCH_DIRECTION))
    	    interactionSpec.setFetchDirection(((Integer)paramsMap.get(AppConstants.FETCH_DIRECTION)).intValue());

        /*@ param int resultsetType The constant indicates the type of result set.
            The valid values are TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE, or TYPE_SCROLL_SENSITIVE. */
	if( paramsMap.containsKey(AppConstants.RESULTSET_TYPE))
	    interactionSpec.setResultSetType(((Integer)paramsMap.get(AppConstants.RESULTSET_TYPE)).intValue());

        /*@param int concurrency The constant denotes the direction in which the rows in a result set
          will be processed. The valid values are CONCUR_READ_ONLY,CONCUR_UPDATABLE. */
	if( paramsMap.containsKey(AppConstants.CONCURRENCY))
	    interactionSpec.setResultSetConcurrency(((Integer)paramsMap.get(AppConstants.CONCURRENCY)).intValue());

    }

 /**
 *  Executes the getDocumentContent interaction on the Resource Adapter.
 *  This interaction would be used by the client application to retrieve the
 *  requested Document page content.
 *  @param Long lDocID
 *                                      The unique id of the document whose content the client wants to read.
 *  @param Integer iPageNo
 *                                      The number of the page, whose content the document wants to retrieve.
 *  @param Integer iPrefetch_count
 *                                      Number of additional pages to pre-fetch from the optical disk,
 *                                      to increase the relative access speed of the document.
 *  @param java.lang.String cache_name
 *                                      Name of the IS page_cache to migrate the page to. If not specified
 *                                      the default cache would be used.
 *  @param Integer iPolling_interval
 *                                      Time, in secs, between each polling attempt. This would be the amount
 *                                      of time after which the code would check to see if the doc is migrated or not.
 *                                      If not specified.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Map getDocumentContent(Long lDocID, Integer iPageNo, Integer iPrefetch_count,
                              String cache_name, Integer iPolling_interval,Integer iPollingInterval_in_millis,Integer iNoOfTimesObj,
                              Boolean enableChecksum, Map InteractionParmsMap)
                              throws ResourceException, Exception{

      try{
           m_RAResourceWarning = "";
			m_GetDocContentInteraction = m_Connection.createInteraction();

            FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

			updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                    interactionSpec.setFunctionName(AppConstants.GET_DOCUMENT_CONTENT_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS */
            MappedRecord mappedRecord =  m_RecordFactory.createMappedRecord(AppConstants.GET_DOCUMENT_CONTENT);
            mappedRecord.put(AppConstants.DOC_ID,lDocID);
            mappedRecord.put(AppConstants.PAGE_NUMBER,iPageNo);
            mappedRecord.put(AppConstants.PREFETCH_COUNT,iPrefetch_count);
            mappedRecord.put(AppConstants.PAGE_CACHE,cache_name);
            mappedRecord.put(AppConstants.POLLING_INTERVAL,iPolling_interval);
            mappedRecord.put(AppConstants.POLLING_INTERVAL_IN_MILLIS,iPollingInterval_in_millis);
            mappedRecord.put(AppConstants.NO_OF_TIMES_OBJECT,iNoOfTimesObj);
            mappedRecord.put(AppConstants.ENABLE_CHECKSUM,enableChecksum);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();
            MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getDocumentContent: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_DOCUMENT_CONTENT_RESULT);
  				m_GetDocContentInteraction.execute(interactionSpec,mappedRecord, outputRecord);
				resultMappedRecord = outputRecord;
            }
            else{
                resultMappedRecord = (MappedRecord)m_GetDocContentInteraction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("getDocumentContent: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(m_GetDocContentInteraction);

            m_InputStream = (InputStream)resultMappedRecord.get(AppConstants.STREAM);
            String mimeType = (String) resultMappedRecord.get(AppConstants.MIME_TYPE);
			String fileName = (String) resultMappedRecord.get(AppConstants.FILE_NAME);
			int totalContentSize = m_InputStream.available();

            /* retuning the Map with information like content size, MIME type, FileName */
			Map interactionExecutionMap = new HashMap();
			interactionExecutionMap.put(AppConstants.TOTAL_CONTENT_SIZE,new Long(totalContentSize));
			interactionExecutionMap.put(AppConstants.MIME_TYPE,mimeType);
			interactionExecutionMap.put(AppConstants.FILE_NAME,fileName);

			/*Code Added for DTS152824 */
            interactionExecutionMap.put(AppConstants.RESULT_INPUTSTREAM,m_InputStream);
			/*End of Code Added for DTS152824 */

			return interactionExecutionMap;

		}catch(ResourceException re){
                        re.printStackTrace();
			throw re;
		}catch(Exception e){
                        e.printStackTrace();
			throw new ResourceException(e.getMessage());
		}
    }

/*Added by harish for get Document Content2 Interaction */

/**
 *  Executes the getDocumentContent2 interaction on the Resource Adapter.
 *  This interaction would be used by the client application to retrieve the
 *  requested Document page content.
 *  @param Long lDocID
 *                                      The unique id of the document whose content the client wants to read.
 *  @param Integer iPageNo
 *                                      The number of the page, whose content the document wants to retrieve.
 *  @param Integer iPrefetch_count
 *                                      Number of additional pages to pre-fetch from the optical disk,
 *                                      to increase the relative access speed of the document.
 *  @param java.lang.String cache_name
 *                                      Name of the IS page_cache to migrate the page to. If not specified
 *                                      the default cache would be used.
 *  @param Integer iPolling_interval
 *                                      Time, in secs, between each polling attempt. This would be the amount
 *                                      of time after which the code would check to see if the doc is migrated or not.
 *                                      If not specified.
 *  @param Integer iLastPageNo
 *                                      The last number of the page, whose content the document wants to retrieve
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
  public synchronized Map getDocumentContent2(Long lDocID, Integer iPageNo, Integer iPrefetch_count,
                              String cache_name, Integer iPolling_interval,Integer iNoOfTimesObj,Integer iPollingInterval_in_millis,
                              Boolean enableChecksum,Integer iLastPageNo, Map InteractionParmsMap)
                              throws ResourceException, Exception{

      try{
           m_RAResourceWarning = "";
			m_GetDocContentInteraction2 = m_Connection.createInteraction();

            FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

			updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            //System.out.println("the function name inside the bean  before assign ''''''"+interactionSpec.getFunctionName());
			if(interactionSpec.getFunctionName() == null)
                    interactionSpec.setFunctionName(AppConstants.GET_DOCUMENT_CONTENT2_FUNCTION_NAME);
            //System.out.println("the function name inside the bean afterr ...."+interactionSpec.getFunctionName());
            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS */
            MappedRecord mappedRecord =  m_RecordFactory.createMappedRecord(AppConstants.GET_DOCUMENT_CONTENT);
            mappedRecord.put(AppConstants.DOC_ID,lDocID);
            mappedRecord.put(AppConstants.PAGE_NUMBER,iPageNo);
            mappedRecord.put(AppConstants.LAST_PAGE_NUMBER,iLastPageNo);
			mappedRecord.put(AppConstants.PREFETCH_COUNT,iPrefetch_count);
            mappedRecord.put(AppConstants.PAGE_CACHE,cache_name);
            mappedRecord.put(AppConstants.POLLING_INTERVAL,iPolling_interval);
            mappedRecord.put(AppConstants.POLLING_INTERVAL_IN_MILLIS,iPollingInterval_in_millis);
            mappedRecord.put(AppConstants.NO_OF_TIMES_OBJECT,iNoOfTimesObj);
            mappedRecord.put(AppConstants.ENABLE_CHECKSUM,enableChecksum);
            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();
            MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getDocumentContent in the client Bean : Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_DOCUMENT_CONTENT_RESULT);
  				m_GetDocContentInteraction2.execute(interactionSpec,mappedRecord, outputRecord);
				resultMappedRecord = outputRecord;
            }
            else{
                resultMappedRecord = (MappedRecord)m_GetDocContentInteraction2.execute(interactionSpec,mappedRecord);
            }
			m_RAResourceWarning = getWarningFromInteraction(m_GetDocContentInteraction2);
                        /*Code Commented For DTS 153017*/
                  //InputStream m_InputStream1 = (InputStream)resultMappedRecord.get(AppConstants.STREAM);
                        /*Code Commented For DTS 153017*/
   			String mimeType = (String) resultMappedRecord.get(AppConstants.MIME_TYPE);
			String fileName = (String) resultMappedRecord.get(AppConstants.FILE_NAME);
			Map restOfPages = new HashMap();
			restOfPages = (Map)resultMappedRecord.get(AppConstants.DOC_CONTENT_BUFFER_PAGES);
                         /*Code Commented For DTS 153017*/
                        //int totalContentSize = m_InputStream1.available();
                        /*Code Commented For DTS 153017*/
			Map interactionExecutionMap = new HashMap();
			interactionExecutionMap.put(AppConstants.DOC_CONTENT_BUFFER_PAGES,restOfPages);
                         /*Code Commented For DTS 153017*/
                        //interactionExecutionMap.put("stream",m_InputStream1);
			//interactionExecutionMap.put(AppConstants.TOTAL_CONTENT_SIZE,new Long(totalContentSize));
                          /*Code Commented For DTS 153017*/
			interactionExecutionMap.put(AppConstants.MIME_TYPE,mimeType);
			interactionExecutionMap.put(AppConstants.FILE_NAME,fileName);

			return interactionExecutionMap;

		}catch(ResourceException re){
                        re.printStackTrace();
			throw re;
		}catch(Exception e){
                        e.printStackTrace();
			throw new ResourceException(e.getMessage());
		}
    }

/*Added by harish for get Document Content2 Interaction */


 /**
 *  Executes the getNextWindow interaction on the Resource Adapter to get the next set of byte
 *  from Input Stream retrieved from IS RA. This interaction would be used by the client
 *  application to retrieve the requested Document page content.
 *  @return byte[]
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized byte[] getNextWindow() throws ResourceException, Exception{
	//Code added for DTS 201757
	try{
		m_GetMessage = GetMessageUtil.getInstance(m_pCurrentLocale);
		m_WindowSize = Integer.parseInt(m_GetMessage.customMessage(MessageConstants.WINDOW_SIZE))*ONE_KB;
	}catch(Exception exp){
		exp.printStackTrace();
	}
	//Code added for DTS 201757 ends

	byte[] bydata = null;
        if(m_InputStream != null && m_GetDocContentInteraction != null){
            try{
                int idataAvailable = m_InputStream.available();
                if( idataAvailable > 0){
                    if( idataAvailable > m_WindowSize ){
                        bydata = new byte[m_WindowSize];
                        m_InputStream.read(bydata);
                    }else{
                        bydata = new byte[idataAvailable];
                        m_InputStream.read(bydata);
                        m_InputStream.close();
                        m_GetDocContentInteraction.close();

                        m_InputStream = null;
                        m_GetDocContentInteraction = null;
                    }
                }
            }catch(ResourceException re){
                re.printStackTrace();
		throw re;
            }catch(Exception e){
                e.printStackTrace();
		throw new ResourceException(e.getMessage());
            }
        }
        return bydata;
    }


    /**
 *  Executes the getNextWindow interaction on the Resource Adapter to get the next set of byte
 *  from Input Stream retrieved from IS RA. This interaction would be used by the client
 *  application to retrieve the requested Document page content.
 *  @return byte[]
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized byte[] getNextWindow(InputStream m_Tis, boolean isLastPage) throws ResourceException, Exception{
      //int totalNoOfTIS = noOfTIS+1;
        byte[] bydata = null;
       // System.out.println("The Interaction  object is ::::::"+m_GetDocContentInteraction2);
         //System.out.println("Called for the ::::::::"+firstTimeCalled);
      //  m_GetDocContentInteraction = m_Connection.createInteraction();
        if(m_Tis != null && m_GetDocContentInteraction2 != null){
            try{
                if(!isISNull){

                int idataAvailable = m_Tis.available();
                if( idataAvailable > 0){
                // System.out.println("***************The winodw size is getNextwin**********"+m_WindowSize);
                    if( idataAvailable > m_WindowSize ){
                        bydata = new byte[m_WindowSize];
                        m_Tis.read(bydata);
                    }else{
                        bydata = new byte[idataAvailable];
                        m_Tis.read(bydata);
                        m_Tis.close();
                        m_Tis = null;
                        isISNull = true;
                        if(isLastPage){
                         isISNull = false;
                        m_GetDocContentInteraction2.close();
                        m_GetDocContentInteraction2 = null;
                        }
                        //Code added for WAS6 changes
                         logoff();
                       //Code added ENDS

                    }
                }
            }else{
              isISNull=false;
            }

            }catch(ResourceException re){
                re.printStackTrace();
                throw re;
            }catch(Exception e){
                e.printStackTrace();
                throw new ResourceException(e.getMessage());
            }
        }
        return bydata;
    }


/**
 *  Executes the Search interaction on the IS RA. This interaction would be used
 *  by the client application to retrieve data from all IS Document Index Records (DIRs)
 *  which would match the input query statement.
 *  @param java.lang.String query
 *  						A SQL query statement. The Select * syntax query would not be supported.
 *  @param java.lang.String foder_name
 *						The search would be performed in the folder name specified.
 *                                              If the name would be terminated with a /, then subfolders would also be searched.
 *  @param Integer max_rows
 *						The maximum number of rows to retrieve
 *  @param java.util.Map InteractionParmsMap
 *						Carries interaction request information as key-value pair.
 *  @throws javax.resource.ResourceException    If error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
     public synchronized List findDocuments(String query, String folder_name, Integer max_rows,
                        Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

        int ifetchSize=0;
        m_findDocResultSet = null;

        try{
            m_RAResourceWarning = "";
	    m_findDocInteraction = m_Connection.createInteraction();

            FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            Integer fetchSizeObject = (Integer) InteractionParmsMap.get(AppConstants.FETCH_SIZE);
            ifetchSize = fetchSizeObject.intValue();

            /* Assigning Interaction Parameters and Resultset parameters to CciInteractionSpec*/
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);
            updateISpecWithResultSetParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.FIND_DOCUMENTS_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord (AppConstants.FIND_DOCUMENTS);
            mappedRecord.put(AppConstants.QUERY, query);
            mappedRecord.put(AppConstants.FOLDER_NAME, folder_name);
            mappedRecord.put(AppConstants.MAX_ROWS, max_rows);

            List resultList;

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

            // System.out.println("findDocuments: Before entering interaction.execute method.");
            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord (AppConstants.FIND_DOCUMENTS_RESULT);
                boolean success = m_findDocInteraction.execute(interactionSpec,mappedRecord,outputRecord);
                m_findDocResultSet = (ResultSet) outputRecord;
            }
            else{
                m_findDocResultSet =(ResultSet)m_findDocInteraction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("findDocuments: After exiting interaction.execute method.");
            m_RAResourceWarning = getWarningFromInteraction(m_findDocInteraction);

            if(m_findDocResultSet == null) return null;

            resultList = getNextResultSet(ifetchSize);
            return resultList;

	}catch(ResourceException re){

            re.printStackTrace();
	    throw re;
	}catch(Exception e){
            e.printStackTrace();
	    throw new ResourceException(e.getMessage());
	}finally{
            /* Scenario when Max Rows is same as Fetch Size, resultset and interaction will be closed */
            if (max_rows!=null){
              try{
                if (max_rows.intValue()== ifetchSize) {
                    try{
                        if(m_findDocResultSet != null){
                          m_findDocResultSet.close();
                          m_findDocResultSet = null;
                        }
                    }catch(SQLException e){
                        throw new ResourceException(e.getMessage());
                    }
                    if( m_findDocInteraction != null) {
                        m_findDocInteraction.close();
                        m_findDocInteraction = null;
                    }
                }
              }catch(NumberFormatException ne){
                /* Don't do anything */
              }
            }
        }

    }
    /**
     *  Executes the Search interaction on the IS RA. This interaction would be used
     *  by the client application to retrieve data from all IS Document Index Records (DIRs)
     *  which would match the input query statement.
     *  @param java.lang.String query
     *  						A SQL query statement. The Select * syntax query would not be supported.
     *  @param java.lang.String foder_name
     *						The search would be performed in the folder name specified.
     *                                              If the name would be terminated with a /, then subfolders would also be searched.
     *  @param Integer max_rows
     *						The maximum number of rows to retrieve
     *  @param java.util.Map InteractionParmsMap
     *						Carries interaction request information as key-value pair.
     *  @throws javax.resource.ResourceException    If error occurs in the Resource Adapter during interaction.
     *  @throws java.lang.Exception
     */
         public synchronized List findDocumentsInOSAR(Long doc_id, Integer rel_op, Integer max_matches,
                            Map InteractionParmsMap) throws ResourceException, Exception{

            /* Cleaning the Resources before starting new Interaction */
            cleanUp();

         //   int ifetchSize=0;
         //   m_findDocResultSet = null;
Interaction interaction = null;
            try{
                m_RAResourceWarning = "";
                interaction = m_Connection.createInteraction();

                FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            //    Integer fetchSizeObject = (Integer) InteractionParmsMap.get(AppConstants.FETCH_SIZE);
              //  ifetchSize = fetchSizeObject.intValue();

                /* Assigning Interaction Parameters and Resultset parameters to CciInteractionSpec*/
                updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);
                updateISpecWithResultSetParams(interactionSpec,InteractionParmsMap);

                if(interactionSpec.getFunctionName() == null)
                    interactionSpec.setFunctionName(AppConstants.FIND_DOCUMENTS_IN_OSAR_FUNCTION_NAME);

                /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                MappedRecord mappedRecord = m_RecordFactory.createMappedRecord (AppConstants.FINDDOCLOCATION_INPUT_RECORD_NAME);
                mappedRecord.put(AppConstants.DOC_ID, doc_id);
                mappedRecord.put(AppConstants.RELATIONAL_OPERATOR, rel_op);
                mappedRecord.put(AppConstants.MAX_MATCHES, max_matches);

                List resultList;

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
                if(execute3Obj != null)
                    bexecute3 = execute3Obj.booleanValue();
                  IndexedRecord resultIndexedRecord = null;
                // System.out.println("findDocuments: Before entering interaction.execute method.");
                /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */

              if(bexecute3){
                    /* Creating the output record for the specific Interaction */
                    IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord(AppConstants.FINDDOCLOCATION_OUTPUT_RECORD_NAME);
                    boolean success = interaction.execute(interactionSpec,mappedRecord,outputRecord);
                    resultIndexedRecord = outputRecord;
                }
                else{
                    resultIndexedRecord = (IndexedRecord)interaction.execute(interactionSpec,mappedRecord);
                }
                // System.out.println("findDocuments: After exiting interaction.execute method.");
                m_RAResourceWarning = getWarningFromInteraction(interaction);

                Iterator iterator = resultIndexedRecord.iterator();

            List resList1 = new ArrayList();
            while(iterator.hasNext()){
                Map map1 = new HashMap();
                map1.putAll((Map)iterator.next());
                resList1.add(map1);
            }

            //Map resultMap = new HashMap();
            //resultMap.put(AppConstants.INTERACTION_DATA, resList1);
          //  return resultMap;

                return resList1;

            }catch(ResourceException re){

                re.printStackTrace();
                throw re;
            }catch(Exception e){
                e.printStackTrace();
                throw new ResourceException(e.getMessage());
            }finally{
              if( interaction != null) interaction.close();
            }

        }

/**
 *  Executes the Search interaction on the IS RA. This interaction would be used
 *  by the client application to retrieve data from all IS Document Index Records (DIRs)
 *  which would match the input query statement.
 *  This interaction is only called incase the server is WebSphere 5.0 since the
 *  interactions have to be closed at the end of execution of each method.
 *  The resultSet obtained from this interaction is displayed in one set itself
 *  on the UI.
 *  @param java.lang.String query
 *  						A SQL query statement. The Select * syntax query would not be supported.
 *  @param java.lang.String foder_name
 *						The search would be performed in the folder name specified.
 *                                              If the name would be terminated with a /, then subfolders would also be searched.
 *  @param Integer max_rows
 *						The maximum number of rows to retrieve
 *  @param java.util.Map InteractionParmsMap
 *						Carries interaction request information as key-value pair.
 *  @throws javax.resource.ResourceException    If error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
     public synchronized List findDocumentsWAS(String query, String folder_name, Integer max_rows,
                        Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

        int ifetchSize=0;
        ResultSet findDocResultSet = null;
//        Interaction findDocInteraction =null;
        try{
            m_RAResourceWarning = "";
	    m_findDocInteraction = m_Connection.createInteraction();

            FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters and Resultset parameters to CciInteractionSpec*/
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);
            updateISpecWithResultSetParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.FIND_DOCUMENTS_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord (AppConstants.FIND_DOCUMENTS);
            mappedRecord.put(AppConstants.QUERY, query);
            mappedRecord.put(AppConstants.FOLDER_NAME, folder_name);
            mappedRecord.put(AppConstants.MAX_ROWS, max_rows);

            List resultList;

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord (AppConstants.FIND_DOCUMENTS_RESULT);
                boolean success = m_findDocInteraction.execute(interactionSpec,mappedRecord,outputRecord);
                findDocResultSet = (ResultSet) outputRecord;
            }
            else{
                findDocResultSet =(ResultSet)m_findDocInteraction.execute(interactionSpec,mappedRecord);
            }
            m_RAResourceWarning = getWarningFromInteraction(m_findDocInteraction);

            if(findDocResultSet == null) return null;

            m_findDocResultSet = findDocResultSet;
            resultList = getNextResultSet(max_rows.intValue());

            return resultList;

	}catch(ResourceException re){
            throw re;
	}catch(Exception e){
            throw new ResourceException(e.getMessage());
	}finally{
            /* For WAS 5.0, Scenario when Max Rows is greater than zero, resultset and interaction have to be closed */
            if( m_findDocInteraction != null) {
                m_findDocInteraction.close();
                m_findDocInteraction = null;
            }
        }
    }

/**
 *  This method gets the Next Set (no. of records = fetchsize) of records from the ResultSet.
 *  @param int fetchSize
 *  			 No of records to be fetched at one pass
 *                       = 0; ResultSet will be closed
 *  @return java.util.List
 *			 Returns the List of records.
 *  @throws javax.resource.ResourceException    If error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized List getNextResultSet(int ifetchSize) throws ResourceException, Exception{

        List resultList = new ArrayList();
        DecimalFormat df = new DecimalFormat(WebConstants.STR_DECIMAL);

        try{
            if (ifetchSize == 0){
                if(m_findDocResultSet != null){
                    m_findDocResultSet.close();
                    m_findDocResultSet = null;
                }
                if( m_findDocInteraction != null){
                    m_findDocInteraction.close();
                    m_findDocInteraction = null;
                }
                return null;
            }
        }catch(SQLException e){
            // don't raise error, but set objects to NULL
            if(m_findDocResultSet != null) m_findDocResultSet = null;
            if(m_findDocInteraction != null) m_findDocInteraction = null;
            return null;
        }

        try{
            if (m_findDocResultSet == null) return null;

            ResultSetMetaData resultSetMetaData = m_findDocResultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            /*  Add the column names in an ColumnList and add it to another LIst ResultList
                as the first element of the List. */
            List columnList = new ArrayList(columnCount);
            for(int index = 1; index <= columnCount; index++){
                columnList.add(resultSetMetaData.getColumnName(index));
            }
            resultList.add(columnList);

            //Add the data into ColumnList and add them into the resultList.
            for(int iRow = 1; iRow <= ifetchSize; iRow++){
                if (m_findDocResultSet.next()){
                    List dataList = new ArrayList(columnCount);
                    for( int index = 1; index <= columnCount; index++){
                        if (resultSetMetaData.getColumnType(index)==Types.DECIMAL){
                          if (m_findDocResultSet.getObject(index) != null){
                              dataList.add(df.format(m_findDocResultSet.getObject(index)));
                          }else{
                              dataList.add(m_findDocResultSet.getObject(index));
                          }
                        }else{
                            dataList.add(m_findDocResultSet.getObject(index));
                        }
                    }
                    resultList.add(dataList);
                }
                else{
                    if(m_findDocResultSet != null)
                        m_findDocResultSet.close();
                        m_findDocResultSet = null;
                    if( m_findDocInteraction != null){
                        m_findDocInteraction.close();
                        m_findDocInteraction = null;
                        break;
                    }
                }
            }
        }catch(SQLException e){
            throw new ResourceException(e.getMessage());
        }
	return resultList;
    }

/**
 *  Executes the getDocClassIndices interaction on the IS RA. This interaction would
 *  be used by the client application to retrieve all indices associated with a
 *  specified Document Class, including their associated data types
 *  @param java.lang.String docclassname
 *                                              DocClassName for which indices has to be retrieved
 *  @param java.util.Map InteractionParmsMap
 *						Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Map getDocClassIndices(String docclassname, Map InteractionParmsMap)
                                                          throws ResourceException, Exception {
        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

	Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
            interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.GET_DOC_CLASS_INDICES_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
	    MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_DOC_CLASS_INDICES);
            mappedRecord.put(AppConstants.DOCCLASS_NAME,docclassname);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

            IndexedRecord resultIndexedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getDocClassIndices: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord(AppConstants.GET_DOC_CLASS_INDICES_RESULT);
                interaction.execute(interactionSpec,mappedRecord,outputRecord);
                resultIndexedRecord = outputRecord;

            }
            else{
                resultIndexedRecord = (IndexedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("getDocClassIndices: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            List resultList = new ArrayList();
            Iterator iterator = resultIndexedRecord.iterator();
            Map map;
            while(iterator.hasNext()){
                map = new HashMap();
                map.putAll((Map)iterator.next());
                resultList.add(map);
            }

            Map resultMap = new HashMap();
            resultMap.put(AppConstants.INTERACTION_DATA, resultList);

	    return resultMap;

        }catch(ResourceException re){
            re.printStackTrace();
            throw re;
        }catch(Exception e){
            e.printStackTrace();
	    throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null) interaction.close();
        }

    }

/**
 *  Executes the getMenuValue interaction on the IS RA. This interaction would be
 *  used by the client application to retrieve a menu value for the specified menu label.
 *  @param String indexName
 *                                      Name of the index field to which the menu label requested for applies.
 *  @param String indexName
 *                                      The menu label for which the client is requesting the value. Used in both input and output records.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Map getMenuValue(String indexName, String menuLabel, Map InteractionParmsMap)
                                                        throws ResourceException, Exception{

        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
            interaction = m_Connection.createInteraction();

            FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.GET_MENU_VALUE_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_MENU_VALUE);
            mappedRecord.put(AppConstants.INDEX_NAME, indexName);
            mappedRecord.put(AppConstants.MENU_LABEL, menuLabel);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getMenuValue: Before entering interaction.execute method.");
            if(bexecute3){
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_MENU_VALUE_RESULT);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
  	        resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("getMenuValue: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

	    Map resultMap = new HashMap();
	    resultMap.putAll(resultMappedRecord);
            return resultMap;

        }catch(ResourceException re){
            re.printStackTrace();
	    throw re;
        }catch(Exception e){
            e.printStackTrace();
	    throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }
    }

/**
 *  Executes the getDocumentProperties interaction on the IS RA. This Interaction
 *  would be used by the client to retrieve the properties of a document in the IS server.
 *  This interaction is called incase the server is any server other than
 *  WebSphere 5.0
 *  @param Long docID
 *		Document ID of the document whose properties are to be retrieved.
 *  @param boolean isLockDesired
 *		Specifies if it is required to lock the properties for update.
 *  @param java.util.Map InteractionParmsMap
 *		Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Map getDocumentProperties(Long docID, boolean isLockDesired, Map InteractionParmsMap)
                                                              throws ResourceException, Exception{

        try{
            m_RAResourceWarning = "";
            m_getDocumentPropInteraction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.GET_DOC_PROPERTIES_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_DOCUMENT_PROPERTIES);
            mappedRecord.put(AppConstants.DOC_ID, docID);
            mappedRecord.put(AppConstants.IS_LOCK_DESIRED, new Boolean(isLockDesired));

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getDocumentProperties: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_DOCUMENT_PROPERTIES_RESULT);
	    	m_getDocumentPropInteraction.execute(interactionSpec,mappedRecord,outputRecord);
                resultMappedRecord = outputRecord;
            }
            else{
		resultMappedRecord = (MappedRecord)m_getDocumentPropInteraction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("getDocumentProperties: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(m_getDocumentPropInteraction);

	    Map resultMap = new HashMap();
            List resultList = new ArrayList();

            IndexedRecord indexedRecord = (IndexedRecord) resultMappedRecord.get(AppConstants.DOC_PROPERTIES);

            Iterator iterator = indexedRecord.iterator();

            Map map;
            while(iterator.hasNext()){
                map = new HashMap();
                map.putAll((Map)iterator.next());
                resultList.add(map);
            }

            /* Passing DocProperties as a List and Item Lock as a array of long to JSP */
            resultMap.put(AppConstants.INTERACTION_DATA, resultList);
            resultMap.put(AppConstants.ITEM_LOCK, resultMappedRecord.get(AppConstants.ITEM_LOCK));

            return resultMap;

        }catch(ResourceException re){
            re.printStackTrace();
	    throw re;
        }catch(Exception e){
            e.printStackTrace();
	    throw new ResourceException(e.getMessage());
        }
    }


/**
 *  Executes the CancelDocPropertiesUpdate interaction on the RA. This interaction
 *  would be used by the client to cancel a pending update. It would also release
 *  a previously acquired lock on the document's properties.
 *  This interaction is called incase the server is any server other than WebSphere 5.0
 *  @param java.util.Map docProperties
 *                                        Only F_DOCNUMBER index will be passed
 *  @param long[] capability
 *                                        It is the IS capability structure
 *  @param java.util.Map InteractionParmsMap
 *		                          Carries interaction request information as key-value pair.
 *  @return long
 *                                        Generic result, 0 if success OR errorcode if there is an error
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long CancelDocumentProperties(Map docProperties, long[] capability, Map InteractionParmsMap)
                                                            throws ResourceException, Exception{
        if (m_getDocumentPropInteraction == null)
            throw new ResourceException(m_GetMessage.customMessage(MessageConstants.MSG_INTERACTION_ALREADY_CLOSED));

        try{
            m_RAResourceWarning = "";
	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.CANCEL_DOC_PROPERTIES_FUNCTION_NAME);

            /* creating DocProperties Indexed Record of DocProperty mapped record having just doc_id */
            MappedRecord docPropertyRecord = m_RecordFactory.createMappedRecord(AppConstants.CANCEL_DOC_PROPERTIES_DOC_PROPERTY);
            docPropertyRecord.putAll(docProperties);

            IndexedRecord docPropertiesRecord = m_RecordFactory.createIndexedRecord(AppConstants.CANCEL_DOC_PROPERTIES_DOC_PROPERTIES);
            docPropertiesRecord.add(docPropertyRecord);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.CANCEL_DOC_PROPERTIES);
            mappedRecord.put(AppConstants.DOC_PROPERTIES, docPropertiesRecord);
            mappedRecord.put(AppConstants.ITEM_LOCK, capability);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("CancelDocumentProperties: Before entering interaction.execute method.");
            if(bexecute3){
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.CANCEL_DOC_PROPERTIES_RESULT);
	    	m_getDocumentPropInteraction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
                resultMappedRecord = (MappedRecord)m_getDocumentPropInteraction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("CancelDocumentProperties: After exiting interaction.execute method.");
            m_RAResourceWarning = getWarningFromInteraction(m_getDocumentPropInteraction);

            long lresult = new Long(resultMappedRecord.get(AppConstants.RESULT).toString()).longValue();
            return lresult;

        }catch(ResourceException re){
            re.printStackTrace();
	    throw re;
        }catch(Exception e){
            e.printStackTrace();
	    throw new ResourceException(e.getMessage());
        }finally{
	    if(m_getDocumentPropInteraction != null)
                m_getDocumentPropInteraction.close();
                m_getDocumentPropInteraction = null;
        }
    }

 /**
 *  Executes the DeleteDocs interaction on the Resource Adapter.
 *  This interaction will be used to delete the specified documents from the IS server.
 *  @param java.util.Vector docSet
 *                                      This would be an list of doc_id
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Map DeleteDocs(Vector docSet, Map InteractionParmsMap)
                                      throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();
	Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
            interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.DELETE_DOC_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            IndexedRecord indexedRecord = m_RecordFactory.createIndexedRecord(AppConstants.DELETE_DOCS);

            /* Adding all parameters to Indexed Records */
            if (docSet != null){
                for (int j=0; j< docSet.size();j++) indexedRecord.add(docSet.get(j));
            }

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    IndexedRecord resultIndexedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("DeleteDocs: Before entering interaction.execute method.");
            if(bexecute3){
                IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord(AppConstants.DELETE_DOCS_RESULT);
	    	interaction.execute(interactionSpec,indexedRecord,outputRecord);
		resultIndexedRecord = outputRecord;
            }
            else{
		resultIndexedRecord = (IndexedRecord)interaction.execute(interactionSpec,indexedRecord);
            }
            // System.out.println("DeleteDocs: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            Iterator iterator = resultIndexedRecord.iterator();

	    List resList1 = new ArrayList();
            while(iterator.hasNext()){
                Map map1 = new HashMap();
		map1.putAll((Map)iterator.next());
                resList1.add(map1);
	    }

            Map resultMap = new HashMap();
            resultMap.put(AppConstants.INTERACTION_DATA, resList1);
            return resultMap;

        }catch(ResourceException re){
            re.printStackTrace();
            throw re;
        }catch(Exception e){
            e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }
    }

/**
 *  Executes the AddDoc interaction on the Resource Adapter.
 *  This Interaction would be used by the client to add a new document to the Image Services.
 *  @param java.lang.String docClassName
 *                                Name of the document's class
 *  @param java.lang.Short docType
 *                                Type of document
 *  @param java.lang.String cacheName
 *                                Name of an IS page cache
 *  @param java.util.List docProperties
 *                                Doc Class Properties of the Doc Class document has to be added.
 *  @param java.io.InputStream[] pageStreams
 *                                One stream for each document page
 *  @param java.util.Set folderSet
 *                                List of folders document has to be added
 *  @param java.lang.String docFamilyName
 *                                Family name for the media surface
 *  @param boolean is_Duplication_OK
 *                                Specifies whether duplicate document can be added to IS
 *  @param java.lang.Long sec_Read
 *                                Read access attribute
 *  @param java.lang.Long sec_Write
 *                                Write access attribute
 *  @param java.lang.Long sec_Append_Execute
 *                                Append or execute access attribute
 *  @param java.util.Map InteractionParmsMap
 *				  Carries interaction request information as key-value pair.
 *  @return long
 *                                Doc ID if document is added successfully, else Error Id
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long AddDoc(String docClassName, Short docType, String cacheName, List docProperties,
                                    InputStream[] pageStreams, Set folderSet, String docFamilyName,
                                    boolean is_Duplication_OK, Long sec_Read, Long sec_Write, Long sec_Append_Execute,
                                    Boolean enableChecksum, Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

	Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.ADD_DOC_FUNCTION_NAME);

            Collection colDocProperties = (Collection) docProperties;

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord =  m_RecordFactory.createMappedRecord(AppConstants.ADD_DOCUMENT);

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
            IndexedRecord folderSetRecord=  m_RecordFactory.createIndexedRecord(AppConstants.ADD_DOCUMENT_FOLDER_SET);
            if (folderSet!=null){
                Iterator iteratorFolder = folderSet.iterator();
                while(iteratorFolder.hasNext()){
                    folderSetRecord.add (iteratorFolder.next());
                }
            }
            mappedRecord.put(AppConstants.FOLDER_SET, folderSetRecord);

            // creating a Indexed record "DocProperties" of Mapped Record "DocProperty"
            IndexedRecord docPropertiesRecord = m_RecordFactory.createIndexedRecord(AppConstants.ADD_DOCUMENT_DOC_PROPERTIES);

            ListIterator iter = docProperties.listIterator();
            while (iter.hasNext()){
                Map docProp = (Map)iter.next();
                MappedRecord docPropertyRecord = m_RecordFactory.createMappedRecord(AppConstants.ADD_DOCUMENT_DOC_PROPERTY);
                docPropertyRecord.putAll(docProp);
                docPropertiesRecord.add(docPropertyRecord);
            }
            mappedRecord.put(AppConstants.INDEX_RECORDS, docPropertiesRecord);

            mappedRecord.put(AppConstants.ENABLE_CHECKSUM, enableChecksum);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("Add Doc: Before entering interaction.execute method.");
            if(bexecute3){
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.ADD_DOCUMENT_RESULT);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
		resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("Add Doc: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            long lresultDocID = new Long((resultMappedRecord.get(AppConstants.DOC_ID)).toString()).longValue();

            return lresultDocID;

        }catch(ResourceException re){
            re.printStackTrace();
            throw re;
        }catch(Exception e){
            e.printStackTrace();
	    throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }

    }

 /**
 *  Executes the getFolderAttributes interaction on the Resource Adapter.
 *  This interaction would be used to get the attributes of the folder specified.
 *  @param ja va.lang.String folderName
 *                                      Folder Name for which the attributes is to be obtained.
 *  @param java.lang.Long folderId
 *                                      Folder ID for which the attributes is to be obtained.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Map getFolderAttributes(String folderName, Long folderId,
                              Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.GET_FOLDER_ATTRIBUTES_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_FOLDER_ATTRIBUTES_INPUT_RECORD);
            mappedRecord.put(AppConstants.F_FOLDER_NAME, folderName);
            mappedRecord.put(AppConstants.F_FOLDER_ID, folderId);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_FOLDER_ATTRIBUTES_OUTPUT_RECORD);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
	        resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("FileDocsInFolder: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            //long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();
            Map folderProperties = new HashMap();
            folderProperties.putAll(resultMappedRecord);

            return folderProperties;

        }catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }

    }

 /**
 *  Executes the getDocFolders interaction on the Resource Adapter.
 *  This interaction would be used by the client to retrieve the set of folders
 *  in which a specified document is filed.
 *  @param java.lang.Long lDocID
 *                                      The doc_id whose folders need to be retrieved.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Vector
 *                                      List of Folders specified document are filed
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized Vector getDocFolders(Long ldocID, Map InteractionParmsMap)
                                        throws ResourceException, Exception{
        /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    /* Assigning Interaction Parameters to CciInteractionSpec */
            FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.GET_DOC_FOLDER_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_DOC_FOLDERS);
            mappedRecord.put(AppConstants.DOC_ID, ldocID);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    IndexedRecord resultIndexedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getDocFolders: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord(AppConstants.GET_DOC_FOLDERS_RESULT);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultIndexedRecord = outputRecord;
            }
            else{
	        resultIndexedRecord = (IndexedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("getDocFolders: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            Vector resultFolders = new Vector();
            Iterator iterator = resultIndexedRecord.iterator();
            while(iterator.hasNext()){
                resultFolders.add(iterator.next());
	    }

            return resultFolders;

        }catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }
    }

 /**
 *  Executes the browse interaction on the Resource Adapter.
 *  This interaction would be used to retrieve the properties of the sub folders inside a specified folder.
 *  @param java.lang.String folderName
 *                                      Name of the folder the contents of which would be retrieved.
 *  @param java.lang.Integer maxLimit
 *                                      Maximum number of items to return.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
     public synchronized Map getFolderFolders(String folderName, Integer maxLimit, Map InteractionParmsMap)
                                                      throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;
        ResultSet resultSet = null;
        List resultList = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.GET_FOLDER_FOLDERS_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_FOLDER_FOLDERS);
            mappedRecord.put(AppConstants.FOLDER_NAME, folderName);
            mappedRecord.put(AppConstants.MAX_LIMIT, maxLimit);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("getFolderFolders: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_FOLDER_FOLDERS_RESULT);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
	        resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("getFolderFolders: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            Map resultMap = new HashMap();

            resultSet = (ResultSet) resultMappedRecord.get(AppConstants.CONTAINED_FOLDERS);

            if (resultSet!=null) resultList = FileNETUtils.getListFromResultSet(resultSet);

            resultMap.put(AppConstants.CONTAINED_FOLDERS, resultList);
            resultMap.put(AppConstants.BASE_FOLDER_NAME, resultMappedRecord.get(AppConstants.BASE_FOLDER_NAME));

            return resultMap;

        }catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            try{
		if(resultSet != null)
	      	    resultSet.close();
            }catch(SQLException e){
                throw new ResourceException(e.getMessage());
	    }
            if( interaction != null) interaction.close();
        }
    }

 /**
 *  Executes the RemoveDocsFromFolders interaction on the Resource Adapter.
 *  This interaction would be used by the client application to retrieve the
 *  requested Document page content.
 *  @param java.lang.String folderName
 *                                      Destination folder.
 *  @param java.lang.Long[] docSet
 *                                      List of Documents to be removed.
 *  @param java.lang.Long placeAfter
 *                                      List of Documents to be removed.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return long
 *                                      If 0, it represents Success; else  the FileNET error_typ
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long RemoveDocsFromFolder(String folderName, Long[] docSet, Long placeAfter, Map InteractionParmsMap)
                                                          throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
            interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.REMOVE_DOC_FROM_FOLDER_FUNCTION_NAME);

            /* Creating Indexed record for DocSet to be passed to IS RA */
            IndexedRecord doc_set= m_RecordFactory.createIndexedRecord(AppConstants.REMOVE_DOCS_FROM_FOLDER_DOC_SET);

            if (docSet!=null){
                for(int i=0; i<docSet.length; i++) doc_set.add(docSet[i]);
            }

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.REMOVE_DOCS_FROM_FOLDER);
            mappedRecord.put(AppConstants.DESTINATION_FOLDER, folderName);
            mappedRecord.put(AppConstants.DOCUMENTS, doc_set);
            mappedRecord.put(AppConstants.PLACE_AFTER, placeAfter);     /* place_after is always null for interaction RemoveDocsFromFolder */

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("RemoveDocsFromFolder: Before entering interaction.execute method.");
	    if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.REMOVE_DOCS_FROM_FOLDER_RESULT);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }else{
		resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("RemoveDocsFromFolder: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            long lresult = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

            return lresult;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null) interaction.close();
        }

    }

 /**
 *  Executes the UpdateDocProperties interaction on the Resource Adapter.
 *  This Interaction will be used by the client to update the specified properties of a document.
 *  This interaction is called incase the server is any server other
 *  than WebSphere 5.0
 *  @param java.util.List docProperties
 *                                      Properties to update
 *  @param long[] capability
 *                                      It is the capability structure.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return long
 *                                      This will be the FileNET error tuple.
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long UpdateDocProperties(List docProperties, long[] capability, Map InteractionParmsMap)
                                                          throws ResourceException, Exception{
        if (m_getDocumentPropInteraction == null)
            throw new ResourceException(m_GetMessage.customMessage(MessageConstants.MSG_INTERACTION_ALREADY_CLOSED));

        try{
            m_RAResourceWarning = "";
	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.UPDATE_DOC_PROPERTIES_FUNCTION_NAME);

            /* creating DocProperties Indexed Record of DocProperty mapped record having all document properties
               - docProperties is a list containing map for document properties */
            IndexedRecord docPropertiesRecord = m_RecordFactory.createIndexedRecord(AppConstants.UPDATE_DOC_PROPERTIES_DOC_PROPERTIES);

            ListIterator iter = docProperties.listIterator();
            while (iter.hasNext()){
                Map docProp = (Map)iter.next();
                MappedRecord docPropertyRecord = m_RecordFactory.createMappedRecord(AppConstants.UPDATE_DOC_PROPERTIES_DOC_PROPERTY);
                docPropertyRecord.putAll(docProp);
                docPropertiesRecord.add(docPropertyRecord);
            }

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.UPDATE_DOC_PROPERTIES);

            /* Adding all parameters to paramsMap */
            mappedRecord.put(AppConstants.DOC_PROPERTIES, docPropertiesRecord);
            mappedRecord.put(AppConstants.ITEM_LOCK, capability);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("UpdateDocProperties: Before entering interaction.execute method.");
            if(bexecute3){
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.UPDATE_DOC_PROPERTIES_RESULT);
	    	m_getDocumentPropInteraction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
		resultMappedRecord = (MappedRecord) m_getDocumentPropInteraction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("UpdateDocProperties: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(m_getDocumentPropInteraction);

            long lresult = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

            return lresult;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( m_getDocumentPropInteraction != null)
		m_getDocumentPropInteraction.close();
                m_getDocumentPropInteraction = null;
	}

    }

//Method for F_CLOSED
    public synchronized long UpdateFClosed(Long doc_id, Boolean new_fclosed, boolean isLockDesired, Map InteractionParmsMap)
            throws ResourceException, Exception {

        Interaction interaction = null;






        try {
            m_RAResourceWarning = "";
            interaction = m_Connection.createInteraction();

            FN_IS_CciInteractionSpec interactionSpec = new
                    FN_IS_CciInteractionSpec();

            if (interactionSpec.getFunctionName() == null) {
                interactionSpec.setFunctionName(AppConstants.
                                                F_CLOSED_FUNCTION_NAME);
            }

            //Creating Input Mapped Record
            MappedRecord inputRecord = m_RecordFactory.createMappedRecord(
                       AppConstants.F_CLOSED_INPUT_RECORD);

               inputRecord.put(AppConstants.DOC_ID, doc_id);               
               inputRecord.put(AppConstants.NEW_FCLOSED, new_fclosed);
               inputRecord.put(AppConstants.IS_LOCK_DESIRED,
                             new Boolean(isLockDesired));

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean) InteractionParmsMap.get(
                        AppConstants.INTERACTION_EXECUTE_3);

                if (execute3Obj != null) {
                    bexecute3 = execute3Obj.booleanValue();
                }
                MappedRecord resultMappedRecord = null;

                if (bexecute3) {
                    MappedRecord outputRecord = m_RecordFactory.createMappedRecord(
                            AppConstants.UPDATE_DOC_PROPERTIES_RESULT);
                    interaction.execute(interactionSpec,inputRecord, outputRecord);
                    resultMappedRecord = outputRecord;
                } else {
                    resultMappedRecord = (MappedRecord) interaction.execute(
                            interactionSpec, inputRecord);
                }
            m_RAResourceWarning = getWarningFromInteraction(interaction);

            long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.
                    RESULT)).toString()).longValue();

            return lresultErrorID;

        } catch (ResourceException re) {
            re.printStackTrace();
            throw re;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResourceException(e.getMessage());
        } finally {
            if (interaction != null) {
                interaction.close();
            }
        }
     }
     //Method for F_CLOSED end

/*
    This method should be called when the client no more needs the clientbean object. This method will
    be called 1)directly by the client when he logs off 2)when this object is unbound from the HTTP
    session of the user if this is used in a web container as a session object.
    This method free the associated resources.
*/

/**
 *  Cleans up the Class Level Resources. Used to cleanup before user switches to other interaction.
 *  @param None
 *  @return None
 */
    private void cleanUp() throws ResourceException{
        try{
            if (m_InputStream!=null){
                m_InputStream.close();
                m_InputStream = null;
            }
        }catch(IOException io){
            // do nothing, Ignore the error and proceed for further cleanup
        }

        if (m_GetDocContentInteraction!=null) {
            m_GetDocContentInteraction.close();
            m_GetDocContentInteraction = null;
        }

        if (m_getDocumentPropInteraction!=null) {
            m_getDocumentPropInteraction.close();
            m_getDocumentPropInteraction = null;
        }

        try{
            if(m_findDocResultSet != null){
                m_findDocResultSet.close();
                m_findDocResultSet = null;
            }
        }catch(SQLException e){
            // do nothing, Ignore the error and proceed for further cleanup
        }
        if (m_findDocInteraction!=null){
            m_findDocInteraction.close();
            m_findDocInteraction = null;
        }
    }

/**
 *  Releases all the associated resources after closing the CciConnection associated with the client.
 *  @param None
 *  @return None
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.rmi.RemoteException if error occurs during method invocation on the IS RA.
 */
    public synchronized void logoff() throws ResourceException{

        try{
            cleanUp();
            /* commented for WAS 6
            if(m_Connection != null)
               m_Connection.close();
             */
            //Code added for WAS6 changes
            if (!m_ConnectionKeepAlive) {
              if (m_Connection != null) {
                m_Connection.close();
                m_Connection = null;
              }
            }
            else {
              if (m_Connection != null)
                m_Connection.close();
              m_Connection = null;
            }
           //Code added ENDS

       }catch(ResourceException re){
            throw new ResourceException(re.getMessage());
        }catch(Exception e){
            // do nothing, ignore the error
        }
    }

/**
 *  Returns the UserName that was passed in the initialize() method.
 *  @param None
 *  @return java.lang.String
 */
	public String getUserName(){
        if(m_LoginType.intValue()==1){
          String username =m_UserName.toUpperCase();
          return WebConstants.LDAP + username;
       }
         return m_UserName;
	}
/**
 *  Returns the name of the library into which the user has logged in.
 *  @param None
 *  @return java.lang.String
 */
	public String getLibraryName(){
		return m_LibraryName;
	}

/**
 *  Returns the name of the library into which the user has logged in.
 *  @param None
 *  @return java.lang.String
 */
	public String getRAVersion(){
		return m_RAVersion;
	}

/**
 *  Gets the Resource Warnings from Resource Adapters for interaction.
 *  @param Interaction interaction
 *  @return java.lang.String
 */
    private String getWarningFromInteraction(Interaction interaction) throws ResourceException{
	ResourceWarning resourceWarning = interaction.getWarnings();
	String warningMsg = "";
	while( resourceWarning != null ){
    	    warningMsg += resourceWarning.getMessage();
	    resourceWarning = resourceWarning.getLinkedWarning();
        }
	return (warningMsg.trim());
    }

/**
 *  Returns the Resource Warnings for specific interactions
 *  @param None
 *  @return java.lang.String
 */
    public String getRAResourceWarning(){
        return (m_RAResourceWarning!=null?m_RAResourceWarning:new String(""));
    }


/**
 *  Executes the GetMenuDescs interaction on the Resource Adapter.
 *  This interaction would be used to retrieve the Menu Description for a specific index name.
 *  @param java.lang.String index_name
 *                                Name of the Index for which the menu desription set is requested.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.List :Returns the list of values of MenuItems.
 */
    public List getMenuDesc(String index_name, Map InteractionParmsMap) throws ResourceException{

        Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                      interactionSpec.setFunctionName(AppConstants.GET_MENU_DESC_FUNCTION_NAME);

              /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
              MappedRecord indexName = m_RecordFactory.createMappedRecord(AppConstants.IndexName);
              indexName.put(AppConstants.INDEX_NAME, index_name);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

	      IndexedRecord menuItemSet = null;
              /* Executes the interaction, checks Interaction_Execute_3 flag and then calls the corresponding interaction.execute method*/
              // System.out.println("getMenuDescs: Before entering interaction.execute method.");
              if(bexecute3){
                  IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord (AppConstants.GET_MENU_DESCS_RESULT);
                  interaction.execute(interactionSpec,indexName, outputRecord);
                  menuItemSet = outputRecord;
             }else{
                  menuItemSet = (IndexedRecord)interaction.execute(interactionSpec, indexName);
             }

              // System.out.println("getMenuDescs: After exiting interaction.execute method.");
              m_RAResourceWarning = getWarningFromInteraction(interaction);

              List resultMenuItemList = new ArrayList();
              Iterator iterator = menuItemSet.iterator();

               Map tempmap;
	        while(iterator.hasNext()){
                        tempmap = new HashMap();
                        tempmap.putAll((Map)iterator.next());
                        resultMenuItemList.add(tempmap);
                }
               return resultMenuItemList;
        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }finally{
            if(interaction != null) interaction.close();
        }

    }

/**
 *  This method calls on the GetMenuDescs interaction and uses its output record
 *  to find the Menu Label for any passed Index Name.
 *  This interaction would be used in AddDoc interaction, DisplayDocProperties interaction and UpdateDocProperties interaction.
 *  @param java.lang.String index_name
 *                                Name of the Index for which the menu Label is requested.
 *  @param java.lang.String menu_value
 *                                Value of the Menu type for which the menu Label is requested
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return String
 *              Returns a Menu Label corresponding to the input.
 */
    public String getMenuLabel(String index_name, Integer menu_value, Map InteractionParmsMap) throws ResourceException{

        List menuDescsList = this.getMenuDesc(index_name, InteractionParmsMap);
        Iterator iterator = menuDescsList.iterator();
        String menuLabelStr = null;
        int menu_value1 = menu_value.intValue();
        if (menu_value1 <= 0){
                  return menuLabelStr;
        } else {
              while (iterator.hasNext() && menuLabelStr == null){
                      Map map=(Map) iterator.next();
                      int ltempVal = new Integer(map.get(AppConstants.MENU_VALUE).toString()).intValue();
                      if(ltempVal == menu_value1){
                              menuLabelStr = (String)map.get(AppConstants.MENU_LABEL);
                              break;
                      }
              }
        }
         return menuLabelStr;
    }

/**
 *  This method calls on the GetMenuDescs interaction and uses its output record
 *  to find the Menu Label for any passed Index Name.
 *  This interaction would be used in AddDoc interaction, DisplayDocProperties interaction and UpdateDocProperties interaction.
 *  @param java.lang.String index_name
 *                                Name of the Index for which the menu Label is requested.
 *  @param java.lang.String menu_value
 *                                Value of the Menu type for which the menu Label is requested
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return String
 *              Returns a Menu Label corresponding to the input.
 */
    public List getIndexNamesList(Map InteractionParmsMap) throws ResourceException{

        List docClassList = new ArrayList();
        try{
        docClassList = this.getDocClassDesc(null,null,InteractionParmsMap);
        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }
        String docClassName = "";
       	Iterator iterator = docClassList.iterator();
        List menuTypeList = new ArrayList();
        while (iterator.hasNext()){
                      Map map=(Map) iterator.next();
                      docClassName = (String)map.get(AppConstants.DOC_CLASS_NAME).toString();
                      Map docClassIndices = new HashMap();
                      try{
                      docClassIndices = this.getDocClassIndices(docClassName, InteractionParmsMap);
                      }catch(ResourceException re){
                                  re.printStackTrace();
                                  throw re;
                      }catch(Exception e){
                                  e.printStackTrace();
                                  throw new ResourceException(e.getMessage());
                      }
                      List docClassIndicesIndexedRecords = (List) docClassIndices.get(AppConstants.INTERACTION_DATA);
					  ListIterator listIterator = docClassIndicesIndexedRecords.listIterator();
                      short nIndexType = 0;
                      while(listIterator.hasNext()){
						Map indexNameTypeMap =(Map)listIterator.next();
					    nIndexType = new Short(indexNameTypeMap.get(AppConstants.INDEX_TYPE).toString()).shortValue();
                        if(nIndexType == 52)menuTypeList.add(indexNameTypeMap.get(AppConstants.INDEX_NAME));
                      }
        }
          return menuTypeList;
    }

  /**
 *  Executes the GetSecurityInfo interaction on the Resource Adapter.
 *  This interaction would be used in DisplayDocProperties interaction and UpdateDocProperties interaction.
 *  @param java.lang.String object_name
 *                                Name of the Object for which the SecurityInfo is requested.
 *  @param java.lang.String object_id
 *                                Label of the Object for which the SecurityInfo is requested
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.List :Returns the list conataining the object name, the object id, its primary group and the groups in which it is a member.
 */
    public List getSecurityInfo(String object_name, Integer object_id, Map InteractionParmsMap) throws ResourceException{

        Interaction interaction = null;

        try{
                m_RAResourceWarning = "";
                interaction = m_Connection.createInteraction();

                FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

                /* Assigning Interaction Parameters to CciInteractionSpec */
                updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);

                if(interactionSpec.getFunctionName() == null)
                          interactionSpec.setFunctionName(AppConstants.GET_SECURITY_INFO_FUNCTION_NAME);

                /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                MappedRecord securityInputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_SECURITY_INFO);
                securityInputRecord.put(AppConstants.OBJECT_NAME, object_name);
	        securityInputRecord.put(AppConstants.OBJECT_ID, object_id);

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
                if(execute3Obj != null)
                        bexecute3 = execute3Obj.booleanValue();

                IndexedRecord securityInfo = null;

               /* Executes the interaction, checks Interaction_Execute_3 flag and then calls the corresponding interaction.execute method*/
              // System.out.println("getSecurityInfo: Before entering interaction.execute method.");
              if(bexecute3){
                    IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord (AppConstants.GET_SECURITY_INFO_RESULT);
                    interaction.execute(interactionSpec,securityInputRecord, outputRecord);
                    securityInfo = outputRecord;
              }else{
                    securityInfo = (IndexedRecord)interaction.execute(interactionSpec, securityInputRecord);
              }
              // System.out.println("getSecurityInfo: After exiting interaction.execute method.");
              m_RAResourceWarning = getWarningFromInteraction(interaction);
              List resultSecurityInfoList = new ArrayList();
              Iterator iterator = securityInfo.iterator();
              Map tempmap;

               while(iterator.hasNext()){
                      tempmap = new HashMap();
                      tempmap.putAll((Map)iterator.next());
                      resultSecurityInfoList.add(tempmap);
               }

                return resultSecurityInfoList;
        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }finally{
                 if( interaction != null) interaction.close();
        }

    }

 /**
 *  Executes the GetDocClassDescs interaction on the Resource Adapter.
 *  This interaction would be used in ShowDocClassProperties interaction and GetDocClassIndices interaction.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.List :Returns a list of Document Class in IS.
 */
    public List getDocClassDesc(Integer doc_class_id, String doc_class_name, Map InteractionParmsMap) throws ResourceException{

        /* Cleaning the Resources before starting new Interaction */
          cleanUp();
          Interaction interaction = null;

        try{
                m_RAResourceWarning = "";
                interaction = m_Connection.createInteraction();

                FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

                /* Assigning Interaction Parameters to CciInteractionSpec */
                updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);

                if(interactionSpec.getFunctionName() == null)
                          interactionSpec.setFunctionName(AppConstants.GET_DOC_CLASS_DESCS_FUNCTION_NAME);

                /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                MappedRecord docClassDesc = m_RecordFactory.createMappedRecord(AppConstants.GET_DOC_CLASS_DESCS);
                docClassDesc.put(AppConstants.DOC_CLASS_ID, doc_class_id);
	        docClassDesc.put(AppConstants.DOC_CLASS_NAME, doc_class_name);

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
                if(execute3Obj != null)
                          bexecute3 = execute3Obj.booleanValue();

               IndexedRecord docClassSet = null;

               /* Executes the interaction, checks Interaction_Execute_3 flag and then calls the corresponding interaction.execute method*/
               // System.out.println("getDocClassDescs: Before entering interaction.execute method.");
               if(bexecute3){
                          IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord (AppConstants.GET_DOC_CLASS_DESCS_RESULT);
                          interaction.execute(interactionSpec,docClassDesc,outputRecord);
                          docClassSet = outputRecord;
               } else {
                          docClassSet = (IndexedRecord)interaction.execute(interactionSpec,docClassDesc);
               }


                // System.out.println("getDocClassDescs: After exiting interaction.execute method.");
                m_RAResourceWarning = getWarningFromInteraction(interaction);

                List resultDocClassSet = new ArrayList();
                Iterator iterator = docClassSet.iterator();

                Map tempmap;
	          while(iterator.hasNext()){
                          tempmap = new HashMap();
                          tempmap.putAll((Map)iterator.next());
                          resultDocClassSet.add(tempmap);
                  }

                   return resultDocClassSet;
        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }finally{
                if( interaction != null)  interaction.close();
        }
    }


 /**
 *  Executes the GetWorkspaces interaction on the Resource Adapter.
 *  This method gets all the workspace names existing in the ISRA.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.List
 *                                      Returns the Queue Names existing in the IS
 */
    public List getWorkspaceNames(Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();

	Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              // Assigning Interaction Parameters to CciInteractionSpec
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                 interactionSpec.setFunctionName(AppConstants.GET_WORKSPACE_NAME_FUNCTION_NAME);

               // Create empty record to be passed as parameter
               Record getWorkspaceNameInputMappedRecord = m_RecordFactory.createMappedRecord(AppConstants.WORKSPACE_NAME_IP_MAP_RECORD);

               boolean bexecute3 = false;
               Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

               if(execute3Obj != null) bexecute3 = execute3Obj.booleanValue();

               IndexedRecord resultIndexedRecord = null;

               // Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method
               if(bexecute3){
                  // Creating the output record for the specific Interaction
                    IndexedRecord outputRecord =  m_RecordFactory.createIndexedRecord(AppConstants.GET_WORKSPACE_NAMES_RESULT);
                    interaction.execute(interactionSpec,getWorkspaceNameInputMappedRecord,outputRecord);
                    resultIndexedRecord = outputRecord;
               }else{
                    resultIndexedRecord = (IndexedRecord) interaction.execute(interactionSpec,getWorkspaceNameInputMappedRecord);
               }
               m_RAResourceWarning = getWarningFromInteraction(interaction);

              List resultWorkspaceNamesList = new ArrayList();
              Iterator iterator = resultIndexedRecord.iterator();
              String workspaceNameStr = null;

              while(iterator.hasNext()){
                    workspaceNameStr =(String)iterator.next();
                    resultWorkspaceNamesList.add(workspaceNameStr);
              }

              return resultWorkspaceNamesList;
        }catch(ResourceException re){
                re.printStackTrace();
                throw re;
        }catch(Exception e){
                e.printStackTrace();
                throw new ResourceException(e.getMessage());
        }finally{
                if( interaction != null)       interaction.close();
        }
    }


/**
 *  Executes the GetQueues interaction on the Resource Adapter.
 *  This method gets all the queue names for the specified workspace existing in the ISRA.
 *  @param java.lang.String workspace_name
 *					Workspace name for which the queues have to be retrieved.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.List
 *                                      Returns the Queue Names existing in the IS
 */
    public List getQueueNames(String workspace_name,Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();

	Interaction interaction = null;
        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              // Assigning Interaction Parameters to CciInteractionSpec
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.GET_QUEUE_NAME_FUNCTION_NAME);

              // Create empty record to be passed as parameter
              MappedRecord getQueueNameInputMappedRecord = m_RecordFactory.createMappedRecord(AppConstants.QUEUE_NAME_IP_MAP_RECORD);
              getQueueNameInputMappedRecord.put(AppConstants.WORKSPACE_NAME_KEY,workspace_name);
              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              IndexedRecord resultIndexedRecord = null;

              // Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method
              if(bexecute3){
                    //Creating the output record for the specific Interaction
                    IndexedRecord outputRecord =  m_RecordFactory.createIndexedRecord(AppConstants.GET_QUEUE_NAMES_RESULT);
                    interaction.execute(interactionSpec,getQueueNameInputMappedRecord,outputRecord);
                    resultIndexedRecord = outputRecord;
              } else {
                    resultIndexedRecord = (IndexedRecord) interaction.execute(interactionSpec,getQueueNameInputMappedRecord);
              }

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              return resultIndexedRecord;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
             if( interaction != null)	interaction.close();
        }
    }

/**
 *  Executes the GetQueueFields interaction on the Resource Adapter.
 *  This method returns a collection of field description for the specified Queue Name.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param java.lang.String workspace_name
 *                              The Workspace Name for which the field description is to be retrieved.
 *  @param java.lang.String queue_Name
 *                              The Queue Name for which the field description is to be retrieved.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return java.util.List
 *                              Returns a List which contains the QueueFieldDescription Map for each field in a queue.
 */
    public List getQueueFields(String wqsServiceName, String workspace_name,String queue_Name,Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();

	Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              //Assigning Interaction Parameters to CciInteractionSpec
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                 interactionSpec.setFunctionName(AppConstants.GET_QUEUE_FIELDS_FUNCTION_NAME);

              // Create MappedRecord to be passed as parameter
              MappedRecord getQueueFieldMapRecord = m_RecordFactory.createMappedRecord(AppConstants.QUEUE_FIELDS_IP_MAP_RECORD);
              //Code added for Robustification DTS 173095
              getQueueFieldMapRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
              //Code added ends for Robustification DTS 173095
              getQueueFieldMapRecord.put(AppConstants.WORKSPACE_NAME_KEY, workspace_name);
              getQueueFieldMapRecord.put(AppConstants.QUEUE_NAME_KEY, queue_Name);

               boolean bexecute3 = false;
               Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

               if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

               IndexedRecord resultIndexedRecord = null;

               // Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method
               //System.out.println("GetQueueFields: Before entering interaction.execute method.");
               if(bexecute3){
                    // Creating the output record for the specific Interaction
                    IndexedRecord outputRecord =  m_RecordFactory.createIndexedRecord(AppConstants.GET_QUEUE_FIELDS_RESULT);
                    interaction.execute(interactionSpec,getQueueFieldMapRecord,outputRecord);
                    resultIndexedRecord = outputRecord;
              } else {
                    resultIndexedRecord = (IndexedRecord) interaction.execute(interactionSpec,getQueueFieldMapRecord);
              }
              //System.out.println("GetQueueFields: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              List resultQueueFieldsList = new ArrayList();
              Iterator iterator = resultIndexedRecord.iterator();
              Map map;
              String queueNameStr = null;
              while(iterator.hasNext()){
                   map = new HashMap();
                   map.putAll((Map)iterator.next());
                   resultQueueFieldsList.add(map);
              }

              return resultQueueFieldsList;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null)	interaction.close();
        }
    }


/**
 *  Executes the GetQueueEntries interaction on the Resource Adapter.
 *  This method generates a table of the existing entries for a given queue in a specific workspace.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param java.lang.String query
 *                              The query string. Query string would hold queue name, WHERE and ORDER
 *								BY clause. The minimal query will be "SELECT * FROM [QueueName]" "
 *  @param java.lang.Integer max_rows
 *								The maximum number of rows to be returned.
 *  @param java.lang.Boolean set_busy
 *								If set, the returned rows would be marked as busy.
 *  @param java.lang.Integer delete_spec
 *								deleteNone - 0, deleteAfterRead - 1, deletePrevious - 2.
 *  @param java.util.Map InteractionParmsMap
 *								Carries interaction request information as key-value pair.
 *  @return java.util.List
 *                              Returns a List which contains the QueueFieldDescription Map for each field in a queue.
 */
    public List getQueueEntries(String wqsServiceName, String query,Integer max_rows,Boolean set_busy,Integer delete_spec,Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();

	Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              // Assigning Interaction Parameters to CciInteractionSpec
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.GET_QUEUE_ENTRIES_FUNCTION_NAME);

              // Create MappedRecord to be passed as parameter
              MappedRecord queueEntriesMapRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_QUEUE_ENTRIES);
              //Code added for Robustification DTS 173095
              queueEntriesMapRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
              //Code added ends for Robustification DTS 173095
              queueEntriesMapRecord.put(AppConstants.QUERY , query);
              queueEntriesMapRecord.put(AppConstants.MAX_ROWS, max_rows);
              queueEntriesMapRecord.put(AppConstants.SET_BUSY, set_busy);
              queueEntriesMapRecord.put(AppConstants.DELETE_SPEC, delete_spec);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              ResultSet getQueueEntriesResultSet = null;

              // Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method
              //System.out.println("GetQueueEntries: Before entering interaction.execute method.");

              if(bexecute3){
                   // Creating the output record for the specific Interaction
                   MappedRecord outputRecord = m_RecordFactory.createMappedRecord (AppConstants.QUEUE_ENTRIES_RECORD);
                   interaction.execute(interactionSpec,queueEntriesMapRecord,outputRecord);
                   getQueueEntriesResultSet = (ResultSet)outputRecord;
              } else {
                   getQueueEntriesResultSet = (ResultSet) interaction.execute(interactionSpec,queueEntriesMapRecord);
              }
              //System.out.println("GetQueueEntries: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              List resultList = new ArrayList();
              resultList = FileNETUtils.getListFromResultSet(getQueueEntriesResultSet) ;
              return resultList;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null) interaction.close();
        }
    }


/**
 *  Executes the InsertQueueEntries interaction on the Resource Adapter.
 *  This method inserts new entries into specific existing queues.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param java.lang.String workspace_name
 *                              WorkspaceName for which the entry is to be made.
 *  @param java.lang.String queue_Name
 *                              QueueName for which the entry is to be made.
 *  @param java.util.List queueEntryList
 *                  	        List of queue entries.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return long
 *                              Generic result, 0 if success OR errorcode if there is an error.
 */
    public long insertQueueEntries(String wqsServiceName, String workspace_name,String queue_Name,List queueEntryList,Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                    interactionSpec.setFunctionName(AppConstants.INSERT_QUEUE_ENTRIES_FUNCTION_NAME);

              /*Create Indexed Record which will contail list of all indexed records to be inserted*/
              IndexedRecord queueEntriesIndexedRecord = m_RecordFactory.createIndexedRecord(AppConstants.INSERT_QUEUE_ENTRIES_INDEXED_RECORD);
              Iterator iterator = queueEntryList.iterator();
              while(iterator.hasNext()){
                  /* Create Indexed Record of the entry to be inserted to store each entry to be inserted*/
                  IndexedRecord indexedRecord = m_RecordFactory.createIndexedRecord(AppConstants.EACH_QUEUE_ENTRIES_INDEXED_RECORD);
                  List eachQueueEntryList = new ArrayList();
                  eachQueueEntryList = (List)iterator.next() ;
                  for (int j=0; j< eachQueueEntryList.size();j++){
                          Map eachFieldNameValueMap = (Map)eachQueueEntryList.get(j) ;
                          MappedRecord queueInsertFieldsMapRecord = m_RecordFactory.createMappedRecord(AppConstants.QUEUE_FIELDS_MAP_RECORD);
                          queueInsertFieldsMapRecord.putAll(eachFieldNameValueMap);
                          indexedRecord.add(queueInsertFieldsMapRecord) ;
                  }//end of for loop
                  queueEntriesIndexedRecord.add(indexedRecord);
              }// end of while loop

              /* Create MappedRecord to be passed as parameter */
              MappedRecord queueInsertRequestMapRecord = m_RecordFactory.createMappedRecord(AppConstants.INSERT_QUEUE_ENTRIES_MAP_RECORD);
              //Code added for Robustification DTS 173095
              queueInsertRequestMapRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
              //Code added ends for Robustification DTS 173095
              queueInsertRequestMapRecord.put(AppConstants.WORKSPACE_NAME_KEY, workspace_name);
              queueInsertRequestMapRecord.put(AppConstants.QUEUE_NAME_KEY, queue_Name);
              queueInsertRequestMapRecord.put(AppConstants.QUEUE_ENTRIES_KEY, queueEntriesIndexedRecord);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              MappedRecord resultMappedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
              //System.out.println("Insert Queue Entries: Before executing interaction.execute method.");
              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.QUEUE_ENTRY_RESULT_MAP_RECORD);
                  interaction.execute(interactionSpec,queueInsertRequestMapRecord,outputRecord);
                  resultMappedRecord = outputRecord;
              } else {
                  resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,queueInsertRequestMapRecord);
              }
              //System.out.println("Insert Queue Entries: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);
              // long lresult = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

              long lresult = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

              return lresult;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null) interaction.close();
        }
    }


/**
 *  Executes the UpdateQueueEntries interaction on the Resource Adapter.
 *  This method updates the queue entry selected by the user for updation.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param java.lang.String workspace_name
 *                              WorkspaceName for which the entry is to be updated.
 *  @param java.lang.String queue_Name
 *                              QueueName for which the entry is to be updated.
 *  @param java.lang.long queue_entry_id
 *                               queue_entry_id specifies the queue entry to be updated.
 *  @param java.util.List queueEntryList
 *                  	        List containing Map of fieldName & the value to be updated..
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return long
 *                              Generic result, 0 if success OR errorcode if there is an error.
 */
    public long updateQueueEntry(String wqsServiceName, String workspace_name,String queue_Name,String queue_entry_id,List queueEntryList,Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

	      FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                 interactionSpec.setFunctionName(AppConstants.UPDATE_QUEUE_ENTRIES_FUNCTION_NAME);
              /*Create Indexed Record which will contail list of all indexed records to be inserted*/
              IndexedRecord queueEntriesIndexedRecord = m_RecordFactory.createIndexedRecord(AppConstants.UPDATE_QUEUE_ENTRY_INDEXED_RECORD);
              Iterator iterator = queueEntryList.iterator();
              Map eachFieldNameValueMap;
              while(iterator.hasNext()){
                  eachFieldNameValueMap = new HashMap();
                  eachFieldNameValueMap = (Map)iterator.next() ;

                  MappedRecord queueUpdateFieldsMapRecord = m_RecordFactory.createMappedRecord(AppConstants.QUEUE_FIELDS_MAP_RECORD);
                                  queueUpdateFieldsMapRecord.putAll(eachFieldNameValueMap);
                  queueEntriesIndexedRecord.add(queueUpdateFieldsMapRecord);
              }

              /* Create MappedRecord to be passed as parameter */
              MappedRecord queueUpdateRequestMapRecord = m_RecordFactory.createMappedRecord(AppConstants.UPDATE_QUEUE_ENTRIES_MAP_RECORD);
              //Code added for Robustification DTS 173095
              queueUpdateRequestMapRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
              //Code added ends for Robustification DTS 173095
              queueUpdateRequestMapRecord.put(AppConstants.WORKSPACE_NAME_KEY, workspace_name);
              queueUpdateRequestMapRecord.put(AppConstants.QUEUE_NAME_KEY, queue_Name);
              queueUpdateRequestMapRecord.put(AppConstants.QUEUE_ENTRY_ID, queue_entry_id);
              queueUpdateRequestMapRecord.put(AppConstants.QUEUE_ENTRIES_UPDATE_KEY, queueEntriesIndexedRecord);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                    bexecute3 = execute3Obj.booleanValue();

              MappedRecord resultMappedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
              //System.out.println("UpdateQueueEntries: before entering interaction.execute method.");

              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.QUEUE_ENTRY_RESULT_MAP_RECORD);
                  interaction.execute(interactionSpec,queueUpdateRequestMapRecord,outputRecord);
                  resultMappedRecord = outputRecord;
              } else {
                  resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,queueUpdateRequestMapRecord);
              }
              //System.out.println("UpdateQueueEntries: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);
              long lresult = new Long(resultMappedRecord.get(AppConstants.RESULT).toString()).longValue();
              return lresult;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null)	interaction.close();
        }
    }

/**
 *  Executes the FileDocInFolder interaction on the Resource Adapter.
 *  This interaction would be used to assign the specified documents to the specified folder.
 *  @param ja va.lang.String folderName
 *                                      Destination folder.
 *  @param java.lang.Long[] docSet
 *                                      List of doc_ids to be added
 *  @param java.lang.Long placeAfter
 *                                      doc id after which the documents are to be added
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return long
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long FileDocsInFolder(String folderName, Long[] docSet, Long placeAfter,
                              Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.FILES_DOC_IN_FOLDER_FUNCTION_NAME);

            /* Creating doc_set indexed record to be passed to IS RA */
            IndexedRecord doc_set= m_RecordFactory.createIndexedRecord(AppConstants.FILES_DOC_IN_FOLDER_DOC_SET);

            if (docSet!=null){
                for(int i=0; i<docSet.length; i++)  doc_set.add(docSet[i]);
            }

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.FILES_DOC_IN_FOLDER);
            mappedRecord.put(AppConstants.DESTINATION_FOLDER, folderName);
            mappedRecord.put(AppConstants.DOCUMENTS, doc_set);
            mappedRecord.put(AppConstants.PLACE_AFTER, placeAfter);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("FileDocsInFolder: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.FILES_DOC_IN_FOLDER_RESULT);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
	        resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("FileDocsInFolder: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

            return lresultErrorID;

        }catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }

    }

    /**
 *  Executes the createQueue interaction on the Resource Adapter.
 *  This interaction would be used to create a queue.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param ja va.lang.String workspaceName
 *                                      Name of workspace in which queue is to be created.
 *  @param java.lang.String queueName
 *                                      Name of the queue.
 *  @param java.lang.String def_permissions_str
 *                                      Permission on the definition of the queue.
 *  @param java.lang.String content_permissions_str
 *                                      Permission on the content of the queue.
 *  @param java.lang.String text_desc
 *                                      Description of the queue.
 *  @param java.util.Vector userFieldDesc
 *                                      Vector containing the description of the queue field defined for the queue.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return long
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long createWorkspace(String wqsServiceName, String workspaceName, String  security_permissions,
                                             String ws_text_desc, Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.CREATE_WORKSPACE_FUNCTION_NAME);

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.CREATEWORKSPACE_INPUT_RECORD_NAME);
            //Code added for Robustification DTS 173095
            mappedRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
            //Code added ends for Robustification DTS 173095
            mappedRecord.put(AppConstants.WORKSPACE_NAME_KEY, workspaceName);
            mappedRecord.put(AppConstants.SECURITY_PERMISSIONS_KEY, security_permissions);
            mappedRecord.put(AppConstants.WORKSPACE_TEXT_DESC, ws_text_desc);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("FileDocsInFolder: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.CREATEWORKSPACE_OUTPUT_RECORD_NAME);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
	        resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("FileDocsInFolder: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

            return lresultErrorID;

        }catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }

    }

/**
 *  Executes the createQueue interaction on the Resource Adapter.
 *  This interaction would be used to create a queue.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param ja va.lang.String workspaceName
 *                                      Name of workspace in which queue is to be created.
 *  @param java.lang.String queueName
 *                                      Name of the queue.
 *  @param java.lang.String def_permissions_str
 *                                      Permission on the definition of the queue.
 *  @param java.lang.String content_permissions_str
 *                                      Permission on the content of the queue.
 *  @param java.lang.String text_desc
 *                                      Description of the queue.
 *  @param java.util.Vector userFieldDesc
 *                                      Vector containing the description of the queue field defined for the queue.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return long
 *  @throws javax.resource.ResourceException if error occurs in the Resource Adapter during interaction.
 *  @throws java.lang.Exception
 */
    public synchronized long createQueue(String wqsServiceName, String workspaceName, String queueName,
                                         String def_permissions_str,String content_permissions_str,
                                         String text_desc, Vector userFieldDesc,
                                         Map InteractionParmsMap) throws ResourceException, Exception{

        /* Cleaning the Resources before starting new Interaction */
        cleanUp();

        Interaction interaction = null;

        try{
            m_RAResourceWarning = "";
	    interaction = m_Connection.createInteraction();

	    FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

            /* Assigning Interaction Parameters to CciInteractionSpec */
            updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

            if(interactionSpec.getFunctionName() == null)
                interactionSpec.setFunctionName(AppConstants.CREATE_QUEUE_FUNCTION_NAME);

            /* Creating doc_set indexed record to be passed to IS RA */
            IndexedRecord queueFieldDescList= m_RecordFactory.createIndexedRecord(AppConstants.QUEUE_FIELD_DESC_LIST);

            if (userFieldDesc!=null){
                for(int i=0; i<userFieldDesc.size(); i++){
                  Map eachEntry = (Map)userFieldDesc.elementAt(i);
                  MappedRecord eachQueueFieldDesc = m_RecordFactory.createMappedRecord(AppConstants.EACH_QUEUE_FIELD_DESC);
                  eachQueueFieldDesc.putAll(eachEntry);
                  queueFieldDescList.add(eachQueueFieldDesc);
                }
            }

            /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
            MappedRecord mappedRecord = m_RecordFactory.createMappedRecord(AppConstants.CREATEQUEUE_INPUT_RECORD_NAME);
            //Code added for Robustification DTS 173095
            mappedRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
            //Code added ends for Robustification DTS 173095
            mappedRecord.put(AppConstants.WORKSPACE_NAME_KEY, workspaceName);
            mappedRecord.put(AppConstants.QUEUE_NAME_KEY, queueName);
            mappedRecord.put(AppConstants.DEFINITION_ACCESS_KEY, def_permissions_str);
            mappedRecord.put(AppConstants.CONTENT_ACCESS_KEY, content_permissions_str);
            mappedRecord.put(AppConstants.QUEUE_TEXT_DESC, text_desc);
            mappedRecord.put(AppConstants.USER_FIELD_DESC_KEY, queueFieldDescList);

            boolean bexecute3 = false;
            Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

            if(execute3Obj != null)
                bexecute3 = execute3Obj.booleanValue();

	    MappedRecord resultMappedRecord = null;

            /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
            // System.out.println("FileDocsInFolder: Before entering interaction.execute method.");
            if(bexecute3){
                /* Creating the output record for the specific Interaction */
                MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.CREATEQUEUE_OUTPUT_RECORD_NAME);
	    	interaction.execute(interactionSpec,mappedRecord,outputRecord);
		resultMappedRecord = outputRecord;
            }
            else{
	        resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,mappedRecord);
            }
            // System.out.println("FileDocsInFolder: After exiting interaction.execute method.");

            m_RAResourceWarning = getWarningFromInteraction(interaction);

            long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

            return lresultErrorID;

        }catch(ResourceException re){
	    re.printStackTrace();
            throw re;
        }catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
	    if( interaction != null) interaction.close();
        }

    }


/**
 *  Executes the DeleteQueueEntries interaction on the Resource Adapter.
 *  This method deletes the queue entries selected by the user for deletion.
 *
 *  @param java.lang.String wqsServiceName Name of the WQS Service.
 *                              This parameter is added for Robustification DTS 173095.
 *  @param java.lang.String workspace_name
 *                              WorkspaceName for which the entry is to be deleted.
 *  @param java.lang.String queue_Name
 *                              QueueName for which the entry is to be deleted.
 *  @param java.util.List queueEntryIDSet
 *                  	        List of queueEntry id's corresponding to each entry to be deleted.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return long
 *                              Generic result, 0 if success OR errorcode if there is an error.
 */
    public long deleteQueueEntries(String wqsServiceName, String workspace_name,String queue_Name,List queueEntryIDSet,Map InteractionParmsMap)throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

    	      FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                    interactionSpec.setFunctionName(AppConstants.DELETE_QUEUE_ENTRIES_FUNCTION_NAME);
              /* Create Indexed Record of the entries to be deleted */
              IndexedRecord queueEntriesIndexedRecord = m_RecordFactory.createIndexedRecord(AppConstants.DELETE_QUEUE_ENTRIES);
              Iterator iterator = queueEntryIDSet.iterator();

              while(iterator.hasNext()){
                  String entryIDStr = (String)iterator.next() ;
                  queueEntriesIndexedRecord.add(entryIDStr);
              }

              /* Create MappedRecord to be passed as parameter */
              MappedRecord queueDeleteRequestMapRecord = m_RecordFactory.createMappedRecord(AppConstants.DELETE_QUEUE_ENTRIES_MAP_RECORD);
              //Code added for Robustification DTS 173095
              queueDeleteRequestMapRecord.put(AppConstants.WQS_SERVICE, wqsServiceName);
              //Code added ends for Robustification DTS 173095
              queueDeleteRequestMapRecord.put(AppConstants.WORKSPACE_NAME_KEY, workspace_name);
              queueDeleteRequestMapRecord.put(AppConstants.QUEUE_NAME_KEY, queue_Name);
              queueDeleteRequestMapRecord.put(AppConstants.QUEUE_ENTRIES_KEY, queueEntriesIndexedRecord);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

              if(execute3Obj != null)
                    bexecute3 = execute3Obj.booleanValue();

              MappedRecord resultMappedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
      	      //System.out.println("DeleteQueueEntries: Before entering interaction.execute method.");
              if(bexecute3){
                    /* Creating the output record for the specific Interaction */
                    MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.QUEUE_ENTRY_RESULT_MAP_RECORD);
                    interaction.execute(interactionSpec,queueDeleteRequestMapRecord,outputRecord);
                    resultMappedRecord = outputRecord;
              } else {
		    resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,queueDeleteRequestMapRecord);
              }
              //System.out.println("DeleteQueueEntries: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              long lresult = new Long(resultMappedRecord.get(AppConstants.RESULT).toString()).longValue();
              return lresult;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null)   interaction.close();
        }
    }

/**
 *  Executes the IsAnnotated interaction on the Resource Adapter.
 *  Returns True/False depending upon whether the document is annotated or not.
 *  @param java.lang.Long doc_id
 *                              Checks for the specified DocID for annotations.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return java.lang.Boolean
 *                              Returns True if document is annotated or else it returns false.
 */
    public boolean IsAnnotated(Long doc_id,Map InteractionParmsMap)throws ResourceException{

	Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.IS_ANNOTATION_FUNCTION_NAME);
              /* Create Mapped Record containing doc_id for which isAnnotated func is called */
               MappedRecord docIDisAnnotatedRequestMapRecord = m_RecordFactory.createMappedRecord(AppConstants.IS_ANNOTATED_DOCID_MAP_RECORD);
              docIDisAnnotatedRequestMapRecord.put(AppConstants.DOC_ID, doc_id);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                 bexecute3 = execute3Obj.booleanValue();

	      MappedRecord resultMappedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */

	      if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.ISANNOTATED_RESULT);
                  interaction.execute(interactionSpec,docIDisAnnotatedRequestMapRecord,outputRecord);
                  resultMappedRecord = outputRecord;
              } else {
		  resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,docIDisAnnotatedRequestMapRecord);
              }
              // System.out.println("RemoveDocsFromFolder: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              Boolean result = (Boolean)resultMappedRecord.get(AppConstants.FLAG) ;
              boolean bresult = result.booleanValue();

              return bresult;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null)	interaction.close();
        }

    }


 /**
 *  Executes the getAnnotations interaction on the Resource Adapter.
 *  Method that retrieve the collection of annotations for the specified document page.
 *  @param java.lang.Long doc_id
 *                              DocID for which the annotations are to be obtained.
 *  @param java.lang.Integer page_number
 *                              page number for which the annotations are to be obtained.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return java.util.Map
 *                              Generic result, 0 if success OR errorcode if there is an error.
 */
    public Map getAnnotationSet(Long doc_id,Integer page_number,Map InteractionParmsMap)throws ResourceException{

	Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

	      FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.GET_ANNOTATION_FUNCTION_NAME);
              /* Create Mapped Record containing xmlStream for which isAnnotated func is called */
              MappedRecord getAnnotationRequestMapRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_ANNOTATIONS_MAP_RECORD);
              getAnnotationRequestMapRecord.put(AppConstants.DOC_ID, doc_id);
              getAnnotationRequestMapRecord.put(AppConstants.PAGE_NUMBER, page_number);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

	      MappedRecord resultMappedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
	      //System.out.println("GetAnnotations: Before entering interaction.execute method.");
	      if(bexecute3){

                  /* Creating the output record for the specific Interaction */
                  MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.GET_ANNOTATIONS_RESULT);
	    	  interaction.execute(interactionSpec,getAnnotationRequestMapRecord,outputRecord);
		  resultMappedRecord = outputRecord;
              } else {

                  resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,getAnnotationRequestMapRecord);
              }
              //System.out.println("GetAnnotations: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              /*Code commented for Globalization Real Fix (DTS 173917).
              InputStream result =  (InputStream)resultMappedRecord.get(AppConstants.XMLSTREAM) ;

              return result;*/

              //Code added for Globalization Real Fix (DTS 173917).
              Map resultMap = new HashMap();
              resultMap.putAll(resultMappedRecord);
              return resultMap;
              //Code added ends for Globalization Real Fix.
	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null)	interaction.close();
        }
    }



/**
 *  Executes the SaveAnnotations interaction on the Resource Adapter.
 *  Method that recieves the collection of annotations to be saved for the document page in an XML stream and passes it to the ISRA.
 *  @param java.io.InputStream[] xmlStream
 *                              Contains XML Stream of data that is to be saved.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return long
 *                              Generic result, 0 if success OR errorcode if there is an error.
 */


/**
 *  Executes the SaveAnnotations interaction on the Resource Adapter.
 *  Method that recieves the collection of annotations to be saved for the document page in an XML stream and passes it to the ISRA.
 *  @param java.io.InputStream[] xmlStream
 *                              Contains XML Stream of data that is to be saved.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return long
 *                              Generic result, 0 if success OR errorcode if there is an error.
 */
public HashMap SaveAnnotationSet(InputStream xmlStream,Map InteractionParmsMap)throws ResourceException{

	Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.SAVE_ANNOTATION_FUNCTION_NAME);
              /* Create Mapped Record containing xmlStream for which isAnnotated func is called */
              MappedRecord saveAnnotationRequestMapRecord = m_RecordFactory.createMappedRecord(AppConstants.ANNOTATIONS_MAP_RECORD);
              saveAnnotationRequestMapRecord.put(AppConstants.XMLSTREAM, xmlStream);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              MappedRecord resultMappedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
              //System.out.println("SaveAnnotations: Before entering interaction.execute method.");
              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  MappedRecord outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.SAVE_ANNOTATIONS_RESULT);
                  interaction.execute(interactionSpec,saveAnnotationRequestMapRecord,outputRecord);
                  resultMappedRecord = outputRecord;
              } else {
                  resultMappedRecord = (MappedRecord) interaction.execute(interactionSpec,saveAnnotationRequestMapRecord);
              }
              //System.out.println("SaveAnnotations: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);
              /*Code Added for DTS 142545*/
              //long lresult = new Long(resultMappedRecord.get(AppConstants.RESULT).toString()).longValue() ;
              HashMap mappedAnnotIds =  (HashMap)resultMappedRecord.get((AppConstants.RESULT_HASH_MAP).toString());

              /*Code Added for DTS 142545*/

              //return lresult;
              return mappedAnnotIds;

	}catch(ResourceException re){
	    re.printStackTrace();
            throw re;
	}catch(Exception e){
	    e.printStackTrace();
            throw new ResourceException(e.getMessage());
	}finally{
            if( interaction != null)	interaction.close();
        }
    }
/**
 *  Executes the GetPasswordStatus interaction on the Resource Adapter.
 *  This interaction would be used to get the password details of a user.
 *  @param java.lang.String object_name
 *                                Name of the Object for which the password status is requested.
 *  @param java.util.Map InteractionParmsMap
 *					Carries interaction request information as key-value pair.
 *  @return java.util.Map :Returns the Map containing the user's password details like, password expiration time, has grace period begun, last successful logon, last failed logon.
 */
    public Map getPasswordStatus(String object_name, Map InteractionParmsMap) throws ResourceException{

        Interaction interaction = null;

        try{
                m_RAResourceWarning = "";
                interaction = m_Connection.createInteraction();

                FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

                /* Assigning Interaction Parameters to CciInteractionSpec */
                updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);

                if(interactionSpec.getFunctionName() == null)
                          interactionSpec.setFunctionName(AppConstants.GET_PASSWORD_STATUS_FUNCTION_NAME);

                /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                MappedRecord pwdStatusInputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_PASSWORD_STATUS_INPUT_RECORD);
                pwdStatusInputRecord.put(AppConstants.OBJECT_NAME, object_name);

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
                if(execute3Obj != null)
                        bexecute3 = execute3Obj.booleanValue();

                MappedRecord pwdStatus = null;

               /* Executes the interaction, checks Interaction_Execute_3 flag and then calls the corresponding interaction.execute method*/
              // System.out.println("getPwdStatus: Before entering interaction.execute method.");
              if(bexecute3){
                    MappedRecord outputRecord = m_RecordFactory.createMappedRecord (AppConstants.GET_PASSWORD_STATUS_OUTPUT_RECORD);
                    interaction.execute(interactionSpec,pwdStatusInputRecord, outputRecord);
                    pwdStatus = outputRecord;
              }else{
                    pwdStatus = (MappedRecord)interaction.execute(interactionSpec, pwdStatusInputRecord);
              }
              // System.out.println("getPwdStatus: After exiting interaction.execute method.");
              m_RAResourceWarning = getWarningFromInteraction(interaction);
              Map pwdStatusMap = new HashMap();
              pwdStatusMap.putAll(pwdStatus);

            return pwdStatusMap;

        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }finally{
                 if( interaction != null) interaction.close();
        }

    }

/**
 *  Executes the changePassword interaction on the Resource Adapter.
 *  This interaction would be used to change the password of the existing user.
 *  @param java.lang.String old_password
 *                                Old Password of the Object for which change of password is requested.
 *  @param java.lang.String new_password
 *                                New Password of the Object for which change of password is requested.
 *  @param java.lang.Long object_id
 *                                Id of the Object for which change of password is requested.
 *  @param java.util.Map InteractionParmsMap
 *				  Carries interaction request information as key-value pair.
 *  @return long :Returns the result of the interaction.
 */
    public long changePassword(String old_password, String new_password, String confirm_password, String user_name, Map InteractionParmsMap) throws ResourceException{

        Interaction interaction = null;

        try{
                m_RAResourceWarning = "";
                interaction = m_Connection.createInteraction();

                FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

                /* Assigning Interaction Parameters to CciInteractionSpec */
                updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);

                if(interactionSpec.getFunctionName() == null)
                          interactionSpec.setFunctionName(AppConstants.CHANGE_PASSWORD_FUNCTION_NAME);

                /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                MappedRecord changePwdInputRecord = m_RecordFactory.createMappedRecord(AppConstants.CHANGE_PASSWORD_INPUT_RECORD);
                changePwdInputRecord.put(AppConstants.OLD_PASSWORD, old_password);
                changePwdInputRecord.put(AppConstants.NEW_PASSWORD, new_password);
                changePwdInputRecord.put(AppConstants.CONFIRM_PASSWORD, confirm_password);
                changePwdInputRecord.put(AppConstants.OBJECT_NAME, user_name);

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
                if(execute3Obj != null)
                        bexecute3 = execute3Obj.booleanValue();

                MappedRecord changePwdOutput = null;

               /* Executes the interaction, checks Interaction_Execute_3 flag and then calls the corresponding interaction.execute method*/
              // System.out.println("changePassword: Before entering interaction.execute method.");
              if(bexecute3){
                    MappedRecord outputRecord = m_RecordFactory.createMappedRecord (AppConstants.CHANGE_PASSWORD_OUTPUT_RECORD);
                    interaction.execute(interactionSpec,changePwdInputRecord, outputRecord);
                    changePwdOutput = outputRecord;
              }else{
                    changePwdOutput = (MappedRecord)interaction.execute(interactionSpec, changePwdInputRecord);
              }
              // System.out.println("changePassword: After exiting interaction.execute method.");
              m_RAResourceWarning = getWarningFromInteraction(interaction);

              long lresult = new Long(changePwdOutput.get(AppConstants.RESULT).toString()).longValue();
              return lresult;

        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }finally{
                 if( interaction != null) interaction.close();
                   //code added for DTS 169459
                    if(cpwlFlag.booleanValue() == true){
                      //System.out.println(":::::::::::::before clientBean logoff::::");
                      logoff();
                     //System.out.println(":::::::::after clientBean logoff:::::::");
                    }
                    // code added for DTS 169459 ends

        }

    }

/**
 *  Executes the getServiceAttributes interaction on the Resource Adapter.
 *  Method that retrieves the printers configured on the IS along with various attributes of printer.

 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return List
 *                              List containing the printer attributes.
 */
	public List getPrinterAttributes(Map InteractionParmsMap)throws ResourceException{

	    Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();
			    /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.GET_PRINTER_ATTRIBUTES_FUNCTION_NAME);
              /* Create empty Mapped Record  */
			  MappedRecord inputRecord =
			     m_RecordFactory.createMappedRecord(AppConstants.PRINTER_ATTRIBUTES_INPUT);


              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              IndexedRecord resultIndexedRecord = null;
			  IndexedRecord outputRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
				//System.out.println("execute value is ***************:"+bexecute3);
              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  outputRecord =  m_RecordFactory.createIndexedRecord(AppConstants.PRINTER_ATTRIBUTES_OUTPUT);
                  interaction.execute(interactionSpec,inputRecord,outputRecord);
                  resultIndexedRecord = outputRecord;
              } else {
                  resultIndexedRecord = (IndexedRecord) interaction.execute(interactionSpec,inputRecord);
              }
              m_RAResourceWarning = getWarningFromInteraction(interaction);
			  List resultPrinterAttributesList = new ArrayList();

			  //Iterator itr = resultIndexedRecord.iterator();
			  Iterator itr = outputRecord.iterator();
			  ///System.out.println("outputRecord size :"+outputRecord.size());
			  Map map = null;
			  while(itr.hasNext()){
				map  = new HashMap();
				map.putAll((Map)itr.next());
				resultPrinterAttributesList.add(map);
			  }
			 // System.out.println("resultPrinterAttributesList*******:"+resultPrinterAttributesList);
			  //System.out.println("no. of printers in clientbean:"+resultPrinterAttributesList.size());
              return resultPrinterAttributesList;

		}catch(ResourceException re){
			re.printStackTrace();
				throw re;
		}catch(Exception e){
			e.printStackTrace();
				throw new ResourceException(e.getMessage());
		}finally{
				if( interaction != null)	interaction.close();
		}
	}

	public HashMap getEachPrinterAttribute(String printer_name, List printerList){
		//System.out.println("Inside getEachPrinterAtttibute function :"+printer_name);
		HashMap hm = new HashMap();
		if(printerList != null){
			for (int i=0; i < printerList.size();i++ )
			{
				hm = (HashMap)printerList.get(i);
				if(hm.containsValue(printer_name))
				{
					//System.out.println("map contains the printername---- before returning");
					return hm;
				}else
					continue;
			}

		}
		return hm;

	}

	public long printFaxDocs(Map printdoc, Map paramsMap) throws ResourceException{
		Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();
              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,paramsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.PRINT_FAX_DOCS_FUNCTION_NAME);
              /* Create empty Mapped Record  */
	      MappedRecord inputRecord = m_RecordFactory.createMappedRecord(AppConstants.PRINT_DOCS_INPUT);

              IndexedRecord i_docSpec = m_RecordFactory.createIndexedRecord(AppConstants.DOC_SPEC_LIST);

	      Vector v_docSpec= (Vector)printdoc.get(AppConstants.DOC_DETAILS);
              for(int i=0; i < v_docSpec.size();i++){
		  MappedRecord map_docDetails = m_RecordFactory.createMappedRecord(AppConstants.DOC_DETAILS);
		  HashMap map = (HashMap)v_docSpec.get(i);
		  map_docDetails.putAll(map);
		  i_docSpec.add(i,map_docDetails);
		  map_docDetails = null;
              }

	      IndexedRecord i_printOptions = m_RecordFactory.createIndexedRecord(AppConstants.PRINT_OPTIONS);
	      Vector v_pri_options = (Vector)printdoc.get(AppConstants.PRINT_OPTIONS);
	      for(int i=0; i < v_pri_options.size();i++){
		  MappedRecord map_printOptions = m_RecordFactory.createMappedRecord(AppConstants.PRINT_OPTIONS);
		  HashMap map = (HashMap)v_pri_options.get(i);
		  map_printOptions.putAll(map);
		  i_printOptions.add(i,map_printOptions);
		  map_printOptions = null;
              }

	      Boolean faxReq = (Boolean)printdoc.get(AppConstants.ISFAX);
	      Short notify = (Short)printdoc.get(AppConstants.NOTIFY);
              inputRecord.put(AppConstants.DOC_SPEC_LIST,i_docSpec); //add doc spec index record to mapped record.
              inputRecord.put(AppConstants.PRINT_OPTIONS,i_printOptions);//add print options to mapped record.
              inputRecord.put(AppConstants.ISFAX, faxReq);
              inputRecord.put(AppConstants.NOTIFY, notify);

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)paramsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              MappedRecord outputRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
	      //System.out.println("execute value is in printfaxdocs ***************:"+bexecute3);
              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
		  // System.out.println("inside if of bexecute3");
                  outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.PRINTDOCUMENTS_RESULT);
		  //System.out.println("ouptutREcord creatred ");
                  interaction.execute(interactionSpec,inputRecord,outputRecord);
		  //System.out.println("3 param interaction executed successfully");

              } else {
		  // System.out.println("inside else of bexecute3");
		  outputRecord =  m_RecordFactory.createMappedRecord(AppConstants.PRINTDOCUMENTS_RESULT);
		 // System.out.println("ouptutREcord creatred ");

                  outputRecord = (MappedRecord) interaction.execute(interactionSpec,inputRecord);
		  //System.out.println("2 param interaction executed successfully");
              }
              m_RAResourceWarning = getWarningFromInteraction(interaction);

                //Iterator itr = resultIndexedRecord.iterator();
                long lresult = new Long((outputRecord.get(AppConstants.RESULT)).toString()).longValue();
                //System.out.println("inside client bean Requestid :"+lresult);
              return lresult;

		}catch(ResourceException re){
			//System.out.println("inside ResourceException catch of client bean");
			re.printStackTrace();
			throw re;
		}catch(Exception e){
			//System.out.println("inside Exception catch of client bean");
			e.printStackTrace();
			throw new ResourceException(e.getMessage());
		}finally{
			if( interaction != null)	interaction.close();
		}

	}

	public boolean setEnableJMSLogging(Map enable, Map paramsMap) throws ResourceException{

        Interaction interaction = null;
        try{
	      m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();
	      /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,paramsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.ENABLE_PERFORMANCE_LOGGING_FUNCTION_NAME);
              /* Create empty Mapped Record  */
                  MappedRecord inputRecord = m_RecordFactory.createMappedRecord(AppConstants.ENABLEPERFORMANCELOGGING_INPUT);
                  inputRecord.putAll(enable);

  	      boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)paramsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

	      MappedRecord outputRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
	      if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  outputRecord = m_RecordFactory.createMappedRecord(AppConstants.ENABLEPERFORMANCELOGGING_OUTPUT);
                  interaction.execute(interactionSpec,inputRecord,outputRecord);
              } else {
                  outputRecord = (MappedRecord)interaction.execute(interactionSpec,inputRecord);
              }

              m_RAResourceWarning = getWarningFromInteraction(interaction);
              Boolean out = (Boolean)outputRecord.get(AppConstants.OUT_PERFORMANCE_LOGGING);
              if(out != null)
	      {
		  return out.booleanValue();
	      }
        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }
         return false;
    }

    public boolean getJMSLoggingValue(Map paramsMap) throws ResourceException{
        Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();
              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,paramsMap);

              if(interactionSpec.getFunctionName() == null)
                  interactionSpec.setFunctionName(AppConstants.GET_ENABLE_PERFORMANCE_LOGGING_FUNCTION_NAME);
              /* Create empty Mapped Record  */
              MappedRecord inputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_ENABLEPERFORMANCELOGGING_INPUT);
              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)paramsMap.get(AppConstants.INTERACTION_EXECUTE_3);
              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              MappedRecord outputRecord = null;
              outputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_ENABLEPERFORMANCELOGGING_OUTPUT);

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
	      //System.out.println("execute value iin getEnablePerfLogging ***************:"+bexecute3);
              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                   interaction.execute(interactionSpec,inputRecord,outputRecord);
              } else {
                  outputRecord = (MappedRecord)interaction.execute(interactionSpec,inputRecord);
              }

              m_RAResourceWarning = getWarningFromInteraction(interaction);
              Boolean out = (Boolean)outputRecord.get(AppConstants.GET_ENABLEPERFORMANCELOGGING_OUTPUT_VALUE);
              if(out != null)
              {
                  return out.booleanValue();
              }
        }catch(ResourceException re){
                    re.printStackTrace();
	            throw re;
        }catch(Exception e){
                    e.printStackTrace();
	            throw new ResourceException(e.getMessage());
        }
          return false;
    }

    /**
    *  Executes the createFolder interaction on the Resource Adapter.
    *  This interaction would be used to create a new folder on the IS.
    *  @param java.lang.String folderName
    *                                      Name of the folder to be created.
    *  @param java.lang.String securityPermissions
    *                                      Security permissions to be set for the folder.
    *  @param Short autoDelPeriod
    *                                      Automatic Deletion period to be set for the folder.
    *  @param Short retentBase
    *                                      Retention Base date to be used for the folder.
    *  @param Long retentOffset
    *                                      Retention Offset to be used for the folder.
    *  @param Short retentDisposition
    *                                      Disposition method to be set for the folder.
    *  @param java.util.Map InteractionParmsMap
    *					   Carries interaction request information as key-value pair.
    *  @return Long
    *                                      Error tuple signifying success/failure of the interaction
    *  @throws javax.resource.ResourceException
    *                                      If error occurs in the Resource Adapter during interaction.
    *  @throws java.lang.Exception
    */
       public synchronized long createFolder(String folderName, String securityPermissions, Short autoDelPeriod,
                                             Short retentBase, Long retentOffset, Short retentDisposition,
                                             Map InteractionParmsMap) throws ResourceException{

           /* Cleaning the Resources before starting new Interaction */
           cleanUp();

           Interaction interaction = null;

           try{
               m_RAResourceWarning = "";
               interaction = m_Connection.createInteraction();

               FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

               /* Assigning Interaction Parameters to CciInteractionSpec */
               updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

               if(interactionSpec.getFunctionName() == null)
                   interactionSpec.setFunctionName(AppConstants.CREATE_FOLDER_FUNCTION_NAME);

               /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
               MappedRecord createFolderMappedRecord = m_RecordFactory.createMappedRecord(AppConstants.CREATE_FOLDER_INPUT_RECORD);
               createFolderMappedRecord.put(AppConstants.FOLDER_NAME, folderName);
               createFolderMappedRecord.put(AppConstants.ACCESS_RESTRICTIONS_KEY, securityPermissions);
               createFolderMappedRecord.put(AppConstants.AUTO_DEL_PERIOD, autoDelPeriod);
               createFolderMappedRecord.put(AppConstants.RETENT_BASE, retentBase);
               createFolderMappedRecord.put(AppConstants.RETENT_OFFSET, retentOffset);
               createFolderMappedRecord.put(AppConstants.RETENT_DISPOSITION, retentDisposition);


               boolean bexecute3 = false;
               Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

               if(execute3Obj != null)
                   bexecute3 = execute3Obj.booleanValue();

               MappedRecord resultMappedRecord = null;

               /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
               if(bexecute3){
                   /* Creating the output record for the specific Interaction */
                   MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.CREATE_FOLDER_OUTPUT_RECORD);
                       interaction.execute(interactionSpec,createFolderMappedRecord,outputRecord);
                   resultMappedRecord = outputRecord;
               }
               else{
                   resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,createFolderMappedRecord);
               }
               // System.out.println("CreateFolder: After exiting interaction.execute method.");

               m_RAResourceWarning = getWarningFromInteraction(interaction);

               long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

               return lresultErrorID;

           }catch(ResourceException re){
               re.printStackTrace();
               throw re;
           }catch(Exception e){
               e.printStackTrace();
               throw new ResourceException(e.getMessage());
           }finally{
               if( interaction != null) interaction.close();
           }

       }

       /**
       *  Executes the updateFolder interaction on the Resource Adapter.
       *  This interaction would be used to update properties of a folder in the IS.
       *  @param java.lang.String folderName
       *                                      Name of the folder to be updated.
       *  @param java.lang.String folderId
       *                                      Id of the folder to be updated.
       *  @param java.lang.String isLeaf
       *                                      If the folder is a leaf folder.
       *  @param java.lang.String isClosed
       *                                      If the folder is already a closed folder.
       *  @param java.lang.String securityPermissions
       *                                      Security permissions to be set for the folder.
       *  @param Short createDate
       *                                      Creation date of the folder.
       *  @param Short archiveDate
       *                                      Archival Date of the folder.
       *  @param Short deleteDate
       *                                      Deletion Date of the folder.
       *  @param Short autoDelPeriod
       *                                      Automatic Deletion period to be set for the folder.
       *  @param Short retentBase
       *                                      Retention Base date to be used for the folder.
       *  @param Long retentOffset
       *                                      Retention Offset to be used for the folder.
       *  @param Short retentDisposition
       *                                      Disposition method to be set for the folder.
       *  @param java.util.Map InteractionParmsMap
       *				      Carries interaction request information as key-value pair.
       *  @return Long
       *                                      Error tuple signifying success/failure of the interaction
       *  @throws javax.resource.ResourceException
       *                                      If error occurs in the Resource Adapter during interaction.
       *  @throws java.lang.Exception
       */
          public synchronized long updateFolder(String folderName, Long folderId, Boolean isLeaf, Boolean isClosed,
                                                String securityPermissions,  Date createDate,
                                                Date archiveDate, Date deleteDate, Short autoDelPeriod,
                                                Short retentBase, Long retentOffset,
                                                Short retentDisposition, Map InteractionParmsMap)
                                                throws ResourceException{

              /* Cleaning the Resources before starting new Interaction */
              cleanUp();

              Interaction interaction = null;

              try{
                  m_RAResourceWarning = "";
                  interaction = m_Connection.createInteraction();

                  FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

                  /* Assigning Interaction Parameters to CciInteractionSpec */
                  updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

                  if(interactionSpec.getFunctionName() == null)
                      interactionSpec.setFunctionName(AppConstants.UPDATE_FOLDER_FUNCTION_NAME);

                  /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                  MappedRecord updateFolderMappedRecord = m_RecordFactory.createMappedRecord(AppConstants.UPDATE_FOLDER_INPUT_RECORD);
                  updateFolderMappedRecord.put(AppConstants.FOLDER_NAME, folderName);
                  updateFolderMappedRecord.put(AppConstants.FOLDER_ID, folderId);
                  updateFolderMappedRecord.put(AppConstants.IS_LEAF, isLeaf);
                  updateFolderMappedRecord.put(AppConstants.IS_CLOSED, isClosed);
                  updateFolderMappedRecord.put(AppConstants.ACCESS_RESTRICTIONS_KEY, securityPermissions);
                  updateFolderMappedRecord.put(AppConstants.F_DATE_CREATE, createDate);
                  updateFolderMappedRecord.put(AppConstants.F_DATE_ARCHIVE, archiveDate);
                  updateFolderMappedRecord.put(AppConstants.F_DATE_DELETE, deleteDate);
                  updateFolderMappedRecord.put(AppConstants.AUTO_DEL_PERIOD, autoDelPeriod);
                  updateFolderMappedRecord.put(AppConstants.RETENT_BASE, retentBase);
                  updateFolderMappedRecord.put(AppConstants.RETENT_OFFSET, retentOffset);
                  updateFolderMappedRecord.put(AppConstants.RETENT_DISPOSITION, retentDisposition);


                  boolean bexecute3 = false;
                  Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

                  if(execute3Obj != null)
                      bexecute3 = execute3Obj.booleanValue();

                  MappedRecord resultMappedRecord = null;

                  /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
                  if(bexecute3){
                      /* Creating the output record for the specific Interaction */
                      MappedRecord outputRecord = m_RecordFactory.createMappedRecord(AppConstants.UPDATE_FOLDER_OUTPUT_RECORD);
                          interaction.execute(interactionSpec,updateFolderMappedRecord,outputRecord);
                      resultMappedRecord = outputRecord;
                  }
                  else{
                      resultMappedRecord = (MappedRecord)interaction.execute(interactionSpec,updateFolderMappedRecord);
                  }
                  // System.out.println("UpdateFolder: After exiting interaction.execute method.");

                  m_RAResourceWarning = getWarningFromInteraction(interaction);

                  long lresultErrorID = new Long((resultMappedRecord.get(AppConstants.RESULT)).toString()).longValue();

                  return lresultErrorID;

              }catch(ResourceException re){
                  re.printStackTrace();
                  throw re;
              }catch(Exception e){
                  e.printStackTrace();
                  throw new ResourceException(e.getMessage());
              }finally{
                  if( interaction != null) interaction.close();
              }

          }

/**
 *  Executes the deleteFolder interaction on the Resource Adapter.
 *  This method deletes existing folders from the IS.
 *  @param java.util.Vector queueEntryList
 *                  	        List of queue entries.
 *  @param java.util.Map InteractionParmsMap
 *			        Carries interaction request information as key-value pair.
 *  @return java.util.List
 *                              List containing generic result, 0 if success OR errorcode if there is an error.
 */
   public synchronized List deleteFolder(Vector deleteFoldersVector, Map InteractionParmsMap)
                                          throws ResourceException{
      /* Cleaning the Resources before starting new Interaction */
        cleanUp();
        Interaction interaction = null;

        try{
              m_RAResourceWarning = "";
              interaction = m_Connection.createInteraction();

              FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

              /* Assigning Interaction Parameters to CciInteractionSpec */
              updateISpecWithISpecParams(interactionSpec,InteractionParmsMap);

              if(interactionSpec.getFunctionName() == null)
                    interactionSpec.setFunctionName(AppConstants.DELETE_FOLDER_FUNCTION_NAME);

              /*Create Indexed Record which will contail list of all indexed records to be inserted*/
              IndexedRecord deleteFolderIndexedRecord = m_RecordFactory.createIndexedRecord(AppConstants.DELETE_FOLDER_INDEXED_RECORD);
              for (int j=0; j< deleteFoldersVector.size();j++){
                Map eachFolderDeleteMap = (Map) deleteFoldersVector.get(j) ;
                MappedRecord folderDeleteMappedRecord = m_RecordFactory.createMappedRecord (AppConstants.FOLDER_DELETE_MAPPED_RECORD);
                folderDeleteMappedRecord.putAll(eachFolderDeleteMap);
                deleteFolderIndexedRecord.add(folderDeleteMappedRecord) ;
              }

              boolean bexecute3 = false;
              Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);

              if(execute3Obj != null)
                  bexecute3 = execute3Obj.booleanValue();

              IndexedRecord resultIndexedRecord = null;

              /* Checking Interaction_Execute_3 flag and then calling the corresponding interaction.execute method */
              //System.out.println("Delete Folder: Before executing interaction.execute method.");
              if(bexecute3){
                  /* Creating the output record for the specific Interaction */
                  IndexedRecord outputRecord =  m_RecordFactory.createIndexedRecord(AppConstants.DELETE_FOLDER_RESULT_INDEX_RECORD);
                  interaction.execute(interactionSpec,deleteFolderIndexedRecord,outputRecord);
                  resultIndexedRecord = outputRecord;
              } else {
                  resultIndexedRecord = (IndexedRecord) interaction.execute(interactionSpec,deleteFolderIndexedRecord);
              }
              //System.out.println("Delete Folder: After exiting interaction.execute method.");

              m_RAResourceWarning = getWarningFromInteraction(interaction);

              Iterator iterator = resultIndexedRecord.iterator();

              List errorTuplesList = new ArrayList();

              while(iterator.hasNext()){
                Map map1 = new HashMap();
                map1.putAll((Map)iterator.next());
                errorTuplesList.add(map1);
              }

              return errorTuplesList;

        }catch(ResourceException re){
            re.printStackTrace();
            throw re;
        }catch(Exception e){
            e.printStackTrace();
            throw new ResourceException(e.getMessage());
        }finally{
            if( interaction != null) interaction.close();
        }
    }

/**
 *  Executes the getCacheList interaction on the Resource Adapter.
 *  This interaction would be used to get the list of configured page cache from the IS .
 *  @param java.util.Map InteractionParmsMap
 *				  Carries interaction request information as key-value pair.
 *  @return List :Returns the list of cache.
 */
    public List getCacheList(Map InteractionParmsMap) throws ResourceException{

        Interaction interaction = null;

        try{
                m_RAResourceWarning = "";
                interaction = m_Connection.createInteraction();

                FN_IS_CciInteractionSpec interactionSpec = new FN_IS_CciInteractionSpec();

                /* Assigning Interaction Parameters to CciInteractionSpec */
                updateISpecWithISpecParams(interactionSpec, InteractionParmsMap);

                if(interactionSpec.getFunctionName() == null)
                          interactionSpec.setFunctionName(AppConstants.GET_CACHE_LIST_FUNCTION_NAME);

                /* Creating Input Mapped record and adding parameter (Map) to be passed to IS RA */
                MappedRecord getCacheListInputRecord = m_RecordFactory.createMappedRecord(AppConstants.GET_CACHE_LIST_INPUT_RECORD);

                boolean bexecute3 = false;
                Boolean execute3Obj = (Boolean)InteractionParmsMap.get(AppConstants.INTERACTION_EXECUTE_3);
                if(execute3Obj != null)
                        bexecute3 = execute3Obj.booleanValue();

                IndexedRecord getCacheListOutput = null;

               /* Executes the interaction, checks Interaction_Execute_3 flag and then calls the corresponding interaction.execute method*/
              // System.out.println("changePassword: Before entering interaction.execute method.");
              if(bexecute3){
                    IndexedRecord outputRecord = m_RecordFactory.createIndexedRecord (AppConstants.GET_CACHE_LIST_OUTPUT_RECORD);
                    interaction.execute(interactionSpec,getCacheListInputRecord, outputRecord);
                    getCacheListOutput = outputRecord;
              }else{
                    getCacheListOutput = (IndexedRecord)interaction.execute(interactionSpec, getCacheListInputRecord);
              }
              // System.out.println("changePassword: After exiting interaction.execute method.");
              m_RAResourceWarning = getWarningFromInteraction(interaction);

              List cacheList = new ArrayList();
              Iterator iterator = getCacheListOutput.iterator();

              while(iterator.hasNext()){
                cacheList.add(iterator.next());
              }

              return cacheList;

        }catch(ResourceException re){
                    re.printStackTrace();
                    throw re;
        }catch(Exception e){
                    e.printStackTrace();
                    throw new ResourceException(e.getMessage());
        }finally{
                 if( interaction != null) interaction.close();
        }

    }


/**
 *  Sets the Appserver name during initializing Sample Application.
 *  @param java.lang.String
 *  @return void
 */
	public void setAppServerName(String sName){
		if (sName!=null) m_AppServer = sName;

                if (sName.indexOf(WebConstants.STR_WEBSPHERE)!=-1 && sName.indexOf(WebConstants.NUM_DEC_4)==-1){
                  /*Commented by Sachin for WAS6 changes
                    m_isWebSphere5 = true;
                    ends*/
                  //Code added by Sachin or WAS6 changes
                    m_isWebSphere6 = true;
                  //Code added ENDS

                }
                if (sName.indexOf(WebConstants.STR_WEBLOGIC)!=-1){
                    m_isWebLogic = true;
                }
	}

/**
 *  Get Appserver name.
 *  @param None
 *  @return java.lang.String
 */
	public String getAppServerName(){
            return m_AppServer;
	}

/**
 *  Returns "TRUE" is AppServer is WebSphere 5.0.
 *  Or else it returns "FALSE" if Appserver is WebLogic or WebSpehere 4.2.
 *  @param None
 *  @return boolean
 */
/*Code commented for WAS6 changes
	public boolean getIsWebSphere5(){
                    return m_isWebSphere5;
	}
ends */
       //Code added for WAS6 changes
          public boolean getIsWebSphere6(){
                 return m_isWebSphere6;
     }
    //Code added ENDS


    /** Code added by for WAS6
      *  Sets the ConnectionInUse flag.
      *  It is specific to WAS6 Implementation. It is being used in scenario when
      *  more than one Interaction is executed from the same Web Container (JSPs/Servlets).
      *  Setting m_ConnectionInUse to True means that there is no need to open the
      *  connection for each and every interaction.
      *   */
        public void setConnectionKeepAlive(boolean keepAlive){
                 m_ConnectionKeepAlive = keepAlive;
             }

/**
 *  Returns "TRUE" is AppServer is BEA's WebLogic
 *  Or else it returns "FALSE"
 *  @param None
 *  @return boolean
 */
	public boolean getIsWebLogic(){
                    return m_isWebLogic;
	}
  /*Added by NJ on 05/01/06 for change password with SLU problem*/
        public void setPassword(String newPassword){
          this.m_Password = newPassword;
        }
        /*End of changes done by NJ for change password with SLU problem*/

	/* Code Added for DTS 180479 - JAAS Authentication Realm
	 * Sets the jaas flag. Based on this flag connection object is retrieve
	 * from connection factory in login method.If flag value is true then connection 
	 * is retrieve without connection spec.
	 */
	public void setJAASEnable(boolean jaasFlag){
		m_jaasFlag = jaasFlag;
	}
	//Code Added for DTS 180479 - JAAS Authentication Realm Ends
	//Added for fetching supported Interactions
		public ArrayList getSupportedInteractions() throws ResourceException{
		    try{
			    ResourceAdapterMetaData rmdata = m_ConnectionFactory.getMetaData();
			    String SuppInteractions[] = rmdata.getInteractionSpecsSupported();
			    System.out.println("The length is "+SuppInteractions.length);
			    ArrayList InteractionList=new ArrayList();
			    for(int i=0; i<SuppInteractions.length; i++){
			        InteractionList.add(SuppInteractions[i]);
			    }
			    return InteractionList;
		    }catch(ResourceException re){
		        re.printStackTrace();
		        throw re;
		    }catch(Exception e){
	            e.printStackTrace();
	            throw new ResourceException(e.getMessage());
		    }
		 }
  }


