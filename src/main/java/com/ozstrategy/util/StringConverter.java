package com.ozstrategy.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;


/**
 * Created by IntelliJ IDEA. User: rojer Date: Jun 11, 2010 Time: 11:38:23 PM To change this template use File |
 * Settings | File Templates.
 */
public class StringConverter {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Convert from percentage string to BigDecimal.
   *
   * @param toConvert String to convert
   * @return converted BigDecimal
   */
  public static BigDecimal fromPercentageString(String toConvert) {
    if (StringUtils.hasText(toConvert)) {
      toConvert = toConvert.trim();

      if (toConvert.matches("^\\d*(\\.\\d*)?%$")) {
        return new BigDecimal(toConvert.substring(0,
          toConvert.length() - 1)).divide(new BigDecimal(
          100));
      } else if (toConvert.matches("^\\d*(\\.\\d*)?$")) {
        return new BigDecimal(toConvert);
      }
    }

    return null;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Trim white space.
   *
   * @param str DOCUMENT ME!
   * @return trim white space
   */
  public static String trimWhiteSpace(String str) {
    if (str != null) {
      return str.replaceAll(" |\t|\n", "");
    } else {
      return null;
    }
  }

  public static String trimParent(String expr) {
    expr = expr.trim();
    int count = StringUtils.countOccurrencesOf(expr, "(") - StringUtils.countOccurrencesOf(expr, ")");

    // 1. Count of Left Parent matches count of Right Parent
    // 2. First Left Parent should be at the left side of the Right Parent
    if (count == 0 && (expr.indexOf("(") < expr.indexOf(")"))) {
      // no change
      return expr;
    } else {
      String ret = expr.replaceFirst("^ *\\(( *\\()*", "");
      ret = ret.replaceAll("(\\) *)*\\) *$", "").trim();

      count = StringUtils.countOccurrencesOf(ret, "(") - StringUtils.countOccurrencesOf(ret, ")");

      if (count > 0) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
          sb.append(")");
        }
        ret += sb.toString();
      }

      return ret;
    }
  }
} // end class StringConverter
