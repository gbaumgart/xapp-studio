package pmedia.utils;

import cmx.tools.GDataDownloader;
import cmx.types.ECMContentSourceType;

public class ECMContentSourceTypeTools 
{

	public static ECMContentSourceType fromUrl(String url)
	{
		//index.php?option=com_content&view=article&id=68&catid=25&Itemid=284
		
		if(url.contains("com_content&view=article"))
		{
			return ECMContentSourceType.JoomlaArticle;
		}
		//index.php?option=com_content&view=category&id=76&Itemid=463
		if(url.contains("com_content&view=category"))
		{
			return ECMContentSourceType.JoomlaCategory;
		}
		
		if(url.contains("com_jevlocations&task=locations.detail"))
		{
			return ECMContentSourceType.JEventsLocationItem;
		}
		
		if(url.contains("?p="))
		{
			return ECMContentSourceType.WordpressPost;
		}
		
		return ECMContentSourceType.Unknown;
	}
	
	public static String toTableName(ECMContentSourceType type)
	{
		return null;
	}
	
	public static String toName(ECMContentSourceType type)
	{
		if(type==ECMContentSourceType.JEventsCategory)
		{
			return "JEvents Category";
		}
		
		if(type==ECMContentSourceType.JEventsLocationCategory)
		{
			return "JEvents Location Category";
		}
		
		if(type==ECMContentSourceType.JoomlaCategory)
		{
			return "Joomla Category";
		}
		
		return null;
	}
	public static ECMContentSourceType FromTableString(String value)
	{
		if(value.equals("articles"))
		{
			return ECMContentSourceType.JoomlaArticle;
		}
		
		
		
		if(value.equals("Picassa")){
			return ECMContentSourceType.GooglePicassaItem;
		}
		
		if(value.equals("Calendar")){
			return ECMContentSourceType.GoogleCalendar;
		}
		
		if(value.equals("Documents"))
		{
			return ECMContentSourceType.GoogleDocumentFolder;
		}
		
		if(value.equals("breezingforms")){
			return ECMContentSourceType.BreezingForm;
		}

		
		if(value.equals("categories"))
		{
			return ECMContentSourceType.JoomlaCategory;
		}
		if(value.equals("sections"))
		{
			return ECMContentSourceType.JoomlaSection;
		}
		if(value.equals("mosetCategories"))
		{
			return ECMContentSourceType.MosetTreeCategory;
		}
		
		if(value.equals("mosetLinks"))
		{
			return ECMContentSourceType.MosetTreeItem;
		}
		
		if(value.equals("jeventLocationCategories"))
		{
			return ECMContentSourceType.JEventsLocationCategory;
		}
		
		if(value.equals("jeventslocations"))
		{
			return ECMContentSourceType.JEventsLocationItem;
		}
		
		if(value.equals("jeventsNormal"))
		{
			return ECMContentSourceType.JEventsCalendarToday;
		}
		
		if(value.equals("vmartCategories"))
		{
			return ECMContentSourceType.VMartCategory;
		}
		
		if(value.equals("wpPosts"))
		{
			return ECMContentSourceType.WordpressPost;
		}
		
		if(value.equals("wpPages"))
		{
			return ECMContentSourceType.WordpressPage;
		}
		
		if(value.equals("wpCategories"))
		{
			return ECMContentSourceType.WordpressCategory;
		}
		
		if(value.equals("vmartLatest") || value.equals("vmartFeatured") || value.equals("vmProducts"))
		{
			return ECMContentSourceType.VMartProductItem;
		}

		return ECMContentSourceType.Unknown;
		
	}
	public static ECMContentSourceType FromString(String value)
	{
		int type = Integer.parseInt(value);
		
		
		if(type==0)
		{
			return ECMContentSourceType.Unknown;
		}
		
		if(type==1)
		{
			return ECMContentSourceType.JoomlaSection;
		}
		
		if(type==2)
		{
			return ECMContentSourceType.JoomlaCategory;
		}
		
		if(type==3)
		{
			return ECMContentSourceType.JoomlaArticle;
		}
		if(type==4)
		{
			return ECMContentSourceType.JEventsCategory;
		}
		if(type==7)
		{
			return ECMContentSourceType.JEventsLocationCategory;
		}
		if(type==8)
		{
			return ECMContentSourceType.MosetTreeCategory;
		}
		
		if(type==9)
		{
			return ECMContentSourceType.JoomlaBannerCategory;
		}
		
		if(type==10)
		{
			return ECMContentSourceType.MosetTreeItemList;
		}
		if(type==11)
		{
			return ECMContentSourceType.JoomlaBannerCategory;
		}
		
		if(type==12)
		{
			return ECMContentSourceType.JoomlaBannerItem;
		}
		
		if(type==13)
		{
			return ECMContentSourceType.FPSSCatgory;
		}
		
		if(type==14)
		{
			return ECMContentSourceType.JEventsItem;
		}
		if(type==15)
		{
			return ECMContentSourceType.ExternalLink;
		}
		if(type==16)
		{
			return ECMContentSourceType.BlueFrameForm;
		}
		
		if(type==17)
		{
			return ECMContentSourceType.BreezingForm;
		}
		
		if(type==18)
		{
			return ECMContentSourceType.RSS;
		}
		
		if(type==19)
		{
			return ECMContentSourceType.WordpressCategory;
		}
		if(type==20)
		{
			return ECMContentSourceType.WordpressPage;
		}
		if(type==21)
		{
			return ECMContentSourceType.WordpressPost;
		}
		if(type==22)
		{
			return ECMContentSourceType.WordpressTag;
		}
		if(type==23)
		{
			return ECMContentSourceType.WordpressUser;
		}
		
		if(type==24)
		{
			return ECMContentSourceType.GoogleYoutubeChannel;
		}
		if(type==25)
		{
			return ECMContentSourceType.GoogleYoutubeItem;
		}
		if(type==26)
		{
			return ECMContentSourceType.GooglePicassaAlbum;
		}
		if(type==27)
		{
			return ECMContentSourceType.GooglePicassaItem;
		}
		if(type==28)
		{
			return ECMContentSourceType.GoogleCalendar;
		}
		if(type==29)
		{
			return ECMContentSourceType.GoogleCalendarItem;
		}
		
		if(type==30)
		{
			return ECMContentSourceType.DataSourceItem;
		}
		
		if(type==31)
		{
			return ECMContentSourceType.StaticWebContent;
		}
		if(type==32)
		{
			return ECMContentSourceType.StaticWebContentCategory;
		}
		if(type==33)
		{
			return ECMContentSourceType.StaticWebContentTag;
		}
		
		if(type==34)
		{
			return ECMContentSourceType.JoomlaArticleList;
		}
		
		if(type==35)
		{
			return ECMContentSourceType.JEventsLocationItem;
		}
		if(type==36)
		{
			return ECMContentSourceType.JEventsLocationItemList;
		}
		
		if(type==37)
		{
			return ECMContentSourceType.VMartCategory;
		}
		if(type==38)
		{
			return ECMContentSourceType.VMartFeatured;
		}
		if(type==39)
		{
			return ECMContentSourceType.VMartLatest;
		}
		if(type==40)
		{
			return ECMContentSourceType.VMartProductList;
		}
		if(type==41)
		{
			return ECMContentSourceType.VMartProductItem;
		}
		
		if(type==42)
		{
			return ECMContentSourceType.RSS_ITEM;
		}
		if(type==43)
		{
			return ECMContentSourceType.PICTURE;
		}
		
		
		if(type==44)
		{
			return ECMContentSourceType.GALLERY;
		}
		
		if(type==45)
		{
			return ECMContentSourceType.PHONE;
		}
		
		if(type==46)
		{
			return ECMContentSourceType.MAP;
		}
		
		if(type==47)
		{
			return ECMContentSourceType.VIDEO;
		}
		
		if(type==48)
		{
			return ECMContentSourceType.FILE;
		}
		
		if(type==49)
		{
			return ECMContentSourceType.TEXT;
		}
		
		if(type==50)
		{
			return ECMContentSourceType.DATA;
		}
		
		if(type==51)
		{
			return ECMContentSourceType.MOPUB;
		}
		
		if(type==52)
		{
			return ECMContentSourceType.RADIO;
		}
		
		if(type==53)
		{
			return ECMContentSourceType.DISQUS;
		}
		
		if(type==54)
		{
			return ECMContentSourceType.TwitterService;
		}
		
		if(type==55)
		{
			return ECMContentSourceType.StaticWebContentVenue;
		}
		
		if(type==56)
		{
			return ECMContentSourceType.StaticWebContentVenueCategory;
		}
		
		if(type==57)
		{
			return ECMContentSourceType.StaticWebContentEvent;
		}
		
		if(type==58)
		{
			return ECMContentSourceType.GoogleDocumentFolder;
		}
		if(type==59)
		{
			return ECMContentSourceType.GoogleDocumentItem;
		}
		
		if(type==60)
		{
			return ECMContentSourceType.Custom;
		}
		
		if(type==200)
		{
			return ECMContentSourceType.SourceTypeEnd;
		}
		
		return ECMContentSourceType.Unknown;
		
	}
	public static int TypeToInteger(ECMContentSourceType type)
	{
		int res = 0;
		
		if(type==ECMContentSourceType.Unknown)
		{
			return 0;
		}
		
		if(type==ECMContentSourceType.JoomlaSection)
		{
			return 1;
		}
		
		if(type==ECMContentSourceType.JoomlaCategory)
		{
			return 2;
		}
		
		if(type==ECMContentSourceType.JoomlaArticle)
		{
			return 3;
		}
		if(type==ECMContentSourceType.JEventsCategory)
		{
			return 4;
		}
		if(type==ECMContentSourceType.JEventsCalendarWeek)
		{
			return 5;
		}
		if(type==ECMContentSourceType.JEventsCalendarToday)
		{
			return 6;
		}
		if(type==ECMContentSourceType.JEventsLocationCategory)
		{
			return 7;
		}
		if(type==ECMContentSourceType.MosetTreeCategory)
		{
			return 8;
		}
		
		if(type==ECMContentSourceType.MosetTreeItem)
		{
			return 9;
		}
		
		if(type==ECMContentSourceType.MosetTreeItemList)
		{
			return 10;
		}
		
		if(type==ECMContentSourceType.JoomlaBannerCategory)
		{
			return 11;
		}
		
		if(type==ECMContentSourceType.JoomlaBannerItem)
		{
			return 12;
		}
		
		if(type==ECMContentSourceType.FPSSCatgory)
		{
			return 13;
		}
		if(type==ECMContentSourceType.JEventsItem)
		{
			return 14;
		}
		
		if(type==ECMContentSourceType.ExternalLink)
		{
			return 15;
		}
		if(type==ECMContentSourceType.BlueFrameForm)
		{
			return 16;
		}
		
		if(type==ECMContentSourceType.BreezingForm)
		{
			return 17;
		}
		if(type==ECMContentSourceType.RSS)
		{
			return 18;
		}
		
		if(type==ECMContentSourceType.WordpressCategory)
		{
			return 19;
		}
		if(type==ECMContentSourceType.WordpressPage)
		{
			return 20;
		}
		if(type==ECMContentSourceType.WordpressPost)
		{
			return 21;
		}
		if(type==ECMContentSourceType.WordpressTag)
		{
			return 22;
		}
		
		if(type==ECMContentSourceType.WordpressUser)
		{
			return 23;
		}
		
		if(type==ECMContentSourceType.GoogleYoutubeChannel)
		{
			return 24;
		}
		if(type==ECMContentSourceType.GoogleYoutubeItem)
		{
			return 25;
		}
		if(type==ECMContentSourceType.GooglePicassaAlbum)
		{
			return 26;
		}
		if(type==ECMContentSourceType.GooglePicassaItem)
		{
			return 27;
		}
		if(type==ECMContentSourceType.GoogleCalendar)
		{
			return 28;
		}
		if(type==ECMContentSourceType.GoogleCalendarItem)
		{
			return 29;
		}
		if(type==ECMContentSourceType.DataSourceItem)
		{
			return 30;
		}
		if(type==ECMContentSourceType.StaticWebContent)
		{
			return 31;
		}
		if(type==ECMContentSourceType.StaticWebContentCategory)
		{
			return 32;
		}
		if(type==ECMContentSourceType.StaticWebContentTag)
		{
			return 33;
		}
		
		if(type==ECMContentSourceType.JoomlaArticleList)
		{
			return 34;
		}
		
		if(type==ECMContentSourceType.JEventsLocationItem)
		{
			return 35;
		
		}
		if(type==ECMContentSourceType.JEventsLocationItemList)
		{
			return 36;
		}
		
		if(type==ECMContentSourceType.VMartCategory)
		{
			return 37;
		}
		if(type==ECMContentSourceType.VMartFeatured)
		{
			return 38;
		}
		
		if(type==ECMContentSourceType.VMartLatest)
		{
			return 39;
		}
		if(type==ECMContentSourceType.VMartProductList)
		{
			return 40;
		}
		if(type==ECMContentSourceType.VMartProductItem)
		{
			return 41;
		}
		
		if(type==ECMContentSourceType.RSS_ITEM)
		{
			return 42;
		}
		if(type==ECMContentSourceType.PICTURE)
		{
			return 43;
		}
		if(type==ECMContentSourceType.GALLERY)
		{
			return 44;
		}
		
		
		if(type==ECMContentSourceType.PHONE)
		{
			return 45;
		}
		if(type==ECMContentSourceType.MAP)
		{
			return 46;
		}
		if(type==ECMContentSourceType.VIDEO)
		{
			return 47;
		}
		
		if(type==ECMContentSourceType.FILE)
		{
			return 48;
		}
		
		if(type==ECMContentSourceType.TEXT)
		{
			return 49;
		}
		
		if(type==ECMContentSourceType.DATA)
		{
			return 50;
		}
		
		if(type==ECMContentSourceType.MOPUB)
		{
			return 51;
		}
		
		if(type==ECMContentSourceType.RADIO)
		{
			return 52;
		}
		
		if(type==ECMContentSourceType.DISQUS)
		{
			return 53;
		}
		
		if(type==ECMContentSourceType.TwitterService)
		{
			return 54;
		}
		
		if(type==ECMContentSourceType.StaticWebContentVenue)
		{
			return 55;
		}
		if(type==ECMContentSourceType.StaticWebContentVenueCategory)
		{
			return 56;
		}
		
		if(type==ECMContentSourceType.StaticWebContentEvent)
		{
			return 57;
		}
		if(type==ECMContentSourceType.GoogleDocumentFolder)
		{
			return 58;
		}
		if(type==ECMContentSourceType.GoogleDocumentItem)
		{
			return 59;
		}
		
		if(type==ECMContentSourceType.Custom)
		{
			return 60;
		}
		
		if(type==ECMContentSourceType.SourceTypeEnd)
		{
			return 200;
		}
		
		return res;
		
	}
}
