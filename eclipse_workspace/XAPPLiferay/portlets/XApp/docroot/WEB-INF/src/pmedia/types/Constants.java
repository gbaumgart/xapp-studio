package pmedia.types;

/**
 * This class holds most of the constants for the platform.  They are here
 * for convenience.
 *
 */
public class Constants
{

   /**
    * Used in various event classes to define where to forward
    * to on different conditions.  See the struts-config.xml file
    * to see where the page that is using this forwards to.
    */
   public static final String FORWARD_SUCCESS = "success";
   
   public static String USERAGENT_PC = "PC";
   public static String USERAGENT_IPHONE = "IPHONE";
   public static String USERAGENT_IPHONE4 = "IPHONE4";
   
   public static String USERAGENT_IPHONE_NATIVE = "IPHONE_NATIVE";
   public static String USERAGENT_IPHONE4_NATIVE = "IPHONE4_NATIVE";
   
   public static String USERAGENT_IPAD = "IPAD";
   public static String USERAGENT_IPAD_NATIVE = "IPAD_NATIVE";
   public static String USERAGENT_ANDROID = "ANDROID";
   public static String USERAGENT_FACEBOOK = "FACEBOOK";
   public static String USERAGENT_TABLET = "TABLET";
   public static String USERAGENT_BLACKBERRY = "BLACKBERRY";
   public static String USERAGENT_DEFAULT = USERAGENT_PC;
   public static String MOBILE_WEB_APP = "MOBILE_WEB_APP";
   public static String MOBILE_WEB_APP_TABLET = "MOBILE_WEB_APP_TABLET";
   public static String ALL = "ALL";
   /**
    * Used in various event classes to define where to forward
    * to on different conditions.  See the struts-config.xml file
    * to see where the page that is using this forwards to.
    */
   public static final String FORWARD_FAILURE = "failure";

   /**
    * Used in various event classes to define where to forward
    * to on different conditions.  See the struts-config.xml file
    * to see where the page that is using this forwards to.
    */
   public static final String FORWARD_PAYMENT = "payment";

   /**
    * Used in various event classes to define where to forward
    * to on different conditions.  See the struts-config.xml file
    * to see where the page that is using this forwards to.
    */
   public static final String FORWARD_SHIPPING = "shipping";

   /**
    * Used in various event classes to define where to forward
    * to on different conditions.  See the struts-config.xml file
    * to see where the page that is using this forwards to.
    */
   public static final String FORWARD_SHOPPINGCART = "shoppingcart";

   /**
    * Used in various event classes to define where to forward
    * to on different conditions.  See the struts-config.xml file
    * to see where the page that is using this forwards to.
    */
   public static final String FORWARD_REGISTER = "register";

   /**
    * Used to designate the language English
    */
   public static final String LANGUAGE_EN = "en";

   /**
    * Used to designate the country US
    */
   public static final String COUNTRY_US = "us";

   /**
    * Used in the jsp's to tell whether or not the shipping
    * address is the same as the billing address.
    */
   public static final String SHIPDIFF_SAME = "same";

   /**
    * The status code for an order
    */
   public static final String STATUS_CODE = "1";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_BILLINGPROFILE = "billingProfile";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_SHIPPINGPROFILE = "shippingProfile";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_ORDER = "order";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_PROFILE = "profile";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_USER = "user";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_LASTPRODUCT = "lastProduct";

   /**
    * 
    * 
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_SEARCH = "search";
   
   
   public static final String ATT_SEARCH_GROUPED = "Grouped";
   public static final String ATT_SEARCH_VALID_LOCATION = "ValidLocation";
   public static final String ATT_SEARCH_TRUSTED_ONLY = "TrustedOnly";
   
   
   public static final String ATTRIBUTE_CURRENT_LOCATIONS = "currentLocations";
   
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_CACHE = "cache";
   
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_REPORT_CACHE = "reportCache";
   
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String REPORT_EVENT_RESULT = "reportEventResult";
   
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String REPORT_CATEGORY_RESULT = "reportCategoryResult";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String REPORT_EVENT_RESULT_ITERATOR = "reportEventResultIterator";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String REPORT_TYPE= "reportType";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_REPORT_SEARCH = "reportSearch";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_LOCATION_SEARCH = "locationSearch";
   
   public static final String ATTRIBUTE_FB_SEARCH_STRING = "fbSearchString";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_LINK_SEARCH = "linkSearch";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_LINK_SEARCH_FB = "linkSearchFB";
   
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_PICTURE_SEARCH = "pictureSearch";
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_FB_USER_EVENTS_SEARCH = "fbUserEvents";
   
   
   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_FB_USER_EVENTS_RESULT_ITERATOR = "fbUserEventsIterator";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_DBA = "dba";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_ERRORMSG = "errorMsg";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_SEARCHTYPE = "searchType";

   /**
    * The name of an attribute set in the session.
    */
   public static final String ATTRIBUTE_KEYWORD = "keyword";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_EXECUTESEARCH = "executeSearch";
   
   
   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_MODIFYSEARCH = "modifySearch";
   
   
   

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_ALBUM = "Album";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_ARTIST = "Artist";
   
   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_GENERIC = "Generic";
   public static final String PARAMETER_CATEGORY = "Category";
   
   
   /**
    * 
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_FACEBOOK = "Facebook";
   
   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_TICKETS = "Tickets";
   
   
   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_ARTISTID = "ArtistID";
   
   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_USERID= "UserID";
   
   public static final String PARAMETER_KEYWORDID= "KeywordID";
   
   

   

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_GENRE = "Genre";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_GENREID = "genreID";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_ACTION = "event";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_ADDITEM = "addItem";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_REMOVEALLITEMS = "removeAllItems";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_UPDATEQUANTITIES = "updateQuantities";

   /**
    * The name of a parameter from a form submission.
    */
   public static final String PARAMETER_TEMPID = "id:";
}
