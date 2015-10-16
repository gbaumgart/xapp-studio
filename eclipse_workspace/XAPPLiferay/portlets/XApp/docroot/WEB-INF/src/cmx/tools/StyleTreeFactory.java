package cmx.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xbill.DNS.SRVRecord;
import org.xml.sax.SAXException;

import pmedia.DataManager.Cache;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.Domain;
import pmedia.types.PMDataTypes;
import pmedia.utils.BitUtils;
import pmedia.utils.CListItemTools;
import pmedia.utils.CSSUtils;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.DBConnectionError;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;
import cmx.types.StyleTree;
import cmx.types.StyleTreeItem;

public class StyleTreeFactory extends ContentTreeFactoryBase
{	
	/**
	 * 
	 * @param label
	 * @param id
	 * @param uid
	 * @param section
	 * @param contentType
	 * @param top
	 * @param dataSourceUID
	 * @return
	 */
	public static StyleTreeItem createTreeItemEx(
			String label,
			String id,
			String uid,
			String section,
			String contentType,
			Boolean top,
			String dataSourceUID,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray)
	{
		StyleTreeItem result = new StyleTreeItem();
		result.children = new ArrayList<Reference>();
		result.name = label;
		
		
		
		int _uid =0;
		try {
			_uid=Integer.parseInt(uid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		result.uid=_uid;
		
		if(id!=null){
			result.id=id;
		}else{
			result.id = UUID.randomUUID().toString();
		}
		if(contentType!=null)
		{
			result.contentType=contentType;
		}else{
			result.contentType = "" + ECMContentSourceType.Unknown;
		}
		
		if(top){
			result.type="top";
		}else{
			result.type="leaf";
		}
		
		if(parentItem!=null)
		{
			Reference itemRef = new Reference();
	    	itemRef._reference="" + result.id;
	    	parentItem.addChild(itemRef);
	    	
		}
		
		if(tree!=null){
			tree.items.add(result);
		}
	
		if(dstArray!=null){
			dstArray.add(result);
		}
		
		return result;
	}
	public static StyleTreeItem createDummyTreeItem(
			String title,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
			)
	{
		StyleTreeItem result = createTreeItemEx(
				title, 
				UUID.randomUUID().toString(), 
				""+-1, 
				null,
				""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown), 
				false, 
				null,
				parentItem,
				tree,
				dstArray);

		return result;
	}
	
	public static StyleTreeItem createDefaultStyleTreeItemWithPath(
			String uuid,
			String appIdentfier,
			String pathPrefix,
			String fileName,
			StyleTreeItem parent,
			StyleTree tree)
	{
		
		StyleTreeItem defaultItem = createDummyTreeItem(fileName, parent, tree, null);
		defaultItem.type="leaf";
		defaultItem.uidStr = fileName;
		defaultItem.setPath(pathPrefix);
		
		if(fileName!=null){
			defaultItem.setPath(pathPrefix + fileName);
		}
		
		if(parent !=null & parent.name!=null && parent.name.length()>0){
			defaultItem.setClassPrefix(parent.name);
		}
		
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = null;
		if(uuid !=null && appIdentfier!=null){
			app =appManager.getApplication(uuid,appIdentfier,"Debug");
		}
		String filePathCSS = null;
		if(app!=null){
			filePathCSS=appManager.getUserAppPath(uuid, appIdentfier)+ pathPrefix +fileName;
		}else{
			filePathCSS= pathPrefix +fileName;
		}
		File cssFile = new File(filePathCSS);
		if(cssFile.exists() && !cssFile.isDirectory())
		{
			String content = null;
			try {
				content = StringUtils.readFileAsString(filePathCSS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(content!=null){
				defaultItem.setContent(content);
			}
		}
		return defaultItem;
	}
	
	public static StyleTreeItem createThemeStyleTreeItemWithPath(
			String uuid,
			String appIdentfier,
			String pathPrefix,
			String fileName,
			StyleTreeItem parent,
			StyleTree tree,
			String themePath)
	{
		
		StyleTreeItem defaultItem = createDummyTreeItem(fileName, parent, tree, null);
		defaultItem.type="leaf";
		defaultItem.uidStr = fileName;
		defaultItem.setId(fileName);
		defaultItem.setPath(pathPrefix);
		if(fileName!=null){
			defaultItem.setPath(pathPrefix + fileName);
		}
		ApplicationManager appManager =ApplicationManager.getInstance();
		String filePathCSS =appManager.getStoreRootPath()+ pathPrefix +fileName;
		File cssFile = new File(filePathCSS);
		if(cssFile.exists() && !cssFile.isDirectory())
		{
			String content = null;
			try {
				content = StringUtils.readFileAsString(filePathCSS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(content!=null){
				defaultItem.setContent(content);
			}
		}
		return defaultItem;
	}
	
	public Boolean isDataSource(String path,Application app)
	{
		//DataSourceBase ds
		return false;
	}
	
	public static String getStyleItemNameWithPath(
			String uuid,
			String appIdentfier,
			String platform, 
			StyleTree tree,
			StyleTreeItem rootItem,
			String path,
			String completePath)
	{
		
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid,appIdentfier,"Debug");
		
		String dsUID=null;
		DataSourceBase ds = null;
		
		String result = null;
		System.out.println("get name for " + path + " in " + completePath);
		String []pathParts=StringUtils.toArray(path, "/");
		
		if(pathParts!=null && pathParts.length > 0)
		{
			dsUID  = pathParts[0];
		}
		
		if(dsUID!=null )
		{
			ds = app.getDataSource(dsUID);
		}
		
		
		ECMContentSourceType cType = ECMContentSourceType.Unknown;
		if(pathParts!=null && pathParts.length > 0)
		{
			if(pathParts.length>=2)
			{
				int type = -1 ; 
				try {
					type = Integer.parseInt(pathParts[1]);	
				} catch (Exception e) 
				{

				}
				if(type!=-1)
				{
					cType =  ECMContentSourceTypeTools.FromString(pathParts[1]);
					if(pathParts.length==2)
					{
						return ECMContentSourceTypeTools.toName(cType);
					}
					
					if(pathParts.length==3)
					{
						//return ECMContentSourceTypeTools.toName(cType);
						if(ds!=null){
							
							DataSourceCache dataSourceCache = ServerCache.getDSC(appManager, app, ds);
							if(dataSourceCache!=null)
							{
								int ref = -1;
								try {
									ref = Integer.parseInt(pathParts[2]);
								} catch (Exception e) {
								}
								BaseData object = dataSourceCache.getObjectByTypeAndRef(cType,ref);
								if(object!=null && object.getTitle()!=null && object.getTitle().length() > 0){
									return object.getTitle();
								}
								
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	public static StyleTreeItem createStyleItemWithPath(
			String uuid,
			String appIdentfier,
			String platform, 
			StyleTree tree,
			StyleTreeItem rootItem,
			String path,
			String completePath)
	{
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = null;
		
		if(uuid !=null && appIdentfier!=null){
			app = appManager.getApplication(uuid,appIdentfier,"Debug");
		}
		
		String stylePlatform ="IPHONE_NATIVE";
		if(platform!=null){
			stylePlatform=platform;
		}
		
		String userPath = null ;
		if( uuid !=null && appIdentfier!=null){
				userPath=appManager.getStoreRootPath() + uuid + "/apps/"+appIdentfier+"/style/" + stylePlatform + "/";
		}
		
		userPath=userPath.replace("//", "/");
		
		String dsUID=null;
		String [] pathParts = StringUtils.toArray(path, "/");
		if(pathParts!=null && pathParts.length > 0)
		{
			dsUID  = pathParts[pathParts.length -1];
		}

		
		File userPathFileObject = new File(path);
		//String []entries = userPathFileObject.list();
		//ArrayList pathItems=null; 
		StyleTreeItem pathRootItem=null;
		if(userPathFileObject!=null && userPathFileObject.exists() && userPathFileObject.isDirectory() && path.equals(completePath))
		{
			String relativePath = path.replace(userPath, "");
			String name = relativePath;
	 		pathRootItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" , name, rootItem, tree);
	 		pathRootItem.type="StyleGroup";
		}
		
		if(userPathFileObject.exists())
		{
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	String absDir = _file.getAbsolutePath();
			 		String relativePath = absDir.replace(userPath, "");
			 		String pathPrefix = absDir.replace(userPath, "");
			 		String []relativeParts=StringUtils.toArray(relativePath, "/");
			 		if(relativeParts!=null && relativeParts.length > 0)
			 		{
			 			relativePath=relativeParts[relativeParts.length-1];
			 		}
			 		if(pathRootItem==null)
			 		{
			 			pathRootItem=rootItem;
			 		}
				 	if(_file.isDirectory())
				 	{
				 		String name = getStyleItemNameWithPath(uuid, appIdentfier, platform, tree, pathRootItem, pathPrefix, completePath);
				 		if(name != null){
				 			relativePath=name;
				 		}
				 		StyleTreeItem pathItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" + pathPrefix, relativePath, pathRootItem, tree);
				 		createStyleItemWithPath(uuid, appIdentfier, platform, tree, pathItem, absDir, completePath);
				 	}else
				 	{
				 		if(absDir.contains("css"))
				 		{
					 		pathPrefix=pathPrefix.replace(relativePath, "");
					 		StyleTreeItem pathItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" + pathPrefix, relativePath, pathRootItem, tree);
				 		}
				 	}
			 }
		}
		/*
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid,appIdentfier,"Debug");
		String userPath = appManager.getStoreRootPath() + uuid + "/apps/"+appIdentfier+"/style/IPHONE_NATIVE/";
		
		userPath=userPath.replace("//", "/");
		
		String dsUID=null;
		String [] pathParts = StringUtils.toArray(path, "/");
		if(pathParts!=null && pathParts.length > 0)
		{
			dsUID  = pathParts[pathParts.length -1];
		}
		
		DataSourceBase ds = null;
		
		if(dsUID!=null )
		{
			ds = app.getDataSource(dsUID);
		}
		if(ds==null)
		{
			//return null;
		}
		
		//String relativePath 
		
		File userPathFileObject = new File(path);
		String []entries = userPathFileObject.list();
		ArrayList pathItems=null; 
		StyleTreeItem pathRootItem=null;
		
		if(userPathFileObject!=null && userPathFileObject.exists() && userPathFileObject.isDirectory() && path.equals(completePath))
		{
			String relativePath = path.replace(userPath, "");
			
			String name = relativePath;
			
			if(ds!=null){
				name = getDataSourceLabel(ds);
			}
	 		pathRootItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" , name, rootItem, tree);
		}
		
		if(userPathFileObject.exists())
		{
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	String absDir = _file.getAbsolutePath();
			 		String relativePath = absDir.replace(userPath, "");
			 		String pathPrefix = absDir.replace(userPath, "");
			 		String []relativeParts=StringUtils.toArray(relativePath, "/");
			 		if(relativeParts!=null && relativeParts.length > 0)
			 		{
			 			relativePath=relativeParts[relativeParts.length-1];
			 		}
			 		if(pathRootItem==null)
			 		{
			 			pathRootItem=rootItem;
			 		}
				 	if(_file.isDirectory())
				 	{
				 		String name = getStyleItemNameWithPath(uuid, appIdentfier, platform, tree, pathRootItem, pathPrefix, completePath);
				 		if(name != null){
				 			relativePath=name;
				 		}
				 		StyleTreeItem pathItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" + pathPrefix, relativePath, pathRootItem, tree);
				 		createStyleItemWithPath(uuid, appIdentfier, platform, tree, pathItem, absDir, completePath);
				 	}else{
				 		pathPrefix=pathPrefix.replace(relativePath, "");
				 		StyleTreeItem pathItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" + pathPrefix, relativePath, pathRootItem, tree);
				 	}
			 }
		}
		*/
		
		return null;
		/*
		
		Collection< File> fentries= FileUtils.listFiles(userPathFileObject, null, true);
		 for (Iterator<File> iterator = fentries.iterator(); iterator.hasNext();) 
		 {
		        File _file = (File) iterator.next();
	          	String abspath = _file.getAbsolutePath();
			 	String fileName = StringUtils.filenameComplete(abspath);
			 	if(fileName.contains(".css"))
			 	{
			 		String relativePath = abspath.replace(userPath, "");
			 		String []relativeParts=StringUtils.toArray(relativePath, "/");
			 		if(relativeParts!=null && relativeParts.length > 0)
			 		{
			 			for(int i = 0 ; i < relativeParts.length ; i ++ )
			 			{

			 				String pathPart = relativeParts[i];
			 				
			 				StyleTreeItem pathItem = new StyleTreeItem();
			 				rootItem.name = pathPart; 
			 				rootItem.id = UUID.randomUUID().toString();
			 				rootItem.children = new ArrayList<Reference>();
			 				tree.items.add(pathItem);
			 				pathItem.type = "leaf";
			 				
			 				if(i==0)
			 				{
			 					Reference ref = new Reference();
			 					ref._reference ="" + pathItem.id;
			 				}
			 				
			 			}
			 		}
			 		
			 	}
		 }
		return null;
		*/
	}
	public static void unregisterStyle(String name,
			String uuid,
			String appIdentfier,
			String platform)
	{
		String stylePlatform ="IPHONE_NATIVE";

		String userPath = ApplicationManager.getInstance().getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/"  + stylePlatform +  "/";
		File defaultFile = new File(userPath+"Default/Default.css");
		if(defaultFile.exists())
		{
			String content = null ; 
			
			try {
				content = StringUtils.readFileAsString(userPath+"Default/Default.css");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(content!=null)
			{
				String newContent =content;
				newContent=StringUtils.replace(newContent, "@import url('" + name +"/ListItems.css')\n" , "");
				newContent=StringUtils.replace(newContent, "@import url('" + name +"/CustomFields.css')\n" , "");
				newContent=StringUtils.replace(newContent, "@import url('" + name +"/Content.css')\n" , "");
				try {
					StringUtils.writeToFile(newContent, userPath+"Default/Default.css");
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	public static void registerNewStyle(String name,
			String uuid,
			String appIdentfier,
			String platform)
	{
		//String stylePlatform ="IPHONE_NATIVE";
		String userPath = ApplicationManager.getInstance().getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/"+ platform   +"/";
		File defaultFile = new File(userPath+"Default/Default.css");
		if(defaultFile.exists())
		{
			String content = null ; 
			
			try {
				content = StringUtils.readFileAsString(userPath+"Default/Default.css");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(content!=null)
			{
				String firstLine = "/* REGISTER CUSTOM STYLES END */\n";
				String newContent ="";
				newContent+="@import url('" + name +"/ListItems.css')\n";
				newContent+="@import url('" + name +"/CustomFields.css')\n";
				newContent+="@import url('" + name +"/Content.css')\n";
				newContent+="@import url('" + name +"/Views.css')\n";
				newContent+="/* REGISTER CUSTOM STYLES END */\n";
				content=content.replace(firstLine, newContent);
				//StringUtils.replace(content, firstLine, newContent);
				try {
					StringUtils.writeToFile(content, userPath+"Default/Default.css");
				} catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		}
	}
	public static DBConnectionError copyStyle(String uuid,
			String appIdentfier,
			String srcPlatform, 
			String dstPlatform,
			String srcRefId,
			String dstRefId)
	{
		DBConnectionError e = new DBConnectionError();
		
		String stylePlatform = srcPlatform;		
		String []pathParts=StringUtils.toArray(srcRefId, "/");
		
		String srcDir = null;
		String dstDir = null;
		
		String userPath = ApplicationManager.getInstance().getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/" + stylePlatform+ "/";
		
		if(pathParts!=null && pathParts.length > 0)
		{
			if(srcRefId.equals("style/"  + stylePlatform +  "/Default/Default"))
			{
				srcDir= "Default";
			}
			
			if(srcDir==null)
			{
				String _srcDirTest = userPath+pathParts[pathParts.length-1];
				File _srcDir = new File (_srcDirTest);
				if(_srcDir.isDirectory()){
					srcDir=pathParts[pathParts.length-1];
				}
			}
		}

		
		
		if(srcDir!=null)
		{
			srcDir=userPath + srcDir;
			dstDir=userPath + dstRefId;
		}
		
		File srcPathDirObject = new File(srcDir +"/");
		File dstPathDirObject = new File(dstDir +"/");
		
		if(dstPathDirObject.exists())
		{			
			dstPathDirObject.delete();
		}
		if(srcPathDirObject.isDirectory())
		{
			try {
				FileUtils.copyDirectory(srcPathDirObject, new File(dstDir));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		File defaultDstFile = new File(dstDir+"/Default.css");
		
		if(defaultDstFile.exists()){
			defaultDstFile.delete();
		}
		
		if(srcRefId.equals("style/" + stylePlatform + "/Default/Default"))
		{
			File listItems=new File(userPath+"ListItems.css");
			File content=new File(userPath+"Content.css");
			File customFields=new File(userPath+"CustomFields.css");
			File views=new File(userPath+"Views.css");
			
			try {
				FileUtils.copyFile(listItems, new File(userPath+dstRefId+"/ListItems.css"));
				FileUtils.copyFile(content, new File(userPath+dstRefId+"/Content.css"));
				FileUtils.copyFile(customFields, new File(userPath+dstRefId+"/CustomFields.css"));
				FileUtils.copyFile(views, new File(userPath+dstRefId+"/Views.css"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return e;
	}
	public static DBConnectionError copyStyleTree(String uuid,
			String appIdentfier,
			String platform, 
			String srcRefId,
			String dstRefId)
	{
		DBConnectionError e = new DBConnectionError();
		
		String stylePlatform ="IPHONE_NATIVE";
		
		String []pathParts=StringUtils.toArray(srcRefId, "/");
		
		String srcDir = null;
		String dstDir = null;
		String userPath = ApplicationManager.getInstance().getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/" + stylePlatform+ "/";
		
		if(pathParts!=null && pathParts.length > 0)
		{
			if(srcRefId.equals("style/"  + stylePlatform +  "/Default/Default.css"))
			{
				srcDir= "Default";
			}
			
			if(srcDir==null)
			{
				String _srcDirTest = userPath+pathParts[pathParts.length-1];
				File _srcDir = new File (_srcDirTest);
				if(_srcDir.isDirectory()){
					srcDir=pathParts[pathParts.length-1];
				}
			}
		}

		
		
		if(srcDir!=null)
		{
			srcDir=userPath + srcDir;
			dstDir=userPath + dstRefId;
		}
		
		File srcPathDirObject = new File(srcDir +"/");
		File dstPathDirObject = new File(dstDir +"/");
		
		if(dstPathDirObject.exists())
		{			
			dstPathDirObject.delete();
		}
		if(srcPathDirObject.isDirectory())
		{
			
			try {
				FileUtils.copyDirectory(srcPathDirObject, new File(dstDir), new FileFilter() {
					  public boolean accept(File pathname) {
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
		}
		File defaultDstFile = new File(dstDir+"/Default.css");
		
		if(defaultDstFile.exists()){
			defaultDstFile.delete();
		}
		
		if(srcRefId.equals("style/" + stylePlatform + "/Default/Default.css"))
		{
			File listItems=new File(userPath+"ListItems.css");
			File content=new File(userPath+"Content.css");
			File customFields=new File(userPath+"CustomFields.css");
			
			File views=new File(userPath+"Views.css");
			
			try {
				FileUtils.copyFile(listItems, new File(userPath+dstRefId+"/ListItems.css"));
				FileUtils.copyFile(content, new File(userPath+dstRefId+"/Content.css"));
				FileUtils.copyFile(customFields, new File(userPath+dstRefId+"/CustomFields.css"));
				FileUtils.copyFile(customFields, new File(userPath+dstRefId+"/Views.css"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return e;
	}
	
	
	public static StyleTree createThemeStyleTree(
			String platform, 
			String template)
	{
		StyleTree tree = new StyleTree();
		tree.id="1";
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);
		
		StyleTreeItem rootItem = new StyleTreeItem();
		rootItem.name = "Styles"; 
		rootItem.id = "ROOT";
		rootItem.children = new ArrayList<Reference>();
		tree.items.add(rootItem);
		rootItem.type = "top";

		ApplicationManager appManager = ApplicationManager.getInstance();
		
		/*
		StyleTreeItem defaultListItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "ListItems.css", defaultTextItem, tree);
		StyleTreeItem contentItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "Content.css", defaultTextItem, tree);
		StyleTreeItem customFieldsItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "CustomFields.css", defaultTextItem, tree);
		StyleTreeItem launchItems = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "LaunchItems.css", defaultTextItem, tree);
		*/
		String userPath = appManager.getStoreRootPath() + "/themes/" + platform +"/" + template + "/files/style/"+platform+"/";
		
		File userPathFileObject = new File(userPath);
		if(userPathFileObject.exists() && userPathFileObject.isDirectory())
		{
			StyleTreeItem defaultTextItem = createThemeStyleTreeItemWithPath(null, null, "/themes/" + platform +"/" + template + "/files/style/" + platform + "/Default/", "Default.css", rootItem, tree,userPath);
			defaultTextItem.type="StyleGroup";
			
			StyleTreeItem defaultListItem = createThemeStyleTreeItemWithPath(null, null, "/themes/" + platform +"/" + template + "/files/style/"+platform+"/", "ListItems.css", defaultTextItem, tree,userPath);
			StyleTreeItem contentItem = createThemeStyleTreeItemWithPath(null, null, "/themes/" + platform +"/" + template + "/files/style/"+platform+"/", "Content.css", defaultTextItem, tree,userPath);
			StyleTreeItem customFieldsItem = createThemeStyleTreeItemWithPath(null, null, "/themes/" + platform +"/" + template + "/files/style/"+platform+"/" , "CustomFields.css", defaultTextItem, tree,userPath);
			StyleTreeItem launchItems = createThemeStyleTreeItemWithPath(null, null, "/themes/" + platform +"/" + template + "/files/style/"+platform+"/", "LaunchItems.css", defaultTextItem, tree,userPath);
			//StyleTreeItem theme = createThemeStyleTreeItemWithPath(null, null, "/themes/" + platform +"/" + template + "/files/style/"+platform+"/", "Theme.css", defaultTextItem, tree,userPath);
			 File[] files = userPathFileObject.listFiles();
			 /*for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		String absDir = _file.getAbsolutePath();
				 		if(!absDir.contains("Default"))
				 		{
				 			StyleTreeItem item = createStyleItemWithPath(null, null, platform, tree, rootItem, absDir,absDir);
				 			if(item!=null)
				 			{				 				
				 			}
				 		}
				 	}
			 }
			 */
		}
		
		
		return tree;
		
	}
	
	public static String getMergedCSS(String uuid,
			String appIdentfier,
			String platform)
	{
		String result ="";
		
		
		StyleTree tree = createStyleTree(uuid, appIdentfier, platform, null, null, null);
		for (int i = 0 ; i<tree.getItems().size() ; i++ )
		{
			StyleTreeItem item = (StyleTreeItem) tree.getItems().get(i);
			if(item.getContent() !=null)
			{
				result +=item.getContent();
			}
		}
		return result;
	}
	public static StyleTree createStyleTree(String uuid,
			String appIdentfier,
			String platform, 
			String pageId,
			String dataSourceUID,
			String refId)
	{
	
		
		StyleTree tree = new StyleTree();
		tree.id="1";
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);
		
		StyleTreeItem rootItem = new StyleTreeItem();
		rootItem.name = "Styles"; 
		rootItem.id = "ROOT";
		rootItem.children = new ArrayList<Reference>();
		tree.items.add(rootItem);
		rootItem.type = "top";

		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(uuid,appIdentfier,"Debug");
		if(app==null){
			return null;
		}
		
		if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE))
		{
			platform=pmedia.types.Constants.USERAGENT_IPHONE_NATIVE;
		}
		/*
		StyleTreeItem defaultListItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "ListItems.css", defaultTextItem, tree);
		StyleTreeItem contentItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "Content.css", defaultTextItem, tree);
		StyleTreeItem customFieldsItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "CustomFields.css", defaultTextItem, tree);
		StyleTreeItem launchItems = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" + "Default/", "LaunchItems.css", defaultTextItem, tree);
		*/
		String userPath = appManager.getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/" + platform +"/";
		
		File userPathFileObject = new File(userPath);
		if(userPathFileObject.exists() && userPathFileObject.isDirectory())
		{
			StyleTreeItem defaultTextItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+ platform +"/" + "Default/", "Default.css", rootItem, tree);
			defaultTextItem.type="StyleGroup";
			StyleTreeItem defaultListItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/", "ListItems.css", defaultTextItem, tree);
			StyleTreeItem contentItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/"  , "Content.css", defaultTextItem, tree);
			StyleTreeItem customFieldsItem = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/" , "CustomFields.css", defaultTextItem, tree);
			StyleTreeItem launchItems = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/", "LaunchItems.css", defaultTextItem, tree);

			StyleTreeItem views = createDefaultStyleTreeItemWithPath(uuid, appIdentfier, "style/"+platform+"/", "Views.css", defaultTextItem, tree);
			
			 File[] files = userPathFileObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory() && !_file.getName().contains("svn"))
				 	{
				 		String absDir = _file.getAbsolutePath();
				 		if(!absDir.contains("Default"))
				 		{
				 			StyleTreeItem item = createStyleItemWithPath(uuid, appIdentfier, platform, tree, rootItem, absDir,absDir);
				 			if(item!=null)
				 			{				 		
				 				
				 			}
				 		}
				 	}
			 }
		}else{
			//platform doesn't exists : 
			String dstPlatform = platform;
			String sourcePlatform=null;
			if(dstPlatform.equals(Constants.USERAGENT_IPHONE_NATIVE))
			{				
				sourcePlatform=Constants.MOBILE_WEB_APP;
			}
			
			if(dstPlatform.equals(Constants.MOBILE_WEB_APP))
			{				
				sourcePlatform=Constants.USERAGENT_IPHONE_NATIVE;
			}
			
			Boolean  copyStyles =true;
			
			if(sourcePlatform!=null && dstPlatform!=null){
				if(sourcePlatform.equals(Constants.MOBILE_WEB_APP) && dstPlatform.equals(Constants.USERAGENT_IPHONE_NATIVE)){
					System.out.println("copying style from " + sourcePlatform + " to : " + dstPlatform);
					copyStyles=false;
				}
			}else{
				return null;
			}
			
			
			//ApplicationManager appManager = ApplicationManager.getInstance();
			
			
			if(copyStyles){
				File srcPath = new File(appManager.getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/" + sourcePlatform +"/");
				File dstPath = new File(appManager.getStoreRootPath() + "/" + uuid + "/apps/"+appIdentfier+"/style/" + dstPlatform +"/");
				if(srcPath.exists() && dstPath.exists()){
					try {
						FileUtils.copyDirectory(srcPath, dstPath);
					} catch (IOException e) 
					{
						e.printStackTrace();
						return null;
					}
				}else{
					
				}
			}
			return createStyleTree(uuid, appIdentfier, platform, pageId, dataSourceUID, refId);
			
		}
		return tree;
		
	}
	
}
