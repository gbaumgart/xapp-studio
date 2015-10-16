package pmedia.DataUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmx.types.ECMContentSourceType;

import pmedia.DataManager.Cache;
import pmedia.types.ArticleCategory;
import pmedia.types.BaseData;
import pmedia.types.CList;
import pmedia.types.CListFormItem;
import pmedia.types.CListItem;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MosetCategory;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
public class BreezingFormTools 
{
	




	public static void addSubmitButton(CListFormItem formsItem)
	{
		Boolean hasSubmit = false;
		for (int i = 0; i < formsItem.fields.size(); i++) 
		{
			CListFormItem formField = formsItem.fields.get(i);
			if(formField.fieldName!=null && formField.fieldName.equals("cf_submit"))
			{
				hasSubmit=true;
			}
		}
		
		if(!hasSubmit){
			
			CListFormItem submit = new CListFormItem();
			submit.fieldType="Regular Button";
			submit.fieldName="cf_submit";
			submit.title="Submit";
			submit.published=true;
			formsItem.fields.add(submit);
			
		}
	}

	public static  CList readBreezingForms(String path,String type,int contentSourceType,String baseRef) throws ParserConfigurationException, SAXException, IOException, ParseException
	{
		CList result = new CList();
		result.items = new ArrayList<CListItem>();
		
		result.baseRef=baseRef;
		
		File file = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		
		try {
			doc = db.parse(file);	
		} catch (Exception e) {
			return result;
		}
		
		doc.getDocumentElement().normalize();

		NodeList nodeLst = doc.getElementsByTagName("item");
		for (int s = 0; s < nodeLst.getLength(); s++) 
		{
		    Node fstNode = nodeLst.item(s);
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
		    {
		    	CListFormItem item = new CListFormItem();
		    	
		    	item.baseRef=baseRef;
		    	Element fstElmnt = (Element) fstNode;
		    	
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		title	
		        //
		    	int pub = 1;
		    	try {
		    		pub = Integer.parseInt(fstElmnt.getAttribute("published"));	
				} catch (Exception e) {
					// TODO: handle exception
				}
		    	item.setTitle(fstElmnt.getAttribute("title"));
		    	item.setType(ECMContentSourceType.BreezingForm);
		    	item.setPublished(pub == 1 ? true : false);
		    	///////////////////////////////////////////////////////////////////////////////////////
		        //
		        //		refId	
		        //
		    	item.ref=String.valueOf(StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id"));
		    	
		    	try {
		    		item.refId=StringUtils.getIntegerFromXMLAttribute(fstElmnt, "id");	
				} catch (Exception e) {
				}
		    	
		    	
		    	/*****
		    	 * 
		    	 */
		    	
		    	NodeList groupsLst  = fstElmnt.getElementsByTagName("fields");
		    	if(groupsLst!=null)
		    	{
			    	
		    		Node cNode = groupsLst.item(0);
			    	
			    	if (cNode!=null && cNode.getNodeType() == Node.ELEMENT_NODE)
				    {
				    	Element cElmnt = (Element) cNode;
				    	NodeList gList = cElmnt.getElementsByTagName("field");
				    	for (int sc = 0; sc < gList.getLength(); sc++)
						{
				    		Element gE = (Element) gList.item(sc);
				    		CListFormItem field = new CListFormItem();
				    		
				    		field.setTitle(gE.getAttribute("title"));
				    		
				    		int fpub = 1;
				    		int order = 0;
					    	try {
					    		fpub = Integer.parseInt(gE.getAttribute("published"));
					    		order = Integer.parseInt(gE.getAttribute("order"));
							} catch (Exception e) {
							}
					    	field.setPublished(pub == 1 ? true : false);
					    	field.setOrder(order);
					    	field.setFieldClass(gE.getAttribute("class"));
					    	field.setFieldType(gE.getAttribute("type"));
					    	field.setFieldName(gE.getAttribute("name"));
					    	if(pub==1){
					    		item.getFields().add(field);
					    	}
					    	
					    	
					    	
				    		/*
					    	int _gId = StringUtils.getIntegerFromXMLAttribute(gE, "id");
					    	if(_gId!=-1)
					    	{
					    		if(article.getRefGroups()==null){
					    			article.setRefGroups(new ArrayList<Reference>());
					    		}
					    		article.getRefGroups().add(new Reference(""+_gId));
					    	}
					    	
					    	Reference r;
					    	
					    	
					    	String _gIdStr = gE.getAttribute("idStr");
					    	if(_gIdStr!=null)
					    	{
					    		if(article.getRefGroupsStr()==null){
					    			article.setRefGroupsStr(new ArrayList<String>());
					    		}
					    		article.getRefGroupsStr().add(_gIdStr);
					    	}
					    	*/
					    	
					    	
					    }
					}
		    	}
		    	
		    	
		    	
		    	
		    	
				//cat.setRefId(cat.index);
				//cat.parent= StringUtils.getIntegerFromXMLElement(fstElmnt, "parent");
				//cat.description= StringUtils.getXMLFromElement(fstElmnt, "descr");
				result.items.add(item);
				
		    }
		}
		
		return result;
	}

}
