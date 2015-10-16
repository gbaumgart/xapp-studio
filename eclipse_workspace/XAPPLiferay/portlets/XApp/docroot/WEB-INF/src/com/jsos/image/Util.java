package com.jsos.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Date;

public class Util
{
  private static final char fillchar = '=';
  private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

  public static String getName(String paramString)
  {
    MessageDigest localMessageDigest;
    try
    {
      localMessageDigest = MessageDigest.getInstance("MD5");
    }
    catch (Exception localException)
    {
      return paramString;
    }
    localMessageDigest.update(paramString.getBytes());
    byte[] arrayOfByte = localMessageDigest.digest();
    String str = encode(arrayOfByte, arrayOfByte.length);
    return str.replaceAll("/", "+");
  }

  public static String getContent(File paramFile)
  {
    byte[] arrayOfByte = new byte[256];
    String str = null;
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      int i;
      if ((i = localFileInputStream.read(arrayOfByte)) > 0)
        str = new String(arrayOfByte, 0, i);
      localFileInputStream.close();
    }
    catch (Exception localException)
    {
      str = null;
    }
    return str;
  }

  public static int dumpFile(File paramFile, OutputStream paramOutputStream)
  {
    byte[] arrayOfByte = new byte[4096];
    int j = 1;
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      int i;
      while ((i = localFileInputStream.read(arrayOfByte)) != -1)
        paramOutputStream.write(arrayOfByte, 0, i);
      localFileInputStream.close();
    }
    catch (Exception localException)
    {
      j = 0;
    }
    return j;
  }

  public static boolean fileExists(File paramFile, long paramLong)
  {
    if (!paramFile.isFile())
      return false;
    if (!paramFile.canRead())
      return false;
    if (paramLong < 0L)
      return true;
    long l1 = new Date().getTime();
    long l2 = paramFile.lastModified() + paramLong * 1000L;
    return l2 > l1;
  }

  public static String getUrlFromQuery(String paramString)
  {
    String str1 = "url=";
    if ((str1 == null) || (paramString == null))
      return "";
    int i = paramString.indexOf(str1);
    if (i < 0)
      return "";
    if (i + str1.length() >= paramString.length())
      return "";
    String str2 = paramString.substring(i + str1.length());
    i = str2.indexOf("&ttl=");
    if (i > 0)
      str2 = str2.substring(0, i);
    return str2;
  }

  public static String getTtlFromQuery(String paramString)
  {
    String str1 = "ttl=";
    if ((str1 == null) || (paramString == null))
      return "";
    int i = paramString.indexOf(str1);
    if (i < 0)
      return "";
    if (i + str1.length() >= paramString.length())
      return "";
    String str2 = paramString.substring(i + str1.length());
    i = str2.indexOf("&url=");
    if (i > 0)
      str2 = str2.substring(0, i);
    return str2;
  }

  public static String decodeURIComponent(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int k = 0;
    int m = 0;
    int n = -1;
    while (m < paramString.length())
    {
      int i = paramString.charAt(m);
      int j;
      switch (i)
      {
      case 37:
        m++;
        i = paramString.charAt(m);
        int i1 = (Character.isDigit(i) ? i - 48 : '\n' + Character.toLowerCase(i) - 97) & 0xF;
        m++;
        i = paramString.charAt(m);
        int i2 = (Character.isDigit(i) ? i - 48 : '\n' + Character.toLowerCase(i) - 97) & 0xF;
        j = i1 << 4 | i2;
        break;
      case 43:
        j = 32;
        break;
      default:
        j = i;
      }
      if ((j & 0xC0) == 128)
      {
        k = k << 6 | j & 0x3F;
        n--;
        if (n == 0)
          localStringBuffer.append((char)k);
      }
      else if ((j & 0x80) == 0)
      {
        localStringBuffer.append((char)j);
      }
      else if ((j & 0xE0) == 192)
      {
        k = j & 0x1F;
        n = 1;
      }
      else if ((j & 0xF0) == 224)
      {
        k = j & 0xF;
        n = 2;
      }
      else if ((j & 0xF8) == 240)
      {
        k = j & 0x7;
        n = 3;
      }
      else if ((j & 0xFC) == 248)
      {
        k = j & 0x3;
        n = 4;
      }
      else
      {
        k = j & 0x1;
        n = 5;
      }
      m++;
    }
    return localStringBuffer.toString();
  }

  private static String toBinaryString(byte paramByte)
  {
    int i = paramByte;
    String str = Integer.toBinaryString(i);
    if (i < 0);
    for (str = str.substring(str.length() - 8); str.length() < 8; str = "0" + str);
    return str;
  }

  private static String encode(byte[] paramArrayOfByte, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramInt; i += 3)
    {
      String str;
      if (i + 2 < paramInt)
      {
        str = toBinaryString(paramArrayOfByte[i]) + toBinaryString(paramArrayOfByte[(i + 1)]) + toBinaryString(paramArrayOfByte[(i + 2)]);
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(0, 6), 2)));
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(6, 12), 2)));
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(12, 18), 2)));
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(18, 24), 2)));
      }
      else if (i + 1 < paramInt)
      {
        str = toBinaryString(paramArrayOfByte[i]) + toBinaryString(paramArrayOfByte[(i + 1)]) + "00";
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(0, 6), 2)));
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(6, 12), 2)));
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(12, 18), 2)));
        localStringBuffer.append('=');
      }
      else
      {
        str = toBinaryString(paramArrayOfByte[i]) + "0000";
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(0, 6), 2)));
        localStringBuffer.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(Integer.parseInt("00" + str.substring(6, 12), 2)));
        localStringBuffer.append('=');
        localStringBuffer.append('=');
      }
    }
    return localStringBuffer.toString();
  }
}

/* Location:           /ProjectsMain/nativeIPhone/IbizaTravel/libs/imagescalePackage.jar
 * Qualified Name:     com.jsos.image.Util
 * JD-Core Version:    0.6.0
 */