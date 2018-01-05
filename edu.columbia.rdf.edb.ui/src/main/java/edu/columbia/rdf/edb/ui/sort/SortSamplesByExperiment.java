package edu.columbia.rdf.edb.ui.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Experiment;
import edu.columbia.rdf.edb.Sample;

/**
 * Sort samples by experiment.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SortSamplesByExperiment extends SampleSorter {
  @Override
  public void arrange(Collection<Sample> samples, ModernTree<Sample> tree, boolean ascending, FilterModel filterModel) {

    Map<Experiment, Set<Sample>> experiments = Experiment.sortSamplesByExperiment(samples);

    List<Experiment> sortedExperiments = sortByTitle(experiments.keySet(), ascending);

    tree.clear();

    TreeRootNode<Sample> root = new TreeRootNode<Sample>();

    for (Experiment experiment : sortedExperiments) {
      if (!filterModel.keep(experiment.getName())) {
        continue;
      }

      TreeNode<Sample> node = new TreeNode<Sample>(experiment.getName());

      List<Sample> sortedSamples = sortByName(experiments.get(experiment), ascending);

      for (Sample sample : sortedSamples) {
        node.addChild(new TreeNode<Sample>(sample.getName(), sample));
      }

      root.addChild(node);
    }

    tree.setRoot(root);
  }

  @Override
  public void filter(Collection<Sample> samples, FilterModel filterModel) {
    super.filter(samples, filterModel);

    Map<Experiment, Set<Sample>> experiments = Experiment.sortSamplesByExperiment(samples);

    Set<String> names = new HashSet<String>();

    for (Experiment experiment : experiments.keySet()) {
      names.add(experiment.getName());
    }

    addSortedFilterNames(names, filterModel);
  }

  public final String getName() {
    return "Experiment";
  }

  public static List<Experiment> sortByTitle(Collection<Experiment> experiments, boolean ascending) {

    List<String> names = new ArrayList<String>();

    Map<String, Experiment> experimentMap = new TreeMap<String, Experiment>();

    for (Experiment experiment : experiments) {
      String name = experiment.getName();

      names.add(name);
      experimentMap.put(name, experiment);
    }

    Collections.sort(names);

    if (!ascending) {
      Collections.reverse(names);
    }

    List<Experiment> sortedExperiments = new ArrayList<Experiment>();

    for (String name : names) {
      sortedExperiments.add(experimentMap.get(name));
    }

    return sortedExperiments;
  }
}
