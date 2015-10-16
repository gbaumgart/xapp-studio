declare option  ddtek:serialize "cdata-section-elements=tags;descr;custombannercode;params";
<Results>
	{
	for $jos_banner in collection("jos_banner")/jos_banner 
	return 
	<item bid="{$jos_banner/bid}" type="{$jos_banner/type}" cid="{$jos_banner/catid}" sticky="{$jos_banner/sticky}" order="{$jos_banner/ordering}" pub="{$jos_banner/showBanner}">
		<clickUrl>
			{
			$jos_banner/clickurl/text()
			}
		</clickUrl>
		<imgUrl>
			{
			$jos_banner/imageurl/text()
			}
		</imgUrl>
		<tags>
			{
			$jos_banner/tags/text()
			}
		</tags>
		<params>
			{
			$jos_banner/params/text()
			}
		</params>
		<descr>
			{
			$jos_banner/description/text()
			}
		</descr>
		<name>
			{
			$jos_banner/name/text()
			}
		</name>
		<custombannercode>
			{
			$jos_banner/custombannercode/text()
			}
		</custombannercode>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

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
				<block path="Results/flwr" x="215" y="85"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)