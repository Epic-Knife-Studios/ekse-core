package com.epicknife.modules.builtin.tcpserver;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class Packet
{

    public short id;
    public MetaHandler handler;
    
    public Packet(short id, MetaHandler handler)
    {
        this.id = id;
        this.handler = handler;
    }
    
    public Packet(short id)
    {
        this(id, new MetaHandler());
    }
    
    public Packet(byte a, byte b)
    {
        this((short)((a << 8) + (b << 0)));
    }
    
    public byte[] getBytes()
    {
        byte a = (byte)(id << 8);
        byte b = (byte)(id << 0);
        return new byte[] {a, b};
    }

    public boolean packetEquals(Packet packet)
    {
        return (this.id == packet.id);
    }

}