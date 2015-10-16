package pmedia.types;

import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import cmx.types.ECMContentSourceType;
import flexjson.*;

public class CListFormItem extends CListItem
{
	public ArrayList<CListFormItem>fields = new ArrayList<CListFormItem>();
	
	public String fieldType;
	public String fieldClass;
	public String fieldName;
	public ArrayList<CListFormItem> getFields() {
		return fields;
	}
	public void setFields(ArrayList<CListFormItem> fields) {
		this.fields = fields;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldClass() {
		return fieldClass;
	}
	public void setFieldClass(String fieldClass) {
		this.fieldClass = fieldClass;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	
	

}
