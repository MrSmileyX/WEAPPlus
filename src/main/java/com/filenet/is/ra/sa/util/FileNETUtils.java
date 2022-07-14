/*
 *
 * Copyright (c) 2002-2003  FileNet Corporation.
 *
 * REVISION HISTORY:
 *	DATE 		NAME			PURPOSE
 *
 *
 *
 *
 *
 *
 */
package com.filenet.is.ra.sa.util;

import java.io.File;
import java.util.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DateFormat;

/*
This class converts the contents of a ResultSet object to a List in the following format. The column
names are added in another List which is in turn added as the first element of the List. All the data
of the ResultSet is added in the List as the remaining elements.
 This is a List of List. In the following diagram the main List is of size 5 and the inner Lists are of
 size of 4.
	4 rows have been returned and the query's select clause contained 4 fields.
|----------------------------------
|Column1 |Column2|Column3|Column4 |	<------List
|----------------------------------
|data1	 |data2  |data3	 |data4	  |	<------List
|----------------------------------
|data1	 |data2  |data3	 |data4	  |
|----------------------------------
|data1	 |data2  |data3	 |data4	  |
|----------------------------------
|data1	 |data2  |data3	 |data4	  |
----------------------------------
*/

/**
* <code>FileNETUtils</code> is an utility class which converts a ResultSet object to a convenient
*  List object.
* @author Ashu Govil, Neha Pandey, Inderjit Singh
* @version 3.2.1
*/

public class FileNETUtils {

/**
 *	Converts a ResultSet object into List object which in turn contains List objects. The first element of
 *  the returned List is a List which contains the ColumnNames of the query results. All the other elements
 *  are List objects which contain the data that is returned by the query.
 *  @param java.sql.ResultSet
 *  @return java.util.List
 *  @throws SQLException
 */
	//Constants added for ISRA 340 From/to requirement
	public static final int FAX_HEADLINE_TO_LEN = 26;
	public static final int FAX_HEADLINE_FROM_LEN = 26;
	public static final int FAX_HEADLINE_LEN = 98;
	public static final String SPACE_DELIM = " ";
    //Constants added for ISRA 340 From/to requirement ends.

    public static List getListFromResultSet(ResultSet resultSet) throws SQLException{

        if (resultSet == null) return null;

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
	int iColumnCount = resultSetMetaData.getColumnCount();

	List resultList = new ArrayList();

        /*  Add the column names in an ColumnList and add it to another LIst ResultList
            as the first element of the List. */
	List columnList = new ArrayList(iColumnCount);
        for(int index = 1; index <= iColumnCount; index++){
	    columnList.add(resultSetMetaData.getColumnName(index));
	}
	resultList.add(columnList);
        DecimalFormat df = new DecimalFormat("#########0.#####################E0##");
        DateFormat dtf = DateFormat.getInstance();
        //Add the data into ColumnList and add them into the resultList.
	while( resultSet.next()){
	    List dataList = new ArrayList(iColumnCount);
	    for( int index = 1; index <= iColumnCount; index++){
                if (resultSetMetaData.getColumnType(index)==Types.DECIMAL){
                    if (resultSet.getObject(index) != null)
                        dataList.add(df.format(resultSet.getObject(index)));
                    else
                        dataList.add(resultSet.getObject(index));
                }
                else{
                    Object obj = resultSet.getObject(index);
                    if(resultSet.wasNull()){
			obj = null;
                    }
                    dataList.add(obj);
                }
		//original stm : dataList.add(resultSet.getObject(index));
	    }
	    resultList.add(dataList);
	}

	return resultList;
    }

/**
 *  Returns Index Description depending upon the index type passed to the function.
 *  @param String indexType
 *  @return java.lang.String
 */
    public String getIndexDescription(String indexType){

        indexType = indexType.trim();

        /* mapping of index type (hexadecimal) and the index type */
        if (indexType.equals("65")) return "Boolean";
        if (indexType.equals("66")) return "Byte";
        if (indexType.equals("67")) return "Short";
        if (indexType.equals("68")) return "Short";
        if (indexType.equals("69")) return "Integer";
        if (indexType.equals("70")) return "Integer";
        if (indexType.equals("71")) return "Long";
        if (indexType.equals("49")) return "Numeric";
        if (indexType.equals("50")) return "String";
        if (indexType.equals("56")) return "Date";
        if (indexType.equals("51")) return "Date";
        if (indexType.equals("52")) return "Menu";
        if (indexType.equals("48")) return "Null";

        if (indexType.equals("1")) return "Decimal";
         if (indexType.equals("12")) return "Decimal";
        if (indexType.equals("2")) return "String";
        if (indexType.equals("3")) return "Time";
        if (indexType.equals("4")) return "Selection";
        if (indexType.equals("5")) return "Long";
        if (indexType.equals("6")) return "Long";
        if (indexType.equals("7")) return "Long";
        if (indexType.equals("20")) return "Integer";//for Sys field F_PRIORITY
        if (indexType.equals("8")) return "Date";
        if (indexType.equals("10")) return "Boolean";
        if (indexType.equals("11")) return "Null";


        return "";    /* If indexType is not defined in case index type is not the defined one
                        Index description is returned as blank string */
    }

