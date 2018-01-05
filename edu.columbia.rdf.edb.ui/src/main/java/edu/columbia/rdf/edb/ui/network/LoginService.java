package edu.columbia.rdf.edb.ui.network;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jebtk.core.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LoginService implements Iterable<Login> {
  private List<Login> logins = new ArrayList<Login>();

  private Path mFile;

  private static final LoginService instance = new LoginService();

  public static final LoginService getInstance() {

    return instance;
  }

  public LoginService() {

    // do nothing
  }

  /**
   * Returns a list of the recent files.
   *
   * @return a list of recent files.
   */
  public final int size() {

    return logins.size();
  }

  /**
   * Adds a new login.
   *
   * @param login
   */
  public final void add(Login login) {

    if (logins.contains(login)) {
      return;
    }

    logins.add(login);
  }

  public void write() throws ParserConfigurationException, TransformerException {
    write(true, false);
  }

  /**
   * Writes login details to file. It has the option of writing user names and
   * passwords along with the server and port which are mandatory.
   *
   * @param writeUsers
   * @param writePasswords
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws TransformerException
   */
  public final void write(boolean writeUsers, boolean writePasswords)
      throws ParserConfigurationException, TransformerException {

    // create the root

    Document doc = XmlUtils.createDoc(); // new XmlDocument(serversElement);

    Element serversElement = doc.createElement("servers");

    doc.appendChild(serversElement);

    for (Login login : logins) {
      serversElement.appendChild(login.toXml(doc));
    }

    XmlUtils.writeXml(doc, mFile);
  }

  public final Iterator<Login> iterator() {
    return logins.iterator();
  }

  public final Login get(int i) {
    if (i < 0 || i >= logins.size()) {
      return null;
    }

    return logins.get(i);
  }
}
