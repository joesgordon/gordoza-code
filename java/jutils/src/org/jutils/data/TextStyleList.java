package org.jutils.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.*;

import org.jutils.io.StringPrintStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextStyleList
{
    /**  */
    public final List<TextStyle> styles;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextStyleList()
    {
        this.styles = new ArrayList<>();
    }

    /***************************************************************************
     * @param description
     **************************************************************************/
    private void addDescription( FontDescription description )
    {
        if( styles.isEmpty() )
        {
            TextStyle ts = new TextStyle( 0, 1, description );
            styles.add( ts );
        }
        else
        {
            TextStyle ts = styles.get( styles.size() - 1 );

            if( ts.description.equals( description ) )
            {
                ts.count++;
            }
            else
            {
                ts = new TextStyle( ts.location + ts.count, 1, description );
                styles.add( ts );
            }
        }
    }

    /***************************************************************************
     * @param doc
     **************************************************************************/
    public void fromStyledDocument( StyledDocument doc )
    {
        for( int i = 0; i < doc.getLength(); i++ )
        {
            AttributeSet set = doc.getCharacterElement( i ).getAttributes();

            FontDescription fd = new FontDescription();

            fd.setAttributes( set );

            addDescription( fd );

            // LogUtils.printDebug( "************" );
            // LogUtils.printDebug( fd.toString() );
        }
    }

    /***************************************************************************
     * @param doc
     **************************************************************************/
    public void toStyledDocument( StyledDocument doc )
    {
        for( TextStyle style : styles )
        {
            SimpleAttributeSet s = new SimpleAttributeSet();
            style.description.getAttributes( s );
            doc.setCharacterAttributes( style.location, style.count, s, true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class TextStyle
    {
        public int location;
        public int count;
        public FontDescription description;

        public TextStyle( int location, int count, FontDescription description )
        {
            this.location = location;
            this.count = count;
            this.description = description;
        }

        @Override
        public String toString()
        {
            try( StringPrintStream str = new StringPrintStream() )
            {
                str.println( "Location: %d", location );
                str.println( "Count: %d", count );
                str.println( description.toString() );

                return str.toString();
            }
        }
    }
}
