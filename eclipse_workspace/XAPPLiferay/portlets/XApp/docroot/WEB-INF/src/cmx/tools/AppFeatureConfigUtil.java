package cmx.tools;

import java.util.ArrayList;

import cmx.types.XASFeature;

public class AppFeatureConfigUtil {

	
	public static ArrayList<XASFeature>getFeatures()
	{
		ArrayList<XASFeature>result = new ArrayList<XASFeature>();

		result.add(new XASFeature(XASFeature.Liferay,""+AppFeatureConfigUtil.isEnabled(XASFeature.Liferay)));
		result.add(new XASFeature(XASFeature.Google,""+AppFeatureConfigUtil.isEnabled(XASFeature.Google)));
		result.add(new XASFeature(XASFeature.Wordpress,""+AppFeatureConfigUtil.isEnabled(XASFeature.Wordpress)));
		result.add(new XASFeature(XASFeature.Banner,""+AppFeatureConfigUtil.isEnabled(XASFeature.Banner)));
		result.add(new XASFeature(XASFeature.Background,""+AppFeatureConfigUtil.isEnabled(XASFeature.Background)));
		result.add(new XASFeature(XASFeature.Logo,""+AppFeatureConfigUtil.isEnabled(XASFeature.Logo)));
		result.add(new XASFeature(XASFeature.VisualCSS,""+AppFeatureConfigUtil.isEnabled(XASFeature.VisualCSS)));
		result.add(new XASFeature(XASFeature.ACECSS,""+AppFeatureConfigUtil.isEnabled(XASFeature.ACECSS)));
		result.add(new XASFeature(XASFeature.VMart,""+AppFeatureConfigUtil.isEnabled(XASFeature.VMart)));
		result.add(new XASFeature(XASFeature.BreezingForms,""+AppFeatureConfigUtil.isEnabled(XASFeature.BreezingForms)));
		result.add(new XASFeature(XASFeature.Moset,""+AppFeatureConfigUtil.isEnabled(XASFeature.Moset)));
		result.add(new XASFeature(XASFeature.Banner2,""+AppFeatureConfigUtil.isEnabled(XASFeature.Banner2)));
		result.add(new XASFeature(XASFeature.MoPub,""+AppFeatureConfigUtil.isEnabled(XASFeature.MoPub)));
		result.add(new XASFeature(XASFeature.Inherit,""+AppFeatureConfigUtil.isEnabled(XASFeature.Inherit)));
		result.add(new XASFeature(XASFeature.Templates,""+AppFeatureConfigUtil.isEnabled(XASFeature.Templates)));
		result.add(new XASFeature(XASFeature.TemplateConfig,""+AppFeatureConfigUtil.isEnabled(XASFeature.TemplateConfig)));
		result.add(new XASFeature(XASFeature.TabletWeb,""+AppFeatureConfigUtil.isEnabled(XASFeature.TabletWeb)));
		result.add(new XASFeature(XASFeature.CustomApp,""+AppFeatureConfigUtil.isEnabled(XASFeature.CustomApp)));
		result.add(new XASFeature(XASFeature.XAS,""+AppFeatureConfigUtil.isEnabled(XASFeature.XAS)));
		result.add(new XASFeature(XASFeature.CustomStyles,""+AppFeatureConfigUtil.isEnabled(XASFeature.CustomStyles)));
		result.add(new XASFeature(XASFeature.XAppConnect,""+AppFeatureConfigUtil.isEnabled(XASFeature.XAppConnect)));
		result.add(new XASFeature(XASFeature.CustomDataSource,""+AppFeatureConfigUtil.isEnabled(XASFeature.CustomDataSource)));
		result.add(new XASFeature(XASFeature.Cloud9,""+AppFeatureConfigUtil.isEnabled(XASFeature.Cloud9)));
		
		
		return result;
	}
	
	public static Boolean isEnabled(String key)
	{
		Boolean result = false;
		String value = System.getProperty("app.feature." + key);
		try {
			result= Boolean.parseBoolean(value);
		} catch (Exception e) {
			return false;
		}
		return result;
	}
	
}
