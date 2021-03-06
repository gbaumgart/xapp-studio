package pmedia.DataJSONTransformer;

import java.util.ArrayList;
import java.util.LinkedList;

import pmedia.DataUtils.LocationPropertyTools;
import pmedia.types.EventData;
import pmedia.types.ResourceData;

import flexjson.JSONContext;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class ResourceDataTransformer extends AbstractTransformer 
{

	private Boolean useRemoteServer=false;
	private String platform=pmedia.types.Constants.USERAGENT_TABLET;
	private ResourceData data=null;
	private String lang="en";
	private String field; 
	public ResourceDataTransformer(Boolean useRemoteServer,String platform,ResourceData _data,String _field,String lang)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform; 
		this.data = _data;
		this.lang = lang;
		this.field = _field;
		//System.out.println("init transformer: ");
	}

	public void transform(Object object) 
	{
		
		JSONContext ctx = getContext();
		LinkedList<Object>stack = ctx.getObjectStack();
		Object a = stack.getFirst();
		Object b = stack.getLast();
		
		String input=null;
		if(object instanceof String )
		{
			input = (String)object;
		}
		
		ArrayList list  = null;
		if(object instanceof ArrayList )
		{
			list = (ArrayList)object;
		}

		if(input!=null && !input.equals("null") &&  input.length() > 0 )
		{
			if(this.field !=null && this.field.equals("iconU r l"))
			{
				String prefix=System.getProperty("ibiza.locationFiles");
				if(input.contains("http"))
				{
					prefix = "";
				}
				//input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + prefix  +  input + "&height=60";
				input = prefix  +  input;
				getContext().writeQuoted(input);
				return;
				//input+="&shadow=true";
				//System.out.println("got json thing : " + input);
			}
		}
		
		// gallery full size  files  : 
		if(field.equals("pictures"))
		{
			
			//list=data.getGalleryFiles();
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
				_file = pmedia.AssetTools.AssetTools.resolvePath(_file, true, null);
				getContext().writeOpenObject();
				getContext().write("src:");
				getContext().writeQuoted(_file);
				getContext().writeCloseObject();
			}
			getContext().writeCloseArray();
			
			return;
			
		}
		
		
		
	}

}
