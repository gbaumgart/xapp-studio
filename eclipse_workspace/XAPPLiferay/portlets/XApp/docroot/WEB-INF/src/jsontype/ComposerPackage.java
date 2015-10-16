package jsontype;

public class ComposerPackage 
{
	    public String name;
	    
	    public boolean enabled;
	    public String description;
	    public String location;
	    public ComposerResources resources;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public ComposerResources getResources() {
			return resources;
		}
		public void setResources(ComposerResources resources) {
			this.resources = resources;
		}
}
