package edu.columbia.rdf.edb.ui.filter.sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jebtk.core.event.ChangeListeners;

import edu.columbia.rdf.edb.SampleSet;

public abstract class SetsModel extends ChangeListeners
    implements Iterable<SampleSet> {
  private static final long serialVersionUID = 1L;

  protected Map<SampleSet, Boolean> mSampleSetMap = new TreeMap<SampleSet, Boolean>();

  @Override
  public Iterator<SampleSet> iterator() {
    return mSampleSetMap.keySet().iterator();

  }

  public boolean getUse(SampleSet g) {
    if (mSampleSetMap.containsKey(g)) {
      return mSampleSetMap.get(g);
    } else {
      return false;
    }
  }

  public void setUse(SampleSet g, boolean use) {
    updateUse(g, use);

    fireChanged();
  }

  public void updateUse(SampleSet g, boolean use) {
    mSampleSetMap.put(g, use);
  }

  /**
   * Return the selected SampleSets.
   * 
   * @return
   */
  public Collection<SampleSet> getSampleSets() {
    Set<SampleSet> ret = new HashSet<SampleSet>(mSampleSetMap.size());

    for (SampleSet g : this) {
      if (getUse(g)) {
        ret.add(g);
      }
    }

    return ret;
  }


}
