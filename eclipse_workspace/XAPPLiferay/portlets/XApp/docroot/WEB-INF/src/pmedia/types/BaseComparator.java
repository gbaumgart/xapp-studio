package pmedia.types;
import java.util.Comparator;

import pmedia.utils.StringHelper;

public class BaseComparator implements Comparator<BaseData>
{
	 public String lang=null;
	 public int compare(BaseData a, BaseData b) 
	 {
	    	String catTermsA[] = StringHelper.split(a.title, " ");
	    	String catTermsB[] = StringHelper.split(b.title, " ");
	    	
	    	String aStr = a.title;
	    	String bStr = b.title;
	    	
	    	if(catTermsA.length > 0)
	    		aStr = catTermsA[0];
	    	
	    	if(catTermsB.length > 0)
	    		bStr = catTermsB[0];
         
	    	return aStr.compareToIgnoreCase(bStr);
         
	  }
}
