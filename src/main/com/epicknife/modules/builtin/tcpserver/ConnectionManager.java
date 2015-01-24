package com.epicknife.modules.builtin.tcpserver;

import com.epicknife.server.Server;
import java.util.ArrayList;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class ConnectionManager extends Thread
{

    private static final ArrayList<Connection> connections = new ArrayList<>();
    
    private static long index = 1L;
    
    @Override
    public void run()
    {
        Connection c = null;
        
        while(true)
        {
            for(int i = 0; i < connections.size(); i++)
            {
                c = connections.get(i);
                if(c.killme())
                {
                    connections.remove(i);
                    i--;
                }
            }
        }
        
    }
    
    public static boolean hasFreeConnections()
    {
        int csize = connections.size();
        int max = Integer.parseInt(Server.svars.getVar("max players"));
        return (csize <= max);
    }
    
    public static String getNewId()
    {
        index++;
        return "" + index;
    }
    
    public static void add(Connection c)
    {
        if(hasFreeConnections())
        {
            connections.add(c);
        }
    }
    
    public static Connection get(String uid)
    {
        for(int i = 0; i < connections.size(); i++)
        {
            Connection c = connections.get(i);
            if(c.userID.equals(uid))
            {
                return c;
            }
        }
        return null;
    }
    
    public static ArrayList<Connection> getAll()
    {
        return connections;
    }
    
}