    public String getFieldType(String indexType){

        if(indexType == null){
            return "";
        }
        indexType = indexType.trim();
        
        if (indexType.equals("1")) return "Number";
        if (indexType.equals("2")) return "String";
        if (indexType.equals("3")) return "Time";
        if (indexType.equals("4")) return "Selection";
        if (indexType.equals("5")) return "Document";
        if (indexType.equals("6")) return "Folder";
        if (indexType.equals("7")) return "Integer";
        if (indexType.equals("8")) return "Date";
        if (indexType.equals("10")) return "Boolean";
        if (indexType.equals("12")) return "Decimal";

        return "";
    }
    
    
/**
 *  Returns Array of String from a String of fields (folder or doc ids) seperated by comma.
 *  @param String text
 *                              Fields (DocIDs or Folder) seperated by Comma
 *  @return java.lang.String[]
 *                              Array of Fields
 */

    public String[] getArrayFromString(String text){

        if (text==null || text.trim().length()<=0) return null;

        StringTokenizer st = new StringTokenizer(text,",");
        int count = st.countTokens();
        int i=0;

        String[] arrayFromString = new String[count];

        while(st.hasMoreTokens())
        {
          arrayFromString[i]= st.nextToken();
          i=i+1;
        }

        return arrayFromString;

    }

   /**
        * This method will decode the Full Folder URL consisting of hex values
        * for characters(also multibyte) in Folder Names to the Folder URL of default String.
        *
        * @param pHexURL
        * @return
        */
       public static String decodeURL(String pHexURL) {
         if(pHexURL == null || pHexURL.trim().length() < 2) {
           return WebConstants.SLASH_STAR;
         } else if(pHexURL.trim().equals(WebConstants.SLASH_STAR)) {
           return pHexURL.trim();
         }
         String lFolderURL = "";
         String[] lSlashParts = null;
         String lTempHexURL = pHexURL.trim();
         if (lTempHexURL.endsWith(WebConstants.SLASH_STAR)) {
           int lPos = lTempHexURL.lastIndexOf(WebConstants.CHAR_FSLASH);
           String lTempFolderNames = lTempHexURL.substring(1, lPos);
           lSlashParts = splitString(lTempFolderNames, WebConstants.CHAR_FSLASH);
         }
         String[] lFinalSlashParts = new String[lSlashParts.length];
         for (int i = 0; i < lSlashParts.length; i++) {
           String[] lDelimiterParts = splitString(lSlashParts[i], WebConstants.HEXSTRING_DELIMITER);
           char[] lCharArrary = new char[lDelimiterParts.length];
           int count = 0;
           for (int j = 0; j < lDelimiterParts.length; j++) {
             char cTemp = (char) Integer.parseInt(lDelimiterParts[j], 16);
             lCharArrary[count++] = cTemp;
           }
           lFinalSlashParts[i] = new String(lCharArrary);
         }
         lFolderURL = WebConstants.CHAR_FSLASH;
         for (int i = 0; i < lFinalSlashParts.length; i++) {
           lFolderURL = lFolderURL + lFinalSlashParts[i] + WebConstants.CHAR_FSLASH;
         }
         lFolderURL = lFolderURL + "*";
         return lFolderURL;
       }

