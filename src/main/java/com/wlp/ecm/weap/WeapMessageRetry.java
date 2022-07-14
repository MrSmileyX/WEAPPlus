package com.wlp.ecm.weap;

import com.wlp.ecm.weap.common.WriteLog;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.WeapConfigSettings;
import com.wlp.ecm.weap.exception.WeapException;
import com.wlp.ecm.weap.file.*;
import com.wlp.ecm.weap.message.BytesMessages;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.BytesMessage;
import javax.jms.QueueConnectionFactory;
import javax.xml.bind.JAXBException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class WeapMessageRetry 
{
	public static void checkForRetries(QueueConnectionFactory qcf, Queue retryQ)
	{
		WriteLog.WriteToLog("Checking for files to reprocess...");
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfiguration.class);
		ctx.refresh();
		
		WeapMessageProcessor messageProcessor = ctx.getBean(WeapMessageProcessor.class);
		WeapConfigSettings settings = ctx.getBean(WeapConfigSettings.class);
		
		String retryPath = "/opt/WLP/WEAP/retry/";
		String debugPath = "/opt/WLP/WEAP/debug/";
		
		String fileExt = "msgdat";
		
		String[] retryFiles = FileUtilities.listFiles(retryPath, fileExt);
		
		String retryName = "";
		String debugName = "";
		
		int filesFound = retryFiles.length;
		
		if (filesFound > 0)
		{
			if (filesFound == 1)
			{
				WriteLog.WriteToLog(filesFound + " file found to reprocess...");
			}
			else
			{
				WriteLog.WriteToLog(filesFound + " files found to reprocess...");
			}
			
			for (int f = 0; f < retryFiles.length; f++)
			{
				String currFile = retryFiles[f];
				
				if (!currFile.isEmpty())
				{
					retryName = retryPath + currFile;
					debugName = debugPath + currFile;
										
					BytesMessage retryMsg = BytesMessages.convertFile(qcf, retryName); 
					
					try 
					{
						messageProcessor.processMessage(retryMsg, retryQ, qcf, false);
					}
					catch (WeapException e) 
					{
						WriteLog.WriteToLog("General Error: " + e.getMessage());
					}
					catch (JMSException e) 
					{
						WriteLog.WriteToLog("JMS Error: " + e.getMessage());
					}
					catch (JAXBException e) 
					{
						WriteLog.WriteToLog("JAXB Error: " + e.getMessage());
					}
				}
			}
			
			FileUtilities.copyFile(retryName, debugName);
			FileUtilities.deleteFile(debugName);
		}
		else
		{
			WriteLog.WriteToLog("No files found to reprocess...");
		}
	}
}
