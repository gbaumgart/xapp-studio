package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pmedia.types.MenueData;
import pmedia.types.PMDataTypes;
import pmedia.types.TranslationData;
import pmedia.utils.StringUtils;
import pmedia.DataManager.DomainCache;
import pmedia.DataManager.ServerCache;

public class TranslationTools 
{
	
	public static HashMap<String, String>generateMenuTranslationTable(String lang,String domain)
	{
		
    	ArrayList<MenueData> menues =  ServerCache.getInstance().getDC("ibiza").get(DomainCache.MENU);
    	
    	if(menues==null)
    	{
    		//menues =  ServerCache.getInstance().getDC("ibiza").get(DomainCache.MENU);
    		System.out.println("have no menues !");
    		return null;
    	}
    	ArrayList<TranslationData> translations =  ServerCache.getInstance().getDC("ibiza").get(DomainCache.TRANSLATIONS);
    	
		HashMap<String, String>result  = new HashMap<String, String>();
		
		for (int i = 0 ; i < menues.size() ; i ++)
		{
			MenueData menu = menues.get(i);
			//public static TranslationData getTranslationByTypeAndIndex(ArrayList<TranslationData>translations,int index,PMDataTypes type,String lang)
			TranslationData mTranslation = TranslationTools.getTranslationByTypeAndIndex(translations,menu.id,PMDataTypes.DITT_JSTRING,lang);
			
			String key = menu.title;
			String value = menu.title;
			if(mTranslation!=null && mTranslation.title !=null && mTranslation.title.length() > 0)
			{				
				value = mTranslation.title;
			}
			result.put(key, value);
		}
		return result;
		
	}
	public static String translateCategory(String domain,String lang,int index)
	{
		ArrayList<TranslationData> translations = ServerCache.getInstance().getDC(domain).get(DomainCache.TRANSLATIONS);
		 for (int s = 0; s < translations.size(); s++)
		 {
			 TranslationData translation = translations.get(s);
			 if(translation.tType == PMDataTypes.DITT_JLOCATION_CAT && translation.article_id==index && translation.lCode.equals(lang)){
				 return translation.title;
			 }
		 }
		 return null;
	}
	
	public static String translateCategoryByIndexAndType(String domain,String lang,int index,PMDataTypes type)
	{
		ArrayList<TranslationData> translations = ServerCache.getInstance().getDC(domain).get(DomainCache.TRANSLATIONS);
		 for (int s = 0; s < translations.size(); s++)
		 {
			 TranslationData translation = translations.get(s);
			 if(translation.tType == type && translation.article_id==index && translation.lCode.equals(lang))
			 {
				 return translation.title;
			 }
		 }
		 return null;
	}
	
	public static TranslationData getCategoryTranslation(String domain,String lang,int index)
	{
		ArrayList<TranslationData> translations = ServerCache.getInstance().getDC(domain).get(DomainCache.TRANSLATIONS);
		 for (int s = 0; s < translations.size(); s++)
		 {
			 TranslationData translation = translations.get(s);
			 if(translation.tType == PMDataTypes.DITT_JLOCATION_CAT && translation.article_id==index && translation.lCode.equals(lang)){
				 return translation;
			 }
		 }
		 return null;
	}
	
