package com.epicknife.server.dataheader;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class DataHeader
{
    public static int readInt(InputStream in)
    {
        try
        {
            byte[] b = new byte[4];
            int len = in.read(b);
            if(len < 4)
            {
                return 0;
            }
            int toReturn = 0;
            toReturn += b[0] << 0;
            toReturn += b[1] << 8;
            toReturn += b[2] << 16;
            toReturn += b[3] << 24;
            return toReturn;
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    
    public static void writeInt(OutputStream out, int i)
    {
        try
        {
            byte[] b = new byte[4];
            b[0] = (byte)(i >> 0);
            b[1] = (byte)(i >> 8);
            b[2] = (byte)(i >> 16);
            b[3] = (byte)(i >> 24);
            out.write(b);
        }
        catch(Exception e)
        {
            // Do nothing!
        }
    }
    
    public static void writeString(OutputStream out, String str)
    {
        try
        {
            if(str == null)
            {
                // Do nothing.
                return;
            }
            
            byte[] b = str.getBytes();
            out.write(b);
        }
        catch(Exception e)
        {
            // Do nothing!
        }
    }
    
    public static String readString(InputStream in, int len)
    {
        try
        {
            byte[] b = new byte[len];
            int read = in.read(b);
            
            if(read < len)
            {
                return null;
            }
            
            return new String(b);
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public String readString(InputStream in)
    {
        return readString(in, readInt(in));
    }
}