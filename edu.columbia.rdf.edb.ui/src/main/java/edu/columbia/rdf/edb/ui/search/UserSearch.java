package edu.columbia.rdf.edb.ui.search;

import java.util.ArrayList;

import org.jebtk.core.xml.XmlRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Describes all the terms in a search
 *
 * @author Antony Holmes Holmes
 *
 */
public class UserSearch extends ArrayList<UserSearchEntry> implements XmlRepresentation {
	private static final long serialVersionUID = 1L;

	// empty search for reuse
	public static final UserSearch NO_SEARCH_CRITERIA = new UserSearch();

	@Override
	public Element toXml(Document doc) {
		Element searchEntry = doc.createElement("user-search");
		
		for (UserSearchEntry entry : this) {
			searchEntry.appendChild(entry.toXml(doc));
		}
		
		return searchEntry;
	}
	
	
	public static UserSearch createDefaultSearch() {
		UserSearch userSearch = new UserSearch();
		
		userSearch.add(UserSearchEntry.createDefaultSearchEntry());
		
		return userSearch;
	}
}
