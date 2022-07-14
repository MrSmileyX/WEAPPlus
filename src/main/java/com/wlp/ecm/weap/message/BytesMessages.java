package com.wlp.ecm.weap.message;

import java.io.*;
import javax.jms.*;

import com.wlp.ecm.weap.common.WriteLog;

public class BytesMessages 
{
	public static BytesMessage convertFile(QueueConnectionFactory responseQcf, String fileName) 
	{
	    FileInputStream      inStream = null;
	    Connection           connection = null;
	    Session              session = null;
	    BytesMessage         bytesMessage = null;
	    int                  bytes_read = 0;
	    final int            BUFLEN = 64;
	    byte[]               buf1 = new byte[BUFLEN];

	    try 
	    {
	    	inStream = new FileInputStream(fileName);
	    }
	    catch (IOException e) 
	    {
	    	WriteLog.WriteToLog("Problem getting file: " + e.getMessage());
	    }
	  
	    try 
	    {
	    	connection = responseQcf.createConnection();
	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    }
	    catch (Exception e) 
	    {
	        WriteLog.WriteToLog("Connection problem: " + e.getMessage());
	        
	        if (connection != null) 
	        {
	            try 
	            {
	                connection.close();
	            } 
	            catch (JMSException ee) 
	            {
	            	WriteLog.WriteToLog("JMS exception occurred: " + ee.getMessage());
	            }
	        }
	    } 

        try 
        {
            bytesMessage = session.createBytesMessage();
            
            while((bytes_read = inStream.read(buf1)) != -1) 
            {
                bytesMessage.writeBytes(buf1, 0, bytes_read);
            }
            
            inStream.close();
        } 
        catch (JMSException jmse) 
        {
        	WriteLog.WriteToLog("JMS exception occurred: " + jmse.getMessage());
        } 
        catch (IOException ioe) 
        {
        	WriteLog.WriteToLog("I/O exception occurred: " + ioe.getMessage());
        }
        finally 
        {
            if (connection != null) 
            {
                try 
                {
                    connection.close();
                } 
                catch (JMSException e) 
                {
                	WriteLog.WriteToLog("JMS exception occurred: " + e.getMessage());
                }
            }
        }
        
        return bytesMessage;
        
    }   
    
}

