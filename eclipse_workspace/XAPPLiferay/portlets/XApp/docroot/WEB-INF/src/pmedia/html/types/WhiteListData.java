package pmedia.html.types;

import java.util.ArrayList;
import java.util.Map;


/***
 * Data only class for feeding Jsoup's Whitelist for cleaning up html elements
 * @author mc007
 *
 */
public class WhiteListData
{
	private ArrayList<String> tagNames;
	
	 
	public  Map<String,ArrayList<String>> attributes;
	private ArrayList<String> enforcedAttributes;
	public  Map<String, Map<String, ArrayList<String>>> protocols; // allowed URL protocols for attributes
	
	
	private String platform = "IPHONE";

	public ArrayList<String> getTagNames() {
		return tagNames;
	}

	public void setTagNames(ArrayList<String> tagNames) {
		this.tagNames = tagNames;
	}
	
	public ArrayList<String> getEnforcedAttributes() {
		return enforcedAttributes;
	}

	public void setEnforcedAttributes(ArrayList<String> enforcedAttributes) {
		this.enforcedAttributes = enforcedAttributes;
	}

	

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Map<String, ArrayList<String>> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, ArrayList<String>> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Map<String, ArrayList<String>>> getProtocols() {
		return protocols;
	}

	public void setProtocols(Map<String, Map<String, ArrayList<String>>> protocols) {
		this.protocols = protocols;
	}

	



	
	
}
