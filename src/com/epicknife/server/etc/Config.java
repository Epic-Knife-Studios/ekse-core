package com.epicknife.server.etc;

import com.epicknife.server.util.io.FileIO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Config
{
    private final HashMap<String, String> required;
    private HashMap<String, String> vars;
    
    public Config()
    {
        this.required = new HashMap<>();
        this.vars = new HashMap<>();
    }
    
    public Config(HashMap<String, String> required)
    {
        this.required = required;
        this.vars = new HashMap<>();
        
        ensureDefaults();
    }
    
    public String getVar(String name)
    {
        ensureDefaults();
        if(this.vars.containsKey(name))
        {
            return this.vars.get(name);
        }
        else
        {
            return null;
        }
    }
    
    public void delVar(String key)
    {
        ensureDefaults();
        if(this.vars.containsKey(key))
        {
            this.vars.remove(key);
        }
        ensureDefaults();
    }
    
    public boolean containsVar(String key)
    {
        ensureDefaults();
        return this.vars.containsKey(key);
    }
    
    public Config setVar(String key, String value)
    {
        ensureDefaults();
        this.vars.put(key, value);
        return this;
    }
    
    public HashMap<String, String> getVars()
    {
        ensureDefaults();
        return this.vars;
    }
    
    public final void ensureDefaults()
    {
        Set<String> keys = this.required.keySet();
        for(String key : keys)
        {
            if(!this.vars.containsKey(key))
            {
                this.vars.put(key, this.required.get(key));
            }
        }
    }
    
    public void readCfg(String name)
    {
        if(FileIO.fileExistsBasic("conf/" + name + ".json"))
        {
            ensureDefaults();
            Gson gson = new Gson();
            this.vars = gson.fromJson(
                    FileIO.readAllText("conf/" + name + ".json"),
                    HashMap.class
            );
            ensureDefaults();
        }
        else
        {
            FileIO.createFileRecursive("conf/" + name + ".json");
            this.writeCfg(name);
        }
    }
    
    public void writeCfg(String name)
    {
        ensureDefaults();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try
        {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("conf/" + name + ".json")
            );
            bw.write(gson.toJson(vars));
            bw.flush();
            bw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}