       /**
        * This method will encode the default String to a new String consisting
        * of hex values.
        *
        * @param pString
        * @return
        */
       public static String toHexString(String pString) {
         if(pString == null || pString.trim().equals("")) {
           return "";
         }
         String lHexString = "";
         String lTempString = pString.trim();
         for (int j = 0; j < lTempString.length(); j++) {
           if(j == 0) {
             lHexString = Integer.toHexString( (int) lTempString.charAt(j));
           }
           else{
             lHexString = lHexString + WebConstants.HEXSTRING_DELIMITER + Integer.toHexString( (int) lTempString.charAt(j));
           }
         }
         return lHexString;
       }

       /**
        * This method will decode the hex String to default String.
        *
        * @param pHexString
        * @return
        */
       public static String toDefaultString(String pHexString) {
         if(pHexString == null || pHexString.trim().equals("")) {
           return "";
         }

         String[] lDelimiterParts = splitString(pHexString, WebConstants.HEXSTRING_DELIMITER);
         char[] lCharArrary = new char[lDelimiterParts.length];
         int count = 0;
         for (int i = 0; i < lDelimiterParts.length; i++) {
           char cTemp = (char) Integer.parseInt(lDelimiterParts[i], 16);
           lCharArrary[count++] = cTemp;
         }
         String lDefaultString = new String(lCharArrary);
         return lDefaultString;
       }

       /**
        * This method will encode the Full Folder URL consisting of default String
        * to the Folder URL consisting of hex values for all the chcracters in Folder Names.
        *
        * @param pFolderURL
        * @return
        */
       public static String encodeURL(String pFolderURL) {
         if(pFolderURL == null || pFolderURL.trim().length() < 2) {
           return WebConstants.SLASH_STAR;
         } else if(pFolderURL.trim().equals(WebConstants.SLASH_STAR)) {
           return pFolderURL.trim();
         }

         String lHexURL = "";
         String lTempFolderURL = pFolderURL.trim();
         String[] lSlashParts = null;
         if (lTempFolderURL.endsWith(WebConstants.SLASH_STAR)) {
           int lPos = lTempFolderURL.lastIndexOf(WebConstants.CHAR_FSLASH);
           String lTempFolderNames = lTempFolderURL.substring(1, lPos);
           lSlashParts = splitString(lTempFolderNames, WebConstants.CHAR_FSLASH);
         }
         String[] lFinalSlashParts = new String[lSlashParts.length];

         for (int i = 0; i < lSlashParts.length; i++) {
           String lTempHexString = "";
           for (int j = 0; j < lSlashParts[i].length(); j++) {
             if(j == 0) {
               lTempHexString = Integer.toHexString( (int) lSlashParts[i].charAt(j));
             }
             else {
               lTempHexString = lTempHexString + WebConstants.HEXSTRING_DELIMITER +
                   Integer.toHexString( (int) lSlashParts[i].charAt(j));
             }
           }
           lFinalSlashParts[i] = lTempHexString;
         }
         lHexURL = WebConstants.CHAR_FSLASH;
         for (int i = 0; i < lFinalSlashParts.length; i++) {
           lHexURL = lHexURL + lFinalSlashParts[i] + WebConstants.CHAR_FSLASH;
         }
         lHexURL = lHexURL + "*";
         return lHexURL;
       }

