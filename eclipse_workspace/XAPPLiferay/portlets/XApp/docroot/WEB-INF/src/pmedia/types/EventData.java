package pmedia.types;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import flexjson.JSON;
public class EventData extends BaseData implements Comparable<EventData>
{
		
	public CListItem getAsListItem(Locale local)
	{
		populateTime(local);
		return getAsListItem();
	}
	
	public CListItem getAsListItem()
	{
		if(listItem!=null){
			return listItem;
		}
		listItem = new CEventItem();
		((CEventItem)listItem).setRefId(refId);
		((CEventItem)listItem).setGroupId(groupId);
		((CEventItem)listItem).setTitle(title);
		
		/***
		 * TODO : remove ori class data
		 */
		((CEventItem)listItem).setDataClass("pmedia.types.EventData");
		
		
		((CEventItem)listItem).setIconUrl(getIconUrl());
		((CEventItem)listItem).setStartDate(start_time);
		((CEventItem)listItem).setEndDate(end_time);
		return listItem;
	}
	
	

	
	//public int event_id;
	
	///////////////////// : linked objects: 
	//category : 
	public Category cat;
	@JSON(include=false)
	public Category getCat() {return cat;}
	public void setCat(Category cat) {this.cat = cat;}

	//location : 
	public LocationData loc;
	@JSON(include=false)
	public LocationData getLoc() {	return loc;	}
	public void setLoc(LocationData loc) {	this.loc = loc;}
	public float noendtime;
	public String location;
	public String getLocation(){return location;}
	public void setLocation(String location) {	this.location = location;}
	
	public String categoryTitle;
	
	public Date time;
	public String timeString;
	public String date;
	public String timeStart;
	
	public String timeExtra;
	public String timeExtraStart;
	public String timeExtraEnd;
	
	public ArrayList<String> pictures=null;
	
	public String ticketAddress;
	public String getTicketAddress() {return ticketAddress;}
	public void setTicketAddress(String ticketAddress) {this.ticketAddress = ticketAddress;}
	
	public String ticketPostcode;
	@JSON(include=false)
	public String getTicketPostcode() {return ticketPostcode;}
	public void setTicketPostcode(String ticketPostcode) {this.ticketPostcode = ticketPostcode;}

	public String ticketPromoter;
	@JSON(include=false)
	public String getTicketPromoter() {return ticketPromoter;}
	public void setTicketPromoter(String ticketPromoter) {	this.ticketPromoter = ticketPromoter;}
	
	public String ticketLink;
	@JSON(include=false)
	public String getTicketLink() {return ticketLink;}
	public void setTicketLink(String ticketLink) {this.ticketLink = ticketLink;}
	
	public String ticketCategory="";
	@JSON(include=false)
	public String getTicketCategory() {return ticketCategory;}
	public void setTicketCategory(String ticketCategory) {this.ticketCategory = ticketCategory;}
	
	
	public String ticketPrice;
	@JSON(include=false)
	public String getTicketPrice() {return ticketPrice;}
	public void setTicketPrice(String ticketPrice) {this.ticketPrice = ticketPrice;}
	
	public String host;
	@JSON(include=false)
	public String getHost() {return host;}
	public void setHost(String host) {		this.host = host;}

	public String venue;
	@JSON(include=false)
	public String getVenue() {return venue;}
	public void setVenue(String venue) {this.venue = venue;}
	

	/////////////////////////////////////////////////////////////////
	//Facebook specific
	
	public String creator_id;
	@JSON(include=false)
	public String getCreator_id() {	return creator_id;}
	public void setCreator_id(String creator_id) {	this.creator_id = creator_id;}

	public Boolean trustedFBUser=false;
	@JSON(include=false)
	
	public Boolean getTrustedFBUser() {	return trustedFBUser;}
	public void setTrustedFBUser(Boolean trustedFBUser) {this.trustedFBUser = trustedFBUser;}

	public  String fbVenueStreet;
	@JSON(include=false)
	public String getFbVenueStreet() {	return fbVenueStreet;}
	public void setFbVenueStreet(String fbVenueStreet) {	this.fbVenueStreet = fbVenueStreet;}

	public  String fbVenueCity;
	@JSON(include=false)
	public String getFbVenueCity() {return fbVenueCity;}
	public void setFbVenueCity(String fbVenueCity) {	this.fbVenueCity = fbVenueCity;}

	public  String fbVenueState;
	@JSON(include=false)
	public String getFbVenueState() {return fbVenueState;}
	public void setFbVenueState(String fbVenueState) {this.fbVenueState = fbVenueState;}

	public  String fbVenueCountry;
	@JSON(include=false)
	public String getFbVenueCountry(){	return fbVenueCountry;}
	public void setFbVenueCountry(String fbVenueCountry){this.fbVenueCountry = fbVenueCountry;}

