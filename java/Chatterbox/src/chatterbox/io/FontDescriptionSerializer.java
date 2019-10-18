package chatterbox.io;

import java.awt.Color;
import java.io.IOException;

import org.jutils.data.FontDescription;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;
import org.jutils.io.LengthStringSerializer;

/***************************************************************************
 * 
 **************************************************************************/
public class FontDescriptionSerializer
    implements IDataSerializer<FontDescription>
{
    /**  */
    private final LengthStringSerializer strSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public FontDescriptionSerializer()
    {
        this.strSerializer = new LengthStringSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public FontDescription read( IDataStream stream ) throws IOException
    {
        FontDescription font = new FontDescription();

        font.name = strSerializer.read( stream );
        font.size = stream.readInt();
        font.bold = stream.readBoolean();
        font.italic = stream.readBoolean();
        font.underline = stream.readBoolean();
        font.strikeThrough = stream.readBoolean();
        font.subscript = stream.readBoolean();
        font.superscript = stream.readBoolean();

        int a = stream.readInt();
        int r = stream.readInt();
        int g = stream.readInt();
        int b = stream.readInt();

        font.color = new Color( r, g, b, a );

        return font;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( FontDescription font, IDataStream stream )
        throws IOException
    {
        strSerializer.write( font.name, stream );
        stream.writeInt( font.size );
        stream.writeBoolean( font.bold );
        stream.writeBoolean( font.italic );
        stream.writeBoolean( font.underline );
        stream.writeBoolean( font.strikeThrough );
        stream.writeBoolean( font.subscript );
        stream.writeBoolean( font.superscript );
        stream.writeInt( font.color.getAlpha() );
        stream.writeInt( font.color.getRed() );
        stream.writeInt( font.color.getGreen() );
        stream.writeInt( font.color.getBlue() );
    }
}
