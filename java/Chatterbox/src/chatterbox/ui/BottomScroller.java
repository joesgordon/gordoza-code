package chatterbox.ui;

import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JTextPane;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BottomScroller extends ComponentAdapter
{
    /**  */
    private final JTextPane textPane;

    /***************************************************************************
     * @param textPane
     **************************************************************************/
    public BottomScroller( JTextPane textPane )
    {
        this.textPane = textPane;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void scrollToBottom()
    {
        textPane.scrollRectToVisible(
            new Rectangle( 0, textPane.getHeight(), 1, 1 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void componentResized( ComponentEvent e )
    {
        scrollToBottom();
    }
}
