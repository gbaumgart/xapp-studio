package cmx.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pmedia.types.CContent;
import pmedia.types.Category;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.data.CContentList;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ECMContentSourceType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

import flexjson.JSONSerializer;

public class LiferayDataSourceUtil {
	
	public static CContentList dumpArticles(String uuid,String appId,String platform,String path,String type)
	{
	
		
		CContentList result = new CContentList();
		
		AssetCategory cat=null;
		ECMContentSourceType catType = ECMContentSourceType.Unknown;
		ECMContentSourceType contentType = ECMContentSourceType.Unknown;
		
		if(type.equals("Articles")){
			cat =LiferayContentTools.getDefaultCategory(uuid, appId, 0, "Articles");
			catType=ECMContentSourceType.StaticWebContentCategory;
			contentType=ECMContentSourceType.StaticWebContent;
		}
		if(type.equals("Locations")){
			cat =LiferayContentTools.getDefaultCategory(uuid, appId, 0, "Locations");
			catType=ECMContentSourceType.StaticWebContentVenueCategory;
			contentType=ECMContentSourceType.StaticWebContentVenue;
		}
		if(cat!=null)
		{
			ArrayList<CContent>allArticles=walkCategoriesForArticles(uuid, appId, platform, type, cat.getCategoryId(), null,catType,contentType);
			result.setItems(allArticles);
		}
		
		File pathO = new File(path);
		if(!pathO.exists()){
			pathO.mkdirs();
		}
		
		
		if(result.getItems()!=null)
		{
			String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(contentType) + "_.json";
			JSONSerializer serializer = new JSONSerializer();
			String serialized = serializer.deepSerialize(result);
			serializer.exclude("items.asListItem");
			try {
				StringUtils.writeToFile(serialized, path + dstPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/*
	public static void setCustomFieldsWithLocation(
			CContent content,
			JournalArticle data,
			ApplicationManager  applicationManager,
			Application  application,
			String platform,
			String lang)
	{
		String lat = LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Lat");
		String lon = LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Lon");
		String city= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "City");
		String street= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Street");
		String phone= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Phone");
		String web= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Web");
		String pcode= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "PostCode");
	}
	*/
	
	public static ArrayList<CContent>walkCategoriesForArticles(String uuid,String appId,String platform,String type,long parent,ArrayList<CContent>dst,ECMContentSourceType catType,ECMContentSourceType contentType)
	{
		if(dst==null)
		{
			dst=new ArrayList<CContent>();
		}
		
		
		Application app = ApplicationManager.getInstance().getApplication(uuid, appId, false);
		

		List<AssetCategory>catItems=null;
		
		try {
			catItems= AssetCategoryLocalServiceUtil.getChildCategories(parent);
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			return dst;
		}
		if(catItems==null){
			return dst;
		}
		
    	for (int s = 0; s < catItems.size() ; s++) 
		{
    		AssetCategory c = catItems.get(s);
    		int cP = (int)c.getParentCategoryId();
    		if(cP!=parent){
    			continue;
    		}
    		
    		
    				    	
	    	Category bc = LiferayContentTools.toBaseData(c);
	    	
	    	if(bc.getTitle().equals("subsub"))
	    	{
	    		//System.out.println("subsub");
	    	}
	    	//4294967296  cats   | all flags	6442450944	
    		ArrayList<JournalArticle >categoryArticles = null;
    		categoryArticles=LiferayContentTools.getCategoryArticles(uuid,appId,bc.refId);
	    	/***
	    	 * add articles
	    	 */
	    	if(categoryArticles!=null && categoryArticles.size() > 0)
	    	{
	    		for (int ai = 0; ai < categoryArticles.size() ; ai++) 
				{
	    			JournalArticle article = categoryArticles.get(ai);
	    			
	    			JournalArticle __article = null ; 
	    			

	    			long _id = article.getClassPK();
	    			long _id2 = article.getId();
	    			
	    			try {
						__article=JournalArticleLocalServiceUtil.getArticle(_id2);
					} catch (PortalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			//53603
	    			
	    			CContent content =  LiferayContentTools.toCContent(uuid, appId, __article, bc.refId,contentType,platform);
	    			if(content!=null)
	    			{
	    				//content.setRefId((int)article.getPrimaryKey());
	    				content.setRefId((int)_id2);
	    				content.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(contentType);
	    				dst.add(content);

	    				if(contentType==ECMContentSourceType.StaticWebContentVenue)
	    				{
	    					//setCustomFieldsWithLocation(content, article, ApplicationManager.getInstance(), app, platform, "en");
	    					LiferayContentTools.setCustomFields(content, __article, uuid, appId, "Liferay", contentType, platform, "en");
	    				}
	    			}
				}
	    	}
		}
		
		return dst;
	}
	public static ArrayList<CContent>walkCategoriesForCategories(String uuid,String appId,String platform,String type,long parent,ArrayList<CContent>dst,ECMContentSourceType catType,ECMContentSourceType contentType)
	{
		if(dst==null)
		{
			dst=new ArrayList<CContent>();
		}
		
		/***
		 * Add the current : 
		 */
		
		AssetCategory cat = null; 
		try {
			cat = AssetCategoryLocalServiceUtil.getAssetCategory(parent);
		} catch (PortalException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SystemException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if(cat!=null){
			CContent _ccat = LiferayContentTools.toCContent(cat);
			_ccat.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
			dst.add(_ccat);
		}else
		{
			return dst;
		}
		

		List<AssetCategory>catItems=null;
		try {
			catItems= AssetCategoryLocalServiceUtil.getChildCategories(parent);
		} catch (SystemException e1) {
			return dst;
		}
		if(catItems==null){
			return dst;
		}
		if(catItems.size()==0)
		{
			return dst;
		}
		for (int s = 0; s < catItems.size() ; s++) 
		{
			
    		AssetCategory c = catItems.get(s);
    		/*
    		CContent ccat = LiferayContentTools.toCContent(c);
    		ccat.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
    		dst.add(ccat);
    		*/
    		
    		List<AssetCategory>subItems=null;
    		
    		try {
				subItems= AssetCategoryLocalServiceUtil.getChildCategories(c.getCategoryId());
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(subItems!=null )
    		{
    			if(subItems.size()>0){
    				return walkCategoriesForCategories(uuid, appId, platform, type, c.getCategoryId(), dst, catType, contentType);
    			}
    		}
    		
    		
    		if(subItems.size()==0)
    		{
    			CContent ccat = LiferayContentTools.toCContent(c);
        		ccat.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
        		dst.add(ccat);
    		}
    		
		}
		
		return dst;
	}
	public static CContentList dumpArticleCategories(String uuid,String appId,String platform,String path,String type)
	{
		CContentList result = new CContentList();
		List<AssetCategory>catItems=null;
		
		ECMContentSourceType catType = ECMContentSourceType.Unknown;
		ECMContentSourceType contentType = ECMContentSourceType.Unknown;
		AssetCategory cat = null;
		if(type.equals("Articles")){
			cat=LiferayContentTools.getDefaultCategory(uuid, appId, 0, "Articles");
			catType=ECMContentSourceType.StaticWebContentCategory;
			contentType=ECMContentSourceType.StaticWebContent;
		}
		
		if(type.equals("Locations")){
			cat=LiferayContentTools.getDefaultCategory(uuid, appId, 0, "Locations");
			catType=ECMContentSourceType.StaticWebContentVenueCategory;
			contentType=ECMContentSourceType.StaticWebContentVenue;
		}

		
		ArrayList<CContent>ccats = walkCategoriesForCategories(uuid, appId, platform, type, cat.getCategoryId(), null, catType, contentType);
		CContent _ccat = LiferayContentTools.toCContent(cat);
		_ccat.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
		ccats.add(_ccat);
		/*
		ArrayList<CContent>ccats= new ArrayList<CContent>();
    	for (int s = 0; s < catItems.size() ; s++) 
		{
    		AssetCategory c = catItems.get(s);
    		CContent ccat = LiferayContentTools.toCContent(c);
    		ccat.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(catType);
    		ccats.add(ccat);
    		
		}*/
    	
    	result.setItems(ccats);
    		

		if(result.getItems()!=null)
		{
			String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(catType) + "_.json";
			JSONSerializer serializer = new JSONSerializer();
			String serialized = serializer.deepSerialize(result);
			serializer.exclude("items.asListItem");
			try {
				StringUtils.writeToFile(serialized, path + dstPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
