package com.ozstrategy.el.util;

import java.math.BigDecimal;

import java.util.Date;

import com.ozstrategy.el.exception.NotSupportException;


/**
 * Created with IntelliJ IDEA. User: rojer Date: 6/6/13 Time: 11:15 PM To change this template use File | Settings |
 * File Templates.
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class CompareUtil {
  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(BigDecimal left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean equalsTo(Double left, Double right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean equalsTo(Float left, Float right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Object left, Object right) {
    if ((left == null) && (right == null)) {
      return true;
    } else if ((left != null) && (right != null)) {
      if (left instanceof BigDecimal) {
        if (right instanceof BigDecimal) {
          return equalsTo((BigDecimal) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return equalsTo((BigDecimal) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return equalsTo((BigDecimal) left, new BigDecimal(((Float) right).doubleValue()));
        } else if (right instanceof Long) {
          return equalsTo((BigDecimal) left, (Long) right);
        } else if (right instanceof Integer) {
          return equalsTo((BigDecimal) left, (Integer) right);
        } else if (right instanceof String) {
          return equalsTo((BigDecimal) left, new BigDecimal((String) right));
        }
      } else if (left instanceof Double) {
        if (right instanceof BigDecimal) {
          return equalsTo(new BigDecimal(((Double) left).doubleValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return equalsTo((Double) left, (Double) right);
        } else if (right instanceof Float) {
          return equalsTo(new BigDecimal(((Double) left).doubleValue()), new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return equalsTo(new BigDecimal(((Double) left).doubleValue()), (Long) right);
        } else if (right instanceof Integer) {
          return equalsTo(new BigDecimal(((Double) left).doubleValue()), (Integer) right);
        } else if (right instanceof String) {
          return equalsTo(new BigDecimal(((Double) left).doubleValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Float) {
        if (right instanceof BigDecimal) {
          return equalsTo(new BigDecimal(((Float) left).floatValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return equalsTo(new BigDecimal(((Float) left).floatValue()), new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return equalsTo((Float) left, (Float) right);
        } else if (right instanceof Long) {
          return equalsTo(new BigDecimal(((Float) left).floatValue()), (Long) right);
        } else if (right instanceof Integer) {
          return equalsTo(new BigDecimal(((Float) left).floatValue()), (Integer) right);
        } else if (right instanceof String) {
          return equalsTo(new BigDecimal(((Float) left).floatValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Long) {
        if (right instanceof BigDecimal) {
          return equalsTo((Long) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return equalsTo((Long) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return equalsTo((Long) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return equalsTo((Long) left, (Long) right);
        } else if (right instanceof Integer) {
          return equalsTo((Long) left, (Integer) right);
        } else if (right instanceof String) {
          return equalsTo((Long) left, Long.valueOf((String) right));
        }
      } else if (left instanceof Integer) {
        if (right instanceof BigDecimal) {
          return equalsTo((Integer) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return equalsTo((Integer) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return equalsTo((Integer) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return equalsTo((Integer) left, (Long) right);
        } else if (right instanceof Integer) {
          return equalsTo((Integer) left, (Integer) right);
        } else if (right instanceof String) {
          return equalsTo((Integer) left, Integer.valueOf((String) right));
        }
      } else if (left instanceof Boolean) {
        if (right instanceof Boolean) {
          return equalsTo((Boolean) left, (Boolean) right);
        }
      } else if (left instanceof String) {
        if (right instanceof Date) {
          if (CalculateUtil.isValidDateOnlyString((String) left)) {
            return equalsTo(CalculateUtil.toDate((String) left), CalculateUtil.toDateOnly((Date) right));
          } else if (CalculateUtil.isValidDateTimeString((String) left)) {
            return equalsTo(CalculateUtil.toDate((String) left), (Date) right);
          }
        } else if (right instanceof String) {
          return equalsTo((String) left, (String) right);
        }
      } else if (left instanceof Date) {
        if (right instanceof Date) {
          return equalsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDateOnly((Date) right));
        } else if (right instanceof String) {
          if (CalculateUtil.isValidDateOnlyString((String) right)) {
            return equalsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDate((String) right));
          } else if (CalculateUtil.isValidDateTimeString((String) right)) {
            return equalsTo((Date) left, CalculateUtil.toDate((String) right));
          }
        }
      } // end if-else
    }   // end if-else

    return false;
  } // end method equalsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */

  public static Boolean equalsTo(Long left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Long left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(Long.valueOf(right)) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Integer left, Long right) {
    if ((left != null) && (right != null)) {
      return ((Long.valueOf(left)).compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Integer left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Boolean left, Boolean right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(String left, String right) {
    if ((left != null) && (right != null)) {
      return left.equalsIgnoreCase(right);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Date left, Date right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(BigDecimal left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(BigDecimal left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Long left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean equalsTo(Integer left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) == 0);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Object left, Object right) {
    if ((left != null) && (right != null)) {
      if (left instanceof BigDecimal) {
        if (right instanceof BigDecimal) {
          return greaterThan((BigDecimal) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThan((BigDecimal) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThan((BigDecimal) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThan((BigDecimal) left, (Long) right);
        } else if (right instanceof Integer) {
          return greaterThan((BigDecimal) left, (Integer) right);
        } else if (right instanceof String) {
          return greaterThan((BigDecimal) left, new BigDecimal((String) right));
        }
      } else if (left instanceof Double) {
        if (right instanceof BigDecimal) {
          return greaterThan(new BigDecimal(((Double) left).doubleValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThan((Double) left, (Double) right);
        } else if (right instanceof Float) {
          return greaterThan(new BigDecimal(((Double) left).doubleValue()),
              new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThan(new BigDecimal(((Double) left).doubleValue()), (Long) right);
        } else if (right instanceof Integer) {
          return greaterThan(new BigDecimal(((Double) left).doubleValue()), (Integer) right);
        } else if (right instanceof String) {
          return greaterThan(new BigDecimal(((Double) left).doubleValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Float) {
        if (right instanceof BigDecimal) {
          return greaterThan(new BigDecimal(((Float) left).floatValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThan(new BigDecimal(((Float) left).floatValue()),
              new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThan((Float) left, (Float) right);
        } else if (right instanceof Long) {
          return greaterThan(new BigDecimal(((Float) left).floatValue()), (Long) right);
        } else if (right instanceof Integer) {
          return greaterThan(new BigDecimal(((Float) left).floatValue()), (Integer) right);
        } else if (right instanceof String) {
          return greaterThan(new BigDecimal(((Float) left).floatValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Long) {
        if (right instanceof BigDecimal) {
          return greaterThan((Long) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThan((Long) left, (new BigDecimal(((Double) right).doubleValue())));
        } else if (right instanceof Float) {
          return greaterThan((Long) left, (new BigDecimal(((Float) right).floatValue())));
        } else if (right instanceof Long) {
          return greaterThan((Long) left, (Long) right);
        } else if (right instanceof Integer) {
          return greaterThan((Long) left, (Integer) right);
        } else if (right instanceof String) {
          return greaterThan((Long) left, Long.valueOf((String) right));
        }
      } else if (left instanceof Integer) {
        if (right instanceof BigDecimal) {
          return greaterThan((Integer) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThan((Integer) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThan((Integer) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThan((Integer) left, (Long) right);
        } else if (right instanceof Integer) {
          return greaterThan((Integer) left, (Integer) right);
        } else if (right instanceof String) {
          return greaterThan((Integer) left, Integer.valueOf((String) right));
        }
      } else if (left instanceof String) {
        if (right instanceof Date) {
          if (CalculateUtil.isValidDateOnlyString((String) left)) {
            return greaterThan(CalculateUtil.toDate((String) left), CalculateUtil.toDateOnly((Date) right));
          } else if (CalculateUtil.isValidDateTimeString((String) left)) {
            return greaterThan(CalculateUtil.toDate((String) left), (Date) right);
          }
        } else if (right instanceof String) {
          return greaterThan((String) left, (String) right);
        }
      } else if (left instanceof Date) {
        if (right instanceof Date) {
          return greaterThan(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDateOnly((Date) right));
        } else if (right instanceof String) {
          if (CalculateUtil.isValidDateOnlyString((String) right)) {
            return greaterThan(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDate((String) right));
          } else if (CalculateUtil.isValidDateTimeString((String) right)) {
            return greaterThan((Date) left, CalculateUtil.toDate((String) right));
          }
        }
      } // end if-else
    }   // end if

    throw new NotSupportException();
  } // end method greaterThan

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(BigDecimal left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Double left, Double right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Float left, Float right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Long left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Long left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(Long.valueOf(right)) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Integer left, Long right) {
    if ((left != null) && (right != null)) {
      return ((Long.valueOf(left)).compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Integer left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Date left, Date right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(String left, String right) {
    if ((left != null) && (right != null)) {
      return ((left.toLowerCase()).compareTo(right.toLowerCase()) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(BigDecimal left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(BigDecimal left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Long left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThan(Integer left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) >= 1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Object left, Object right) {
    if ((left != null) && (right != null)) {
      if (left instanceof BigDecimal) {
        if (right instanceof BigDecimal) {
          return greaterThanOrEqualsTo((BigDecimal) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThanOrEqualsTo((BigDecimal) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThanOrEqualsTo((BigDecimal) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThanOrEqualsTo((BigDecimal) left, (Long) right);
        } else if (right instanceof Integer) {
          return greaterThanOrEqualsTo((BigDecimal) left, (Integer) right);
        } else if (right instanceof String) {
          return greaterThanOrEqualsTo((BigDecimal) left, new BigDecimal((String) right));
        }
      } else if (left instanceof Double) {
        if (right instanceof BigDecimal) {
          return greaterThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThanOrEqualsTo((Double) left, (Double) right);
        } else if (right instanceof Float) {
          return greaterThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()),
              new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), (Long) right);
        } else if (right instanceof Integer) {
          return greaterThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), (Integer) right);
        } else if (right instanceof String) {
          return greaterThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Float) {
        if (right instanceof BigDecimal) {
          return greaterThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()),
              new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThanOrEqualsTo((Float) left, (Float) right);
        } else if (right instanceof Long) {
          return greaterThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), (Long) right);
        } else if (right instanceof Integer) {
          return greaterThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), (Integer) right);
        } else if (right instanceof String) {
          return greaterThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Long) {
        if (right instanceof BigDecimal) {
          return greaterThanOrEqualsTo((Long) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThanOrEqualsTo((Long) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThanOrEqualsTo((Long) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThanOrEqualsTo((Long) left, (Long) right);
        } else if (right instanceof Integer) {
          return greaterThanOrEqualsTo((Long) left, (Integer) right);
        } else if (right instanceof String) {
          return greaterThanOrEqualsTo((Long) left, Long.valueOf((String) right));
        }
      } else if (left instanceof Integer) {
        if (right instanceof BigDecimal) {
          return greaterThanOrEqualsTo((Integer) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return greaterThanOrEqualsTo((Integer) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return greaterThanOrEqualsTo((Integer) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return greaterThanOrEqualsTo((Integer) left, (Long) right);
        } else if (right instanceof Integer) {
          return greaterThanOrEqualsTo((Integer) left, (Integer) right);
        } else if (right instanceof String) {
          return greaterThanOrEqualsTo((Integer) left, Integer.valueOf((String) right));
        }
      } else if (left instanceof String) {
        if (right instanceof Date) {
          if (CalculateUtil.isValidDateOnlyString((String) left)) {
            return greaterThanOrEqualsTo(CalculateUtil.toDate((String) left), CalculateUtil.toDateOnly((Date) right));
          } else if (CalculateUtil.isValidDateTimeString((String) left)) {
            return greaterThanOrEqualsTo(CalculateUtil.toDate((String) left), (Date) right);
          }
        } else if (right instanceof String) {
          return greaterThanOrEqualsTo((String) left, (String) right);
        }
      } else if (left instanceof Date) {
        if (right instanceof Date) {
          return greaterThanOrEqualsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDateOnly((Date) right));
        } else if (right instanceof String) {
          if (CalculateUtil.isValidDateOnlyString((String) right)) {
            return greaterThanOrEqualsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDate((String) right));
          } else if (CalculateUtil.isValidDateTimeString((String) right)) {
            return greaterThanOrEqualsTo((Date) left, CalculateUtil.toDate((String) right));
          }
        }
      } // end if-else
    }   // end if

    return ((left == null) && (right == null));
  } // end method greaterThanOrEqualsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(BigDecimal left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Double left, Double right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Float left, Float right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Long left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Long left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(Long.valueOf(right)) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Integer left, Long right) {
    if ((left != null) && (right != null)) {
      return ((Long.valueOf(left)).compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Integer left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Date left, Date right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(String left, String right) {
    if ((left != null) && (right != null)) {
      return ((left.toLowerCase()).compareTo(right.toLowerCase()) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(BigDecimal left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(BigDecimal left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Long left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean greaterThanOrEqualsTo(Integer left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) > -1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean in(BigDecimal left, BigDecimal[] right) {
    if ((left != null) && (right != null)) {
      for (BigDecimal item : right) {
        if (left.compareTo(item) == 0) {
          return true;
        }
      }

      return false;
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean in(Long left, Long[] right) {
    if ((left != null) && (right != null)) {
      for (Long item : right) {
        if (left.compareTo(item) == 0) {
          return true;
        }
      }

      return false;
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean in(Integer left, Integer[] right) {
    if ((left != null) && (right != null)) {
      for (Integer item : right) {
        if (left.compareTo(item) == 0) {
          return true;
        }
      }

      return false;
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean in(String left, String[] right) {
    if ((left != null) && (right != null)) {
      for (String item : right) {
        if (left.equals(item)) {
          return true;
        }
      }

      return false;
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean in(Date left, Date[] right) {
    if ((left != null) && (right != null)) {
      for (Date item : right) {
        if (left.equals(item)) {
          return true;
        }
      }

      return false;
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean in(Object left, Object[] right) {
    if ((left != null) && (right != null)) {
      for (Object item : right) {
        if (item.getClass().equals(left.getClass()) && left.equals(item)) {
          return true;
        }
      }

      return false;
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Object left, Object right) {
    if ((left != null) && (right != null)) {
      if (left instanceof BigDecimal) {
        if (right instanceof BigDecimal) {
          return lessThan((BigDecimal) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThan((BigDecimal) left, (new BigDecimal(((Double) right).doubleValue())));
        } else if (right instanceof Float) {
          return lessThan((BigDecimal) left, (new BigDecimal(((Float) right).floatValue())));
        } else if (right instanceof Long) {
          return lessThan((BigDecimal) left, (Long) right);
        } else if (right instanceof Integer) {
          return lessThan((BigDecimal) left, (Integer) right);
        } else if (right instanceof String) {
          return lessThan((BigDecimal) left, new BigDecimal((String) right));
        }
      } else if (left instanceof Double) {
        if (right instanceof BigDecimal) {
          return lessThan((new BigDecimal(((Double) left).doubleValue())), (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThan((Double) left, (Double) right);
        } else if (right instanceof Float) {
          return lessThan(new BigDecimal(((Double) left).doubleValue()), new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThan(new BigDecimal(((Double) left).doubleValue()), (Long) right);
        } else if (right instanceof Integer) {
          return lessThan(new BigDecimal(((Double) left).doubleValue()), (Integer) right);
        } else if (right instanceof String) {
          return lessThan(new BigDecimal(((Double) left).doubleValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Float) {
        if (right instanceof BigDecimal) {
          return lessThan(new BigDecimal(((Float) left).floatValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThan(new BigDecimal(((Float) left).floatValue()), new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThan((Float) left, (Float) right);
        } else if (right instanceof Long) {
          return lessThan((new BigDecimal(((Float) left).floatValue())), (Long) right);
        } else if (right instanceof Integer) {
          return lessThan((new BigDecimal(((Float) left).floatValue())), (Integer) right);
        } else if (right instanceof String) {
          return lessThan((new BigDecimal(((Float) left).floatValue())), new BigDecimal((String) right));
        }
      } else if (left instanceof Long) {
        if (right instanceof BigDecimal) {
          return lessThan((Long) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThan((Long) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThan((Long) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThan((Long) left, (Long) right);
        } else if (right instanceof Integer) {
          return lessThan((Long) left, (Integer) right);
        } else if (right instanceof String) {
          return lessThan((Long) left, Long.valueOf((String) right));
        }
      } else if (left instanceof Integer) {
        if (right instanceof BigDecimal) {
          return lessThan((Integer) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThan((Integer) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThan((Integer) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThan((Integer) left, (Long) right);
        } else if (right instanceof Integer) {
          return lessThan((Integer) left, (Integer) right);
        } else if (right instanceof String) {
          return lessThan((Integer) left, Integer.valueOf((String) right));
        }
      } else if (left instanceof String) {
        if (right instanceof Date) {
          if (CalculateUtil.isValidDateOnlyString((String) left)) {
            return lessThan(CalculateUtil.toDate((String) left), CalculateUtil.toDateOnly((Date) right));
          } else if (CalculateUtil.isValidDateTimeString((String) left)) {
            return lessThan(CalculateUtil.toDate((String) left), (Date) right);
          }
        } else if (right instanceof String) {
          return lessThan((String) left, (String) right);
        }
      } else if (left instanceof Date) {
        if (right instanceof Date) {
          return lessThan(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDateOnly((Date) right));
        } else if (right instanceof String) {
          if (CalculateUtil.isValidDateOnlyString((String) right)) {
            return lessThan(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDate((String) right));
          } else if (CalculateUtil.isValidDateTimeString((String) right)) {
            return lessThan((Date) left, CalculateUtil.toDate((String) right));
          }
        }
      } // end if-else
    }   // end if

    throw new NotSupportException();
  } // end method lessThan

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(BigDecimal left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Double left, Double right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean lessThan(Float left, Float right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Long left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(Long.valueOf(right)) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Integer left, Long right) {
    if ((left != null) && (right != null)) {
      return ((Long.valueOf(left)).compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Long left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Integer left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Date left, Date right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(String left, String right) {
    if ((left != null) && (right != null)) {
      return ((left.toLowerCase()).compareTo(right.toLowerCase()) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(BigDecimal left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(BigDecimal left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Long left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThan(Integer left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) <= -1);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Object left, Object right) {
    if ((left != null) && (right != null)) {
      if (left instanceof BigDecimal) {
        if (right instanceof BigDecimal) {
          return lessThanOrEqualsTo((BigDecimal) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThanOrEqualsTo((BigDecimal) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThanOrEqualsTo((BigDecimal) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThanOrEqualsTo((BigDecimal) left, (Long) right);
        } else if (right instanceof Integer) {
          return lessThanOrEqualsTo((BigDecimal) left, (Integer) right);
        } else if (right instanceof String) {
          return lessThanOrEqualsTo((BigDecimal) left, new BigDecimal((String) right));
        }
      } else if (left instanceof Double) {
        if (right instanceof BigDecimal) {
          return lessThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThanOrEqualsTo((Double) left, (Double) right);
        } else if (right instanceof Float) {
          return lessThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()),
              new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), (Long) right);
        } else if (right instanceof Integer) {
          return lessThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), (Integer) right);
        } else if (right instanceof String) {
          return lessThanOrEqualsTo(new BigDecimal(((Double) left).doubleValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Float) {
        if (right instanceof BigDecimal) {
          return lessThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()),
              new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThanOrEqualsTo((Float) left, (Float) right);
        } else if (right instanceof Long) {
          return lessThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), (Long) right);
        } else if (right instanceof Integer) {
          return lessThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), (Integer) right);
        } else if (right instanceof String) {
          return lessThanOrEqualsTo(new BigDecimal(((Float) left).floatValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Long) {
        if (right instanceof BigDecimal) {
          return lessThanOrEqualsTo((Long) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThanOrEqualsTo((Long) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThanOrEqualsTo((Long) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThanOrEqualsTo((Long) left, (Long) right);
        } else if (right instanceof Integer) {
          return lessThanOrEqualsTo((Long) left, (Integer) right);
        } else if (right instanceof String) {
          return lessThanOrEqualsTo((Long) left, Long.valueOf((String) right));
        }
      } else if (left instanceof Integer) {
        if (right instanceof BigDecimal) {
          return lessThanOrEqualsTo((Integer) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return lessThanOrEqualsTo((Integer) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return lessThanOrEqualsTo((Integer) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return lessThanOrEqualsTo((Integer) left, (Long) right);
        } else if (right instanceof Integer) {
          return lessThanOrEqualsTo((Integer) left, (Integer) right);
        } else if (right instanceof String) {
          return lessThanOrEqualsTo((Integer) left, Integer.valueOf((String) right));
        }
      } else if (left instanceof String) {
        if (right instanceof Date) {
          if (CalculateUtil.isValidDateOnlyString((String) left)) {
            return lessThanOrEqualsTo(CalculateUtil.toDate((String) left), CalculateUtil.toDateOnly((Date) right));
          } else if (CalculateUtil.isValidDateTimeString((String) left)) {
            return lessThanOrEqualsTo(CalculateUtil.toDate((String) left), (Date) right);
          }
        } else if (right instanceof String) {
          return lessThanOrEqualsTo((String) left, (String) right);
        }
      } else if (left instanceof Date) {
        if (right instanceof Date) {
          return lessThanOrEqualsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDateOnly((Date) right));
        } else if (right instanceof String) {
          if (CalculateUtil.isValidDateOnlyString((String) right)) {
            return lessThanOrEqualsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDate((String) right));
          } else if (CalculateUtil.isValidDateTimeString((String) right)) {
            return lessThanOrEqualsTo((Date) left, CalculateUtil.toDate((String) right));
          }
        }
      } // end if-else
    }   // end if

    return ((left == null) && (right == null));
  } // end method lessThanOrEqualsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(BigDecimal left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Double left, Double right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Float left, Float right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Long left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Integer left, Long right) {
    if ((left != null) && (right != null)) {
      return ((Long.valueOf(left)).compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Long left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(Long.valueOf(right)) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Integer left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Date left, Date right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(String left, String right) {
    if ((left != null) && (right != null)) {
      return ((left.toLowerCase()).compareTo(right.toLowerCase()) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(BigDecimal left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(BigDecimal left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Long left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean lessThanOrEqualsTo(Integer left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) < 1);
    }

    return ((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Object left, Object right) {
    if ((left == null) && (right == null)) {
      return false;
    } else if ((left != null) && (right != null)) {
      if (left instanceof BigDecimal) {
        if (right instanceof BigDecimal) {
          return notEqualsTo((BigDecimal) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return notEqualsTo((BigDecimal) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return notEqualsTo((BigDecimal) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return notEqualsTo((BigDecimal) left, (Long) right);
        } else if (right instanceof Integer) {
          return notEqualsTo((BigDecimal) left, (Integer) right);
        } else if (right instanceof String) {
          return notEqualsTo((BigDecimal) left, new BigDecimal((String) right));
        }
      } else if (left instanceof Double) {
        if (right instanceof BigDecimal) {
          return notEqualsTo(new BigDecimal(((Double) left).doubleValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return notEqualsTo((Double) left, (Double) right);
        } else if (right instanceof Float) {
          return notEqualsTo(new BigDecimal(((Double) left).doubleValue()),
              new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return notEqualsTo(new BigDecimal(((Double) left).doubleValue()), (Long) right);
        } else if (right instanceof Integer) {
          return notEqualsTo(new BigDecimal(((Double) left).doubleValue()), (Integer) right);
        } else if (right instanceof String) {
          return notEqualsTo(new BigDecimal(((Double) left).doubleValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Float) {
        if (right instanceof BigDecimal) {
          return notEqualsTo(new BigDecimal(((Float) left).floatValue()), (BigDecimal) right);
        } else if (right instanceof Double) {
          return notEqualsTo(new BigDecimal(((Float) left).floatValue()),
              new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return notEqualsTo((Float) left, (Float) right);
        } else if (right instanceof Long) {
          return notEqualsTo(new BigDecimal(((Float) left).floatValue()), (Long) right);
        } else if (right instanceof Integer) {
          return notEqualsTo(new BigDecimal(((Float) left).floatValue()), (Integer) right);
        } else if (right instanceof String) {
          return notEqualsTo(new BigDecimal(((Float) left).floatValue()), new BigDecimal((String) right));
        }
      } else if (left instanceof Long) {
        if (right instanceof BigDecimal) {
          return notEqualsTo((Long) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return notEqualsTo((Long) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return notEqualsTo((Long) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return notEqualsTo((Long) left, (Long) right);
        } else if (right instanceof Integer) {
          return notEqualsTo((Long) left, (Integer) right);
        } else if (right instanceof String) {
          return notEqualsTo((Long) left, Long.valueOf((String) right));
        }
      } else if (left instanceof Integer) {
        if (right instanceof BigDecimal) {
          return notEqualsTo((Integer) left, (BigDecimal) right);
        } else if (right instanceof Double) {
          return notEqualsTo((Integer) left, new BigDecimal(((Double) right).doubleValue()));
        } else if (right instanceof Float) {
          return notEqualsTo((Integer) left, new BigDecimal(((Float) right).floatValue()));
        } else if (right instanceof Long) {
          return notEqualsTo((Integer) left, (Long) right);
        } else if (right instanceof Integer) {
          return notEqualsTo((Integer) left, (Integer) right);
        } else if (right instanceof String) {
          return notEqualsTo((Integer) left, Integer.valueOf((String) right));
        }
      } else if (left instanceof Boolean) {
        if (right instanceof Boolean) {
          return notEqualsTo((Boolean) left, (Boolean) right);
        }
      } else if (left instanceof String) {
        if (right instanceof Date) {
          if (CalculateUtil.isValidDateOnlyString((String) left)) {
            return notEqualsTo(CalculateUtil.toDate((String) left), CalculateUtil.toDateOnly((Date) right));
          } else if (CalculateUtil.isValidDateTimeString((String) left)) {
            return notEqualsTo(CalculateUtil.toDate((String) left), (Date) right);
          }
        } else if (right instanceof String) {
          return notEqualsTo((String) left, (String) right);
        }
      } else if (left instanceof Date) {
        if (right instanceof Date) {
          return notEqualsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDateOnly((Date) right));
        } else if (right instanceof String) {
          if (CalculateUtil.isValidDateOnlyString((String) right)) {
            return notEqualsTo(CalculateUtil.toDateOnly((Date) left), CalculateUtil.toDate((String) right));
          } else if (CalculateUtil.isValidDateTimeString((String) right)) {
            return notEqualsTo((Date) left, CalculateUtil.toDate((String) right));
          }
        }
      } // end if-else
    }   // end if

    return true;
  } // end method notEqualsTo

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(BigDecimal left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Double left, Double right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Float left, Float right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Long left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Integer left, Long right) {
    if ((left != null) && (right != null)) {
      return ((Long.valueOf(left).compareTo(right) != 0));
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Integer left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left).compareTo(right) != 0));
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Long left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(Long.valueOf(right)) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Integer left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Boolean left, Boolean right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(String left, String right) {
    if ((left != null) && (right != null)) {
      return !(left.toLowerCase()).equals(right.toLowerCase());
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Date left, Date right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(BigDecimal left, Long right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(BigDecimal left, Integer right) {
    if ((left != null) && (right != null)) {
      return (left.compareTo(new BigDecimal(right)) != 0);
    }

    return !((left == null) && (right == null));
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   left   DOCUMENT ME!
   * @param   right  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  NotSupportException  DOCUMENT ME!
   */
  public static Boolean notEqualsTo(Long left, BigDecimal right) {
    if ((left != null) && (right != null)) {
      return ((new BigDecimal(left)).compareTo(right) != 0);
    }

    return !((left == null) && (right == null));
  }
} // end class CompareUtil
