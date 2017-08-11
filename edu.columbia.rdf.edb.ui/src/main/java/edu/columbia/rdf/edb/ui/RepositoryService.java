package edu.columbia.rdf.edb.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores a global instance of an experiment
 * repository.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class RepositoryService {
	/**
	 * The default repository name.
	 */
	public static final String DEFAULT_REP = "all";
	
	private Map<String, Repository> mRepositoryMap =
			new HashMap<String, Repository>();

	private Repository mDefaultRep;

	private static class RepositoryServiceLoader {
		private static final RepositoryService INSTANCE = new RepositoryService();
	}

	/**
	 * Gets the single instance of SettingsService.
	 *
	 * @return single instance of SettingsService
	 */
	public static RepositoryService getInstance() {
		return RepositoryServiceLoader.INSTANCE;
	}

	private RepositoryService() {
		// do nothing
	}
	
	public void setRepository(String name, Repository repository) {
		mRepositoryMap.put(name, repository);
		
		System.err.println("repository " + name + " " + (repository != null));
		
		mDefaultRep = repository;
	}
	
	public Repository getRepository() {
		return getRepository(RepositoryService.DEFAULT_REP);
	}
	
	public Repository getRepository(String name) {
		return mRepositoryMap.get(name);
	}
	
	public Repository getCurrentRepository() {
		return mDefaultRep;
	}
}
