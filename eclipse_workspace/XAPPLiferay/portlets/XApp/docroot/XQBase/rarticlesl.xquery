declare variable $lang as xs:string external;
<articles v="3">
	{
	for $articulo_lan in collection("articulo_lan")/articulo_lan 
	let $b := collection("articulo")/articulo[string(id) = string($articulo_lan/id)] 
	where ( string($b/tipo) = string("2") or (string($b/tipo) = string("5") or (string($b/tipo) = string("1")))   or (string($b/tipo) = string("8")) ) and( string($b/fini) = string("0000-00-00") and string($b/fend) = string("0000-00-00") and string($articulo_lan/lang) = string($lang)  )
	return 
	<art l="{$articulo_lan/lang}" t="{$articulo_lan/tit}" id="{$articulo_lan/id}">
		<descr>
			{
			$articulo_lan/descr/text()
			}
		</descr>
		<subt>
			{
			$articulo_lan/subtit/text()
			}
		</subt>
		<observ>
			{
			$articulo_lan/observ/text()
			}
		</observ>

	</art>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=site;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="158" y="36"/>
				<block path="articles/flwr/=[1]" x="91" y="100"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)