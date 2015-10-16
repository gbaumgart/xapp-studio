package cmb.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHCommandPipe {
	
	
	private String applicationId;
	
	private String bundleId;
	
	private String applicationTitle;
	
	private String uuid;
	private String platform;
	private String type;
	
	private String buildStatus=null;
	private String buildStatusMessage=null;
	
	public String rtConfig;
	
	private float buildStatusProgress=0.0f;
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
	/***
	 * SSH Internals : 
	 */
	
	JSch jsch;
	Session session;
	
	Channel channel;
	
	static int checkAck(InputStream in) throws IOException{
	    int b=in.read();
	    // b may be 0 for success,
	    //          1 for error,
	    //          2 for fatal error,
	    //          -1
	    if(b==0) return b;
	    if(b==-1) return b;

	    if(b==1 || b==2){
	      StringBuffer sb=new StringBuffer();
	      int c;
	      do {
		c=in.read();
		sb.append((char)c);
	      }
	      while(c!='\n');
	      if(b==1){ // error
		System.out.print(sb.toString());
	      }
	      if(b==2){ // fatal error
		System.out.print(sb.toString());
	      }
	    }
	    return b;
	  }
	public Boolean downloadFromHost(String localFile,String remoteFile)
	{
		Boolean result = true;
		FileInputStream fis=null;
	    try{

	      String lfile=remoteFile;
	      
	      String prefix=null;
	      
	      
	      String rfile=localFile;

	      boolean ptimestamp = true;

	      // exec 'scp -t rfile' remotely
	      String command="scp -f "+rfile;
	      //Channel channel=session.openChannel("exec");
	      ((ChannelExec)channel).setCommand(command);

	      // get I/O streams for remote scp
	      OutputStream out=channel.getOutputStream();
	      InputStream in=channel.getInputStream();

	      channel.connect();
	      FileOutputStream fos=null;

	      byte[] buf=new byte[1024];

	      // send '\0'
	      buf[0]=0; out.write(buf, 0, 1); out.flush();

	      while(true){
	int c=checkAck(in);
	        if(c!='C'){
	break;
	}

	        // read '0644 '
	        in.read(buf, 0, 5);

	        long filesize=0L;
	        while(true){
	          if(in.read(buf, 0, 1)<0){
	            // error
	            break;
	          }
	          if(buf[0]==' ')break;
	          filesize=filesize*10L+(long)(buf[0]-'0');
	        }

	        String file=null;
	        for(int i=0;;i++){
	          in.read(buf, i, 1);
	          if(buf[i]==(byte)0x0a){
	            file=new String(buf, 0, i);
	            break;
	   }
	        }

	//System.out.println("filesize="+filesize+", file="+file);

	        // send '\0'
	        buf[0]=0; out.write(buf, 0, 1); out.flush();

	        // read a content of lfile
	        fos=new FileOutputStream(prefix==null ? lfile : prefix+file);
	        int foo;
	        while(true){
	          if(buf.length<filesize) foo=buf.length;
	else foo=(int)filesize;
	          foo=in.read(buf, 0, foo);
	          if(foo<0){
	            // error
	            break;
	          }
	          fos.write(buf, 0, foo);
	          filesize-=foo;
	          if(filesize==0L) break;
	        }
	        fos.close();
	        fos=null;

	if(checkAck(in)!=0){
	System.exit(0);
	}

	        // send '\0'
	        buf[0]=0; out.write(buf, 0, 1); out.flush();
	      }



	      channel.disconnect();
	      session.disconnect();
	      return true;
	    }
	    catch(Exception e){
	      System.out.println(e);
	      try{if(fis!=null)fis.close();}catch(Exception ee){}
	    }
		
		
		return true;
	}
	public Boolean copyToBuildHost(String localFile,String remoteFile)
	{
		Boolean result = true;
		
		FileInputStream fis=null;
	    try{

	      String lfile=localFile;
	      
	      
	      String rfile=remoteFile;

	      boolean ptimestamp = true;

	      // exec 'scp -t rfile' remotely
	      String command="scp " + (ptimestamp ? "-p" :"") +" -t "+rfile;
	      //Channel channel=session.openChannel("exec");
	      ((ChannelExec)channel).setCommand(command);

	      // get I/O streams for remote scp
	      OutputStream out=channel.getOutputStream();
	      InputStream in=channel.getInputStream();

	      channel.connect();

	      if(checkAck(in)!=0){
	    	  //System.exit(0);
	    	  
	    	  return false;
	      }

	      File _lfile = new File(lfile);

	      if(ptimestamp){
	        command="T "+(_lfile.lastModified()/1000)+" 0";
	        // The access time should be sent here,
	        // but it is not accessible with JavaAPI ;-<
	        command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
	        out.write(command.getBytes()); out.flush();
	        if(checkAck(in)!=0){
	        		//System.exit(0);
	        }
	      }

	      // send "C0644 filesize filename", where filename should not include '/'
	      long filesize=_lfile.length();
	      command="C0644 "+filesize+" ";
	      if(lfile.lastIndexOf('/')>0){
	        command+=lfile.substring(lfile.lastIndexOf('/')+1);
	      }
	      else{
	        command+=lfile;
	      }
	      command+="\n";
	      out.write(command.getBytes()); out.flush();
	      if(checkAck(in)!=0){
	    	  return false;
	      }

	      // send a content of lfile
	      fis=new FileInputStream(lfile);
	      byte[] buf=new byte[1024];
	      while(true){
	        int len=fis.read(buf, 0, buf.length);
		if(len<=0) break;
	        out.write(buf, 0, len); //out.flush();
	      }
	      fis.close();
	      fis=null;
	      // send '\0'
	      buf[0]=0; out.write(buf, 0, 1); out.flush();
	      if(checkAck(in)!=0){
	    	  return false;
	      }
	      out.close();

	      channel.disconnect();
	      session.disconnect();
	      return true;
	    }
	    catch(Exception e){
	      System.out.println(e);
	      try{if(fis!=null)fis.close();}catch(Exception ee){}
	    }
		
		
		return true;
	}
	
	public Boolean initSession(String _user,String _pass,String _host, int _port) throws JSchException
	{
		System.out.println("loggin in to " + _host + " with user : " + _user+ " and passwd : " + _pass + " and port : " +_port );
		
		JSch jsch = new JSch();
		String host = _host;
		String password = _pass;
		int port = _port;
		String username = _user;
		session=jsch.getSession(username, host, port);
		//session.setX11Host(host);
		//int xport=0;
		//session.setX11Port(xport+6000);
		session.setPassword(password);
		try {
			session.connect();	
		} catch (Exception e) {
				e.printStackTrace();
		}
		try {
			channel=session.openChannel("exec");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    
		return true;
	}
	
	
	private String username;
	private String host;
	private String password;
	private int port;
	private String fileToCopyFirstToHost;
	private String fileToCopyFirstToHostDestination;
	private String pipeUID;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getFileToCopyFirstToHost() {
		return fileToCopyFirstToHost;
	}

	public void setFileToCopyFirstToHost(String fileToCopyFirstToHost) {
		this.fileToCopyFirstToHost = fileToCopyFirstToHost;
	}

	public String getFileToCopyFirstToHostDestination() {
		return fileToCopyFirstToHostDestination;
	}

	public void setFileToCopyFirstToHostDestination(
			String fileToCopyFirstToHostDestination) {
		this.fileToCopyFirstToHostDestination = fileToCopyFirstToHostDestination;
	}

	public String getPipeUID() {
		return pipeUID;
	}

	public void setPipeUID(String pipeUID) {
		this.pipeUID = pipeUID;
	}

	public JSch getJsch() {
		return jsch;
	}

	public void setJsch(JSch jsch) {
		this.jsch = jsch;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	public String getRtConfig() {
		return rtConfig;
	}
	public void setRtConfig(String rtConfig) {
		this.rtConfig = rtConfig;
	}
	
	
	
	
}
