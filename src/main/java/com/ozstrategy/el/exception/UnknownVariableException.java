package com.ozstrategy.el.exception;


/**
 * DOCUMENT ME!
 *
 * @author   Rojer Luo
 * @version  $Revision$, $Date$
 */
public class UnknownVariableException extends RuntimeException {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  String unknownVariables;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new NotSupportException object.
   *
   * @param  names  DOCUMENT ME!
   */
  public UnknownVariableException(String names) {
    super("Unknown Variable(s): '" + names + "'");
    unknownVariables = names;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getUnknownVariables() {
    return unknownVariables;
  }
} // end class UnknownVariableException
