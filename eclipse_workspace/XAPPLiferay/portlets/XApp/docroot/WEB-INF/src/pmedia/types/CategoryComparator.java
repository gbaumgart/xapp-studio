package pmedia.types;
import java.util.Comparator;

import pmedia.utils.StringHelper;

public class CategoryComparator implements Comparator<Category>
{
	 public String lang=null;
	 public int compare(Category a, Category b) 
	 {
	    	String catTermsA[] = StringHelper.split(a.title, " ");
	    	String catTermsB[] = StringHelper.split(b.title, " ");
	    	
	    	String aStr = a.title;
	    	String bStr = b.title;
	    	
	    	if(lang!=null)
	    	{
	    		TranslationData ta = a.getTranslation(lang);
	    		if(ta!=null &&  ta.title!=null && ta.title.length() > 0)
	    			aStr=ta.title;
	    		
	    		TranslationData tb = b.getTranslation(lang);
	    		if(tb!=null && tb.title!=null && tb.title.length() > 0)
	    			aStr=tb.title;
	    		
	    	}
	    	if(catTermsA.length > 0)
	    		aStr = catTermsA[0];
	    	
	    	if(catTermsB.length > 0)
	    		bStr = catTermsB[0];
         
	    	return aStr.compareToIgnoreCase(bStr);
         
	  }
}
