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
 */
package com.filenet.is.ra.sa.util;

import java.sql.ResultSet;

/**
 * <code>AppConstants</code> defines all the application level constants that are used by all the
 * classes that comprise the Sample Application. Most of the constants are used as keys when
 * the interaction request data is passed in the form of Record during Interaction.execute() to the RA.
 *
 * @author Ashu Govil, Kiran Kumar, Neha Pandey, Nikhil Bhatia
 * @version 3.0
 */

public final class AppConstants {
	//Constant used in client bean
	
	public static final String DELETE_SPECS_VALUE = "0";
	//This class defines the constants that are used across the whole system.
	//It defines the names using which the data is sent to the Resource Adapter.

    //findDocuments
	public static final String QUERY = "query";
	public static final String FOLDER_NAME = "folder_name";
	public static final String MAX_ROWS = "max_rows";
    public static final String DEFAULT_QUERY ="select F_DOCNUMBER, F_DOCFORMAT, F_PAGES from FnDocument where F_DOCNUMBER > 1";
    public static final String DEFAULT_MAX_ROWS = "16";
    public static final String FINDDOCLOCATION_INPUT_RECORD_NAME = "FindDocLocation";

    public static final String FINDDOCLOCATION_OUTPUT_RECORD_NAME = "DocLocSet";
    public static final String RELATIONAL_OPERATOR ="rel_op";
    public static final String MAX_MATCHES ="max_matches";

	//getDocumentContents
	public static final String DOC_ID = "doc_id";
	public static final String PAGE_NUMBER = "page_number";
	public static final String RESULT_INPUTSTREAM = "result_inputstream";
    public static final String LAST_PAGE_NUMBER = "last_page_number";

	/*Added by harish for get Document Content2 Interaction*/
	public static final String PAGE_CACHE = "page_cache";
	public static final String PREFETCH_COUNT = "prefetch_count";
	public static final String POLLING_INTERVAL = "polling_interval";
    public static final String POLLING_INTERVAL_IN_MILLIS = "polling_interval_ms";
    public static final String STREAM = "stream";
    public static final String NO_OF_TIMES_OBJECT = "no_of_times_to_poll";

	//getDocClassIndices
	public static final String DOCCLASS_NAME = "docclass_name";
	public static final String INDEX_TYPE = "index_type";
    public static final String IS_REQUIRED = "required";

    //getMenuValue
	public static final String INDEX_NAME = "index_name";
	public static final String MENU_LABEL = "menu_label";
	public static final String MENU_VALUE = "menu_value";
    public static final String MENU_ID = "menu_id";

    //getDocumentProperties
	public static final String IS_LOCK_DESIRED = "is_lock_desired";
	public static final String DOC_PROPERTIES = "doc_properties";
	public static final String ITEM_LOCK = "item_lock";
	public static final String INDEX_VALUE = "index_value";

    //AddDoc
	public static final String DOC_CLASS_NAME = "doc_class_name";
	public static final String DOC_TYPE = "doc_type";
	public static final String DOC_FAMILIY_NAME = "doc_family_name";
	public static final String IS_DUPLICATION_OK = "is_duplication_ok";
	public static final String CLUSTER_KEY = "cluster_key";
	public static final String SEC_READ = "sec_read";
	public static final String SEC_WRITE = "sec_write";
	public static final String SEC_APPEND_EXECUTE = "sec_append_execute";
	public static final String CACHE_NAME = "cache_name";
	public static final String INDEX_RECORDS = "index_records";
	public static final String DOC_PAGE_STREAMS = "doc_page_streams";
	public static final String FOLDER_SET = "folder_set";
    public static final String ENABLE_CHECKSUM = "checksum_enabled";

	public static final String RESULT = "result";

	public static final String ERROR_CODE = "error_code";

	public static final String DESTINATION_FOLDER = "destination_folder";
	public static final String DOCUMENTS = "documents";
	public static final String PLACE_AFTER = "place_after";

