package com.wlp.ecm.weap.reporting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wlp.ecm.weap.common.*;
import com.wlp.ecm.weap.config.AppConfiguration;
import com.wlp.ecm.weap.config.DBSettings;
import com.wlp.ecm.weap.config.jaxb.BatchIdConfig;
import com.wlp.ecm.weap.config.jaxb.Tables;

public class Database 
{
	
	public static boolean logEntry(DBSettings dbConfig, MessageData md, String xmlPath)
	{
		boolean entryLogged = false;
		boolean tableReady = false; 
	
		String thisClass = dbConfig.getDBClass();
		String thisOCI = dbConfig.getDBThin();
		String thisURL = dbConfig.getDBURL();
		String thisUser = dbConfig.getDBUser();
		String thisPass = dbConfig.getDBPass();
		String tableName = dbConfig.getDBTable();
	
		WriteLog.WriteToLog("Database Class: " + thisClass);
		WriteLog.WriteToLog("Database Thin  : " + thisOCI);
		WriteLog.WriteToLog("Database URL  : " + thisURL);
		WriteLog.WriteToLog("Database User : " + thisUser);
		WriteLog.WriteToLog("Database Pass : " + thisPass);
		WriteLog.WriteToLog("Database Table: " + tableName);
		
		Connection myConn = connectOracleDB(thisClass, thisOCI, thisURL, thisUser, thisPass);
		WriteLog.WriteToLog("Connected to " + thisURL + ".");
		
		WriteLog.WriteToLog("Generating create table SQL.");
		sqlRecord createInfo = SQLtoXMLParser.createSQLRecord(xmlPath);
		
		if (myConn != null)
		{
			tableReady = checkForTable(myConn, tableName, createInfo);
			
			if (tableReady)
			{
				WriteLog.WriteToLog("Table found... inseting log entry.");
				String insertSQL = createInsertSQL(md, createInfo);
				
				entryLogged = insertRecord(myConn, insertSQL);
			}
			else
			{
				WriteLog.WriteToLog("Table does not exist... skipping entry logging.");
			}
		}
		else
		{
			WriteLog.WriteToLog("Error! Unable to connect to database!");
		}
		
		return entryLogged;
	}
	
	
	public static boolean checkForTable(Connection myConn, String tableName, sqlRecord createInfo)
	{
		boolean tableFound = false;
		
		WriteLog.WriteToLog("Checking if table " + tableName + " exists...");
		
		boolean wasFound = tableExists(myConn, tableName);
			
		if (wasFound)
		{
			WriteLog.WriteToLog("Table " + tableName + " exists...");
			tableFound = true;
		}
		else
		{
			tableFound = false;

			try
			{
				WriteLog.WriteToLog("Table " + tableName + " does not exist... creating.");
				tableFound = createTable(myConn, createInfo);
			}
			catch (SQLException sqle)
			{
				WriteLog.WriteToLog("ERROR Creating table!");
				WriteLog.WriteToLog("Exception: " + sqle.getMessage());
				
				tableFound = false;
			}
			catch (Exception e)
			{
				WriteLog.WriteToLog("ERROR Creating table!");
				WriteLog.WriteToLog("Exception: " + e.getMessage());
				
				tableFound = false;
			}
		}
		
		return tableFound;
	}
	
	public static Connection connectOracleDB(String dbClass, String dbOCI, String dbURL, String dbUser, String dbPass) 
	{
		Connection subConn = null;

		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
		}
		catch (ClassNotFoundException ex) 
		{
			WriteLog.WriteToLog("Error: unable to load driver class!");
		}
		
		WriteLog.WriteToLog("URL: " + dbOCI + dbURL);

		try 
		{
			subConn = DriverManager.getConnection(dbOCI + dbURL, dbUser, dbPass);
			
			WriteLog.WriteToLog("Made Connection.");
			
			subConn.setAutoCommit(true);
		}
		catch (SQLException sqle) 
		{
			WriteLog.WriteToLog("Error: unable to create connection!");
			WriteLog.WriteToLog(sqle.getMessage());
		}
		catch (Exception e)
		{
			WriteLog.WriteToLog("Error: unable to create connection!");
			WriteLog.WriteToLog(e.getMessage());
		}	

		if (subConn != null) 
		{
			WriteLog.WriteToLog("Connection Successful!");
		}
		else 
		{
			WriteLog.WriteToLog("Oracle Connection failed.");
		}

