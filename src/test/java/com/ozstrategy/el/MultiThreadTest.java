package com.ozstrategy.el;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ozstrategy.el.impl.ResolverContext;
import com.ozstrategy.el.model.Responsible;
import com.ozstrategy.el.util.CalculateUtil;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 5/20/14 Time: 9:59 AM To change this template use File | Settings |
 * File Templates.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class MultiThreadTest {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(MultiThreadTest.class);

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private int counter = 0;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @DataProvider(
    name     = "dp",
    parallel = true
  )
  public Object[][] createData1() {
    return new Object[][] {
        { new Responsible(31, CalculateUtil.toDate("1982-01-01"), "Mike", "AAAA1", "AAAA2"), "AAAA1", "AAAA2" },
        { new Responsible(32, CalculateUtil.toDate("1981-06-01"), "Jean", "BBBB1", "BBBB2"), "BBBB1", "BBBB2" },
// { new Responsible(33, CalculateUtil.toDate("1980-06-01"), "Jean", "CCCC1", "CCCC2"), "CCCC1", "CCCC2" },
// { new Responsible(34, CalculateUtil.toDate("1979-06-01"), "Jean", "DDDD1", "DDDD2"), "DDDD1", "DDDD2" },
// { new Responsible(35, CalculateUtil.toDate("1978-06-01"), "Jean", "EEEE1", "EEEE2"), "EEEE1", "EEEE2" },
// { new Responsible(36, CalculateUtil.toDate("1977-06-01"), "Jean", "FFFF1", "FFFF2"), "FFFF1", "FFFF2" },
// { new Responsible(37, CalculateUtil.toDate("1976-06-01"), "Jean", "GGGG1", "GGGG2"), "GGGG1", "GGGG2" },
// { new Responsible(38, CalculateUtil.toDate("1975-06-01"), "Jean", "HHHH1", "HHHH2"), "HHHH1", "HHHH2" },
// { new Responsible(39, CalculateUtil.toDate("1974-06-01"), "Jean", "IIII1", "IIII2"), "IIII1", "IIII2" },
// { new Responsible(40, CalculateUtil.toDate("1973-06-01"), "Jean", "JJJJ1", "JJJJ2"), "JJJJ1", "JJJJ2" },
      };
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   */
  @BeforeSuite public void setUp() {
    // code that will be invoked when this test is instantiated
// ResolverContext.addExposedClass(CalculateUtil.class);
// ResolverContext.addExposedClass(Function.class);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   responsible  DOCUMENT ME!
   * @param   checkName1   DOCUMENT ME!
   * @param   checkName2   DOCUMENT ME!
   *
   * @throws  InterruptedException  DOCUMENT ME!
   */
  @Test(dataProvider = "dp")
  public void testEval(Responsible responsible, String checkName1, String checkName2) throws InterruptedException {
    counter++;
// Thread.sleep(200);

    for (int i = 0; i < 1000; i++) {
      ResolverContext context = createContext(responsible);

      StringBuilder sb = new StringBuilder();
      sb.append("No." + counter + " - ");
      sb.append("Thread#: " + Thread.currentThread().getId());
      sb.append("\n\tExpect:" + checkName1 + "-" + checkName2);
      sb.append("\n\tFirstName:" + context.eval("firstName"));
      sb.append("\n\tLastName:" + context.eval("lastName"));
      sb.append("\n\tBirthDay:" + context.eval("birthDay"));
      sb.append("\n\tAge:" + context.eval("age"));
      logger.info(sb.toString());
// System.out.println(sb.toString());


      assertEquals(context.eval("firstName"), checkName1);
      assertEquals(context.eval("lastName"), checkName2);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private ResolverContext createContext(Responsible responsible) {
    ResolverContext context = new ResolverContext();

    context.addValue("responsible", responsible);
    context.addDefinition("firstName", "responsible.firstName", String.class);
    context.addDefinition("lastName", "responsible.lastName", String.class);
    context.addDefinition("birthDay", "responsible.birthday", Date.class);
    context.addDefinition("age", "responsible.age", Integer.class);

    return context;
  }
} // end class MultiThreadTest
