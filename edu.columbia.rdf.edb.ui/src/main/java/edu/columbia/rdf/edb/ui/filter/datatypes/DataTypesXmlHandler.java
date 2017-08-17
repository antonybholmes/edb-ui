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
package edu.columbia.rdf.edb.ui.filter.datatypes;



import org.jebtk.core.text.TextUtils;
import org.jebtk.bioinformatics.annotation.Type;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



// TODO: Auto-generated Javadoc
/**
 * The class KeyXmlHandler.
 */
public class DataTypesXmlHandler extends DefaultHandler {
	
	
	private DataTypesModel mModel;

	/**
	 * Instantiates a new key xml handler.
	 *
	 * @param service the service
	 */
	public DataTypesXmlHandler(DataTypesModel model) {
		mModel = model;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, 
			String localName,
			String qName, 
            Attributes attributes) throws SAXException {
		
		if (qName.equals("type")) {
			String name = attributes.getValue("name").toLowerCase();
			boolean selected = attributes.getValue("selected").equals(TextUtils.TRUE);
			
			for (Type t : mModel) {
				if (t.getName().toLowerCase().contains(name)) {
					mModel.updateUse(t, selected);
				}
			}
		} 
	}
}