	public  String fbVenueLat;
	@JSON(include=false)
	public String getFbVenueLat() {return fbVenueLat;}
	public void setFbVenueLat(String fbVenueLat) {this.fbVenueLat = fbVenueLat;}

	public  String fbVenueLon;
	@JSON(include=false)
	public String getFbVenueLon() {return fbVenueLon;}
	public void setFbVenueLon(String fbVenueLon) {	this.fbVenueLon = fbVenueLon;}

	
	
	/////////////////////////////////////////////////////////////////
	// type related : 
	public Boolean isTicketSource=false;
	public Boolean getIsTicketSource() {	return isTicketSource;	}
	public void setIsTicketSource(Boolean isTicketSource) {this.isTicketSource = isTicketSource;}

	private int isSpaceHolder;
	public int getIsSpaceHolder(){return isSpaceHolder;}
	public void setIsSpaceHolder(int isSpaceHolder) {this.isSpaceHolder = isSpaceHolder;}

	
	public String eventSourceType= "database";
	public String getEventSourceType() {return eventSourceType;}
	public void setEventSourceType(String eventSourceType){this.eventSourceType = eventSourceType;}

	
	private String locationLogo;
	public boolean invalid;
	public float tags;
	public String extraInfo;

	public boolean didFlickrSearch=false;
	public boolean isDidFlickrSearch() {return didFlickrSearch;	}
	public void setDidFlickrSearch(boolean didFlickrSearch) {this.didFlickrSearch = didFlickrSearch;}

	
	/////////////////////////////////////////////////////////////////
	// Time specific
	

	public String sStartRepition;
	@JSON(include=true)
	public String getsStartRepition() {return sStartRepition;}
	public void setsStartRepition(String sStartRepition) {this.sStartRepition = sStartRepition;}

	public String sEndRepition;
	@JSON(include=true)
	public String getsEndRepition() {return sEndRepition;}
	public void setsEndRepition(String sEndRepition) {this.sEndRepition = sEndRepition;}

	
	public int repIndex;
	@JSON(include=false)
	public int getRepIndex() {return repIndex;}
	public void setRepIndex(int repIndex) {this.repIndex = repIndex;}

	public Date start_time;
	@JSON(include=true)
	public Date getStart_time() {		return start_time;	}
	public void setStart_time(Date start_time) {		this.start_time = start_time;	}

	public Date end_time;
	@JSON(include=true)
	public Date getEnd_time() {return end_time;	}
	public void setEnd_time(Date end_time) {this.end_time = end_time;	}


	public long start_time_e;
	@JSON(include=true)
	public long getStart_time_e(){return start_time_e;}
	public void setStart_time_e(long start_time_e){	this.start_time_e = start_time_e;}

	
	public long end_time_e;
	@JSON(include=true)
	public long getEnd_time_e() {	return end_time_e;}
	public void setEnd_time_e(long end_time_e) {this.end_time_e = end_time_e;}

	public long start_time_e_s;
	@JSON(include=false)
	public long getStart_time_e_s() {return start_time_e_s;}
	public void setStart_time_e_s(long start_time_e_s){this.start_time_e_s = start_time_e_s;}


	public long end_time_e_s;
	@JSON(include=false)
	public long getEnd_time_e_s() {		return end_time_e_s;	}
	public void setEnd_time_e_s(long end_time_e_s) {		this.end_time_e_s = end_time_e_s;	}


	///filter variables :
	public boolean matchVenue = false;
	@JSON(include=false)
	public boolean isMatchVenue() {		return matchVenue;	}
	public void setMatchVenue(boolean matchVenue) {		this.matchVenue = matchVenue;	}

	public boolean matchSummary = false;
	@JSON(include=false)
	public boolean isMatchSummary() {		return matchSummary;	}
	public void setMatchSummary(boolean matchSummary) {		this.matchSummary = matchSummary;	}


	public boolean matchHost = false;
	@JSON(include=false)
	public boolean isMatchHost() {		return matchHost;	}
	public void setMatchHost(boolean matchHost) {		this.matchHost = matchHost;	}

	public boolean matchName = false;
	@JSON(include=false)
	public boolean isMatchName() {		return matchName;	}
	public void setMatchName(boolean matchName) {		this.matchName = matchName;	}

	
	public boolean matchLocation = false;
	@JSON(include=false)
	public boolean isMatchLocation() {		return matchLocation;	}
	public void setMatchLocation(boolean matchLocation) {		this.matchLocation = matchLocation;	}
	
