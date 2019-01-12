package edu.columbia.rdf.edb.ui.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jebtk.core.io.PathUtils;
import org.jebtk.core.path.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Represents all of the search fields available for the search component.
 * 
 * @author Antony Holmes
 *
 */
public class SearchCategoryService implements Iterable<SearchCategoryGroup> {
  public static final java.nio.file.Path SEARCH_CATEGORIES_XML_FILE = PathUtils
      .getPath("res/search.categories.xml");

  private final static Logger LOG = LoggerFactory
      .getLogger(SearchCategoryService.class);

  private List<SearchCategoryGroup> groups = new ArrayList<SearchCategoryGroup>();

  private Map<String, SearchCategoryGroup> groupMap = new HashMap<String, SearchCategoryGroup>();

  private Map<String, SearchCategory> categoryMap = new HashMap<String, SearchCategory>();

  private Map<Path, SearchCategory> pathMap = new HashMap<Path, SearchCategory>();

  private boolean mAutoLoad = true;

  private static class SearchCategoryServiceLoader {

    /** The Constant INSTANCE. */
    private static final SearchCategoryService INSTANCE = new SearchCategoryService();
  }

  /**
   * Gets the single instance of SettingsService.
   *
   * @return single instance of SettingsService
   */
  public static SearchCategoryService getInstance() {
    return SearchCategoryServiceLoader.INSTANCE;
  }

  private void autoLoad() {
    if (mAutoLoad) {
      // Set this here to stop recursive infinite calling
      // of this method.
      mAutoLoad = false;

      try {
        autoLoadXml();
      } catch (SAXException | IOException | ParserConfigurationException e) {
        e.printStackTrace();
      }
      // autoLoadJson();

    }
  }

  private void autoLoadXml()
      throws SAXException, IOException, ParserConfigurationException {
    loadXml(SEARCH_CATEGORIES_XML_FILE);
  }

  public void addGroup(SearchCategoryGroup group) {
    autoLoad();

    groups.add(group);

    groupMap.put(group.getName(), group);

    for (SearchCategory category : group) {
      categoryMap.put(category.getName(), category);
      pathMap.put(category.getPath(), category);
    }
  }

  public SearchCategoryGroup getGroup(String name) {
    autoLoad();

    return groupMap.get(name);
  }

  public SearchCategory get(Path path) {
    autoLoad();

    return pathMap.get(path);
  }

  public void loadXml(java.nio.file.Path file)
      throws SAXException, IOException, ParserConfigurationException {
    LOG.info("Parsing search catergories in {}...", file);

    // clear();

    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    SearchCategoryXmlHandler handler = new SearchCategoryXmlHandler();

    saxParser.parse(file.toFile(), handler);
  }

  private void clear() {
    groups.clear();
    groupMap.clear();
    categoryMap.clear();
  }

  public SearchCategory getSearchCategory(String name) {
    autoLoad();

    return categoryMap.get(name);
  }

  @Override
  public Iterator<SearchCategoryGroup> iterator() {
    autoLoad();

    return groups.iterator();
  }
}
