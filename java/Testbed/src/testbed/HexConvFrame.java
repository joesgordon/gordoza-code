package testbed;

import java.awt.*;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.ui.*;
import org.jutils.ui.UValidationTextField.TextValidator;
import org.jutils.ui.event.ItemActionListener;

public class HexConvFrame extends FrameRunner
{
    private final UValidationTextField decField;
    private final UValidationTextField hexField;
    private final FieldValidator decListener;
    private final FieldValidator hexListener;

    public HexConvFrame()
    {
        decField = new UValidationTextField();
        hexField = new UValidationTextField();

        decListener = new DecValidator();
        hexListener = new HexValidator();
    }

    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();

        frame.setContentPane( createContentPane() );
        frame.setSize( 400, 200 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    private Container createContentPane()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        JLabel decLabel = new JLabel( "Decimal :" );
        JLabel hexLabel = new JLabel( "Hex :" );

        decField.setValidator( decListener );
        hexField.setValidator( hexListener );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 6, 6,
                6, 6 ), 0, 0 );
        panel.add( decLabel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        panel.add( decField, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 12,
                6, 6, 6 ), 0, 0 );
        panel.add( hexLabel, constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        panel.add( hexField, constraints );

        return panel;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    private void setHexField( long num )
    {
        hexField.setText( String.format( "%X", num ) );
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new HexConvFrame() );
    }

    private class DecValidator extends FieldValidator
    {
        @Override
        public boolean validate( String text )
        {
            hexListener.setEnabled( false );
            try
            {
                long i = Long.parseLong( text );
                setHexField( i );
            }
            catch( NumberFormatException ex )
            {
                System.err.println( ex.getMessage() );
                // ex.printStackTrace();
                return false;
            }
            finally
            {
                hexListener.setEnabled( true );
            }

            return true;
        }
    }

    private class HexValidator extends FieldValidator
    {
        @Override
        public boolean validate( String text )
        {
            decListener.setEnabled( false );
            try
            {
                long i = Utils.parseHexLong( text );
                decField.setText( "" + i );
            }
            catch( NumberFormatException ex )
            {
                System.err.println( ex.getMessage() );
                // ex.printStackTrace();
                return false;
            }
            finally
            {
                decListener.setEnabled( true );
            }

            return true;
        }
    }

    private abstract class FieldValidator implements TextValidator
    {
        private boolean enabled;

        public FieldValidator()
        {
            this.enabled = true;
        }

        public final void setEnabled( boolean enabled )
        {
            this.enabled = enabled;
        }

        @Override
        public final boolean validateText( String text )
        {
            if( enabled )
            {
                return validate( text );
            }

            return true;
        }

        protected abstract boolean validate( String text );
    }

    public interface IConverter<T, V>
    {
        public V convertTo( T item );

        public T convertFrom( V item );
    }

    public interface IConverterView<T>
    {
        public String getTypeName();

        public void addChangeListener( ItemActionListener<T> l );

        public void setChangeListenersEnabled( boolean enabled );

        public T getItem();

        public void setItem( T item );

        public Component getView();
    }
}
