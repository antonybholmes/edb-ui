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
import java.util.HashMap;
import java.util.Map;

import org.jebtk.core.http.URLPath;
import org.jebtk.core.path.Path;

import edu.columbia.rdf.edb.Tag;

// TODO: Auto-generated Javadoc
/**
 * The Class TagsCache.
 */
public abstract class TagsCache extends EntityCache<Tag> {
  /** The m url. */
  protected URLPath mUrl;

  /** The m path map. */
  protected Map<Path, Tag> mPathMap = new HashMap<Path, Tag>();

  /**
   * Instantiates a new tags cache.
   *
   * @param url the url
   */
  public TagsCache(URLPath url) {
    mUrl = url;
  }

  /**
   * Gets the tag.
   *
   * @param path the path
   * @return the tag
   */
  public Tag getTag(Path path) {
    try {
      cache();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return mPathMap.get(path);
  }
}
