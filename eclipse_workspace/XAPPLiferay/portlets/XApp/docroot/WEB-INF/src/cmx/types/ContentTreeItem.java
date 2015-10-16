package cmx.types;

import java.util.ArrayList;



import pmedia.utils.CITools;

public class ContentTreeItem{

	public String id;
	public String name;
	public String label;
	public String type;
	public String dataSourceUID;
	public String getDataSourceUID() {
		return dataSourceUID;
	}
	public void setDataSourceUID(String dataSourceUID) {
		this.dataSourceUID = dataSourceUID;
	}
	public String contentType;
	public String xcGroup;
	
	

	public String getXcGroup() {
		return xcGroup;
	}
	public void setXcGroup(String xcGroup) {
		this.xcGroup = xcGroup;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String section;
	
	public int uid;
	public String uidStr;
	public String getUidStr() {
		return uidStr;
	}
	public void setUidStr(String uidStr) {
		this.uidStr = uidStr;
	}
	public ArrayList<Reference>children;
	
	public Boolean hasReference(Reference ref)
	{
		if(children!=null){
			
			for(int i = 0  ;  i < children.size() ; i ++){
				Reference _ref = children.get(i);
				if(_ref._reference.equals(ref._reference))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	public void addChild(Reference ref){
		if(children==null)
			children = new ArrayList<Reference>();

		if(!hasReference(ref))
		{
			children.add(ref);
		}
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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the children
	 */
	public ArrayList<Reference> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<Reference> children) {
		this.children = children;
	}
	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}
	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}
	
		
}
