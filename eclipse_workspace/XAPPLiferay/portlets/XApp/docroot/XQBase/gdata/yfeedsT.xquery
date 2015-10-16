<articles v="3">
	{
	for $net.bican.wordpress.Page in /list/net.bican.wordpress.Page 
	return 
	<art owner="{$net.bican.wordpress.Page/wp__author__id}" created="{$net.bican.wordpress.Page/dateCreated}" id="{$net.bican.wordpress.Page/page__id}">
		<title>
			{
			$net.bican.wordpress.Page/title/text()
			}
		</title>
		<introtext>
			{
			$net.bican.wordpress.Page/description/text()
			}
		</introtext>
		<catgories>
			{
			for $string in $net.bican.wordpress.Page/categories/list/string 
			return 
			<category>
				{
				$string/text()
				}
			</category>
			}
		</catgories>
	</art>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="..\inputs\wpRawPosts.xml" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="..\inputs\wpRawPosts.xml" srcSchemaRoot="list" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="185" y="133"/>
				<block path="articles/flwr/art/catgories/flwr" x="80" y="170"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)