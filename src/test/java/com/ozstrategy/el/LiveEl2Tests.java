/**
 *
 */
package com.ozstrategy.el;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Iterator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class LiveEl2Tests extends TestCase {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  protected String fileName = "AllClientSyntax_20140225.xlsx";

  /** DOCUMENT ME! */
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

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

    int total = 0;

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
          logger.debug("Working on '" + sheetName + "'");
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

                if ("Syntax".equalsIgnoreCase(cellValue) || "Variable_Syntax1".equalsIgnoreCase(cellValue)) {
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
              if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String syntaxValue = cell.getStringCellValue();

                if (StringUtils.hasText(syntaxValue)) {
                  Boolean result = runParserTest(syntaxValue);

                  if (!result) {
                    total++;

                    // get client name and rule name, etc.

                    logger.info("Issue No." + total + " found......");
                    logger.info("------------------------------------------------------------------");
                    logger.info("\n" + syntaxValue + "\n");
                    logger.info("------------------------------------------------------------------");

                    StringBuilder sb = new StringBuilder();
                    sb.append("Sheet :'").append(sheetName).append("' - Row NO. - :").append(cell.getRowIndex() + 1);

                    for (int j = 1; j < syntaxColumn; j++) {
                      Cell oCell = row.getCell(j);

                      if (oCell.getCellType() == Cell.CELL_TYPE_STRING) {
                        sb.append("-->").append(oCell.getStringCellValue());
                      }
                    }

                    logger.info(sb.toString());
                    logger.info("------------------------------------------------------------------");
// Assert.assertTrue(result);
                  } // end if
                }   // end if
              }     // end if
            }       // end if

            workedSyntax++;
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
    logger.info("Total issue found:" + total);
// Assert.assertTrue(total == 0);
  } // end method testFromXls

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
    CharStream        input  = new ANTLRInputStream(expr);
    OzElLexer         lexer  = new OzElLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    OzElParser        parser = new OzElParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new OzVerboseListener());

    boolean       testPass = false;
    VerifyVisitor visitor  = new VerifyVisitor();

    try {
      ParseTree tree = parser.program(); // parse

// visitor.visit(tree);

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

// if (logger.isDebugEnabled()) {
// logger.debug("Used Variables:" + visitor.getUsedVariables());
// }
//
// if (logger.isDebugEnabled()) {
// logger.debug("Unknown Variables:" + visitor.getUnknownVariables());
// }
//
// if (logger.isDebugEnabled()) {
// logger.debug(testPass ? "Passed.\n" : "Failed.\n");
// }
//
    return testPass;
  } // end method runParserTest
} // end class LiveEl2Tests
