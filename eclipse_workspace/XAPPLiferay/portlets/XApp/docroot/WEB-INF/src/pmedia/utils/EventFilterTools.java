package pmedia.utils;

import java.util.ArrayList;
import java.util.Date;
import pmedia.types.EventData;

public class EventFilterTools
{

	
	 public static String stripNonValidXMLCharacters(String in) 
	 {

		 
	        StringBuffer out = new StringBuffer(); // Used to hold the output.
	        char current; // Used to reference the current character.

	        if (in == null || ("".equals(in))) return ""; // vacancy test.
	        for (int i = 0; i < in.length(); i++) 
	        {
	            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
	            if ((current == 0x9) ||
	                (current == 0xA) ||
	                (current == 0xD) ||
	                ((current >= 0x20) && (current <= 0xD7FF)) ||
	                ((current >= 0xE000) && (current <= 0xFFFD)) ||
	                ((current >= 0x10000) && (current <= 0x10FFFF)))
	            {
	                out.append(current);
	                //System.out.println(" stripping invalid utf8 char : " + current);
	            }else
	            {
	            	//System.out.println(" stripping invalid utf8 char : " + current);
	            }
	        }
	        int before = out.toString().length();
	        String result = out.toString().replace("[^a-zA-Z0-9_.]+", "");
	        int after = result.length();
	        if(before!=after)
	        	System.out.println(" removed special char");
	        return result;
	        //return Regexp(out.toString(), "[^a-zA-Z0-9_.]+", "");
	    }    

	
	public static ArrayList<EventData>clean(ArrayList<EventData>a)
	{
		ArrayList<EventData> result = new ArrayList<EventData>();
		return result;
	}
	public static ArrayList<EventData>combine(ArrayList<EventData>a,ArrayList<EventData>b)
	{
		ArrayList<EventData> result = new ArrayList<EventData>();
		result.addAll(a);
		result.addAll(b);
		return result;
		/*
		for (int s = 0; s < b.size() ; s++)
		{
		    EventData e = b.get(s);
		    a.add(e);
		    
		}
		*/
		
	}
	
	public static ArrayList<EventData>getRange(ArrayList<EventData>src,Date start,Date end)
	{
		ArrayList<EventData> result = new ArrayList<EventData>();
		
		for (int s = 0; s < src.size() ; s++)
		{
		    EventData e = src.get(s);
		    if(e !=null)
		    {
		    	
		    	Date a = (Date)e.start_time.clone();
		    	Date b = (Date)e.end_time.clone();
		    	a.setHours(0);
		    	b.setHours(0);
		    	if(  a.getTime() >= start.getTime() && a.getTime() <= end.getTime()   )
		    	{
		    		result.add(e);
		    	}
		    	
		    	/*Calendar startDate = Calendar.getInstance();
				startDate.setTime(sTime);
				//startDate.add(Calendar.HOUR, -8);
				java.sql.Timestamp sTimeT = new java.sql.Timestamp(startDate.getTimeInMillis());

				Calendar endDate = Calendar.getInstance();
				endDate.setTime(eTime);
				//endDate.add(Calendar.HOUR, -8);
				java.sql.Timestamp eTimeT = new java.sql.Timestamp(endDate.getTimeInMillis());
*/
		    }
		}
		
		return result;
	}
	
}

