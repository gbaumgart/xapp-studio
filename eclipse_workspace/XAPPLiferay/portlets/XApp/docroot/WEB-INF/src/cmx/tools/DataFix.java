package cmx.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;

import pmedia.DataUtils.EventDataArrayTools;
import pmedia.types.EventData;


public class DataFix
{



	public static void fixEvent(EventData event)
	{

		Calendar startDate = Calendar.getInstance();
		String extraString="";
		String extraDescription="";
		String dateString = "";
		
		//07/14/2012 (using the American way of putting the month first, then the day then the year), this should also be in bold,

		if(event.start_time!=null)
		{
			startDate.setTime(event.start_time);
			
			extraString = (startDate.get(Calendar.MONTH)+1) + "/"  + startDate.get(Calendar.DATE)  + "/"  + startDate.get(Calendar.YEAR)  + " ";
			
			dateString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			
			extraDescription =  (startDate.get(Calendar.MONTH)+1) + "/"  + startDate.get(Calendar.DATE)  + "/"  + startDate.get(Calendar.YEAR)  + " ";
			
			extraString = "" + extraDescription;
			String minuteString = "" + startDate.get(Calendar.MINUTE);
			String hourString = "" + startDate.get(Calendar.HOUR_OF_DAY);
			if(minuteString.equals("0"))
			{
				minuteString = "00";
			}
			if(hourString.equals("0"))
			{
				hourString = "00";
			}
			extraDescription = extraDescription + hourString + ":" + minuteString ;

		}else
		{
			//System.out.println("start date invalid  : " + event.description);
		}

		if(event.end_time!=null)
		{

			startDate.setTime(event.end_time);
			//extraString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			extraDescription +=" - "+(startDate.get(Calendar.MONTH)+1) + "/"  + startDate.get(Calendar.DATE)  + "/"  + startDate.get(Calendar.YEAR)  + " ";
			String minuteString = "" + startDate.get(Calendar.MINUTE);
			String hourString = "" + startDate.get(Calendar.HOUR_OF_DAY);
			if(minuteString.equals("0"))
			{
				minuteString = "00";
			}
			if(hourString.equals("0"))
			{
				hourString = "00";
			}
			extraDescription = extraDescription + hourString + ":" + minuteString ;
		}

		//extraDescription +="<br/>";
		if(event.extraInfo !=null && event.extraInfo.length() == 1 && event.extraInfo.equals("."))
		{
			event.extraInfo = "";
		}


		event.extraInfo = extraString + " " + event.extraInfo;

		if(event.description==null)
		{
			event.description = "";
		}
		
		event.description ="<b>" +extraDescription +"</b><br/>" + event.description;
		
		event.description = "<b>" + event.title +"</b><br/><br/>" + event.description;
		
		event.description = event.description.replaceAll("\\p{Cntrl}", "");
		
	}
	public static int cMIndex= 0;
	public static EventData cloneEvent(EventData event,int index)
	{

		EventData out = new EventData();
		out.groupId = event.groupId;
		out.extraInfo = event.extraInfo;
		out.locRefId = event.locRefId;
		out.description = event.description;
		out.title =event.title;
		
		if(event.locRefStr!=null && event.locRefStr.length() > 0)
		{
			event.locRefStr = event.locRefStr;
		}
		
		out.setPublished(event.published);
		
		int startIndex=index*19;
		String stime = event.sStartRepition.substring(startIndex, startIndex + 19);
		String etime = event.sEndRepition.substring(startIndex, startIndex + 19);
		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try
		{
			out.start_time = df.parse(stime);
		} catch (ParseException e)
		{
			e.printStackTrace();

		}
		try
		{
			out.end_time = df.parse(etime);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		if(event.iconUrl!=null){
			out.iconUrl=event.iconUrl;
		}
		
		cMIndex +=1;
		out.uid = "" + cMIndex;
		out.refId=cMIndex;
		out.id=cMIndex;
		return out;

	}
	public static void checkMultiEvents(ArrayList <EventData>events)
	{
		for(int i = 0 ; i < events.size() ; i ++)
		{
			EventData ev = events.get(i);
			if(ev.published &&  ev.sStartRepition!=null && ev.sStartRepition.length() > 20)
			{
				//System.out.println("mstart time : " + ev.sStartRepition);

				int nbEvents = ev.sStartRepition.length() / 20;
				for(int sindex =  1 ; sindex < nbEvents   ; sindex ++)
				{
					EventData clone  = cloneEvent(ev, sindex);
					events.add(clone);
					//System.out.println("mstart time : " + ev.sStartRepition + " nb dates :" + nbEvents);
				}


			}
		}
	}
	public static void fixEvents(ArrayList<EventData>elist,String outputPath)
	{
		checkMultiEvents(elist);
		if(elist!=null && elist.size() > 0 )
		{
			for(int i = 0 ; i < elist.size() ; i ++)
			{
				EventData ev = elist.get(i);
				fixEvent(ev);
			}
			try
			{
				EventDataArrayTools.convertToXML2(elist, outputPath, true, null);
			} catch (ParserConfigurationException e)
			{
				e.printStackTrace();
			}

		}
		//System.out.println("got events : " + elist.size());

	}

}
