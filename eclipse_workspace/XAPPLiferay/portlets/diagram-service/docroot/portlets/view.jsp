<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml"
xmlns:b3mn="http://b3mn.org/2007/b3mn"
xmlns:ext="http://b3mn.org/2007/ext"
xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
xmlns:atom="http://b3mn.org/2007/atom+xhtml">

<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" %>
<%
	String path = request.getContextPath();
	path="/diagrams-web/editor";
	
	String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	
	//basePath+="backend/";
	
	System.out.println("basePath: " + basePath);
	
	String dstLang = "en_us";
	Locale rLocale = request.getLocale();
	if(rLocale!=null)
	{
		dstLang = rLocale.getLanguage();
	}
	
	System.out.println("dst language in : " + dstLang );
	
	if(!dstLang.equals("en_us") && !dstLang.equals("es") && !dstLang.equals("ru") && !dstLang.equals("de"))
	{
		dstLang = "en_us";
	}
	
	System.out.println("dst language : " + dstLang );
	
	
 %>
 
<head profile="http://purl.org/NET/erdf/profile">
    <title>Oryx-Editor - Oryx</title>
    <base href="<%=basePath%>">
    <!-- libraries -->
    <script src="lib/prototype-1.5.1.js" type="text/javascript"></script>
    <script src="lib/path_parser.js" type="text/javascript"></script>
    <script src="lib/ext-2.0.2/adapter/ext/ext-base.js" type="text/javascript"></script>

    <script src="lib/ext-2.0.2/ext-all-debug.js" type="text/javascript"></script>
    <script src="lib/ext-2.0.2/color-field.js" type="text/javascript"></script>

    <style media="screen" type="text/css">
        @import url("lib/ext-2.0.2/resources/css/ext-all.css");
        @import url("lib/ext-2.0.2/resources/css/xtheme-gray.css");

    </style>
    <!-- oryx editor -->
    <!-- language files -->
    
    <script src="i18n/translation_<%=dstLang %>.js" type="text/javascript"></script>
    
    
    <script src="scripts/utils.js" type="text/javascript"></script>
    <script src="scripts/kickstart.js" type="text/javascript"></script>
    <script src="scripts/erdfparser.js" type="text/javascript"></script>
    <script src="scripts/datamanager.js" type="text/javascript"></script>
    <script src="scripts/clazz.js" type="text/javascript"></script>


    <script src="scripts/Core/SVG/editpathhandler.js" type="text/javascript"></script>
    <script src="scripts/Core/SVG/minmaxpathhandler.js" type="text/javascript"></script>
    <script src="scripts/Core/SVG/pointspathhandler.js" type="text/javascript"></script>
    <script src="scripts/Core/SVG/svgmarker.js" type="text/javascript"></script>
    <script src="scripts/Core/SVG/svgshape.js" type="text/javascript"></script>
    <script src="scripts/Core/SVG/label.js" type="text/javascript"></script>
    <script src="scripts/Core/Math/math.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/stencil.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/property.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/propertyitem.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/complexpropertyitem.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/rules.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/stencilset.js" type="text/javascript"></script>
    <script src="scripts/Core/StencilSet/stencilsets.js" type="text/javascript"></script>



    <script src="scripts/Core/uiobject.js" type="text/javascript"></script>

    <script src="scripts/Core/abstractPlugin.js" type="text/javascript"></script>
    <script src="scripts/Core/abstractLayouter.js" type="text/javascript"></script>
    <script src="scripts/Core/abstractshape.js" type="text/javascript"></script>

    <script src="scripts/Core/Controls/control.js" type="text/javascript"></script>
    <script src="scripts/Core/Controls/docker.js" type="text/javascript"></script>
    <script src="scripts/Core/Controls/magnet.js" type="text/javascript"></script>

    <script src="scripts/Core/command.js" type="text/javascript"></script>
    <script src="scripts/Core/bounds.js" type="text/javascript"></script>
    <script src="scripts/Core/canvas.js" type="text/javascript"></script>


    <script src="scripts/Core/svgDrag.js" type="text/javascript"></script>
    <script src="scripts/Core/shape.js" type="text/javascript"></script>
    <script src="scripts/Core/node.js" type="text/javascript"></script>
    <script src="scripts/Core/edge.js" type="text/javascript"></script>
    
    <script src="scripts/Plugins/RichTextToolbar.js" type="text/javascript"></script>
    

    <script src="scripts/config.js" type="text/javascript"></script>
    <script src="scripts/oryx.js" type="text/javascript"></script>

    <script src="scripts/Core/main.js" type="text/javascript"></script>


    <script src="profiles/bpmn2.0.js" type="text/javascript"></script>

    <!--script src="scripts/Plugins/bpmn2pn.js" type="text/javascript"></script-->

    <link rel="Stylesheet" media="screen" href="css/theme_norm.css" type="text/css"/>
    <!-- erdf schemas -->
    <link rel="schema.dc" href="http://purl.org/dc/elements/1.1/"/>
    <link rel="schema.dcTerms" href="http://purl.org/dc/terms/"/>
    <link rel="schema.b3mn" href="http://b3mn.org"/>
    <link rel="schema.oryx" href="http://oryx-editor.org/"/>
    <link rel="schema.raziel" href="http://raziel.org/"/>

    <script type='text/javascript'>
        if (!ORYX) var ORYX = {};
        if (!ORYX.CONFIG) ORYX.CONFIG = {};
            ORYX.CONFIG.PLUGINS_CONFIG = ORYX.CONFIG.PROFILE_PATH + 'bpmn2.0.xml';
            ORYX.CONFIG.SSET = 'stencilsets/bpmn2.0/bpmn2.0.json';
            ORYX.CONFIG.SSEXTS = [];
            if ('undefined' == typeof(window.onOryxResourcesLoaded))
            {
                ORYX.Log.warn('No adapter to repository specified, default used. You need a function window.onOryxResourcesLoaded that obtains model-JSON from your repository');
                window.onOryxResourcesLoaded = function ()
                {
                    if (location.hash.slice(1).length == 0 || location.hash.slice(1).indexOf('new') != -1)
                    {
                        var stencilset = ORYX.Utils.getParamFromUrl('stencilset') ? ORYX.Utils.getParamFromUrl('stencilset') : 'stencilsets/bpmn2.0/bpmn2.0.json';
                        console.error('loading editor');
                        new ORYX.Editor({
                            id: 'oryx-canvas123',
                            stencilset: {
                                url: '/oryx/' + stencilset
                            }
                        });
                    } else {
                        console.error('creating editor from url');
                        ORYX.Editor.createByUrl('/backend/poem' + location.hash.slice(1) + '/json', {id: 'oryx-canvas123'});
                    }
                }
            }
    </script>
</head>
<body style="overflow:hidden;">
<div id="root" class="xapp-diagram-manager-root" style="width:100%;height:800px;position: relative"></div>
<div class='processdata' style='display:none'>
</div>
</body>
</html>
