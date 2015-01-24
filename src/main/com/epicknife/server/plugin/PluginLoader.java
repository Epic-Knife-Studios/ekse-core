package com.epicknife.server.plugin;

import com.epicknife.server.util.io.FileIO;
import com.google.gson.Gson;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class PluginLoader
{

    public static PluginList loadAllPlugins(String dir)
    {
        if(!FileIO.fileExistsBasic(dir))
        {
            FileIO.mkdirRecursive(dir);
            return new PluginList();
        }
        if(!FileIO.fileExistsBasic(dir + "/plugins.json"))
        {
            return new PluginList();
        }
        
        PluginList list = new PluginList();
        
        String[] ofiles = FileIO.listFilesRecursive(dir);
        ArrayList<URL> furls = new ArrayList<URL>();
        ArrayList<String> files = new ArrayList<String>();
        for(int i = 0; i < ofiles.length; i++)
        {
            if(ofiles[i].endsWith(".jar") || ofiles[i].endsWith(".zip"))
            {
                files.add(ofiles[i]);
            }
        }
        
        for(int i = 0; i < files.size(); i++)
        {
            try {
                furls.add(new File(files.get(i)).toURI().toURL());
            } catch (Exception ex) {
                
            }
        }
        
        URL[] urls = new URL[furls.size()];
        for(int i = 0; i < furls.size(); i++)
        {
            urls[i] = furls.get(i);
        }
        
        ClassLoader loader = URLClassLoader.newInstance(urls);
        Gson gson = new Gson();
        PluginInfo info = new PluginInfo();
        info = gson.fromJson(FileIO.readAllText(dir+"/plugins.json"), PluginInfo.class);
        ArrayList<JavaPlugin> plugins = new ArrayList<JavaPlugin>();
        int amt = info.plugins.size();
        for(int i = 0; i < info.plugins.size(); i++)
        {
            try
            {
                if(info.plugins.get(i) != null)
                {
                    Class<?> clazz = Class.forName(info.plugins.get(i), true, loader);
                    Class<? extends JavaPlugin> runClass = clazz.asSubclass(JavaPlugin.class);
                    Constructor<? extends JavaPlugin> ctor = runClass.getConstructor();
                    plugins.add(ctor.newInstance());
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                amt--;
                i--;
                continue;
            }
        }
        for(int i = 0; i < plugins.size(); i++)
        {
            list.addPlugin(plugins.get(i));
        }
        
        return list;
    }

}