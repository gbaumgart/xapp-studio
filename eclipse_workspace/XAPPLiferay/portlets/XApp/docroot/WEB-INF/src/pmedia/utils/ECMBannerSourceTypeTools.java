package pmedia.utils;

import cmx.types.ECMBannerSourceType;

public class ECMBannerSourceTypeTools {

	public static ECMBannerSourceType FromString(String value)
	{
		int type = Integer.parseInt(value);

		if(type==0)
		{
			return ECMBannerSourceType.Unknown;
		}
		
		if(type==1)
		{
			return ECMBannerSourceType.JoomlaSection;
		}
		
		if(type==2)
		{
			return ECMBannerSourceType.JoomlaCategory;
		}
		
		if(type==3)
		{
			return ECMBannerSourceType.FPSSCategory;
		}
		if(type==4)
		{
			return ECMBannerSourceType.JoomlaBannerCategory;
		}
		
		return ECMBannerSourceType.Unknown;
		
	}

	public static int TypeToInteger(ECMBannerSourceType type)
	{
		int res = 0;
		
		if(type==ECMBannerSourceType.Unknown)
		{
			return 0;
		}
		
		if(type==ECMBannerSourceType.JoomlaSection)
		{
			return 1;
		}
		
		if(type==ECMBannerSourceType.JoomlaCategory)
		{
			return 2;
		}
		
		if(type==ECMBannerSourceType.FPSSCategory)
		{
			return 3;
		}
		if(type==ECMBannerSourceType.JoomlaBannerCategory)
		{
			return 4;
		}

		return res;
		
	}
}
