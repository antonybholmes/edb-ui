package edu.columbia.rdf.edb.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jebtk.core.io.Io;

import edu.columbia.rdf.edb.ui.network.ServerException;

/**
 * Allows a repository to be restored from disk using serialization.
 * 
 * @author Antony Holmes Holmes
 *
 */
public abstract class RepositorySession {
  public static final File SESSION_FILE = new File("cache/session.dat");

  public Repository restore() throws IOException, ClassNotFoundException, ServerException {
    return restore(SESSION_FILE);
  }

  /**
   * Deletes the session file to force it to be reloaded
   */
  public static void clear() {
    Io.delete(SESSION_FILE);
  }

  public abstract Repository restore(File sessionFile)
      throws FileNotFoundException, IOException, ClassNotFoundException, ServerException;
}
