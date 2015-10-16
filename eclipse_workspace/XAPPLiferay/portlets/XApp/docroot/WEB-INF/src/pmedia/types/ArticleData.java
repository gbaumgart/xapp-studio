package pmedia.types;

import java.util.ArrayList;

public class ArticleData extends BaseData
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public CListItem getAsListItem()
	{
		CListItem item = super.getAsListItem();
		
		item.setDataClass("pmedia.types.ArticleData");
		item.setRefId(refId);
		getPicture();
		getPictures();
		getIconUrl();
		item.setIconUrl(getIconUrl());
		return item;
	}
	
	public String location;


	public String uid;
	public String phone;
	public String www;
	public String logoLocalPath;
	public String logoRemotePath;
	public String longtitude;
	public String latitude;
	public String city;
	

	
	public int featured;
	public int type;
	public int aid;
	public int acc;
	public String altTitle;
    int parent;
    public Boolean pub;
    public Boolean isBeach;
    public PMDataTypes translationType;
    public int region;
    public String video;
    ArrayList<String>relatedData;
    double lat;
    double lng;

	public String street;
	public int access;
	public int geozoom;
	public String pcode;
	public String country;
	public int city_cat;
	
	public String staticMap1;

	public Category category;
	public String categoryFullString=null;

	public String ErrorReport;

	public String getVideo(){return video;}
	public void setVideo(String video) {this.video = video;}

    //DITTranslation *translation;
    //DMRelatedData *cachedData;
	
	

	public int forcedEventCatgory=0;
	public int getForcedEventCategory()
	{	return forcedEventCatgory; }

	public void setForcedEventCategory(int value)
	{	this.forcedEventCatgory = value; }

	public Boolean trusted = false;
	public Boolean getTrusted()
  	{
  		return trusted;
  	}

	public void setTrusted(Boolean value)
  	{
  		this.trusted = value;
  	}

	public String getId()
	{
		String a = ""+ refId;
		return a;
	}

	public int getFeatured() {
		return featured;
	}
	public void setFeatured(int featured) {
		this.featured = featured;
	}


}
