package edu.columbia.rdf.edb.ui.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a named group of search fields.
 *
 * @author Antony Holmes
 *
 */
public class SearchCategoryGroup
    implements Iterable<SearchCategory>, Comparable<SearchCategoryGroup> {
  private String mName;

  private boolean mDisplay;

  private List<SearchCategory> mCategories = new ArrayList<SearchCategory>();

  private Map<String, SearchCategory> mMap = new HashMap<String, SearchCategory>();

  public SearchCategoryGroup(String name) {
    this(name, true);
  }

  public SearchCategoryGroup(String name, boolean display) {
    mName = name;
    mDisplay = display;
  }

  public void addCategory(SearchCategory category) {
    if (mMap.containsKey(category.getName().toLowerCase())) {
      return;
    }

    mCategories.add(category);

    mMap.put(category.getName().toLowerCase(), category);
  }

  /**
   * Returns true if the group should be displayed.
   *
   * @return
   */
  public boolean display() {
    return mDisplay;
  }

  public final String getName() {

    return mName;
  }

  public int compareTo(SearchCategoryGroup g) {
    return mName.compareTo(g.getName());
  }

  public SearchCategory getSearchCategory(String name) {
    return mMap.get(name.toLowerCase());
  }

  public Iterator<SearchCategory> iterator() {
    return mCategories.iterator();
  }
}
