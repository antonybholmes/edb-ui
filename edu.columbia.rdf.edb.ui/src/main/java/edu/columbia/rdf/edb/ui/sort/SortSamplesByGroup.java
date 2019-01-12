package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.Sample;

/**
 * Sort samples by Array Design.
 * 
 * @author Antony Holmes
 *
 */
public class SortSamplesByGroup extends SampleSorter {
  @Override
  public void arrange(Collection<Sample> samples,
      ModernTree<Sample> tree,
      boolean ascending,
      FilterModel filterModel) {
    ListMultiMap<Group, Sample> map = ArrayListMultiMap.create();

    for (Sample sample : samples) {
      for (Group g : sample.getGroups()) {
        String name = g.getName();

        if (!filterModel.keep(name)) {
          continue;
        }

        map.get(g).add(sample);
      }
    }

    arrange(map, ascending, tree);
  }

  @Override
  public void filter(Collection<Sample> samples, FilterModel filterModel) {
    super.filter(samples, filterModel);

    Set<String> names = new HashSet<String>();

    for (Sample sample : samples) {
      for (Group g : sample.getGroups()) {
        // String name = person.getName();

        // names.add(name);
        names.add(g.getName());
      }
    }

    // Add names using surname, forname sorting
    addSortedFilterNames(names, filterModel);
  }

  public final String getName() {
    return "Group";
  }
}
