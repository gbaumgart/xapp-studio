package pmedia.types;

public class CField
{
	
	int refId;
	public String refIdStr;
	public String title;
	public int sourceType;
	public String dsURL;
	public String value;
	public String iconUrl;
	
	public String dataSource;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String cssClass;
	
	public String cssBackgroundClass;

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getCssBackgroundClass() {
		return cssBackgroundClass;
	}

	public void setCssBackgroundClass(String cssBackgroundClass) {
		this.cssBackgroundClass = cssBackgroundClass;
	}

	public String getDsURL() {
		return dsURL;
	}

	public void setDsURL(String dsURL) {
		this.dsURL = dsURL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	


	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	public String getRefIdStr() {
		return refIdStr;
	}

	public void setRefIdStr(String refIdStr) {
		this.refIdStr = refIdStr;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
}
