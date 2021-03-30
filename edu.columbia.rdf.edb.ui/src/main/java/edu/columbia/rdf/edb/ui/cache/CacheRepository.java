/**
 * Copyright 2018 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.edb.ui.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.http.URLPath;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Experiment;
import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTags;
import edu.columbia.rdf.edb.Species;
import edu.columbia.rdf.edb.ui.Repository;

// TODO: Auto-generated Javadoc
/**
 * Represents a database of experiments and samples. These can be from caArray
 * or some other database.
 *
 * @author Antony Holmes
 *
 */
public abstract class CacheRepository extends Repository {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m use file cache. */
  protected boolean mUseFileCache = false; // SettingsService.getInstance().getBool("edb.save-db-cache");

  /** The log. */
  protected final Logger LOG = LoggerFactory.getLogger(CacheRepository.class);

  // protected UrlBuilder mAuthUrl;

  /** The m version. */
  private int mVersion = -1;

  /** The m about url. */
  private URLPath mAboutUrl;

  /** The m types url. */
  protected URLPath mTypesUrl;

  /** The m species. */
  protected OrganismsCache mSpecies;
  
  protected GenomesCache mGenomes;
  
  protected SetsCache mSets;

  /** The m experiments. */
  protected ExperimentsCache mExperiments;

  /** The m tags. */
  protected TagsCache mTags;

  /** The m data types. */
  protected DataTypesCache mDataTypes;

  /** The m persons. */
  protected PersonsCache mPersons;

  /** The m file types. */
  protected FileTypesCache mFileTypes;

  /** The m geo platforms. */
  protected GeoPlatformCache mGeoPlatforms;

  /** The m geo series. */
  protected GeoSeriesCache mGeoSeries;

  /** The m login. */
  protected EDBWLogin mLogin;

  /** The m api url. */
  private URLPath mApiUrl;

  /** The m sample persons. */
  protected SamplePersonsCache mSamplePersons;

  /** The m groups. */
  protected GroupsCache mGroups;

  /** The m user groups. */
  private UserGroupsCache mUserGroups;

  /**
   * Instantiates a new cache repository.
   *
   * @param login the login
   * @throws MalformedURLException the malformed URL exception
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public CacheRepository(EDBWLogin login)
      throws MalformedURLException, UnsupportedEncodingException {
    mLogin = login;

    mApiUrl = login.getURL();

    // mAuthUrl = login.getAuthUrl();

    mExperiments = new ExperimentsCache(login);
    mPersons = new PersonsCache(login);
    mGroups = new GroupsCache(login);
    mUserGroups = new UserGroupsCache(login);
    mSamplePersons = new SamplePersonsCache(login, mPersons);
    mAboutUrl = mApiUrl.join("about");
    mTypesUrl = mApiUrl.join("types");
    mSpecies = new OrganismsCache(mTypesUrl.join("organisms").toURL());
    mGenomes = new GenomesCache(mTypesUrl.join("genomes").toURL());
    mTags = new JsonTagsCache(mTypesUrl.join("tags"));
    mSets = new SetsCache(login);
    //mTags = new TextTagsCache(mTypesUrl.resolve("tags"));
    mDataTypes = new DataTypesCache(mTypesUrl);
    mFileTypes = new FileTypesCache(mTypesUrl);

    mGeoPlatforms = new GeoPlatformCache(mTypesUrl);
    mGeoSeries = new GeoSeriesCache(mTypesUrl);

    // KeywordsService.getInstance().loadXml(KEYWORDS_FILE);
  }

  /**
   * Causes the database to be cached locally for speed.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void cache() throws IOException {
    clearCache();

    cacheVersion();
  }

  /**
   * Clear cache.
   */
  public void clearCache() {
    // experimentCache.clear();
    mExperiments.clear();
    mSpecies.clear();
    mTags.clear();
    mDataTypes.clear();
    mGroups.clear();
  }

  /**
   * Stores the current db version so that we know if there have been updates.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void cacheVersion() throws IOException {
    Json json;

    System.err.println("cahce " + mAboutUrl);
    json = new JsonParser().parse(mAboutUrl);

    setVersion(json.get("version").getInt());
  }

  /**
   * Gets the version.
   *
   * @return the version
   */
  public int getVersion() {
    return mVersion;
  }

  /**
   * Sets the version.
   *
   * @param version the new version
   */
  public void setVersion(int version) {
    mVersion = version;
  }

  /**
   * Gets the organism.
   *
   * @param id the id
   * @return the organism
   */
  public Species getOrganism(int id) {
    return mSpecies.get(id);
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.Repository#getExperiment(int)
   */
  public Experiment getExperiment(int id) {
    return mExperiments.get(id);
  }

  /**
   * Gets the expression.
   *
   * @param id the id
   * @return the expression
   */
  public Type getExpression(int id) {
    return mDataTypes.get(id);
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.Repository#getUserGroups()
   */
  @Override
  public Collection<Group> getUserGroups() {
    return mUserGroups.getValues();
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.Repository#getGroups()
   */
  @Override
  public Collection<Group> getGroups() {
    return mGroups.getValues();
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.Repository#saveSession(java.io.File)
   */
  @Override
  public void saveSession(File sessionFile) throws IOException {
    if (!mUseFileCache) {
      return;
    }

    System.err.println("Saving session to " + sessionFile);

    FileOutputStream f = new FileOutputStream(sessionFile);

    ObjectOutput s = new ObjectOutputStream(f);

    try {
      s.writeObject(this);

      s.flush();
      s.close();
    } finally {
      s.flush();
      s.close();
    }
  }

  /**
   * Cache sample fields.
   *
   * @param sample the sample
   * @return the sample tags
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract SampleTags cacheSampleFields(Sample sample)
      throws IOException;

  /**
   * Gets the tags cache.
   *
   * @return the tags cache
   */
  public TagsCache getTagsCache() {
    return mTags;
  }
}
