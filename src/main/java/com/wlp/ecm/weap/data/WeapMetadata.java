package com.wlp.ecm.weap.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.config.jaxb.BatchIdConfig;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.message.WeapMessageHeader;
import com.wlp.ecm.weap.message.WeapRegion;

public class WeapMetadata {
	private String objectStoreName = null;
	private String libraryName = null;
	
	private Boolean conversionRequired = null;
	
	private String documentClass = null;
	private String documentType = null;
	private String acn = null;
	private String dcn = null;
	private String systemId = null;
	private String routeCode = null;
	private String memberNumber = null;
	
	private String providerNumber = null;
	private String altId = null;
	private String returnReason = null;
	private String spouseSsn = null;
	private String partnerId = null;
	private String trackingNumber = null;
	
	private Date hostDate = null;
	private Date origDate = null;
	
	private String sourceId = "WEAP";
	private String hcid = null;
	private String eid = null;
	private String mbu = null;
	private String brand = null;
	private String brokerTin = null;
	private String groupNumber = null;
	private String generalAgency = null;
	private String businessUnit = null;
	private String dateControlNumber = null;
	private String salesChannel = null;
	private String stateBusinessCode = null;
	private String caseType = null;
	private String applicantFirstName = null;
	private String applicantLastName = null;
	private String applicantMiddleInitial = null;
	private Date applicantDob = null;
	private String applicantSsn = null;
	private String dependantSsn = null;
	
	private String TranCode = null;
	private String AltSysId = null;
	private String FullName = null;
	private String FirstName = null;
	private String LastName = null;
	private String MedicareID = null;
	private String ProductType = null;
	
	private String batchId = null;
	
	private String stateCd = null;
	private String applicationType = null;
	private String applicationStatus = null;
	private String fDocClassName = null;
	private ArrayList<String> documentID = new ArrayList<String>();
	private String repository = null;
	private String repositoryName = null;
	
	private String covPgDocID = null;
	private String appPgDocID = null;
	
	public static synchronized WeapMetadata createMetadata(WeapMessageHeader msgHeader) throws WeapException {
		LoggingUtil.LogTraceStartMsg();
		WeapMetadata weapMetadata = null;
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		WeapConfigSettings config = ctx.getBean(WeapConfigSettings.class);
		
		WeapRegion region = deriveRegion(config, msgHeader);
		
		switch (region) 
		{
			case EAST:
				weapMetadata = new WeapMetadataEast(config, msgHeader); 
				break;
			case CENTRAL:
				weapMetadata = new WeapMetadataCentral(config, msgHeader); 
				break;
			case WEST:
				weapMetadata = new WeapMetadataWest(config, msgHeader); 
				break;
		}
		
		LoggingUtil.LogTraceEndMsg();
		return weapMetadata;
	}
	
	protected WeapMetadata(WeapConfigSettings config, WeapMessageHeader msgHeader, WeapRegion region) {
		LoggingUtil.LogTraceStartMsg();

		objectStoreName = deriveObjectStoreName(config, region);
		libraryName = deriveLibraryName(config, region);
		conversionRequired = deriveImageConversion(msgHeader, region);
		// get all the common message data

		acn = msgHeader.getAcn();
		brand = msgHeader.getBrand();
		stateCd = msgHeader.getStateCd();
		mbu = msgHeader.getMbu();
		eid = msgHeader.getEid();
		hcid = msgHeader.getHcid();
		partnerId = msgHeader.getPartnerId();
		applicationType = msgHeader.getAppType();
		applicationStatus = msgHeader.getAppStatus();
		hostDate = msgHeader.getReqEffDate();
		origDate = msgHeader.getAppRecDate();
		TranCode = msgHeader.getTranCode();
		
		brokerTin = msgHeader.getBrokerTin();
		applicantFirstName = msgHeader.getApplicantFirstName();
		applicantLastName = msgHeader.getApplicantLastName();
		applicantMiddleInitial = msgHeader.getApplicantMi();
		applicantDob = msgHeader.getApplicantDob();
		applicantSsn = msgHeader.getApplicantSsn();
		spouseSsn = msgHeader.getSpouseSsn();
		trackingNumber = msgHeader.getTrackingNumber();
		generalAgency = msgHeader.getGeneralAgency();
		dependantSsn = msgHeader.getDep1Ssn();
		
		MedicareID = msgHeader.getMedicareID();
		ProductType = msgHeader.getProductType();
		
		AltSysId = msgHeader.getMbu() + "_" + msgHeader.getStateCd();
		
		FirstName = msgHeader.getApplicantFirstName();
		LastName = msgHeader.getApplicantLastName();
		FullName = LastName + " " + FirstName;
				
		LoggingUtil.LogTraceEndMsg();
	}

