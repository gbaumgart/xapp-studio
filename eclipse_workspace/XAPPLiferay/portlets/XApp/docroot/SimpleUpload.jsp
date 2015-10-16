<%@page import="flexjson.JSONSerializer"%>
<%@page import="pmedia.types.FileUploadStatus"%>
<%@ page language="java" import="javazoom.upload.*,java.util.*" %>
<%

 		String uuid = request.getParameter("uuid");
 		String appId = request.getParameter("appId");
 		String pageId = request.getParameter("pageId");
 		String ciId = request.getParameter("ciId");
 		String responseStatusText = "";
 		
 		
%>

<jsp:useBean id="upListener" scope="session" class="pmedia.wizard.WizardUploadListener"/>
<jsp:useBean id="upBean" scope="page" class="javazoom.upload.UploadBean" >
<jsp:setProperty name="upBean" property="folderstore" value="/tmp/" />
<jsp:setProperty name="upBean" property="parsertmpdir" value="/tmp/"/>
<jsp:setProperty name="upBean" property="filesizelimit" value="85899345982"/>
<jsp:setProperty name="upBean" property="overwrite" value="true"/>
<jsp:setProperty name="upBean" property="dump" value="true"/>
<jsp:setProperty name="upListener" property="uuid" value="<%=uuid %>" />
<jsp:setProperty name="upListener" property="appId" value="<%=appId %>" />
<jsp:setProperty name="upListener" property="pageId" value="<%=pageId %>" />
<jsp:setProperty name="upListener" property="ciId" value="<%=ciId %>" />


<% upBean.addUploadListener(upListener);
	   upListener.theSession = request.getSession(); 
%>  
</jsp:useBean>

  

<%


      String filename = request.getHeader("relativefilename");
      if (MultipartFormDataRequest.isMultipartFormData(request))
      {
      	System.out.println("incoming mrequest");
      	
         // Uses MultipartFormDataRequest to parse the HTTP request.
         MultipartFormDataRequest mrequest =null;
         
         try{
         	mrequest =new MultipartFormDataRequest(request);
         }catch (Exception e)
         {
         	
         	e.printStackTrace();
         	         	
         }
         
         String todo = "upload";
         
       //  String title = mrequest.getParameter("title");
      //   String tags = mrequest.getParameter("tags");
         
         /*
         System.out.println("uuid "  + uuid  );
         System.out.println("appid: "  + appId  );
         */
         
         FileUploadStatus status = new FileUploadStatus();
         
         
         
         //if (mrequest != null) todo = mrequest.getParameter("todo");
	     if ( (todo.equalsIgnoreCase("upload")) )
	     {	     
	     		System.out.println("start parsing files");
                Hashtable files = mrequest.getFiles();
                
                if ( (files != null) && (!files.isEmpty()) )
                {
                    UploadFile file = (UploadFile) files.get("uploadedfile0");
                    if (file != null){ 
	                    
	                    //System.out.println("<li>Form field : uploadfile"+"<BR> Uploaded file : "+file.getFileName()+" ("+file.getFileSize()+" bytes)"+"<BR> Content Type : "+file.getContentType());
	                    // Uses the bean now to store specified by jsp:setProperty at the top.
	                    
	                    status.fileName = file.getFileName();
	                    JSONSerializer serializer = new JSONSerializer();
	                    
	                    responseStatusText = serializer.serialize(status);
	                    
	                    upBean.store(mrequest, "uploadedfile0");
	                    //break;
	                    
						//System.out.println("storing file "  + file.getFileName()  );
					}else
					{
					
						file = (UploadFile) files.get("uploadedfileFlash");
						if(file!=null){
							System.out.println("<li>Form field : uploadfile"+"<BR> Uploaded file : "+file.getFileName()+" ("+file.getFileSize()+" bytes)"+"<BR> Content Type : "+file.getContentType());
		                    // Uses the bean now to store specified by jsp:setProperty at the top.
		                    upBean.store(mrequest, "uploadedfileFlash");
							System.out.println("storing file from flash "  + file.getFileName()  );
						}					
					}
                }
                else
                {
                  System.out.println("No uploaded files");
                }
	     }
         else System.out.println("<BR> todo="+todo);
      }
%>
<textarea style="width:600px; height:150px;"><%=responseStatusText %></textarea>