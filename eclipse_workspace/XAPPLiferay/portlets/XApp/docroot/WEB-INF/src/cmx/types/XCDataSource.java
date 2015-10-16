package cmx.types;

import java.util.ArrayList;
import java.util.UUID;

import jsontype.ComposerResources;
import cmx.tools.CIFactory;
import pmedia.utils.ECMTypeTools;

public class XCDataSource extends DataSourceBase{
	
	public ComposerResources resources;
	
	public XCDataSource() 
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
		ConfigurableInformation type= CIFactory.SimpleStringCI("Type", "", 7);
		ConfigurableInformation authClass= CIFactory.SimpleStringCI("AuthClass", "", 8);
		ConfigurableInformation rpcURL= CIFactory.SimpleStringCI("RPCUrl", "", 9);
		ConfigurableInformation identifier= CIFactory.SimpleStringCI("identifier", "", 10);
		
		_inputs.add(host);
		_inputs.add(user);
		_inputs.add(password);
		_inputs.add(dataBase);
		_inputs.add(prefix);
		_inputs.add(version);
		_inputs.add(url);
		_inputs.add(type);
		_inputs.add(authClass);
		_inputs.add(rpcURL);
		_inputs.add(identifier);
		
	}
	public void setRPCUrl(String value) 
	{
		ConfigurableInformation _ci = getInputs().get(9);
		_ci.setValue(value);
	}
	public String getRPCUrl() {
		ConfigurableInformation _ci = getInputs().get(9);return _ci.getValue();
	}
	
	public void setAuthClass(String value) 
	{
		ConfigurableInformation _ci = getInputs().get(8);
		_ci.setValue(value);
	}
	public String getAuthClass() {
		ConfigurableInformation _ci = getInputs().get(8);return _ci.getValue();
	}
	
	public void setIdentifier(String value) 
	{
		ConfigurableInformation _ci = getInputs().get(10);
		_ci.setValue(value);
	}
	public String getIdentifier() {
		ConfigurableInformation _ci = getInputs().get(10);return _ci.getValue();
	}
	public ComposerResources getResources() {
		return resources;
	}
	public void setResources(ComposerResources resources) {
		this.resources = resources;
	}
}
