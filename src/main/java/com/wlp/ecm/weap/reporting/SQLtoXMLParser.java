package com.wlp.ecm.weap.reporting;

import java.io.FileInputStream;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.jaxb.Table;
import com.wlp.ecm.weap.config.jaxb.Tables;
import com.wlp.ecm.weap.config.jaxb.Field;

public class SQLtoXMLParser 
{
	protected static synchronized sqlRecord createSQLRecord(String xmlPath)
	{
		sqlRecord nextRec = null;
		
		try 
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Tables.class);
	
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Tables tblConfig = null;
			FileInputStream fis = null;
			
			try 
			{
				fis = new FileInputStream(xmlPath);
				tblConfig = (Tables)jaxbUnmarshaller.unmarshal(fis);
			
				if (tblConfig.getTable().size() > 0)
				{
					Table inTable = tblConfig.getTable().get(0);
					String tblID = inTable.getID();
					
					nextRec = new sqlRecord(tblID);
					nextRec.setName(inTable.getName());					
					
					ArrayList<Field> inFields = new ArrayList<Field>();
					
					for (int f = 0; f < inTable.fields.field.size(); f++)
					{
						Field thisFld = inTable.fields.field.get(f); 
						sqlField tempFld = new sqlField(tblID);
						
						tempFld.setDesc(thisFld.getDesc());
						tempFld.setType(thisFld.getType());
						tempFld.setSize(thisFld.getSize());
						tempFld.setNull(thisFld.getNull());
												
						nextRec.addField(tempFld);
					}
					
					WriteLog.WriteToLog("Fields found: " + nextRec.sqlFields.size());
				}
			}
			catch(Exception e)
			{
				WriteLog.WriteToLog(e.getMessage());
			}
			
		}
		catch (JAXBException e) 
		{
			WriteLog.WriteToLog(e.getMessage());
		} 
		
		return nextRec;
	}
}
