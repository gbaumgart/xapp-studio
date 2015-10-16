declare option  ddtek:serialize "cdata-section-elements=introtext";
declare option ddtek:detect-XPST0005 "no";


<articles v="3">
	{
	for $jos_content in collection("wp_posts")/wp_posts 
	let $term_relation := collection("wp_term_relationships")/wp_term_relationships[(string(object_id) = string($jos_content/ID))] , 
	    $terms := collection("wp_terms")/wp_terms[(string(term_id) = string($term_relation/term_taxonomy_id))] 
	where (string($jos_content/post_status) = string("publish")) and (string($jos_content/post_type) = string("post")) 
	return 
	<art parent="{$jos_content/post_parent}" version="" owner="" created="{$jos_content/post_modified}" createdBy="" ordering="{$jos_content/menu_order}" sec="" cid="" ac="" pub="{$jos_content/ping_status}" id="{$jos_content/ID}">
		<title>
			{
			$jos_content/post_title/text()
			}
		</title>
		<fulltext/>
		<introtext>
			{
			$jos_content/post_content/text()
			}
		</introtext>
		<alias/>
		<attributes/>
		<categories>
			{
			for $wp_term_relationships in $term_relation 
			let $termtax := collection("wp_term_taxonomy")/wp_term_taxonomy[(string(taxonomy) = string("category")) and (string(term_taxonomy_id) = string($wp_term_relationships/term_taxonomy_id))], 
				$term := collection("wp_terms")/wp_terms[(string(term_id) = string($termtax/term_id))]
			where $termtax/term_id 
			return 
			<category id="{$termtax/term_id}" title="{$term/name}">{$term/name/text()}</category>
			}
		</categories>
	</art>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="1" profiledepth="" profilelength="100" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="3408" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=wordpress;urltype=.xml"/>
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
				<block path="articles/flwr/art/categories/flwr" x="145" y="261"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source">
			<Fragment url="" path="" action=""/>
		</MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)