package com.filenet.is.ra.sa.util;

/**
 * Title:        Sample Application
 * Description:
 * Copyright:    Copyright (c) 2005-2006
 * Company:      FileNet Corporation
 * @author
 * @version 3.2.1
 */
import java.util.*;

public class WebConstants {

//Constants used in JSPs and Servlets
  public static final String FN_ANNO_SIZE = "FnAnnoSize";
  public static final String FN_NUM_OF_FIELDS = "FnNumOfFields";
  public static final String FILE_TYPE_TIFF = "image/tiff";
  public static final String FILE_TYPE_JPEG = "image/jpeg";
  public static final String FILE_TYPE_JPG = "image/jpg";

  public static final String ACTION_COMMAND = "action_command";
  public static final String CURRENT_JSP = "currentjsp";
  public static final String RESULT = "result";
  public static final String CLIENT_BEAN = "clientBean";
  public static final String CONTEXT_NAME = "CONTEXT_NAME";
  public static final String DEPLOYED_ENVIRONMENT = "DEPLOYED_ENVIRONMENT";
  public static final String LOGIN_MODE = "loginMode";
  public static final String LOGIN_TYPE = "loginType";
  public static final String USER_MODE = "userMode";
  public static final String TESTER_MODE = "testerMode";
  public static final String GET_DOC_CONTENT_QUERY = "select F_DOCNUMBER, F_PAGES, F_DOCFORMAT, F_DOCTYPE from FnDocument where F_DOCNUMBER =";
  public static final String BROWSE_QUERY = "select F_DOCNUMBER, F_ENTRYDATE, F_PAGES, F_DOCCLASSNAME, F_DOCFORMAT from FnDocument where F_DOCNUMBER > 1";
  public static final String FOLDER_QUERY = "select F_DOCNUMBER, F_DOCFORMAT, F_PAGES, F_DOCCLASSNAME, F_ENTRYDATE from FnDocument";
  public static final String INTERACTION_EXECUTE3 = "interactionExecute3";

//       Code Added for ISRA Globalization Real fix
  public static final String UTF8 = "UTF-8";
  public static final String EXCEPTION = "Exception:";
  public static final String CONTENT_TYPE_TEXT = "text/plain";
  public static final String CONTENT_TYPE_HTML = "text/html";

  public static final String TIF_TYPE = "TIF";
  public static final String TIFF_TYPE = "TIFF";
  public static final String BMP_TYPE = "BMP";
  public static final String JPG_TYPE = "JPG";
  public static final String JPEG_TYPE = "JPEG";
  public static final String JPE_TYPE = "JPE";
  public static final String TXT_TYPE = "TXT";
  public static final String HTM_TYPE = "HTM";
  public static final String HTML_TYPE = "HTML";
  public static final String GIF_TYPE = "GIF";

  public static final String FILE_TYPE_GIF = "image/gif";
  public static final String FILE_TYPE_BMP = "image/bmp";
  public static final String FILE_TYPE_TIF = "image/tif";
  public static final String CONTENT_TYPE_IMAGE = "INX_IMAGE_DOC";
  public static final String CONTENT_TYPE_OTHER = "INX_OTHER_DOC";

  public static final String PROP_COUNT = "PropsCount";
  public static final String PROPERTY_TYPE = "_TYPE";
  public static final String PROPERTY_VALUE = "_VALUE";

  public static final String MIME_TYPE = "application/octet-stream";

  public static final String NAME_VALUE = ";name=";
  public static final String NULL_VALUE = "null";
  public static final String NUM_10000 = "10000";
  public static final String NUM_0 = "0";
  public static final String NUM_1 = "1";
  public static final String NUM_2 = "2";
  public static final String NUM_3 = "3";
  public static final String NUM_5 = "5";
  public static final String NUM_10 = "10";
  public static final String NUM_16 = "16";
  public static final String NUM_48 = "48";
  public static final String NUM_49 = "49";
  public static final String NUM_50 = "50";
  public static final String NUM_51 = "51";
  public static final String NUM_52 = "52";
  public static final String NUM_53 = "53";
  public static final String NUM_56 = "56";
  public static final String NUM_65 = "65";
  public static final String NUM_66 = "66";
  public static final String NUM_67 = "67";
  public static final String NUM_68 = "68";
  public static final String NUM_69 = "69";
  public static final String NUM_70 = "70";
  public static final String NUM_71 = "71";
  public static final String NUM_5000 = "5000";
  public static final String ERROR = "Error:";

  public static final String CHAR_FSLASH = "/";
  public static final String CHAR_EQUALS = "=";
  public static final String CHAR_AMPERSAND = "&";
  public static final String CHAR_QMARK = "?";

  public static final String CHAR_DBSLASH = "\\";

  public static final String WAS = "IBM WebSphere";

  public static final String PERM_ANYONE = "ANYONE";
  public static final String PERM_NONE = "NONE";
  public static final String COUNTER = "counter";
  public static final String CHARSET_PARAM = "text/plain; charset=UTF-8";
  public static final String DISPLAY_JAVA_VIEWER = "DISPLAY_JAVA_VIEWER";
  //Constant "IS_JAVA_VIEWER_EXISTS" added for DTS183831
  public static final String IS_JAVA_VIEWER_EXISTS = "Isviewerexists";
  public static final String PAGECOUNT = "PAGECOUNT";
  public static final String ST_UNKNOWN = "unknown";
  public static final String CONTENT_DISPOSITION = "Content-disposition";

  public static final String ANNOT_XSL = "/web/Annot.xsl";

  public static final String FILENAME = "filename=";
  public static final String FILE_NAME = "filename";
  public static final String ALL_PAGES = "allPages";
  public static final String MIMETYPE = "mimeType";
  public static final String IMAGES = "images";
  public static final String LDAP = "LDAP/";
  public static final String ISRATEMP = "ISRATEMP";
  public static final String SEARCH = "Search.jsp";
  public static final String SEARCHWAS = "SearchWAS";
  public static final String IBM_WAS4 = "IBM WebSphere Application Server/4.";
  public static final String DEL_DIRNAME = "/web/temp/";
  public static final String SESSION_DIR = "/web/temp/Sample_";
  public static final String STR_BROWSE = "Browse";
  public static final String STR_SEARCH = "Search";
  public static final String MDY_DATE = "MM/dd/yyyy HH:mm";
  public static final String ANNOTID_STATUS = "status=";
  public static final String YES = "yes";
  public static final String NO = "no";
  public static final String JNDI_LOCATOR = "java:comp/env/";
  public static final String USEPRINTVIEWOPTION = "USEPRINTVIEWOPTION";
  public static final String PRINTVIEWOPTION = "PRINTVIEWOPTION";

  public static final String PROPERTY = "PROPERTY";
  public static final String DOC_PROPCOUNT = "DocPropCount";
  public static final String FOLDER_NAME1 = "folderName1";

