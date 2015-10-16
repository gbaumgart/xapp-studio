package pmedia.types;

public class PictureTransformOptions {

	public Boolean addShadow=false;
	public String JSClickFunction=null;
	public String resizeWidth=null;
	public String resizeHeight=null;
	
	
	public static PictureTransformOptions defaultOptions()
	{
		PictureTransformOptions result = new PictureTransformOptions();
		result.addShadow=true;
		return result;
	}
}
