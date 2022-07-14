package com.wlp.ecm.weap.file;

import com.wlp.ecm.weap.common.*;
import com.wlp.ecm.weap.reporting.*;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

public class FileUtilities
{
	
	public static void writeToFile(String fileName, InputStream iStream) throws IOException 
	{
		writeToFile(fileName, iStream, false);
	}

	public static void writeToFile(String fileName, InputStream iStream, boolean createDir) throws IOException 
	{
		String me = "FileUtils.WriteToFile";
	
		if (fileName == null) 
		{
			throw new IOException(me + ": filename is null");
		}
		
		if (iStream == null) 
		{
			throw new IOException(me + ": InputStream is null");
		}

		File theFile = new File(fileName);

		// Check if a file exists.
		if (theFile.exists()) 
		{
			String msg = theFile.isDirectory() ? "directory" : (!theFile.canWrite() ? "not writable" : null);
			
			if (msg != null) 
			{
				throw new IOException(me + ": file '" + fileName + "' is " + msg);
			}
		}

		// Create directory for the file, if requested.
		if (createDir && theFile.getParentFile() != null) 
		{
			theFile.getParentFile().mkdirs();
		}

		// Save InputStream to the file.
		BufferedOutputStream fOut = null;
		
		try 
		{
			fOut = new BufferedOutputStream(new FileOutputStream(theFile));

			byte[] buffer = new byte[512];
			int bytesRead = 0;
			
			while ((bytesRead = iStream.read(buffer)) != -1) 
			{
				fOut.write(buffer, 0, bytesRead);
			}
		} 
		catch (Exception e) 
		{
			throw new IOException(me + " failed, got: " + e.toString());
		} 
		finally 
		{
			close(iStream, fOut);
		}
	}

	public static long writeResultSet(ResultSet dataIn, String fileOut)
	{
		PrintWriter out = null;
		int dataCols = 0;
		int dataRows = 0;
		String lineVal = "";
		String fieldVal = "";
		String finalVal = "";
		
		try 
		{
			File fLog = new File(fileOut);

			try
			{
				fLog.createNewFile();
			}
			catch (IOException e)
			{
				System.out.println("I/O Error");
				System.exit(0);
			}

			out = openWriter(fileOut);

			try
			{
				ResultSetMetaData rsmd = dataIn.getMetaData();
				dataCols = rsmd.getColumnCount();
		
				lineVal = "";
				
				for (int f = 1; f <= dataCols; f++)
		    	{
					if (f == 1)
		       		{
		       			lineVal = (rsmd.getColumnName(f));
		       		}
		       		else
		       		{
		       			lineVal += ",";
		       			lineVal += (rsmd.getColumnName(f));
		       		}
		        }
				
				lineVal.replace("\r\n","");
		       	lineVal.replace("\r","");
		       	lineVal.replace("\n","");
				
				lineVal += "\r\n";
				writeLine(lineVal, out);
				
			    while (dataIn.next()) 
			    {
			    	lineVal = "";
			    	fieldVal = "";
			    	finalVal = "";
			    	
			       	for (int f = 1; f <= dataCols; f++)
			    	{
			       		//currProc.addProcessed();
			       		fieldVal = dataIn.getString(f);
			       		
			       		if (dataIn.wasNull()) 
			       		{
			       			finalVal = "";
			       		}
			       		else
			       		{
			       			finalVal = fieldVal.replaceAll(",", " ");
			       			finalVal = finalVal.replaceAll("\r\n","");
			       			finalVal = finalVal.replaceAll("\r","");
			       			finalVal = finalVal.replaceAll("\n","");
			       		}
			       		
			       		if (f == 1)
			       		{
			       			lineVal = finalVal;
			       		}
			       		else
			       		{
			       			lineVal += ",";
			       			lineVal += finalVal;
			       		}
			        }
			       	
			       	lineVal = lineVal.replaceAll("\r\n","");
			       	lineVal = lineVal.replaceAll("\r","");
			       	lineVal = lineVal.replaceAll("\n","");
       				
			       	lineVal = lineVal += "\r\n";
	       			
			       	writeLine(lineVal, out);
			       	
			       	dataRows++;
			    }
			}
		    catch (SQLException sqle) 
	    	{
	    		WriteLog.WriteToLog("SQL Error: " + sqle.getMessage());
	    	}
		} 
		finally 
		{ 
		    if (dataIn != null) 
		    {
		    	try 
		    	{ 
		    		dataIn.close(); 
		    	} 
		    	catch (SQLException sqle) 
		    	{
		    		WriteLog.WriteToLog("SQL Error: " + sqle.getMessage());
		    	}
		    }
		    
		    if (out != null)
		    {
		    	out.close(); 
		    }
		}
		
		return dataRows;
	}
	
	protected static void close(InputStream iStream, OutputStream oStream) throws IOException 
	{
		try 
		{
			if (iStream != null) 
			{
				iStream.close();
			}
		} 
		finally 
		{
			if (oStream != null) 
			{
				oStream.close();
			}
		}
	}

	public static String getFileName(String fileName) 
	{
		String fileNamePart = fileName;
		int loc = fileName.indexOf(".");
		
		if (loc >= 0) 
		{
			fileNamePart = fileName.substring(0, loc);
		}
		
		return fileNamePart;
	}
	
		
	public static void deleteFile(String fileName) 
	{
		WriteLog.WriteToLog("Deleting file " + fileName + ".");
		new File(fileName).delete();
		WriteLog.WriteToLog("Deleted file.");
	}
	
	public static String[] listFiles(String dirName, String fileType)
	{	
		String[] listOfFiles; 
		File folder = new File(dirName);
		
		if (fileType.equals(""))
		{
			listOfFiles = folder.list();
		}
		else
		{
			listOfFiles = folder.list(new FileExtensionFilter(fileType));
		}
		
		return listOfFiles;
	}
	
	public static void copyFile(String fileIn, String fileOut)
	{
		InputStream inStream = null;
		OutputStream outStream = null;
		
		WriteLog.WriteToLog("Copying file...");
		WriteLog.WriteToLog("SOURCE:      " + fileIn);
		WriteLog.WriteToLog("DESTINATION: " + fileOut);

		try
		{

			File afile = new File(fileIn);
			File bfile = new File(fileOut);

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[8192];

			int length;

			//copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0)
			{
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();
			
			WriteLog.WriteToLog("Successfully copied file " + fileIn + ".");
		}
		catch(IOException e)
		{
			WriteLog.WriteToLog("Error copying file " + fileIn + ".");
			WriteLog.WriteToLog(e.getMessage());
		}
		
	}

		
	public static boolean copyAFile(String fileIn, String fileOut)
	{
		boolean wasCopied = false;
		InputStream inStream = null;
		OutputStream outStream = null;

		try
		{

			File afile = new File(fileIn);
			File bfile = new File(fileOut);

			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;

			//copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0)
			{
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();
			
			wasCopied = true;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			wasCopied = false;
		}
		
		return wasCopied;
	}

	private static PrintWriter openWriter(String name)
	{
		try
		{
			File file = new File(name);

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw, true);

			return out;
		}
		catch (IOException e)
		{
			System.out.println("I/O Error");
			System.exit(0);
		}

		return null;
	}

	private static void writeLine(String FullMsg, PrintWriter fileOut)
	{
		fileOut.print(FullMsg);
	}
}