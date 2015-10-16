<Results>
	{
	for $net.bican.wordpress.Comment in /list/net.bican.wordpress.Comment 
	return 
	<comment relTitle="{$net.bican.wordpress.Comment/post__title}" ownerTitle="{$net.bican.wordpress.Comment/author}" refId="{$net.bican.wordpress.Comment/comment__id}" relId="{$net.bican.wordpress.Comment/post__id}" created="{$net.bican.wordpress.Comment/date__created__gmt}" status="{$net.bican.wordpress.Comment/status}" parent="{$net.bican.wordpress.Comment/parent}" owner="{$net.bican.wordpress.Comment/user__id}">
		<link>
			{
			$net.bican.wordpress.Comment/link/text()
			}
		</link>
		<descr>
			{
			$net.bican.wordpress.Comment/content/text()
			}
		</descr>
		<ownerLink>
			{
			$net.bican.wordpress.Comment/author__url/text()
			}
		</ownerLink>
	</comment>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="..\inputs\wpRawComments.xml" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="..\inputs\wpRawComments.xml" srcSchemaRoot="list" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="198" y="108"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)