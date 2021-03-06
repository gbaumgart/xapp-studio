package pmedia.DataJSONTransformer;

import java.util.LinkedList;

import pmedia.DataUtils.LocationPropertyTools;
import pmedia.types.EventData;
import flexjson.JSONContext;
import flexjson.transformer.AbstractTransformer;

public class EventDataTransformer extends AbstractTransformer 
{

	private Boolean useRemoteServer=false;
	private String platform=pmedia.types.Constants.USERAGENT_TABLET;
	private EventData event=null;
	private String lang="en";
	private String field; 
	public EventDataTransformer(Boolean useRemoteServer,String platform,EventData _event,String _field,String lang)
	{
		this.useRemoteServer = useRemoteServer;
		this.platform = platform; 
		this.event = _event;
		this.lang = lang;
		this.field = _field;
		//System.out.println("init transformer: ");
	}

	public void transform(Object object) 
	{
		
		JSONContext ctx = getContext();
		//LinkedList<Object>stack = ctx.getObjectStack();
		
		String input=(String)object;

		if(input!=null && !input.equals("null") &&  input.length() > 0 )
		{
		
			
			if(field !=null && field.equals("iconU r l"))
			{
				if(event !=null && event.iconUrl !=null && event.iconUrl.length() > 0)
				{
					if(event.iconUrl.contains("http"))
					{
						input=event.iconUrl;
					}
				}
				String prefix=System.getProperty("ibiza.locationFiles");
				if(input.contains("http"))
				{
					prefix = "";
				}
				input = System.getProperty("imageProcessorUrl") + "/servlets/ImageScaleIcon?src=" + prefix  +  input + "&width=55";
				//input = "http://192.168.1.37:8082/Pmaster" + "/servlets/ImageScaleIcon?src=" + prefix  +  input + "&width=55";
				input+="&shadow=true";
				input+="&icon=true";
				
				
				//System.out.println("got json thing : " + input);
			}
			
			/*if(this.field!=null && this.field.equals("s t a t i c M a p U r l") && event.loc!=null)
			{
				String width = "400";
				String height = "400";
				String mapUrl = Location.getMapUrl(event.loc, null); 
				input = mapUrl;
			}
			*/
		}
		getContext().writeQuoted(input);
	}

}
