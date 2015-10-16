<%
	String theAppTemplate =request.getParameter("appTemplate");
    if(theAppTemplate!=null)
    {
    	request.getSession().setAttribute("appTemplate", theAppTemplate);
    }else{
    
    }
%>
