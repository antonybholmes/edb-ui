package edu.columbia.rdf.edb.ui.search;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jebtk.core.path.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all of the search fields available for the search component.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SearchCategoryService implements Iterable<SearchCategoryGroup> {
  public static final File DEFAULT_SEARCH_CATEGORIES_XML_FILE = new File("res/search.categories.xml");

  private final static Logger LOG = LoggerFactory.getLogger(SearchCategoryService.class);

  private List<SearchCategoryGroup> groups = new ArrayList<SearchCategoryGroup>();

  private Map<String, SearchCategoryGroup> groupMap = new HashMap<String, SearchCategoryGroup>();

  private Map<String, SearchCategory> categoryMap = new HashMap<String, SearchCategory>();

  private Map<Path, SearchCategory> pathMap = new HashMap<Path, SearchCategory>();

  private static SearchCategoryService instance = new SearchCategoryService();

  public static SearchCategoryService getInstance() {
    return instance;
  }

  public void addGroup(SearchCategoryGroup group) {
    groups.add(group);

    groupMap.put(group.getName(), group);

    for (SearchCategory category : group) {
      categoryMap.put(category.getName(), category);
      pathMap.put(category.getPath(), category);
    }
  }

  public SearchCategoryGroup getGroup(String name) {
    return groupMap.get(name);
  }

  public SearchCategory get(Path path) {
    return pathMap.get(path);
  }

  public void loadXml(File file) {
    LOG.info("Parsing search catergories in {}...", file);

    clear();

    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();

      SearchCategoryXmlHandler handler = new SearchCategoryXmlHandler();

      saxParser.parse(file.getAbsolutePath(), handler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void clear() {
    groups.clear();
    groupMap.clear();
    categoryMap.clear();
  }

  public SearchCategory getSearchCategory(String name) {
    return categoryMap.get(name);
  }

  @Override
  public Iterator<SearchCategoryGroup> iterator() {
    return groups.iterator();
  }
}
