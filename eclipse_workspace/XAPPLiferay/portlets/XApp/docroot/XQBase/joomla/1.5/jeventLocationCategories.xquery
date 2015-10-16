declare option ddtek:detect-XPST0005 "no";
<Results>
	{
	for $categories in collection("prefixxcategories")/prefixxcategories
	where string($categories/section) = string("com_jevlocations2")
	return
	<item pub="{$categories/published}" level="-1" >
		<id>
			{
			$categories/id/text()
			}
		</id>
		<title>
			{
			$categories/title/text()
			}
		</title>
		<parent>
			{
			$categories/parent_id/text()
			}
		</parent>
		<section>
			{
			$categories/section/text()
			}
		</section>
		<access>
			{
			$categories/access/text()
			}
		</access>
		<descr>
			{
			$categories/description/text()
			}
		</descr>
		<extension>
			{
			$categories/section/text()
			}
		</extension>
	</item>
	}
</Results>

(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

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
				<block path="Results/flwr/or[1]" x="338" y="58"/>
				<block path="Results/flwr/or[1]/and[0]" x="292" y="52"/>
				<block path="Results/flwr/or[1]/and[0]/=[0]" x="246" y="46"/>
				<block path="Results/flwr/or[1]/and[0]/=[1]" x="246" y="74"/>
				<block path="Results/flwr/or[1]/=[1]" x="292" y="80"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)