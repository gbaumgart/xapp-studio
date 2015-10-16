package cmx.types;

import java.util.ArrayList;

public class Resource 
{
	
	private ArrayList<ConfigurableInformation>parameters;
	
	public String runTimeConfiguration="";
	
	public boolean enabled=true;
	
	public boolean addFileTime=false;
	
	
	public int order=0;
	
	public String absoluteRootUrl="";
	
	public String platform="";
	/**
	 * Local Path on disc
	 */
	public String name="";
	/**
	 * Local Path on disc
	 */
	public String path="";
	
	/**
	 * Dynamic resolved by host. Relative or absolute.
	 */
	public String url="";
	
	/**
	 * Relative or absolute.
	 */
	public String urlOri="";
	
	
	/***
	 * Mount point in web : XApp-portlet/[mountPoint] 
	 */
	public String mountPoint="";
	
	/***
	 * Local, Proxy, JavaScript, CSS 
	 */
	public String type="";

	public String getRunTimeConfiguration() {
		return runTimeConfiguration;
	}

	public void setRunTimeConfiguration(String runTimeConfiguration) {
		this.runTimeConfiguration = runTimeConfiguration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlOri() {
		return urlOri;
	}

	public void setUrlOri(String urlOri) {
		this.urlOri = urlOri;
	}

	public String getMountPoint() {
		return mountPoint;
	}

	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public ArrayList<ConfigurableInformation> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<ConfigurableInformation> parameters) {
		this.parameters = parameters;
	}

	public String getAbsoluteRootUrl() {
		return absoluteRootUrl;
	}

	public void setAbsoluteRootUrl(String absoluteRootUrl) {
		this.absoluteRootUrl = absoluteRootUrl;
	}

	public boolean isAddFileTime() {
		return addFileTime;
	}

	public void setAddFileTime(boolean addFileTime) {
		this.addFileTime = addFileTime;
	}
	
}