    public static final String NEW_FCLOSED = "new_fclosed";
	public static final String MAX_LIMIT = "max_limit";
	public static final String BASE_FOLDER_NAME = "base_folder_name";
	public static final String CONTAINED_FOLDERS = "contained_folders";

	public static final String FOLDER_ID = "folder_id";
	public static final String IS_LEAF = "is_leaf";
	public static final String IS_CLOSED = "is_closed";
	public static final String DATE_CREATED = "date_created";
	public static final String DATE_ARCHIVE = "date_archive";
	public static final String DATE_DELETED = "date_deleted";
	public static final String AUTO_DEL_PERIOD = "auto_del_period";
	public static final String RETENT_BASE = "retent_base";
	public static final String RETENT_OFFSET = "retent_offset";
	public static final String RETENT_DISPOSITION = "retent_disposition";

    //Input record for All IS RA Ineteraction
	public static final String FIND_DOCUMENTS = "QueryRequest";
	public static final String GET_DOCUMENT_CONTENT = "DocContentRequest";
	public static final String GET_DOC_CLASS_INDICES = "DocClassName";
	public static final String GET_MENU_VALUE = "MenuValueRequest";

	public static final String GET_DOCUMENT_PROPERTIES = "DocumentPropertiesRequest";
	public static final String ADD_DOCUMENT = "AddDocRequest";
    public static final String ADD_DOCUMENT_FOLDER_SET = "FolderSet";
    public static final String ADD_DOCUMENT_DOC_PROPERTIES = "DocProperties";
    public static final String ADD_DOCUMENT_DOC_PROPERTY = "DocProperty";

	public static final String CANCEL_DOC_PROPERTIES = "DocumentPropertiesReturn";
    public static final String CANCEL_DOC_PROPERTIES_DOC_PROPERTIES = "DocProperties";
    public static final String CANCEL_DOC_PROPERTIES_DOC_PROPERTY = "DocProperty";

	public static final String DELETE_DOCS = "DocSet";
	public static final String FILES_DOC_IN_FOLDER = "DocumentsAndFolder";
    public static final String FILES_DOC_IN_FOLDER_DOC_SET = "DocSet";
	public static final String GET_DOC_FOLDERS = "DocID";
	public static final String GET_FOLDER_FOLDERS = "FolderRequest";
	public static final String REMOVE_DOCS_FROM_FOLDER = "DocumentsAndFolder";
	public static final String REMOVE_DOCS_FROM_FOLDER_DOC_SET = "DocSet";
	public static final String UPDATE_DOC_PROPERTIES = "DocumentPropertiesReturn";
    public static final String UPDATE_DOC_PROPERTIES_DOC_PROPERTIES = "DocProperties";
    public static final String UPDATE_DOC_PROPERTIES_DOC_PROPERTY = "DocProperty";

    public static final String GET_DOC_CLASS_DESCS_RESULT = "DocClassSet";
    
    public static final String GET_DOC_CLASS_DESCS = "DocClassDesc";
    public static final String GET_SECURITY_INFO = "SecurityInputRecord";
    public static final String DOC_CLASS_ID = "doc_class_id";
    public static final String OBJECT_NAME = "object_name";
    public static final String OBJECT_ID = "object_id";
    public static final String DOCUMENTPAGE = "DocumentPage";

    public static final String IndexName = "IndexName";
    public static final String docID = "doc_id";

    //Output record for All IS RA Interaction
	public static final String FIND_DOCUMENTS_RESULT = "QueryResult";
	public static final String GET_DOCUMENT_CONTENT_RESULT = "DocContent";

	public static final String GET_DOC_CLASS_INDICES_RESULT = "DocClassIndex";
	public static final String GET_MENU_VALUE_RESULT = "MenuValueReturn";

    public static final String GET_MENU_DESCS_RESULT = "MenuItemSet";
    public static final String GET_SECURITY_INFO_RESULT = "SecurityInfo";
    public static final String GET_DOC_CLASS_DESCS_FUNCTION_NAME = "GetDocClassDesc";
    public static final String GET_MENU_DESC_FUNCTION_NAME = "GetMenuDesc";
    public static final String GET_SECURITY_INFO_FUNCTION_NAME = "GetSecurityInfo";

