package org.jutils.io.cksum;

import java.util.zip.CRC32;

public class Crc32Checksum implements IChecksum
{
    private final CRC32 crc;

    public Crc32Checksum()
    {
        this.crc = new CRC32();
    }

    @Override
    public void update( byte [] bytes )
    {
        crc.update( bytes );
    }

    @Override
    public void update( byte [] bytes, int off, int len )
    {
        crc.update( bytes, off, len );
    }

    @Override
    public void reset()
    {
        crc.reset();
    }

    @Override
    public byte [] getChecksum()
    {
        long csm = crc.getValue();
        byte [] b = new byte[8];
        int shift;

        for( int i = 0; i < b.length; ++i )
        {
            shift = 8 * ( 7 - i );
            b[i] = ( byte )( ( csm >> shift ) & 0xFF );
        }

        return b;
    }

}
