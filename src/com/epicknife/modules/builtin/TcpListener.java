package com.epicknife.modules.builtin;

import com.epicknife.modules.builtin.tcpserver.ListenerThread;
import com.epicknife.server.Server;
import com.epicknife.server.etc.Config;
import com.epicknife.server.plugin.JavaPlugin;
import java.util.HashMap;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class TcpListener extends JavaPlugin
{

    public static Config svars;
    
    public TcpListener()
    {
        HashMap<String, String> defaultCfg = new HashMap<>();
        defaultCfg.put("port", "55769");
        svars = new Config(defaultCfg);
    }
    
    @Override
    public void onStart()
    {
        svars.readCfg("tcplistener");
        Server.addReceiver(new ListenerThread(
        		Integer.parseInt(svars.getVar("port"))
		));
    }
    
    @Override
    public String getFancyName()
    {
        return "TCP Listening server";
    }
    
    @Override
    public String getInternalName()
    {
        return "tcplistener";
    }
    
    @Override
    public int getVersion()
    {
        return 0;
    }

}