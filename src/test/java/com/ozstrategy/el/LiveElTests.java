/**
 *
 */
package com.ozstrategy.el;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.apache.poi.ss.usermodel.*;

import org.junit.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.util.StringUtils;

import com.ozstrategy.el.antlr.OzElLexer;
import com.ozstrategy.el.antlr.OzElParser;
import com.ozstrategy.el.antlr.OzVerboseListener;
import com.ozstrategy.el.impl.VerifyVisitor;

import junit.framework.TestCase;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer
 * @version  $Revision$, $Date$
 */
public class LiveElTests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected String fileName = "Current_Active_Syntax_All_Clients.xlsx";

  /** DOCUMENT ME! */
  protected Properties mapping = new Properties();

  /** DOCUMENT ME! */
  protected String tempName = "preProcessor.properties";

  /** DOCUMENT ME! */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  junit.framework.TestCase#setUp()
   */
  @Override public void setUp() throws Exception {
    Resource resource = new ClassPathResource(tempName);
    mapping.load(resource.getInputStream());

    if (logger.isDebugEnabled()) {
      logger.debug("Load Mappings:" + mapping.size());
    }

// resource = new ClassPathResource(fileName);
// File file = resource.getFile();
// fileName = file.getAbsolutePath();
// logger.debug("Open XLS File: '" + fileName + "'");
//
// if(file.exists()){
// logger.debug("File found..");
// }

    super.setUp();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public void testFromXls() throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug(">>>>>Test Positive Cases Started....");
    }

    FileInputStream fis          = null;
    int             workedSheet  = 0;
    int             workedSyntax = 0;
    StringBuffer    workedSheets = new StringBuffer();

    try {
      //
      // Create a FileInputStream that will be use to read the
      // excel file.
      //
      logger.info("Loading Syntax from file:'" + fileName + "'");

      InputStream is = new FileInputStream(fileName);

      Workbook wb = WorkbookFactory.create(is);

      int sheets = wb.getNumberOfSheets();

      for (int i = 0; i < sheets; i++) {
        // loop through all sheets
        String sheetName = wb.getSheetName(i);
        workedSheets.append(",");
        workedSheets.append(sheetName);

        if (logger.isDebugEnabled()) {
          logger.debug("Working on " + sheetName);
        }

        Sheet sheet        = wb.getSheetAt(i);
        int   syntaxColumn = -1;

        // When we have a sheet object in hand we can iterator on
        // each sheet's rows and on each row's cells. We store the
        // data read on an ArrayList so that we can printed the
        // content of the excel to the console.
        //
        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
          Row row = (Row) rows.next();

          if (syntaxColumn == -1) {
            int columns = row.getPhysicalNumberOfCells();

            // look for Syntax column
            for (int j = 1; j <= columns; j++) {
              Cell cell = row.getCell(j);

              if (cell != null) {
                String cellValue = cell.getStringCellValue();

                if ("Syntax".equalsIgnoreCase(cellValue)) {
                  syntaxColumn = j;
                  workedSheet++;

                  // found, break
                  break;
                }
              }
            }
          } else {
            // get syntax from the cell of syntax column
            Cell cell = row.getCell(syntaxColumn);

            if (cell != null) {
              String syntaxValue = cell.getStringCellValue();

              if (StringUtils.hasText(syntaxValue)) {
// runParserTest(syntaxValue);
                Assert.assertTrue(runParserTest(syntaxValue));
                workedSyntax++;
              }
            }
          } // end if-else
        }   // end while
      }     // end for
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fis != null) {
        fis.close();
      }
    }       // end try-catch-finally

    if (logger.isDebugEnabled()) {
      logger.debug("Test Positive Cases Completed.<<<<");
    }

    logger.info("Worked Sheets:'" + workedSheets.substring(1) + "'");
    logger.info("Total sheets:" + workedSheet);
    logger.info("Total syntax verified:" + workedSyntax);
  } // end method testFromXls

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   content  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected String process(String content) {
    String expression = content;

    // pre-process
    int processorNum = Integer.parseInt(mapping.getProperty(
          "total.preprocessor", "0"));

    for (int i = 1; i <= processorNum; i++) {
      String keyName   = "preprocessor." + i + ".expression";
      String valueName = "preprocessor." + i + ".result";
      String key       = mapping.getProperty(keyName);
      String value     = mapping.getProperty(valueName);

      expression = stringReplace(expression, key, value);
    }

// OzPreProcessor processor = new OzPreProcessor();
// processor.setMapping(mapping);
// expression = processor.process(content);
// expression = processor.process(expression, true, true, true);

    return expression;
  } // end method process

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   expr  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  protected boolean runParserTest(String expr) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("Parsing Expression : '" + expr + "' ......");
    }

    CharStream        input  = new ANTLRInputStream(process(expr));
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    boolean       testPass = false;
    VerifyVisitor visitor  = new VerifyVisitor();


    try {
      ParseTree tree = parser.program(); // parse

// OzElValue retValue = visitor.visit(tree);

      testPass = true;
    } catch (RuntimeException e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.toString());
      }
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug(e.toString());
      }
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Used Variable(s): " + visitor.getUsedVariables());
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Unknown Variable(s): " + visitor.getUnknownVariables());
    }

    if (logger.isDebugEnabled()) {
      logger.debug(testPass ? "Passed.\n" : "Failed.\n");
    }

    return testPass;
  } // end method runParserTest

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
  private String stringReplace(String inputStr, String patternStr,
    String replacementStr) {
    // Compile regular expression
    Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);

    // Replace all occurrences of pattern in input
    Matcher matcher   = pattern.matcher(inputStr);
    String  outputStr = matcher.replaceAll(Matcher.quoteReplacement(replacementStr));

    return outputStr;
  }

} // end class LiveElTests