	protected static synchronized String generateBatchId(String batchIdFilePath) throws WeapException {
		String batchId = null;

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(BatchIdConfig.class);
	
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			BatchIdConfig batchIdConfig = null;
			FileInputStream fis = null;
			
			try 
			{
				fis = new FileInputStream(batchIdFilePath);
				batchIdConfig = (BatchIdConfig)jaxbUnmarshaller.unmarshal(fis);
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
			
			int latestNumber = batchIdConfig.getBatchLatestNumber();
			String latestDay = batchIdConfig.getBatchLatestDay();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String today = sdf.format(new Date());
			
			if (today.equals(latestDay)) 
			{
				latestNumber++;
				batchIdConfig.setBatchLatestNumber(latestNumber);
			} 
			else 
			{
				batchIdConfig.setBatchLatestNumber(batchIdConfig.getBatchIdSeed());
				batchIdConfig.setBatchLatestDay(today);
			}
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	
			FileOutputStream fos = null; 
			try 
			{
				fos = new FileOutputStream(batchIdFilePath); 
				jaxbMarshaller.marshal(batchIdConfig, fos);
			} 
			finally 
			{
				if (fos != null) 
				{
					try 
					{
						fos.close();
					} 
					catch (IOException e) 
					{
					
					}
				}
			}
			
			batchId = String.format("%05d", latestNumber);
			
		} 
		catch (JAXBException e) 
		{
			throw new WeapException("E1027", e);
		} 
		catch (FileNotFoundException e) 
		{
			throw new WeapException("E1027", e);
		}
		
		return batchId;
	}
	
	public WeapRegion getWeapRegion() {
		return null;
	}
	
