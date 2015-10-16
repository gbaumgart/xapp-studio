package pmedia.utils;

import java.util.ArrayList;

import jsontype.ComposerPackage;
import jsontype.ComposerPackages;
import cmx.types.Resources;
import cmx.types.XCDataSource;
import cmx.types.XCDatasourceInfo;

public class ComposerPackageUtils {
	
	
	public static ArrayList<Resources> getResourcesByRunTimeConfiguration(ComposerPackages packages,String rtConfig,XCDatasourceInfo xds){
		
		ArrayList<Resources>result = new ArrayList<Resources>();
		if(packages!=null){
			
			if(packages.getItems()!=null){
				
				ArrayList<ComposerPackage>cpackages = packages.getItems();
				for(int i = 0 ; i < cpackages.size() ; i++){
					ComposerPackage pkg = cpackages.get(i);
					
					if(pkg.getResources()!=null){
						
						
						if(rtConfig.equals("debug") && pkg.getResources().getDebug()!=null){
								result.add(pkg.getResources().getDebug());
						}
						
						if(rtConfig.contains("release") && pkg.getResources().getRelease()!=null){
							result.add(pkg.getResources().getRelease());
						}
					}
				}
			}
		}
		
		if(result.size()==0){
			return null;
		}
		return result;
	}
	
}
