package cmq.types;

import java.util.ArrayList;

public class ApplicationTemplate 
{
	int order;
	public String applicationThemeTemplate;
	
	public String applicationTemplatePlatform;
	
	public String applicationTemplateIdentifier;
	
	public String applicationTemplateTitle;
	
	public String applicationTemplatePicture;
	
	public String applicationTemplateCSS;
	
	public ArrayList<ATNavigationSettings>navSettings;
	
	public String description;
	
	public String iconSet;
	
	public String templateIdentifier;
	
	public ArrayList<ATNavigationSettings> getNavSettings() 
	{
		return navSettings;
	}
	public void setNavSettings(ArrayList<ATNavigationSettings> navSettings) {
		this.navSettings = navSettings;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIconSet() {
		return iconSet;
	}
	public void setIconSet(String iconSet) {
		this.iconSet = iconSet;
	}
	public String getTemplateIdentifier() {
		return templateIdentifier;
	}
	public void setTemplateIdentifier(String templateIdentifier) {
		this.templateIdentifier = templateIdentifier;
	}
	public String getApplicationTemplateIdentifier() {
		return applicationTemplateIdentifier;
	}
	public void setApplicationTemplateIdentifier(
			String applicationTemplateIdentifier) {
		this.applicationTemplateIdentifier = applicationTemplateIdentifier;
	}
	public String getApplicationTemplateTitle() {
		return applicationTemplateTitle;
	}
	public void setApplicationTemplateTitle(String applicationTemplateTitle) {
		this.applicationTemplateTitle = applicationTemplateTitle;
	}
	public String getApplicationTemplatePlatform() {
		return applicationTemplatePlatform;
	}
	public void setApplicationTemplatePlatform(String applicationTemplatePlatform) {
		this.applicationTemplatePlatform = applicationTemplatePlatform;
	}
	public String getApplicationThemeTemplate() {
		return applicationThemeTemplate;
	}
	public void setApplicationThemeTemplate(String applicationThemeTemplate) {
		this.applicationThemeTemplate = applicationThemeTemplate;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getApplicationTemplatePicture() {
		return applicationTemplatePicture;
	}
	public void setApplicationTemplatePicture(String applicationTemplatePicture) {
		this.applicationTemplatePicture = applicationTemplatePicture;
	}
	public String getApplicationTemplateCSS() {
		return applicationTemplateCSS;
	}
	public void setApplicationTemplateCSS(String applicationTemplateCSS) {
		this.applicationTemplateCSS = applicationTemplateCSS;
	}
	
	
}
