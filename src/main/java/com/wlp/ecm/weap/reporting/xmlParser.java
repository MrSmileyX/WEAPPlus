package com.wlp.ecm.weap.reporting;

import com.wlp.ecm.weap.common.*;
import com.wlp.ecm.weap.config.jaxb.BatchIdConfig;
import com.wlp.ecm.weap.config.jaxb.Tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlParser 
{	
	public static sqlRecord ReadSQLXML(String xmlPath)
	{
		sqlRecord nextRec = null;
		boolean validNode = false;
		int sqlRecsFnd  = 0;
		ArrayList<sqlField> fields = new ArrayList<sqlField>();
		
		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			WriteLog.WriteToLog("Loading XML (" + xmlPath + ") data to create table...");
			
			WriteLog.WriteToLog("Generating file input stream...");
			InputStream xmlFile = getXMLStream(xmlPath);
					
			if (xmlFile != null)
			{
				Document document = builder.parse(xmlFile);
						
				WriteLog.WriteToLog("Generating file input stream...");
				InputStream myIS = getXMLStream(xmlPath);
				
				//Iterating through the nodes and extracting the data.
				WriteLog.WriteToLog("Parsing XML data to create table...");
				NodeList nodeList = document.getDocumentElement().getChildNodes();
		
				for (int i = 0; i < nodeList.getLength(); i++) 
				{
					Node node = nodeList.item(i);
					
				    if (node instanceof Element) 
				    {
				    	validNode = true;
				    	String dbID = node.getAttributes().getNamedItem("ID").getNodeValue();
				    	
				    	WriteLog.WriteToLog("ID: " + dbID);
				    	nextRec = new sqlRecord(dbID);
	
				    	sqlRecsFnd++;
				    	
				    	WriteLog.WriteToLog("Records Found: " + sqlRecsFnd);
				    	NodeList childNodes = node.getChildNodes();
				    	WriteLog.WriteToLog("Child Records Found: " + childNodes.getLength());
				  
				    	for (int x = 0; x < childNodes.getLength(); x++)
				    	{
				    		Node thisNode = childNodes.item(x);
				    		String nodeName =  thisNode.getNodeName();
				    		
				    		if (nodeName.equals("Name"))
				    		{
				    			nextRec.setName(thisNode.getTextContent());
				    		}
				    		
				    		if (nodeName.equals("Fields"))
				    		{
				    			NodeList secNodes = thisNode.getChildNodes();
				    			
				    			for (int y = 0; y < secNodes.getLength(); y++)
						    	{
				    				Node fldNode = secNodes.item(y);
						    		String subNodeName =  fldNode.getNodeName();
						    							    
				    				if (subNodeName.equals("Field"))
						    		{
				    					sqlField tempFld = new sqlField(dbID);
				    					
						    			NodeList babyNodes = fldNode.getChildNodes();
						    			
						    			for(int z = 0; z < babyNodes.getLength(); z++)
						    			{
						    				Node nowNode = babyNodes.item(z);
						    				
						    				String fieldName =  nowNode.getNodeName();
						    				
						    				if(fieldName.equalsIgnoreCase("DESC"))
						    				{
						    					tempFld.setDesc(nowNode.getTextContent());
						    				}
						    				else if(fieldName.equalsIgnoreCase("TYPE"))
						    				{
						    					tempFld.setType(nowNode.getTextContent());
						    				}
						    				else if(fieldName.equalsIgnoreCase("SIZE"))
						    				{
						    					tempFld.setSize(nowNode.getTextContent());
						    				}
						    				else if(fieldName.equalsIgnoreCase("NULL"))
						    				{
						    					tempFld.setNull(nowNode.getTextContent());
						    				}
						    			}
						    			
						    			nextRec.addField(tempFld);
						    		}
						    	}
				    		}
				    	}
				    	
				    }
				    else
				    {
				    	validNode = false;
				    }
				}
			}
			else
			{
				WriteLog.WriteToLog("Unable to load XML file: " + xmlPath + "!");
			}
			
			if (nextRec.sqlFields.size() > 0)
			{
				WriteLog.WriteToLog(nextRec.sqlFields.size() + " fields added.");
			}
			else
			{
				WriteLog.WriteToLog("No fields added!");
			}
		}
		catch(ParserConfigurationException pce)
		{
			WriteLog.WriteToLog(pce.getMessage());
		}
		catch(SAXException saxe)
		{
			WriteLog.WriteToLog(saxe.getMessage());
		}
		catch(IOException ioe)
		{
			WriteLog.WriteToLog(ioe.getMessage());
		}
		
		return nextRec;
	}
	
	public static InputStream getXMLStream(String xmlPath)
	{
		FileInputStream fis = null;
		
		try 
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Tables.class);
	
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Tables tableConfig = null;
			
			try 
			{
				fis = new FileInputStream(xmlPath);
				tableConfig = (Tables)jaxbUnmarshaller.unmarshal(fis);
			}
			finally 
			{
				if (fis != null) 
				{
					try 
					{
						fis.close();
					}
					catch (IOException e) 
					{
					}
				}
			}
		}
		catch (JAXBException e) 
		{
			WriteLog.WriteToLog(e.getMessage());
		} 
		catch (FileNotFoundException e) 
		{
			WriteLog.WriteToLog(e.getMessage());
		}
		
		return fis;
	}
	
	public static void ModifyXML(sqlRecord currRpt, String xmlFile) 
	{
		String parentTag = "";
		String dateTag = "";
		String lastRunDate = "";
		
		try 
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFile);

			// Get the root element
			Node company = doc.getFirstChild();

			// Get the staff element by tag name directly
			Node parentNode = doc.getElementsByTagName(parentTag).item(0);

			// loop the staff child node
 			NodeList list = parentNode.getChildNodes();

			for (int i = 0; i < list.getLength(); i++) 
			{
				Node node = list.item(i);

				if (dateTag.equals(node.getNodeName())) 
				{
					node.setTextContent(lastRunDate);
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
	
			DOMSource source = new DOMSource(doc);
			
			StreamResult result = new StreamResult(new File(xmlFile));
			transformer.transform(source, result);
	
			WriteLog.WriteToLog("Done");
		}
		catch (ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		} 
		catch (TransformerException tfe) 
		{
			tfe.printStackTrace();
		} 
		catch (IOException ioe) 
		{
			ioe.printStackTrace();
		} 
		catch (SAXException sae) 
		{
			sae.printStackTrace();
		}
	}
}
