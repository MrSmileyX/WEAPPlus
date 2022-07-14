package com.wlp.ecm.weap.config.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="batchid-config")
@XmlAccessorType(XmlAccessType.NONE)
public class BatchIdConfig 
{
	
	@XmlElement(name="seed", required=true, defaultValue="1")
	private int batchIdSeed = 0;

	@XmlElement(name="latest-number", required=true, defaultValue="1")
	private int batchLatestNumber = 0;

	@XmlElement(name="latest-day", required=true)
	private String batchLatestDay = null;
	
	public int getBatchIdSeed() 
	{
		return batchIdSeed;
	}
	
	public void setBatchIdSeed(int batchIdSeed) 
	{		
		this.batchIdSeed = batchIdSeed;
	}
	
	public int getBatchLatestNumber() 
	{
		return batchLatestNumber;
	}
	
	public void setBatchLatestNumber(int batchLatestNumber) 
	{
		this.batchLatestNumber = batchLatestNumber;
	}
	
	public String getBatchLatestDay() 
	{
		return batchLatestDay;
	}
	
	public void setBatchLatestDay(String batchLatestDay) 
	{
		this.batchLatestDay = batchLatestDay;
	}
}
