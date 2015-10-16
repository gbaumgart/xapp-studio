declare option ddtek:detect-XPST0005 "no";


<Results>
	{
	for $jos_sections in collection("prefixxsections")/prefixxsections 
	return 
	<item ordering="{$jos_sections/ordering}" title="{$jos_sections/title}" alias="{$jos_sections/alias}" access="{$jos_sections/access}" scope="{$jos_sections/scope}" id="{$jos_sections/id}" pub="{$jos_sections/published}">
		<description>
			{
			$jos_sections/description/text()
			}
		</description>
		<image>
			{
			$jos_sections/image/text()
			}
		</image>
		<title>
			{
			$jos_sections/title/text()
			}
		</title>
		<id>
			{
			$jos_sections/id/text()
			}
		</id>
		<params>
			{
			$jos_sections/params/text()
			}
		</params>
		<section>com_content</section>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline=""
		          additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no" validator="internal"
		          customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=ibiza;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="227" y="59"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)