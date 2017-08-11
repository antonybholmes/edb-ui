package edu.columbia.rdf.edb.ui.search;

import org.abh.common.path.Path;
import org.abh.common.search.SearchStackOperator;
import org.abh.common.text.TextUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class UserSearchXmlHandler extends DefaultHandler {
	private UserSearch mUserSearch;
	private String mSearch;
	private String mMode = TextUtils.EMPTY_STRING;
	private SearchStackOperator mOperator;
	private SearchCategory mField;

	@Override
	public final void startElement(String uri,
    		String localName,
    		String qName,
    		Attributes attributes) throws SAXException {

    	if (qName.equals("user-search")) {
    		mUserSearch = new UserSearch();
    	} else if (qName.equals("field")) {
    		mField = new SearchCategory(attributes.getValue("name"),
    				new Path(attributes.getValue("field")));
    	} else {
    		// do nothing
    	}
	}
	
	@Override
	public final void endElement(String uri,
    		String localName,
    		String qName) throws SAXException {

    	if (qName.equals("user-search-entry")) {
    		UserSearchEntry entry = new UserSearchEntry(mOperator,
    				mField,
    				mSearch);
    		
    		mUserSearch.add(entry);
    	}
    	
    	mMode = TextUtils.EMPTY_STRING;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		if (mMode.equals("operator")) {
			mOperator = SearchStackOperator.parseOperator(new String(ch, start, length));
		} else if (mMode.equals("search")) {
			mSearch = new String(ch, start, length);
		} else {
			
		}
	}
    
    public UserSearch getSearch() {
    	return mUserSearch;
    }
}
