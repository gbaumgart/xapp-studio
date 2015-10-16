<Locations v="3">
	{
	for $empresa in collection("empresa")/empresa 
	return 
	<loc c0="{$empresa/vimeo_id}" mt="{$empresa/tipo}" st="{$empresa/subtipo}" rid="{$empresa/idfed}" nhabs="{$empresa/nhabs}" bcat="{$empresa/categoria}" zon="{$empresa/zona}" mun="{$empresa/mun}" lid="{$empresa/loc}" m="{$empresa/mail}" img="" pub="{$empresa/act}" prio="" s="" cID="" lat="{$empresa/lat}" ln="{$empresa/lon}" pc="{$empresa/cp}" gz="" p="{$empresa/tel}" id="{$empresa/id}">
		<title>
			{
			$empresa/nom/text()
			}
		</title>
		<www>
			{
			$empresa/web/text()
			}
		</www>
		<street>
			{
			$empresa/dir/text()
			}
		</street>
	</loc>
	}
</Locations>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=empresa;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=site;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="Locations/flwr" x="129" y="80"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)