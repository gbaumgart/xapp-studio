package pmedia.types;
public class Domain 
{
	private static final long serialVersionUID = 1L;
	public static int counter=0;
	public String title="";
	public String fbApiKey="";
	public String fbSecret="";
	public String fbUserID="";
	public String eventPictureStore="";
	public String locationPictureStore="";
	public String dialPrefix="";
	
	public String searchCountry="";
	public String id="";
	
	public String getId(){return id;}
	public void setId(String id){this.id= id;}
	
	public String getTitle(){		return title;	}
	public void setTitle(String domain) {		this.title= domain;	}
	
	public String getDomain(){		return title;	}
	public void setDomain(String domain) {		this.title= domain;	}
	
	
	public Domain()
	{
		id = "" + (counter);
		counter++;
	}
}