	public String getObjectStoreName() {
		return objectStoreName;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public Boolean getConversionRequired() {
		return conversionRequired;
	}
	
	public String getDocumentClass() {
		return documentClass;
	}

	public String getDocumentType() {
		return documentType;
	}

	public String getDcn() {
		return dcn;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getAltSysId() {
		return AltSysId;
	}
	
	public String getFullName() {
		return FullName;
	}
	
	public String getFirstName() {
		return FirstName;
	}
	
	public String getLastName() {
		return LastName;
	}
	
	public String getRouteCode() {
		return routeCode;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public String getProviderNumber() {
		return providerNumber;
	}

	public String getAltId() {
		return altId;
	}

	public String getTranCode() {
		return TranCode;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public String getSpouseSsn() {
		return spouseSsn;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public Date getHostDate() {
		return hostDate;
	}

	public Date getOrigDate() {
		return origDate;
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getHcid() {
		return hcid;
	}

	public String getMedicareID()
	{
		return MedicareID;
	}

	public String getProductType()
	{
		return ProductType;
	}
	

	public String getEid() {
		return eid;
	}

	public String getMbu() {
		return mbu;
	}

	public String getBrand() {
		return brand;
	}

	public String getBrokerTin() {
		return brokerTin;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public String getGeneralAgency() {
		return generalAgency;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}
	
	public String getDateControlNumber() {
		return dateControlNumber;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public String getStateBusinessCode() {
		return stateBusinessCode;
	}

	public String getCaseType() {
		return caseType;
	}

	public String getApplicantMiddleInitial() {
		return applicantMiddleInitial;
	}

	public Date getApplicantDob() {
		return applicantDob;
	}

	public String getApplicantSsn() {
		return applicantSsn;
	}

	public String getDependantSsn() {
		return dependantSsn;
	}

	public String getApplicantFirstName() {
		return applicantFirstName;
	}

	public String getApplicantLastName() {
		return applicantLastName;
	}

	public String getStateCd() {
		return stateCd;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public String getAcn() {
		return acn;
	}

	public String getRepository() {
		return repository;
	}
	
	public String getRepositoryName() {
		return this.repositoryName;
	}
	
	public String getDocIDs() {
		return documentID.toString();
	}

	
	public String getBatchId() {
		if (batchId == null) {
			return "";
		} else {
			return batchId;
		}
	}
	
	/***** protected *****/
	
	protected void setDocumentClass(String documentClass) {
		this.documentClass = documentClass;
	}

	protected void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	protected void setDcn(String dcn) {
		this.dcn = dcn;
	}

	protected void setTranCode(String TranCode) 
	{
		this.TranCode = TranCode;
	}

	protected void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	protected void setRouteCode(String routeCode) {
		if (routeCode.equalsIgnoreCase("INDIVIDUAL")) {
			this.routeCode = "IDV";
		} else {
			this.routeCode = routeCode;
		}
	}

	protected void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber.replace("-", "");
		this.memberNumber = this.memberNumber.replace(" ", "");
	}

	protected void setProviderNumber(String providerNumber) {
		this.providerNumber = providerNumber;
	}

	protected void setAltId(String altId) {
		this.altId = altId;
	}
	
	protected void setAltSysId(String AltSysId) 
	{
		this.AltSysId = AltSysId;
	}
	
	protected void setFullName(String FullName) 
	{
		this.FullName = FullName;
	}

	protected void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	protected void setSpouseSsn(String spouseSsn) {
		this.spouseSsn = spouseSsn;
	}

	protected void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	protected void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	protected void setHostDate(Date hostDate) {
		this.hostDate = hostDate;
	}

	protected void setOrigDate(Date origDate) {
		this.origDate = origDate;
	}

	protected void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	protected void setHcid(String hcid) {
		this.hcid = hcid;
	}

	protected void setEid(String eid) {
		this.eid = eid;
	}

	protected void setMbu(String mbu) {
		this.mbu = mbu;
	}

	protected void setBrand(String brand) {
		this.brand = brand;
	}

	protected void setBrokerTin(String brokerTin) {
		this.brokerTin = brokerTin;
	}

	protected void setRepository(String repo) {
		this.repository = repo;
	}
	
	protected void setRepository(WeapConfigSettings config) {
		WriteLog.WriteToLog("Retrieving repository for " + this.documentClass);
		
		this.repository = lookupRepository(config, this.documentClass);
		WriteLog.WriteToLog("Repository: " + this.repository);
		
		this.repositoryName = lookupRepositoryName(config, this.repository);
		WriteLog.WriteToLog("Repository Name: " + this.repositoryName);
	}
	
	protected void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	protected void setGeneralAgency(String generalAgency) {
		this.generalAgency = generalAgency;
	}

	protected void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	protected void setDateControlNumber(String dateControlNumber) {
		this.dateControlNumber = dateControlNumber;
	}

	protected void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	protected void setStateBusinessCode(String stateBusinessCode) {
		this.stateBusinessCode = stateBusinessCode;
	}

	protected void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	protected void setApplicantMiddleInitial(String applicantMiddleInitial) {
		this.applicantMiddleInitial = applicantMiddleInitial;
	}

	protected void setApplicantDob(Date applicantDob) {
		this.applicantDob = applicantDob;
	}

	protected void setApplicantSsn(String applicantSsn) {
		this.applicantSsn = applicantSsn;
	}

	protected void setDependantSsn(String dependantSsn) {
		this.dependantSsn = dependantSsn;
	}

	protected void setApplicantFirstName(String applicantFirstName) {
		this.applicantFirstName = applicantFirstName;
	}

	protected void setApplicantLastName(String applicantLastName) {
		this.applicantLastName = applicantLastName;
	}

	protected void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	protected void addDocumentID(String docID) 
	{
		this.documentID.add(docID);
	}

	protected void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	protected void setAcn(String acn) {
		this.acn = acn;
	}

	public void setMedicareID(String MedicareID)
	{
		this.MedicareID = MedicareID;
	}
	
	public void setProductType(String ProductType)
	{
		this.ProductType = ProductType;
	}
	
	protected void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public String getfDocClassName() {
		return fDocClassName;
	}

	public void setfDocClassName(String fDocClassName) {
		this.fDocClassName = fDocClassName;
	}

	protected String deriveObjectStoreName(WeapConfigSettings config, WeapRegion region) {
		LoggingUtil.LogTraceStartMsg();

		String objectStore = null;
		
		if (region == WeapRegion.EAST) {
			objectStore = config.getObjectStoreNameEast();
		} else if (region == WeapRegion.WEST) {
			objectStore =  config.getObjectStoreNameWest();
		} else if (region == WeapRegion.CENTRAL) {
			objectStore =  config.getObjectStoreNameCentral();
		} else {
			objectStore =  config.getObjectStoreNameWest();
		}
		
		LoggingUtil.LogTraceEndMsg();
		return objectStore;
	}

	protected String deriveLibraryName(WeapConfigSettings config, WeapRegion region) {
		LoggingUtil.LogTraceStartMsg();

		String israLibraryName = null;
		
		if (region == WeapRegion.EAST) {
			israLibraryName = config.getIsraLibraryNameEast();
		} else if (region == WeapRegion.WEST) {
			israLibraryName = config.getIsraLibraryNameWest();
		} else if (region == WeapRegion.CENTRAL) {
			israLibraryName = config.getIsraLibraryNameCentral();
		} else {
			israLibraryName = config.getIsraLibraryNameWest();
		}
		
		LoggingUtil.LogTraceEndMsg();
		return israLibraryName ;
	}

	protected static WeapRegion deriveRegion(WeapConfigSettings config, WeapMessageHeader msgHeader) {
		LoggingUtil.LogTraceStartMsg();

		WeapRegion region = WeapRegion.UNKNOWN;
		String brand = msgHeader.getBrand();
		String appType = msgHeader.getAppType();
		String stateCd = msgHeader.getStateCd();
			
		region = lookupRegion(config, brand + "." + appType + "." + msgHeader.getPartnerId());
		
		if (region == WeapRegion.UNKNOWN) 
		{
			region = lookupRegion(config, brand + "." + appType + "." + stateCd);
		}
		
		if (region == WeapRegion.UNKNOWN) 
		{
			region = lookupRegion(config, brand + "." + appType);
		}
		
		if (region == WeapRegion.UNKNOWN) 
		{
			region = lookupRegion(config, brand + "." + stateCd);
		}
		
		if (region == WeapRegion.UNKNOWN) 
		{
			region = lookupRegion(config, brand);
		}
		
		// override region for new england states CT, ME, & NH 
		if (stateCd.equalsIgnoreCase("CT") || stateCd.equalsIgnoreCase("ME") || stateCd.equalsIgnoreCase("NH") ) 
		{
			WriteLog.WriteToLog("Override 1: Defaulting to EAST region for CT,ME,NH applications...");
			region = WeapRegion.EAST;
		}
		
		// override region for MEDISYS 
		if (msgHeader.getMbu().equalsIgnoreCase("MSMEDSYS")) 
		{
			WriteLog.WriteToLog("Override 2: Defaulting to WEST region for MEDISYS applications...");
			region = WeapRegion.WEST;
		}
		
		if (region == WeapRegion.UNKNOWN) 
		{
			region = WeapRegion.WEST;
		}
			
		LoggingUtil.LogTraceEndMsg();
		return region;
	}
	
	protected Boolean deriveImageConversion(WeapMessageHeader msgHeader, WeapRegion region) {
		LoggingUtil.LogTraceStartMsg();

		Boolean convert = null;
		String partnerId = msgHeader.getPartnerId();
		String state = msgHeader.getStateCd(); 
		if (region == WeapRegion.CENTRAL || region == WeapRegion.EAST) {
			if (partnerId.equalsIgnoreCase("EHEALTH")) {
				convert = false;
			} else {
				convert = true;
			}
		} else if (region == WeapRegion.WEST) {
			if (state.equalsIgnoreCase("VA") && partnerId.equalsIgnoreCase("SECA")) {
				convert = true;
			} else {
				convert = false;
			}
			
		}
		
		LoggingUtil.LogTraceEndMsg();
		return convert;
	}
	
	protected static String lookupRepository(WeapConfigSettings config, String key)
	{
		LoggingUtil.LogTraceStartMsg();
		
		String repo = "";
		
		if (config.containsProperty("repository", key.toUpperCase()))
		{
			repo = config.getPropertyString("repository", key.toUpperCase());
		}
		else
		{
			WriteLog.WriteToLog("Key not found for repository." + key.toUpperCase() + " - Defaulting to IS.");
			repo = "IS";
		}
		
		return repo;
	}

	protected static String lookupRepositoryName(WeapConfigSettings config, String key)
	{
		LoggingUtil.LogTraceStartMsg();
		
		String repoNm = "";
		
		if (config.containsProperty("repositoryName", key.toUpperCase()))
		{
			repoNm = config.getPropertyString("repositoryName", key.toUpperCase());
		}
		else
		{
			WriteLog.WriteToLog("Key not found for repositoryName." + key.toUpperCase() + " - Defaulting to FileNet Image Services.");
			repoNm = "FileNet Image Services";
		}
		
		return repoNm;
	}
	
	protected static WeapRegion lookupRegion(WeapConfigSettings config, String key){
		LoggingUtil.LogTraceStartMsg();

		String regionStr = "";
		WeapRegion region = WeapRegion.UNKNOWN;
		if (config.containsProperty("BRAND", key)) {
			regionStr = config.getPropertyString("BRAND", key);
		}
		if (regionStr.equalsIgnoreCase("WEST")) {
			region = WeapRegion.WEST;
		} else if (regionStr.equalsIgnoreCase("EAST")) {
			region = WeapRegion.EAST;
		} else if (regionStr.equalsIgnoreCase("CENTRAL")) {
			region = WeapRegion.CENTRAL;
		}
		
		LoggingUtil.LogTraceEndMsg();
		return region;
	}
	
	protected String applyStringPrefix(String val, String prefix, int maxLength) {
		LoggingUtil.LogTraceStartMsg();

		String res = val; 
		if (val.length() < maxLength) {
			res = StringUtils.repeat(prefix, maxLength - val.length()) + val;
		}
		
		LoggingUtil.LogTraceEndMsg();
		return res;
	}
	
	protected String findConfigProperty(WeapConfigSettings config, String category, String possibleEntries[]) {
		LoggingUtil.LogTraceStartMsg();
		String configProp = null;

		for (String entry : possibleEntries) {
			if (config.containsProperty(category, entry)) {
				configProp = config.getPropertyString(category, entry);
				break;
			}
		}
		
		LoggingUtil.LogTraceEndMsg();
		return configProp;
	}

	@Override
	public String toString() {
		String str = String.format("objectStoreName: %s, libraryName: %s, conversionRequired: %s, documentClass: %s, documentType: %s, acn: %s, dcn: %s, systemId: %s, routeCode: %s, memberNumber: %s, providerNumber: %s, altId: %s, returnReason: %s, spouseSsn: %s, partnerId: %s, trackingNumber: %s, hostDate: %s, origDate: %s, sourceId: %s, hcid: %s, eid: %s, mbu: %s, brand: %s, brokerTin: %s, groupNumber: %s, generalAgency: %s, businessUnit: %s, dateControlNumber: %s, salesChannel: %s, stateBusinessCode: %s, caseType: %s, applicantFirstName: %s, applicantLastName: %s, applicantMiddleInitial: %s, applicantDob: %s, applicantSsn: %s, dependantSsn: %s, stateCd: %s, applicationType: %s, applicationStatus: %s", objectStoreName, libraryName, conversionRequired, documentClass, documentType, acn, dcn, systemId, routeCode, memberNumber, providerNumber, altId, returnReason, spouseSsn, partnerId, trackingNumber, hostDate, origDate, sourceId, hcid, eid, mbu, brand, brokerTin, groupNumber, generalAgency, businessUnit, dateControlNumber, salesChannel, stateBusinessCode, caseType, applicantFirstName, applicantLastName, applicantMiddleInitial, applicantDob, applicantSsn, dependantSsn, stateCd, applicationType, applicationStatus);
		return str;
	}
}
