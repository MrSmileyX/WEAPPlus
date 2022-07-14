package com.wlp.ecm.weap.config;

import com.wlp.ecm.weap.common.*;

public class DBSettings 
{
	String dbaseRegion = "";
	String dbaseUrl = "";
	String dbaseClass = "";
	String dbaseThin = "";
	String dbaseOCI = "";
	String dbaseUser = "";
	String dbasePass = "";
	String dbaseTable = "";
			
	public void setDBRegion(String dbRegion)
	{
		this.dbaseRegion = dbRegion;
	}
	
	public String getDBRegion()
	{
		return this.dbaseRegion;
	}
	
	public void setDBURL(String dbUrl)
	{
		this.dbaseUrl = dbUrl;
	}
	
	public String getDBURL()
	{
		return this.dbaseUrl;
	}
	
	public void setDBClass(String dbClass)
	{
		this.dbaseClass = dbClass;
	}
	
	public String getDBClass()
	{
		return this.dbaseClass;
	}
	
	public void setDBThin(String dbThin)
	{
		this.dbaseThin = dbThin;
	}
	
	public String getDBThin()
	{
		return this.dbaseThin;
	}
	
	public void setDBOCI(String dbOCI)
	{
		this.dbaseOCI = dbOCI;
	}
	
	public String getDBOCI()
	{
		return this.dbaseOCI;
	}
	
	public void setDBUser(String dbUser)
	{
		this.dbaseUser = dbUser;
	}
	
	public String getDBUser()
	{
		return this.dbaseUser;
	}
	
	public void setDBPass(String dbPass)
	{
		this.dbasePass = Encryption.Decrypt(dbPass);
	}
	
	public String getDBPass()
	{
		return this.dbasePass;
	}
	
	public void setDBTable(String dbTable)
	{
		this.dbaseTable = dbTable;
	}
	
	public String getDBTable()
	{
		return this.dbaseTable;
	}
	
}
