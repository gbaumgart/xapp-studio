declare option ddtek:detect-XPST0005 "no";<Results v="3">	{	for $jc in collection("prefixxvirtuemart_categories")/prefixxvirtuemart_categories 	let $c := collection("prefixxvirtuemart_categories_en_gb")/prefixxvirtuemart_categories_en_gb[string(virtuemart_category_id) = string($jc/virtuemart_category_id)],	    $d := collection("prefixxvirtuemart_category_categories")/prefixxvirtuemart_category_categories[string(category_child_id) = string($jc/virtuemart_category_id)],	    $mg := collection("prefixxvirtuemart_category_medias")/prefixxvirtuemart_category_medias[string(virtuemart_category_id) = string($jc/virtuemart_category_id)]	return 	<item ownerRefId="{$c/created_by}" pub="{$jc/published}">		<pictures>		{				for $pic in $mg				let $mpic := collection("prefixxvirtuemart_medias")/prefixxvirtuemart_medias[string(virtuemart_media_id) = string($pic/virtuemart_media_id)]				return				<picture mime="{$mpic/file_mimetype}" title="{$mpic/file_title}">				{					$mpic/file_url/text()				}				</picture>		}		</pictures>		<id>			{			$jc/virtuemart_category_id/text()			}		</id>		<title>			{			$c/category_name/text()			}</title>		<parent>			{			$d/category_parent_id/text()			}</parent>		<section>			</section>		<access>			</access>		<introText>						{			$c/category_description/text()			}</introText>	</item>	}</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.<metaInformation>	<scenarios>		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"		          validator="internal" customvalidator="">			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;table=jos_virtuemart_categories;password=42622D3D44725F;xmlforest=true;DatabaseName=joomla_6;urltype=.xml"/>			<advancedProperties name="DocumentURIResolver" value=""/>			<advancedProperties name="CollectionURIResolver" value=""/>			<advancedProperties name="ModuleURIResolver" value=""/>		</scenario>	</scenarios>	<MapperMetaTag>		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>		<MapperBlockPosition>			<template name="xquery_body">				<block path="Results/flwr" x="431" y="54"/>			</template>		</MapperBlockPosition>		<TemplateContext></TemplateContext>		<MapperFilter side="source"></MapperFilter>	</MapperMetaTag></metaInformation>:)