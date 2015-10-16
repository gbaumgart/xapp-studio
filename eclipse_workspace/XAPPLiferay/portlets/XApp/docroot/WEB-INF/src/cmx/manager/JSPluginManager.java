package cmx.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jsontype.DojoPackage;
import pmedia.utils.StringUtils;
import cmx.tools.ResourceUtil;
import cmx.types.Resources;
import flexjson.JSONDeserializer;

public class JSPluginManager 
{
	
	public static ArrayList<Resources>getPluginResourcesFromPath(String pluginRoot,String rtConfig)
	{
		
		String plgPackageFilePath = pluginRoot + "/resources-"+rtConfig+".json";
	 	File plgPackageObj  = new File(plgPackageFilePath);
	 	if(plgPackageObj.exists() && !plgPackageObj.isDirectory())
	 	{
	 		Resources res=ResourceUtil.fromPath(plgPackageFilePath);
	 		if(res !=null)
	 		{
	 			ArrayList<Resources>result = new ArrayList<Resources>();
	 			result.add(res);
	 			return result;
	 		}
	 	}
		return null;
	}
	
	
	public static DojoPackage getDojoPackageFromPath(String path)
	{
		//read content 
 		String packageContent = null ; 
 		
 		try {
			packageContent=StringUtils.readFileAsString(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
 		
 		if(packageContent==null  || packageContent.length()==0)
 		{
 			return null;
 		}
 		

 		//convert to json
 		DojoPackage pkg = null;
 		JSONDeserializer<DojoPackage> dser = new JSONDeserializer<DojoPackage>();
 		pkg=dser.deserialize(packageContent);
 		if(pkg==null)
 		{
 			return null;
 		}
		return pkg;
	}
	
	public static ArrayList<DojoPackage>getDojoPluginPackages(String pluginRoot,String rtConfig,String systemAppName)
	{
		/*
		 * Parse plugin root directory, 
		 */
		
		ArrayList<DojoPackage>result = new ArrayList<DojoPackage>();
		
		File pluginDirObject = new File(pluginRoot);
		if(pluginDirObject.exists() && pluginDirObject.isDirectory())
		{
			 File[] files = pluginDirObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		//now try to find the plugin for the system app:
					 	String plgPackageFilePath = _file.getAbsolutePath() + "/"+systemAppName + "/package.json";
					 	File plgPackageObj  = new File(plgPackageFilePath);
					 	if(plgPackageObj.exists() && !plgPackageObj.isDirectory())
					 	{
					 		DojoPackage pkg = getDojoPackageFromPath(plgPackageFilePath);
					 		if(pkg !=null && pkg.enabled)
					 		{
					 			result.add(pkg);
					 		}
					 	}
				 	}
			 }
		}
		return result;
				
	}
	public static ArrayList<Resources>getPluginResources(String pluginRoot,String rtConfig,String systemAppName)
	{
		/*
		 * Parse plugin root directory, 
		 */
		
		ArrayList<Resources>result = new ArrayList<Resources>();
		
		File pluginDirObject = new File(pluginRoot);
		if(pluginDirObject.exists() && pluginDirObject.isDirectory())
		{
			 File[] files = pluginDirObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
			
				 	if(_file.isDirectory())
				 	{
				 		
				 		//now try to find the plugin for the system app:
					 	String plgPackageFilePath = _file.getAbsolutePath() + "/"+systemAppName + "/package.json";
					 	File plgPackageObj  = new File(plgPackageFilePath);
					 	if(plgPackageObj.exists() && !plgPackageObj.isDirectory())
					 	{
					 		//read content 
					 		String packageContent = null ; 
					 		
					 		try {
								packageContent=StringUtils.readFileAsString(plgPackageFilePath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								continue;
							}
					 		
					 		if(packageContent==null  || packageContent.length()==0)
					 		{
					 			continue;
					 		}
					 		

					 		//convert to json
					 		DojoPackage pkg = null;
					 		JSONDeserializer<DojoPackage> dser = new JSONDeserializer<DojoPackage>();
					 		pkg=dser.deserialize(packageContent);
					 		
					 		if(pkg==null || !pkg.enabled)
					 		{
					 			continue;
					 		}
					 		
					 		System.out.println("found plugin : " + plgPackageFilePath);
					 		
					 		//now build the resources
					 		ArrayList<Resources>res = getPluginResourcesFromPath(_file.getAbsolutePath() + "/"+systemAppName, rtConfig);
					 		if(res!=null && res.size()>0)
					 		{
					 			result.addAll(res);
					 		}
					 	}
				 	}
			 }
		}	
		
		if(result.size()>0)
		{
			return result;
		}
		
		return null;
	}
	
}
