package pmedia.types;

public class CIOverride 
{
	
	public String value;
	
	int id =-1;
	int flags=-1;
	int pageId=-1;
	int dstId=-1;
	int contentRefId=-1;
	String sourceType="";
	
	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String dataRef;
	public String dataSource;
	
	
	
	public String title;
	
	public String dstIdStr;
	public String refGroupStr;
	
	
	
	
	//backwards compatible : Style Override ! 
	public String contentRefStr;
	//public String dataSourceUid;
	//////
	
	public int getId() {return id;}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) 
	{
		this.pageId = pageId;
	}
	public int getDstId() {
		return dstId;
	}
	public void setDstId(int dstId) 
	{
		this.dstId = dstId;
	}
	public int getContentRefId() {
		return contentRefId;
	}
	public void setContentRefId(int contentRefId) {
		this.contentRefId = contentRefId;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDstIdStr() {
		return dstIdStr;
	}
	public void setDstIdStr(String dstIdStr) {
		this.dstIdStr = dstIdStr;
	}
	public String getRefGroupStr() {
		return refGroupStr;
	}
	public void setRefGroupStr(String refGroupStr) {
		this.refGroupStr = refGroupStr;
	}
	public String getDataRef() {
		return dataRef;
	}
	public void setDataRef(String dataRef) {
		this.dataRef = dataRef;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContentRefStr() {
		return contentRefStr;
	}
	public void setContentRefStr(String contentRefStr) {
		this.contentRefStr = contentRefStr;
	}
	/*
	public String getDataSourceUid() {
		return dataSourceUid;
	}
	public void setDataSourceUid(String dataSourceUid) {
		this.dataSourceUid = dataSourceUid;
	}
	*/
	
	
}
