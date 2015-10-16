package xappconnect.types;

import java.util.ArrayList;
import java.util.UUID;

import pmedia.types.ApplicationMetaDataKeys;
import pmedia.utils.CITools;

import cmx.tools.CIFactory;
import cmx.types.Configation;
import cmx.types.ConfigurableInformation;
import cmx.types.Reference;

public class CustomType extends Configation 
{

	public ArrayList<Reference>references = new ArrayList<Reference>();
	
	public void addReference(String uuid)
	{
		if(!hasReference(uuid))
		{
			references.add(new Reference(uuid));
		}
	}
	public boolean hasReference(String uuid)
	{
		for (int i = 0 ; i < references.size(); i ++)
		{
			Reference ref = references.get(i);
			if(ref.get_reference().equals(uuid))
			{
				return true;
			}
		}
		return false;
	}
	
	public String name;
	
	public String uuid;
	
	public String parent;
	
	public Boolean hidden;
	
	public Boolean enabled;
	
	public Boolean system;
	
	public Boolean folder;
	
	public String parentDataSourceType;
	
	public String urlSchema;
	
	public String userData;
	
	public String serviceClass;
	
	public String onPostScript;
	
	public String onPreScript;
	
	public String treeFactoryClass;
	
	public String clientDriverClass;
	
	public String clientRenderClass;
	
	public String adapterClass;
	
	public String queries;
	public String schemas;
	public String method;
	
	public String defaultGroupId;
	
	public String groupSelectStatement;
	
	public String orderStatement;
	
	public String limitStatement;
	public String friendlyName;
	public String icon;
	
	
	/***
	 * 
	 */
	public Boolean isDetail;
	public String relations;
	
	
	
	public ConfigurableInformation getByReferenceCI(ConfigurableInformation in)
	{
		String inid = in.getId();
		
		
		for(int i  = 0 ; i < getInputs().size() ; i++){
			ConfigurableInformation ci = getInputs().get(i);
			if(ci.getId().equals(inid)){
				return ci;
			}
		}
		
		
		return null;
	}
	
