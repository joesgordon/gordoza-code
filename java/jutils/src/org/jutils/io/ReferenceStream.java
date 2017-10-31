package org.jutils.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jutils.ValidationException;

public final class ReferenceStream<T> implements IReferenceStream<T>
{
    private final IDataSerializer<T> serializer;
    private final File refFileToDelete;
    private final IDataStream refStream;
    private final File itemsFileToDelete;
    private final IDataStream itemsStream;

    private long count;

    public ReferenceStream( IDataSerializer<T> serializer )
        throws FileNotFoundException, IOException
    {
        this( serializer, createTempFile(), true );
    }

    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile )
        throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, false );
    }

    public ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        File referenceFile ) throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, false, createStream( itemsFile ),
            referenceFile, false, createStream( referenceFile ) );
    }

    public ReferenceStream( IDataSerializer<T> serializer,
        IDataStream refStream, IDataStream itemsStream ) throws IOException
    {
        this( serializer, null, false, refStream, null, false, itemsStream );
    }

    private ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        boolean deleteItems ) throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, deleteItems, createTempFile(), true );
    }

    private ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        boolean deleteItems, File referenceFile, boolean deleteReference )
        throws FileNotFoundException, IOException
    {
        this( serializer, itemsFile, deleteItems, createStream( itemsFile ),
            referenceFile, deleteReference, createStream( referenceFile ) );
    }

    private ReferenceStream( IDataSerializer<T> serializer, File itemsFile,
        boolean deleteItems, IDataStream itemsStream, File referenceFile,
        boolean deleteReference, IDataStream referenceStream )
        throws FileNotFoundException, IOException
    {
        this.serializer = serializer;

        this.itemsFileToDelete = deleteItems ? itemsFile : null;
        this.itemsStream = itemsStream;

        this.refFileToDelete = deleteReference ? referenceFile : null;
        this.refStream = referenceStream;

        this.count = refStream.getLength() / 8;

        if( itemsStream.getLength() > 0 && refStream.getLength() == 0 )
        {
            while( itemsStream.getAvailable() > 0 )
            {
                try
                {
                    serializer.read( itemsStream );
                    count++;
                }
                catch( ValidationException ex )
                {
                    count = 0;
                    throw new IOException( ex );
                }
            }
        }
    }

    @Override
    public void close() throws IOException
    {
        refStream.close();
        itemsStream.close();

        if( refFileToDelete != null )
        {
            refFileToDelete.delete();
        }

        if( itemsFileToDelete != null )
        {
            itemsFileToDelete.delete();
        }
    }

    private static File createTempFile() throws IOException
    {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "uuuu-MM-dd_HHmmss_SSS_" );
        String prefix = time.format( formatter );
        File file = File.createTempFile( prefix, "_refstream.bin" );

        file.deleteOnExit();

        return file;
    }

    @Override
    public long getCount()
    {
        return count;
    }

    @Override
    public void write( T item ) throws IOException
    {
        long rpos = refStream.getLength();
        long ipos = itemsStream.getLength();

        refStream.seek( rpos );
        itemsStream.seek( ipos );

        refStream.writeLong( ipos );
        serializer.write( item, itemsStream );

        count++;
    }

    @Override
    public T read( long index ) throws IOException, ValidationException
    {
        long rpos = index * 8;
        long ipos;

        refStream.seek( rpos );

        ipos = refStream.readLong();

        itemsStream.seek( ipos );

        return serializer.read( itemsStream );
    }

    @Override
    public List<T> read( long index, int count )
        throws IOException, ValidationException
    {
        List<T> items = new ArrayList<>( count );
        long rpos = index * 8;
        long ipos;

        refStream.seek( rpos );

        ipos = refStream.readLong();

        itemsStream.seek( ipos );

        for( int i = 0; i < count; i++ )
        {
            items.add( serializer.read( itemsStream ) );
        }

        return items;
    }

    @SuppressWarnings( "resource")
    private static IDataStream createStream( File file )
        throws FileNotFoundException
    {
        // TODO !!!! fix bug in BufferedStream.
        // To illustrate, start NetMessagesViewMain. Go back. Add.
        // return new DataStream( new BufferedStream( new FileStream( file ) )
        // );
        return new DataStream( new FileStream( file ) );
    }
}
