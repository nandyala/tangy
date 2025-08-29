package com.ozstrategy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA. User: rojer Date: Mar 19, 2010 Time: 10:51:47 PM To change this template use File |
 * Settings | File Templates.
 */
public class IoUtil {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * Convert byte stream serializable object.
   *
   * @param   stream  byte stream
   *
   * @return  Serializable object
   */
  public static Serializable getSerializable(byte[] stream) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(stream);
      ObjectInputStream ois = new ObjectInputStream(bais);

      return (Serializable) ois.readObject();
    }
    catch (Exception e) {
      return null;
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Convert serializable object to byte array.
   *
   * @param   serializable  serializable object
   *
   * @return  converted byte array
   */
  public static byte[] toByteArray(Serializable serializable) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = null;
      oos = new ObjectOutputStream(baos);
      oos.writeObject(serializable);
      oos.flush();

      return baos.toByteArray();
    }
    catch (IOException e) {
      return null;
    }
  }
} // end class IoUtil