  public static final String IMG_DIR = "/images";
  public static final String STR_ADD_DOC = "AddDoc";
  public static final String ADD_DOC_JSP = "AddDoc.jsp";
  public static final String UPLOAD_DIR = "/Upload_";
  public static final String DIR_PATH = "dirPath";
  public static final String SLASH_STAR = "/*";
  public static final String STR_ADD_FOLDER = "AddFolder";
  public static final String ADD_FOLDER_JSP = "AddFolder.jsp";
  public static final String TUPLE_ONE = "1,1,1";
  public static final String STR_NULL = "NULL";
  public static final String BROWSE_JSP = "Browse.jsp";
  public static final String STR_VUDOCPROPERTIES = "View/UpdateDocProperties";
  public static final String VIEWDOCPROPERTIES_JSP =
      "ViewDocumentProperties.jsp";
  public static final String UPDATEDOC_PROPERTIES_JSP =
      "UpdateDocProperties.jsp";
  public static final String STRQUERY_SELECT = "select ";
  public static final String STRQUERY_KEYCONDITION =
      " from FnDocument Key Condition ";
  public static final String ALPHABET_F = "F_";
  public static final String RETAIN_MSG = "retainMsg";
  public static final String STR_DECIMAL =
      "#########0.#########################";
  public static final String STR_QUOT = "&quot;";
  public static final String JAVA_DATE = "java.sql.Date";
  public static final String JAVA_TIMESTAMP = "java.sql.Timestamp";
  public static final String MESSAGE = "message";
  public static final String SINGLE_SELECT_MESSAGE = "singleselectmessage";
  public static final String FOLDER_MESSAGE = "foldermessage";
  public static final String SEARCHWAS_JSP = "SearchWAS.jsp";
  public static final String STR_PAGECOUNT = "pagecount";
  public static final String STR_REMOVEDOCSFROM_FOLDER =
      "RemoveDocsFromFolder ";
  public static final String REMOVEDOCSFROM_FOLDER_JSP =
      "RemoveDocsFromFolder.jsp";
  public static final String STR_QUEUEMAINT = "QueueMaint";
  public static final String STR_CHANGE_PWD = "ChangePassword";
  public static final String STR_QUEUE_MAINT = "QueueMaintenance";
  public static final String STR_CREATE_QUEUE = "CreateQueue";
  public static final String BTN_SUBMIT = "bSubmit";
  public static final String ERR_TUPLE = "<156,2,16>";
  public static final String RENDEZVOUS_FLD = "RendezvousField";
  public static final String COUNTR = "Counter";
  public static final String SELECT_FRM = "Select * from ";
  public static final String QUEUE_MAINT_JSP = "QueuesMaint.jsp";
  public static final String STR_PRINTFAX = "Print/FaxDocuments";
  public static final String STR_LOGIN = "Login";
  //variable added for changePassword(DTS 169459) interaction
  public static final String STR_CPWLOGIN = "CPWLogin";
  public static final String STR_SEARCHRESULTS_WAS = "SearchResultsWAS ";
  public static final String COL_DATA = "colData";
  public static final String ENTERPRISE = "Enterprise";
  public static final String F_DOCNUMBER1 = "F_DOCNUMBER, ";
  public static final String STR_SEARCHRESULTS = "SearchResults ";
  public static final String STR_IMAGE = "Image";
  public static final String STR_TEXT = "Text";
  public static final String STR_MIXED = "Mixed";
  public static final String STR_OTHER = "Other";
  public static final String STR_CREATE_WKSP = "CreateWorkspace";
  public static final String EJECT_TRAY1 = "EjectTray";
  public static final String STR_SELECTED = "Selected";
  public static final String STR_QUEUECHANGED = "queueChanged";
  public static final String STR_WSPACECHANGED = "workspaceChanged";
  public static final String STR_DELETE_DOC = "DeleteDocs";
  public static final String DELETE_DOC_JSP = "DeleteDocsResults.jsp";
  public static final String STR_DELETE_FOLDER = "DeleteFolder";
  public static final String DELETE_FOLDER_JSP = "DeleteFolder.jsp";
  public static final String DELETE_FOLDER_SUBMIT = "DeleteFolderSubmit";
  public static final String CANCEL = "Cancel";
  public static final String REDIRECT1 = "redirect1";
  public static final String CURRENT_FOLDR = "currentfolder";
  public static final String SAVE = "Save";
  public static final String GMVALUERESULTS_JSP = "GetMenuValueResults.jsp";
  public static final String STR_INDEXNAMECHGD = "indexnameChanged";
  public static final String STR_HASH = "#";
  public static final String HELP_HTML = "/Help.html";
  public static final String STR_QUEUESMAINT = "QueuesMaint";
  public static final String STR_SAVE_QUEUE = "SaveQueue";
  public static final String STR_INSERT_QUEUEENTRY = "InsertQueueEntry";
  public static final String ADD = "Add";
  public static final String STR_GFATTRIBUTES = "GetFolderAttributes";
  public static final String GFATTRIBUTES_JSP = "GetFolderAttributes.jsp";
  public static final String STR_SAVE_WKSP = "SaveWorkspace";
  public static final String STR_MENULBL_CHANGED = "menulabelChanged";
  public static final String STR_INDEXNME_CHANGED = "indexnameChanged";
  public static final String STR_GETMENUVALUE = "GetMenuValue";
  public static final String GETMENUVALUE_JSP = "GetMenuValue.jsp";
  public static final String SHOW_QUEUE_ENTRY = "showQueueEntry";
  public static final String SHOW_QUEUES = "showQueues";
  public static final String FIELD_TYPE_BYTE = "Byte";
  public static final String FIELD_TYPE_SHORT = "Short";
  public static final String FIELD_TYPE_LONG = "Long";
  public static final String DAEJA_JIJAR_PATH =
      "/FNImageViewer/FNJavaV1Files/ji.jar";
  public static final String FOB_EXTN = ".FOB";
  public static final String FLAG_EQ = "flag=";
  public static final String BASE_FILENAME = "basefilename";
  public static final String PAGE_NO = "pagenumber";
  public static final String CONTEXT_NM = "contextName";
  public static final String LIB_NM = "libName";
  public static final String VALIDATE_DOCID = "ValidateDocId";
  public static final String CALLED = "called";
  public static final String STR_FOLDERNAME = "foldername";
  public static final String STR_FOLDERSECREAD = "folderSecRead";
  public static final String STR_FOLDERSECWRITE = "folderSecWrite";
  public static final String STR_FOLDERSECAPPEXE = "folderSecAppExe";
  public static final String STR_DIGSIGNATURE = "Digital Signature";
  public static final String STR_lDOCID = "l_DocID";
  public static final String DISP_FOLDER_DOCS_FILED =
      "DisplayFoldersDocFiledIn";
  public static final String DISP_FOLDER_DOCS_FILED_JSP =
      "DisplayFoldersDocFiledIn.jsp";
  public static final String FILE_DOCS_IN_FOLDER = "FileDocsInFolder";
  public static final String FILE_DOCS_IN_FOLDER_JSP = "FileDocsInFolder.jsp";
  public static final String STR_GETDIGITALSIGNATURES = "GetDigitalSignatures ";
  public static final String GETDIGITALSIGNATURES_JSP =
      "GetDigitalSignatures.jsp";
  public static final String FILE_DOCS_IN_FOLDER_RESULT =
      "FileDocsInFolderResults";
  public static final String FILE_DOCS_IN_FOLDER_RESULT_JSP =
      "FileDocsInFolderResults.jsp";
  public static final String GET_CACHELIST = "GetCacheList";
  public static final String GET_CACHELIST_JSP = "GetCacheList.jsp";
  public static final String MSG_DOCTYPE_NOTSUPPORTED =
      "Document Type not supported";
  public static final String STR_QUERY = "select F_DOCNUMBER, F_DOCFORMAT, F_PAGES from FnDocument where F_DOCCLASSNAME = '";
  public static final String STR_ADD_DIGITALSIGNATURE = "AddDigitalSignature";
  public static final String STR_GETDOCCLASSINDICES = "GetDocClassIndices";
  public static final String GETDOCCLASSINDICES_JSP = "GetDocClassIndices.jsp";
  public static final String STR_LESSTHANMSG = "lessthanmessage";
  public static final String MSG_FIRSTPAGENEGATIVE =
      "firstpagenegativevaluemessage";
  public static final String MSG_LASTPAGENEGATIVE =
      "lastpagenegativevaluemessage";
  public static final String MSG_VALIDINT = "validintegermessage";
  public static final String STR_TARGETURL = "targeturl";
  public static final String STR_GETDOCCONTENT2 = "GetDocumentContent2";
  public static final String GETDOCCONTENT2PARAM_JSP =
      "GetDocumentContent2Parameters.jsp";
  public static final String STR_DOCUMENTSTATUS = "DocumentStatus";
  public static final String DOCUMENTSTATUSPARAM_JSP = "GetDocumentStatusParameters.jsp";
  public static final String DOCMNT_ID = "DocId";
  public static final String EDIT = "Edit";
  public static final String FILE_NM = "fileName";
  public static final String CMB_FILE_NM = "cmbFileNames";
  public static final String SUBMIT_FLAG = "submitflag";
  public static final String UPDATE = "Update";
  public static final String CANCEL_UPDATE = "CancelUpdate";
  public static final String STR_WEBSPHERE = "WebSphere";
  public static final String STR_WEBLOGIC = "WebLogic";
  public static final String NUM_DEC_4 = "4.0";
  public static final String STR_DATA = "Data";
  public static final String STR_TMP = ".tmp";
  public static final String STR_CONTENT_TYPE = "content-type";
  public static final String STR_CONTENT_TYPE1 = "content-type:";
  public static final String STR_CONTENT_TYPE2 = "Content-Type";
  public static final String STR_MULTIPART_FORM_DATA = "multipart/form-data";
  public static final String STR_CONTENT_DISPOSITION = "content-disposition:";
  public static final String STR_BOUNDARY = "boundary=";
  public static final String STR_CONTENT_DISPOSITION1 = "content-disposition: ";
  public static final String STR_NAME_SLASH = "name=\"";
  public static final String STR_FILENAME_SLASH = "filename=\"";
  public static final String STR_FORM_DATA = "form-data";
  public static final String RECORD_COUNT = "recordCount";
  public static final String RESOURCE_BULDLE_SAMPLE =
      "com.filenet.is.ra.sa.util.FNISRASampleMessages";
  public static final String PACKAGE_GETMSGIMPLEMENTATION =
      "com.filenet.is.ra.sa.util.GetMessageImpl";
  public static final String STR_ADD_DOCUMENTS = "AddDocuments ";
  public static final String STR_UPDATEFOLDER_PROPERTIES =
      "UpdateFolderProperties";
  public static final String UPDATEFOLDER_PROPERTIES_JSP = "UpdateFolder.jsp";
  public static final String STR_PRINT = "Print";
  public static final String STR_MSG = "Message for key '[";
  public static final String STR_MSG1 = "]' not found in ResourceBundle.";
  public static final String ADD_FOLDER_SUBMIT = "AddFolderSubmit";
  public static final String LOCALE_NULL = "Current Locale is Null";
  public static final String HEXSTRING_DELIMITER = "|";
  public static final String FROM_BROWSE = "fromBrowse";
  public static final String FROM_SEARCH = "fromSearch";
  public static final String FROM_FOLDER_SERVLET = "fromFolderServlet";
  public static final String FROM_ADDFOLDER_SERVLET = "fromAddDocServlet";
  public static final String FROM_QENTRYSERVLET = "fromQEntryServlet";