	public String getTimeStringStartEndHours()
	{

		Calendar startDate = Calendar.getInstance();
		String extraString="";
		String extraDescription="";
		String dateString = "";

		if(start_time!=null)
		{
			startDate.setTime(start_time);
			//startDate.add(Calendar.HOUR, -8);
			extraString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			dateString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			extraDescription ="";// startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1) + " ";

			extraString = "" + extraDescription;
			String minuteString = "" + startDate.get(Calendar.MINUTE);
			String hourString = "" + startDate.get(Calendar.HOUR_OF_DAY);
			if(minuteString.equals("0"))
			{
				minuteString = "00";
			}
			if(hourString.equals("0"))
			{
				hourString = "00";
			}
			extraDescription = extraDescription + hourString + ":" + minuteString ;

		}else
		{
			//System.out.println("start date invalid  : " + event.summary);
		}

		if(end_time!=null)
		{

			startDate.setTime(end_time);
			//extraString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			extraDescription +="  -  ";//+startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1) + " ";
			String minuteString = "" + startDate.get(Calendar.MINUTE);
			String hourString = "" + startDate.get(Calendar.HOUR_OF_DAY);
			if(minuteString.equals("0"))
			{
				minuteString = "00";
			}
			if(hourString.equals("0"))
			{
				hourString = "00";
			}
			extraDescription = extraDescription + hourString + ":" + minuteString ;
		}
		
		return extraDescription;
		//System.out.println("extra string : " + extraDescription + " " + event.summary );
	}

	public String getTimeStringStartEnd()
	{

		Calendar startDate = Calendar.getInstance();
		String extraString="";
		String extraDescription="";
		String dateString = "";

		if(start_time!=null)
		{
			startDate.setTime(start_time);
			//startDate.add(Calendar.HOUR, -8);
			extraString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			dateString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			extraDescription = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1) + " ";

			extraString = "" + extraDescription;
			String minuteString = "" + startDate.get(Calendar.MINUTE);
			String hourString = "" + startDate.get(Calendar.HOUR_OF_DAY);
			if(minuteString.equals("0"))
			{
				minuteString = "00";
			}
			if(hourString.equals("0"))
			{
				hourString = "00";
			}
			extraDescription = extraDescription + hourString + ":" + minuteString ;

		}else
		{
			//System.out.println("start date invalid  : " + event.summary);
		}

		if(end_time!=null)
		{

			startDate.setTime(end_time);
			//extraString = startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1);
			extraDescription +="  -  "+startDate.get(Calendar.DATE) + "." + (startDate.get(Calendar.MONTH)+1) + " ";
			String minuteString = "" + startDate.get(Calendar.MINUTE);
			String hourString = "" + startDate.get(Calendar.HOUR_OF_DAY);
			if(minuteString.equals("0"))
			{
				minuteString = "00";
			}
			if(hourString.equals("0"))
			{
				hourString = "00";
			}
			extraDescription = extraDescription + hourString + ":" + minuteString ;
		}
		
