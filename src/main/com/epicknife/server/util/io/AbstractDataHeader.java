package com.epicknife.server.util.io;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public abstract class AbstractDataHeader<D> implements IDataHeader<D>
{

    private byte major, minor;
    protected D data;

    public AbstractDataHeader(byte major, byte minor)
    {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public final byte getMajor()
    {
        return this.major;
    }

    @Override
    public final byte getMinor()
    {
        return this.minor;
    }

    @Override
    public D getData()
    {
        return this.data;
    }

    @Override
    public void setData(D data)
    {
        this.data = data;
    }
}