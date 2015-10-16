package cmx.types;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import pmedia.utils.ResourceTools;
import pmedia.utils.StringUtils;

public class ShowcaseManager 
{
	
	public static String getTemplateAssetRootFolder(String templateName,
			String templatePlatform)
	{
		return System.getProperty("TemplateStoreRoot") + templatePlatform + "/" + templateName  + "/files/Assets/";
	}
	public static String getTemplateStyleRootFolder(String templateName,
			String templatePlatform)
	{
		
		return System.getProperty("TemplateStoreRoot") + templatePlatform +"/" + templateName + "/files/style/" + templatePlatform+"/";
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
	
	public static Boolean adjustAppBackground(String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode,
			String filePath)
	{
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid, appIdentfier, false);
		if(app==null){
			return true;
		}
		
		Page homePage = app.getElementById("0");
		if(homePage!=null)
		{
			ConfigurableInformation bg = homePage.getItemByChainAndName(0, "BACKGROUND");
			if(bg!=null){
				bg.setValue(filePath);
				appManager.saveApplication(app);
			}
		}
		
		return true;
	}
	public static Boolean adjustCSSFileToApp(String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode,
			String filePath)
	{
		
		String cssFileContent =null;
		
		try {
			cssFileContent =StringUtils.readFileAsString(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		
		
		/***
		 * The original content should contain : "/themes/IPHONE_NATIVE/Elegant/files/"
		 */
		String oriPathPrefix = "themes/"+templatePlatform+"/"+templateTitle+"/files/";
		
		/***
		 * The destination path should start with this : /ibizamedia5/Assets/
		 */
		String newPathPrefix = appIdentfier +"/";
		
		cssFileContent=cssFileContent.replace("url('/" + oriPathPrefix, "url('/" +newPathPrefix);
		
		try {
			StringUtils.writeToFile(cssFileContent, filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	public static Boolean adjustTemplateToApp(String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode)
	{
		Boolean result =true;
		
		/***
		 * Adjust Urls in css files
		 */
		
		String listItemsPathStr = getUserStyleRootFolder(uuid, appIdentfier, platform) + "ListItems.css";
		String contentPathStr = getUserStyleRootFolder(uuid, appIdentfier, platform) + "Content.css";
		String customFieldsPathStr = getUserStyleRootFolder(uuid, appIdentfier, platform) + "CustomFields.css";
		String launchItemsPathStr = getUserStyleRootFolder(uuid, appIdentfier, platform) + "LaunchItems.css";
		
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, listItemsPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, contentPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, customFieldsPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, launchItemsPathStr);
		return result;
	}
	
	public static Boolean copyTemplateToApp(String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode)
	{
		Boolean result =true;
		
		
		/////////////////////////////////////
		//copy assets 
		String srcPathStr = getTemplateAssetRootFolder(templateTitle,templatePlatform);
		File srcPath = new File(srcPathStr);
		if(!srcPath.exists()){
			System.out.println("template source asset folder : " + srcPathStr + " doesn't exists");
			return false;
		}
		
		String dstPathStr = getUserAssetRootFolder(uuid,appIdentfier) + "/Assets/";
		File dstPath = new File(dstPathStr);
		if(!dstPath.exists()){
			System.out.println("user asset folder : " + dstPathStr + " doesn't exists");
			dstPath.mkdirs();
		}
		
		try {
			System.out.println("copying template source asset folder : " + srcPathStr + " to : " + dstPathStr);
			//FileUtils.copyDirectory(srcPath, dstPath);
			ResourceTools.copyDirectory(srcPath, dstPath);
		} catch (IOException e) 
		{
			result=false;
			e.printStackTrace();
		}
		
		
		/****
		 * Now copy the style files
		 */
		srcPathStr = getTemplateStyleRootFolder(templateTitle,templatePlatform);
		dstPathStr = getUserStyleRootFolder(uuid, appIdentfier, platform);
		srcPath = new File(srcPathStr);
		if(!srcPath.exists()){
			System.out.println("template source style folder : " + srcPathStr + " doesn't exists");
			return false;
		}
		
		dstPath = new File(dstPathStr);
		if(!dstPath.exists()){
			System.out.println("user style folder : " + dstPathStr + " doesn't exists");
			dstPath.mkdirs();
		}
		
		try {
			System.out.println("copying style folder : " + srcPathStr + " to : " + dstPathStr);
			//FileUtils.copyDirectory(srcPath, dstPath);
			ResourceTools.copyDirectory(srcPath, dstPath);
		} catch (IOException e) 
		{
			result=false;
			e.printStackTrace();
		}
		
		return result;
	}
	public static Boolean prepareTemplateChange(String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode)
	{
		Boolean result =true;
		
		return result;
	}
	
	public static DBConnectionError applyTemplate(
			String uuid,
			String appIdentfier,
			String platform, 
			String templateTitle,
			String templatePlatform,
			int mode)
	{
		DBConnectionError e= new DBConnectionError();
		
		System.out.println("Applying Template");
		
		if(prepareTemplateChange(uuid, appIdentfier, platform, templateTitle, templatePlatform,mode))
		{
			/***
			 * 1. Backup Old Template ?
			 */
			
			/***
			 * 1. Copy files over ?
			 */
			if(!copyTemplateToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform,mode))
			{
				e.type=1;
				e.msg="Couldn't copy template";	
				return e;
			}
			
			
			/***
			 * 1. Copy files over ?
			 */
			if(!adjustTemplateToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform,mode))
			{
				e.type=1;
				e.msg="Couldn't adjust template";
				return e;
			}
			
			/***
			 * 1. Copy files over ?
			 */
			String appBackground = "/" + appIdentfier +"/Assets/backgrounds/defaultBG@2x.png";
			if(!adjustAppBackground(uuid, appIdentfier, platform, templateTitle, templatePlatform,mode,appBackground))
			{
				e.type=1;
				e.msg="Couldn't adjust template";
				return e;
			}
			
		}
		//ArrayList<TemplateInfo>templates
		return e;
		//return StyleTreeFactory.createStyleTree(uuid, appIdentfier, platform, pageId, dataSourceUID, refId);
	
	}
	
	public static ShowcaseInfo getShowcase(String showcaseId,
			String uuid,
			String appIdentfier,
			String platform,
			String lang)
			{
				//ShowcaseInfo result = new ArrayList<TemplateInfo>();
				ShowcaseInfo result = new ShowcaseInfo();
				String showcasePath=System.getProperty("ShowcaseStoreRoot") + showcaseId;
				result = getShowcaseInfoFromPath(showcaseId,showcasePath, platform,lang);
				
				
				return result;
			}
	
	
	

	public static void createJSONDummy(String title,String description,String path)
	{
		ShowcaseInfo result = new ShowcaseInfo();
		result.setUserId("11166763-e89c-44ba-aba7-4e9f4fdf97a9");
		result.setAppId("webapp");
		
		result.setTitle(title);
		result.setDescription(description);
		
		ShowcaseInfoPage page = new ShowcaseInfoPage();
		page.title="Launch";
		page.picture="launch.png";
		page.description="This is how the main navigation looks like";
		result.getPages().add(page);
		
		
		ShowcaseInfoPage page1 = new ShowcaseInfoPage();
		page1.title="ListItens";
		page1.picture="lists.png";
		page1.description="This is how lists will look like";
		result.getPages().add(page1);
		
		
		ShowcaseInfoPage page2 = new ShowcaseInfoPage();
		page2.title="Content";
		page2.picture="content.png";
		page2.description="This is how content will look like";
		result.getPages().add(page2);
		
		ShowcaseInfoPage page3 = new ShowcaseInfoPage();
		page3.title="Custom ";
		page3.picture="custom.png";
		page3.description="This is how custom fields will look like";
		result.getPages().add(page3);
		
		
		JSONSerializer serializer = new JSONSerializer();
		String serialized = serializer.deepSerialize(result);
		try {
			StringUtils.writeToFile(serialized, path+"/templateInfo.json");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void updatePaths(ShowcaseInfo info,String showCaseId,String platform,String lang)
	{
		for (int i = 0; i < info.getPages().size(); i++) {
			ShowcaseInfoPage page = info.getPages().get(i);
			
			String fullPath=System.getProperty("WebPath")+"CMAC/showcase/"+showCaseId+"/";
			
			String pageDescription = null;
			try {
				String pathPageDescr =  System.getProperty("AppStoreRoot") +"showcase/" + showCaseId+"/" +lang +"/"+ "page_" + i  + ".html";
				if(new File(pathPageDescr).exists())
				{
					pageDescription = StringUtils.readFileAsString(pathPageDescr);
					page.setDescription(pageDescription);
				}else{
					pathPageDescr =  System.getProperty("AppStoreRoot") +"showcase/" + showCaseId+"/" +"page_" + i  + ".html";
					if(new File(pathPageDescr).exists())
					{
						pageDescription = StringUtils.readFileAsString(pathPageDescr);
						page.setDescription(pageDescription);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			page.setWebLocationPicture(fullPath+page.getPicture());
			
		}
	}
	public static void updateEdgePages(ShowcaseInfo info,String showCaseId,String platform,String lang)
	{
		for (int i = 0; i < info.getPages().size(); i++) {
			ShowcaseInfoPage page = info.getPages().get(i);
			
			String fullPath=System.getProperty("WebPath")+"CMAC/showcase/"+showCaseId+"/"+ "edge_" + i  + "/index.html";
			String edgeFile = System.getProperty("AppStoreRoot") +"showcase/" + showCaseId+"/" + "edge_" + i  + "/index.html";
			if(new File(edgeFile).exists())
			{
				page.setEdgeFile(fullPath);
			}else{
				page.setEdgeFile(null);
			}
		}
	}
	
	public static ShowcaseInfo getShowcaseInfoFromPath(String showcaseId,String path,String platform,String lang)
	{
		ShowcaseInfo result = new ShowcaseInfo();
		
		String description=null;
		try {
			description = StringUtils.readFileAsString(path+"/description.html");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if(description==null || description.length()==0){
			return null;
		}
		result.setTitle(showcaseId);
		result.setDescription(description);
		String jsonPath = path+"/showcaseInfo.json";
		String json=null;
		File jsonFile = new File(jsonPath);
		if(!jsonFile.exists()){
			createJSONDummy(showcaseId, description, path);
		}
		

		
		
		JSONDeserializer<ShowcaseInfo>deserializer= new JSONDeserializer();
		try {
			 json = StringUtils.readFileAsString(jsonPath);
		} catch (IOException e) 
		{
			System.out.println("application : " + jsonPath+ " doesnt exists");
		}
		
		if(json!=null && json.length() > 0)
		{
			result=  deserializer.deserialize(json);
			/*
			result.setApps(new ArrayList<ShowcaseApps>());
			
			ShowcaseApps app = new ShowcaseApps();
			app.appId="appID";
			app.uuid="uuid";
			result.getApps().add(app);
			*/
		}
		
		JSONSerializer ser = new JSONSerializer();
		String serStr = ser.deepSerialize(result);
		
		/*
		try {
			StringUtils.writeToFile(serStr,jsonPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/*
		JSONSerializer serializer = new JSONSerializer();
		String serialized = serializer.deepSerialize(result);
		try {
			StringUtils.writeToFile(serialized,jsonPath);
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		result.setPlatform(platform);
		
		updatePaths(result,showcaseId, platform,lang);
		updateEdgePages(result, showcaseId, platform,lang);
		
		
		
		return result;
	}
}