	public static final String GET_DOCUMENT_PROPERTIES_RESULT = "DocumentPropertiesReturn";
	public static final String ADD_DOCUMENT_RESULT = "DocID";
	public static final String CANCEL_DOC_PROPERTIES_RESULT = "GenericResult";
	public static final String DELETE_DOCS_RESULT = "DocErrorSet";
	public static final String FILES_DOC_IN_FOLDER_RESULT = "GenericResult";
	public static final String GET_DOC_FOLDERS_RESULT = "FolderSet";
	public static final String GET_FOLDER_FOLDERS_RESULT = "FolderFolders";
	public static final String REMOVE_DOCS_FROM_FOLDER_RESULT = "GenericResult";
	public static final String UPDATE_DOC_PROPERTIES_RESULT = "GenericResult";

	public static final String F_DOCNUMBER = "F_DOCNUMBER";
	public static final String F_DOCCLASSNAME = "F_DOCCLASSNAME";
	public static final String F_PAGES = "F_PAGES";

	//Function names defined in IS RA Interface
	public static final String FIND_DOCUMENTS_FUNCTION_NAME = "FindDocuments";
    public static final String FIND_DOCUMENTS_IN_OSAR_FUNCTION_NAME = "FindDocumentLocation";
	public static final String GET_DOCUMENT_CONTENT_FUNCTION_NAME = "GetDocumentContent";
	
	public static final String GET_DOCUMENT_CONTENT2_FUNCTION_NAME = "GetDocumentContent2";

	public static final String GET_DOC_CLASS_INDICES_FUNCTION_NAME = "GetDocClassIndices";
	public static final String GET_MENU_VALUE_FUNCTION_NAME = "GetMenuValue";

	public static final String ADD_DOC_FUNCTION_NAME = "AddDoc";
	public static final String GET_DOC_PROPERTIES_FUNCTION_NAME = "GetDocProperties";
	public static final String UPDATE_DOC_PROPERTIES_FUNCTION_NAME = "UpdateDocProperties";
	public static final String CANCEL_DOC_PROPERTIES_FUNCTION_NAME = "CancelDocPropertiesUpdate";
	public static final String DELETE_DOC_FUNCTION_NAME = "DeleteDocs";
	public static final String GET_FOLDER_FOLDERS_FUNCTION_NAME = "GetFolderFolders";
	public static final String FILES_DOC_IN_FOLDER_FUNCTION_NAME = "FileDocsInFolder";

	public static final String REMOVE_DOC_FROM_FOLDER_FUNCTION_NAME = "RemoveDocsFromFolder";
	public static final String GET_DOC_FOLDER_FUNCTION_NAME = "GetDocFolders";

	public static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";

	public static final String MANAGED_ENVIRONMENT = "MANAGED";
	public static final String NON_MANAGED_ENVIRONMENT = "NON_MANAGED";
	public static final String NON_MANAGED_JNDI_ENVIRONMENT = "NON_MANAGED_JNDI";

	//This array contains all the system indices except doc number and docclassname.
	public static final String [] SYSTEM_INDICES = {"F_ARCHIVEDATE","F_CLOSED","F_DELETEDATE",
                                                    "F_DOCCLASSNUMBER","F_DOCFORMAT","F_DOCLOCATION",
                                                    "F_DOCTYPE","F_ENTRYDATE","F_RETENTBASE",
                                                    "F_RETENTDISP","F_RETENTOFFSET","F_PAGES","F_ACCESSRIGHTS"};
	
	public static final String FUNCTION_NAME = "functionName";
	public static final String INTERACTION_VERB = "interactionVerb";
	public static final int SYNC_SEND = 0;
	public static final int SYNC_SEND_RECEIVE = 1;
	public static final int SYNC_RECEIVE = 2;

	public static final String EXECUTION_TIMEOUT = "executionTimeout";

	public static final String FETCH_SIZE = "fetchSize";

