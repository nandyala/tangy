package com.ozstrategy.strategy;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;

import com.ozstrategy.util.StringUtil;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer
 * @version  $Revision$, $Date$
 */
public class OzPreProcessor implements PreProcessor, InitializingBean {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected Properties mapping = new Properties();

  /** DOCUMENT ME! */
  protected String tempName;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    Assert.notNull(tempName);

    mapping.load(this.getClass().getResourceAsStream(tempName));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  PreProcessor#process(String)
   */
  @Override public String process(String content) {
    String retExpr = content;

    int processorNum = Integer.parseInt(mapping.getProperty(
          "total.preprocessor", "0"));

    for (int i = 1; i <= processorNum; i++) {
      String keyName   = "preprocessor." + i + ".expression";
      String valueName = "preprocessor." + i + ".result";
      String key       = mapping.getProperty(keyName);
      String value     = mapping.getProperty(valueName);

      retExpr = StringUtil.stringReplace(retExpr, key, value);
    }

    return retExpr;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Pre-process with additional mapping process and replace.
   *
   * @param   content          to be process
   * @param   spaceReplace     replace white space
   * @param   quoteReplace     Quote replace
   * @param   operatorReplace  operator replace
   *
   * @return  DOCUMENT ME!
   */
  @Override public String process(String content, boolean spaceReplace, boolean quoteReplace, boolean operatorReplace) {
    String expression = content;

    if (spaceReplace) {
      expression = expression.replaceAll("\n|\r", " ");
    }

    if (quoteReplace) {
      expression = expression.replaceAll("\"", "\\\"");
    }

    // pre-process
    expression = process(expression);

    // if (forceMapping != null) {
    //
    // for (String key : forceMapping.keySet()) {
    // String value = forceMapping.get(key);
    // expression = StringUtil.stringReplace(expression, key, value);
    // }
    // }

    if (operatorReplace) {
// expression = expression.replaceAll("(\\b=\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "==");
// expression = expression.replaceAll("(\\b<>\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "!=");
// expression = expression.replaceAll("(\\b====\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "==");
// expression = expression.replaceAll("(\\b<==\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "<=");
// expression = expression.replaceAll("(\\b>==\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", ">=");
// expression = expression.replaceAll("(\\b!==\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "!=");
// expression = expression.replaceAll("(\\b:==\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "=");
      expression = replaceWithoutInString(expression, "=", "==");
      expression = replaceWithoutInString(expression, "<>", "!=");
      expression = replaceWithoutInString(expression, "====", "==");
      expression = replaceWithoutInString(expression, "<==", "<=");
      expression = replaceWithoutInString(expression, ">==", ">=");
      expression = replaceWithoutInString(expression, "!==", "!=");
      expression = replaceWithoutInString(expression, ":==", "=");
    }

    // replace and/or
    // expression = expression.replaceAll(" and ", " && ");
    // expression = expression.replaceAll("\\)and\\(", ") && (");
    // expression = expression.replaceAll("\\)and ", ") && ");
    // expression = expression.replaceAll(" and\\(", " && (");
    // expression = expression.replaceAll(" or ", " || ");
    // expression = expression.replaceAll("\\)or\\(", ") || (");
    // expression = expression.replaceAll("\\)or ", ") || ");
    // expression = expression.replaceAll(" or\\(", " || (");

    expression = replaceWithoutInString(expression, "(\\band\\b)", "&&");
    expression = replaceWithoutInString(expression, "(\\bor\\b)", "||");
// expression = expression.replaceAll("(\\band\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "&&");
// expression = expression.replaceAll("(\\bor\\b)(?=(?:[^\"]|\"[^\"]*\")*$)", "||");

    return expression;
  } // end method process

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  mapping  DOCUMENT ME!
   */
  public void setMapping(Properties mapping) {
    this.mapping = mapping;
  }

  /*
  * (non-Javadoc)
  *
  * @see com.ozstrategy.rule.PreProcessor#process(java.lang.String)
  */

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  tempName  the preProcessTemp to set
   */
  public void setTempName(String tempName) {
    this.tempName = tempName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   content    DOCUMENT ME!
   * @param   fromRegEx  DOCUMENT ME!
   * @param   toString   DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  String replaceWithoutInString(String content, String fromRegEx, String toString) {
    Pattern      pattern = Pattern.compile("\\\"[^\\\"]+\\\"|\\\'[^\\\']+\\\'");
    Matcher      matcher = pattern.matcher(content);
    StringBuffer sb      = new StringBuffer();

    int pos = 0;

    while (matcher.find()) {
      sb.append(content.substring(pos, matcher.start()).replaceAll(fromRegEx, toString));
      sb.append(matcher.group());
      pos = matcher.end();
    }

    if (pos < content.length()) {
      sb.append(content.substring(pos).replaceAll(fromRegEx, toString));
    }

    return sb.toString();
  }
} // end class OzPreProcessor
