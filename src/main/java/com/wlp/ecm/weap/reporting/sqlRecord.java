package com.wlp.ecm.weap.reporting;

import java.util.ArrayList;

public class sqlRecord
{
	String sqlNumber;
	long rowCount;
	
	private String sqlName = "";

	ArrayList<sqlField> sqlFields = new ArrayList<sqlField>();
	
	public sqlRecord(String qn)
	{
		this.setQueryNumber(qn);
		this.setRowCount(0);
	}

	public void addField(sqlField inField)
	{
		this.sqlFields.add(inField);
	}
	
	public void setQueryNumber(String qn)
	{
		this.sqlNumber = qn;
	}
	
	public void setName(String nameVal)
	{
		this.sqlName = nameVal;
	}
	
	public void setRowCount(long val)
	{
		this.rowCount = val;
	}
	
	public String getName()
	{
		return this.sqlName;
	}
			
	public long getRowCount()
	{
		return this.rowCount;
	}
	
	public long getFieldCount()
	{
		return this.sqlFields.size();
	}
	
	public String createQuery()
	{
		ArrayList<String> createCols = new ArrayList<String>();
		
		String createHdr = "";
		String tableName = "";
		String createEnd = "";
		String openParen = "(";
		String colRow = "";
		String newLine = "\r\n";
		
		createHdr = "CREATE TABLE ";
		tableName = this.getName();
		createEnd = ")";
		
		String queryPartA = createHdr + tableName + newLine + openParen;
		String colInfo = "";
		
		for (int f = 0; f < this.sqlFields.size(); f++)
		{
			sqlField newField = this.sqlFields.get(f);
			
			String sizeDef = "";
			
			if (newField.getSize().trim().isEmpty())
			{
				sizeDef = "";
			}
			else
			{
				sizeDef = "(" + newField.getSize() + ")";
			}
			
			String column = newField.getDesc() + " " + newField.getType() + sizeDef + " " + newField.getNull();  
		
			if (f == 0)
			{
				colInfo += column;
			}
			else
			{
				colInfo += "," + newLine + column;
			}
		}
		
		String fullQuery = queryPartA + colInfo + createEnd; 
		
		return fullQuery;
	}
		
}
