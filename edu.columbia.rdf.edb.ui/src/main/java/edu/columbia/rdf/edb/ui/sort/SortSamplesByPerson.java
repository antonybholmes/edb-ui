package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.tree.ModernTree;

import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.Sample;

/**
 * Sort samples by Array Design.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SortSamplesByPerson extends SampleSorter {
	@Override
	public void arrange(Collection<Sample> samples, 
			ModernTree<Sample> tree, 
			boolean ascending,
			FilterModel filterModel) {
		ListMultiMap<Person, Sample> map = ArrayListMultiMap.create();

		for (Sample sample : samples) {
			for (Person person : sample.getPersons()) {
				String name = person.getName();
				
				if (!filterModel.keep(name)) {
					continue;
				}

				map.get(person).add(sample);
			}
		}

		arrange(map, ascending, tree);
	}

	@Override
	public void filter(Collection<Sample> samples, 
			FilterModel filterModel) {
		super.filter(samples, filterModel);

		Set<String> names = new HashSet<String>();

		for (Sample sample : samples) {
			for (Person person : sample.getPersons()) {
				//String name = person.getName();
				
				//names.add(name);
				names.add(person.toString());
			}
		}

		// Add names using surname, forname sorting
		addSortedFilterNames(names, filterModel);
	}

	public final String getName() {
		return "Person";
	}
}
