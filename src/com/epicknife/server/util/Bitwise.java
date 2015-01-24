package com.epicknife.server.util;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Bitwise
{
    
    public static byte[] serializeInt(int i)
    {
        byte[] b = new byte[4];
        b[0] = (byte)(i >>> 24);
        b[1] = (byte)(i >>> 16);
        b[2] = (byte)(i >>> 8);
        b[3] = (byte)(i >>> 0);
        return b;
    }
    
    public static int deserializeInt(byte[] b)
    {
        int i = 0;
        i = b[0] << 24;
        i = b[1] << 16;
        i = b[2] << 8;
        i = b[3] << 0;
        return i;
    }
    
    public static boolean isNumeric(char c)
    {
        return (c == '0' || c == '1' || c == '2' || 
                c == '3' || c == '4' || c == '5' ||
                c == '6' || c == '7' || c == '8' ||
                c == '9' || c == '+' || c == '-' ||
                c == '.');
    }
    
    public static boolean isAlphaNumeric(char c)
    {
        return (c == 'a' || c == 'b' || c == 'c' ||
                c == 'd' || c == 'e' || c == 'f' ||
                c == 'g' || c == 'h' || c == 'i' ||
                c == 'j' || c == 'k' || c == 'l' ||
                c == 'm' || c == 'n' || c == 'o' ||
                c == 'p' || c == 'q' || c == 'r' ||
                c == 's' || c == 't' || c == 'u' ||
                c == 'v' || c == 'w' || c == 'x' ||
                c == 'y' || c == 'z' || c == '0' ||
                c == '1' || c == '2' || c == '3' ||
                c == '4' || c == '5' || c == '6' ||
                c == '7' || c == '8' || c == '9');
    }
    
    public static boolean isAlphaNumeric(String str)
    {
        char[] chars = str.toLowerCase().toCharArray();
        for(char c : chars)
        {
            if(!isAlphaNumeric(c)) { return false; }
        }
        return true;
    }

}
