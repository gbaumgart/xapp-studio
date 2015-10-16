package pmedia.DataUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import pmedia.types.JSConstants;
import pmedia.types.LocationData;
public class LocationPropertyTools 
{
	
	
	
	
	public static String getMapUrl(LocationData loc,HttpSession session)
	{
		String width = "400";
		String height = "400";
		
		if(session!=null)
		{
			String tmpW = (String)session.getAttribute("VAR_SCREENWIDTH");
			String tmpH = (String)session.getAttribute("VAR_SCREENHEIGHT");
			if(tmpW!=null && tmpH!=null)
			{
				width = tmpW;
				height = tmpH;
			}
			
		}
		String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + 
		loc.latitude + "," + loc.longtitude +" &zoom=13&markers=" + loc.latitude + "," +  loc.longtitude + "&maptype=hybrid&size=" + width +"x" + height+"&sensor=false";
		return mapUrl;
	}
	public static String getVideoLink(LocationData loc,HttpSession session)
	{
		
		String testString  = loc.video;
		String result = "";
		Boolean isVimeo = false;
		//vimeo id !
		if(!testString.contains(".") && !testString.contains("/"))
		{
			//change to file name first :
			testString = testString + "-sd.m4v";
			isVimeo = true;
		}
		
		//check local cache directory :
		String localDirectory = System.getProperty("webapp.root") + "/cache/files/site/videos/";
		File videoLocal  = new File(localDirectory + testString);
		if(videoLocal.exists())
		{
			return localDirectory + testString; 
		}
		//
		if(isVimeo)
		{
			return System.getProperty("ibiza.locationFiles") + "files/site/videos/" + testString;
		}
		
		
		//String UrlBase="http://www.facebook.com/sharer/sharer.php?t=";
		result = pmedia.AssetTools.AssetTools.resolvePath(testString, true, null);
		
		
		/*shareUrlBase+= loc.title+"&u=";
		
		if(session.getAttribute("isFacebookApp")!=null)
		{
			shareUrlBase +=System.getProperty("ibiza.facebookAppUrl") + "/facebookApp.jsp?loc=";
		}else
		{
			shareUrlBase += (String)session.getAttribute("baseUrl") + "/content/demos/mobileGallery/facebookApp.jsp?loc="; 
		}
		
		shareUrlBase += "" +loc.location_id;
		String tab=(String)session.getAttribute(JSConstants.VAR_LAST_LOC_TAB_TYPE);
		if(tab!=null)
		{
			shareUrlBase+="&tab=" +tab;
		}
		*/
		return result;
	}
	public static String getFacebookShareLink(LocationData loc,HttpSession session)
	{
		String shareUrlBase="http://www.facebook.com/sharer/sharer.php?t=";
		shareUrlBase+= loc.title+"&u=";
		
		if(session.getAttribute("isFacebookApp")!=null)
		{
			shareUrlBase +=System.getProperty("ibiza.facebookAppUrl") + "/facebookApp.jsp?loc=";
		}else
		{
			shareUrlBase += (String)session.getAttribute("baseUrl") + "/content/demos/mobileGallery/facebookApp.jsp?loc="; 
		}
		
		shareUrlBase += "" +loc.location_id;
		String tab=(String)session.getAttribute(JSConstants.VAR_LAST_LOC_TAB_TYPE);
		if(tab!=null)
		{
			shareUrlBase+="&tab=" +tab;
		}
		return shareUrlBase;
	}
	public static String getEMailShareLink(LocationData loc,HttpSession session,Boolean searchFlickr,Boolean addShadow)
	{
		String result = null;
		result = "mailto:?";//subject=SweetWords
		result+="subject=" + "Location Details - Ibiza Media: " + loc.title;
		result+="&body=";
		
		//http://apps.facebook.com/ibizamediatest/facebookApp.jsp?loc=357
		String bs="Name:" + loc.title + "\n";
		bs+="Web-Link:" + session.getAttribute("baseUrl") + "/content/demos/mobileGallery/facebookApp.jsp?loc=" + loc.location_id + "\n";
		
		String tab=(String)session.getAttribute(JSConstants.VAR_LAST_LOC_TAB_TYPE);
		if(tab!=null)
		{
			bs+="&tab=" +tab;
		}
		
		 
		String bsEncoded = "";
		 try 
		 {
			bsEncoded = URLEncoder.encode(bs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result+=bsEncoded;
		//result = "<a href=\"mailto:?";//subject=SweetWords
		//mailto:?subject=asdfasdf
		
		//&body=";//</a>
		
		return result ;
	}
	
	/*
	public static String getGalleryUrl(LocationData loc,HttpSession session,Boolean searchFlickr)
	{
		String result = null;
		Boolean useFlash= 
			!session.getAttribute("UserAgent").equals(Constants.USERAGENT_IPHONE) &&  
			!session.getAttribute("UserAgent").equals(Constants.USERAGENT_TABLET) &&
			!session.getAttribute("UserAgent").equals(Constants.USERAGENT_ANDROID);
		
		if(loc.photoListFlickr==null && searchFlickr)
		{
			LocationTools.searchFlickr(loc);
		}
		//useFlash=false;
		if( (loc.getPictures()!=null &&  loc.getPictures().size() > 1) || 
				(loc.photoListFlickr!=null && loc.photoListFlickr.size() > 0))
		{
			if(!useFlash)
			{
				String folderName = StringUtils.replace(loc.title," ","") + "Gallery";
				result=System.getProperty("galleryPathHTML") + "locations/" + folderName + "/";
			}else
			{
				String folderName = StringUtils.replace(loc.title," ","") + "Gallery";
				result=System.getProperty("galleryPath") + "locations/" + folderName + "/";
			}
		}
		return result;
	}
	*/
}
