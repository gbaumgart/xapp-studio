package pmedia.utils;

import cmx.types.ECMContentSourceType;

public class UrlSchemaFactory {

	public static String getRefId(String url)
	{
		String splitted[] = StringUtils.toArray(url, "&");
		if(splitted!=null){
			
			for(int i = 0 ; i < splitted.length ; i++)
			{
				String part=splitted[i];
				if(part.contains("id=")){
					String subSplitted[] = StringUtils.toArray(part, "=");
					if(subSplitted.length > 1)
					{
						String subSplitted_j253[] = StringUtils.toArray(subSplitted[1], ":");
						if(subSplitted_j253!=null && subSplitted_j253.length > 1)
						{
							return subSplitted_j253[0];
						}else{
							return subSplitted[1];
						}
						//return subSplitted[1];
					}
				}
			}
		}
		
		String splitted2[] = StringUtils.toArray(url, "?");
		if(splitted2!=null){
			
			for(int i = 0 ; i < splitted2.length ; i++)
			{
				String part=splitted2[i];
				if(part.contains("p=")){
					String subSplitted[] = StringUtils.toArray(part, "=");
					if(subSplitted.length > 1)
					{
						String subSplitted_j253[] = StringUtils.toArray(subSplitted[1], ":");
						if(subSplitted_j253!=null && subSplitted_j253.length > 1)
						{
							return subSplitted_j253[0];
						}else{
							return subSplitted[1];
						}
					}
				}
			}
		}
		return null;
	}
	public static Boolean isJoomlaUrl(
			String url,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			String baseRef
			)
	{
		if(url.contains("com_content") || url.contains("com_jevlocations"))
		{
			return true;
		}
		
		return false;
	}
	public static Boolean isWordpressUrl(
			String url,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			String baseRef
			)
	{
		if(url.contains("?p="))
		{
			return true;
		}
		return false;
	}
	public static String toNativeDSUrl(
			String url, 
			String uuid, 
			String applicationId, 
			String  dsUID,
			String baseRef)
	{
		String result="";
		if(isJoomlaUrl(url, uuid, applicationId, dsUID, baseRef))
		{
			ECMContentSourceType cType = ECMContentSourceTypeTools.fromUrl(url);
			String refId = getRefId(url);
			if( refId!=null && cType!=ECMContentSourceType.Unknown)
			{
				result+=applicationId + "://dsUrl/" + dsUID + "/" + ECMContentSourceTypeTools.TypeToInteger(cType) + "/" + refId;
				return result;
			}
		}else if(isWordpressUrl(url, uuid, applicationId, dsUID, baseRef))
		{	
			ECMContentSourceType cType = ECMContentSourceTypeTools.fromUrl(url);
			String refId = getRefId(url);
			if( refId!=null && cType!=ECMContentSourceType.Unknown)
			{
				result+=applicationId + "://dsUrl/" + dsUID + "/" + ECMContentSourceTypeTools.TypeToInteger(cType) + "/" + refId;
				return result;
			}
			
		}else if(!url.contains("http"))
		{
			return baseRef  + "/" + url;
		}
		return url;
	}
	public static String toDSUrl(
			String url, 
			String uuid, 
			String applicationId, 
			String  dsUID,
			String baseRef)
	{
		String result="";
		if(isJoomlaUrl(url, uuid, applicationId, dsUID, baseRef) || isWordpressUrl(url, uuid, applicationId, dsUID, baseRef))
		{
			ECMContentSourceType cType = ECMContentSourceTypeTools.fromUrl(url);
			String refId = getRefId(url);
			if( refId!=null && cType!=ECMContentSourceType.Unknown){
				result+=""+ uuid + "/"  + applicationId + "/" + dsUID + "/" + ECMContentSourceTypeTools.TypeToInteger(cType) + "/" + refId+"/0";
				return result;
			}
			
		}
		return null;
	}
	public static String toDSUrl(
			String uuid,
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			String refId,
			String language,
			int internalIndex)
	{
		
		String result = "";
		result+= uuid + "/"  + applicationId + "/" + dsUID + "/" + ECMContentSourceTypeTools.TypeToInteger(cType) + "/" + refId;
		if(internalIndex!=-1){
			result+="/"+internalIndex;
		}
		return result;
	}
}
