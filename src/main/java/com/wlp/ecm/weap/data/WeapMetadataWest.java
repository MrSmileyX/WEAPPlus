package com.wlp.ecm.weap.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.message.WeapMessageHeader;
import com.wlp.ecm.weap.message.WeapRegion;

public class WeapMetadataWest extends WeapMetadata {

	protected WeapMetadataWest(WeapConfigSettings config, WeapMessageHeader msgHeader) throws WeapException {
		super(config, msgHeader, WeapRegion.WEST);
		LoggingUtil.LogTraceStartMsg();
		
		setHcid(msgHeader.getHcid());
		String eid = msgHeader.getEid(); 
		setEid(eid);
		String mbu = msgHeader.getMbu(); 
		setMbu(mbu);
		String brand = msgHeader.getBrand(); 
		setBrand(brand);
		setBrokerTin(msgHeader.getBrokerTin());
		setGroupNumber(msgHeader.getTrackingNumber());
		String generalAgency = msgHeader.getGeneralAgency();		
		setGeneralAgency(generalAgency);
		
		setMedicareID(msgHeader.getMedicareID());
		setProductType(msgHeader.getProductType());
		setTranCode(msgHeader.getTranCode());
		setAltSysId(msgHeader.getMbu() + "_" + msgHeader.getStateCd());
		setFullName(msgHeader.getApplicantLastName() + " " + msgHeader.getApplicantFirstName());
		
		String mbuType = null;
		String batchPrefix = "";
		
		if (mbu.equalsIgnoreCase("INDIVIDUAL")) 
		{
			mbuType = "I";
			batchPrefix = "ip";
			setDocumentClass("IIW_INDIVIDUAL"); // 05
			setfDocClassName("IIW_INDIVIDUAL");
		} 
		else if (mbu.equalsIgnoreCase("SMALLGROUP")) 
		{
			mbuType = "S";
			batchPrefix = "sp";
			setDocumentClass("IIW_SMALL_GROUP"); // 06
		}
		else if (mbu.equalsIgnoreCase("SENIOR")) 
		{
			mbuType = "X";
			batchPrefix = "xp";
			setDocumentClass("SR_SECURE"); // 15
		}
		else if (mbu.equalsIgnoreCase("MSMEDSYS")) 
		{
			WriteLog.WriteToLog("Set doc class to MED_SUPP document class for all MSMEDSYS documents.");
			mbuType = "X";
			batchPrefix = "MS";
			setDocumentClass("MED_SUPP");
		}
		else 
		{
			mbuType = "U";
			setDocumentClass("MED_SUPP");
		}
		
		if (batchPrefix.length() > 0) 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String batchId = batchPrefix + sdf.format(new Date()) + "I" + generateBatchId(config.getBatchIdFilePath());
			setBatchId(batchId);
		}
		
				
		if (mbuType.equals("X")) 
		{
			setMemberNumber( msgHeader.getApplicantSsn() );
		}
		else 
		{
			if (eid.length() == 0) 
			{
				setMemberNumber( msgHeader.getApplicantSsn() );
			} 
			else 
			{
				setMemberNumber(eid);
			}
		}
		
		String partnerId = msgHeader.getPartnerId();
        setPartnerId(partnerId);
        
        String appType = msgHeader.getAppType();
        
        String partnerIdAppType = partnerId + "." + appType;
        String mbuAppType = mbu + "." + appType;

        String docVal = "";

        if (partnerId.equalsIgnoreCase("SECA") && appType.equalsIgnoreCase("SHORTTERM")) {
        	docVal = "NAPP";
        } else {
            docVal = findConfigProperty(config, "APP.DOCTYPE", new String[] {partnerIdAppType, mbuAppType, "DEFAULT"});
        }
        setDocumentType(docVal);
        
        String caseType = findConfigProperty(config, "CASETYPE", new String[] {partnerIdAppType, mbuAppType, "DEFAULT"});
        if (generalAgency.length() > 0) {
        	String genAgencyCaseType = config.getPropertyString("CASETYPE", "GENERAL.AGENCY");
        	if ((genAgencyCaseType != null) && ( genAgencyCaseType.length() > 0 )) {
        		caseType = genAgencyCaseType;
        	}
        }
        setCaseType(caseType);

        setBrokerTin( msgHeader.getBrokerTin());
        
        //SimpleDateFormat sdfYearMnthDay = new SimpleDateFormat("yyMMdd");
        //String dcn = mbuType + "P" + sdfYearMnthDay.format(new Date()) + msgHeader.getAcn();
        
        setDcn(msgHeader.getAcn());
        
        String businessUnit = findConfigProperty(config, "BUSUNIT", new String[] {brand + "." + partnerId , brand});;
        setBusinessUnit(businessUnit);
        
        SimpleDateFormat sdfJulianDate = new SimpleDateFormat("yyDDD");
        String dateSeed = config.getPropertyString("DOCDATA", "DATESEED");
        setDateControlNumber( sdfJulianDate.format(new Date()) + dateSeed );
        setSalesChannel( config.getPropertyString("DOCDATA", "SALESCHNL") );
        setStateBusinessCode( msgHeader.getStateCd() +  businessUnit );
        setApplicantMiddleInitial( msgHeader.getApplicantMi() );
        setApplicantDob(msgHeader.getApplicantDob());
        String appSsn = msgHeader.getApplicantSsn();
        
        setRepository(config);
		        
        if (NumberUtils.isNumber( appSsn )) 
        {
        	setApplicantSsn( appSsn );
        }
        else 
        {
        	setApplicantSsn("");
        }
        
        setDependantSsn(msgHeader.getDep1Ssn());

        LoggingUtil.LogTraceEndMsg();
	}

	@Override
	public WeapRegion getWeapRegion() {
		return WeapRegion.WEST;
	}
}
