package chatterbox.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;

import org.jutils.core.IconConstants;
import org.jutils.core.data.FontDescription;
import org.jutils.core.ui.FontChooserView;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.ItemActionList;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IDataView;

import chatterbox.data.DecoratedText;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DecoratedTextView implements IDataView<DecoratedText>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextPane textField;
    /**  */
    private final ItemActionList<DecoratedText> enterListeners;

    /**  */
    private DecoratedText text;

    /***************************************************************************
     * 
     **************************************************************************/
    public DecoratedTextView()
    {
        this.textField = new JTextPane();
        this.view = createView();
        this.enterListeners = new ItemActionList<>();

        textField.getDocument().addDocumentListener(
            new GrowingTextDocumentListener( textField ) );

        setData( new DecoratedText() );

        textField.getDocument().addDocumentListener(
            new TextChangedListener( () -> updateData() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateData()
    {
        text.attributes.fromStyledDocument( textField.getStyledDocument() );
        text.text = textField.getText();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel contentPanel = new JPanel( new GridBagLayout() );
        ActionListener fontButtonListener = new FontListener( this );

        contentPanel.setBorder( BorderFactory.createEtchedBorder() );

        JScrollPane msgScrollPane = new GrowingScrollPane( textField );

        JToolBar toolbar = new JToolBar();
        BottomScroller bottomScroller = new BottomScroller( textField );
        JButton fontButton = new JButton( "Font" );

        fontButton.setIcon( IconConstants.getIcon( IconConstants.FONT_24 ) );
        fontButton.addActionListener( fontButtonListener );

        textField.addComponentListener( bottomScroller );
        addEnterHook( textField );

        msgScrollPane.setMinimumSize( new Dimension( 100, 48 ) );
        msgScrollPane.setMaximumSize( new Dimension( 100, 150 ) );
        msgScrollPane.setBorder( null );
        msgScrollPane.setBorder(
            BorderFactory.createMatteBorder( 1, 0, 0, 0, Color.gray ) );

        toolbar.add( fontButton );
        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        contentPanel.add( toolbar,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( msgScrollPane,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return contentPanel;
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
        l = ( e ) -> enterListeners.fireListeners( this, text );
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
    public DecoratedText getData()
    {
        return text;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( DecoratedText data )
    {
        this.text = data;

        textField.setText( data.text );
        if( data.attributes != null )
        {
            text.attributes.toStyledDocument( textField.getStyledDocument() );
        }
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addEnterListener( ItemActionListener<DecoratedText> l )
    {
        enterListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FontListener implements ActionListener
    {
        private final DecoratedTextView view;

        public FontListener( DecoratedTextView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            FontChooserView fontChooser = new FontChooserView();
            OkDialogView dialogView = new OkDialogView( view.textField,
                fontChooser.getView(), ModalityType.DOCUMENT_MODAL );

            FontDescription desc = new FontDescription();

            desc.setAttributes( view.textField.getCharacterAttributes() );
            fontChooser.setData( desc );
            dialogView.pack();

            if( dialogView.show() )
            {
                SimpleAttributeSet s = new SimpleAttributeSet();
                fontChooser.getData().getAttributes( s );

                if( s != null )
                {
                    view.textField.setCharacterAttributes( s, true );
                    view.text.attributes.fromStyledDocument(
                        view.textField.getStyledDocument() );
                }
            }
        }
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
