package pmedia.types;


public class PMPlatformRenderConfiguration 
{
	public String platform=Constants.USERAGENT_PC;
	public int listItemHeight=70;
	public int listItemIconHeight=60;
	
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return the listItemHeight
	 */
	public int getListItemHeight() {
		return listItemHeight;
	}
	/**
	 * @param listItemHeight the listItemHeight to set
	 */
	public void setListItemHeight(int listItemHeight) {
		this.listItemHeight = listItemHeight;
	}
	/**
	 * @return the listItemIconHeight
	 */
	public int getListItemIconHeight() {
		return listItemIconHeight;
	}
	/**
	 * @param listItemIconHeight the listItemIconHeight to set
	 */
	public void setListItemIconHeight(int listItemIconHeight) {
		this.listItemIconHeight = listItemIconHeight;
	}
	
}
