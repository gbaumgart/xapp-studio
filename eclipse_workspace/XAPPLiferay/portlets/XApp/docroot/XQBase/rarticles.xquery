<articles v="3">
	{
	for $articulo in collection("articulo")/articulo 

	where (string($articulo/tipo) = string("2") or (string($articulo/tipo) = string("5")  or (string($articulo/tipo) = string("1"))   )  or (string($articulo/tipo) = string("8")) ) and( string($articulo/fini) = string("0000-00-00") and string($articulo/fend) = string("0000-00-00"))
	return 
	<art r="{$articulo/loc}" id="{$articulo/id}" title="{$articulo/nom}" pub="{$articulo/act}" type="{$articulo/tipo}" beach="{$articulo/es_playa}" city="{$articulo/es_local}" v="{$articulo/vimeo_id}">
		<title/>
	</art>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=articulo;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=site;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="165" y="90"/>
				<block path="articles/flwr/and[1]" x="114" y="76"/>
				<block path="articles/flwr/and[1]/or[0]" x="68" y="70"/>
				<block path="articles/flwr/and[1]/or[0]/=[0]" x="22" y="64"/>
				<block path="articles/flwr/and[1]/or[0]/=[1]" x="22" y="92"/>
				<block path="articles/flwr/and[1]/and[1]" x="68" y="98"/>
				<block path="articles/flwr/and[1]/and[1]/=[0]" x="22" y="92"/>
				<block path="articles/flwr/and[1]/and[1]/=[1]" x="22" y="120"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)