package cmx.types;

public class XASFeature {

	public static final String Liferay="Liferay";
	public static final String Wordpress="Wordpress";
	public static final String Google="Google";
	public static final String Banner="Banner";
	public static final String Banner2="Banner2";
	public static final String Logo="Logo";
	public static final String Background="Background";
	public static final String GooglePicassa="GooglePicassa";
	public static final String GoogleCalendar="GoogleCalendar";
	public static final String VisualCSS="VisualCSS";
	public static final String ACECSS="ACECSS";

	public static final String VMart="VMart";
	public static final String Moset="Moset";
	public static final String BreezingForms="BreezingForms";
	public static final String MoPub="MoPub";
	public static final String Inherit="Inherit";
	
	public static final String Templates="Templates";
	public static final String TemplateConfig="TemplateConfig";
	public static final String TabletWeb="TabletWeb";
	public static final String CustomApp="CustomApp";
	public static final String XAS="XAS";
	public static final String CustomStyles="CustomStyles";
	public static final String XAppConnect="XAppConnect";
	public static final String CustomDataSource="CustomDataSource";
	public static final String Cloud9="Cloud9";
	
	public XASFeature(String _key,String _value)
	{
		value=_value;
		key=_key;
	}
	
	public String key;
	public String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