		return extraDescription;
		//System.out.println("extra string : " + extraDescription + " " + event.summary );
	}

	public void populateTime(Locale locale)
	{
		if(timeExtra==null)
			return;

		if(timeExtra.length() == 0)
			return;

		//split time
		//String splitted[] = org.apache.commons.lang.StringUtils.split(timeExtra,"-");
		String splitted[] = timeExtra.split("-");

		if(splitted !=null && splitted[0]!=null)
		{
			DateFormat format  = new java.text.SimpleDateFormat("dd MMM yyyy hh.mmaa",new Locale("en", "US"));

			String timeS = splitted[0];

			timeS = timeS.replace(" ","");
			timeS = timeS.replace(":",".");
			timeS = timeS.toLowerCase();

			String formatString = "dd MMM yyyy hh";

			if(timeS.contains("."))
				formatString+=".mm";


			if(timeS.contains("am")  || timeS.contains("pm") )
				formatString+="aa";

			format = new java.text.SimpleDateFormat(formatString,locale);

			Date start = new Date();

			try{
				start = format.parse( "30 Sep 2010 "+timeS);
				start_time.setHours(start.getHours());
				start_time.setMinutes(start.getMinutes());


			}catch(ParseException pe)
			{
				//System.out.println("invalid start date : " + splitted[0] + "  :: " + formatString);
			}
		}
		if(splitted !=null && splitted.length > 1 && splitted[1]!=null)
		{
			DateFormat format  = new java.text.SimpleDateFormat("dd MMM yyyy hh.mmaa",new Locale("en", "US"));
			String timeS = splitted[1];

			timeS = timeS.replace(" ","");
			timeS = timeS.replace(":",".");
			timeS = timeS.toLowerCase();

			String formatString = "dd MMM yyyy hh";

			if(timeS.contains("."))
				formatString+=".mm";


			if(timeS.contains("am")  || timeS.contains("pm") )
				formatString+="aa";

			format = new java.text.SimpleDateFormat(formatString,new Locale("en", "US"));

			Date start = new Date();

			try{
				start = format.parse( "30 Sep 2010 "+timeS);
				//System.out.println(" st " + start.toString() );
				end_time.setHours(start.getHours());
				end_time.setMinutes(start.getMinutes());


			}catch(ParseException pe)
			{
				//System.out.println("invalid end date : " + splitted[1] + "  :: " + formatString);
			}
		}
	}

	public void populateTime()
	{

		if(timeExtra==null)
			return;

		if(timeExtra.length() == 0)
			return;

		//split time
		//String splitted[] = org.apache.commons.lang.StringUtils.split(timeExtra,"-");
		String splitted[] = timeExtra.split("-");

		if(splitted !=null && splitted[0]!=null)
		{
			DateFormat format  = new java.text.SimpleDateFormat("dd MMM yyyy hh.mmaa",new Locale("en", "US"));

			String timeS = splitted[0];

			timeS = timeS.replace(" ","");
			timeS = timeS.replace(":",".");
			timeS = timeS.toLowerCase();

			String formatString = "dd MMM yyyy hh";

			if(timeS.contains("."))
				formatString+=".mm";


			if(timeS.contains("am")  || timeS.contains("pm") )
				formatString+="aa";

			format = new java.text.SimpleDateFormat(formatString,new Locale("en", "US"));

			Date start = new Date();

			try{
				start = format.parse( "30 Sep 2010 "+timeS);
				start_time.setHours(start.getHours());
				start_time.setMinutes(start.getMinutes());


			}catch(ParseException pe)
			{
				//System.out.println("invalid start date : " + splitted[0] + "  :: " + formatString);
			}
		}
		if(splitted !=null && splitted.length > 1 && splitted[1]!=null)
		{
			DateFormat format  = new java.text.SimpleDateFormat("dd MMM yyyy hh.mmaa",new Locale("en", "US"));
			String timeS = splitted[1];

			timeS = timeS.replace(" ","");
			timeS = timeS.replace(":",".");
			timeS = timeS.toLowerCase();

			String formatString = "dd MMM yyyy hh";

			if(timeS.contains("."))
				formatString+=".mm";


			if(timeS.contains("am")  || timeS.contains("pm") )
				formatString+="aa";

			format = new java.text.SimpleDateFormat(formatString,new Locale("en", "US"));

			Date start = new Date();

			try{
				start = format.parse( "30 Sep 2010 "+timeS);
				//System.out.println(" st " + start.toString() );
				end_time.setHours(start.getHours());
				end_time.setMinutes(start.getMinutes());


			}catch(ParseException pe)
			{
				//System.out.println("invalid end date : " + splitted[1] + "  :: " + formatString);
			}
		}



	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//		SQL
	//
	public String getUpdateString() {

		return "";

		/*
		String ret = new String( "UPDATE Picture SET title = \'"
                + DataMiddle.escapeSingleQuote( m_Title )
                + "\', description = \'"
                + DataMiddle.escapeSingleQuote( m_Description )
                + "\', voting = \'"
                + DataMiddle.escapeSingleQuote( m_voting.GetAsSQLString() )
                + "\' WHERE id_picture = \'" + m_ID_Picture
                + "\'" );
		return ret;
		*/

	}
	public String dumpAsHTML(int index)
	{

		if(creator_id !=null && uid!=null && creator_id.length() > 0 && uid.length() > 0)
		{
			String type = "";

			if(matchHost)
				type =  " by host match";

			if(matchLocation)
				type =  " by location match";

			if(matchVenue)
				type =  " by venue match";

			if(matchSummary)
				type =  " by summary match";

			String creator ="<b><a href=\'" + "http://www.facebook.com/profile.php?id=" + creator_id + "\' target=_blank>" + "  Creator" + "  </a></b>";
			String out ="<b>" + index + ".</b>" + "__" +   "<a href=\'" + "http://www.facebook.com/event.php?eid=" + uid + "\' target=_blank>" + title + "</a>" + creator + type;

			//String out = "\n"  + summary + "\n";

			if(loc !=null && loc.title !=null && loc.title.length() > 0)
			{
				out+="<b> Location : " + loc.title + "</b>" + "</br>" + "</br>";
			}
			//<a href="http://www.facebook.com/profile.php?id=584771820">Stefan Alzate Ramirez</a>
			return out;
		}
		return "";
	}

	public int compareTo(EventData o) 
	{
		int result = 0 ;
		if(o!=null && o.start_time!=null)
			result = start_time.getTime() > o.start_time.getTime() ? 1 : 0;
    	//System.out.println(a.start_time +   "  - " + b.start_time + " = " + result );
    	return result;
	}
	/**
	 * @return the categoryTitle
	 */
	public String getCategoryTitle() {
		return categoryTitle;
	}
	/**
	 * @param categoryTitle the categoryTitle to set
	 */
	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}


}
