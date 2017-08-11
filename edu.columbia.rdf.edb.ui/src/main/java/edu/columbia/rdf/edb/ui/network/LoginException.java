package edu.columbia.rdf.edb.ui.network;

import edu.columbia.rdf.edb.ui.ExperimentSearchException;

/**
 * Logins entries for a connection to a caArray server.
 *
 * @author Antony Holmes Holmes
 *
 */

public class LoginException extends ExperimentSearchException {
	private static final long serialVersionUID = 1L;


	public LoginException(String message) {
		super(message);
	}
}
