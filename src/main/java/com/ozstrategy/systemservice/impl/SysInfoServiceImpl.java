package com.ozstrategy.systemservice.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.sql.DataSource;

import org.jasypt.util.text.BasicTextEncryptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.core.io.Resource;

import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

import com.ozstrategy.systemservice.SysInfoService;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer
 * @version  $Revision$, $Date$
 */
//@Service("sysInfoService")
public class SysInfoServiceImpl implements SysInfoService, ApplicationContextAware, InitializingBean {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static String LICENSE_NAME = "classpath:license.bin";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private ApplicationContext ctx;
  private String             dbName;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override public void afterPropertiesSet() throws Exception {
    if (ctx != null) {
      DataSource dataSource = (DataSource) ctx.getBean("dataSource");
      String     jdbcUrl    = dataSource.getConnection().getMetaData().getURL();
      String[]   parts      = jdbcUrl.split("//|/|\\?");
      dbName = parts[2];
      dataSource.getConnection().close();
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * return random string.
   *
   * @return  random string
   */
  public String getRandomString() {
    return "m0S>hb.vB6[*m0$&1`\\=";
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  SysInfoService#isSystemValid()
   */
  @Override public boolean isSystemValid() {
    try {
      // load license file from classpath
      Resource licenseFile = ctx.getResource(LICENSE_NAME);
      String   encryptInfo = inputStream2String(
          licenseFile.getInputStream());

      BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
      textEncryptor.setPassword(getRandomString());

      String[] decryptInfo = StringUtils.delimitedListToStringArray(
          textEncryptor.decrypt(encryptInfo), "^");

      // // dbName
      // if (!dbName.equals(decryptInfo[0])) {
      // return false;
      // }
      //
      // // host name
      // if (!SystemUtil.getHostName().equals(decryptInfo[1])) {
      // return false;
      // }
      //
      // // mac addresses
      // if (!SystemUtil.containMacAddress(decryptInfo[2])) {
      // return false;
      // }

      // expiration date
      SimpleDateFormat sdf          = new SimpleDateFormat("yyyy-MM-dd");
      Date             expirateDate = sdf.parse(decryptInfo[3]);

      if (expirateDate.before(new Date())) {
        // expired
        return false;
      }

      return true;
    } catch (Exception e) { } // end try-catch


    return false;
  } // end method isSystemValid

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
   */
  @Override public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    this.ctx = ctx;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * set db name.
   *
   * @param  dbName  db name
   */
  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * convert from stream to string.
   *
   * @param   is  DOCUMENT ME!
   *
   * @return  convert from stream to string
   *
   * @throws  IOException
   */
  private String inputStream2String(InputStream is) throws IOException {
    BufferedReader in     = new BufferedReader(new InputStreamReader(is, "utf-8"));
    StringBuffer   buffer = new StringBuffer();
    String         line   = "";

    while ((line = in.readLine()) != null) {
      buffer.append(line);
    }

    return buffer.toString();
  }
} // end class SysInfoServiceImpl
