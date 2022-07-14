package com.wlp.ecm.weap.config;

import java.util.ArrayList;

import org.springframework.core.env.Environment;

public class WeapConfigSettings 
{
	String templateDirectory = null;
	String contentEngineServerUri = null;
	String repositoryType = null;
	
	String objectStoreNameEast = null;
	String objectStoreNameWest = null;
	String objectStoreNameCentral = null;
	
	String israLibraryNameEast = null;
	String israLibraryNameWest = null;
	String israLibraryNameCentral = null;
	
	String israUserIdEast=null;
	String israUserPasswordEast=null;
	String israUserIdWest=null;
	
	String israUserPasswordWest=null;
	String israUserIdCentral=null;
	String israUserPasswordCentral=null;

	String debugDataDirectory = null;
	String messageCharacterSet = null;
	String batchIdFilePath = null;
	
	Environment environment = null;
	
	String tableDefPath = null;
	
	ArrayList<String> defApp = new ArrayList<String>();
	
	int appCountVal = 0;
	
	public void setAppCount(String appCountProp)
	{
		this.appCountVal = Integer.parseInt(appCountProp);		
	}
	
	public int getAppCount()
	{
		return this.appCountVal;
	}
	
	public void setTableDefPath(String theTablePath)
	{
		this.tableDefPath = theTablePath;
	}
	
	public String getTableDefPath()
	{
		return this.tableDefPath;
	}
	
	public void addDefaultApp(String appType)
	{
		this.defApp.add(appType);
	}
	
	public ArrayList<String> getDefaultApps()
	{
		return this.defApp;
	}
	
	public void setContentEngineServerUri(String contentEngineServerUri) 
	{
		this.contentEngineServerUri = contentEngineServerUri;
	}

	public String getContentEngineServerUri() 
	{
		return contentEngineServerUri;
	}

	public String getTemplateDirectory() 
	{
		return templateDirectory;
	}

	public void setTemplateDirectory(String templateDirectory) 
	{
		this.templateDirectory = templateDirectory;
	}

	public String getObjectStoreNameEast() 
	{
		return objectStoreNameEast;
	}

	public void setObjectStoreNameEast(String objectStoreNameEast) 
	{
		this.objectStoreNameEast = objectStoreNameEast;
	}

	public String getObjectStoreNameWest() 
	{
		return objectStoreNameWest;
	}

	public void setObjectStoreNameWest(String objectStoreNameWest) 
	{
		this.objectStoreNameWest = objectStoreNameWest;
	}

	public String getObjectStoreNameCentral() 
	{
		return objectStoreNameCentral;
	}

	public void setObjectStoreNameCentral(String objectStoreNameCentral) 
	{
		this.objectStoreNameCentral = objectStoreNameCentral;
	}

	public String getIsraLibraryNameEast() 
	{
		return israLibraryNameEast;
	}

	public void setIsraLibraryNameEast(String israLibraryNameEast) 
	{
		this.israLibraryNameEast = israLibraryNameEast;
	}

	public String getIsraLibraryNameWest() 
	{
		return israLibraryNameWest;
	}

	public void setIsraLibraryNameWest(String israLibraryNameWest) 
	{
		this.israLibraryNameWest = israLibraryNameWest;
	}

	public String getIsraLibraryNameCentral() 
	{
		return israLibraryNameCentral;
	}

	public void setIsraLibraryNameCentral(String israLibraryNameCentral) 
	{
		this.israLibraryNameCentral = israLibraryNameCentral;
	}

	public String getIsraUserIdEast() 
	{
		return israUserIdEast;
	}

	public void setIsraUserIdEast(String israUserIdEast) 
	{
		this.israUserIdEast = israUserIdEast;
	}

	public String getIsraUserPasswordEast() 
	{
		return israUserPasswordEast;
	}

	public void setIsraUserPasswordEast(String israUserPasswordEast) 
	{
		this.israUserPasswordEast = israUserPasswordEast;
	}

	public String getIsraUserIdWest() 
	{
		return israUserIdWest;
	}

	public void setIsraUserIdWest(String israUserIdWest) 
	{
		this.israUserIdWest = israUserIdWest;
	}

	public String getIsraUserPasswordWest() 
	{
		return israUserPasswordWest;
	}

	public void setIsraUserPasswordWest(String israUserPasswordWest) 
	{
		this.israUserPasswordWest = israUserPasswordWest;
	}

	public String getIsraUserIdCentral() 
	{
		return israUserIdCentral;
	}

	public void setIsraUserIdCentral(String israUserIdCentral) 
	{
		this.israUserIdCentral = israUserIdCentral;
	}

	public String getIsraUserPasswordCentral() 
	{
		return israUserPasswordCentral;
	}

	public void setIsraUserPasswordCentral(String israUserPasswordCentral) 
	{
		this.israUserPasswordCentral = israUserPasswordCentral;
	}

	public void setEnvironment(Environment environment) 
	{
		this.environment = environment;
	}
	
	public boolean containsProperty(String propName) 
	{
		return environment.containsProperty(propName);
	}

	public boolean containsProperty(String category, String propName) 
	{
		return environment.containsProperty(category + "." + propName);
	}
	
	public String getPropertyString(String propName) 
	{
		return removeQuotes(environment.getProperty(propName));
	}
	
	public String getPropertyString(String category, String propName) 
	{
		return removeQuotes( environment.getProperty(category + "." + propName));
	}
	
	public String getDebugDataDirectory() 
	{
		return debugDataDirectory;
	}

	public void setDebugDataDirectory(String debugDataDirectory) 
	{
		this.debugDataDirectory = debugDataDirectory;
	}

	public String getMessageCharacterSet() 
	{
		return messageCharacterSet;
	}

	public void setMessageCharacterSet(String messageCharacterSet) 
	{
		this.messageCharacterSet = messageCharacterSet;
	}

	public String getBatchIdFilePath() 
	{
		return batchIdFilePath;
	}

	public void setBatchIdFilePath(String batchIdFilePath) 
	{
		this.batchIdFilePath = batchIdFilePath;
	}

	private String removeQuotes(String val) 
	{
		if (val.startsWith("\"") && val.endsWith("\"")) 
		{
			return val.substring(1, val.length()-1);
		}
		else
		{
			return val;
		}
	}
}
