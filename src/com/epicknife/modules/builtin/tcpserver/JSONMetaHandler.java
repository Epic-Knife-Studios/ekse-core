package com.epicknife.modules.builtin.tcpserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class JSONMetaHandler extends MetaHandler
{
    
    private HashMap<String, String> meta;
    private final Gson gson;
    
    public JSONMetaHandler()
    {
        this.meta = new HashMap<>();
        gson = new Gson();
    }
    
    public String getString(String name)
    {
        String str = null;
        
        if(this.meta != null)
        {
            str = this.meta.get(name);
        }
        
        return str;
    }
    
    public boolean contains(String name)
    {
        if(this.meta != null)
        {
            return this.meta.containsKey(name);
        }
        
        return false;
    }
    
    public void setString(String key, String value)
    {
        if(this.meta != null)
        {
            this.meta.put(key, value);
        }
    }
    
    public void setMeta(HashMap<String, String> meta)
    {
        this.meta = meta;
    }
    
    public HashMap<String, String> getMeta()
    {
        return this.meta;
    }
    
    @Override
    public void readMeta(InputStream in)
    {
        byte[] b = new byte[4];
        try
        {
            int len = in.read(b);
            if(len < 4)
            {
                this.meta = new HashMap();
            }
            int i = 0;
            i += b[0] << 0;
            i += b[1] << 8;
            i += b[2] << 16;
            i += b[3] << 24;
            b = new byte[i];
            len = in.read(b);
            if(len < i)
            {
                this.meta = new HashMap();
            }
            this.meta = gson.fromJson(new String(b), HashMap.class);
        }
        catch(IOException | JsonSyntaxException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void writeMeta(OutputStream out)
    {
        String str = "{}"; 
        
        if(this.meta != null)
        {
            str = gson.toJson(this.meta);
        }
        
        byte[] b = str.getBytes();
        
        int len = b.length;
        
        try
        {
            byte[] i = new byte[4];
            i[0] = (byte)(len>>0);
            i[1] = (byte)(len>>8);
            i[2] = (byte)(len>>16);
            i[3] = (byte)(len>>24);
            out.write(i);
            out.write(b);
        }
        catch(Exception e)
        {
            
        }
    }
    
}
