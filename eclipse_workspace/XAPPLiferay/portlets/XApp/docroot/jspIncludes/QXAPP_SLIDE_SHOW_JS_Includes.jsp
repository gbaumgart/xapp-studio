<%
	String _xappBaseDirectory = (String)request.getSession().getAttribute("xappBaseDirectory");
%>

<script type="text/javascript" src="lib/external/jshashtable.min.js"></script>
<script type="text/javascript" src="lib/external/klass.min.js"></script>
<!-- script type="text/javascript" src="lib/external/soundmanager2.js"></script-->
<script type="text/javascript">
				
				var disqus_shortname = 'joomla254';
				var disqus_url = 'http://www.pearls-media.com/joomla254/index.php/using-joomla/extensions/components/content-component/article-category-list/8-beginners';
				var disqus_identifier = '';
				var disqus_developer = '1';
				var disqus_iframe_css="http://www.pearls-media.com/XApp-portlet/css/Disqus.css";

				
				var addthis_config = {"data_track_clickback":false};
				
				var DISQUS = null;
				var resetDisqus = function(newId){
				    disqus_identifier=newId;
				};
				
				var disqus_config = function()
				{
					this.language = '';
					this.callbacks.afterRender.push(function(){
					    	ctx.getViewManager().onDisqusReady();
					});
				    this.callbacks.onReady.push(function(){
					 });
				};
				function openFacebookShare()
				{
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
		            console.error("login fb");
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
		            console.error("boot fb");
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

	