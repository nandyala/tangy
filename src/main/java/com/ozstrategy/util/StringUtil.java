package com.ozstrategy.util;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.StringUtils;


/**
 * String Utility Class This is used to encode passwords programmatically.
 *
 * <p><a h ref="StringUtil.java.html"><i>View Source</i></a></p>
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
public class StringUtil {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  // ~ Static fields/initializers =============================================

  private static final Log log = LogFactory.getLog(StringUtil.class);

  // ~ Methods ================================================================

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Decode a string using Base64 encoding.
   *
   * @param   str  DOCUMENT ME!
   *
   * @return  String
   */
  public static String decodeString(String str) {
    try {
      return new String(Base64.decodeBase64(str.getBytes()), "UTF-8");
    } catch (UnsupportedEncodingException e) { }

    return "";
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Encode a string using algorithm specified in web.xml and return the resulting encrypted password. If exception, the
   * plain credentials string is returned
   *
   * @param   password   Password or other credentials to use in authenticating this username
   * @param   algorithm  Algorithm used to do the digest
   *
   * @return  encypted password based on the algorithm.
   */
  public static String encodePassword(String password, String algorithm) {
    byte[] unencodedPassword = null;

    try {
      unencodedPassword = password.getBytes("UTF-8");

      MessageDigest md = null;

      try {
        // first create an instance, given the provider
        md = MessageDigest.getInstance(algorithm);
      } catch (Exception e) {
        log.error("Exception: " + e, e);

        return password;
      }

      md.reset();

      // call the update method one or more times
      // (useful when you don't know the size of your data, eg. stream)
      md.update(unencodedPassword);

      // now calculate the hash
      byte[] encodedPassword = md.digest();

      StringBuffer buf = new StringBuffer();

      for (int i = 0; i < encodedPassword.length; i++) {
        if ((encodedPassword[i] & 0xff) < 0x10) {
          buf.append("0");
        }

        buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
      }

      return buf.toString();
    } catch (UnsupportedEncodingException e) { } // end try-catch


    return "";
  } // end method encodePassword

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Encode a string using Base64 encoding. Used when storing passwords as cookies. This is weak encoding in that anyone
   * can use the decodeString routine to reverse the encoding.
   *
   * @param   str  DOCUMENT ME!
   *
   * @return  String
   */
  public static String encodeString(String str) {
    try {
      return new String(Base64.encodeBase64(str.getBytes()), "UTF-8").trim();
    } catch (UnsupportedEncodingException e) { }

    return "";
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Check if a String element is in a String Array.
   *
   * @param   element  DOCUMENT ME!
   * @param   array    DOCUMENT ME!
   *
   * @return  check if a String element is in a String Array.
   */
  public static boolean isInArray(String element, String[] array) {
    if (element == null) {
      for (String a : array) {
        if (a == null) {
          return true;
        }
      }
    } else {
      for (String a : array) {
        if (element.equals(a)) {
          return true;
        }
      }
    }

    return false;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   input            DOCUMENT ME!
   * @param   totalLength      DOCUMENT ME!
   * @param   clearTextLength  DOCUMENT ME!
   * @param   maskChar         DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String mask(String input, int totalLength, int clearTextLength, String maskChar) {
    if (StringUtils.hasText(input)) {
      // String padding to clearLength
      if (input.length() < clearTextLength) {
        int           appendLength = clearTextLength - input.length();
        StringBuilder prefix       = new StringBuilder();

        for (int i = 0; i < appendLength; i++) {
          prefix.append(maskChar);
        }

        input = prefix.append(input).toString();
      } else {
        input = input.substring(input.length() - clearTextLength, input.length());
      }

      // Pad the prefix
      StringBuilder paddingPrefix = new StringBuilder();

      for (int i = 0; i < (totalLength - input.length()); i++) {
        paddingPrefix.append(maskChar);
      }

      return paddingPrefix.append(input).toString();
    } else {
      StringBuilder paddingPrefix = new StringBuilder();

      for (int i = 0; i < totalLength; i++) {
        paddingPrefix.append(maskChar);
      }

      return paddingPrefix.toString();
    } // end if-else
  }   // end method mask

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   inputStr        DOCUMENT ME!
   * @param   patternStr      DOCUMENT ME!
   * @param   replacementStr  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String stringReplace(String inputStr, String patternStr,
    String replacementStr) {
    // Compile regular expression
    Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);

    // Replace all occurrences of pattern in input
    Matcher matcher   = pattern.matcher(inputStr);
    String  outputStr = matcher.replaceAll(Matcher.quoteReplacement(replacementStr));

    return outputStr;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   in  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static String stripInvalidChar(String in) {
    try {
      byte[] utf = in.getBytes("UTF-8");

      int len = utf.length;

      for (int i = 0; i < len; i++) {
        int ii = utf[i];

        if (ii < 0) {
          utf[i] = 32;
        }
      }

      return new String(utf, "UTF-8");

    } catch (UnsupportedEncodingException c) { }

    return "";
  }

} // end class StringUtil
