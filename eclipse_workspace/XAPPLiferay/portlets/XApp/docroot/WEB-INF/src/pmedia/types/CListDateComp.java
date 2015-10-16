package pmedia.types;

import java.util.Comparator;
import java.util.Date;

public class CListDateComp implements Comparator<CListItem> {

	 
	
		@Override
	public int compare(CListItem arg0, CListItem arg1) {

		if( arg0.creationDate!=null && arg1.creationDate!=null)
		{
			 return arg0.creationDate.compareTo(arg1.creationDate);
		 }
		
		return 0;
	}
	
}