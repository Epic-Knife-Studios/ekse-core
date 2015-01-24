package com.epicknife.modules.builtin.tcpserver;

import com.epicknife.server.event.events.ServerEvent;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class PacketEvent extends ServerEvent
{

    private final Packet packet;
    private final String id;
    
    public PacketEvent(Packet p, String id)
    {
        this.packet = p;
        this.id = id;
    }
    
    public Packet getPacket()
    {
        return this.packet;
    }
    
    public String getId()
    {
        return this.id;
    }

}