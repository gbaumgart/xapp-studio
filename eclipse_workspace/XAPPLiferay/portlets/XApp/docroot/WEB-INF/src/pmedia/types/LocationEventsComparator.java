package pmedia.types;

import java.util.ArrayList;
import java.util.Comparator;

import pmedia.DataManager.Cache;

public class LocationEventsComparator implements Comparator<EventData>
{

	 Cache cache= null;
	 public LocationEventsComparator( Cache c )
	 {
		 cache = c;
	 }
	 public int compare(EventData a, EventData b)
	 {

		 if(cache ==null)
			 return 1;

		 if(cache.ticketLocationEvents == null)
			 return 1;

		 if(a.loc !=null && b.loc !=null )
		 {

			 String locIDA = "" + a.loc.location_id;
			 String locIDB = "" + b.loc.location_id;

			 ArrayList<EventData>aList = cache.ticketLocationEvents.get( locIDA );
			 ArrayList<EventData>bList = cache.ticketLocationEvents.get( locIDB );

			 if(aList !=null && bList !=null)
			 {
				 return aList.size() > bList.size() ? 0 : 1;
			 }
		 }
		 return 1;
	  }
}