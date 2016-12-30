package chatterbox.ui;

import java.awt.Component;
import java.awt.Window;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GrowingTextDocumentListener implements DocumentListener
{
    private final Component comp;

    public GrowingTextDocumentListener( Component comp )
    {
        this.comp = comp;
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
        revalidateWindowLater();
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
        revalidateWindowLater();
    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
    }

    private void revalidateWindowLater()
    {
        Timer timer = new Timer( 50, ( e ) -> revalidateWindow() );

        timer.setRepeats( false );
        timer.start();
    }

    private void revalidateWindow()
    {
        Window window = SwingUtilities.getWindowAncestor( comp );

        window.validate();
    }
}
