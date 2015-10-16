package cmx.types;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.BaseDataArrayTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.types.ApplicationConfiguration;
import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.types.BaseData;
import pmedia.types.CIOverride;
import pmedia.utils.CITools;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.ECMLayoutTypeTools;
import pmedia.utils.ECMTypeTools;
import cmx.tools.ApplicationMetaDataFactory;
import cmx.tools.CIFactory;
import cmx.tools.CIUpgrader;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class Application {
	
	int isQXApp;
	
	int isNew=1;
	
	public String title;
	public String iconUrl;
	public String styleVersion="1";
	public int flushCache=1;
	ArrayList<XCDatasourceInfo>xcDataSourceInfos;
	
	
	public String getStyleVersion() {
		return styleVersion;
	}
	public void setStyleVersion(String styleVersion) {
		this.styleVersion = styleVersion;
	}
	public String platform="IPHONE_NATIVE";
	public String runTimeConfiguration="Release";
	public ArrayList<DataSourceBase>dataSources=new ArrayList<DataSourceBase>();
	public ArrayList<pmedia.types.CIOverride>ciOverrides=new ArrayList<pmedia.types.CIOverride>();
	public ApplicationMetaData metaData;
	public ApplicationMetaData getMetaData() {
		return metaData;
	}
	public void setMetaData(ApplicationMetaData metaData) {
		this.metaData = metaData;
	}
	public ArrayList<pmedia.types.CIOverride> getCiOverrides() {
		return ciOverrides;
	}
	public void setCiOverrides(ArrayList<pmedia.types.CIOverride> ciOverrides) {
		this.ciOverrides = ciOverrides;
	}
	public String userIdentifier;
	public String applicationIdentifier;
	public int elementSeq=0;

	public int ciSeq=0;
	
	private ApplicationConfiguration appSettings=null;
	
	public void addDataSourcesToItems(){
		for(int i = 0  ; i  <  dataSources.size() ; i++)	
		{
			DataSourceBase ds = dataSources.get(i);
			
		}
	}
	
	public DataSourceBase getDataSource(String uid)
	{
		for(int i = 0  ; i  <  dataSources.size() ; i++)	{
			DataSourceBase ds = dataSources.get(i);
			if(ds.getUid().equals(uid))
				return ds;
			
		}
		return null;
	}
	public DataSourceBase getDataSource(String host,String db)
	{
		for(int i = 0  ; i  <  dataSources.size() ; i++)	{
			DataSourceBase ds = dataSources.get(i);
			if(ds.getHost()!=null && ds.getHost().equals(host) && ds.getDatabase()!=null && ds.getDatabase().equals(db))
				return ds;
		}
		return null;
	}
	
	public DataSourceBase getDataSourceByHost(String host)
	{
		for(int i = 0  ; i  <  dataSources.size() ; i++)	{
			DataSourceBase ds = dataSources.get(i);
			if( ds.getHost()!=null && ds.getHost().equals(host))
				return ds;
		}
		return null;
	}
	public void addDataSource(DataSourceBase dataSource)
	{
		//SQLDataSource ds = getDataSource(dataSource.getUid());
		if(dataSource.getUid()==null){
			dataSource.setUid(UUID.randomUUID().toString());
		}
		this.getDataSources().add(dataSource);
	
	}
	public int _createNewElementUID(Object ci)
	{
		if(ci==null)
			return -1;
		
		int res = elementSeq;
		elementSeq++;
		return res;
	}
	
	/*
	public int _createCIUID(Object ci)
	{
		if(ci==null)
			return -1;
	
		int res = ciSeq;
		ciSeq++;
		return res;
	}
	*/
	
	public String label="name";
	
	public Page getElementById(int id)
	{
		for(int i = 0  ; i  <  items.size() ; i++)	{
			
			Page  p = items.get(i) ;
			if(p.getUid()==id)
			{
				return p;
			}
		}
		return null;
	}
	
	public Page getElementById(String id)
	{
		for(int i = 0  ; i  <  items.size() ; i++)	{
			
			Page  p = items.get(i) ;
			if(p.getId().equals(id))
			{
				return p;
			}
		}
		return null;
	}
	
	public void upgradeApplication(ApplicationManager appMan)
	{
		
		if(getMetaData()==null)
		{
			setMetaData(new ApplicationMetaData());
		}
		
		ApplicationMetaDataFactory.setDefaults(getMetaData(),getApplicationIdentifier());
		
		DataSourceBase sqlDataSource = getDataSource("Liferay");
		if(sqlDataSource==null)
		{
			
			sqlDataSource = new SQLDataSource();
			sqlDataSource.setHost("127.0.0.1");
			sqlDataSource.setPrefix(null);
			sqlDataSource.setDatabase(null);
			sqlDataSource.setPassword(null);
			sqlDataSource.setUser(getUserIdentifier());
			sqlDataSource.setType("Liferay");
			sqlDataSource.setVersion("6.1");
			sqlDataSource.setUrl("127.0.0.1");
			sqlDataSource.setUid("Liferay");
			addDataSource(sqlDataSource);
		}
		
		ArrayList<Page> subPages= getSubPages("0");
		if(subPages!=null && subPages.size()>0)
		{
			int cOrder=0;
		
			for(int pi = 0  ; pi  <  subPages.size() ; pi++)	
			{
				Page  p = subPages.get(pi);
				ConfigurableInformation ciData= p.getItemByChainAndName(0,CINames.CONTENT_SOURCE);
				if(ciData != null)
				{
					if(ciData.order==-1)
					{
						ciData.order=cOrder;
					}
				}
				cOrder++;
				
				/***
				 * sub sub 
				 */
				
				ArrayList<Page> pages= getSubPages("" + p.uid);
				int sorder=0;
				for(int si = 0  ; si  <  pages.size() ; si++)	
				{
					Page  sp = pages.get(si);
					ConfigurableInformation sciData= sp.getItemByChainAndName(0,CINames.CONTENT_SOURCE);
					if(sciData != null)
					{
						if(sciData.order==-1)
						{
							sciData.order=sorder;
						}
					}
					sorder++;
				}
				
			}
		}
		
		ArrayList<Page> rootPages= getPageByType("page");
		int maxp = rootPages.size();
		
		
		
		
		
		
		for(int i = 0  ; i  <  maxp ; i++)	
		{
			Page  p = rootPages.get(i) ;
			
			
			
			
			
			ConfigurableInformation ciBanner= p.getItemByChainAndName(0,CINames.BannerSource);
			if(ciBanner == null)
			{
				
				ConfigurableInformation contentSource = new ConfigurableInformation();
				contentSource.setDescription(CINames.BannerSource);
				contentSource.setName(CINames.BannerSource);
				contentSource.setTitle("");
				contentSource.setEnumType(ECMTypeTools.ToInteger(ECMType.BannerSource));
				//contentSource.setValue(((Integer)ECMBannerSourceTypeTools.TypeToInteger(ECMBannerSourceType.Unknown)).toString());
				contentSource.setValue(((Integer)ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown)).toString());
				contentSource.setType(CITools.CIToInteger(CIType.BANNER));
				contentSource.setId(String.valueOf(this._createNewElementUID(contentSource)));
				contentSource.setVisible(true);
				contentSource.setDataRef("unset");
				contentSource.setUid(_createNewElementUID(contentSource));
				//contentSource.setUid(_createCIUID(contentSource));
				contentSource.setParentId(p.getId());
				p.getInputs().add(contentSource);
			}
			
			ciBanner = null;
			ciBanner= p.getItemByChainAndName(0,CINames.BannerSource2);
			if(ciBanner == null)
			{
				
				ConfigurableInformation contentSource = new ConfigurableInformation();
				contentSource.setDescription(CINames.BannerSource2);
				contentSource.setName(CINames.BannerSource2);
				contentSource.setTitle("");
				contentSource.setEnumType(ECMTypeTools.ToInteger(ECMType.BannerSource2));
				//contentSource.setValue(((Integer)ECMBannerSourceTypeTools.TypeToInteger(ECMBannerSourceType.Unknown)).toString());
				contentSource.setValue(((Integer)ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown)).toString());
				contentSource.setType(CITools.CIToInteger(CIType.BANNER2));
				contentSource.setId(String.valueOf(this._createNewElementUID(contentSource)));
				contentSource.setVisible(true);
				contentSource.setDataRef("unset");
				contentSource.setUid(_createNewElementUID(contentSource));
				contentSource.setParentId(p.getId());
				p.getInputs().add(contentSource);
			}
			
			ConfigurableInformation ciLogo= p.getItemByChainAndName(0,CINames.Logo);
			if(ciLogo == null)
			{
				
				ciLogo = new ConfigurableInformation();
				ciLogo.setDescription(CINames.Logo);
				ciLogo.setName(CINames.Logo);
				ciLogo.setTitle("");
				//contentSource.setEnumType(ECMTypeTools.ToInteger(ECMType.BannerSource));
				//contentSource.setValue(((Integer)ECMBannerSourceTypeTools.TypeToInteger(ECMBannerSourceType.Unknown)).toString());
				ciLogo.setValue(null);
				ciLogo.setType(CITools.CIToInteger(CIType.LOGO));
				ciLogo.setId(String.valueOf(this._createNewElementUID(ciLogo)));
				ciLogo.setVisible(true);
				ciLogo.setDataRef("unset");
				ciLogo.setUid(_createNewElementUID(ciLogo));
				ciLogo.setParentId(p.getId());
				p.getInputs().add(ciLogo);
			}
		}
		//CIUpgrader.addDataOptions(appMan, this,"page");
		//CIUpgrader.addDataOptions(appMan, this,"view");
		
		appMan.saveApplication(this);
		
		
	}
	public ArrayList<Page> getPageByType(String type)
	{
		ArrayList<Page> result = new ArrayList<Page>();
		for(int i = 0  ; i  <  items.size() ; i++)	{
			
			Page  p = items.get(i) ;
			if(p.getType().equals(type))
			{
				result.add(p);
			}
		}
		return result;
	}
	
	int getLastOrderFromPage(Page page)
	{
		int max = 0;
		
		return max;
	}
	public ArrayList<Page> getSubPages(String parent)
	{
		ArrayList<Page> result = new ArrayList<Page>();
		
		for(int i = 0  ; i  <  items.size() ; i++)	{
			
			Page  p = items.get(i) ;
			if(p.parent !=null && p.parent.equals(parent))
			{
				result.add(p);
			}
		}
		return result;
	}
	
	public Page getElementByRealId(String id)
	{
		for(int i = 0  ; i  <  items.size() ; i++)	{
			
			Page  p = items.get(i) ;
			if(p.getId().equals(id))
			{
				return p;
			}
		}
		return null;
	}


	public Page itemDeleted(String itemJSON){
		
		JSONSerializer serializer = new JSONSerializer();
		Page page= null;
		Page newPage =null;
		
		JSONDeserializer derializerSC = new JSONDeserializer<Page>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			page= (Page) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	
    	if(page!=null)
    	{
    		
    		Page element = getElementById(page.getUid());
    		if(element!=null)
    		{
    			Page parentPage = getElementById( Integer.parseInt(page.getParent()));
    			if(parentPage!=null)
    			{
    				/****
    				 * find sub pages : 
    				 */
    				ArrayList<Page> subPages= getSubPages("" +element.uid);
    				if(subPages!=null && subPages.size()>0)
    				{
    					for(int pi = 0  ; pi  <  subPages.size() ; pi++)	
    					{
    						Page subPage = subPages.get(pi);
    						items.remove(subPage);
    					}
    				}
    				
    				
    				parentPage.deleteSubPage(element);
    				items.remove(element);
    			}
    		}
    	}
    	
    	return newPage;
	}
	

	public Page createNewPage(String itemJSON)
	{
		Page page= null;
		Page newPage =null;
		@SuppressWarnings("rawtypes")
		JSONDeserializer derializerSC = new JSONDeserializer<Page>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			page= (Page) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	
    	if(page!=null)
    	{
    		Page element = getElementById(page.getUid());
    		if(element!=null)
    		{
    			newPage = generateNewPage(element);
    		}
    	}
    	
    	return newPage;
	}
	
	public Page createNewPageEx(String itemJSON,String newTitle,String dataRef,int contentSourceType,String dataSourceUID)
	{
		Page page= null;
		Page newPage =null;
		@SuppressWarnings("rawtypes")
		JSONDeserializer derializerSC = new JSONDeserializer<Page>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			page= (Page) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	
    	if(page!=null)
    	{
    		Page element = getElementById(page.getUid());
    		if(element!=null)
    		{
    			newPage = generateNewPage(element);
    			if(newPage!=null)
    			{
    				
    				ConfigurableInformation ciTitle = newPage.getItemByChainAndName(0, "Title");
    				ciTitle.setValue(newTitle);
    				
    				ConfigurableInformation contentSource = newPage.getItemByChainAndName(0, "Content Source");
    				contentSource.setDataRef(dataRef);
    				contentSource.setDataSource(dataSourceUID);
    				String csType = "";
    				try {
						csType = String.valueOf(contentSourceType);
					} catch (Exception e) {
						// TODO: handle exception
					}
    				contentSource.setValue(csType);
    				
    			}
    		}
    	}
    	
    	return newPage;
	}
	
	public Page itemAdded(String itemJSON){
		
		JSONSerializer serializer = new JSONSerializer();
		Page page= null;
		Page newPage =null;
    	//System.out.println("item changed :   " + itemJSON + "\n : " + t);
    	//searchConfigurationStr=  ""
    	//derializerSC.use("searchSources.values", PMSearchFieldTypes.class);
    	
		
		JSONDeserializer derializerSC = new JSONDeserializer<ConfigurableInformation>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		//System.out.println(searchConfigurationStr);
    		try {
    			page= (Page) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	
    	if(page!=null)
    	{
    		Page element = getElementById( Integer.parseInt(page.getParent()));
    		if(element!=null)
    		{
    			newPage = generateNewPage(element);
    		}
    	}
    	
    	return newPage;
	}
	public String getNewPageId(Page parentPage)
	{
		String prefix = "";
		
		ConfigurableInformation ciLayout = parentPage.getItemByChainAndName(0, "Layout");
		if(ciLayout!=null)
		{
			int ciLayoutValueIcons = ECMLayoutTypeTools.ECMLayoutTypeToInteger(ECMLayoutType.Icons);
			if(ciLayout.value.equals(String.valueOf(ECMLayoutTypeTools.ECMLayoutTypeToInteger(ECMLayoutType.Icons))))
			{
				prefix = "icon";
			}
			
			if(ciLayout.value.equals(String.valueOf(ECMLayoutTypeTools.ECMLayoutTypeToInteger(ECMLayoutType.BottomTabs))))
			{
				prefix = "tab";
			}
		}
		return prefix + _createNewElementUID(this);
	}
	public Page generateNewPage(Page parentPage)
	{
		
		String name = getNewPageId(parentPage);
		Page subPage = new Page();
		subPage.setType("view");
		subPage.setName(name);
		subPage.setLabel(name);
		
		subPage.setId(name);
		
		
		subPage.setParent( String.valueOf(parentPage.getUid()));
		subPage.setUid(this._createNewElementUID(this));
		items.add(subPage);
		
		CIFactory.addBasePropertiesForType(ECMLinkableItem.Tab, subPage.getInputs(),this,subPage.getId());
		
		
		ConfigurableInformation contentSource = new ConfigurableInformation();
		contentSource.setDescription("Content Source");
		contentSource.setName("Content Source");
		contentSource.setTitle(getNewPageId(parentPage));
		contentSource.setEnumType(ECMTypeTools.ToInteger(ECMType.ContentSource));
		contentSource.setValue(((Integer)ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown)).toString());
		contentSource.setType(CITools.CIToInteger(CIType.ENUMERATION));
		contentSource.setId(String.valueOf(this._createNewElementUID(contentSource)));
		contentSource.setVisible(true);
		contentSource.setDataRef("unset");
		contentSource.setUid(Integer.parseInt(contentSource.getId()));
		contentSource.setParentId(subPage.getId());
		
		//ConfigurableInformation offlineCI = CIFactory.TypedCI(CIType.BOOL, CINames.OfflineContent, String.valueOf(false) ,_createNewElementUID(contentSource),CIGroupType.Data);
		//offlineCI.setVisible(true);
		//contentSource.addParam(offlineCI);
		subPage.getInputs().add(contentSource);
		
		Reference ref1 = new Reference(name);
		parentPage.addRef(ref1);
		
		ArrayList<Page> subPages= getSubPages(""+parentPage.uid);
		if(subPages!=null)
		{
			contentSource.order=subPages.size();
		}
		return subPage;
		
	}
	

	public ConfigurableInformation getItem(String itemJSON){
		
		
		JSONSerializer serializer = new JSONSerializer();
		ConfigurableInformation item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<ConfigurableInformation>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		try {
    			item= (ConfigurableInformation) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	ConfigurableInformation updatedItem = null;
    	if(item!=null)
    	{
    		Page element = getElementById(item.getParentId());
    		if(element!=null)
    		{
    			updatedItem=element.getItem(item);
    			if(updatedItem!=null)
    			{
    				return updatedItem;
    			}
    		}
    	}
    	return null;
	}

	
	public ArrayList<BaseData>getDataListByType(ApplicationManager appManager,ECMContentSourceType type,DataSourceBase dataSource)
	{
		@SuppressWarnings("rawtypes")
		ArrayList result = null; 
		String dbPath = appManager.getDataPath(this,type,dataSource);
		switch (type) {
			case JoomlaArticle:
			{
				result = ServerCache.getInstance().getDC(getApplicationIdentifier()).jArticles;
				if(result==null)
				{
					if(dataSource.getVersion().equals("1.7")){
						try {
							result= ArticleTools.readJArticlesFromFile(dbPath);
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else{
						try {
							result = ArticleTools.readJArticlesFromFile(dbPath);
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) 
						{
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					ServerCache.getInstance().getDC(getApplicationIdentifier()).jArticles=result;
				}
				break;
			}
			case JoomlaSection:
			{
				result = ServerCache.getInstance().getDC(getApplicationIdentifier()).articleSections;
				//result = null; 
				if(result==null)
				{
					if(dataSource.getVersion().equals("1.7")){
						try {
							result= CategoryTools.readFromFile17(dbPath, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection));
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else{
						try {
							result = CategoryTools.readFromFile(dbPath, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaSection));
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					ServerCache.getInstance().getDC(getApplicationIdentifier()).articleSections=result;
				}
				
				break;
			}
			case JoomlaCategory:
			{
				result = ServerCache.getInstance().getDC(getApplicationIdentifier()).articleCategories;
				if(result==null)
				{
					if(dataSource.getVersion().equals("1.7")){
						try {
							result= CategoryTools.readFromFile17(dbPath, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory));
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						try {
							result = CategoryTools.readFromFile(dbPath, "com_content",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaCategory));
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					ServerCache.getInstance().getDC(getApplicationIdentifier()).articleCategories=result;
				}
				
				break;
			}
			case JoomlaBannerCategory:
			{
				result = ServerCache.getInstance().getDC(getApplicationIdentifier()).joomlaBannerCategories;
				if(result==null)
				{
						try {
							if(dataSource.getVersion().equals("1.7")){
								result= CategoryTools.readFromFile17(dbPath, "com_banners",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory));
							}else{
								result= CategoryTools.readFromFile(dbPath, "com_banner",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JoomlaBannerCategory));
							}
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					ServerCache.getInstance().getDC(getApplicationIdentifier()).joomlaBannerCategories=result;
				break;
			}
			case MosetTreeCategory:
			{
				result = ServerCache.getInstance().getDC(getApplicationIdentifier()).mosetCategories;
				if(result==null){
					try {
						result= CategoryTools.readMosetCategoriesFromFile(dbPath, "com_mtree",ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MosetTreeCategory));
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(result!=null && result.size() > 0){
						ServerCache.getInstance().getDC(getApplicationIdentifier()).mosetCategories=result;
					}
					break;
				}
			}
		}
		
		return result;
	}
	
	
	public BaseData getDataByContentSourceCI(ApplicationManager appManager,ConfigurableInformation contentSourceCI){
		
		BaseData result = null;
		if(contentSourceCI.dataSource!=null)
		{
			ECMContentSourceType  sType = ECMContentSourceTypeTools.FromString(contentSourceCI.value);
			DataSourceBase dataSource = getDataSource(contentSourceCI.dataSource);
			if(dataSource!=null)
			{
				ArrayList<BaseData>dataAll = getDataListByType(appManager,sType,dataSource);
				
				if(dataAll !=null && dataAll.size() > 0)
				{
					result = BaseDataArrayTools.getByIndex(dataAll, Integer.valueOf(contentSourceCI.dataRef));
				}
			}else{
				System.out.println("could not extract SQLDataSource from CI :  " + contentSourceCI.dataSource);
			}
		}
		return result;
		
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

	
	public CIOverride getCIOverride(int id)
	{
		for(int i = 0  ; i  <  ciOverrides.size() ; i++)	
		{			
			CIOverride _co=ciOverrides.get(i);
			if(_co.getId()==id){
				return _co;
			}
		}
		return null;
	}
	
	public CIOverride updateCIOverride(String itemJSON)
	{
		CIOverride item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<CIOverride>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		try {
    			item= (CIOverride) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;
			}
    	}
    	if(item!=null)
    	{
    		CIOverride _c = getCIOverride(item.getId());
    		if(_c!=null)
    		{
    			_c.setFlags(item.getFlags());
    			_c.setValue(item.getValue());
    			_c.setDataSource(item.getDataSource());
    			_c.setDataRef(item.getDataRef());
    		}
    		return _c;
    	}
    	return null;
	}
	public void deleteCIOverride(String itemJSON)
	{
		CIOverride item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<CIOverride>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		try {
    			item= (CIOverride) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				e.printStackTrace();
				return ;
			}
    	}

    	if(item!=null)
    	{
    		CIOverride _c = getCIOverride(item.getId());
    		if(_c!=null)
    		{    			
    			this.getCiOverrides().remove(_c);
    		}
    		//this.getCiOverrides().add(item);
    		return;
    	}
	}
	
	public CIOverride createCIOverride(String itemJSON)
	{
		CIOverride item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<CIOverride>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		try {
    			item= (CIOverride) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				e.printStackTrace();
				return null;
			}
    	}

    	if(item!=null)
    	{
    		item.setId(this._createNewElementUID(item));
    		//CIOverride _c = getCIOverride(item.getId());
    		this.getCiOverrides().add(item);
    		return item;
    	}
    	
    	//ConfigurableInformation updatedItem = null;
    	/*
    	
    	if(item!=null)
    	{
    		Page element = getElementById(item.getParentId());
    		if(element!=null)
    		{
    			updatedItem =element.updateItem(item);
    			if(updatedItem!=null)
    			{
    				//checkForAssetModification(updatedItem);
    				return updatedItem;
    			}
    		}
    	}
    	*/
    	return null;
	}
	public ConfigurableInformation createCI(String itemJSON)
	{
		ConfigurableInformation item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<ConfigurableInformation>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		try {
    			item= (ConfigurableInformation) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	ConfigurableInformation updatedItem = null;
    	
    	if(item!=null)
    	{
    		Page element = getElementById(item.getParentId());
    		if(element!=null)
    		{
    			updatedItem =element.updateItem(item);
    			if(updatedItem!=null)
    			{
    				//checkForAssetModification(updatedItem);
    				return updatedItem;
    			}
    		}
    	}
    	return null;
	}
	public ConfigurableInformation itemChanged(String itemJSON){
		
		
		ConfigurableInformation item= null;
    	JSONDeserializer derializerSC = new JSONDeserializer<ConfigurableInformation>();
    	if(itemJSON!=null && itemJSON.length() > 0)
    	{
    		try {
    			item= (ConfigurableInformation) derializerSC.deserialize(itemJSON);	
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
    	}
    	
    	ConfigurableInformation updatedItem = null;
    	
    	if(item!=null)
    	{
    		Page element = getElementById(item.getParentId());
    		if(element!=null)
    		{
    			updatedItem =element.updateItem(item);
    			if(updatedItem!=null)
    			{
    				//checkForAssetModification(updatedItem);
    				return updatedItem;
    			}
    		}
    	}
    	return null;
	}

	public ConfigurableInformation itemChanged(ConfigurableInformation item)
	{
		
		ConfigurableInformation updatedItem = null;
    	if(item!=null)
    	{
    		Page element = getElementById(item.getParentId());
    		if(element!=null)
    		{
    			updatedItem =element.updateItem(item);
    			if(updatedItem!=null)
    			{
    				return updatedItem;
    			}
    		}
    	}
    	return null;
	}
	
	public ConfigurableInformation metaDataChanged(ConfigurableInformation item)
	{
		if(item!=null)
    	{
    		
    		ConfigurableInformation oriCI = CITools.getById(getMetaData().getProperties(), item.getName());
    		if(oriCI!=null)
    		{
    			oriCI.setValue(item.value);
    			return oriCI;
    		}
    	}
    	return null;
	}
	public Application() {
		identifier = "id";
		//identifier=null;
		items = new ArrayList<Page>();
		
		Page rootPage = new Page();
		rootPage.setType("page");
		//rootPage.setName
		items.add(rootPage);
		Reference ref = new Reference("tab0");
		rootPage.addRef(ref);
		rootPage.setId(String.valueOf(this._createNewElementUID(rootPage)));
		rootPage.setUid(Integer.parseInt(rootPage.getId()));
		
		//Reference ref1 = new Reference("tab1");
		//rootPage.addRef(ref1);
		
		
		ConfigurableInformation layoutCI = new ConfigurableInformation();

		layoutCI.setDescription("Layout");
		layoutCI.setName("Layout");
		layoutCI.setEnumType(ECMTypeTools.ToInteger(ECMType.Layout));
		layoutCI.setValue(((Integer)ECMLayoutTypeTools.ECMLayoutTypeToInteger(ECMLayoutType.Icons)).toString());
		layoutCI.setType(CITools.CIToInteger(CIType.ENUMERATION));
		layoutCI.setId(String.valueOf(_createNewElementUID(layoutCI)));
		
		//layoutCI.setUid(_createCIUID(layoutCI));
		layoutCI.setParentId(rootPage.getId());
		layoutCI.setVisible(true);
		rootPage.getInputs().add(layoutCI);
		
		ConfigurableInformation backgroundCI = CIFactory.TypedCI(CIType.IMAGE, CINames.BACKGROUND,null,_createNewElementUID(rootPage),CIGroupType.Visual);
		backgroundCI.setVisible(true);
		backgroundCI.setParentId(rootPage.getId());
		//backgroundCI.setUid(_createCIUID(backgroundCI));
		
		
		
		rootPage.getInputs().add(backgroundCI);
		
		
		
		Page subPage = new Page();
		subPage.setType("view");
		subPage.setName("Tab 0");
		subPage.setLabel("Tab 0");
		subPage.setId("tab0");
		subPage.setParent(rootPage.getId());
		subPage.setUid(this._createNewElementUID(subPage));
		items.add(subPage);

		
		CIFactory.addBasePropertiesForType(ECMLinkableItem.Tab, subPage.getInputs(),this,subPage.getId());
		ConfigurableInformation contentSource = new ConfigurableInformation();
		contentSource.setDescription("Content Source");
		contentSource.setName("Content Source");
		contentSource.setTitle("tab0");
		contentSource.setEnumType(ECMTypeTools.ToInteger(ECMType.ContentSource));
		contentSource.setValue(((Integer)ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.Unknown)).toString());
		contentSource.setType(CITools.CIToInteger(CIType.ENUMERATION));
		contentSource.setId(String.valueOf(this._createNewElementUID(contentSource)));
		contentSource.setUid(Integer.parseInt(contentSource.getId()));
		contentSource.setVisible(true);
		contentSource.setDataRef("nuset");
		
		//contentSource.setUid(_createCIUID(contentSource));
		contentSource.setParentId(subPage.getId());
		subPage.getInputs().add(contentSource);
		
	}
	public String identifier;//="ibizamedia";
	
	public ArrayList<Page>items;

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the items
	 */
	public ArrayList<Page> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(ArrayList<Page> items) {
		this.items = items;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}



	/**
	 * @return the elementSeq
	 */
	public int getElementSeq() {
		return elementSeq;
	}

	/**
	 * @param elementSeq the elementSeq to set
	 */
	public void setElementSeq(int elementSeq) {
		this.elementSeq = elementSeq;
	}

	/**
	 * @return the ciSeq
	 */
	public int getCiSeq() {
		return ciSeq;
	}

	/**
	 * @param ciSeq the ciSeq to set
	 */
	public void setciSeq(int _ciSeq) 
	{
		ciSeq = _ciSeq;
	}

	/**
	 * @return the applicationIdentifier
	 */
	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}

	/**
	 * @param applicationIdentifier the applicationIdentifier to set
	 */
	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	/**
	 * @return the userIdentifier
	 */
	public String getUserIdentifier() {
		return userIdentifier;
	}

	/**
	 * @param userIdentifier the userIdentifier to set
	 */
	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	/**
	 * @return the dataSources
	 */
	public ArrayList<DataSourceBase> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(ArrayList<DataSourceBase> dataSources) 
	{
		this.dataSources = dataSources;
	}
	/**
	 * @return the appSettings
	 */
	public ApplicationConfiguration getAppSettings() {
		return appSettings;
	}
	/**
	 * @param appSettings the appSettings to set
	 */
	public void setAppSettings(ApplicationConfiguration appSettings) {
		this.appSettings = appSettings;
		appSettings.setAppIdentifier(getApplicationIdentifier());
		
	}

	public String getRunTimeConfiguration() {
		return runTimeConfiguration;
	}

	public void setRunTimeConfiguration(String runTimeConfiguration) {
		this.runTimeConfiguration = runTimeConfiguration;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public int getIsQXApp() {
		return isQXApp;
	}
	public void setIsQXApp(int isQXApp) {
		this.isQXApp = isQXApp;
	}
	public String getTitle() 
	{
		ApplicationMetaData appMeta = getMetaData();
		if(appMeta!=null){
			ConfigurableInformation _titleCI = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
			if(_titleCI!=null){
				if(_titleCI.getValue()!=null && _titleCI.getValue().length()>0 )
				{
					return _titleCI.getValue();
				}
			}
		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIconUrl() 
	{
		ApplicationMetaData appMeta = getMetaData();
		if(appMeta!=null){
			ConfigurableInformation _titleCI = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
			if(_titleCI!=null){
				if(_titleCI.getValue()!=null && _titleCI.getValue().length()>0 )
				{
					return _titleCI.getValue();
				}
			}
		}
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public ArrayList<XCDatasourceInfo> getXcDataSourceInfos() {
		return xcDataSourceInfos;
	}
	public void setXcDataSourceInfos(ArrayList<XCDatasourceInfo> xcDataSourceInfos) {
		this.xcDataSourceInfos = xcDataSourceInfos;
	}
	public int getFlushCache() {
		return flushCache;
	}
	public void setFlushCache(int flushCache) {
		this.flushCache = flushCache;
	}
	
	
}
