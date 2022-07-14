package com.wlp.ecm.weap.reporting;

import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.DBSettings;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.config.jaxb.BatchIdConfig;
import com.wlp.ecm.weap.config.jaxb.Tables;
import com.wlp.ecm.weap.common.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Reporting 
{
	public static void logMessage(MessageData md)
	{
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		
		WriteLog.WriteToLog("Retrieving database settings...");
		DBSettings dbConfig = ctx.getBean(DBSettings.class);
		WeapConfigSettings weapCfg = ctx.getBean(WeapConfigSettings.class);
		
		String tableDef = weapCfg.getTableDefPath();		
		WriteLog.WriteToLog("Table Path: " + tableDef);
		
		Database.logEntry(dbConfig, md, tableDef);
	}
}
