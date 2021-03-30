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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.annotation.Entity;
import org.jebtk.core.json.JsonParser;

// TODO: Auto-generated Javadoc
/**
 * Caches data if not present.
 *
 * @author Antony Holmes
 * @param <T> the generic type
 */
public abstract class EntityCache<T extends Entity> implements Iterable<T> {
  
  /** The m entities map. */
  protected Map<Integer, T> mEntitiesMap = new HashMap<Integer, T>();
  
  /** The m entities. */
  protected List<T> mEntities = new ArrayList<T>(1000);

  /** The m json parser. */
  protected JsonParser mJsonParser = new JsonParser();

  /**
   * Put.
   *
   * @param id the id
   * @param entity the entity
   */
  public void put(int id, T entity) {
    mEntitiesMap.put(id, entity);
    mEntities.add(entity);
  }

  /**
   * Gets the.
   *
   * @param id the id
   * @return the t
   */
  public T get(int id) {
    T entity = mEntitiesMap.get(id);

    if (entity != null) {
      return entity;
    }

    // Cache miss so try to reload cache
    try {
      cache();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return mEntitiesMap.get(id);
  }

  /**
   * Cache.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract void cache() throws IOException;

  /**
   * Clear.
   */
  public void clear() {
    mEntitiesMap.clear();
    mEntities.clear();
  }

  /**
   * Return the values in the cache.
   * 
   * @return A collection of the values in the cache.
   */
  public Collection<T> getValues() {
    try {
      cache();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return mEntities;
  }

  /* (non-Javadoc)
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<T> iterator() {
    return mEntities.iterator();
  }
}
