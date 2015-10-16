<Results>
	{
	for $jos_categories in collection("jos_jf_content")/jos_jf_content 
	where ((((string($jos_categories/reference_table) = string("categories")) and (string($jos_categories/reference_field) = string("title"))) or ((string($jos_categories/reference_table) = string("menu")) and (string($jos_categories/reference_field) = string("name")))) or (((string($jos_categories/reference_table) = string("jev_locations")) and (string($jos_categories/reference_field) = string("title"))) and (number($jos_categories/published) = 1))) 
	return 
	<item>
		<id>
			{
			$jos_categories/reference_id/text()
			}
		</id>
		<title>
			{
			$jos_categories/value/text()
			}
		</title>
		<language_id>
			{
			$jos_categories/language_id/text()
			}
		</language_id>
		<type>
			{
			$jos_categories/reference_table/text()
			}
		</type>
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
				<block path="Results/flwr" x="327" y="414"/>
				<block path="Results/flwr/or[1]" x="335" y="58"/>
				<block path="Results/flwr/or[1]/or[0]" x="289" y="15"/>
				<block path="Results/flwr/or[1]/or[0]/and[0]" x="234" y="3"/>
				<block path="Results/flwr/or[1]/or[0]/and[0]/=[0]" x="139" y="0"/>
				<block path="Results/flwr/or[1]/or[0]/and[0]/=[1]" x="173" y="72"/>
				<block path="Results/flwr/or[1]/or[0]/and[1]" x="273" y="129"/>
				<block path="Results/flwr/or[1]/or[0]/and[1]/=[0]" x="190" y="125"/>
				<block path="Results/flwr/or[1]/or[0]/and[1]/=[1]" x="237" y="164"/>
				<block path="Results/flwr/or[1]/and[1]" x="309" y="207"/>
				<block path="Results/flwr/or[1]/and[1]/and[0]" x="230" y="74"/>
				<block path="Results/flwr/or[1]/and[1]/and[0]/=[0]" x="158" y="34"/>
				<block path="Results/flwr/or[1]/and[1]/and[0]/=[1]" x="223" y="140"/>
				<block path="Results/flwr/or[1]/and[1]/=[1]" x="264" y="273"/>
				<block path="Results/flwr/or[1]/and[1]/=[1]/number[0]" x="78" y="262"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)