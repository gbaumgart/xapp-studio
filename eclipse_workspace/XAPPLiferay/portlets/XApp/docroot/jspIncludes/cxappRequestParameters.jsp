<%@page import="java.io.File"%>
<%@page import="cmx.tools.AppFeatureConfigUtil"%>
<%@page import="cmx.FolderKeyResolverUtil"%>
<%@page import="cmx.types.FolderKeys"%>
<%
	
	if(AppFeatureConfigUtil.isEnabled("CustomApp"))
	{
	
		String __uuid=(String)request.getSession().getAttribute("uuid");
		String __appId = (String)request.getSession().getAttribute("appId");
		String cxapp =request.getParameter("cxapp");
	    if(cxapp!=null)
	    {
	    	request.getSession().setAttribute("cxapp", cxapp);
	    }else
	    {
	    	
		}
	    
	    String cxappRoot =request.getParameter("cxappRoot");
	    if(cxappRoot!=null)
	    {
	    	request.getSession().setAttribute("cxappRoot", cxappRoot);
	    }else
	    {
   			/***
    			Try app path : 
    		*/
    		String testPath=FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_CXAPP, __uuid, __appId);
    		if(testPath!=null)
    		{
    			//System.out.println("t" + testPath);
    			//cxappRoot = FolderKeys.CM_APP_ROOT + "/" +FolderKeys.CM_CXAPP; 
    			cxappRoot = __uuid + "/apps/" + __appId + "/cxapp";
    			request.getSession().setAttribute("cxappRoot", cxappRoot);
    			cxapp="cxapp";	    			
    			request.getSession().setAttribute("cxapp", cxapp);
    		}
    		
    		
    		
    		/***
    			Try app path : 
    		*/
    		String userPath=FolderKeyResolverUtil.toLocalPath(FolderKeys.CM_USER_ROOT, __uuid, __appId)+ FolderKeyResolverUtil.resolveKey(FolderKeys.CM_CXAPP);
    		if(userPath!=null)
    		{
    			File f= new File (userPath);
    			if(f.exists() && f.isDirectory())
    			{
	    			cxappRoot = __uuid + "/apps/cxapp";
	    			request.getSession().setAttribute("cxappRoot", cxappRoot);
	    			cxapp="cxapp";	    			
	    			request.getSession().setAttribute("cxapp", cxapp);
    			}
    		}


	    	if(cxapp!=null)
	    	{
	    		
	    	}else{
	    		request.getSession().setAttribute("cxapp", null);
    			request.getSession().setAttribute("cxappRoot", null);
	    	}        	    	
	    }
    }else
    {
    	request.getSession().setAttribute("cxapp", null);
    	request.getSession().setAttribute("cxappRoot", null);
    }	
%>
