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

import java.util.*;
import com.filenet.is.ra.sa.util.AppConstants;

/**
 * <code>InteractionBean</code> is a data object which stores all the common parameters provided to
 * carry out any interaction. These properties are borrowed from javax.resource.cci.InteractionSpec interface.
 * It has got properties and corresponding Accessors and Mutators.
 * This enables the programmer to use this class in the jsp:setProperty action.
 * All the interaction specific classes extends this class to add interaction specific properties.
 *
 * @author Ashu Govil, Kiran Kumar
 * @version 1.0
 *
 * implements - Serialiazable
 */

public abstract class InteractionBean implements java.io.Serializable{

    private String mFunctionName;
    private int m_nInteractionVerb = AppConstants.SYNC_SEND_RECEIVE;
    private long m_lExecutionTimeout = 10000;                         //10sec is the default value
    private boolean m_bInteractionExecute3 = false;

    /**
     * Empty constructor, does nothing
     */
    public InteractionBean() {
    }

  /**
   *  sets the FunctionName attribute of this bean.
   *  @param java.lang.String
   *  @return None
   *  @throws None
   */
    public void setFunctionName(String functionName) {
      if(functionName != null && functionName.trim().length() > 0)
          mFunctionName = functionName;
    }
  /**
   *  sets the InteractionVerb attribute of this bean.
   *  @param java.lang.String
   *  @return None
   *  @throws None
   */
    public void setInteractionVerb(String iVerb) {
      if( iVerb == null) return;
      try{
          m_nInteractionVerb = Integer.parseInt(iVerb);
      }catch(NumberFormatException ne){}
    }

    /**
     *  sets the ExecutionTimeout attribute of this bean.
     *  @param java.lang.String
     *  @return None
     *  @throws None
     */
    public void setExecutionTimeout(String executionTimeout) {
        if( executionTimeout == null ) return;
        try{
          m_lExecutionTimeout = Long.parseLong(executionTimeout);
        }catch(NumberFormatException ne){}
      }

    /**
     *  sets the InteractionExecute3 attribute of this bean. If the passed in String is not equal to null,
     *  then the property is set as true. Else false.
     *  If true, InteractionSpec.execute(InteractionSpec,Record,Record) version of execute method will be
     *  used to execute the interaction in the RA.
     *  @param java.lang.String
     *  @return None
     *  @throws None
     */
    public void setInteractionExecute3(String execute3) {
          if(execute3 != null)
                  m_bInteractionExecute3 = true;
    }

    /**
     *  Returns the FunctionName attribute of this bean.
     *  @param None
     *  @return java.lang.String
     *  @throws None
     */
    public String getFunctionName() {
        return mFunctionName != null ? mFunctionName:null;
    }

    /**
     *  Returns the InteractionVerb attribute of this bean.
     *  @param None
     *  @return java.lang.String
     *  @throws None
     */
    public String getInteractionVerb() {
        return m_nInteractionVerb != Integer.MIN_VALUE ? String.valueOf(m_nInteractionVerb):null;
    }

    /**
     *  Returns the ExecutionTimeout attribute of this bean.
     *  @param None
     *  @return java.lang.String
     *  @throws None
     */
    public String getExecutionTimeout() {
        return m_lExecutionTimeout != Long.MIN_VALUE ? String.valueOf(m_lExecutionTimeout):null;
    }

    /**
     *	Returns the InteractionExecute3 attribute of this bean.
     *  @param None
     *  @return java.lang.String
     *  @throws None
     */
    public boolean getInteractionExecute3() {
        return m_bInteractionExecute3;
      }

    /**
     *  Returns a Map object which contains all the attributes in a key value format. This Map can be
     *  further used to send to the Resource Adapter to invoke all the interactions.
     *  @param None
     *  @return java.util.Map
     *  @throws None
     */

    protected Map getParamsMap() {
        Map paramsMap = new HashMap();

        if(mFunctionName != null)
            paramsMap.put(AppConstants.FUNCTION_NAME,mFunctionName);

        paramsMap.put(AppConstants.INTERACTION_VERB,new Integer(m_nInteractionVerb));
        paramsMap.put(AppConstants.EXECUTION_TIMEOUT,new Long(m_lExecutionTimeout));

        paramsMap.put(AppConstants.INTERACTION_EXECUTE_3,new Boolean(m_bInteractionExecute3));

        return paramsMap;
    }
} // end of Interaction Bean class