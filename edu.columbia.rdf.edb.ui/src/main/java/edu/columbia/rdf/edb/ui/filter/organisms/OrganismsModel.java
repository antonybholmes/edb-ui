package edu.columbia.rdf.edb.ui.filter.organisms;

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

import edu.columbia.rdf.edb.Species;
import edu.columbia.rdf.edb.ui.Repository;

public class OrganismsModel extends ChangeListeners implements Iterable<Species>, XmlRepresentation {
	private static final long serialVersionUID = 1L;

	protected Map<Species, Boolean> mTypeMap = new TreeMap<Species, Boolean>();
	
	
	@Override
	public Iterator<Species> iterator() {
		return mTypeMap.keySet().iterator();
		
	}
	
	public boolean getUse(Species t) {
		if (mTypeMap.containsKey(t)) {
			return mTypeMap.get(t);
		} else {
			return false;
		}
	}
	
	public void setUse(Species t, boolean use) {
		updateUse(t, use);
		
		fireChanged();
	}
	
	public void updateUse(Species t, boolean use) {
		mTypeMap.put(t, use);
	}

	/**
	 * Return the selected groups.
	 * 
	 * @return
	 */
	public Collection<Species> getOrganisms() {
		Set<Species> ret = new HashSet<Species>(mTypeMap.size());
		
		for (Species t : this) {
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
			return Repository.ALL_ORGANISMS;
		}
	}
	
	@Override
	public Element toXml(Document doc) {
		Element element = doc.createElement("organisms");

		for (Species t : this) {
			Element te = doc.createElement("organism");
			
			te.setAttribute("name", t.getName());
			te.setAttribute("selected", Boolean.toString(getUse(t)));
			
			element.appendChild(te);
		}

		return element;
	}
}
