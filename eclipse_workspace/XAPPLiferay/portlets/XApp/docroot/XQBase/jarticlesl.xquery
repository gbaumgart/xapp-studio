declare variable $lang as xs:string external;
<articles v="3">
	{
	for $jc in collection("jos_content")/jos_content
	let $c := collection("jos_jf_content")/jos_jf_content[string(reference_id) = string($jc/id) and string(reference_field) = string("title") and string(language_id) = string($lang)]
	let $d := collection("jos_jf_content")/jos_jf_content[string(reference_id) = string($jc/id) and string(reference_field) = string("introtext") and string(language_id) = string($lang)]
	where string($jc/sectionid) = string("15")
	return
	<art l="{$c/language_id}" t="{$c/value}" id="{$jc/id}">
		<descr>
		{
		 $d/value/text()
		}
		</descr>
	</art>
	}
</articles>
(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;table=jos_content;password=42622D3D44725F;xmlforest=true;DatabaseName=ibiza;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="329" y="29"/>
				<block path="articles/flwr/=[1]" x="343" y="76"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)