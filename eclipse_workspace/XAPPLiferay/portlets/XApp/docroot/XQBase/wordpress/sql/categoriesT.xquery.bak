declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";


<Results>
	{
	for $wp_term_taxonomy in collection("wp_term_taxonomy")/wp_term_taxonomy 
	let $term := collection("wp_terms")/wp_terms[(string(term_id) = string($wp_term_taxonomy/term_id))] 
	where string($wp_term_taxonomy/taxonomy) = string("category") 
	return 
	<item type="" id="{$term/term_id}">
		<title>
			

			{
			$term/name/text()
			}
		</title>
		<parent>
			{
			$wp_term_taxonomy/parent/text()
			}
		</parent>
		<descr>
			{
			$wp_term_taxonomy/description/text()
			}
		</descr>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="1" profiledepth="" profilelength="100" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="3408" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=wordpress;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="bSchemaAware" value="true"/>
			<advancedProperties name="bXml11" value="false"/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="bStrip" value="true"/>
			<advancedProperties name="iValidation" value="0"/>
			<advancedProperties name="bExtensions" value="true"/>
			<advancedProperties name="iWhitespace" value="0"/>
			<advancedProperties name="bTinyTree" value="true"/>
			<advancedProperties name="bWarnings" value="true"/>
			<advancedProperties name="bUseDTD" value="false"/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="275" y="219"/>
				<block path="Results/flwr/=[1]" x="180" y="243"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)