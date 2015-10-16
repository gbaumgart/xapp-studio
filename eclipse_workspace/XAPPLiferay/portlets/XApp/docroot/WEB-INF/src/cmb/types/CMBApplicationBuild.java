package cmb.types;

import java.util.Date;

public class CMBApplicationBuild {

	private String uuid;
	private String applicationId;
	
	private String buildStatus=null;
	private String buildStatusMessage=null;
	private float buildStatusProgress=0.0f;
	
	private Date startTimestamp;
	private String platform;
	private String type;
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
	

	public Date getStartTimestamp() {
		return startTimestamp;
	}
	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBuildStatus() {
		return buildStatus;
	}
	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}
	public String getBuildStatusMessage() {
		return buildStatusMessage;
	}
	public void setBuildStatusMessage(String buildStatusMessage) {
		this.buildStatusMessage = buildStatusMessage;
	}
	public float getBuildStatusProgress() {
		return buildStatusProgress;
	}
	public void setBuildStatusProgress(float buildStatusProgress) {
		this.buildStatusProgress = buildStatusProgress;
	}
	
	
}
