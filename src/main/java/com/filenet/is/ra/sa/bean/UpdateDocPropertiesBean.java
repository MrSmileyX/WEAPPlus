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
 */
package com.filenet.is.ra.sa.bean;

import java.io.Serializable;
import java.util.*;

/**
 *
 * <code>UpdateDocPropertiesBean</code> is a data object which stores all the
 * parameters provided to carry out UpdateDocProperties interaction. It has got properties
 * and corresponding Accessors and Mutators. This enables the programmer to use
 * this class in the jsp:setProperty action.
 *
 * @author Ashu Govil, Kiran Kumar
 * @version 1.0
 *
 * extends - InteractionBean
 * implements - Serialiazable
 */

public class UpdateDocPropertiesBean extends InteractionBean implements Serializable {

    private String m_DocID = "";

    /**
     * Empty constructor, does nothing
     */
    public UpdateDocPropertiesBean() {
    }

    /**
     *  sets the DocumentID attribute of this bean.
     *  @param java.lang.String
     *  @return None
     *  @throws None
     */
    public void setDocID(String docID){
        if(docID != null && docID.trim().length()>0)
            m_DocID = docID;
    }

   /**
    *  Returns the DocumentID attribute of this bean.
    *  @param None
    *  @return java.lang.String
    *  @throws None
    */
     public String getDocID(){
        return m_DocID;
    }

    /**
     *  Returns a Map object which contains all the attributes in a key value format.
     *  This Map can be further used to send to the Resource Adapter to invoke the
     *  UpdateDocumentProperties interaction.
     *  @param None
     *  @return java.util.Map
     *  @throws None
     */
    public Map getParamsMap(){
        Map paramsMap = new HashMap();
        paramsMap.putAll(super.getParamsMap());

        return paramsMap;
    }

}