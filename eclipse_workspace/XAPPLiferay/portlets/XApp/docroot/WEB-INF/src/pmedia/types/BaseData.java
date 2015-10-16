package pmedia.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import cmx.types.ECMContentSourceType;
import cmx.types.Reference;
import flexjson.*;
public class BaseData
{
	
	/**
	 * new CM Attributes : 
	 */
	
	public String refIdStr;
	
	public int ownerRefId;
	public String ownerRefStr;
	
	public int refId;
	public String picture;
	public ECMContentSourceType type = ECMContentSourceType.Unknown ;
	public CListItem listItem;
	
	public boolean hasSubCategories=false;
	
	public ArrayList<CField>customFields;
	public CListItem getAsListItem(Locale local)
	{
		return getAsListItem();
	}
	
	public CListItem getAsListItem()
	{
		if(listItem!=null)
		{
			listItem.setHasSubGroups(hasSubCategories);
			return listItem;
		}
		
		listItem = new CListItem();
		listItem.setRefId(refId);
		listItem.setRefIdStr(refIdStr);
		listItem.setGroupId(groupId);
		listItem.setGroupIdStr(groupStr);
		listItem.setTitle(title);
		listItem.setDataClass("pmedia.types.BaseData");
		listItem.setIconUrl(getIconUrl());
		listItem.setOwnerRefId(getOwnerRefId());
		listItem.setOwnerRefStr(getOwnerRefStr());
		listItem.setHasSubGroups(hasSubCategories);
		
		return listItem;
	}
	public void addCustomField(CField field)
	{
		if(customFields==null){
			customFields = new ArrayList<CField>();
		}
		customFields.add(field);
	}
	
	
	/***
	 * Refactored 
	 */
	public String longtitude;
	public String latitude;
	
	
	/***
	 * Original 
	 */
	private ArrayList<MediaItemBase>pictureItems;
	private static final long serialVersionUID = 1L;
	
	public Boolean didEventSearch=false;
	public Boolean didPictureSearch=false;
	
	
	public Boolean didStatusTest=false;
	public int id;
	public String title;
	public String location;
	public String description="";
	public String introText="";
	public String descriptionExtra="";
	
	public Date created;
	public String uid;
	public String treeUid;
	public String categoryIcon;
	public String localizedCategoryTitle="";
	
	public int groupId;
	public String groupStr;
	
	public int locRefId;
	public String locRefStr;
	
	
	public boolean published;
	public int priority;
	//public int cat_id;
	public ArrayList<String>galleryFiles=new ArrayList<String>();
	public String iconUrl;
	public ArrayList<String>galleryThumbnailFiles=new ArrayList<String>();
	
	public ArrayList<String>pictures=null;
	public String descriptionNoPictures=null;
	public String descriptionPicture="";
	public ArticleData mappedArticle=null;
	public Boolean didMapping=false;
	public ArrayList<String>flickrMediumPictures;
	public Category category;
	
	public String categoryFullString=null;
	public String sourceType="Database";
	public ArrayList<Reference>refGroups=null;
	
	
	
	public String getTreeUid() {
		if(treeUid==null){
			treeUid=UUID.randomUUID().toString();
		}
		return treeUid;
	}
	public void setTreeUid(String treeUid) {
		this.treeUid = treeUid;
	}
	public ArrayList<Reference> getRefGroups() 
	{
		return refGroups;
	}
	public void setRefGroups(ArrayList<Reference> refGroups) 
	{
		this.refGroups = refGroups;
	}
	
	public ArrayList<String>refGroupsStr=null;
	public ArrayList<String> getRefGroupsStr() 
	{
		return refGroupsStr;
	}
	public void setRefGroupsStr(ArrayList<String> refGroupsStr) 
	{
		this.refGroupsStr = refGroupsStr;
	}

	public ArrayList<String> getGalleryTitles() {	return galleryTitles;}
	public void setGalleryTitles(ArrayList<String> galleryTitles) {this.galleryTitles = galleryTitles;}

	public ArrayList<String>galleryTitles=null;
	public String mapUrl;
	public String video="";
	
	public String getMapUrl() {return mapUrl;}
	public void setMapUrl(String mapUrl) {	this.mapUrl = mapUrl;}
	
	
	public String getVideo() {return video;}
	public void setVideo(String video) {this.video = video;}
	
	public void populateStatusVariables()
	{
		if(didStatusTest)
			return;
		
		
		
		getPictures();
		
		if(getPictures() !=null && getPictures().size() > 1)
		{			
			//hasPictures=true;
		}
		didStatusTest=true;
		
	}
	
	
	public ArrayList<String> getGalleryThumbnailFiles() 
	{
		if(galleryThumbnailFiles!=null)
		{
			if(galleryThumbnailFiles.size()==0)
				galleryThumbnailFiles.add("dummy");
			
		}
		
		return galleryThumbnailFiles;
	}

	public void setGalleryThumbnailFiles(ArrayList<String> galleryThumbnailFiles) {
		this.galleryThumbnailFiles = galleryThumbnailFiles;
	}

