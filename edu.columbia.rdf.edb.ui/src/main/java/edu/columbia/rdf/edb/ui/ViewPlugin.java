package edu.columbia.rdf.edb.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jebtk.core.path.Path;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.SelectionModel;
import org.jebtk.modern.menu.ModernPopupMenu;
import org.jebtk.modern.search.SortModel;
import org.jebtk.modern.status.StatusModel;
import org.jebtk.modern.window.ModernRibbonWindow;

import edu.columbia.rdf.edb.DataView;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.search.SearchCategoryService;

/**
 * All custom data views for the experimentdb should implement this.
 * 
 * @author Antony Holmes Holmes
 */
public abstract class ViewPlugin implements Comparable<ViewPlugin> {

  /**
   * Should add sample sorters specific to the type of view this plugin
   * represents. E.g. for microarray this could be the addition of a sorter to
   * sort samples by array platform.
   * 
   * @param sampleSortModel
   */
  public void initSampleSorters(SortModel<Sample> sampleSortModel) {
    // Do nothing
  }

  /**
   * Should add sample viewers specific to the type of view this plugin
   * describes. Sample views are similar to sample sorters, but used for the
   * very left folders pane in the main UI.
   * 
   * @param views
   */
  public void initSampleFolders(SortModel<Sample> views) {
    // do nothing
  }

  /**
   * Should be overridden to add custom search functionality for features
   * specific to this plugin, e.g. for microarrays, this could be to allow you
   * to search by array plaform.
   * 
   * @param searchCategoryService
   */
  public void initSearchCategories(
      SearchCategoryService searchCategoryService) {
    // do nothing
  }

  /**
   * Should return the name of the view. This is matched to a sample's
   * expression type to determine how it should be displayed.
   * 
   * @return
   */
  public abstract String getExpressionType();

  public void init(ModernRibbonWindow parent,
      StatusModel statusModel,
      SelectionModel<Sample> selectedSamples) {
    // Do nothing
  }

  @Override
  public int compareTo(ViewPlugin v) {
    return getExpressionType().compareTo(v.getExpressionType());
  }

  @Override
  public int hashCode() {
    return getExpressionType().hashCode();
  }

  public abstract ModernComponent getSamplePanel(Sample sample);

  public abstract Path getDisplayField1();

  public abstract Path getDisplayField2();

  public abstract DataView getDataView();

  public void customizeSampleMenu(ModernPopupMenu menu) {
    // TODO Auto-generated method stub
  }

  /**
   * Returns true if one of the samples is locked.
   * 
   * @param samples
   * @return
   */
  public static boolean checkForLocked(Collection<Sample> samples) {
    for (Sample sample : samples) {
      if (sample.getLocked()) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns only the unlocked samples.
   * 
   * @param samples
   * @return
   */
  public static final List<Sample> getUnlockedSamples(
      Collection<Sample> samples) {
    List<Sample> ret = new ArrayList<Sample>(samples.size());

    for (Sample sample : samples) {
      if (!sample.getLocked()) {
        ret.add(sample);
      }
    }

    return ret;
  }
}
