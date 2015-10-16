package pmedia.types;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import cmx.types.ECMContentSourceType;
import flexjson.*;
public class CListItemBanner extends CListItem
{
	public String params;
	
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
