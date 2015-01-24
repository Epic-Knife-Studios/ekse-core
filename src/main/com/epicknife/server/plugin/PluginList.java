package com.epicknife.server.plugin;

import java.util.ArrayList;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class PluginList
{

    public ArrayList<JavaPlugin> plugins;
    
    public void addPlugin(JavaPlugin plgn)
    {
        if(plugins == null)
        {
            plugins = new ArrayList<JavaPlugin>();
        }
        
        plugins.add(plgn);
    }
    
    public void startPlugins()
    {
        if(plugins == null)
        {
            plugins = new ArrayList<JavaPlugin>();
        }
        else
        {
            for(int i = 0; i < plugins.size(); i++)
            {
                plugins.get(i).onStart();
            }
        }
    }
    
    public void stopPlugins()
    {
        if(plugins == null)
        {
            plugins = new ArrayList<JavaPlugin>();
        }
        else
        {
            for(int i = 0; i < plugins.size(); i++)
            {
                plugins.get(i).onStop();
            }
        }
    }

}