	public static TranslationData getTranslationByTypeAndIndex(ArrayList<TranslationData>translations,int index,PMDataTypes type,String lang)
	{
		 for (int s = 0; s < translations.size(); s++)
		 {
			 TranslationData translation = translations.get(s);
			 if(translation.tType == type && translation.article_id==index && translation.lCode.equals(lang))
				 return translation;
			 
		 }
		 return null;
		 
	}
	public static String getTranslationTextByTypeAndIndex(ArrayList<TranslationData>translations,int index,PMDataTypes type,String lang)
	{
		 for (int s = 0; s < translations.size(); s++)
		 {
			 TranslationData translation = translations.get(s);
			 if(translation.tType == type && translation.article_id==index && translation.lCode.equals(lang))
				 return translation.descr;
			 
		 }
		 return null;
		 
	}
	public static ArrayList<TranslationData>readITArticleTranslationFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	translation.lCode = fstElmnt.getAttribute("l");
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	translation.title= fstElmnt.getAttribute("t");
		    	translation.tType = PMDataTypes.DITT_ARTICLE;
		    	translation.descr = StringUtils.getXMLFromElement(fstElmnt, "descr");
		    	translation.subt = StringUtils.getXMLFromElement(fstElmnt, "subt");
		    	translation.observ = StringUtils.getXMLFromElement(fstElmnt, "observ");
		    	translation.breve = StringUtils.getXMLFromElement(fstElmnt, "breve");
		    	result.add(translation);
		    }
		 }
		return result;
	}
	
	public static String getLangCodeJoomla(int code)
	{
		
		switch (code) 
		{
			case 1:
				return "en";
			case 2:
				return "de";
			case 3:
				return "es";
			case 4:
				return "fr";
			case 5:
				return "it";
			case 6:
				return "nl";
		}
		
		return "";
	}
	public static ArrayList<TranslationData>readLocationCategoryTranslationsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	String tmplCode = fstElmnt.getAttribute("l");
		    	if(tmplCode==null || tmplCode.length()==0)
		    		continue;
		    	
		    	translation.title= fstElmnt.getAttribute("t");
		    	if(translation.title==null || translation.title.length()==0)
		    		continue;
		    	
		    	translation.lCode = getLangCodeJoomla(Integer.parseInt(tmplCode));
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	
		    	translation.tType = PMDataTypes.DITT_JLOCATION_CAT;
		    	result.add(translation);
		    }
		 }
		return result;
	}
	
	public static ArrayList<TranslationData>readEventCategoryTranslationsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	String tmplCode = fstElmnt.getAttribute("l");
		    	if(tmplCode==null || tmplCode.length()==0)
		    		continue;
		    	
		    	translation.title= fstElmnt.getAttribute("t");
		    	if(translation.title==null || translation.title.length()==0)
		    		continue;
		    	
		    	translation.lCode = getLangCodeJoomla(Integer.parseInt(tmplCode));
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	
		    	translation.tType = PMDataTypes.DITT_JEVENT_CAT;
		    	result.add(translation);
		    }
		 }
		return result;
	}
	
	public static String langCodeJoomla(int index)
	{
		switch (index) 
		{
			case 1:
				return "en";
				
			case 2:
				return "de";
			
			case 3:
				return "es";
		}
		
		return "en";
	}
	public static ArrayList<TranslationData>readJArticleTranslationsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	String tmplCode = fstElmnt.getAttribute("l");
		    	if(tmplCode==null || tmplCode.length()==0)
		    		continue;
		    	
		    	translation.title= fstElmnt.getAttribute("t");
		    	if(translation.title==null || translation.title.length()==0)
		    		continue;
		    	
		    	translation.lCode = getLangCodeJoomla(Integer.parseInt(tmplCode));
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	translation.descr = StringUtils.getXMLFromElement(fstElmnt, "descr");
		    	translation.tType = PMDataTypes.DITT_JARTICLE;
		    	
		    	result.add(translation);
		    }
		 }
		return result;
	}
	
	public static ArrayList<TranslationData>readJLocationTranslationsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	String tmplCode = fstElmnt.getAttribute("l");
		    	if(tmplCode==null || tmplCode.length()==0)
		    		continue;
		    	
		    	translation.title= fstElmnt.getAttribute("t");
		    	if(translation.title==null || translation.title.length()==0)
		    		continue;
		    	
		    	
		    	
		    	translation.lCode = getLangCodeJoomla(Integer.parseInt(tmplCode));
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	translation.descr = StringUtils.getXMLFromElement(fstElmnt, "descr");
		    	translation.tType = PMDataTypes.DITT_JLOCATION;
		    	
		    	result.add(translation);
		    }
		 }
		return result;
	}
	public static ArrayList<TranslationData>readJEventsTranslationsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	String tmplCode = fstElmnt.getAttribute("l");
		    	if(tmplCode==null || tmplCode.length()==0)
		    		continue;
		    	
		    	translation.title= fstElmnt.getAttribute("t");
		    	if(translation.title==null || translation.title.length()==0)
		    		continue;
		    	
		    	translation.lCode = getLangCodeJoomla(Integer.parseInt(tmplCode));
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	translation.descr = StringUtils.getXMLFromElement(fstElmnt, "descr");
		    	translation.tType = PMDataTypes.DITT_JEVENT;
		    	
		    	result.add(translation);
		    }
		 }
		return result;
	}
	
	public static ArrayList<TranslationData>readJStringsTranslationsFromFile(String path) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		ArrayList<TranslationData > result = new ArrayList<TranslationData>();

		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("art");
		 for (int s = 0; s < nodeLst.getLength(); s++)
		 {
		    Node fstNode = nodeLst.item(s);

		    if (fstNode.getNodeType() == Node.ELEMENT_NODE)
		    {
		    	TranslationData translation = new TranslationData();
		    	Element fstElmnt = (Element) fstNode;
		    	String tmplCode = fstElmnt.getAttribute("l");
		    	if(tmplCode==null || tmplCode.length()==0)
		    		continue;
		    	
		    	translation.title= fstElmnt.getAttribute("t");
		    	if(translation.title==null || translation.title.length()==0)
		    		continue;
		    	
		    	translation.lCode = getLangCodeJoomla(Integer.parseInt(tmplCode));
		    	
		    	translation.article_id = Integer.parseInt(fstElmnt.getAttribute("id"));
		    	translation.descr = StringUtils.getXMLFromElement(fstElmnt, "descr");
		    	translation.tType = PMDataTypes.DITT_JSTRING;
		    	
		    	result.add(translation);
		    }
		 }
		return result;
	}
}
