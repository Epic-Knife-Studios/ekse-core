package com.epicknife.modules.builtin.tcpserver;

import com.epicknife.server.Server;
import com.epicknife.server.event.EventManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Connection extends Thread
{

    public final String userID;
    private ConnectionStatus status;
    
    private Socket socket;
    public PacketManager sph;
    
    public Connection(Socket sock, String id)
    {
        this.status = ConnectionStatus.IDLE;
        this.userID = id;
        try
        {
            this.sph = new PacketManager(this.socket.getInputStream(), this.socket.getOutputStream());
        }
        catch(IOException e)
        {
            e.printStackTrace();
            this.status = ConnectionStatus.ERROR;
        }
    }
    
    public boolean killme()
    {
        return (this.status == ConnectionStatus.KILLME);
    }
    
    public void doHandling()
    {
        while(this.status == ConnectionStatus.CONNECTED) //Only run if the socket is connected
        {
            try
            {
                Packet packet = this.sph.read();
                if(packet != null) //Execute the packets that the input stream sends us
                {
                    EventManager.raiseEvent(new PacketEvent(packet, userID));
                }
                else
                {
                    continue;
                }
                
            }
            catch(Exception e)
            {
                Server.log.logWarning("ConnectionThread", "Error in connection: bad packet!");
                this.status = ConnectionStatus.KILLME;
            }
        }
    }
    
    @Override
    public void run()
    {
        while(this.status != ConnectionStatus.KILLME)
        {
            doHandling();
        }
        try
        {
            this.sph.close();
            this.sph = null;
            this.socket.close();
            this.socket = null;
        }
        catch(Exception e)
        {
            this.sph = null;
            this.socket = null;
            this.status = ConnectionStatus.KILLME;
        }
    }
    
    public void sendPacket(Packet packet) throws Exception
    {
        this.sph.write(packet);
    }
    
    public OutputStream getOutputStream() throws IOException
    {
        return this.socket.getOutputStream();
    }
    
    public InputStream getInputStream() throws IOException
    {
        return this.getInputStream();
    }
    
    public ConnectionStatus isConnected()
    {
        return this.status;
    }
    
    public String getID()
    {
        return this.userID;
    }
    
    public void close()
    {
        this.status = ConnectionStatus.DISCONNECTED;
    }
    
    private enum ConnectionStatus
    {
        IDLE,
        CONNECTED,
        CONNECTING,
        CLOSING,
        DISCONNECTED,
        ERROR,
        KILLME
    }

}
