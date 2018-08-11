package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import org.jutils.SwingUtils;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.*;
import org.jutils.ui.fields.HexAreaFormField;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageTextView implements IDataView<byte[]>
{
    /**  */
    private final String CR = "" + ( char )0xD;
    /**  */
    private final String LF = "" + ( char )0xA;

    /**  */
    private final JComponent view;
    /**  */
    private final JTextArea textField;
    /**  */
    private final HexAreaFormField hexField;
    /**  */
    private final ItemActionList<byte[]> enterListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public MessageTextView()
    {
        this.textField = new JTextArea();
        this.hexField = new HexAreaFormField( "Message Bytes" );
        this.view = createView();
        this.enterListeners = new ItemActionList<>();

        // textField.getDocument().addDocumentListener(
        // new GrowingTextDocumentListener( textField ) );

        setData( HexUtils.fromHexStringToArray( "EB91" ) );

        SwingUtils.addKeyListener( textField, "shift ENTER", false,
            ( e ) -> insertText( LF ), "Shift+Enter Listener" );
        SwingUtils.addKeyListener( textField, "control ENTER", false,
            ( e ) -> insertText( CR ), "Control+Enter Listener" );
    }

    /***************************************************************************
     * Inserts the provided text at the current cursor position in the
     * {@link #textField}.
     * @param text the text to be inserted.
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
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new GridLayout( 2, 1, 4, 4 ) );

        TitleView textView = new TitleView( "Text", createTextView() );
        TitleView hexView = new TitleView( "Hex", hexField.getView() );

        panel.add( textView.getView() );
        panel.add( hexView.getView() );

        addEnterHook( hexField.getTextArea() );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTextView()
    {
        JPanel contentPanel = new JPanel( new BorderLayout() );

        // JScrollPane msgScrollPane = new GrowingScrollPane( textField );
        JScrollPane msgScrollPane = new JScrollPane( textField );

        // BottomScroller bottomScroller = new BottomScroller( textField );

        // textField.addComponentListener( bottomScroller );
        addEnterHook( textField );

        msgScrollPane.setMinimumSize( new Dimension( 100, 48 ) );
        msgScrollPane.setMaximumSize( new Dimension( 100, 150 ) );
        msgScrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        msgScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        contentPanel.add( msgScrollPane, BorderLayout.CENTER );

        return contentPanel;
    }

    /***************************************************************************
     * @param textPane
     **************************************************************************/
    private void addEnterHook( JTextArea textPane )
    {
        KeyStroke ks;
        String aname;
        ActionListener l;
        Action action;
        ActionMap amap;
        InputMap imap;

        ks = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 );
        aname = "SEND_MESSAGE";
        l = ( e ) -> enterListeners.fireListeners( this,
            textField.getText().getBytes( hexField.UTF8 ) );
        action = new ActionAdapter( l, aname, null );
        amap = textPane.getActionMap();
        imap = textPane.getInputMap();
        imap.put( ks, aname );
        amap.put( aname, action );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte[] getData()
    {
        if( hexField.getView().isShowing() )
        {
            return hexField.getValue();
        }

        return textField.getText().getBytes();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( byte[] text )
    {
        textField.setText( new String( text ) );
        hexField.setValue( text );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addEnterListener( ItemActionListener<byte[]> l )
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
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
        hexField.setEditable( editable );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setText( String text )
    {
        textField.setText( text );
        hexField.setText( text );
    }
}
