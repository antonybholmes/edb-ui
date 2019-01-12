package edu.columbia.rdf.edb.ui.filter.groups;

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

import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.ui.Repository;

public abstract class GroupsModel extends ChangeListeners
    implements Iterable<Group>, XmlRepresentation {
  private static final long serialVersionUID = 1L;

  protected Map<Group, Boolean> mGroupMap = new TreeMap<Group, Boolean>();

  @Override
  public Iterator<Group> iterator() {
    return mGroupMap.keySet().iterator();

  }

  public boolean getUse(Group g) {
    if (mGroupMap.containsKey(g)) {
      return mGroupMap.get(g);
    } else {
      return false;
    }
  }

  public void setUse(Group g, boolean use) {
    updateUse(g, use);

    fireChanged();
  }

  public void updateUse(Group g, boolean use) {
    mGroupMap.put(g, use);
  }

  /**
   * Return the selected groups.
   * 
   * @return
   */
  public Collection<Group> getGroups() {
    Set<Group> ret = new HashSet<Group>(mGroupMap.size());

    for (Group g : this) {
      if (getUse(g)) {
        ret.add(g);
      }
    }

    // If all of the groups are selected, it is faster to return the
    // empty set of groups to indicate all groups should be searched
    // since this requires fewer filtering checks.
    if (ret.size() < mGroupMap.size()) {
      return ret;
    } else {
      return Repository.ALL_GROUPS;
    }
  }

  public abstract Collection<Group> getAllGroups();

  @Override
  public Element toXml(Document doc) {
    Element element = doc.createElement("groups");

    for (Group g : this) {
      Element ge = doc.createElement("group");

      ge.setAttribute("name", g.getName());
      ge.setAttribute("selected", Boolean.toString(getUse(g)));

      element.appendChild(ge);
    }

    return element;
  }

}