	public static final String FETCH_DIRECTION = "fetchDirection";
	public static final int FETCH_FORWARD = ResultSet.FETCH_FORWARD;
	public static final int FETCH_REVERSE = ResultSet.FETCH_REVERSE;
	public static final int FETCH_UNKNOWN = ResultSet.FETCH_UNKNOWN;

	public static final String MAX_FIELD_SIZE = "maxFieldSize";

	public static final String RESULTSET_TYPE = "resultSetType";
	public static final int TYPE_FORWARD_ONLY = ResultSet.TYPE_FORWARD_ONLY;
	public static final int TYPE_SCROLL_INSENSITIVE = ResultSet.TYPE_SCROLL_INSENSITIVE;
	public static final int TYPE_SCROLL_SENSITIVE = ResultSet.TYPE_SCROLL_SENSITIVE;

	public static final String CONCURRENCY = "resultSetConcurrency";
	public static final int CONCUR_READ_ONLY = ResultSet.CONCUR_READ_ONLY;
	public static final int CONCUR_UPDATABLE = ResultSet.CONCUR_UPDATABLE;

	public static final String INTERACTION_DATA = "INTERACTION_DATA";
	public static final String INTERACTION_EXECUTION_TIME_TAKEN = "INTERACTION_EXECUTION_TIME_TAKEN";
	public static final String WARNING_MESSAGE = "WARNING_MESSAGE";
	public static final String INTERACTION_EXECUTE_3 = "interactionExecute3";

	public static final String FILE_NAME = "file_name";
	public static final String MIME_TYPE = "mime_type";
	public static final String DOC_CONTENT_BUFFER_PAGES = "buffer_pages";
    public static final String PATH_NAME = "path_name";

	public static final String FIRST_WINDOW_DATA = "firstWindowData";
	public static final String TOTAL_CONTENT_SIZE = "totalContentSize";

    public static final String IndexRecord = "QueueEntry";
    public static final String WORKSPACE_NAME_KEY = "workspace_name";
    public static final String QUEUE_NAME_KEY = "queue_name";
    public static final String SET_BUSY = "set_busy";
    public static final String DELETE_SPEC = "delete_spec";

    public static final String QUEUE_ENTRIES_RECORD = "RecordEntries";
    public static final String QUEUE_ENTRY_ID = "queue_entry_id";
    public static final String GET_QUEUE_ENTRIES = "QueueRequest";
    public static final String UPDATE_QUEUE_ENTRIES = "UpdateQueueEntriesInput";
    public static final String DELETE_QUEUE_ENTRIES = "DeleteQueueEntriesInput";
    public static final String QUEUE_ENTRY_RESULT_MAP_RECORD = "queue_entry_result";
    public static final String GET_QUEUE_NAMES_RESULT = "Queues";
    public static final String GET_QUEUE_FIELDS_RESULT = "get_queue_fields_result";
    public static final String GET_WORKSPACE_NAMES_RESULT = "get_workspace_names_result";
    public static final String SELECTED_QUEUE_ROW_NO = "row_number";

    public static final String DELETE_QUEUE_ENTRIES_MAP_RECORD = "QueueEntryIDSet";
    public static final String UPDATE_QUEUE_ENTRY_INDEXED_RECORD = "queue_field";
    public static final String UPDATE_QUEUE_ENTRIES_MAP_RECORD = "QueueIDEntry";
    public static final String INSERT_QUEUE_ENTRIES_MAP_RECORD = "QueueInsertRequest";
    public static final String EACH_QUEUE_ENTRIES_INDEXED_RECORD ="each_queue_entries";
    public static final String INSERT_QUEUE_ENTRIES_INDEXED_RECORD = "final_queue_entries";
    public static final String QUEUE_ENTRIES_KEY = "queue_entries";
    public static final String QUEUE_ENTRIES_UPDATE_KEY = "queue_field";

