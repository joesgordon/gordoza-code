package org.jutils.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AltEditorPane extends JTextPane
{
    /**  */
    protected StyledDocument doc = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public AltEditorPane()
    {
        super();
        init();
    }

    /***************************************************************************
     * @param doc
     * @throws IOException
     **************************************************************************/
    public AltEditorPane( StyledDocument doc ) throws IOException
    {
        super( doc );
        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init()
    {
        doc = getStyledDocument();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setText( String text )
    {
        super.setText( text );
        init();
    }

    /***************************************************************************
     * @param text
     * @param a
     * @throws BadLocationException
     **************************************************************************/
    public void appendText( String text, AttributeSet a )
    {
        try
        {
            doc.insertString( doc.getLength(), text, a );
        }
        catch( BadLocationException ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setDocument( Document doc )
    {
        if( doc instanceof StyledDocument )
        {
            super.setStyledDocument( ( StyledDocument )doc );
        }
        else
        {
            throw new IllegalArgumentException( "Document must be of type " +
                StyledDocument.class.getName() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setStyledDocument( StyledDocument doc )
    {
        super.setDocument( doc );
        this.doc = doc;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean getScrollableTracksViewportWidth()
    {
        Component parent = getParent();
        ComponentUI ui = getUI();

        return parent != null ? ( ui.getPreferredSize( this ).width <= parent.getSize().width )
            : true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setBounds( int x, int y, int width, int height )
    {
        Dimension size = this.getPreferredSize();
        super.setBounds( x, y, Math.max( size.width, width ), height );
    }

}
