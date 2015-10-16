package pmedia.utils;

import cmx.types.CIGroupType;
import cmx.types.CIType;

public class CIGroupTools {

	public static int CIGroupToInteger(CIGroupType type)
	{
		int res = -1;
		if(type==CIGroupType.Data)
		{
			return 0;
		}
		
		if(type==CIGroupType.Visual)
		{
			return 1;
		}
		
		if(type==CIGroupType.Unkown)
		{
			return -1;
		}
		return res;
		
	}
}
