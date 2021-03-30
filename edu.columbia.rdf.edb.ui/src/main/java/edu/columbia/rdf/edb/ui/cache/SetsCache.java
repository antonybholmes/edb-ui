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

import java.io.IOException;
import java.net.URL;

import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;

import edu.columbia.rdf.edb.EDB;
import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.SampleSet;

// TODO: Auto-generated Javadoc
/**
 * The Class TypesCache.
 */
public class SetsCache extends EntityCache<SampleSet> {
  
  /** The m login. */
  private EDBWLogin mLogin;
  
  /**
   * Instantiates a new sample persons cache.
   *
   * @param login the login
   * @param persons the persons
   */
  public SetsCache(EDBWLogin login) {
    mLogin = login;
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.cache.EntitiesCache#cache()
   */
  @Override
  public void cache() throws IOException {
    if (mEntities.size() > 0) {
      return;
    }

    URL url = mLogin.getURL().join("samples").join("sets").toURL();

    Json json = new JsonParser().parse(url);

    System.err.println("sets " + url);
    
    for (int i = 0; i < json.size(); ++i) {
      Json sampleJSON = json.get(i);
      
      int id = sampleJSON.get(EDB.HEADING_ID).getInt();
      String name = sampleJSON.get(EDB.HEADING_NAME_SHORT).getString();
      

      put(id, new SampleSet(id, name));
    }
  }
}
