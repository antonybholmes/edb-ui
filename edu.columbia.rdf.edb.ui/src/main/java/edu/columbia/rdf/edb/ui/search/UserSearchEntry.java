package edu.columbia.rdf.edb.ui.search;

import org.jebtk.core.path.Path;
import org.jebtk.core.search.SearchStackOperator;
import org.jebtk.core.xml.XmlRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Describes part of a search and how it is logically linked to other terms.
 * 
 * @author Antony Holmes
 *
 */
public class UserSearchEntry
    implements XmlRepresentation, Comparable<UserSearchEntry> {
  public static final SearchCategory DEFAULT_FIELD = new SearchCategory(
      "All Categories", new Path("/All"));

  public static final SearchStackOperator DEFAULT_BOOL_TERM = SearchStackOperator.AND;

  private SearchStackOperator mOperator;
  private String mSearch;
  private SearchCategory mField;

  /**
   * Creates a new search entry.
   *
   * @param operator
   * @param path
   * @param value
   * @throws MatchStackException
   */
  public UserSearchEntry(SearchStackOperator operator, SearchCategory field,
      String search) {

    mOperator = operator;
    mField = field;
    mSearch = search;
  }

  /**
   * Returns an operator which must be one of the logical operators (and, or,
   * xor etc).
   * 
   * @return
   */
  public final SearchStackOperator getOperator() {
    return mOperator;
  }

  public final SearchCategory getField() {
    return mField;
  }

  //
  // Factory methods
  //

  public static UserSearchEntry createDefaultSearchEntry() {
    return new UserSearchEntry(DEFAULT_BOOL_TERM, DEFAULT_FIELD, "");
  }

  public static UserSearchEntry create(SearchCategory field) {
    return create(DEFAULT_BOOL_TERM, field, "");
  }
  
  public static UserSearchEntry create(String search) {
    return create(DEFAULT_BOOL_TERM, DEFAULT_FIELD, search);
  }

  public static UserSearchEntry create(SearchStackOperator operator,
      SearchCategory field,
      String text) {
    return new UserSearchEntry(operator, field, text);
  }

  public int compareTo(UserSearchEntry o) {
    if (!mField.equals(o.getField())) {
      return mField.compareTo(o.getField());
    }

    return mSearch.compareTo(o.getText());
  }

  public String getText() {
    return mSearch;
  }

  @Override
  public Element toXml(Document doc) {
    Element searchEntry = doc.createElement("user-search");

    searchEntry.setAttribute("operator", mOperator.toString());
    searchEntry.setAttribute("search", mSearch);
    searchEntry.setAttribute("field-name", mField.getName());
    searchEntry.setAttribute("field-path", mField.getPath().toString());

    return searchEntry;
  }
}
