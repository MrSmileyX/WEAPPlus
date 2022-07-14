/*
 *
 * Copyright (c) 2005-2006  FileNet Corporation.
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

import java.util.*;

/**
 *
 * <p>Title: </p>
 * <p>Description:  <code>GetMessageImpl</code> defines the method necessary for
 * Localization of the Sample Application. It gives a specific Label or Message
 * corresponding to each action/screen in every functionality.</p>
 * <p>Copyright: Copyright (c) 2005-2006  </p>
 * <p>Company: FileNet Corporation.</p>
 * @author Inderjit Singh
 * @version 3.2.1
 */
public class GetMessageImpl implements GetMessage {

  /** ResourceBundle name */
  private ResourceBundle m_Bundle = null;

  /**
   * Constructor
   *
   * @param pCurrentLocale
   */
  public GetMessageImpl(Locale pCurrentLocale) {
    try {
      m_Bundle = ResourceBundle.getBundle(WebConstants.RESOURCE_BULDLE_SAMPLE,
                                          pCurrentLocale);
    }
    catch (MissingResourceException mrex) {
      mrex.printStackTrace();
    }
  }

  /**
   * returns the locale specific dynamic message as a Java String.
   * @param key   Represents a key to the properties stored in properties bundle.
   * @return String   The corresponding string value in ResourceBundle.
   * @throws none
   */
  public String customMessage(String key) {
    String lCustomMessage = null;
    try {
      lCustomMessage = this.m_Bundle.getString(key);
    }
    catch (MissingResourceException mre) {
      return WebConstants.STR_MSG + key + WebConstants.STR_MSG1;
    }
    catch (NullPointerException mrex) {
      return WebConstants.STR_MSG + key + WebConstants.STR_MSG1;
    }
    return lCustomMessage;
  }
}