declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";
<articles v="3">
	{
	for $net.bican.wordpress.JoomlaXMLData in /list/net.bican.wordpress.JoomlaXMLData
	return
	<item id="{$net.bican.wordpress.JoomlaXMLData/refId}"
	published="{$net.bican.wordpress.JoomlaXMLData/published}"
    title="{$net.bican.wordpress.JoomlaXMLData/title}">
		<introtext>
			{
			$net.bican.wordpress.JoomlaXMLData/description/text()
			}
		</introtext>
        <fields>
            {
            for $field in $net.bican.wordpress.JoomlaXMLData/items/net.bican.wordpress.JoomlaXMLData
            return
            <field published="{$field/published}" type="{$field/type}" title="{$field/title}" name="{$field/name}">
            </field>
            }
        </fields>
	</item>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="..\inputs\breezingRawForms.xml" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="..\inputs\breezingRawForms.xml" srcSchemaRoot="list" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
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