    public static final String QUEUE_FIELD_NAME_KEY = "field_name";
    public static final String QUEUE_FIELD_VALUE_KEY = "field_value";
    public static final String QUEUE_FIELD_TYPE_KEY = "field_type";
    public static final String QUEUE_FIELD_LENGTH_KEY = "field_length";
    public static final String QUEUE_FIELD_KEY_TYPE_KEY = "key_type";
    public static final String QUEUE_FIELD_IS_REQUIRED_KEY = "is_required";
    public static final String QUEUE_FIELD_IS_RENDEVOUS_KEY = "is_rendevous";
    public static final String QUEUE_FIELD_CAN_BE_DISPLAYED_KEY = "can_be_displayed";

    public static final String QUEUE_FIELDS_MAP_RECORD = "queue_fields";
    public static final String QUEUE_FIELDS_IP_MAP_RECORD = "QueueName";
    public static final String QUEUE_NAME_IP_MAP_RECORD = "WorkspaceName";
    public static final String WORKSPACE_NAME_IP_MAP_RECORD = "GetWorkspaceNameInput";

    public static final String GET_WORKSPACE_NAME_FUNCTION_NAME = "GetWorkspaces";
    public static final String GET_QUEUE_NAME_FUNCTION_NAME = "GetQueues";
    public static final String GET_QUEUE_ENTRIES_FUNCTION_NAME = "GetQueueEntries";
    public static final String INSERT_QUEUE_ENTRIES_FUNCTION_NAME = "InsertQueueEntries";
    public static final String UPDATE_QUEUE_ENTRIES_FUNCTION_NAME = "UpdateQueueEntries";
    public static final String DELETE_QUEUE_ENTRIES_FUNCTION_NAME = "DeleteQueueEntries";
    public static final String GET_QUEUE_FIELDS_FUNCTION_NAME = "GetQueueFields";
    public static final String QUEUE_FIELD_DESCRIPTION_RESULT = "FieldValues";

    //annotations
	public static final String GET_ANNOTATIONS_MAP_RECORD = "DocumentPage";
    public static final String ANNOTATIONS_MAP_RECORD = "Annotations";
    public static final String XMLSTREAM = "XMLStream";
    public static final String IS_ANNOTATION_FUNCTION_NAME = "IsAnnotated";
    public static final String IS_ANNOTATED_DOCID_MAP_RECORD = "DocID";
    public static final String GET_ANNOTATION_FUNCTION_NAME = "GetAnnotations";
    public static final String SAVE_ANNOTATION_FUNCTION_NAME = "SaveAnnotations";
    public static final String FLAG = "flag";
    public static final String ISANNOTATED_RESULT = "GenericFlag";
    public static final String GET_ANNOTATIONS_RESULT = "Annotations";
    public static final String SAVE_ANNOTATIONS_RESULT = "GenericResult";

    //Get user group list xml
    public static final String PRIMARY_GROUP = "primary_group";
    public static final String GENERAL_INFO_FOR_USER_GROUP_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    public static final String XSD_XML_ROOT_NODE_START = "<usergroup xmlns=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://tempuri.org/UserGroup.xsd \"";
    public static final String XSD_XML_ROOT_NODE_END = "</usergroup>";

    //getFolderAttributes
    public static final String GET_FOLDER_ATTRIBUTES_FUNCTION_NAME = "GetFolderAttributes";
    public static final String GET_FOLDER_ATTRIBUTES_INPUT_RECORD = "FolderAttributeRequest";
    public static final String GET_FOLDER_ATTRIBUTES_OUTPUT_RECORD = "FolderProperties";
    public static final String F_FOLDER_NAME = "folder_name";
    public static final String F_FOLDER_ID = "folder_id";

    //createQueue and createWorkspace.
    public static final String CREATE_QUEUE_FUNCTION_NAME = "CreateQueue";
    public static final String QUEUE_FIELD_DESC_LIST = "QueueFieldDescList";
    public static final String EACH_QUEUE_FIELD_DESC = "EachQueueFieldDesc";
    
    /**Name of input MappedRecord in Interaction CreateQueue */
    public static final String CREATEQUEUE_INPUT_RECORD_NAME = "QueueDescription";
    
    /**Name of output MappedRecord in Interaction CreateQueue */
    public static final String CREATEQUEUE_OUTPUT_RECORD_NAME = "GenericResult";

