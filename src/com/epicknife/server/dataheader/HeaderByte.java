package com.epicknife.server.dataheader;

import java.io.IOException;

import com.epicknife.server.util.io.AbstractDataHeader;
import com.epicknife.server.util.io.HeaderInputStream;
import com.epicknife.server.util.io.HeaderOutputStream;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class HeaderByte extends AbstractDataHeader<Byte>
{

    public HeaderByte()
    {
        super((byte)-126, (byte)-127);
    }

    @Override
    public void read(HeaderInputStream in) throws IOException
    {
        this.data = in.readByte();
    }

    @Override
    public void write(HeaderOutputStream out) throws IOException
    {
        out.writeByte(this.data);
    }

}
