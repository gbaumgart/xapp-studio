package cmx.tools;

import java.util.ArrayList;

import pmedia.utils.CIGroupTools;
import pmedia.utils.CITools;
import pmedia.utils.ECMChainTypeTools;
import pmedia.utils.ECMLayoutTypeTools;
import pmedia.utils.ECMTypeTools;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.CIGroupType;
import cmx.types.CINames;
import cmx.types.CIType;
import cmx.types.ConfigurableInformation;
import cmx.types.ECMCainType;
import cmx.types.ECMLayoutType;
import cmx.types.ECMLinkableItem;
import cmx.types.ECMType;
import cmx.types.Page;

public class CIUpgrader {

	
	public static void addDataOptions(ApplicationManager appMan,Application app,String pageType)
	{
		
		ArrayList<Page> rootPages= app.getPageByType(pageType);
		ArrayList<ConfigurableInformation>dsCIs=CITools.getAllDataSourceCI(rootPages);
		
		int maxp = dsCIs.size();

		for(int i = 0  ; i  <  maxp ; i++)	
		{
			ConfigurableInformation  dsci = dsCIs.get(i);
			
			ConfigurableInformation  offlineCI = dsci.getParamByName(CINames.OfflineContent);
			if(offlineCI==null)
			{
				//ConfigurableInformation TypedCI(CIType type,String name,String value,int id,CIGroupType gType)
				//int newCIID = app._createCIUID(rootPages);
			//	offlineCI = CIFactory.TypedCI(CIType.BOOL, CINames.OfflineContent, String.valueOf(false) ,app._createNewElementUID(rootPages),CIGroupType.Data);
			////	offlineCI.setUid(Integer.parseInt(offlineCI.getId()));
			//	offlineCI.setVisible(true);
			//	dsci.addParam(offlineCI);
			}
		}
		
		
		
	}
	
	public static ConfigurableInformation TypedCI(CIType type,String name,String value,int id)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setDescription(name);
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(value);
		res.setType(CITools.CIToInteger(type));
		res.setId(String.valueOf(id));
		return res;
	}
	
	public static ConfigurableInformation TypedCI(CIType type,String name,String value,int id,CIGroupType gType)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setDescription(name);
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(value);
		res.setGroup(CIGroupTools.CIGroupToInteger(gType));
		res.setType(CITools.CIToInteger(type));
		res.setId(String.valueOf(id));
		return res;
	}
	
	public static ConfigurableInformation SimpleStringCI(String name,String value, int id)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setDescription(name);
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(value);
		res.setType(CITools.CIToInteger(CIType.STRING));
		res.setId(String.valueOf(id));
		return res;
	}
		
	public static void addBasePropertiesForType(ECMLinkableItem linkType,ArrayList<ConfigurableInformation>dst,Application app,String parentID)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		
		
		res.setDescription("Title");
		res.setName("Title");
		res.setValue("Title");
		res.setType(CITools.CIToInteger(CIType.STRING));
		
		res.setId(String.valueOf(app._createNewElementUID(res)));
		//res.setUid(app._createCIUID(res));
		
		res.setChainType(ECMChainTypeTools.toInteger(ECMCainType.INPUTS));
		
		
		res.setParentId(parentID);
		res.setVisible(true);
		
		//contentSource.setVisible(true);
		dst.add(res);
		
		
		
		res = new ConfigurableInformation();
		
		res.setDescription("Icon");
		res.setName("Icon");
		res.setValue(null);
		res.setType(CITools.CIToInteger(CIType.ICON));
		res.setChainType(ECMChainTypeTools.toInteger(ECMCainType.INPUTS));
		res.setParentId(parentID);
		res.setVisible(true);
		res.setId(String.valueOf(app._createNewElementUID(res)));
		
		//res.setUid(app._createCIUID(res));
		dst.add(res);
		//return res;
	}

}
