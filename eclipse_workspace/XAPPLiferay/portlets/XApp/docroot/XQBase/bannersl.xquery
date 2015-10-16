declare variable $lang as xs:string external;
<articles v="3">
	{
	for $jc in collection("jos_banner")/jos_banner
	let $c := collection("jos_jf_content")/jos_jf_content[string(reference_id) = string($jc/bid) and string(reference_field) = string("name") and string(language_id) = string($lang) and string(reference_table) = string("banner")]
	let $d := collection("jos_jf_content")/jos_jf_content[string(reference_id) = string($jc/bid) and string(reference_field) = string("custombannercode") and string(language_id) = string($lang) and string(reference_table) = string("banner")]
	return
	<art l="{$c/language_id}" t="{$c/value}" id="{$jc/bid}">
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
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;table=jos_banner;password=42622D3D44725F;xmlforest=true;DatabaseName=ibiza;urltype=.xml"/>
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
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)
