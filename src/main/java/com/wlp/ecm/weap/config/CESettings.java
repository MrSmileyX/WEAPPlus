package com.wlp.ecm.weap.config;

import java.util.ArrayList;

public  class CESettings 
{
	int ce51Version = 0;
	
	String ce51URI = null;
	String ce51User = null;
	String ce51Pass = null;
	String ce51OSName = null;
	String ce51OSType = null;
	
	int ce52Version = 0;
	String ce52URI = null;
	String ce52User = null;
	String ce52Pass = null;
	String ce52OSName = null;
	String ce52OSType = null;
	
	int ceVersion = 0;
	
	String loggerClass = "com.wellpoint.error";
	String apploggerClass = "com.wellpoint.log";
	String InitialConextFactory = "com.ibm.websphere.naming.WsnInitialContextFactory";
	
	
	public void set51Ver(String ver)
	{
		this.ce51Version = Integer.parseInt(ver);
	}
	
	public int get51Ver()
	{
		return this.ce51Version;
	}

	public void set51URI(String uri)
	{
		this.ce51URI = uri;
	}
	
	public String get51URI()
	{
		return this.ce51URI;
	}

	public void set51User(String user)
	{
		this.ce51User = user;
	}
	
	public String get51User()
	{
		return this.ce51User;
	}
	
	public void set51Pass(String pass)
	{
		this.ce51Pass = pass;
	}
	
	public String get51Pass()
	{
		return this.ce51Pass;
	}
	
	public void set51OSName(String osName)
	{
		this.ce51OSName = osName;
	}
	
	public String get51OSName()
	{
		return this.ce51OSName;
	}

	public void set51OSType(String osType)
	{
		this.ce51OSType = osType;
	}
	
	public String get51OSType()
	{
		return this.ce51OSType;
	}

	public void set52Ver(String ver)
	{
		this.ce52Version = Integer.parseInt(ver);
	}
	
	public int get52Ver()
	{
		return this.ce52Version;
	}

	public void set52URI(String uri)
	{
		this.ce52URI = uri;
	}
	
	public String get52URI()
	{
		return this.ce52URI;
	}

	public void set52User(String user)
	{
		this.ce52User = user;
	}
	
	public String get52User()
	{
		return this.ce52User;
	}
	
	public void set52Pass(String pass)
	{
		this.ce52Pass = pass;
	}
	
	public String get52Pass()
	{
		return this.ce52Pass;
	}
	
	public void set52OSName(String osName)
	{
		this.ce52OSName = osName;
	}
	
	public String get52OSName()
	{
		return this.ce52OSName;
	}

	public void set52OSType(String osType)
	{
		this.ce52OSType = osType;
	}
	
	public String get52OSType()
	{
		return this.ce52OSType;
	}

	public String getLoggerClass()
	{
		return this.loggerClass;
	}
	
	public String getAppLogClass()
	{
		return this.apploggerClass;
	}
	
	public String getInitialContext()
	{
		return this.InitialConextFactory;
	}
	
	public void setCEVersion(int ceVer)
	{
		this.ceVersion = ceVer;
	}
	
	public int getCEVersion()
	{
		return this.ceVersion;
	}
}

