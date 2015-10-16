package pmedia.html.types;

import java.util.regex.PatternSyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pmedia.html.factory.WhiteListDataFactory;
import pmedia.html.tools.HTMLConverter;
import pmedia.types.Constants;
import pmedia.utils.UrlSchemaFactory;
import cmx.types.ECMContentSourceType;

public class ClientFilter {
	
	public static String filter(String input,
			String uuid, 
			String applicationId , 
			String  dsUID,
			String baseRef,
			ECMContentSourceType cType,
			String refId,
			WhiteListData whiteList,
			String platform,
			String language,
			int maxSize)
	{
		
		if(platform==null){
			platform = pmedia.types.Constants.USERAGENT_IPHONE;
		}
		
		if(whiteList==null)
		{
			if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE))
			{
				whiteList=WhiteListDataFactory.mobileBase(applicationId);
			}
			
			if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE_NATIVE))
			{
				whiteList=WhiteListDataFactory.iphoneNativeBase(applicationId);
			}
			
		}
		
		if(input !=null){
			input=input.replace(applicationId+"://", "http://"+applicationId+"://");
		}
		
		String result = null;
		if(input!=null && input.length() > 0){
		
			result =baseFilter(input, uuid, applicationId, dsUID, baseRef,cType,refId, whiteList, platform, language, maxSize);
			result = linkFilter(result, uuid, applicationId, dsUID,baseRef,cType,refId, whiteList, platform, language, maxSize);
			result = imageFilter(result, uuid, applicationId, dsUID,baseRef, cType,refId, whiteList, platform, language, maxSize);
		}else{
			result = "";
		}
		
		if(result!=null){
			result=result.replace("http://"+applicationId+"://", applicationId+"://");
		}
		
		return result;
	}
	
	public static String baseFilter(
	String input,
	String uuid, 
	String applicationId , 
	String  dsUID, 
	String baseRef,
	ECMContentSourceType cType,
	String refId,
	WhiteListData whiteList,
	String platform,
	String language,
	int maxSize)
	{
		
		String result = HTMLConverter.convert(
	      			input, 
	      			uuid , 
	      			applicationId, 
	      			dsUID, 
	      			cType, 
	      			whiteList, 
	      			platform, 0);
		
		
		try {
			result = result.replaceAll("(?s)\\{.*?\\}", "");
		} catch (PatternSyntaxException ex) 
		{
			
		} catch (IllegalArgumentException ex) {
			
		} catch (IndexOutOfBoundsException ex) {
		}
		
		try {
			result = result.replaceAll("(?s)\\[.*?\\]", "");
		} catch (PatternSyntaxException ex) 
		{
			
		} catch (IllegalArgumentException ex) {
			
		} catch (IndexOutOfBoundsException ex) {
		}
		
		return result;
	}
	public static String replaceImageSources(
			String input,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			String baseRef,
			ECMContentSourceType cType,
			String refId,
			String platform,
			String language,
			int maxSize
			)
    {
    	if(input==null || input.length()==0)
			return null;
    	
		Document doc = Jsoup.parse(input);
		Elements pngs = doc.select("img");
		int pIndex=0;
		
		Boolean multiplePictures = pngs !=null ? pngs.size() > 0 ? true : false : false; 
		for (Element img : pngs)
		{
		        String url = img.attr("src");
		        {
		        	String prefix="";
		        	if(!url.contains("http")){
		        		if(baseRef!=null){
		        			prefix=baseRef;
		        		}
		        	}
		        	if(prefix.length()!=0)
		        	{
		        		url=prefix+"/"+url;
		        	}
		        	img.attr("src", url);
		        	img.attr("class", "PictureItems");
		        	String width = img.attr("width");
		        	
		        	String onClickUrl=null ;
		        	if(!multiplePictures){
		        		onClickUrl="ctx.getUrlHandler().openUrl('" +"tt://pictureView/" + url + "\',null);";
		        	}else{
		        		String galleryUrl=UrlSchemaFactory.toDSUrl(uuid, applicationId, dsUID, cType, refId, language,pIndex);
		        		onClickUrl="ctx.getUrlHandler().openUrl('" +"tt://pictureGallery/" + galleryUrl + "\',null);";
		        	}
		        	
		        	img.attr("onClick", onClickUrl);
		        	img.attr("width","97%");
		        	img.removeAttr("height");
		        	pIndex++;
		        	
		        	if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE_NATIVE))
		        	{
			        	//org.jsoup.nodes.Element newLink= img.before("<a href=\"http://www.google.com\">");
			        	//img.nextSibling().after("</a>");
		        	}
		        	//url = url.replace("http://","tt://");
		        	
//		        	org.jsoup.nodes.Element newLink= link.before("<div class=\"articleLink\" " +
	//	        			"onClick=\"pm.urlHandler.openInternalUrl(\'" + url + "\',null);\" >"  +link.html() + "</div>");
		        }
		}
		/*
		Elements links = doc.select("a");
		for (org.jsoup.nodes.Element link : links)
		{
		        String url = link.attr("href");
		        {
		        	url = url.replace("http://","tt://");
		        	org.jsoup.nodes.Element newLink= link.before("<div class=\"articleLink\" " +
		        			"onClick=\"pm.urlHandler.openInternalUrl(\'" + url + "\',null);\" >"  +link.html() + "</div>");
		        }
		 }*/
		
		//pngs.remove();
		return doc.body().html();
    }
	public static String imageFilter(
			String input,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			String baseRef,
			ECMContentSourceType cType,
			String refId,
			WhiteListData whiteList,
			String platform,
			String language,
			int maxSize)
			{
				//StringUtils.convertLinks(input);
				String result = replaceImageSources(input, uuid, applicationId, dsUID, baseRef, cType,refId, platform, language, maxSize);
				return result;
			}
	
	
	public static Boolean isExternalLink(String url, String uuid, String applicationId, String baseRef)
	{
		if(url.contains("http"))
		{
			if(url.contains(baseRef)){
				return false;
			}else{
				return true;
			}
		}else{
			
		}
		return false;
	}
	
	
	public static String linkFilter(
			String input,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			String baseRef,
			ECMContentSourceType cType,
			String refId,
			WhiteListData whiteList,
			String platform,
			String language,
			int maxSize)
			{
				String result = input;
				Document doc = null;
				
				try {
					doc = Jsoup.parse(input);	
				} catch (Exception e) {
					return input;
				}
				Elements links = doc.select("a");
				for (org.jsoup.nodes.Element link : links)
				{
					Boolean deleteA=true;
			        String url = link.attr("href");
			        if(url!=null && url.length() > 0)
			        {
			        	
			        	Elements innerElements = link.getElementsByTag("img");
			        	if(innerElements.size()>0 && innerElements.size()==1)
			        	{
			        		org.jsoup.nodes.Element imgElement=innerElements.get(0);
			        		if(imgElement.attr("src").equals(url)){
			        			deleteA=false;
			        			link.attr("href", "");
			        			link.tagName("div");
			        			//String imgLink=innerElements.get(0);
			        		}
			        	}
			        	
			        	if(isExternalLink(url, uuid, applicationId, baseRef))
			        	{
			        		
			        		org.jsoup.nodes.Element newLink= link.before("<span class=\"articleLink\" " +
			        					"onClick=\"ctx.getUrlHandler().openExternalLocation(\'" + url + "\',null);\" >"  +link.html() + "</span>");
			        	}else
			        	{
			        		String newUrl=UrlSchemaFactory.toDSUrl(url, uuid, applicationId,dsUID, baseRef);
			        		if(platform.equals(Constants.USERAGENT_IPHONE))
			        		{
			        			
				        		if(newUrl!=null)
				        		{
				        			org.jsoup.nodes.Element newLink= link.before("<span class=\"articleLink\" " +
				        					"onClick=\"ctx.getUrlHandler().openDSUrl(\'" + newUrl + "\',null);\" >"  +link.html() + "</span>");
				        		
				        		}
				        		
			        		}
			        		if(platform.equals(Constants.USERAGENT_IPHONE4_NATIVE))
			        		{
			        			deleteA=false;
			        			link.attr("href",UrlSchemaFactory.toNativeDSUrl(url, uuid, applicationId, dsUID, baseRef));
			        		}
			        	}
			        	
			        	
			        	if(deleteA)
			        	{
			        		link.remove();
			        	}
			        }
				 }
				//links.remove();
				return doc.body().html();
			}

}
