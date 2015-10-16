package pmedia.utils;
import java.util.Comparator;

import pmedia.types.CContent;
import pmedia.types.EventData;
public class CEventDateComparator implements Comparator<CContent> 
{

	    public int compare(CContent a, CContent b) 
	    {
	    	
	    	int result = a.startDate.getTime() > b.startDate.getTime() ? 1 : 0;
	    	//System.out.println(a.start_time +   "  - " + b.start_time + " = " + result );
	    	return result;
	    }
}