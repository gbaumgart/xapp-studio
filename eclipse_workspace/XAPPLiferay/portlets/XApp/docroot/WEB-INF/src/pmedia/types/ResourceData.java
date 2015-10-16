package pmedia.types;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import flexjson.JSON;
public class ResourceData
{
	public String uid;
	public int loc_id;
	public int cat_id;
	public int event_id;
	
	public int type;
	
	public int getType() {return type;}
	public void setType(int type) {this.type = type;}

	///////////////////// : linked objects: 
	//category : 
	public Category cat;
	@JSON(include=false)
	public Category getCat() {return cat;}
	public void setCat(Category cat) {this.cat = cat;}

	//location : 
	public LocationData loc;
	@JSON(include=false)
	public LocationData getLoc() {	return loc;	}
	public void setLoc(LocationData loc) {	this.loc = loc;}

	public String mapUrl;
	public String getMapUrl() {return mapUrl;}
	public void setMapUrl(String mapUrl) {	this.mapUrl = mapUrl;}

	
	public String summary;
	public String location;
	public String getLocation(){return location;}
	public void setLocation(String location) {	this.location = location;}
	
	public String category;
	public String getCategory() {return category;}
	public void setCategory(String category) {this.category = category;}

	
	public ArrayList<String> pictures=null;
	public String stdPictureLink = "";
	public String stdPictureLinkLocal = "";
	public String iconUrl;
	
	public ArrayList<String>flickrMediumPictures;
	public ArrayList<String> getFlickrMediumPictures(){return flickrMediumPictures;}
	public void setFlickrMediumPictures(ArrayList<String> flickrMediumPictures){this.flickrMediumPictures = flickrMediumPictures;}
	
	public ArrayList<String>galleryFiles=null;
	public ArrayList<String>galleryThumbnailFiles=null;
	public ArrayList<String>galleryTitles=null;
	
	public ArrayList<String> getGalleryTitles() {	return galleryTitles;}
	public void setGalleryTitles(ArrayList<String> galleryTitles) {this.galleryTitles = galleryTitles;}
	
	public ArrayList<String> getGalleryThumbnailFiles(){return galleryThumbnailFiles;}
	public void setGalleryThumbnailFiles(ArrayList<String> galleryThumbnailFiles) {this.galleryThumbnailFiles = galleryThumbnailFiles;}

	public ArrayList<String> getGalleryFiles()	{return galleryFiles;}
	public void setGalleryFiles(ArrayList<String> galleryFiles){this.galleryFiles = galleryFiles;}

	
	public String getIconUrl() 
	{
		if(iconUrl==null)
		{
			if(getPictures()!=null && getPictures().size() > 0 )
			{
				iconUrl = getPictures().get(0);
			}
		}
		return iconUrl;
	}
	public void setIconUrl(String iconUrl)
	{
		this.iconUrl = iconUrl;
	}

	public String descriptionNoPictures=null;
	public String htmlText;
	public String description;
	
	
	/////////////////////////////////////////////////////////////////
	//Facebook specific
		
	
	/////////////////////////////////////////////////////////////////
	// type related : 
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
		if(this.description!=null && this.description.length()  > 0)
		{
			getPictures();
		}
	}

	public String eventSourceType= "database";
	public String getEventSourceType() {return eventSourceType;}
	public void setEventSourceType(String eventSourceType){this.eventSourceType = eventSourceType;}
	
	public boolean didFlickrSearch=false;
	public boolean isDidFlickrSearch() {return didFlickrSearch;	}
	public void setDidFlickrSearch(boolean didFlickrSearch) {this.didFlickrSearch = didFlickrSearch;}

	
	/////////////////////////////////////////////////////////////////
	// Time specific
	
	public boolean matchLocation = false;
	public int id;
	public String title;
	public Boolean pub;
	public String alias;
	public String custom0;

	public String custom1;
	public String www;
	public String getWWW() {
		return www;
	}
	public void setWWW(String www) {
		this.www = www;
	}
	public String getCustom1() {return custom1;}
	public void setCustom1(String custom1) {this.custom1 = custom1;}
	
	public String getCustom0() {return custom0;}
	public void setCustom0(String custom0) {this.custom0 = custom0;}
	
	public String getAlias() {return alias;}
	public void setAlias(String alias) {this.alias = alias;}
	
	public Boolean getPub() {return pub;}
	public void setPub(Boolean pub) {	this.pub = pub;}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public int getId() {return id;}
	public void setId(int id) {		this.id = id;	}
	@JSON(include=false)
	public boolean isMatchLocation() {		return matchLocation;	}
	public void setMatchLocation(boolean matchLocation) {		this.matchLocation = matchLocation;	}
	
	public String getDescriptionNoPictures()
	{
		if(description==null || description.length() == 0)
			return null;

		if(descriptionNoPictures==null)
		{
			Whitelist clean = Whitelist.simpleText().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","strong");
			Cleaner cleaner = new Cleaner(clean);
			Document doc = Jsoup.parse(description);
			descriptionNoPictures = cleaner.clean(doc).body().html();
		}
		return descriptionNoPictures;
	}

	public void setDescriptionNoPictures(String descriptionNoPictures)
	{

		this.descriptionNoPictures = descriptionNoPictures;
	}

	public ArrayList<String> getPictures()
	{
		/*
		if(pictures!=null)
		{
			pictures.clear();
		}
		pictures=null;
		*/
		
		if(pictures==null)
		{
			if(description!=null && description.length() > 0)
			{
				Document doc = Jsoup.parse(description);
				//Elements pngs = doc.select("img[src~=.(png|jpg)]");
				Elements pngs = doc.select("img");
				for (Element img : pngs)
				{
				        String url = img.attr("src");
				        {
				            if(pictures==null)
				            	pictures = new ArrayList<String>();

				            if(url.length() > 0 && !pictures.contains(url))
				            {
				            	pictures.add(""+url);
				            }
				        }

				    }
			}
		}
		return pictures;
	}

	public void setPictures(ArrayList<String> pictures)
	{
		this.pictures = pictures;
	}

	
}
