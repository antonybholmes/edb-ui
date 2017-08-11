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

import org.abh.common.AppService;
import org.abh.common.event.ChangeEvent;
import org.abh.common.event.ChangeListener;
import org.abh.common.io.FileUtils;
import org.abh.common.io.PathUtils;
import org.abh.common.xml.XmlUtils;
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

	private static final Path GROUPS_FILE = PathUtils.getPath("res/groups.xml");

	private static final Path USER_GROUPS_FILE = 
			AppService.getInstance().getFile("user.groups.xml");

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

		Collection<Group> groups = RepositoryService
				.getInstance()
				.getCurrentRepository()
				.getUserGroups();

		for (Group g : groups) {
			mGroupMap.put(g, false);
		}

		mAutoLoad = false;

		try {
			autoLoadXml(GROUPS_FILE);
			autoLoadXml(USER_GROUPS_FILE);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		mAllGroups = RepositoryService
				.getInstance()
				.getCurrentRepository()
				.getGroups();
	}

	private void autoLoadXml(Path file) throws SAXException, IOException, ParserConfigurationException {
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
	private synchronized boolean autoLoadXml(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		if (is == null) {
			return false;
		}

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		GroupsXmlHandler handler = new GroupsXmlHandler(this);

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

	private void save() throws TransformerException, ParserConfigurationException {
		XmlUtils.writeXml(this, USER_GROUPS_FILE);
	}

	@Override
	public Collection<Group> getAllGroups() {
		return mAllGroups;
	}
}
