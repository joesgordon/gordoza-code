package testbed;

import java.awt.*;

import javax.swing.*;

import org.jutils.NumberParsingUtils;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.validation.ValidationException;
import org.jutils.ui.validation.ValidationTextField;
import org.jutils.ui.validators.ITextValidator;

public class HexConvFrame implements IFrameApp
{
    private final ValidationTextField decField;
    private final ValidationTextField hexField;
    private final FieldValidator decListener;
    private final FieldValidator hexListener;

    public HexConvFrame()
    {
        decField = new ValidationTextField();
        hexField = new ValidationTextField();

        decListener = new DecValidator();
        hexListener = new HexValidator();
    }

    @Override
    public JFrame createFrame()
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
        panel.add( decField.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 12,
                6, 6, 6 ), 0, 0 );
        panel.add( hexLabel, constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        panel.add( hexField.getView(), constraints );

        return panel;
    }

    @Override
    public void finalizeGui()
    {
    }

    private void setHexField( long num )
    {
        hexField.setText( String.format( "%X", num ) );
    }

    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new HexConvFrame() );
    }

    private class DecValidator extends FieldValidator
    {
        @Override
        public void validate( String text ) throws ValidationException
        {
            hexListener.setEnabled( false );
            try
            {
                long i = Long.parseLong( text );
                setHexField( i );
            }
            catch( NumberFormatException ex )
            {
                throw new ValidationException( ex.getMessage() );
            }
            finally
            {
                hexListener.setEnabled( true );
            }
        }
    }

    private class HexValidator extends FieldValidator
    {
        @Override
        public void validate( String text ) throws ValidationException
        {
            decListener.setEnabled( false );
            try
            {
                long i = NumberParsingUtils.parseHexLong( text );
                decField.setText( "" + i );
            }
            catch( NumberFormatException ex )
            {
                throw new ValidationException( ex.getMessage() );
            }
            finally
            {
                decListener.setEnabled( true );
            }
        }
    }

    private abstract class FieldValidator implements ITextValidator
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
        public final void validateText( String text )
            throws ValidationException
        {
            if( enabled )
            {
                validate( text );
            }
        }

        protected abstract void validate( String text )
            throws ValidationException;
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
