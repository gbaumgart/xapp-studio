package cmm.types;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cmm.utils.LiferayTools;
import cmx.tools.Crypto;
import cmx.tools.LiferayContentTools;
import cmx.tools.TrackingUtils;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.TemplateInfo;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.User;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import pmedia.types.Constants;
import pmedia.utils.StringUtils;


public class SubscriptionManager 
{

	public ArrayList<CMMSubscription> getAddons(CMMUser user,String appId) 
	{
		return createAddons(user,appId);
	}
	public ArrayList<CMMSubscription>loadAddons(String file)
	{
		ArrayList<CMMSubscription> result = null;
		String description=null;
		try {
			description = StringUtils.readFileAsString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(description==null || description.length()==0){
			return null;
		}
		String json=null;
		
		
		JSONDeserializer<ArrayList<CMMSubscription> >deserializer= new JSONDeserializer();
		if(description!=null && description.length() > 0)
		{
			result=  deserializer.deserialize(description);
		}
		
		return result;
	}
	public ArrayList<CMMSubscription>adjustAddons(ArrayList<CMMSubscription> subscriptions,CMMUser user,String appId)
	{	
		ArrayList<CMMSubscription> result= new ArrayList<CMMSubscription>();
		for (int i = 0; i < subscriptions.size(); i++) 
		{
			CMMSubscription subscription=subscriptions.get(i);
			String prefix =getBuyPrefix(user);
			
			String buyLink = getBuyLink(subscription.getReferenceNumber(),"EUR","en",appId);
			subscription.setBuyLink(prefix+StringUtils.encode(buyLink));
			
			
			CMMSubscription userAppSubscription = hasAppSubscription(user.getUuid(), subscription.getReferenceNumber(), appId);
			if(userAppSubscription!=null)
			{
				subscription.setOrderStatus(userAppSubscription.getOrderStatus());
				subscription.setExpirationDate(userAppSubscription.getExpirationDate());
				subscription.setAppId(userAppSubscription.getAppId());
				subscription.setUuid(userAppSubscription.getUuid());
				subscription.setPublishStatus(userAppSubscription.getPublishStatus());
			}else{
				subscription.setAppId(appId);
				subscription.setUuid(user.getUuid());
			}
			
			
			Date exp= new Date();
			exp.setDate(25);
			exp.setMonth(9);
			long p = exp.getTime();
			//System.out.println("pppp " + p);
			
			
			/*
			String buyLink = getBuyLink(result.getReferenceNumber(),"EUR","en",appId);
			buyLink = getBuyFreeLink(result.getReferenceNumber(), period, "EUR", ipnPrice, "en", appId, "CCVISAMC");
			result.setBuyLink(prefix+StringUtils.encode(buyLink));
			*/
			
		}
		
		return subscriptions;
	}
	
	public ArrayList<CMMSubscription>getPlatformAddons(ArrayList<CMMSubscription> subscriptions,String platform)
	{
		ArrayList<CMMSubscription> result= new ArrayList<CMMSubscription>();
		for (int i = 0; i < subscriptions.size(); i++) 
		{
			CMMSubscription subscription=subscriptions.get(i);
			if(subscription.platform!=null && subscription.platform.equals(platform))
			{
				result.add(subscription);
			}else if(platform==Constants.ALL)
			{
				result.add(subscription);
			}
		}
		return result;
	}
	/***
	 * 
	 * @param user
	 * @param appId
	 * @return
	 */
	private ArrayList<CMMSubscription> createAddons(CMMUser user,String appId)
	{
		
		
		ArrayList<CMMSubscription> result=null;
		
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(user.getUuid(), appId, false);
		
		String appPlatform=Constants.ALL;
		if(app!=null)
		{
			//appPlatform = app.getPlatform();
		}
		
		ArrayList<CMMSubscription> loadedSubscriptions=null;
		String fileSubSubscriptions = System.getProperty("AppStoreRoot") + "/sec/addons.json";
		if(new File(fileSubSubscriptions).exists())
		{
			loadedSubscriptions = loadAddons(fileSubSubscriptions);
			if(loadedSubscriptions!=null)
			{
				
				JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
				serializer.prettyPrint(true);
		      	String serialized=serializer.deepSerialize(loadedSubscriptions);
		      	if(serialized!=null)
		      	{
		      		try {
						StringUtils.writeToFile(serialized, fileSubSubscriptions);
					} catch (IOException e) 
					{					
						e.printStackTrace();
					}
		      	}
				
				ArrayList<CMMSubscription> platformSubscriptions=getPlatformAddons(loadedSubscriptions,appPlatform);
				if(platformSubscriptions!=null){
					result = adjustAddons(platformSubscriptions, user, appId);
					if(result !=null)
					{
						return result;
					}
				}
			}
		}
		
		
		
		if(appPlatform.equals(Constants.USERAGENT_IPHONE_NATIVE))
		{
			result = createAvaiableAppSubscriptionsNativeIPHONE(user, appId);
		}
		//https://secure.avangate.com/order/trial.php?PRODS=4566317&QTY=1&PAY_TYPE=CCVISAMC&PRICES4566317[EUR]=0&TPERIOD=36000&PHASH=9895617c1c619075d450bea581c8284f
		if(appPlatform.equals(Constants.MOBILE_WEB_APP))
		{
			//result=createAvaiableAppSubscriptionsWebApp(user, appId);
		}
		
		
		
		result.addAll(createAvaiableAppSubscriptionsWebApp(user, appId));
		
		
		
		
		/*
		if(result!=null)
		{
			String file = System.getProperty("AppStoreRoot") + "subscriptions.json";
			JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
	      	String serialized=serializer.deepSerialize(result);
	      	if(serialized!=null)
	      	{
	      		
	      		try {
					//StringUtils.writeToFile(serialized, file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      	}
		}
		*/
		
		
		
		return result;
	}
	
	private ArrayList<CMMSubscription>avaiableSubscriptions;
	
	private ArrayList<CMMSubscriptionStatus>subscriptionsInPorgress;
	
	public String publish(String uuid,String appID , String referenceNumber)
	{
		
		User liferayUser = LiferayContentTools.getLiferayUserByUUID(uuid);
		if(liferayUser==null){
			return null;
		}
		ArrayList<CMMSubscription>userSubscriptions = LiferayTools.getUserSubscriptions(liferayUser.getUuid());
		if(userSubscriptions ==null)
		{
			return null;
		}
		if(userSubscriptions.size()==0)
		{
			return null;
		}
		
		CMMSubscription appSubscription = hasAppSubscription(uuid,referenceNumber,appID);
		if(appSubscription ==null)
		{
			return null;
		}
		appSubscription.publishStatus="Publishing in Progress";
		updateUserSubscription(userSubscriptions, appSubscription);
		LiferayTools.setUserSubscriptions(liferayUser.getUuid(), userSubscriptions);
		
		String body  = " User " + liferayUser.getUuid() + " published his application " + appSubscription.getAppId();
		try {
			TrackingUtils.sendMail("Publish App", body, liferayUser.getUserUuid(),appID);
		} catch (SystemException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}
	public Boolean isValidSubscription(CMMSubscription subscription)
	{
		
		if(subscription.getCustomerEMail()==null){
			return false;
		}
		
		if(subscription.getOrderStatus()==null){
			return false;
		}
		
		if(subscription.getOrderStatus().length() == 0){
			return false;
		}
		/*
		if(subscription.getExpirationDate() == null)
		{
			return false;
		}
		*/
		
		if(subscription.getSalesDate() == null)
		{
			return false;
		}
		
		if(subscription.getIpAddress() == null)
		{
			return false;
		}
		if(subscription.getReferenceNumber() == null)
		{
			return false;
		}
		
		/*
		if(subscription.getLicenseType() == null)
		{
			return false;
		}
		*/
		if(subscription.getOrderLanguage()== null)
		{
			return false;
		}
		
		User liferayUser = LiferayContentTools.getLiferayUserByEMail(subscription.getCustomerEMail());
		if(liferayUser==null)
		{
			try {
				throw new Exception("isValid Subscription :: Could find liferay user");
			} catch (Exception e) 
			{
				e.printStackTrace();
				return false;
			}
		}
		
		
		
		
		
		return true;
	}
	public ArrayList<CMMSubscription>updateUserSubscription(ArrayList<CMMSubscription>userSubscriptions, CMMSubscription subscription)
	{
		
		
		for(int  i = 0 ; i < userSubscriptions.size() ; i ++)
		{
			CMMSubscription userSubscription = userSubscriptions.get(i);
			if(userSubscription.getReferenceNumber().equals(subscription.getReferenceNumber()) && userSubscription.getAppId().equals(subscription.getAppId()))
			{
				
				userSubscription.setLicenseType(subscription.getLicenseType());
				userSubscription.setExpirationDate(subscription.getExpirationDate());
				userSubscription.setSalesDate(subscription.getSalesDate());
				userSubscription.setPublishStatus(subscription.getPublishStatus());
			}
		}
		
		return userSubscriptions;
		
	}
	
	
	public String getBuyPrefix(CMMUser user)
	{
		User luser = LiferayContentTools.getLiferayUserByEMail(user.getEmail());
		String street = "street";
		String pcode = "07840";
		String city = "Santa Eulalia";
		String country = "Spain";
		String ccode ="ES";
		
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";
		
		if(luser!=null){
			
			List<Address> addresses = null;
			try {
				 addresses=luser.getAddresses();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(addresses!=null && addresses.size() > 0){
				
				Address address = addresses.get(0);
				if(address.getStreet1()!=null)
				{
					street=address.getStreet1(); 
				}
				if(address.getCity()!=null)
				{
					city=address.getCity(); 
				}
				if(address.getCountry()!=null)
				{
					Country c = address.getCountry();
					country=c.getName(); 
					ccode = c.getIdd();
				}
				if(address.getZip()!=null)
				{
					pcode = address.getZip(); 
				}
			}
			
		}

		prefix +="BILL_ZIPCODE=" + pcode +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() +
				"&BILL_CITY="+city+ "&BILL_ADDRESS=" + street +"&BILL_COUNTRYCODE="+ccode+"&URL=";

		return prefix;
	}
	public ArrayList<CMMSubscription>adjustSubscriptions(ArrayList<CMMSubscription> subscriptions,CMMUser user,String appId)
	{	
		ArrayList<CMMSubscription> result= new ArrayList<CMMSubscription>();
		for (int i = 0; i < subscriptions.size(); i++) 
		{
			CMMSubscription subscription=subscriptions.get(i);
			String prefix =getBuyPrefix(user);
			
			String buyLink = getBuyLink(subscription.getReferenceNumber(),"EUR","en",appId);
			subscription.setBuyLink(prefix+StringUtils.encode(buyLink));
			
			
			CMMSubscription userAppSubscription = hasAppSubscription(user.getUuid(), subscription.getReferenceNumber(), appId);
			if(userAppSubscription!=null)
			{
				subscription.setOrderStatus(userAppSubscription.getOrderStatus());
				subscription.setExpirationDate(userAppSubscription.getExpirationDate());
				subscription.setAppId(userAppSubscription.getAppId());
				subscription.setUuid(userAppSubscription.getUuid());
				subscription.setPublishStatus(userAppSubscription.getPublishStatus());
			}else{
				subscription.setAppId(appId);
				subscription.setUuid(user.getUuid());
			}
			
			
			Date exp= new Date();
			exp.setDate(25);
			exp.setMonth(9);
			long p = exp.getTime();
			//System.out.println("pppp " + p);
			
			
			/*
			String buyLink = getBuyLink(result.getReferenceNumber(),"EUR","en",appId);
			buyLink = getBuyFreeLink(result.getReferenceNumber(), period, "EUR", ipnPrice, "en", appId, "CCVISAMC");
			result.setBuyLink(prefix+StringUtils.encode(buyLink));
			*/
			
		}
		
		return subscriptions;
	}
	public void saveSubscriptions(ArrayList<CMMSubscription> subscriptions,String file)
	{
		
	}
	
	public ArrayList<CMMSubscription>getPlatformSubscriptions(ArrayList<CMMSubscription> subscriptions,String platform)
	{
		ArrayList<CMMSubscription> result= new ArrayList<CMMSubscription>();
		for (int i = 0; i < subscriptions.size(); i++) 
		{
			CMMSubscription subscription=subscriptions.get(i);
			if(subscription.platform!=null && subscription.platform.equals(platform))
			{
				result.add(subscription);
			}else if(platform==Constants.ALL)
			{
				result.add(subscription);
			}
		}
		return result;
	}
	public ArrayList<CMMSubscription>loadSubscriptions(String file)
	{
		ArrayList<CMMSubscription> result = null;
		String description=null;
		try {
			description = StringUtils.readFileAsString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(description==null || description.length()==0){
			return null;
		}
		String json=null;
		
		
		JSONDeserializer<ArrayList<CMMSubscription> >deserializer= new JSONDeserializer();
		if(description!=null && description.length() > 0)
		{
			result=  deserializer.deserialize(description);
		}
		
		return result;
	}
	public void onSubscriptionPurchase(CMMSubscription subscription)
	{
		System.out.println("\n \t\t onSubscription \n\n");
		Boolean isValidSubscription = isValidSubscription(subscription);
		if(!isValidSubscription){
			/*
			try {
				//throw new Exception("onSubscription :: Received Invalid Subscription");
			} catch (Exception e) 
			{
				//e.printStackTrace();
			}
			*/
			System.out.println("\n \t\t onSubscription : invalid\n\n");
			return;
		}
		
		User liferayUser = LiferayContentTools.getLiferayUserByEMail(subscription.getCustomerEMail());
		if(liferayUser==null)
		{
			System.out.println("\n \t\t Could find liferay user\n\n");
			return;
			/*
			try {
				throw new Exception("onSubscription Subscription :: Could find liferay user");
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			*/
		}
		//find subscription status in queue :
		
		CMMSubscriptionStatus status = getSubscriptionStatus(liferayUser.getUuid(),subscription.getAppId(),subscription.getReferenceNumber());
		if(status!=null)
		{
			System.out.println("\t\t onSubscription : found pending subscription");
			if(subscription.getOrderStatus().equals("TEST") || subscription.getOrderStatus().equals("COMPLETE"))
			{
				status.setCheckoutStatus("COMPLETE");
				subscription.setOrderStatus("COMPLETE");
				
				CMMSubscription userSubscription = SubscriptionManager.hasSubscription(liferayUser.getUuid(),subscription.getAppId(), subscription.getReferenceNumber());
				if(userSubscription!=null)
				{
					ArrayList<CMMSubscription>userSubscriptions = LiferayTools.getUserSubscriptions(liferayUser.getUuid());
					if(userSubscriptions!=null)
					{
						/*
						userSubscription.setExpirationDate(subscription.getExpirationDate());
						userSubscription.setSalesDate(subscription.getSalesDate());
						userSubscription.setLicenseType(subscription.getLicenseType());
						*/
						userSubscriptions = updateUserSubscription(userSubscriptions, subscription);
						LiferayTools.setUserSubscriptions(liferayUser.getUuid(), userSubscriptions);
					}
					
				}else{
					LiferayTools.addUserSubscription(liferayUser, subscription);
				}
			}
		}
	}
	
	public CMMSubscriptionStatus getSubscriptionStatus(String uuid,String appId,String referenceNumber)
	{
		//ArrayList<CMMSubscription>_userSubscriptions = LiferayTools.getUserSubscriptions(uuid);
		//System.out.println("user subscriptions : " + _userSubscriptions.size());
		
		
		/*
		String salesDate = "2012-03-17 10:50:20"; 
				
		DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
        Date dSalesDate = null; 
        
        try {
			dSalesDate = format.parse(salesDate);
		} catch (ParseException e) {
			
		}
        
        System.out.println("sales date   : "  + dSalesDate.toString());
        */
		if(subscriptionsInPorgress==null)
		{
			subscriptionsInPorgress = new ArrayList<CMMSubscriptionStatus>();
		}
		
		
		for(int  i = 0 ; i < subscriptionsInPorgress.size() ; i ++)
		{
			CMMSubscriptionStatus status = subscriptionsInPorgress.get(i);
			if(  status.getAppId().equals(appId) && status.getUuid().equals(uuid) && status.getReferenceNumber().equals(referenceNumber))
			{
				return status;
			}
		}
		return null;
	}
	
	public void addToPurchaseQueue(String uuid,String appId,String referenceNumber)
	{
		if(subscriptionsInPorgress==null)
		{
			subscriptionsInPorgress=new ArrayList<CMMSubscriptionStatus>();
		}
		
		CMMSubscriptionStatus status = getSubscriptionStatus(uuid,appId, referenceNumber);
		if(status!=null)
		{
			System.out.println("#### Subscription Purchase already in progress");
			subscriptionsInPorgress.remove(status);
			status=null;
		}
		
		status = new CMMSubscriptionStatus();
		status.setUuid(uuid);
		status.setReferenceNumber(referenceNumber);
		status.setAppId(appId);
		status.setCheckoutStatus("REQUESTED");
		subscriptionsInPorgress.add(status);
		
	}
	//"https://secure.avangate.com/order/checkout.php?PRODS=4555627&QTY=1&ORDERSTYLE=nLW45JTPmH4=
	private static final String trialBaseUrl = "https://secure.avangate.com/order/trial.php?";
	private static final String orderBaseUrl = "https://secure.avangate.com/order/checkout.php?";
	private static final String secretKey = "P2_8+wn8^8x33F@N9(q^";
	
	/***
	 *
	 * https://secure.avangate.com/order/trial.php?
	 * PRODS=4555627&QTY=1&
	 * CURRENCY=EUR&
	 * ORDERSTYLE=nLW45JTPmH4=&
	 * SRC=CM&
	 * PRICES4555627%5BEUR%5D=0&
	 * TPERIOD=7&PHASH=777e05e1fbba3b68688151b21cef1612
	 * 
	 * @param productId
	 * @return
	 */
	//
	public String getBuyFreeLink(String productId,String trialPeriod,String currency,String price,String lang,String appId,String paytype)
	{
		String result = SubscriptionManager.trialBaseUrl;
		
		String productParameters ="PRODS="+productId;
		
		//add currency : 
		String parameters="&CURRENCY="+currency;
		
		//add prices : 
		String priceParameter="&PRICES"+productId+"["+currency+"]=" + price;
		
		//add trial period: 
		String trialParameter ="&TPERIOD="+trialPeriod;
		
		//add hash code : 
		String key = SubscriptionManager.secretKey;

		
		String text = productParameters + "&QTY=1" +priceParameter + trialParameter;
		int length = text.length();
		String textEncrypt= ""  + length + text;
		String code = StringUtils.sStringToHMACMD5(textEncrypt,key);
		
		parameters+="&PHASH="+code;
		parameters+="&ORDERSTYLE=nLWs5JapnH4=";
		if(Boolean.parseBoolean(System.getProperty("AvangateTestMode")))
		{
			parameters+="&DOTEST=1";
		}
		
		String ekey = "asd28071977";
        Crypto encrypter = new Crypto(ekey);
		if(appId!=null && appId.length()>0)
		{
			appId = encrypter.encrypt(appId);
		}
		parameters+="&REF="+appId;
		
		//	https://secure.avangate.com/order/trial.php?PRODS=4555626&QTY=1&PAY_TYPE=CCVISAMC&PRICES4555626[EUR]=0&TPERIOD=7&PHASH=7d38f745211ac44a24c0bd8ef1409c27
		return result+ text  + parameters ;
		
	}
	public String getBuyLink(String productId,String currency,String lang,String appId)
	{
		String result = SubscriptionManager.orderBaseUrl;
		
		String productParameters ="PRODS="+productId;
		
		//add currency : 
		String parameters="&CURRENCY="+currency;
		
		//add prices : 
		//String priceParameter="&PRICES"+productId+"["+currency+"]=" + price;
		
		
		//add hash code : 
		//String key = SubscriptionManager.secretKey;

		
		String text = productParameters + "&QTY=1" ;
		//int length = text.length();
		//String textEncrypt= ""  + length + text;
		//String code = StringUtils.sStringToHMACMD5(textEncrypt,key);
		
		//parameters+="&PHASH="+code;
		parameters+="&ORDERSTYLE=nLWs5JapnH4=";
		String ekey = "asd28071977";
        Crypto encrypter = new Crypto(ekey);
		if(appId!=null && appId.length()>0)
		{
			appId = encrypter.encrypt(appId);
		}
		parameters+="&REF="+appId;
		
		if(Boolean.parseBoolean(System.getProperty("AvangateTestMode")))
		{
			parameters+="&DOTEST=1";
		}
		
//https://secure.avangate.com/order/trial.php?PRODS=4555626&QTY=1&PAY_TYPE=CCVISAMC&PRICES4555626[EUR]=0&TPERIOD=7&PHASH=7d38f745211ac44a24c0bd8ef1409c27
		return result+ text  + parameters ;
	}
	public String getTrialLink(String productId,String trialPeriod,String currency,String price,String lang,String appId)
	{
		String result = SubscriptionManager.trialBaseUrl;
		
		String productParameters ="PRODS="+productId;
		
		//add currency : 
		String parameters="&CURRENCY="+currency;
		
		//add prices : 
		String priceParameter="&PRICES"+productId+"["+currency+"]=" + price;
		
		//add trial period: 
		String trialParameter ="&TPERIOD="+trialPeriod;
		
		//add hash code : 
		String key = SubscriptionManager.secretKey;

		
		String text = productParameters + "&QTY=1" +priceParameter + trialParameter;
		int length = text.length();
		String textEncrypt= ""  + length + text;
		String code = StringUtils.sStringToHMACMD5(textEncrypt,key);
		
		parameters+="&PHASH="+code;
		parameters+="&ORDERSTYLE=nLWs5JapnH4=";
		if(Boolean.parseBoolean(System.getProperty("AvangateTestMode")))
		{
			parameters+="&DOTEST=1";
		}
		
		String ekey = "asd28071977";
        Crypto encrypter = new Crypto(ekey);
		if(appId!=null && appId.length()>0)
		{
			appId = encrypter.encrypt(appId);
		}
		parameters+="&REF="+appId;
		
//https://secure.avangate.com/order/trial.php?PRODS=4555626&QTY=1&PAY_TYPE=CCVISAMC&PRICES4555626[EUR]=0&TPERIOD=7&PHASH=7d38f745211ac44a24c0bd8ef1409c27
		return result+ text  + parameters ;
	}
	public static CMMSubscription hasAppSubscription(String uuid,String referenceNumber,String appId)
	{
		ArrayList<CMMSubscription>userSubscriptions  = LiferayTools.getUserSubscriptions(uuid);
		if(userSubscriptions!=null)
		{
			for(int i = 0 ; i < userSubscriptions.size() ; i++)
			{
				CMMSubscription subscription= userSubscriptions.get(i);
				if(subscription.getReferenceNumber().equals(referenceNumber))
				{
					if(subscription.getAppId()!=null && subscription.getAppId().equals(appId))
					{
						return subscription;
					}
				}
			}
		}
		return null;
		
	}
	public static CMMSubscription hasSubscription(String uuid,String appId, String referenceNumber)
	{
		ArrayList<CMMSubscription>userSubscriptions  = LiferayTools.getUserSubscriptions(uuid);
		if(userSubscriptions!=null)
		{
			for(int i = 0 ; i < userSubscriptions.size() ; i++){
				CMMSubscription subscription= userSubscriptions.get(i);
				if(subscription.getReferenceNumber().equals(referenceNumber) && subscription.getAppId().equals(appId))
				{
					return subscription;
				}
			}
		}
		return null;
		
	}
	private ArrayList<CMMSubscription> createAvaiableSubscriptions(CMMUser user)
	{
		
		
		ArrayList<CMMSubscription> result = new ArrayList<CMMSubscription>();
		
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";
		prefix +="BILL_ZIPCODE=78640" +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() + "&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=";
		/***
		 * Resource  + Editor Pack   = 1 Month
		 */
		float rawPrice = 0.0f;
		String plainLink=null;
		String plainLink64=null;
		String buyLink=null;
		
	
		/*
		CMMSubscription _1M_REPACK = SubscriptionManager.hasSubscription(user.getUuid(),"4555627");
		
		if(_1M_REPACK==null)
		{
			_1M_REPACK = new CMMSubscription();
		}
		
		_1M_REPACK.setIpnProductCode("1M_REPACK");
		_1M_REPACK.setReferenceNumber("4555627");
		_1M_REPACK.setIpnName("1 Month Resource And Editor Pack");
		_1M_REPACK.setIpnPrice("2.49 Euro / Month");
		_1M_REPACK.setIpnAnnualRef("4555626");
		_1M_REPACK.setGroupId(0);
		rawPrice =2.45f;
		_1M_REPACK.setRawPrice(rawPrice);
		_1M_REPACK.setIsMonthly(true);
		
		
		String plainLink = getTrialLink(_1M_REPACK.getReferenceNumber(), "7", "EUR","0","en");
		String plainLink64  = StringUtils.encode(plainLink);
		//prefix +=plainLink64;
		//_1M_REPACK.setTrialLink(plainLink);
		_1M_REPACK.setTrialLink(prefix + plainLink64);
		
		String buyLink = getBuyLink(_1M_REPACK.getReferenceNumber(),"EUR","en");

		_1M_REPACK.setBuyLink(prefix+StringUtils.encode(buyLink));
		
		result.add(_1M_REPACK);
		
		//System.out.println(_1M_REPACK.getTrialLink());
		//System.out.println(prefix);
		System.out.println(plainLink);
		*/
		
		
		
		/***
		 * Resource  + Editor Pack   = 1 YEAR
		 */
		/*
		CMMSubscription _1Y_REPACK = SubscriptionManager.hasSubscription(user.getUuid(),"4555626");
		if(_1Y_REPACK==null){
			_1Y_REPACK = new CMMSubscription();
		}
		
		_1Y_REPACK.setIpnProductCode("1Y_REPACK");
		_1Y_REPACK.setReferenceNumber("4555626");
		_1Y_REPACK.setIpnName("1 Year Resource And Editor Pack");
		_1Y_REPACK.setIpnPrice("23.99 Euro / Year");
		
		rawPrice = 23.99f;
		_1Y_REPACK.setRawPrice(rawPrice);
		_1Y_REPACK.setYourSavingsText("Your Savings : 4.78 Euro = 20%");
		
		_1Y_REPACK.setIsAnnual(true);
		
		plainLink = getTrialLink(_1Y_REPACK.getReferenceNumber(), "7", "EUR","0","en");
		 
		plainLink64  = StringUtils.encode(plainLink);
		//prefix +=plainLink64;
		//_1Y_REPACK.setTrialLink(plainLink);
		_1Y_REPACK.setTrialLink(prefix+plainLink64);
		
		buyLink = getBuyLink(_1Y_REPACK.getReferenceNumber(),"EUR","en");

		_1Y_REPACK.setGroupId(0);
		
		//System.out.println(_1M_REPACK.getTrialLink());
		System.out.println(plainLink);
		//7d38f745211ac44a24c0bd8ef1409c27
		result.add(_1Y_REPACK);
		*/
		
		/***
		 * 1x 1Y iOS Native Application Base
		 */
		CMMSubscription _1Y_IOS_APPLICATION_BASE = null;
				/*SubscriptionManager.hasSubscription(user.getUuid(),"4555728");
		if(_1Y_IOS_APPLICATION_BASE==null){
			_1Y_IOS_APPLICATION_BASE = new CMMSubscription();
		}
		
		_1Y_IOS_APPLICATION_BASE.setIpnProductCode("1Y_IOS_APPLICATION_BASE");
		_1Y_IOS_APPLICATION_BASE.setReferenceNumber("4555728");
		_1Y_IOS_APPLICATION_BASE.setIpnName("1 Year iOS Application(Base Only)");
		_1Y_IOS_APPLICATION_BASE.setIpnPrice("199.99 Euro / Year");
		
		rawPrice = 170.0f;
		_1Y_IOS_APPLICATION_BASE.setRawPrice(rawPrice);
		
		_1Y_IOS_APPLICATION_BASE.setIsAnnual(true);
		plainLink = getTrialLink(_1Y_IOS_APPLICATION_BASE.getReferenceNumber(), "7", "EUR","0","en","");
		plainLink64  = StringUtils.encode(plainLink);
		//_1Y_IOS_APPLICATION_BASE.setTrialLink(prefix+plainLink64);
		buyLink = getBuyLink(_1Y_IOS_APPLICATION_BASE.getReferenceNumber(),"EUR","en","");
		_1Y_IOS_APPLICATION_BASE.setGroupId(1);
		//System.out.println(plainLink);
		result.add(_1Y_IOS_APPLICATION_BASE);
		*/
		
		return result;
		
	}
	
	
	private ArrayList<CMMSubscription> createAvaiableAppSubscriptionsWebApp(CMMUser user,String appId)
	{
		ArrayList<CMMSubscription> result = new ArrayList<CMMSubscription>();
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";
		prefix +="BILL_ZIPCODE=78640" +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() + "&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=";
		/***
		 * Resource  + Editor Pack   = 1 Month
		 */
		
		/***
		 * 1x iOS Native Application Base
		 */
		/*
		CMMSubscription _1Y_IOS_APPLICATION_BASE = SubscriptionManager.hasSubscription(user.getUuid(),"4555728");
		if(_1Y_IOS_APPLICATION_BASE==null)
		{
			_1Y_IOS_APPLICATION_BASE = new CMMSubscription();
			
		}
		
		_1Y_IOS_APPLICATION_BASE.setAppId(appId);
		_1Y_IOS_APPLICATION_BASE.setIpnProductCode("1Y_IOS_APPLICATION_BASE");
		_1Y_IOS_APPLICATION_BASE.setReferenceNumber("4555728");
		_1Y_IOS_APPLICATION_BASE.setIpnName("1 native iOS Application(Base)");
		_1Y_IOS_APPLICATION_BASE.setIpnPrice("199.99 Euro / Year + VAT");
		rawPrice = 199.99f;
		_1Y_IOS_APPLICATION_BASE.setRawPrice(rawPrice);
		_1Y_IOS_APPLICATION_BASE.setIsAnnual(true);
		
		//_1Y_IOS_APPLICATION_BASE.setTrialLink(prefix+plainLink64);
		buyLink = getBuyLink(_1Y_IOS_APPLICATION_BASE.getReferenceNumber(),"EUR","en",appId);
		_1Y_IOS_APPLICATION_BASE.setGroupId(1);
		_1Y_IOS_APPLICATION_BASE.setBuyLink(prefix+StringUtils.encode(buyLink));
		//System.out.println(plainLink);
		
		result.add(_1Y_IOS_APPLICATION_BASE);
		*/
		CMMSubscription ios1M = createFreeSubscription(user, user.getUuid(), appId, "JOOMLA_WEB_APP_BASE", "4566317", "Joomla Mobile Web App","0 Euro", 0.0, "", 1, true, false,"365");
		result.add(ios1M);
		return result;
	}
	
	private ArrayList<CMMSubscription> createAvaiableAppSubscriptionsNativeIPHONE(CMMUser user,String appId)
	{
		ArrayList<CMMSubscription> result = new ArrayList<CMMSubscription>();
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";
		prefix +="BILL_ZIPCODE=78640" +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() + "&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=";
		/***
		 * Resource  + Editor Pack   = 1 Month
		 */
		float rawPrice = 0.0f;
		String plainLink=null;
		String plainLink64=null;
		String buyLink=null;
		
		return null;
		
		/***
		 * 1x iOS Native Application Base
		 *
		 */
		
		/*
		CMMSubscription _1Y_IOS_APPLICATION_BASE = SubscriptionManager.hasSubscription(user.getUuid(),"4555728");
		if(_1Y_IOS_APPLICATION_BASE==null)
		{
			_1Y_IOS_APPLICATION_BASE = new CMMSubscription();
			
		}
		
		_1Y_IOS_APPLICATION_BASE.setAppId(appId);
		_1Y_IOS_APPLICATION_BASE.setIpnProductCode("1Y_IOS_APPLICATION_BASE");
		_1Y_IOS_APPLICATION_BASE.setReferenceNumber("4555728");
		_1Y_IOS_APPLICATION_BASE.setIpnName("1 native iOS Application(Base)");
		_1Y_IOS_APPLICATION_BASE.setIpnPrice("199.99 Euro / Year + VAT");
		rawPrice = 199.99f;
		_1Y_IOS_APPLICATION_BASE.setRawPrice(rawPrice);
		_1Y_IOS_APPLICATION_BASE.setIsAnnual(true);
		//plainLink = getTrialLink(_1Y_IOS_APPLICATION_BASE.getReferenceNumber(), "7", "EUR","0","en",appId);
		//plainLink64  = StringUtils.encode(plainLink);
		
		//_1Y_IOS_APPLICATION_BASE.setTrialLink(prefix+plainLink64);
		buyLink = getBuyLink(_1Y_IOS_APPLICATION_BASE.getReferenceNumber(),"EUR","en",appId);
		_1Y_IOS_APPLICATION_BASE.setGroupId(1);
		_1Y_IOS_APPLICATION_BASE.setBuyLink(prefix+StringUtils.encode(buyLink));
		//System.out.println(plainLink);
		
		result.add(_1Y_IOS_APPLICATION_BASE);
		
		CMMSubscription ios1M = createSubscription(user, user.getUuid(), appId, "1M_IOS_APPLICATION_BASE", "4559467", "1 Month iOS Application(Base Only)","19.99 Euro / Month", 19.99, "", 1, false, true);
		result.add(ios1M);
		return result;
		*/
	}
	
	/***
	 * 
	 * @param user
	 * @param appId
	 * @return
	 */
	private ArrayList<CMMSubscription> createAvaiableAppSubscriptions(CMMUser user,String appId)
	{
		
		
		ArrayList<CMMSubscription> result=null;
		
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(user.getUuid(), appId, false);
		
		String appPlatform=Constants.ALL;
		if(app!=null)
		{
			//appPlatform = app.getPlatform();
		}
		
		ArrayList<CMMSubscription> loadedSubscriptions=null;
		String fileSubSubscriptions = System.getProperty("AppStoreRoot") + "/sec/subscriptions.json";
		if(new File(fileSubSubscriptions).exists())
		{
			loadedSubscriptions = loadSubscriptions(fileSubSubscriptions);
			if(loadedSubscriptions!=null)
			{
				
				JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
		      	String serialized=serializer.deepSerialize(loadedSubscriptions);
		      	if(serialized!=null)
		      	{
		      		try {
						StringUtils.writeToFile(serialized, fileSubSubscriptions);
					} catch (IOException e) 
					{					
						e.printStackTrace();
					}
		      	}
				
				ArrayList<CMMSubscription> platformSubscriptions=getPlatformSubscriptions(loadedSubscriptions,appPlatform);
				if(platformSubscriptions!=null){
					result = adjustSubscriptions(platformSubscriptions, user, appId);
					if(result !=null)
					{
						return result;
					}
				}
			}
		}
		
		
		
		if(appPlatform.equals(Constants.USERAGENT_IPHONE_NATIVE))
		{
			result = createAvaiableAppSubscriptionsNativeIPHONE(user, appId);
		}
		//https://secure.avangate.com/order/trial.php?PRODS=4566317&QTY=1&PAY_TYPE=CCVISAMC&PRICES4566317[EUR]=0&TPERIOD=36000&PHASH=9895617c1c619075d450bea581c8284f
		if(appPlatform.equals(Constants.MOBILE_WEB_APP))
		{
			//result=createAvaiableAppSubscriptionsWebApp(user, appId);
		}
		
		
		
		result.addAll(createAvaiableAppSubscriptionsWebApp(user, appId));
		
		
		
		
		/*
		if(result!=null)
		{
			String file = System.getProperty("AppStoreRoot") + "subscriptions.json";
			JSONSerializer serializer = new JSONSerializer();//.transform(new EventDataTransformer(true,pmedia.databeans.Constants.USERAGENT_TABLET), "iconUrl");
	      	String serialized=serializer.deepSerialize(result);
	      	if(serialized!=null)
	      	{
	      		
	      		try {
					//StringUtils.writeToFile(serialized, file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      	}
		}
		*/
		
		
		
		return result;
	}
	
	public CMMSubscription createSubscription(CMMUser user ,String uuid,String appId,String productCode,String refNumber,String ipnName,String ipnPrice,double rawPrice,String savingText,int groupId,Boolean annual,Boolean monthly )
	{
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";
		
		User luser = LiferayContentTools.getLiferayUserByEMail(user.getEmail());
		
		String street = "street";
		String pcode = "07840";
		String city = "Santa Eulalia";
		String country = "Spain";
		String ccode ="ES";
		
		if(luser!=null){
			
			List<Address> addresses = null;
			try {
				 addresses=luser.getAddresses();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(addresses!=null && addresses.size() > 0){
				
				Address address = addresses.get(0);
				if(address.getStreet1()!=null)
				{
					street=address.getStreet1(); 
				}
				if(address.getCity()!=null)
				{
					city=address.getCity(); 
				}
				if(address.getCountry()!=null)
				{
					Country c = address.getCountry();
					country=c.getName(); 
					ccode = c.getIdd();
				}
				if(address.getZip()!=null)
				{
					pcode = address.getZip(); 
				}
			}
			
		}
		
		
		prefix +="BILL_ZIPCODE=" + pcode +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() +
				"&BILL_CITY="+city+ "&BILL_ADDRESS=" + street +"&BILL_COUNTRYCODE="+ccode+"&URL=";
		
		
		CMMSubscription result = SubscriptionManager.hasSubscription(uuid,appId,refNumber);
		if(result==null)
		{
			result = new CMMSubscription();
		}
		
		result.setIpnProductCode(productCode);
		result.setReferenceNumber(refNumber);
		result.setIpnName(ipnName);
		result.setIpnPrice(ipnPrice);
		result.setRawPrice((float)rawPrice);
		result.setIsAnnual(annual);
		result.setIsMonthly(monthly);
		//https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&BILL_ZIPCODE=78640&BILL_EMAIL=admin@pearls-media.com&BILL_FNAME=Guenter&BILL_LNAME=Baumgart&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=https%3A%2F%2Fsecure.avangate.com%2Forder%2Fcheckout.php%3FPRODS%3D4559467%26QTY%3D1%26CURRENCY%3DEUR%26ORDERSTYLE%3DnLWs5JapnH4%3D%26DOTEST%3D1%26REF%3D5AAB443C5816666A75178CAEB77EA38E%26DOTEST%3D1
		//String plainLink = getTrialLink(result.getReferenceNumber(), "7", "EUR","0","en","");
		/*
		plainLink64  = StringUtils.encode(plainLink);
		//_1Y_IOS_APPLICATION_BASE.setTrialLink(prefix+plainLink64);
		*/
		String buyLink = getBuyLink(result.getReferenceNumber(),"EUR","en",appId);
		//https://secure.avangate.com/order/trial.php?PRODS=4566317&QTY=1&PAY_TYPE=CCVISAMC&PRICES4566317[EUR]=0&TPERIOD=365&PHASH=00275f67838545979893bd920d01aee5
		
		//public String getBuyFreeLink(String productId,String trialPeriod,String currency,String price,String lang,String appId,String paytype)
		//buyLink = getBuyFreeLink(result.getReferenceNumber(), "365", "EUR", "0", "en", appId,"CCVISAMC");
		
		result.setBuyLink(prefix+StringUtils.encode(buyLink));
		
		//String plainLink = getBuyFreeLink(result.getReferenceNumber(), "365", "EUR","0","en","");
		
		System.out.println("########buy link : " + prefix+StringUtils.encode(buyLink));
		result.setGroupId(1);
		return result;
	}
	public CMMSubscription createFreeSubscription(CMMUser user ,String uuid,String appId,String productCode,String refNumber,String ipnName,String ipnPrice,double rawPrice,String savingText,int groupId,Boolean annual,Boolean monthly,String period )
	{
		
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";

		User luser = LiferayContentTools.getLiferayUserByEMail(user.getEmail());
		
		String street = "street";
		String pcode = "07840";
		String city = "Santa Eulalia";
		String country = "Spain";
		String ccode ="ES";
		
		if(luser!=null){
			
			List<Address> addresses = null;
			try {
				 addresses=luser.getAddresses();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(addresses!=null && addresses.size() > 0){
				
				Address address = addresses.get(0);
				if(address.getStreet1()!=null)
				{
					street=address.getStreet1(); 
				}
				if(address.getCity()!=null)
				{
					city=address.getCity(); 
				}
				if(address.getCountry()!=null)
				{
					Country c = address.getCountry();
					country=c.getA2(); 
					ccode = c.getA2();
				}
				if(address.getZip()!=null)
				{
					pcode = address.getZip(); 
				}
			}
			
		}
		
		
		prefix +="BILL_ZIPCODE="+pcode + "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() +
				"&BILL_CITY="+city+ "&BILL_ADDRESS=" + street + "&BILL_COUNTRYCODE="+ccode+"&URL=";


		
		//prefix +="BILL_ZIPCODE=78640" +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() + "&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=";
		
		CMMSubscription result = SubscriptionManager.hasSubscription(uuid,appId,refNumber);
		if(result==null)
		{
			result = new CMMSubscription();
		}
		
		result.setIpnProductCode(productCode);
		result.setReferenceNumber(refNumber);
		result.setIpnName(ipnName);
		result.setIpnPrice(ipnPrice);
		result.setRawPrice((float)rawPrice);
		result.setIsAnnual(annual);
		result.setIsMonthly(monthly);
		result.setIsFree(true);
		

		String buyLink = getBuyLink(result.getReferenceNumber(),"EUR","en",appId);
		buyLink = getBuyFreeLink(result.getReferenceNumber(), period, "EUR", ipnPrice, "en", appId, "CCVISAMC");
		result.setBuyLink(prefix+StringUtils.encode(buyLink));
		System.out.println("########buy free link : " + prefix+StringUtils.encode(buyLink));
		result.setGroupId(1);
		return result;
	}
	
	public ArrayList<CMMSubscription> getAvaiableSubscriptions(CMMUser user) 
	{
		return createAvaiableSubscriptions(user);
	}
	
	public ArrayList<CMMSubscription> getAvaiableAppSubscriptions(CMMUser user,String appId) 
	{
		return createAvaiableAppSubscriptions(user,appId);
	}
	
	
	
		
	public ArrayList<CMMSubscription> getUserSubscriptions(CMMUser user) 
	{
		ArrayList<CMMSubscription>userSubscriptions = new ArrayList<CMMSubscription>();
		
		return userSubscriptions;
		
	}

	public void setAvaiableSubscriptions(
			ArrayList<CMMSubscription> avaiableSubscriptions) {
		this.avaiableSubscriptions = avaiableSubscriptions;
	}

	private static SubscriptionManager sInstance = new SubscriptionManager();
	
	public static SubscriptionManager getInstance(){
		//ServerCache sc = ServerCache.getInstance();
		return SubscriptionManager.sInstance;
	};
	
	
	
	/*public Application getById(String uuid,String appId){
		
		for (int i  = 0  ;  i < applications.size() ;  i ++){
			Application app = applications.get(i);
			if(app.getUserIdentifier().equals(uuid) && app.getApplicationIdentifier().equals(appId)){
				return app;
			}
		}
		
		return null;
	}
	
	*/
	


	/**
	 * @return the applications
	 */
	
}
