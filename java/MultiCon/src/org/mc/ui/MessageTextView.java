package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.jutils.SwingUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageTextView implements IDataView<String>
{
    private final String CR = "" + ( char )0xD;

    /**  */
    private final JPanel view;
    /**  */
    private final JTextPane textField;
    /**  */
    private final ItemActionList<String> enterListeners;

    /**  */
    private IUpdater<String> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public MessageTextView()
    {
        this.textField = new JTextPane();
        this.view = createView();
        this.enterListeners = new ItemActionList<>();

        // textField.getDocument().addDocumentListener(
        // new GrowingTextDocumentListener( textField ) );

        setData( new String() );

        textField.getDocument().addDocumentListener(
            new TextChangedListener( () -> updateData() ) );
        SwingUtils.addKeyListener( textField, "shift ENTER", false,
            ( e ) -> insertText( "\n" ), "Shift+Enter Listener" );
        SwingUtils.addKeyListener( textField, "control ENTER", false,
            ( e ) -> insertText( CR ), "Control+Enter Listener" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void insertText( String text )
    {
        int offset = textField.getCaretPosition();
        try
        {

            textField.getDocument().insertString( offset, text, null );
        }
        catch( BadLocationException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateData()
    {
        if( updater != null )
        {
            updater.update( textField.getText() );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.setBorder( BorderFactory.createEtchedBorder() );

        // JScrollPane msgScrollPane = new GrowingScrollPane( textField );
        JScrollPane msgScrollPane = new JScrollPane( textField );

        // BottomScroller bottomScroller = new BottomScroller( textField );

        // textField.addComponentListener( bottomScroller );
        addEnterHook( textField );

        msgScrollPane.setMinimumSize( new Dimension( 100, 48 ) );
        msgScrollPane.setMaximumSize( new Dimension( 100, 150 ) );
        msgScrollPane.setBorder( null );
        msgScrollPane.setBorder(
            BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.gray ) );

        contentPanel.add( msgScrollPane, BorderLayout.CENTER );

        return contentPanel;
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * @param textPane
     **************************************************************************/
    private void addEnterHook( JTextPane textPane )
    {
        KeyStroke ks;
        String aname;
        Action action;
        ActionMap amap;
        InputMap imap;
        ActionListener l;

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 );
        aname = "SEND_MESSAGE";
        l = ( e ) -> enterListeners.fireListeners( this, textField.getText() );
        action = new ActionAdapter( l, aname, null );
        amap = textPane.getActionMap();
        imap = textPane.getInputMap();
        imap.put( ks, aname );
        amap.put( aname, action );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getData()
    {
        return textField.getText();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( String text )
    {
        textField.setText( text );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addEnterListener( ItemActionListener<String> l )
    {
        enterListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        textField.selectAll();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TextChangedListener implements DocumentListener
    {
        private final Runnable callback;

        public TextChangedListener( Runnable callback )
        {
            this.callback = callback;
        }

        @Override
        public void insertUpdate( DocumentEvent e )
        {
            callback.run();
        }

        @Override
        public void removeUpdate( DocumentEvent e )
        {
            callback.run();
        }

        @Override
        public void changedUpdate( DocumentEvent e )
        {
            callback.run();
        }
    }
}
