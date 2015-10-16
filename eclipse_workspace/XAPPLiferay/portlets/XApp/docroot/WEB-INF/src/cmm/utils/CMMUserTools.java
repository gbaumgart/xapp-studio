package cmm.utils;
import java.io.Serializable;
import java.util.List;

import com.liferay.portlet.expando.model.ExpandoBridge;

import cmm.types.CMMUser;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Address;;
public class CMMUserTools {

	public static CMMUser fromLiferayUser(com.liferay.portal.model.User user)
	{
		CMMUser result = null;
		if(user!=null)
		{
			result = new CMMUser();
			//result.setAddress(user.getAddresses());
			result.setEmail(user.getEmailAddress());
			result.setFirstName(user.getFirstName());
			result.setLastName(user.getLastName());
			result.setPassword(user.getPasswordUnencrypted());
			result.setPasswordEncrypted(user.getPassword());
			result.setUuid(user.getUuid());
			
			List<Address> addresses = null;
			
			try {
				addresses= user.getAddresses();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(addresses!=null && addresses.size() > 0)
			{
				Address  a=addresses.get(0);
				if(a!=null){
					result.setAddress(a.getStreet1());
					result.setPostcode(a.getZip());
					result.setCountry(a.getCountry().getName());
				}
				
			}
			
			ExpandoBridge bridge = user.getExpandoBridge();
		 	if(bridge!=null)
		 	{
		 		Serializable ser = bridge.getAttribute("Subscriptions");
		 		if(ser!=null)
		 		{

		 			String userSubscription = (String)ser;
				 	if(userSubscription!=null)
				 	{
				 		System.out.println("User Subscriptions : " + userSubscription);
				 		result.setSubscriptions(userSubscription);
				 	}	 			 	
			 	}
		 	}
		}
		//result.setCountry(user.getC)
		return result;
	}
}
