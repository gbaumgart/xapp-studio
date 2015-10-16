declare variable $lang as xs:string external;
<routes v="3">
	{
	for $t_ruta_lan in collection("t_ruta_lan")/t_ruta_lan 
	where string($t_ruta_lan/lang) = string($lang)
	return 
	<route l="{$t_ruta_lan/lang}" t="{$t_ruta_lan/tit}" id="{$t_ruta_lan/id}">
		<car>
			{
			$t_ruta_lan/car/text()
			}
		</car>
		<description>
			{
			$t_ruta_lan/descr/text()
			}
		</description>
		<rec>
			{
			$t_ruta_lan/rec/text()
			}
		</rec>
		<rut>
			{
			$t_ruta_lan/rut/text()
			}
		</rut>
	</route>
	}
</routes>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=t_ruta_lan;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=site;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="routes/flwr" x="176" y="54"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)