package pmedia.types;

import java.util.Date;

public class CEventItem extends CListItem{
	
	public Date startDate;
	public Date endDate;
	
	
	public String startDateString;
	public String endDateString;
	public String venueString;
	public String venueRefIdString;
	public String categoryString;
	
	public String getStartDateString() {
		return startDateString;
	}
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	public String getEndDateString() {
		return endDateString;
	}
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}
	public String getVenueString() {
		return venueString;
	}
	public void setVenueString(String venueString) {
		this.venueString = venueString;
	}
	public String getVenueRefIdString() {
		return venueRefIdString;
	}
	public void setVenueRefIdString(String venueRefIdString) {
		this.venueRefIdString = venueRefIdString;
	}
	public String getCategoryString() {
		return categoryString;
	}
	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
