package com.wlp.ecm.weap.data;

import com.filenet.api.util.ConfigurationParameters;
import com.filenet.api.constants.ConfigurationParameter;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.wlp.ecm.weap.common.*;
import com.wlp.ecm.weap.config.*;
import java.io.InputStream;
import java.util.Properties;


public class WeapCEConnector 
{
	public ObjectStore CELogon(CESettings ceConfig)
	{
		ObjectStore ceObjStore = null;

		Properties systemProps = System.getProperties();
		
		systemProps.put("com.ibm.CORBA.ConfigURL","/opt/IBM/FileNet/AE/Router/batchLib/sas.client.props");
		
		systemProps.put("java.naming.factory.initial","com.ibm.websphere.naming.WsnInitialContextFactory");
		systemProps.put("java.naming.provider.url", ceConfig.get51URI());
		systemProps.put("java.security.auth.login.config","/opt/IBM/FileNet/AE/CE_API/config/jaas.conf.WebSphere");
		
		systemProps.put("javax.net.debug","all");
		
		systemProps.put("javax.net.ssl.trustStore","/opt/IBM/FileNet/AE/Router/batchLib/security/cacerts");
		systemProps.put("javax.net.ssl.trustStoreType","JKS");
		systemProps.put("javax.net.ssl.trustStorePassword","changeit");
		
		systemProps.put("com.ibm.jsse2.disableSSLv3","false");
		
		System.setProperties(systemProps);

		switch (ceConfig.getCEVersion())
		{
			case 51:
				WriteLog.WriteToLog("CELogon51!");
				ceObjStore = CELogon51(ceConfig);
				break;
			case 52:
				WriteLog.WriteToLog("CELogon52!");
				ceObjStore = CELogon51(ceConfig);
				break;
			default:
				WriteLog.WriteToLog("Invalid CE Version! Version: " + ceConfig.getCEVersion());
				ceObjStore = null;
				break;
		}		
		
		return ceObjStore;
	}
	
	
	public ObjectStore CELogon52(CESettings ceConfig)
	{
		ObjectStore ceObjStore = null;
		com.filenet.api.core.Connection ceConn = null;
		Domain domain = null;
	  
		UserContext uc = null;
		javax.security.auth.Subject subject = null;
		    
		//String jacePath52 = "/lib/Jace-5.2.1.jar";
		
		try
		{
			//ConfigurationParameters parameters = new ConfigurationParameters();
			//WriteLog.WriteToLog("Setting JACE Jar path... " + jacePath52);
			//parameters.setParameter(ConfigurationParameter.CONNECTION_IMPLEMENTATION_API_LOCATION, jacePath52);

			WriteLog.WriteToLog("Creating connection to CE 5.2.");
			WriteLog.WriteToLog("Connecting to " + ceConfig.get52URI() + " as user " + ceConfig.get52User() + "...");
			
			ceConn = com.filenet.api.core.Factory.Connection.getConnection(ceConfig.get52URI(), null);
					
			WriteLog.WriteToLog("Getting user context...");
		    uc = UserContext.get();
		    
		    WriteLog.WriteToLog("Getting subject...");
		    subject = UserContext.createSubject(ceConn, ceConfig.get52User(), ceConfig.get52Pass(), ceConfig.get52OSType());
		    
		    WriteLog.WriteToLog("Pushing subject...");
		    uc.pushSubject(subject);
		    
		    WriteLog.WriteToLog("Fetching domain...");
		    domain = com.filenet.api.core.Factory.Domain.fetchInstance(ceConn, null, null);

		    WriteLog.WriteToLog("Getting the object store: " + ceConfig.get52OSName() + ".");
		    ceObjStore = Factory.ObjectStore.fetchInstance((com.filenet.api.core.Domain) domain, ceConfig.get52OSName(), null);
		    
		    WriteLog.WriteToLog("Connected to CE.");
		    
		}
		catch(Exception objException)
		{
			WriteLog.WriteToLog(objException.getMessage());
			
			if(objException != null && objException.getMessage().indexOf("Unable to use the specified connection to communicate with the server") >- 1)
			{  
				WriteLog.WriteToLog("Content Engine is down. Verify all content services.");
			}
			else if(objException != null && objException.getMessage().indexOf("The user is not authenticated") > -1)
			{
				WriteLog.WriteToLog("Invalid User ID or Password");
			}
			else if(objException != null && objException.getMessage().indexOf("The URI for server communication cannot be determined") >- 1)
			{
				WriteLog.WriteToLog("CE URI is not accessible." + objException);
			}
			else if(objException != null && objException.getMessage().indexOf("An error was returned from the server. A Web services request received an HTTP response that did not have a SOAP") >- 1)
			{
				WriteLog.WriteToLog("Content Engine is down[" + "" + "]. Verify all content services.");
			}
			else
			{
				WriteLog.WriteToLog("Failed to Establish Session with CE.");
			}
					
			ceConn = null;
		}
	    
		return ceObjStore;
	}
	
	
	public ObjectStore CELogon51(CESettings ceConfig)
	{
		WriteLog.WriteToLog("OS set to null...");
		ObjectStore ceObjStore = null;
	   
		WriteLog.WriteToLog("Connection set to null...");
		com.filenet.api.core.Connection ceConn = null;
		
		WriteLog.WriteToLog("Domain set to null...");
		Domain domain = null;
	              
		WriteLog.WriteToLog("User Context set to null...");
		UserContext uc = null;
		
		WriteLog.WriteToLog("Auth Subject set to null...");
		javax.security.auth.Subject subject = null;
		
		try
		{
			WriteLog.WriteToLog("Initializing configuration parameters...");
			ConfigurationParameters parameters = new ConfigurationParameters();
			
			WriteLog.WriteToLog("Setting timeout to 120000 ms...");
			Integer connTimeout = 120000;
			
			WriteLog.WriteToLog("Setting timeout parameter...");
			parameters.setParameter(ConfigurationParameter.WSI_TRANSPORT_CONNECTION_TIMEOUT, connTimeout); 
			
			WriteLog.WriteToLog("Creating connection to CE 5.1.");
			WriteLog.WriteToLog("Connecting to " + ceConfig.get51URI() + " as user " + ceConfig.get51User() + "...");
			
			ceConn = com.filenet.api.core.Factory.Connection.getConnection(ceConfig.get51URI(), parameters);
			
			WriteLog.WriteToLog("Getting user context...");
			uc = UserContext.get();
			
			WriteLog.WriteToLog("Creating subject...");
		    subject = UserContext.createSubject(ceConn, ceConfig.get51User(), ceConfig.get51Pass(), ceConfig.get51OSType());
		    
		    WriteLog.WriteToLog("Pushing subject...");
		    uc.pushSubject(subject);
		    
		    WriteLog.WriteToLog("Fetching domain...");
		    domain = com.filenet.api.core.Factory.Domain.fetchInstance(ceConn, null, null);
		    
		    WriteLog.WriteToLog("Fetching object store...");
		    ceObjStore = Factory.ObjectStore.fetchInstance((com.filenet.api.core.Domain) domain, ceConfig.get51OSName(), null); 
		}
		catch(Exception e)
		{
			WriteLog.WriteToLog(e.getMessage());
			domain = null;
		}
	    
		WriteLog.WriteToLog("Returning object store...");
		return ceObjStore;
	}
	  
	
}