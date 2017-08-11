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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.abh.common.event.ChangeEvent;
import org.abh.common.event.ChangeListener;
import org.abh.common.settings.SettingsService;
import org.abh.common.tree.TreeNode;
import org.abh.common.ui.UI;
import org.abh.common.ui.UIService;
import org.abh.common.ui.event.ModernClickEvent;
import org.abh.common.ui.event.ModernClickListener;
import org.abh.common.ui.event.ModernSelectionListener;
import org.abh.common.ui.graphics.icons.MinusVectorIcon;
import org.abh.common.ui.graphics.icons.PlusVectorIcon;
import org.abh.common.ui.menu.ModernCheckBoxMenuItem;
import org.abh.common.ui.menu.ModernIconMenuItem;
import org.abh.common.ui.menu.ModernPopupMenu;
import org.abh.common.ui.menu.ModernTitleIconMenuItem;
import org.abh.common.ui.panel.ModernPanel;
import org.abh.common.ui.scrollpane.ModernScrollPane;
import org.abh.common.ui.scrollpane.ScrollBarPolicy;
import org.abh.common.ui.search.FilterEventListener;
import org.abh.common.ui.search.FilterModel;
import org.abh.common.ui.search.SortPanel;
import org.abh.common.ui.tree.ModernTree;
import org.abh.common.ui.tree.ModernTreeNodeRenderer;
import org.abh.common.ui.tree.TreeNodeFileCountRenderer;
import org.abh.common.ui.view.ViewModel;
import org.abh.common.ui.widget.ModernTwoStateWidget;
import org.abh.common.ui.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;


public class SamplesTreePanel extends ModernPanel implements ModernClickListener {
	private static final long serialVersionUID = 1L;

	private static final ModernTreeNodeRenderer TREE_RENDERER = 
			new TreeNodeFileCountRenderer();

	private static final ModernTreeNodeRenderer LIST_RENDERER = 
			new SamplesListTreeNodeRenderer();

	private ModernTree<Sample> mTree = new ModernTree<Sample>();
	
	private FilterModel mFilterModel = new FilterModel();
	
	private SortPanel<Sample> mSamplesSortPanel;

	private ModernPopupMenu menu;
	
	private ModernTwoStateWidget mSortMenuItem = 
			new ModernCheckBoxMenuItem("Sort Descending");
	
	private ModernIconMenuItem expandMenuItem = 
			new ModernIconMenuItem("Expand All", UIService.getInstance().loadIcon(PlusVectorIcon.class, 16));
	
	private ModernIconMenuItem collapseMenuItem = 
			new ModernIconMenuItem("Collapse All", UIService.getInstance().loadIcon(MinusVectorIcon.class, 16));


	private ViewModel mViewModel = 
			new ViewModel(SettingsService.getInstance().getAsString("edb.reads.chip-seq.default-view"));
	
	private SampleModel mSampleModel;

