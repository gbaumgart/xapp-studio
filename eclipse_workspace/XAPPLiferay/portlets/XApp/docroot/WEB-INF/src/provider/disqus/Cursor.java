
package provider.disqus;
public class Cursor{
   	private boolean hasNext;
   	private boolean hasPrev;
   	private String id;
   	private boolean more;
   	private String next;
   	private String prev;
   	private String total;

 	public boolean getHasNext(){
		return this.hasNext;
	}
	public void setHasNext(boolean hasNext){
		this.hasNext = hasNext;
	}
 	public boolean getHasPrev(){
		return this.hasPrev;
	}
	public void setHasPrev(boolean hasPrev){
		this.hasPrev = hasPrev;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public boolean getMore(){
		return this.more;
	}
	public void setMore(boolean more){
		this.more = more;
	}
 	public String getNext(){
		return this.next;
	}
	public void setNext(String next){
		this.next = next;
	}
 	public String getPrev(){
		return this.prev;
	}
	public void setPrev(String prev){
		this.prev = prev;
	}
 	public String getTotal(){
		return this.total;
	}
	public void setTotal(String total){
		this.total = total;
	}
}
