// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.jsos.restproxy;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.*;

// Referenced classes of package com.jsos.restproxy:
//            GetPost, Util

public class RestProxyServlet extends HttpServlet
{

    public RestProxyServlet()
    {
    }

    public void init()
        throws ServletException
    {
        proxyHost = getInitParameter("proxyHost");
        proxyPort = getInitParameter("proxyPort");
        rewriteHost = getInitParameter("rewriteHost");
        rewriteHost="true";
        encoding = getInitParameter("encoding");
        remoteHost = getInitParameter("remoteHost");
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        String s = remoteHost;
        if(s == null)
            s = httpservletrequest.getQueryString();
        if(s == null)
        {
            httpservletresponse.sendError(503, "could not get request URL");
            return;
        }
        if(s.length() == 0)
        {
            httpservletresponse.sendError(503, "could not get request URL");
            return;
        }
        String s1 = Util.decodeURIComponent(s);
        Hashtable hashtable = new Hashtable();
        getHeaders(httpservletrequest, hashtable);
        if("true".equals(rewriteHost))
            hashtable.put("Host", httpservletrequest.getServerName());
        GetPost getpost = new GetPost();
        String s2 = getpost.doAction(httpservletrequest.getMethod().toUpperCase(), s1, null, hashtable, -1, proxyHost, proxyPort, encoding, httpservletrequest.getInputStream(), httpservletresponse);
        if(s2 != null)
            httpservletresponse.sendError(503, s2);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        doGet(httpservletrequest, httpservletresponse);
    }

    private void getHeaders(HttpServletRequest httpservletrequest, Hashtable hashtable)
    {
        String s;
        for(Enumeration enumeration = httpservletrequest.getHeaderNames(); enumeration.hasMoreElements(); hashtable.put(s, httpservletrequest.getHeader(s)))
            s = (String)enumeration.nextElement();

    }

    private String getHostInfo(String s)
    {
        String s1 = s;
        int i = s1.indexOf("://");
        if(i > 0)
            s1 = s1.substring(i + 3);
        i = s1.indexOf("/");
        if(i > 0)
            s1 = s1.substring(0, i);
        i = s1.indexOf("?");
        if(i > 0)
            s1 = s1.substring(0, i);
        i = s1.indexOf("#");
        if(i > 0)
            s1 = s1.substring(0, i);
        i = s1.indexOf(";");
        if(i > 0)
            s1 = s1.substring(0, i);
        return s1;
    }

    private String proxyHost;
    private String proxyPort;
    private String rewriteHost;
    private String encoding;
    private String remoteHost;
}
