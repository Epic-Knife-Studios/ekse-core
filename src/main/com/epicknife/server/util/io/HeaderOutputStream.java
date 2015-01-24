package com.epicknife.server.util.io;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class HeaderOutputStream extends BufferedOutputStream implements DataOutput
{

    public HeaderOutputStream(OutputStream out)
    {
        super(out);
    }

    public HeaderOutputStream(OutputStream out, boolean compressed) throws IOException
    {
        super((compressed ? new GZIPOutputStream(out) : out));
    }

    public HeaderOutputStream(OutputStream out, int buffersize)
    {
        super(out, buffersize);
    }

    public HeaderOutputStream(OutputStream out, int buffersize, boolean compressed) throws IOException
    {
        super((compressed ? new GZIPOutputStream(out) : out), buffersize);
    }

    public synchronized void writeHeader(IDataHeader<?> header) throws IOException
    {
        this.writeByte(header.getMajor());
        this.writeByte(header.getMinor());
        header.write(this);
    }

    @Override
    public synchronized void writeBoolean(boolean data) throws IOException
    {
        this.out.write((data ? 1 : 0));
    }

    @Override
    public synchronized void writeByte(int data) throws IOException
    {
        this.out.write(data);
    }

    @Override
    public synchronized void writeBytes(String data) throws IOException
    {
        int len = data.length();
        for(int i = 0; i < len; i++)
        {
            this.out.write((byte)data.charAt(i));
        }
    }

    @Override
    public synchronized void writeChar(int data) throws IOException
    {
        this.writeShort(data);

    }

    @Override
    public synchronized void writeChars(String data) throws IOException
    {
        int len = data.length();
        for(int i = 0; i < len; i++)
        {
            int v = data.charAt(i);
            this.out.write((v >>> 8) & 0xFF);
            this.out.write((v >>> 0) & 0xFF);
        }
    }

    @Override
    public synchronized void writeDouble(double data) throws IOException
    {
        this.writeLong(Double.doubleToLongBits(data));
    }

    @Override
    public synchronized void writeFloat(float data) throws IOException
    {
        this.writeInt(Float.floatToIntBits(data));
    }

    @Override
    public synchronized void writeInt(int data) throws IOException
    {
        this.out.write((data >>> 24) & 0xFF);
        this.out.write((data >>> 16) & 0xFF);
        this.out.write((data >>> 8) & 0xFF);
        this.out.write((data >>> 0) & 0xFF);
    }

    @Override
    public synchronized void writeLong(long data) throws IOException
    {
        byte writeBuffer[] = new byte[8];
        writeBuffer[0] = (byte)(data >>> 56);
        writeBuffer[1] = (byte)(data >>> 48);
        writeBuffer[2] = (byte)(data >>> 40);
        writeBuffer[3] = (byte)(data >>> 32);
        writeBuffer[4] = (byte)(data >>> 24);
        writeBuffer[5] = (byte)(data >>> 16);
        writeBuffer[6] = (byte)(data >>> 8);
        writeBuffer[7] = (byte)(data >>> 0);
        this.out.write(writeBuffer, 0, 8);

    }

    @Override
    public synchronized void writeShort(int data) throws IOException
    {
        this.out.write((data >>> 8) & 0xFF);
        this.out.write((data >>> 0) & 0xFF);
    }

    @Override
    public synchronized void writeUTF(String data) throws IOException
    {
        byte[] temp = data.getBytes(Charset.forName("UTF-8"));
        this.writeInt(temp.length); // Write the length of the UTF-8 string to the stream
        this.out.write(temp, 0, temp.length);
    }

    @Override
    public synchronized void flush() throws IOException
    {
        this.out.flush();
    }

}
