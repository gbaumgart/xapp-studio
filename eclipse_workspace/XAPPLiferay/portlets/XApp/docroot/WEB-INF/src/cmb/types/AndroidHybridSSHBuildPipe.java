package cmb.types;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

public class AndroidHybridSSHBuildPipe extends SSHCommandPipe 
{
	
	private BuildManager delegate=null;
	public Boolean excecute(String command) throws IOException, JSchException{
		
	      Boolean result = true;
	      
	      Channel channel=session.openChannel("exec");
	      
	      String excuteDir=System.getProperty("ANDROID_HYBRID_BUILD_ROOT");
	      String finalCommand = excuteDir+command;
	      
	      
	      finalCommand+=" " + getApplicationId() ;//1
	      finalCommand+=" " + getUuid();//2
	      
	      String bundleId = getBundleId();
	      if(bundleId==null || bundleId.length() ==0)
	      {
	    	  bundleId ="com.pm."+getApplicationId().toLowerCase();
	      }
	      
	      finalCommand+=" " + bundleId;
	      finalCommand+=" " + "\"" +getApplicationTitle() + "\"";//4
	      
	      finalCommand+=" " + "\"" + excuteDir + "\"";//5
	      
	      finalCommand+=" " + "\"" + rtConfig + "\"";//6
	      
	      System.out.println("excecuting : " + finalCommand);
	      
	      ((ChannelExec)channel).setCommand(finalCommand);
	      
	      
	      //System.out.println("excecuting : " + finalCommand);

	      channel.setInputStream(null);
	      //((ChannelExec)channel).setErrStream(System.err);
	      InputStream in=channel.getInputStream();

	      channel.connect();

	      byte[] tmp=new byte[1024];
	      while(true){
	        while(in.available()>0){
	          int i=in.read(tmp, 0, 1024);
	          if(i<0)
	        	  break;
	          
	          System.out.print(new String(tmp, 0, i));
	          if(getDelegate()!=null){
	        	  getDelegate().onPipeMessage(this, new String(tmp, 0, i));
	          }
	        }
	        if(channel.isClosed())
	        {
	          System.out.println("exit-status: "+channel.getExitStatus());
	          break;
	        }
	        try{Thread.sleep(1000);}catch(Exception ee){}
	      }
	      channel.disconnect();
	      session.disconnect();
	    
	    

		return result;
	}
	
	
	private String applicationId;
	
	private String bundleId;
	
	private String applicationTitle;
	
	private String uuid;
	private String platform;
	private String type;
	
	
	
	
	private String buildStatus=null;
	private String buildStatusMessage=null;
	private float buildStatusProgress=0.0f;
	
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getApplicationTitle() {
		return applicationTitle;
	}
	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}
	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	public BuildManager getDelegate() {
		return delegate;
	}
	public void setDelegate(BuildManager delegate) {
		this.delegate = delegate;
	}
	
	
}
