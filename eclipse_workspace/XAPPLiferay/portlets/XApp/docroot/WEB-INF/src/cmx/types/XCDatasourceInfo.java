package cmx.types;

import jsontype.ComposerPackages;

public class XCDatasourceInfo {
	public String url;
	
	public Boolean hasJSONP;
	
	public ComposerPackages composerPackages;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getHasJSONP() {
		return hasJSONP;
	}
	public void setHasJSONP(Boolean hasJSONP) {
		this.hasJSONP = hasJSONP;
	}
	public ComposerPackages getComposerPackages() {
		return composerPackages;
	}
	public void setComposerPackages(ComposerPackages composerPackages) {
		this.composerPackages = composerPackages;
	}
	
}
