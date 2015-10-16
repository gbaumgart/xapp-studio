package jsontype;

public class DojoPackage 
{
	    public String name;
	    public String namespace;
	    public String version;
	    public String main;
	    public boolean enabled;
	    public String description;
	    public String dojoBuild;
	    public String location;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNamespace() {
			return namespace;
		}
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getMain() {
			return main;
		}
		public void setMain(String main) {
			this.main = main;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDojoBuild() {
			return dojoBuild;
		}
		public void setDojoBuild(String dojoBuild) {
			this.dojoBuild = dojoBuild;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
}
