package cmx.manager;
import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
public class GTrackerManager {

	public static JGoogleAnalyticsTracker tracker=null;
	
	
	public static String openArticle="openArticle";
	public static String openFeed="openFeed";
	public static String openGDoc="openGDoc";
	public static String addDS="addDS";
	public static String addFeed="addFeed";
	public static String uploadFile="uploadFile";
	
	
	public static JGoogleAnalyticsTracker getTracker()
	{
		
		if(GTrackerManager.tracker==null)
		{
			GTrackerManager.tracker = new JGoogleAnalyticsTracker("xas","1.0",System.getProperty("xasTrackerUID"));
		}
		
		return GTrackerManager.tracker; 
	
	}
	
	
	public static void trackXAPPEvent(String event,String parameter)
	{
		trackEvent("xapp", event, parameter);
	}
	
	public static void trackQXAPPEvent(String event,String parameter)
	{
		trackEvent("qxapp", event, parameter);
	}
	
	public static void trackXASEvent(String event,String parameter)
	{
		trackEvent("xas", event, parameter);
	}
	
	public static void trackEvent(String systemName,String event,String parameter)
	{
		
		FocusPoint parentFocusPoint = new FocusPoint(systemName);
		String outEvent =event;
		if(parameter!=null)
		{
			outEvent+="::"+parameter;
		}
		FocusPoint asyncChildFocuPoint = new FocusPoint(outEvent, parentFocusPoint);
	    getTracker().trackAsynchronously(asyncChildFocuPoint);
	}
}
