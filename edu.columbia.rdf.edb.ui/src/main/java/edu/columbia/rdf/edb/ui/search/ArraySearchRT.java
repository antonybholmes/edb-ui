package edu.columbia.rdf.edb.ui.search;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.collections.CollectionUtils;

import edu.columbia.rdf.edb.Groups;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.Species;
import edu.columbia.rdf.edb.ui.Repository;
import edu.columbia.rdf.edb.ui.RepositoryService;

/**
 * A client to connect to a caarray server and search it.
 *
 * @author Antony Holmes Holmes
 */
public class ArraySearchRT {
  /**
   * Searches for samples then orders them by experiment.
   *
   * @param connection
   * @param searchStack
   * @param mOrganisms
   * @param mDataTypes
   * @return
   * @throws IOException
   */
  /*
   * public List<Experiment> searchExperiments(SearchStackElementCategory[]
   * searchStack) { Collection<Sample> samples = searchSamples(searchStack);
   * 
   * Set<Experiment> experiments = Experiment.sortSamplesByExperiment(samples);
   * 
   * return ArrayUtils.toList(experiments); }
   */

  public List<Sample> searchSamples(
      Deque<SearchStackElementCategory> searchStack) throws IOException {
    return searchSamples(searchStack,
        Repository.ALL_TYPES,
        Repository.ALL_ORGANISMS,
        Repository.ALL_GROUPS);
  }

  /**
   * Searches for samples.
   *
   * @param connection
   * @param searchStack
   * @return
   * @throws IOException
   */
  public List<Sample> searchSamples(
      Deque<SearchStackElementCategory> searchStack,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Groups groups) throws IOException {
    // Stack<Integer> indexes = new Stack<Integer>();

    // for speed use a custom stack
    // int indexTop = -1;
    // int[] indexStack = new int[16];

    // If no search criteria are specified or the search is an empty string,
    // return
    // all experiments.

    Repository store = RepositoryService.getInstance().getRepository();

    if (searchStack.size() == 0 || (searchStack.size() == 1
        && searchStack.peek().getSearch() == null)) {
      return store.getAllSamples();
    }

    SearchCategory searchField = null;

    // Collection<SampleSearchResult> tempSamples = null;

    SearchStackElementCategory op = null;
    List<Sample> samples1;
    List<Sample> samples2;

    // Send multiple searches to the database and collate the results
    // at the end

    Deque<List<Sample>> resultStack = new ArrayDeque<List<Sample>>(100);

    while (searchStack.size() > 0) {
      op = searchStack.pop();

      // note that for binary operators, both operands must be
      // popped off the stack even if we use lazy evaluation,
      // hence the use of i1 and i2. This applies elsewhere
      // in the code as well.

      // System.err.println("op " + op.mType);

      switch (op.mType) {
      case MATCH:
        searchField = op.getSearchField();

        // The path to the words of interest
        resultStack.push(store.searchSamples(op.getSearch().search,
            searchField.getPath(),
            dataTypes,
            organisms,
            groups));

        break;
      case AND:
        samples2 = resultStack.pop();
        samples1 = resultStack.pop();

        resultStack.push(CollectionUtils.intersect(samples1, samples2));

        break;
      case OR:
        samples2 = resultStack.pop();
        samples1 = resultStack.pop();

        resultStack.push(CollectionUtils.union(samples1, samples2));

        break;
      case XOR:
      case NAND:
        samples2 = resultStack.pop();
        samples1 = resultStack.pop();

        resultStack.push(CollectionUtils.xor(samples1, samples2));

        break;
      default:
        // do nothing
        break;
      }
    }

    // System.err.println("ca search:" + op.type + " " +
    // searchStack[searchStack.size() - 1).samples);

    return resultStack.peek();
  }
}