    //Constants used in CreateWorkSpace and CreateQueue
    public static final String DEFINITION_ACCESS_KEY = "definition_access";
    public static final String CONTENT_ACCESS_KEY = "content_access";
    public static final String USER_FIELD_DESC_KEY = "user_field_desc";
    public static final String SECURITY_PERMISSIONS_KEY = "Access_restrictions";
    public static final String WORKSPACE_TEXT_DESC = "text_desc";
    public static final String QUEUE_TEXT_DESC = "text_desc";

    public static final String FIELD_NAME_KEY = "fieldName";
    public static final String FIELD_TYPE_KEY = "fieldType";
    public static final String FIELD_LENGTH_KEY = "fieldLength";
    public static final String FIELD_UNIQUE_KEY = "unique";
    public static final String FIELD_IS_REQUIRED_KEY = "isRequired";
    public static final String FIELD_IS_RENDEZVOUS_KEY = "isRendezvous";
    public static final String FIELD_CAN_BE_DISPLAYED_KEY = "canBeDisplayed";
    public static final String CREATE_WORKSPACE_FUNCTION_NAME = "CreateWorkSpace";
    
    /**Name of input MappedRecord in Interaction CreateWorkSpace */
    public static final String CREATEWORKSPACE_INPUT_RECORD_NAME = "WorkSpaceDescription";
    
    /**Name of output MappedRecord in Interaction CreateWorkSpace */
    public static final String CREATEWORKSPACE_OUTPUT_RECORD_NAME = "GenericResult";

    /**Key to retrieve Folder Attributes from MappedRecord.*/
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

    // GetPasswordStatus and Change Password interaction constants
    public static final String GET_PASSWORD_STATUS_FUNCTION_NAME = "GetPasswordStatus";
    public static final String GET_PASSWORD_STATUS_INPUT_RECORD = "StatusRequest";
    public static final String GET_PASSWORD_STATUS_OUTPUT_RECORD = "StatusDescription";
    public static final String CHANGE_PASSWORD_FUNCTION_NAME = "ChangePassword";
    public static final String CHANGE_PASSWORD_INPUT_RECORD = "PasswordChange";
    public static final String CHANGE_PASSWORD_OUTPUT_RECORD = "GenericResult";

    // Constants used in GetPasswordStatus and ChangePassword
    public static final String OLD_PASSWORD = "old_password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CONFIRM_PASSWORD = "confirm_password";
    public static final String PASSWORD_EXPIRATION_TIME = "pwdExpiresTime";
    public static final String PASSWORD_GRACE_PERIOD = "pwdGracePeriod";
    public static final String PASSWORD_EXPIRED = "FN_IS_RA_10536";
    public static final String PASSWORD_EXPIRED1 = "FN_IS_RA_10901";
    public static final String QUEUE_ACCESS_VIOLATION ="FN_IS_RA_20353";

    public static final String NUMBER_OF_LOGONS = "nbrLogons";
    public static final String LAST_SUCCESSFUL_LOGON_TIME = "successLogTime";
    public static final String LAST_SUCCESSFUL_LOGON_WHERE = "successWhere";
    public static final String LAST_FAILED_LOGON_TIME = "failedLogTime";
    public static final String LAST_FAILED_LOGON_WHERE = "failedWhere";
    public static final String LAST_FAILED_LOGON_ERROR = "failedError";

    //constants used in print/fax
    public static final String GET_PRINTER_ATTRIBUTES_FUNCTION_NAME = "GetPrinterAttributes";
    public static final String PRINT_FAX_DOCS_FUNCTION_NAME = "PrintDocs";
    public static final String PRINTER_ATTRIBUTES_INPUT = "PrinterAttributes";
    public static final String PRINTER_ATTRIBUTES_OUTPUT = "PrinterAttribs";
    public static final String ENABLE_PERFORMANCE_LOGGING_FUNCTION_NAME = "EnablePerformanceLogging";
    public static final String GET_ENABLE_PERFORMANCE_LOGGING_FUNCTION_NAME = "GetEnablePerformanceLogging";
    public static final String ENABLEPERFORMANCELOGGING_INPUT = "EnablePerformanceLogging";
    public static final String GET_ENABLEPERFORMANCELOGGING_INPUT = "GetEnablePerformanceLogging";
    public static final String ENABLEPERFORMANCELOGGING_OUTPUT = "OutputEnableLogging";
    public static final String GET_ENABLEPERFORMANCELOGGING_OUTPUT = "OutputGetEnableLogging";

