package pmedia.types;

import java.util.ArrayList;

public class SearchConfiguration 
{
	
	public static SearchConfiguration allSourcesDefault()
	{
		SearchConfiguration result = new SearchConfiguration();
		
		//result.getSearchSources().isEmpty()
		ArrayList<SearchableSourceConfiguration>sourceConfigs = result.getSearchSourceConfigurations();
		
		sourceConfigs.add(SearchableSourceConfiguration.defaultConfiguration(PMSearchSourceType.PMSST_LOCATIONS));
		sourceConfigs.add(SearchableSourceConfiguration.defaultConfiguration(PMSearchSourceType.PMSST_ARTICLES));
		sourceConfigs.add(SearchableSourceConfiguration.defaultConfiguration(PMSearchSourceType.PMSST_EVENTS));
		sourceConfigs.add(SearchableSourceConfiguration.defaultConfiguration(PMSearchSourceType.PMSST_CATEGORIES));
		
		return result;
	}
	
	/**
	 * Simple map to enable fields per source
	 */
	private ArrayList<SearchableSourceConfiguration>searchSourceConfigurations=new ArrayList<SearchableSourceConfiguration>();

	/**
	 * @return the searchSourceConfigurations
	 */
	public ArrayList<SearchableSourceConfiguration> getSearchSourceConfigurations() {
		return searchSourceConfigurations;
	}

	/**
	 * @param searchSourceConfigurations the searchSourceConfigurations to set
	 */
	public void setSearchSourceConfigurations(
			ArrayList<SearchableSourceConfiguration> searchSourceConfigurations) {
		this.searchSourceConfigurations = searchSourceConfigurations;
	}
	
	
}