       /**
        * This method will split a String with a particular delimiter.
        *
        * @param pString
        * @param pDelimiter
        * @return
        */
       private static String[] splitString(String pString, String pDelimiter) {
         StringTokenizer lStringTok = new StringTokenizer(pString, pDelimiter);
         int lCount = lStringTok.countTokens();
         int i=0;
         String[] lArrayFromString = new String[lCount];

         while(lStringTok.hasMoreTokens()) {
           lArrayFromString[i] = lStringTok.nextToken();
           i = i + 1;
         }
         return lArrayFromString;
       }

   /**

/**
 *  Deletes temporary files and folders created for AddDoc Interaction and GetDocContent Interaction
 *  @param java.io.File file
 *                              Directory under which all temporary files and folders has to be deleted
 *  @return void
 *                              Array of Fields
 */
    public void delFilesAndDirs(File file) {

        File dir = file;
        String[] list = dir.list();

        for(int i=0; i< list.length; i++) {
            File file1 = new File(dir,list[i]);
            if(file1.isDirectory()) {
                delFilesAndDirs(file1);
                boolean success = file1.delete();
            }
            else if(file1.isFile()) {
                boolean success = file1.delete();
            }
        }
    }

/**
 *  Calculates archive or delete date by adding specified months to a base date
 *  @param java.util.Date
 *                              Base date upon which new date is to be calculated
 *  @param java.lang.String
 *                              Offset number of months
 *  @return java.util.Date
 *                              New date at an offset from the base date
 */
    public java.util.Date getOffsetDate(java.util.Date baseDate, String offsetStr) {
      int dateYear = baseDate.getYear();
      int dateMonth = baseDate.getMonth();
      int offsetYears = 0;
      int offsetMonths = 0;
      int offset = 0;

         try{
             offset = Integer.parseInt(offsetStr);
         }catch(NumberFormatException nfe){
             throw nfe;
         }

      offsetYears = offset/12;
      offsetMonths = offset%12;

      int abc = dateMonth + offsetMonths;

      if(abc > 12){
        offsetYears++;
        offsetMonths = abc%12;
      }

      dateYear = dateYear + offsetYears;
      dateMonth = dateMonth + offsetMonths;

      baseDate.setMonth(dateMonth);
      baseDate.setYear(dateYear);

      return baseDate;

    }

	// Code added for ISRA 340 From/to requirement.
	public static String concat(String strTo, String strFrom, String strSubject) throws Exception {
		if (strTo == null && strFrom == null && strSubject == null) {
			throw new Exception("Fields cannot be null");
		}
		strTo = strTo == null ? "" : strTo;
		StringBuffer sbHeadline = new StringBuffer(strTo);
		int iNumOfSpaces = FAX_HEADLINE_TO_LEN - strTo.length();
		if (iNumOfSpaces>0) {
			for(int i=0; i<iNumOfSpaces; i++) {
				sbHeadline.append(SPACE_DELIM);
			}
		}
		strFrom = strFrom == null ? "" : strFrom;
		sbHeadline.append(strFrom);
		iNumOfSpaces = FAX_HEADLINE_TO_LEN + FAX_HEADLINE_FROM_LEN - sbHeadline.length();
		if (iNumOfSpaces>0) {
			for(int i=0; i<iNumOfSpaces; i++) {
				sbHeadline.append(SPACE_DELIM);
			}
		}
		strSubject = strSubject == null ? "" : strSubject;
		sbHeadline.append(strSubject);
		if(sbHeadline.length()>FAX_HEADLINE_LEN) {
			sbHeadline.delete(FAX_HEADLINE_LEN, sbHeadline.length());
		}
		return sbHeadline.toString();
	}
	// Code added for ISRA 340 From/to requirement ends.

    
}