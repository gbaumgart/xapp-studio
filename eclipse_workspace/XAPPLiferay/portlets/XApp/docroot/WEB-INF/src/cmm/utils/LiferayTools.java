package cmm.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import pmedia.utils.TimeUtils;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.model.ExpandoValue;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import cmm.types.CMMSubscription;
import cmx.tools.LiferayContentTools;
import cmx.types.ConfigurableInformation;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LiferayTools {

	public static void addUserSubscription(User user, CMMSubscription subscription)
	{
		if(user==null)
		{
			return;
		}
		
		JSONSerializer serializer = new JSONSerializer();
		String subscriptionText = null;
		
		try {
			subscriptionText = serializer.deepSerialize(subscription);
		} catch (Exception e) {
			return;
		}
		
		if(subscriptionText!=null && subscriptionText.length() > 0)
		{
			ArrayList<CMMSubscription>userSubscriptions = LiferayTools.getUserSubscriptions(user.getUuid());
			if(userSubscriptions!=null)
			{
				System.out.println("\n\n updating user subscriptions");
				userSubscriptions.add(subscription);
				LiferayTools.setUserSubscriptions(user.getUuid(), userSubscriptions);
			}
		}
		
	}
	public static int getCompanyId()
	{
		String cStr = System.getProperty("companyId");
		return Integer.parseInt(cStr);
		
	}
	public static void setUserSubscriptions(String userUUID,ArrayList<CMMSubscription>userSubscriptions)
	{
		
		
		User liferayUser = LiferayContentTools.getLiferayUserByUUID(userUUID);
		if(liferayUser==null)
		{
			try {
				throw new Exception("getUserSubscriptions :: Could find liferay user");
			} catch (Exception e) 
			{
				e.printStackTrace();
				return ;
			}
		}
		
		//com.liferay.portlet.expando.model.impl.ExpandoBridgeImp expandoBridge = new ExpandoBridgeImpl(com.liferay.portal.model.Group.class.getName());
		
		ExpandoBridge bridge = liferayUser.getExpandoBridge();
		//ExpandoValueLocalServiceUtil.addValue(companyId, className, tableName, columnName, classPK, data)
			 	
	 	// create default 
	 	if(userSubscriptions!=null)
	 	{
	 		JSONSerializer serializer = new JSONSerializer();
	 		String userSubscriptionText = null;
	 		try {
	 			userSubscriptionText=serializer.deepSerialize(userSubscriptions);	
			} catch (Exception e) {
				
			}
	 		
	 		if(userSubscriptionText!=null && userSubscriptionText.length() > 0)
	 		{
	 			if(bridge!=null){
	 				try {
						ExpandoValueLocalServiceUtil.addValue(getCompanyId(),User.class.getName(),ExpandoTableConstants.DEFAULT_TABLE_NAME,"Subscriptions",liferayUser.getUserId(),userSubscriptionText);
					} catch (PortalException e) {
						e.printStackTrace();
					} catch (SystemException e) {
						e.printStackTrace();
					}
	 			}
	 		}
	 	}
	}
	public static ArrayList<CMMSubscription>getUserSubscriptions(String userUUID)
	{
		User liferayUser = LiferayContentTools.getLiferayUserByUUID(userUUID);
		if(liferayUser==null)
		{
			try {
				throw new Exception("getUserSubscriptions :: Could find liferay user");
			} catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			}
		}
		
		ArrayList<CMMSubscription>userSubscriptions = null;
		
		
		//com.liferay.portlet.expando.model.impl.ExpandoBridgeImp expandoBridge = new ExpandoBridgeImpl(com.liferay.portal.model.Group.class.getName());
		
		ExpandoBridge bridge = liferayUser.getExpandoBridge();
		//ExpandoValueLocalServiceUtil.addValue(companyId, className, tableName, columnName, classPK, data)
		String userSubscription = null;
		
		ExpandoValue userSubscriptionExpValue = null;
		try {
			userSubscriptionExpValue= ExpandoValueLocalServiceUtil.getValue(getCompanyId(),User.class.getName(),ExpandoTableConstants.DEFAULT_TABLE_NAME,"Subscriptions",liferayUser.getUserId()); 
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(userSubscriptionExpValue!=null){
			userSubscription=userSubscriptionExpValue.getData();
		}
		
	 	if(userSubscription!=null && userSubscription.length() > 0)
	 	{
	 			//System.out.println("User Subscriptions : " + userSubscription);
	 			JSONDeserializer derializerSC = new JSONDeserializer<ArrayList<CMMSubscription>>();
	 			try {
	    			userSubscriptions= (ArrayList<CMMSubscription>)derializerSC.deserialize(userSubscription);	
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	 	}	 			 	
	 	
	 	if(userSubscriptions!=null)
	 	{
	 		Date now=new Date();
	 		for(int i = 0 ; i < userSubscriptions.size() ; i ++)
	 		{
	 			CMMSubscription s = userSubscriptions.get(i);
	 			if(s.getExpirationDate()!=null)
	 			{
	 				//Date expiry = new Date(s.getExpirationDate().);
	 				long ddiff = TimeUtils.daysBetween(now, s.getExpirationDate());
	 				if(ddiff<5){
	 					s.setExpired(true);
	 				}
	 				//System.out.println("expires in " + ddiff);
	 			}
	 		}
	 	}
	 	
	 	
	 	// create default 
	 	if(userSubscriptions==null){
	 		
	 		userSubscriptions = new ArrayList<CMMSubscription>();
	 		JSONSerializer serializer = new JSONSerializer();
	 		String userSubscriptionText = null;
	 		try {
	 			userSubscriptionText=serializer.deepSerialize(userSubscriptions);	
			} catch (Exception e) {
				
			}
	 		
	 		if(userSubscriptionText!=null && userSubscriptionText.length() > 0){
 				try {
 					ExpandoValueLocalServiceUtil.addValue(getCompanyId(),User.class.getName(),ExpandoTableConstants.DEFAULT_TABLE_NAME,"Subscriptions",liferayUser.getUserId(),userSubscriptionText);
				} catch (PortalException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
	 		}
	 	}
		return userSubscriptions;
	}
	
}
