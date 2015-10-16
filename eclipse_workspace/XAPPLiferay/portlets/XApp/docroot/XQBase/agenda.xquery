<articles v="3">
	{
	for $articulo in collection("articulo")/articulo 
	where (((string($articulo/tipo) = string("2")) and (string($articulo/fini) != string("0000-00-00"))) and (year-from-date($articulo/fini) >= 2011)) 
	return 
	<art ln="{$articulo/lon}" fiesta="{$articulo/fiesta}" lat="{$articulo/lat}" lu="{$articulo/lugar}" sport="{$articulo/deporte}" cult="{$articulo/cultura}" z="{$articulo/es_playa}" lid="{$articulo/loc}" eTimeH="{$articulo/hend}" eTime="{$articulo/fend}" sTimeH="{$articulo/hini}" id="{$articulo/id}" sTime="{$articulo/fini}">
		<title>
			{
			$articulo/nom/text()
			}
		</title>
		<www>
			{
			$articulo/url/text()
			}
		</www>
		
	</art>
	}
</articles>(: Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" useresolver="yes" url="" outputurl="" processortype="datadirect" tcpport="29471536" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" host="" port="0" user="" password="" validateoutput="no"
		          validator="internal" customvalidator="">
			<collection name="mysql_localhost_3306" url="jdbc:xquery:mysql://localhost:3306;table=articulo;user=root;password=42622D3D44725F;xmlforest=true;DatabaseName=site;urltype=.xml"/>
			<advancedProperties name="DocumentURIResolver" value=""/>
			<advancedProperties name="CollectionURIResolver" value=""/>
			<advancedProperties name="ModuleURIResolver" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition>
			<template name="xquery_body">
				<block path="articles/flwr" x="423" y="33"/>
				<block path="articles/flwr/and[1]" x="338" y="76"/>
				<block path="articles/flwr/and[1]/and[0]" x="292" y="70"/>
				<block path="articles/flwr/and[1]/and[0]/=[0]" x="246" y="64"/>
				<block path="articles/flwr/and[1]/and[0]/!=[1]" x="246" y="92"/>
				<block path="articles/flwr/and[1]/&gt;=[1]" x="292" y="98"/>
				<block path="articles/flwr/and[1]/&gt;=[1]/year-from-date[0]" x="246" y="92"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
:)