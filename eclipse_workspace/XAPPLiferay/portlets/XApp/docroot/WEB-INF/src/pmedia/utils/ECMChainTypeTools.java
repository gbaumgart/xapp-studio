package pmedia.utils;

import cmx.types.ECMCainType;

public class ECMChainTypeTools{

	public static int toInteger(ECMCainType type)
	{
		int res = -1;
		
		if(type==ECMCainType.INPUTS)
		{
			return 0;
		}
		
		if(type==ECMCainType.OUTPUTS)
		{
			return 1;
		}
		
		return res;
		
	}
}
