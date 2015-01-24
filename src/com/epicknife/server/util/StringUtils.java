package com.epicknife.server.util;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class StringUtils
{

    public static boolean emptyOrNull(String s)
    {
        return (s == null || s.equals(""));
    }
    
    public static String[] combineStringArray(String[] a, String[] b)
    {
        String[] c = new String[a.length + b.length];
        for(int i = 0; i < a.length; i++)
        {
            c[i] = a[i];
        }
        for(int i = 0; i < b.length; i++)
        {
            c[a.length + i] = b[i];
        }
        return c;
    }

}