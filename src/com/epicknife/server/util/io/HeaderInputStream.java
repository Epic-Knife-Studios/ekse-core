package com.epicknife.server.util.io;

import com.epicknife.server.util.Pair;
import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public class HeaderInputStream extends BufferedInputStream implements DataInput
{
    private volatile byte header[];

    public HeaderInputStream(InputStream in)
    {
        super(in);
    }

    public HeaderInputStream(InputStream in, boolean compressed) throws IOException
    {
        super((compressed ? new GZIPInputStream(in) : in));
    }

    public HeaderInputStream(InputStream in, int buffersize)
    {
        super(in, buffersize);
    }

    public HeaderInputStream(InputStream in, int buffersize, boolean compressed) throws IOException
    {
        super((compressed ? new GZIPInputStream(in) : in), buffersize);
    }

    public Pair<Byte, Byte> readHeaderBytes() throws IOException
    {
        this.header = new byte[2];
        int len = this.in.read(this.header);
        if(len < 2) throw new EOFException("Unable to read DataHeader from Stream!");
        return new Pair<>(this.header[0], this.header[1]);
    }

    public void readHeaderData(IDataHeader<?> header) throws IOException
    {
        header.read(this);
    }

    @Override
    public int skipBytes(int length) throws IOException
    {
        int total = 0;
        int current = 0;

        while((total < length) && ((current = (int)this.in.skip(length - total)) > 0))
        {
            total += current;
        }

        return total;
    }

    @Override
    public boolean readBoolean() throws IOException
    {
        int ch = this.in.read();
        if(ch < 0) throw new EOFException("Unable to read Boolean from Stream!");

        return (ch != 0);
    }

    @Override
    public byte readByte() throws IOException
    {
        int ch = this.in.read();
        if(ch < 0) throw new EOFException("Unable to read Byte from Stream!");

        return ((byte)ch);
    }

    @Override
    public int readUnsignedByte() throws IOException
    {
        int ch = this.in.read();
        if(ch < 0) throw new EOFException("Unable to read Byte from Stream!");

        return ch;
    }

    @Override
    public char readChar() throws IOException
    {
        this.header = new byte[2];
        int len = this.in.read(this.header);
        if(len < 2) throw new EOFException("Unable to read Char from Stream!");

        return (char)((this.header[0] << 8) + (this.header[1] << 0));
    }

    @Override
    public double readDouble() throws IOException
    {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public float readFloat() throws IOException
    {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public void readFully(byte[] buffer) throws IOException
    {
        this.readFully(buffer, 0, buffer.length);
    }

    @Override
    public void readFully(byte[] buffer, int offset, int length) throws IOException
    {
        if(length < 0) throw new IndexOutOfBoundsException("Length cannot be less then 0!");
        int i = 0;

        while(i < length)
        {
            int count = this.in.read(buffer, offset + i, length - i);
            if(count < 0) throw new EOFException("Reached EOF while reading " + length + " bytes!");
            i += count;
        }

    }

    @Override
    public int readInt() throws IOException
    {
        this.header = new byte[4];
        int len = this.in.read(this.header);
        if(len < 4) throw new EOFException("Unable to read Int from Stream!");
        return ((this.header[0] << 24) + (this.header[1] << 16) + (this.header[2] << 8) + (this.header[3] << 0));
    }

    @Override
    public long readLong() throws IOException
    {
        this.header = new byte[8];
        this.readFully(this.header);
        return (((long)this.header[0] << 56) + ((long)(this.header[1] & 255) << 48) + ((long)(this.header[2] & 255) << 40)
                + ((long)(this.header[3] & 255) << 32) + ((long)(this.header[4] & 255) << 24) + ((this.header[5] & 255) << 16)
                + ((this.header[6] & 255) << 8) + ((this.header[7] & 255) << 0));
    }

    @Override
    public short readShort() throws IOException
    {
        this.header = new byte[2];
        int len = this.in.read(this.header);
        if(len < 2) throw new EOFException("Unable to read Short from Stream!");

        return (short)((this.header[0] << 8) + (this.header[1] << 0));
    }

    @Override
    public int readUnsignedShort() throws IOException
    {
        this.header = new byte[2];
        int len = this.in.read(this.header);
        if(len < 2) throw new EOFException("Unable to read Short from Stream!");

        return ((this.header[0] << 8) + (this.header[1] << 0));
    }

    @Override
    public String readLine() throws IOException
    {
        throw new UnsupportedOperationException("readLine() not supported in HeaderInputStream");
    }

    @Override
    public String readUTF() throws IOException
    {
        int length = this.readInt(); // Read the length of the string
        byte[] data = new byte[length]; // Set up the byte array to store the inputstream data in
        this.readFully(data); // read the inputstream data into the buffer
        return new String(data, 0, data.length, Charset.forName("UTF-8"));
    }

}
