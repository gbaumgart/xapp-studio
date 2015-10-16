<!DOCTYPE html>

<#include init />

<#assign deviceBrand = themeDisplay.device.getBrand()?lower_case>
<#assign deviceOS = themeDisplay.device.getOS()?lower_case?replace(" ", "-")>

<html class="<@liferay.language key="lang.dir" /> device-brand-${deviceBrand} device-os-${deviceOS}" dir="<@liferay.language key="lang.dir" />" lang="${w3c_language_id}">

<head>
	<title>${the_title} - ${company_name}</title>

	<meta name="viewport" content="target-densitydpi=160dpi, width=device-width, minimum-scale=1, maximum-scale=1.25">

	<#if (deviceBrand == 'apple')>
		<meta name="format-detection" content="telephone=no">
	</#if>

	${theme.include(top_head_include)}
</head>

<#if (deviceBrand != 'desktop' && deviceBrand != 'unknown')>
	<#assign css_class = "${css_class} aui-view-${themeDisplay.device.screenSize.width}" />
</#if>

<body class="${css_class}">

${theme.include(body_top_include)}

<#if is_signed_in>
        <@liferay.dockbar />
</#if>

<div id="wrapper">
	<a href="#main-content" id="skip-to-content"><@liferay.language key="skip-to-content" /></a>
    <#if !is_signed_in>
		    <!--div id="heading-sign-in">
    			<a href="${sign_in_url}" id="sign-in" rel="nofollow">${sign_in_text}</a>
    	    </div-->
	</#if>

	<header id="banner" role="banner">
	<div id="heading-inner">
		<div id="heading">
			<div class="company-title">
    			<a class="${logo_css_class}" href="${site_default_url}" title="<@liferay.language key="go-to" /> ${site_name}">
            					<img alt="${logo_description}" src="${site_logo}" width="200px" />
    			</a>
			</div>
		</div>

		<#if has_navigation>
			<#include "${full_templates_path}/navigation.ftl" />
		</#if>
		</div>
	</header>

    <!--div id="headerContent">
    </div-->

	<div id="content">
	    <div id="mainContentOuter">
		<!--nav class="site-breadcrumbs" id="breadcrumbs">
			<h1>
				<span><@liferay.language key="breadcrumbs" /></span>
			</h1>

			<@liferay.breadcrumbs />
		</nav-->

		<#if selectable>
			${theme.include(content_include)}
		<#else>
			${portletDisplay.recycle()}

			${portletDisplay.setTitle(the_title)}

			${theme.wrapPortlet("portlet.ftl", content_include)}
		</#if>
		</div>
	</div>

	<footer id="footer" role="contentinfo">
	    <div id="footer-inner">
            <span class="copyright-footer">

            </span>
		</div>
	</footer>
</div>

${theme.include(body_bottom_include)}

</body>

${theme.include(bottom_include)}

</html>