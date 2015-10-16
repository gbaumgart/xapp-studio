<%
	String _xappBaseDirectory = (String)request.getSession().getAttribute("xappBaseDirectory");
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration"); 
    Boolean debugCSS = true;
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release") || RTConfig.equals("release-debug"))
		{
			debugCSS=false;
		}
	}
	//System.out.println("mob css::::root :" + _xappBaseDirectory);
%>

  <% if(debugCSS){%>
		<link href="<%=_xappBaseDirectory %>cssCommon/video-js.css" type="text/css" media="screen" rel="stylesheet">
		<link href="<%=_xappBaseDirectory %>cssCommon/photoswipe.css" type="text/css" rel="stylesheet"/>
		<link href="<%=_xappBaseDirectory %>cssCommon/psstyles.css" type="text/css" rel="stylesheet"/>
		
		<link href="<%=_xappBaseDirectory %>cssMobile/commons.css" rel="stylesheet"/>
		<link href="<%=_xappBaseDirectory %>cssCommon/forms.css" type="text/css" rel="stylesheet"/>
		
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>dojox/mobile/themes/iphone/iphone.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>cssMobile/themes/iphone/gallery.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>cssCommon/mobileCommons.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>dojox/mobile/themes/common/domButtons/DomButtonRedBadge.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>dojox/mobile/themes/common/domButtons/DomButtonBlackRightArrow16.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>dojox/mobile/themes/common/domButtons/DomButtonWhiteDownArrow16.css"></link>
		<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>cssCommon/xappwebfix.css"></link>
	<%}else{%>
    	<link rel="stylesheet" type="text/css" href="<%=_xappBaseDirectory %>xapp/resources/app.css">
	<%}%>

<style type="text/css">
		
		html, body {
            height: 100%;
            width: 100%;
            padding: 0;
            border: 0;
            position: relative;
        }



        #main {
            height: 100%;
            width: 100%;
            border: 0;
        }

        #header {
            margin: 0;
        }

        #leftAccordion {
            width: 25%;
        }

        #bottomTabs {
            height: 40%;
        }

        #hs-1width {
            width: 400px;
            height: 40px;
        }

            /* pre-loader specific stuff to prevent unsightly flash of unstyled content */
        #loader {
            padding: 0;
            margin: 0;
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: #ededed;
            z-index: 999;
            vertical-align: middle;
        }
        #loaderInner {
            padding: 5px;
            position: relative;
            left: 0;
            top: 0;
            width: 175px;
            background: #3c3;
            color: #fff;
        }
    </style>