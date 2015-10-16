package pmedia.DataJSONTransformer;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import cmx.types.Application;

import pmedia.types.BaseData;
import pmedia.types.LocationData;
import pmedia.types.PictureTransformOptions;
import pmedia.utils.ApplicationContentTools;
import pmedia.utils.StringUtils;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class BaseDataTransformer extends AbstractTransformer 
{

	private Boolean useRemoteServer=false;
	private String platform=pmedia.types.Constants.USERAGENT_TABLET;
	private String field="iconUrl"; 
	private BaseData data=null;
	private HttpSession session=null;
	private Application application=null;
	private String lang=null;
	private PictureTransformOptions pictureTransformOptions=null;
	
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
		String galleryPath=rootUrl+data.title.replace(" ","") + "Gallery/";
		
		
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
				 	
				 	/*
				 	if(i>maxPerPage)
				 		break;
			
				 	*/
				 	String _item = System.getProperty("galleryPathWeb") + data.title.replace(" ","") + "Gallery/" + _file.getName();


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
		String galleryPath=rootUrl+data.title.replace(" ","") + "Gallery/";
		
		
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
				 	/*
				 	 if(i>maxPerPage)
					 		break;
				 	 */
				 	String _item = System.getProperty("galleryPathWeb") + data.title.replace(" ","") + "Gallery/" + _file.getName();
			
				 	result.add(_item);
			  }
		}
		return result;
	}
	public BaseDataTransformer(Boolean useRemoteServer,String platform,BaseData _data, String field)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform; 
		this.field = field;
		this.data=_data;
		//System.out.println("init transformer: ");
	}
	
	public BaseDataTransformer(
			Boolean useRemoteServer,
			String platform,
			String _lang,
			BaseData _data, 
			String field,
			Application _application,
			HttpSession _session,
			PictureTransformOptions _pictureOptions)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform; 
		this.field = field;
		this.data=_data;
		this.session = _session;
		this.application = _application;
		this.lang = _lang;
		this.pictureTransformOptions = _pictureOptions;
		//System.out.println("init transformer: ");
	}

	public void transform(Object object) 
	{
		String input=null;//(String)object;
		if(object instanceof String )
		{
			input = (String)object;
		}
		
		if(platform==null)
		{
			platform="PC";
		}
		
		ArrayList list  = null;
		if(object instanceof ArrayList )
		{
			list = (ArrayList)object;
		}
		
		String width ="180";
		if(platform.equals(pmedia.types.Constants.USERAGENT_PC))
		{
			width="300";
		}
		if(platform.equals(pmedia.types.Constants.USERAGENT_IPHONE))
		{
			width="300";
		}
		if(platform.equals(pmedia.types.Constants.USERAGENT_IPAD))
		{
			width="300";
		}
		
		if(platform.equals(pmedia.types.Constants.USERAGENT_TABLET))
		{
			width="300";
		}
		
		// large description picture : 
		if(field.equals("descriptionPicture"))
		{
			if(data!=null && data.getPictures()!=null && data.getPictures().size()  > 0 )
			{
				
				input = System.getProperty("imageProcessorUrl") +"servlets/"; 
				
				String parms = "ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  data.getPictures().get(0) + "&height=" + width;
				parms+="&shadow=true";
				//input +=StringUtils.encode(parms);
				input +=parms;


				getContext().writeQuoted(input);
				return;
			}
			
			if(data!=null && data.iconUrl!=null && data.iconUrl.length() > 0 )
			{				
				if(data.iconUrl.contains("http"))
				{
					input=data.iconUrl;
					input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + input + "&height=" + width;
					input+="&shadow=true";
					getContext().writeQuoted(input);
					return;
				}
				
			}
			if(data!=null)
			{
				input = System.getProperty("ibiza.defaultLocationIcon");
				getContext().writeQuoted(input);
				return;
			}
			
		}
		
		// gallery thumbnail files  : 
		if(field.equals("galleryThumbnailFiles"))
		{
			
			if(data!=null)
			{
				list=data.getGalleryThumbnailFiles();
				
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
			if(data!=null)
			{
				list=data.getGalleryFiles();
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
		
		
		// gallery full size  files  : 
		if(field.equals("pictures"))
		{
			if(data!=null)
			{
				list=data.getPictures();
				if(list!=null && list.size()==1)
				{
					String _first = (String)list.get(0);
					/*if(_first.equals("dummy"))
					{
						list.clear();
						ArrayList<String>files=getGalleryFiles();
						list.addAll(files);
					}
					*/
				}
				TypeContext typeContext = getContext().writeOpenArray();
				for(int i = 0 ; i < list.size() ; i++)
				{
					String _file = (String)list.get(i);
					String _resolved = pmedia.AssetTools.AssetTools.resolvePath(_file, true, null);
					
					//getContext().transform(_file);
					if (!typeContext.isFirst())
					{ 
						getContext().writeComma();
					}
					typeContext.setFirst(false); 
					
					getContext().writeQuoted(_resolved);
				}
				getContext().writeCloseArray();
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
				input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + System.getProperty("ibiza.locationFiles")  +  input + "&height=60";
				input+="&shadow=true";
				getContext().writeQuoted(input);
			}
		}
		
		if(field.equals("description"))
		{
			if(application!=null)
			{
				input=ApplicationContentTools.transformHTMLText(application, input,pictureTransformOptions);
				//System.out.println(input);
				getContext().writeQuoted(input);
			}
		}
	}
	/**
	 * @return the pictureTransformOptions
	 */
	public PictureTransformOptions getPictureTransformOptions() {
		return pictureTransformOptions;
	}
	/**
	 * @param pictureTransformOptions the pictureTransformOptions to set
	 */
	public void setPictureTransformOptions(
			PictureTransformOptions pictureTransformOptions) {
		this.pictureTransformOptions = pictureTransformOptions;
	}

}