  public static final String ENCODING_ISO8859_1 = "ISO8859_1";
  public static final String ENCODING_Cp1252 = "Cp1252";
  public static final String ENCODING_ISO8859_2 = "ISO8859_2";
  public static final String ENCODING_Cp1250 = "Cp1250";
  public static final String ENCODING_ISO8859_5 = "ISO8859_5";
  public static final String ENCODING_Cp1251 = "Cp1251";
  public static final String ENCODING_ISO8859_6 = "ISO8859_6";
  public static final String ENCODING_Cp1256 = "Cp1256";
  public static final String ENCODING_ISO8859_7 = "ISO8859_7";
  public static final String ENCODING_Cp1253 = "Cp1253";
  public static final String ENCODING_ISO8859_8 = "ISO8859_8";
  public static final String ENCODING_Cp1255 = "Cp1255";
  public static final String ENCODING_ISO8859_9 = "ISO8859_9";
  public static final String ENCODING_Cp1254 = "Cp1254";
  /*Commented for DTS 193639
  public static final String ENCODING_ISO8859_13 = "ISO8859_13";
  Comment for DTS 193639 ends.*/

  //Code added for DTS 193639
  public static final String ENCODING_ISO8859_4 = "ISO8859_4";
  //Code added for DTS 193639 ends

  public static final String ENCODING_Cp1257 = "Cp1257";

// List of FileNet's System Index that can not be updated by Client
  public static final String F_DOCNUMBER = "F_DOCNUMBER";
  public static final String F_DOCNUMBER_TYPE = "71";
  public static final String F_DOCCLASSNUMBER = "F_DOCCLASSNUMBER";
  public static final String F_DOCCLASSNAME = "F_DOCCLASSNAME";
  public static final String F_ENTRYDATE = "F_ENTRYDATE";
  public static final String F_RETENTOFFSET = "F_RETENTOFFSET";
  public static final String F_DOCTYPE = "F_DOCTYPE";
  public static final String F_ARCHIVEDATE = "F_ARCHIVEDATE";
  public static final String F_DELETEDATE = "F_DELETEDATE";
  public static final String F_RETENTBASE = "F_RETENTBASE";
  public static final String F_RETENTDISP = "F_RETENTDISP";
  public static final String F_DOCLOCATION = "F_DOCLOCATION";
  public static final String F_ANNOTATIONFLAG = "F_ANNOTATIONFLAG";
  public static final String F_PAGES = "F_PAGES";
  public static final String F_CLOSED = "F_CLOSED";
  public static final String F_ACCESSRIGHTS = "F_ACCESSRIGHTS";
  public static final String ERROR_CODE_INIT = "* ErrorCode:";
  public static final String EXCEPTION_CODE_INIT = "<br>* Exception:";
  public static final String EXCEPTION_CODE_DOC_ID = " Doc Id = ";
  public static final String EXCEPTION_CODE_ERROR_CODE_NO = " Error Code : ";
  public static final String FALSE_STRING = "false";
  public static final String TRUE_STRING = "true";
  public static final String OPEN_TAB_GIF = "btn_opn_d.gif";
  public static final String CLOSE_TAB_GIF = "btn_cls_d.gif";
  public static final String SES_CHOSEN_TAB = "ChosenTab";

//Default Values:
//Header.jsp
  public static final String TAB_LEFT_GIF = "tab_C1_Left_SA.gif";
  public static final String TAB_RIGHT_GIF = "tab_C1_Right_SA.gif";
  public static final String TAB_MID_COLOUR = "#336699";
  public static final String TAB_LEFT_D_GIF = "tab_C1_Left_SA_d.gif";
  public static final String TAB_RIGHT_D_GIF = "tab_C1_Right_SA_d.gif";
  public static final String TAB_MID_D_COLOUR = "#9b9b9b";
  public static final String TAB_TEXT_COLOUR = "#FFFFFF";
  public static final String METADATA_GIF = "metadata.gif";
  public static final String LOGOUT_GIF = "library_logoutH.gif";
  public static final String HELP_GIF = "help.gif";
  public static final String LOGOUT_D_GIF = "library_logoutD.gif";
  public static final String FILENET_LOGO_GIF = "filenetlogo.gif";
  public static final String CHANGE_PWD_D_GIF = "library_ChangePasswordD.gif";
  public static final String CHANGE_PWD_H_GIF = "library_ChangePasswordh.gif";

//1. ISRA-Enterprise version =Phase2 Interaction; ISRA-View version=ONLY Phase1 interaction
  public static final String RA_VERSION = "RA_VERSION";
  public static final String VERSION_VIEW = "VIEW";
  public static final String VERSION_ENTERPRISE = "ENTERPRISE";

//Session Variables - Control Names for each screen
  //Login
  public static final String USER_NAME = "userName"; //Control Name
  public static final String PASSWORD = "password"; //Control Name
  public static final String LIBRARY_NAME = "libraryName"; //Control Name
  public static final String LOOKUP_NAME = "LOOKUP_NAME"; //not used any where
  public static final String LIBRARY_NAMES = "LIBRARY_NAMES"; //Application Parameters
  public static final String TEMP_DIR = "TEMP_DIR"; //Session variable being set once but not used anywhere
  public static final String TEMP_SESSION_DIR = "TEMP_SESSION_DIR"; //Session variable
  public static final String TEMP_DATA_DIR = "TEMP_DATA_DIR"; //Session variable
  public static final String RA_RESOURCE_WARNINGS = "RAResourceWarnings"; //Session variable
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String STATUS_MESSAGE = "statusMessage";

  //1 findDocuments
  public static final String QUERY = "query"; //Control Name
  public static final String FOLDER_NAME = "folderName"; //Control Name
  public static final String MAX_ROWS = "maxRows"; //Control Name
  public static final String SEARCH_SUBMIT = "executeQuerySubmit"; //Control Name
  public static final String SEARCH_RESULTSET = "SEARCHRESULTSET"; //not used
  public static final String SEARCH_START_ROW = "SEARCHSTARTROW"; //not used
  public static final String SEARCH_FETCH_SIZE = "SEARCHFETCHSIZE"; //session variable
  public static final String SEARCH_NEXT = "SearchNext"; //session variable
  public static final String SEARCH_TOTAL_ROWS = "TotalRows"; //session variable
  public static final String PAGE_COUNT = "PageCount";
  public static final String PAGE_COUNTER = "PageCounter";
  public static final String CHECKBOX_SELECT = "Select";
  public static final String DOC_FOLDERS_DOC_ID = "DocID";
  public static final String NEXT_SET = "NEXTSET";
  public static final String FOLDER_NAME_BROWSE = "folderNameBrowse";
  //Code added for WAS6 changes
  public static final String SEARCH_RESULTS_FLAG = "searchResultsFlag";
  //Code added ENDS

