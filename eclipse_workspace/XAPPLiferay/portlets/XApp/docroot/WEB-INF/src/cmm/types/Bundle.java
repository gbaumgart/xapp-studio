package cmm.types;

import java.util.ArrayList;

public class Bundle extends CMMSubscription{
	public ArrayList<PackageInfo>packages;

	public ArrayList<PackageInfo> getPackages() {
		return packages;
	}

	public void setPackages(ArrayList<PackageInfo> packages) {
		this.packages = packages;
	}
}
