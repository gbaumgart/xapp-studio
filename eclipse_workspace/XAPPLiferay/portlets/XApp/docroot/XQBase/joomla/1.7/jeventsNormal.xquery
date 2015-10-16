<Results type="DEvent">
	{
	for $jos_jevents_vevdetail in collection("prefixxjevents_vevdetail")/prefixxjevents_vevdetail
	let $s_repeat := collection("prefixxjevents_repetition")/prefixxjevents_repetition[string(eventid) = string($jos_jevents_vevdetail/evdet_id)] ,
	    $files_link := collection("prefixxjev_files")/prefixxjev_files[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)] ,
	    $eventID := collection("prefixxjevents_vevent")/prefixxjevents_vevent[string(ev_id) = string($jos_jevents_vevdetail/evdet_id)]
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
