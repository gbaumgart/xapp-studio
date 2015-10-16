package pmedia.types;
import java.util.ArrayList;

import pmedia.DataUtils.TranslationTools;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import flexjson.*;

public class Category  extends BaseData
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public CListItem getAsListItem()
	{
		CListItem item = super.getAsListItem();

		item.setDataClass("pmedia.types.Category");
		item.setRefId(refId);
		getPicture();
		getPictures();
		getIconUrl();
		item.setIconUrl(getIconUrl());
		
		return item;
	}
	
	public String color;
	
	public int access;
	
	public int intendLevel=0;
	public int categorySourceType=-1;
	
	//public String type;
	
	private String localizedTitle=null;
	public String getLocalizedTitle(){return localizedTitle;}
	public void setLocalizedTitle(String localizedTitle) {this.localizedTitle = localizedTitle;}
	
	
	public String ErrorReport="";
	@JSON(include=false)
	public String getErrorReport() {return ErrorReport;}
	public void setErrorReport(String errorReport) {ErrorReport = errorReport;}

	
	
	
	
	public ArrayList<LocationData>locations=null;
	@JSON(include=false)
	public ArrayList<LocationData> getLocations() {	return locations;}
	public void setLocations(ArrayList<LocationData> locations) {	this.locations = locations;}


	public ArrayList<EventData>eventsAll=null;
	@JSON(include=false)
	public ArrayList<EventData> getEventsAll() {return eventsAll;}
	public void setEventsAll(ArrayList<EventData> eventsAll) {	this.eventsAll = eventsAll;}


	public ArrayList<EventData>eventsFB=null;
	@JSON(include=false)
	public ArrayList<EventData> getEventsFB() {return eventsFB;}
	public void setEventsFB(ArrayList<EventData> eventsFB) {	this.eventsFB = eventsFB;}

	public ArrayList<EventData>eventsTickets=null;
	@JSON(include=false)
	public ArrayList<EventData> getEventsTickets() {return eventsTickets;}
	public void setEventsTickets(ArrayList<EventData> eventsTickets) {this.eventsTickets = eventsTickets;}

	public ArrayList<TranslationData>translations=null;
	
	public TranslationData getTranslation(String lang)
	{
		if(translations!=null)
		{
			for (int s = 0; s < translations.size() ; s++) 
			{
				TranslationData trans = translations.get(s);
				if(trans.lCode.equals(lang))
					return trans;
			}
		}
		
		return null;
			
	}


	public String getId()
	{
		String a = ""+ id;
		return a;
	}
	/**
	 * @return the intendLevel
	 */
	public int getIntendLevel() {
		return intendLevel;
	}
	/**
	 * @param intendLevel the intendLevel to set
	 */
	public void setIntendLevel(int intendLevel) {
		this.intendLevel = intendLevel;
	}
	/**
	 * @return the categorySourceType
	 */
	public int getCategorySourceType() {
		return categorySourceType;
	}
	/**
	 * @param categorySourceType the categorySourceType to set
	 */
	public void setCategorySourceType(int categorySourceType) {
		this.categorySourceType = categorySourceType;
	}
	
}
