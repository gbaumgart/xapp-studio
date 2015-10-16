package cmx.types;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import cmx.tools.LiferayContentTools;

import com.liferay.portal.model.User;

public class Cloud9Thread implements Runnable
{
	public String workspace;
	public Process process; 
	public BufferedReader buf=null;
	public int port;
	public Thread thread;
	public String pid;
	public String uuid;
	
	public Cloud9Thread(String _workspace)
	{
		workspace=_workspace;
	}
	
	public static void killUserSession(String workspace,String uuid)
	{
		ArrayList<String>processes = cleanup();
		if(processes==null){
			return;
		}
		for(int i = 0 ; i < processes.size() ;i ++){
			
			String process  = processes.get(i);
			
		}
	}
	
	public static ArrayList<String> cleanup(){
		BufferedReader buf=null;
		
		ArrayList<String> result = new ArrayList<String>();
		
		Runtime run = Runtime.getRuntime();
		Process process =null; 
		try {
			process = run.exec("ps -aux");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		try {
			while ((line=buf.readLine())!=null) 
			{
				System.out.println(line);
				
				if(line!=null && line.contains("PID"))
				{
					
				}
				if(line.contains("Process terminated with"))
				{
					
				}
			}
			} catch (IOException e) 
			{
				e.printStackTrace();
			} 
		
		
		return result;
	}
	
	public void run() {
      
		String cloud9Root = System.getProperty("cloud9Root");
		try
		{
			//killUserSession(null, uuid);
			Boolean runAsChroot = Boolean.parseBoolean(System.getProperty("runCloud9InChroot"));
			User luser = LiferayContentTools.getLiferayUserByUUID(uuid);
			if(luser==null)
			{
				return;
			}
			Runtime run = Runtime.getRuntime();
			String node=System.getProperty("nodePath");
			String fullCMD="";
			
			
			
			if(!runAsChroot){
				//fullCMD = node + " " + cloud9Root + "server.js" + " -p " + port+ " -w " + workspace;
				//fullCMD = node + " " + cloud9Root + "server.js" + " -p " + port+ " -w " + workspace;
				fullCMD = System.getProperty("xasWebRoot") +"/c9.sh";
				File _s = new File(fullCMD);
				_s.setExecutable(true);
				if(uuid.equals(System.getProperty("adminUser"))){
					fullCMD +=" admin" + " -p " + port + " -w " + workspace;
				}else{
					fullCMD +=" default" + " -p " + port + " -w " + workspace;
				}
				process = run.exec(fullCMD);
			}else{
				fullCMD = System.getProperty("xasWebRoot") +"/c9Jail.sh";
				File _s = new File(fullCMD);
				_s.setExecutable(true);
				
				//fullCMD +=" " + port + " " + workspace;
				if(uuid.equals(System.getProperty("adminUser"))){
					fullCMD +=" admin" + " " + port + " " + workspace;
				}else{
					fullCMD +=" default" + " " + port + " " + workspace;
				}
				process = run.exec(fullCMD);
				
				/*
				
				fullCMD = node + " " + cloud9Root + "server.js" + " -p " + port+ " -w " + workspace;
				fullCMD ="dchroot -c lenny " +"\"" + fullCMD + "\"";
				
				process = run.exec("/bin/sh");
				BufferedWriter outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				outCommand.write(fullCMD + "\n");
				outCommand.flush();
				*/
			}

			System.out.println("full scrupt " + fullCMD);
			
			buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			try {
				while ((line=buf.readLine())!=null) 
				{
					System.out.println(line);
					
					if(line!=null && line.contains("PID"))
					{
						String[] args = line.split("PID");
						if(args!=null )
						{
							pid=args[1];
							System.out.println(" have pid : " + pid);
						}
						
					}
					if(line.contains("Process terminated with"))
					{
						this.process.destroy();
						thread.stop();
						
					}
				}
				} catch (IOException e) 
				{
					e.printStackTrace();
				} 
			
		}catch (IOException e)
		{
	          System.out.println("exception happened - here's what I know: ");
	          e.printStackTrace();
		}
    }
	
	/*
	public void runOld() {
	      
		String cloud9Root = System.getProperty("cloud9Root");
		try
		{
			
			Boolean runAsChroot = Boolean.parseBoolean(System.getProperty("runCloud9InChroot"));
			
			
			User luser = LiferayContentTools.getLiferayUserByUUID(uuid);
			if(luser==null)
			{
				return;
			}
			
			
			Runtime run = Runtime.getRuntime() ;
			
			String node=System.getProperty("nodePath");
			String fullCMD="";
			
			if(!runAsChroot){
				fullCMD = node + " " + cloud9Root + "server.js" + " -p " + port+ " -w " + workspace;
				//fullCMD +=" --username " + luser.getEmailAddress();
				//fullCMD +=" --password " + luser.getPasswordUnencrypted();
				process = run.exec(fullCMD);
			}else{
				//dchroot -c lenny "/usr/local/bin/node /PMaster/cloud9/server.js -w /var/www/vhosts/pearls-media.com/httpdocs/CMAC/11166763-e89c4ba-aba7-4e9f4fdf97a9/apps/mygeneralappb4/
				
				fullCMD = node + " " + cloud9Root + "server.js" + " -p " + port+ " -w " + workspace;
				//fullCMD +=" --username " + luser.getEmailAddress();
				//fullCMD +=" --password " + luser.getPassword();

				fullCMD ="dchroot -c lenny " +"\"" + fullCMD + "\"";
				
				process = run.exec("/bin/sh");
				BufferedWriter outCommand = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				outCommand.write(fullCMD + "\n");
				outCommand.flush();
				
			}
			
			
			System.out.println("full scrupt " + fullCMD);
			
			buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			try {
				while ((line=buf.readLine())!=null) 
				{
					System.out.println(line);
					if(line.contains("Process terminated with"))
					{
						this.process.destroy();
						thread.stop();
						
					}
				}
				} catch (IOException e) 
				{
					e.printStackTrace();
				} 
			
		}catch (IOException e)
		{
	          System.out.println("exception happened - here's what I know: ");
	          e.printStackTrace();
		}
    }
	*/

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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
}