	public CustomType()
	{
		
		super();

		ArrayList<ConfigurableInformation>_inputs = getInputs();
		
		ConfigurableInformation name = CIFactory.SimpleStringCI("name", "", 0);
		_inputs.add(name);
		
		ConfigurableInformation uuid= CIFactory.SimpleStringCI("uuid", UUID.randomUUID().toString(), 1);
		uuid.setVisible(false);
		uuid.setId(UUID.randomUUID().toString());
		/*
		if(uuidIn!=null)
		{
			uuid.setValue(uuidIn);
		}
		*/
		_inputs.add(uuid);
		
		
		ConfigurableInformation parent= CIFactory.SimpleStringCI("parent", "", 2);
		parent.setId(UUID.randomUUID().toString());
		_inputs.add(parent);
		
		ConfigurableInformation hidden= CIFactory.SimpleBoolean("hidden", false, 3);
	//	hidden.setId(UUID.randomUUID().toString());
		_inputs.add(hidden);
		
		ConfigurableInformation enabled= CIFactory.SimpleBoolean("enabled", true, 4);
	//	enabled.setId(UUID.randomUUID().toString());
		_inputs.add(enabled);
		
		ConfigurableInformation system= CIFactory.SimpleBoolean("system", true, 5);
	//	system.setId(UUID.randomUUID().toString());
		_inputs.add(system);
		
		ConfigurableInformation parentDataSourceType= CIFactory.SimpleStringCI("parentDataSourceType", "", 6);
		parentDataSourceType.setId(UUID.randomUUID().toString());
		_inputs.add(parentDataSourceType);
		
		ConfigurableInformation urlSchema= CIFactory.SimpleStringCI("urlSchema", "", 7);
//		urlSchema.setId(UUID.randomUUID().toString());
		_inputs.add(urlSchema);
		
		ConfigurableInformation userData= CIFactory.SimpleStringCI("userData", "", 8);
		_inputs.add(userData);
		
		ConfigurableInformation serviceClass= CIFactory.SimpleStringCI("serviceClass", "", 9);
		_inputs.add(serviceClass);
		
		ConfigurableInformation onPreScript= CIFactory.SimpleStringCI("onPreScript", "", 10);
		_inputs.add(onPreScript);
		
		ConfigurableInformation onPostScript= CIFactory.SimpleStringCI("onPostScript", "", 11);
		_inputs.add(onPostScript);
		
		ConfigurableInformation treeFactoryClass= CIFactory.SimpleStringCI("treeFactoryClass", "", 12);
		_inputs.add(treeFactoryClass);
		
		ConfigurableInformation clientDriverClass= CIFactory.SimpleStringCI("clientDriverClass", "", 13);
		_inputs.add(clientDriverClass);
		
		
		ConfigurableInformation adapterClass= CIFactory.SimpleStringCI("adapterClass", "", 14);
		_inputs.add(adapterClass);
				
		ConfigurableInformation folder= CIFactory.SimpleBoolean("folder", true, 15);
		_inputs.add(folder);
		
		
		ConfigurableInformation clientRenderClass= CIFactory.SimpleStringCI("clientRenderClass", "", 16);
		_inputs.add(clientRenderClass);
		
		/***
		 * 
		 */
		ConfigurableInformation queries= CIFactory.SimpleStringCI("queries", "", 17);
		queries.setId(UUID.randomUUID().toString());
		queries.setType(25);
		_inputs.add(queries);
		
		ConfigurableInformation schemas= CIFactory.SimpleStringCI("schemas", "", 18);
		schemas.setId(UUID.randomUUID().toString());
		schemas.setType(26);
		_inputs.add(schemas);
		
		ConfigurableInformation method= CIFactory.SimpleStringCI("method", "", 19);
		method.setType(13);
		_inputs.add(method);
		
		ConfigurableInformation dG= CIFactory.SimpleStringCI("defaultGroupId", "", 20);
		dG.setType(13);
		_inputs.add(dG);
		
		ConfigurableInformation dSG= CIFactory.SimpleStringCI("groupSelectStatement", "", 21);
		dSG.setType(13);
		_inputs.add(dSG);
		
		
		
		ConfigurableInformation detail= CIFactory.SimpleStringCI("isDetail", "", 22);
		detail.setType(0);
		_inputs.add(detail);
		
		ConfigurableInformation relations= CIFactory.SimpleStringCI("relations", "", 23);
		relations.setType(27);
		_inputs.add(relations);
		
		ConfigurableInformation oS= CIFactory.SimpleStringCI("orderStatement", "", 24);
		oS.setType(13);
		_inputs.add(oS);
		
		ConfigurableInformation lS= CIFactory.SimpleStringCI("limitStatement", "", 25);
		lS.setType(13);
		_inputs.add(lS);
		
		ConfigurableInformation fn= CIFactory.SimpleStringCI("friendlyName", "", 26);
		fn.setType(13);
		_inputs.add(fn);
		
		ConfigurableInformation icon= CIFactory.SimpleStringCI("icon", "", 27);
		icon.setType(13);
		_inputs.add(icon);
		
		
		
	}
	
	
	public String getName() {
		ConfigurableInformation _ci = getInputs().get(0);return _ci.getValue();
	}
	
	public void setName(String value) {
		ConfigurableInformation _ci = getInputs().get(0);
		_ci.setValue(value);
	}


	public String getUuid() {
		ConfigurableInformation _ci = getInputs().get(1);return _ci.getValue();
	}


	public void setUuid(String uuid) {
		ConfigurableInformation _ci = getInputs().get(1);
		_ci.setValue(uuid);
	}


	public String getParent() {
		ConfigurableInformation _ci = getInputs().get(2);return _ci.getValue();
	}


	public void setParent(String parent) {
		ConfigurableInformation _ci = getInputs().get(2);
		_ci.setValue(parent);
	}


	public Boolean getHidden() {
		ConfigurableInformation _ci = getInputs().get(3);return Boolean.parseBoolean(_ci.getValue());
	}


