package edu.columbia.rdf.edb.ui.search;

import java.util.List;

import org.jebtk.core.search.SearchStackElement;

import edu.columbia.rdf.edb.Sample;

/**
 * Captures the what the user is searching for as both a string and queue.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class Search {
	public String search;
	public List<SearchStackElement<Sample>> searchQueue;

	public Search(String search, 
			List<SearchStackElement<Sample>> searchQueue) {
		this.search = search;
		this.searchQueue = searchQueue;
	}
}
