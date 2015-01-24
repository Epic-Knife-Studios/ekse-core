package com.epicknife.server.util.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Webreader
{

    public static String readURL(String path) throws Exception
    {
        String r = "";
        URL u = new URL(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
        String line;
        while((line = br.readLine()) != null)
        {
            r = r + line;
        }
        br.close();
        return r;
    }

}