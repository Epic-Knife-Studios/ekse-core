package com.epicknife.modules.builtin.tcpserver;

import com.epicknife.server.Server;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import com.epicknife.server.network.ConnectionReceiver;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class ListenerThread extends ConnectionReceiver
{

    private ServerSocket socket;
    private int port;

    public ListenerThread(int port)
    {
        super("ListerThread");
        this.port = port;
    }
    
    @Override
    public void onStart()
    {
        Server.log.logInfo("TcpListener", "Creating new Tcp Listener on port " + this.port + "...");
        try
        {
            socket = new ServerSocket(this.port);
        }
        catch(NumberFormatException | IOException e)
        {
            Server.log.logFatal("TcpListener",
                    "Could not create server socket!  Exiting!");
            e.printStackTrace();
            return;
        }
        Server.log.logInfo("TcpListener", "Success!  Accepting connections!");
        this.start();
    }

    @Override
    public void run()
    {
        while(Server.isRunning())
        {
            try
            {
                Socket s = socket.accept();
                Server.log.logInfo("TcpListener", "Connection: "
                        + s.getInetAddress().getHostName());
                Connection c = new Connection(s, ConnectionManager.getNewId());
                c.start();
                ConnectionManager.add(c);
            }
            catch(Exception e)
            {
                Server.log.logWarning("TcpListener", "Error accepting connection!");
                e.printStackTrace();
            }
        }
        try
        {
            socket.close();
        }
        catch(Exception ignore)
        {}
    }
    
    @Override
    public void onStop()
    {
        try
        {
            socket.close();
            this.stop();
        }
        catch(Exception ignore)
        {}
    }

}