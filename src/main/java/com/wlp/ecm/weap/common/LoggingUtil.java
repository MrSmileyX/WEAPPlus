package com.wlp.ecm.weap.common;

import java.io.FileOutputStream;
import org.apache.log4j.Logger;

public class LoggingUtil {
	private static Logger logger = Logger.getLogger(LoggingUtil.class);

	// in the stacktrace
	// 		0 contains 
	//		1 contains getStackTrace
	//		2 contains the method in this class like LogTraceStartMsg
	//      3 contains the method that called this class.

	public static void LogTraceStartMsg() {
		// no need to synchronize here... why?
		//      logger is thread safe
		//		ste is final and each thread has its own copy.
		
		if (logger.isTraceEnabled()) 
		{
			final StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
			String msg = String.format("START: %s() of %s in %s(line %d)", ste.getMethodName(), ste.getClassName(), ste.getFileName(), ste.getLineNumber());
			logger.trace(msg);
		}
	}

	public static void LogTraceEndMsg() 
	{
		if (logger.isTraceEnabled()) 
		{
			final StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
			String msg = String.format("END:   %s() of %s in %s(line %d)", ste.getMethodName(), ste.getClassName(), ste.getFileName(), ste.getLineNumber());
			logger.trace(msg);
		}
	}
	
	public static void LogDebugMsg(String msg) 
	{
		// TODO: Change to a message code and get the actual msg from messages.properties
		if (logger.isDebugEnabled()) 
		{
			final StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
			String newMsg = String.format("'%s' in %s(line %d)", msg, ste.getFileName(), ste.getLineNumber());
			logger.debug(newMsg);
		}
	}

	public static void LogDebugMsg(Object data) {
		if (logger.isDebugEnabled()) {
			logger.debug( data.getClass().getSimpleName() + ": " + data.toString() );
		}
	}

	public static void LogDebugMsg(byte[] msgBytes, String filePath) {
		if (logger.isDebugEnabled()) {
			try {
				FileOutputStream fos = new FileOutputStream(filePath);
				fos.write(msgBytes);
				fos.close();
				logger.debug("Binary message data written to '" + filePath + "'");
			} catch (Exception e) {
				logger.error("Failure attempting to write msg data to " + filePath, e);
			}
		}
	}
	
	public static void LogErrorMsg(Throwable e) {
		final StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
		String method = ste.getClassName() + "." + ste.getMethodName() + "()";
		logger.error("Exception " + e.getClass().getName() + " caught in " + method, e);
		WriteLog.WriteToLog("Exception " + e.getClass().getName() + " caught in " + method);
		WriteLog.WriteToLog(e.getMessage());
	}

	public static void LogInfoMsg(String infoMsg)
	{
		// TODO: Change to a message code and get the actual msg from messages.properties
		logger.info(infoMsg);
	}

	public static void LogWarningMsg(String warnMsg) 
	{
		logger.warn(warnMsg);
	}
}
