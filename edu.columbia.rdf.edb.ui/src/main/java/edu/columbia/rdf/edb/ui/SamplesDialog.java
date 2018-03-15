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
package edu.columbia.rdf.edb.ui;

import java.awt.Component;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.MinusVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.panel.CardPanel;
import org.jebtk.modern.search.ModernSearchPanel;
import org.jebtk.modern.search.SearchModel;
import org.jebtk.modern.tabs.TabPanel;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ui.filter.datatypes.DataTypesModel;
import edu.columbia.rdf.edb.ui.filter.datatypes.DataTypesService;
import edu.columbia.rdf.edb.ui.filter.groups.GroupsModel;
import edu.columbia.rdf.edb.ui.filter.groups.GroupsPanel;
import edu.columbia.rdf.edb.ui.filter.groups.GroupsService;
import edu.columbia.rdf.edb.ui.filter.organisms.OrganismsPanel;
import edu.columbia.rdf.edb.ui.filter.organisms.OrganismsService;

public class SamplesDialog extends ModernDialogHelpWindow
    implements ModernClickListener {
  private static final long serialVersionUID = 1L;

  private ModernTree<Sample> mTree = new ModernTree<Sample>();

  private ModernButton mExpandButton = new ModernButton(
      UIService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  private ModernButton mCollapseButton = new ModernButton(
      UIService.getInstance().loadIcon(MinusVectorIcon.class, 16));

  private SampleSortModel mSortModel;

  private SampleModel mSampleModel = new SampleModel();

  private SamplesTreePanel mSamplesPanel;

  private ModernSearchPanel mSearchPanel;

  private String mRepName;

  private GroupsModel mUserGroupsModel = GroupsService.getInstance();

  private DataTypesModel mDataTypesModel = DataTypesService.getInstance();

  private OrganismsService mOrganismsModel = OrganismsService.getInstance();

  public class RefreshEvents implements ModernClickListener {
    @Override
    public void clicked(ModernClickEvent e) {
      try {
        search();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

  }

  public class CollapseEvents implements ModernClickListener {
    @Override
    public void clicked(ModernClickEvent e) {
      mTree.getRoot().setChildrenAreExpanded(false);
    }
  }

  public class ExpandEvents implements ModernClickListener {
    @Override
    public void clicked(ModernClickEvent e) {
      mTree.getRoot().setChildrenAreExpanded(true);
    }
  }

  private class SearchEvents implements ModernClickListener {

    @Override
    public void clicked(ModernClickEvent e) {
      try {
        search();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * 
   * @param parent
   * @param title
   * @param help
   * @param repName The repository name to use for searching.
   * @param sortModel
   * @param searchModel
   */
  public SamplesDialog(ModernWindow parent, String title, String help,
      String repName, SampleSortModel sortModel, SearchModel searchModel) {
    super(parent, help);

    mRepName = repName;
    mSortModel = sortModel;
    mSearchPanel = new ModernSearchPanel(searchModel);

    createUi();

    mSearchPanel.addClickListener(new SearchEvents());
    mCollapseButton.addClickListener(new CollapseEvents());
    mExpandButton.addClickListener(new ExpandEvents());

    addWindowFocusListener(new WindowWidgetFocusEvents(mOkButton));

    try {
      search();
    } catch (IOException e) {
      e.printStackTrace();
    }

    setTitle(title);

    setResizable(true);

    setSize(800, 600);

    UI.centerWindowToScreen(this);
  }

  public void createUi() {

    //
    // Filter side bar
    //

    ChangeListener l = new ChangeListener() {
      @Override
      public void changed(ChangeEvent e) {
        try {
          search();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    };

    GroupsPanel userGroupsPanel = new GroupsPanel(mUserGroupsModel);
    // Respond when a user group is changed
    mUserGroupsModel.addChangeListener(l);

    // DataTypesPanel dataTypesPanel = new DataTypesPanel(mDataTypesModel,
    // mOrganismsModel);
    OrganismsPanel organismsPanel = new OrganismsPanel(mOrganismsModel);

    mDataTypesModel.addChangeListener(l);
    mOrganismsModel.addChangeListener(l);

    getIconTabs().addTab("GROUPS", 'G', userGroupsPanel);
    // addLeftTab("DATA TYPES", 'D', dataTypesPanel);
    getIconTabs()
        .addTab("ORGANISMS", 'O', new TabPanel("Organisms", organismsPanel));

    // IconTabsPanel viewPanel = new IconTabsPanel(groupTabsModel, 36, 22);
    // //new
    // ModernComponent(new IconTabsPanel(groupTabsModel, 30, 20),
    // ModernWidget.DOUBLE_BORDER);

    // Show the column groups by default
    getIconTabs().changeTab(0);

    // getTabsPane().getModel().addLeftTab("Filter", viewPanel, 200, 100, 500);

    //
    // Content
    //

    ModernComponent panel = new ModernComponent();

    panel.setHeader(new ModernComponent(mSearchPanel,
        BorderService.getInstance().createBottomBorder(20)));

    mSamplesPanel = new SamplesTreePanel(mParent, mSampleModel, mSortModel);

    panel.setBody(mSamplesPanel); // new ModernContentPanel(mSamplesPanel));

    Component c = footer();

    if (c != null) {
      panel.setFooter(c);
    }

    getTabsPane().getModel()
        .setCenterTab(new ModernComponent(
            new CardPanel(panel),
            ModernWidget.DOUBLE_BORDER));

    // setCard(panel); //new ModernPaddedPanel(new
    // ModernLineBorderPanel(panel)));

    setDarkBackground();
  }

  protected Component footer() {
    return null;
  }

  /**
   * Generate a tree view of a sample folder and its sub folders.
   * 
   * @param sampleFolder
   * @param tree
   * @param ascending
   * @throws IOException
   * @throws ParseException
   * @throws java.text.ParseException
   */
  public void search() throws IOException {
    // mSampleModel.set(mAssembly.getSamples(mSearchPanel.getText()));

    Repository store = RepositoryService.getInstance().getRepository(mRepName);

    List<Sample> samples = store.searchSamples(mSearchPanel.getText(),
        mOrganismsModel.getOrganisms(),
        mUserGroupsModel.getGroups());

    mSampleModel.set(samples);
  }

  public void setSelected(int i) {
    mTree.selectNode(i);
  }

  public List<Sample> getSelectedSamples() {
    List<Sample> samples = mSamplesPanel.getSelectedSamples();

    List<Sample> ret = new ArrayList<Sample>(samples.size());

    for (Sample sample : mSamplesPanel.getSelectedSamples()) {
      if (!sample.getLocked()) {
        ret.add(sample);
      }
    }

    return ret;
  }

  public Sample getSelectedSample() {
    List<Sample> samples = getSelectedSamples();

    if (samples.size() > 0) {
      return samples.get(0);
    } else {
      return null;
    }
  }
}
