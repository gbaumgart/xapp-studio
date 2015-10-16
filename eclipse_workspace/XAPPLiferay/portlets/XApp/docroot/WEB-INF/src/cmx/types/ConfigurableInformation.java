package cmx.types;

import java.util.ArrayList;

public class ConfigurableInformation {

	String id="noID";
	public int type;
	public String name;
	public String title="";
	public int group=-1;
	public String platform;
	
	public int order=-1;
	public int getOrder() 
	{
		return order;
	}
	public void setOrder(int order) 
	{
		this.order = order;
	}
	
	public String description;
	public String value;
	public int chainType=0;
	public int uid=-1;
	public String parentId="-1";
	
	public String storeDestination;
	
	public String getStoreDestination() {
		return storeDestination;
	}
	public void setStoreDestination(String storeDestination) {
		this.storeDestination = storeDestination;
	}
	public String dataSource;
	public String dataRef;
	public Boolean visible=true;
	public int enumType;
	public boolean enabled=true;
	public int flags=-1;
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	public ArrayList<ConfigurableInformation>params;
	
	
	
	
	public void addParam(ConfigurableInformation param){
		
		if(params==null)
		{
			params=new ArrayList<ConfigurableInformation>();
			
		}
		params.add(param);
	}
	public ConfigurableInformation getParamByName(String name)
	{
		if(params==null)
			return null;
		ArrayList<ConfigurableInformation>dstChain = params;
		if(dstChain!=null)
		{
				for(int i = 0  ; i  <  dstChain.size() ; i++)	{
					ConfigurableInformation  ci = dstChain.get(i) ;
					if(ci.getName().equals(name))
					{
						return ci;
					}
				}
		}
		return null;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the enumType
	 */
	public int getEnumType() {
		return enumType;
	}
	/**
	 * @param enumType the enumType to set
	 */
	public void setEnumType(int enumType) {
		this.enumType = enumType;
	}
	/**
	 * @return the chainType
	 */
	public int getChainType() {
		return chainType;
	}
	/**
	 * @param chainType the chainType to set
	 */
	public void setChainType(int chainType) {
		this.chainType = chainType;
	}
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) 
	{
		this.parentId = parentId;
	}
	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * @return the dataRef
	 */
	public String getDataRef() {
		return dataRef;
	}
	/**
	 * @param dataRef the dataRef to set
	 */
	public void setDataRef(String dataRef) {
		this.dataRef = dataRef;
	}
	/**
	 * @return the enabled
	 */
	public boolean getEnabled() 
	{
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the visible
	 */
	public Boolean getVisible() {
		return visible;
	}
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the group
	 */
	public int getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(int group) {
		this.group = group;
	}
	/**
	 * @return the params
	 */
	public ArrayList<ConfigurableInformation> getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(ArrayList<ConfigurableInformation> params) {
		this.params = params;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
}
