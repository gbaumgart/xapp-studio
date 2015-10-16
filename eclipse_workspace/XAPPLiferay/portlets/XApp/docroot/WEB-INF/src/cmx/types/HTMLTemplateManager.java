
package cmx.types;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import cmx.tools.ResourceUtil;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import pmedia.types.CList;
import pmedia.types.CListIconItem;
import pmedia.utils.ResourceTools;
import pmedia.utils.StringUtils;

public class HTMLTemplateManager 
{

	public static ArrayList<HTMLTemplate>getTemplatesEx(
			String uuid,
			String appIdentfier,
			String platform,
			String searchPath,
			ArrayList<HTMLTemplate>dst)
		
			{
				if(platform==null){
					platform="";
				}
		
		
				if(dst==null){
					dst = new ArrayList<HTMLTemplate>();
				}
				
				
				
				//String themeRootPath=System.getProperty("HTMLTemplateStoreRoot") + platform;
				
				
				//String dummyTemplatePath = themeRootPath +"BlogCompact";
				//createJSONDummy("BlogCompact", "BlogCompact", dummyTemplatePath);
				
				File userPathFileObject = new File(searchPath);
				if(!userPathFileObject.exists() || !userPathFileObject.isDirectory()){
					return dst;
				}
				//String []entries = userPathFileObject.list();
				
				if(userPathFileObject.exists())
				{
					 File[] files = userPathFileObject.listFiles();
					 for (int i=0; i<files.length; i++) 
					 {
						 	File _file = files[i];
						 	String absDir = _file.getAbsolutePath();
					 		if(_file.isDirectory())
						 	{
					 			//File infoFile = new File(absDir+"/templateInfo.json");
					 			//if(infoFile.exists()){
				 				String []pathParts=StringUtils.toArray(absDir, "/");
				 				HTMLTemplate template = getTemplateInfoFromPath(pathParts[pathParts.length -1],absDir,platform);
				 				if(template!=null){
				 					dst.add(template);
				 				}
					 			//}
						 	}
					 }
				}
				return dst;
			
			}
	public static ArrayList<HTMLTemplate>getTemplates(
			String uuid,
			String appIdentfier,
			String platform)
		
			{
		
				if(platform==null){
					platform="";
				}
		
				ArrayList<HTMLTemplate>result = new ArrayList<HTMLTemplate>();	
				
				//system
				String themeRootPath=System.getProperty("HTMLTemplateStoreRoot") + platform;
				getTemplatesEx(uuid, appIdentfier, platform, themeRootPath,result);
				
				//user 
				String userTemplates = ResourceUtil.resolveConstantsAbsolute("%XUSER%/htmlTemplates",uuid ,appIdentfier);
				getTemplatesEx(uuid, appIdentfier, platform, userTemplates,result);
				
				//user 
				String appTemplates = ResourceUtil.resolveConstantsAbsolute("%XAPP%/htmlTemplates",uuid ,appIdentfier);
				getTemplatesEx(uuid, appIdentfier, platform, appTemplates,result);
				
				
				
				
		
				/*
				if(platform==null){
					platform="";
				}
		
		
				ArrayList<HTMLTemplate>result = new ArrayList<HTMLTemplate>();
				
				String themeRootPath=System.getProperty("HTMLTemplateStoreRoot") + platform;
				
				
				//String dummyTemplatePath = themeRootPath +"BlogCompact";
				//createJSONDummy("BlogCompact", "BlogCompact", dummyTemplatePath);
				
				File userPathFileObject = new File(themeRootPath);
				String []entries = userPathFileObject.list();
				
				if(userPathFileObject.exists())
				{
					 File[] files = userPathFileObject.listFiles();
					 for (int i=0; i<files.length; i++) 
					 {
						 	File _file = files[i];
						 	String absDir = _file.getAbsolutePath();
					 		if(_file.isDirectory())
						 	{
					 			//File infoFile = new File(absDir+"/templateInfo.json");
					 			//if(infoFile.exists()){
				 				String []pathParts=StringUtils.toArray(absDir, "/");
				 				HTMLTemplate template = getTemplateInfoFromPath(pathParts[pathParts.length -1],absDir,platform);
				 				if(template!=null){
				 					result.add(template);
				 				}
					 			//}
						 	}
					 }
				}
				return result;
				*/
				
				
				return result;
				
			}
	
