package cmx.tools;

import java.util.ArrayList;

import pmedia.utils.CIGroupTools;
import pmedia.utils.CITools;
import pmedia.utils.ECMChainTypeTools;
import pmedia.utils.ECMLayoutTypeTools;
import pmedia.utils.ECMTypeTools;
import cmx.types.Application;
import cmx.types.CIGroupType;
import cmx.types.CIType;
import cmx.types.ConfigurableInformation;
import cmx.types.ECMCainType;
import cmx.types.ECMLayoutType;
import cmx.types.ECMLinkableItem;
import cmx.types.ECMType;
import cmx.types.Page;

public class CIFactory {

	
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
	
	public static ConfigurableInformation StructuredCI(String name,String value,int id)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setDescription(name);
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(value);
		res.setType(CITools.CIToInteger(CIType.STRUCTURE));
		res.setId(String.valueOf(id));
		res.params = new ArrayList<ConfigurableInformation>();
		return res;
	}
	
	public static ConfigurableInformation TypedCI(CIType type,String name,String value,int id,CIGroupType gType)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setDescription(name);
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(value);
		res.setTitle(name);
		res.setGroup(CIGroupTools.CIGroupToInteger(gType));
		res.setType(CITools.CIToInteger(type));
		res.setId(String.valueOf(id));
		res.setUid(Integer.parseInt(res.getId()));
		return res;
	}
	
	public static ConfigurableInformation AppMetaDataCI(CIType type,String name,String value,String displayTitle)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(value);
		res.setTitle(displayTitle);
		res.setType(CITools.CIToInteger(type));
		res.setId(name);
		res.setVisible(true);
		res.setStoreDestination("metaDataStore");
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
	
	public static ConfigurableInformation SimpleBoolean(String name,Boolean value, int id)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		res.setDescription(name);
		res.setName(name);
		res.setEnumType(-1);
		res.setValue(Boolean.toString(value));
		res.setType(CITools.CIToInteger(CIType.BOOL));
		res.setId(String.valueOf(id));
		return res;
	}
		
	public static void addBasePropertiesForType(ECMLinkableItem linkType,ArrayList<ConfigurableInformation>dst,Application app,String parentID)
	{
		ConfigurableInformation res = new ConfigurableInformation();
		
		
		res.setDescription("Title");
		res.setName("Title");
		res.setValue("MyTitle");
		res.setType(CITools.CIToInteger(CIType.STRING));
		
		res.setId(String.valueOf(app._createNewElementUID(res)));
		
		res.setUid(Integer.parseInt(res.getId()));
		
		res.setChainType(ECMChainTypeTools.toInteger(ECMCainType.INPUTS));

		res.setParentId(parentID);
		res.setVisible(true);
		dst.add(res);
		
		
		
		res = new ConfigurableInformation();
		res.setDescription("Icon");
		res.setName("Icon");
		res.setTitle("Icon");
		
		res.setValue(null);
		
		res.setType(CITools.CIToInteger(CIType.ICON));
		res.setChainType(ECMChainTypeTools.toInteger(ECMCainType.INPUTS));
		res.setParentId(parentID);
		res.setVisible(true);
		
		res.setId(String.valueOf(app._createNewElementUID(res)));
		res.setUid(Integer.parseInt(res.getId()));
		
		dst.add(res);
		//return res;
	}

}
