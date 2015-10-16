<Locations>
	{
	for $jos_jev_locations in collection("jos_jev_locations")/jos_jev_locations
	where string($jos_jev_locations/published) = string("true")
	order by $jos_jev_locations/loc_id
	return
	<location_item>
		<loc_id>
			{
			$jos_jev_locations/loc_id/text()
			}
		</loc_id>
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
		<phone>
			{
			$jos_jev_locations/phone/text()
			}
		</phone>
		<long>
			{
			$jos_jev_locations/geolon/text()
			}
		</long>
		<lat>
			{
			$jos_jev_locations/geolat/text()
			}
		</lat>
		<description>
			{
			$jos_jev_locations/description/text()
			}
		</description>
		<cat_id>
			{
			$jos_jev_locations/loccat/text()
			}
		</cat_id>
		<published>
			{
			$jos_jev_locations/published/text()
			}
		</published>
		<access>
			{
			$jos_jev_locations/access/text()
			}
		</access>
		<priority>
			{
			$jos_jev_locations/priority/text()
			}
		</priority>
		<street>
			{
			$jos_jev_locations/street/text()
			}
		</street>
		<global>
			{
			$jos_jev_locations/global/text()
			}
		</global>
		<geozoom>
			{
			$jos_jev_locations/geozoom/text()
			}
		</geozoom>
		<city>
			{
			$jos_jev_locations/city/text()
			}
		</city>
		<postcode>
			{
			$jos_jev_locations/postcode/text()
			}
		</postcode>
		<city_cat>
			{
			$jos_jev_locations/catid/text()
			}
		</city_cat>
	</location_item>
	}
</Locations>
(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

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
				<block path="Locations/flwr/=[1]" x="281" y="58"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)