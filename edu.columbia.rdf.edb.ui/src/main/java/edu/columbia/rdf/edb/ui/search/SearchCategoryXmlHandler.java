package edu.columbia.rdf.edb.ui.search;

import org.abh.common.path.StrictPath;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class SearchCategoryXmlHandler extends DefaultHandler {
	private SearchCategory category;
	private SearchCategoryGroup group;

	public final void startElement(String uri,
    		String localName,
    		String qName,
    		Attributes attributes) throws SAXException {

    	if (qName.equals("group")) {
    		group = new SearchCategoryGroup(attributes.getValue("name"), 
    				attributes.getValue("display") != null ? attributes.getValue("display").equals("true") : true);
    	} else if (qName.equals("category")) {
    		category = new SearchCategory(attributes.getValue("name"), 
    				new StrictPath(attributes.getValue("path")),
    				attributes.getValue("description") != null ? attributes.getValue("description") : "");
    			
    		group.addCategory(category);
        } else {
    		// do nothing
    	}
    }

    public final void endElement(String uri,
    		String localName,
    		String qName) throws SAXException {
    	if (qName.equals("group")) {
    		SearchCategoryService.getInstance().addGroup(group);
    	}
    }
}
