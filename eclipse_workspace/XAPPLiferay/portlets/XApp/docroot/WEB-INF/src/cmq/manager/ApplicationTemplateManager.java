package cmq.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pmedia.utils.StringUtils;

import cmq.types.ATNavType;
import cmq.types.ATNavigationSettings;
import cmq.types.ApplicationTemplate;
import cmx.types.TemplateInfo;
import cmx.types.TemplateInfoPage;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * 
 * @author admin
 *
 */
public class ApplicationTemplateManager 
{
		
	public static ATNavigationSettings createNavSettings(String title,Boolean enabled,String type)
	{
		ATNavigationSettings result  = new ATNavigationSettings();
		result.title=title;
		
		
		return result;
		
	}
	public static ApplicationTemplate createJSONDummy(String templateIdentifier,String path)
	{
		
		File pathO = new File(path);
		if(!pathO.exists()){
			pathO.mkdirs();
		}
		
		ApplicationTemplate result = createDefault(templateIdentifier,templateIdentifier);
		
		result.applicationTemplateIdentifier=templateIdentifier;
		
		result.applicationTemplateTitle=templateIdentifier;
		
		result.applicationTemplateCSS=templateIdentifier+".css";
		
		
		JSONSerializer serializer = new JSONSerializer();
		String serialized = serializer.deepSerialize(result);
		try {
			StringUtils.writeToFile(serialized, path+"/templateInfo.json");
		}catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static ApplicationTemplate getApplicationTemplateFromPath(String templateIdentifier,String path,String platform)
	{
		ApplicationTemplate result = new ApplicationTemplate();
	
		
		String jsonPath = path+"/templateInfo.json";
		String json=null;
		File jsonFile = new File(jsonPath);
		if(!jsonFile.exists())
		{
			result=createJSONDummy(templateIdentifier,path);
			return result;
		}
		
		
		String description=null;
		try {
			description = StringUtils.readFileAsString(path+"/description.html");
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return null;
		}
		
		if(description==null || description.length()==0){
			//return null;
		}
		
		//result.setTitle(title);
		result.setDescription(description);
		
		
		
		JSONDeserializer<ApplicationTemplate>deserializer= new JSONDeserializer();
		
		try {
			 json = StringUtils.readFileAsString(jsonPath);
		} catch (IOException e) {
			//System.out.println("application : " + jsonPath+ " doesnt exists");
		}
		
		if(json!=null && json.length() > 0)
		{
			result=  deserializer.deserialize(json);
		}
		
		//result.setPlatform(platform);
		//updatePaths(result, platform);
		return result;
		
	}
	public static ApplicationTemplate createDefault(String title,String identifier)
	{
		ApplicationTemplate result = new ApplicationTemplate();
		
		result.templateIdentifier=identifier;
		
		result.applicationTemplateTitle=identifier;
		
		result.navSettings=new ArrayList<ATNavigationSettings>();
		
		result.iconSet="Mono";
		
		result.applicationTemplatePlatform=pmedia.types.PlatformGroup.IPHONE_NATIVE;
		
		/***
		 * 	App Navigation Settings
		 */
		
		ATNavigationSettings news = createNavSettings("News", true, ATNavType.NEWS);
		ATNavigationSettings places = createNavSettings("Places", true, ATNavType.PLACES);
		ATNavigationSettings events = createNavSettings("Events", true, ATNavType.EVENTS);
		
		result.navSettings.add(news);
		result.navSettings.add(places);
		result.navSettings.add(events);
		
		return result;
	}
	
	public static ArrayList<ApplicationTemplate> getApplicationTemplates(String lang)
	{
		ArrayList<ApplicationTemplate> result = new ArrayList<ApplicationTemplate>();
		
		String rootPath = System.getProperty("ApplicationTemplateStoreRoot");
		File rootPathO = new File(rootPath);
		if(rootPathO.exists() && rootPathO.isDirectory())
		{
			 File[] files = rootPathO.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		String absDir = _file.getAbsolutePath();
				 		String appFile = absDir + "/templateInfo.json";
				 		File appFileObject = new File(appFile);
				 		if(appFileObject.exists())
				 		{
				 			ApplicationTemplate template = getApplicationTemplate("","", "", _file.getName());
				 			if(template!=null)
				 			{
				 				//load description
				 				String path = rootPath + lang + "/description.html";
				 				String data = null ; 
				 				
				 				try {
									data = StringUtils.readFileAsString(path);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
				 				
				 				if(data==null){
				 					path = rootPath + _file.getName() + "/description.html";
				 					try {
										data = StringUtils.readFileAsString(path);
									} catch (IOException e) {

									}
				 					
				 				}
				 				if(data!=null)
				 				{
				 					//template.description=data;
				 				}
				 				result.add(template);
				 			}
				 		}
				 	}
			 }
		}
		return result;
	}
	
	public static ApplicationTemplate getApplicationTemplate(String uuid,
			String appIdentfier,
			String platform,
			String applicationTemplateIdentifier)
	{
		String path = System.getProperty("ApplicationTemplateStoreRoot") + applicationTemplateIdentifier +"/";
		
		ApplicationTemplate template = getApplicationTemplateFromPath(applicationTemplateIdentifier,path, platform);
		
		return template;
		
	}
	
}

