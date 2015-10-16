package cmx.types;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import pmedia.types.CList;
import pmedia.types.CListIconItem;
import pmedia.utils.ResourceTools;
import pmedia.utils.SimpleImageInfo;
import pmedia.utils.StringUtils;

public class TemplateManager 
{
	public static CList getAsset(String uuid,String appId,String identifier)
	{
		CList result = new CList();
		result.setItems(new ArrayList<CListIconItem>());
		
		String prefix = "/SharedAssets/" +identifier+"/";
		
		String themeRootPath=System.getProperty("AppStoreRoot") + "/shared/SharedAssets/" + identifier;
		
		if(identifier.equals("MyUploads")){
			themeRootPath = ApplicationManager.getInstance().getUserAppPath(uuid, appId) +"Assets/QXAppUploads/";
			prefix="/" + appId+ "/Assets/QXAppUploads/";
		}
		
		File userPathFileObject = new File(themeRootPath);
		if(userPathFileObject.exists() && userPathFileObject.isDirectory())
		{
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	
				 	
				 	String fileName= StringUtils.filenameComplete(_file.getAbsolutePath());
				 	if( (fileName.contains(".png") || fileName.contains(".jpg")) && 
				 			!fileName.contains("preview") && !fileName.contains("Thumb"))
				 	{
				 		CListIconItem icon = new CListIconItem();
					 	icon.thumb = prefix + fileName;
					 	icon.title="";
					 	
					 	icon.large = prefix + fileName;
					 	
					 	String filePathAbs = _file.getAbsolutePath();
					 	if(new File(filePathAbs).exists()){
					 		SimpleImageInfo imageInfo=null;
							try {
								imageInfo = new SimpleImageInfo(new File(filePathAbs));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(imageInfo!=null){
								icon.width=imageInfo.getWidth();
								icon.height=imageInfo.getHeight();
								icon.dimension = imageInfo.toString();
								
							}
					 	}
					 	
					 	result.getItems().add(icon);
				 	}
			 }
		}
		return result;
	}
	
