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
	public void filter(Collection<Sample> samples, 
			FilterModel filterModel) {
		super.filter(samples, filterModel);
		
		Set<String> names = new TreeSet<String>();
		
		for (Sample sample : samples) {
			names.add(sample.getOrganism().getScientificName());
		}
		
		addFilterNames(names, filterModel);
	}

	public final String getName() {
		return "Organism";
	}
}
