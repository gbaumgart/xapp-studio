package pmedia.types;

import java.util.ArrayList;
import java.util.Comparator;

import pmedia.DataManager.Cache;

public class FBUserEventsComparator implements Comparator<EventData>
{

	 Cache cache= null;
	 public FBUserEventsComparator( Cache c )
	 {
		 cache = c;
	 }
	 public int compare(EventData a, EventData b)
	 {

		 if(cache ==null)
			 return 1;

		 if(a.creator_id !=null && b.creator_id !=null && cache.fbUserEvents !=null)
		 {

			 ArrayList<EventData>aList = cache.fbUserEvents.get( a.creator_id );
			 ArrayList<EventData>bList = cache.fbUserEvents.get( b.creator_id );

			 if(aList !=null && bList !=null)
			 {
				 return aList.size() > bList.size() ? 0 : 1;
			 }
		 }
		 return 1;
	  }
}