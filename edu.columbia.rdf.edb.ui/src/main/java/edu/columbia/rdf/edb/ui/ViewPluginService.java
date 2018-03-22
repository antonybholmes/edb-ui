package edu.columbia.rdf.edb.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jebtk.modern.ModernComponent;

import edu.columbia.rdf.edb.Sample;

/**
 * Caches experiment field views which describe how an experiment and its
 * samples should be display to the user. This allows inadequacies of caArray to
 * be abstracted away from the user so they get a consistent data view.
 *
 * @author Antony Holmes Holmes
 *
 */
public class ViewPluginService implements Iterable<ViewPlugin>, Serializable {

  private static final long serialVersionUID = 1L;

  private static final ViewPluginService INSTANCE = new ViewPluginService();

  public static final ViewPluginService instance() {
    return INSTANCE;
  }

  private List<ViewPlugin> mViews = new ArrayList<ViewPlugin>();

  private Map<String, ViewPlugin> mViewMap = new HashMap<String, ViewPlugin>();

  public ViewPluginService() {
    // load();
  }

  public void add(ViewPlugin plugin) {
    mViews.add(plugin);
    mViewMap.put(plugin.getExpressionType(), plugin);
    
    plugin.init();
  }

  @Override
  public Iterator<ViewPlugin> iterator() {
    return mViews.iterator();
  }

  public ViewPlugin getView(Sample sample) {
    return getView(sample.getExpressionType().getName());
  }

  public ViewPlugin getView(String expressionType) {
    return mViewMap.get(expressionType);
  }

  public ModernComponent getSamplePanel(Sample sample) {
    return getView(sample).getSamplePanel(sample);
  }
}
