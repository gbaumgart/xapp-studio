declare option ddtek:detect-XPST0005 "no";
<Locations>
	{
	for $prefixxjev_locations in collection("prefixxjev_locations")/prefixxjev_locations 
	let $c := collection("prefixxcategories")/prefixxcategories[string(id) = string($prefixxjev_locations/catid)]
	where string($prefixxjev_locations/published) = string("true")
	order by $prefixxjev_locations/loc_id 
	return 
	<location_item img="{$prefixxjev_locations/image}" pub="{$prefixxjev_locations/published}" prio="{$prefixxjev_locations/priority}" s="{$prefixxjev_locations/access}" cID="{$prefixxjev_locations/loccat}" lat="{$prefixxjev_locations/geolat}" ln="{$prefixxjev_locations/geolon}" pc="{$prefixxjev_locations/postcode}" gz="{$prefixxjev_locations/geozoom}" p="{$prefixxjev_locations/phone}" id="{$prefixxjev_locations/loc_id}" r="{$prefixxjev_locations/catid}">
		<title>
			{
			$prefixxjev_locations/title/text()
			}
		</title>
		<www>
			{
			$prefixxjev_locations/url/text()
			}
		</www>
		<description>
			{
			$prefixxjev_locations/description/text()
			}
		</description>
		<street>
			{
			$prefixxjev_locations/street/text()
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
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=prefixxjev_locations;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=ibiza;urltype=.xml"/>
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