    public static final String OUT_PERFORMANCE_LOGGING = "out_logging_required";
    public static final String GET_ENABLEPERFORMANCELOGGING_OUTPUT_VALUE = "get_perf_logging";
    public static final String DOC_SPEC_LIST = "DocSpecificationList";
    public static final String PRINT_OPTIONS = "OptionList";
    public static final String ISFAX = "FaxRequest";
    public static final String NOTIFY = "Notify";

    public static final String DOC_DETAILS = "Docdetails";
    public static final String PRINT_DOCS_INPUT = "PrintRequest";
    public static final String PRINTDOCUMENTS_RESULT = "RequestID";

    // constants used in Add Folder interaction
    public static final String CREATE_FOLDER_FUNCTION_NAME = "CreateFolder";
    public static final String CREATE_FOLDER_INPUT_RECORD = "FolderProperties";
    public static final String CREATE_FOLDER_OUTPUT_RECORD = "GenericResult";
    public static final String ACCESS_RESTRICTIONS_KEY = "AccessRestrictions";

    // constants used in Update Folder interaction
    public static final String UPDATE_FOLDER_FUNCTION_NAME = "UpdateFolder";
    public static final String UPDATE_FOLDER_INPUT_RECORD = "UpdateFolder";
    public static final String UPDATE_FOLDER_OUTPUT_RECORD = "GenericResult";

	//constants used in Update F_Closed
    public static final String F_CLOSED_FUNCTION_NAME = "SetFCLOSEDProperty";
    public static final String F_CLOSED_INPUT_RECORD = "inputRecord";

    // constants used in Delete Folder interaction
    public static final String DELETE_FOLDER_FUNCTION_NAME = "DeleteFolder";
    public static final String DELETE_FOLDER_INDEXED_RECORD = "DeleteFolderSet";
    public static final String FOLDER_DELETE_MAPPED_RECORD = "FolderDelete";
    public static final String DELETE_FOLDER_RESULT_INDEX_RECORD = "FolderErrorSet";
    public static final String UNFILE_DOCS = "unfile_docs";
    public static final String SUB_FOLDERS = "do_subfolders";

    // constants used in Get Cache List interaction
    public static final String GET_CACHE_LIST_FUNCTION_NAME = "GetCacheList";
    public static final String GET_CACHE_LIST_INPUT_RECORD = "GetCacheListInput";
    public static final String GET_CACHE_LIST_OUTPUT_RECORD = "CacheList";
    public static final String NEW_ANNOTATIONS = "new_annotations";
    public static final String MODIFY_ANNOTATIONS = "modify_annotations";
    public static final String DELETE_ANNOTATIONS = "delete_annotations";
    public static final String ANNOT_CREATE_DATE = "annot_create_date";
    public static final String STATUS = "status";
    public static final String RESPONSE = "response";
    public static final String ANNOT_ID = "annot_Id";
    public static final String NEW_ID = "new_ID";
    public static final String OLD_ID = "old_ID";
    public static final String RESULT_HASH_MAP = "ResultHashMap";
    public static final String CLIENT_CODEPAGE = "ClientCodepage";//Added for Globalization Real Fix (DTS 173917).

	public static String WcmApiConfig = "C:\\Personal\\RADWorkSpace\\BlueCardExt\\WcmApiConfig.properties";
	public static String FileNetConfig = "C:\\Personal\\RADWorkSpace\\BlueCardExt\\FileNet.properties";
	public static String logConfig = "C:\\Personal\\RADWorkSpace\\BlueCardExt\\Properties\\log4j.properties";
    public static final String WQS_SERVICE = "WQSService";

}