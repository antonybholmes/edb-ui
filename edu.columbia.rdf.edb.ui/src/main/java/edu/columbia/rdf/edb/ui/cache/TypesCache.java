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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.http.URLPath;
import org.jebtk.core.json.Json;

import edu.columbia.rdf.edb.EDB;

// TODO: Auto-generated Javadoc
/**
 * The Class TypesCache.
 */
public class TypesCache extends EntityCache<Type> {
  
  /** The m url. */
  protected URL mUrl;

  /**
   * Instantiates a new types cache.
   *
   * @param typesUrl the types url
   * @param type the type
   * @throws MalformedURLException the malformed URL exception
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public TypesCache(URLPath typesUrl, String type)
      throws MalformedURLException, UnsupportedEncodingException {
    mUrl = typesUrl.join(type).toURL();
  }

  /**
   * Instantiates a new types cache.
   *
   * @param typesUrl the types url
   * @param type1 the type 1
   * @param type2 the type 2
   * @throws MalformedURLException the malformed URL exception
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public TypesCache(URLPath typesUrl, String type1, String type2)
      throws MalformedURLException, UnsupportedEncodingException {
    mUrl = typesUrl.join(type1).join(type2).toURL();
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

      put(id, new Type(id, name));
    }
  }
}