	public ArrayList<String> getGalleryFiles() 
	{
		if(galleryFiles!=null)
		{
			if(galleryFiles.size()==0)
				galleryFiles.add("dummy");
		}
		
		return galleryFiles;
	}

	public void setGalleryFiles(ArrayList<String> galleryFiles) 
	{
		this.galleryFiles = galleryFiles;
	}


	
	
	//public PhotoList photoListFlickr=null;
	public ArrayList<String> getFlickrMediumPictures() 
	{
		return flickrMediumPictures;
	}

	public void setFlickrMediumPictures(ArrayList<String> flickrMediumPictures) 
	{
		this.flickrMediumPictures = flickrMediumPictures;
	}

	public String getDescriptionNoPictures()
	{
		if(description==null || description.length()==0)
			return null;
		if(descriptionNoPictures==null)
		{
			Whitelist clean = Whitelist.basic().addTags("blockquote", "cite", "code", "p", "q", "s", "strike","br","a","strong");
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
	
	
	public void setPictures(ArrayList<String> pictures)
	{
		this.pictures = pictures;
	}

	public ArrayList<String> getPictures()
	{
		if(this.pictures!=null)
			return this.pictures;
		
		
		
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
			            	this.pictures = new ArrayList<String>();

			            if(url.length() > 0 && !pictures.contains(url))
			            {
			            	this.pictures.add(""+url);
			            }
			        }

			    }
		}
		
		if(picture!=null && picture.length() > 0 )
		{
			if(pictures==null)
            	this.pictures = new ArrayList<String>();
			
			pictures.add(0,picture);
			
		}
		
		return this.pictures;
	}

	
	public String getIconUrl() 
	{
		if(iconUrl==null)
		{
			if(getPictures()!=null && getPictures().size() > 0)
			{
				iconUrl = getPictures().get(0);
			}else
			{
				iconUrl="";
			}
		}
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}


	
	@JSON(include=false)
	public Category getCategory() {return category;}
	public void setCategory(Category category) {this.category = category;}

	
	
	public String getId()
	{
		String a = ""+ id;
		return a;
	}

	public String getTitle()
	{
		return title;
	}
	public void setTitle(String cmdArg) {
		this.title = cmdArg;
	}


	/**
	 * @return the descriptionPicture
	 */
	public String getDescriptionPicture() {
		return descriptionPicture;
	}

	/**
	 * @param descriptionPicture the descriptionPicture to set
	 */
	public void setDescriptionPicture(String descriptionPicture) {
		this.descriptionPicture = descriptionPicture;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the categoryIcon
	 */
	public String getCategoryIcon() {
		return categoryIcon;
	}
	/**
	 * @param categoryIcon the categoryIcon to set
	 */
	public void setCategoryIcon(String categoryIcon) {
		this.categoryIcon = categoryIcon;
	}
	/**
	 * @return the localizedCategoryTitle
	 */
	public String getLocalizedCategoryTitle() {
		return localizedCategoryTitle;
	}
	/**
	 * @param localizedCategoryTitle the localizedCategoryTitle to set
	 */
	public void setLocalizedCategoryTitle(String localizedCategoryTitle) {
		this.localizedCategoryTitle = localizedCategoryTitle;
	}
	/**
	 * @return the pictureItems
	 */
	public ArrayList<MediaItemBase> getPictureItems() {
		return pictureItems;
	}
	/**
	 * @param pictureItems the pictureItems to set
	 */
	public void setPictureItems(ArrayList<MediaItemBase> pictureItems) {
		this.pictureItems = pictureItems;
	}
	/**
	 * @return the refId
	 */
	public int getRefId() {
		return refId;
	}
	/**
	 * @param refId the refId to set
	 */
	public void setRefId(int refId) {
		this.refId = refId;
	}
	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}
	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}
	/**
	 * @return the type
	 */
	public ECMContentSourceType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ECMContentSourceType type) {
		this.type = type;
	}
	/**
	 * @return the introText
	 */
	public String getIntroText() {
		return introText;
	}
	/**
	 * @param introText the introText to set
	 */
	public void setIntroText(String introText) {
		this.introText = introText;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}




	public int getGroupId() {
		return groupId;
	}




	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}




	public String getGroupStr() {
		return groupStr;
	}




	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public ArrayList<CField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(ArrayList<CField> customFields) {
		this.customFields = customFields;
	}

	public int getOwnerRefId() {
		return ownerRefId;
	}

	public void setOwnerRefId(int ownerRefId) {
		this.ownerRefId = ownerRefId;
	}

	public String getOwnerRefStr() {
		return ownerRefStr;
	}

	public void setOwnerRefStr(String ownerRefStr) {
		this.ownerRefStr = ownerRefStr;
	}

	public int getLocRefId() {
		return locRefId;
	}

	public void setLocRefId(int locRefId) {
		this.locRefId = locRefId;
	}

	public String getLocRefStr() {
		return locRefStr;
	}

	public void setLocRefStr(String locRefStr) {
		this.locRefStr = locRefStr;
	}

	public String getRefIdStr() {
		return refIdStr;
	}

	public void setRefIdStr(String refIdStr) {
		this.refIdStr = refIdStr;
	}
}
