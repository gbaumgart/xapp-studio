package cmx.types;

import java.util.ArrayList;
import java.util.UUID;

import cmx.tools.CIFactory;

import pmedia.utils.ECMTypeTools;

public class XMLDataSource extends DataSourceBase{
	
	public XMLDataSource() 
	{
		ArrayList<ConfigurableInformation>_inputs = getInputs();
		this.setUid(UUID.randomUUID().toString());
		
		ConfigurableInformation host = CIFactory.SimpleStringCI("Host", "", 0);
		ConfigurableInformation user = CIFactory.SimpleStringCI("User", "", 1);
		ConfigurableInformation password = CIFactory.SimpleStringCI("Password", "", 2);
		ConfigurableInformation dataBase= CIFactory.SimpleStringCI("Database", "", 3);
		ConfigurableInformation prefix = CIFactory.SimpleStringCI("Prefix", "", 4);
		ConfigurableInformation version = CIFactory.SimpleStringCI("Version", "", 5);
		ConfigurableInformation url= CIFactory.SimpleStringCI("Url", "", 6);
		ConfigurableInformation type= CIFactory.SimpleStringCI("XMLRPC", "", 7);
		
		
		_inputs.add(host);
		_inputs.add(user);
		_inputs.add(password);
		_inputs.add(dataBase);
		_inputs.add(prefix);
		_inputs.add(version);
		_inputs.add(url);
		_inputs.add(type);
		
	}
	public void setRPCUrl(String value) 
	{
		ConfigurableInformation _ci = getInputs().get(7);
		_ci.setValue(value);
	}
	public String getRPCUrl() {
		ConfigurableInformation _ci = getInputs().get(7);return _ci.getValue();
	}
}
