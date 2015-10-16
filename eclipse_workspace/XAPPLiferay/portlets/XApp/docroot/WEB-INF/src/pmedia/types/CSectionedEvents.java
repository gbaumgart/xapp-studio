package pmedia.types;

import java.util.ArrayList;

public class CSectionedEvents 
{
	public String sectionText="";
	
	public ArrayList<CContent>events=new ArrayList<CContent>();
	/**
	 * @return the events
	 */
	public ArrayList<CContent> getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(ArrayList<CContent> events) {
		this.events = events;
	}
	/**
	 * @return the sectionText
	 */
	public String getSectionText() {
		return sectionText;
	}
	/**
	 * @param sectionText the sectionText to set
	 */
	public void setSectionText(String sectionText) {
		this.sectionText = sectionText;
	}
}
