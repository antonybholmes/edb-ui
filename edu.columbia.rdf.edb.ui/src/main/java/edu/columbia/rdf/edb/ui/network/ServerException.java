package edu.columbia.rdf.edb.ui.network;

import edu.columbia.rdf.edb.ui.ExperimentSearchException;

/**
 * To be thrown when we cannot connect to a server
 *
 * @author Antony Holmes Holmes
 *
 */
public class ServerException extends ExperimentSearchException {
	private static final long serialVersionUID = 1L;

	public ServerException(String message) {
		super(message);
	}
}
