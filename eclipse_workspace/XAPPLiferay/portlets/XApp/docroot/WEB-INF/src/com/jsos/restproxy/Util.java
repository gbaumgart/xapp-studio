// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.jsos.restproxy;


public class Util
{

    public Util()
    {
    }

    public static String getFromQuery(String s, String s1)
    {
        if(s1 == null || s == null)
            return "";
        int i = s.indexOf(s1);
        if(i < 0)
            return "";
        if(i + s1.length() >= s.length())
            return "";
        String s2 = s.substring(i + s1.length());
        i = s2.indexOf("&");
        if(i > 0)
            s2 = s2.substring(0, i);
        return s2;
    }

    public static String decodeURIComponent(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int j = 0;
        int k = 0;
        int l = -1;
        for(; k < s.length(); k++)
        {
            char c = s.charAt(k);
            int i;
            switch(c)
            {
            case 37: // '%'
                c = s.charAt(++k);
                int i1 = (Character.isDigit(c) ? c - 48 : (10 + Character.toLowerCase(c)) - 97) & 0xf;
                c = s.charAt(++k);
                int j1 = (Character.isDigit(c) ? c - 48 : (10 + Character.toLowerCase(c)) - 97) & 0xf;
                i = i1 << 4 | j1;
                break;

            case 43: // '+'
                i = 32;
                break;

            default:
                i = c;
                break;
            }
            if((i & 0xc0) == 128)
            {
                j = j << 6 | i & 0x3f;
                if(--l == 0)
                    stringbuffer.append((char)j);
                continue;
            }
            if((i & 0x80) == 0)
            {
                stringbuffer.append((char)i);
                continue;
            }
            if((i & 0xe0) == 192)
            {
                j = i & 0x1f;
                l = 1;
                continue;
            }
            if((i & 0xf0) == 224)
            {
                j = i & 0xf;
                l = 2;
                continue;
            }
            if((i & 0xf8) == 240)
            {
                j = i & 7;
                l = 3;
                continue;
            }
            if((i & 0xfc) == 248)
            {
                j = i & 3;
                l = 4;
            } else
            {
                j = i & 1;
                l = 5;
            }
        }

        return stringbuffer.toString();
    }
}
