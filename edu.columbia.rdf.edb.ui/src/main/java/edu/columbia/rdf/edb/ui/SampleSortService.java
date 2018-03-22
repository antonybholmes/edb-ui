/**
 * Copyright 2017 Antony Holmes
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

import edu.columbia.rdf.edb.ui.sort.SortSamplesByExperiment;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByExpressionType;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByGEOPlatform;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByGEOSeries;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByGroup;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByName;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByOrganism;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByPerson;

// TODO: Auto-generated Javadoc
/**
 * The Class SampleSortService.
 */
public class SampleSortService extends SampleSortModel {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant INSTANCE. */
  private static final SampleSortService INSTANCE = new SampleSortService();

  /**
   * Gets the single instance of SampleSortService.
   *
   * @return single instance of SampleSortService
   */
  public static final SampleSortService getInstance() {
    return INSTANCE;
  }

  /**
   * Instantiates a new sample sort service.
   */
  private SampleSortService() {
    add(new SortSamplesByExperiment());
    add(new SortSamplesByExpressionType());
    add(new SortSamplesByName());
    add(new SortSamplesByOrganism());
    add(new SortSamplesByPerson());
    add(new SortSamplesByGEOSeries());
    add(new SortSamplesByGEOPlatform());
    add(new SortSamplesByGroup());

    /*
     * add(new SortSamplesByArrayPlatform()); add(new
     * SortSamplesBySourceCellType()); add(new
     * SortSamplesBySourceDiseaseState()); add(new
     * SortSamplesBySourceDiseaseStatus()); add(new
     * SortSamplesBySourceGender()); add(new
     * SortSamplesByMicroarrayBasedClassification()); add(new
     * SortSamplesBySourceMaterialType()); add(new
     * SortSamplesBySourceOrganismPart());
     */

    setSorter("Sample Name");
  }
}
