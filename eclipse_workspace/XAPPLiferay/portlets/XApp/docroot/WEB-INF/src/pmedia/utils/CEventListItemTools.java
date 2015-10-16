package pmedia.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import pmedia.types.CContent;
import pmedia.types.CEventItem;
import pmedia.types.CGroupedList;
import pmedia.types.CList;

public class CEventListItemTools extends CListItemToolsBase 
{
	public static ArrayList toCEventItems(
			ArrayList <CContent>data,Locale local)
	{
		ArrayList<CEventItem>result = new ArrayList<CEventItem>();
		DateFormat df = new SimpleDateFormat("EEEE  HH:mm");
		
		if(local==null){
			df = new SimpleDateFormat("EEEE  HH:mm");
		}else{
			df= new SimpleDateFormat("EEEE  HH:mm",local);
		}
		  
		for(int i = 0 ; i < data.size() ; i++)
		{
			   CContent e = (CContent) data.get(i);
			   
			   CEventItem item  = new CEventItem();
			   item.setTitle(e.getTitle());
			   //item.setGroupIdStr(e.getGroupStr());
			   item.setStartDate(e.startDate);
			   item.setEndDate(e.endDate);
			   item.setLocRefStr(e.getLocRefStr());
			   item.setSourceType(e.getSourceType());
			   item.setDataSource(e.getDataSourceUID());
			   item.setRefIdStr(e.getRefIdStr());
			   item.setOwnerRefStr(e.getOwnerRefStr());
			   item.setStartDateString(df.format(e.startDate));
			   item.setEndDateString(df.format(e.endDate));
			   //item.setVenueString(e.getLocRefStr());
			   item.setOwnerRefStr(e.getLocRefStr());
			   result.add(item);
		}
		return result;
	
	}
	
	public static CGroupedList createGoupedListByStartDate(
			ArrayList data,
			ApplicationManager manager,
			Application application,
			DataSourceBase ds,
			Locale locale,
			int maxDiff)
	{
		
		CGroupedList groupedList = new CGroupedList();
		if(data.size()==0){
			return groupedList;
		}
		groupedList.setTitle("Events");
		
		
		
	   CEventItem  currentElement = (CEventItem) data.get(0);
	   Date currentDate = currentElement.getStartDate();
	   
	   CList dst  = new CList();
	   dst.getItems().add(currentElement);
	   
	   
	   String dformat= new SimpleDateFormat("dd. EEEE MMM",locale).format(currentElement.getStartDate());
	   dst.setTitle(dformat);
		
	   Calendar startCDate = Calendar.getInstance();
	   Calendar eCDate = Calendar.getInstance();
	   
	   startCDate.setTime(currentDate);
	   startCDate.set(Calendar.HOUR, 0);
	   startCDate.set(Calendar.MINUTE, 0);
	   currentDate = startCDate.getTime();
	   
	   groupedList.items.add(dst);
	   
	   
	   int  dDay = startCDate.get(Calendar.DAY_OF_YEAR); 

	   for(int i = 1 ; i < data.size() ; i++)
	   {
		   CEventItem nextElement = (CEventItem) data.get(i);
		   
		
		   eCDate.setTime(nextElement.getStartDate());
		   eCDate.set(Calendar.HOUR, 1);
		   eCDate.set(Calendar.MINUTE, 1);
		   int  cdDay = eCDate.get(Calendar.DAY_OF_YEAR); 
		   if(maxDiff!=0)
		   {
			   if(groupedList.getItems().size() >=maxDiff )
			   {
				   break;
			   }
		   }
		   if(cdDay!=dDay)
		   {
			   dDay=cdDay;
			   currentElement = nextElement;
			   currentDate = eCDate.getTime();
			   dst = new CList();
			   dformat= new SimpleDateFormat("dd. EEEE MMM",locale).format(currentElement.getStartDate());
			   dst.setTitle(dformat);
			   
			   groupedList.items.add(dst);
		   }
		   dst.getItems().add(nextElement);
	   }
		return groupedList;
		
	}
	
	
}
