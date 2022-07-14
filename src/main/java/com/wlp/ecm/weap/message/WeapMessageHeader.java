package com.wlp.ecm.weap.message;

import java.util.Date;

public class WeapMessageHeader {
	private static final String EMPTY_STRING = "";
	
	private String TranCode = null;
	private String acn = null;
	private String brand = null;
	private String stateCd = null;
	private String mbu = null;
	private String eid = null;
	private String hcid = null;
	private String partnerId = null;
	private String appType = null;
	private String appStatus = null;
	private Date reqEffDate = null;
	private Date appRecDate = null;
	private String brokerTin = null;
	private String applicantLastName = null;
	private String applicantFirstName = null;
	private String applicantMi = null;
	private Date applicantDob = null;
	private String applicantSsn = null;
	private String spouseSsn = null;
	private String trackingNumber = null;
	private String generalAgency = null;
	private String dep1Ssn = null;
	private String MedicareID = null;
	private String ProductType = null;
	private String AltSysId = null;
	private String FullName = null;
	
	public String getTranCode() {
		if (TranCode == null) {
			return EMPTY_STRING;
		} else {
			return TranCode;
		}
	}
	public void setTranCode(String TranCode) {
		this.TranCode = TranCode;
	}
	
	public String getAcn() {
		if (acn == null) {
			return EMPTY_STRING;
		} else {
			return acn;
		}
	}
	public void setAcn(String acn) {
		this.acn = acn;
	}
	public String getBrand() {
		if (brand == null) {
			return EMPTY_STRING;
		} else {
			return brand;
		}
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getStateCd() {
		if (stateCd == null) {
			return EMPTY_STRING;
		} else {
			return stateCd;
		}
	}
	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}
	public String getMbu() {
		if (mbu == null) {
			return EMPTY_STRING;
		} else {
			return mbu;
		}
	}
	public void setMbu(String mbu) {
		this.mbu = mbu;
	}
	public String getEid() {
		if (eid == null) {
			return EMPTY_STRING;
		} else {
			return eid;
		}
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getHcid() {
		if (hcid == null) {
			return EMPTY_STRING;
		} else {
			return hcid;
		}
	}
	public void setHcid(String hcid) {
		this.hcid = hcid;
	}
	public String getPartnerId() {
		if (partnerId == null) {
			return EMPTY_STRING;
		} else {
			return partnerId;
		}
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getAppType() {
		if (appType == null) {
			return EMPTY_STRING;
		} else {
			return appType;
		}
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getAppStatus() {
		if (appStatus == null) {
			return EMPTY_STRING;
		} else {
			return appStatus;
		}
	}
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	public Date getReqEffDate() {
		return reqEffDate;
	}
	public void setReqEffDate(Date reqEffDate) {
		this.reqEffDate = reqEffDate;
	}
	public Date getAppRecDate() {
		return appRecDate;
	}
	public void setAppRecDate(Date appRecDate) {
		this.appRecDate = appRecDate;
	}
	public String getBrokerTin() {
		if (brokerTin == null) {
			return EMPTY_STRING;
		} else {
			return brokerTin;
		}
	}
	public void setBrokerTin(String brokerTin) {
		this.brokerTin = brokerTin;
	}
	public String getApplicantLastName() {
		if (applicantLastName == null) {
			return EMPTY_STRING;
		} else {
			return applicantLastName;
		}
	}
	public void setApplicantLastName(String applicantLastName) {
		this.applicantLastName = applicantLastName;
	}
	public String getApplicantFirstName() {
		if (applicantFirstName == null) {
			return EMPTY_STRING;
		} else {
			return applicantFirstName;
		}
	}
	public void setApplicantFirstName(String applicantFirstName) {
		this.applicantFirstName = applicantFirstName;
	}
	public String getApplicantMi() {
		if (applicantMi == null) {
			return EMPTY_STRING;
		} else {
			return applicantMi;
		}
	}
	public void setApplicantMi(String applicantMi) {
		this.applicantMi = applicantMi;
	}
	public Date getApplicantDob() {
		return applicantDob;
	}
	public void setApplicantDob(Date applicantDob) {
		this.applicantDob = applicantDob;
	}
	public String getApplicantSsn() {
		if (applicantSsn == null) {
			return EMPTY_STRING;
		} else {
			return applicantSsn;
		}
	}
	public void setApplicantSsn(String applicantSsn) {
		this.applicantSsn = applicantSsn;
	}
	public String getSpouseSsn() {
		if (spouseSsn == null) {
			return EMPTY_STRING;
		} else {
			return spouseSsn;
		}
	}
	public void setSpouseSsn(String spouseSsn) {
		this.spouseSsn = spouseSsn;
	}
	public String getTrackingNumber() {
		if (trackingNumber == null) {
			return EMPTY_STRING;
		} else {
			return trackingNumber;
		}
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getGeneralAgency() {
		if (generalAgency == null) {
			return EMPTY_STRING;
		} else {
			return generalAgency;
		}
	}
	public void setGeneralAgency(String generalAgency) {
		this.generalAgency = generalAgency;
	}
	public String getDep1Ssn() {
		if (dep1Ssn == null) {
			return EMPTY_STRING;
		} else {
			return dep1Ssn;
		}
	}
	
	public void setDep1Ssn(String dep1Ssn) {
		this.dep1Ssn = dep1Ssn;
	}
	
	public String getMedicareID() 
	{
		if (MedicareID == null) 
		{
			return EMPTY_STRING;
		}
		else 
		{
			return MedicareID;
		}
	}
	
	public void setMedicareID(String MedicareID) 
	{
		this.MedicareID = MedicareID;
	}

	public String getProductType() {
		if (ProductType == null) {
			return EMPTY_STRING;
		} else {
			return ProductType;
		}
	}
	
	public void setProductType(String ProductType) {
		this.ProductType = ProductType;
	}
	
	public String getAltSysId() 
	{
		if (AltSysId == null) 
		{
			return EMPTY_STRING;
		}
		else 
		{
			return AltSysId;
		}
	}
	
	public void setAltSysId(String AltSysId) 
	{
		this.AltSysId = AltSysId;
	}

	public String getFullName() {
		if (FullName == null) {
			return EMPTY_STRING;
		} else {
			return FullName;
		}
	}
	
	public void setFullName(String FullName) {
		this.FullName = FullName;
	}
	

	@Override
	public String toString() {
		return String.format("TranCode: %s, acn: %s, brand: %s, stateCd: %s, mbu: %s, eid: %s, hcid: %s, partnerId: %s, appType: %s, appStatus: %s, reqEffDate: %s, appRecDate: %s, brokerTin: %s, applicantLastName: %s, applicantFirstName: %s, applicantMi: %s, applicantDob: %s, applicantSsn: %s, spouseSsn: %s, trackingNumber: %s, generalAgency: %s, dep1Ssn: %s", TranCode, acn, brand, stateCd, mbu, eid, hcid, partnerId, appType, appStatus, reqEffDate, appRecDate, brokerTin, applicantLastName, applicantFirstName, applicantMi, applicantDob, applicantSsn, spouseSsn, trackingNumber, generalAgency, dep1Ssn);
	}
}
