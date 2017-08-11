package edu.columbia.rdf.edb.ui.search;

import org.abh.common.search.SearchStackOperator;

public class OperatorException extends MatchStackException {
	private static final long serialVersionUID = 1L;

	public OperatorException(SearchStackOperator operator) {
		super("Badly formed expression. " + SearchStackOperator.getOperator(operator) + " is not allowed.");
	}
	
	public OperatorException(String operator) {
		super("Badly formed expression. " + operator.toUpperCase() + " is not allowed.");
	}
}
