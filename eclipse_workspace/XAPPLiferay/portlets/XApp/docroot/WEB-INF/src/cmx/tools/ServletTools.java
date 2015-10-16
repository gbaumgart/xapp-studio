package cmx.tools;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.MarkUp;
import net.sourceforge.wurfl.core.WURFLHolder;
import net.sourceforge.wurfl.core.WURFLManager;
import net.sourceforge.wurfl.core.exc.CapabilityNotDefinedException;

public class ServletTools {

	public static String getDevice(HttpServletRequest request, HttpServletResponse response,ServletContext context) throws ServletException, IOException
    {
		WURFLHolder wurfl = (WURFLHolder) context.getAttribute(WURFLHolder.class.getName());
		if(wurfl!=null){
			WURFLManager manager = wurfl.getWURFLManager();
			if(manager!=null){
				Device wdevice = manager.getDeviceForRequest(request);
				if(wdevice!=null)
				{
					String isIOS = wdevice.getCapability("device_os");
					return isIOS;
					
				}else{
					System.out.println("couldn't determine wurlf device");
				}
			}else{
				System.out.println("couldn't create wurfl manager");
			}
		}else{
			System.out.println("couldn't init wurfl instance");
		}
		return null;
    }
	public static Device getWurflDevice(HttpServletRequest request, HttpServletResponse response,ServletContext context) throws ServletException, IOException
    {
		WURFLHolder wurfl = (WURFLHolder) context.getAttribute(WURFLHolder.class.getName());
		if(wurfl!=null){
			WURFLManager manager = wurfl.getWURFLManager();
			if(manager!=null){
				Device wdevice = manager.getDeviceForRequest(request);
				if(wdevice!=null)
				{
					return wdevice;
				}else{
					System.out.println("couldn't determine wurlf device");
				}
			}else{
				System.out.println("couldn't create wurfl manager");
			}
		}else{
			System.out.println("couldn't init wurfl instance");
		}
		return null;
    }
	
	public static boolean isDesktop(HttpServletRequest request, HttpServletResponse response,ServletContext context) throws ServletException, IOException
    {
		WURFLHolder wurfl = (WURFLHolder) context.getAttribute(WURFLHolder.class.getName());
		if(wurfl!=null){
			WURFLManager manager = wurfl.getWURFLManager();
			if(manager!=null){
				Device wdevice = manager.getDeviceForRequest(request);
				if(wdevice!=null)
				{
					String isIOS = wdevice.getCapability("device_os");
					System.out.println("device : " + isIOS);
					if(isIOS!=null && (isIOS.equals("iPhone OS") || isIOS.equals("iOS"))){
						return false;
					}
					return wdevice.getCapabilityAsBool("is_wireless_device")==false && wdevice.getCapabilityAsBool("device_claims_web_support")==true;
				}else{
					System.out.println("couldn't determine wurlf device");
				}
			}else{
				System.out.println("couldn't create wurfl manager");
			}
		}else{
			System.out.println("couldn't init wurfl instance");
		}
		
		return false;
    }
}
