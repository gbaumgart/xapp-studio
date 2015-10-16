// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.jsos.restproxy;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;

public class GetPost
{

    public GetPost()
    {
        NEWLINE = "\n";
        NEWLINE = System.getProperty("line.separator");
    }

    public String doAction(String s, String s1, Hashtable hashtable, Hashtable hashtable1, int i, String s2, String s3, 
            String s4, ServletInputStream servletinputstream, HttpServletResponse httpservletresponse)
    {
        if(i > 0)
        {
            Properties properties = System.getProperties();
            properties.put("sun.net.client.defaultConnectTimeout", (new StringBuilder()).append("").append(i).toString());
            properties.put("sun.net.client.defaultReadTimeout", (new StringBuilder()).append("").append(i).toString());
            System.setProperties(properties);
        }
        if(s2 != null && s3 != null)
        {
            Properties properties1 = System.getProperties();
            properties1.put("proxySet", "true");
            properties1.put("proxyHost", s2);
            properties1.put("proxyPort", s3);
            System.setProperties(properties1);
        }
        if("GET".equals(s.toUpperCase()))
            return doGet(s1, hashtable1, s4, httpservletresponse);
        else
            return doPost(s1, hashtable, hashtable1, s4, servletinputstream, httpservletresponse);
    }

    private String doPost(String s, Hashtable hashtable, Hashtable hashtable1, String s1, ServletInputStream servletinputstream, HttpServletResponse httpservletresponse)
    {
        Object obj = null;
        URLConnection urlconnection = null;
        Object obj1 = null;
        BufferedOutputStream bufferedoutputstream = null;
        String s2 = "";
        if(hashtable != null)
        {
            for(Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements();)
            {
                if(s2.length() > 0)
                    s2 = (new StringBuilder()).append(s2).append("&").toString();
                String s3 = (String)enumeration.nextElement();
                s2 = (new StringBuilder()).append(s2).append(s3).append("=").toString();
                s2 = (new StringBuilder()).append(s2).append(URLEncoder.encode((String)hashtable.get(s3))).toString();
            }

        }
        try
        {
            URL url = new URL(s);
            urlconnection = url.openConnection();
            urlconnection.setDoOutput(true);
            urlconnection.setDoInput(true);
            urlconnection.setUseCaches(false);
            urlconnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            if(hashtable1 != null)
            {
                String s4;
                for(Enumeration enumeration1 = hashtable1.keys(); enumeration1.hasMoreElements(); urlconnection.setRequestProperty(s4, (String)hashtable1.get(s4)))
                    s4 = (String)enumeration1.nextElement();

            }
            PrintWriter printwriter = new PrintWriter(urlconnection.getOutputStream());
            printwriter.print(s2);
            int j;
            while((j = servletinputstream.read()) != -1) 
                printwriter.write(j);
            printwriter.close();
        }
        catch(Exception exception)
        {
            return getMessage(s, exception);
        }
        try
        {
            String s5 = urlconnection.getContentType();
            if(s5 != null && s1 != null)
                s5 = rewriteEncoding(s5, s1);
            if(s5 != null)
                httpservletresponse.setContentType(s5);
            String s6 = urlconnection.getContentEncoding();
            if(s6 == null)
                s6 = "";
            try
            {
                if(s6.indexOf("gzip") >= 0)
                    obj1 = new GZIPInputStream(urlconnection.getInputStream());
                else
                    obj1 = new BufferedInputStream(urlconnection.getInputStream());
                bufferedoutputstream = new BufferedOutputStream(httpservletresponse.getOutputStream());
                int i;
                while((i = ((InputStream) (obj1)).read()) >= 0) 
                    bufferedoutputstream.write(i);
            }
            catch(Exception exception2)
            {
                return getMessage(s, exception2);
            }
        }
        catch(Exception exception1)
        {
            return getMessage(s, exception1);
        }
        if(obj1 != null){
            try {
				((InputStream) (obj1)).close();
			} catch (IOException e) {

				e.printStackTrace();
			}
        }
        if(bufferedoutputstream != null)
        {
            try {
				bufferedoutputstream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			try {
				bufferedoutputstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return null;
    }

    private String doGet(String s, Hashtable hashtable, String s1, HttpServletResponse httpservletresponse)
    {
        Object obj = null;
        HttpURLConnection httpurlconnection = null;
        Object obj1 = null;
        BufferedOutputStream bufferedoutputstream = null;
        String s2 = "";
        try
        {
            URL url = new URL(s);
            httpurlconnection = (HttpURLConnection)url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setRequestMethod("GET");
            if(hashtable != null)
            {
                String s3;
                for(Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements(); httpurlconnection.setRequestProperty(s3, (String)hashtable.get(s3)))
                    s3 = (String)enumeration.nextElement();

            }
            httpurlconnection.connect();
            int j = httpurlconnection.getResponseCode();
        }
        catch(Exception exception)
        {
        	System.out.println(exception);
            return getMessage(s, exception);
        }
        try
        {
            String s4 = httpurlconnection.getContentType();
            if(s4 != null && s1 != null)
                s4 = rewriteEncoding(s4, s1);
            if(s4 != null)
                httpservletresponse.setContentType(s4);
            else
                s4 = "";
            String s5 = httpurlconnection.getContentEncoding();
            if(s5 == null)
                s5 = "";
            try
            {
                if(s5.indexOf("gzip") >= 0)
                    obj1 = new GZIPInputStream(httpurlconnection.getInputStream());
                else
                    obj1 = new BufferedInputStream(httpurlconnection.getInputStream());
                bufferedoutputstream = new BufferedOutputStream(httpservletresponse.getOutputStream());
                int i;
                while((i = ((InputStream) (obj1)).read()) >= 0) 
                    bufferedoutputstream.write(i);
            }
            catch(Exception exception2)
            {
            	System.out.println(exception2);
                return getMessage(s, exception2);
            }
        }
        catch(Exception exception1)
        {
        	System.out.println(exception1);
            return getMessage(s, exception1);
        }
        if(obj1 != null)
			try {
				((InputStream) (obj1)).close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        if(bufferedoutputstream != null)
        {
            try {
				bufferedoutputstream.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				bufferedoutputstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return null;
    }

    protected String getMessage(String s, Exception exception)
    {
        String s1 = exception.getClass().getName();
        int i = s1.lastIndexOf('.');
        s1 = s1.substring(i + 1);
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        exception.printStackTrace(printwriter);
        return (new StringBuilder()).append("Request: ").append(s).append("\nException: ").append(s1).append(": ").append(exception.getMessage()).append("\n").append(stringwriter.getBuffer().toString()).toString();
    }

    private InputStreamReader getInputStreamReader(InputStream inputstream, String s)
        throws UnsupportedEncodingException
    {
        if(s == null)
            return new InputStreamReader(inputstream);
        else
            return new InputStreamReader(inputstream, s);
    }

    private String rewriteEncoding(String s, String s1)
    {
        if(s.indexOf("charset") < 0)
            return (new StringBuilder()).append(s).append(";charset=").append(s1).toString();
        int i = s.indexOf(";");
        if(i < 0)
            i = s.indexOf("charset");
        return (new StringBuilder()).append(s.substring(0, i)).append(";charset=").append(s1).toString();
    }

    private String NEWLINE;
}
