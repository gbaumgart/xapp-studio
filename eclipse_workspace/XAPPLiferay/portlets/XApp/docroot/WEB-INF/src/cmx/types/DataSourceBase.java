package cmx.types;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import cmx.tools.CIFactory;

public class DataSourceBase extends Configation {

	public String name;
	public String uid;
	public String applicationIdentifier;
	public String userIdentifier;
	public String version;
	public String type;
	public String port;
	public String url;
	public Boolean isDownloaded = false;
	public ArrayList<Integer> features;

	public DataSourceBase() {
		super();
	}
	
	/**
	 * @return the port
	 */
	public String getPort() {
		
		ConfigurableInformation _ci = null;
		
		try {
			_ci = getInputs().get(8);	
		} catch (Exception e) 
		{
			if(_ci==null)
			{
				_ci= CIFactory.SimpleStringCI("port", "", 8);
				ArrayList<ConfigurableInformation>_inputs = getInputs();
				_inputs.add(_ci);
			}
		}
		
		
		return _ci.getValue();
	}

	/**
	 * @param version the version to set
	 */
	public void setPort(String port) 
	{
		ConfigurableInformation _ci = null;
		try {
			_ci=getInputs().get(8);
		} catch (Exception e) {
			_ci= CIFactory.SimpleStringCI("port", "", 8);
			ArrayList<ConfigurableInformation>_inputs = getInputs();
			_inputs.add(_ci);
		}
		_ci.setValue(port);
	}
	
	/**
	 * @return the version
	 */
	public String getType() {
		ConfigurableInformation _ci = getInputs().get(7);
		if(_ci==null){
			_ci= CIFactory.SimpleStringCI("MySQL", "", 7);
			ArrayList<ConfigurableInformation>_inputs = getInputs();
			_inputs.add(_ci);
		}
		return _ci.getValue();
	}

	/**
	 * @param version the version to set
	 */
	public void setType(String version) {
		ConfigurableInformation _ci = null;
		try {
			_ci=getInputs().get(7);
		} catch (Exception e) {
			_ci= CIFactory.SimpleStringCI("MySQL", "", 7);
			ArrayList<ConfigurableInformation>_inputs = getInputs();
			_inputs.add(_ci);
		}
		
		_ci.setValue(version);
	}

	public String getUser() {
		ConfigurableInformation _ci = getInputs().get(1);return _ci.getValue();
	}

	public String getUrl() {
		ConfigurableInformation _ci = getInputs().get(6);return _ci.getValue();
	}

	public void setUrl(String url) {
		ConfigurableInformation _ci = getInputs().get(6);
		_ci.setValue(url);
	}

	public String getHost() 
	{
		ConfigurableInformation _ci = getInputs().get(0);
		String value = _ci.getValue();
		
		/*try {
			URI uri = new URI(value);
			value= uri.getHost();
		} catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}*/
		
		return value;
	}
	
	public String getDomain()
	{
		
		String _host = getUrl();
		if(_host!=null){
			_host=_host.replace(" ", "");
		}
		try {
		URI uri = new URI(_host);
		_host= uri.getHost();
		} catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
		return _host;
	}

	public String getPassword() {
		ConfigurableInformation _ci = getInputs().get(2);return _ci.getValue();
	}

	public String getDatabase() {
		ConfigurableInformation _ci = getInputs().get(3);return _ci.getValue();
	}

	public String getPrefix() {
		ConfigurableInformation _ci = getInputs().get(4);return _ci.getValue();
	}

	public void setUser(String user) {
		ConfigurableInformation _usr = getInputs().get(1);
		_usr.setValue(user);
	}

	public void setHost(String host) {
		ConfigurableInformation _host = getInputs().get(0);
		_host.setValue(host);
	}

	public void setPassword(String pwd) {
		ConfigurableInformation _ci = getInputs().get(2);
		_ci.setValue(pwd);
	}

	public void setDatabase(String db) {
		ConfigurableInformation _ci = getInputs().get(3);
		_ci.setValue(db);
	}

	public void setPrefix(String value) {
		ConfigurableInformation _ci = getInputs().get(4);
		_ci.setValue(value);
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the applicationIdentifier
	 */
	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}

	/**
	 * @param applicationIdentifier the applicationIdentifier to set
	 */
	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	/**
	 * @return the userIdentifier
	 */
	public String getUserIdentifier() {
		return userIdentifier;
	}

	/**
	 * @param userIdentifier the userIdentifier to set
	 */
	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		ConfigurableInformation _ci = getInputs().get(5);return _ci.getValue();
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		ConfigurableInformation _ci = getInputs().get(5);
		_ci.setValue(version);
	}
	
	
	
	


	/**
	 * @return the features
	 */
	public ArrayList<Integer> getFeatures() {
		return features;
	}

	/**
	 * @param features the features to set
	 */
	public void setFeatures(ArrayList<Integer> features) {
		this.features = features;
	}

	/**
	 * @return the isDownloaded
	 */
	public Boolean getIsDownloaded() {
		return isDownloaded;
	}

	/**
	 * @param isDownloaded the isDownloaded to set
	 */
	public void setIsDownloaded(Boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}

}