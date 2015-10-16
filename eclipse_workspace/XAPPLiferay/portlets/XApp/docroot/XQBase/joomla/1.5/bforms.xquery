<Results type="DForm" version="3.1">
	{
	for $bob_form_forms in collection("prefixxform_forms")/prefixxform_forms 
	let $form_details := collection("prefixxform_fields")/prefixxform_fields[string(form_id) = string($bob_form_forms/id)] 
	return 
	<item access="{$bob_form_forms/access}" enctype="{$bob_form_forms/enctype}" published="{$bob_form_forms/published}" title="{$bob_form_forms/form_name}" id="{$bob_form_forms/id}">
	


		<descr>
			{
			$bob_form_forms/introtext/text()
			}
		</descr>
		<customJS>
			{
			$bob_form_forms/custom_js/text()
			}
		</customJS>
		<fields>
			{
			for $bob_form_fields in $form_details 
			return 
			<field required="{$bob_form_fields/required}" published="{$bob_form_fields/published}" ordering="{$bob_form_fields/ordering}" maxLength="{$bob_form_fields/maxlength}" class="{$bob_form_fields/class}" type="{$bob_form_fields/type}" title="{$bob_form_fields/publictitle}" plugin="{$bob_form_fields/plugin}">
				<params>
					{
					$bob_form_fields/params/text()
					}
				</params>
				<descr>
					{
					$bob_form_fields/desc/text()
					}
				</descr>
			</field>
			}
		</fields>
	</item>
	}
</Results>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="0" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline=""
		          additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no" validator="internal"
		          customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=buzzoffbase;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Results/flwr" x="311" y="108"/>
				<block path="Results/flwr/item/fields/flwr" x="80" y="191"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)