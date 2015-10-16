package pmedia.AssetTools;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import pmedia.types.PMPlatformRenderConfiguration;
import pmedia.utils.SettingsUtil;
import pmedia.utils.StringUtils;

public class AssetTools {

	public static String makeIconPath(String input,String platform)
	{
		String result = input; 
		
		try {
			result =StringUtils.path(input) +"/"+platform + "/"  + StringUtils.filename(input) + "Icon" + "." + StringUtils.extension(input);	
		} catch (Exception e) 
		{
			System.out.println("can not create icon name : " + input);
			return null;
		}
		return result;
	}

	public static String getIconRemoteUrl(String input,PMPlatformRenderConfiguration rConfig)
	{
	
		String result = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  input + "&height="+rConfig.listItemIconHeight;
		result+="&shadow=true";
		return result; 
	}

	public static String makeIconName(String input)
	{
		String result = input; 
		
		try {
			result =StringUtils.path(input) + StringUtils.filename(input) + "Icon" + "." + StringUtils.extension(input);	
		} catch (Exception e) 
		{
			System.out.println("can not create icon name : " + input);
			return null;
		}
		return result;
	}

	public static String resolveIconPath(String input,String platform,Boolean toWeb,HttpSession session)
	{
		if(input.contains("http"))
		{
			return AssetTools.resolvePath(input, toWeb, session);
		}
		String iconLocalFile = makeIconPath(input,platform);
		return AssetTools.resolvePath(iconLocalFile, toWeb, session);
	}

	public static String resolvePath(String input,Boolean toWeb,HttpSession session)
	{
		String result = "";
		
		if(input==null || input.length() ==0 )
			return null;
		
		if(input.contains("http"))
		{
			return input;
		}
		
		File testFile = new File(input);
		if(testFile.exists())
			return input;
		
		//search with in local disc :
		String prefix = System.getProperty("webapp.root");
		String webPrefix  = System.getProperty("contextName");
		testFile = new File(prefix+input);
		if(testFile.exists())
		{
			if(!toWeb){
				
			return input;
			
		}else
			{
				
			}
			
		}
		
		//search with in web apps local cache directory :
		prefix =  prefix + "cache/";
		testFile = new File(prefix+input);
		if(testFile.exists())
		{
			if(!toWeb){
				return testFile.getAbsolutePath();
			}else
			{
				return "cache/" + input;
			}
		}
		
		
		return SettingsUtil.getProperty("ibiza.masterLocation") + input;
		
		
		//return result;
	}

	public static boolean hasGallery(String title)
	{
		ArrayList<String>result = new ArrayList<String>();
		String baseUrl=System.getProperty("baseUrl");
		
		//System.getProperty("isSlave")
		Boolean isSlave = System.getProperty("isSlave").equals("true") ? true : false;
		String rootUrl=System.getProperty("webapp.root") + "cache/files/locations/";
		if(!isSlave)
		{
			rootUrl=System.getProperty("galleryPathLocal");
		}
		
		String galleryPath=rootUrl+title.replace(" ","") + "Gallery/";
		
		File dir = new File(galleryPath);
		int maxPerPage=20;
		
		
		if(dir.exists())
		{
			String[] children = dir.list();
			File[] files = dir.listFiles();
			if(files !=null && files.length > 1)
				return true;
			
		}
		return false;
	}

}
