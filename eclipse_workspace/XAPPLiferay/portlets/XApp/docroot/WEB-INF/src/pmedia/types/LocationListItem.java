package pmedia.types;

public class LocationListItem 
{
	private int id;
	private String name;
	private static final long serialVersionUID = 1L;
	
	public LocationListItem(int id, String name)
	{
		this.id = id;
		this.name = name;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLabel()
	{
		return this.name;
	}
	
	public int getValue()
	{
		return this.id;
	}
}
