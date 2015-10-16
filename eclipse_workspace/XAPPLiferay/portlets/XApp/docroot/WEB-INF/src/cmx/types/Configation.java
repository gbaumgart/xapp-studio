package cmx.types;

import java.util.ArrayList;

public class Configation {
	
	
	public void init()
	{		
		inputs = new ArrayList<ConfigurableInformation>();
		outputs = new ArrayList<ConfigurableInformation>();
		
	}
	String id="noID";
	ArrayList<ConfigurableInformation>inputs;
	ArrayList<ConfigurableInformation>outputs;
	/**
	 * @return the inputs
	 */
	public ArrayList<ConfigurableInformation> getInputs() {
		if(inputs==null)
			inputs = new ArrayList<ConfigurableInformation>();
		return inputs;
	}
	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(ArrayList<ConfigurableInformation> inputs) {
		this.inputs = inputs;
	}
	/**
	 * @return the outputs
	 */
	public ArrayList<ConfigurableInformation> getOutputs() {
		if(outputs==null)
			outputs= new ArrayList<ConfigurableInformation>();
		return outputs;
	}
	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(ArrayList<ConfigurableInformation> outputs) {
		this.outputs = outputs;
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
	
}
