package cmx.types;

public class ServiceSettings {

	public String FeatherEditorApiKey;
	//remote: pm.cmSession.getWebserverHostURL() +"/ajaxplorer/?external_selector_type=popup&relative_path=" + assetFolder + "&filetypes=jpeg+jpg+gif+png",
	public String AjaxplorerRootUrl;
	
	public String WebPath;
	
	public String ImageScaleServlet;
	
	

	
	public String getWebPath() {
		return WebPath;
	}

	public void setWebPath(String webPath) {
		WebPath = webPath;
	}

	/**
	 * @return the ajaxplorerRootUrl
	 */
	public String getAjaxplorerRootUrl() {
		return AjaxplorerRootUrl;
	}

	/**
	 * @param ajaxplorerRootUrl the ajaxplorerRootUrl to set
	 */
	public void setAjaxplorerRootUrl(String ajaxplorerRootUrl) {
		AjaxplorerRootUrl = ajaxplorerRootUrl;
	}

	/**
	 * @return the featherEditorApiKey
	 */
	public String getFeatherEditorApiKey() {
		return FeatherEditorApiKey;
	}

	/**
	 * @param featherEditorApiKey the featherEditorApiKey to set
	 */
	public void setFeatherEditorApiKey(String featherEditorApiKey) {
		FeatherEditorApiKey = featherEditorApiKey;
	}

	public String getImageScaleServlet() {
		return ImageScaleServlet;
	}

	public void setImageScaleServlet(String imageScaleServlet) {
		ImageScaleServlet = imageScaleServlet;
	}
	
	
}