	public void setHidden(Boolean hidden) {
		ConfigurableInformation _ci = getInputs().get(3);
		_ci.setValue(Boolean.toString(hidden));
	}


	public Boolean getEnabled() {
		ConfigurableInformation _ci = getInputs().get(4);return Boolean.parseBoolean(_ci.getValue());
	}


	public void setEnabled(Boolean enabled) {
		ConfigurableInformation _ci = getInputs().get(4);
		_ci.setValue(Boolean.toString(enabled));
	}


	public Boolean getSystem() {
		ConfigurableInformation _ci = getInputs().get(5);return Boolean.parseBoolean(_ci.getValue());
	}


	public void setSystem(Boolean system) {
		ConfigurableInformation _ci = getInputs().get(5);
		_ci.setValue(Boolean.toString(system));
	}


	public String getParentDataSourceType() {
		ConfigurableInformation _ci = getInputs().get(6);return _ci.getValue();
	}


	public void setParentDataSourceType(String parentDataSourceType) {
		ConfigurableInformation _ci = getInputs().get(6);
		_ci.setValue(parentDataSourceType);
	}


	public String getUrlSchema() {
		ConfigurableInformation _ci = getInputs().get(7);return _ci.getValue();
	}


	public void setUrlSchema(String urlSchema) {
		ConfigurableInformation _ci = getInputs().get(7);
		_ci.setValue(urlSchema);
	}


	public String getUserData() {
		ConfigurableInformation _ci = getInputs().get(8);return _ci.getValue();
	}


	public void setUserData(String userData) {
		ConfigurableInformation _ci = getInputs().get(8);
		_ci.setValue(userData);
	}


	public String getServiceClass() {
		ConfigurableInformation _ci = getInputs().get(9);return _ci.getValue();
	}


	public void setServiceClass(String serviceClass) {
		ConfigurableInformation _ci = getInputs().get(9);
		_ci.setValue(serviceClass);
	}


	public String getOnPreScript() {
		ConfigurableInformation _ci = getInputs().get(10);return _ci.getValue();
	}


	public void setOnPreScript(String onPreScript) {
		ConfigurableInformation _ci = getInputs().get(10);
		_ci.setValue(onPreScript);
	}
	
	public String getOnPostScript() {
		ConfigurableInformation _ci = getInputs().get(11);return _ci.getValue();
	}


	public void setOnPostScript(String onPostScript) {
		ConfigurableInformation _ci = getInputs().get(11);
		_ci.setValue(onPostScript);
	}


	public String getTreeFactoryClass() {
		ConfigurableInformation _ci = getInputs().get(12);return _ci.getValue();
	}


	public void setTreeFactoryClass(String treeFactoryClass) {
		
		ConfigurableInformation _ci = getInputs().get(12);
		
		_ci.setValue(treeFactoryClass);
		
		
	}
	
	
	public String getClientDriverClass() 
	{
		ConfigurableInformation _ci = getInputs().get(13);return _ci.getValue();
	}
	
	public void setClientDriverClass(String clientClass) 
	{
		ConfigurableInformation _ci = getInputs().get(13);
		_ci.setValue(clientClass);
	}


	public String getAdapterClass() {
		ConfigurableInformation _ci = getInputs().get(14);return _ci.getValue();
	}


	public void setAdapterClass(String adapterClass) {
		ConfigurableInformation _ci = getInputs().get(14);
		_ci.setValue(adapterClass);
	}


	public Boolean getFolder() {
		ConfigurableInformation _ci = getInputs().get(15);return Boolean.parseBoolean(_ci.getValue());
	}
	
	public void setFolder(Boolean folder) {
		ConfigurableInformation _ci = getInputs().get(15);
		_ci.setValue(Boolean.toString(folder));
	}


	public ArrayList<Reference> getReferences() {
		return references;
	}


	public void setReferences(ArrayList<Reference> references) {
		this.references = references;
	}
	public String getClientRenderClass() {
		ConfigurableInformation _ci = getInputs().get(16);return _ci.getValue();
	}
	
