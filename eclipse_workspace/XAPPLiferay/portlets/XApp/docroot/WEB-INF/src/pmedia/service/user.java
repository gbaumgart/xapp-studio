/*
package pmedia.service;
import java.io.IOException;
import java.text.ParseException;


import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;

import cmx.tools.JoomlaContentTreeFactory;
import cmx.types.Application;
import cmx.types.ApplicationManager;
import cmx.types.ContentTree;
import cmx.types.Reference;

@Path("/user")
public class user 
{
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{uuid}/{appId}/{mode}/{device}")
	public Response getMsg(@PathParam("uuid") String uuid, @PathParam("appId") String appId,@PathParam("mode") String mode,@PathParam("mode") String device) 
	{
		//http://192.168.1.37:8080/XApp-portlet/applications/datasources/11166763-e89c-44ba-aba7-4e9f4fdf97a9/ibizamedia5/
		String output = "Jersey say : " + uuid;
 		System.out.println("jersey");
 		ApplicationManager appManager = ApplicationManager.getInstance();
 		Application app = appManager.getApplication(uuid, appId, false);
 		ContentTree tree = null;
 		try {
			tree = JoomlaContentTreeFactory.createDataSourceTree(appManager,app,0);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
 		
		return Response.status(200).entity(tree.items).build();
		
	}
	
	@Produces("text/plain")
    public String getClichedMessage() 
	{
		return "Hello World";
    }
}
*/