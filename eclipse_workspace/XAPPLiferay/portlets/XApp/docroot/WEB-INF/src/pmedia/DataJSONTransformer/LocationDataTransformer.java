package pmedia.DataJSONTransformer;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import pmedia.AssetTools.AssetTools;
import pmedia.types.LocationData;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class LocationDataTransformer extends AbstractTransformer 
{

	private Boolean useRemoteServer=false;
	private String platform=pmedia.types.Constants.USERAGENT_TABLET;
	private String field="iconUrl"; 
	private LocationData location=null;
	private HttpSession session=null;

	public ArrayList<String>getGalleryThumbnailsFiles()
	{
		ArrayList<String>result = new ArrayList<String>();
		String baseUrl=System.getProperty("baseUrl");
		
		Boolean isSlave = System.getProperty("isSlave").equals("true") ? true : false;
		String rootUrl=System.getProperty("webapp.root") + "cache/files/locations/";
		if(!isSlave)
		{
			rootUrl=System.getProperty("galleryPathLocal");
		}
		String galleryPath=rootUrl+location.title.replace(" ","") + "Gallery/";
		
		
		File dir = new File(galleryPath);
		int maxPerPage=20;
		
		
		if(dir.exists())
		{
			String[] children = dir.list();
			File[] files = dir.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		continue;
				 	}
				 	
				 	if(_file.getName().contains("xml"))
				 		continue;
				 	
				 	
				 	if(!_file.getName().contains("tn_"))
				 		continue;

				 	String _item = System.getProperty("galleryPathWeb") + location.title.replace(" ","") + "Gallery/" + _file.getName();

				 	result.add(_item);
			  }
		}
		return result;
	}
	public ArrayList<String>getGalleryFiles()
	{
		ArrayList<String>result = new ArrayList<String>();
		String baseUrl=System.getProperty("baseUrl");
		
		Boolean isSlave = System.getProperty("isSlave").equals("true") ? true : false;
		String rootUrl=System.getProperty("webapp.root") + "cache/files/locations/";
		if(!isSlave)
		{
			rootUrl=System.getProperty("galleryPathLocal");
		}
		String galleryPath=rootUrl+location.title.replace(" ","") + "Gallery/";
		
		
		File dir = null;
		
		dir = new File(galleryPath);
		int maxPerPage = 20;
		if(dir.exists())
		{
			String[] children = dir.list();
			File[] files = dir.listFiles();
			 for (int i=0; i<files.length; i++) 
			 {
				
				 	File _file = files[i];
				 	if(_file.isDirectory())
				 	{
				 		continue;
				 	}
				 	if(_file.getName().contains("tn_"))
				 		continue;
				 	
				 	if(_file.getName().contains("xml"))
				 		continue;

				 	String _item = System.getProperty("galleryPathWeb") + location.title.replace(" ","") + "Gallery/" + _file.getName();
				 	result.add(_item);
			  }
		}
		return result;
	}
	public LocationDataTransformer(Boolean useRemoteServer,String platform,LocationData loc, String field,HttpSession _session)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform; 
		this.field = field;
		this.location=loc;
		this.session=_session;
		//System.out.println("init transformer: ");
	}

	public void transform(Object object) 
	{
		String input=null;//(String)object;
		if(object instanceof String )
		{
			input = (String)object;
		}
		
		ArrayList list  = null;
		if(object instanceof ArrayList )
		{
			list = (ArrayList)object;
		}
		
		// large description picture : 
		if(field.equals("video"))
		{
			if(location!=null && location.video!=null && location.video.length()  > 0 )
			{
				String result = pmedia.DataUtils.LocationPropertyTools.getVideoLink(location, null);
				if(result!=null && result.length() > 0)
				{
					getContext().writeQuoted(result);
					return;
				}else
				{
					getContext().write("null");
					return;
				}
			}
		}
		
		// large description picture : 
		if(field.equals("descriptionPicture"))
		{
			if(location!=null && location.getPictures()!=null && location.getPictures().size()  > 0 )
			{

				String height ="180";
				input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  location.getPictures().get(0) + "&height=180";
				input+="&shadow=true";
				getContext().writeQuoted(input);
				return;
			}
			
			if(location!=null)
			{
				input = System.getProperty("ibiza.defaultLocationIcon");
				getContext().writeQuoted(input);
				return;
			}
			
		}
		
		
		
		// gallery thumbnail files  : 
		if(field.equals("galleryThumbnailFiles"))
		{
			
			if(location!=null)
			{
				list=location.getGalleryThumbnailFiles();
				
				if(list!=null && list.size()==1)
				{
					String _first = (String)list.get(0);
					if(_first.equals("dummy"))
					{
						list.clear();
						ArrayList<String>files=getGalleryThumbnailsFiles();
						list.addAll(files);
					}
				}
				
				TypeContext typeContext = getContext().writeOpenArray();
				for(int i = 0 ; i < list.size() ; i++)
				{
					String _file = (String)list.get(i);
					//getContext().transform(_file);
					if (!typeContext.isFirst())
					{ 
						getContext().writeComma();
					}
					typeContext.setFirst(false); 
					
					getContext().writeQuoted(_file);
				}
				getContext().writeCloseArray();
			}
		}
		
		// gallery full size  files  : 
		if(field.equals("galleryFiles"))
		{
			if(location!=null)
			{
				list=location.getGalleryFiles();
				if(list!=null && list.size()==1)
				{
					String _first = (String)list.get(0);
					if(_first.equals("dummy"))
					{
						list.clear();
						ArrayList<String>files=getGalleryFiles();
						list.addAll(files);
					}
				}
				TypeContext typeContext = getContext().writeOpenArray();
				for(int i = 0 ; i < list.size() ; i++)
				{
					String _file = (String)list.get(i);
					//getContext().transform(_file);
					if (!typeContext.isFirst())
					{ 
						getContext().writeComma();
					}
					typeContext.setFirst(false); 
					
					getContext().writeQuoted(_file);
				}
				getContext().writeCloseArray();
			}
		}
		// static map url  : 
		if(field.equals("staticMapUrl"))
		{
			if(location!=null)
			{
				//Generate URL

				//http://maps.google.com/maps/api/staticmap?center=38.908801,1.433029&zoom=13&markers=38.908857,1.432378&maptype=hybrid&size=500x400&sensor=TRUE_OR_FALSE
				//String mapUrl = "http://maps.google.com/maps/m?dc=gorganic#ll=";
				//mapUrl += location.latitude + "," + location.longtitude;
				//mapUrl += "&z=15&view=m&ac=m";
				String width = "400";
				String height = "400";
				
				String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + location.latitude + "," + location.longtitude +" &zoom=13&markers=" + location.latitude + "," +  location.longtitude + "&maptype=hybrid&size=" + width +"x" + height+"&sensor=false";
				
//				input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  location.getPictures().get(0) + "&height=180";
//				input+="&shadow=true";
				//System.out.println("loc map url : " + mapUrl);
				input = mapUrl;
				getContext().writeQuoted(input);
				return;
			}
			
		}
		
		// list icon : 
		if(field.equals("iconUrl"))
		{
			if(input!=null && input.length() ==0)
			{
				input = System.getProperty("ibiza.defaultLocationIcon");
				getContext().writeQuoted(input);
				return;
			}
		
			if(input!=null && !input.equals("null") &&  input.length() > 0 )
			{
				
				String height ="60";
				
				/*
				if(session!=null)
				{
					String _h = (String)session.getAttribute("PixelRatio");
					if(_h!=null && _h.equals("2"))
					{
						height="120";
					}
				}
				*/
				
				input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  input + "&width="+height;
				input+="&shadow=true&icon=true";
				getContext().writeQuoted(input);
			}
		}
		
		// list icon : 
		if(field.equals("categoryIcon"))
		{
			
			if(input!=null && !input.equals("null") &&  input.length() > 0 )
			{
				String newInput = AssetTools.resolvePath(input, true, null);
				if(newInput!=null && newInput.length() > 0)
				{
					input=newInput;
				}
				getContext().writeQuoted(input);
			}
		}
	}

}
