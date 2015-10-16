<Results>
{
	for $jos_comments in collection("jos_jxcomments_comments")/jos_jxcomments_comments 
	return 
	<item>
		<thread_id>
			{
			$jos_comments/thread_id/text()
			}
		</thread_id>
		<user_id>
			{
			$jos_comments/user_id/text()
			}
		</user_id>
		<context_id>
			{
			$jos_comments/context_id/text()
			}
		</context_id>
		<notify>
			{
			$jos_comments/notify/text()
			}
		</notify>
		<score>
			{
			$jos_comments/score/text()
			}
		</score>
		<name>
			{
			$jos_comments/name/text()
			}
		</name>
		<subject>
			{
			$jos_comments/subject/text()
			}
		</subject>
		<body>
			{
			$jos_comments/body/text()
			}
		</body>
		<created_date>
			{
			$jos_comments/created_date/text()
			}
		</created_date>
		<published>
			{
			$jos_comments/published/text()
			}
		</published>
		<id>
			{
			$jos_comments/id/text()
			}
		</id>
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
				<block path="Results/flwr" x="248" y="173"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)