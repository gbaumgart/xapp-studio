<Results>
	{
	for $prefixxcategories in collection("prefixxjevlocation_categories")/prefixxjevlocation_categories
	return
	<item pub="{$prefixxcategories/published}">

		<id>
			{
			$prefixxcategories/id/text()
			}
		</id>
		<title>
			{
			$prefixxcategories/title/text()
			}
		</title>
		<parent>
			{
			$prefixxcategories/parent_id/text()
			}
		</parent>
		<section>
			{
			$prefixxcategories/section/text()
			}
		</section>
		<access>
			{
			$prefixxcategories/access/text()
			}
		</access>
		<descr>
			{
			$prefixxcategories/description/text()
			}
		</descr>
	</item>
	}
</Results>

(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline=""
		          additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no" validator="internal"
		          customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=joomla_2;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="327" y="414"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)