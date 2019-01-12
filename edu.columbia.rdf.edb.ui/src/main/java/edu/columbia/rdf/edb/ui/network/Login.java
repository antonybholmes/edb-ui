package edu.columbia.rdf.edb.ui.network;

import java.io.Serializable;

import org.jebtk.core.xml.XmlRepresentation;

/**
 * Logins entries for a connection to a server.
 *
 * @author Antony Holmes
 *
 */
public abstract class Login
    implements Comparable<Login>, XmlRepresentation, Serializable {
  private static final long serialVersionUID = 1L;

  public abstract String getUser();

  public abstract String getPassword();

  public abstract String getServer();

  public abstract int getPort();
}
