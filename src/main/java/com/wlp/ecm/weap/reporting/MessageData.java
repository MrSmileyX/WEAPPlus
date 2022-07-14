package com.wlp.ecm.weap.reporting;

import com.wlp.ecm.weap.data.WeapMetadata;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.message.WeapRegion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageData 
{
	private String messageID = null;
	private String msgTimeStamp = null;
	private String procTimeStamp = null;
	private String serverName = null;
	private String acn = null;
	private String dcn = null;
	private String mbu = null;
	private String memberNum = null;
	private String stateCd = null;
	private String regionCd = null;
	private String firstName = null;
	private String lastName = null;
	private String brandVal = null;
	private String appType = null;
	private String docClass = null;
	private String repositoryDesc = null;
	private String repositoryName = null;
	private String coverPageCnt = null;
	private String coverDocNum = null;
	private String coverFormat = null;
	private String coverStatus = null;
	private String coverMessage = null;
	private String appPageCnt = null;
	private String appDocNum = null;
	private String appFormat = null;
	private String appStatus = null;
	private String appMessage = null;
	private String bkupFileNm = null;
	
	public MessageData (WeapMetadata wmd, String messageID)
	{
		MessageData newMessage = null;
		
		this.setMessageID(messageID);
		this.setProcTimeStamp(getDateVal());
		this.setServerName(WriteLog.getServerName());
		this.setACN(wmd.getAcn());
		this.setDCN(wmd.getDcn());
		this.setMBU(wmd.getMbu());
		this.setMemberNum(wmd.getMemberNumber());
		this.setStateCode(wmd.getStateCd());
		this.setRegionCode(wmd.getWeapRegion());
		this.setFirstName(wmd.getFirstName());
		this.setLastName(wmd.getLastName());
		this.setBrandVal(wmd.getBrand());
		this.setAppType(wmd.getApplicationType());
		this.setDocClass(wmd.getDocumentClass());
		this.setRepoName(wmd.getRepository());
		this.setRepoDesc(wmd.getRepositoryName());
	}
	
	public String getDateVal()
	{
		String dateVal = "";
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS aa (z)");
		Date dtNow = new Date();
		dateVal = df.format(dtNow);
		
		return dateVal;
	}
	
	public void setMessageID(String msgID)
	{
		this.messageID = msgID;
	}
	
	public void setMsgTimeStamp(String msgTs)
	{
		this.msgTimeStamp = msgTs;
	}
	
	public void setProcTimeStamp(String procTs)
	{
		this.procTimeStamp = procTs;
	}
	
	public void setServerName(String srvrNm)
	{
		this.serverName = srvrNm;
	}
	
	public void setACN(String acn)
	{
		this.acn = acn;
	}
	
	public void setDCN(String dcn)
	{
		this.dcn = dcn;
	}
	
	public void setMBU(String mbu)
	{
		this.mbu = mbu;
	}
	
	public void setMemberNum(String mbrNbr)
	{
		this.memberNum = mbrNbr;
	}
	
	public void setStateCode(String stateCd)
	{
		this.stateCd = stateCd;
	}
	
	public void setRegionCode(WeapRegion regionCd)
	{
		this.regionCd = regionCd.toString();
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	public void setBrandVal(String brandVal)
	{
		this.brandVal = brandVal;
	}
	
	public void setAppType(String appType)
	{
		this.appType = appType;
	}
	
	public void setDocClass(String docClass)
	{
		this.docClass = docClass;
	}
	
	public void setRepoDesc(String repoType)
	{
		this.repositoryDesc = repoType;
	}
	
	public void setRepoName(String repoName)
	{
		this.repositoryName = repoName;
	}
	
	public void setCoverPageCnt(String cpPgCnt)
	{
		if (cpPgCnt.equals("") || cpPgCnt.isEmpty()  ||  cpPgCnt == null)
		{
			cpPgCnt = "0";
		}
		
		this.coverPageCnt = cpPgCnt;
	}
	
	public void setCoverDocNum(String cpDocNum)
	{
		if (cpDocNum.equals("") || cpDocNum.isEmpty()  ||  cpDocNum == null)
		{
			cpDocNum = "0";
		}
		
		this.coverDocNum = cpDocNum;
	}
	
	public void setCoverFmt(String cpFormat)
	{
		this.coverFormat = cpFormat;
	}
	
	public void setCoverStatus(String cpStatus)
	{
		this.coverStatus = cpStatus;
	}
	
	public void setCoverMessage(String cpMessage)
	{
		this.coverMessage = cpMessage;
	}
	
	public void setAppPageCnt(String appPgCnt)
	{
		if (appPgCnt.equals("") || appPgCnt.isEmpty()  ||  appPgCnt == null)
		{
			appPgCnt = "0";
		}
		
		this.appPageCnt = appPgCnt;
	}
	
	public void setAppDocNum(String appDocNum)
	{
		if (appDocNum.equals("") || appDocNum.isEmpty()  ||  appDocNum == null)
		{
			appDocNum = "0";
		}
		
		this.appDocNum = appDocNum;
	}
	
	public void setAppFmt(String appFormat)
	{
		this.appFormat = appFormat;
	}
	
	public void setAppStatus(String appStatus)
	{
		this.appStatus = appStatus;
	}
	
	public void setAppMessage(String appMessage)
	{
		this.appMessage = appMessage;
	}
	
	public void setBkupFileNm(String bkupFile)
	{
		this.bkupFileNm = bkupFile;
	}

	public String getMessageID()
	{
		return this.messageID;
	}
	
	public String getMsgTimeStamp()
	{
		return this.msgTimeStamp;
	}
	
	public String getProcTimeStamp()
	{
		return this.procTimeStamp;
	}
		
	public String getServerName()
	{
		return this.serverName;
	}
	
	public String getACN()
	{
		return this.acn;
	}
	
	public String getDCN()
	{
		return this.dcn;
	}
	
	public String getMBU()
	{
		return this.mbu;
	}
	
	public String getMemberNum()
	{
		return this.memberNum;
	}
	
	public String getStateCode()
	{
		return this.stateCd;
	}
	
	public String getRegionCode()
	{
		return this.regionCd;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public String getBrandVal()
	{
		return this.brandVal;
	}
	
	public String getAppType()
	{
		return this.appType;
	}
	
	public String getDocClass()
	{
		return this.docClass;
	}
	
	public String getRepoDesc()
	{
		return this.repositoryDesc;
	}
	
	public String getRepoName()
	{
		return this.repositoryName;
	}
	
	public String getCoverPageCnt()
	{
		return this.coverPageCnt;
	}
	
	public String getCoverDocNum()
	{
		return this.coverDocNum;
	}
	
	public String getCoverFmt()
	{
		return this.coverFormat;
	}
	
	public String getCoverStatus()
	{
		return this.coverStatus;
	}
	
	public String getCoverMessage()
	{
		return this.coverMessage;
	}
	
	public String getAppPageCnt()
	{
		return this.appPageCnt;
	}
	
	public String getAppDocNum()
	{
		return this.appDocNum;
	}
	
	public String getAppFmt()
	{
		return this.appFormat;
	}
	
	public String getAppStatus()
	{
		return this.appStatus;
	}
	
	public String getAppMessage()
	{
		return this.appMessage;
	}
	
	public String getBkupFileNm()
	{
		return this.bkupFileNm;
	}

}