	public static CList getIconSet(String uuid,String appId,String identifier)
	{
		CList result = new CList();
		result.setItems(new ArrayList<CListIconItem>());
		
		String prefix = "/SharedAssets/iconSets/" +identifier+"/";
		String themeRootPath=System.getProperty("AppStoreRoot") + "/shared/SharedAssets/iconSets/" + identifier;
		
		if(identifier.equals("MyUploads")){
			themeRootPath = ApplicationManager.getInstance().getUserAppPath(uuid, appId) +"Assets/QXAppUploads/";
			prefix="/" + appId+ "/Assets/QXAppUploads/";
		}
		
		File userPathFileObject = new File(themeRootPath);
		if(userPathFileObject.exists() && userPathFileObject.isDirectory())
		{
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	
				 	if(_file.isDirectory()){
				 		continue;
				 	}
				 	
				 	String fileName= StringUtils.filenameComplete(_file.getAbsolutePath());
				 	//String fileNameLower = StringUtils.filenameComplete(_file.getAbsolutePath());
				 	//fileNameLower = fileNameLower.toLowerCase();
				 	
				 	
				 	if(fileName !=null && fileName.length()>0)
				 	{
				 		if( (fileName.contains(".png") || fileName.contains(".jpg")) && 
					 			!fileName.contains("preview") && !fileName.contains("Thumb"))
					 	{
					 		CListIconItem icon = new CListIconItem();
						 	icon.thumb = prefix + fileName;
						 	icon.title="";
						 	icon.large = prefix + fileName;
						 	result.getItems().add(icon);
						 	String filePathAbs = _file.getAbsolutePath();
						 	if(new File(filePathAbs).exists()){
						 		SimpleImageInfo imageInfo=null;
								try {
									imageInfo = new SimpleImageInfo(new File(filePathAbs));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(imageInfo!=null){
									icon.width=imageInfo.getWidth();
									icon.height=imageInfo.getHeight();
									icon.dimension = imageInfo.toString();
									
								}
						 	}
					 	}
				 	}
			 }
		}
		return result;
	}
	
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
		 * uuid	"68f147c9-1625-4e29-9f84-66f2cef2cc53" (id=209)	
appIdentfier	"qxevents8FC3F245EB8D7BED5A546EE4B76454DE" (id=231)	
platform	"IPHONE_NATIVE" (id=232)	
templateTitle	"Dark" (id=233)	
templatePlatform	"IPHONE_NATIVE" (id=232)	
mode	0	
filePath	"/Applications/MAMP/htdocs/CMAC/68f147c9-1625-4e29-9f84-66f2cef2cc53/apps/qxevents8FC3F245EB8D7BED5A546EE4B76454DE/style/IPHONE_NATIVE/ListItems.css" (id=236)	
cssFileContent	"@variables dummy3 {\n  dummy: 0px;\n}\n.itemBackground {\n  background-image: url('/nativedarktemplate/Assets/backgrounds/cells/locationCellBGBuzz.png');\n  background-color: rgba(0,0,0,0);\n  background-size: 100% 100%;\n  opacity: 1.0;\n  padding-left: 0px;\n  padding-right: 0;\n  padding-top: 0px;\n  padding-bottom: 0px;\n  border: 0px solid rgba(5,0,196,0.29);\n  border-radius: 0px;\n  border-color: rgba(104,89,89,0.406);\n  box-shadow: 0px 0px 0pt rgba(0,0,0,0.493);\n  border-width: 1px;\n  overflow: hidden;\n}\n.item {\n  height: 80px !important;\n}\n.itemArrow {\n}\n.itemIcon {\n  background-image: url('/nativedarktemplate/Assets/iconsShared/playButton@2x.png');\n  background-color: rgba(0,0,0,0.0);\n  float: left;\n  width: 80px !important;\n  height: 80px !important;\n  padding-left: 0;\n  padding-right: 0;\n  padding-top: 0;\n  padding-bottom: 0;\n  margin-top: 0px;\n  margin-bottom: 0;\n  margin-left: 0px;\n  margin-right: 0px;\n  border: 0px solid rgba(5,0,196,0.29);\n  border-width: 1px;\n  border-radius: 0px;\n  border-color: rgba(0,0,0,0.667);\n  box-shadow: rgba(0,0,0,0.652) 2px 0px 4px;\n  opacity: 1.0;\n}\n.itemTitle {\n  font-family: Helvetica;\n  font-size: 15px;\n  font-style: normal;\n  -ios-number-of-lines: 2;\n  color: rgb(255, 255, 255);\n  text-align: left;\n  padding-left: 0;\n  padding-right: 0;\n  padding-top: 0;\n  padding-bottom: 0;\n  margin-top: 0;\n  margin-bottom: 0;\n  margin-left: 0px;\n  margin-right: 0;\n  white-space: pre-wrap !important;\n  text-shadow: 0px 0px 0pt rgba(0,0,0,1);\n}\n.itemDetail {\n  font-family: Arial;\n  font-size: 14px;\n  font-style: normal;\n  -ios-number-of-lines: 2;\n  color: #1c3b69;\n  text-align: left;\n  padding-left: 0;\n  padding-right: 18;\n  padding-top: 0;\n  padding-bottom: 1;\n  margin-top: -24;\n  margin-bottom: 0;\n  margin-left: 12;\n  margin-right: 0;\n  text-shadow: 0px 0px 0pt rgba(0,0,0,0.3);\n}\n.itemCaption {\n  font-family: Arial;\n  font-size: 13px;\n  font-style: normal;\n  -ios-number-of-lines: 1;\n  color: rgb(133, 143, 156);\n  text-align: left;\n  width: 0;\n  padding-left: 0px;\n  padding-right: 0;\n  padding-top: 0;\n  padding-bottom: 0;\n  margin-top: 0;\n  margin-bottom: 0;\n  margin-left: 0px;\n  margin-right: 0;\n  white-space: pre-wrap !important;\n  text-shadow: 0px 0px 0pt rgba(0,0,0,1);\n}\n.itemSmallTextLeft {\n  font-size: 12;\n  font-style: normal;\n  -ios-number-of-lines: 1;\n  color: black;\n  text-align: left;\n}\n.itemSmallTextRight {\n  font-size: 11;\n  font-weight: normal;\n  -ios-number-of-lines: 1;\n  color: black;\n  text-align: right;\n}\n.itemFooterTextLeft {\n  font-family: Verdana;\n  font-size: 11px;\n  font-style: normal;\n  -ios-number-of-lines: 1;\n  color: rgb(252, 252, 252);\n  text-align: left;\n  padding-left: 0;\n  padding-right: 0;\n  padding-top: 0;\n  padding-bottom: 0;\n  margin-top: 0px;\n  margin-bottom: 7;\n  margin-left: 0px;\n  margin-right: 0;\n  text-shadow: rgba(0,0,0,0.315) 1px 1px 0px;\n}\n.itemFooterTextRight {\n  font-family: Arial;\n  font-size: 12px;\n  font-style: normal;\n  -ios-number-of-lines: 1;\n  color: rgb(255, 255, 255);\n  text-align: right;\n  padding-left: 0;\n  padding-right: 0;\n  padding-top: 0;\n  padding-bottom: 0;\n  margin-top: 0;\n  margin-bottom: 7;\n  margin-left: -7;\n  margin-right: 0px;\n  text-shadow: 0px 0px 0pt rgba(0,0,0,0.261);\n}\n.cellMain {\n}\n.cellFooter {\n  vertical-align: baseline;\n  bottom: 0;\n}\n.listItemSelected {\n  color: white;\n}\n.listItemSelected .mblDomButton div {\n  border-color: white;\n}\n.listItemLabelSelected {\n  background-color: #048bf4;\n}\n" (id=237)	
oriPathPrefix	"themes/IPHONE_NATIVE/Dark/files/" (id=238)	
newPathPrefix	"qxevents8FC3F245EB8D7BED5A546EE4B76454DE/" (id=239)	

