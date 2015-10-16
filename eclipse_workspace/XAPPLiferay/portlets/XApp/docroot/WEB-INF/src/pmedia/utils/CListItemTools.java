
package pmedia.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import pmedia.types.ArticleData;
import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CEventItem;
import pmedia.types.CFeedItem;
import pmedia.types.CListDateComp;
import pmedia.types.CListDateCompReverse;
import pmedia.types.CListItem;
import pmedia.types.EventData;
import cmx.types.DataSourceBase;
import cmx.types.ECMContentSourceType;

import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.module.itunes.AbstractITunesObject;
import com.sun.syndication.feed.module.itunes.EntryInformation;
import com.sun.syndication.feed.module.itunes.FeedInformation;
import com.sun.syndication.feed.module.itunes.FeedInformationImpl;
import com.sun.syndication.feed.module.itunes.ITunes;
import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.module.mediarss.MediaEntryModuleImpl;
import com.sun.syndication.feed.module.mediarss.MediaModule;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.MediaGroup;
import com.sun.syndication.feed.module.mediarss.types.Thumbnail;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class CListItemTools {

	
		public static ArrayList<CListItem>toCListItems(ArrayList<CContent>data)
	   {
			
			ArrayList<CListItem>result = new ArrayList<CListItem>();
			for (int s = 0; s < data.size() ; s++) 
			{
				CContent c = data.get(s);
				CListItem listItem = new CListItem();
				listItem.setRefId(c.refId);
				listItem.setCreationDate(c.getCreationDate());
				listItem.setRefIdStr(c.refIdStr);
				listItem.setGroupId(c.groupId);
				listItem.setGroupIdStr(c.groupStr);
				listItem.setTitle(c.title);
				listItem.setDataClass("pmedia.types.BaseData");
				listItem.setIconUrl(c.getIconUrl());
				listItem.setOwnerRefId(c.getOwnerRefId());
				listItem.setOwnerRefStr(c.getOwnerRefStr());
				listItem.setHasSubGroups(c.hasSubCategories);
				listItem.setSourceType(c.getSourceType());
				listItem.setWebLink(c.getFileRef());
				listItem.setLocRefStr(c.getLocRefStr());
				result.add(listItem);
				//result.add(data.get(s).getAsListItem());
			}
				
			return result;
	   }
	public static CListItem toCListItem(BaseData dataItem,Locale locale,DataSourceBase ds,String newItemClass,ECMContentSourceType sourceType)
	   {
		   	CListItem item = dataItem.getAsListItem();
			if(dataItem.created!=null)
			{
				DateFormat df = new SimpleDateFormat("EEEE  HH:mm");
				item.setDateString(df.format(dataItem.created));
			}
			if(newItemClass!=null)
			{
				item.dataClass = newItemClass;
			}
			if(sourceType!=null)
			{
				item.sourceType = "" + ECMContentSourceTypeTools.TypeToInteger(sourceType);
			}
			return item;
	   }
	   /*
	   public ArrayList<CList>buildSectionedEvents(Vector<EventData>src,Locale locale)
	   {
		   if(src==null)
			   return null;
		   ArrayList<CList>result = new ArrayList<CList>();
		   
		   EventData currentElement = src.get(0);
		   Date currentDate = currentElement.start_time;
		   
		   CList dst  = new CList();
		   dst.getItems().add(currentElement);
		   
		   String dformat= new SimpleDateFormat("dd. EEEE MMM",locale).format(currentElement.start_time);
		   dst.setTitle(dformat);

		   Calendar startCDate = Calendar.getInstance();
		   Calendar eCDate = Calendar.getInstance();
		   
		   startCDate.setTime(currentDate);
		   startCDate.set(Calendar.HOUR, 0);
		   startCDate.set(Calendar.MINUTE, 0);
		   currentDate = startCDate.getTime();
		   
		   int maxDiff = 4;
		   int  dDay = startCDate.get(Calendar.DAY_OF_YEAR); 

		   for(int i = 1 ; i < src.size() ; i++)
		   {
			   EventData nextElement = src.get(i);
			
			   eCDate.setTime(nextElement.start_time);
			   eCDate.set(Calendar.HOUR, 1);
			   eCDate.set(Calendar.MINUTE, 1);
			   Date eDate = eCDate.getTime();
			   long diff  = TimeUtils.daysBetween(currentDate,eCDate.getTime());
			   long diffH  = TimeUtils.daysBetween(currentDate,eCDate.getTime());
			   int  cdDay = eCDate.get(Calendar.DAY_OF_YEAR); 
			   if(maxDiff!=0)
			   {
				   if(result.size() >=maxDiff )
				   {
					   break;
				   }
			   }
			   //if(diff>0)
			   
			   if(cdDay!=dDay)
			   {
				   //System.out.println("e:" + nextElement.summary + " diff : " + diff + " diffH  : " + diffH);
				   //new section
				   dDay=cdDay;
				   currentElement = nextElement;
				   currentDate = eCDate.getTime();
				   dst = new CList();
				   dformat= new SimpleDateFormat("dd. EEEE MMM",locale).format(currentElement.start_time);
				   dst.setSectionText(dformat);
				   result.add(dst);
				   
			   }
			   dst.getEvents().add(nextElement);
		   }
		   return result;
		   
	   }*/
	public static ArrayList<CListItem> adjustIconUrl(ArrayList<CListItem>items,DataSourceBase ds)
	{
		for (int s = 0; s < items.size() ; s++) 
		{
			CListItem item = items.get(s);
			if(item.getIconUrl()!=null && item.getIconUrl().length() > 1)
			{
				String prefix ="";
				if(!item.getIconUrl().contains("http") && ds!=null && ds.getUrl() !=null)
				{
					prefix = ds.getUrl();
					
				}
				//String newIconUrl=System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + prefix +"/" +  item.iconUrl + "&width=80";
				//item.setIconUrl(newIconUrl);
			}
		}
		return items;
	}
	public static ArrayList<CListItem> setBaseRef(ArrayList<CListItem>items,DataSourceBase ds)
	{
		if(ds!=null && ds.getUrl()!=null)
		{
			for (int s = 0; s < items.size() ; s++) 
			{
				CListItem item = items.get(s);
				item.setBaseRef(ds.getUrl() +"/");
			}
		}
		return items;
	}
	public static CListItem getByRef(ArrayList<CListItem>items,String ref)
	{
		for (int s = 0; s < items.size() ; s++) 
		{
			CListItem item = items.get(s);
			if(item.getRef()!=null && item.getRef().equals(ref))
			{
				return item;
			}
		}
		return null;
	}
	public static CListItem getByRefIdStr(ArrayList<CListItem>items,String refIdStr)
	{
		for (int s = 0; s < items.size() ; s++) 
		{
			CListItem item = items.get(s);
			if(item.getRefIdStr()!=null && item.getRefIdStr().equals(refIdStr))
			{
				return item;
			}
		}
		return null;
	}
	
	public static ArrayList<CListItem>getByGroupId(ArrayList<CListItem>items,int groupId)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		for (int s = 0; s < items.size() ; s++) 
		{
			CListItem item = items.get(s);
			if(item.getGroupId() == groupId)
			{
				result.add(item);
			}
		}
		return result;
	}
	public static ArrayList<CListItem>getByGroupId(ArrayList<CListItem>items,String groupId)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		for (int s = 0; s < items.size() ; s++) 
		{
			CListItem item = items.get(s);
			if(item.getGroupIdStr()!=null && item.getGroupIdStr().equals(groupId)){
				result.add(item);
			}
		}
		return result;
	}
	public static void addMediaItems(MediaModule mediaModule,CListItem item)
	{
		if (mediaModule!=null && mediaModule instanceof MediaEntryModule ){
	        MediaEntryModule mentry = (MediaEntryModule ) mediaModule;

	        for (MediaGroup mg : mentry.getMediaGroups()) 
	        {
	            for (MediaContent mc : mg.getContents()) 
	            {
	            	
	                if (mc.getType()!=null && mc.getType().startsWith("image")) 
	                {
	                    String imgUrl = mc.getReference().toString();
	                    item.picture = imgUrl;
	                    item.sourceType = "" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GALLERY);
	                    item.refIdStr= java.net.URLDecoder.decode(item.ref);
	                    item.mediaType="image";
	                }
	                
	                Thumbnail[] mThumbs = mg.getMetadata().getThumbnail();
	                if(mThumbs!=null && mThumbs[0]!=null)
	                {
	                	Thumbnail tn = mThumbs[0];
	                	item.iconUrl = tn.getUrl().toString();
	                }
	            }
	        }
		}
	}
	public static void addPodcastItems(Module mediaModule,CListItem item)
	{
		if (mediaModule!=null && mediaModule instanceof ITunes )
		{
	        EntryInformation mentry = (EntryInformation)mediaModule;
	        if(mentry.getDuration()!=null)
	        {
	        	CFeedItem feedItem = (CFeedItem)item;
	        	feedItem.setDuration(mentry.getDuration().getMilliseconds());
	        	float _secs = mentry.getDuration().getMilliseconds() /1000;
	            int _minutes = (int)(_secs / 60);
	            int _seconds = ((int)_secs % 60);
	            feedItem.setDateString( _minutes +":" + _seconds +"");
	            if(mentry.getSummary()!=null){
	            	feedItem.setIntroText(mentry.getSummary());
	            }
	            ITunes itunesModule = (ITunes)mediaModule;
	            if(itunesModule!=null)
	            {
	            	/*
	            	FeedInformation fInfo= (FeedInformation)itunesModule;
	            	if(fInfo!=null){
	            		if(fInfo.getImage()!=null)
		            	{
		            		feedItem.setIconUrl(fInfo.getImage().toString());
		            	}
	            	}
	            	*/
	            	//mentry.
	            	//itunesModule.
	            	/*
	            	FeedInformationImpl feedInfo = (FeedInformationImpl) itunesModule;
		            if(feedInfo!=null)
		            {
		            	if(feedInfo.getImage()!=null)
		            	{
		            		//feedItem.setIconUrl(feedInfo.getImage().toString());
		            	}
		            }
		            */
	            }
	            
	            /*
	            FeedInformationImpl feedInfo = (FeedInformationImpl) mediaModule;
	            if(feedInfo!=null)
	            {
	            	if(feedInfo.getImage()!=null)
	            	{
	            		feedItem.setIconUrl(feedInfo.getImage().toString());
	            	}
	            }
	            */
	            //mentry.getAuthor().get
	            //mentry.
	            
	            /*
	            org.rometool.feed.module.itunes.FeedInformationImpl
	            ITunes itunesModule = (ITunes)mediaModule;
	            if(itunesModule!=null)
	            {
	            	//if(itunesModule.)
	            }
	            */
	        }
		}
	}
	public static ArrayList<CListItem> toListItems(SyndFeed feed,String newItemClass)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		List feedEntries = feed.getEntries();
		for (Object c : feedEntries) 
		{
            SyndEntry syndicateEntry = (SyndEntry)c;
        	CFeedItem item = new CFeedItem();
        	item.setTitle(syndicateEntry.getTitle().trim());
        	
        	if(syndicateEntry.getLink()!=null)
        	{
        		String theLink = syndicateEntry.getLink();
        		if(theLink.endsWith("php") || theLink.endsWith("html") || theLink.endsWith("htm")){
        			theLink+="?full=1";
        		}
        		//System.out.println("the link : " + theLink);
        		item.setRef(java.net.URLEncoder.encode(theLink));
        		//item.setRefId(java.net.URLEncoder.encode(syndicateEntry.getLink()));
        	}
        	
        	DateFormat df = new SimpleDateFormat("EEEE  HH:mm");
        	try{
	        	if(df!=null && syndicateEntry.getPublishedDate()!=null){
	        		item.setDateString(df.format(syndicateEntry.getPublishedDate()));
	        	}
	        	
	        	if(syndicateEntry.getPublishedDate() !=null)
	        	{
	        		item.setCreationDate(syndicateEntry.getPublishedDate());
	        		item.setCreated("" + syndicateEntry.getPublishedDate().getTime());
	        	}
        	}catch(Exception e){
        		
        	}

        	SyndContent content = syndicateEntry.getDescription();
        	List contents = syndicateEntry.getContents();
        	List enclosures = syndicateEntry.getEnclosures();
        	if(content!=null && content.getValue()!=null)
        	{
        		String contentStr = content.getValue();
        		contentStr=StringUtils.removeHTML(contentStr);
        		if(contentStr.length() > 80){
        			//contentStr=contentStr.substring(0,79);
        		}
        		item.setIntroText(contentStr);
        	}
        	if(contents !=null && contents.size() > 0){
        		
        		SyndContent sContent = (SyndContent)contents.get(0);
        		String cEntry = sContent.toString();
        		String icon = StringUtils.getFirstPicture(cEntry);
        		if(icon!=null && icon.length() > 0)
        		{
        			item.setIconUrl(icon);
        			//item.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.RSS)
        		}else{
        			if(feed.getLink() !=null){
        				item.setIconUrl( feed.getLink() + "/favicon.ico" );
        			}
        		}
        	}
        	result.add(item);
			if(newItemClass!=null)
			{
				item.dataClass = newItemClass;
				item.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.ExternalLink);
			}
			
			/***
			 * Parse Media Feed
			 */
			//try podcast
			Module module = syndicateEntry.getModule("http://www.itunes.com/dtds/podcast-1.0.dtd");
			if(module!=null)
			{
				addPodcastItems(module, item);
				Module imodule = feed.getModule(AbstractITunesObject.URI);
		        FeedInformationImpl feedInfo = (FeedInformationImpl) imodule;
		        
				
				//List enclosures = syndicateEntry.getEnclosures();
				if(enclosures!=null){
					if(enclosures.size() >0)
					{
						
						SyndEnclosureImpl eEntry = (SyndEnclosureImpl) enclosures.get(0);
						if(eEntry!=null)
						{
							 //eEntry.getType()
							if(eEntry.getType()!=null && eEntry.getType().equals("audio/mpeg"))
							{
								item.sourceType=""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.RADIO);
							}
							if(eEntry.getUrl()!=null )
							{
								item.webLink= java.net.URLDecoder.decode(eEntry.getUrl());
								item.ref=java.net.URLDecoder.decode(eEntry.getUrl());
								/*
								item.webLink= java.net.URLEncoder.encode(eEntry.getUrl());
								item.ref=java.net.URLEncoder.encode(eEntry.getUrl());
								*/
							}
							
							if(feedInfo !=null && feedInfo.getImage()!=null)
							{
								//item.setIconUrl(newIconUrl);
								String newIconUrl=System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + feedInfo.getImage().toString() + "&width=120";
								//item.setIconUrl(feedInfo.getImage().toString());
								item.setIconUrl(newIconUrl);
								
							}
						}
						
					}
				}
				
			}else{			  
				MediaModule mediaModule =  (MediaEntryModuleImpl)syndicateEntry.getModule(MediaEntryModule.URI);
				if(mediaModule!=null)
				{
					addMediaItems(mediaModule, item);
				}
			}
			/*
			MediaEntryModuleImpl mModule =  (MediaEntryModuleImpl)syndicateEntry.getModule(MediaEntryModule.URI);
			if(mModule!=null){
				
				
				MediaModule[] mod = (MediaModule[]) mModule.getMediaGroups();
				
				 Thumbnail[] mThumbs = mModule.getMetadata().getThumbnail();
				 if (mThumbs != null) {
	                    for (int i = 0; i < mThumbs.length; i++) {
	                        String imgUrl = mThumbs[i].getUrl().getPath();
	                        System.out.println("got MediaModule img " + i + ": " + imgUrl);
	                    }
	                }
	                System.out.println("MediaModule title: " + mModule.getMetadata().getTitle());
	                if (mModule.getMetadata(). != null) {
	                    for (int i = 0; i < mModule.get().length; i++) {
	                        MediaContent mc = mModule.getMediaContent()[i];
	                        mThumbs = mc.getMediaThumbnails();
	                        if (mThumbs != null) {
	                            for (int n = 0; n < mThumbs.length; n++) {
	                                String imgUrl = 	mThumbs[n].getUrl();
	                                System.out.println("got MediaContentImage " + n + " img: " + imgUrl);
	                            }
	                        }
	                        System.out.println("MediaContent title:" + mc.getTitle());
	                        System.out.println("MediaContent text:" + mc.getText());
	         
	                    }
	         
	                }
	                
			}
			*/
			
			
			if(result !=null && result.size()>0){
				CFeedItem _item = (CFeedItem) result.get(0);
				if(_item.creationDate!=null)
				{
					Collections.sort(result, new CListDateCompReverse());
				}
			}
			
			
		}
		return result;
	}
	public static ArrayList<CListItem> toListItems(Vector<BaseData> items,String newItemClass,ECMContentSourceType sourceType,DataSourceBase dataSource,Locale local)
	{
		ArrayList<BaseData>input = new ArrayList<BaseData>();
		if(items!=null)
			input.addAll(0, items);
		return toListItems(input, newItemClass, sourceType,dataSource,local);
		
	}
	public static ArrayList<CListItem> toListItems(ArrayList<BaseData> items,String newItemClass,ECMContentSourceType sourceType,DataSourceBase dataSource,Locale local)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		for(int i = 0 ; i < items.size() ; i ++){
			
			BaseData dataItem =  items.get(i);
			
			if(!dataItem.published){
				continue;
			}
			

			CListItem item = dataItem.getAsListItem();
			item.ownerRefStr = dataItem.getOwnerRefStr();
			DateFormat df = null;
			if(local==null){
				df = new SimpleDateFormat("EEEE  HH:mm");
			}else{
				df= new SimpleDateFormat("EEEE  HH:mm",local);
			}
			if(dataItem.created!=null)
			{
				
				//SimpleDateFormat simpleDateFormatter= new SimpleDateFormat();
				item.setDateString(df.format(dataItem.created));
				item.setCreationDate(dataItem.created);
			}
			item.setHasSubGroups(dataItem.hasSubCategories);
			result.add(item);
			
			if(newItemClass!=null)
			{
				item.dataClass = newItemClass;
			}
			
			if(dataSource!=null && dataSource.getUid()!=null)
			{
				item.setDataSource(dataSource.getUid());
			}
			
			if(sourceType!=ECMContentSourceType.Unknown)
			{
				item.sourceType="" + ECMContentSourceTypeTools.TypeToInteger(sourceType);
			}
		
				
			if(dataItem instanceof ArticleData){
				
				ArticleData art = (ArticleData)dataItem;
				if(art.description!=null){
					String introIn = art.description;
					introIn=StringUtils.removeHTML2(introIn);
					item.setIntroText(introIn);
				}
			}
				
			/***
			 * Event Class
			 */
			if(dataItem instanceof EventData)
			{
				EventData event = (EventData)dataItem;
				CEventItem cEventItem = (CEventItem)item;
				if(event.start_time!=null)
					cEventItem.setStartDateString(df.format(event.start_time));
				
				if(event.end_time!=null)
					cEventItem.setEndDateString(df.format(event.end_time));
				
				if(event.loc !=null && event.loc.title !=null && event.loc.title.length() > 0){
					cEventItem.venueString=event.loc.title;
				}
				
				if(event.cat !=null && event.cat.title !=null && event.cat.title.length() > 0){
					cEventItem.groupIdStr=event.cat.title;
				}
				
			}
			
			/***
			 * Event Class
			 */
			if(dataItem instanceof CContent)
			{
				CContent event = (CContent)dataItem;
				CEventItem cEventItem = (CEventItem)item;
				if(event.startDate!=null)
					cEventItem.setStartDateString(df.format(event.startDate));
				
				if(event.endDate!=null)
					cEventItem.setEndDateString(df.format(event.endDate));
				
				
				if(event.locRefStr !=null){
					cEventItem.venueString=event.locRefStr;
				}
				cEventItem.groupIdStr=event.groupStr;
			
			}
			
				
			
			
		}
		return result;
	}
	
	public static ArrayList<CListItem> updateDatasource(ArrayList<CListItem> items,String dataSource)
	{		

		if(dataSource==null){
			return items;
		}

		for(int i = 0 ; i < items.size() ; i ++){
			CListItem item = items.get(i);
			item.setDataSource(dataSource);
		}
		return items;
	}
	public static ArrayList<CListItem> updateSourceType(ArrayList<CListItem> items,String sourceType)
	{		

		if(sourceType==null){
			return items;
		}

		for(int i = 0 ; i < items.size() ; i ++){
			CListItem item = items.get(i);
			item.setSourceType(sourceType);
		}
		return items;
	}
	public static ArrayList<CListItem> toListItems(ArrayList<BaseData> items,String newItemClass,String sourceType)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		for(int i = 0 ; i < items.size() ; i ++){
			
			BaseData dataItem =  items.get(i);
			CListItem item = null ;
			
			if(dataItem instanceof EventData){
				
				item = ((EventData)dataItem).getAsListItem();
			}else{
				item = dataItem.getAsListItem();
			}
			
			if(dataItem instanceof ArticleData){
				
				item = ((ArticleData)dataItem).getAsListItem();
				ArticleData art = (ArticleData)dataItem;
				if(art.description!=null){
					String introIn = art.description;
					introIn=StringUtils.removeHTML2(introIn);
					item.setIntroText(introIn);
				}
				
				
			}else{
				item = dataItem.getAsListItem();
			}
			
			if(dataItem.created!=null)
			{
				DateFormat df = new SimpleDateFormat("EEEE  HH:mm");
				//SimpleDateFormat simpleDateFormatter= new SimpleDateFormat();
				item.setDateString(df.format(dataItem.created));
			}
			
			result.add(item);
			
			if(newItemClass!=null){
				item.dataClass = newItemClass;
			}
			if(sourceType!=null)
			{
				item.sourceType = sourceType;
			}
		}
		return result;
	}
	
	
}
