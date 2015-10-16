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

import pmedia.DataManager.Cache;
import pmedia.types.ArticleCategory;
import pmedia.types.BaseData;
import pmedia.types.Category;
import pmedia.types.CategoryComparator;
import pmedia.types.EventData;
import pmedia.types.LocationData;
import pmedia.types.MosetCategory;
import pmedia.utils.StringHelper;
import pmedia.utils.StringUtils;
public class BaseDataArrayTools 
{
	
	public static Boolean hasSubCategories(ArrayList<Category>srcCategories,int index)
	{
		if(srcCategories==null)
			return false;
		
		for (int s = 0; s < srcCategories.size() ; s++) 
		{
			Category c = srcCategories.get(s);
			
			if(c.id !=index && c.groupId==index)
			{
				return true;
			}
		}
		return false;
	}
	public static BaseData getByIndex(ArrayList data,int index)
	{
		if(data!=null)
		{
			for (int s = 0; s < data.size() ; s++) 
			 {
			    BaseData d = (BaseData) data.get(s);
			    if(d.refId == index)
			    	return d;
			 }
		}
		return null;
	}
	public static ArrayList getByGroupIndex(ArrayList data,int index)
	{
		ArrayList result=new ArrayList();
		if(data!=null)
		{
			for (int s = 0; s < data.size() ; s++) 
			 {
			    BaseData d = (BaseData) data.get(s);
			    if(d.getGroupId() == index)
			    	result.add(d);
			 }
		}
		if(result.size() > 0){
			return result;
		}
		return null;
	}
	
	public static ArrayList<Category> getCategoriesByParentIndex(ArrayList<Category>srcCategories,int index)
	{
		ArrayList<Category> result = new ArrayList<Category>();
		if(srcCategories!=null)
		{
			for (int s = 0; s < srcCategories.size() ; s++) 
			 {
			    Category c = srcCategories.get(s);
			    if(c.groupId == index){
			    	result.add(c);
			    	c.hasSubCategories = BaseDataArrayTools.hasSubCategories(srcCategories, c.refId);
			    }
			 }
		}
		return result;
	}
	
}
