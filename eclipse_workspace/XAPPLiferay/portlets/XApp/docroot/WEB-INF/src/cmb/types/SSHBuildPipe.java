package cmb.types;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

public class SSHBuildPipe extends SSHCommandPipe 
{
	
	private BuildManager delegate=null;
	
	public Boolean excecute(String command) throws IOException, JSchException{
		
	      Boolean result = true;
	      
	      Channel channel=session.openChannel("exec");
	      
	      String excuteDir=System.getProperty("OSX_BUILD_CMIOS_REMOTE_DIR");
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	public BuildManager getDelegate() {
		return delegate;
	}
	public void setDelegate(BuildManager delegate) {
		this.delegate = delegate;
	}
	

	
	
}
