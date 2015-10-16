<Results>
	{
	for $jos_ratings in collection("jos_jxcomments_ratings")/jos_jxcomments_ratings 
	where string($jos_ratings/context) = string("jevlocation") 
	return 
	<item>
		<thread_id>
			{
			$jos_ratings/thread_id/text()
			}
		</thread_id>
		<context>
			{
			$jos_ratings/context/text()
			}
		</context>
		<pscore_total>
			{
			$jos_ratings/pscore_total/text()
			}
		</pscore_total>
		<pscore_count>
			{
			$jos_ratings/pscore_count/text()
			}
		</pscore_count>
		<pscore>
			{
			$jos_ratings/pscore/text()
			}
		</pscore>
		<mscore_total>
			{
			$jos_ratings/mscore_total/text()
			}
		</mscore_total>
		<mscore_count>
			{
			$jos_ratings/mscore_count/text()
			}
		</mscore_count>
		<update_date>
			{
			$jos_ratings/updated_date/text()
			}
		</update_date>
		<context_id>
			{
			$jos_ratings/context_id/text()
			}
		</context_id>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline=""
		          additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no" validator="internal"
		          customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=poi;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="384" y="36"/>
				<block path="Results/flwr/=[1]" x="152" y="7"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)