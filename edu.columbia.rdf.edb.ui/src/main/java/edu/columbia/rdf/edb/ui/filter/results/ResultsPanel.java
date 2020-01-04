/**
 * Copyright 2017 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.edb.ui.filter.results;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.search.FilterEventListener;
import org.jebtk.modern.search.FilterModel;
import org.jebtk.modern.text.ModernSubHeadingLabel;

import edu.columbia.rdf.edb.ui.SampleSortModel;

/**
 * Displays groupings of samples so users can quickly find related samples.
 * 
 * @author Antony Holmes
 *
 */
public class ResultsPanel extends ModernComponent {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  private FilterModel mModel;

  private Map<ModernTwoStateWidget, String> mCheckMap = new HashMap<ModernTwoStateWidget, String>();

  private ModernTwoStateWidget mCheckAll = new ModernCheckSwitch("Select All",
      true);

  private SampleSortModel mSortModel;

  private class FilterEvents implements FilterEventListener {

    @Override
    public void filtersUpdated(ChangeEvent e) {
      // TODO Auto-generated method stub

    }

    @Override
    public void filtersChanged(ChangeEvent e) {
      setup();
    }

  }

  public ResultsPanel(SampleSortModel sortModel, FilterModel model) {
    mSortModel = sortModel;
    mModel = model;

    mModel.addFilterListener(new FilterEvents());

    // setBorder(DOUBLE_BORDER);

    setup();
  }

  public void setup() {
    Box box = VBox.create();

    box.add(new ModernSubHeadingLabel(
        "Sort By " + mSortModel.getSorter().getName()));
    box.add(UI.createVGap(10));
    box.add(mCheckAll);
    box.add(UI.createVGap(10));

    mCheckAll.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        checkAll();
      }
    });

    ModernClickListener l = new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        ModernTwoStateWidget check = (ModernTwoStateWidget) e.getSource();

        String n = mCheckMap.get(check);

        mModel.setFilter(n, check.isSelected());
      }
    };

    for (String t : mModel) {
      ModernTwoStateWidget check = new ModernCheckSwitch(t, mModel.keep(t));

      box.add(check);

      mCheckMap.put(check, t);

      check.addClickListener(l);
    }

    setBody(new ModernScrollPane(box)
        .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
        .setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW));

    mCheckAll.setSelected(true);

    setBorder(BORDER);
  }

  private void checkAll() {
    for (ModernTwoStateWidget c : mCheckMap.keySet()) {
      c.setSelected(mCheckAll.isSelected());
      mModel.updateFilter(mCheckMap.get(c), c.isSelected());
    }

    // Finally trigger a refresh via the model
    mModel.fireFiltersUpdated();
  }
}
