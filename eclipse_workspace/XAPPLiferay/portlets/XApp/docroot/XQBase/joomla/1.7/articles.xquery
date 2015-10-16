declare option  ddtek:serialize "cdata-section-elements=introtext";
<articles v="3">
	{
	for $jos_content in collection("prefixxcontent")/prefixxcontent
	return 
	<art version="{$jos_content/version}" createdBy="{$jos_content/created_by}" ordering="{$jos_content/ordering}" sec="{$jos_content/sectionid}" cid="{$jos_content/catid}" ac="{$jos_content/access}" pub="{$jos_content/state}" id="{$jos_content/id}">
		<title>
			{
			$jos_content/title/text()
			}
		</title>
		<fulltext>
			{
			$jos_content/fulltext/text()
			}
		</fulltext>
		<introtext>
			{
			$jos_content/introtext/text()
			}
		</introtext>
		<alias>
			{
			$jos_content/alias/text()
			}
		</alias>
	</art>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=jos_content;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=ibiza;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="201" y="51"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)