  //2 getDocumentContents
  public static final String DOC_ID = "docId"; //docId Control Name
  public static final String DOCID = "docid"; //docId Control Name
  public static final String PAGE_NUMBER = "PageNumber"; //Control Name
  public static final String PAGE_CACHE = "PageCache"; //Control Name
  public static final String PREFETCH_COUNT = "PrefetchCount"; //Control Name
  public static final String POLLING_INTERVAL = "PollingInterval"; //Control Name
  public static final String PAGE = "page"; //Control Name
  public static final String GET_DOCUMENT_CONTENT_SUBMIT =
      "getDocContentSubmit"; //Control Name

  //3 getDocClassIndices
  public static final String DOC_CLASS_NAME = "docClassName"; //Control Name
  public static final String GET_DOC_CLASS_INDICES_SUBMIT =
      "getDocClassIndicesSubmit"; //Control Name
  public static final String INDEX_VALUE = "Index Value";

  //4 getMenuValue
  public static final String INDEX_NAME = "indexName"; //Control Name
  public static final String MENU_LABEL = "menuLabel"; //Control Name
  public static final String GET_MENU_VALUE_SUBMIT = "getMenuValueSubmit"; //Control Name

  //5 getDocumentProperties
  public static final String LOCK_PROPERTIES_FOR_UPDATE = "lockProperty"; //Control Name
  public static final String GET_DOC_PROPERTIES_SUBMIT =
      "GetDocPropertiesSubmit"; //Control Name

  //8 AddDoc
  public static final String DOC_CLASS_PROPERTIES = "docClassProperties"; //Control Name
  public static final String F_DOCFORMAT = "F_DOCFORMAT"; //Control Name
  public static final String DOCUMENT_TYPE = "docType"; //Control Name
  public static final String DOC_CLASS = "docClass"; //Control Name
  public static final String DOC_FAMILY_NAME = "docFamilyName"; //Control Name
  public static final String IS_DUPLICATION_OK = "isDuplicationOK"; //Control Name
  public static final String SEC_READ = "secRead"; //not used
  public static final String SEC_WRITE = "secWrite"; //not used
  public static final String SEC_APPEND_EXECUTE = "secAppendExecute"; //not used
  public static final String DOCUMENT_PROPERTIES = "documentProperties"; //not used
  public static final String FOLDER_NAMES = "folderNames"; //Control Name
  public static final String CACHE_NAME = "cacheName"; //not used
  public static final String FILES_NAMES = "fileNames"; //not used

  public static final String UPLOAD_FILE_NAMES = "uploadfilenames"; //Control Name
  public static final String UPLOAD_TMP_FILE_NAMES = "uploadtmpfilenames"; //Control Name
  public static final String CONTENT_TYPE = "contenttype"; //Session variable
  public static final String ADD_DOC_SUBMIT = "AddDocSubmit"; //Control Name
  public static final String GET_DOC_CLASS_PROPERTIES_SUBMIT =
      "GetDocClassPropertiesSubmit"; //Control Name
  public static final String UPLOAD_DOC_SUBMIT = "UploadDocSubmit"; //Control Name

  public static final String DIRECTORY = "directory"; //not used
  public static final String INDEX = "INDEX";
  public static final String PROPS_COUNT = "PropsCount";

  //9 DeleteDocs
  public static final String DELETE_DOC_SUBMIT = "DeleteDocSubmit"; //Control Name
  public static final String ROW_COUNT = "rowCount";

  //10 FilesDocInFolder
  public static final String DOC_IDS = "docIDs"; //Control Name
  public static final String DOC_ID_AFTER = "docIdAfter"; //Control Name
  public static final String FILE_DOC_IN_FOLDER_SUBMIT =
      "FilesDocInFolderSubmit"; //Control Name
  public static final String FILE_DOC_IN_FOLDER_FLAG = "FileDocInFolderFlag";
  public static final String MAX_ROWS_100 = "100"; //Control Name
  public static final String CURRENT_FOLDER = "currentFolder"; //Control Name

  //11 getDocFolder
  public static final String GET_DOC_FOLDERS_SUBMIT = "GetDocFoldersSubmit"; //Control Name
  public static final String GET_DOC_FOLDERS_FLAG = "GetDocFoldersFlag";

  //12 getFoldersFolders/Browse
  // public static final String GET_FOLDER_FOLDERS_SUBMIT= "GetFolderFoldersSubmit"; //Control Name
  public static final String ADD_DOC_GIF = "doc_addH.gif"; //Image
  public static final String FOLDER_GIF = "Folder.gif"; //Image
  public static final String SHADOW_SEPERATOR_GIF = "ShadowSeperator.gif"; //Image
  public static final String SPACER_GIF = "spacer.gif"; //Image
  public static final String DOC_DELETE_GIF = "doc_deleteh.gif"; //Image
  public static final String DOC_UNFILE_GIF = "doc_unfileh.gif"; //Image
  public static final String DOC_FILE_GIF = "doc_fileh.gif"; //Image
  public static final String DOC_ANNOTATED_GIF = "Doc_annotated.gif"; //Image
  public static final String DOC_GIF = "Doc.gif"; //Image
  public static final String PROP_GIF = "prop.gif"; //Image
  public static final String DOC_FOLDER_GIF = "doc_folderH.gif"; //Image
  public static final String DOC_DELETE_D_GIF = "doc_deleted.gif"; //Image
  public static final String DOC_UNFILE_D_GIF = "doc_unfiled.gif"; //Image
  public static final String DOC_FILE_D_GIF = "doc_filed.gif"; //Image
  public static final String FOLDER_ROW_COUNT = "FolderRowCount";
  public static final String FOLDER_PROPERTIES_GIF = "folder_propertiesH.gif";
  public static final String FOLDER_SELECT_CHECKBOX = "FolderSelect";

  //13 RemoveDocFromFolders
  public static final String REMOVE_DOCS_FROM_FOLDER_SUBMIT =
      "RemoveDocsFromFolderSubmit"; //Control Name

  //14 CancelDocPropertiesUpdate
  public static final String CANCEL_DOC_PROPERTIES_UPDATE_SUBMIT =
      "CancelDocPropertiesUpdateSubmit"; //Control Name

  //15 Update doc properties
  public static final String UPDATE_DOC_PROPERTIES_SUBMIT =
      "UpdateDocPropertiesSubmit"; //Control Name

  //16 Queue Maintenance
  public static final String WORKSPACE_NAMES_LST = "lstSelectWorkSpaceDropDown"; //Control Name
  public static final String QUEUE_NAMES_LST = "lstSelectQueueDropDown"; //Control Name
  public static final String SHOW_QUEUES_SUBMIT = "showQueuesSubmit"; //Control Name

  public static final String SHOW_QUEUE_ENTRY_SYSTEM_FIELDS_CHK =
      "chkShowSystemField"; //Control Name
  public static final String QUEUE_ENTRY_MAX_ROWS_TXT = "txtEnterMaxRows"; //Control Name
  public static final String DISPLAY_QUEUE_ENTRIES_IMG =
      "imgDisplayQueueEntries"; //Control Name

  //Create Queue
  public static final String WORKSPACE_NAME = "WorkspaceName"; //Control Name
  public static final String QUEUE_NAME = "QueueName"; //Control Name
  public static final String PARAMETER_DEFINITION = "ParameterDefinition"; //Control Name
  public static final String CONTENT_DEFINITION = "ContentDefinition"; //Control Name
  public static final String QUEUE_DESCRIPTION = "QueueDescription"; //Control Name
  public static final String FIELD_NAME = "fieldName"; //Control Name
  public static final String FIELD_TYPE = "fieldType"; //Control Name
  public static final String FIELD_LENGTH = "fieldLength"; //Control Name
  public static final String UNIQUE_FIELD = "unique"; //Control Name
  public static final String REQUIRED_FIELD = "isRequired"; //Control Name
  public static final String RENDEZVOUS_FIELD = "isRendezvous"; //Control Name
  public static final String DISPLAY_FIELD = "canBeDisplayed"; //Control Name

  public static final String SECURITY_DEFINITION = "SecurityDefinition"; //Control Name

