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
package edu.columbia.rdf.edb.ui.filter.organisms;


import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import org.abh.common.ui.ModernComponent;
import org.abh.common.ui.UI;
import org.abh.common.ui.button.ModernCheckSwitch;
import org.abh.common.ui.event.ModernClickEvent;
import org.abh.common.ui.event.ModernClickListener;
import org.abh.common.ui.panel.VBox;
import org.abh.common.ui.scrollpane.ModernScrollPane;
import org.abh.common.ui.scrollpane.ScrollBarPolicy;
import org.abh.common.ui.text.ModernSubHeadingLabel;
import org.abh.common.ui.widget.ModernTwoStateWidget;

import edu.columbia.rdf.edb.Species;

/**
 * Displays groupings of samples so users can quickly find related
 * samples.
 * 
 * @author Antony Holmes
 *
 */
public class OrganismsPanel extends ModernComponent implements ModernClickListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private OrganismsModel mModel;

	private Map<ModernTwoStateWidget, Species> mCheckMap =
			new HashMap<ModernTwoStateWidget, Species>();
	
	private ModernTwoStateWidget mCheckAll = new ModernCheckSwitch("Select All"); 

	public OrganismsPanel(OrganismsModel model) {
		mModel = model;
		
		Box box = VBox.create();
		
		box.add(new ModernSubHeadingLabel("Organisms"));
		box.add(UI.createVGap(10));
		
		box.add(mCheckAll);
		box.add(UI.createVGap(10));
		
		mCheckAll.addClickListener(new ModernClickListener() {

			@Override
			public void clicked(ModernClickEvent e) {
				checkAll();
			}});
		
		for (Species t : model) {
			ModernTwoStateWidget check =
					new ModernCheckSwitch(t.getName(), model.getUse(t));
			
			box.add(check);
			
			mCheckMap.put(check, t);
			
			check.addClickListener(this);
		}
		
		setBody(new ModernScrollPane(box)
				.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
				.setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW));
		
		setBorder(DOUBLE_BORDER);
	}

	@Override
	public void clicked(ModernClickEvent e) {
		ModernTwoStateWidget check = (ModernTwoStateWidget)e.getSource();
		
		Species g = mCheckMap.get(check);
	
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
	
