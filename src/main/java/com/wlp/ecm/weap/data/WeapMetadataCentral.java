package com.wlp.ecm.weap.data;

import java.util.ArrayList;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.message.WeapMessageHeader;
import com.wlp.ecm.weap.message.WeapRegion;

public class WeapMetadataCentral extends WeapMetadata {
	
	protected WeapMetadataCentral(WeapConfigSettings config, WeapMessageHeader msgHeader) {
		
		super(config, msgHeader, WeapRegion.CENTRAL);
		LoggingUtil.LogTraceStartMsg();
		
		boolean isDefault = false;
		
		setDcn(msgHeader.getAcn());
		setSystemId("FACETS");
		setRouteCode(msgHeader.getMbu());
		setMemberNumber(applyStringPrefix(msgHeader.getHcid(), "0", 10));
        setProviderNumber(msgHeader.getBrokerTin());
        setAltId(msgHeader.getApplicantSsn());
        setReturnReason(msgHeader.getApplicantLastName());
        setSpouseSsn(msgHeader.getSpouseSsn());
        String partnerId = msgHeader.getPartnerId(); 
        setPartnerId(partnerId);
        
		setMedicareID(msgHeader.getMedicareID());
		setProductType(msgHeader.getProductType());
		setTranCode(msgHeader.getTranCode());
		setAltSysId(msgHeader.getMbu() + "_" + msgHeader.getStateCd());
		setFullName(msgHeader.getApplicantLastName() + " " + msgHeader.getApplicantFirstName());
		
        // sTrackNum = msgHeader.get
		if (partnerId.equalsIgnoreCase("EHEALTH")) 
		{
			setDocumentClass( "MW_IDV_MEM");  //913
		} 
		else 
		{
			setDocumentClass( "MW_IDV_MEM");  // 907
		}
		setHostDate(msgHeader.getReqEffDate());
		setOrigDate(msgHeader.getAppRecDate());
        setDocumentType("");
        setSourceId("");

        String appType = msgHeader.getAppType();

        if (appType.toUpperCase().startsWith("S")) 
		{
			setDocumentType("INDVSHTERMAPP");
		}
		
		if (appType.equalsIgnoreCase("OPEN ENROLLMENT")) 
		{
			setDocumentType("INDVHIPAAAPPS");
		}
		
		if (appType.equalsIgnoreCase("DENTAL")) 
		{
			setDocumentType("INDVDENTALAPP");
            setSourceId("MQ_COI_IDV");
            setRouteCode("IDV");
		}
		
		if (partnerId.equalsIgnoreCase("EHEALTH") && !appType.equalsIgnoreCase("UWLETTER")) 
		{
			setDocumentType("INDVEHEALTHAPP");
		}
		
		if (partnerId.equalsIgnoreCase("INTACT") || partnerId.equalsIgnoreCase("OLS")) 
		{
			setDocumentType("INDVMEDSUPPAPP");
            setSourceId("MQ_COI_IDV");
            setRouteCode("IDV");
		}
		
		if (appType.equalsIgnoreCase("UWLETTER")) 
		{
            setDocumentType("IDVUWLetter");
            setSourceId("IES");
		}
        
		if (getDocumentType().length() == 0) 
		{
        	setDocumentType("INDVOTHERAPP");
        }
		
        if (getSourceId().length() == 0) 
        {
        	setSourceId("MQ_COI_IDV");
        }
		
        String testApp = appType.toUpperCase();
        WriteLog.WriteToLog("Application Type: " + testApp);       
        
        ArrayList<String> defaults = new ArrayList<String>(); 
        defaults = config.getDefaultApps();
        
        isDefault = false;
        
        for (int i = 0; i < defaults.size(); i++)
        {
        	WriteLog.WriteToLog("AppType: " + testApp + " - " + "Default " + i + ": " + defaults.get(i));
        	
        	if(testApp.equals(defaults.get(i)) || testApp.isEmpty())
        	{
        		isDefault = true;
        		break;
        	}
        }
        
        if (isDefault)
        {
        	WriteLog.WriteStatus("Default Application detected... setting as loose app...");
        	WriteLog.WriteStatus("Central Application Type: " + appType);
        	
			setDocumentClass("MW_MEDD_MEM");  // 911
            setDocumentType("LOOSE_APP");
		}
		
        String newMBU = getMbu();
        
        if (newMBU.toUpperCase().equals("MSMEDSYS"))
        {
        	WriteLog.WriteToLog("Set doc class to MED_SUPP document class for all MSMEDSYS documents.");
        	setDocumentClass("MED_SUPP");
        }
        	
        String hcid = msgHeader.getHcid();
		String eid = msgHeader.getEid();
		
		if (hcid.length() > 0) 
		{
			setMemberNumber(hcid);
		}
		else 
		{
			setMemberNumber(eid);
		}
		
		setRepository(config);
		
		LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public WeapRegion getWeapRegion() 
	{
		return WeapRegion.CENTRAL;
	}
}
