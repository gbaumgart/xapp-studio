

package cmx.action;

import java.util.ArrayList;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.annotations.SMDMethod;

import pmedia.utils.CITools;
import pmedia.utils.SecurityUtil;

import xappconnect.Utils.CustomTypeUtils;
import xappconnect.manager.XAppConnectManager;
import xappconnect.types.CustomType;
import cmx.tools.CustomTypeTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ConfigurableInformation;
import cmx.types.CustomTypeTree;

import com.opensymphony.xwork2.Action;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class XAppConnectAction
   extends CMBaseAction implements ServletRequestAware,ServletResponseAware
{
	
	public ApplicationManager getApplicationManager(String identifier)
	{
		appManager = ApplicationManager.getInstance();
		return appManager;
	}
	public ConfigurableInformation getItemFromJSON(String itemJSON)
	{
		
		JSONSerializer serializer = new JSONSerializer();
		ConfigurableInformation item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<ConfigurableInformation>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			item= (ConfigurableInformation) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
	
			}
    	}
    	return item;
	}
	
	@SMDMethod
	public void itemChanged(String rtConfig,String platform,String scope,String appIdentfier,String uuid,String itemJSON,String itemIdentifier) throws java.lang.Exception
	{
		if(!SecurityUtil.isValidAppAction(uuid, appIdentfier, getRequest()))
		{
			return;
		}
		
		
		boolean isValid=true;
		if(scope!=null && scope.equals("%XASWEB%/ctypes/"))
		{
			Boolean isValidAdminAction = SecurityUtil.isValidAdminAction(uuid, appIdentfier, getRequest());
			String ok = isValidAdminAction.toString();
			if(ok.equals("false"))
			{
				isValid=false;
			}
		}
		
		if(!isValid){
			return;
		}
		
		ConfigurableInformation newCi = getItemFromJSON(itemJSON);
		
		CustomType type= CustomTypeUtils.loadType(rtConfig, platform, uuid, appIdentfier, itemIdentifier, scope);
		
		if(type==null)
		{
			return;
		}
		
		ConfigurableInformation realCI = type.getByReferenceCI(newCi);
		
		if(realCI==null){
			System.out.println("couldnt find ori CI : " + newCi.getId());
			return;
		}
		
		/***
		 * Name changed !
		 */
		if(realCI.getName().equals("name"))
		{
			if(!realCI.getValue().equals(newCi.getValue()))
			{
				CustomTypeUtils.deleteType(type, rtConfig, platform, scope, uuid, appIdentfier);
			}
		}
		
		
		
		realCI.setValue(newCi.getValue());
		CustomTypeUtils.saveType(type,rtConfig,platform,scope,uuid,appIdentfier,true);
		
		if(application!=null){
			application.flushCache=1;
		}
		
		/*
		appManager  = getApplicationManager(appIdentfier);
		if(uuid==null)
		{
			throw new java.lang.Exception("no uuid");
		}

		application= appManager.getApplication(uuid,appIdentfier,false);

		ConfigurableInformation newCi = getItemFromJSON(itemJSON);
		
		if(application!=null)
		{
			ConfigurableInformation updatedItem=null;
			
			ConfigurableInformation oriCi = application.getItem(itemJSON);
			
			if(newCi!=null && oriCi!=null)
			{				
					//newCi =checkForAssetModification(appManager, application, oriCi,newCi);
			
			}
			updatedItem  = application.itemChanged(newCi);
			//appManager.saveApplication(application);
		}		
		*/
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9097347805333543588L;

	@SMDMethod
	public CustomTypeTree getTypesTree(String rtConfig , String platform ,String uuid,String appIdentfier,String scope,String flags)
	{
		CustomTypeTree result = CustomTypeTreeFactory.getTypesTree(rtConfig, platform, uuid, appIdentfier, scope, flags);
		return result;
	}
	
	@SMDMethod
	public ArrayList<CustomType> getTypes(String rtConfig , String platform ,String uuid,String appIdentfier,String scope)
	{
		return XAppConnectManager.getTypes(rtConfig, platform, uuid, appIdentfier, scope);
	}
	
	/*
	rtConfig,platform,scope,
    name,isFolder,urlSchema,clientDriverClass,
    */
	
	@SMDMethod
	public CustomType createNew( 
			String rtConfig , String platform , String scope, 
			String parent,String name, Boolean isFolder,String urlSchema,String clientDriverClass,
			String uuid,String appIdentfier)
	{
		boolean isValid =SecurityUtil.isValidAdminAction(uuid, appIdentfier, getRequest()); 
		if(!isValid)
		{
			return null;
		}
		//CustomTypeTree result = CustomTypeTreeFactory.getTypesTree(rtConfig, platform, uuid, applicationIdentifier, scope, flags);

		
		return XAppConnectManager.createNew(rtConfig, platform, scope, parent,name, isFolder, urlSchema, clientDriverClass, uuid, appIdentfier);
	}
	
	private ApplicationManager appManager=null;
	public Application application=null;

	@SMDMethod
    
	public String smd() {
        return Action.SUCCESS;
    }
	

}
