package cmx.types;

import java.util.ArrayList;

public class ShowcaseInfo {

	private ArrayList<ShowcaseApps>apps = new ArrayList<ShowcaseApps>();
	
	private ArrayList<ShowcaseInfoPage>pages = new ArrayList<ShowcaseInfoPage>();
	
	private String title;
	private String id;
	private String description;
	private String platform;
	private String appId;
	private String userId;
	private String duration;
	private String height;
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	private String width;
	
	private boolean convertable;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public boolean isConvertable() {
		return convertable;
	}
	public void setConvertable(boolean convertable) {
		this.convertable = convertable;
	}
	public ArrayList<ShowcaseInfoPage> getPages() {
		return pages;
	}
	public void setPages(ArrayList<ShowcaseInfoPage> pages) {
		this.pages = pages;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public ArrayList<ShowcaseApps> getApps() {
		return apps;
	}
	public void setApps(ArrayList<ShowcaseApps> apps) {
		this.apps = apps;
	}
	
	
	
	
}
