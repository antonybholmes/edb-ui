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
package edu.columbia.rdf.edb.ui.filter.sets;

import java.util.Map.Entry;

import org.jebtk.core.collections.IterHashMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ButtonStyle;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernClickWidget;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.text.ModernSubHeadingLabel;

import edu.columbia.rdf.edb.SampleSet;

// TODO: Auto-generated Javadoc
/**
 * Displays groupings of samples so users can quickly find related samples.
 * 
 * @author Antony Holmes
 *
 */
public class SetsPanel extends VBox
    implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  private SetsModel mModel;

  private IterMap<ModernTwoStateWidget, SampleSet> mCheckMap = 
      new IterHashMap<ModernTwoStateWidget, SampleSet>();

  private ModernTwoStateWidget mCheckAll = new ModernCheckSwitch("Select All");
  
  private ModernClickWidget mApplyButton =
      new RibbonButton("Apply").setButtonStyle(ButtonStyle.PILL_CONTENT);

  public SetsPanel(SetsModel model) {
    mModel = model;

    //ModernSubCollapsePane collapsePane = new ModernSubCollapsePane();

    //Box box = VBox.create();

    /// box.add(new ModernSubHeadingLabel("My Groups"));
    // box.add(UI.createVGap(10));

    add(new ModernSubHeadingLabel("Sample Sets"));
    add(UI.createVGap(10));
    
    add(mCheckAll);
    add(UI.createVGap(10));

    for (SampleSet g : model) {
      ModernTwoStateWidget check = new ModernCheckSwitch(g.getName(),
          model.getUse(g));

      add(check);

      mCheckMap.put(check, g);

      //check.addClickListener(this);
    }

    //add(UI.createVGap(10));

    //mCheckAllMode.setSelected(model.getAllMode());
    //add(mCheckAllMode);
    
    add(UI.createVGap(20));
    
    //Box box = new HBox();
    //box.add(Box.createHorizontalGlue());
    //box.add(mApplyButton);
    //add(box);
    
    add(mApplyButton);
    
    setBorder(ModernWidget.BORDER);

    //collapsePane.addTab("My Groups", box);

    //collapsePane.addTab("Groups", new AllGroupsPanel(model.getAllGroups()));

    //collapsePane.setExpanded(true);

    //setBody(new ModernScrollPane(collapsePane)
    //    .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
     //   .setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW));

    // setBorder(BORDER);

    mCheckAll.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        checkAll();
      }
    });
    
    mApplyButton.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        apply();
      }});
  }
  
  @Override
  public void clicked(ModernClickEvent e) {
    ModernTwoStateWidget check = (ModernTwoStateWidget) e.getSource();

    SampleSet g = mCheckMap.get(check);

    mModel.setUse(g, check.isSelected());
  }

  private void checkAll() {
    for (ModernTwoStateWidget c : mCheckMap.keySet()) {
      c.setSelected(mCheckAll.isSelected());
      //mModel.updateUse(mCheckMap.get(c), c.isSelected());
    }

    // Finally trigger a refresh via the model
    //mModel.fireChanged();
  }
  
  private void apply() {
    for (Entry<ModernTwoStateWidget, SampleSet> item : mCheckMap) {
      mModel.updateUse(item.getValue(), item.getKey().isSelected());
      System.err.println("aha " + item.getValue().getName() + " " + item.getKey().isSelected());
    }

    // Finally trigger a refresh via the model
    mModel.fireChanged();
  }
}