  //To perform Insert Operation:
  public static final String INSERT_QUEUE_ENTRY_IMG = "imgInsertQueueEntry"; //Control Name

  //To perform Update Operation:
  public static final String UPDATE_QUEUE_ENTRY_IMG = "imgUpdateQueueEntry"; //Control Name

  //To perform Delete Operation:
  public static final String DELETE_QUEUE_ENTRIES_IMG = "imgDeleteQueueEntries"; //Control Name

  //17 Queue Entry Maintenance
  public static final String SAVE_GIF = "save.gif";
  public static final String CANCEL_GIF = "cancelH.gif";
  public static final String SELECTED_ROW = "selectedRow";
  public static final String BROWSER_TYPE = "browserType";
  public static final String QUEUE_ENTRY_ID = "queue_entry_id";
  public static final String QUEUE_ENTRY_COUNT = "count";
  public static final String INSER_UPDATE_QUEUE_ENTRY_OK_IMG =
      "imgInsertUpdateQueueEntryOk"; //Control Name
  public static final String INSER_UPDATE_QUEUE_ENTRY_CANCEL_IMG =
      "imgInsertUpdateQueueEntryCANCEL"; //Control Name
  public static final String WORKSPACE = "workspace";
  public static final String WORKSPACE_DESCRIPTION = "WorkspaceDescription";
  //queues
  public static final String QUEUES_MAINTENANCE_HANDLER = "QueuesMaint";
  public static final String INSERT_UPDATE_QUEUE_ENTRY_HANDLER =
      "insertUpdateQueueEntry";
  public static final String DELETE_QUEUE_ENTRIES_HANDLER =
      "DeleteQueueEntries";
  public static final String INSERT_UPDATE_QUEUE_ENTRY_SERVLET_HANDLER =
      "InsertUpdateQueueEntryServlet";

  //Define the action commands for the request handlers
  public static final String SES_FOLDER_NAME = "sesFolderName";
  public static final String METADATA_HANDLER = "MetaData";

  //QUEUE SYSTEM FIELDS
  public static final String[] QUEUE_SYSTEM_FIELDS = {
      "F_PRIORITY", "F_BUSY",
      "F_DELAYTIME", "F_ENTRYTIME", "F_TIMEOUT",
      "F_USER_NAME", "F_GROUP_NAME"};
  public static final String[] QUEUE_SYSTEM_DATA_TYPE = {
      "20", "10", "3", "3", "3", "2", "2"};
  public static final String[] QUEUE_SYSTEM_DATA_TYPE_NAME = {
      "Integer", "Boolean", "3", "3", "3", "String", "String"};

  public static final String PRINT_DOCS_SUBMIT = "PrintDocsSubmit";
  public static final String FAX_DOCS_SUBMIT = "FaxDocsSubmit";
  /*Code Added For DTS 149561*/
  public static final String PRINT_SERVICE = "PrintService";
  /*Code Added Ends For DTS 149561*/

  public static final String CONTROLLER = "controller";
  public static final String LOGIN_PAGE_HANDLER = "login";
  public static final String LOGOFF_PAGE_HANDLER = "logoff";
  public static final String WELCOME_PAGE_HANDLER = "welcome";
  public static final String MAIN_PAGE_HANDLER = "mainPage";
  //Code added for WAS6 changes
  public static final String MAIN_PAGE_WAS_HANDLER = "mainPageWAS";
  //Code added ENDS

  public static final String SEARCH_HANDLER = "findDocuments";
  public static final String SEARCH_RESULTS_HANDLER = "findDocResults";
  //Code added for WAS6 changes
  public static final String SEARCH_WAS_HANDLER = "findDocumentsWAS";
  public static final String SEARCH_RESULTS_WAS_HANDLER = "findDocResultsWAS";
  //Code added ENDS

  public static final String SEARCH_PROPERTIES = "findDocProperties";
  public static final String GET_DOC_PROPERTIES_RESULTS_HANDLER =
      "getDocPropertiesResults";
  public static final String GET_DOCUMENT_CONTENT_RESULTS_HANDLER =
      "getDocumentContentResults";
  public static final String GET_ANNOTATIONS_RESULTS_HANDLER =
      "getAnnotationsResults";
  public static final String SAVE_ANNOTATIONS_RESULTS_HANDLER =
      "saveAnnotationsResults";
  public static final String USER_GROUP_LIST_RESULTS_HANDLER =
      "userGroupListResults";
  public static final String GET_DOCUMENT_CONTENT_HANDLER =
      "getDocumentContent";
  public static final String GET_DOCUMENT_CONTENT_VIEWER =
      "getDocumentContentViewer";
  public static final String GET_DOC_CLASS_INDICES_HANDLER =
      "getDocClassIndices";
  public static final String GET_DOC_CLASS_INDICES_RESULTS_HANDLER =
      "getDocClassIndicesResults";
  public static final String GET_MENU_VALUE_HANDLER = "getMenuValue";
  public static final String GET_MENU_VALUE_RESULTS_HANDLER =
      "getMenuValueResults";
  public static final String ADD_DOC_HANDLER = "adddoc";
  public static final String UPLOAD_DOC_HANDLER = "uploaddoc";
  public static final String ADD_DOC_DOC_CLASS_PROPERTIES_RESULT_HANDLER =
      "AddDocDocClassProperties";
  public static final String UPLOAD_DOC_RESULT_HANDLER =
      "UploadDocResultHandler";
  public static final String ADD_DOC_RESULT_HANDLER = "adddocresults";
  public static final String DELETE_DOC_RESULT_HANDLER = "deletedocsresult";
  public static final String FILE_DOCS_IN_FOLDER_HANDLER = "filedocsinfolder";
  public static final String FILE_DOCS_IN_FOLDER_RESULT_HANDLER =
      "filedocsinfolderresult";
  public static final String GET_DOC_FOLDERS_RESULT_HANDLER =
      "getdocfoldersresult";
  public static final String BROWSE_RESULT_HANDLER = "browseResult";
  public static final String REMOVE_DOCS_FROM_FOLDER_RESULT_HANDLER =
      "removedocsfromfolderresult";
  public static final String CANCEL_DOC_PROPERTIES_RESULT_HANDLER =
      "CancelDocPropertiesUpdateResults";
  public static final String UPDATE_DOC_PROPERTIES_RESULT_HANDLER =
      "UpdateDocPropertiesResults";
  public static final String GET_FOLDER_ATTRIBUTES_HANDLER =
      "getFolderAttributes";
  public static final String GET_SUPPORTED_INTERACTIONS_HANDLER=
      "getSupportedInteractions";
  public static final String GET_FOLDER_ATTRIBUTES_RESULT_HANDLER =
      "getFolderAttributesResults";
  public static final String CREATE_WORKSPACE_HANDLER = "createWorkspace";
  public static final String CREATE_WORKSPACE_RESULT_HANDLER =
      "createWorkspaceServlet";
  public static final String CREATE_QUEUE_HANDLER = "createQueue";
  public static final String CREATE_QUEUE_RESULT_HANDLER = "createQueueServlet";
  public static final String PRINT_HANDLER = "printDocuments";
  public static final String FAX_HANDLER = "faxDocuments";
  public static final String PRINT_FAX_RESULT_HANDLER =
      "printfaxdocumentresults";
  public static final String ADD_FOLDER_HANDLER = "addfolder";
  public static final String DELETE_FOLDER_HANDLER = "deletefolder";
  public static final String UPDATE_FOLDER_HANDLER = "updatefolder";
  public static final String ADD_FOLDER_RESULT_HANDLER = "addfolderresults";
  public static final String DELETE_FOLDER_RESULT_HANDLER =
      "deleteFolderResult";
  public static final String UPDATE_FOLDER_RESULT_HANDLER =
      "updateFolderResult";
  public static final String GET_CACHE_LIST_HANDLER = "getCacheList";
  public static final String GET_MULTI_PAGE_DOC_HANDLER =
      "getMultiPageDocContent";
  public static final String GET_DOCUMENT_CONTENT_VIEWER2 =
      "getDocumentContentViewer2";
  public static final String GET_DOCUMENT_CONTENT_RESULTS_HANDLER2 =
      "getDocumentContentResults2";
  public static final String GET_DOCUMENT_STATUS_HANDLER =
      "getDocumentStatus";
  public static final String FORWARD = "forward";

