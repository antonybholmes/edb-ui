/**
 * Copyright 2016 Antony Holmes
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

import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogWindow;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.window.ModernWindow;

public class AllGroupsDialog extends ModernDialogWindow {

  

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * @param parent
   * @param title
   * @param help
   * @param repName The repository name to use for searching.
   * @param sortModel
   * @param searchModel
   */
  public AllGroupsDialog(ModernWindow parent, GroupsModel model) {
    super(parent);

    setTitle("All Groups");

    setResizable(true);
    
    createUi(model);

    setSize(240, 320);

    UI.centerWindowToScreen(this);
  }

  public void createUi(GroupsModel model) {

    //
    // Filter side bar
    //

   
    setCard(new ModernScrollPane(new AllGroupsPanel(model.getAllGroups())).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER));

    // setCard(panel); //new ModernPaddedPanel(new
    // ModernLineBorderPanel(panel)));

    //setDarkBackground();
  }
}
