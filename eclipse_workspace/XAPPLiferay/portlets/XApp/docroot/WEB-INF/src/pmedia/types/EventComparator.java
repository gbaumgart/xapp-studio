package pmedia.types;

import java.util.Comparator;
import java.util.Date;

import pmedia.utils.StringHelper;

public class EventComparator implements Comparator<EventData> 
{

	   @Override
		public int compare(EventData arg0, EventData arg1) 
	   {
		    
		   Date startTimeA =arg0.getStart_time();
		   Date startTimeB =arg1.getStart_time();
		   
		   if(startTimeA!=null && startTimeB!=null)
		   {
			   return startTimeA.compareTo(startTimeB);
		   }
		   
		    
			return 0;
		}
}
