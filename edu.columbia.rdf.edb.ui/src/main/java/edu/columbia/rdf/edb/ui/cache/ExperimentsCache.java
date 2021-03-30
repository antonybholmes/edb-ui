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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jebtk.core.json.Json;

import edu.columbia.rdf.edb.EDB;
import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Experiment;

/**
 * The Class ExperimentsCache.
 */
public class ExperimentsCache extends EntityCache<Experiment> {
  
  /** The m login. */
  private EDBWLogin mLogin;

  /**
   * Instantiates a new experiments cache.
   *
   * @param login the login
   */
  public ExperimentsCache(EDBWLogin login) {
    mLogin = login;
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.cache.EntityCache#cache()
   */
  @Override
  public void cache() throws IOException {
    URL url = mLogin.getURL().join("experiments").toURL();

    System.err.println(url);

    Json json = mJsonParser.parse(url);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    for (int i = 0; i < json.size(); ++i) {
      // System.err.println(i + " " + json.get(i));

      // ensure the id is cast to an int before converting to a sting
      int id = json.get(i).getInt(EDB.HEADING_ID);

      String publicId = json.get(i).getString(EDB.HEADING_PID);

      String name = json.get(i).getString(EDB.HEADING_NAME_SHORT);

      String description = json.get(i).getString(EDB.HEADING_DESCRIPTION);

      try {
        Date date = formatter.parse(json.get(i).getString(EDB.HEADING_DATE));

        Experiment experiment = new Experiment(id, publicId, name, description,
            date);

        put(experiment.getId(), experiment);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      // System.err.println("cache " + experiment.getId());
    }
  }
}
