package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;

public class SortSamplesByName extends SampleSorter {

  @Override
  public void arrange(Collection<Sample> samples, ModernTree<Sample> tree, boolean ascending, FilterModel filterModel) {
    ListMultiMap<String, Sample> map = ArrayListMultiMap.create();

    for (Sample sample : samples) {
      String name = sample.getName();

      String t = name.substring(0, 1).toUpperCase();

      if (filterModel.keep(t)) {
        map.get(t).add(sample);
      }
    }

    arrange(map, ascending, tree);
  }

  /*
   * @Override public void arrange(Collection<Sample> samples, ModernTree<Sample>
   * tree, boolean ascending, FilterModel filterModel) { List<Sample>
   * sortedSamples = sortByName(samples, ascending);
   * 
   * tree.clear();
   * 
   * TreeRootNode<Sample> root = new TreeRootNode<Sample>();
   * 
   * for (Sample sample : sortedSamples) { if
   * (!filterModel.keep(sample.getName())) { continue; }
   * 
   * TreeNode<Sample> node = new TreeNode<Sample>(sample.getName(), sample);
   * 
   * root.addChild(node); }
   * 
   * tree.setRoot(root); }
   */

  @Override
  public void filter(Collection<Sample> samples, FilterModel filterModel) {
    super.filter(samples, filterModel);

    Set<String> names = new HashSet<String>(samples.size());

    for (Sample sample : samples) {
      String name = sample.getName();
      String t = name.substring(0, 1).toUpperCase();

      names.add(t);
    }

    addSortedFilterNames(names, filterModel);
  }

  public final String getName() {
    return "Sample Name";
  }
}