	public void setClientRenderClass(String clientRenderClass) 
	{
		ConfigurableInformation _ci = getInputs().get(16);
		_ci.setValue(clientRenderClass);
	}
	
	
	/***
	 * 
	 */
	public String getQueries(){
		ConfigurableInformation _ci = getInputs().get(17);return _ci.getValue();
	}


	public void setQueries(String queries) {
		ConfigurableInformation _ci = getInputs().get(17);
		_ci.setValue(queries);
	}
	
	
	public String getSchemas(){
		ConfigurableInformation _ci = getInputs().get(18);return _ci.getValue();
	}


	public void setSchemas(String queries) {
		ConfigurableInformation _ci = getInputs().get(18);
		_ci.setValue(queries);
	}
	
	public String getMethod(){
		ConfigurableInformation _ci = getInputs().get(19);return _ci.getValue();
	}


	public void setMethod(String method) {
		ConfigurableInformation _ci = getInputs().get(19);
		_ci.setValue(method);
	}
	
	public String getDefaultGroupId(){
		ConfigurableInformation _ci = getInputs().get(20);return _ci.getValue();
	}


	public void setDefaultGroupId(String DefaultGroupId) 
	{
		ConfigurableInformation _ci = getInputs().get(20);
		_ci.setValue(DefaultGroupId);
	}
	
	public String getGroupSelectStatement() {
		ConfigurableInformation _ci = getInputs().get(21);
		if(_ci!=null)
		{
			return _ci.getValue();
		}
		return "";
		
	}
	public void setGroupSelectStatement(String groupSelectStatement) {
		ConfigurableInformation _ci = getInputs().get(21);
		if(_ci!=null)
		{
			_ci.setValue(groupSelectStatement);
		}
	}
	public Boolean getIsDetail() {
		ConfigurableInformation _ci = CITools.getByName(getInputs(),"isDetail");
		if(_ci!=null)
			return Boolean.parseBoolean(_ci.getValue());
		
		return false;
	}
	public void setIsDetail(Boolean isDetail) {
		ConfigurableInformation _ci = CITools.getByName(getInputs(),"isDetail");
		if(_ci!=null){
			_ci.setValue(Boolean.toString(isDetail));
		}
	}
	public String getRelations() {
		ConfigurableInformation _ci = CITools.getByName(getInputs(),"relations");
		if(_ci!=null){
			return _ci.getValue();
		}
		
		return null;
	}
	public void setRelations(String relations) {
		ConfigurableInformation _ci = CITools.getByName(getInputs(),"relations");
		if(_ci!=null)
		{
			_ci.setValue(relations);
		}
	}
	
	public ConfigurableInformation getCiByIndex(int index)
	{
		if(index<getInputs().size()){
			return getInputs().get(index);
		}
		return null;
	}
	
	
	/***
	 * 
	 */
	
	public String getOrderStatement() {
		ConfigurableInformation _ci = getInputs().get(23);
		if(_ci!=null)
		{
			return _ci.getValue();
		}
		return "";
		
	}
	public void setOrderStatement(String groupSelectStatement) {
		ConfigurableInformation _ci = getInputs().get(23);
		if(_ci!=null)
		{
			_ci.setValue(groupSelectStatement);
		}
	}
	
	/***
	 * 
	 * @return
	 */
	public String getLimitStatement() {
		ConfigurableInformation _ci = getInputs().get(24);
		if(_ci!=null)
		{
			return _ci.getValue();
		}
		return "";
		
	}
	public void setLimitSelectStatement(String groupSelectStatement) {
		ConfigurableInformation _ci = getInputs().get(24);
		if(_ci!=null)
		{
			_ci.setValue(groupSelectStatement);
		}
	}
	
	/***
	 * 
	 * @return
	 */
	public String getFriendlyName() {
		ConfigurableInformation _ci = CITools.getByName(getInputs(),"friendlyName");
		if(_ci!=null){
			return _ci.getValue();
		}
		return "";
		
	}
	public void setFriendlyName(String name) {
		ConfigurableInformation _ci = CITools.getByName(getInputs(),"friendlyName");
		if(_ci!=null)
		{
			_ci.setValue(name);
		}
	}
	

}