package cmx.types;

import java.io.BufferedReader;

public class ScriptSession {

	public String appId;
	public String uuid;
	public String editorUrl;
	public String editorPort;
	public String cxAppFolder;
	public String workspace;
	public Process process; 
	public BufferedReader buf=null;
	public Cloud9Thread thread;
	public String sessionKey;
	public String sessionID;
	public String referer;
	
	public int status=0;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getEditorUrl() {
		return editorUrl;
	}
	public void setEditorUrl(String editorUrl) {
		this.editorUrl = editorUrl;
	}
	public String getEditorPort() {
		return editorPort;
	}
	public void setEditorPort(String editorPort) {
		this.editorPort = editorPort;
	}
	public String getCxAppFolder() {
		return cxAppFolder;
	}
	public void setCxAppFolder(String cxAppFolder) {
		this.cxAppFolder = cxAppFolder;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public Process getProcess() {
		return process;
	}
	public void setProcess(Process process) {
		this.process = process;
	}
	public BufferedReader getBuf() {
		return buf;
	}
	public void setBuf(BufferedReader buf) {
		this.buf = buf;
	}
	public Cloud9Thread getThread() {
		return thread;
	}
	public void setThread(Cloud9Thread thread) {
		this.thread = thread;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
}
