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
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.annotation.Entity;
import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.json.JsonParser;

// TODO: Auto-generated Javadoc
/**
 * Caches data if not present.
 *
 * @author Antony Holmes
 * @param <T> the generic type
 */
public abstract class EntitiesCache<T extends Entity> {
  
  /** The m entities. */
  protected Map<Integer, List<T>> mEntities = ArrayListMultiMap.create();

  /** The m json parser. */
  protected JsonParser mJsonParser = new JsonParser();

  /**
   * Gets the.
   *
   * @param id the id
   * @return the list
   */
  public List<T> get(int id) {
    List<T> entries = mEntities.get(id);

    // System.err.println("id " + id + " " + entries.size());

    if (entries.size() > 0) {
      return entries;
    }

    // Cache miss so try to reload cache
    try {
      cache();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return mEntities.get(id);
  }

  /**
   * Cache.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public abstract void cache() throws IOException, ParseException;

  /**
   * Clear.
   */
  public void clear() {
    mEntities.clear();
  }
}
