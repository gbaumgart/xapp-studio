package cmm.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.xml.sax.SAXException;

import com.liferay.portal.kernel.mail.MailMessage;

import cmm.types.CMMSubscription;
import cmm.types.SubscriptionManager;
import cmx.tools.Crypto;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.SQLDataSource;

import pmedia.DataJSONTransformer.ArticleDataTranslationTransformer;
import pmedia.DataJSONTransformer.BaseDataTransformer;
import pmedia.DataManager.Cache;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;

import pmedia.DataUtils.ArticleTools;
import pmedia.DataUtils.CategoryTools;
import pmedia.DataUtils.MediaUtils;
import pmedia.SearchBeans.EventSearch;

import pmedia.types.ArticleData;
import pmedia.types.Category;
import pmedia.types.Domain;
import pmedia.types.LocationData;
import pmedia.types.MediaItemBase;
import pmedia.types.PMDataTypes;
import pmedia.utils.StringUtils;
import flexjson.JSONSerializer;


public class AvangateServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    protected void doContent(PrintWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
    	System.out.println("ffffPost");
    }
    
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {

    	//System.out.println("%%% IPN NOTIFCATION");
    	String html="";
    	System.out.println("title" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1 ALIGN=CENTER>" + "No Title" + "</H1>\n" +
                "<TABLE BORDER=1 ALIGN=CENTER>\n" +
                "<TR BGCOLOR=\"#FFAD00\">\n" +
                "<TH>Parameter Name<TH>Parameter Value(s)");
    	
    	html="title" +
        "<BODY BGCOLOR=\"#FDF5E6\">\n" +
        "<H1 ALIGN=CENTER>" + "No Title" + "</H1>\n" +
        "<TABLE BORDER=1 ALIGN=CENTER>\n" +
        "<TR BGCOLOR=\"#FFAD00\">\n" +
        "<TH>Parameter Name<TH>Parameter Value(s)";
    	
    	StringBuffer postParms = request.getRequestURL();
    	Enumeration paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
          String paramName = (String)paramNames.nextElement();
          System.out.println("<TR><TD>" + paramName + "\n<TD>");
          html+="<TR><TD>" + paramName + "\n<TD>";
          String[] paramValues = request.getParameterValues(paramName);
          if (paramValues.length == 1) {
            String paramValue = paramValues[0];
            if (paramValue.length() == 0){
            	System.out.print("<I>No Value</I>");
            html+="<I>No Value</I>";
            }
            else
            	System.out.print(paramValue);
            html+=paramValue;
          } else {
        	  System.out.println("<UL>");
        	  html+="<UL>";
            for(int i=0; i<paramValues.length; i++) {
            	System.out.println("<LI>" + paramValues[i]);
            	html+="<LI>" + paramValues[i];
            }
            System.out.println("</UL>");
            html+="</UL>";
          }
        }
        
        System.out.println("</TABLE>\n</BODY></HTML>");
        html+="</TABLE>\n</BODY></HTML>";
        
        String customerEMAIL = request.getParameter("CUSTOMEREMAIL");
        String ipnPID = request.getParameter("IPN_PID[]");
        String ipnPCODE = request.getParameter("IPN_PCODE[]");
        String referrer = request.getParameter("IPN_REFERRER");
        String paymethod = request.getParameter("PAYMETHOD");
        String licenseType = request.getParameter("IPN_LICENSE_TYPE[]");
        String currency = request.getParameter("CURRENCY");
        String salesDate = request.getParameter("PAYMENTDATE");
        String expiresDate = request.getParameter("IPN_LICENSE_EXP[]");
        String price = request.getParameter("IPN_PRICE[]");
        String hash = request.getParameter("HASH");
        String orderStatus = request.getParameter("ORDERSTATUS");
        String orderLanguage = request.getParameter("LANGUAGE");
        String ipAdress = request.getParameter("IPADDRESS");
        String ref = request.getParameter("REFNOEXT");
        

        com.liferay.portal.kernel.mail.MailMessage msg = new MailMessage();
        msg.setSubject("OnSubscription from Avanagate : " + customerEMAIL + " :: ref : " + ref + " :: ");
        msg.setHTMLFormat(true);
        
        html+="<br/><br/>";
        //com.liferay.mail.service.MailServiceUtil.sendEmail(com.liferay.portal.kernel.mail.MailMessage message)
        
        
        /***
         * Data Conversations
         */
        
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dSalesDate = null;
        if(salesDate!=null){
	        try {
				dSalesDate = format.parse(salesDate);
			} catch (ParseException e) 
			{
				e.printStackTrace();
			}
        }
        
        format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dExpirationDate = null; 
	        
        if(expiresDate!=null)
        {
	        try {
				dExpirationDate = format.parse(expiresDate);
			} catch (ParseException e) 
			{
				e.printStackTrace();
			}
        }
        
        CMMSubscription s = new CMMSubscription();
        if(ref!=null)
        {
        	if( ref==null || ref.equals(""))
        	{
        		System.out.println("-------------------------------------------HAVE NO REF");
        		return;
        	}
	        String key = "asd28071977";
	        Crypto encrypter = new Crypto(key);
	        s.setAppId(encrypter.decrypt(ref));
        }
        s.setCustomerEMail(customerEMAIL);
        s.setIpnProductCode(ipnPCODE);//1M_REPACK
        s.setReferenceNumber(ipnPID);//4555627
        s.setOrderStatus(orderStatus);
        s.setHash(hash);
        s.setIpnPayMethod(paymethod);
        s.setLicenseType(licenseType);
        s.setOrderLanguage(orderLanguage);
        s.setSalesDate(dSalesDate);
        s.setExpirationDate(dExpirationDate);
        s.setIpAddress(ipAdress);
        s.setIpnPrice(price);
        s.setReferrer(referrer);
        
        SubscriptionManager manager = SubscriptionManager.getInstance();
        manager.onSubscriptionPurchase(s);
        
        JSONSerializer serializer = new JSONSerializer();
        if(s!=null){
        	html+=serializer.deepSerialize(s);
        }
        msg.setBody(html);
        MailMessage message = null;
        InternetAddress from = new InternetAddress("form@pearls-media.com", "Liferay");
        message  = new MailMessage(from, from, "On Subscription : " + s.getCustomerEMail() + " :: " + s.getReferenceNumber() + " :: " + s.getAppId() + " :: " + s.getIpnPrice() + "  :: " +s.getIpnProductCode() , html, true);
        com.liferay.mail.service.MailServiceUtil.sendEmail(message);
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	String yourString= "129PRODS=123456&QTY=1&OPTIONS123456=option1,option2&PRICES123456[EUR]=10&PRICES123456[USD]=11.5&PLNKEXP=1286532283&PLNKID=4A4681F0E5";
    	String key = "_SECRET_KEY_";
    	
    	String code = StringUtils.sStringToHMACMD5(yourString, key);
    	System.out.println("hmac code : " + code);
    	
    	
    	StringBuffer postParms = request.getRequestURL();
    	Enumeration paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()) {
          String paramName = (String)paramNames.nextElement();
          System.out.println("<TR><TD>" + paramName + "\n<TD>");
          String[] paramValues = request.getParameterValues(paramName);
          if (paramValues.length == 1) {
            String paramValue = paramValues[0];
            if (paramValue.length() == 0)
            	System.out.print("<I>No Value</I>");
            else
            	System.out.print(paramValue);
          } else {
        	  System.out.println("<UL>");
            for(int i=0; i<paramValues.length; i++) {
            	System.out.println("<LI>" + paramValues[i]);
            }
            System.out.println("</UL>");
          }
        }
    	
    	/***
    	 * Avangate :
    	 * 
    	 * Merchant Code : PEARLSME
    	 * Secret Key :P2_8+wn8^8x33F@N9(q^ 
    	 * 
    	 * 
    	 * https://secure.avangate.com/order/trial.php?PRODS=4555564&QTY=1&CURRENCY=EUR&LANGUAGES=de,en,es&PAY_TYPE=CCVISAMC&PRICES4555564[EUR]=1&TPERIOD=30&PHASH=5fa3954abc0d16f4c65733d944d7c864
    	 * 
    	 * https://secure.avangate.com/order/trial.php?PRODS=4555564&QTY=1&CURRENCY=EUR&LANGUAGES=de,en&PAY_TYPE=CCVISAMC&SRC=CM&PRICES4555564[EUR]=0&TPERIOD=30&PHASH=6f94cb13b7f390cf699c3cb9e093400e
    	 * 
    	 * https%3A//secure.avangate.com/order/trial.php%3FPRODS%3D4555564%26QTY%3D1%26CURRENCY%3DEUR%26LANGUAGES%3Dde%2Cen%26PAY_TYPE%3DCCVISAMC%26SRC%3DCM%26PRICES4555564%5BEUR%5D%3D0%26TPERIOD%3D30%26PHASH%3D6f94cb13b7f390cf699c3cb9e093400e
    	 * 
    	 * https://secure.avangate.com/order/pf.php?MERCHANT=PEARLSME&BILL_EMAIL=john.doe@example.com&BILL_FNAME=GuenterBF&BILL_LNAME=BFBaumgart&BILL_CITY=SantaEulalia&BILL_ADRESS=ApartaDo&URL=https://secure.avangate.com/order/checkout.php?PRODS=4555564&QTY=1&DOTEST=1
    	 * 
    	 * 
    	 * Price 0 : https://secure.avangate.com/order/checkout.php?PRODS=4555564&QTY=1&CURRENCY=EUR&PRICES4555564[EUR]=0&PHASH=795e3081067287b70b7d72672ba2e0e9&DOTEST=1
    	 * 
    	 * BILL_FNAME  BILL_LNAME BILL_COMPANY
	
BILL_FISCALCODE BILL_EMAIL BILL_PHONE
BILL_FAX BILL_ADDRESS  BILL_ADDRESS2 BILL_ZIPCODE BILL_CITY  BILL_STATE BILL_COUNTRYCODE
    	 * 
    	 * 		    Visa 4111 1111 1111 1111
    	 * 			March 2014  | 123  | Avangate Customer 
    	 * 
					    MasterCard 5555 5555 5555 4444
					    American Express 3782 8224 6310 005
					    Discover 6011 1111 1111 1117
					    JCB 3566 1111 1111 1113
					    UATP 1354 1234 5678 911
					    Maestro (International) 5033 9619 8909 17 / 5868 2416 0825 5333 38
					    Maestro (UK Domestic)
					        Issue number not required: 6759 4111 0000 0008
					        One-digit issue number required: 6759 5600 4500 5727 054
					        Two-digit issue number required: 5641 8211 1116 6669
					    Solo
					    Issue number not required: 6334 5898 9800 0001
					    One-digit issue number required: 6767 8200 9988 0077 06
					    Two-digit issue number required: 6334 9711 1111 1114
					    5817840047112340
					    Carte Bleue (via Moneybookers)

    	 */
    	//
    	
    	String _uuid = request.getParameter("uuid");
    	String _uuid0 = request.getParameter("SubscriptionQuantity");
    	String _uuid1 = request.getParameter("SubscriptionReference");
    	
    	String _uuid2 = request.getParameter("SourceKey");
    	String _uuid3 = request.getParameter("OrderReferrer");
    	String _uuid4 = request.getParameter("referrer");
    	String _uuid5 = request.getParameter("SubscriptionReferrer");//works!
    	String _uuid6 = request.getParameter("productPath");
    	String _uuid7 = request.getParameter("CustomerEMail");
    	System.out.println("ffff  " + postParms.toString());
    	return;
    	/*
    	getCMObjects(request, response, true);
    	
    	String action= request.getParameter("action");
    	if(action.equals("getApplication"))
    	{
    		getApplication(request, response);
    	}
    	*/
    }
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        //resolver = (ContentResolver)getServletContext().getAttribute("org.mortbay.ijetty.contentResolver");
    }

}
