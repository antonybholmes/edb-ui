package edu.columbia.rdf.edb.ui.search;

import org.jebtk.core.path.Path;
import org.jebtk.core.xml.XmlRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A search category has a name and a path pointing into a keyword database,
 *
 * @author Antony Holmes
 *
 */
public class SearchCategory
    implements XmlRepresentation, Comparable<SearchCategory> {
  private String mName;
  private Path mPath;
  private String mDescription;

  public SearchCategory(String name, Path path, String description) {
    mName = name;
    mPath = path;
    mDescription = description;
  }

  public SearchCategory(String name, Path path) {
    this(name, path, name);
  }

  public final String getName() {
    return mName;
  }

  public Path getPath() {
    return mPath;
  }

  public int compareTo(SearchCategory g) {
    return mPath.compareTo(g.getPath());
  }

  public String getDescription() {
    return mDescription;
  }

  @Override
  public Element toXml(Document doc) {
    Element category = doc.createElement("category");

    category.appendChild(doc.createElement("path")
        .appendChild(doc.createTextNode(mPath.toString())));
    category.appendChild(doc.createElement("description")
        .appendChild(doc.createTextNode(mDescription)));

    return category;
  }
}
