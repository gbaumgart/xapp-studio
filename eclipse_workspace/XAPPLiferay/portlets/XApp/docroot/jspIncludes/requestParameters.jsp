<%
	String _uuid =request.getParameter("uuid");
    String _appId = request.getParameter("appId");
    String _url = request.getParameter("url");
    String _device = request.getParameter("device");
    
    String _cacheBust = request.getParameter("cacheBust");
    
    String _runTimeConfiguration=request.getParameter("runTimeConfiguration");
   
    
    if(_uuid!=null && _uuid.length()>35)
    {
    	_uuid=_uuid.substring(0,36);
    }
    
    if(_appId!=null && _appId.contains("/"))
    {
    	_url=_appId.substring(_appId.indexOf("/"),_appId.length());
    	_appId=_appId.substring(0,_appId.indexOf("/"));    
    	
    }
    if(_appId!=null){
    	request.getSession().setAttribute("appId", _appId);
    }
    if(_url!=null){
    	request.getSession().setAttribute("url", _url);
    }
    if(_device!=null){
    	request.getSession().setAttribute("device", _device);
    }
    
     if(_cacheBust!=null){
    	request.getSession().setAttribute("cacheBust", _cacheBust);
    }else{
    	request.getSession().setAttribute("cacheBust", null);
    }
    
    if(_runTimeConfiguration!=null){
    	request.getSession().setAttribute("runTimeConfiguration", _appId);
    }

    if(_uuid!=null){
    	request.getSession().setAttribute("uuid", _uuid.substring(0,36));
    }
     
    
    if(_runTimeConfiguration!=null){
    	request.getSession().setAttribute("runTimeConfiguration", _runTimeConfiguration);
    }	     

%>
