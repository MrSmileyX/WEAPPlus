package com.wlp.ecm.weap.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import com.wlp.ecm.weap.WeapMessageProcessor;
import com.wlp.ecm.weap.common.LoggingUtil;
import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.data.WeapRepository;
import com.wlp.ecm.weap.data.WeapRepositoryCE;
import com.wlp.ecm.weap.data.WeapRepositoryISRA;
import com.wlp.ecm.weap.exception.WeapException;

@Configuration
@PropertySource("weap-config.properties")

public class AppConfiguration 
{
	@Autowired
	Environment env;

	@Bean
	@Scope(value="prototype")
	public WeapMessageProcessor weapMessageProcessor() 
	{
		return new WeapMessageProcessor();
	}
	
	@Bean
	@Scope(value="singleton")
	public CESettings ceConnSettings()
	{
		CESettings ceConnSettings = new CESettings();
		
		ceConnSettings.set51Ver(env.getProperty("ce.51.version"));
		ceConnSettings.set51URI(env.getProperty("ce.51.uri"));
		ceConnSettings.set51User(env.getProperty("ce.51.user.id"));
		ceConnSettings.set51Pass(env.getProperty("ce.51.password"));
		ceConnSettings.set51OSName(env.getProperty("ce.51.obj.store"));
		ceConnSettings.set51OSType(env.getProperty("ce.51.os.type"));
		
		ceConnSettings.set52Ver(env.getProperty("ce.52.version"));
		ceConnSettings.set52URI(env.getProperty("ce.52.uri"));
		ceConnSettings.set52User(env.getProperty("ce.52.user.id"));
		ceConnSettings.set52Pass(env.getProperty("ce.52.password"));
		ceConnSettings.set52OSName(env.getProperty("ce.52.obj.store"));
		ceConnSettings.set52OSType(env.getProperty("ce.52.os.type"));
		
		return ceConnSettings;
	}
	
	@Bean
	@Scope(value="singleton")
	public DBSettings getDBSettings()
	{
		DBSettings databaseProps = new DBSettings();
		
		databaseProps.setDBClass(env.getProperty("dbase.Class"));
		databaseProps.setDBOCI(env.getProperty("dbase.OCI"));
		databaseProps.setDBUser(env.getProperty("dbase.User"));
		databaseProps.setDBPass(env.getProperty("dbase.Pass"));
		databaseProps.setDBThin(env.getProperty("dbase.Thin"));
		databaseProps.setDBURL(env.getProperty("dbase.Url"));
		databaseProps.setDBRegion(env.getProperty("dbase.Region"));
		databaseProps.setDBTable(env.getProperty("dbase.Table"));		

		return databaseProps;
	}
	
	
	@Bean
	@Scope(value="singleton")
	public WeapConfigSettings weapConfigSettings() 
	{
		
		String propTemp = "";
		
		int noErr = 0;
		int lvl = 0;
		
		LoggingUtil.LogTraceStartMsg();

		WeapConfigSettings weapConfigSettings = new WeapConfigSettings();
		
		// lets make sure it ends with a slash
		String directory = normalizePath(env.getProperty("template.directory"));
		weapConfigSettings.setTemplateDirectory(directory);
		weapConfigSettings.setContentEngineServerUri(env.getProperty("content.engine.server.uri"));
		weapConfigSettings.setObjectStoreNameEast(env.getProperty("object.store.name.east"));
		weapConfigSettings.setObjectStoreNameWest(env.getProperty("object.store.name.west"));
		weapConfigSettings.setObjectStoreNameCentral(env.getProperty("object.store.name.central"));
		weapConfigSettings.setIsraLibraryNameEast(env.getProperty("isra.library.name.east"));
		weapConfigSettings.setIsraLibraryNameWest(env.getProperty("isra.library.name.west"));
		weapConfigSettings.setIsraLibraryNameCentral(env.getProperty("isra.library.name.central"));
		weapConfigSettings.setIsraUserIdEast(env.getProperty("isra.user.id.east"));
		weapConfigSettings.setIsraUserPasswordEast(env.getProperty("isra.user.password.east"));
		weapConfigSettings.setIsraUserIdWest(env.getProperty("isra.user.id.west"));
		weapConfigSettings.setIsraUserPasswordWest(env.getProperty("isra.user.password.west"));
		weapConfigSettings.setIsraUserIdCentral(env.getProperty("isra.user.id.central"));
		weapConfigSettings.setIsraUserPasswordCentral(env.getProperty("isra.user.password.central"));
		
		weapConfigSettings.setDebugDataDirectory(normalizePath(env.getProperty("debug.data.directory")));
		weapConfigSettings.setMessageCharacterSet(env.getProperty("message.character.set"));
		
		weapConfigSettings.setBatchIdFilePath(env.getProperty("batchid.file.path"));
		weapConfigSettings.setTableDefPath(env.getProperty("table.file.path"));
		
		weapConfigSettings.setAppCount(env.getProperty("default.app.count")); 
		
		for (int app = 0; app < weapConfigSettings.getAppCount(); app++)
		{	
			propTemp = env.getProperty("default.app." + app).toUpperCase();
			weapConfigSettings.addDefaultApp(propTemp);
		}

		weapConfigSettings.setEnvironment(env);

		LoggingUtil.LogTraceEndMsg();
		return weapConfigSettings;
	}
	
