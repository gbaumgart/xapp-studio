package cmx.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.internet.InternetAddress;

import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.DataSourceBase;

import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.model.User;

public class TrackingUtils {
	
	public static void trackEvent(String subject,String body)
	{
		  JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(subject,"1.3.2","UA-3652513-12");
		  FocusPoint focusPoint = new FocusPoint(body);
		  tracker.trackAsynchronously(focusPoint);
	}
	public static void sendMail(String subject,String body,String uuid,String appId)
	{
		
		if(System.getProperty("enableTracking") !=null && System.getProperty("enableTracking").equals("false")){
			return;
		}
		
		if(appId==null){
			appId="noAppId";
		}
		
		if(uuid!=null){
			User user = LiferayContentTools.getLiferayUserByUUID(uuid);
			if(user!=null){
				subject+=" :: " + user.getEmailAddress() + "::" + uuid + " :: appId " +appId;
				
				ApplicationManager am = ApplicationManager.getInstance();
				ArrayList<Application>userApps=am.getUserApplicationsFull(uuid);
				
				if(userApps !=null && userApps.size()>0){
					for(int i  = 0  ;  i < userApps.size(); i++){
						
						Application app = userApps.get(i);
						ArrayList<DataSourceBase>datasources = app.getDataSources();
						
						for(int j = 0  ; j  <  datasources.size() ; j++)	
						{
							DataSourceBase ds = datasources.get(j);
							Boolean ok = ds.getType().equals("JoomlaMySQL") || ds.getType().equals("XAppConnect") || ds.getType().equals("JoomlaXML") || ds.getType().equals("WordpressXML");
							if(!ok){
								continue;
							}
							String siteUrl = ds.getUrl();
							if(siteUrl!=null && siteUrl.length()>0){
								
								String mobileAppUrl= "";
								mobileAppUrl = "<br/><a href=\"" + siteUrl + "\">" + siteUrl +"</a>";
								body+=mobileAppUrl;
							}
							
						}
						body+="<br/>";
						body+="<br/>";
						body+="<br/>";
						
						
					}
					body+="User has : " + userApps.size() + " Applications <br/>";
				}
				
			}
			
			String mobileAppUrl= "http://www.xapp-studio.com/XApp-portlet/mobileClientBoot.jsp?appId=" +appId + "&uuid=" + uuid +"&noSim=true&width=320&height=480";
			mobileAppUrl = "<br/><a href=\"" + mobileAppUrl + "\">Phone Simulator</a>";
			body+=mobileAppUrl;
			
			mobileAppUrl= "http://www.xapp-studio.com/XApp-portlet/mobileClientBoot.jsp?appId=" +appId + "&uuid=" + uuid +"&noSim=true";
			mobileAppUrl = "<br/><br/><a href=\"" + mobileAppUrl + "\">Phone</a>";
			body+=mobileAppUrl;
		}
		
		
		
		com.liferay.portal.kernel.mail.MailMessage msg = new MailMessage();
        msg.setSubject("Liferay Message : " + subject);
        msg.setHTMLFormat(true);
        
        msg.setBody(body);
        MailMessage message = null;
        InternetAddress from = null;
		try {
			from = new InternetAddress("form@pearls-media.com", "Liferay");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        message  = new MailMessage(from, from, subject , body, true);
        com.liferay.mail.service.MailServiceUtil.sendEmail(message);
	}
}
