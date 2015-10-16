<Results>
	{
	for $jos_jev_tags in collection("jos_jev_people")/jos_jev_people 
	let $b := collection("jos_jev_customfields2")/jos_jev_customfields2[(string(target_id) = string($jos_jev_tags/pers_id)) and (string(name) = string("custom0"))] , 
	    $c := collection("jos_jev_customfields2")/jos_jev_customfields2[(string(target_id) = string($jos_jev_tags/pers_id)) and (string(name) = string("custom1"))] , 
	    $d := collection("jos_jev_customfields2")/jos_jev_customfields2[(string(target_id) = string($jos_jev_tags/pers_id)) and (string(name) = string("native"))]  ,
	    $e := collection("jos_jev_customfields2")/jos_jev_customfields2[(string(target_id) = string($jos_jev_tags/pers_id)) and (string(name) = string("hideSmallBanner"))] 
	return 
	<items native="{$d/value}" hideSmall="{$e/value}">
		<title>
			{
			$jos_jev_tags/title/text()
			}
		</title>
		<c0>
			{
			$b/value/text()
			}
		</c0>
		<c1>
			{
			$c/value/text()
			}
		</c1>
		<alias>
			{
			$jos_jev_tags/alias/text()
			}
		</alias>
		<description>
			{
			$jos_jev_tags/description/text()
			}
		</description>
		<published>
			{
			$jos_jev_tags/published/text()
			}
		</published>
		<image>
			{
			$jos_jev_tags/image/text()
			}
		</image>
		<cat>
			{
			$jos_jev_tags/catid0/text()
			}
		</cat>
		<type>
			{
			$jos_jev_tags/type_id/text()
			}
		</type>
		<www>
			{
			$jos_jev_tags/www/text()
			}
		</www>
		<id>
			{
			$jos_jev_tags/pers_id/text()
			}
		</id>
		<alias>
			{
			$jos_jev_tags/alias/text()
			}
		</alias>
	</items>
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
				<block path="Results/flwr" x="341" y="33"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)