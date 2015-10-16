<Locations>
	{
	for $links in collection("bob_mt_links")/bob_mt_links 
	let $b := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("30"))]
	let $file := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("23"))]
	let $visa := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("31"))]
	let $dressCode := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("36"))]
	let $audience := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("34"))]
	let $english := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("33"))]
	let $hours := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("39"))]
	let $atmosphere := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("35"))]
	let $tags := collection("bob_mt_cfvalues")/bob_mt_cfvalues[string(link_id) = string($links/link_id) and (string(cf_id) = string("28"))]
	let $cats := collection("bob_mt_cl")/bob_mt_cl[string(link_id) = string($links/link_id)]
	let $pic := collection("bob_mt_cfvalues_att")/bob_mt_cfvalues_att[string(link_id) = string($links/link_id)]
	where string($links/link_published) = string("1")
	order by $links/link_id 
	
	return 
	<location_item picture="{$pic/raw_filename}" cID="{$cats/cat_id}" pub="{$links/link_published}" s="{$links/link_approved}" lat="{$links/lat}" ln="{$links/lng}" pc="{$links/postcode}" gz="{$links/zoom}" p="{$links/telephone}" id="{$links/link_id}"  visa="{$visa/value/text()}" dressCode="{$dressCode/value/text()}" audience="{$audience/value/text()}" english="{$english/value/text()}" hours="{$hours/value/text()}" atmosphere="{$atmosphere/value/text()}" tags="{$tags/value/text()}" rating="{$links/link_rating/text()}"  featured="{$links/link_featured/text()}">
		<title>
			{
			$links/link_name/text()
			}
		</title>
		<www>
			{
			$links/website/text()
			}
		</www>
		<email>
			{
			$links/email/text()
			}
		</email>
		<price>
			{
			$b/value/text()
			}
		</price>
		
		<description>
			{
			$links/link_desc/text()
			}
		</description>
		<street>
			{
			$links/address/text()
			}
		</street>
		<city>
			{
			$links/city/text()
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
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=buzzoffbase;urltype=.xml"/>
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