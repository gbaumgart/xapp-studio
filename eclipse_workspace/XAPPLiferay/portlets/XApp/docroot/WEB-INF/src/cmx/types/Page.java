package cmx.types;

import java.util.ArrayList;

import pmedia.utils.CITools;

public class Page extends Configation{

	public String id;
	public String name;
	public String label;
	public String type;
	public String parent;
	public String layout;
	public int uid;
	public int order=-1;
	
	public boolean enabled=true;
	
	
	public String qxappTabIdentifier=null;
	
	public ArrayList<Reference> children;
	

	public void deleteSubPage(Page page){
		
		for(int i = 0  ; i  <  children.size() ; i++)	{
			Reference  ref = children.get(i) ;
			if(ref._reference.equals(page.id))
			{
				children.remove(ref);
				break;
			}
		}
	}

	public ConfigurableInformation getItemByChainAndName(int chain,String name)
	{
		ArrayList<ConfigurableInformation>dstChain = chain==0 ? inputs : outputs;
		if(dstChain!=null)
		{
				for(int i = 0  ; i  <  dstChain.size() ; i++)	{
					ConfigurableInformation  ci = dstChain.get(i) ;
					if(ci.getName().equals(name))
					{
						return ci;
					}
				}
		}
		return null;
	}
	public ConfigurableInformation getItemByChainAndUID(int chain,int uid)
	{
		
		ArrayList<ConfigurableInformation>dstChain = chain==0 ? inputs : outputs;
		if(dstChain!=null)
		{
				for(int i = 0  ; i  <  dstChain.size() ; i++)	{
					
					ConfigurableInformation  ci = dstChain.get(i) ;
					if(ci.getUid()==uid)
					{
						return ci;
					}
				}
		}
		return null;
	}
	
	public ConfigurableInformation updateItem(ConfigurableInformation item)
	{
		ConfigurableInformation rci = getItemByChainAndUID(item.chainType, item.getUid());
		if(rci!=null)
		{
			rci.setValue(item.value);
			rci.setTitle(item.title);
			
			rci.setDataSource(item.dataSource);
			if(item.dataSource!=null && item.dataSource.length() > 0)
			{
				rci.setDataSource(item.dataSource);
				
			}else{
				//rci.setDataRef(item.dataRef);
				if(rci.getDataRef()!=null && rci.getDataRef().length() > 0)
				{
					//System.out.println("zeoo datasource");
				}
			}
			
			if(item.dataRef!=null && item.dataRef.length() > 0)
			{
				rci.setDataRef(item.dataRef);
			}else{
				//rci.setDataRef(item.dataRef);
				if(rci.getDataRef()!=null && rci.getDataRef().length() > 0)
				{
					//System.out.println("zeoo");
				}
			}
			
			rci.setFlags(item.flags);
			
			rci.setEnabled(item.enabled);
			//if(item.order!=1)
			//{
				
			rci.setOrder(item.order);
			//}
			
			return rci;
		}
		return null;
	}
	
	public ConfigurableInformation getItem(ConfigurableInformation item)
	{
		ConfigurableInformation rci = getItemByChainAndUID(item.chainType, item.getUid());
		if(rci!=null)
		{
			return rci;
		}
		return null;
	}
	public Page()
	{
		
		//init();
		
		id="FirstPage";
		name = "First Page";
		label = "First Page";
		type = "page";
		layout="tabs";
		
		/*
		ConfigurableInformation layoutCI = new ConfigurableInformation();
		layoutCI.setDescription("Layout");
		layoutCI.setName("Layout");
		layoutCI.setType(CITools.CIToInteger(CIType.ENUMERATION));
		inputs.add(layoutCI);
		*/
		//children = new ArrayList<Reference>();
	}
	
	public void addRef(Reference ref)
	{
		if(children==null)
		{
			children = new ArrayList<Reference>();
		}
		children.add(ref);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}


	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}


	/**
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}


	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}


	/**
	 * @return the children
	 */
	public ArrayList<Reference> getChildren() {
		return children;
	}


	/**
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<Reference> children) {
		this.children = children;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	public String getQxappTabIdentifier() 
	{
		return qxappTabIdentifier;
	}

	public void setQxappTabIdentifier(String qxappTabIdentifier) {
		this.qxappTabIdentifier = qxappTabIdentifier;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


}
