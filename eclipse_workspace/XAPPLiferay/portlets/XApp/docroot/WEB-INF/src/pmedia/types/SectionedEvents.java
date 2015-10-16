package pmedia.types;

import java.util.ArrayList;

public class SectionedEvents 
{
	public String sectionText="";
	
	public ArrayList<EventData>events=new ArrayList<EventData>();
	/**
	 * @return the events
	 */
	public ArrayList<EventData> getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(ArrayList<EventData> events) {
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
