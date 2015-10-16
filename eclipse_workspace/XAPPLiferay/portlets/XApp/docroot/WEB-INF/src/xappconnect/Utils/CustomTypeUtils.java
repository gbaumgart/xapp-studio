package xappconnect.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import jsontype.DojoPackage;

import pmedia.utils.CITools;
import pmedia.utils.StringUtils;

import cmx.tools.CIFactory;
import cmx.tools.ResourceUtil;
import cmx.types.ConfigurableInformation;
import cmx.types.Resources;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import xappconnect.types.CustomType;

public class CustomTypeUtils 
{
	
	
	public static String toHTML(ArrayList<CustomType>types, String uuid,String appId, String systemAppName,String rtConfig)
	{
		return null;
	}

	public static ArrayList<CustomType>getTypesAll(String rtConfig,String platform,String uuid,String appId,String systemAppName)
	{
		ArrayList<CustomType>result = new ArrayList<CustomType>();
			
			
		String ctScope = "%XASWEB%";
		String prefix = "/ctypes/";
		//public static ArrayList<CustomType>getTypes(String rtConfig,String platform,String uuid,String appId,String systemAppName,String storeRoot)
		ArrayList<CustomType> pTypesSystem = CustomTypeUtils.getTypes(rtConfig, "IPHONE_NATIVE", uuid,appId, systemAppName, ctScope + prefix);
		ArrayList<CustomType> pTypesUser = CustomTypeUtils.getTypes(rtConfig, "IPHONE_NATIVE", uuid,appId, systemAppName, "%XUSER%/ctypes");
		ArrayList<CustomType> pTypesApp = CustomTypeUtils.getTypes(rtConfig, "IPHONE_NATIVE", uuid,appId, systemAppName, "%XAPP%/ctypes");
		result.addAll(pTypesSystem);
		result.addAll(pTypesUser);
		result.addAll(pTypesApp);
		return result;
	}
	public static ArrayList<CustomType>getTypes(String rtConfig,String platform,String uuid,String appId,String systemAppName,String storeRoot)
	{
		ArrayList<CustomType>result = new ArrayList<CustomType>();
		
		if(storeRoot!=null)
		{
			storeRoot= ResourceUtil.resolveConstantsAbsolute(storeRoot, uuid, appId);
		}
		String dstFilePath =storeRoot + "/" + platform + "/" + rtConfig + "/";
		/*
		 * Parse plugin root directory, 
		 */
		File pluginDirObject = new File(dstFilePath);
		if(pluginDirObject.exists() && pluginDirObject.isDirectory())
		{
			 File[] files = pluginDirObject.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isFile() && _file.getAbsolutePath().endsWith(".json"))
				 	{
				 		CustomType type = loadFromPath(_file.getAbsolutePath());
				 		if(type!=null && type.getEnabled())
				 		{
				 			result.add(type);
				 		}
				 	}
			 }
		}	
		
		if(result.size()>0)
		{
			return result;
		}
		
		return result;
	}
	

	public static void deleteType(CustomType type,String rtConfig,String platform,String dstPath,String uuid,String appId)
	{
		String root = System.getProperty("XAppConnectTypeStore");
		if(dstPath!=null)
		{
			//root = "" + dstPath;
			root = ResourceUtil.resolveConstantsAbsolute(dstPath, uuid, appId);
		}
		String dstFilePath =root + "/" + platform + "/" + rtConfig + "/" ;
		
		File dstFileObj = new File(dstFilePath);
		if(!dstFileObj.exists()){
			dstFileObj.mkdirs();
		}
		String name = type.getName().replace(" ", "");
		dstFilePath+=name + ".json";
		dstFileObj=new File(dstFilePath);
		if(dstFileObj.exists())
		{
			dstFileObj.delete();
		}
	}
	public static void saveType(CustomType type,String rtConfig,String platform,String dstPath,String uuid,String appId,Boolean overwrite)
	{
		
		if(type==null){
			return;
		}
		
		String root = System.getProperty("XAppConnectTypeStore");
		if(dstPath!=null)
		{
			//root = "" + dstPath;
			root = ResourceUtil.resolveConstantsAbsolute(dstPath, uuid, appId);
		}
		String dstFilePath =root + "/" + platform + "/" + rtConfig + "/" ;
		
		File dstFileObj = new File(dstFilePath);
		if(!dstFileObj.exists()){
			dstFileObj.mkdirs();
		}
		String name = type.getName().replace(" ", "");
		dstFilePath+=name + ".json";
		
		if(type.getId()!=null && type.getId().equals("noID")){
			type.setId(UUID.randomUUID().toString());
		}
		
		if(overwrite){
			dstFileObj=new File(dstFilePath);
			if(dstFileObj.exists())
			{
				dstFileObj.delete();
			}
		}
		
		JSONSerializer serializer = new JSONSerializer();
		serializer.prettyPrint(true);
    	String jsonres = serializer.deepSerialize(type);
    	
    	if(jsonres==null || jsonres.equals("null") || jsonres.length()==0){
    		System.out.println("saving custom type  failed : invalid content!");
    		return;
    	}
    	System.out.println("saving custom type  to " + dstFilePath + " root : " + root);
    	
    	try {
    		//System.out.println("saving ctype : " + jsonres.length() + " at :" +dstFilePath);
			StringUtils.writeToFile(jsonres, dstFilePath);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static CustomType fixType(CustomType type)
	{
		if(type==null)
		{
			return null;
		}
		ConfigurableInformation isDetail = CITools.getByName(type.getInputs(), "isDetail");
		if(isDetail==null){
			isDetail= CIFactory.SimpleBoolean("isDetail", false, 22);
			//isDetail.setType(15);
			type.getInputs().add(isDetail);
		}
		
		ConfigurableInformation userData = CITools.getByName(type.getInputs(), "userData");
		if(userData!=null){
			userData.setType(26);
		}
		
		
		ConfigurableInformation relations = CITools.getByName(type.getInputs(), "relations");
		if(relations==null){
			relations= CIFactory.SimpleStringCI("relations", "", 23);
			relations.setType(27);
			type.getInputs().add(relations);
		}
		
		ConfigurableInformation oS= CITools.getByName(type.getInputs(), "orderSelectStatement");
		if(oS==null){
			oS= CIFactory.SimpleStringCI("orderSelectStatement", "", 24);
			oS.setType(13);
			type.getInputs().add(oS);
		}
		
		ConfigurableInformation lS= CITools.getByName(type.getInputs(), "limitSelectStatement");
		if(lS==null){
			lS= CIFactory.SimpleStringCI("limitSelectStatement", "", 25);
			lS.setType(13);
			type.getInputs().add(lS);
		}
		
		ConfigurableInformation fn= CITools.getByName(type.getInputs(), "friendlyName");
		if(fn==null){
			fn= CIFactory.SimpleStringCI("friendlyName", "", 26);
			fn.setType(13);
			type.getInputs().add(fn);
		}
		
		ConfigurableInformation icon= CITools.getByName(type.getInputs(), "icon");
		if(icon==null){
			icon= CIFactory.SimpleStringCI("icon", "", 27);
			icon.setType(13);
			type.getInputs().add(icon);
		}
		
		 for (int i=0; i<type.getInputs().size(); i++) 
		 {
			 ConfigurableInformation ci = type.getInputs().get(i);
			 if(!ci.getId().matches("\\b[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}\\b"))
			 {
				 ci.setId(UUID.randomUUID().toString());
			 }
		 }
		 return type;
	}
	public static CustomType getByUrlSchema(ArrayList<CustomType>types, String schema)
	{
		
		for(int i = 0 ; i <types.size() ; i ++){
			CustomType type = types.get(i);
			ConfigurableInformation ci = CITools.getByName(type.getInputs(), "urlSchema");
			if(ci!=null && ci.getValue()!=null && ci.getValue().equals(schema)){
				return type;
			}
		}
		return null;
	}
	public static CustomType loadFromPath(String filePath)
	{
		if(filePath==null){
			return null;
		}
		//System.out.println("loading custom type from : " + filePath);
		//String root = System.getProperty("XAppConnectTypeStore");
		JSONDeserializer<CustomType> dserializer = new JSONDeserializer<CustomType>();
		String data = null;
		try {
			data = StringUtils.readFileAsString(filePath);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		if(data==null){
			return null;
		}
		
		CustomType result =null;
		try 
		{
   			result = (CustomType)dserializer.deserialize(data);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		if(result==null){
			System.out.println("something wrong with loading custom type : " +filePath);
		}
		
		fixType(result);
		JSONSerializer serializer = new JSONSerializer();
		serializer.prettyPrint(true);
    	String jsonres = serializer.deepSerialize(result);
    	
    	try {
			StringUtils.writeToFile(jsonres, filePath);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		if(result!=null&&result.getId()!=null && result.getId().equals("noID"))
		{
			result.setId(UUID.randomUUID().toString());
		}
		return result;
		
	}
	public static CustomType loadType(String rtConfig,String platform,String uuid,String appId,String name, String dstPath)
	{
		String root = System.getProperty("XAppConnectTypeStore");
		if(dstPath!=null)
		{
			//root = "" + dstPath;
			root = ResourceUtil.resolveConstantsAbsolute(dstPath, uuid, appId);
		}
		name = name.replace(" ", "");
		String dstFilePath =root + "/" + platform + "/" + rtConfig + "/"  + name + ".json";
		
		JSONDeserializer dserializer = new JSONDeserializer<CustomType>();
		String data = null;
		try {
			data = StringUtils.readFileAsString(dstFilePath);
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			
		}
		if(data==null){
			return null;
		}
		
		CustomType result =null;
		try 
		{
   			result = (CustomType)dserializer.deserialize(data);
		} catch (Exception e) 
		{
		}
		
		if(result.getId()!=null && result.getId().equals("noID")){
			result.setId(UUID.randomUUID().toString());
		}
		
		return result;
	}
}
