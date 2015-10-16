declare variable $lang as xs:string external;
<articlesl v="3">
	{
	for $articulo in collection("articulo_lan")/articulo_lan 
	where string($articulo/lang) = string($lang)
	return 
	<artl id="{$articulo/id}" subt="{$articulo/subtit}" l="{$articulo/lang}">
		<title>
			{
			$articulo/tit/text()
			}
		</title>
		<breve>
			{
			$articulo/breve/text()
			}
		</breve>
		<descr>
			{
			$articulo/descr/text()
			}
		</descr>
		<observ>
			{
			$articulo/observ/text()
			}
		</observ>
	</artl>
	}
</articlesl>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=articulo_lan;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=site;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articlesl/flwr" x="175" y="38"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)