		return subConn;
	}

	public static Connection connectSQLDB(String sqlDB, String sqlCat) 
	{
		String sURL = "";
		Connection subConn = null;

		try 
		{
			WriteLog.WriteToLog("Connecting to check database...");

			sURL = "jdbc:sqlserver://VA10PWVSQL121.us.ad.wellpoint.com\\SQL01:10001;initialcatalog=fawlppr0;integratedsecurity=true;";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			subConn = DriverManager.getConnection(sURL);
		}
		catch (ClassNotFoundException cnfE) 
		{
			WriteLog.WriteToLog("JDBC Class error: " + cnfE.getMessage());
		}
		catch (SQLException sqlE) 
		{
			WriteLog.WriteToLog("SQL error: " + sqlE.getMessage());
		}
		catch (Exception exe) 
		{
			WriteLog.WriteToLog("Process error: " + exe.getMessage());
		}

		if (subConn != null) 
		{
			WriteLog.WriteToLog("Connected.");
		}
		else 
		{
			WriteLog.WriteToLog("SQL Server Connection failed.");
		}

		return subConn;
	}

	public static boolean tableExists(Connection conn, String tableName) 
	{
		boolean doesExist = false;
		String tableSrch = "";
		
		WriteLog.WriteToLog("Performing table existence check for " + tableName + "...");
		
		try 
		{
			if (tableName.contains(" "))
			{
				tableSrch = tableName.substring(0, tableName.indexOf(" "));
			}
			else
			{
				tableSrch = tableName;
			}
			
			DatabaseMetaData meta = conn.getMetaData();

			ResultSet res = meta.getTables(null, null, tableSrch.toUpperCase(), new String[] { "TABLE" });

			while (res.next() && doesExist == false) 
			{
				String sCurrTbl = res.getString("TABLE_NAME");

				if (sCurrTbl.toUpperCase().equals(tableSrch.toUpperCase())) 
				{
					doesExist = true;
				}
			}
		}
		catch (SQLException sqlE) 
		{
			WriteLog.WriteToLog("Table search error: " + sqlE.getMessage());
		}

		return doesExist;
	}
	
	public static boolean Disconnect(Connection cnDisc) 
	{
		try 
		{
			WriteLog.WriteToLog("Disconnecting from database...");

			if (cnDisc.isClosed() == false) 
			{
				cnDisc.close();
				WriteLog.WriteToLog("Disconnected.");
				return true;
			} 
			else 
			{
				WriteLog.WriteToLog("Failed to disconnect from database!");
				return false;
			}
		} 
		catch (SQLException sqlE) 
		{
			return false;
		}
	}

	public static boolean createTable(Connection dbConn, sqlRecord createInfo) throws SQLException
	{
		String tableName = createInfo.getName();
		String createTableSQL = createInfo.createQuery();
				
		Statement statement = null;
		
		WriteLog.WriteToLog("Creating table " + tableName + "...");
		WriteLog.WriteToLog(createTableSQL);
		
		try 
		{
			statement = dbConn.createStatement();
			statement.execute(createTableSQL);

			WriteLog.WriteToLog("Table " + tableName + " is created!");
		}
		catch (SQLException e) 
		{
			WriteLog.WriteToLog(e.getMessage());
		} 
		finally 
		{
			if (statement != null) 
			{
				statement.close();
			}

			if (dbConn != null) 
			{
				dbConn.close();
			}
		}

		boolean created = tableExists(dbConn, tableName);
		return created;
	}
	
	public static boolean insertRecord(Connection connO, String insertStmt)
	{
		boolean bDidInsert = false;
		Statement stmtPData = null;
				
		try
		{
			stmtPData = connO.createStatement();
		}
		catch (SQLException e)
		{
			WriteLog.WriteToLog(e.getMessage());
		}

		if (stmtPData != null)
		{
			try
			{
				WriteLog.WriteToLog("Inserting WEAP log record...");

				stmtPData.executeQuery(insertStmt);
				bDidInsert = true;
				
				WriteLog.WriteToLog("WEAP log record inserted.");
			}
			catch (SQLException e)
			{
				String sErrMsg = e.getMessage();

				if(sErrMsg.startsWith("ORA-00942"))
				{
					WriteLog.WriteToLog(sErrMsg);
					WriteLog.WriteToLog("Table was not found");
				}
				else
				{
					WriteLog.WriteToLog("The following SQL error has occurred.");
					WriteLog.WriteToLog(sErrMsg);
				}
				
				bDidInsert = false;
			}
			
			try
			{
				stmtPData.close();
			}
			catch (SQLException sqle)
			{
				WriteLog.WriteToLog(sqle.getMessage());
			}
		}
		
		return bDidInsert;
	}
	
	
	public static int getRowCount(ResultSet resultToCnt) 
	{
		int count = 0;

		try 
		{
			while (resultToCnt.next()) 
			{
				++count;
			}

			if (count == 0) 
			{
				WriteLog.WriteToLog("No records found");
			}
		} 
		catch (SQLException sqlE) 
		{
			WriteLog.WriteToLog("Error: " + sqlE.getMessage());
		}

		return count;
	}
	
	public static String createInsertSQL(MessageData md, sqlRecord createInfo)
	{
		boolean wasInserted = false;
		
		String header = "INSERT INTO ";
		String newLine = "\r\n";
		String openParen = "(";
		String closeParen = ")";
		String valuesEnd = ")";
		String valuesStmt = "VALUES ";
		String colRows = "";
		String colVals = "";
		String insertStmt = "";
		
		String tableName = createInfo.getName();
		String messageTS = convertTimeStamp(md.getMsgTimeStamp());
		String processTS = convertTimeStamp(md.getProcTimeStamp());
		
		for (int c = 0; c < createInfo.sqlFields.size(); c++)
		{
			sqlField thisField = createInfo.sqlFields.get(c);
			String fldName = thisField.getDesc();
			 
			if (c == 0)
			{
				colRows = openParen + fldName;
			}
			else
			{
				colRows += ", " + fldName;
			} 
		}
		 
		colRows += closeParen;
		 
		String orcMsgTs = "TO_TIMESTAMP('" + messageTS + "', 'YYYY/MM/DD HH:MI:SS')";
		String orcProcTs = "TO_TIMESTAMP('" + processTS + "', 'YYYY/MM/DD HH:MI:SS')";
		
		colVals = openParen;
		colVals += "'" + md.getMessageID() + "', ";
		colVals += orcMsgTs + ", ";
		colVals += orcProcTs  + ", ";
		colVals += "'" + md.getServerName() + "', ";
		colVals += "'" + md.getACN() + "', ";
		colVals += "'" + md.getDCN() + "', ";
		colVals += "'" + md.getMBU() + "', ";
		colVals += "'" + md.getMemberNum() + "', ";
		colVals += "'" + md.getStateCode() + "', ";
		colVals += "'" + md.getRegionCode() + "', ";
		colVals += "'" + md.getFirstName() + "', ";
		colVals += "'" + md.getLastName() + "', ";
		colVals += "'" + md.getBrandVal() + "', ";
		colVals += "'" + md.getAppType() + "', ";
		colVals += "'" + md.getDocClass() + "', ";
		colVals += "'" + md.getRepoName() + "', ";
		colVals += "'" + md.getRepoDesc() + "', ";
		colVals += md.getCoverPageCnt() + ", ";
		colVals += md.getCoverDocNum() + ", ";
		colVals += "'" + md.getCoverFmt() + "', ";
		colVals += "'" + md.getCoverStatus() + "', ";
		colVals += "'" + md.getCoverMessage() + "', ";
		colVals += md.getAppPageCnt() + ", ";
		colVals += md.getAppDocNum() + ", ";
		colVals += "'" + md.getAppFmt() + "', ";
		colVals += "'" + md.getAppStatus() + "', ";
		colVals += "'" + md.getAppMessage() + "', ";
		colVals += "'" + md.getBkupFileNm() + "'";
		colVals += valuesEnd;
		
		insertStmt = header + tableName + newLine + colRows + newLine + valuesStmt + colVals;
		
		return insertStmt;
	}
	
	public static String convertTimeStamp(String tsIn)
	{
		DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS aa (z)");
		DateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		java.util.Date date = null;
		
		try 
		{
			date = originalFormat.parse(tsIn);
		}
		catch (ParseException e) 
		{
			WriteLog.WriteToLog("Unable to convert timestamp!");
		}
		
		String formatDate = targetFormat.format(date); 
		
		return formatDate;
	}
	
}