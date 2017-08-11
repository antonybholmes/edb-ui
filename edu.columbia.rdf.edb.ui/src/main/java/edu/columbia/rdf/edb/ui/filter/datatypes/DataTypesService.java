package edu.columbia.rdf.edb.ui.filter.datatypes;

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
import org.abh.common.bioinformatics.annotation.Type;
import org.abh.common.event.ChangeEvent;
import org.abh.common.event.ChangeListener;
import org.abh.common.io.FileUtils;
import org.abh.common.io.PathUtils;
import org.abh.common.xml.XmlUtils;
import org.xml.sax.SAXException;

import edu.columbia.rdf.edb.ui.RepositoryService;

public class DataTypesService extends DataTypesModel implements ChangeListener {
	private static final long serialVersionUID = 1L;

	private static class ServiceLoader {
		private static final DataTypesService INSTANCE = new DataTypesService();
	}

	public static DataTypesService getInstance() {
		return ServiceLoader.INSTANCE;
	}

	private static final Path DATA_TYPES_FILE = 
			PathUtils.getPath("res/default.data.types.xml");

	private static final Path USER_DATA_TYPES_FILE = 
			AppService.getInstance().getFile("user.data.types.xml");

	private boolean mAutoLoad = true;

	private DataTypesService() {
		addChangeListener(this);
	}

	@Override
	public Iterator<Type> iterator() {
		autoLoad();

		return mTypeMap.keySet().iterator();

	}

	private void autoLoad() {
		if (!mAutoLoad) {
			return;
		}

		Collection<Type> groups = RepositoryService
				.getInstance()
				.getCurrentRepository()
				.getDataTypes();

		for (Type g : groups) {
			mTypeMap.put(g, true);
		}

		mAutoLoad = false;

		try {
			autoLoadXml(DATA_TYPES_FILE);
			autoLoadXml(USER_DATA_TYPES_FILE);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
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

		DataTypesXmlHandler handler = new DataTypesXmlHandler(this);

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
		XmlUtils.writeXml(this, USER_DATA_TYPES_FILE);
	}
}
