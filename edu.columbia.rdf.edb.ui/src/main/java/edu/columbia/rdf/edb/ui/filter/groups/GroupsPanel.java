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
package edu.columbia.rdf.edb.ui.filter.groups;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.collapsepane.ModernSubCollapsePane;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.widget.ModernTwoStateWidget;

import edu.columbia.rdf.edb.Group;

// TODO: Auto-generated Javadoc
/**
 * Displays groupings of samples so users can quickly find related samples.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class GroupsPanel extends ModernComponent
    implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  private GroupsModel mModel;

  private Map<ModernTwoStateWidget, Group> mCheckMap = new HashMap<ModernTwoStateWidget, Group>();

  private ModernTwoStateWidget mCheckAll = new ModernCheckSwitch("Select All");

  private ModernTwoStateWidget mCheckAllMode = new ModernCheckSwitch(
      "In All Groups");

  public GroupsPanel(GroupsModel model) {
    mModel = model;

    ModernSubCollapsePane collapsePane = new ModernSubCollapsePane();

    Box box = VBox.create();

    /// box.add(new ModernSubHeadingLabel("My Groups"));
    // box.add(UI.createVGap(10));

    box.add(mCheckAll);
    box.add(UI.createVGap(10));

    for (Group g : model) {
      ModernTwoStateWidget check = new ModernCheckSwitch(g.getName(),
          model.getUse(g), g.getColor());

      box.add(check);

      mCheckMap.put(check, g);

      check.addClickListener(this);
    }

    box.add(UI.createVGap(10));

    mCheckAllMode.setSelected(model.getAllMode());
    box.add(mCheckAllMode);
    box.setBorder(BORDER);

    collapsePane.addTab("My Groups", box);

    collapsePane.addTab("Groups", new AllGroupsPanel(model.getAllGroups()));

    collapsePane.setExpanded(true);

    setBody(new ModernScrollPane(collapsePane)
        .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
        .setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW));

    // setBorder(BORDER);

    mCheckAll.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        checkAll();
      }
    });

    mCheckAllMode.addClickListener(new ModernClickListener() {
      @Override
      public void clicked(ModernClickEvent e) {
        mModel.setAllMode(mCheckAllMode.isSelected());
      }
    });
  }

  @Override
  public void clicked(ModernClickEvent e) {
    ModernTwoStateWidget check = (ModernTwoStateWidget) e.getSource();

    Group g = mCheckMap.get(check);

    mModel.setUse(g, check.isSelected());
  }

  private void checkAll() {
    for (ModernTwoStateWidget c : mCheckMap.keySet()) {
      c.setSelected(mCheckAll.isSelected());
      mModel.updateUse(mCheckMap.get(c), c.isSelected());
    }

    // Finally trigger a refresh via the model
    mModel.fireChanged();
  }
}
