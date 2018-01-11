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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.path.Path;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Groups;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.Species;

/**
 * Specialized repository for querying samples of a specific type.
 *
 * @author Antony Holmes Holmes
 */
public class RestrictedTypeRepository extends EDBRepository {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  private Set<Type> mTypes;

  /**
   * Instantiates a new chip seq repository.
   *
   * @param login the login
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public RestrictedTypeRepository(EDBWLogin login, Type type)
      throws IOException {
    super(login);

    mTypes = CollectionUtils.toSet(type);
  }

  @Override
  public List<Sample> searchSamples(String query,
      Path path,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Groups groups) throws IOException {
    return super.searchSamples(query, path, mTypes, organisms, groups);
  }
}
