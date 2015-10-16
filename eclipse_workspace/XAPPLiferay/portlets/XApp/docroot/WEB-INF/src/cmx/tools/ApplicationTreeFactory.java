package cmx.tools;

import java.util.ArrayList;

import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.ContentTreeItem;
import cmx.types.DataSourceBase;
import cmx.types.Reference;

public class ApplicationTreeFactory 
{	
	
	public static ContentTree createApplicationTree(String uuid,ApplicationManager appManager,long flags)
	{		
		ContentTree tree = new ContentTree();
		tree.id="1";
		ArrayList<ContentTreeItem> result = new ArrayList<ContentTreeItem>();
		tree.setItems(result);

		
		ContentTreeItem rootItem = new ContentTreeItem();
    	rootItem.name = "Applications"; 
    	rootItem.label= "Applications";
    	rootItem.id = "9898989";
    	rootItem.children = new ArrayList<Reference>();
   	
    	tree.items.add(rootItem);
    	rootItem.type = "top";
    	rootItem.contentType = "group";
    	
    	ArrayList<Application>applications= appManager.getUserApplications(uuid);
		for(int i = 0  ; i  <  applications.size() ; i++)	
		{
			Application app = applications.get(i);
			ContentTreeItem item = new ContentTreeItem();
	    	
			item.name =  app.getApplicationIdentifier();
			item.label =  app.getApplicationIdentifier();
			item.id = ""+app.getApplicationIdentifier();
			
			item.setChildren(null);

			Reference ref = new Reference();
	    	ref._reference="" + item.id;

	    	rootItem.addChild(ref);
	    	result.add(item);
	    	item.type = "Application";
		}
		return tree;
	}
}
