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
import java.util.Collection;
import java.util.Date;

import org.jebtk.bioinformatics.annotation.Species;
import org.jebtk.bioinformatics.annotation.Type;

import edu.columbia.rdf.edb.Experiment;
import edu.columbia.rdf.edb.GEO;
import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.RepositoryService;

// TODO: Auto-generated Javadoc
/**
 * The Class SampleAutoCache.
 */
public class SampleAutoCache extends Sample {

  /**
   * Instantiates a new sample auto cache.
   *
   * @param id the id
   * @param experiment the experiment
   * @param expression the expression
   * @param name the name
   * @param organism the organism
   * @param date the date
   */
  public SampleAutoCache(int id, Experiment experiment, Type expression,
      String name, Species organism, Date date) {
    super(id, experiment, expression, name, organism, date);

    mTags = new SampleTagsAutoCache(id);
  }

  /*
   * @Override public SampleTags getTags() { if (mTags.size() == 0) { try {
   * RepositoryService.getInstance().getRepository().cacheTags(mId, mTags); }
   * catch (IOException e) { e.printStackTrace(); } }
   * 
   * return mTags; }
   */

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.Sample#getPersons()
   */
  @Override
  public Collection<Person> getPersons() {
    if (mPersons.size() == 0) {
      try {
        RepositoryService.getInstance().getRepository().cachePersons(mId,
            mPersons);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return super.getPersons();
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.Sample#getGEO()
   */
  @Override
  public GEO getGEO() {
    if (mGeo == null) {
      try {
        RepositoryService.getInstance().getRepository().cacheGEO(this);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return mGeo;
  }

}
