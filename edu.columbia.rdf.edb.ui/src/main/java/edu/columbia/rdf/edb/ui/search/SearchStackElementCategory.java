package edu.columbia.rdf.edb.ui.search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.jebtk.core.search.SearchStackElement;
import org.jebtk.core.search.SearchStackOperator;

import edu.columbia.rdf.edb.Sample;

/**
 * Describes a search operation for determining if an experiment matches some
 * criteria or other.
 *
 * @author Antony Holmes Holmes
 *
 */
public class SearchStackElementCategory {

  /**
   * Essentially the string we want to find
   */
  private Search mSearch = null;

  private SearchCategory mField = null;

  public SearchStackOperator mType;

  public SearchStackElementCategory(SearchStackOperator type) {
    mType = type;
  }

  public SearchStackElementCategory(SearchCategory field, Search search) {
    mType = SearchStackOperator.MATCH;

    mField = field;
    mSearch = search;
  }

  public final Search getSearch() {
    return mSearch; // new ArrayDeque<SearchStackElement<Sample>>(mSearch);
  }

  public final SearchCategory getSearchField() {
    return mField;
  }

  /**
   * Creates a search stack from a user search. A search stack is a fast AST
   * representation of a search that can be evaluated on a stack repeatedly to
   * search samples quickly.
   * 
   * @param search
   * @return
   * @throws Exception
   */
  public static Deque<SearchStackElementCategory> getSearchStack(UserSearch search) throws Exception {
    List<SearchStackElementCategory> searchList = new ArrayList<SearchStackElementCategory>();

    Deque<SearchStackOperator> operatorStack = new ArrayDeque<SearchStackOperator>();

    // more than one field to search for
    // int mappedIndex;

    for (int i = 0; i < search.size(); ++i) {
      UserSearchEntry searchEntry = search.get(i);

      // mappedIndex = orderMap.get(i);

      // we build the tree as if the first element (true) has
      // already been added. In that case if we are reading
      // in the expression as though it is in an infix notation
      // we would read the operator first and then the next
      // operand

      if (i > 0) {
        addLowerPrecedenceOps(searchEntry.getOperator(), searchList, operatorStack);
      }

      List<SearchStackElement<Sample>> searchQueue = SearchStackElement.parseQuery(searchEntry.getText());

      SearchStackElementCategory element = new SearchStackElementCategory(searchEntry.getField(),
          new Search(searchEntry.getText(), searchQueue));

      searchList.add(element);
    }

    // add any remaining operatorStack onto the stack
    while (operatorStack.size() > 0) {
      SearchStackOperator operator = operatorStack.pop();

      // addOperatorToStack(stack, op);
      searchList.add(new SearchStackElementCategory(operator));
    }

    Collections.reverse(searchList);

    Deque<SearchStackElementCategory> searchStack = new ArrayDeque<SearchStackElementCategory>();

    // Now the stack can be evaluated correctly
    // The first elements will be terms to match to
    for (SearchStackElementCategory item : searchList) {
      searchStack.push(item);
    }

    return searchStack;
  }

  /**
   * Ensures ops with a higher predence are evaluated first. For example AND is
   * always evaluated before OR.
   *
   * @param operator
   * @param operatorStack
   * @param stack
   * @throws Exception
   *           {
   */
  private static void addLowerPrecedenceOps(SearchStackOperator operator, List<SearchStackElementCategory> stack,
      Deque<SearchStackOperator> operatorStack) throws Exception {
    int precedence = SearchStackOperator.precedence(operator);

    SearchStackOperator op;

    // deal with existing operatorStack

    while (operatorStack.size() > 0) {
      op = operatorStack.peek();

      if ((SearchStackOperator.isLeftAssociative(operator) && (precedence <= SearchStackOperator.precedence(op)))
          || (!SearchStackOperator.isLeftAssociative(operator) && (precedence < SearchStackOperator.precedence(op)))) {
        // addOperatorToStack(stack, op);
        stack.add(new SearchStackElementCategory(op));

        // remove the operator as we have dealt with it
        operatorStack.pop();
      } else {
        break;
      }
    }

    // add the operator of interest
    operatorStack.push(operator);
  }
}
