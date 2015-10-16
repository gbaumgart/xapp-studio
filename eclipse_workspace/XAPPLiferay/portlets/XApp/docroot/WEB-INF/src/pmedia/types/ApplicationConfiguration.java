package pmedia.types;

public class ApplicationConfiguration 
{
	
	private double defaultLat=38.9049;
	private double defaultLon=1.42376;
	
	
	
	//servlet urls 
	private String appIdentifier = "imDefault";
	/**
	 * Multi-Tenant !
	 */
	
	private String appName="Ibiza Media";
	
	/**
	 * Path to pass image urls for resizing and effects. The servlet can be remote or local.  
	 * Populated by XML 
	 */
	private String dataProxyUrl;
	
	/**
	 * Path to pass image urls for resizing and effects. The servlet can be remote or local.  
	 * Populated by XML 
	 */
	private String imageProcessorUrl;
	
	/**
	 * Url to pass image urls for resizing and effects. The servlet can be remote or local.
	 * Populated by XML 
	 */
	private String imageProcessorUrlLocal;
	/**
	 * Specifies where Joomla related images are.  
	 * Populated by XML 
	 */
	private String joomlaImagesPath; 
	/**
	 * Populated by XML 
	 */
	private String joomlaImagesUrlLocal="";
	private Boolean hasBanners=true;
	private String servletContextName="";
	private String dbName="ibiza";
	private int bannerDelay=3000;
	
	
	
	private String www;
	private String cmMaster;
	private String cmMasterDevelopment;
	
	
	
	
	
	/**
	 * @return the imageProcessorUrl
	 */
	public String getImageProcessorUrl() {
		return imageProcessorUrl;
	}
	/**
	 * @param imageProcessorUrl the imageProcessorUrl to set
	 */
	public void setImageProcessorUrl(String imageProcessorUrl) {
		this.imageProcessorUrl = imageProcessorUrl;
	}
	/**
	 * @return the imageProcessorUrlLocal
	 */
	public String getImageProcessorUrlLocal() {
		return imageProcessorUrlLocal;
	}
	/**
	 * @param imageProcessorUrlLocal the imageProcessorUrlLocal to set
	 */
	public void setImageProcessorUrlLocal(String imageProcessorUrlLocal) {
		this.imageProcessorUrlLocal = imageProcessorUrlLocal;
	}
	/**
	 * @return the joomlaImagesPath
	 */
	public String getJoomlaImagesPath() {
		return joomlaImagesPath;
	}
	/**
	 * @param joomlaImagesPath the joomlaImagesPath to set
	 */
	public void setJoomlaImagesPath(String joomlaImagesPath) {
		this.joomlaImagesPath = joomlaImagesPath;
	}
	/**
	 * @return the joomlaImagesUrlLocal
	 */
	public String getJoomlaImagesUrlLocal() {
		return joomlaImagesUrlLocal;
	}
	/**
	 * @param joomlaImagesUrlLocal the joomlaImagesUrlLocal to set
	 */
	public void setJoomlaImagesUrlLocal(String joomlaImagesUrlLocal) {
		this.joomlaImagesUrlLocal = joomlaImagesUrlLocal;
	}
	/**
	 * @return the hasBanners
	 */
	public Boolean getHasBanners() {
		return hasBanners;
	}
	/**
	 * @param hasBanners the hasBanners to set
	 */
	public void setHasBanners(Boolean hasBanners) {
		this.hasBanners = hasBanners;
	}
	/**
	 * @return the servletContextName
	 */
	public String getServletContextName() {
		return servletContextName;
	}
	/**
	 * @param servletContextName the servletContextName to set
	 */
	public void setServletContextName(String servletContextName) {
		this.servletContextName = servletContextName;
	}
	/**
	 * @return the dbName
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return the appIdentifier
	 */
	public String getAppIdentifier() {
		return appIdentifier;
	}
	/**
	 * @param appIdentifier the appIdentifier to set
	 */
	public void setAppIdentifier(String appIdentifier) {
		this.appIdentifier = appIdentifier;
	}
	/**
	 * @return the bannerDelay
	 */
	public int getBannerDelay() {
		return bannerDelay;
	}
	/**
	 * @param bannerDelay the bannerDelay to set
	 */
	public void setBannerDelay(int bannerDelay) {
		this.bannerDelay = bannerDelay;
	}
	/**
	 * @return the defaultLat
	 */
	public double getDefaultLat() {
		return defaultLat;
	}
	/**
	 * @param defaultLat the defaultLat to set
	 */
	public void setDefaultLat(double defaultLat) {
		this.defaultLat = defaultLat;
	}
	/**
	 * @return the defaultLon
	 */
	public double getDefaultLon() {
		return defaultLon;
	}
	/**
	 * @param defaultLon the defaultLon to set
	 */
	public void setDefaultLon(double defaultLon) {
		this.defaultLon = defaultLon;
	}
	/**
	 * @return the dataProxyUrl
	 */
	public String getDataProxyUrl() {
		return dataProxyUrl;
	}
	/**
	 * @param dataProxyUrl the dataProxyUrl to set
	 */
	public void setDataProxyUrl(String dataProxyUrl) {
		this.dataProxyUrl = dataProxyUrl;
	}
	/**
	 * @return the www
	 */
	public String getWww() {
		return www;
	}
	/**
	 * @param www the www to set
	 */
	public void setWww(String www) {
		this.www = www;
	}
	/**
	 * @return the cmMaster
	 */
	public String getCmMaster() {
		return cmMaster;
	}
	/**
	 * @param cmMaster the cmMaster to set
	 */
	public void setCmMaster(String cmMaster) {
		this.cmMaster = cmMaster;
	}
	/**
	 * @return the cmMasterDevelopment
	 */
	public String getCmMasterDevelopment() {
		return cmMasterDevelopment;
	}
	/**
	 * @param cmMasterDevelopment the cmMasterDevelopment to set
	 */
	public void setCmMasterDevelopment(String cmMasterDevelopment) {
		this.cmMasterDevelopment = cmMasterDevelopment;
	}
	

	
	
}
