package com.epicknife.server.util;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Time
{

    public static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(Exception ignore) {}
    }
    
    public static long getExecutionSpeed()
    {
        long a = System.currentTimeMillis();
        long b = System.currentTimeMillis();
        long count = 0;
        while((b-a) < 1000)
        {
            b = System.currentTimeMillis();
            count++;
        }
        return count;
    }

}