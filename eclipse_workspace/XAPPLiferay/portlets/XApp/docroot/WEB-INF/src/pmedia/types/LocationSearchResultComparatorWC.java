package pmedia.types;

import java.util.Comparator;

public class LocationSearchResultComparatorWC implements Comparator<LocationSearchResult> {

	public int compare(LocationSearchResult a, LocationSearchResult b)
    {
    	return a.charMatchCount == 0 ? 1 : b.charMatchCount ==0 ? 1:0;
    }
}
