package pmedia.utils;

import flexjson.JSONSerializer;

public class JSONTools {


	public static void optimizedAppBundle(JSONSerializer serializer){
		
	}
	public static void optimizedCustomTypes(JSONSerializer serializer){
		/**
		 * "chainType": 0,
        "class": "cmx.types.ConfigurableInformation",
        "dataRef": null,
        "dataSource": null,
        "description": "name",
        "enabled": true,
        "enumType": -1,
        "flags": -1,
        "group": -1,
        "id": "8cd52fe9-ccce-45a4-a1ec-2b09172f6ce5",
        "name": "name",
        "order": -1,
        "params": null,
        "parentId": "-1",
        "platform": null,
        "storeDestination": null,
        "title": "",
        "type": 13,
        "uid": -1,
        "value": "Categories",
        "visible": true
		 */
		if(serializer!=null){
			
			serializer.exclude("app.dataSources.inputs.platform");
			serializer.exclude("app.dataSources.inputs.description");
			serializer.exclude("app.dataSources.inputs.enumType");
			serializer.exclude("app.dataSources.inputs.storeDestination");
			serializer.exclude("app.dataSources.inputs.parentId");
			/*serializer.exclude("app.dataSources.inputs.flags");*/
			//serializer.exclude("app.dataSources.inputs.title");
			serializer.exclude("app.dataSources.inputs.order");
			serializer.exclude("app.dataSources.inputs.dataSource");
			serializer.exclude("app.dataSources.inputs.dataRef");
			serializer.exclude("app.dataSources.inputs.chainType");
			serializer.exclude("app.dataSources.inputs.visible");
			serializer.exclude("app.dataSources.inputs.params");
			serializer.exclude("app.dataSources.inputs.class");
			serializer.exclude("app.dataSources.inputs.uid");
			serializer.exclude("app.dataSources.inputs.enabled");
			serializer.exclude("app.dataSources.inputs.type");
			serializer.exclude("app.dataSources.inputs.group");
		}
	}
}
