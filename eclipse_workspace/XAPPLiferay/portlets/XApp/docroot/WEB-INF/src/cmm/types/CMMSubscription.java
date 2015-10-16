package cmm.types;

import java.util.Date;


public class CMMSubscription {

	
	private String couponCode;
	private Date couponCodeExpireDate=new Date();
	private float couponCodeDiscount;
	
	private boolean enabled=true;
	private String templatePath;
	private String moreLink;
	
	private String orderLanguage;
	private String orderStatus;
	private String ipnDate;
	private String referenceNumber;
	private String ipnLicenseReference;
	private String ipnProductCode;
	private String ipnLicenseExpiration;
	private String ipnPrice;
	private String licenseType;
	
	private String ipAddress;
	
	public int numberOfApplications;
	
	public String platform;
	public String description;
	public Boolean free;
	
	private Date salesDate;
	private Date expirationDate;
	private Boolean expired=false;
	
	private Boolean isFree=false;
	
	
	private String ipnName;
	private String ipnPayMethod;
	int groupId=-1;
	private Boolean isTrial;
	private Boolean isMonthly=false;
	private Boolean isAnnual=false;
	private String ipnAnnualRef;
	
	private String yourSavingsText;
	private String buyLink;
	private String hash;
	private String appId;
	
	public String publishStatus;
	
	public String getOrderLanguage() {
		return orderLanguage;
	}
	public void setOrderLanguage(String orderLanguage) {
		this.orderLanguage = orderLanguage;
	}
	
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	private String referrer;
	public String getReferrer() {
		return referrer;
	}
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	private int checkoutStatus=-1;	
	public int getCheckoutStatus() {
		return checkoutStatus;
	}
	public void setCheckoutStatus(int checkoutStatus) {
		this.checkoutStatus = checkoutStatus;
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
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getIpnDate() {
		return ipnDate;
	}
	public void setIpnDate(String ipnDate) {
		this.ipnDate = ipnDate;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getIpnLicenseReference() {
		return ipnLicenseReference;
	}
	public void setIpnLicenseReference(String ipnLicenseReference) {
		this.ipnLicenseReference = ipnLicenseReference;
	}
	public String getIpnProductCode() {
		return ipnProductCode;
	}
	public void setIpnProductCode(String ipnProductCode) {
		this.ipnProductCode = ipnProductCode;
	}
	public String getIpnLicenseExpiration() {
		return ipnLicenseExpiration;
	}
	public void setIpnLicenseExpiration(String ipnLicenseExpiration) {
		this.ipnLicenseExpiration = ipnLicenseExpiration;
	}
	public String getIpnPrice() {
		return ipnPrice;
	}
	public void setIpnPrice(String ipnPrice) {
		this.ipnPrice = ipnPrice;
	}
	public String getIpnName() {
		return ipnName;
	}
	public void setIpnName(String ipnName) {
		this.ipnName = ipnName;
	}
	public String getIpnPayMethod() {
		return ipnPayMethod;
	}
	public void setIpnPayMethod(String ipnPayMethod) {
		this.ipnPayMethod = ipnPayMethod;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public String getYourSavingsText() {
		return yourSavingsText;
	}
	public void setYourSavingsText(String yourSavingsText) {
		this.yourSavingsText = yourSavingsText;
	}
	public float getRawPrice() {
		return rawPrice;
	}
	public void setRawPrice(float rawPrice) {
		this.rawPrice = rawPrice;
	}
	private float rawPrice ;
	
	public Boolean getIsMonthly() {
		return isMonthly;
	}
	public void setIsMonthly(Boolean isMonthly) {
		this.isMonthly = isMonthly;
	}
	public Boolean getIsAnnual() {
		return isAnnual;
	}
	public void setIsAnnual(Boolean isAnnual) {
		this.isAnnual = isAnnual;
	}
	public String getIpnAnnualRef() {
		return ipnAnnualRef;
	}
	public void setIpnAnnualRef(String ipnAnnualRef) {
		this.ipnAnnualRef = ipnAnnualRef;
	}
	
	
	
	public Boolean getIsTrial() {
		return isTrial;
	}
	public void setIsTrial(Boolean isTrial) {
		this.isTrial = isTrial;
	}
	
	private String trialLink;
	public String getTrialLink() {
		return trialLink;
	}
	public void setTrialLink(String trialLink) {
		this.trialLink = trialLink;
	}
	public String getBuyLink() {
		return buyLink;
	}
	public void setBuyLink(String buyLink) {
		this.buyLink = buyLink;
	}
	public Date getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Boolean getExpired() {
		return expired;
	}
	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
	public Boolean getIsFree() {
		return isFree;
	}
	public void setIsFree(Boolean isFree) {
		this.isFree = isFree;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getFree() {
		return free;
	}
	public void setFree(Boolean free) {
		this.free = free;
	}
	public int getNumberOfApplications() {
		return numberOfApplications;
	}
	public void setNumberOfApplications(int numberOfApplications) {
		this.numberOfApplications = numberOfApplications;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getTemplatePath() {
		return templatePath;
	}
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	public String getMoreLink() {
		return moreLink;
	}
	public void setMoreLink(String moreLink) {
		this.moreLink = moreLink;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	
	public float getCouponCodeDiscount() {
		return couponCodeDiscount;
	}
	public void setCouponCodeDiscount(float couponCodeDiscount) {
		this.couponCodeDiscount = couponCodeDiscount;
	}
	public Date getCouponCodeExpireDate() {
		return couponCodeExpireDate;
	}
	public void setCouponCodeExpireDate(Date couponCodeExpireDate) {
		this.couponCodeExpireDate = couponCodeExpireDate;
	}
	public String getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
	
}
