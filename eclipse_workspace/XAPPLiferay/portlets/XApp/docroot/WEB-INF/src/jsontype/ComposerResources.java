package jsontype;

import cmx.types.Resources;

public class ComposerResources 
{
	public Resources debug;
	
	public Resources release;

	public Resources getDebug() {
		return debug;
	}

	public void setDebug(Resources debug) {
		this.debug = debug;
	}

	public Resources getRelease() {
		return release;
	}

	public void setRelease(Resources release) {
		this.release = release;
	}
}