	private String normalizePath(String dir) {
		String tempdir = dir;
		if (tempdir != null) {
			if (!tempdir.endsWith("\\") && !tempdir.endsWith("/")) {
				tempdir += "/";
			}
		}
		return tempdir;
	}
	
	@Bean(name="documentClassPropertyMapping")
	@Scope(value="prototype")
	public DocumentClassPropertyMapping getDocumentClassPropertyMapping(String repoType) throws WeapException 
	{
		String mappingXML = "";
		String xmlMapFile = "";
		
		LoggingUtil.LogTraceStartMsg();

		//String repoType = env.getProperty("respository-type");
		DocumentClassPropertyMapping classMapping = new DocumentClassPropertyMapping();
		
		mappingXML = "class.mapping." + repoType.toLowerCase() + ".xml";
		
		WriteLog.WriteToLog("Retrieving XML mapping: " + mappingXML);
		xmlMapFile = env.getProperty(mappingXML);
		WriteLog.WriteToLog("XML Mapping file: " + xmlMapFile);
		classMapping.setClassMappingXml(xmlMapFile);
		 
		LoggingUtil.LogTraceEndMsg();
		return classMapping;
	}

	/*
	 * We want to break out the content engine credentials into a separate class
	 * because the method of retrieving it will be different and will likely need to
	 * be scoped differently.  We may want to retrieve them from WAS J2C authentication
	 * instead of a properties file. 
	 */
	@Bean(name="contentEngineUserCredentials")
	@Scope(value="singleton")
	public UserCredentials getUserCredentials() {
		LoggingUtil.LogTraceStartMsg();

		UserCredentials creds = new UserCredentials();
		
		creds.setUser( env.getProperty("content.engine.user.id") );
		creds.setPswd( env.getProperty("content.engine.password") );

		LoggingUtil.LogTraceEndMsg();
		return creds;
	}
	
	@Bean(name="israCredentialsEast")
	@Scope(value="singleton")
	public UserCredentials getIsraCredentialsEast() {
		LoggingUtil.LogTraceStartMsg();

		UserCredentials creds = new UserCredentials();
		
		creds.setUser( env.getProperty("isra.user.id.east") );
		creds.setPswd( env.getProperty("isra.user.password.east") );

		LoggingUtil.LogTraceEndMsg();
		return creds;
	}

	@Bean(name="israCredentialsWest")
	@Scope(value="singleton")
	public UserCredentials getIsraCredentialsWest() {
		LoggingUtil.LogTraceStartMsg();

		UserCredentials creds = new UserCredentials();
		
		creds.setUser(env.getProperty("isra.user.id.west"));
		creds.setPswd(env.getProperty("isra.user.password.west"));

		LoggingUtil.LogTraceEndMsg();
		return creds;
	}

	@Bean(name="israCredentialsCentral")
	@Scope(value="singleton")
	public UserCredentials getIsraCredentialsCentral() 
	{
		LoggingUtil.LogTraceStartMsg();

		UserCredentials creds = new UserCredentials();
		
		creds.setUser(env.getProperty("isra.user.id.central"));
		creds.setPswd(env.getProperty("isra.user.password.central"));

		LoggingUtil.LogTraceEndMsg();
		return creds;
	}

	@Bean(name="WeapRepository")
	@Scope(value="prototype")
	public WeapRepository getWeapRepository(String repoType) 
	{
		WeapRepository weapRepo = null;
		
		WriteLog.WriteToLog("Repository type: " + repoType);
		
		if (repoType != null) 
		{
			if (repoType.equalsIgnoreCase("IS")) 
			{
				weapRepo = new WeapRepositoryISRA();
			} 
			else if (repoType.equalsIgnoreCase("CE")) 
			{
				weapRepo = new WeapRepositoryCE();
			}
		}
		
		return weapRepo;
	}
	
	@Bean(name="messageSource") // the name messageSource is required 
	public MessageSource getMessageSource() 
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages");
		messageSource.setDefaultEncoding("windows-1252");
		messageSource.setCacheSeconds(86400);
		
		return messageSource;
	}
}
