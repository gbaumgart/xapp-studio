package cmx.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import pmedia.utils.ECMContentSourceTypeTools;
import cmx.tools.ResourceUtil;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.ECMContentSourceType;
import cmx.types.Reference;
import cmx.types.Resource;
import cmx.types.ResourceTree;
import cmx.types.ResourceTreeItem;
import cmx.types.Resources;
import cmx.types.StyleTreeItem;
import flexjson.JSONDeserializer;
 
public class ResourceManager 
{
	public static Resources updateResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path,
			String oldResource,
			String newResource)
	{
		
		Resources result =null;
		
		String resourcesPath = ResourceUtil.resolveConstantsAbsolute(path, uuid, appIdentfier);
		if(resourcesPath==null)
		{
			return null;
		}
		
		File resourcesFile = new File(resourcesPath);
		if(!resourcesFile.exists())
		{
			result = new Resources();
			ResourceUtil.save(result, resourcesPath);
		}else{
			result = ResourceUtil.fromPath(resourcesPath);
			//ResourceUtil.resolve(result, uuid, appIdentfier, "");
		}
		
		Resource oldRes=null;
		Resource newRes=null;
		if(newResource!=null)
		{
			JSONDeserializer< Resource> dser= new JSONDeserializer<Resource>();
			try {
				oldRes = dser.deserialize(oldResource);	
			} catch (Exception e) {

			}
			
			try {
				newRes = dser.deserialize(newResource);	
			} catch (Exception e) {

			}
		
			if(newRes!=null && oldRes!=null)
			{
				//result.getItems().add(newRes);
				
				Resource oriRes = ResourceUtil.findResource(result, oldRes.url, newRes.path, newRes.type);
				if(oriRes!=null)
				{
					//result.getItems().remove(oriRes);
					oriRes.url = newRes.url;
					oriRes.type = newRes.type;
					oriRes.urlOri = newRes.urlOri;
					oriRes.path = newRes.path;
					oriRes.enabled=newRes.enabled;
					
				}
				ResourceUtil.save(result, resourcesPath);
			}
		}
		return result;
	}
	public static Resources removeResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path,
			String newResource)
	{
		
		Resources result =null;
		
		String resourcesPath = ResourceUtil.resolveConstantsAbsolute(path, uuid, appIdentfier);
		if(resourcesPath==null)
		{
			return null;
		}
		
		File resourcesFile = new File(resourcesPath);
		if(!resourcesFile.exists())
		{
			result = new Resources();
			ResourceUtil.save(result, resourcesPath);
		}else{
			result = ResourceUtil.fromPath(resourcesPath);
			//ResourceUtil.resolve(result, uuid, appIdentfier, "");
		}
		
		Resource newRes=null;
		if(newResource!=null)
		{
			JSONDeserializer< Resource> dser= new JSONDeserializer<Resource>();
			try {
				newRes = dser.deserialize(newResource);	
			} catch (Exception e) {

			}
		
			if(newRes!=null){
				//result.getItems().add(newRes);
				
				Resource oriRes = ResourceUtil.findResource(result, newRes.url, newRes.path, newRes.type);
				if(oriRes!=null)
				{
					result.getItems().remove(oriRes);
				}
				ResourceUtil.save(result, resourcesPath);
			}
		}
		return result;
	}
	
	public static Resources addResource(
			String uuid,
			String appIdentfier,
			String platform,
			String path,
			String newResource)
	{
		
		Resources result =null;
		
		String resourcesPath = ResourceUtil.resolveConstantsAbsolute(path, uuid, appIdentfier);
		if(resourcesPath==null)
		{
			return null;
		}
		
		File resourcesFile = new File(resourcesPath);
		if(!resourcesFile.exists())
		{
			result = new Resources();
			ResourceUtil.save(result, resourcesPath);
		}else{
			result = ResourceUtil.fromPath(resourcesPath);
			ResourceUtil.resolve(result, uuid, appIdentfier, "");
		}
		
		Resource newRes=null;
		if(newResource!=null)
		{
			JSONDeserializer< Resource> dser= new JSONDeserializer<Resource>();
			
			
			try {
				newRes = dser.deserialize(newResource);	
			} catch (Exception e) {

			}
		
			if(newRes!=null)
			{
				result.getItems().add(newRes);
				ResourceUtil.saveEx(result, resourcesPath,true);
			}
		}
		return result;
	}
	public static Resources getResources(
			String uuid,
			String appIdentfier,
			String platform,
			String path)
	{
		
		Resources result =null;
		
		String resourcesPath = ResourceUtil.resolveConstantsAbsolute(path, uuid, appIdentfier);
		if(resourcesPath==null)
		{
			return null;
		}
		
		File resourcesFile = new File(resourcesPath);
		if(!resourcesFile.exists())
		{
			result = new Resources();
			ResourceUtil.save(result, resourcesPath);
		}else{
			result = ResourceUtil.fromPath(resourcesPath);
			ResourceUtil.resolve(result, uuid, appIdentfier, "");
		}
		return result;
	}
	public static ResourceTree getResourceTree(
			String uuid,
			String appIdentfier,
			String platform,
			String appName)
	{
		ResourceTree tree = new ResourceTree();
		tree.id="1";
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);
		
		/***
		 * 1. Root Item
		 */
		StyleTreeItem rootItem = new StyleTreeItem();
		
		rootItem.name = "Resources";
		rootItem.id = "ROOT";
		rootItem.children = new ArrayList<Reference>();
		tree.items.add(rootItem);
		rootItem.type = "top";

		/***
		 * 1. Write cxapp system resources
		 */
		
		ResourceTreeItem cappAppRoot = createDummyTreeItem("Custom XApp",rootItem, tree, null);
		cappAppRoot.type="ResourceGroup";
		cappAppRoot.uidStr = "CXAPP";
		
		
		ResourceTreeItem cappAppRootDebug = createDummyTreeItem("Debug",cappAppRoot, tree, null);
		cappAppRootDebug.type="Resources";
		cappAppRootDebug.uidStr = "Resources Debug";
		cappAppRootDebug.setPath("%XAPP%/resources-debug.json");
		
		ResourceTreeItem cappAppRootRelease = createDummyTreeItem("Release",cappAppRoot, tree, null);
		cappAppRootRelease.type="Resources";
		cappAppRootRelease.uidStr = "Resources Release";
		cappAppRootRelease.setPath("%XAPP%/resources-release.json");
		
		
		//cappAppRoot.setId("CXAPP");
		
		
		//defaultItem.setPath("%XAPP%/");
		
		return tree;
	}
	public static ResourceTreeItem createDummyTreeItem(
			String title,
			ContentTreeItem parentItem,
			ContentTree tree,
			ArrayList<ContentTreeItem> dstArray
			)
	{
		ResourceTreeItem result = createTreeItemEx(
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
	public static ResourceTreeItem createTreeItemEx(
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
		ResourceTreeItem result = new ResourceTreeItem();
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
}
