package com.wlp.ecm.weap.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.exception.WeapException;

public class DocumentClassPropertyMapping 
{
	
	private Map<String, WeapDocumentClass> documentClasses = null;
	
	public DocumentClassPropertyMapping() 
	{
	
	}

	public void setClassMappingXml(String classMappingXml) throws WeapException {
		
		LoggingUtil.LogTraceStartMsg();
		Document doc = null;
		
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(classMappingXml);
			doc = db.parse(is);
		} 
		catch (SAXException e) 
		{
			throw new WeapException("E1001", e);
		} 
		catch (IOException e) 
		{
			throw new WeapException("E1001", e);
		} 
		catch (ParserConfigurationException e) 
		{
			throw new WeapException("E1001", e);
		}
		
		// use HashTable because it is synchronized.
		documentClasses = new Hashtable<String, WeapDocumentClass>();
		
		NodeList classNodes = doc.getElementsByTagName("class");
		
		for (int iter=0; iter < classNodes.getLength(); iter++) 
		{
			Node classNode = classNodes.item(iter);
			
			if (classNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				WeapDocumentClass docClass = new WeapDocumentClass((Element)classNode);
				documentClasses.put(docClass.getName(), docClass);
			}
		}
		
		LoggingUtil.LogTraceEndMsg();
	}
	
	public Map<String, WeapDocumentClass> getDocumentClasses() 
	{
		return documentClasses;
	}

}
