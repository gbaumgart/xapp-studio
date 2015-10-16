<Locations>
	{
	for $jos_jev_locations in collection("jos_jev_locations")/jos_jev_locations 
	let $b := collection("jos_jev_customfields3")/jos_jev_customfields3[string(target_id) = string($jos_jev_locations/loc_id) and (string(name) = string("video0"))]
	let $c := collection("jos_categories")/jos_categories[string(id) = string($jos_jev_locations/catid)]
	where string($jos_jev_locations/published) = string("true")
	order by $jos_jev_locations/loc_id 
	return 
	<location_item img="{$jos_jev_locations/image}" pub="{$jos_jev_locations/published}" prio="{$jos_jev_locations/priority}" s="{$jos_jev_locations/access}" cID="{$jos_jev_locations/loccat}" lat="{$jos_jev_locations/geolat}" ln="{$jos_jev_locations/geolon}" pc="{$jos_jev_locations/postcode}" gz="{$jos_jev_locations/geozoom}" p="{$jos_jev_locations/phone}" id="{$jos_jev_locations/loc_id}" r="{$jos_jev_locations/catid}">
		<title>
			{
			$jos_jev_locations/title/text()
			}
		</title>
		<www>
			{
			$jos_jev_locations/url/text()
			}
		</www>
		<video0>
			{
			$b/value/text()
			}
		</video0>
		
		<description>
			{
			$jos_jev_locations/description/text()
			}
		</description>
		<street>
			{
			$jos_jev_locations/street/text()
			}
		</street>
		<city>
			{
			$c/name/text()
			}
		</city>
	</location_item>
	}
</Locations>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=jos_jev_locations;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=ibiza;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Locations/flwr" x="328" y="25"/>
				<block path="Locations/flwr/=[1]" x="333" y="58"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)