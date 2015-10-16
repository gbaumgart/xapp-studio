package cmx.tools;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gdata.data.dublincore.Title;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import pmedia.DataUtils.MediaUtils;
import pmedia.Servlets.HttpSessionCollector;
import pmedia.Servlets.SessionCreateAction;
import pmedia.html.factory.WhiteListDataFactory;
import pmedia.html.types.ClientFilter;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CField;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.MediaItemBase;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmm.utils.LiferayTools;
import cmx.data.CContentFactory;
import cmx.types.ECMContentSourceType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journal.service.persistence.JournalArticlePersistence;

public class LiferayContentTools {
	
	
	public static long getScopeId()
	{
		return Long.parseLong(System.getProperty("scopeId"));
	}
	public static long getCompanyId(){
		return Long.parseLong(System.getProperty("companyId"));
	}
	
	public static ArrayList<AssetCategory> getCategoriesByParentIndex(List<AssetCategory>srcCategories,int index)
	{
		ArrayList<AssetCategory> result = new ArrayList<AssetCategory>();
		if(srcCategories!=null)
		{
			for (int s = 0; s < srcCategories.size() ; s++) 
			 {
				AssetCategory c = srcCategories.get(s);
			    if(c.getParentCategoryId() == index){
			    	result.add(c);
			    }
			 }
		}
		return result;
	}
	public static boolean hasSubCategories(List<AssetCategory>srcCategories,int index)
	{
		if(srcCategories==null)
			return false;
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			AssetCategory c = srcCategories.get(s);
			
			if(c.getCategoryId() !=index && c.getParentCategoryId()==index)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasSubCategory(String uuid,String appId,String type,int refId)
	{
		
		return false;
	}
	public static ServiceContext createDefaultServiceContext(String uuid)
	{
		
		ServiceContext sctx=new ServiceContext();
		sctx.setScopeGroupId(getScopeId());
    	sctx.setSignedIn(true);
    	
    	User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
    	sctx.setCompanyId(getCompanyId());
    	sctx.setUserId(lUser.getUserId());
    	return sctx;
		
	}

	public static ArrayList<JournalArticle> getCategoryArticles(String uuid,String appId,long parentId)
	{
		ArrayList<JournalArticle>result= new ArrayList<JournalArticle>();
		
		long classNameId = PortalUtil.getClassNameId(JournalArticle.class.getName());
		
		AssetEntryQuery aeq = new AssetEntryQuery();
		aeq.setAllCategoryIds(new long[]{parentId});
		//aeq.setAnyCategoryIds(new long[]{parentId});
		
		List<AssetCategory>childcats=null;
		
		try {
			childcats=AssetCategoryLocalServiceUtil.getChildCategories(parentId);
		} catch (SystemException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if(childcats!=null && childcats.size()>0)
		{
			long[] ret = new long[childcats.size()];
		    for (int i=0; i < ret.length; i++)
		    {
		        ret[i] = childcats.get(i).getCategoryId();
		    }
		    aeq.setNotAllCategoryIds(ret);
		}
		
		
		aeq.setClassNameIds(new long []{classNameId});
		List<AssetEntry> entries=null;
		try {
			entries = AssetEntryLocalServiceUtil.getEntries(aeq);
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		
		for (AssetEntry entry : entries) 
		{
			String className = PortalUtil.getClassName(entry.getClassNameId());
			  AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
			  AssetRenderer assetRenderer=null;
			  long classPK = entry.getClassPK();
			  try {
				assetRenderer = assetRendererFactory.getAssetRenderer(entry.getClassPK());
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			  if(assetRenderer==null){
				  continue;
			  }
			  long primId = entry.getPrimaryKey();
			  //assetRenderer.get
			  //assetRenderer.get
			  long cl = assetRenderer.getClassPK();// getClassPK() with JournalArticleLocalServiceUtil.getLatestArticle(classPK)
			  JournalArticle __article = null;
			  JournalArticle __article2 = null;
			  
			  //__article=getArticleByUserIdAndArticleId(uuid, ""+cl);
			  __article=getArticleByUserIdAndResourceId(uuid, cl);
			  
			  
			  			  
			  /*
			  try {
					__article = JournalArticleLocalServiceUtil.getLatestArticle(cl);
				} catch (PortalException e) 
				{
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			  */
			  if(__article!=null)
			  {
				  
				  double latestVersion=0;
				  __article2=getLatestVersion(__article, uuid);
				  /*
					try {
						latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(__article.getGroupId(),""+__article.getArticleId());
					} catch (PortalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					
				  result.add(__article);
			  }
			  
			  
		}
		
		return result;
		
		
		
	}
	public static AssetCategory getArticleCategory(String uuid,String appId,JournalArticle article)
	{

		JournalArticleResource aRes=null;
		
		try {
			aRes = article.getArticleResource();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		List<JournalArticleResource>resources = null;
		if(aRes!=null){
			try {
				 resources = JournalArticleResourceLocalServiceUtil.getArticleResources(getScopeId());
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

				long classNameId = PortalUtil.getClassNameId(JournalArticle.class.getName());
				AssetEntryQuery aeq = new AssetEntryQuery();
				aeq.setAllCategoryIds(new long[]{49402});
				aeq.setClassNameIds(new long []{classNameId});
				List<AssetEntry> entries=null;
				try {
					entries = AssetEntryLocalServiceUtil.getEntries(aeq);
				} catch (SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for (AssetEntry entry : entries) 
				{
					String className = PortalUtil.getClassName(entry.getClassNameId());
					  AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);
					  AssetRenderer assetRenderer=null;
					  try {
						assetRenderer = assetRendererFactory.getAssetRenderer(entry.getClassPK());
					} catch (PortalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  
					  
					  //assetRenderer.get
					  long cl = assetRenderer.getClassPK();// getClassPK() with JournalArticleLocalServiceUtil.getLatestArticle(classPK)
					  JournalArticle __article = null;
					  try {
						__article = JournalArticleLocalServiceUtil.getLatestArticle(cl);
					} catch (PortalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  
					  
				}
//		AssetCategoryServiceUtil.get
		//aid : 49802
		if(resources!=null)
		{
			//bbd74472-0504-40b2-9930-c2e54f3f5c71||49402
			for (int i = 0; i < resources.size(); i++) 
			{
				JournalArticleResource res = resources.get(i);
				if(res.getArticleId().equals(article.getArticleId()))
				{
					//49104
					AssetCategory cat = null;
					//cat = AssetCategoryLocalServiceUtil.getAssetCategoryByUuidAndGroupId(uuid, groupId)
					try {
						cat = AssetCategoryLocalServiceUtil.getAssetCategory(res.getGroupId());
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
					
					if(cat!=null){
						return cat;
					}
				}
			}
		}
		
		return null;
	}
	public static void setCustomFields(
			CContent content,
			JournalArticle data,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			String platform,
			String lang)
	{
		if(content.customFields!=null){
			content.customFields.clear();
		}else{
			content.setCustomFields(new ArrayList<CField>());
		}
		
		String lat = LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Lat");
		
		String lon = LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Lon");
		String city= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "City");
		String street= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Street");
		String phone= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Phone");
		String web= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "Web");
		
		String pcode= LiferayContentTools.getLiferayArticleContentByStructureElement(data, "PostCode");
		
		if(lat!=null && lon!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = lat+"|" + lon;
			mapLink.title = "Show onMap";
			mapLink.sourceType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MAP);
			mapLink.refIdStr="lat|lon";
			//mapLink.dataSource="Liferay";
			content.getCustomFields().add(mapLink);
			
		}
		
		if(street!=null){
			CField mapLink  = new CField();
			mapLink.value = street;
			mapLink.title = street;
			
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.TEXT);
			mapLink.refIdStr="street";
		//	mapLink.dataSource="Liferay";
			content.getCustomFields().add(mapLink);
		}
		
		if(city!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = city;
			mapLink.title = city;
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.TEXT);
			mapLink.refIdStr="city";
			//mapLink.dataSource="Liferay";
			content.getCustomFields().add(mapLink);
		}
		
		if(phone!=null && phone.length() > 0)
		{
			CField mapLink  = new CField();
			mapLink.value = phone;
			mapLink.title = phone;
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.PHONE);
			mapLink.refIdStr="phone";
		//	mapLink.dataSource="Liferay";
			content.getCustomFields().add(mapLink);
		}
		
		if(web!=null && web.length() > 0)
		{
			CField mapLink  = new CField();
			mapLink.value = web;
			mapLink.title = web.replace("http://", "");
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
			mapLink.refIdStr="web";
		//	mapLink.dataSource="Liferay";
			content.getCustomFields().add(mapLink);
		}
		
		if(pcode!=null && pcode.length() > 0)
		{
			CField mapLink  = new CField();
			mapLink.value = pcode;
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.TEXT);
			mapLink.refIdStr="pcode";
		//	mapLink.dataSource="Liferay";
			content.getCustomFields().add(mapLink);
		}
		
		
	}

	public static CContent toCContent(String uuid,String appId,JournalArticle article,int categoryId,ECMContentSourceType cType,String platform)
	{
		CContent result= new CContent();
		result.refId=Integer.parseInt(article.getArticleId());
		result.title=article.getTitle(LocaleUtil.getDefault());
		result.groupId=categoryId;
		result.treeUid=UUID.randomUUID().toString();
		result.published=true;
		
		//result.setBaseRef(baseRef)
		
		result.setDataSourceUID("Liferay");
		
		String plainContent = LiferayContentTools.getLiferayArticleContentByStructureElement(article, "Content");
		result.description=plainContent;
		
		ArticleData jArticle=new ArticleData();
		jArticle.description=""+plainContent;
		jArticle.getPictures();
		
		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(plainContent);
		
		
		if(pictureItems!=null)
		{
			result.setPictureItems(new ArrayList<MediaItemBase>());
			//content.getPictureItems().addAll(data.getPictureItems());
			MediaUtils.addPictureItems(pictureItems, result.getPictureItems(),"");
		}
		/*
		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(newRawDescription);
		if(pictureItems!=null && pictureItems.size() > 0)
		{
			if(content.getPictureItems()==null){
				content.setPictureItems(pictureItems);
			}else{
				//content.getPictureItems().addAll(pictureItems);
				MediaUtils.addPictureItems(pictureItems, content.getPictureItems(),ds.getUrl());
			}
		}
		
		
		if(!platform.equals(pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE))
		{
			String descriptionWithPictures = CContentFactory.mergePictureItems(content.getPictureItems(), newRawDescription, baseRef);
			//second pass : 
			newRawDescription = ClientFilter.filter(descriptionWithPictures, uuid, applicationId, dsUID, baseRef,cType,"" + data.getRefId(), whiteList, platform, lang, maxSize);
			content.setDescription(newRawDescription);
		}else{
			content.setDescription(newRawDescription);
		}
		 */
		
		result.refId=(int)article.getId();
		if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE4_NATIVE))
		{
			String newRawDescription = ClientFilter.filter(plainContent, uuid, appId, "Liferay", "",cType,"" + result.getRefId(), WhiteListDataFactory.iphoneNativeBase(appId), platform, "en", 120);
			result.setDescription(newRawDescription);
		
		}else if(platform.equals(pmedia.types.Constants.MOBILE_WEB_APP))
		{
			String newRawDescription = ClientFilter.filter(plainContent, uuid, appId, "Liferay", "",cType,"" + result.getRefId(), WhiteListDataFactory.mobileBase(appId), platform, "en", 120);
			result.setDescription(newRawDescription);
		}
		result.refId=Integer.parseInt(article.getArticleId());
		
		//result.setDescription(newRawDescription);
		
		/*
		if(plainContent!=null && plainContent.length() >0)
    	{
    		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(plainContent);
    		if(pictureItems!=null && pictureItems.size() > 0)
    		{
    			jArticle.setPictureItems(pictureItems);
    		}
    	}
    	*/
		
		
		
		
		return result;
	}
	public static ArticleData toBaseData(String uuid,String appId,ArrayList <JournalArticle>articles,JournalArticle article,int categoryId)
	{
		ArticleData result= new ArticleData();
		if(article==null){
			return null;
		}
		
		int op =(int) article.getPrimaryKey();;
		int op2 = (int)article.getResourcePrimKey();
		result.refId=(int) article.getPrimaryKey();
		//result.refId=Integer.parseInt(article.getArticleId());
		result.title=article.getTitle(LocaleUtil.getDefault());
		result.groupId=categoryId;
		result.treeUid=UUID.randomUUID().toString();
		result.published=true;
		
		return result;
	}
	public static JournalArticle createLocation(String uuid,String appIdentfier,String title,String content,int catId,String street,String phone,String pcode,String city,String web,String lat,String lon,String image)
	{
		AssetCategory parentCategory=null;
		/***
		 * content location : 
		 * 
		 <root available-locales="en_US" default-locale="en_US"><dynamic-element instance-id="axvcN9kp" name="Content" type="text_area" index-type="text"><dynamic-content><![CDATA[<p>
	dfasdfasdf</p>
	]]></dynamic-content></dynamic-element><dynamic-element instance-id="435vhIKf" name="City" type="text" index-type=""><dynamic-content><![CDATA[asdfasdf]]></dynamic-content></dynamic-element><dynamic-element instance-id="O2GNKBrZ" name="Featured" type="boolean" index-type=""><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id="pFOkdsf3" name="Icon" type="image" index-type=""><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id="eeL4RGlF" name="Web" type="text" index-type=""><dynamic-content><![CDATA[www]]></dynamic-content></dynamic-element><dynamic-element instance-id="57H0Hz7I" name="Lon" type="text" index-type=""><dynamic-content><![CDATA[3.4]]></dynamic-content></dynamic-element><dynamic-element instance-id="CmuxJ2UM" name="Lat" type="text" index-type=""><dynamic-content><![CDATA[asdfasdfa]]></dynamic-content></dynamic-element><dynamic-element instance-id="U8sM71DN" name="PostCode" type="text" index-type="text"><dynamic-content><![CDATA[80555]]></dynamic-content></dynamic-element><dynamic-element instance-id="v5wlSSQ3" name="Phone" type="text" index-type="keyword"><dynamic-content><![CDATA[3478787]]></dynamic-content></dynamic-element><dynamic-element instance-id="gY1LgDke" name="Street" type="text" index-type="text"><dynamic-content><![CDATA[street]]></dynamic-content></dynamic-element></root>

		 */
		/***
		 * groupId	49104	
			classNameId	0	
			classPK	0	
			articleId	"" (id=174)	
			autoArticleId	true	
			titleMap	HashMap<K,V>  (id=185)	
			descriptionMap	HashMap<K,V>  (id=190)	
			content	"<root available-locales="en_US" default-locale="en_US"><dynamic-element instance-id="k8MZr0lD" name="Content" type="text_area" index-type="text"><dynamic-content><![CDATA[<p>\n\tasdfasdf</p>\n]]></dynamic-content></dynamic-element></root>" (id=191)	
			type	"general" (id=192)	
			structureId	"50605" (id=193)	
			templateId	"50607" (id=194)	
			layoutUuid	null	
			displayDateMonth	7	
			displayDateDay	30	
			displayDateYear	2012	
			displayDateHour	1	
			displayDateMinute	44	
			expirationDateMonth	0	
			expirationDateDay	0	
			expirationDateYear	0	
			expirationDateHour	0	
			expirationDateMinute	0	
			neverExpire	true	
			reviewDateMonth	0	
			reviewDateDay	0	
			reviewDateYear	0	
			reviewDateHour	0	
			reviewDateMinute	0	
			neverReview	true	
			indexable	true	
			smallImage	false	
			smallImageURL	"" (id=195)	
			smallFile	File  (id=196)	
			images	HashMap<K,V>  (id=198)	
			articleURL	"http://mc007ibi.dyndns.org:8080/group/control_panel/manage?p_p_id=15&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&doAsGroupId=49104&refererPlid=10876&_15_struts_action=%2Fjournal%2Fedit_article" (id=199)	
			serviceContext	ServiceContext  (id=200)	
		 */
		
		ServiceContext sctx=createDefaultServiceContext(uuid);
		User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
    	long groupid = getScopeId();
    	long classNameId=0;
    	long classPK=0;
    	
    	String articleId = "";
    	
    	boolean autoArticleId=true;
    	
    	java.util.Map<java.util.Locale, java.lang.String> titleMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	titleMap.put(LocaleUtil.getDefault(), title);
    	
    	java.util.Map<java.util.Locale, java.lang.String> descMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	descMap.put(LocaleUtil.getDefault(), content);
    	
    	String type="general";
    	String structureId = System.getProperty("StructureIdLocation");
    	String templateId = System.getProperty("TemplateIdLocation");
    	String layoutUuid=null; 
    	
    	/***
    	 * 
    	 */
    	Date date = new Date();
    	java.io.File smallFile =new File(""); //java.util.Map<java.lang.String, byte[]> images>
    	java.util.Map<java.lang.String, byte[]> images = new java.util.HashMap<java.lang.String, byte[]>();
    	
    	JournalArticle result = null;
    	
    	//String innerContent = "<root available-locales=\"en_US\" default-locale=\"en_US\"><dynamic-element instance-id=\"q6HBXqNR\" name=\"Content\" type=\"text_area\" index-type=\"text\"><dynamic-content><![CDATA[" + content +"]]></dynamic-content></dynamic-element></root>";
    	String innerContent ="<root available-locales=\"en_US\" default-locale=\"en_US\">    <dynamic-element instance-id=\"axvcN9kp\" name=\"Content\" type=\"text_area\" index-type=\"text\">        " +
    			"<dynamic-content><![CDATA[" + content +"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"435vhIKf\" name=\"City\" type=\"text\" index-type=\"\">        " +
    				"<dynamic-content><![CDATA["+city+"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"O2GNKBrZ\" name=\"Featured\" type=\"boolean\" index-type=\"\">      " +
    				"<dynamic-content><![CDATA[true]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"pFOkdsf3\" name=\"Icon\" type=\"image\" index-type=\"\">        " +
    				"<dynamic-content><![CDATA[" + image+ "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"eeL4RGlF\" name=\"Web\" type=\"text\" index-type=\"\">       " +
    				"<dynamic-content><![CDATA[" + web  + "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"57H0Hz7I\" name=\"Lon\" type=\"text\" index-type=\"\">       " +
    				" <dynamic-content><![CDATA[" +lon + "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"CmuxJ2UM\" name=\"Lat\" type=\"text\" index-type=\"\">      " +
    				"<dynamic-content><![CDATA[" + lat+ "]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"U8sM71DN\" name=\"PostCode\" type=\"text\" index-type=\"text\">        " +
    				"<dynamic-content><![CDATA[" + pcode +"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"v5wlSSQ3\" name=\"Phone\" type=\"text\" index-type=\"keyword\">        " +
    				"<dynamic-content><![CDATA[" + phone +"]]></dynamic-content>    </dynamic-element>    " +
    			"<dynamic-element instance-id=\"gY1LgDke\" name=\"Street\" type=\"text\" index-type=\"text\">        " +
    				"<dynamic-content><![CDATA[" +street +"]]></dynamic-content>    </dynamic-element>" +
    			"</root>";
    
    	//result = JournalArticleLocalServiceUtil.addArticle(userId, groupId, classNameId, classPK, articleId, autoArticleId, version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext)
    	//result = JournalArticleLocalServiceUtil.addArticle(userId, groupId, classNameId, classPK, articleId, autoArticleId, version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext)
    	
    	Calendar startDate = Calendar.getInstance();
		//07/14/2012 (using the American way of putting the month first, then the day then the year), this should also be in bold,

			
    	startDate.setTime(new Date());
    	
    	int  month = startDate.get(Calendar.MONTH)-1;
    	if(month<0){
    		month = 0;
    	}
    	try {
			result = JournalArticleLocalServiceUtil.addArticle(lUser.getUserId(), groupid,0,classNameId, classPK, 
			    	articleId, true, 1.0, titleMap, descMap, innerContent , type, structureId, templateId, layoutUuid, 
			    	month, startDate.get(Calendar.DATE)-1, startDate.get(Calendar.YEAR)-1, date.getHours(), date.getMinutes(), 0,  
			    	0, 0, 0, 0, 
			    	true, 0, 0, 0, 0, 0, true, true, false, "", smallFile, images, "", sctx);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//JournalArticleLocalServiceUtil.updateAsset(userId, article, assetCategoryIds, assetTagNames, assetLinkEntryIds)
		//JournalArticle result=JournalArticleLocalServiceUtil.addArticle(userId, groupId, classNameId, classPK, 
    	//articleId, autoArticleId, version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid, 
    	//displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, 
    	//expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, 
    	//neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext);
    	//49402
    	//sctx.setAssetCategoryIds(assetCategoryIds)
    	long cats[]={catId};
    	String tags[]={};
    	long entryIds[]={};
    	if(result!=null){
	    	try {
				JournalArticleLocalServiceUtil.updateAsset(lUser.getUserId(), result, cats, tags, entryIds);
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
    	
    	result.setNew(false);
    	try {
			JournalArticleLocalServiceUtil.updateJournalArticle(result);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
	}
	
	public static JournalArticle createArticle(String uuid,String appIdentfier,String title,String content,int catId)
	{
		AssetCategory parentCategory=null;
		/***
		 * content location : 
		 * 
		 <root available-locales="en_US" default-locale="en_US"><dynamic-element instance-id="axvcN9kp" name="Content" type="text_area" index-type="text"><dynamic-content><![CDATA[<p>
	dfasdfasdf</p>
	]]></dynamic-content></dynamic-element><dynamic-element instance-id="435vhIKf" name="City" type="text" index-type=""><dynamic-content><![CDATA[asdfasdf]]></dynamic-content></dynamic-element><dynamic-element instance-id="O2GNKBrZ" name="Featured" type="boolean" index-type=""><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id="pFOkdsf3" name="Icon" type="image" index-type=""><dynamic-content><![CDATA[]]></dynamic-content></dynamic-element><dynamic-element instance-id="eeL4RGlF" name="Web" type="text" index-type=""><dynamic-content><![CDATA[www]]></dynamic-content></dynamic-element><dynamic-element instance-id="57H0Hz7I" name="Lon" type="text" index-type=""><dynamic-content><![CDATA[3.4]]></dynamic-content></dynamic-element><dynamic-element instance-id="CmuxJ2UM" name="Lat" type="text" index-type=""><dynamic-content><![CDATA[asdfasdfa]]></dynamic-content></dynamic-element><dynamic-element instance-id="U8sM71DN" name="PostCode" type="text" index-type="text"><dynamic-content><![CDATA[80555]]></dynamic-content></dynamic-element><dynamic-element instance-id="v5wlSSQ3" name="Phone" type="text" index-type="keyword"><dynamic-content><![CDATA[3478787]]></dynamic-content></dynamic-element><dynamic-element instance-id="gY1LgDke" name="Street" type="text" index-type="text"><dynamic-content><![CDATA[street]]></dynamic-content></dynamic-element></root>

		 */
		/***
		 * groupId	49104	
			classNameId	0	
			classPK	0	
			articleId	"" (id=174)	
			autoArticleId	true	
			titleMap	HashMap<K,V>  (id=185)	
			descriptionMap	HashMap<K,V>  (id=190)	
			content	"<root available-locales="en_US" default-locale="en_US"><dynamic-element instance-id="k8MZr0lD" name="Content" type="text_area" index-type="text"><dynamic-content><![CDATA[<p>\n\tasdfasdf</p>\n]]></dynamic-content></dynamic-element></root>" (id=191)	
			type	"general" (id=192)	
			structureId	"50605" (id=193)	
			templateId	"50607" (id=194)	
			layoutUuid	null	
			displayDateMonth	7	
			displayDateDay	30	
			displayDateYear	2012	
			displayDateHour	1	
			displayDateMinute	44	
			expirationDateMonth	0	
			expirationDateDay	0	
			expirationDateYear	0	
			expirationDateHour	0	
			expirationDateMinute	0	
			neverExpire	true	
			reviewDateMonth	0	
			reviewDateDay	0	
			reviewDateYear	0	
			reviewDateHour	0	
			reviewDateMinute	0	
			neverReview	true	
			indexable	true	
			smallImage	false	
			smallImageURL	"" (id=195)	
			smallFile	File  (id=196)	
			images	HashMap<K,V>  (id=198)	
			articleURL	"http://mc007ibi.dyndns.org:8080/group/control_panel/manage?p_p_id=15&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&doAsGroupId=49104&refererPlid=10876&_15_struts_action=%2Fjournal%2Fedit_article" (id=199)	
			serviceContext	ServiceContext  (id=200)	
		 */
		
		ServiceContext sctx=createDefaultServiceContext(uuid);
		User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
    	long groupid = getScopeId();
    	long classNameId=0;
    	long classPK=0;
    	
    	String articleId = "";
    	
    	boolean autoArticleId=true;
    	
    	java.util.Map<java.util.Locale, java.lang.String> titleMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	titleMap.put(LocaleUtil.getDefault(), title);
    	
    	java.util.Map<java.util.Locale, java.lang.String> descMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	descMap.put(LocaleUtil.getDefault(), content);
    	
    	String type="general";
    	String structureId = System.getProperty("StructureIdArticle");
    	String templateId = System.getProperty("TemplateIdArticle");
    	String layoutUuid=null; 
    	
    	/***
    	 * 
    	 */
    	Date date = new Date();
    	java.io.File smallFile =new File(""); //java.util.Map<java.lang.String, byte[]> images>
    	java.util.Map<java.lang.String, byte[]> images = new java.util.HashMap<java.lang.String, byte[]>();
    	
    	JournalArticle result = null;
    	
    	String innerContent = "<root available-locales=\"en_US\" default-locale=\"en_US\"><dynamic-element instance-id=\"q6HBXqNR\" name=\"Content\" type=\"text_area\" index-type=\"text\"><dynamic-content><![CDATA[" + content +"]]></dynamic-content></dynamic-element></root>";
    
    	//result = JournalArticleLocalServiceUtil.addArticle(userId, groupId, classNameId, classPK, articleId, autoArticleId, version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext)
    	//result = JournalArticleLocalServiceUtil.addArticle(userId, groupId, classNameId, classPK, articleId, autoArticleId, version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid, displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext)
    	
    	//JournalArticleLocalServiceUtil.ad
    	
    	Calendar startDate = Calendar.getInstance();
		//07/14/2012 (using the American way of putting the month first, then the day then the year), this should also be in bold,

			
    	startDate.setTime(new Date());
    	int month = startDate.get(Calendar.MONTH)-1;
    	if(month < 0 ){
    		month =0;
    	}
    	
    	try {
			result = JournalArticleLocalServiceUtil.addArticle(lUser.getUserId(), groupid,0,classNameId, classPK, 
			    	articleId, true, 1.0, titleMap, descMap, innerContent , type, structureId, templateId, layoutUuid, 
			    	month, startDate.get(Calendar.DATE)-1, startDate.get(Calendar.YEAR)-1, date.getHours(), date.getMinutes(), 0, 
			    	0, 0, 0, 0, 
			    	true, 0, 0, 0, 0, 0, true, false, false, "", smallFile, images, "", sctx);
			
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	//JournalArticleLocalServiceUtil.updateAsset(userId, article, assetCategoryIds, assetTagNames, assetLinkEntryIds)
    	
		//JournalArticle result=JournalArticleLocalServiceUtil.addArticle(userId, groupId, classNameId, classPK, 
    	//articleId, autoArticleId, version, titleMap, descriptionMap, content, type, structureId, templateId, layoutUuid, 
    	//displayDateMonth, displayDateDay, displayDateYear, displayDateHour, displayDateMinute, expirationDateMonth, 
    	//expirationDateDay, expirationDateYear, expirationDateHour, expirationDateMinute, 
    	//neverExpire, reviewDateMonth, reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute, neverReview, indexable, smallImage, smallImageURL, smallImageFile, images, articleURL, serviceContext);
    	//49402
    	//sctx.setAssetCategoryIds(assetCategoryIds)
    	long cats[]={catId};
    	String tags[]={};
    	long entryIds[]={};
    	if(result!=null){
	    	try {
				JournalArticleLocalServiceUtil.updateAsset(lUser.getUserId(), result, cats, tags, entryIds);
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	//result.setNew(false);
    	try {
			JournalArticleLocalServiceUtil.updateJournalArticle(result);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
		
		return result;
	}
	
	public static void deleteArticleSafe(String uuid,String appIdentfier,int refId)
	{
		
		JournalArticle _article = null;
			_article = LiferayContentTools.getArticleByUserIdAndArticleId(uuid,""+refId);
		if(_article==null){
			try {
				_article=JournalArticleLocalServiceUtil.getArticle(refId);
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(_article!=null){
			try {
				//JournalArticleLocalServiceUtil.deleteJournalArticle(_article.getPrimaryKey());
				JournalArticleLocalServiceUtil.deleteJournalArticle((_article.getId()));
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void deleteLocationSafe(String uuid,String appIdentfier,int refId)
	{
		
	}
	public static void deleteCategorySafe(String uuid,String appIdentfier,int catId)
	{
		AssetCategory parentCategory=null;
		
		
		try {
			parentCategory=AssetCategoryLocalServiceUtil.getAssetCategory(catId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if(parentCategory!=null)
		{
			try {
				AssetCategoryLocalServiceUtil.deleteAssetCategory(parentCategory);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
	public static void updateCategorySafe(String uuid,String appIdentfier,String newTitle, int parent, int catId)
	{
		AssetCategory parentCategory=null;
		try {
			parentCategory=AssetCategoryLocalServiceUtil.getAssetCategory(catId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if(parentCategory!=null){
			
			if(newTitle!=null)
			{
				parentCategory.setTitle(newTitle);
			}
			

			if(parentCategory.getParentCategoryId()!=parent)
			{
				try {
					AssetCategoryLocalServiceUtil.moveCategory(catId, parent, parentCategory.getVocabularyId(), createDefaultServiceContext(uuid));
				} catch (PortalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				AssetCategoryLocalServiceUtil.updateAssetCategory(parentCategory);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static Category updateCategorySafe(String uuid,String appIdentfier,String title, String dataRef)
	{
		AssetCategory parentCategory=null;
		
		try {
			parentCategory = AssetCategoryLocalServiceUtil.getAssetCategory(Long.parseLong(dataRef));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(parentCategory!=null){
			parentCategory.setTitle(title);
			try {
				AssetCategoryLocalServiceUtil.updateAssetCategory(parentCategory);
			} catch (SystemException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return toBaseData(parentCategory);
	}
	public static Category createCategorySafe(String uuid,String appIdentfier,String title, int parent, String type)
	{
		AssetCategory parentCategory=null;
		if(parent==0){
			
			if(type.equals("Articles"))
			{
				parentCategory=getDefaultCategory(uuid, appIdentfier, 0, "Articles");
			}
			
			if(type.equals("Locations"))
			{
				parentCategory=getDefaultCategory(uuid, appIdentfier, 0, "Locations");
			}
			if(type.equals("People"))
			{
				parentCategory=getDefaultCategory(uuid, appIdentfier, 0, "People");
			}
			
			if(parentCategory!=null){
				parent=(int) parentCategory.getCategoryId();
			}
			
		}
		if(parent!=0){
			AssetCategory cat=createCategory(uuid, appIdentfier, parent, title,type);
			if(cat!=null){
				return toBaseData(cat);
			}
		}
		
		return null;
		
	}
	public static Category toBaseData(AssetCategory cat){
		
		Category result= new Category();
		
		
		result.refId=(int)cat.getCategoryId();
		result.groupId = (int)cat.getParentCategoryId();
		result.title=cat.getTitle(LocaleUtil.getDefault());
		result.treeUid=UUID.randomUUID().toString();
		result.published=true;
		
		return result;
		
	}

	public static BaseData toBaseData(JournalArticle article,int categoryIndex)
	{
		BaseData result= new Category();
		result.refId=Integer.parseInt(article.getArticleId());
		result.groupId = categoryIndex;
		result.title=article.getTitle(LocaleUtil.getDefault());
		result.treeUid=UUID.randomUUID().toString();
		result.published=true;
		result.created = article.getModifiedDate();
		return result;
	}


		
	public static CContent toCContent(AssetCategory cat)
	{
		CContent result= new CContent();
		result.refId=(int)cat.getCategoryId();
		result.groupId = (int)cat.getParentCategoryId();
		result.title=cat.getTitle(LocaleUtil.getDefault());
		result.treeUid=UUID.randomUUID().toString();
		result.published=true;
		result.setDataSourceUID("Liferay");
		return result;
		
	}


	public static List<AssetCategory>getCategoriesByType(String uuid,String appId,int parent,String type,List<AssetCategory>dst)
	{
		if(dst==null){
			dst = new ArrayList<AssetCategory>();
		}
		AssetCategory cat=null;
		
		try {
			cat = AssetCategoryLocalServiceUtil.getAssetCategory(parent);
		} catch (PortalException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return dst;
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return dst;
		}
		
		/*
		if(cat==null){
			try 
			{
				cat = AssetCategoryLocalServiceUtil.getAssetCategory(parent);
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return dst;
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return dst;
			}
			
		}
		if(cat==null){
			//return dst;
		}
		*/
		List<AssetCategory> result =null; 
		
		if(!dst.contains(cat))
		{
			//dst.add(cat);
		}
		
		try 
		{
			result = AssetCategoryLocalServiceUtil.getChildCategories(cat.getCategoryId());
			if(result!=null && result.size()>0)
			{
				dst.addAll(result);
				/*
				for (int i = 0; i < result.size(); i++) 
				{
					AssetCategory _cat = result.get(i);
					List<AssetCategory> subs = AssetCategoryLocalServiceUtil.getChildCategories(_cat.getCategoryId());
					if(subs!=null && subs.size()>0)
					{
						//dst=getCategoriesByType(uuid, appId, (int) _cat.getCategoryId(), type, dst);
					}
				}
				*/
			}
			
		} catch (SystemException e) 
		{
			e.printStackTrace();
		}
		return dst;
	}
	public static AssetCategory getDefaultCategory(String uuid,String appId,int parent,String type)
	{
		User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
    	
    	List<AssetCategory> vocCats = null; //
    	
    	AssetVocabulary voc = getVocabulary(uuid, appId);
    	if(voc==null){
    		return null;
    	}
    	
    	try {
			vocCats = AssetCategoryLocalServiceUtil.getVocabularyCategories(parent, voc.getVocabularyId(), 0, 1000,null);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    	AssetCategory cat = null;
    	for (AssetCategory groupCat: vocCats) 
    	{
			if(groupCat.getUserId()==lUser.getUserId())
    		{
				if(groupCat.getTitle(LocaleUtil.getDefault()).equals(type))
    			{
    				return groupCat;
    			}
    		}
    	}
		return null;
	}
	
	public static AssetVocabulary getVocabulary(String uuid,String appId)
	{
		User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
    	
    	List<AssetVocabulary> vocs = null; //
			
    	try {
			vocs = AssetVocabularyLocalServiceUtil.getGroupVocabularies(getScopeId());
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (SystemException e) 
		{
			return null;
		}

    	for (AssetVocabulary groupVocabulary: vocs) {
    		if(groupVocabulary.getUserId()==lUser.getUserId())
    		{
    			if(groupVocabulary.getTitle(LocaleUtil.getDefault()).equals(appId))
    			{
    				return groupVocabulary;
    			}
    		}
    	}
		return null;
	}
	
	/*
	public static AssetVocabulary deleteVocabulary(String uuid,String appId)
	{
		ServiceContext serviceContext = createDefaultServiceContext(uuid);
		AssetVocabulary voc = null ; 
		
		List<AssetVocabulary> listVocs = null;
		try 
		{
			listVocs = AssetVocabularyLocalServiceUtil.getAssetVocabularies(0, AssetVocabularyLocalServiceUtil.getAssetVocabulariesCount());
		} catch (SystemException e) 
		{

			e.printStackTrace();
		}
	}
	*/
	public static AssetVocabulary createVocabulary(String uuid,String appId)
	{
		ServiceContext serviceContext = createDefaultServiceContext(uuid);
		java.util.Map<java.util.Locale, java.lang.String> titleMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	java.util.Map<java.util.Locale, java.lang.String> descMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	
    	titleMap.put(LocaleUtil.getDefault(), appId);
    	User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
    	
    	titleMap.put(LocaleUtil.getDefault(), appId);
    	
    	AssetVocabulary voc = null ; 
    	
    	try {
			voc = AssetVocabularyLocalServiceUtil.addVocabulary(lUser.getUserId(), titleMap, descMap, "", serviceContext);
		} catch (PortalException e) {

			e.printStackTrace();
		} catch (SystemException e) {

			e.printStackTrace();
		}
    	
    	return voc;
		
	}
	
	public static Boolean prepareUser(String uuid,String appId)
	{
			AssetVocabulary voc = getVocabulary(uuid, appId);
			
			if(voc==null){
				voc=createVocabulary(uuid, appId);
			}
			if(voc==null)
			{
				return false;
			}
			
			
			if(!hasDefaultCategories(uuid, appId))
			{
				createDefaultCategories(uuid, appId);
			}
			
			
			return true;
	}
	
	public static boolean hasDefaultCategories(String uuid,String appId)
	{
		AssetVocabulary voc = getVocabulary(uuid, appId);
		
		if(voc==null)
		{
			voc=createVocabulary(uuid, appId);
		}
		if(voc==null)
		{
			return false;
		}
		
		AssetCategory articleDefaultCat=getDefaultCategory(uuid, appId, 0, "Articles");
		AssetCategory articleDefaultLocations=getDefaultCategory(uuid, appId, 0, "Locations");
		AssetCategory articleDefaultPeople=getDefaultCategory(uuid, appId, 0, "People");
		if(articleDefaultCat!=null && articleDefaultLocations!=null && articleDefaultPeople!=null){
			return true;
		}
				
		return false;
	}
	
	public static AssetCategory createCategory(String uuid,String appId,int parent,String title,String type)
	{
		AssetVocabulary voc = getVocabulary(uuid, appId);
		if(voc==null)
		{
			return null;
		}
		User lUser = getLiferayUserByUUID(uuid);
    	if(lUser==null){
    		return null;
    	}
		
		AssetCategory cat = null;
    	
		java.util.Map<java.util.Locale, java.lang.String> titleMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	java.util.Map<java.util.Locale, java.lang.String> descMap = new java.util.HashMap<java.util.Locale, java.lang.String>();
    	
    	titleMap.put(LocaleUtil.getDefault(), title);
    	//descMap.put(LocaleUtil.getDefault(), "cattitlewfcsd");
		try 
		{
			cat = AssetCategoryLocalServiceUtil.addCategory(lUser.getUserId(), parent, titleMap, descMap, voc.getVocabularyId(), null, createDefaultServiceContext(uuid));
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return cat;
	}
	
	public static boolean createDefaultCategories(String uuid,String appId)
	{
			
		AssetVocabulary voc = getVocabulary(uuid, appId);
		if(voc==null)
		{
			return false;
		}
		
		AssetCategory articleDefaultCat=getDefaultCategory(uuid, appId, 0, "Articles");
		AssetCategory articleDefaultLocations=getDefaultCategory(uuid, appId, 0, "Locations");
		AssetCategory articleDefaultPeople=getDefaultCategory(uuid, appId, 0, "People");
		if(articleDefaultCat==null)
		{
			articleDefaultCat=createCategory(uuid, appId, 0, "Articles",null);
		}
		if(articleDefaultLocations==null)
		{
			articleDefaultLocations=createCategory(uuid, appId, 0, "Locations",null);
		}
		if(articleDefaultPeople==null)
		{
			articleDefaultPeople=createCategory(uuid, appId, 0, "People",null);
		}
		
		return true;
	}
	public static User authUser(String login,String password,HttpServletRequest request)
	{

		Company company = null ;
		User user = null;
		try {
			company = PortalUtil.getCompany(request);
		} catch (PortalException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		long userId = GetterUtil.getLong(login);
		long companyId = company.getCompanyId();
		
		//UserLocalServiceUtil.authenticateForDigest(companyId, username, realm, nonce, method, uri, response)

		try {
			userId = UserLocalServiceUtil.authenticateForBasic(
				companyId, CompanyConstants.AUTH_TYPE_EA, login, password);
		} catch (PortalException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userId > 0) {
				
			try {
				user = UserLocalServiceUtil.getUserById(userId);
			} catch (PortalException e) {
				return null;
			} catch (SystemException e) {
				return null;
			}

			return user;
		}

		try {
			userId = UserLocalServiceUtil.authenticateForBasic(
				companyId, CompanyConstants.AUTH_TYPE_SN, login, password);
		} catch (PortalException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (userId > 0) {
			try {
				user = UserLocalServiceUtil.getUserById(userId);
			} catch (PortalException e) {
				return null;
			} catch (SystemException e) {
				return null;
			}

			return user;
		}

		try {
			userId = UserLocalServiceUtil.authenticateForBasic(
				companyId, CompanyConstants.AUTH_TYPE_ID, login, password);
		} catch (PortalException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(userId>0){
			try {
				user = UserLocalServiceUtil.getUserById(userId);
			} catch (PortalException e) {
				return null;
			} catch (SystemException e) {
				return null;
			}

			return user;
		}
		return null;
	}
	
	public static User isValidAdminUser(String uuid,HttpServletRequest request)
	{
		if(uuid.equals( System.getProperty("adminUser"))){
			return isValidUser(uuid, request); 
		}
		
		return null;
	}
	public static User isValidUser(String uuid,HttpServletRequest request)
	{
		//UserLocalServiceUtil.authenticateByEmailAddress(companyId, emailAddress, password, headerMap, parameterMap, resultsMap)
		User user = getLiferayUserByUUID(uuid);
	
		HttpServletRequest httpReqOri = PortalUtil.getOriginalServletRequest(request);
		if(httpReqOri==null){
			return null;
		}
		
		Boolean try1 = false;
		Boolean valid = false;
		HttpSession _s = SessionCreateAction.find(request.getSession().getId());
		if(_s!=null){
			Object isSignedInMain = _s.getAttribute("userSignedIn");
			if(isSignedInMain!=null){
				//System.out.println("s");
				try {
					valid = Boolean.parseBoolean((String)isSignedInMain);
					if(valid){
						return user;
					}
				} catch (Exception e) {
					
				}
				
			}
		}else{
			_s = HttpSessionCollector.find(request.getSession().getId());
			if(_s!=null){
				Object isSignedInMain = _s.getAttribute("userSignedIn");
				if(isSignedInMain!=null && isSignedInMain.getClass() == String.class ){
					//if(isSignedIn.getClass())
					try {
						valid = Boolean.parseBoolean((String)isSignedInMain);
					} catch (Exception e) {
						
					}
				}else if(isSignedInMain!=null && isSignedInMain.getClass() == Boolean.class ){
					valid=(Boolean)isSignedInMain;
				}
				
				
				if(valid && user!=null){
					return user;
				}
				
			}
			
		}
		
		Object isSignedIn = httpReqOri.getSession().getAttribute("userSignedIn");
		
		if(isSignedIn!=null && isSignedIn.getClass() == String.class ){
			//if(isSignedIn.getClass())
			try {
				valid = Boolean.parseBoolean((String)isSignedIn);
			} catch (Exception e) {
				
			}
		}else if(isSignedIn!=null && isSignedIn.getClass() == Boolean.class ){
			valid=(Boolean)isSignedIn;
		}
		
		if(valid && user!=null){
			return user;
		}
	
		if(!valid){
		
			Object isSignedIn2 = request.getSession().getAttribute("userSignedIn");
			if(isSignedIn2!=null && isSignedIn2.getClass() == String.class ){
				//if(isSignedIn.getClass())
				try {
					valid = Boolean.parseBoolean((String)isSignedIn2);
				} catch (Exception e) {
					
				}
			}else if(isSignedIn2!=null && isSignedIn2.getClass() == Boolean.class ){
				valid=(Boolean)isSignedIn2;
			}
		}
		
		
		String userSignedInUUID = (String)request.getSession().getAttribute("userUUID");
		
		//valid=isSignedIn;
		/*
		
		*/

		if(user!=null)
		{
			if(user.isActive() /*&& user.isAgreedToTermsOfUse() */ && !user.isLockout() )
			{
				if(userSignedInUUID!=null )
				{
					if(userSignedInUUID.equals(uuid) || userSignedInUUID.equals( System.getProperty("adminUser")))
					{
						if(valid)
						{
							return user;
						}
					}
					
					//check admin roles
					List<Role> userRoles = null;
					try {
						userRoles = user.getRoles();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(Role r : userRoles){
						
						if("Administrator".equalsIgnoreCase(r.getName())){
							if(valid)
							{
								return user;
							}
						}else{
					
						}
					}
				}else if( uuid.equals(System.getProperty("adminUser")))
				{
					if(System.getProperty("checkUser").equals("false"))
					{
						return user;
					}
				}
			}
		}
		return null;
	}
	public static User getLiferayUserByEMail(String email)
	{

		if(email==null){
			return null;
		}
		User user = null;
		
		int companyId = LiferayTools.getCompanyId();
		try {
			user = UserLocalServiceUtil.getUserByEmailAddress(companyId, email);
		} catch (PortalException e) {
			//e.printStackTrace();
			System.out.println("getLiferayUserByEMail " +email + " ::failed "); 
		} catch (SystemException e) 
		{
			//e.printStackTrace();
			System.out.println("getLiferayUserByEMail " +email + " ::failed"); 
		}
		return user;
	}
	public static User getLiferayUserByUUID(String uuid){

		User user = null;
		
		try {
			user = UserLocalServiceUtil.getUserByUuidAndCompanyId(uuid, Long.parseLong(System.getProperty("companyId")));
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	public static List<User>getLiferayUsers()
	{
		List<User>result = new ArrayList<User>();
		try {
			result = UserLocalServiceUtil.getGroupUsers(Long.parseLong(System.getProperty("companyId")));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static JournalArticle getLatestVersion(List <JournalArticle>articles,long userId,double version){
		
		for(int i = 0 ; i < articles.size() ; i++)
		{
			JournalArticle article = (JournalArticle) articles.get(i);
		
			if(article.getUserId() == userId && article.getVersion()==version)
			{				
				return article;
			}
		}
		return null;
	}
	public static JournalArticle getLatestVersion(JournalArticle article,String uuid)
	{
		
		ArrayList <JournalArticle>result = new ArrayList<JournalArticle>();
		List articles=null;
		try {
			articles=JournalArticleLocalServiceUtil.getArticles(getScopeId());
		} catch (SystemException e) 
		{
			e.printStackTrace();
			return article;
		}

		User user = LiferayContentTools.getLiferayUserByUUID(uuid);
		if(user==null)
			return article;
		
		//long userId = 
		for(int i = 0 ; i < articles.size() ; i++)
		{
			JournalArticle _article = (JournalArticle) articles.get(i);
			/**
			 * TODO  
			 */
			if(article.getUserId() == user.getUserId())
			{
				double latestVersion=0;
				try {
					latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(getScopeId(),""+article.getArticleId());
				} catch (PortalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(latestVersion!=0){
					JournalArticle latestVersionArticle = LiferayContentTools.getLatestVersion(articles, user.getUserId(), latestVersion);
					
					if(latestVersionArticle !=null)
					{
						return latestVersionArticle;
					}
				}
			}
		}
		return article;
	}
	public static boolean hasArticle(ArrayList<JournalArticle>articles,String id){
		
		for(int i = 0 ; i < articles.size() ; i++)
		{
			JournalArticle article = (JournalArticle) articles.get(i);
			if(article.getArticleId().equals(id)){
				return true;
			}
		}
		
		return false;
	}
	
	public static String setLiferayArticleContentByStructureElement(JournalArticle article,String Element,String newContent)
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db =null; 
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		Document doc = null ; 
		String content = article.getContent();
		
		try {
			doc = db.parse(new ByteArrayInputStream(content.getBytes()));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();

		NodeList nodeLst = doc.getElementsByTagName("dynamic-element");
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	Element fstElmnt = (Element) fstNode;
		    	if(fstElmnt!=null )
		    	{		
		    		String elementName = fstElmnt.getAttribute("name");
		    		if(elementName !=null && elementName.equals(Element))
		    		{
		    			if(StringUtils.setXMLFromElement(fstElmnt, "dynamic-content",newContent))
		    			{
		    				TransformerFactory tf = TransformerFactory.newInstance();
		    				Transformer transformer = null;
							try {
								transformer = tf.newTransformer();
							} catch (TransformerConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return null;
							}
		    				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    				StringWriter writer = new StringWriter();
		    				try {
								transformer.transform(new DOMSource(doc), new StreamResult(writer));
							} catch (TransformerException e) 
							{
								e.printStackTrace();
								return null;
							}
		    				//String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
		    				String output = writer.getBuffer().toString();
		    				if(output!=null)
		    				{
		    					return output;
		    				}

		    			}
		    		}
		    	}
		    }
		}
		return null;
	}
	public static String getLiferayArticleContentByStructureElement(JournalArticle article,String Element)
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		try {
			dbf.setFeature("http://xml.org/sax/features/namespaces", false);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbf.setFeature("http://xml.org/sax/features/validation", false);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DocumentBuilder db =null; 
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}

		db.setEntityResolver(new EntityResolver() {
			
			@Override
			public InputSource resolveEntity(String arg0, String arg1)
					throws SAXException, IOException {
				
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		/*() {
	        @Override
	        public InputSource resolveEntity(String publicId, String systemId)
	                throws SAXException, IOException {
	            if (systemId.contains("foo.dtd")) {
	                return new InputSource(new StringReader(""));
	            } else {
	                return null;
	            }
	        }
	    });
	    */

		/***
		 * 
		 * <?xml version="1.0"?>

	<root available-locales="en_US" default-locale="en_US">
	<static-content language-id="en_US">&lt;p&gt; asdfasdfasdf&lt;/p&gt; &lt;p&gt; asdfasdfasd&lt;/p&gt;</static-content>
	</root>
		 * 
		 */
		
		String name = "static-content";
		
		
		com.liferay.portal.kernel.xml.Document document=null;
		String allContent=article.getContentByLocale(article.getDefaultLocale());
		try {
			document = SAXReaderUtil.read(article.getContentByLocale(article.getDefaultLocale()));
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//com.liferay.portal.kernel.xml.Node node = document.selectSingleNode("/root/dynamic-element[@name='" + name + "']/dynamic-content");
		com.liferay.portal.kernel.xml.Node node = document.selectSingleNode("/root/static-content");
		String value = null;//node.getText();
		if(node!=null){
			//value = node.getText();
		}
		
		
		String content = article.getContent();
		Document doc = null ; 
		
		//db.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		//db.
		
		try {
			doc = db.parse(new ByteArrayInputStream(content.getBytes()));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return null;
		}
		/*
		com.liferay.portal.kernel.xml.Document contentDoc=null;
		try {
			 contentDoc = SAXReaderUtil.read(content);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		

		doc.getDocumentElement().normalize();
		
		doc.getDocumentElement().normalize();

		NodeList nodeLst = doc.getElementsByTagName("dynamic-element");
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	Element fstElmnt = (Element) fstNode;
		    	if(fstElmnt!=null )
		    	{		
		    		String elementName = fstElmnt.getAttribute("name");
		    		if(elementName !=null && elementName.equals(Element))
		    		{
		    			return StringUtils.getXMLFromElement(fstElmnt, "dynamic-content");
		    			
		    			
		    		}
		    		
		    	}
		    }
		}

		/*
		NodeList nodeLst = doc.getElementsByTagName("static-content");
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	Element fstElmnt = (Element) fstNode;
		    	if(fstElmnt!=null )
		    	{		
		    		String elementName = fstElmnt.getAttribute("name");
		    		if(elementName !=null && elementName.equals(Element))
		    		{
		    			return StringUtils.getXMLFromElement(fstElmnt, "static-content");
		    		}
		    	}
		    }
		}
		
		*/
		return null;
	}
	public static JournalArticle getArticleByUserIdAndResourceId(String userId,long resourceId)
	{
		//mc007ibi.dyndns.org:8080/XApp-portlet/content?action=detail&refId=53004&cType=55&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&appIdentifier=qxevents
		List <JournalArticle>articles = LiferayContentTools.getArticlesByUserId(userId,-1);
	
		articles=null;
		try {
			articles=JournalArticleLocalServiceUtil.getArticles(getScopeId());
		} catch (SystemException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		
		for(int i = 0 ; i < articles.size() ; i++)
		{
			JournalArticle article = (JournalArticle) articles.get(i);
			long aid = Long.parseLong(article.getArticleId());
			long primId=article.getResourcePrimKey();
			long primId2=article.getPrimaryKey();
			//System.out.println("p" + primId);
			JournalArticle _latest=LiferayContentTools.getLatestVersion(article, userId);
			
			if( primId==resourceId )
			{
				return article;
			}
		}
		
		JournalArticle article = null;
		/*
		try {
			article = JournalArticleLocalServiceUtil.getArticle(articleRefId);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<JournalArticle>resArticles=null;
		*/
		//resArticles=JournalArticleLocalServiceUtil.get
		return null;
		
	}
	
	public static JournalArticle getArticleByUserIdAndArticleId(String userId,String articleId)
	{
		//mc007ibi.dyndns.org:8080/XApp-portlet/content?action=detail&refId=53004&cType=55&uuid=11166763-e89c-44ba-aba7-4e9f4fdf97a9&appIdentifier=qxevents
		ArrayList <JournalArticle>articles = LiferayContentTools.getArticlesByUserId(userId,-1);
		long articleRefId = -1; 
		try {
			articleRefId=Long.parseLong(articleId);
		} catch (Exception e) {
			return null;
		}
		
		
		
		for(int i = 0 ; i < articles.size() ; i++)
		{
			JournalArticle article = (JournalArticle) articles.get(i);
			long aid = Long.parseLong(article.getArticleId());
			long primId=article.getResourcePrimKey();
			long primId2=article.getPrimaryKey();
			//System.out.println("p" + primId);
			JournalArticle _latest=LiferayContentTools.getLatestVersion(article, userId);
			
			if( primId==articleRefId )
			{
				return article;
			}
		}
		
		JournalArticle article = null;
		
		try {
			article = JournalArticleLocalServiceUtil.getArticle(articleRefId);
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<JournalArticle>resArticles=null;
		//resArticles=JournalArticleLocalServiceUtil.get
		return null;
		
	}
	public static ArrayList<JournalArticle>getArticlesByUserId(String userId,int categoryId)
	{
		ArrayList <JournalArticle>result = new ArrayList<JournalArticle>();
		List articles=null;
		try {
			articles=JournalArticleLocalServiceUtil.getArticles(getScopeId());
		} catch (SystemException e) 
		{
			e.printStackTrace();
			return null;
		}

		User user = LiferayContentTools.getLiferayUserByUUID(userId);
		if(user==null)
			return null;
		
		//long userId = 
		for(int i = 0 ; i < articles.size() ; i++)
		{
			
			JournalArticle article = (JournalArticle) articles.get(i);
			//if category given, make a test:

			/**
			 * TODO  
			 */
			//List<AssetCategory> categories = AssetCategoryLocalServiceUtil.getCategories(JournalArticle.class.getName(), article1.getResourcePrimKey());
			//JournalArticleServiceUtil.getArticlesByArticleId(groupId, articleId, start, end, obc)
			if(article.getUserId() == user.getUserId())
			{
				double latestVersion=0;
				try {
					latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(getScopeId(),""+article.getArticleId());
				} catch (PortalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(latestVersion!=0){
					JournalArticle latestVersionArticle = LiferayContentTools.getLatestVersion(articles, user.getUserId(), latestVersion);
					
					if(latestVersionArticle !=null && !LiferayContentTools.hasArticle(result, latestVersionArticle.getArticleId()))
					{
						String structureId=latestVersionArticle.getStructureId();
						if(latestVersionArticle.getStructureId()!=null  && latestVersionArticle.getStructureId().length() >0)
						{
							//result.add(latestVersionArticle);
						}
						
						result.add(latestVersionArticle);
					}
				}
				//result.add(article);
			}
		}
		
		
		
		return result;
		
	}
	
	public static ArrayList<JournalArticle>getArticlesByUserIdAndParent(String userId,int categoryId)
	{
		ArrayList <JournalArticle>result = new ArrayList<JournalArticle>();
		
		List articles=null;
		try {
			//JournalArticleLocalServiceUtil.getArtic
			articles=JournalArticleLocalServiceUtil.getArticles(getScopeId());
			//articles=(ArrayList<JournalArticle>) JournalArticleLocalServiceUtil.getArticles();
		} catch (SystemException e) 
		{
			e.printStackTrace();
			return null;
		}

		User user = LiferayContentTools.getLiferayUserByUUID(userId);
		if(user==null)
			return null;
		
		//long userId = 
		for(int i = 0 ; i < articles.size() ; i++)
		{
			
			JournalArticle article = (JournalArticle) articles.get(i);
			//if category given, make a test:

			/*
			 * {uuid=705b4765-de0f-40bd-8f88-9959bcd22454, id=50004, resourcePrimKey=49804, groupId=49104, companyId=10153, userId=10195, userName

			 */
			/**
			 * TODO  
			 */
			//List<AssetCategory> categories = AssetCategoryLocalServiceUtil.getCategories(JournalArticle.class.getName(), article1.getResourcePrimKey());
			//JournalArticleServiceUtil.getArticlesByArticleId(groupId, articleId, start, end, obc)
			if(article.getUserId() == user.getUserId())
			{
				double latestVersion=0;
				try {
					latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(getScopeId(),""+article.getArticleId());
				} catch (PortalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(latestVersion!=0){
					JournalArticle latestVersionArticle = LiferayContentTools.getLatestVersion(articles, user.getUserId(), latestVersion);
					if(latestVersionArticle !=null && !LiferayContentTools.hasArticle(result, latestVersionArticle.getArticleId()))
					{
						String structureId=latestVersionArticle.getStructureId();
						if(latestVersionArticle.getStructureId()!=null  && latestVersionArticle.getStructureId().length() >0)
						{
							//result.add(latestVersionArticle);
						}
						
						result.add(latestVersionArticle);
					}
				}
				//result.add(article);
			}
		}
		
		
		
		return result;
		
	}

}
