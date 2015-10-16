package pmedia.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.EventData;
import pmedia.types.LocationData;

public class Cache 
{
	
	public static Domain getDomain(String title)
	{
		if(domains !=null && domains.size() > 0)
		{
			for(int i =0 ; i < domains.size() ; i ++ )
			{
				Domain d= domains.get(i);
				if(d !=null)
				{
					if(d.title.equalsIgnoreCase(title))
						return d;
				}
			}
		}
		return null;
	}
	public static Domain domain = null;
	public static Boolean locationCheck=false;
	
	public static ArrayList<Domain> domains =null;
	public static ArrayList<Category> categories =null;
	public static ArrayList<Category> locationCategories =null;
	public static ArrayList<LocationData> locations =null;
	public static ArrayList<EventData> lastFBEvents=null;
	
	public HashMap<String, ArrayList<EventData>>fbUserEvents=null;
	public HashMap<String, ArrayList<EventData>>ticketLocationEvents=null;
	
	public void insertFBUserEvent(String userID, EventData event)
	{
		
		if(fbUserEvents ==null)
			fbUserEvents = new HashMap<String, ArrayList<EventData>>();
		
		if(ticketLocationEvents ==null)
			ticketLocationEvents = new HashMap<String, ArrayList<EventData>>();
		
		
		if(event.eventSourceType.equals("Facebook"))
        {
			//get the list
			ArrayList<EventData> eList = fbUserEvents.get(userID);
			if(eList ==null)
				eList = new ArrayList<EventData>();
			
			if(!eList.contains(event))
			{
				eList.add(event);
				fbUserEvents.put(userID, eList);
			}
        }
		
		if(event.eventSourceType.equals("Tickets"))
        {
			//get the list
			ArrayList<EventData> eList = ticketLocationEvents.get(userID);
			if(eList ==null)
			{
				eList = new ArrayList<EventData>();
			}
			
			if(!eList.contains(event))
			{
				eList.add(event);
				ticketLocationEvents.put(userID, eList);
			}
        }
	}
}
