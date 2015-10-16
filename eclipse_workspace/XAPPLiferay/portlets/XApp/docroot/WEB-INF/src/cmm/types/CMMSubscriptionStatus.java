package cmm.types;


public class CMMSubscriptionStatus 
{

	
	private String checkoutStatus="REQUESTED";
	public String appId;
	
	private String referenceNumber;
	public String getCheckoutStatus() {
		return checkoutStatus;
	}
	public void setCheckoutStatus(String checkoutStatus) {
		this.checkoutStatus = checkoutStatus;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	private String uuid;	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	private String customerEMail;
	public String getCustomerEMail() {
		return customerEMail;
	}
	public void setCustomerEMail(String customerEMail) {
		this.customerEMail = customerEMail;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

	
}
