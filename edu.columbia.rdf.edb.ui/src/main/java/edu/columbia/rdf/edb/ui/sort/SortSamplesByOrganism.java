package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;

/**
 * Sort samples by Array Design.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SortSamplesByOrganism extends SampleSorter {
  public void arrange(Collection<Sample> samples,
      ModernTree<Sample> tree,
      boolean ascending,
      FilterModel filterModel) {
    ListMultiMap<String, Sample> map = ArrayListMultiMap.create();

    for (Sample sample : samples) {
      if (!filterModel.keep(sample.getOrganism().getScientificName())) {
        continue;
      }

      map.get(sample.getOrganism().getScientificName()).add(sample);
    }

    arrange(map, ascending, tree);
  }

  @Override
  public void filter(Collection<Sample> samples, FilterModel filterModel) {
    super.filter(samples, filterModel);

    Set<String> names = new HashSet<String>();

    for (Sample sample : samples) {
      names.add(sample.getOrganism().getScientificName());
    }

    addSortedFilterNames(names, filterModel);
  }

  public final String getName() {
    return "Organism";
  }
}
