package edu.columbia.rdf.edb.ui;

import org.jebtk.core.MessageException;

/**
 * All Array Search errors should derive from this.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class ExperimentSearchException extends MessageException {
  private static final long serialVersionUID = 1L;

  public ExperimentSearchException(String message) {
    super(message);
  }

}
