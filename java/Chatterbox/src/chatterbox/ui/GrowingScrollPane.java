package chatterbox.ui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

/***************************************************************************
 * 
 **************************************************************************/
public class GrowingScrollPane extends JScrollPane
{
    /**  */
    private static final long serialVersionUID = 4602409080360339374L;

    /***************************************************************************
     * @param view
     **************************************************************************/
    public GrowingScrollPane( JComponent view )
    {
        super( view );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension getPreferredSize()
    {
        Dimension size = super.getPreferredSize();

        if( size.height < getMinimumSize().height )
        {
            size.height = getMinimumSize().height;
        }
        else if( size.height > getMaximumSize().height )
        {
            size.height = getMaximumSize().height;
        }

        return size;
    }
}
