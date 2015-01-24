package com.epicknife.modules.builtin.tcpserver;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class PacketManager
{

    InputStream in;
    OutputStream out;

    public PacketManager(InputStream in, OutputStream out)
    {
        this.in = in;
        this.out = out;
    }

    public Packet read()
    {
        try
        {
            byte[] b = new byte[2];
            int len = in.read(b);
            if(len < 2)
            {
                throw new Exception("Reached EOS while reading packet!");
            }
            Packet p = new Packet(b[0], b[1]);
            p.handler.readMeta(in);
            return p;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public void write(Packet pack)
    {
        try
        {
            out.write(pack.getBytes());
            pack.handler.writeMeta(out);
        }
        catch(Exception e)
        {

        }
    }
    
    public void close()
    {
        try
        {
            this.in.close();
            this.out.close();
        }
        catch(Exception e)
        {
            this.in = null;
            this.out = null;
        }
    }

}