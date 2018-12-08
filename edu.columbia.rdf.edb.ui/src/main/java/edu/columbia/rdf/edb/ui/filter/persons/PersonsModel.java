package edu.columbia.rdf.edb.ui.filter.persons;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.xml.XmlRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.ui.Repository;

public class PersonsModel extends ChangeListeners
    implements Iterable<Person>, XmlRepresentation {
  private static final long serialVersionUID = 1L;

  protected Map<Person, Boolean> mTypeMap = new TreeMap<Person, Boolean>();

  @Override
  public Iterator<Person> iterator() {
    return mTypeMap.keySet().iterator();

  }

  public boolean getUse(Person t) {
    if (mTypeMap.containsKey(t)) {
      return mTypeMap.get(t);
    } else {
      return false;
    }
  }

  public void setUse(Person t, boolean use) {
    updateUse(t, use);

    fireChanged();
  }

  public void updateUse(Person t, boolean use) {
    mTypeMap.put(t, use);
  }

  /**
   * Return the selected groups.
   * 
   * @return
   */
  public Collection<Person> getPersons() {
    Set<Person> ret = new HashSet<Person>(mTypeMap.size());

    for (Person t : this) {
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
      return Repository.ALL_PERSONS;
    }
  }

  @Override
  public Element toXml(Document doc) {
    Element element = doc.createElement("persons");

    for (Person t : this) {
      Element te = doc.createElement("person");

      te.setAttribute("name", t.getName());
      te.setAttribute("selected", Boolean.toString(getUse(t)));

      element.appendChild(te);
    }

    return element;
  }
}
