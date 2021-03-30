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

import edu.columbia.rdf.edb.EDB;
import edu.columbia.rdf.edb.Species;

// TODO: Auto-generated Javadoc
/**
 * Keep track of organims we download from the database.
 * 
 * @author Antony Holmes
 *
 */
public class OrganismsCache extends EntityCache<Species> {
  
  /** The m url. */
  private URL mUrl;

  /**
   * Instantiates a new organisms cache.
   *
   * @param url the url
   */
  public OrganismsCache(URL url) {
    mUrl = url;
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.cache.EntityCache#cache()
   */
  @Override
  public void cache() throws IOException {
    if (mEntities.size() > 0) {
      return;
    }

    Json json = mJsonParser.parse(mUrl);

    for (int i = 0; i < json.size(); ++i) {
      Json sectionTypeJSON = json.get(i);

      int id = sectionTypeJSON.get(EDB.HEADING_ID).getInt();
      String name = sectionTypeJSON.get(EDB.HEADING_NAME_SHORT).getString();
      String scientificName = sectionTypeJSON.get("sn").getString();

      put(id, new Species(id, name, scientificName));
    }
  }
}
