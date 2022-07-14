package com.wlp.ecm.weap.data;

import java.util.Date;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.message.WeapMessageHeader;
import com.wlp.ecm.weap.message.WeapRegion;

/**
 * 
 * @author AD07673
 *
 *
 * Metadata:
 * 		DocumentClass
 *		Dcn
 *		RouteCode
 *		MemberNumber
 *		DocVal
 *		SourceId
 */

public class WeapMetadataEast extends WeapMetadata {

	private Date appReceiveDate = null;
	
	protected WeapMetadataEast(WeapConfigSettings config, WeapMessageHeader msgHeader) {
		super(config, msgHeader, WeapRegion.EAST);
		LoggingUtil.LogTraceStartMsg();
		
		String newMBU = getMbu();
		
		if (newMBU.toUpperCase().equals("MSMEDSYS"))
        {
			WriteLog.WriteToLog("Set doc class to Case Manager enrollment class for all MSMEDSYS documents.");
        	setDocumentClass("MED_SUPP");
        }
		else
		{
			setDocumentClass("ACES_MEM");  // 203 
		}
		
		setDcn(msgHeader.getAcn());
		setSystemId("ACES");
		setRouteCode(msgHeader.getStateCd());
		setMemberNumber( msgHeader.getHcid());
		setDocumentType("IM_TK_APP");
		setSourceId("MQ_TK_IDV");
		appReceiveDate = msgHeader.getAppRecDate();

		setMedicareID(msgHeader.getMedicareID());
		setProductType(msgHeader.getProductType());
		setTranCode(msgHeader.getTranCode());
		setAltSysId(msgHeader.getMbu() + "_" + msgHeader.getStateCd());
		setFullName(msgHeader.getApplicantLastName() + " " + msgHeader.getApplicantFirstName());
		
		setRepository(config);
		
		LoggingUtil.LogTraceEndMsg();
	}
	
	@Override
	public WeapRegion getWeapRegion() {
		return WeapRegion.CENTRAL;
	}

	public Date getAppReceiveDate() {
		return appReceiveDate;
	}

}
