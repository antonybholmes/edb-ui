package edu.columbia.rdf.edb.ui.network;

import org.abh.common.pool.ObjectPool;

import edu.columbia.rdf.edb.ui.Repository;

public class SharedConnectionService {
	private ObjectPool<Repository> mPool = null;

	private static final SharedConnectionService instance = new SharedConnectionService();

	public static final SharedConnectionService getInstance() {

		return instance;
	}

	private SharedConnectionService() {

		// do nothing
	}
	
	public void setPool(ObjectPool<Repository> pool) {
		mPool = pool;
	}
	
	public ObjectPool<Repository> getPool() {
		return mPool;
	}
}
