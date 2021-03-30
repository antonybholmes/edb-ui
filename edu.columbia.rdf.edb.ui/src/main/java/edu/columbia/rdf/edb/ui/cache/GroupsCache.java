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

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;

import edu.columbia.rdf.edb.EDB;
import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Group;

/**
 * The Class GroupsCache stores available groups.
 */
public class GroupsCache extends EntityCache<Group> {
  
  /** The m login. */
  private EDBWLogin mLogin;

  /**
   * Instantiates a new groups cache.
   *
   * @param login the login
   */
  public GroupsCache(EDBWLogin login) {
    mLogin = login;
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.cache.EntityCache#cache()
   */
  @Override
  public void cache() throws IOException {
    if (mEntities.size() > 0) {
      return;
    }

    URL url = mLogin.getURL().join("groups").toURL();

    Json json = new JsonParser().parse(url);

    for (int i = 0; i < json.size(); ++i) {
      Json groupJson = json.get(i);

      int id = groupJson.getInt(EDB.HEADING_ID);

      String name = groupJson.getString(EDB.HEADING_NAME_SHORT);

      Color color = groupJson.getColor(EDB.HEADING_COLOR);

      // System.err.println("g " + name + " " + color);

      Group group = new Group(id, name, color);

      put(id, group);
    }

    Collections.sort(mEntities);
  }
}
