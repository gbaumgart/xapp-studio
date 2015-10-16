package pmedia.utils;

import cmx.types.ECMLayoutType;

public class ECMLayoutTypeTools {

	public static int ECMLayoutTypeToInteger(ECMLayoutType type)
	{
		int res = -1;
		
		if(type==ECMLayoutType.Icons)
		{
			return 0;
		}
		
		if(type==ECMLayoutType.BottomTabs)
		{
			return 1;
		}
		
		if(type==ECMLayoutType.TopTabs)
		{
			return 2;
		}
		
		return res;
		
	}
}
