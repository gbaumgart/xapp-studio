package pmedia.types;

public class MediaItemBase 
{
	private String title;
	
	private String fullSizeLocation;
	private String thumbLocation;
	
	private MediaType type;
	private int flags;
	private String width;
	private String height;
	
	
	
	
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
	 * @return the fullSizeLocation
	 */
	public String getFullSizeLocation() {
		return fullSizeLocation;
	}
	/**
	 * @param fullSizeLocation the fullSizeLocation to set
	 */
	public void setFullSizeLocation(String fullSizeLocation) {
		this.fullSizeLocation = fullSizeLocation;
	}
	/**
	 * @return the thumbLocation
	 */
	public String getThumbLocation() {
		return thumbLocation;
	}
	/**
	 * @param thumbLocation the thumbLocation to set
	 */
	public void setThumbLocation(String thumbLocation) {
		this.thumbLocation = thumbLocation;
	}
	
	
	/**
	 * @return the flags
	 */
	public int getFlags() {
		return flags;
	}
	/**
	 * @param flags the flags to set
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}
	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}
	/**
	 * @return the type
	 */
	public MediaType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(MediaType type) {
		this.type = type;
	}
	
}
