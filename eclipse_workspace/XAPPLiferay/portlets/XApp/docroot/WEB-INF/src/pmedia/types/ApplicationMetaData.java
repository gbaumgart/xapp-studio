/**
 * 
 */
package pmedia.types;

import java.util.ArrayList;

import cmx.types.ConfigurableInformation;

/**
 * @author mc007
 *
 */
public class ApplicationMetaData {
	
	
	private ArrayList<ConfigurableInformation>properties;
	private String version="1.0";
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ApplicationMetaData() 
	{
		if(properties==null)
		{
			properties=new ArrayList<ConfigurableInformation>();
		}
	}
	
	public ArrayList<ConfigurableInformation> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<ConfigurableInformation> properties) {
		this.properties = properties;
	}
	
	
	
}
