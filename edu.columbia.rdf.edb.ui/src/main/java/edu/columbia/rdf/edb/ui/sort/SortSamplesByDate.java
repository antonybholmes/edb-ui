package edu.columbia.rdf.edb.ui.sort;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.abh.common.collections.ArrayListMultiMap;
import org.abh.common.collections.CollectionUtils;
import org.abh.common.collections.ListMultiMap;
import org.abh.common.text.DateUtils;
import org.abh.common.ui.search.FilterModel;
import org.abh.common.ui.tree.ModernTree;

import edu.columbia.rdf.edb.Sample;

public class SortSamplesByDate extends SampleSorter {
	
	@Override
	public void arrange(Collection<Sample> samples, 
			ModernTree<Sample> tree, 
			boolean ascending,
			FilterModel filterModel) {

		ListMultiMap<Date, Sample> map = ArrayListMultiMap.create();

		DateFormat format = DateUtils.createMMDDYYYYFormat();
		
		for (Sample sample : samples) {
			Date date = sample.getDate();

			if (filterModel.keep(format.format(date))) {
				map.get(date).add(sample);
			}
		}
		
		arrange(map, format, ascending, tree);
	}
	
	@Override
	public void filter(Collection<Sample> samples, 
			FilterModel filterModel) {
		super.filter(samples, filterModel);
		
		Set<Date> dates = new TreeSet<Date>();
		
		for (Sample sample : samples) {
			dates.add(sample.getDate());
		}
		
		//List<String> names = new ArrayList<String>();
		
		//for (FormattedDate date : dates) {
		//	names.add(date.toString());
		//}
		
		addFilterNames(DateUtils.format(CollectionUtils.sort(dates)), filterModel);
	}

	public final String getName() {
		return "Date";
	}
}
