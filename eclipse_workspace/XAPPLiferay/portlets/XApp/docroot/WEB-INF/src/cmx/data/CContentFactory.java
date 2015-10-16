package cmx.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.jcraft.jsch.jce.MD5;

import cmx.cache.DataSourceCache;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;
import pmedia.DataManager.ServerCache;
import pmedia.DataUtils.MediaUtils;
import pmedia.html.types.ClientFilter;
import pmedia.html.types.WhiteListData;
import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CField;
import pmedia.types.Category;
import pmedia.types.Constants;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MediaItemBase;
import pmedia.types.MosetItem;
import pmedia.types.ProductData;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.TimeUtils;

public class CContentFactory 
{

	public static void setCustomFieldsWithEvent(
			CContent content,
			EventData data,
			ApplicationManager  applicationManager,
			Application  application,
			DataSourceBase ds,
			DataSourceCache dsc,
			String platform,
			String lang)
	{
		if(data.locRefId!=0)
		{
			CField locLink  = new CField();
			locLink.title = "";
			if(data.loc!=null)
			{ 
				LocationData loc = data.loc;
				locLink.title =data.loc.title;
				loc.populateStatusVariables();
				String locIcon=loc.getIconUrl();
				if(locIcon!=null&&locIcon.length()>0){
					if(!locIcon.contains("http")){
						if(ds.getUrl()!=null){
							locIcon=ds.getUrl()+"/"+locIcon;
							locLink.setIconUrl(locIcon);
						}
					}
				}
				
			}
			locLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsLocationItem);
			locLink.setRefId(data.locRefId);
			locLink.setDataSource(ds.getUid());
			content.getCustomFields().add(locLink);
		}else if(data.locRefStr!=null)
		{
			CField locLink  = new CField();
			locLink.title =data.locRefStr;
			locLink.setDataSource(ds.getUid());
			locLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.TEXT);
			content.getCustomFields().add(locLink);
		}
	}
	public static void setCustomFieldsWithMosetItem(
			CContent content,
			MosetItem data,
			ApplicationManager  applicationManager,
			Application  application,
			DataSourceBase ds,
			DataSourceCache dsc,
			String platform,
			String lang)
	{
		if(data.latitude!=null && data.longtitude!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = data.latitude+"|" + data.longtitude;
			String title = "Show onMap";
			
			if(data.street!=null)
			{
				title=data.street;
				
				if(data.city!=null){
					title+="\n"+data.city;
				}
				if(data.pcode!=null){
					title +=" " + data.pcode;
				}
			}
			mapLink.setTitle(title);
			mapLink.sourceType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MAP);
			content.getCustomFields().add(mapLink);
			mapLink.setDataSource(ds.getUid());
		}
		/*
		if(data.street!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = data.street;
			mapLink.title = data.street;
			if(data.pcode !=null)
			{
				mapLink.title+=" " + data.pcode;
			}
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.TEXT);
			content.getCustomFields().add(mapLink);
		}
		*/
		
		if(data.phone!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = data.phone;
			mapLink.title = data.phone;
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.PHONE);
			content.getCustomFields().add(mapLink);
		}
		
		/*
		if(data.www!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = data.www;
			mapLink.title = data.www;
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
			content.getCustomFields().add(mapLink);
		}
		*/
		
		
	}
	public static void setCustomFieldsWithLocation(
			CContent content,
			LocationData data,
			ApplicationManager  applicationManager,
			Application  application,
			DataSourceBase ds,
			DataSourceCache dsc,
			String platform,
			String lang)
	{
		if(content.customFields!=null){
			content.customFields.clear();
		}else{
			content.setCustomFields(new ArrayList<CField>());
		}
		if(data.latitude!=null && data.longtitude!=null)
		{
			CField mapLink  = new CField();
			mapLink.value = data.latitude+"|" + data.longtitude;
			mapLink.title = "Show onMap";
			mapLink.sourceType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.MAP);
			content.getCustomFields().add(mapLink);
		}
		
		if(data.street!=null && data.street.length() > 0)
		{
			CField mapLink  = new CField();
			mapLink.value = data.street;
			mapLink.title = data.street;
			if(data.pcode !=null)
			{
				mapLink.title+=" " + data.pcode;
			}
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.TEXT);
			content.getCustomFields().add(mapLink);
		}
		
		if(data.phone!=null && data.phone.length() > 0)
		{
			CField mapLink  = new CField();
			mapLink.value = data.phone;
			mapLink.title = data.phone;
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.PHONE);
			content.getCustomFields().add(mapLink);
		}
		
		if(data.www!=null && data.www.length() > 0)
		{
			CField mapLink  = new CField();
			mapLink.value = data.www;
			mapLink.title = data.www.replace("http://", "");
			mapLink.sourceType=ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
			content.getCustomFields().add(mapLink);
		}
		
		
	}
	
	public static void addJoomlaFields(
			CContent content,
			ArticleData data,
			ApplicationManager  applicationManager,
			Application  application,
			DataSourceBase ds,
			DataSourceCache dsc,
			String platform,
			String lang)
	{
		if(content==null){
			return;
		}
		
		if(data == null){
			return;
		}
		if(content.customFields==null){
			return;
		}
		if(data.description==null){
			return;
		}
		Pattern p=null;
		Matcher m= null;
		p = Pattern.compile("\\s*(?i)youtube}\\s*(([^{/]*))");
		m= p.matcher(data.description);
		while (m.find())
		{
			try {
				String word0=m.group(1);
				if(word0!=null&& word0.length()>5){
					CField youtubeLink  = new CField();
					youtubeLink.value ="http://www.youtube.com/embed/"+word0;
					youtubeLink.title = "Play Video";
					youtubeLink.sourceType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
					content.getCustomFields().add(youtubeLink);
					content.description = content.description.replace("{youtube}" + word0 +"{/youtube}", "");
					content.description = content.description.replace("<p>" + word0  + "</p>", "");
					content.description = content.description.replace(word0, "");//2hmJURr5c1w
				}
			} catch (Exception e) {

			}
    		
		}
	}
	public static void addCustomFieldsWithArticle(
			CContent content,
			ArticleData data,
			ApplicationManager  applicationManager,
			Application  application,
			DataSourceBase ds,
			DataSourceCache dsc,
			String platform,
			String lang)
	{
		
		if(content.customFields!=null){
			//content.customFields.clear();
		}else{
			content.setCustomFields(new ArrayList<CField>());
		}
		addJoomlaFields(content, data, applicationManager, application, ds, dsc, platform, lang);
		/*
		CField mapLink  = new CField();
		//MD5 md5 = new MD5();
		mapLink.value =""+data.refId;
		mapLink.title = "Show Comments";
		mapLink.sourceType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.DISQUS);
		content.getCustomFields().add(mapLink);
		*/
		
	}
	public static void setCustomFieldsWithArticle(
			CContent content,
			ArticleData data,
			ApplicationManager  applicationManager,
			Application  application,
			DataSourceBase ds,
			DataSourceCache dsc,
			String platform,
			String lang)
	{
		if(content.customFields!=null){
			content.customFields.clear();
		}else{
			content.setCustomFields(new ArrayList<CField>());
		}
		addCustomFieldsWithArticle(content, data, applicationManager, application, ds, dsc, platform, lang);
		/*
		CField mapLink  = new CField();
		//MD5 md5 = new MD5();
		mapLink.value =""+data.refId;
		mapLink.title = "Show Comments";
		mapLink.sourceType = ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.DISQUS);
		content.getCustomFields().add(mapLink);
		*/
		
	}
	public static void setCustomFields(
			CContent content,
			BaseData data,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			String platform,
			String lang)
	{
		ApplicationManager appManager = ApplicationManager.getInstance();

		Application app = appManager.getApplication(uuid, applicationId, false);
		DataSourceBase ds = null;
		if(app!=null)
		{
			ds = app.getDataSource(dsUID);
		}
		if(lang==null)
		{
			lang="en";
		}
		if(platform==null)
		{
			platform=Constants.USERAGENT_IPHONE;
		}
		
		if(app==null){
			return;
		}
		
		if(ds==null)
		{
			return;
		}
		
		DataSourceCache dsc=null;
    	try {
    		dsc = ServerCache.getDSC(appManager, app, ds);	
		} catch (Exception e) 
		{
			e.printStackTrace();
			// TODO: handle exception
		}
    	
    	if(dsc==null)
    	{
    		return;
    	}
    	
    	if(content.getCustomFields()!=null && content.getCustomFields().size() > 0)
    	{
    		return;
    	}
    	
    	if(content.getCustomFields()==null)
    	{
    		content.setCustomFields(new ArrayList<CField>());
    	}
    	
    	if(data instanceof ArticleData && !(data instanceof MosetItem))
		{
    		setCustomFieldsWithArticle(content,(ArticleData)data, appManager, app, ds, dsc, platform, lang);
		}
    	
    	if(data instanceof LocationData && !(data instanceof MosetItem))
		{
    		setCustomFieldsWithLocation(content,(LocationData)data, appManager, app, ds, dsc, platform, lang);
		}
    	
    	if(data instanceof MosetItem)
		{
    		setCustomFieldsWithMosetItem(content,(MosetItem)data, appManager, app, ds, dsc, platform, lang);
		}
    	
    	if(data instanceof EventData)
		{
    		setCustomFieldsWithEvent(content,(EventData)data, appManager, app, ds, dsc, platform, lang);
		}
    	
		
	}
	public static void setBaseData(CContent content,
			BaseData data,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			String baseRef )
	{
		content.setApplicationId(applicationId);
		content.setUuid(uuid);
		content.setRefId(data.refId);
		content.setTitle(data.getTitle());
		content.setGroupId(data.groupId);
		
		if(data.created!=null)
		{
			content.setCreationDate(data.created);
		}
		
		//@TODO silly update
		if(data.getGroupId()!=0 && content.getGroupId()==0)
		{
			content.setGroupId(data.getGroupId());
		}
		
		content.setDataSourceUID(dsUID);
		content.published=data.published;
		content.setOwnerRefId(data.getOwnerRefId());
		content.setOwnerRefStr(data.getOwnerRefStr());
		if(data instanceof LocationData)
		{
			content.latitude=data.latitude;
			content.longtitude=data.longtitude;
		}
		
		if(data.getIconUrl()!=null && data.getIconUrl().length()>0)
		{
			content.setIconUrl(data.getIconUrl());
		}
		
		if(data instanceof ArticleData)
		{
			//content.setGroupId(data.groupId);
		}
		
		if(data instanceof Category)
		{
			content.setGroupId(data.groupId);
			if(data.getIntroText()!=null){
				content.setIntroText(data.getIntroText());
			}
		}
		
		if(data instanceof ProductData)
		{
			content.setPrice(((ProductData) data).getPrice());
			content.setCurrency(((ProductData) data).getCurrency());
			
		}
		
		if(data.getRefGroups()!=null)
		{
			content.setRefGroups(data.getRefGroups());
		}
		
		if(data.getRefGroupsStr()!=null)
		{
			content.setRefGroupsStr(data.getRefGroupsStr());
		}
		
		if(data.sourceType !=null && data.sourceType.equals(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.WordpressPost)))
		{
			//System.out.println("wp");
		}
		
		content.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(cType));
		
		if(data instanceof MosetItem)
		{
			
			MosetItem item = (MosetItem)data;
			if(item.city!=null && item.city.length() > 0){
				content.setLocRefStr(item.city);
			}
		}
		
		if(data instanceof EventData)
		{
			
			//content.setGroupId(data.groupId);
			EventData event = (EventData) data;
			content.setStartDate(((EventData) data).getStart_time());
			content.setEndDate(((EventData) data).getEnd_time());
			content.setGroupId(data.groupId);
			content.setGroupStr(data.groupStr);
			content.setLocRefId(data.locRefId);
			content.setLocRefStr(data.locRefStr);
			content.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.JEventsItem));
			
			//content.setRefId(event.getRefId());
		}
		
		
		content.setBaseRef(baseRef);
		
	}
	public static CContent fromLocationData(
			LocationData data,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			WhiteListData whiteList,
			String platform,
			String lang,
			int maxSize
			)
	{
		
		ApplicationManager appManager = ApplicationManager.getInstance();

		Application app = appManager.getApplication(uuid, applicationId, false);
		DataSourceBase ds = null;
		if(app!=null)
		{
			ds = app.getDataSource(dsUID);
		}
		if(lang==null)
		{
			lang="en";
		}
		if(platform==null)
		{
			platform=Constants.USERAGENT_IPHONE;
		}
		CContent content = new CContent();
		setBaseData(content, data, uuid, applicationId, dsUID, cType, ds.getUrl());
		
		String rawDescription = data.getDescription();
		String baseRef ="";
		if(ds!=null){
			baseRef = ds.getUrl();
		}
		String newRawDescription = ClientFilter.filter(rawDescription, uuid, applicationId, dsUID, baseRef,cType,"" + data.getRefId(), whiteList, platform, lang, maxSize);
		content.setDescription(newRawDescription);
		
		ArrayList<MediaItemBase> pictureItems= MediaUtils.getPictureItemsFromText(newRawDescription);
		if(pictureItems!=null && pictureItems.size() > 0)
		{
			content.setPictureItems(pictureItems);
		}
		return content;
	}
	
	public static String mergePictureItems(ArrayList<MediaItemBase> src,String description,String baseRef)
	{
		if(src ==null){
			return description;
		}
		
		String result=description;
		
		String insertPicture="";
		for (int i = 0; i < src.size(); i++) 
		{
			MediaItemBase item = src.get(i);
			if(item.getFullSizeLocation() !=null &&!result.contains(item.getFullSizeLocation()))
			{
				insertPicture+="<img src=\"" + item.getFullSizeLocation() +"\"></img>";
			}
		}
		
		return insertPicture+result;
	}
	
	public static CContent fromArticleData(BaseData data,
			String uuid, 
			String applicationId , 
			String  dsUID, 
			ECMContentSourceType cType,
			WhiteListData whiteList,
			String platform,
			String lang,
			int maxSize
			)
	{
		
		ApplicationManager appManager = ApplicationManager.getInstance();

		Application app = appManager.getApplication(uuid, applicationId, false);
		DataSourceBase ds = null;
		if(app!=null)
		{
			ds = app.getDataSource(dsUID);
		}
		if(lang==null)
		{
			lang="en";
		}
		if(platform==null)
		{
			platform=Constants.USERAGENT_IPHONE;
		}
		CContent content = new CContent();
		if(data instanceof EventData)
		{
			Date now = new Date();
			EventData e = (EventData)data;
			if(e.start_time!=null){
				long diff  = TimeUtils.daysBetween(now,e.start_time);
				if(diff<0){
					//return null;
				}
			}
		}
		
		setBaseData(content, data, uuid, applicationId, dsUID, cType, ds.getUrl());
		String rawDescription = data.getDescription();
		String baseRef ="";
		if(ds!=null)
		{
			baseRef = ds.getUrl();
		}
		String newRawDescription = ClientFilter.filter(rawDescription, uuid, applicationId, dsUID, baseRef,cType,"" + data.getRefId(), whiteList, platform, lang, maxSize);
		
		
		
		
		if(data.getPictureItems()!=null)
		{
			content.setPictureItems(new ArrayList<MediaItemBase>());
			//content.getPictureItems().addAll(data.getPictureItems());
			MediaUtils.addPictureItems(data.getPictureItems(), content.getPictureItems(),ds.getUrl());
		}
		
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
		
		
		if(content.introText!=null && content.introText.length()==0){
			content.introText=null;
		}
		
		if(content.localizedCategoryTitle!=null && content.localizedCategoryTitle.length()==0){
			content.localizedCategoryTitle=null;
		}
		
		if(content.descriptionPicture!=null && content.descriptionPicture.length()==0){
			content.descriptionPicture=null;
		}
		
		if(content.descriptionExtra!=null && content.descriptionExtra.length()==0){
			content.descriptionExtra=null;
		}
		
		
		if(content.video!=null && content.video.length()==0){
			content.video=null;
		}
		
		if(content.description!=null){
			content.setDescription(content.getDescription().replaceAll("\n", ""));
		}
		if(content.introText!=null){
			content.setIntroText(content.getIntroText().replaceAll("\n", ""));
		}
		content.setGalleryFiles(null);
		content.setGalleryThumbnailFiles(null);
		
		
		
		
		return content;
	}
	
}
