package pmedia.types;

import java.util.ArrayList;

import cmx.types.ECMContentSourceType;
public class CList
{
	
	public ArrayList items=new ArrayList();
	
	public CListItem getItemByRefId(int refId)
	{
		CListItem result = null;
		for (int i = 0; i < items.size(); i++) 
		{
			CListItem item = (CListItem) items.get(i);
			if(item.refId==refId)
			{
				return item;
			}
		}
		return null;
	}
	
	public String baseRef;
	/**
	 * new CM Attributes : 
	 */
	public int refId;
	
	public String refIdString;
	
	
	public String picture;
	public ECMContentSourceType type = ECMContentSourceType.Unknown ;
	
	public String dataClass;
	/***
	 * Original 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public String title;

	public boolean published;
	public String iconUrl;
	public String sourceType="Database";
	public String introText;
	
	



	/**
	 * @return the refId
	 */
	public int getRefId() {
		return refId;
	}
	/**
	 * @param refId the refId to set
	 */
	public void setRefId(int refId) {
		this.refId = refId;
	}
	
	
	/**
	 * @return the type
	 */
	public ECMContentSourceType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ECMContentSourceType type) {
		this.type = type;
	}
	
	/**
	 * @return the introText
	 */
	public String getIntroText() {
		return introText;
	}
	/**
	 * @param introText the introText to set
	 */
	public void setIntroText(String introText) {
		this.introText = introText;
	}
	/**
	 * @return the groupId
	 */


	/**
	 * @return the dataClass
	 */
	public String getDataClass() {
		return dataClass;
	}
	/**
	 * @param dataClass the dataClass to set
	 */
	public void setDataClass(String dataClass) {
		this.dataClass = dataClass;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}
	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	/**
	 * @return the items
	 */
	public ArrayList<CListItem> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(ArrayList items) {
		this.items = items;
	}
	public String getBaseRef() {
		return baseRef;
	}
	public void setBaseRef(String baseRef) {
		this.baseRef = baseRef;
	}
	public String getRefIdString() {
		return refIdString;
	}
	public void setRefIdString(String refIdString) {
		this.refIdString = refIdString;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String cmdArg) {
		this.title = cmdArg;
	}
}
