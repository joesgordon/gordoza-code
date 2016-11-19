package testbed;

import java.awt.Container;
import java.util.Locale;

import javax.swing.*;
import javax.swing.text.*;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AutoCompleteBox<T> implements IView<JComboBox<T>>
{
    /**  */
    private final JComboBox<T> field;
    /**  */
    private final DefaultComboBoxModel<T> model;

    /***************************************************************************
     * @param items
     **************************************************************************/
    public AutoCompleteBox( T[] items )
    {
        this.model = new DefaultComboBoxModel<>( items );
        this.field = new JComboBox<>( model );

        // field.setKeySelectionManager( new AutoKeySelectionManager() );
        field.addActionListener( ( e ) -> findAndSelect() );

        JTextField tf = ( JTextField )field.getEditor().getEditorComponent();
        tf.setDocument( new CBDocument2() );
        // tf.getDocument().addDocumentListener( new TextUpdate( this ) );

        model.setSelectedItem( null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void findAndSelect()
    {
        JTextField tf = ( JTextField )field.getEditor().getEditorComponent();
        String text = tf.getText();
        ComboBoxModel<T> aModel = field.getModel();

        text = text.toLowerCase( Locale.ENGLISH );

        for( int i = 0; i < aModel.getSize(); i++ )
        {
            T item = aModel.getElementAt( i );
            String current = item.toString().toLowerCase( Locale.ENGLISH );

            if( current.startsWith( text ) )
            {
                tf.setText( current );
                tf.setSelectionStart( text.length() );
                tf.setSelectionEnd( current.length() );

                model.setSelectedItem( item );

                break;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
        // private final FireableComboBox<T> comboBox;

        // public CBDocument2( FireableComboBox<T> comboBox )
        // {
        // this.comboBox = comboBox;
        // }

        @Override
        public void insertString( int offset, String str, AttributeSet a )
            throws BadLocationException
        {
            if( str == null )
            {
                return;
            }

            super.insertString( offset, str, a );

            findAndSelect();

            // if( !comboBox.isPopupVisible() && str.length() != 0 )
            // {
            // comboBox.fireSuperActionEvent();
            // }
        }
    }
}
