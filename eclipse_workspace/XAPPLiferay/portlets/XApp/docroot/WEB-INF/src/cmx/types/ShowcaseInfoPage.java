package cmx.types;

public class ShowcaseInfoPage {

	public String title;
	public String description;
	public String picture;
	
	private String pictureHeight;
	public String getPictureHeight() {
		return pictureHeight;
	}
	public void setPictureHeight(String pictureHeight) {
		this.pictureHeight = pictureHeight;
	}
	public String getPictureWidth() {
		return pictureWidth;
	}
	public void setPictureWidth(String pictureWidth) {
		this.pictureWidth = pictureWidth;
	}
	public String getPictureMode() {
		return pictureMode;
	}
	public void setPictureMode(String pictureMode) {
		this.pictureMode = pictureMode;
	}
	public boolean isShowText() {
		return showText;
	}
	public void setShowText(boolean showText) {
		this.showText = showText;
	}
	private String pictureWidth;
	private String textWidth;
	public String getTextWidth() {
		return textWidth;
	}
	public void setTextWidth(String textWidth) {
		this.textWidth = textWidth;
	}
	private String pictureMode;
	private boolean showText; 
	
	public String edgeFile;
	
	public String webLocationPicture;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getWebLocationPicture() {
		return webLocationPicture;
	}
	public void setWebLocationPicture(String webLocationPicture) {
		this.webLocationPicture = webLocationPicture;
	}
	public String getEdgeFile() {
		return edgeFile;
	}
	public void setEdgeFile(String edgeFile) {
		this.edgeFile = edgeFile;
	}
}