	public static void createJSONDummy(String title,String description,String path)
	{
		HTMLTemplate result = new HTMLTemplate();
		
		result.setTitle(title);
		result.setDescription(description);
		if(new File(path +"/templateInfo.json").exists()){
			return;
		}
		result.setId(title);

		JSONSerializer serializer = new JSONSerializer();
		String serialized = serializer.deepSerialize(result);
		try {
			StringUtils.writeToFile(serialized, path+"/templateInfo.json");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void updatePaths(TemplateInfo info,String platform)
	{
		for (int i = 0; i < info.getPages().size(); i++) {
			TemplateInfoPage page = info.getPages().get(i);
			String fullPath=System.getProperty("WebPath")+"CMAC/themes/"+platform+"/"+info.getTitle()+"/";
			page.setWebLocationPicture(fullPath+page.getPicture());
			
		}
	}
	public static HTMLTemplate getTemplateInfoFromPath(String title,String path,String platform)
	{
		HTMLTemplate result = new HTMLTemplate();
		/*
		String description=null;
		try {
			description = StringUtils.readFileAsString(path+"/description.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(description==null || description.length()==0){
			//return null;
		}
		*/

		result.setTitle(title);
		//result.setDescription(description);
		
		String jsonPath = path+"/templateInfo.json";
		String json=null;
		File jsonFile = new File(jsonPath);
		if(!jsonFile.exists()){
			createJSONDummy(title, title, path);
		}
		JSONDeserializer<HTMLTemplate>deserializer= new JSONDeserializer();
		
		
		try {
			 json = StringUtils.readFileAsString(jsonPath);
		} catch (IOException e) {
			System.out.println("application : " + jsonPath+ " doesnt exists");
		}
		
		if(json!=null && json.length() > 0)
		{
			result=  deserializer.deserialize(json);
		}
		if(result!=null){
			
			/***
			 * listTemplate
			 */
			String listTemplate = "listTemplate.html";
			if(result.getListTemplate()!=null && result.getListTemplate().length()>0){
				listTemplate=result.getListTemplate();
			}
			String listTemplateContent = "";
			try {
				listTemplateContent = StringUtils.readFileAsString(path + "/" +listTemplate);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(listTemplateContent!=null){
				result.setListTemplate(listTemplateContent);
			}
			
			
			/***
			 * listTemplate
			 */
			String contentTemplate = "contentTemplate.html";
			if(result.getContentTemplate()!=null && result.getContentTemplate().length()>0){
				contentTemplate=result.getContentTemplate();
			}
			String contentTemplateContent = "";
			try {
				contentTemplateContent = StringUtils.readFileAsString(path + "/"+ contentTemplate);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(contentTemplateContent!=null){
				result.setContentTemplate(contentTemplateContent);
			}
		}
		
		result.setPlatform(platform);

		return result;
		
	}


	public static String getTemplateAssetRootFolder(String templateName,
			String templatePlatform)
	{
		return System.getProperty("HTMLTemplateStoreRoot") + templatePlatform + "/" + templateName  + "/files/Assets/";
	}
	public static String getTemplateStyleRootFolder(String templateName,
			String templatePlatform)
	{
		
		return System.getProperty("HTMLTemplateStoreRoot") + templatePlatform +"/" + templateName + "/files/style/" + templatePlatform+"/";
	}
	
	public static String getUserAssetRootFolder(String uuid,
			String appIdentfier)
	{
		ApplicationManager appMgr = ApplicationManager.getInstance();
		String userPath = appMgr.getStoreRootPath() + uuid + "/apps/"+appIdentfier+ "/";///Assets/";
		return userPath;
	}
	public static String getUserStyleRootFolder(String uuid,
			String appIdentfier,
			String platform)
	{
		ApplicationManager appMgr = ApplicationManager.getInstance();
		String userPath = appMgr.getStoreRootPath() + uuid + "/apps/"+appIdentfier+"/style/" + platform+ "/";
		
		return userPath;
	}
}
