declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";
<articles v="3">
	{
    for $net.bican.wordpress.JoomlaXMLData in /list/net.bican.wordpress.JoomlaXMLData
	return 
	<location_item created="{$net.bican.wordpress.JoomlaXMLData/created}" 
	id="{$net.bican.wordpress.JoomlaXMLData/refId}" 
	ac="{$net.bican.wordpress.JoomlaXMLData/access}" 
	cID="{$net.bican.wordpress.JoomlaXMLData/groupId}"
	lat="{$net.bican.wordpress.JoomlaXMLData/lat}"
	ln="{$net.bican.wordpress.JoomlaXMLData/ln}"
	pc="{$net.bican.wordpress.JoomlaXMLData/postcode}"
    picture="{$net.bican.wordpress.JoomlaXMLData/image}"
	published="{$net.bican.wordpress.JoomlaXMLData/published}"
	>
		<title>
			{
			$net.bican.wordpress.JoomlaXMLData/title/text()
			}
		</title>
		<description>
			{
			$net.bican.wordpress.JoomlaXMLData/description/text()
			}
		</description>
		<street>
			{
			$net.bican.wordpress.JoomlaXMLData/street/text()
			}
		</street>
		<www>
			{
			$net.bican.wordpress.JoomlaXMLData/www/text()
			}
		</www>
        <city>
            {
            $net.bican.wordpress.JoomlaXMLData/region/text()
            }
        </city>
        <city>
            {
            $net.bican.wordpress.JoomlaXMLData/email/text()
            }
        </city>
	</location_item>
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