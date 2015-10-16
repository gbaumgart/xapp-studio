package pmedia.types;

import java.util.Comparator;


public class LocationSearchResultComparator implements Comparator<LocationSearchResult>{

    public int compare(LocationSearchResult a, LocationSearchResult b)
    {
    	return a.wordCount > b.wordCount ? 0 :1;
    }

}
