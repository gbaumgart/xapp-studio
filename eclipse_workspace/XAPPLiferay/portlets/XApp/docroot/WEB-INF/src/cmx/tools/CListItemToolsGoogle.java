package cmx.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import cmx.types.ECMContentSourceType;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YtStatistics;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

import pmedia.types.BaseData;
import pmedia.types.CContent;
import pmedia.types.CFeedItem;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.CVideoItem;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;

public class CListItemToolsGoogle 
{
	
	//public static CList listWithItemsAndHeaderData()
	
	public static ArrayList<CContent> calendarsToListItems(List<CalendarEntry>entries,String newItemClass)
	{
		ArrayList<CContent>result = new ArrayList<CContent>();
		 for(int i=0; i<entries.size(); i++) {
	          
			 CalendarEntry entry = entries.get(i);
	          //System.out.println(entry.getMediaContents());
	          //System.out.println(entry.getMediaThumbnails());
	          //String icon=null;
	          String url = null;
	          
	          CContent item = new CContent();
	          URL feedUrl = null;
	          List links = entry.getLinks();
				if(links!=null){
					
					Link link=(Link) links.get(0);
					
					if(link!=null)
					{
						try {
							feedUrl=new URL(link.getHref());
						} catch (MalformedURLException e) 
						{
							e.printStackTrace();
						}
					}
				}else{
					continue;
				}
				CalendarEventFeed resultFeed=null;
				//String _feedUrl = feedUrl.toString().replace("/private/full", "/public/full");
				
			  String _feedUrl = feedUrl.toString();
			  item.setRefIdStr(Base64.encodeBase64URLSafeString(_feedUrl.getBytes()));
			  
	          //item.setRefIdStr(StringUtils.encode(_feedUrl));
	          
				
			  StringUtils.encode(entry.getId());
			  
	          
	          item.setTitle(entry.getTitle().getPlainText());
	          item.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar));
	          //entry.
	          //item.setRef(entry.getId());
	          //url = entry.getHtmlLink().getHref();
	          //item.setWebLink(url);
	          //item.setIconUrl(icon);
	          result.add(item);
		 }
		return result;
	}
	
	public static CContent toCContent(CalendarEventEntry entry,String dsUID)
	{
		CContent result = new CContent();
		result.setTitle(entry.getTitle().getPlainText());
		List<When>times = entry.getTimes();
		
		for(int i = 0 ; i < times.size() ; i++)
		{
			
			When when = times.get(i);
			DateTime sTime = when.getStartTime();
			DateTime eTime = when.getEndTime();
			result.setStartDate(new Date(sTime.getValue()));
			result.setEndDate(new Date(eTime.getValue()));
		}
		/*
		WebContent content =  entry.getWebContent();
		//System.out.println("content : " + entry.getWebContentLink().getHref());
		System.out.println("descr : " + entry.getPlainTextContent());
		*/
		if(entry.getPlainTextContent()!=null)
		{
			result.setDescription(entry.getPlainTextContent());
		}
		
		for(Where w:entry.getLocations())
		{
				//e.setPlace(w.getValueString());
			System.out.println("Location : " +w.getValueString() + " for " + result.title);
			if(result.getLocRefStr()==null){
				result.setLocRefStr(w.getValueString());
				break;
			}
			
		}
	
		result.setDataSourceUID(dsUID);
		result.setRefIdStr(entry.getIcalUID());
		result.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendarItem));
		
		//entry.get
		
		/*
		if(entry.getSummary()!=null){
			result.setDescription(entry.getSummary().getPlainText());
			if(result.description.length() > 0)
			{
				System.out.println(result.getDescription());
			}
		}
		*/
			
			//result.setDe
			//result.setRe
			
			
		
		
		return result;
	}
	public static ArrayList<CContent> calendarEntriesToListItems(List<CalendarEventEntry>entries,String newItemClass,String groupIdStr,String dsUID)
	{
		ArrayList<CContent>result = new ArrayList<CContent>();
		 for(int i=0; i<entries.size(); i++) {
	          
			 CalendarEventEntry entry = entries.get(i);
			 //CalendarEventEntry event = entries.get(i);
	          //System.out.println(entry.getMediaContents());
	          //System.out.println(entry.getMediaThumbnails());
	          //String icon=null;
	          //String url = null;
	          CContent item = toCContent(entry,dsUID);
	          item.setGroupStr(groupIdStr);
	          
	          
	          //item.setTitle(entry.getTitle().getPlainText());
	          
	          //List<When>times entry.getTimes()
	          
	          //entry.get
	          
	          //item.setStartDate(entry.getW)
	          //entry.getTimesCleaned();
	          //entry.
	          //item.setRef(entry.getId());
	          //url = entry.getHtmlLink().getHref();
	          //item.setWebLink(url);
	          //item.setIconUrl(icon);
	          result.add(item);
		 }
		return result;
	}
	public static ArrayList<CListItem> photoEntriesToListItems(List<PhotoEntry>entries,String groupId,String newItemClass)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		 for(int i=0; i<entries.size(); i++) {

			 PhotoEntry entry = entries.get(i);
	          //System.out.println(entry.getMediaContents());
				
	          //System.out.println(entry.getMediaThumbnails());
	          
	          //String icon=null;
	          String url = null;
	          CListItem item = new CListItem();
	          item.setTitle(entry.getTitle().getPlainText());
	          item.setRef(entry.getGphotoId());
	          item.setGroupIdStr(groupId);
	          item.setIntroText(entry.getSummary().getPlainText());
	          MediaGroup photoMediaGroup = entry.getMediaGroup();
				if (photoMediaGroup != null) 
				{
					for (MediaThumbnail thumbnail : photoMediaGroup
							.getThumbnails()) 
					{
						
						if(item.getIconUrl()==null){
							item.setIconUrl(thumbnail.getUrl());
						}
						System.out.println("Thumbnail Dimensions: "
								+ thumbnail.getWidth() + 'x'
								+ thumbnail.getHeight());
						System.out.println("Thumbnail URL: "
								+ thumbnail.getUrl());
					}

					for (com.google.gdata.data.media.mediarss.MediaContent content : photoMediaGroup.getContents()) 
					{
						System.out.println("Content Dimensions: "
								+ content.getWidth() + 'x'
								+ content.getHeight());
						System.out.println("Content URL: "
								+ content.getUrl());
						if(item.getWebLink()==null)
						{
							item.setIconUrl(content.getUrl());
							item.setWebLink(content.getUrl());
						}
					}

				}
	          
	          
	          //url = entry.getHtmlLink().getHref();
	          //item.setWebLink(url);
	          //item.setIconUrl(icon);
	          result.add(item);
		 }
		return result;
	}
	public static ArrayList<CListItem> albumEntriesToListItems(List<AlbumEntry>entries,String newItemClass)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		 for(int i=0; i<entries.size(); i++) {
	          AlbumEntry entry = entries.get(i);
	          
	          //System.out.println(entry.getMediaContents());
				
	          //System.out.println(entry.getMediaThumbnails());
	          
	          //String icon=null;
	          String url = null;
	          CListItem item = new CListItem();
	          item.setTitle(entry.getTitle().getPlainText());
	          item.setRef(entry.getGphotoId());
	          //url = entry.getHtmlLink().getHref();
	          //item.setWebLink(url);
	          //item.setIconUrl(icon);
	          result.add(item);
		 }
		return result;
	}
	public static ArrayList<CVideoItem> toListItems(List<VideoEntry> entries,String newItemClass)
	{
		ArrayList<CVideoItem>result = new ArrayList<CVideoItem>();
		 for(int i=0; i<entries.size(); i++) {
	          VideoEntry entry = entries.get(i);
	          int count = 1;
	          
	          String icon=null;
	          String url = null;
	          YouTubeMediaGroup mediaGroup = entry.getMediaGroup();
	          for(MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails()) {
	              if(icon==null){
	            	  icon = mediaThumbnail.getUrl();
	              }
	            }
	          
	          YtStatistics stats=entry.getStatistics();
	          CVideoItem item = new CVideoItem();
	          item.setTitle(entry.getTitle().getPlainText());
	          item.setRef(mediaGroup.getVideoId());
	          url = entry.getHtmlLink().getHref();
	          item.setDuration(""+mediaGroup.getDuration());
	          item.setIconUrl(icon);
	          result.add(item);
	          
		 }
		return result;
	}
	public static ArrayList<CListItem> toListItems(ArrayList<BaseData> items,String newItemClass)
	{
		ArrayList<CListItem>result = new ArrayList<CListItem>();
		for(int i = 0 ; i < items.size() ; i ++){
			
			BaseData dataItem =  items.get(i);
			CListItem item = dataItem.getAsListItem();
			
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
		}
		return result;
	}
	
	
}
