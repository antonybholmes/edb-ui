package edu.columbia.rdf.edb.ui.filter.datatypes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.xml.XmlRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.edb.ui.Repository;

public class DataTypesModel extends ChangeListeners
    implements Iterable<Type>, XmlRepresentation {
  private static final long serialVersionUID = 1L;

  protected Map<Type, Boolean> mTypeMap = new TreeMap<Type, Boolean>();

  @Override
  public Iterator<Type> iterator() {
    return mTypeMap.keySet().iterator();

  }

  public boolean getUse(Type t) {
    if (mTypeMap.containsKey(t)) {
      return mTypeMap.get(t);
    } else {
      return false;
    }
  }

  public void setUse(Type t, boolean use) {
    updateUse(t, use);

    fireChanged();
  }

  public void updateUse(Type t, boolean use) {
    mTypeMap.put(t, use);
  }

  /**
   * Return the selected groups.
   * 
   * @return
   */
  public Collection<Type> getTypes() {
    Set<Type> ret = new HashSet<Type>(mTypeMap.size());

    for (Type t : this) {
      if (getUse(t)) {
        ret.add(t);
      }
    }

    // If all of the groups are selected, it is faster to return the
    // empty set of groups to indicate all groups should be searched
    // since this requires fewer filtering checks.
    if (ret.size() < mTypeMap.size()) {
      return ret;
    } else {
      return Repository.ALL_TYPES;
    }
  }

  @Override
  public Element toXml(Document doc) {
    Element element = doc.createElement("types");

    for (Type t : this) {
      Element te = doc.createElement("type");

      te.setAttribute("name", t.getName());
      te.setAttribute("selected", Boolean.toString(getUse(t)));

      element.appendChild(te);
    }

    return element;
  }
}
