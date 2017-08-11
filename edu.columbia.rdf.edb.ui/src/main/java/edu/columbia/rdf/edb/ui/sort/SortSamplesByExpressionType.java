package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.abh.common.collections.ArrayListMultiMap;
import org.abh.common.collections.ListMultiMap;
import org.abh.common.ui.search.FilterModel;
import org.abh.common.ui.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;

/**
 * Sort samples by experiment.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SortSamplesByExpressionType extends SampleSorter {
	@Override
	public void arrange(Collection<Sample> samples, 
			ModernTree<Sample> tree, 
			boolean ascending,
			FilterModel filterModel) {
		ListMultiMap<String, Sample> map = ArrayListMultiMap.create();

		for (Sample sample : samples) {
			String name = sample.getExpressionType().getName();

			if (!filterModel.keep(name)) {
				continue;
			}
				
			map.get(name).add(sample);
		}
		
		arrange(map, ascending, tree);
	}
	
	@Override
	public void filter(Collection<Sample> samples, 
			FilterModel filterModel) {
		super.filter(samples, filterModel);
		
		Set<String> names = new TreeSet<String>();
		
		for (Sample sample : samples) {
			String name = sample.getExpressionType().getName();
			
			names.add(name);
		}
		
		addFilterNames(names, filterModel);
	}

	public final String getName() {
		return "Expression Type";
	}
}
