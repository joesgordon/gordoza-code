package testbed;

import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.data.SystemProperty;
import org.jutils.data.UIProperty;
import org.jutils.io.parsers.StringParser;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.fields.ComboFormField;
import org.jutils.ui.fields.StringFormField;

public final class BindingMain
{
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new BindingApp() );
    }

    private static final class BindingApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView view = new StandardFrameView();

            view.setTitle( "Binding Test" );
            view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            view.setSize( 500, 500 );

            view.setContent( createContent() );

            SwingUtils.installEscapeCloseOperation( view.getView() );

            return view.getView();
        }

        private Container createContent()
        {
            StandardFormView form = new StandardFormView();
            JComboBox<SystemProperty> combo = new JComboBox<>(
                SystemProperty.values() );
            StringFormField strField = new StringFormField( "StringFormField" );
            ComboFormField<String> uiField = new ComboFormField<>(
                "ComboFormField", strs() );

            combo.setEditable( true );
            uiField.setUserEditable( new StringParser() );

            form.addField( "JComboBox", combo );
            form.addField( strField );
            form.addField( uiField );

            JPanel panel = form.getView();
            ActionListener l = ( e ) -> SwingUtils.showErrorMessage( panel,
                "Enter pressed", "Yay!" );
            SwingUtils.addKeyListener( panel, "ENTER", true, l,
                "enterpressed" );
            SwingUtils.addKeyListener( combo, "ENTER", true, l,
                "enterpressed" );
            SwingUtils.addKeyListener(
                ( JComponent )combo.getEditor().getEditorComponent(), "ENTER",
                false, l, "enterpressed" );
            SwingUtils.addKeyListener( uiField.getView(), "ENTER", false, l,
                "enterpressed" );

            combo.addActionListener( l );

            return form.getView();
        }

        private static String[] strs()
        {
            UIProperty[] vals = UIProperty.values();
            String[] names = new String[vals.length];

            for( int i = 0; i < vals.length; i++ )
            {
                names[i] = vals[i].key;
            }

            return names;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
