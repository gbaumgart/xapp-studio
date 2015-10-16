package pmedia.utils;

import pmedia.types.CIOverrideFlags;
import cmx.types.ECMContentSourceType;

public class CIOverrideFlagTools {

	public static CIOverrideFlags FromString(String value)
	{
		int type = Integer.parseInt(value);
		
		if(type==0)
		{
			return CIOverrideFlags.None;
		}
		
		if(type==1)
		{
			return CIOverrideFlags.HideInThisElement;
		}
		
		if(type==2)
		{
			return CIOverrideFlags.HideInAllFollowElements;
		}
		
		if(type==2)
		{
			return CIOverrideFlags.ShowInThisElement;
		}
		if(type==2)
		{
			return CIOverrideFlags.ShowInAllFollowElements;
		}
		
		return CIOverrideFlags.Unknown;
		
	}
	public static int TypeToInteger(CIOverrideFlags type)
	{
		int res = 0;
		
		if(type==CIOverrideFlags.None)
		{
			return 0;
		}
		
		if(type==CIOverrideFlags.HideInThisElement)
		{
			return 1;
		}
		
		if(type==CIOverrideFlags.HideInAllFollowElements)
		{
			return 2;
		}
		if(type==CIOverrideFlags.ShowInThisElement)
		{
			return 3;
		}
		if(type==CIOverrideFlags.ShowInAllFollowElements)
		{
			return 4;
		}
		
		if(type==CIOverrideFlags.Unknown)
		{
			return 5;
		}
		return res;
		
	}
}
