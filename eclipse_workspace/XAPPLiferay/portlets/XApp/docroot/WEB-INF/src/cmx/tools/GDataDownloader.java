package cmx.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import pmedia.types.CContent;
import pmedia.types.CList;
import pmedia.types.CListItem;
import pmedia.types.CVideoItem;
import pmedia.utils.ECMContentSourceTypeTools;
import pmedia.utils.StringUtils;
import cmx.data.CContentList;
import cmx.types.DBConnectionError;
import cmx.types.ECMContentSourceType;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.docs.MetadataEntry;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import flexjson.JSONSerializer;


	public class GDataDownloader {

		
		public static final String SPREADSHEETS_SERVICE_NAME = "wise";
		  public static final String SPREADSHEETS_HOST = "spreadsheets.google.com";

		  private final static String URL_FEED = "/feeds";
		  public final String URL_DOWNLOAD = "/download";
		  private final String URL_DOCLIST_FEED = "/private/full";

		  private final String URL_DEFAULT = "/default";
		  private final String URL_FOLDERS = "/contents";
		  private final String URL_ACL = "/acl";
		  private final String URL_REVISIONS = "/revisions";

		  private final String URL_CATEGORY_DOCUMENT = "/-/document";
		  private final String URL_CATEGORY_SPREADSHEET = "/-/spreadsheet";
		  private final String URL_CATEGORY_PDF = "/-/pdf";
		  private final String URL_CATEGORY_PRESENTATION = "/-/presentation";
		  private final String URL_CATEGORY_STARRED = "/-/starred";
		  private final String URL_CATEGORY_TRASHED = "/-/trashed";
		  private final String URL_CATEGORY_FOLDER = "/-/folder";
		  private final String URL_CATEGORY_EXPORT = "/Export";

		  private final String PARAMETER_SHOW_FOLDERS = "showfolders=true";

		  private static final String APPLICATION_NAME = "JavaGDataClientSampleAppV3.0";
		  
		  private static String host="docs.google.com";

		  private final Map<String, String> DOWNLOAD_DOCUMENT_FORMATS;
		  {
		    DOWNLOAD_DOCUMENT_FORMATS = new HashMap<String, String>();
		    DOWNLOAD_DOCUMENT_FORMATS.put("doc", "doc");
		    DOWNLOAD_DOCUMENT_FORMATS.put("txt", "txt");
		    DOWNLOAD_DOCUMENT_FORMATS.put("odt", "odt");
		    DOWNLOAD_DOCUMENT_FORMATS.put("pdf", "pdf");
		    DOWNLOAD_DOCUMENT_FORMATS.put("png", "png");
		    DOWNLOAD_DOCUMENT_FORMATS.put("rtf", "rtf");
		    DOWNLOAD_DOCUMENT_FORMATS.put("html", "html");
		    DOWNLOAD_DOCUMENT_FORMATS.put("zip", "zip");
		  }

		  private final Map<String, String> DOWNLOAD_PRESENTATION_FORMATS;
		  {
		    DOWNLOAD_PRESENTATION_FORMATS = new HashMap<String, String>();
		    DOWNLOAD_PRESENTATION_FORMATS.put("pdf", "pdf");
		    DOWNLOAD_PRESENTATION_FORMATS.put("png", "png");
		    DOWNLOAD_PRESENTATION_FORMATS.put("ppt", "ppt");
		    DOWNLOAD_PRESENTATION_FORMATS.put("swf", "swf");
		    DOWNLOAD_PRESENTATION_FORMATS.put("txt", "txt");
		  }

		  private final Map<String, String> DOWNLOAD_SPREADSHEET_FORMATS;
		  {
		    DOWNLOAD_SPREADSHEET_FORMATS = new HashMap<String, String>();
		    DOWNLOAD_SPREADSHEET_FORMATS.put("xls", "xls");
		    DOWNLOAD_SPREADSHEET_FORMATS.put("ods", "ods");
		    DOWNLOAD_SPREADSHEET_FORMATS.put("pdf", "pdf");
		    DOWNLOAD_SPREADSHEET_FORMATS.put("csv", "csv");
		    DOWNLOAD_SPREADSHEET_FORMATS.put("tsv", "tsv");
		    DOWNLOAD_SPREADSHEET_FORMATS.put("html", "html");
		  }
		
		public static void toXML(CList list,String filePath)
		{
			String path = StringUtils.path(filePath);
			File pathO = new File(path);
			if(!pathO.exists()){
				pathO.mkdirs();
			}
			
			XStream xstream = new XStream(new StaxDriver());
			String xmlPages = xstream.toXML(list);
			if(xmlPages!=null)
			{
				try {
					StringUtils.writeToFile(xmlPages, filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void toXML(ArrayList<CListItem>list,String filePath)
		{
			String path = StringUtils.path(filePath);
			File pathO = new File(path);
			if(!pathO.exists()){
				pathO.mkdirs();
			}
			
			XStream xstream = new XStream(new StaxDriver());
			String xmlPages = xstream.toXML(list);
			if(xmlPages!=null)
			{
				try {
					StringUtils.writeToFile(xmlPages, filePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		   * Retrieves the albums for the given user.
		   */
		public static List<AlbumEntry> getAlbums(UserFeed userFeed) throws IOException,ServiceException 
		{
		    List<GphotoEntry> entries = userFeed.getEntries();
		    List<AlbumEntry> albums = new ArrayList<AlbumEntry>();
		    for (GphotoEntry entry : entries) 
		    {
			      GphotoEntry adapted = entry.getAdaptedEntry();
			      if (adapted instanceof AlbumEntry) 
			      {
			        albums.add((AlbumEntry) adapted);
			      }
		    }
		    return albums;
		}
		
		public static List<PhotoEntry> getAlbums(AlbumFeed userFeed) throws IOException,ServiceException 
		{
		    List<GphotoEntry> entries = userFeed.getEntries();
		    List<AlbumEntry> albums = new ArrayList<AlbumEntry>();
		    for (GphotoEntry entry : entries) 
		    {
			      GphotoEntry adapted = entry.getAdaptedEntry();
			      if (adapted instanceof AlbumEntry) 
			      {
			        albums.add((AlbumEntry) adapted);
			      }
		    }
		    return null;
		}
		
		
		public static void printDocumentEntry(DocumentListEntry doc) 
		{
		    StringBuffer output = new StringBuffer();
		    output.append(" -- " + doc.getTitle().getPlainText() + " ");
		    if (!doc.getParentLinks().isEmpty()) 
		    {
		      for (Link link : doc.getParentLinks()) 
		      {
		        output.append("[" + link.getTitle() + "] ");
		      }
		    }
		    output.append(doc.getResourceId());
		    System.out.println(output);
		  }
		  
		/***
		 * 
		 * @param path
		 * @param uuid
		 * @param applicationIdentifier
		 * @param service
		 * @param channel
		 * @param user
		 * @param pswd
		 * @param dsUID
		 * @return
		 */
		
		public static boolean hasItem(ArrayList<CContent>items,String refIdStr)
		{
			for (int i = 0; i < items.size(); i++) 
			{
				CContent c = items.get(i);
				if(c.refIdStr.equals(refIdStr)){
					return true;
				}
			}
			return false;
		}
		
		public static DBConnectionError downloadGDataDocumentAsPDF(
				String path,
				String uuid,
	    		String applicationIdentifier,
				String service,
				String resourceId,
				String user, 
				String pswd,
				String dsUID) 
		{
			DBConnectionError f = new DBConnectionError();
			
			return f;
			
		}
		public static DBConnectionError downloadGDataDocumentFolder(
				String path,
				String uuid,
	    		String applicationIdentifier,
				String service,
				String channel,
				String user, 
				String pswd,
				String dsUID) 
		{
			String DEFAULT_HOST = "docs.google.com";
			
			GDocumentList documentList =null;
			
			try {
				documentList =new GDocumentList("any",DEFAULT_HOST);
			} catch (GDocumentListException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			
			
			DBConnectionError res = new DBConnectionError();
			
			if(!user.contains("@gmail.com"))
			{
				user+="@gmail.com";
			}
			
			if (user !=null &&user.length()>0 && pswd!= null && pswd.length()> 0) 
			{
			      try {
			    	  documentList.login(user, pswd);
			      } catch (AuthenticationException e) 
			      {
			    	    e.printStackTrace();
			    	  	res.type=-1;
						res.msg="failed";
						res.msgInternal = "Illegal username/password combination.";
						return res;
			      } catch (GDocumentListException e) 
			      {
					// TODO Auto-generated catch block
					e.printStackTrace();
		    	  	res.type=-1;
					res.msg="failed";
					res.msgInternal = "Illegal username/password combination.";
					return res;
				}
			}
			
			DocumentListFeed feed = null;

			try {
				feed = documentList.getDocsListFeed("all");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GDocumentListException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("saving doc feeds to : ");
			CContentList folderList = new CContentList();
			folderList.setItems(new ArrayList<CContent>());
			
			if (feed != null) 
			 {
			      for (DocumentListEntry entry : feed.getEntries()) 
			      {
			    	  //debug
			    	  printDocumentEntry(entry);
			    	  if (!entry.getParentLinks().isEmpty()) 
					  {
					      for (Link link : entry.getParentLinks()) 
					      {
					    	  //output.append("[" + link.getTitle() + "] ");
					    	  //list.setTitle(channel);
					    	  if(!hasItem(folderList.getItems(), Base64.encodeBase64String(link.getHref().getBytes())))
					    	  {
						    	  CContent folder = new CContent();
						    	  folder.setTitle(link.getTitle());
						    	  folder.setRefIdStr(Base64.encodeBase64String(link.getHref().getBytes()));
						    	  folder.setPublished(true);
						    	  folder.setDataSourceUID(dsUID);
						    	  folder.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder));
						    	  folderList.getItems().add(folder);
					    	  }
					      }
					  }
			      }
			 }
			
			if(folderList.getItems().size()>0)
			{
				String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentFolder) + "_.json";
				JSONSerializer serializer = new JSONSerializer();
				String serialized = serializer.deepSerialize(folderList);
				serializer.exclude("items.asListItem");
				try 
				{
					StringUtils.writeToFile(serialized, path + dstPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			/***
			 * Now we write out the document links
			 */
			CContentList docList = new CContentList();
			docList.setItems(new ArrayList<CContent>());
			if (feed != null) 
			{
			      for (DocumentListEntry _entry : feed.getEntries()) 
			      {
			    	  //debug
			    	  //printDocumentEntry(entry);
			    	  CContent item = new CContent();
			    	  item.setTitle(_entry.getTitle().getPlainText());
			    	  
			    	  List _links = _entry.getLinks();
			    	  Link _dlink = null;
			    	  if(_links!=null)
			    	  {
							_dlink=(Link) _links.get(0);
							item.setRefIdStr(Base64.encodeBase64String(_dlink.getHref().getBytes()));	
			    	  }
			    	  item.setRefIdStr(Base64.encodeBase64String(_entry.getResourceId().getBytes()));
			    	  System.out.println(" refidstr : " + item.getRefIdStr());
			    	  item.setPublished(true);
			    	  item.setSourceType(""+ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentItem));
			    	  item.setDataSourceUID(dsUID);
			    	  
			    	  item.setOwnerRefStr(_entry.getLastModifiedBy().getName());
			    	  //item.setCreated(new Date(_entry.getUpdated().getValue()));
			    	  item.setCreationDate(new Date(_entry.getUpdated().getValue()));
			    	  
			    	  //item.setCreated(_entry.getLastModifiedBy());
			    	  if (!_entry.getParentLinks().isEmpty()) 
					  {
			    		  
					      for (Link glink : _entry.getParentLinks()) 
					      {
					    	  item.setGroupStr(Base64.encodeBase64String(glink.getHref().getBytes()));
					    	  
					    	  break;
					      }
					  }
			    	  
			    	  /***
			    	   * build file path : 
			    	   */
			    	  String URL_DOWNLOAD = "/download";
			    	  String URL_CATEGORY_EXPORT = "/Export";
			    	  String[] parameters = {"docID=" + _entry.getDocId(), "exportFormat=" + "pdf"};
			    	  
			    	  String docType = null;//entry.getMediaSource().getContentType();

			    	  
			    	  try {
						docType = documentList.getResourceIdPrefix(_entry.getResourceId());
			    	  } catch (GDocumentListException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
			    	  }
			    	  
			    	  URL furl = null;
			    	  
			    	  if(docType !=null){
			    		  
			    		  item.setLocRefStr(docType);
			    		  
			    		  
				    	  if (docType.equals("spreadsheet")) 
				    	  {
				    			try {
				    				furl =  buildUrl(SPREADSHEETS_HOST, URL_DOWNLOAD + "/spreadsheets"
				    				        + URL_CATEGORY_EXPORT, parameters);
				    				
				    				
									//furl = GDataDownloader.buildUrl(SPREADSHEETS_HOST + "/spreadsheets" + URL_CATEGORY_EXPORT,
									///        parameters);
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (GDocumentListException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	  }else if (docType.equals("presentation"))
				    	  {
				    		  try {
									furl = GDataDownloader.buildUrl(URL_DOWNLOAD + "/presentations" + URL_CATEGORY_EXPORT,
									        parameters);
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (GDocumentListException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	  }else if (docType.equals("document"))
				    	  {
				    		
				    		  try {
									furl = GDataDownloader.buildUrl(URL_DOWNLOAD + "/documents" + URL_CATEGORY_EXPORT,
									        parameters);
									
									} catch (MalformedURLException e) 
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (GDocumentListException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				    	 
				    	  }if (docType.equals("file"))
				    	  {
				    		  	continue;
				    	        
				    		  	/*MediaContent mc = null;
								try {
									mc = (MediaContent) documentList.getDocsListEntry(_entry.getDocId()).getContent();
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ServiceException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (GDocumentListException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	        String fileExtension = mc.getMimeType().getSubType();
				    	        URL exportUrl=null;
				    	        try {
									exportUrl = new URL(mc.getUri());
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	        System.out.println("file : "  + exportUrl +  " ext : " + fileExtension );
				    	        */
				    	        // PDF file cannot be exported in different formats.

				    	        /*String requestedFormat = args[2]
				    	            .substring(args[2].lastIndexOf(".") + 1);
				    	        if (!requestedFormat.equals(fileExtension)) {

				    	          String[] formatWarning = {"Warning: "
				    	              + mc.getMimeType().getMediaType() + " cannot be downloaded as a "
				    	              + requestedFormat + ". Using ." + fileExtension + " instead."};
				    	          printMessage(formatWarning);
				    	        }
				    	        */
				    	  }
			    	  }
			    	  
			    	  if(furl!=null)
			    	  {
			    		  item.setFileRef(furl.toString());
			    		  //System.out.println(furl.toString());
			    	  }

			    	  docList.getItems().add(item);
			    	  
			    	    //downloadFile(url, filepath);
			      }
			      
			      String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleDocumentItem) + "_.json";
			      
			      JSONSerializer serializer = new JSONSerializer();
			      String serialized = serializer.deepSerialize(docList);
			      serializer.exclude("items.asListItem");
					
			      try 
			      {
					
			    	  StringUtils.writeToFile(serialized, path + dstPath);
					
			      } catch (IOException e) {
						e.printStackTrace();
					
			      }
			      
			 }
			
			
			 

			
			return res;
		}
		
		
		public static DBConnectionError downloadGDataCalendar(
				String path,
				String uuid,
	    		String applicationIdentifier,
				String service,
				String channel,
				String user, 
				String pswd,
				String dsUID) 
		{
			DBConnectionError res = new DBConnectionError();
			
			if(!user.contains("@gmail.com"))
			{
				user+="@gmail.com";
			}
			
			CalendarService myService = new CalendarService(service+channel);
			if (user !=null &&user.length()>0 && pswd!= null && pswd.length()> 0) 
			{
			      try {
			        myService.setUserCredentials(user, pswd);
			      } catch (AuthenticationException e) 
			      {
			    	  
			    	   e.printStackTrace();
			    	  	res.type=-1;
						res.msg="failed";
						res.msgInternal = "Illegal username/password combination.";
						return res;
			      }
			
			}
	        URL metafeedUrl = null;
	        
	        try {
	        	String userFeedName=new String(channel);
        		if(!userFeedName.contains("@gmail.com"))
        		{
        			userFeedName+="@gmail.com";
        		}
        		metafeedUrl = new URL("https://www.google.com/calendar/feeds/"+userFeedName+"/allcalendars/full");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			}
			
			CalendarFeed userFeed = null;
	        
	        try {
	        	userFeed = myService.getFeed(metafeedUrl, CalendarFeed.class);
			} catch (IOException e) {
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			} catch (ServiceException e) {

				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				
				e.printStackTrace();
				return res;
			}
			String inputFile=new String(path);
			//inputFile+=service+"/" +channel;

			String albumsFile =inputFile + "/calendars.xml";
			System.out.println("saving calendar feeds to : " + albumsFile);
			List<CalendarEntry>entries = null;
			entries = userFeed.getEntries();
			
			ArrayList<CContent>eventsAll  = new ArrayList<CContent>();
			
			
			/***
			 * 
			 */
			if(entries!=null && entries.size()>0){
				
				CContentList list = new CContentList();
				//list.setTitle(channel);
				
				ArrayList<CContent>items = CListItemToolsGoogle.calendarsToListItems(entries,"ndata");
				
				list.setItems(items);
				String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendar) + "_.json";
				JSONSerializer serializer = new JSONSerializer();
				String serialized = serializer.deepSerialize(list);
				serializer.exclude("items.asListItem");
				try 
				{
					StringUtils.writeToFile(serialized, path + dstPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//GDataDownloader.toXML(list, albumsFile);
			}
			
			
			for (int i = 0; i < userFeed.getEntries().size(); i++) 
			{
				CalendarEntry entry = userFeed.getEntries().get(i);
				if(entry!=null)
				{
					String METAFEED_URL_BASE = "https://www.google.com/calendar/feeds/";
					String EVENT_FEED_URL_SUFFIX = "/private/full";
					URL feedUrl =null;
					try {
						feedUrl = new URL(METAFEED_URL_BASE + user + EVENT_FEED_URL_SUFFIX);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				    Link a =entry.getLink("", "");
					String idcal  = entry.getId();
					
					List links = entry.getLinks();
					if(links!=null){
						
						Link link=(Link) links.get(0);
						
						if(link!=null)
						{
							try {
								feedUrl=new URL(link.getHref());
							} catch (MalformedURLException e) 
							{
								e.printStackTrace();
							}
						}
					}else{
						continue;
					}
					CalendarEventFeed resultFeed=null;
					//String _feedUrl = feedUrl.toString().replace("/private/full", "/public/full");
					String _feedUrl = feedUrl.toString();
					try {
						feedUrl=new URL(_feedUrl);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					CalendarQuery query = new CalendarQuery(feedUrl);
					
					String fields = "entry(@gd:etag,id,title,content,summary,*)";
					
					//String fields = "entry(@gd:*)";
					//query.setFields(fields);
					query.setStringCustomParameter("singleevents", "true");
					query.setStringCustomParameter("orderby", "starttime");
					query.setStringCustomParameter("sortorder","ascending");
					//System.out.println("idcal  : " + feedUrl.toString());
					//System.out.println(" query : " + query.getFeedUrl().toString());
					try {
						
						resultFeed = myService.query(query,CalendarEventFeed.class);
					} catch (MalformedURLException e) {
						e.printStackTrace();
						continue;
					} catch (IOException e) {
						
						e.printStackTrace();
						continue;
					} catch (ServiceException e) {
						
						e.printStackTrace();
						continue;
					}
				    //System.out.println("All events on your calendar:" + entry.getTitle().getPlainText());
				    ArrayList<CContent>events = CListItemToolsGoogle.calendarEntriesToListItems(resultFeed.getEntries(),"",Base64.encodeBase64URLSafeString(_feedUrl.getBytes()),dsUID);
				    if(events!=null)
				    {
				    	eventsAll.addAll(events);
				    }
				}
			}
			
			
			CContentList result = new CContentList();
			result.setItems(eventsAll);
			String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GoogleCalendarItem) + "_.json";
			JSONSerializer serializer = new JSONSerializer();
			String serialized = serializer.deepSerialize(result);
			serializer.exclude("items.asListItem");
			try 
			{
				StringUtils.writeToFile(serialized, path + dstPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return res;
		}
		
		public static DBConnectionError downloadGDataPicassa(
				String path,
				String uuid,
	    		String applicationIdentifier,
				String service,
				String channel,
				String user, 
				String pswd) 
		{
			DBConnectionError res = new DBConnectionError();
			
			PicasawebService myService = new PicasawebService(service+channel);
			
			if (user !=null &&user.length()>0 && pswd!= null && pswd.length()> 0) 
			{
			      try {
			        myService.setUserCredentials(user, pswd);
			      } catch (AuthenticationException e) 
			      {
			    	  
			    	   e.printStackTrace();
			    	  	res.type=-1;
						res.msg="failed";
						res.msgInternal = "Illegal username/password combination.";
						return res;
			      }
			
			}
			
	        URL metafeedUrl = null;
	        
	        try {
	        	metafeedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/"+channel);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			}
			UserFeed userFeed = null;
	        
	        try {
	        	userFeed = myService.getFeed(metafeedUrl, UserFeed.class);
			} catch (IOException e) {
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;
			} catch (ServiceException e) {

				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				
				e.printStackTrace();
				return res;
			}
			String inputFile=new String(path);
			//inputFile+=service+"/" +channel;

			String albumsFile =inputFile + "/albums.xml";
			String photosFile =inputFile + "/photos.xml";
			
			System.out.println("saving picassa feed to : " + albumsFile);
			List<AlbumEntry>albums = null;
			ArrayList<CListItem>photosAll= new ArrayList<CListItem>();
			
			
			try {
				albums = GDataDownloader.getAlbums(userFeed);
			} catch (IOException e) {
				e.printStackTrace();
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				return res;

			} catch (ServiceException e) {
				res.type=-1;
				res.msg="failed";
				res.msgInternal = e.getLocalizedMessage();
				e.printStackTrace();
				return res;
			}
			
			/***
			 * 
			 */
			if(albums!=null && albums.size()>0){
				/***
				 * Save out albums first
				 */
				String BASE_URL = "http://picasaweb.google.com/data/feed/api/user/";

				CList albumList = new CList();
				albumList.setTitle(channel);
				
				ArrayList<CListItem>items = CListItemToolsGoogle.albumEntriesToListItems(albums,"ndata");
				albumList.setItems(items);
				GDataDownloader.toXML(albumList, albumsFile);
				for(int i = 0 ; i < albums.size() ;  i ++)
				{
					AlbumEntry album = albums.get(i);
					URL albumFeedUrl = null ;
					
					try {
						albumFeedUrl = new URL(BASE_URL + "default/albumid/" + album.getGphotoId());
					} catch (MalformedURLException e) 
					{
						e.printStackTrace();
						albumFeedUrl = null;
					}
					
					AlbumFeed albumFeed=null;
					try {
						albumFeed = myService.getFeed(albumFeedUrl,AlbumFeed.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
					if(albumFeedUrl!=null){
						
						List<PhotoEntry>photos = albumFeed.getPhotoEntries();
						if(photos!=null && photos.size() > 0)
						{
							CList photoList = new CList();
							photoList.setRefIdString(album.getGphotoId());
							ArrayList<CListItem> cPhotos = CListItemToolsGoogle.photoEntriesToListItems(photos, album.getGphotoId(), "");
							photoList.setItems(cPhotos);
							photosAll.addAll(cPhotos);
							
						}
					}
				}
				GDataDownloader.toXML(photosAll, photosFile);
				
				
				CList result = new CList();
				result.setItems(photosAll);
				String dstPath= "c_" + ECMContentSourceTypeTools.TypeToInteger(ECMContentSourceType.GooglePicassaItem) + "_.json";
				JSONSerializer serializer = new JSONSerializer();
				String serialized = serializer.deepSerialize(result);
				serializer.exclude("items.asListItem");
				try 
				{
					StringUtils.writeToFile(serialized, path + dstPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		
			return res;
		}
		public static DBConnectionError downloadGDataYoutube(
			String path,
			String uuid,
    		String applicationIdentifier,
			String service,
			String channel,
			String user, 
			String pswd) 
	{
		DBConnectionError res = new DBConnectionError();
		
		YouTubeService myService = new YouTubeService(service+channel);
        URL metafeedUrl = null;
        
        try {
			metafeedUrl = new URL("http://gdata.youtube.com/feeds/api/users/" +channel + "/uploads");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			res.type=-1;
			res.msg="failed";
			res.msgInternal = e.getLocalizedMessage();
			return res;
		}
        VideoFeed resultFeed = null;
        
        try {
			resultFeed = myService.getFeed(metafeedUrl, VideoFeed.class);
		} catch (IOException e) {
			e.printStackTrace();
			res.type=-1;
			res.msg="failed";
			res.msgInternal = e.getLocalizedMessage();
			return res;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			res.type=-1;
			res.msg="failed";
			res.msgInternal = e.getLocalizedMessage();
			
			e.printStackTrace();
			return res;
		}
		String inputFile=new String(path);
		
			
		//inputFile+=service+"/" +channel;
		File pathO = new File(inputFile);
		if(!pathO.exists()){
			pathO.mkdirs();
		}
		
		inputFile+="/uploads.xml";
		System.out.println("saving yt feed to : " + inputFile);
		List<VideoEntry> entries = resultFeed.getEntries();
		if(entries!=null && entries.size()>0){
			
			CList list = new CList();
			list.setTitle(channel);
			
			ArrayList<CVideoItem>items = CListItemToolsGoogle.toListItems(entries, "ndata");
			list.setItems(items);
			
			
			XStream xstream = new XStream(new StaxDriver());
			String xmlPages = xstream.toXML(list);
			if(xmlPages!=null)
			{
				try {
					StringUtils.writeToFile(xmlPages, inputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	
		private static URL buildUrl(String domain, String path, String[] parameters)
			      throws MalformedURLException, GDocumentListException 
			      {
			    if (path == null) {
			      throw new GDocumentListException("null path");
			    }

			    StringBuffer url = new StringBuffer();
			    url.append("https://" + domain + URL_FEED + path);

			    if (parameters != null && parameters.length > 0) {
			      url.append("?");
			      for (int i = 0; i < parameters.length; i++) {
			        url.append(parameters[i]);
			        if (i != (parameters.length - 1)) {
			          url.append("&");
			        }
			      }
			    }

			    return new URL(url.toString());
			  }
		private static URL buildUrl(String path, String[] parameters)
			      throws MalformedURLException, GDocumentListException {
			    if (path == null) {
			      throw new GDocumentListException("null path");
			    }

			    return buildUrl(host, path, parameters);
			  }
		private URL buildUrl(String path) throws MalformedURLException, GDocumentListException {
		    if (path == null) {
		      throw new GDocumentListException("null path");
		    }

		    return buildUrl(path, null);
		}
	
}
