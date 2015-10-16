declare option ddtek:detect-XPST0005 "no";
<Results type="DForm" version="3.1">
	{
	for $bob_form_forms in collection("prefixxfacileforms_forms")/prefixxfacileforms_forms
	let $form_details := collection("prefixxfacileforms_elements")/prefixxfacileforms_elements[string(form) = string($bob_form_forms/id)] 
	return 
	<item access="{$bob_form_forms/access}" title="{$bob_form_forms/name}" id="{$bob_form_forms/id}">
		<descr>
			{
			$bob_form_forms/description/text()
			}
		</descr>
		<fields>
			{
			for $bob_form_fields in $form_details 
			return 
			<field published="{$bob_form_fields/published}"  name="{$bob_form_fields/name}"
                   ordering="{$bob_form_fields/ordering}" type="{$bob_form_fields/type}"
                   title="{$bob_form_fields/title}">
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