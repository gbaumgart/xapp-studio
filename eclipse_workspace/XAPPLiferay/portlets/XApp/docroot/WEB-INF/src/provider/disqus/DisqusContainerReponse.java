
package provider.disqus;

import java.util.ArrayList;
import java.util.Date;

public class DisqusContainerReponse{
   	
	private Date timeStamp;
	private int code;
   	private Cursor cursor;
   	private ArrayList response;

 	public Number getCode(){
		return this.code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
 	public Cursor getCursor(){
		return this.cursor;
	}
	public void setCursor(Cursor cursor){
		this.cursor = cursor;
	}
 	public ArrayList getResponse()
 	{
		return this.response;
	}
	public void setResponse(ArrayList response){
		this.response = response;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
