<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	String _xappBaseDirectory = (String)request.getSession().getAttribute("xappBaseDirectory");
	String RTConfig = (String)request.getSession().getAttribute("runTimeConfiguration"); 
    Boolean debugCSS = true;
	if(RTConfig!=null)
	{
		if(RTConfig.equals("release") || RTConfig.equals("release-debug"))
		{
			//debugCSS=false;
		}
	}
%>
    <script type="text/javascript" charset="utf-8" src="<%=_xappBaseDirectory %>external/klass.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=_xappBaseDirectory %>external/code.photoswipe-3.0.5.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="<%=_xappBaseDirectory %>dojox/mobile/deviceTheme.js"></script>
	<script type="text/javascript" src="<%=_xappBaseDirectory %>external/jshashtable.min.js"></script>
	<script type="text/javascript" src="<%=_xappBaseDirectory %>external/cssParser.min.js"></script>
	<script type="text/javascript" src="<%=_xappBaseDirectory %>external/qr/qrcode.js"></script>
	<script type="text/javascript" src="<%=_xappBaseDirectory %>external/soundmanager2.js"></script>
	
	
	<script type="text/javascript">



				var disqus_shortname = 'joomla254';
				var disqus_url = 'http:/www.pearls-media.com/joomla254/index.php/using-joomla/extensions/components/content-component/article-category-list/8-beginners';
				var disqus_identifier = '';
				var disqus_developer = '1';
				var disqus_iframe_css="www.pearls-media.com/XApp-portlet/css/Disqus.css";
				
				var DISQUS = null;
				var resetDisqus = function(newId){
				
				    disqus_identifier=newId;
				    //http://mc007ibi.dyndns.org:8888/joomla254/index.php/using-joomla/extensions/components/content-component/article-category-list/8-beginners
				    /*
				    try{
						if(DISQUS){
							    DISQUS.reset({
							      reload: false,
							      config: function () {
							        this.page.identifier = newId;
							        this.page.url = 'http://mc007ibi.dyndns.org:8888/joomla254/#!newid';
							      }
							    });
						    }
					    }catch(e){
					    	console.error("d crash");
					    }
					    */
					    
				};
				
				var disqus_config = function()
				{
					this.language = '';
					this.callbacks.afterRender.push(function(){
					    	//console.error("after render");
					    	ctx.getViewManager().onDisqusReady();
					});
				    this.callbacks.onReady.push(function(){
					    	
					    	/*
					    	console.error("on ready");
					    	ctx.getViewManager().onDisqusReady();
					    	console.error("ond ready");
					    	var a = dojo.body().getElementsByClassName("layout");
				            var disqusRoot = dojo.byId("layout");
				            if(disqusRoot){
				                domClass.add(disqusRoot,"disqusRoot");
				            }
				            */
					 });
				};
				function openFacebookShare()
				{
					//menu.hide(true);
					

    /*
					if(!hasFacebook){
						ctx.getUrlHandler().openExternalLocation("http://www.facebook.com/sharer.php?u=http://www.Google.com");
					}else{
					   var title = xapp.utils.getShareTitle();
					   var url = xapp.utils.getShareUrl();
					   var pic = xapp.utils.getSharePicture();
					   var text = xapp.utils.getShareDetailText();
					   xapp.utils.openFacebookShare(title,url,null,pic,text);
					}
    */
				};
				function loginFB(appId) 
				{
					/*
		            if (isConnected) {
		                console.error("already connected fb, get users");
		                //getUserFriends();
		                return;
		            }
		            */
		            //console.error("login fb");
		            //window.fbAsyncInit = function() {
		            try{
		            	if(FB!='undefined')
                        {
				            FB.init({ appId:fbAppId,
				                status: true,
				                cookie: true,
				                xfbml: true,
				                oauth: true});
		            		
		            		//FB.Event.subscribe('auth.statusChange', handleStatusChange);		            //};
		        			};
	        				
	        			}catch(e){
	        			}
        			
        		  }
				  function bootFacebook(fbAppId) 
				  {
		            //console.error("boot fb");
		            var e = document.createElement('script');
		            e.async = true;
		            e.src = document.location.protocol + '//connect.facebook.net/en_US/all.js';
		            document.getElementById('fb-root').appendChild(e);
		            setTimeout(function() {
		                loginFB(fbAppId);
		            }, 1000);
		        };
		        function onSharePickerOpen()
		        {
		        	//console.error("on share picker open");
		        	xapp.utils.onSharePickerOpen();
		        };
		        var rpcProxy=null;
		        var rpcProxyDomain=null;
		        var rpcProxyDomainPort=null;
		        var rpcProxyPrefix=null;
				
				/*
				var disqus_ready=function() 
				{
                       console.error("ready");
				};
				function disqus_config() {
					    
				}
				*/
                                

   </script>
	
	<!-- 	
	<script type="text/javascript" src="<%=_xappBaseDirectory %>external/jshashtable_src.js"></script>
	
    <script type="text/javascript" charset="utf-8" src="/html/lib/external/mxn/mxn.js?(google, yahoo, cloudmade, microsoft, openlayers,googlev3)"></script>
    <script type="text/javascript" charset="utf-8" src="<%=_xappBaseDirectory %>external/klass.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=_xappBaseDirectory %>external/code.photoswipe-3.0.4.min.js"></script>        
    -->