	private SampleSortModel mSortModel;


	
	private class MouseEvents extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			showPopup(e);
	    }

	    @Override
		public void mouseReleased(MouseEvent e) {
			showPopup(e);
	    }

		
	    private void showPopup(MouseEvent e) {
	        if (!e.getSource().equals(mTree)) {
	        	return;
	        }
	        
	        if (!e.isPopupTrigger()) {
	        	return;
	        }
	        
	    	menu.showPopup(e.getComponent(), e.getX(), e.getY());
	    }

	}
	
	private class SelectionEvents implements ModernSelectionListener {

		@Override
		public void selectionChanged(ChangeEvent e) {
			filterSamples();
		}
		
	}
	
	private class FilterEvents implements FilterEventListener {

		@Override
		public void filterChanged(ChangeEvent e) {
			loadSamples();
		}

		@Override
		public void filtersUpdated(ChangeEvent e) {
			//filterSamples();
		}
		
	}
	
	private class SortEvents implements ChangeListener {
		@Override
		public void changed(ChangeEvent e) {
			mSortMenuItem.setSelected(!mSortModel.getSortAscending());
			
			filterSamples();
		}
	}
	
	private class ViewEvents implements ChangeListener {

		@Override
		public void changed(ChangeEvent e) {
			viewChanged();
		}
		
	}
	
	public SamplesTreePanel(ModernWindow parent, 
			SampleModel sampleModel,
			SampleSortModel sortModel) {
		mSampleModel = sampleModel;
		mSortModel = sortModel;
		
		mSamplesSortPanel = new SortPanel<Sample>(parent, 
				mSortModel, 
				mFilterModel,
				mViewModel);

		setup();
	}

	private void setup() {
		
		mSortModel.addChangeListener(new SortEvents());
		mFilterModel.addFilterListener(new FilterEvents());
		mSampleModel.addSelectionListener(new SelectionEvents());
		mViewModel.addChangeListener(new ViewEvents());
		//setBackground(Color.PINK);
		
	
		
		mSamplesSortPanel.setBorder(BOTTOM_BORDER);
		setHeader(mSamplesSortPanel);

		//mTree.setNodeRenderer(new SamplesListTreeNodeRenderer());
		//mTree.setNodeRenderer(new SamplesTreeNodeRenderer());
		
		mTree.addMouseListener(new MouseEvents());
		
		//mTree.setBorder(RIGHT_BORDER);
		
		ModernScrollPane scrollPane = new ModernScrollPane(mTree)
				.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
				.setVScrollSep(PADDING);
		//scrollPane.setOpaque(true);
		//scrollPane.setBackground(Color.WHITE);
		//scrollPane.setBorder(DialogButton.DARK_BORDER);

		setBody(scrollPane); //new ModernDialogContentPanel(new ModernComponent(scrollPane, PADDING)));


		createMenu();
		
		// Set the default sorter
	    mSortModel.setSorter("ChIP-seq Type");
	    
	    // Ensure the UI matches the view
	    viewChanged();
	}
	
	private void createMenu() {
		menu = new ModernPopupMenu();
		
		menu.addClickListener(this);
		
		menu.add(new ModernIconMenuItem(UI.MENU_COPY, 
				UIService.getInstance().loadIcon("copy", 16)));
		
		menu.add(new ModernTitleIconMenuItem("Sort Options"));
		
		menu.add(expandMenuItem);
		menu.add(collapseMenuItem);
		
		menu.add(mSortMenuItem);
	}

	public final void addSelectionListener(ModernSelectionListener l) {
		mTree.addSelectionListener(l);
	}

	/*
	public final void showSampleWindows() {

		GuiFrame window;

		SampleSearchResult sample;

		for (ModernTreeNode<SampleSearchResult> node : tree.getSelectedNodes()) {
			if (node.getData() == null) {
				continue;
			}

			sample = node.getData();

			window = WindowServer.getInstance().findByName(sample.getName());

			if (window != null) {
				WindowServer.getInstance().setFocus(window);

				continue;
			}

			window = new SampleWindow(sample);

			window.setVisible(true);
		}
	}
	*/

	public void filterSamples() {
		
		mSortModel.getSorter().filter(mSampleModel.getItems(), mFilterModel);
	
		
		loadSamples();
	}
	
	public void loadSamples() {
		
		mSortModel.getSorter().arrange(mSampleModel.getItems(), 
				mTree, 
				mSortModel.getSortAscending(),
				mFilterModel);

		
		mTree.getRoot().setChildrenAreExpanded(mSortModel.getExpanded());
		
		
		// Select the first node that is not a header
		
		///if (mTree.getChildCount() > 1) {
		//	mTree.getSelectionModel().setSelection(1);
		//}
	}

	public List<Sample> getSelectedSamples() {

		List<Sample> samples = new ArrayList<Sample>();

		for (TreeNode<Sample> node : mTree.getSelectedNodes()) {
			if (node.getValue() == null) {
				continue;
			}

			Sample sample = node.getValue();

			samples.add(sample);
		}

		return samples;
	}
	
	public Sample getSelectedSample() {

		if (mTree.getSelectedNode() != null) {
			return mTree.getSelectedNode().getValue();
		} else {
			return null;
		}
	}

	public void setSelectedSample(Sample sample) {
		setSelectedSample(sample.getName());
	}
	
	public void setSelectedSample(String name) {
		setSelectedSample(mTree.getNodeIndexByName(name));
	}

	public void setSelectedSample(int row) {
		mTree.selectNode(row);
	}

	public void clicked(ModernClickEvent e) {
		if (e.getSource().equals(collapseMenuItem)) {
			mSortModel.setExpanded(false);
		} else if (e.getSource().equals(expandMenuItem)) {
			mSortModel.setExpanded(true);
		} else if (e.getSource().equals(mSortMenuItem)) {
			mSortModel.setSortAscending(!mSortMenuItem.isSelected());
		} else {
			//
		}
	}
	
	private void viewChanged() {
		if (mViewModel.getView().equals("tree")) {
			mTree.setNodeRenderer(TREE_RENDERER);
		} else {
			mTree.setNodeRenderer(LIST_RENDERER);
		}
		
		SettingsService.getInstance().update("edb.reads.chip-seq.default-view", 
				mViewModel.getView());
	}

	
}
