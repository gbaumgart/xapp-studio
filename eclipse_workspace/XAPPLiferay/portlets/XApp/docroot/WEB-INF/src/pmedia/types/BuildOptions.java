package pmedia.types;

public class BuildOptions 
{
	public int type=0;
	public String dataHost;
	public String runTimeConfiguration;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDataHost() {
		return dataHost;
	}
	public void setDataHost(String dataHost) {
		this.dataHost = dataHost;
	}
	public String getRunTimeConfiguration() {
		return runTimeConfiguration;
	}
	public void setRunTimeConfiguration(String runTimeConfiguration) {
		this.runTimeConfiguration = runTimeConfiguration;
	}
	
}
