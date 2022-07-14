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

/**
 * <p>Title: </p>
 * <p>Description: <code>GetMessage</code> declares the method necessary for
 * Localization of the Sample Application. It gives a specific Label or Message
 * corresponding to each action/screen in every functionality.</p>
 * <p>Copyright: Copyright (c) 2005-2006  </p>
 * <p>Company: FileNet Corporation.</p>
 * @author Inderjit Singh
 * @version 3.2.1
 */

public interface GetMessage {
  /**
   * returns the locale specific dynamic message as a Java String.
   * @param key   Represents a key to the properties stored in properties bundle.
   * @return String   The corresponding string value in ResourceBundle.
   * @throws none
   */
  public String customMessage(String key);
}