  //Code for F_CLOSED
  public static final String DOCUMENT_STATUS_VIEWER = "documentStatusViewer";
  public static final String DOCUMENT_STATUS_RESULT_HANDLER = "documentStatusResult";

  /**Key to retrieve Folder Attributes from MappedRecord.*/
  //public static final String FOLDER_NAME="folder_name";
  public static final String F_FOLDER_ID = "folder_id";
  public static final String F_IS_LEAF = "is_leaf";
  public static final String F_IS_NON_LEAF = "is_nonleaf";
  public static final String F_IS_CLOSED = "is_closed";
  public static final String F_SEC_READ = "sec_read";
  public static final String F_SEC_WRITE = "sec_write";
  public static final String F_SEC_APPEND_EXECUTE = "sec_append_execute";
  public static final String F_DATE_CREATE = "date_create";
  public static final String F_DATE_ARCHIVE = "date_archive";
  public static final String F_DATE_DELETE = "date_delete";
  public static final String F_AUTO_DEL_PERIOD = "auto_del_period";
  public static final String F_RETENT_BASE = "retent_base";
  public static final String F_RETENT_OFFSET = "retent_offset";
  public static final String F_RETENT_DISPOSITION = "retent_disposition";

  /*Constants for Change Password Interaction*/
  public static final String CHANGE_PASSWORD_HANDLER = "changePassword";
  // code added for  DTS  169459
  public static final String CHANGE_PASSWORD_HANDLER_WL = "changePasswordwl";
  //code added for  DTS  169459 ends
  public static final String CHANGE_PASSWORD_RESULT_HANDLER =
      "changePasswordServlet";
  public static final String OLD_PASSWORD = "oldPassword";
  public static final String NEW_PASSWORD = "newPassword";
  public static final String CONFIRM_NEW_PASSWORD = "confirmNewPassword";

  /* Constants for Enable JMS Logging */
  public static final String ENABLE_LOGGING_HANDLER = "enableJMSLogging";

  public static final String PRINT_DOCS = "printdocs";
  public static final String PRI_DOC_IDS = "docIds";
  public static final String PRINT_PAGES = "PrintPages";
  public static final String ISFAX = "Fax";
  public static final String PAPER = "Paper";
  public static final String PRIORITY = "Priority";
  public static final String PRINTER_NAME = "PrinterName";
  public static final String COLLATE = "Collate";
  public static final String ACCESS_RESTRICTIONS = "AccessRestrictions";
  public static final String COPIES = "Copies";
  public static final String PRINT_TIME = "PrintTime";
  public static final String STAPLE = "Staple";
  public static final String TWOSIDED = "TwoSided";
  public static final String FORM = "Form";
  public static final String NOTE = "Note";
  public static final String ANNOTATIONS = "Annotations";
  public static final String REQUEST_HEADER = "RequestHeader";
  public static final String DOC_HEADER = "DocHeader";
  public static final String SCALING = "Scaling";
  public static final String ORIENTATION = "Orientation";
  public static final String PHONE_NUMBER = "PhoneNumber";
  public static final String HEADLINE_MESSAGE = "HeadlineMessage";

  /* Constants added for ISRA 340 Requirement From/To Requirement. */
  public static final String TO_MESSAGE = "ToMessage";
  public static final String FROM_MESSAGE = "FromMessage";
  public static final String SUBJECT_MESSAGE = "SubjectMessage";
  /* Constants added for ISRA 340 Requiement From/To Requirement ends. */

  public static final String FAX_MODE = "FaxMode";
  public static final String PAGE_FOOTNOTE = "PageFootnote";
  public static final String TIME_FOOTNOTE = "TimeFootnote";
  public static final String FIRSTPAGE = "FirstPage";
  public static final String LASTPAGE = "LastPage";
  public static final String PAPER_SUPPORTED = "PaperSupported";
  public static final String OVERLAY = "Overlay";
  public static final String MEMO = "Memo";
  public static final String PHONE_EXTN = "PhoneExtn";
  public static final String COVER_DOC = "CoverDoc";
  public static final String COVER_SSN = "CoverSsn";
  public static final String EJECT_TRAY = "EjectTrays";
  public static final String X_POSITION = "overlay_xPos";
  public static final String Y_POSITION = "overlay_yPos";
  public static final String OVERLAY_UNITS = "overlay_units";
  public static final String OVERLAY_TEXT = "overlay_text";

  //paper sizes
  public static final String STR_UNKNOWN = "UNKNOWN";
  public static final String STR_LETTER = "LETTER";
  public static final String STR_LEGAL = "LEGAL";
  public static final String STR_B = "B";
  public static final String STR_C = "C";
  public static final String STR_D = "D";
  public static final String STR_E = "E";
  public static final String STR_A0 = "A0";
  public static final String STR_A1 = "A1";
  public static final String STR_A2 = "A2";
  public static final String STR_A3 = "A3";
  public static final String STR_A4 = "A4";
  public static final String STR_A5 = "A5";
  public static final String STR_B4 = "B4";
  public static final String STR_B5 = "B5";
  public static final String STR_X18X24 = "x18x24";
  public static final String STR_TOP = "TOP";
  public static final String STR_BOTTOM = "BOTTOM";
  public static final String STR_THIRD = "THIRD";
  public static final String STR_DONT_CARE = "DON'T CARE";
  public static final String STR_HALF_LETTER = "HALF_LETTER";
  public static final String STR_BEST_AVAIL = "BEST_AVAIL";
  public static final String STR_X10X14 = "x10x14";
  public static final String STR_GLOBAL_DEFAULT = "GLOBAL_DEFAULT";
  public static final String STR_EXECUTIVE = "EXECUTIVE";

  public static final int UNKNOWN = 0;
  public static final int LETTER = 1;
  public static final int LEGAL = 2;
  public static final int B = 3;
  public static final int C = 4;
  public static final int D = 5;
  public static final int E = 6;
  public static final int A0 = 7;
  public static final int A1 = 8;
  public static final int A2 = 9;
  public static final int A3 = 10;
  public static final int A4 = 11;
  public static final int A5 = 12;
  public static final int B4 = 13;
  public static final int B5 = 14;
  public static final int X18X24 = 15;
  public static final int TOP = 16;
  public static final int BOTTOM = 17;
  public static final int THIRD = 18;
  public static final int DONT_CARE = 19;
  public static final int HALF_LETTER = 20;
  public static final int BEST_AVAIL = 21;
  public static final int X10X14 = 22;
  public static final int GLOBAL_DEFAULT = 23;
  public static final int EXECUTIVE = 24;

  //printdocs
  public static final String DOC_SPECS_LIST = "DocSpecificationList";
  public static final String PRI_DOC_ID = "doc_id";
  public static final String FIRST_PAGE = "FirstPage";
  public static final String LAST_PAGE = "LastPage";
  public static final String SERVICE_SOURCE_CHOICE = "ServiceSourceChoice";

  //ENABLE JMS Logging.
  public static final String JMS_LOGGING_VALUE = "jmsLoggingValue";
  public static final String PERFORMANCE_LOGGING = "Islogging_required";

  //checksum
  public static final String ENABLE_CHECKSUM = "enableChecksum";

  //Create Queue constants
  public static final String LABEL_ANYONE = "(ANYONE)";
  public static final String LABEL_NONE = "(NONE)";
  public static final String LABEL_READ = "(Read)";
  public static final String LABEL_WRITE = "(Write)";
  public static final String LABEL_APPEND_EXECUTE = "(Append/Execute)";
  public static final String FIELD_TYPE_NUMBER = "Number";
  public static final String FIELD_TYPE_STRING = "String";
  public static final String FIELD_TYPE_TIME = "Time";
  public static final String FIELD_TYPE_SELECTION = "Selection";
  public static final String FIELD_TYPE_DOCUMENT = "Document";
  public static final String FIELD_TYPE_FOLDER = "Folder";
  public static final String FIELD_TYPE_INTEGER = "Integer";
  public static final String FIELD_TYPE_DATE = "Date";
  public static final String FIELD_TYPE_BOOLEAN = "Boolean";
  public static final String FIELD_TYPE_DECIMAL = "Decimal";
  //Commented for DTS180654
  //Undo code comment FOR DTS 185849
  public static final String FIELD_TYPE_NULL = "Null";

