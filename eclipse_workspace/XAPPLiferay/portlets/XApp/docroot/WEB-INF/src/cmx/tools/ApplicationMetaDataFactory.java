package cmx.tools;
import java.util.ArrayList;

import org.w3c.tidy.PPrint;

import cmx.types.CIType;
import cmx.types.ConfigurableInformation;
import pmedia.types.ApplicationMetaData;
import pmedia.types.ApplicationMetaDataKeys;
import pmedia.utils.CITools;

public class ApplicationMetaDataFactory 
{
	public static void setDefaults(ApplicationMetaData appMeta,String applicationId)
	{

		if(appMeta.getProperties()==null){
			appMeta.setProperties(new ArrayList<ConfigurableInformation>());
		}
		/****
		 * Commons
		 */

		
		//CM_PROMO_PIC_URL_0
		ConfigurableInformation cmIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_ICON_0);
		if(cmIcon0==null)
		{
			cmIcon0 = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.CM_APP_ICON_0, null, 
					"Application Icon");
			appMeta.getProperties().add(cmIcon0);
		}
		cmIcon0.setOrder(1);
		cmIcon0.setParentId(applicationId);
		
		//CM_PROMO_PIC_URL_0
		ConfigurableInformation cmPromoPic0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_PROMO_PIC_URL_0);
		if(cmPromoPic0==null)
		{
			cmPromoPic0 = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.CM_PROMO_PIC_URL_0, null, 
					"Promotion Picture");
			appMeta.getProperties().add(cmPromoPic0);
		}
		cmPromoPic0.setOrder(2);
		cmPromoPic0.setParentId(applicationId);
		
		//CM_APP_TITLE
		ConfigurableInformation cmAppTitle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_TITLE);
		if(cmAppTitle==null)
		{
			cmAppTitle = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.CM_APP_TITLE, null, 
					"Title");
			appMeta.getProperties().add(cmAppTitle);
		}
		cmAppTitle.setOrder(0);
		cmAppTitle.setParentId(applicationId);

		
		//CM_APP_SCHEMA_PREFIX
		ConfigurableInformation cmAppSchemaPrefix = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_SCHEMA_PREFIX);
		if(cmAppSchemaPrefix==null)
		{
			cmAppSchemaPrefix = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.CM_APP_SCHEMA_PREFIX, null, 
					"Internal Url Schema Prefix");
			cmAppSchemaPrefix.setVisible(false);
			appMeta.getProperties().add(cmAppSchemaPrefix);
		}
		cmAppSchemaPrefix.setParentId(applicationId);
		
		
		//CM_APP_ICON_SET
		ConfigurableInformation cmIconSet = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_ICON_SET);
		if(cmIconSet==null)
		{
			cmIconSet = CIFactory.AppMetaDataCI(CIType.ICON_SET, ApplicationMetaDataKeys.CM_APP_ICON_SET, null, 
					"Icons");
			cmIconSet.setValue("monoWhite");
			appMeta.getProperties().add(cmIconSet);
		}
		cmIconSet.setOrder(3);
		cmIconSet.setParentId(applicationId);
		
		//CM_APP_TITLE
		ConfigurableInformation cmDescr = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_DESCRIPTION);
		if(cmDescr==null)
		{
			cmDescr = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.CM_APP_DESCRIPTION, null, 
					"Description");
			
			appMeta.getProperties().add(cmDescr);
		}
		cmDescr.setOrder(3);
		cmDescr.setParentId(applicationId);
		
		/****
		 * Facebook
		 */
		//FB_SHARE_APP_ID
		ConfigurableInformation cmFBAppId = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.FB_SHARE_APP_ID);
		if(cmFBAppId==null)
		{
			cmFBAppId = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.FB_SHARE_APP_ID, null, 
					"Application Id");
			appMeta.getProperties().add(cmFBAppId);
		}
		cmFBAppId.setParentId(applicationId);
		
		//FB_SHARE_APP_ID
		ConfigurableInformation cmFBAppSecret = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.FB_SHARE_APP_SECRET);
		if(cmFBAppSecret==null)
		{
			cmFBAppSecret= CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.FB_SHARE_APP_SECRET, null, 
					"Application Secret");
			appMeta.getProperties().add(cmFBAppSecret);
		}
		cmFBAppSecret.setParentId(applicationId);
		/***
		 * Googles
		 */
		
		//GOOGLE_TRACKER_ID
		ConfigurableInformation gTrackerId = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.GOOGLE_TRACKER_ID);
		if(gTrackerId==null)
		{
			gTrackerId = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.GOOGLE_TRACKER_ID, null, 
					"Google Analytics UA");
			
			appMeta.getProperties().add(gTrackerId);
		}
		gTrackerId.setParentId(applicationId);
		
		/***
		 * Apple
		 */
		
		//Apple
		
		ConfigurableInformation gAppleBundle = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_BUNDLE_ID);
		if(gAppleBundle==null)
		{
			gAppleBundle = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.APPLE_BUNDLE_ID, null, 
					"Apple Bundle Id");
			appMeta.getProperties().add(gAppleBundle);
		}
		gAppleBundle.setParentId(applicationId);
		
		
		//apple iphone icon
		ConfigurableInformation gAppleIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_0);
		if(gAppleIcon0==null)
		{
			gAppleIcon0 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_ICON_0, null, 
					"Apple App Store Icon");
			
			appMeta.getProperties().add(gAppleIcon0);
		}
		gAppleIcon0.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIcon0.setParentId(applicationId);
		
		
		ConfigurableInformation gAppleIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_1);
		if(gAppleIcon1==null)
		{
			gAppleIcon1 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_ICON_1, null, 
					"Apple App Store Retina Icon");
			
			appMeta.getProperties().add(gAppleIcon1);
		}
		gAppleIcon1.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIcon1.setParentId(applicationId);
		
		
		ConfigurableInformation gAppleIcon2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_ICON_2);
		if(gAppleIcon2==null)
		{
			gAppleIcon2 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_ICON_2, null, 
					"Apple App Store Retina Icon 2");
			
			appMeta.getProperties().add(gAppleIcon2);
		}
		gAppleIcon2.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIcon2.setParentId(applicationId);
		
		//apple ipad icon
		ConfigurableInformation gAppleIPADIcon0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_0);
		if(gAppleIPADIcon0==null)
		{
			gAppleIPADIcon0 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_IPAD_ICON_0, null, 
					"Apple App Store iPad Icon");
			
			appMeta.getProperties().add(gAppleIPADIcon0);
		}
		gAppleIPADIcon0.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIPADIcon0.setParentId(applicationId);
		//1
		ConfigurableInformation gAppleIPADIcon1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_1);
		if(gAppleIPADIcon1==null)
		{
			gAppleIPADIcon1 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_IPAD_ICON_1, null, 
					"Apple App Store iPad Retina Icon");
			
			appMeta.getProperties().add(gAppleIPADIcon1);
		}
		gAppleIPADIcon1.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIPADIcon1.setParentId(applicationId);
		//2
		ConfigurableInformation gAppleIPADIcon2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_2);
		if(gAppleIPADIcon2==null)
		{
			gAppleIPADIcon2 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_IPAD_ICON_2, null, 
					"Apple App Store iPad Icon");
			
			appMeta.getProperties().add(gAppleIPADIcon2);
		}
		gAppleIPADIcon2.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIPADIcon2.setParentId(applicationId);
		
		//2
		ConfigurableInformation gAppleIPADIcon3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_ICON_3);
		if(gAppleIPADIcon3==null)
		{
			gAppleIPADIcon3 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_IPAD_ICON_3, null, 
					"Apple App Store iPad Icon");
			
			appMeta.getProperties().add(gAppleIPADIcon3);
		}
		gAppleIPADIcon3.setType(CITools.CIToInteger(CIType.ICON));
		gAppleIPADIcon3.setParentId(applicationId);
		
		
		//launch icons
		
		
		
		ConfigurableInformation aLaunch1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_0);
		if(aLaunch1==null)
		{
			aLaunch1 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.APPLE_LAUNCH_0, null, 
					"Apple Launch Screen");
			
			appMeta.getProperties().add(aLaunch1);
		}
		aLaunch1.setType(CITools.CIToInteger(CIType.IMAGE));
		aLaunch1.setParentId(applicationId);
		
		/***
		 * smartphone launch : 
		 */
		ConfigurableInformation aLaunch2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_1);
		if(aLaunch2==null)
		{
			aLaunch2 = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_LAUNCH_1, null, 
					"Apple Launch Screen 2");
			
			appMeta.getProperties().add(aLaunch2);
		}
		aLaunch2.setType(CITools.CIToInteger(CIType.IMAGE));
		aLaunch2.setParentId(applicationId);
		
		ConfigurableInformation aLaunch3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_2);
		if(aLaunch3==null)
		{
			aLaunch3 = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_LAUNCH_2, null, "Apple Launch Screen iPhone5");
			appMeta.getProperties().add(aLaunch3);
		}
		aLaunch3.setParentId(applicationId);
		aLaunch3.setType(CITools.CIToInteger(CIType.IMAGE));
		
		ConfigurableInformation aLaunch4 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_3);
		if(aLaunch4==null)
		{
			aLaunch4 = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_LAUNCH_3, null, "Apple Launch Screen iPhone5 Retina");
			appMeta.getProperties().add(aLaunch4);
		}
		aLaunch4.setParentId(applicationId);
		aLaunch4.setType(CITools.CIToInteger(CIType.IMAGE));
		
		ConfigurableInformation aLaunch5 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LAUNCH_4);
		if(aLaunch5==null)
		{
			aLaunch5 = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_LAUNCH_4, null, "Apple Launch Screen iPhone5 Retina");
			appMeta.getProperties().add(aLaunch5);
		}
		aLaunch5.setParentId(applicationId);
		
		ConfigurableInformation appFlags = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APP_FLAGS);
		if(appFlags==null)
		{
			appFlags = CIFactory.AppMetaDataCI(CIType.FLAGS, ApplicationMetaDataKeys.APP_FLAGS, null, "Application Settings");
			appFlags.setValue("0");
			appFlags.dataRef = "{\"items\":[{\"label\":\"Prevent Cache\",\"value\":2,\"description\":\"Prevents the application to accept cached settings\"},{\"label\":\"No Offline Cache\",\"value\":3,\"description\":\"Don\'t cache content in application cache\"},{\"label\":\"No Manifest\",\"value\":4,\"description\":\"Don\'t allow HTML manifest\"},{\"label\":\"No Server Cache\",\"value\":5,\"description\":\"Prevent server side cache\"},{\"label\":\"Force debug\",\"value\":6,\"description\":\"Forces debug version\"},{\"label\":\"Force phone\",\"value\":7,\"description\":\"Disable tablet version\"}]}";
			appMeta.getProperties().add(appFlags);
		}
		appFlags.setParentId(applicationId);
		
		
		//{\"items\":[{\"label\":\"Prevent Cache\",\"value\":2,\"description\":\"Prevents the application to accept cached settings\"},{\"label\":\"No Offline Cache\",\"value\":3,\"description\":\"Don\'t cache content in application cache\"},{\"label\":\"No Manifest\",\"value\":4,\"description\":\"Don\'t allow HTML manifest\"},{\"label\":\"No Server Cache\",\"value\":5,\"description\":\"Prevent server side cache\"},{\"label\":\"Force debug\",\"value\":6,\"description\":\"Forces debug version\"},{\"label\":\"Force phone\",\"value\":7,\"description\":\"Disable tablet version\"}]}
		
		
		/***
		 * fixing
		 */
		/*
		ConfigurableInformation testLaunch = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_0);
		if(testLaunch !=null)
		{
			appMeta.getProperties().remove(testLaunch);
		}
		testLaunch=null;
		testLaunch = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_1);
		if(testLaunch !=null)
		{
			appMeta.getProperties().remove(testLaunch);
		}
		testLaunch=null;
		testLaunch = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_2);
		if(testLaunch !=null)
		{
			appMeta.getProperties().remove(testLaunch);
		}
		testLaunch=null;
		testLaunch = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_2);
		if(testLaunch !=null)
		{
			appMeta.getProperties().remove(testLaunch);
		}
		*/
		
		
		
		
		
		/***
		 * iPad Launch Images
		 */
		ConfigurableInformation aIPADLaunch0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_0);
		if(aIPADLaunch0 ==null)
		{
			aIPADLaunch0  = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_0, null, "Apple iPad Launch Screen Non-Retina (iOS 6.1 Prior)");
			appMeta.getProperties().add(aIPADLaunch0);
		}
		aIPADLaunch0.setParentId(applicationId);
		
		/***
		 * iPad Launch Image 1
		 */
		ConfigurableInformation aIPADLaunch1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_1);
		if(aIPADLaunch1 ==null)
		{
			aIPADLaunch1  = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_1, null, "Apple iPad Launch Screen Retina (iOS 6.1 Prior)");
			appMeta.getProperties().add(aIPADLaunch1);
		}
		aIPADLaunch1.setParentId(applicationId);
		
		/***
		 * iPad Launch Images
		 */
		ConfigurableInformation aIPADLaunch2 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_2);
		if(aIPADLaunch2 ==null)
		{
			aIPADLaunch2  = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_2, null, "Apple iPad Launch Screen Non-Retina");
			appMeta.getProperties().add(aIPADLaunch2);
		}
		aIPADLaunch0.setParentId(applicationId);
		
		/***
		 * iPad Launch Image 1
		 */
		ConfigurableInformation aIPADLaunch3 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_3);
		if(aIPADLaunch3 ==null)
		{
			aIPADLaunch3  = CIFactory.AppMetaDataCI(CIType.IMAGE, ApplicationMetaDataKeys.APPLE_IPAD_LAUNCH_3, null, "Apple iPad Launch Screen Retina");
			appMeta.getProperties().add(aIPADLaunch3);
		}
		aIPADLaunch3.setParentId(applicationId);
		
		
		
		
		
		
		
		/***
		 * tablet launch
		 */
		ConfigurableInformation tabletLaunch0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TABLET_LAUNCH_1);
		if(tabletLaunch0==null)
		{
			tabletLaunch0 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.TABLET_LAUNCH_1, null, 
					"Tablet Launch Screen Portrait");
			
			appMeta.getProperties().add(tabletLaunch0);
		}
		tabletLaunch0.setType(CITools.CIToInteger(CIType.ICON));
		tabletLaunch0.setParentId(applicationId);
		
		ConfigurableInformation tabletLaunch1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TABLET_LAUNCH_2);
		if(tabletLaunch1==null)
		{
			tabletLaunch1 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.TABLET_LAUNCH_2, null, 
					"Tablet Launch Screen Landscape");
			
			appMeta.getProperties().add(tabletLaunch1);
		}
		tabletLaunch1.setType(CITools.CIToInteger(CIType.ICON));
		tabletLaunch1.setParentId(applicationId);
		
		
		/***
		 * tablet back
		 */
		ConfigurableInformation tabletBG0 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TABLET_BACKGROUND_1);
		if(tabletBG0==null)
		{
			tabletBG0 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.TABLET_BACKGROUND_1, null, 
					"Tablet Background Portrait");
			
			appMeta.getProperties().add(tabletBG0);
		}
		tabletBG0.setType(CITools.CIToInteger(CIType.ICON));
		tabletBG0.setParentId(applicationId);
		
		ConfigurableInformation tabletBG1 = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TABLET_BACKGROUND_2);
		if(tabletBG1==null)
		{
			tabletBG1 = CIFactory.AppMetaDataCI(CIType.ICON, ApplicationMetaDataKeys.TABLET_BACKGROUND_2, null, 
					"Tablet Background Screen Landscape");
			
			appMeta.getProperties().add(tabletBG1);
		}
		tabletBG1.setType(CITools.CIToInteger(CIType.ICON));
		tabletBG1.setParentId(applicationId);
		
		
		
		/***
		 * 
		 */
		
		//apple app keywords
		ConfigurableInformation akw = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_KEYWORDS);
		if(akw==null)
		{
			akw = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.APPLE_KEYWORDS, null, 
					"App Keywords");
			appMeta.getProperties().add(akw);
		}
		akw.setParentId(applicationId);
		
		
		//apple app store login
		ConfigurableInformation aUser = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LOGIN_USER);
		if(aUser==null)
		{
			aUser = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.APPLE_LOGIN_USER, null, 
					"Apple App Store Login");
			appMeta.getProperties().add(aUser);
		}
		aUser.setParentId(applicationId);
		
		//pass
		ConfigurableInformation aPass = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.APPLE_LOGIN_PASS);
		if(aPass==null)
		{
			aPass = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.APPLE_LOGIN_PASS, null, 
					"Apple App Store Login Password");
			appMeta.getProperties().add(aPass);
		}
		aPass.setParentId(applicationId);

		

		//CM_APP_TITLE
		ConfigurableInformation cmAppVT = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.CM_APP_VISUAL_TEMPLATE);
		if(cmAppVT==null)
		{
			cmAppVT = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.CM_APP_VISUAL_TEMPLATE, null, 
					"Visual Template");
			appMeta.getProperties().add(cmAppVT);
		}
		cmAppVT.setParentId(applicationId);
		
		
		
		
		
		/***
		 * Twitter 
		 */
		ConfigurableInformation twCKey = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TWITTER_CONSUMER_KEY);
		if(twCKey==null)
		{
			twCKey = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.TWITTER_CONSUMER_KEY, null, 
					"Twitter Consumer Key");
			appMeta.getProperties().add(twCKey);
		}
		twCKey.setParentId(applicationId);
		
		ConfigurableInformation twSecret = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TWITTER_CONSUMER_SECRET);
		if(twSecret==null)
		{
			twSecret = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.TWITTER_CONSUMER_SECRET, null, 
					"Twitter Secret");
			appMeta.getProperties().add(twSecret);
		}
		twSecret.setParentId(applicationId);
		
		ConfigurableInformation twUrl = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.TWITTER_CONSUMER_CALL_BACK_URL);
		if(twUrl==null)
		{
			twUrl= CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.TWITTER_CONSUMER_CALL_BACK_URL, null, 
					"Twitter Callback Url");
			appMeta.getProperties().add(twUrl);
		}
		twUrl.setParentId(applicationId);
		
		
		/***
		 * BitLy
		 */
		ConfigurableInformation blLogin = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.BITLY_LOGIN);
		if(blLogin==null)
		{
			blLogin = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.BITLY_LOGIN, null, 
					"Bitly Login");
			appMeta.getProperties().add(blLogin);
		}
		blLogin.setParentId(applicationId);
		
		ConfigurableInformation blKey = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.BITLY_KEY);
		if(blKey==null)
		{
			blKey = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.BITLY_KEY, null, 
					"Bitly Key");
			appMeta.getProperties().add(blKey);
		}
		blKey.setParentId(applicationId);
		
		
		/***
		 * Four Square 
		 */
		ConfigurableInformation fsKey = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.FS_KEY);
		if(fsKey==null)
		{
			fsKey = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.FS_KEY, null, 
					"Foursquare Client Id");
			appMeta.getProperties().add(fsKey);
		}
		fsKey.setParentId(applicationId);
		
		ConfigurableInformation fsUrl = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.FS_RURL);
		if(fsUrl==null)
		{
			fsUrl= CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.FS_RURL, null, 
					"Foursquare Redirect Url");
			appMeta.getProperties().add(fsUrl);
			fsUrl.setValue("app://foursquare");
		}
		fsUrl.setParentId(applicationId);
		
		/***
		 * Android - Package 
		 */
		ConfigurableInformation androidPackage = CITools.getById(appMeta.getProperties(), ApplicationMetaDataKeys.ANDROID_PACKAGE);
		if(androidPackage==null)
		{
			androidPackage = CIFactory.AppMetaDataCI(CIType.STRING, ApplicationMetaDataKeys.ANDROID_PACKAGE, null, 
					"Android Package");
			appMeta.getProperties().add(androidPackage);
		}
		androidPackage.setParentId(applicationId);
	}
}
