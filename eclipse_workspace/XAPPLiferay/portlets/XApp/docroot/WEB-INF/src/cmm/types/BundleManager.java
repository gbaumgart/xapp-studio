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


public class BundleManager 
{
	
	private ArrayList<Bundle>avaiableSubscriptions;
	
	
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
	public ArrayList<Bundle>adjustBundles(ArrayList<Bundle> bundles,CMMUser user,String appId)
	{	
		ArrayList<Bundle> result= new ArrayList<Bundle>();
		for (int i = 0; i < bundles.size(); i++) 
		{
			Bundle bundle=bundles.get(i);
			if(bundle.packages==null){
				
				bundle.packages=new ArrayList<PackageInfo>();
				PackageInfo pinfo = new PackageInfo();
				pinfo.enabled=true;
				pinfo.productCode="";
				bundle.packages.add(pinfo);
				
			}
			//String prefix =getBuyPrefix(user);
			//String buyLink = getBuyLink(bundle.getReferenceNumber(),"EUR","en",appId);
			/*
			subscription.setBuyLink(prefix+StringUtils.encode(buyLink));
			subscription.setAppId(appId);
			subscription.setUuid(user.getUuid());
			*/
		}
		return bundles;
	}
	
	public ArrayList<Bundle>getPlatformBundles(ArrayList<Bundle> bundles,String platform)
	{
		ArrayList<Bundle> result= new ArrayList<Bundle>();
		for (int i = 0; i < bundles.size(); i++) 
		{
			Bundle subscription=bundles.get(i);
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
	public ArrayList<Bundle>loadBundles(String file)
	{
		ArrayList<Bundle> result = null;
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
		
		
		JSONDeserializer<ArrayList<Bundle> >deserializer= new JSONDeserializer();
		if(description!=null && description.length() > 0)
		{
			result=  deserializer.deserialize(description);
		}
		
		return result;
	}
	
	public ArrayList<Package>loadPackages(String file)
	{
		ArrayList<Package> result = null;
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
		
		
		JSONDeserializer<ArrayList<Package> >deserializer= new JSONDeserializer();
		if(description!=null && description.length() > 0)
		{
			result=  deserializer.deserialize(description);
		}
		
		return result;
	}
	
	private static final String trialBaseUrl = "https://secure.avangate.com/order/trial.php?";
	private static final String orderBaseUrl = "https://secure.avangate.com/order/checkout.php?";
	private static final String secretKey = "P2_8+wn8^8x33F@N9(q^";
	
	public String getBuyFreeLink(String productId,String trialPeriod,String currency,String price,String lang,String appId,String paytype)
	{
		String result = BundleManager.trialBaseUrl;
		
		String productParameters ="PRODS="+productId;
		
		//add currency : 
		String parameters="&CURRENCY="+currency;
		
		//add prices : 
		String priceParameter="&PRICES"+productId+"["+currency+"]=" + price;
		
		//add trial period: 
		String trialParameter ="&TPERIOD="+trialPeriod;
		
		//add hash code : 
		String key = BundleManager.secretKey;

		
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
		String result = BundleManager.orderBaseUrl;
		
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
		String result = BundleManager.trialBaseUrl;
		
		String productParameters ="PRODS="+productId;
		
		//add currency : 
		String parameters="&CURRENCY="+currency;
		
		//add prices : 
		String priceParameter="&PRICES"+productId+"["+currency+"]=" + price;
		
		//add trial period: 
		String trialParameter ="&TPERIOD="+trialPeriod;
		
		//add hash code : 
		String key = BundleManager.secretKey;

		
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
	

	private ArrayList<Bundle> createAvaiableBundles(CMMUser user)
	{
		
		
		ArrayList<Bundle> result = new ArrayList<Bundle>();
		
		String prefix ="https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&";
		prefix +="BILL_ZIPCODE=78640" +  "&BILL_EMAIL=" + user.getEmail() + "&BILL_FNAME=" + user.getFirstName() + "&BILL_LNAME=" + user.getLastName() + "&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=";

		
		
		return result;
		
	}
	/***
	 * 
	 * @param user
	 * @param appId
	 * @return
	 */
	private ArrayList<Package> createPackages(CMMUser user,String appId)
	{
		ArrayList<Package> result=null;
		
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(user.getUuid(), appId, false);
		
		String appPlatform=Constants.ALL;
		if(app!=null)
		{
			//appPlatform = app.getPlatform();
		}
		
		ArrayList<Package> loadedPackages=null;
		String filePackages = System.getProperty("AppStoreRoot") + "packages.json";
		
		if(new File(filePackages).exists())
		{
			loadedPackages = loadPackages(filePackages);
			
			if(loadedPackages!=null)
			{
				return loadedPackages;
				
				/*
				JSONSerializer serializer = new JSONSerializer();
		      	String serialized=serializer.deepSerialize(loadedPackages);
		      	if(serialized!=null)
		      	{
		      		try {
						StringUtils.writeToFile(serialized, fileSubSubscriptions);
					} catch (IOException e) 
					{					
						e.printStackTrace();
					}
		      	}
		      	*/

		      	/*
		      	ArrayList<Bundle> platformSubscriptions=getPlatformSubscriptions(loadedSubscriptions,appPlatform);
				if(platformSubscriptions!=null){
					result = adjustSubscriptions(platformSubscriptions, user, appId);
					if(result !=null)
					{
						return result;
					}
				}
				*/
			}
		}
		
		
		//result.addAll(createAvaiableAppSubscriptionsWebApp(user, appId));

		return result;
	}
	/***
	 * 
	 * @param user
	 * @param appId
	 * @return
	 */
	private ArrayList<Bundle> createBundles(CMMUser user,String appId)
	{
		
		
		ArrayList<Bundle> result=null;
		
		ApplicationManager appManager = ApplicationManager.getInstance();
		Application app = appManager.getApplication(user.getUuid(), appId, false);
		
		String appPlatform=Constants.ALL;
		if(app!=null)
		{
			//appPlatform = app.getPlatform();
		}
		
		ArrayList<Bundle> loadedSubscriptions=null;
		String fileSubSubscriptions = System.getProperty("AppStoreRoot") + "/sec/bundles.json";
		if(new File(fileSubSubscriptions).exists())
		{
			loadedSubscriptions = loadBundles(fileSubSubscriptions);
			if(loadedSubscriptions!=null)
			{
				
				ArrayList<Bundle> platformBundles=getPlatformBundles(loadedSubscriptions,appPlatform);
				if(platformBundles!=null){
					result = adjustBundles(platformBundles, user, appId);
					if(result !=null)
					{
						
						
						JSONSerializer serializer = new JSONSerializer();
						serializer.prettyPrint(true);
				      	String serialized=serializer.deepSerialize(result);
				      	
				      	if(serialized!=null)
				      	{
				      		try {
								StringUtils.writeToFile(serialized, fileSubSubscriptions);
							} catch (IOException e) 
							{					
								e.printStackTrace();
							}
				      	}
						
						return result;
					}
				}
			}
		}
		//result.addAll(createAvaiableAppSubscriptionsWebApp(user, appId));

		return result;
	}
	
	
	public ArrayList<Bundle> getAvaiableBundles(CMMUser user) 
	{
		return createAvaiableBundles(user);
	}
	
	public ArrayList<Bundle> getBundles(CMMUser user,String appId) 
	{
		return createBundles(user,appId);
	}
	
		
	public ArrayList<Bundle> getUserSubscriptions(CMMUser user) 
	{
		ArrayList<Bundle>userSubscriptions = new ArrayList<Bundle>();
		
		return userSubscriptions;
		
	}

	public void setAvaiableSubscriptions(
			ArrayList<Bundle> avaiableSubscriptions) {
		this.avaiableSubscriptions = avaiableSubscriptions;
	}

	private static BundleManager sInstance = new BundleManager();
	
	public static BundleManager getInstance(){
		//ServerCache sc = ServerCache.getInstance();
		return BundleManager.sInstance;
	};
	
	
}
