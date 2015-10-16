<%
	String _xappBaseDirectory = (String)request.getSession().getAttribute("xappBaseDirectory");
	
	String _path = request.getContextPath();
	String _basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+_path+"/";
	if(!_basePath.contains("8080"))
	{
		_basePath=_basePath.replace(":80","");
	}
%>
<script type="text/javascript" src="<%=_basePath %>lib/external/jshashtable.min.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/klass.min.js"></script>
<script type="text/javascript" src="<%=_basePath %>lib/external/qr/qrcode.js"></script>

