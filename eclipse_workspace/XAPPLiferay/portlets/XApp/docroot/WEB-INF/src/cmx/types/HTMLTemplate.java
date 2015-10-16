package cmx.types;

import java.util.ArrayList;

public class HTMLTemplate {

	
	private Boolean templated=true;
    private float height;
    private Boolean cutTitle=true;
    private int iconResizeWidth;
    private Boolean cutCaption=false;
    private int maxPages;
    private int pageSize;
    private int maxPagesTablet;
    private int cutDetail;
    private int pageSizeTablet;
    private Boolean freeStyle=true;
    private Boolean pictureFirst=true;
    private String listContainerClass;
    
    private String listTemplate;
    private String contentTemplate;
	
	
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
	public Boolean getTemplated() {
		return templated;
	}
	public void setTemplated(Boolean templated) {
		this.templated = templated;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public Boolean getCutTitle() {
		return cutTitle;
	}
	public void setCutTitle(Boolean cutTitle) {
		this.cutTitle = cutTitle;
	}
	public int getIconResizeWidth() {
		return iconResizeWidth;
	}
	public void setIconResizeWidth(int iconResizeWidth) {
		this.iconResizeWidth = iconResizeWidth;
	}
	public Boolean getCutCaption() {
		return cutCaption;
	}
	public void setCutCaption(Boolean cutCaption) {
		this.cutCaption = cutCaption;
	}
	public int getMaxPages() {
		return maxPages;
	}
	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getMaxPagesTablet() {
		return maxPagesTablet;
	}
	public void setMaxPagesTablet(int maxPagesTablet) {
		this.maxPagesTablet = maxPagesTablet;
	}
	public int getCutDetail() {
		return cutDetail;
	}
	public void setCutDetail(int cutDetail) {
		this.cutDetail = cutDetail;
	}
	public int getPageSizeTablet() {
		return pageSizeTablet;
	}
	public void setPageSizeTablet(int pageSizeTablet) {
		this.pageSizeTablet = pageSizeTablet;
	}
	public Boolean getFreeStyle() {
		return freeStyle;
	}
	public void setFreeStyle(Boolean freeStyle) {
		this.freeStyle = freeStyle;
	}
	public Boolean getPictureFirst() {
		return pictureFirst;
	}
	public void setPictureFirst(Boolean pictureFirst) {
		this.pictureFirst = pictureFirst;
	}
	public String getListContainerClass() {
		return listContainerClass;
	}
	public void setListContainerClass(String listContainerClass) {
		this.listContainerClass = listContainerClass;
	}
	public String getListTemplate() {
		return listTemplate;
	}
	public void setListTemplate(String listTemplate) {
		this.listTemplate = listTemplate;
	}
	public String getContentTemplate() {
		return contentTemplate;
	}
	public void setContentTemplate(String contentTemplate) {
		this.contentTemplate = contentTemplate;
	}
	
	
	
	
}
