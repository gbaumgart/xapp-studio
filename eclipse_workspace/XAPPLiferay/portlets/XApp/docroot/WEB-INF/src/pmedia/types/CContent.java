package pmedia.types;

import java.util.Date;


/**
 * Base class to provide cleaned up html content through servlets to clients.  
 * @author mc007
 *
 */
public class CContent extends BaseData
{
	
	public String fileRef;
	public String sku;
	public String price;
	public String currency;
	public String dimensions;
	public String weight;
	public String getPrice() 
	{
		return price;
	}
	public void setPrice(String price) 
	{
		this.price = price;
	}
	public String getCurrency() 
	{
		return currency;
	}
	public void setCurrency(String currency) 
	{
		this.currency = currency;
	}
	public String getDimensions() {
		return dimensions;
	}
	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	/**
	 * For events
	 */
	public  Date startDate;
	
	/**
	 * For events
	 */
	public Date endDate;
	
	
	/**
	 * Tracks the original data class
	 */
	private Date creationDate;
	
	/**
	 * Tracks the original data class
	 */
	private String dataClass;
	
	/**
	 * Tracks the data source
	 */
	
	private String dataSourceUID;
	/***
	 * Tracks the platform being requested for
	 */
	private String platform;
	
	/***
	 * Base Ref
	 */
	private String baseRef;

	
	/**
	 * Tracks user UUID
	 * @return
	 */
	private String uuid;
	
	/**
	 * Tracks user Application Id
	 * @return
	 */
	private String applicationId;
	
	
	
	
	public String getDataSourceUID() {
		return dataSourceUID;
	}

	public void setDataSourceUID(String dataSourceUID) {
		this.dataSourceUID = dataSourceUID;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getBaseRef() {
		return baseRef;
	}

	public void setBaseRef(String baseRef) {
		this.baseRef = baseRef;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getDataClass() {
		return dataClass;
	}

	public void setDataClass(String dataClass) {
		this.dataClass = dataClass;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	public String getFileRef() {
		return fileRef;
	}
	public void setFileRef(String fileRef) {
		this.fileRef = fileRef;
	}
	
	
	
}
