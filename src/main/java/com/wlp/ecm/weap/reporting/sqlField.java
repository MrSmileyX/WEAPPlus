package com.wlp.ecm.weap.reporting;
public class sqlField 
{
	private String queryNbr = "";
	private String fldDesc = "";
	private String fldType = "";
	private String fldSize = "";
	private String fldNull = "";
	
	public sqlField(String queryNum)
	{
		this.setNumber(queryNum);
	}
	
	public void setNumber(String qn)
	{
		this.queryNbr = qn;
	}
	
	public void setDesc(String descVal)
	{
		this.fldDesc = descVal;
	}
	
	public void setType(String typeVal)
	{
		this.fldType = typeVal;
	}
	
	public void setSize(String sizeVal)
	{
		this.fldSize = sizeVal;
	}
	
	public void setNull(String nullVal)
	{
		this.fldNull = nullVal;
	}
	
	public String getNumber()
	{
		return this.queryNbr;
	}
	
	public String getDesc()
	{
		return this.fldDesc;
	}
	
	public String getType()
	{
		return this.fldType;
	}
	
	public String getSize()
	{
		return this.fldSize;
	}
	
	public String getNull()
	{
		return this.fldNull;
	}
}
