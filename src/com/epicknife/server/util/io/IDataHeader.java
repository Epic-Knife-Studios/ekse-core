package com.epicknife.server.util.io;

import java.io.IOException;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
interface IDataHeader<D>
{

    public byte getMajor();

    public byte getMinor();

    public D getData();

    public void setData(D data);

    public void read(HeaderInputStream in) throws IOException;

    public void write(HeaderOutputStream out) throws IOException;

}
