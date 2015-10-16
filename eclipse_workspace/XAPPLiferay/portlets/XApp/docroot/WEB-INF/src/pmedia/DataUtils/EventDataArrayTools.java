package pmedia.DataUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pmedia.types.Category;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MediaItemBase;
import pmedia.types.MediaType;
import pmedia.utils.EventFilterTools;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
import pmedia.utils.TimeUtils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class EventDataArrayTools
{

	public static int CONST_CITY_CATS[]= {};
	public static void convertToXML2(ArrayList<EventData> src,String path,Boolean addCData,String range) throws ParserConfigurationException
	{

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	
		documentBuilderFactory.setValidating(false);
    	DocumentBuilder documentBuilder;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();

    	Element rootElement = document.createElement("Results");
    	document.appendChild(rootElement);
    	rootElement.setAttribute("range", range);


		for (int s = 0; s < src.size() ; s++)
		{
		    EventData e = src.get(s);
		    if(e !=null && e.start_time !=null && e.end_time!=null)
		    {
		    	Element itemRoot = toXML(e, document);
		    	rootElement.appendChild(itemRoot);
		    }
		}

		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream( new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		OutputFormat of = new OutputFormat("XML","UTF-8",true);
		of.setIndent(1);
		of.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(fos,of);
		try {
			serializer.asDOMSerializer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serializer.serialize( document );
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	public static void updateCategoryTitle(String lang,ArrayList<EventData>events,ArrayList<Category>categories)
	{
		for (int s = 0; s < events.size() ; s++) 
		{
			EventData event = events.get(s);
			
			if(event.cat==null)
			{
				event.cat =CategoryTools.getCatByIndex(categories, event.getGroupId());
			}
			
			if(event.cat==null)
				continue;

			event.setGroupStr(event.cat.getTitle());
		}
	}
	
	public static void sort(Vector<EventData> a)
	{
	
		if(a==null || a.size()==0)
			return;
	  for(int i=0; i<a.size()-1; i++)
	  {
	      int pos = findMinPos(a,i);
	      swap(a,i,pos);
	     }
	 }
	private static void swap(Vector<EventData>a,int pos1,int pos2)
	 {
		EventData temp = a.get(pos1);
		a.set(pos1, a.get(pos2));
		//a[pos1]=a[pos2];
		//a[pos2]=temp;
		a.set(pos2, temp);
	 }
	private static int findMinPos(Vector<EventData> a,int pos)
	{
	  for(int i=pos+1; i<a.size(); i++)
	     if(a.get(i).getStart_time().compareTo(a.get(pos).getStart_time())<0)
	        pos=i;
	  return pos;
	 }
	
	public static void sort(ArrayList<EventData> a)
	{
	  for(int i=0; i<a.size()-1; i++)
	  {
	      int pos = findMinPos(a,i);
	      swap(a,i,pos);
	     }
	 }
	private static void swap(ArrayList<EventData>a,int pos1,int pos2)
	 {
		EventData temp = a.get(pos1);
		a.set(pos1, a.get(pos2));
		//a[pos1]=a[pos2];
		//a[pos2]=temp;
		a.set(pos2, temp);
	 }
	private static int findMinPos(ArrayList<EventData> a,int pos)
	{
	  for(int i=pos+1; i<a.size(); i++)
	     if(a.get(i).getStart_time().compareTo(a.get(pos).getStart_time())<0)
	        pos=i;
	  return pos;
	 }
	public static ArrayList<EventData>sortByDate(ArrayList<EventData>src)
	{
		   EventData res[] = new EventData[src.size()]; 
		   //EventData number[] = new EventData[src.size()];
		   int nElems = src.size();
		   int in, out;
		   for (out = 0; out < nElems; out++) 
		   {
			   EventData temp = src.get(out);
			   in = out;
			   EventData tempA = src.get(in -1);
			   if(temp!=null && tempA!=null)
			   {
				   //int compare = tempA.getStart_time().compareTo(temp.getStart_time());
				   //System.out.println("comparing  tempA : " + tempA.getStart_time() + " with : " + temp.getStart_time() + "  = " +  compare);
				   
			   }else
			   {
				   System.out.println("have null element");
			   }
			   try {
				   while (in > 0 && (src.get(in - 1).getStart_time().compareTo(temp.getStart_time())) > 0) 
				   {
				        res[in] = res[in - 1];
				        --in;
				   }
					
			   } catch (Exception e) {
						// TODO: handle exception
				   System.out.println("crash");
				   e.printStackTrace();
				   continue;
				
			   }
			   
		      res[in] = temp;
			}

		   ArrayList<EventData>result = new ArrayList<EventData>();
		   for (out = 0; out < nElems; out++) 
		   {
			   EventData temp = res[out];
			   if(temp!=null)
				   result.add(temp);
		   }
		   
		   return result;
	   }
	public static ArrayList<EventData>sortByDate(Vector<EventData>src)
	{
		   EventData res[] = new EventData[src.size()]; 
		   //EventData number[] = new EventData[src.size()];
		   int nElems = src.size();
		   int in, out;
		   for (out = 1; out < nElems; out++) 
		   {
			   EventData temp = src.get(out);
			   in = out;
			   EventData tempA = src.get(in -1);
			   if(temp!=null && tempA!=null)
			   {
				   
				   int compare = tempA.getStart_time().compareTo(temp.getStart_time());
				   //System.out.println("comparing  tempA : " + tempA.getStart_time() + " with : " + temp.getStart_time() + "  = " +  compare);
				   
			   }else
			   {
				   System.out.println("have null element");
			   }

			   while (in > 0 && src.get(in - 1).getStart_time().compareTo(temp.getStart_time()) > 0) 
			   {
			        res[in] = res[in - 1];
			        --in;
			   }
		      res[in] = temp;
			}
		//return res;
		   
		   ArrayList<EventData>result = new ArrayList<EventData>();
		   for (out = 0; out < nElems; out++) 
		   {
			   EventData temp = res[out];
			   if(temp!=null)
				   result.add(temp);
		   }
		   
		   return result;
	   }
	public static EventData updateTicketCategory(EventData e,HashMap<String,String> trustedTicketLocations, ArrayList<Category> cats,HashMap<Integer,Integer>categoryMappings )
	{

		if(e.title.equals("Jongleurs Posh"))
		{
//			System.out.println("found trusted ticket : ");
		}

		String sCat = "";
		if(e.loc !=null   )
		{
			String meta = trustedTicketLocations.get( Integer.toString(e.loc.location_id) );
			if(meta !=null)
			{

				String ticketCatSingle = null;
				if( e.ticketCategory != null && e.ticketCategory.length() > 0)
					ticketCatSingle = CategoryTools.getTicketCategory( e.ticketCategory );

				Category c = null;


				if(ticketCatSingle !=null)
				{
					//find ticket category :
					c= CategoryTools.getCatByName(cats, ticketCatSingle );
				}else
				{

					int catID = Integer.parseInt(trustedTicketLocations.get( Integer.toString(e.loc.location_id)));
					if(catID!=0)
					{
						c= CategoryTools.getCatByIndex(cats, catID );

						if(c!=null)
						{
							//System.out.println("found trusted ticket : " + e.summary + " for cat  : " + c.title);
						}
					}
				}

				if(c!=null)
				{

					//System.out.println("linking " + ticketCatSingle + " to : " + c.title);
					e.groupId = c.id;
					if(categoryMappings !=null && categoryMappings.containsKey(c.id))
					{
						int dstKey = categoryMappings.get( c.id );
						if(dstKey !=0)
						{
							Category dstCat = CategoryTools.getCatByIndex( cats,dstKey);
							if(dstCat !=null)
							{
								//System.out.println("remap ticket category : " + c.title + " to : " + dstCat.title);
								e.groupId = dstCat.id;
							}
						}

					}

				}else
				{
					//System.out.println( "couldn't link ::::::" + ticketCatSingle + " for Ticket : " + e.summary);
				}


			}

		}
		return e;
	}

	public static EventData updateFBCategory(EventData e,HashMap<String,String> trustedFBUsers,ArrayList<Category> cats,HashMap<Integer,Integer>categoryMappings  )
	{

		if(e.title.contains("I Love Pacha"))
		{
			System.out.println("stop");
		}

		String sCat = "";
		String meta = trustedFBUsers.get( e.creator_id );
		if(meta !=null)
		{

			if( meta.contains("|"))
			{
				//String split[] = meta.split("|");
				String split[] = meta.split("|");
				try
				{
					if(split !=null  && split.length > 0 && split[1] !=null )
					{
						sCat = split[1];
						//System.out.println(sCat);

						e.groupId = Integer.parseInt(split[1]);

						if(categoryMappings !=null && categoryMappings.containsKey(e.groupId))
						{
							int dstKey = categoryMappings.get( e.groupId );
							if(dstKey !=0)
							{
								Category dstCat = CategoryTools.getCatByIndex( cats,dstKey);
								if(dstCat !=null)
								{
									//System.out.println("remap facebook category : " + e.groupId + " to : " + dstCat.title);
									e.groupId = dstCat.id;
								}
							}

						}
					}
				}catch(Exception err)
				{
					e.groupId = 0;
				}
			}else
			{

				try{
					String splitLocs[] = meta.split(";");
					if(splitLocs !=null  && splitLocs.length > 0 )
					{
						if(e.loc!=null)
						{
							for( int s = 0 ; s < splitLocs.length ; s ++ )
							{

								String split[] = splitLocs[s].split("-");
								if(split!=null && split.length > 1 && split[0] !=null && split[1] !=null)
								{

									if(split[0].equals( Integer.toString( e.loc.location_id )))
									{
										e.groupId = Integer.parseInt( split[1] );
									}
								}
							}
						}
					}
				}catch(Exception err)
				{
					e.groupId = 0;
				}
			}
		}
		return e;

	}

	public static Element toXMLSmall(EventData e,Document document,Boolean addCData)
	{
		Element itemRoot = document.createElement("item");

		itemRoot.setAttribute("lID", e.location !=null ? Integer.toString(e.loc.location_id) : "" );
		itemRoot.setAttribute("cID", e.groupId !=0 ? Integer.toString(e.groupId) : "" );

		//itemRoot.setAttribute("title", e.summary);

		Element title = document.createElement("title");
    	title.appendChild(document.createTextNode(e.title));
    	itemRoot.appendChild(title);
    	itemRoot.setAttribute("i", e.uid);


    	Element sTime = document.createElement("sTime");
    	String dateString = null;
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        dateString = df.format(e.start_time);
        sTime.appendChild(document.createTextNode(dateString));
    	itemRoot.setAttribute("s", dateString);

    	Element eTime = document.createElement("eTime");
    	df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String eDateString = df.format(e.end_time);

        if(!eDateString.equals(dateString)){
	        eTime.appendChild(document.createTextNode(dateString));
	    	itemRoot.setAttribute("e",eDateString );
		}


    	Element nTime = document.createElement("noETime");
        nTime.appendChild(document.createTextNode(e.noendtime > 0 ? "true" : "false"));
    	itemRoot.setAttribute("nE",e.noendtime > 0 ? "1" : "0");

    	if(e.iconUrl !=null && e.iconUrl.length() > 0)
    	{
	    	Element pic = document.createElement("pic");
	        pic.appendChild(document.createTextNode(e.iconUrl));
	    	itemRoot.appendChild(pic);
    	}


    	////////////////////////////////////////////////////////////////
    	//
    	//		ticket link
    	//
    	String dLink ="";
    	if(e.eventSourceType.equals("Tickets"))
    	{

    		e.ticketLink = e.ticketLink.replace("[[", "");
    		e.ticketLink = e.ticketLink.replace("]]", "");
    		dLink = e.ticketLink;

    	}

    	if(e.eventSourceType.equals("Facebook"))
    	{
    		dLink = "http://www.facebook.com/event.php?eid=" + e.uid;
    	}

    	if(dLink.length() > 0)
    	{
	    	Element tLink = document.createElement("l");
	        tLink.appendChild(document.createTextNode(dLink));
	    	itemRoot.appendChild(tLink);
    	}
    	///////////////////////////////////////////////////////////////////
    	//
    	//		Ticket price
    	//
    	String ticketPrice = "";
    	
    	String detailString = e.description;
    	if(e.description !=null && e.description.length() > 0)
    	{
    		if(addCData)

    		detailString ="<![CDATA[" + e.description +"]]>";
	    	Element descr = document.createElement("detail");
	        descr.appendChild(document.createTextNode(detailString));
	    	itemRoot.appendChild(descr);
    	}

    	    	/*
    	Element locID = document.createElement("lID");
    	String locationID =  "";
    	if(e.loc !=null){locationID+=Integer.toString(e.loc.location_id) ;}
    	locID.appendChild(document.createTextNode(locationID));
    	itemRoot.appendChild(locID);


    	Element cID = document.createElement("cID");
    	String cIDText = e.cat !=null ? Integer.toString(e.cat.id) : "0";
        cID.appendChild(document.createTextNode(cIDText));
    	itemRoot.appendChild(cID);*/

    	if(e.extraInfo !=null && e.extraInfo.length() > 0)
    	{
    		Element extra = document.createElement("extra");
    		extra.appendChild(document.createTextNode(e.extraInfo));
	    	itemRoot.appendChild(extra);
    	}

		return itemRoot;

	}

	public static Element toXML(EventData e,Document document)
	{
		Element itemRoot = document.createElement("item");

		itemRoot.setAttribute("eid", ""+e.refId);
		
		Element title = document.createElement("title");
    	title.appendChild(document.createTextNode(e.title));
    	itemRoot.appendChild(title);

    	Element sTime = document.createElement("sTime");
    	String dateString = null;
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateString = df.format(e.start_time);
        sTime.appendChild(document.createTextNode(dateString));
    	itemRoot.appendChild(sTime);

    	Element eTime = document.createElement("eTime");
    	df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateString = df.format(e.end_time);
        eTime.appendChild(document.createTextNode(dateString));
    	itemRoot.appendChild(eTime);

    	Element nTime = document.createElement("noETime");
        nTime.appendChild(document.createTextNode(e.noendtime > 0 ? "true" : "false"));
    	itemRoot.appendChild(nTime);
    	if(e.iconUrl!=null && e.iconUrl.length() >0)
    	{
    		Element pic = document.createElement("pic");
            pic.appendChild(document.createTextNode(e.iconUrl));
        	itemRoot.appendChild(pic);
    	}
    	/*
    	Element pic = document.createElement("pic");
        pic.appendChild(document.createTextNode(e.stdPictureLink));
    	itemRoot.appendChild(pic);*/

    	//String detailString ="<![CDATA[" + e.description +"]]>";
    	Element descr = document.createElement("detail");
        //descr.appendChild(document.createTextNode(detailString));
        descr.appendChild(document.createTextNode(e.description));
    	itemRoot.appendChild(descr);

    	Element locID = document.createElement("lID");
    	String locationID ="";
    	
    	if(e.locRefStr==null)
    	{
    		locationID+=Integer.toString(e.locRefId) ;
    	}else{
    		locationID+=e.locRefStr;
    	}
    	
    	locID.appendChild(document.createTextNode(locationID));
    	itemRoot.appendChild(locID);


    	Element cID = document.createElement("cID");
    	String cIDText = Integer.toString(e.groupId);
        cID.appendChild(document.createTextNode(cIDText));
    	itemRoot.appendChild(cID);


    	Element extra = document.createElement("extra");
    	extra.appendChild(document.createTextNode(e.extraInfo));
    	itemRoot.appendChild(extra);


		return itemRoot;

	}

	
	public static ArrayList<EventData>readLocalTicketsFromFileInternalFormat(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
		ArrayList<EventData > result = new ArrayList<EventData>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new InputStreamReader(new FileInputStream(file), "UTF-8")));
		//Document doc = db.parse(new InputSource(new InputStreamReader(new FileInputStream(file), "ISO-8859-1")));
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("item");
		for (int s = 0; s < nodeLst.getLength(); s++)
		{
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	//get uid :

		    	EventData e = new EventData();
		    	e.isTicketSource = true;
		    	e.eventSourceType = "Tickets";
		    	Element fstElmnt = (Element) fstNode;
		    	e.uid = fstElmnt.getAttribute("id");
		    	String venue = "";
		    	
		    	//Element venueElement = fstNode.getE .getElementsByTagName("venue");
		    	//venue = venueElement.getAttribute("title");


		    	// promoter :
		    	/*NodeList fstNmElmntLstPromoter = fstElmnt.getElementsByTagName("promoter");
		        if(fstNmElmntLstPromoter !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLstPromoter.item(0);
			        if(fstNmElmnt !=null)
			        {
			        	//String a = fstNmElmnt.getAttribute("title");
			        	e.ticketPromoter = fstNmElmnt.getAttribute("name");
			        }
		        }*/


		        /*NodeList fstNmElmntLstImages = fstElmnt.getElementsByTagName("images");
		        if(fstNmElmntLstImages !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLstImages.item(0);
			        if(fstNmElmnt !=null)
			        {

			        	NodeList fstNmElmntAddress = fstNmElmnt.getElementsByTagName("medium");
				        if( fstNmElmntAddress != null)
				        {
				        	Element adressElement = (Element) fstNmElmntAddress.item(0);
				        	if(adressElement !=null)
				        	{
				        		NodeList adressNodeLst = adressElement.getChildNodes();
				        		if(adressNodeLst !=null && adressNodeLst.item(0) !=null )
				        		{
				        			e.stdPictureLink = ((Node)adressNodeLst.item(0)).getNodeValue();
				        			//System.out.println("address: " + e.stdPictureLink);
				        		}
				        	}
				        }
			        }
		        }*/


				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		venue name
		        //
		        /*
		        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("venue");
		        if(fstNmElmntLst !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			        if(fstNmElmnt !=null)
			        {
			        	//String a = fstNmElmnt.getAttribute("title");
			        	e.venue = fstNmElmnt.getAttribute("title");
			        	e.host = fstNmElmnt.getAttribute("title");
			        }

			        NodeList fstNmElmntAddress = fstNmElmnt.getElementsByTagName("address");
			        if( fstNmElmntAddress != null)
			        {
			        	Element adressElement = (Element) fstNmElmntAddress.item(0);
			        	if(adressElement !=null)
			        	{
			        		NodeList adressNodeLst = adressElement.getChildNodes();
			        		if(adressNodeLst !=null && adressNodeLst.item(0) !=null )
			        		{
			        			e.ticketAddress = ((Node)adressNodeLst.item(0)).getNodeValue();
			        			//System.out.println("address: " + address);
			        			//e.stdPictureLink = ((Node)stdPic.item(0)).getNodeValue();
			        			//System.out.println("std pic : " + e.stdPictureLink);
				        	}
			        	}
			        }

			        NodeList fstNmElmntPostCode = fstNmElmnt.getElementsByTagName("postcode");
			        if( fstNmElmntPostCode != null)
			        {
			        	Element pcElement = (Element) fstNmElmntPostCode.item(0);
			        	if(pcElement !=null)
			        	{
			        		NodeList pcNodeLst = pcElement.getChildNodes();
			        		if(pcNodeLst !=null && pcNodeLst.item(0) !=null )
			        		{
			        			e.ticketPostcode = ((Node)pcNodeLst.item(0)).getNodeValue();
			        			//System.out.println("postcode: " + e.ticketPostcode);
			        			//e.stdPictureLink = ((Node)stdPic.item(0)).getNodeValue();
			        			//System.out.println("std pic : " + e.stdPictureLink);
				        	}
			        	}
			        }
		        }
		        */

				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		summary
		        //
		        NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("title");
		        Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		        NodeList lstNm = lstNmElmnt.getChildNodes();
		        e.title = ((Node)lstNm.item(0)).getNodeValue();

		        e.start_time = new Date();
		        e.end_time = new Date();




		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		event flyer
		        //
		        NodeList stdPicElmntLst = fstElmnt.getElementsByTagName("mPic");
		        if( stdPicElmntLst != null)
		        {
		        	Element stdPicElmnt = (Element) stdPicElmntLst.item(0);
		        	if(stdPicElmnt !=null)
		        	{
		        		NodeList stdPic = stdPicElmnt.getChildNodes();
		        		if(stdPic !=null && stdPic.item(0) !=null )
		        		{
		        			e.iconUrl = ((Node)stdPic.item(0)).getNodeValue();
		        			if(e.iconUrl!=null && e.iconUrl.length() > 0)
		        			{
		        				e.pictures = new ArrayList<String>();
		        				e.pictures.add(e.iconUrl);
		        			}
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		location
		        //
		        NodeList locElmntLst = fstElmnt.getElementsByTagName("location");
		        if( locElmntLst != null)
		        {
		        	Element locElmnt = (Element) locElmntLst.item(0);
		        	if(locElmnt !=null)
		        	{
		        		NodeList loc = locElmnt.getChildNodes();
		        		if(loc !=null && loc.item(0) !=null )
		        		{
		        			e.location = ((Node)loc.item(0)).getNodeValue();
		        			//e.venue = e.location;
		        			//e.host = e.location;
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }
		        
		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		extra
		        //
		        NodeList extraElmntLst = fstElmnt.getElementsByTagName("extra");
		        if( extraElmntLst != null)
		        {
		        	Element locElmnt = (Element) extraElmntLst.item(0);
		        	if(locElmnt !=null)
		        	{
		        		NodeList loc = locElmnt.getChildNodes();
		        		if(loc !=null && loc.item(0) !=null )
		        		{
		        			e.extraInfo = ((Node)loc.item(0)).getNodeValue();
		        			//e.venue = e.location;
		        			//e.host = e.location;
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }
		        
		        NodeList descrElmntLst = fstElmnt.getElementsByTagName("detail0");
		        if( descrElmntLst != null)
		        {
		        	Element locElmnt = (Element) descrElmntLst.item(0);
		        	if(locElmnt !=null)
		        	{
		        		NodeList loc = locElmnt.getChildNodes();
		        		if(loc !=null && loc.item(0) !=null )
		        		{
		        			e.description = ((Node)loc.item(0)).getNodeValue();
		        			//e.venue = e.location;
		        			//e.host = e.location;
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }



		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		ticket link
		        //
		        NodeList tlElmntLst = fstElmnt.getElementsByTagName("dlink");
		        if( locElmntLst != null)
		        {
		        	Element tlElmnt = (Element) tlElmntLst.item(0);
		        	if(tlElmnt !=null)
		        	{
		        		NodeList tLink= tlElmnt.getChildNodes();
		        		if(tLink !=null && tLink.item(0) !=null )
		        		{
		        			e.ticketLink = ((Node)tLink.item(0)).getNodeValue();
			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		ticket category
		        //
		        NodeList catElmntLst = fstElmnt.getElementsByTagName("tCat");
		        if( catElmntLst != null)
		        {
		        	Element catElmnt = (Element) catElmntLst.item(0);
		        	if(catElmnt !=null)
		        	{
		        		NodeList tCat= catElmnt.getChildNodes();
		        		if( tCat !=null && tCat.item(0) !=null )
		        		{
		        			String cat = ((Node)tCat.item(0)).getNodeValue();

		        			if(cat!=null)
		        				e.ticketCategory = cat;

			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		timeExtra : used by weGotTickets only
		        //
		        NodeList timeElmntLst = fstElmnt.getElementsByTagName("timeExtra");
		        if( timeElmntLst != null)
		        {
		        	Element timeElmnt = (Element) timeElmntLst.item(0);
		        	if(timeElmnt !=null)
		        	{
		        		NodeList time = timeElmnt.getChildNodes();
		        		if(time !=null && time.item(0) !=null )
		        		{
		        			e.timeExtra = ((Node)time.item(0)).getNodeValue();

		        			//e.venue = e.location;
		        			//e.host = e.location;
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }

				/////////////////////////////////////////////////////////////////////////////////
		        //
		        //		start_time
		        //


		        NodeList stElmntLst = fstElmnt.getElementsByTagName("sTime");
		        if( stElmntLst != null)
		        {
		        	Element stElmnt = (Element) stElmntLst.item(0);
		        	if(stElmnt !=null)
		        	{
		        		NodeList st = stElmnt.getChildNodes();
		        		if(st !=null && st.item(0) !=null )
		        		{
		        			String startTime = ((Node)st.item(0)).getNodeValue();

		        			String type = stElmnt.getAttribute("type");
		        			//System.out.println("type :: " + type);

		        			DateFormat format = null;
		        			Boolean was=false;
		        			if(type.equals("MEDIUM-SHORT"))
		        			{
		        				//format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);



		        				String splitted[] = startTime.split(",");
		        				was = true;
		        				if(splitted!=null && splitted.length > 1)
		        				{
		        					startTime = splitted[1];
		        				}
		        				startTime = startTime.trim();
		        				format = new java.text.SimpleDateFormat("dd MMM yyyy",new Locale("en", "US"));

		        			}else
		        			{
		        				format  = new java.text.SimpleDateFormat(type);
		        			}
		        			//DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		        			try{
		        				e.start_time = format.parse(startTime);
		        				/*if(was)
		        					System.out.println(" st " + startTime + "::" + e.start_time.toString());*/

		        			}catch(ParseException pe)
		        			{
		        				System.out.println("invalid date : " + startTime);
		        				e.start_time = new Date();

		        				continue;
		        			}
			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////
		        //
		        //		end_time
		        //

		        NodeList etElmntLst = fstElmnt.getElementsByTagName("eTime");
		        if( etElmntLst != null)
		        {
		        	Element etElmnt = (Element) etElmntLst.item(0);
		        	if(etElmnt !=null)
		        	{
		        		NodeList et = etElmnt.getChildNodes();
		        		if(et !=null && et.item(0) !=null )
		        		{
		        			String endTime = ((Node)et.item(0)).getNodeValue();


		        			String type = etElmnt.getAttribute("type");
		        			//System.out.println("type :: " + type);

		        			DateFormat format = null;
		        			Boolean was=false;
		        			if(type.equals("MEDIUM-SHORT"))
		        			{
		        				//format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);

		        				String splitted[] = endTime.split(",");
		        				was = true;
		        				if(splitted!=null && splitted.length > 1)
		        				{
		        					endTime = splitted[1];
		        				}
		        				endTime = endTime.trim();
		        				format = new java.text.SimpleDateFormat("dd MMM yyyy",new Locale("en", "US"));

		        			}else
		        			{
		        				format  = new java.text.SimpleDateFormat(type);
		        			}
		        			//DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		        			try{
		        				e.end_time = format.parse(endTime);
		        				/*if(was)
		        					System.out.println(" et " + endTime + "::" + e.end_time.toString());*/

		        			}catch(ParseException pe)
		        			{
		        				System.out.println("invalid date : " + endTime);
		        				e.start_time = new Date();
		        				continue;
		        			}
			        	}
		        	}
		        }




		        // paris mobile !!
		        NodeList priceElmntLst = fstElmnt.getElementsByTagName("price");
		        if( priceElmntLst != null)
		        {
		        	Element priceElmnt = (Element) priceElmntLst.item(0);
		        	if(priceElmnt !=null)
		        	{
		        		NodeList price = priceElmnt.getChildNodes();
		        		if(price!=null && price.item(0) !=null )
		        		{
		        			e.ticketPrice = ((Node)price.item(0)).getNodeValue();
			        	}
		        	}
		        }

		        // paris mobile !!
		        NodeList pricesElmntLst = fstElmnt.getElementsByTagName("prices");
		        if( pricesElmntLst != null)
		        {

		        	/*if(pricesElmntLst.getLength() > 1 )
		        	{
		        		System.out.println( "l::" + pricesElmntLst.getLength());
		        	}

		        	for( int i = 0 ;  i <  pricesElmntLst.getLength() ; i ++ )
        			{

        			}*/

		        	Element pricesElmnt = (Element) pricesElmntLst.item(0);
		        	if(pricesElmnt !=null)
		        	{

		        		 NodeList pr = pricesElmnt.getElementsByTagName("price");
		        		 if(pr!=null)
		        		 {

		        			 
		        			for( int i = 0 ;  i <  pr.getLength() ; i ++ )
			        		{

		        				Element pE = (Element) pr.item(i);
		    		        	if(pE !=null)
		    		        	{

		    		        		NodeList p = pE.getChildNodes();
		    		        		if(p !=null && p.item(0) !=null )
		    		        		{
		    		        			
		    		        		}



		    		        	}

			        		}
		        		 }
		        	}
		        }



		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //
		        /*NodeList liElmntLst = fstElmnt.getElementsByTagName("locRefId");
		        if( liElmntLst != null)
		        {
		        	Element liElmnt = (Element) liElmntLst.item(0);
		        	if(liElmnt !=null)
		        	{
		        		NodeList li = liElmnt.getChildNodes();
		        		if(li !=null && li.item(0) !=null )
		        		{
		        			e.locRefId = Integer.parseInt(((Node)li.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }*/

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //

		        /*NodeList cElmntLst = fstElmnt.getElementsByTagName("groupId");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			e.groupId = Integer.parseInt(((Node)c.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }
		        */
		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		extra info
		        //
		        /*
		        NodeList exElmntLst = fstElmnt.getElementsByTagName("extra");
		        if( exElmntLst != null)
		        {
		        	Element exElmnt = (Element) exElmntLst.item(0);
		        	if(exElmnt !=null)
		        	{
		        		NodeList extra = exElmnt.getChildNodes();
		        		if(extra !=null && extra.item(0) !=null )
		        		{
		        			e.extraInfo = ((Node)extra.item(0)).getNodeValue();
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }
		        */

		        e.populateTime();
		        result.add(e);
		    }
		 }
		//System.out.println("Information of all employees");
		return result;
	}

	public static boolean isCityCategory(int groupId)
	{
		int i = 0 ;
		for( i  = 0 ; i < CONST_CITY_CATS.length ; i++)
		{
			if(CONST_CITY_CATS[i] == groupId)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isValidCategory(String []cats, int groupId)
	{
		int i = 0 ;
		for( i  = 0 ; i < cats.length ; i++)
		{
			if(Integer.parseInt(cats[i]) == groupId)
			{
				return true;
			}
		}
		return false;
	}

	public static ArrayList<EventData>determineCatId(ArrayList<EventData>srcEvents,ArrayList<EventData>pastEvents,ArrayList<Category>srcCategories)
	{
		for (int s = 0; s < srcEvents.size() ; s++)
		{
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	ArrayList<EventData> locEvents = getEventsForLocation(pastEvents,e.loc);
		    	if(locEvents !=null && locEvents.size() > 0)
		    	{
		    		ArrayList cats = getCatIDs(locEvents);
		    		if(cats !=null && cats.size() > 0 )
		    		{
		    			int finalCategory = getMostUsedCatID(cats);
		    			e.groupId = finalCategory;
		    			Category c = CategoryTools.getCatByIndex(srcCategories, e.groupId);
				    	if(c !=null)
				    	{
				    		e.cat = c;

				    		//System.out.println("e: title : summary" + e.summary + "\n for category " + c.title);
				    	}
		    		}
		    	}
		    }
		}

		return srcEvents;
	}

	public static ArrayList<EventData>cleanFBEvents(ArrayList<EventData>srcEvents,ArrayList<EventData>fbEvents)
	{
		if(srcEvents == null){
			System.out.println("clean fb events : srcEvents = null");
			return null;

		}

		if(fbEvents == null){
			System.out.println("clean fb events : fbEvents = null");
			return null;
		}

		ArrayList<EventData>result = new ArrayList<EventData>();

		for (int s = 0; s < srcEvents.size() ; s++)
		{
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	/*String test = "Matinee - Opening";
	    		if(e.summary.toLowerCase().contains(test.toLowerCase()))
	    		{
	    			System.out.println("stop here");
	    		}
	    		*/

		    	/*if(e.summary.contentEquals("NOVA FLOW @ SATURDAY NIGHT PROJECT - PRIVILEGE (Coco Loco)"))
	    		{
		    		//System.out.println("stop here");
	    		}
		    	*/
		    	EventData matchEvent = findEventByLocationAndDate(fbEvents, e,true);
		    	if(matchEvent !=null)
		    	{
		    		//System.out.println("found uplicatie event in fb : " + matchEvent.summary + "eSummary:" + e.summary +  "\n\t" + "fbtime:" + matchEvent.start_time.toString() + " eTime : " + e.start_time.toString() );
		    		fbEvents.remove(matchEvent);
		    	}else
		    	{
		    		result.add(matchEvent);
		    	}
		    }
		}
		return fbEvents;
	}

	public static EventData findEventByLocationAndDate(ArrayList<EventData>FBEvents,EventData testEvent,boolean isFBEvent)
	{
		for (int s = 0; s < FBEvents.size() ; s++)
		{
		    EventData e = FBEvents.get(s);
		    if(e !=null)
		    {

		    	/*
		    	if(testEvent.summary.contentEquals("NOVA FLOW @ SATURDAY NIGHT PROJECT - PRIVILEGE (Coco Loco)"))
	    		{
	    			if(e.summary.contentEquals("NOVA FLOW @ SATURDAY NIGHT PROJECT - PRIVILEGE (Coco Loco)"))
		    		{

		    			System.out.println("stop here");
		    			System.out.println("stop here");
		    			System.out.println("stop here");
		    			System.out.println("stop here");
		    			System.out.println("stop here");


		    		}

	    		}
		    	*/
		    	if(e.loc !=null && testEvent.loc!=null && (e.loc == testEvent.loc))
		    	{


		    		Date d1  = (Date) e.start_time.clone();
		    		Date d2 = (Date) testEvent.start_time.clone();

		    		/*
		    		d1.setHours(0);
		    		d1.setMinutes(0);

		    		d2.setHours(0);
		    		d2.setMinutes(0);
		    		*/


		    		/*
		    		if(isFBEvent)
		    		{
		    			d1.setDate( d1.getDate() );
		    		}
		    		*/

		    		long ddiff = TimeUtils.daysBetween(d1, d2);
		    		if(ddiff == 0)
		    		{
		    			return e;
		    		}
		    	}
		    }
		}
		return null;
	}

	public static int getMatchCount(String a,String b)
	{

    	String aTerms[] = StringHelper.split(a.toLowerCase()," ");
    	String bTerms[] = StringHelper.split(b.toLowerCase()," ");


    	int nbMatches = 0;
		for(int lti = 0 ; lti < aTerms.length ; lti++ )
		{

			for(int eti = 0 ; eti < bTerms.length ; eti++ )
			{
				String lText = aTerms[lti];
				String eText = bTerms[eti];
				if(lText.equalsIgnoreCase(eText))
				{
					nbMatches++;
				}
			}
		}
		return nbMatches;
	}

	public static Boolean isExactMatch(String a,String b)
	{
		String aTerms[] = StringHelper.split(a.toLowerCase()," ");
    	String bTerms[] = StringHelper.split(b.toLowerCase()," ");

    	int nbMatches = 0;
		for(int lti = 0 ; lti < aTerms.length ; lti++ )
		{

			for(int eti = 0 ; eti < bTerms.length ; eti++ )
			{
				String lText = aTerms[lti];
				String eText = bTerms[eti];
				if(lText.equalsIgnoreCase(eText))
				{
					nbMatches++;
				}
			}
		}

		if( nbMatches == aTerms.length && nbMatches == bTerms.length )
			return true;

		return false;
	}


	public static LocationData getBestLocation(ArrayList<LocationData>locations,String eventLocationString)
	{
		LocationData testLocation = null;

		for (int ls = 0; ls < locations.size() ; ls++)
		{
   		    LocationData l = locations.get(ls);

   		    if(testLocation==null)
   		    {
   		    	testLocation = l;
   		    }

   		    ////////////length test
   		    if( isCityCategory(testLocation.groupId) && !isCityCategory(l.groupId))
	   		{
	   			testLocation = l;
	   		}

   		    /*
   		    if(l.location_id ==213)
   			{
   				System.out.println("stop");
   			}
   		    */
   		    //if(testLocation.groupId != CONST_CITY_CAT && l.groupId !=CONST_CITY_CAT)

	   		if( !isCityCategory(testLocation.groupId) && !isCityCategory(l.groupId))
	   		{
	   			//testLocation = l;

	   			/*
	   			if(l.title.length()  > testLocation.title.length())
			    {
	   				testLocation = l;

	   				if(l.title.toLowerCase().contains("san juan"))
		   			{
		   				System.out.println("stop");
		   			}

			    }
	   			*/


	   			int matchCountPrev =getMatchCount(testLocation.title, eventLocationString);
	   			int matchCountNow =getMatchCount(l.title, eventLocationString);

	   			if(matchCountNow >  matchCountPrev)
	   				testLocation = l;

	   			if( isExactMatch(l.title, eventLocationString ))
	   				return l;


    			/*
    			if(nbMatches >0 && locs.contains(l) == false )
    				locs.add(l);

    			System.out.println("word by word for " + l.title + " and " +e.location + "\n\t words in evenLocation : " +eventLocationTerms.length + "\n\t words in testLocation :  " + testLocationTerms.length  + "\n\t single match count : " + nbMatches );
    			*/
		    			//String locTerms[] = StringHelper.split(l.title," ");

	   		}

		}
		return testLocation;
	}


	public static String findLocation(HashMap<String,String> helpers,String searchTerm)
	{

		java.util.Collection<String>values = helpers.values();

		//Iterator<Map<String,String>> itr = (Iterator<Map<String, String>>) helpers.entrySet();

		/*for (
			    Iterator itr = (Iterator) helpers.entrySet();
			    itr.hasNext();
			)
		{

			Map.Entry<String,String> entry = (Map.Entry)itr.next();
		    String key = (String)entry.getKey();
		    String value = (String)entry.getValue();

		    if(key.contains("la grange"))
		    {
				System.out.println("found");
		    }

			int distance = StringHelper.computeLevenshteinDistance(key,searchTerm );
			int wDiff = Math.abs(key.length() - searchTerm.length());
			if(distance <= 5 && wDiff <=4 )
    		{
    			return helpers.get(key);
    		}

		}
		*/

		Set st = helpers.keySet();

		Iterator itr = st.iterator();
		while(itr.hasNext())
		{
			String locTerm =(String)itr.next();
			/*Map.Entry<String,String> entry = (Map.Entry)itr.next();
		    String key = (String)entry.getKey();
		    String value = (String)entry.getValue();
		    */
		    if(searchTerm.contains("ue du faubourg du"))
		    {
				//System.out.println("found");
		    }

			int distance = StringHelper.computeLevenshteinDistance(locTerm,searchTerm );
			int wDiff = Math.abs(locTerm.length() - searchTerm.length());
			if(distance < 2 && wDiff <=4 )
    		{
    			return helpers.get(locTerm);
    		}
		}


		/* ArrayList<String> searchTermLocationStrings= StringHelper.split2(l.title.toLowerCase(), " ",2,blackList);
		    int wordcount = 0;
		    LocationSearchResult sResult = new LocationSearchResult();

		    for (int i = 0; i < searchTermLocationStrings.size() ; i++)
		    {

		    	String testTermLoc = searchTermLocationStrings.get(i);
		    	for (int j = 0; j < searchTermStrings.size() ; j++)
	   		    {
		    		String testTerm = searchTermStrings.get(j);
		    		int distance = StringHelper.computeLevenshteinDistance(testTermLoc,testTerm );
		    		if(distance <= 2 )
		    		{
		    			wordcount++;
		    		}

		    		if(wordcount >0)
		    		{
		    			sResult.charMatchCount = testTermLoc.length() - testTerm.length();
		    		}

	*/
		//System.out.println(itr.next());
		return "";
	}

	public static ArrayList<EventData>updateLocation(ArrayList<EventData>srcEvents,ArrayList<LocationData>srcLocations,String domain,javax.sql.DataSource ds)
	{

		if(srcEvents==null)
			return null;

		HashMap<String,String>result = new HashMap<String, String>();

		if(srcLocations !=null)
		{
			for (int s = 0; s < srcEvents.size() ; s++)
			{
			    EventData e = srcEvents.get(s);
			    if(e !=null && e.location !=null )
			    {


			    	String tl = "L'ETAGE CLUB (Paris) // 77, rue du Faubourg du Temple // 75010 Paris // M�tro Goncourt (ligne 11) // ENTREE GRATUITE POUR TOUS!!!!";
			    	if(e.location.equals("L'ETAGE CLUB (Paris) // 77, rue du Faubourg du Temple // 75010 Paris // M�tro Goncourt (ligne 11) // ENTREE GRATUITE POUR TOUS!!!!"))
			    	{
			    		//System.out.print("stop");
			    	}
			    	if(e.title.contains("RAHZEL (The Roots / USA)"))
			    	{
			    		//System.out.print("stop");
		    		}
			    	if(e.uid !=null)
			    	{
			    		if(e.uid.equals("128297870553656"))
			    		{
				    		//System.out.print("stop");
			    		}
			    	}

			    	//System.out.print(e.location + "\n");

			    	if(e.location.toLowerCase().equals(tl.trim().toLowerCase()))
			    	{
			    		//System.out.print("stop");
			    	}

			    	String locIDHelper ="";

			    	if(e.location !=null && e.location.length() > 0)
			    	{
			    		//locIDHelper = result.get(e.location.trim().toLowerCase());
			    		locIDHelper = findLocation(result, e.location.trim().toLowerCase());
			    	}


			    	if(locIDHelper !=null && locIDHelper.length() > 0)
			    	{
			    		e.loc = LocationArrayTools.getLocationByIndex(srcLocations, Integer.parseInt(locIDHelper));
			    		//System.out.println("setting ticket location : " + e.location + " to "  + e.loc.title);
			    		if(e.loc !=null)
			    			continue;

			    	}


			    	e.loc = LocationArrayTools.findBest(e.location.trim(),srcLocations,domain);

			    	if(e.title.equals("KRS-ONE"))
			    	{
			    		//System.out.print("stop");
			    	}


			    }
			 }
		}
		return srcEvents;
	}
	
public static EventData getByUID(ArrayList<EventData>srcEvents,String test)
	{
		for (int s = 0; s < srcEvents.size() ; s++)
		{
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	if(e.uid !=null && e.uid.length() > 0 && e.uid.contentEquals(test))
		    	{
		    		return e;
		    	}
		    }
		}

		return null;
	}
	public static EventData getByUIDVector(Vector<EventData> srcEvents,String test)
	{
		for (int s = 0; s < srcEvents.size() ; s++)
		{
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	if(e.uid !=null && e.uid.length() > 0 && e.uid.contentEquals(test))
		    	{
		    		return e;
		    	}
		    }
		}

		return null;
	}
	public static ArrayList<EventData>findByLocationMatch(ArrayList<EventData>srcEvents,ArrayList<LocationData>srcLocations)
	{
		String terms[] = LocationArrayTools.getLocationTitleList(srcLocations);

		ArrayList<EventData>result = new ArrayList<EventData>();

		for (int s = 0; s < srcEvents.size() ; s++)
		{
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {

		    	////////////////////////////
		    	//test venue field :
		    	boolean matchVenue = false;
		    	boolean matchHost = false;

		    	boolean matchLocation = false;
		    	boolean matchSummary= false;

		    	if(e.title !=null && e.title.length() > 0)
		    	{
		    		matchSummary = StringHelper.equalsAnyIgnoreCase(e.title,terms);
		    		if(matchSummary)
		    		{
		    			//System.out.println("found event by summary match " + e.summary);
		    			e.matchSummary= true;
		    			result.add(e);
		    		}
		    	}

		    	if(e.venue !=null && e.venue.length() > 0 && !result.contains(e))
		    	{
		    		matchVenue = StringHelper.equalsAnyIgnoreCase(e.venue,terms);
		    		if(matchVenue)
		    		{
		    			//System.out.println("found event by venue match " + e.summary + " with venue: " + e.venue);
		    			e.matchVenue= true;
		    			result.add(e);
		    		}
		    	}

		    	if(e.host !=null && e.host.length() > 0 && !result.contains(e))
		    	{
		    		matchHost = StringHelper.equalsAnyIgnoreCase(e.host,terms);
		    		if(matchHost)
		    		{
		    			//System.out.println("found event by host match " + e.summary +" with host : " + e.host);
		    			e.matchHost = true;
		    			result.add(e);
		    		}
		    	}

		    	if(e.location !=null && e.location.length() > 0 && !result.contains(e))
		    	{
		    		matchLocation= StringHelper.equalsAnyIgnoreCase(e.location,terms);
		    		if(matchLocation)
		    		{
		    			//System.out.println("found event by location match " + e.summary +" with location : " + e.location);
		    			result.add(e);
		    			e.matchLocation = true;
		    		}
		    	}
		    }
		 }
		return srcEvents;
	}

	public static ArrayList<EventData>updateCategoryData(ArrayList<EventData>srcEvents,ArrayList<Category>srcCategories)
	{
		if(srcEvents == null )
			return srcEvents;

		for (int s = 0; s < srcEvents.size() ; s++)
		 {
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	Category c = CategoryTools.getCatByIndex(srcCategories, e.groupId);
		    	if(c !=null)
		    	{
		    		e.cat = c;
		    		e.categoryTitle = c.title;
		    	}else
		    	{
		    		e.cat= null;
		    		e.categoryTitle = "";
		    	}
		    }
		 }
		return srcEvents;

	}

	public static ArrayList getCatIDs(ArrayList<EventData>srcEvents)
	{
		ArrayList result = new ArrayList();
		for (int s = 0; s < srcEvents.size() ; s++)
		{
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	Integer id = e.groupId;
		    	result.add(id);
		    }
		}
		return result;
	}

	public static int getNumCatID(ArrayList src,Integer groupId)
	{
		int op = 0;
		for (int s = 0; s < src.size() ; s++)
		{
			Integer cCat  = (Integer)src.get(s);
			if(cCat == groupId )
			{
				op++;
			}
		}

		return op;
	}
	public static int getMostUsedCatID(ArrayList src)
	{

		if(src.size() == 0)
			return 0;

		int result =0;
		for (int s = 0; s < src.size() ; s++)
		{
			Integer cCat  = (Integer)src.get(s);
			int count = getNumCatID(src, cCat);
			if(count > result)
			{
				result = cCat;
			}
		}
		return result;
	}



	public static ArrayList<EventData>getEventsForLocation(ArrayList<EventData>srcEvents,LocationData location)
	{
		if(srcEvents ==null )
			return null;

		ArrayList<EventData > result = new ArrayList<EventData>();

		for (int s = 0; s < srcEvents.size() ; s++)
		 {
		    EventData e = srcEvents.get(s);
		    if(e !=null && e.loc != null && e.loc == location)
		    {
		    	result.add(e);
		    }
		 }
		return result;
	}

	public static ArrayList<EventData>updateLocationData(ArrayList<EventData>srcEvents,ArrayList<LocationData>srcLocations)
	{
		if(srcEvents == null)
			return srcEvents;

		if(srcLocations ==null )
			return srcEvents;

		for (int s = 0; s < srcEvents.size() ; s++)
		 {
		    EventData e = srcEvents.get(s);
		    if(e !=null)
		    {
		    	if(e.loc!=null){
		    		continue;
		    	}
		    	
		    	LocationData l = LocationArrayTools.getLocationByIndex(srcLocations, e.locRefId);
		    	if(l !=null)
		    	{
		    		e.loc = l;
		    		e.location = l.title;
		    		e.locRefStr=l.title;
		    	}
		    }
		 }
		return srcEvents;
	}

	public static ArrayList<EventData>readLocalTicketsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));

		ArrayList<EventData > result = new ArrayList<EventData>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());

		NodeList nodeLst = doc.getElementsByTagName("event");

		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	//get uid :

		    	EventData e = new EventData();
		    	e.isTicketSource = true;
		    	Element fstElmnt = (Element) fstNode;
		    	e.uid = fstElmnt.getAttribute("id");
		    	String venue = "";

		    	//Element venueElement = fstNode.getE .getElementsByTagName("venue");
		    	//venue = venueElement.getAttribute("title");


		    	// promoter :
		    	NodeList fstNmElmntLstPromoter = fstElmnt.getElementsByTagName("promoter");
		        if(fstNmElmntLstPromoter !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLstPromoter.item(0);
			        if(fstNmElmnt !=null)
			        {
			        	//String a = fstNmElmnt.getAttribute("title");
			        	e.ticketPromoter = fstNmElmnt.getAttribute("name");
			        }
		        }
		        NodeList fstNmElmntLstImages = fstElmnt.getElementsByTagName("images");
		        if(fstNmElmntLstImages !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLstImages.item(0);
			        if(fstNmElmnt !=null)
			        {

			        	NodeList fstNmElmntAddress = fstNmElmnt.getElementsByTagName("medium");
				        if( fstNmElmntAddress != null)
				        {
				        	Element adressElement = (Element) fstNmElmntAddress.item(0);
				        	if(adressElement !=null)
				        	{
				        		NodeList adressNodeLst = adressElement.getChildNodes();
				        		if(adressNodeLst !=null && adressNodeLst.item(0) !=null )
				        		{
				        			e.iconUrl = ((Node)adressNodeLst.item(0)).getNodeValue();
				        			//System.out.println("address: " + e.stdPictureLink);
				        		}
				        	}
				        }
			        }
		        }


				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		venue name
		        //
		        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("venue");
		        if(fstNmElmntLst !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			        if(fstNmElmnt !=null)
			        {
			        	//String a = fstNmElmnt.getAttribute("title");
			        	e.venue = fstNmElmnt.getAttribute("title");
			        	e.host = fstNmElmnt.getAttribute("title");
			        }

			        NodeList fstNmElmntAddress = fstNmElmnt.getElementsByTagName("address");
			        if( fstNmElmntAddress != null)
			        {
			        	Element adressElement = (Element) fstNmElmntAddress.item(0);
			        	if(adressElement !=null)
			        	{
			        		NodeList adressNodeLst = adressElement.getChildNodes();
			        		if(adressNodeLst !=null && adressNodeLst.item(0) !=null )
			        		{
			        			e.ticketAddress = ((Node)adressNodeLst.item(0)).getNodeValue();
			        			//System.out.println("address: " + address);
			        			//e.stdPictureLink = ((Node)stdPic.item(0)).getNodeValue();
			        			//System.out.println("std pic : " + e.stdPictureLink);
				        	}
			        	}
			        }

			        NodeList fstNmElmntPostCode = fstNmElmnt.getElementsByTagName("postcode");
			        if( fstNmElmntPostCode != null)
			        {
			        	Element pcElement = (Element) fstNmElmntPostCode.item(0);
			        	if(pcElement !=null)
			        	{
			        		NodeList pcNodeLst = pcElement.getChildNodes();
			        		if(pcNodeLst !=null && pcNodeLst.item(0) !=null )
			        		{
			        			e.ticketPostcode = ((Node)pcNodeLst.item(0)).getNodeValue();
			        			//System.out.println("postcode: " + e.ticketPostcode);
			        			//e.stdPictureLink = ((Node)stdPic.item(0)).getNodeValue();
			        			//System.out.println("std pic : " + e.stdPictureLink);
				        	}
			        	}
			        }

		        }

				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		summary
		        //
		        NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("title");
		        Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		        NodeList lstNm = lstNmElmnt.getChildNodes();
		        e.title = ((Node)lstNm.item(0)).getNodeValue();

		        e.start_time = new Date();
		        e.end_time = new Date();


		        //System.out.println("name : " + ((Node) lstNm.item(0)).getNodeValue());


		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		event flyer
		        //
		        /*NodeList stdPicElmntLst = fstElmnt.getElementsByTagName("std_pic");
		        if( stdPicElmntLst != null)
		        {
		        	Element stdPicElmnt = (Element) stdPicElmntLst.item(0);
		        	if(stdPicElmnt !=null)
		        	{
		        		NodeList stdPic = stdPicElmnt.getChildNodes();
		        		if(stdPic !=null && stdPic.item(0) !=null )
		        		{
		        			e.stdPictureLink = ((Node)stdPic.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }*/
		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		location
		        //
		        /*NodeList locElmntLst = fstElmnt.getElementsByTagName("location");
		        if( locElmntLst != null)
		        {
		        	Element locElmnt = (Element) locElmntLst.item(0);
		        	if(locElmnt !=null)
		        	{
		        		NodeList loc = locElmnt.getChildNodes();
		        		if(loc !=null && loc.item(0) !=null )
		        		{
		        			e.location = ((Node)loc.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }*/


				/////////////////////////////////////////////////////////////////////////////////
		        //
		        //		start_time
		        //
		        /*
		        NodeList stElmntLst = fstElmnt.getElementsByTagName("start_repeat");
		        if( stElmntLst != null)
		        {
		        	Element stElmnt = (Element) stElmntLst.item(0);
		        	if(stElmnt !=null)
		        	{
		        		NodeList st = stElmnt.getChildNodes();
		        		if(st !=null && st.item(0) !=null )
		        		{
		        			String startTime = ((Node)st.item(0)).getNodeValue();
		        			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		        			e.start_time = df.parse(startTime);
		        			//System.out.println("end time : " + e.start_time.toString());
			        	}
		        	}
		        }*/

		        /////////////////////////////////////////////////////////////////////////////////
		        //
		        //		end_time
		        //
		        /*
		        NodeList etElmntLst = fstElmnt.getElementsByTagName("end_repeat");
		        if( etElmntLst != null)
		        {
		        	Element etElmnt = (Element) etElmntLst.item(0);
		        	if(etElmnt !=null)
		        	{
		        		NodeList et = etElmnt.getChildNodes();
		        		if(et !=null && et.item(0) !=null )
		        		{
		        			String endTime = ((Node)et.item(0)).getNodeValue();
		        			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		        			e.end_time = df.parse(endTime);

		        			//System.out.println("end time : " + e.end_time.toString());

			        	}
		        	}
		        }
		        */

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //
		        /*NodeList liElmntLst = fstElmnt.getElementsByTagName("locRefId");
		        if( liElmntLst != null)
		        {
		        	Element liElmnt = (Element) liElmntLst.item(0);
		        	if(liElmnt !=null)
		        	{
		        		NodeList li = liElmnt.getChildNodes();
		        		if(li !=null && li.item(0) !=null )
		        		{
		        			e.locRefId = Integer.parseInt(((Node)li.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }*/

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //

		        /*NodeList cElmntLst = fstElmnt.getElementsByTagName("groupId");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			e.groupId = Integer.parseInt(((Node)c.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }
		        */
		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		extra info
		        //
		        /*
		        NodeList exElmntLst = fstElmnt.getElementsByTagName("extra");
		        if( exElmntLst != null)
		        {
		        	Element exElmnt = (Element) exElmntLst.item(0);
		        	if(exElmnt !=null)
		        	{
		        		NodeList extra = exElmnt.getChildNodes();
		        		if(extra !=null && extra.item(0) !=null )
		        		{
		        			e.extraInfo = ((Node)extra.item(0)).getNodeValue();
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }
		        */

		        result.add(e);
		    }
		 }
		//System.out.println("Information of all employees");
		return result;
	}


	public static ArrayList<EventData>readLocalEventsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));

		ArrayList<EventData > result = new ArrayList<EventData>();
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();

		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());

		NodeList nodeLst = doc.getElementsByTagName("event_item");

		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	//get uid :

		    	EventData e = new EventData();
		    	e.eventSourceType = "database";
		    	Element fstElmnt = (Element) fstNode;
				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		uid
		        //
		        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("event_id");
		        if(fstNmElmntLst !=null)
		        {
			        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
			        if(fstNmElmnt !=null)
			        {
			        	NodeList fstNm = fstNmElmnt.getChildNodes();
			        	if(fstNm!=null)
			        	{
			        		e.uid = ((Node)fstNm.item(0)).getNodeValue();
			        	}
			        }
		        }

				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		summary
		        //
		        NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("Summary");
		        Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		        NodeList lstNm = lstNmElmnt.getChildNodes();
		        e.title = ((Node)lstNm.item(0)).getNodeValue();
		        //System.out.println("name : " + ((Node) lstNm.item(0)).getNodeValue());


		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		event flyer
		        //
		        NodeList stdPicElmntLst = fstElmnt.getElementsByTagName("std_pic");
		        if( stdPicElmntLst != null)
		        {
		        	Element stdPicElmnt = (Element) stdPicElmntLst.item(0);
		        	if(stdPicElmnt !=null)
		        	{
		        		NodeList stdPic = stdPicElmnt.getChildNodes();
		        		if(stdPic !=null && stdPic.item(0) !=null )
		        		{
		        			e.iconUrl = ((Node)stdPic.item(0)).getNodeValue();
		        			
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }
		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		location
		        //
		        NodeList locElmntLst = fstElmnt.getElementsByTagName("location");
		        if( locElmntLst != null)
		        {
		        	Element locElmnt = (Element) locElmntLst.item(0);
		        	if(locElmnt !=null)
		        	{
		        		NodeList loc = locElmnt.getChildNodes();
		        		if(loc !=null && loc.item(0) !=null )
		        		{
		        			e.location = ((Node)loc.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }


				/////////////////////////////////////////////////////////////////////////////////
		        //
		        //		start_time
		        //
		        NodeList stElmntLst = fstElmnt.getElementsByTagName("start_repeat");
		        if( stElmntLst != null)
		        {
		        	Element stElmnt = (Element) stElmntLst.item(0);
		        	if(stElmnt !=null)
		        	{
		        		NodeList st = stElmnt.getChildNodes();
		        		if(st !=null && st.item(0) !=null )
		        		{
		        			String startTime = ((Node)st.item(0)).getNodeValue();
		        			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		        			e.start_time = df.parse(startTime);
		        			//System.out.println("end time : " + e.start_time.toString());
			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////
		        //
		        //		end_time
		        //
		        NodeList etElmntLst = fstElmnt.getElementsByTagName("end_repeat");
		        if( etElmntLst != null)
		        {
		        	Element etElmnt = (Element) etElmntLst.item(0);
		        	if(etElmnt !=null)
		        	{
		        		NodeList et = etElmnt.getChildNodes();
		        		if(et !=null && et.item(0) !=null )
		        		{
		        			String endTime = ((Node)et.item(0)).getNodeValue();
		        			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		        			e.end_time = df.parse(endTime);

		        			//System.out.println("end time : " + e.end_time.toString());

			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //
		        NodeList liElmntLst = fstElmnt.getElementsByTagName("locRefId");
		        if( liElmntLst != null)
		        {
		        	Element liElmnt = (Element) liElmntLst.item(0);
		        	if(liElmnt !=null)
		        	{
		        		NodeList li = liElmnt.getChildNodes();
		        		if(li !=null && li.item(0) !=null )
		        		{
		        			e.locRefId = Integer.parseInt(((Node)li.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //
		        NodeList cElmntLst = fstElmnt.getElementsByTagName("groupId");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			e.groupId = Integer.parseInt(((Node)c.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		extra info
		        //
		        NodeList exElmntLst = fstElmnt.getElementsByTagName("extra");
		        if( exElmntLst != null)
		        {
		        	Element exElmnt = (Element) exElmntLst.item(0);
		        	if(exElmnt !=null)
		        	{
		        		NodeList extra = exElmnt.getChildNodes();
		        		if(extra !=null && extra.item(0) !=null )
		        		{
		        			e.extraInfo = ((Node)extra.item(0)).getNodeValue();
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		creator id
		        //
		        /*NodeList cElmntLst = fstElmnt.getElementsByTagName("host");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			e.creator_id = Integer.parseInt(((Node)c.item(0)).getNodeValue());
			        	}
		        	}
		        }
		        */

		        result.add(e);
		    }
		 }
		//System.out.println("Information of all employees");
		return result;
	}

	public static ArrayList<EventData>readLocalEventsFromFile2(String path,String baseRef) throws ParserConfigurationException, SAXException, IOException, ParseException
	{

	
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
		ArrayList<EventData > result = new ArrayList<EventData>();
		File file = new File(path);
		if(!file.exists())
			return result;

		//return result;


		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		try {
			doc  = db.parse(file);	
		} catch (Exception e) {
				return result;
		}
		
		doc.getDocumentElement().normalize();
		//System.out.println("reading local events" + path);

		NodeList nodeLst = doc.getElementsByTagName("item");

		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	//get uid :

		    	EventData e = new EventData();
		    	e.eventSourceType = "database";
		    	Element fstElmnt = (Element) fstNode;
				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		uid
		        //
		    	e.refId  = -1;
		    	try {
		    		e.refId = Integer.parseInt(fstElmnt.getAttribute("eid"));
				} catch (Exception e2) 
				{
				}
		    	
		    	int pub=1;
		    	try {
		    		pub = Integer.parseInt(fstElmnt.getAttribute("pub"));
				} catch (Exception e2) 
				{
					
				}
		    	e.setPublished(pub==1 ? true : false);
		    	
		    	if(pub==0){
		    		continue;
		    	}
		    			
				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		summary
		        //
		    	/*
		        NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("title");
		        Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		        NodeList lstNm = lstNmElmnt.getChildNodes();
		        */
		        e.title = StringUtils.getXMLFromElement(fstElmnt, "title");
		        //System.out.println("name : " + ((Node) lstNm.item(0)).getNodeValue());

				/////////////////////////////////////////////////////////////////////////////////
		        //
		        //		start_time
		        //
		        NodeList stElmntLst = fstElmnt.getElementsByTagName("sTime");
		        if( stElmntLst != null)
		        {
		        	Element stElmnt = (Element) stElmntLst.item(0);
		        	if(stElmnt !=null)
		        	{
		        		NodeList st = stElmnt.getChildNodes();
		        		if(st !=null && st.item(0) !=null )
		        		{
		        			String startTime = ((Node)st.item(0)).getNodeValue();
		        			if(startTime==null){
		        				continue;
		        			}
		        			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		        			
		        			try {
		        				e.start_time = df.parse(startTime);
			        			e.sStartRepition = startTime;	
							} catch (Exception e2) {
								e2.printStackTrace();
							}
		        			
		        			
		        			

		        			if(startTime.length() > 20)
		        			{
		        			//	System.out.println("start time : " + startTime);
		        			}

		        			//System.out.println("start time : " + e.start_time.toString());
			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////
		        //
		        //		end_time
		        //
		        NodeList etElmntLst = fstElmnt.getElementsByTagName("eTime");
		        if( etElmntLst != null)
		        {
		        	Element etElmnt = (Element) etElmntLst.item(0);
		        	if(etElmnt !=null)
		        	{
		        		NodeList et = etElmnt.getChildNodes();
		        		if(et !=null && et.item(0) !=null )
		        		{
		        			String endTime = ((Node)et.item(0)).getNodeValue();
		        			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		        			//String date = new java.text.SimpleDateFormat("yyyy-MM-ddTHH:mm:ss").format(new java.util.Date (endTime*1000));
		        			try {
			        			e.end_time = df.parse(endTime);
			        			e.sEndRepition = endTime;
							} catch (Exception e2) {
								e2.printStackTrace();
							}
		        	//		System.out.println("end time : " + e.end_time.toString());

			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //
		        NodeList liElmntLst = fstElmnt.getElementsByTagName("lID");
		        if( liElmntLst != null)
		        {
		        	Element liElmnt = (Element) liElmntLst.item(0);
		        	if(liElmnt !=null)
		        	{
		        		NodeList li = liElmnt.getChildNodes();
		        		if(li !=null && li.item(0) !=null )
		        		{
		        			int _locRefId=-1;
		        			String _locRefIdStr=null;
		        			try {
		        				_locRefId=Integer.parseInt(((Node)li.item(0)).getNodeValue());
							} catch (Exception e2) {
								_locRefIdStr = StringUtils.getXMLFromElement(fstElmnt, "lID");
							}
		        			
		        			if(_locRefId!=-1){
		        				e.locRefId=_locRefId;
		        			}else if(_locRefIdStr!=null){
		        				e.locRefStr=_locRefIdStr;
		        			}
		        			//e.locRefId = Integer.parseInt(((Node)li.item(0)).getNodeValue());
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		locRefId
		        //
		        NodeList cElmntLst = fstElmnt.getElementsByTagName("cID");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			e.groupId = Integer.parseInt(((Node)c.item(0)).getNodeValue());
		            	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		extra info
		        //
		        NodeList exElmntLst = fstElmnt.getElementsByTagName("extra");
		        if( exElmntLst != null)
		        {
		        	Element exElmnt = (Element) exElmntLst.item(0);
		        	if(exElmnt !=null)
		        	{
		        		NodeList extra = exElmnt.getChildNodes();
		        		if(extra !=null && extra.item(0) !=null )
		        		{
		        			e.extraInfo = ((Node)extra.item(0)).getNodeValue();
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }

		        NodeList detailElmntLst = fstElmnt.getElementsByTagName("detail");
		        if( detailElmntLst != null)
		        {
		        	Element detailElement = (Element) detailElmntLst.item(0);
		        	if(detailElement !=null)
		        	{
		        		NodeList detail = detailElement.getChildNodes();
		        		if(detail !=null && detail.item(0) !=null )
		        		{
		        			e.description = ((Node)detail.item(0)).getNodeValue();
			        	}
		        	}
		        }
		        
		        String stdPic = StringUtils.getXMLFromElement(fstElmnt, "pic");
		        if(stdPic!=null && stdPic.length() > 0)
		        {
		        	String prefix="";
		        	if(!stdPic.contains("/images/stories/jevents/"))
		        	{
		        		prefix = "/images/stories/jevents/";
		        	}
		        	e.iconUrl=prefix+stdPic;
		        	
		        	
		        	if(e.iconUrl!=null)
					{
						if(baseRef!=null){
							prefix=baseRef;
						}
						String iconUrl = baseRef +  e.iconUrl;
						e.setPicture(iconUrl);
						e.setPictureItems(new ArrayList<MediaItemBase>());
						MediaItemBase picItem = new MediaItemBase();
						picItem.setType(MediaType.MT_PICTURE);
						picItem.setFullSizeLocation(iconUrl);
						e.getPictureItems().add(picItem);
					}
		        	
		        	
		        }
		        
		        
		        result.add(e);
		    }
		 }

		 return result;
	}

	public static ArrayList<EventData>readFBEventsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{

		TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
		//ISO-8859-1
		ArrayList<EventData > result = new ArrayList<EventData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		//Document doc = db.parse(file);
		Document doc = db.parse(new InputSource(new InputStreamReader(new FileInputStream(file), "UTF-8")));
		doc.getDocumentElement().normalize();

		//System.out.println("Root element " + doc.getDocumentElement().getNodeName());

		NodeList nodeLst = doc.getElementsByTagName("event");

		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	//get uid :

		    	EventData e = new EventData();
		    	e.eventSourceType = "Facebook";

		    	Element fstElmnt = (Element) fstNode;


				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		uid
		        //
		        NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("eid");
		        Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		        NodeList fstNm = fstNmElmnt.getChildNodes();
		        //System.out.println("eid : "  + ((Node) fstNm.item(0)).getNodeValue());
		        e.uid = ((Node)fstNm.item(0)).getNodeValue();

				/////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		summary
		        //
		        NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("name");
		        Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		        NodeList lstNm = lstNmElmnt.getChildNodes();
		        e.title = ((Node)lstNm.item(0)).getNodeValue();


		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		event flyer
		        //

		        ///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		event flyer
		        //
		        NodeList stdPicElmntLst = fstElmnt.getElementsByTagName("pic_big");
		        if( stdPicElmntLst != null)
		        {
		        	Element stdPicElmnt = (Element) stdPicElmntLst.item(0);
		        	if(stdPicElmnt !=null)
		        	{
		        		NodeList stdPic = stdPicElmnt.getChildNodes();
		        		if(stdPic !=null && stdPic.item(0) !=null )
		        		{
		        			e.iconUrl = ((Node)stdPic.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.stdPictureLink);
			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		location
		        //
		        NodeList locElmntLst = fstElmnt.getElementsByTagName("location");
		        if( locElmntLst != null)
		        {
		        	Element locElmnt = (Element) locElmntLst.item(0);
		        	if(locElmnt !=null)
		        	{
		        		NodeList loc = locElmnt.getChildNodes();
		        		if(loc !=null && loc.item(0) !=null )
		        		{
		        			e.location = ((Node)loc.item(0)).getNodeValue();
		        			//System.out.println("std pic : " + e.location);
			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		extra venue fields
		        //
		        NodeList venueElmntLst = fstElmnt.getElementsByTagName("venue");
		        if( venueElmntLst != null)
		        {
		        	Element venueElmnt = (Element) venueElmntLst.item(0);
		        	if(venueElmnt !=null)
		        	{

		        		//////////////////////////////////////////////////////////////////////
		        		//
		        		//street :
		        		NodeList venueStreetElmntLst = venueElmnt.getElementsByTagName("street");
		        		Element streetElmnt = (Element) venueStreetElmntLst.item(0);
			        	if(streetElmnt !=null)
			        	{
			        		NodeList street = streetElmnt.getChildNodes();
			        		if(street!=null && street.item(0) !=null )
			        		{
			        			e.fbVenueStreet = ((Node)street.item(0)).getNodeValue();
			        		}
			        	}


			        	//////////////////////////////////////////////////////////////////////
		        		//
		        		//city :
		        		NodeList venueCityElmntLst = venueElmnt.getElementsByTagName("street");
		        		Element cityElmnt = (Element) venueCityElmntLst.item(0);
			        	if(cityElmnt !=null)
			        	{
			        		NodeList city = cityElmnt.getChildNodes();
			        		if(city!=null && city.item(0) !=null )
			        		{
			        			e.fbVenueCity = ((Node)city.item(0)).getNodeValue();
			        		}
			        	}

			        	//////////////////////////////////////////////////////////////////////
		        		//
		        		//Country :
		        		NodeList venueCountryElmntLst = venueElmnt.getElementsByTagName("street");
		        		Element countryElmnt = (Element) venueCountryElmntLst.item(0);
			        	if(countryElmnt !=null)
			        	{
			        		NodeList country= countryElmnt.getChildNodes();
			        		if(country!=null && country.item(0) !=null )
			        		{
			        			e.fbVenueCountry = ((Node)country.item(0)).getNodeValue();
			        		}
			        	}

			        	//////////////////////////////////////////////////////////////////////
		        		//
		        		//State :
		        		NodeList venueStateElmntLst = venueElmnt.getElementsByTagName("street");
		        		Element stateElmnt = (Element) venueStateElmntLst.item(0);
			        	if(stateElmnt !=null)
			        	{
			        		NodeList state= stateElmnt.getChildNodes();
			        		if(state!=null && state.item(0) !=null )
			        		{
			        			e.fbVenueState= ((Node)state.item(0)).getNodeValue();
			        		}
			        	}

		        	}
		        }


				///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		host
		        //
		        NodeList hostElmntLst = fstElmnt.getElementsByTagName("host");
		        if( hostElmntLst != null)
		        {
		        	Element hostElmnt = (Element) hostElmntLst.item(0);
		        	if(hostElmnt !=null)
		        	{
		        		NodeList host = hostElmnt.getChildNodes();
		        		if(host !=null && host.item(0) !=null )
		        		{
		        			e.host = ((Node)host.item(0)).getNodeValue();
		        			//System.out.println("host  : " + e.host);
			        	}
		        	}
		        }

				/////////////////////////////////////////////////////////////////////////////////
		        //
		        //		start_time
		        //
		        NodeList stElmntLst = fstElmnt.getElementsByTagName("start_time");
		        if( stElmntLst != null)
		        {
		        	Element stElmnt = (Element) stElmntLst.item(0);
		        	if(stElmnt !=null)
		        	{
		        		NodeList st = stElmnt.getChildNodes();
		        		if(st !=null && st.item(0) !=null )
		        		{
		        			long startTime = Long.parseLong(((Node)st.item(0)).getNodeValue());
		        			DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		        			String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (startTime*1000));
		        			e.start_time = df.parse(date);
		        			e.start_time_e  = startTime;


		        			java.sql.Date sTime = new java.sql.Date(e.start_time_e *1000);
		        			Calendar startDate = Calendar.getInstance();
		        			startDate.setTime(sTime);
		        			startDate.add(Calendar.HOUR, -8);

		        			e.start_time = startDate.getTime();
		        			e.start_time_e = startDate.getTimeInMillis();
		        			//e.start_time_e_s = startDate.getTimeInMillis();


		        			//String sTimeC = event.start_time.toString();


		        			if(e.title.contains("MOONLIGHT"))
		        			{
				    			//System.out.println("stop");
				    		}
		        			Date now = new Date();
		        			now.setHours(0);
		        			now.setMinutes(0);


		        			Date other = (Date) e.start_time.clone();
		        			other.setHours(0);
		        			other.setMinutes(0);
		        			other.setYear(now.getYear());
		        			long ddiff = TimeUtils.daysBetween(now, other);
				    		if(ddiff < 0)
				    		{
				    			//return e;
				    			//System.out.println("event from before : " + e.summary + "for date " + e.start_time.toString() );
				    			//continue;
				    		}
		        			//System.out.println("end time : " + e.end_time.toString() + " for e: " + e.summary);

			        	}
		        	}
		        }

		        /////////////////////////////////////////////////////////////////////////////////
		        //
		        //		end_time
		        //
		        NodeList etElmntLst = fstElmnt.getElementsByTagName("end_time");
		        if( etElmntLst != null)
		        {
		        	Element etElmnt = (Element) etElmntLst.item(0);
		        	if(etElmnt !=null)
		        	{
		        		NodeList et = etElmnt.getChildNodes();
		        		if(et !=null && et.item(0) !=null )
		        		{
		        			long endTime = Long.parseLong(((Node)et.item(0)).getNodeValue());
		        			DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		        			String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (endTime*1000));
		        			e.end_time = df.parse(date);
		        			e.end_time_e = endTime;

		        			java.sql.Date eTime = new java.sql.Date(e.end_time_e *1000);
		        			Calendar endDate = Calendar.getInstance();
		        			endDate.setTime(eTime);
		        			endDate.add(Calendar.HOUR, -8);

		        			e.end_time = endDate.getTime();
		        			e.end_time_e = endDate.getTimeInMillis();


		        			if(e.title.contains("MOONLIGHT"))
		        			{
				    			//System.out.println("stop");
				    		}
		        			Date now = new Date();
		        			now.setHours(0);
		        			now.setMinutes(0);

		        			Date other = (Date) e.end_time.clone();
		        			other.setHours(0);
		        			other.setMinutes(0);
		        			other.setYear(now.getYear());
		        			long ddiff = TimeUtils.daysBetween(now, other);
				    		if(ddiff < 0)
				    		{
				    			//return e;
				    			//System.out.println("event from before : " + e.summary + "for edate " + e.end_time.toString() );
				    			continue;
				    		}



			        	}
		        	}
		        }

		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		description
		        //
		        NodeList dElmntLst = fstElmnt.getElementsByTagName("description");
		        if( dElmntLst != null)
		        {
		        	Element dElmnt = (Element) dElmntLst.item(0);
		        	if(dElmnt !=null)
		        	{
		        		NodeList d = dElmnt.getChildNodes();
		        		if(d !=null && d.item(0) !=null )
		        		{
		        			e.description = ((Node)d.item(0)).getNodeValue();
		        			e.description = EventFilterTools.stripNonValidXMLCharacters(e.description);
		        			//System.out.println("description  : " + e.description);
			        	}
		        	}
		        }


		        ///////////////////////////////////////////////////////////////////////////////////
		        //
		        //		creator id
		        //
		        NodeList cElmntLst = fstElmnt.getElementsByTagName("creator");
		        if( cElmntLst != null)
		        {
		        	Element cElmnt = (Element) cElmntLst.item(0);
		        	if(cElmnt !=null)
		        	{
		        		NodeList c = cElmnt.getChildNodes();
		        		if(c !=null && c.item(0) !=null )
		        		{
		        			e.creator_id = ((Node)c.item(0)).getNodeValue();
			        	}
		        	}
		        }
		        result.add(e);
		    }
		 }
		//System.out.println("Information of all employees");
		return result;
	}

	public static boolean isValidUser(ArrayList<String>blist,String test)
	{
		if (blist ==null)
			return true;

		for(int i =0 ; i < blist.size() ; i++)
		{
			String userid = blist.get(i);
			if(userid !=null && userid.length() > 0 && test !=null && test.length() > 0 && userid.contentEquals(test))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isValidEvent(ArrayList<String>blist,String test)
	{
		for(int i =0 ; i < blist.size() ; i++)
		{
			String eventid = blist.get(i);
			if(eventid !=null && eventid.length() > 0 && eventid.contentEquals(test))
			{
				return false;
			}
		}
		return true;
	}

	public class CategorySum
	{
		public Integer id;
		public int count;
	}
}

