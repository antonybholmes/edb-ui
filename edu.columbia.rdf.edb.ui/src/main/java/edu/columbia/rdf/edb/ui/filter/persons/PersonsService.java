package edu.columbia.rdf.edb.ui.filter.persons;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.xml.XmlUtils;
import org.xml.sax.SAXException;

import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.ui.RepositoryService;

public class PersonsService extends PersonsModel implements ChangeListener {
  private static final long serialVersionUID = 1L;

  private static class ServiceLoader {
    private static final PersonsService INSTANCE = new PersonsService();
  }

  public static PersonsService getInstance() {
    return ServiceLoader.INSTANCE;
  }

  private static final Path PERSONS_FILE = PathUtils
      .getPath("res/persons.xml");

  private static final Path USER_PERSONS_FILE = 
      PathUtils.getPath("user.persons.xml");

  private boolean mAutoLoad = true;

  private PersonsService() {
    addChangeListener(this);
  }

  @Override
  public Iterator<Person> iterator() {
    autoLoad();

    return mTypeMap.keySet().iterator();
  }

  private void autoLoad() {
    if (!mAutoLoad) {
      return;
    }

    Collection<Person> groups = RepositoryService.getInstance()
        .getRepository().getPersons();

    for (Person g : groups) {
      mTypeMap.put(g, true);
    }

    mAutoLoad = false;

    try {
      autoLoadXml(PERSONS_FILE);
      autoLoadXml(USER_PERSONS_FILE);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  private void autoLoadXml(Path file)
      throws SAXException, IOException, ParserConfigurationException {
    if (!FileUtils.exists(file)) {
      return;
    }

    InputStream stream = FileUtils.newBufferedInputStream(file);

    try {
      autoLoadXml(stream);
    } finally {
      stream.close();
    }
  }

  /**
   * Load xml.
   *
   * @param is the is
   * @param update the update
   * @return true, if successful
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParserConfigurationException the parser configuration exception
   */
  private synchronized boolean autoLoadXml(InputStream is)
      throws SAXException, IOException, ParserConfigurationException {
    if (is == null) {
      return false;
    }

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    PersonsXmlHandler handler = new PersonsXmlHandler(this);

    saxParser.parse(is, handler);

    return true;
  }

  @Override
  public void changed(ChangeEvent e) {
    try {
      // Save any user changes
      save();
    } catch (TransformerException | ParserConfigurationException e1) {
      e1.printStackTrace();
    }
  }

  private void save()
      throws TransformerException, ParserConfigurationException {
    XmlUtils.writeXml(this, USER_PERSONS_FILE);
  }
}
