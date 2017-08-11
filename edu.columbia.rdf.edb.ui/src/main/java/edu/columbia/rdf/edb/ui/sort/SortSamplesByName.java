package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.abh.common.collections.CollectionUtils;
import org.abh.common.tree.TreeNode;
import org.abh.common.tree.TreeRootNode;
import org.abh.common.ui.search.FilterModel;
import org.abh.common.ui.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;

public class SortSamplesByName extends SampleSorter {
	@Override
	public void arrange(Collection<Sample> samples, 
			ModernTree<Sample> tree, 
			boolean ascending,
			FilterModel filterModel) {
		List<Sample> sortedSamples = sortByName(samples, ascending);

		tree.clear();

		TreeRootNode<Sample> root = 
				new TreeRootNode<Sample>();
		
		for (Sample sample : sortedSamples) {
			if (!filterModel.keep(sample.getName())) {
				continue;
			}
			
			TreeNode<Sample> node = 
					new TreeNode<Sample>(sample.getName(), sample);

			root.addChild(node);
		}

		tree.setRoot(root);
	}
	
	@Override
	public void filter(Collection<Sample> samples, 
			FilterModel filterModel) {
		super.filter(samples, filterModel);
		
		Set<String> names = new HashSet<String>();
		
		for (Sample sample : samples) {
			names.add(sample.getName());
		}
		
		addFilterNames(CollectionUtils.sort(names), filterModel);
	}

	public final String getName() {
		return "Sample Name";
	}
}
