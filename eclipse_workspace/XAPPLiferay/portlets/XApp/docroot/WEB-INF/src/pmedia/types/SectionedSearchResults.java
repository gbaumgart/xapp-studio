package pmedia.types;

import java.util.ArrayList;
import java.util.HashMap;
public class SectionedSearchResults
{
	private HashMap<PMSearchSourceType, ArrayList<SearchResult>>searchResults=new HashMap<PMSearchSourceType, ArrayList<SearchResult>>();

	/**
	 * @return the searchResults
	 */
	public HashMap<PMSearchSourceType, ArrayList<SearchResult>> getSearchResults() {
		return searchResults;
	}

	/**
	 * @param searchResults the searchResults to set
	 */
	public void setSearchResults(
			HashMap<PMSearchSourceType, ArrayList<SearchResult>> searchResults) {
		this.searchResults = searchResults;
	}
	
}
