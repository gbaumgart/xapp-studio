package pmedia.types;

import java.util.Date;

import pmedia.utils.TimeUtils;

/**
 * 
 * @author admin
 *
 */
public class PagingInfo 
{
	public static PagingInfo getDefaultListPagingSmartphone()
	{
		PagingInfo res = new PagingInfo();
		res.start = "0";
		res.max = "15";
		
		return res;
	}
	public String start=null;
	public String end=null;
	public String max=null;
	public String getStart() 
	{
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}

	public int getMaxAsInt(){
		
		Integer _max = -1;
		if(this.max!=null){
			try {
				_max = Integer.parseInt(this.max);
			} catch (Exception e) {

			}
		}
		return _max;
	}
	
	public int getEndAsInt()
	{
		Integer _end = -1;
		if(this.end!=null){
			try {
				_end = Integer.parseInt(this.end);
			} catch (Exception e) {

			}
		}
		return _end;
	}
	public int getStartAsInt()
	{
		Integer _start = 0;
		if(this.start!=null){
			try {
				_start= Integer.parseInt(this.start);
			} catch (Exception e) {

			}
		}
		return _start;
	}
	public Date getStartAsDate()
	{
		Date startDate = new Date();
    	if(this.start!=null && this.start.length() > 0)
    	{
    		startDate=TimeUtils.fromDojoSpinWheel(this.start);
    	}
		return startDate;
	}
	public Date getEndAsDate()
	{
		Date endDate = new Date();
    	if(this.end!=null && this.end.length() > 0)
    	{
    		endDate=TimeUtils.fromDojoSpinWheel(this.end);
    	}
		return endDate;
	}
	
	public Date getMaxAsDate()
	{
		Date maxDate = new Date();
    	if(this.max!=null && this.max.length() > 0)
    	{
    		maxDate=TimeUtils.fromDojoSpinWheel(this.max);
    	}
		return maxDate;
	}
}
