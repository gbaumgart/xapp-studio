declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";
<articles v="3">
	{
	for $jos_content in collection("prefixxcomments")/prefixxcomments
	return
	<art parent="{$jos_content/comment_parent}" ownerName="{$jos_content/comment_author}" createdBy="{$jos_content/user_id}" created="{$jos_content/comment_date}" cid="{$jos_content/comment_post_ID}" id="{$jos_content/comment_ID}">
		<introtext>
			{
			$jos_content/comment_content/text()
			}
		</introtext>
	</art>
	}
</articles>
(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="1" profiledepth="" profilelength="100" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="3408" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=wordpress2;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="bSchemaAware" value="true"/>
			<advancedProperties name="bXml11" value="false"/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="iValidation" value="0"/>
			<advancedProperties name="bStrip" value="true"/>
			<advancedProperties name="bExtensions" value="true"/>
			<advancedProperties name="iWhitespace" value="0"/>
			<advancedProperties name="bTinyTree" value="true"/>
			<advancedProperties name="bUseDTD" value="false"/>
			<advancedProperties name="bWarnings" value="true"/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="128" y="174"/>
				<block path="articles/flwr/and[1]" x="316" y="76"/>
				<block path="articles/flwr/and[1]/=[0]" x="270" y="70"/>
				<block path="articles/flwr/and[1]/=[1]" x="270" y="98"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)