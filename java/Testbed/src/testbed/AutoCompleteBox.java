package testbed;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.*;
import javax.swing.JComboBox.KeySelectionManager;
import javax.swing.text.*;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class AutoCompleteBox<T> implements IView<JComboBox<T>>
{
    private final FireableComboBox<T> field;
    private final DefaultComboBoxModel<T> model;

    public AutoCompleteBox( T[] items )
    {
        this.model = new DefaultComboBoxModel<>( items );
        this.field = new FireableComboBox<>( model );

        field.setKeySelectionManager( new AutoKeySelectionManager() );
        field.addActionListener( ( e ) -> findAndSelect() );
        JTextField tf = ( JTextField )field.getEditor().getEditorComponent();
        tf.setDocument( new CBDocument2( field ) );
    }

    private void findAndSelect()
    {
        JTextField tf = ( JTextField )field.getEditor().getEditorComponent();
        String text = tf.getText();
        ComboBoxModel<T> aModel = field.getModel();
        String current;
        for( int i = 0; i < aModel.getSize(); i++ )
        {
            current = aModel.getElementAt( i ).toString();
            if( current.toLowerCase().startsWith( text.toLowerCase() ) )
            {
                tf.setText( current );
                tf.setSelectionStart( text.length() );
                tf.setSelectionEnd( current.length() );
                break;
            }
        }
    }

    @Override
    public JComboBox<T> getView()
    {
        return field;
    }

    /***************************************************************************
     * @param arg
     **************************************************************************/
    public static void main( String arg[] )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                JFrame f = new JFrame( "AutoCompleteComboBox" );
                f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                f.setSize( 200, 300 );
                Container cp = f.getContentPane();
                cp.setLayout( null );
                Locale[] locales = Locale.getAvailableLocales();//
                AutoCompleteBox<Locale> abox = new AutoCompleteBox<Locale>(
                    locales );
                JComboBox<Locale> cBox = abox.getView();
                // JComboBox<Locale> cBox = abox;
                cBox.setBounds( 50, 50, 100, 21 );
                cBox.setEditable( true );
                cp.add( cBox );
                return f;
            }
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class CBDocument2 extends PlainDocument
    {
        private static final long serialVersionUID = 8461393629310010285L;
        private final FireableComboBox<T> comboBox;

        public CBDocument2( FireableComboBox<T> comboBox )
        {
            this.comboBox = comboBox;
        }

        @Override
        public void insertString( int offset, String str, AttributeSet a )
            throws BadLocationException
        {
            if( str == null )
                return;
            super.insertString( offset, str, a );
            if( !comboBox.isPopupVisible() && str.length() != 0 )
            {
                comboBox.fireSuperActionEvent();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AutoKeySelectionManager implements KeySelectionManager
    {
        private String searchFor;
        private long lap;

        public AutoKeySelectionManager()
        {
            lap = new java.util.Date().getTime();
        }

        @Override
        public int selectionForKey( char aKey,
            @SuppressWarnings( "rawtypes") ComboBoxModel aModel )
        {
            long now = new java.util.Date().getTime();

            if( searchFor != null && aKey == KeyEvent.VK_BACK_SPACE &&
                searchFor.length() > 0 )
            {
                searchFor = searchFor.substring( 0, searchFor.length() - 1 );
            }
            else
            {
                // LogUtils.printDebug(lap);
                if( lap + 1000 < now )
                {
                    searchFor = "" + aKey;
                }
                else
                {
                    searchFor = searchFor + aKey;
                }
            }

            String searchStr = searchFor.toLowerCase( Locale.ENGLISH );

            lap = now;

            for( int i = 0; i < aModel.getSize(); i++ )
            {
                Object obj = aModel.getElementAt( i );
                String current = obj.toString().toLowerCase( Locale.ENGLISH );

                if( current.startsWith( searchStr ) )
                {
                    return i;
                }
            }

            return -1;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class FireableComboBox<T> extends JComboBox<T>
    {
        private static final long serialVersionUID = 1445722791528115948L;

        // public FireableComboBox( T[] items )
        // {
        // super( items );
        // }

        public FireableComboBox( ComboBoxModel<T> model )
        {
            super( model );
        }

        public void fireSuperActionEvent()
        {
            super.fireActionEvent();
        }
    }
}
