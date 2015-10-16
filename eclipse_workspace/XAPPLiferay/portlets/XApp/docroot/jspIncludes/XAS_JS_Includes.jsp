<%
	String _xappBaseDirectory = (String)request.getSession().getAttribute("xappBaseDirectory");
	String _path = request.getContextPath();
	
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	if(!_basePath.contains("8080"))
	{
		_basePath=_basePath.replace(":80","");
	}
%>
<script type="text/javascript" src="<%=_basePath %>lib/external/easyXDM.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/jshashtable.min.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/cssParser.min.js"></script>
<script type="text/javascript" src="http://feather.aviary.com/js/feather.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/klass.min.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/code.photoswipe-3.0.4.min.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/qr/qrcode.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/soundmanager2.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/colorPicker_src.js"></script>
<script src="<%=_basePath %>lib/ace-noconflict.min.js" type="text/javascript"></script>
<!-- script src="http://maps.google.com/maps/api/js?v=3.1&sensor=true&language=en" type="text/javascript"></script-->

<script type="text/javascript" charset="utf-8" src="<%=_xappBaseDirectory %>dojox/mobile/deviceTheme.js"></script>
<script type="text/javascript">
				
				var disqus_shortname = 'joomla254';
				var disqus_url = 'http://www.pearls-media.com/joomla254/index.php/using-joomla/extensions/components/content-component/article-category-list/8-beginners';
				var disqus_identifier = '';
				var disqus_developer = '0';
				var disqus_iframe_css="http://www.pearls-media.com/XApp-portlet/css/Disqus.css";
				var device=null;
				
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
					

					if(!hasFacebook){
						ctx.getUrlHandler().openExternalLocation("http://www.facebook.com/sharer.php?u=http://www.Google.com");
					}else{
					   var title = xapp.utils.getShareTitle();
					   var url = xapp.utils.getShareUrl();
					   var pic = xapp.utils.getSharePicture();
					   var text = xapp.utils.getShareDetailText();
					   xapp.utils.openFacebookShare(title,url,null,pic,text);
					}
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
		            FB.init({ appId:fbAppId,
		                status: true,
		                cookie: true,
		                xfbml: true,
		                oauth: true});
            		
            		//FB.Event.subscribe('auth.statusChange', handleStatusChange);
            //};
        			};
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
				
</script>
<!-- script src="http://maps.google.com/maps?file=api&v=2&key=ABQIAAAA6ka83PEs9WszgUvcGo1PSxQ34KTTOwBGWn8n7hm26gL35-q5RxT7qnWZs8azJY7Cvs64FQ6ez7wpFQ"></script-->
<!-- script type="text/javascript" charset="utf-8" src="/html/lib/external/mxn/mxn.js?(google, yahoo, cloudmade, microsoft, openlayers,googlev3)"></script-->

	