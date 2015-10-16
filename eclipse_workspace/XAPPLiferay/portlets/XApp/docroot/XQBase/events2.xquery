<Results type="DEvent">
	{
	for $jos_jevents_vevdetail in collection("jos_jevents_vevdetail")/jos_jevents_vevdetail
	let $b := collection("jos_jev_locations")/jos_jev_locations[string(loc_id) = string($jos_jevents_vevdetail/location)] ,
	    $s_repeat := collection("jos_jevents_repetition")/jos_jevents_repetition[string(eventid) = string($jos_jevents_vevdetail/evdet_id)] ,
	    $files_link := collection("jos_jev_files")/jos_jev_files[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)] ,
	    $eventID := collection("jos_jevents_vevent")/jos_jevents_vevent[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)] ,
	    $tags := collection("jos_jev_tageventsmap")/jos_jev_tageventsmap[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)]
	order by $jos_jevents_vevdetail/evdet_id descending
	return
	<item eid="{$jos_jevents_vevdetail/evdet_id}">
		
		
		<title>
			{
			$jos_jevents_vevdetail/summary/text()
			}
		</title>
		<sTime>
			{
			$s_repeat/startrepeat/text()
			}
		</sTime>
		<eTime>
			{
			$s_repeat/endrepeat/text()
			}
		</eTime>
		<noETime>
			{
			$jos_jevents_vevdetail/noendtime/text()
			}
		</noETime>
		<pic>
			{
			$files_link/filename/text()
			}
		</pic>
		<detail>
			{
			$jos_jevents_vevdetail/description/text()
			}
		</detail>
		<id>
			{
			$eventID/uid/text()
			}
		</id>
		<lID>
			{
			$jos_jevents_vevdetail/location/text()
			}
		</lID>
		<cID>
			{
			$eventID/catid/text()
			}
		</cID>
		<extra>
			{
			$jos_jevents_vevdetail/extra_info/text()
			}
		</extra>
		<tags>
			{
			$tags/tag_id/text()
			}
		</tags>
	</item>
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
				<block path="Results/flwr" x="327" y="414"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)
