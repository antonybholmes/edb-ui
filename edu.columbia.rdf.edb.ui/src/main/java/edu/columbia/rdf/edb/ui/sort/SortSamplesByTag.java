package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.jebtk.core.path.Path;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTag;

public abstract class SortSamplesByTag extends SampleSorter {

  private Path mPath;

  public SortSamplesByTag(Path path) {
    mPath = path;
  }

  @Override
  public void arrange(Collection<Sample> samples, ModernTree<Sample> tree, boolean ascending, FilterModel filterModel) {
    sortByField(samples, tree, mPath, ascending, filterModel);
  }

  @Override
  public void filter(Collection<Sample> samples, FilterModel filterModel) {
    super.filter(samples, filterModel);

    Set<String> names = new TreeSet<String>();

    for (Sample sample : samples) {
      SampleTag tag = sample.getTags().getTag(mPath);

      if (tag != null) {
        String name = sample.getTags().getTag(mPath).getName();
        names.add(name);
      }
    }

    addFilterNames(names, filterModel);
  }
}
