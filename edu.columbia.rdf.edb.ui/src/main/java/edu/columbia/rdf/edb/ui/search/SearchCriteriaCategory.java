package edu.columbia.rdf.edb.ui.search;

import java.util.Deque;



public interface SearchCriteriaCategory {

	UserSearch getSearch();

	/**
	 * Loads a search and removes any existing ones.
	 * @param search
	 */
	void loadSearch(UserSearch search);

	/**
	 * Loads a complex search and appends to existing search.
	 *
	 * @param searchCriteria
	 */
	void addUserSearch(UserSearch userSearch);

	/**
	 * Adds an entry to the existing search.
	 *
	 * @param searchCriteria
	 */
	void addUserSearchEntry(UserSearchEntry searchCriteria);

	/**
	 * Gets a stack representation of the system.
	 *
	 * @return
	 */
	Deque<SearchStackElementCategory> getSearchStack() throws Exception;
}
