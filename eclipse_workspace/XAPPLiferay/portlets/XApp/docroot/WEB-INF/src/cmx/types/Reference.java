package cmx.types;

import java.util.ArrayList;

public class Reference {

	public String _reference;

	public Reference(String namedRef) {
		_reference = namedRef;
	}
	public Reference() {
		
	}
	/**
	 * @return the _reference
	 */
	public String get_reference() {
		return _reference;
	}

	/**
	 * @param _reference the _reference to set
	 */
	public void set_reference(String _reference) {
		this._reference = _reference;
	}

}
