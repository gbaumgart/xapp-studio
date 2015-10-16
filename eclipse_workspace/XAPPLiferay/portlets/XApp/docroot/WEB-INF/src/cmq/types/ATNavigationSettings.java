package cmq.types;

public class ATNavigationSettings {
	
	public String type;
	public Boolean enabled;
	public String description;
	public String title;
	public long dataSourceTypeMask;
	public String iconClass;
	public Boolean hasSubs;
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	public long getDataSourceTypeMask() {
		return dataSourceTypeMask;
	}
	public void setDataSourceTypeMask(long dataSourceTypeMask) {
		this.dataSourceTypeMask = dataSourceTypeMask;
	}
	public String getIconClass() {
		return iconClass;
	}
	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
	
}
