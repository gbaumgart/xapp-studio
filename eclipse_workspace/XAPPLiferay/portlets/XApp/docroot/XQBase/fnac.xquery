declare namespace a = "http://zanox.com/productdata/exportservice/v1";

<Results>
	{
	for $product in /a:products/a:product 
	where (contains($product/a:extra2,"PARIS") and contains($product/a:merchantCategory,"Mus")) 
	return 
	<item id="{$product/a:number}">
		<title>
			{
			$product/a:name/text()
			}
		</title>
		<id/>
		<location>
			{
			$product/a:manufacturer/text()
			}
		</location>
		<sTime type="yyyy-MM-dd'T'HH:mm:ss">
			{
			$product/a:validFrom/text()
			}
		</sTime>
		<eTime type="yyyy-MM-dd'T'HH:mm:ss">
			{
			$product/a:validTo/text()
			}
		</eTime>
		<lID/>
		<cID/>
		<extra/>
		<tags/>
		<dlink>
			{
			$product/a:deepLink/text()
			}
		</dlink>
		<mPic>
			{
			$product/a:largeImage/text()
			}
		</mPic>
		<tCat>
			{
			$product/a:merchantCategory/text()
			}
		</tCat>
		<detail0/>
		<detail1>
			{
			$product/a:longDescription/text()
			}
		</detail1>
		<price>
			{
			$product/a:price/text()
			}
		</price>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="fnac_raw.xml" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="fnac_raw.xml" srcSchemaRoot="products" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="350" y="48"/>
				<block path="Results/flwr/and[1]" x="285" y="81"/>
				<block path="Results/flwr/and[1]/contains[0]" x="292" y="52"/>
				<block path="Results/flwr/and[1]/contains[1]" x="200" y="95"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)