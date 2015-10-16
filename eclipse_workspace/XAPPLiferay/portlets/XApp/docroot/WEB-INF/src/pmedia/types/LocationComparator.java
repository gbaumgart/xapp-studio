package pmedia.types;

import java.util.Comparator;

import pmedia.utils.StringHelper;

public class LocationComparator implements Comparator<LocationData> 
{

	    public int compare(LocationData a, LocationData b) 
	    {
	    	String locationTermsA[] = StringHelper.split(a.title, " ");
	    	String locationTermsB[] = StringHelper.split(b.title, " ");
	    	
	    	String aStr = a.title;
	    	String bStr = b.title;
	    	if(locationTermsA.length > 0)
	    		aStr = locationTermsA[0];
	    	
	    	if(locationTermsB.length > 0)
	    		bStr = locationTermsB[0];
            
	    	return aStr.compareToIgnoreCase(bStr);
            
	    }
}
