package edu.columbia.rdf.edb.ui.sort;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.abh.common.collections.ArrayListMultiMap;
import org.abh.common.collections.CollectionUtils;
import org.abh.common.collections.ListMultiMap;
import org.abh.common.ui.search.FilterModel;
import org.abh.common.ui.tree.ModernTree;

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

		Set<Person> names = new TreeSet<Person>();

		for (Sample sample : samples) {
			for (Person person : sample.getPersons()) {
				//String name = person.getName();
				
				//names.add(name);
				names.add(person);
			}
		}

		// Add names using surname, forname sorting
		addFilterNames(CollectionUtils.toString(names), filterModel);
	}

	public final String getName() {
		return "Person";
	}
}