  public static final String UNIQUE_FIELD_NOT_INVERT = "Not Invert";
  public static final String UNIQUE_FIELD_INVERT = "Invert";
  public static final String UNIQUE_FIELD_UNIQUE_INVERT = "Unique Invert";

  //Delete Folders
  public static final String FOLDER_NAME_LBL = "FolderName";
  public static final String SUB_FOLDER_LBL = "SubFolder";
  public static final String UNFILE_DOCS_LBL = "UnFile";

  //Add Folder
  public static final String ADD_FOLDER_GIF = "folder_addH.gif";
  public static final String FOLDER_NAME_TEXTBOX = "folder_name";
  public static final String FOLDER_SEC_CHKBOX = "FOLDER_SEC_VALUE";
  public static final String FOLDER_AUTO_DEL_PERIOD_TEXTBOX =
      "folder_auto_del_period";
  public static final String FOLDER_RETENT_BASE_TEXTBOX = "folder_retent_base";
  public static final String FOLDER_RETENT_OFFSET_TEXTBOX =
      "folder_retent_offset";
  public static final String FOLDER_RETENT_DISP_TEXTBOX =
      "folder_retent_disposition";

  //Update Folder
  public static final String FOLDER_ID_TEXTBOX = "folderId";
  public static final String FOLDER_IS_LEAF_TEXTBOX = "folderIsLeaf";
  public static final String FOLDER_IS_NON_LEAF_TEXTBOX = "folderIsNonLeaf";
  public static final String FOLDER_IS_CLOSED_TEXTBOX = "folderClosed";
  public static final String FOLDER_CREATE_DATE_TEXTBOX = "folderCreationDate";
  public static final String FOLDER_ARCHIVE_DATE_TEXTBOX = "folderArchiveDate";
  public static final String FOLDER_DELETE_DATE_TEXTBOX = "folderDeleteDate";
  public static final String FOLDER_AUTO_DEL_TEXTBOX = "folderAutoDelPeriod";
  public static final String FOLDER_BASE_RETENT_TEXTBOX = "folderRetentBase";
  public static final String FOLDER_OFFSET_RETENT_TEXTBOX =
      "folderRetentOffset";
  public static final String FOLDER_DISP_RETENT_TEXTBOX =
      "folderRetentDisposition";

  //Zurich Annotations
  public static final String DIGITAL_SIGNATURE_RESULT_HANDLER =
      "digitalsignaturesresults";
  public static final String GET_DOCUMENT_CONTENT_IE_VIEWER =
      "getDocumentContentIEViewer";
  public static final String SUBMIT_SIGNATURE_RESULT_HANDLER =
      "submitDigitalsignaturesresults";
  public static final String DIGITAL_SIGNATURE_DOCCLASS_NAME = "DOCCLASSNAME";
  public static final String DIGITAL_SIGNATURE_MAX_ROWS = "MAXROWS";
  //Remote Printing of Zurich Annotations:
  public static final String ELEMENT_PROP_DESC = "PropDesc";
  public static final String ATTR_STATE = "STATE";
  public static final String ATTR_STATE_ADD = "add";
  public static final String ATTR_STATE_CHANGE = "change";
  public static final String ATTR_CLASSNAME = "F_CLASSNAME";
  public static final String ATTR_SUB_CLASSNAME = "F_SUBCLASS";
  public static final String ATTR_F_VIEWOPTION = "F_VIEWOPTION";
  public static final String ATTR_F_CLASSID = "F_CLASSID";

  public static final String ATTR_VALUE_PROPRIETARY = "Proprietary";
  public static final String ATTR_VALUE_V1_NOTE = "v1-Note";
  public static final String ATTR_VALUE_STICKY_NOTE = "StickyNote";
  public static final String ATTR_VALUE_CLASSID_PROPRIETARY =
      "{A91E5DF2-6B7B-11D1-B6D7-00609705F027}";
  public static final String ATTR_VALUE_CLASSID_STICKY_NOTE =
      "{5CF11945-018F-11D0-A87A-00A0246922A5}";

  public static final String XSD_XML_HEADER =
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

  //DocumentStatus
  public static final String DOC_STATUS_LABEL = "docStatus";
  public static final String RADIO_BUTTON = "RadioButton";
  public static final String ARCHIVE = "Archive";
  public static final String CLOSED = "Closed";
  public static final String UPDATE_STATUS = "UpdateStatus";

  //GetDocumentContent2
  public static final String MULTI_PAGE_DOC_CONTENT_GIF = "multiPage.gif";
  public static final String DOCID_TEXTBOX = "docId";
  public static final String PAGE_NUMBER_TEXTBOX = "pageNum";
  public static final String LAST_PAGE_NUMBER_TEXTBOX = "lastPageNum";
  public static final String PAGE_CACHE_TEXTBOX = "pageCache";
  public static final String POLLING_INTERVAL_TEXTBOX = "pollingInterval";
  public static final String NO_OF_TIMES_OBJECT_TEXTBOX = "noOfTimesObject";
  public static final String PREFETCH_COUNT_TEXTBOX = "prefetchCount";
  public static final String LAST_PAGE_NUMBER = "lastPageNum";
  //Code added for DTS 164740
  public static final String ISRA_ANONYMOUS_USER = "LDAP/ISRA_ANONYMOUS_USER";
  //Code added ends for DTS 164740'

  //Code added for Robustification DTS 173095
  public static final String WQS_SERVICE_NAME = "WQSServiceName";
  //Code added ends for Robustification DTS 173095

  //Code Added for DTS 180479 - JAAS Authentication Realm
	public static final String JAAS_FLAG = "jaasFlag";
	public static final String JAAS_ENABLE = "JAAS_ENABLE";
	public static final String MODULE_NAME = "MODULE_NAME";
	public static final String CBHANDLER_NAME = "CBHANDLER_NAME";
  //Code Added for DTS 180479 - JAAS Authentication Realm Ends