		 */
		
		String themeRootPath=System.getProperty("TemplateStoreRoot") + platform;
		
		TemplateInfo template =  getTemplateInfoFromPath(templateTitle, themeRootPath +"/" + templateTitle , templatePlatform);
		if(template!=null){
			System.out.println("have template");
		}
		
		/***
		 * The original content should contain : "/themes/IPHONE_NATIVE/Elegant/files/"
		 */
		//String oriPathPrefix = "themes/"+templatePlatform+"/"+templateTitle+"/files/";
		String oriPathPrefix = ""+template.getAppId();
		
		
		/***
		 * The destination path should start with this : /ibizamedia5/Assets/
		 */
		String newPathPrefix = appIdentfier;
		
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
		String viewsPathStr = getUserStyleRootFolder(uuid, appIdentfier, platform) + "Views.css";
		
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, listItemsPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, contentPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, customFieldsPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, launchItemsPathStr);
		adjustCSSFileToApp(uuid, appIdentfier, platform, templateTitle, templatePlatform, 0, viewsPathStr);
		
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
			FileUtils.copyDirectory(srcPath, dstPath, new FileFilter() 
			{
				  public boolean accept(File pathname) 
				  {
				       if(pathname.getAbsolutePath().contains("svn"))
				       {
				    	   return false;
				       }
			        return true;
			    }
			}, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*
		try {
			System.out.println("copying template source asset folder : " + srcPathStr + " to : " + dstPathStr);
			//FileUtils.copyDirectory(srcPath, dstPath);
			ResourceTools.copyDirectory(srcPath, dstPath);
		} catch (IOException e) 
		{
			result=false;
			e.printStackTrace();
		}
		*/
		
		
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
	public static ArrayList<TemplateInfo>getTemplates(
			String uuid,
			String appIdentfier,
			String platform)
			{
				ArrayList<TemplateInfo>result = new ArrayList<TemplateInfo>();
				
				String themeRootPath=System.getProperty("TemplateStoreRoot") + platform;
				
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
					 			File descriptionFile = new File(absDir+"/description.html");
					 			if(descriptionFile.exists()){
					 				String []pathParts=StringUtils.toArray(absDir, "/");
					 				TemplateInfo template = getTemplateInfoFromPath(pathParts[pathParts.length -1],absDir,platform);
					 				if(template!=null){
					 					result.add(template);
					 					/*
					 					try {
											//template.setDescription( StringUtils.readFileAsString(absDir +"/description.html") );
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										*/
					 				}
					 			}
						 	}
					 }
				}
				return result;
			}
	
	public static void createJSONDummy(String title,String description,String path)
	{
		TemplateInfo result = new TemplateInfo();
		result.setUserId("11166763-e89c-44ba-aba7-4e9f4fdf97a9");
		result.setAppId("webapp");
		
		result.setTitle(title);
		result.setDescription(description);
		
		TemplateInfoPage page = new TemplateInfoPage();
		page.title="Launch";
		page.picture="launch.png";
		page.description="This is how the main navigation looks like";
		result.getPages().add(page);
		
		
		TemplateInfoPage page1 = new TemplateInfoPage();
		page1.title="ListItens";
		page1.picture="lists.png";
		page1.description="This is how lists will look like";
		result.getPages().add(page1);
		
		
		TemplateInfoPage page2 = new TemplateInfoPage();
		page2.title="Content";
		page2.picture="content.png";
		page2.description="This is how content will look like";
		result.getPages().add(page2);
		
		TemplateInfoPage page3 = new TemplateInfoPage();
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
	public static void updatePaths(TemplateInfo info,String platform)
	{
		for (int i = 0; i < info.getPages().size(); i++) {
			TemplateInfoPage page = info.getPages().get(i);
			String fullPath=System.getProperty("WebPath")+"CMAC/themes/"+platform+"/"+info.getTitle()+"/";
			page.setWebLocationPicture(fullPath+page.getPicture());
			
		}
	}
	public static TemplateInfo getTemplateInfoFromPath(String title,String path,String platform)
	{
		TemplateInfo result = new TemplateInfo();
		
		String description=null;
		try {
			description = StringUtils.readFileAsString(path+"/description.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(description==null || description.length()==0){
			return null;
		}
		result.setTitle(title);
		result.setDescription(description);
		
		String jsonPath = path+"/templateInfo.json";
		String json=null;
		File jsonFile = new File(jsonPath);
		if(!jsonFile.exists()){
			createJSONDummy(title, description, path);
		}
		JSONDeserializer<TemplateInfo>deserializer= new JSONDeserializer();
		
		
		try {
			 json = StringUtils.readFileAsString(jsonPath);
		} catch (IOException e) {
			System.out.println("application : " + jsonPath+ " doesnt exists");
		}
		
		if(json!=null && json.length() > 0)
		{
			result=  deserializer.deserialize(json);
		}
		result.setPlatform(platform);
		updatePaths(result, platform);
		
		
		return result;
		
	}
}
