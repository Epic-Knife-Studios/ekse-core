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
public class HeaderFloat extends AbstractDataHeader<Float>
{

    public HeaderFloat()
    {
        super((byte)-126, (byte)-123);
    }

    @Override
    public void read(HeaderInputStream in) throws IOException
    {
        this.data = in.readFloat();
    }

    @Override
    public void write(HeaderOutputStream out) throws IOException
    {
        out.writeFloat(this.data);
    }

}
