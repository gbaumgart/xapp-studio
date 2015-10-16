package cmx.types;

import java.util.ArrayList;

public class Resources 
{
	public boolean hasResource(Resource resource)
	{
		for(int i= 0 ; i < getItems().size() ; i ++)
		{
			Resource r = getItems().get(i);
			if(r.url!=null && r.type!=null)
			{
				if(resource.url !=null && resource.type!=null)
				{
					if(r.url.equals(resource.url) && r.type.equals(resource.type))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	public void merge(Resources  resources)
	{
		for(int i= 0 ; i < resources.getItems().size() ; i ++)
		{
			Resource r = resources.getItems().get(i);
			if(!hasResource(r))
			{
				getItems().add(r);
			}
		}
	}
	
	public ArrayList<ResourceInclude>includes= new ArrayList<ResourceInclude>();
	
	public ArrayList<Resource>items = new ArrayList<Resource>();
	
	public String distDir;
	
	public ArrayList<Resource> getItems() 
	{
		return items;
	}
	public void setItems(ArrayList<Resource> items) 
	{
		this.items = items;
	}
	public String getDistDir() {
		return distDir;
	}
	public void setDistDir(String distDir) {
		this.distDir = distDir;
	}
	public ArrayList<ResourceInclude> getIncludes() {
		return includes;
	}
	public void setIncludes(ArrayList<ResourceInclude> includes) {
		this.includes = includes;
	}
	
	
	
}
