package pmedia.utils;
import java.util.Comparator;
import pmedia.types.EventData;
public class EventDateComparator implements Comparator<EventData> 
{

	    public int compare(EventData a, EventData b) 
	    {
	    	
	    	int result = a.start_time.getTime() > b.start_time.getTime() ? 1 : 0;
	    	//System.out.println(a.start_time +   "  - " + b.start_time + " = " + result );
	    	return result;
	    }
}