package edu.columbia.rdf.edb.ui.filter.groups;

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

import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.ui.RepositoryService;

public class GroupsService extends GroupsModel implements ChangeListener {
  private static final long serialVersionUID = 1L;

  private static class ServiceLoader {
    private static final GroupsService INSTANCE = new GroupsService();
  }

  public static GroupsService getInstance() {
    return ServiceLoader.INSTANCE;
  }

  private static final Path GROUPS_FILE = PathUtils
      .getPath("res/groups.xml");

  private static final Path USER_GROUPS_FILE = PathUtils
      .getPath("groups.xml"); //AppService.getInstance().getFile("groups.xml");

  private boolean mAutoLoad = true;

  private Collection<Group> mAllGroups;

  private GroupsService() {
    addChangeListener(this);
  }

  @Override
  public Iterator<Group> iterator() {
    autoLoad();

    return mGroupMap.keySet().iterator();
  }

  private void autoLoad() {
    if (!mAutoLoad) {
      return;
    }
    
    mAutoLoad = false;

    Collection<Group> groups = RepositoryService.getInstance()
        .getCurrentRepository().getUserGroups();

    for (Group g : groups) {
      mGroupMap.put(g, false);
    }

    
    // Override or add groups using configurable files.
    
    try {
      autoLoadXml(GROUPS_FILE);
      autoLoadXml(USER_GROUPS_FILE);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    // Maintain a list of all groups regardless of whether user is part of
    // them or not
    mAllGroups = RepositoryService.getInstance().getCurrentRepository()
        .getGroups();
  }

  private void autoLoadXml(Path file)
      throws SAXException, IOException, ParserConfigurationException {
    if (!FileUtils.exists(file)) {
      return;
    }
    
    InputStream is = FileUtils.newBufferedInputStream(file);

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();

      GroupsXmlHandler handler = new GroupsXmlHandler(this);

      saxParser.parse(is, handler);
    } finally {
      is.close();
    }
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
    XmlUtils.writeXml(this, USER_GROUPS_FILE);
  }

  @Override
  public Collection<Group> getAllGroups() {
    return mAllGroups;
  }
}
