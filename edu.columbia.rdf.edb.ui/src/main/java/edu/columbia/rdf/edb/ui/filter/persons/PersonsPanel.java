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
package edu.columbia.rdf.edb.ui.filter.persons;

import java.util.HashMap;
import java.util.Map;

import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernTwoStateWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;

import edu.columbia.rdf.edb.Person;

/**
 * Displays groupings of samples so users can quickly find related samples.
 * 
 * @author Antony Holmes
 *
 */
public class PersonsPanel extends VBox
    implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  private PersonsModel mModel;

  private Map<ModernTwoStateWidget, Person> mCheckMap = new HashMap<ModernTwoStateWidget, Person>();

  private ModernTwoStateWidget mCheckAll = new ModernCheckSwitch("Select All",
      true);

  public PersonsPanel(PersonsModel model) {
    mModel = model;

    //Box box = VBox.create();

    // box.add(new ModernSubHeadingLabel("Organisms"));

    add(UI.createVGap(10));

    add(mCheckAll);
    add(UI.createVGap(10));

    mCheckAll.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        checkAll();
      }
    });

    for (Person t : model) {
      ModernTwoStateWidget check = new ModernCheckSwitch(t.getName(),
          model.getUse(t));

      add(check);

      mCheckMap.put(check, t);

      check.addClickListener(this);
    }

    //setBody(new ModernScrollPane(box)
    //    .setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
    //   .setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW));

    setBorder(ModernWidget.BORDER);
  }

  @Override
  public void clicked(ModernClickEvent e) {
    ModernTwoStateWidget check = (ModernTwoStateWidget) e.getSource();

    Person g = mCheckMap.get(check);

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
