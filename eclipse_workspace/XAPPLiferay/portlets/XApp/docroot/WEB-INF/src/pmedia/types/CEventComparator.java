package pmedia.types;

import java.util.Comparator;
import java.util.Date;

import pmedia.utils.StringHelper;

public class CEventComparator implements Comparator<CContent> 
{

	   @Override
		public int compare(CContent arg0, CContent arg1) 
	   {
		    
		   Date startTimeA =arg0.startDate;
		   Date startTimeB =arg1.endDate;
		   if(startTimeA!=null && startTimeB!=null)
		   {
			   return startTimeA.compareTo(startTimeB);
		   }
			return 0;
		}
}
