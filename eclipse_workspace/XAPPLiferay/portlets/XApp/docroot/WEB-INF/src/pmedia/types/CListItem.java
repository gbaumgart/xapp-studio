package pmedia.types;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import cmx.types.ECMContentSourceType;
import flexjson.*;

public class CListItem implements Comparable<CListItem>
{
	
	 @Override
	  public int compareTo(CListItem o) {
	    //return getDateTime().compareTo(o.getDateTime());
		 if(creationDate!=null && o.creationDate!=null){
			 return creationDate.compareTo(o.creationDate);
		 }
		 return 0;
	  }
	 
	private static final long serialVersionUID = 1L;
	public String baseRef;
	
	/**
	 * new CM Attributes : 
	 */
	public int refId;
	public String refIdStr;
	public String groupIdStr;
	
	public String dataSource;
	public String iconClass;
	
	public String getIconClass() {
		return iconClass;
	}
	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public int commentCount=0;
	
	public int ownerRefId;
	public String ownerRefStr;
	
	public String picture;
	public ECMContentSourceType type = ECMContentSourceType.Unknown ;
	
	public String dataClass;
	public String dateString;
	
	public Date creationDate;
	public String created;
	
	
	

	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public String locRefStr;
	
	public int order;
	
	boolean hasSubGroups;
	
	/***
	 * Original 
	 */
	
	
	
	
	public String title;
	public String introText="";
	public String categoryIconUrl;
	public String localizedCategoryTitle="";
	public boolean published;
	
	public int priority;
	public int groupId;
	public String iconUrl;
	public String ref;
	
	
	public String sourceType="Database";
	public String mediaType="AUDIO";
	public String webLink;
	
	public String getGroupIdStr() {
		return groupIdStr;
	}
	public void setGroupIdStr(String groupIdStr) {
		this.groupIdStr = groupIdStr;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
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
	public String getWebLink() {
		return webLink;
	}
	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String cmdArg) {
		this.title = cmdArg;
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
	 * @return the groupId
	 */
	public int getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the dataClass
	 */
	public String getDataClass() {
		return dataClass;
	}
	/**
	 * @param dataClass the dataClass to set
	 */
	public void setDataClass(String dataClass) {
		this.dataClass = dataClass;
	}
	/**
	 * @return the categoryIconUrl
	 */
	public String getCategoryIconUrl() {
		return categoryIconUrl;
	}
	/**
	 * @param categoryIconUrl the categoryIconUrl to set
	 */
	public void setCategoryIconUrl(String categoryIconUrl) {
		this.categoryIconUrl = categoryIconUrl;
	}
	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}
	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}
	/**
	 * @param ref the ref to set
	 */
	public void setRef(String ref) {
		this.ref = ref;
	}
	/**
	 * @return the dateString
	 */
	public String getDateString() {
		return dateString;
	}
	/**
	 * @param dateString the dateString to set
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getBaseRef() {
		return baseRef;
	}
	public void setBaseRef(String baseRef) {
		this.baseRef = baseRef;
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isHasSubGroups() {
		return hasSubGroups;
	}
	public void setHasSubGroups(boolean hasSubGroups) {
		this.hasSubGroups = hasSubGroups;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getRefIdStr() {
		return refIdStr;
	}
	public void setRefIdStr(String refIdStr) {
		this.refIdStr = refIdStr;
	}
	public String getLocRefStr() {
		return locRefStr;
	}
	public void setLocRefStr(String locRefStr) {
		this.locRefStr = locRefStr;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
}
