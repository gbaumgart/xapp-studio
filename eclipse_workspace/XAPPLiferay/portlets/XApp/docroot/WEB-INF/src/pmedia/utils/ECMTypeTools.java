package pmedia.utils;

import cmx.types.ECMType;

public class ECMTypeTools {

	public static int ToInteger(ECMType type)
	{
		int res = -1;
		
		if(type==ECMType.Layout)
		{
			return 0;
		}
		
		if(type==ECMType.ContentSource)
		{
			return 1;
		}
		
		if(type==ECMType.BannerSource)
		{
			return 2;
		}
		
		if(type==ECMType.BannerSource2)
		{
			return 2;
		}
		
		return res;
		
	}
}
