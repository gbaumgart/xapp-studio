package cmx.manager;
import java.io.IOException;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class GroovyScriptManager {
	
	public static String runScript(String scriptInRoot,String var0,String var1,String var2,String var3,String var4,String var5,String var6,String var7,String var8)
	{
		
		String result = null;
		String[] roots = new String[] { System.getProperty("GroovyRoot") };
		GroovyScriptEngine gse = null;
		try {
			gse = new GroovyScriptEngine(roots);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Binding binding = new Binding();
		
		
		if(var0!=null){
			binding.setVariable("var0", var0);
		}
		
		if(var1!=null){
			binding.setVariable("var1", var1);
		}
		
		if(var2!=null){
			binding.setVariable("var2", var2);
		}
		if(var3!=null){
			binding.setVariable("var3", var3);
		}
		
		if(var4!=null){
			binding.setVariable("var4", var4);
		}
		if(var5!=null){
			binding.setVariable("var5", var5);
		}
		
		if(var6!=null){
			binding.setVariable("var6", var6);
		}
		if(var7!=null){
			binding.setVariable("var7", var7);
		}
		if(var8!=null){
			binding.setVariable("var8", var8);
		}
		
		try {
			gse.run(scriptInRoot, binding);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		 
		Object res= binding.getVariable("result");
		if(res!=null){
			return res.toString();
		}
		return result;
	}
	
}
