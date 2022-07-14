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

import java.util.*;
import java.lang.reflect.InvocationTargetException;

/**
 * <code>GetMessageUtil</code> defines the method necessary for Localization
 * of the Sample Application. It calls a function from GetMessageImplementationUtils
 * to provide locale specific dynamic message as a Java String.
 */
/**
 * <p>Title: </p>
 * <p>Description: <code>GetMessageUtil</code> declares the method necessary for
 * Localization of the Sample Application. It gives a specific Label or Message
 * corresponding to each action/screen in every functionality.</p>
 * <p>Copyright: Copyright (c) 2005-2006  </p>
 * <p>Company: FileNet Corporation.</p>
 * @author Inderjit Singh
 * @version 3.2.1
 */
public class GetMessageUtil {
  /** Cache of ResourceBundles for various locales */
  private static Hashtable m_GetMessageCache = null;
  private static Locale m_DefaultLocale = null;

  /**
   * Empty Constructor
   */
  public GetMessageUtil() {
  }

  /**
   * static block for initializing the GetMessgaeCache
   */
  static {
    try {
      m_DefaultLocale = Locale.getDefault();
      m_GetMessageCache = new Hashtable();
      m_GetMessageCache.put(m_DefaultLocale,
                            getGetMessageInstance(m_DefaultLocale));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * This method returns an instance of GetMessage which is unique
   * for a particular locale.
   *
   * @param pCurrentLocale
   * @return
   */
  public static GetMessage getInstance(Locale pCurrentLocale) {
    GetMessage lGetMessage = null;
    ResourceBundle l_Bundle = null;

    try {
      if (pCurrentLocale != null && pCurrentLocale != m_DefaultLocale) {
        lGetMessage = (GetMessage) m_GetMessageCache.get(pCurrentLocale);

        if (lGetMessage == null) {
          lGetMessage = getGetMessageInstance(pCurrentLocale);
          m_GetMessageCache.put(pCurrentLocale, lGetMessage);
        }
      }
      else {
        lGetMessage = (GetMessage) m_GetMessageCache.get(m_DefaultLocale);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return lGetMessage;
  }

  /**
   * This method instantiates and returns an instance of the GetMessage
   * for a particular locale.
   *
   * @param pCurrentLocale
   * @return
   * @throws java.lang.Exception
   */
  private static GetMessage getGetMessageInstance(Locale pCurrentLocale) throws
      Exception {
    Locale lCurrentLocale = null;
    GetMessage lGetMessage = null;

    try {
      Class lGetMessageClass = Class.forName(WebConstants.
                                             PACKAGE_GETMSGIMPLEMENTATION);
      Object[] lLocale = {
          pCurrentLocale};

      if (pCurrentLocale != null) {
        lGetMessage = (GetMessage) ( (lGetMessageClass.getConstructors())[0]).
            newInstance(lLocale);
      }
      else {
        throw new Exception(WebConstants.LOCALE_NULL);
      }
    }
    catch (ClassNotFoundException lExcp) {
      throw new Exception(lExcp.getMessage());
    }
    catch (IllegalAccessException lExcp) {
      throw new Exception(lExcp.getMessage());
    }
    catch (InstantiationException lExcp) {
      throw new Exception(lExcp.getMessage());
    }
    catch (InvocationTargetException lExcp) {
      throw new Exception(lExcp.getMessage());
    }
    catch (SecurityException lExcp) {
      throw new Exception(lExcp.getMessage());
    }
    catch (Exception lExcp) {
      throw lExcp;
    }
    return lGetMessage;
  }
}