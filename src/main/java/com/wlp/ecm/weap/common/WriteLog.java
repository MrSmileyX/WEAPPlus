package com.wlp.ecm.weap.common;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.wlp.ecm.weap.data.WeapMetadata; 

public class WriteLog
{
	public static void WriteToLog(String Message)
	{
		String sTS;
		String sLogNm;
		String sFilePath;
		String sLine;
		String fileDt;
		String showOutput = "N";
		String server = "";
		boolean bNew = false;

		server = getServerName();
		showOutput = "Y";
		
		DateFormat df = new SimpleDateFormat("MMddyy");
		Date dtNow = new Date();
		fileDt = df.format(dtNow);

		sLogNm = "WeapProcLog_" + fileDt + "_" + server + ".log";
		
		sFilePath = "/opt/WLP/WEAP/logs/" + sLogNm;

		File fLog = new File(sFilePath);

		try
		{
			bNew = fLog.createNewFile();
		}
		catch (IOException e)
		{
			System.out.println("I/O Error");
			System.exit(0);
		}

		PrintWriter out = openWriter(sFilePath);

		sTS = makeTimeStmp();

		if (bNew)
		{
			writeLine(sTS + "  Log started.", out);
			bNew = false;
		}

		sLine = sTS + "  " + Message;
		
		writeLine(sLine, out);

		out.close();

		if (showOutput.equals("Y") || showOutput.equals("y"))
		{
			System.out.println(sLine);
		}
	}

	public static void WriteStatus(String Message)
	{
		String sTS;
		String sLogNm;
		String sFilePath;
		String sLine;
		String showOutput = "N";
		String server = "";
		
		boolean bNew = false;

		sLogNm = getLogName();

		server = getServerName();
		showOutput = "Y";
		sFilePath = "/opt/WLP/WEAP/logs/" + sLogNm;

		File fLog = new File(sFilePath);

		try
		{
			bNew = fLog.createNewFile();
		}
		catch (IOException e)
		{
			System.out.println("I/O Error");
			System.exit(0);
		}

		PrintWriter out = openWriter(sFilePath);

		if (bNew)
		{
			writeLine("Log started.", out);
			bNew = false;
		}

		sLine = Message;
		
		writeLine(sLine, out);

		out.close();

		if (showOutput.equals("Y") || showOutput.equals("y"))
		{
			System.out.println(sLine);
		}
	}

	private static String makeTimeStmp()
	{
		String sTimeStamp = "";

		DateFormat df = new SimpleDateFormat("[MM/dd/yyyy HH:mm:ss]");
		Date dtNow = new Date();
		sTimeStamp = df.format(dtNow);

		
		return sTimeStamp;
	}
	
	public static String getServerName()
	{
		String srvrName = "";
		String srvrNbr = "";
		
		InetAddress ip;
		
		try
		{
			ip = InetAddress.getLocalHost();
			srvrName = ip.getHostName();
		}
		catch (UnknownHostException e) 
		{	 
            e.printStackTrace();
        }

		if (srvrName.length() > 3)
		{
			srvrNbr = srvrName.substring(srvrName.length() - 3);
		}
		else
		{
			srvrNbr = srvrName;
		}
		
		return srvrNbr;

	}

	private static String getLogName()
	{
		String sFileDt = "";
		String sFileNm = "";
		String srvrName = "";
		String srvrNbr = "";
		
		InetAddress ip;
		
		try
		{
			ip = InetAddress.getLocalHost();
			srvrName = ip.getHostName();
		}
		catch (UnknownHostException e) 
		{	 
            e.printStackTrace();
        }

		if (srvrName.length() > 3)
		{
			srvrNbr = srvrName.substring(srvrName.length() - 3);
		}
		else
		{
			srvrNbr = srvrName;
		}
		
		DateFormat df = new SimpleDateFormat("MMddyy");
		Date dtNow = new Date();
		sFileDt = df.format(dtNow);

		sFileNm = "WeapStatus_" + srvrNbr + "_" + sFileDt + ".log";

		return sFileNm;
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

	public static void writeReport(WeapMetadata md)
	{
		String sTS;
		String sLogNm;
		String sFilePath;
		String sLine;
		String fileDt;
		String server = "";
		String showOutput = "N";
		boolean bNew = false;

		showOutput = "Y";
		server = getServerName();
		
		DateFormat df = new SimpleDateFormat("MMddyy");
		Date dtNow = new Date();
		fileDt = df.format(dtNow);

		sLogNm = "WeapReport_" + fileDt + "_" + server + ".csv";
		
		sFilePath = "/opt/WLP/WEAP/logs/" + sLogNm;

		File fLog = new File(sFilePath);

		try
		{
			bNew = fLog.createNewFile();
		}
		catch (IOException e)
		{
			System.out.println("I/O Error");
			System.exit(0);
		}

		PrintWriter out = openWriter(sFilePath);

		sTS = makeTimeStmp();

		if (bNew)
		{ 
			writeLine("ACN, DCN, MBU, MemberNum, State, Region, LName, FName, Brand, AppType, DocClass, Repository, Documents [Application, Coverpage]", out);
			bNew = false;
		}

		sLine = md.getAcn() + "," + 
				md.getDcn() + "," +
				md.getMbu() + "," +
				md.getMemberNumber() + "," +
				md.getStateCd() + "," +
				md.getWeapRegion() + "," +
				md.getApplicantLastName() + "," +
				md.getApplicantFirstName() + "," +
				md.getBrand() + "," +
				md.getApplicationType() + "," +
				md.getDocumentClass() + "," +
				md.getRepository() + "," +
				md.getDocIDs();
		
		writeLine(sLine, out);

		out.close();
	}
	 
	public static void writeTraceMsg(Exception e)
	{
		WriteToLog("Translating stack trace...");
		
    	StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	e.printStackTrace(pw);
    	
    	String traceMsg = sw.toString();
    	
    	WriteToLog("ERROR Occurred! Trace Message is as follows: ");
    	WriteToLog(traceMsg);
    }
	
	private static void writeLine(String FullMsg, PrintWriter fileOut)
	{
		fileOut.println(FullMsg);
	}
}