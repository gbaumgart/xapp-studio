package pmedia.types;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.search.Sort;

public class SearchableSourceConfiguration 
{
	
	
	private PMSearchSourceType sourceType=PMSearchSourceType.PMSST_LOCATIONS;
	
	public static SearchableSourceConfiguration defaultConfiguration(PMSearchSourceType sourceType)
	{
		SearchableSourceConfiguration result = new SearchableSourceConfiguration();
		ArrayList<PMSearchFieldTypes>fields = new ArrayList<PMSearchFieldTypes>();
		result.setSourceType(sourceType);
		
		fields.add(PMSearchFieldTypes.PM_SFT_TITLE);
		fields.add(PMSearchFieldTypes.PM_SFT_DESCRIPTION);
		result.setSearchFields(fields);
		return result;
		
	}
	/**
	 * Simple map to enable fields per source
	 */
	private ArrayList<PMSearchFieldTypes>searchFields=new ArrayList<PMSearchFieldTypes>();
	
	/** Looks in other index databases.*/
	private Boolean allowOtherLanguages=false;
	
	/** Highlight search term in description fields.*/
	private Boolean highlightTerm=true;
	private String highlightFragmentClass="searchHighlight";
	private int highlightFragmentLength=150;
//	private String highlightFragmentTemplateEnd="</span>";
	
	/** Generate a more item at the end of the list */
	private Boolean includeMoreItem=false;
	
	private int hitsPerPage=10;
	
	
	/** default sort field */
	//private Sort defaultSort=Sort.RELEVANCE;
	
	
	
	/**
	 * @return the allowOtherLanguages
	 */
	public Boolean getAllowOtherLanguages() {
		return allowOtherLanguages;
	}
	/**
	 * @param allowOtherLanguages the allowOtherLanguages to set
	 */
	public void setAllowOtherLanguages(Boolean allowOtherLanguages) {
		this.allowOtherLanguages = allowOtherLanguages;
	}
	/**
	 * @return the highlightTerm
	 */
	public Boolean getHighlightTerm() {
		return highlightTerm;
	}
	/**
	 * @param highlightTerm the highlightTerm to set
	 */
	public void setHighlightTerm(Boolean highlightTerm) {
		this.highlightTerm = highlightTerm;
	}
	/**
	 * @return the includeMoreItem
	 */
	public Boolean getIncludeMoreItem() {
		return includeMoreItem;
	}
	/**
	 * @param includeMoreItem the includeMoreItem to set
	 */
	public void setIncludeMoreItem(Boolean includeMoreItem) {
		this.includeMoreItem = includeMoreItem;
	}
	/**
	 * @return the sourceType
	 */
	public PMSearchSourceType getSourceType() {
		return sourceType;
	}
	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(PMSearchSourceType sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * @return the searchFields
	 */
	public ArrayList<PMSearchFieldTypes> getSearchFields() {
		return searchFields;
	}
	/**
	 * @param searchFields the searchFields to set
	 */
	public void setSearchFields(ArrayList<PMSearchFieldTypes> searchFields) {
		this.searchFields = searchFields;
	}
	/**
	 * @return the hitsPerPage
	 */
	public int getHitsPerPage() {
		return hitsPerPage;
	}
	/**
	 * @param hitsPerPage the hitsPerPage to set
	 */
	public void setHitsPerPage(int hitsPerPage) {
		this.hitsPerPage = hitsPerPage;
	}
	/**
	 * @return the highlightFragmentClass
	 */
	public String getHighlightFragmentClass() {
		return highlightFragmentClass;
	}
	/**
	 * @param highlightFragmentClass the highlightFragmentClass to set
	 */
	public void setHighlightFragmentClass(String highlightFragmentClass) {
		this.highlightFragmentClass = highlightFragmentClass;
	}
	/**
	 * @return the highlightFragmentLength
	 */
	public int getHighlightFragmentLength() {
		return highlightFragmentLength;
	}
	/**
	 * @param highlightFragmentLength the highlightFragmentLength to set
	 */
	public void setHighlightFragmentLength(int highlightFragmentLength) {
		this.highlightFragmentLength = highlightFragmentLength;
	}
	
	
}
