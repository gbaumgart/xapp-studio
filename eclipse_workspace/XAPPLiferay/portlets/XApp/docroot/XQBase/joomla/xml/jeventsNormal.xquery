declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";
<articles v="3">
	{
	for $net.bican.wordpress.JoomlaXMLData in /list/net.bican.wordpress.JoomlaXMLData 
	return 
	<item eid="{$net.bican.wordpress.JoomlaXMLData/refId}" cid="{$net.bican.wordpress.JoomlaXMLData/catid}" ac="{$net.bican.wordpress.JoomlaXMLData/access}" owner="{$net.bican.wordpress.JoomlaXMLData/created__by}" ownerName="{$net.bican.wordpress.JoomlaXMLData/created__by__name}"
	pub="{$net.bican.wordpress.JoomlaXMLData/state}">

		<cID>
			{
			$net.bican.wordpress.JoomlaXMLData/groupId/text()
			}
		</cID>
		<lID>
			{
			$net.bican.wordpress.JoomlaXMLData/locRefId/text()
			}
		</lID>
		<location>
			{
			$net.bican.wordpress.JoomlaXMLData/locRefId/text()
			}
		</location>
		<sTime>
			{
			$net.bican.wordpress.JoomlaXMLData/sTime/text()
			}
		</sTime>
		<eTime>
			{
			$net.bican.wordpress.JoomlaXMLData/eTime/text()
			}
		</eTime>
		<noETime>
			{
			$net.bican.wordpress.JoomlaXMLData/noendtime/text()
			}
		</noETime>
		<title>
			{
			$net.bican.wordpress.JoomlaXMLData/title/text()
			}
		</title>
		<detail>
			{
			$net.bican.wordpress.JoomlaXMLData/description/text()
			}
		</detail>
		<extra_info>
			{
			$net.bican.wordpress.JoomlaXMLData/extra_info/text()
			}
		</extra_info>
	</item>
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