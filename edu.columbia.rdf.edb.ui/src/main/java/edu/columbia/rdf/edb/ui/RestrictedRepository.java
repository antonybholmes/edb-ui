/**
 * Copyright 2016 Antony Holmes
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
package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.path.Path;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Groups;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.Species;

/**
 * Specialized repository for querying ChIP-seq only samples.
 *
 * @author Antony Holmes Holmes
 */
public class RestrictedRepository extends EDBRepository {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  private Path mPath;

  /**
   * Instantiates a new chip seq repository.
   *
   * @param login the login
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public RestrictedRepository(EDBWLogin login, Path path) throws IOException {
    super(login);

    mPath = path;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.lib.rdf.edb.Repository#searchSamples(java.lang.String)
   */
  @Override
  public List<Sample> searchSamples(String query) throws IOException {
    return searchSamples(query, mPath, ALL_TYPES, ALL_ORGANISMS, ALL_GROUPS);
  }

  @Override
  public List<Sample> searchSamples(String query,
      Path path,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Groups groups) throws IOException {
    URL url = getSearchSamplesUrl().param("p", path.toString())
        .param("q", query).param("ext", "persons").param("ext", "tags").toUrl();

    System.err.println(url);

    Json json = new JsonParser().parse(url);

    return parseSampleJson(json);
  }
}
