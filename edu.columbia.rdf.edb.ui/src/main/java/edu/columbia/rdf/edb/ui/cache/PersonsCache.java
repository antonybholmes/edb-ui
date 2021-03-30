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

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Person;

// TODO: Auto-generated Javadoc
/**
 * The Class PersonsCache.
 */
public class PersonsCache extends EntityCache<Person> {
  
  /** The m login. */
  private EDBWLogin mLogin;

  /**
   * Instantiates a new persons cache.
   *
   * @param login the login
   */
  public PersonsCache(EDBWLogin login) {
    mLogin = login;
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.ui.cache.EntityCache#cache()
   */
  @Override
  public void cache() throws IOException {
    URL url = mLogin.getURL().join("persons").toURL();

    Json json = new JsonParser().parse(url);

    System.err.println("persons url " + url);

    for (int i = 0; i < json.size(); ++i) {
      Json personJSON = json.get(i);

      int personId = personJSON.getInt("id");

      String firstName = personJSON.getString("first_name");
      String lastName = personJSON.getString("last_name");
      String email = personJSON.getString("email");

      Person person = new Person(personId, firstName, lastName, email);

      put(personId, person);
    }
  }
}