  private static final HashMap mActionCommand_HandlerMap;
//Prepare HashMap for URL's
  // Put the action command as the key and the corresponding RequestHandler as the value.
  static {
    mActionCommand_HandlerMap = new HashMap();
    mActionCommand_HandlerMap.put(CONTROLLER, "/Dispatcher");
    mActionCommand_HandlerMap.put(WELCOME_PAGE_HANDLER, "/Login.jsp");
    mActionCommand_HandlerMap.put(LOGIN_PAGE_HANDLER, "/LoginServlet");
    mActionCommand_HandlerMap.put(LOGOFF_PAGE_HANDLER, "/LogOffServlet");
    mActionCommand_HandlerMap.put(MAIN_PAGE_HANDLER, "/web/Search.jsp");
    //Code added for WAS6 changes
    mActionCommand_HandlerMap.put(MAIN_PAGE_WAS_HANDLER, "/web/SearchWAS.jsp");
    //Code added ENDS

    mActionCommand_HandlerMap.put(SEARCH_HANDLER, "/web/Search.jsp");
    mActionCommand_HandlerMap.put(SEARCH_RESULTS_HANDLER,
                                  "/web/SearchResults.jsp");
    //Code added for WAS6 changes
    mActionCommand_HandlerMap.put(SEARCH_WAS_HANDLER, "/web/SearchWAS.jsp");
    mActionCommand_HandlerMap.put(SEARCH_RESULTS_WAS_HANDLER,
                                  "/web/SearchResultsWAS.jsp");
    //Code added ENDS

    mActionCommand_HandlerMap.put(SEARCH_PROPERTIES,
                                  "/web/ViewDocumentProperties.jsp");
    mActionCommand_HandlerMap.put(GET_DOC_PROPERTIES_RESULTS_HANDLER,
                                  "/web/UpdateDocProperties.jsp");
    mActionCommand_HandlerMap.put(GET_DOCUMENT_CONTENT_RESULTS_HANDLER,
                                  "/GetDocumentContentServlet");
    mActionCommand_HandlerMap.put(GET_ANNOTATIONS_RESULTS_HANDLER,
                                  "/GetAnnotationsServlet");
    mActionCommand_HandlerMap.put(SAVE_ANNOTATIONS_RESULTS_HANDLER,
                                  "/SaveAnnotationsServlet");
    mActionCommand_HandlerMap.put(USER_GROUP_LIST_RESULTS_HANDLER,
                                  "/UserGroupListServlet");

    mActionCommand_HandlerMap.put(GET_DOCUMENT_CONTENT_HANDLER,
                                  "/web/GetDocumentContent.jsp");
    mActionCommand_HandlerMap.put(GET_DOCUMENT_CONTENT_VIEWER,
                                  "/web/DisplayDocument.jsp");
    mActionCommand_HandlerMap.put(GET_DOC_CLASS_INDICES_HANDLER,
                                  "/web/GetDocClassIndices.jsp");
    mActionCommand_HandlerMap.put(GET_DOC_CLASS_INDICES_RESULTS_HANDLER,
                                  "/web/GetDocClassIndicesResults.jsp");
    mActionCommand_HandlerMap.put(GET_MENU_VALUE_HANDLER,
                                  "/web/GetMenuValue.jsp");
    mActionCommand_HandlerMap.put(GET_MENU_VALUE_RESULTS_HANDLER,
                                  "/web/GetMenuValueResults.jsp");
    mActionCommand_HandlerMap.put(ADD_DOC_HANDLER, "/web/AddDoc.jsp");
    mActionCommand_HandlerMap.put(UPLOAD_DOC_HANDLER, "/web/UploadDoc.jsp");
    mActionCommand_HandlerMap.put(ADD_DOC_RESULT_HANDLER, "/AddDocServlet");
    mActionCommand_HandlerMap.put(ADD_DOC_DOC_CLASS_PROPERTIES_RESULT_HANDLER,
                                  "/web/DisplayDocClassProperties.jsp");
    mActionCommand_HandlerMap.put(UPLOAD_DOC_RESULT_HANDLER, "/UploadServlet");
    mActionCommand_HandlerMap.put(DELETE_DOC_RESULT_HANDLER,
                                  "/web/DeleteDocs.jsp");
    mActionCommand_HandlerMap.put(FILE_DOCS_IN_FOLDER_HANDLER,
                                  "/web/FileDocsInFolder.jsp");
    mActionCommand_HandlerMap.put(FILE_DOCS_IN_FOLDER_RESULT_HANDLER,
                                  "/web/FileDocsInFolderResults.jsp");
    mActionCommand_HandlerMap.put(GET_DOC_FOLDERS_RESULT_HANDLER,
                                  "/web/DisplayFoldersDocFiledIn.jsp");
    mActionCommand_HandlerMap.put(BROWSE_RESULT_HANDLER, "/web/Browse.jsp");
    mActionCommand_HandlerMap.put(REMOVE_DOCS_FROM_FOLDER_RESULT_HANDLER,
                                  "/web/RemoveDocsFromFolder.jsp");
    mActionCommand_HandlerMap.put(CANCEL_DOC_PROPERTIES_RESULT_HANDLER,
                                  "/web/CancelDocPropUpdate.jsp");
    mActionCommand_HandlerMap.put(UPDATE_DOC_PROPERTIES_RESULT_HANDLER,
                                  "/UpdateDocumentPropertiesServlet");
    mActionCommand_HandlerMap.put(QUEUES_MAINTENANCE_HANDLER,
                                  "/web/QueuesMaint.jsp");
    //queues
    mActionCommand_HandlerMap.put(INSERT_UPDATE_QUEUE_ENTRY_HANDLER,
                                  "/web/InsertUpdateQueueEntry.jsp");
    mActionCommand_HandlerMap.put(DELETE_QUEUE_ENTRIES_HANDLER,
                                  "/web/DeleteQueueEntries.jsp");
    mActionCommand_HandlerMap.put(INSERT_UPDATE_QUEUE_ENTRY_SERVLET_HANDLER,
                                  "/InsertUpdateQueueEntryServlet");
    // password, folder attributes
    mActionCommand_HandlerMap.put(CHANGE_PASSWORD_RESULT_HANDLER,
                                  "/ChangePasswordServlet");
    mActionCommand_HandlerMap.put(CHANGE_PASSWORD_HANDLER,
                                  "/web/ChangePassword.jsp");
    // code added for  DTS  169459
    mActionCommand_HandlerMap.put(CHANGE_PASSWORD_HANDLER_WL,
                                  "/web/ChangePassword.jsp");
    //code added  for  DTS  169459 ends

    mActionCommand_HandlerMap.put(GET_FOLDER_ATTRIBUTES_HANDLER,
                                  "/web/GetFolderAttributes.jsp");
    mActionCommand_HandlerMap.put(GET_SUPPORTED_INTERACTIONS_HANDLER,
            					  "/web/GetSupportedInteractions.jsp");
    mActionCommand_HandlerMap.put(GET_FOLDER_ATTRIBUTES_RESULT_HANDLER,
                                  "/web/GetFolderAttributesResults.jsp");
    mActionCommand_HandlerMap.put(CREATE_WORKSPACE_RESULT_HANDLER,
                                  "/CreateWorkspaceServlet");
    mActionCommand_HandlerMap.put(CREATE_WORKSPACE_HANDLER,
                                  "/web/CreateWorkspace.jsp");
    mActionCommand_HandlerMap.put(CREATE_QUEUE_RESULT_HANDLER,
                                  "/CreateQueueServlet");
    mActionCommand_HandlerMap.put(CREATE_QUEUE_HANDLER, "/web/CreateQueue.jsp");
    mActionCommand_HandlerMap.put(PRINT_FAX_RESULT_HANDLER,
                                  "/PrintFaxDocumentsServlet");
    mActionCommand_HandlerMap.put(PRINT_HANDLER, "/web/PrintFaxDocuments.jsp");
    mActionCommand_HandlerMap.put(FAX_HANDLER, "/web/PrintFaxDocuments.jsp");
    mActionCommand_HandlerMap.put(ENABLE_LOGGING_HANDLER,
                                  "/web/EnableJMSLogging.jsp");
    mActionCommand_HandlerMap.put(ADD_FOLDER_HANDLER, "/web/AddFolder.jsp");
    mActionCommand_HandlerMap.put(UPDATE_FOLDER_HANDLER,
                                  "/web/UpdateFolder.jsp");
    mActionCommand_HandlerMap.put(DELETE_FOLDER_HANDLER,
                                  "/web/DeleteFolder.jsp");
    mActionCommand_HandlerMap.put(ADD_FOLDER_RESULT_HANDLER,
                                  "/AddFolderServlet");
    mActionCommand_HandlerMap.put(DELETE_FOLDER_RESULT_HANDLER,
                                  "/DeleteFolderServlet");
    mActionCommand_HandlerMap.put(UPDATE_FOLDER_RESULT_HANDLER,
                                  "/UpdateFolderServlet");
    mActionCommand_HandlerMap.put(GET_CACHE_LIST_HANDLER,
                                  "/web/GetCacheList.jsp");
    mActionCommand_HandlerMap.put(DIGITAL_SIGNATURE_RESULT_HANDLER,
                                  "/web/GetDigitalSignatures.jsp");
    mActionCommand_HandlerMap.put(SUBMIT_SIGNATURE_RESULT_HANDLER,
                                  "/web/SubmitDigitalSignatures.jsp");
    mActionCommand_HandlerMap.put(GET_DOCUMENT_CONTENT_IE_VIEWER,
                                  "/web/DisplayDocumentIE.jsp");
    mActionCommand_HandlerMap.put(GET_MULTI_PAGE_DOC_HANDLER,
                                  "/web/GetDocumentContent2Parameters.jsp");
    mActionCommand_HandlerMap.put(GET_DOCUMENT_CONTENT_VIEWER2,
                                  "/web/DisplayDocument2.jsp");
    mActionCommand_HandlerMap.put(GET_DOCUMENT_CONTENT_RESULTS_HANDLER2,
                                  "/GetDocumentContentServlet2");
    mActionCommand_HandlerMap.put(FORWARD, "/web/Forward.jsp");

	mActionCommand_HandlerMap.put(GET_DOCUMENT_STATUS_HANDLER, "/web/DocumentStatus.jsp");
    mActionCommand_HandlerMap.put(DOCUMENT_STATUS_VIEWER, "/web/SearchResultsWAS.jsp");
    mActionCommand_HandlerMap.put(DOCUMENT_STATUS_RESULT_HANDLER, "/DocumentStatusServlet");
  }

  //returns the Map object.
  /**
   *	Returns a Map object which contains the action_commands and the request handlers as key-value pairs.
   *  @param None
   *  @return java.util.Map
   *  @throws None
   */
  public static HashMap getWebConstantsMap() {
    return mActionCommand_HandlerMap;
  }

}