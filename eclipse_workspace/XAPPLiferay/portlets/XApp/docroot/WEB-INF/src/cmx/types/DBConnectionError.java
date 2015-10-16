package cmx.types;

public class DBConnectionError {
	
	public int type=0;
	public String msg="ok";
	public String msgInternal="";
	public String identifier="";
	public String uid="";
	
	public DataSourceBase dataSource;
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return the msgInternal
	 */
	public String getMsgInternal() {
		return msgInternal;
	}
	/**
	 * @param msgInternal the msgInternal to set
	 */
	public void setMsgInternal(String msgInternal) {
		this.msgInternal = msgInternal;
	}
	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the dataSource
	 */
	public DataSourceBase getDataSource() {
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSourceBase dataSource) {
		this.dataSource = dataSource;
	}
	
}
