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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.jebtk.core.http.URLPath;

// TODO: Auto-generated Javadoc
/**
 * The Class FileTypesCache.
 */
public class FileTypesCache extends TypesCache {

  /**
   * Instantiates a new file types cache.
   *
   * @param typesUrl the types url
   * @throws MalformedURLException the malformed URL exception
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public FileTypesCache(URLPath typesUrl)
      throws MalformedURLException, UnsupportedEncodingException {
    super(typesUrl, "file");
  }

}
