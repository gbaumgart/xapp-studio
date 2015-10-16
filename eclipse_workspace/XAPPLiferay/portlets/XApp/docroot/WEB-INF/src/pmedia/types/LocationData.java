package pmedia.types;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import flexjson.*;
public class LocationData extends BaseData
{
	private static final long serialVersionUID = 1L;
	public int location_id;
	public String location;
	public String stdPictureLink;
	public String htmlText;
	
	private boolean didRatingSearch=false;
	
	private Rating rating=null;
	
	
	public String uid;
	public String phone;
	public String www;
	public String logoLocalPath;
	public String logoRemotePath;
	
	public String city;
	public String street;
	public int access;
	public boolean published;
	public int priority;
	public int geozoom;
	public String pcode;
	public String country;
	public int city_cat;
	public int cat_id;
	public String region;
	
	public String staticMap1;
	public String staticMapUrl="";
	
	public int forcedEventCatgory=0;
	
	public ArrayList<String>pictures=null;
	
	public ArticleData mappedArticle=null;
	public Boolean didMapping=false;
	public Boolean trusted = false;
	public String categoryFullString=null;
	
	
	public String ErrorReport=null;
	
	

	public String getStaticMapUrl(){
		return staticMapUrl;
	}

	public void setStaticMapUrl(String staticMapUrl) {
		this.staticMapUrl = staticMapUrl;
	}

	public int getForcedEventCategory()
	{	return forcedEventCatgory; }

	public void setForcedEventCategory(int value)
	{	this.forcedEventCatgory = value; }

	
	public Boolean getTrusted()
  	{
  		return trusted;
  	}

	public void setTrusted(Boolean value)
  	{
  		this.trusted = value;
  	}
	
	@JSON(include=false)
	public String getErrorReport() {return ErrorReport;}
	public void setErrorReport(String errorReport) {ErrorReport = errorReport;}

	public String getId()
	{
		String a = ""+ location_id;
		return a;
	}

	/**
	 * @return the didRatingSearch
	 */
	public boolean isDidRatingSearch() {
		return didRatingSearch;
	}

	/**
	 * @param didRatingSearch the didRatingSearch to set
	 */
	public void setDidRatingSearch(boolean didRatingSearch) {
		this.didRatingSearch = didRatingSearch;
	}

	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}
}
