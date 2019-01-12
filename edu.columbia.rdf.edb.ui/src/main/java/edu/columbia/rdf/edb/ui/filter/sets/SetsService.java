package edu.columbia.rdf.edb.ui.filter.sets;

import java.util.Iterator;

import edu.columbia.rdf.edb.SampleSet;
import edu.columbia.rdf.edb.ui.RepositoryService;

public class SetsService extends SetsModel {
  private static final long serialVersionUID = 1L;

  private static class ServiceLoader {
    private static final SetsService INSTANCE = new SetsService();
  }

  public static SetsService getInstance() {
    return ServiceLoader.INSTANCE;
  }

  private boolean mAutoLoad = true;

  private SetsService() {
    
  }

  @Override
  public Iterator<SampleSet> iterator() {
    autoLoad();

    return mSampleSetMap.keySet().iterator();
  }

  private void autoLoad() {
    if (!mAutoLoad) {
      return;
    }
    
    mAutoLoad = false;

    Iterable<SampleSet> set = RepositoryService.getInstance()
        .getRepository().getSets();

    for (SampleSet s : set) {
      mSampleSetMap.put(s, false);
    }
  }

}
