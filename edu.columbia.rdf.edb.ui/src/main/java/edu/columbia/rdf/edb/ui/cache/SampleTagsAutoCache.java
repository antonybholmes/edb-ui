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
import java.util.Iterator;

import org.jebtk.core.path.Path;

import edu.columbia.rdf.edb.SampleTag;
import edu.columbia.rdf.edb.SampleTags;
import edu.columbia.rdf.edb.ui.RepositoryService;

// TODO: Auto-generated Javadoc
/**
 * Attempts to auto load tags from database if they don't exist.
 * 
 * @author Antony Holmes
 *
 */
public class SampleTagsAutoCache extends SampleTags {

  /** The m tags cache. */
  private TagsCache mTagsCache;
  
  /** The m sample id. */
  private int mSampleId;

  /**
   * Instantiates a new sample tags auto cache.
   *
   * @param sampleId the sample id
   */
  public SampleTagsAutoCache(int sampleId) {
    mSampleId = sampleId;

    mTagsCache = ((CacheRepository) RepositoryService.getInstance()
        .getRepository()).getTagsCache();
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.SampleTags#getTag(org.jebtk.core.path.Path)
   */
  @Override
  public SampleTag getTag(Path path) {
    if (!mMap.containsKey(path)) {
      //Tag tag = mTagsCache.getTag(path);

      try {
        //RepositoryService.getInstance().getRepository()
        //    .cacheTag(mSampleId, tag.getId(), this);
        RepositoryService.getInstance().getRepository()
        .cacheTags(mSampleId, this);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return mMap.get(path);
  }

  /* (non-Javadoc)
   * @see edu.columbia.rdf.edb.SampleTags#iterator()
   */
  @Override
  public Iterator<SampleTag> iterator() {
    cache();

    return super.iterator();
  }

  /**
   * Cache.
   */
  private void cache() {
    if (mMap.size() == 0) {
      try {
        RepositoryService.getInstance()
            .getRepository(RepositoryService.DEFAULT_REP)
            .cacheTags(mSampleId, this);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
