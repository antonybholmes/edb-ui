package edu.columbia.rdf.edb.ui.network;

import org.abh.common.pool.ObjectCreator;

import edu.columbia.rdf.edb.ui.Repository;

public class SharedConnection implements ObjectCreator<Repository> {
	protected Repository mConnection;

	public SharedConnection(Repository connection) {
		mConnection = connection;
	}
	
	public Repository create() {
		return mConnection;
	}
}
