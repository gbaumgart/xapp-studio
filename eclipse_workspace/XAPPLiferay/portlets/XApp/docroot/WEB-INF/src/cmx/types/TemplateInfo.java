package cmx.types;

import java.util.ArrayList;

public class TemplateInfo {

	
	private ArrayList<TemplateInfoPage>pages = new ArrayList<TemplateInfoPage>();
	
	private String title;
	private String id;
	private String description;
	private String platform;
	private String appId;
	private String userId;
	
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
	public ArrayList<TemplateInfoPage> getPages() {
		return pages;
	}
	public void setPages(ArrayList<TemplateInfoPage> pages) {
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
	
	
	
	
}
