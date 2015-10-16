
package pmedia.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CEventItem;
import pmedia.types.CFeedItem;
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

public class CContentTools {

	
	public static CContent getByRefIdStr(ArrayList<CContent>items,String refIdStr)
	{
		for (int s = 0; s < items.size() ; s++) 
		{
			CContent item = items.get(s);
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
	                    item.sourceType = mc.getType();
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
	            feedItem.setDateString( _minutes +"m:" + _seconds +"s");
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
        		item.setRef(java.net.URLEncoder.encode(syndicateEntry.getLink()));
        		//item.setRefId(java.net.URLEncoder.encode(syndicateEntry.getLink()));
        	}
        	
        	DateFormat df = new SimpleDateFormat("EEEE  HH:mm");
        	item.setDateString(df.format(syndicateEntry.getPublishedDate()));
        	
        	if(syndicateEntry.getPublishedDate() !=null)
        	{
        		item.setCreationDate(syndicateEntry.getPublishedDate());
        	}

        	SyndContent content = syndicateEntry.getDescription();
        	List contents = syndicateEntry.getContents();
        	List enclosures = syndicateEntry.getEnclosures();
        	if(content!=null && content.getValue()!=null)
        	{
        		String contentStr = content.getValue();
        		contentStr=StringUtils.removeHTML(contentStr);
        		if(contentStr.length() > 80){
        			contentStr=contentStr.substring(0,79);
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
			
		}
		return result;
	}
	public static ArrayList<CListItem> toListItems(Vector<BaseData> items,String newItemClass,ECMContentSourceType sourceType,DataSourceBase dataSource)
	{
		ArrayList<BaseData>input = new ArrayList<BaseData>();
		if(items!=null)
			input.addAll(0, items);
		return toListItems(input, newItemClass, sourceType,dataSource);
		
	}
	public static ArrayList<CListItem> toListItems(ArrayList<BaseData> items,String newItemClass,ECMContentSourceType sourceType,DataSourceBase dataSource)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		for(int i = 0 ; i < items.size() ; i ++){
			
			BaseData dataItem =  items.get(i);
			

			CListItem item = dataItem.getAsListItem();
			item.ownerRefStr = dataItem.getOwnerRefStr();
			DateFormat df = new SimpleDateFormat("EEEE  HH:mm");
			if(dataItem.created!=null)
			{
				
				//SimpleDateFormat simpleDateFormatter= new SimpleDateFormat();
				item.setDateString(df.format(dataItem.created));
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
