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
public class HeaderDouble extends AbstractDataHeader<Double>
{

    public HeaderDouble()
    {
        super((byte)-126, (byte)-122);
    }

    @Override
    public void read(HeaderInputStream in) throws IOException
    {
        this.data = in.readDouble();
    }

    @Override
    public void write(HeaderOutputStream out) throws IOException
    {
        out.writeDouble(this.data);
    }

}
