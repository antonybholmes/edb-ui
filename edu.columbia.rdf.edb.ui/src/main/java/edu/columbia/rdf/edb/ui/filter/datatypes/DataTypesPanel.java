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
package edu.columbia.rdf.edb.ui.filter.datatypes;


import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.text.ModernSubHeadingLabel;
import org.jebtk.modern.widget.ModernTwoStateWidget;

import edu.columbia.rdf.edb.Species;
import edu.columbia.rdf.edb.ui.filter.organisms.OrganismsModel;

/**
 * Displays groupings of samples so users can quickly find related
 * samples.
 * 
 * @author Antony Holmes
 *
 */
public class DataTypesPanel extends ModernComponent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private DataTypesModel mModel;

	private Map<ModernTwoStateWidget, Type> mCheckMap =
			new HashMap<ModernTwoStateWidget, Type>();

	private Map<ModernTwoStateWidget, Species> mCheckOrganismsMap =
			new HashMap<ModernTwoStateWidget, Species>();

	private ModernTwoStateWidget mCheckAll = 
			new ModernCheckSwitch("Select All", true);

	private OrganismsModel mOrganismsModel; 

	public DataTypesPanel(DataTypesModel model, OrganismsModel organismsModel) {
		mModel = model;
		mOrganismsModel = organismsModel;

		Box box = VBox.create();

		box.add(mCheckAll);
		box.add(UI.createVGap(20));


		box.add(new ModernSubHeadingLabel("Data Types"));
		box.add(UI.createVGap(10));



		mCheckAll.addClickListener(new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				checkAll();
			}});

		ModernClickListener l = new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				ModernTwoStateWidget check = (ModernTwoStateWidget)e.getSource();

				Type g = mCheckMap.get(check);

				mModel.setUse(g, check.isSelected());
			}
		};

		for (Type t : model) {
			ModernTwoStateWidget check =
					new ModernCheckSwitch(t.getName(), model.getUse(t));

			box.add(check);

			mCheckMap.put(check, t);

			check.addClickListener(l);
		}

		box.add(UI.createVGap(20));
		box.add(new ModernSubHeadingLabel("Organisms"));
		box.add(UI.createVGap(10));

		//box.add(mCheckAll);
		//box.add(UI.createVGap(10));

		l = new ModernClickListener() {
			@Override
			public void clicked(ModernClickEvent e) {
				ModernTwoStateWidget check = (ModernTwoStateWidget)e.getSource();

				Species o = mCheckOrganismsMap.get(check);

				mOrganismsModel.setUse(o, check.isSelected());
			}
		};

		for (Species t : organismsModel) {
			ModernTwoStateWidget check =
					new ModernCheckSwitch(t.getName(), organismsModel.getUse(t));

			box.add(check);

			mCheckOrganismsMap.put(check, t);

			check.addClickListener(l);
		}

		setBody(new ModernScrollPane(box)
				.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER)
				.setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW));

		setBorder(BORDER);
	}

	private void checkAll() {
		for (ModernTwoStateWidget c : mCheckMap.keySet()) {
			c.setSelected(mCheckAll.isSelected());
			mModel.updateUse(mCheckMap.get(c), c.isSelected());
		}

		for (ModernTwoStateWidget c : mCheckOrganismsMap.keySet()) {
			c.setSelected(mCheckAll.isSelected());
			mOrganismsModel.updateUse(mCheckOrganismsMap.get(c), c.isSelected());
		}

		// Finally trigger a refresh via the model
		mModel.fireChanged();
	}
}

