package com.epicknife.server.dataheader;

import java.io.IOException;

import com.epicknife.server.util.io.AbstractDataHeader;
import com.epicknife.server.util.io.HeaderInputStream;
import com.epicknife.server.util.io.HeaderOutputStream;
import com.epicknife.server.util.Pair;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
@SuppressWarnings("rawtypes")
public class HeaderPair extends AbstractDataHeader<Pair>
{

    public HeaderPair()
    {
        super((byte)-127, (byte)-126);
    }

    @Override
    public void read(HeaderInputStream in) throws IOException
    {

    }

    @Override
    public void write(HeaderOutputStream out) throws IOException
    {
        // TODO Auto-generated method stub

    }

}
