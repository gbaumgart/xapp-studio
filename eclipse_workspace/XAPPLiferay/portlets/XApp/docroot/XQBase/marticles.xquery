<articles v="3">
	{
	for $articulo in collection("articulo")/articulo 
	where string($articulo/tipo) = string("1") 
	return 
	<art id="{$articulo/id}" title="{$articulo/nom}" pub="{$articulo/act}" type="{$articulo/tipo}">
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
				<block path="articles/flwr/=[1]" x="126" y="213"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)