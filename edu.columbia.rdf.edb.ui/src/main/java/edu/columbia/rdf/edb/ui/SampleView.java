package edu.columbia.rdf.edb.ui;

import java.util.List;
import java.util.Map;

import org.abh.common.collections.CollectionUtils;
import org.abh.common.tree.TreeNode;
import org.abh.common.tree.TreeRootNode;
import org.abh.common.ui.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.sort.SampleSorter;


public abstract class SampleView extends SampleSorter {
	
	@Override
	public <X extends Comparable<? super X>> void arrange(Map<X, ? extends Iterable<Sample>> map,
			boolean ascending,
			ModernTree<Sample> tree) {
		List<String> sortedNames = 
				CollectionUtils.toString(CollectionUtils.sortKeys(map, ascending));
		
		tree.clear();

		TreeRootNode<Sample> root = 
				new TreeRootNode<Sample>();
		
		TreeNode<Sample> titleNode = 
				new TreeNode<Sample>(getName());
		
		root.addChild(titleNode);

		for (String array : sortedNames) {
			TreeNode<Sample> node = new TreeNode<Sample>(array.toString());

			List<Sample> sortedSamples = sortByName(map.get(array), ascending);

			for (Sample sample : sortedSamples) {
				node.addChild(new TreeNode<Sample>(sample.getName(), sample));
			}
			
			node.setIsExpandable(false);

			titleNode.addChild(node);
		}
		
		tree.setRoot(root);
		
		tree.getSelectionModel().setSelection(0);
	}
}
