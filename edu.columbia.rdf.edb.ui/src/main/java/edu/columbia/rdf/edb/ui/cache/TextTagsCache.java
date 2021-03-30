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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.jebtk.core.http.URLPath;
import org.jebtk.core.http.URLUtils;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;

import edu.columbia.rdf.edb.Tag;

// TODO: Auto-generated Javadoc
/**
 * The Class TagsCache.
 */
public class TextTagsCache extends TagsCache {
  /**
   * Instantiates a new tags cache.
   *
   * @param url the url
   */
  public TextTagsCache(URLPath url) {
    super(url.param("format", "text"));
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.cache.EntityCache#cache()
   */
  @Override
  public void cache() throws IOException {
    if (mEntities.size() > 0) {
      return;
    }

    Splitter split = Splitter.on(TextUtils.COLON_DELIMITER).limit(2);
    
    BufferedReader reader = URLUtils.newBufferedReader(mUrl);

    String line;
    List<String> tokens;
    int id;
    Tag tag;
    String name;
    
    while ((line = reader.readLine()) != null) {
      tokens = split.text(line);
      
      id = Integer.parseInt(tokens.get(0));
      name = tokens.get(1);

      tag = new Tag(id, name);

      put(id, tag);
      
      mPathMap.put(tag.getPath(), tag);
    }
  }
}
