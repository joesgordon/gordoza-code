package chatterbox.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GrowingTextPane extends JTextPane
{
    public GrowingTextPane()
    {
        super();

        getDocument().addDocumentListener( new DocumentListener()
        {
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

            private void revalidateWindow()
            {
                Window window = SwingUtilities.getWindowAncestor( GrowingTextPane.this );

                window.validate();
            }

            private void revalidateWindowLater()
            {
                Timer timer = new Timer( 50, new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        revalidateWindow();
                    }
                } );

                timer.setRepeats( false );
                timer.start();
            }
        } );
    }
}
