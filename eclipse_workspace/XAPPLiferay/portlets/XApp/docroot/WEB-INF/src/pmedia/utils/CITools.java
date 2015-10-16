package pmedia.utils;

import java.util.ArrayList;

import cmx.types.CIType;
import cmx.types.ConfigurableInformation;
import cmx.types.Page;

public class CITools {

	public static ArrayList<ConfigurableInformation>getAllDataSourceCI(ArrayList<Page> pages)
	{
		ArrayList<ConfigurableInformation> result = new ArrayList<ConfigurableInformation>();
		int maxp = pages.size();

		for(int i = 0  ; i  <  maxp ; i++)	
		{
			Page  p = pages.get(i) ;
			ConfigurableInformation dsCI  = p.getItemByChainAndName(0,CINames.CIContentSource);
			if(dsCI!=null)
			{				
				result.add(dsCI);
			}
			
			dsCI=null;
			dsCI  = p.getItemByChainAndName(0,CINames.CIBannerSource);
			if(dsCI!=null)
			{				
				result.add(dsCI);
			}
			
			dsCI=null;
			dsCI  = p.getItemByChainAndName(0,CINames.CIBannerSource2);
			if(dsCI!=null)
			{				
				result.add(dsCI);
			}
		}
		return result;
	}
	public static ConfigurableInformation getById(ArrayList<ConfigurableInformation>cis,String id)
	{
		for(int i = 0  ; i  <  cis.size() ; i++)	
		{
			ConfigurableInformation  ci = cis.get(i) ;
			if(ci.getId().equals(id))
			{
				return ci;
			}
		}
		
		return null;
	}
	
	public static ConfigurableInformation getByName(ArrayList<ConfigurableInformation>cis,String name)
	{
		for(int i = 0  ; i  <  cis.size() ; i++)	
		{
			ConfigurableInformation  ci = cis.get(i) ;
			if(ci.getName().equals(name))
			{
				return ci;
			}
		}
		
		return null;
	}
	
	public static int CIToInteger(CIType type)
	{
		int res = -1;
		if(type==CIType.BOOL)
		{
			return 0;
		}
		
		if(type==CIType.BOX)
		{
			return 1;
		}
		
		if(type==CIType.COLOUR)
		{
			return 2;
		}
		if(type==CIType.ENUMERATION)
		{
			return 3;
		}
		if(type==CIType.FILE)
		{
			return 4;
		}
		if(type==CIType.FLAGS)
		{
			return 5;
		}
		if(type==CIType.FLOAT)
		{
			return 6;
		}
		
		if(type==CIType.INTEGER)
		{
			return 7;
		}
		if(type==CIType.MATRIX)
		{
			return 8;
		}
		
		if(type==CIType.OBJECT)
		{
			return 9;
		}
		if(type==CIType.REFERENCE)
		{
			return 10;
		}
		
		if(type==CIType.QUATERNION)
		{
			return 11;
		}
		if(type==CIType.RECTANGLE)
		{
			return 12;
		}
		if(type==CIType.STRING)
		{
			return 13;
		}
		if(type==CIType.UNKNOWN)
		{
			return -1;
		}
		if(type==CIType.VECTOR)
		{
			return 14;
		}
		if(type==CIType.VECTOR2D)
		{
			return 15;
		}

		if(type==CIType.VECTOR4D)
		{
			return 16;
		}
		if(type==CIType.ICON)
		{
			return 17;
		}
		if(type==CIType.IMAGE)
		{
			return 18;
		}
		
		if(type==CIType.BANNER)
		{
			return 19;
		}
		
		if(type==CIType.LOGO)
		{
			return 20;
		}
		
		if(type==CIType.STRUCTURE)
		{
			return 21;
		}
		
		if(type==CIType.BANNER2)
		{
			return 22;
		}
		
		if(type==CIType.ICON_SET)
		{
			return 23;
		}
		return res;
		
	}
	
	public static CIType CIFromInteger(int type)
	{
		int res = -1;
		if(type==0)
		{
			return CIType.BOOL;
		}
		
		if(type==1)
		{
			return CIType.BOX;
		}
		
		if(type==2)
		{
			return CIType.COLOUR;
		}
		if(type==3)
		{
			return CIType.ENUMERATION;
		}
		if(type==4)
		{
			return CIType.FILE;
		}
		if(type==5)
		{
			return CIType.FLAGS;
		}
		if(type==6)
		{
			return CIType.FLOAT;
		}
		
		if(type==7)
		{
			return CIType.INTEGER;
		}
		if(type==8)
		{
			return CIType.MATRIX;
		}
		
		if(type==9)
		{
			return CIType.OBJECT;
		}
		if(type==10)
		{
			return CIType.REFERENCE;
		}
		
		if(type==11)
		{
			return CIType.QUATERNION;
		}
		if(type==12)
		{
			return CIType.RECTANGLE;
		}
		if(type==13)
		{
			return CIType.STRING;
		}
		if(type==-1)
		{
			return CIType.UNKNOWN;
		}
		if(type==14)
		{
			return CIType.VECTOR;
		}
		if(type==15)
		{
			return CIType.VECTOR2D;
		}

		if(type==16)
		{
			return CIType.VECTOR4D;
		}
		if(type==17)
		{
			return CIType.ICON;
		}
		if(type==18)
		{
			return CIType.IMAGE;
		}
		
		if(type==19)
		{
			return CIType.BANNER;
		}
		
		if(type==20)
		{
			return CIType.LOGO;
		}
		
		if(type==21)
		{
			return CIType.STRUCTURE;
		}
		
		if(type==22)
		{
			return CIType.BANNER2;
		}
		
		if(type==23)
		{
			return CIType.ICON_SET;
		}
		return CIType.UNKNOWN;
	}
}
