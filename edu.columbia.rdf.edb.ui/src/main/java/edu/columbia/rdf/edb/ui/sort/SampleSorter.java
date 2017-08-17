package edu.columbia.rdf.edb.ui.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.core.path.Path;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.search.Sorter;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTag;

/**
 * The experiment tree can be sorted in multiple ways. Given a list
 * of experiments and a tree, generate a custom sorted tree of experiments.
 *
 * @author Antony Holmes Holmes
 * @param <T>
 *
 */
public abstract class SampleSorter extends Sorter<Sample> {
	public abstract String getName();
	
	@Override
	public String getType() {
		return "Common Properties";
	}

	//public abstract void arrange(Experiments experiments, DefaultListModel model, boolean ascending);
	
	/*
	public void sort(Collection<Sample> samples, 
			ModernTree<Sample> tree, 
			boolean ascending,
			FilterModel filterModel) {
		filter(samples, filterModel);
		
		arrange(samples, tree, ascending, filterModel);
	}
	*/
	
	/*
	@Override
	public abstract void arrange(Collection<Sample> samples, 
			ModernTree<Sample> tree, 
			boolean ascending,
			FilterModel filterModel);
	*/
	
	@Override
	public void filter(Collection<Sample> samples, FilterModel filterModel) {
		filterModel.clear();
	}
	
	//public static void addFilterNames(Collection<String> names, 
	//		FilterModel filterModel) {
	//	filterModel.setFilter(names, true);
	//}
	

	public int compareTo(SampleSorter s) {
		return getName().compareTo(s.getName());
	}

	public boolean equals(Object o) {
		if (o instanceof SampleSorter) {
			return false;
		}
		
		return getName().equals(((SampleSorter)o).getName());
	}



	/**
	 * Sort the samples from a list of experiments by a particular field.
	 *
	 * @param samples
	 * @param tree
	 * @param sectionType
	 * @param field
	 * @param ascending
	 */
	protected static void sortByField(Collection<Sample> samples,
			ModernTree<Sample> tree,
			Path path,
			boolean ascending,
			FilterModel filterModel) {
		ListMultiMap<String, Sample> map = ArrayListMultiMap.create();

		// map the samples by the field of interest

		for (Sample sample : samples) {
			SampleTag field = sample.getTags().getTag(path);
			
			if (field == null) {
				continue;
			}
			
			String name = field.getValue();

			if (filterModel.keep(name)) {
				map.get(name).add(sample);
			}
		}

		// get a list of the all the keys used and sort them

		List<String> names = new ArrayList<String>();

		for (String name : map.keySet()) {
			names.add(name);
		}

		Collections.sort(names);

		if (!ascending) {
			Collections.reverse(names);
		}

		// iterate over the keys/groups and add the samples
		// (first sorting samples within a group by name) {
		tree.clear();
		
		TreeRootNode<Sample> root = new TreeRootNode<Sample>();

		for (String name : names) {
			TreeNode<Sample> node = new TreeNode<Sample>(name);

			List<Sample> sortedSamples = sortByName(map.get(name), true);

			for (Sample sample : sortedSamples) {
				node.addChild(new TreeNode<Sample>(sample.getName(), sample));
			}

			root.addChild(node);
		}
		
		tree.setRoot(root);
	}
}
