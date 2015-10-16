package pmedia.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class TimeUtils 
{
	
	public static Date fromDojoSpinWheel(String input)
	{
		
		Date result = new Date();
		
		 //SimpleDateFormat format = new SimpleDateFormat("yyyy','MMM','dd");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	        // See if we can parse the output of Date.toString()
	        try {
	            result = format.parse(input);
	            System.out.println(" 2. " + result.toString());
	        }
	        catch(ParseException pe) {
	            System.out.println("ERROR: Cannot parse \"" + input + "\"");
	        }
		
		
		return result;
	}
	
	public static long hoursBetween(Date d1, Date d2) 
	{  
		Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        
        // Set the date for both of the calendar instance
        cal1.setTime(d1);
        cal2.setTime(d2);
        
        // Get the represented date in milliseconds
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        
        // Calculate difference in milliseconds
        long diff = milis2 - milis1;
        
        // Calculate difference in seconds
        long diffSeconds = diff / 1000;
        
        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);
        // Calculate difference in hours
        long diffHours = diff / (60 * 60 * 1000);
        return diffHours;
        
        
		//TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
		
		/*
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(d1);  
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(d2);  
		Calendar date = (Calendar) startDate.clone();  
		long daysBetween = 0;  
		
		while (date.before(endDate)) 
		{  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}*/  
		
		//return daysBetween;  
	}  
	public static long daysBetween(Date d1, Date d2) 
	{  
		Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        
        // Set the date for both of the calendar instance
        cal1.setTime(d1);
        cal2.setTime(d2);
        
        // Get the represented date in milliseconds
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        
        // Calculate difference in milliseconds
        long diff = milis2 - milis1;
        
        // Calculate difference in seconds
        long diffSeconds = diff / 1000;
        
        // Calculate difference in minutes
        long diffMinutes = diff / (60 * 1000);
        
        // Calculate difference in hours
        long diffHours = diff / (60 * 60 * 1000);
        
        // Calculate difference in days
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
        
        
		//TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
		
		/*
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(d1);  
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(d2);  
		Calendar date = (Calendar) startDate.clone();  
		long daysBetween = 0;  
		
		while (date.before(endDate)) 
		{  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}*/  
		
		//return daysBetween;  
	}  
}
