<Results>
	{
	for $net.bican.wordpress.JoomlaCategory in /list/net.bican.wordpress.JoomlaCategory 
	return 
	<item pub="{$net.bican.wordpress.JoomlaCategory/published}">
		<id>
			{
			$net.bican.wordpress.JoomlaCategory/id/text()
			}
		</id>
		<title>
			{
			$net.bican.wordpress.JoomlaCategory/title/text()
			}
		</title>
		<descr>
			{
			$net.bican.wordpress.JoomlaCategory/description/text()
			}
		</descr>
		<parent>
			{
			$net.bican.wordpress.JoomlaCategory/parent__id/text()
			}
		</parent>
		<extension>
			{
			$net.bican.wordpress.JoomlaCategory/extension/text()
			}
		</extension>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="..\inputs\joomlaRawCategories.xml" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="..\inputs\joomlaRawCategories.xml" srcSchemaRoot="list" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="135" y="100"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)