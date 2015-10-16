declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";
<articles v="3">
	{
	for $net.bican.wordpress.JoomlaArticle in /list/net.bican.wordpress.JoomlaArticle 
	return 
	<art created="{$net.bican.wordpress.JoomlaArticle/created}" id="{$net.bican.wordpress.JoomlaArticle/id}" cid="{$net.bican.wordpress.JoomlaArticle/catid}" ac="{$net.bican.wordpress.JoomlaArticle/access}" owner="{$net.bican.wordpress.JoomlaArticle/created__by}" ownerName="{$net.bican.wordpress.JoomlaArticle/created__by__name}" 
	published="{$net.bican.wordpress.JoomlaArticle/state}">
		<title>
			{
			$net.bican.wordpress.JoomlaArticle/title/text()
			}
		</title>
		<introtext>
			{
			$net.bican.wordpress.JoomlaArticle/introtext/text()
			}
		</introtext>
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
				<block path="articles/flwr" x="188" y="82"/>
				<block path="articles/flwr/art/catgories/flwr" x="80" y="170"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)