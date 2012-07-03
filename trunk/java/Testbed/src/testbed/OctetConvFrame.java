package testbed;

import java.awt.*;
import java.nio.ByteBuffer;

import javax.swing.*;

import org.jutils.NumberParsingUtils;
import org.jutils.ui.*;
import org.jutils.ui.UValidationTextField.ITextValidator;
import org.jutils.ui.model.FormatException;

public class OctetConvFrame extends FrameRunner
{
    private final UValidationTextField octField;
    private final UValidationTextField decField;
    private final UValidationTextField hexField;
    private final FieldValidator octListener;
    private final FieldValidator decListener;
    private final FieldValidator hexListener;

    public OctetConvFrame()
    {
        octField = new UValidationTextField();
        decField = new UValidationTextField();
        hexField = new UValidationTextField();

        octListener = new OctValidator();
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

        JLabel octLabel = new JLabel( "Octet :" );
        JLabel decLabel = new JLabel( "Decimal :" );
        JLabel hexLabel = new JLabel( "Hex :" );

        octField.setValidator( octListener );
        decField.setValidator( decListener );
        hexField.setValidator( hexListener );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 6, 6,
                6, 6 ), 0, 0 );
        panel.add( octLabel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        panel.add( octField.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 6, 6,
                6, 6 ), 0, 0 );
        panel.add( decLabel, constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        panel.add( decField.getView(), constraints );

        constraints = new GridBagConstraints( 0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 12,
                6, 6, 6 ), 0, 0 );
        panel.add( hexLabel, constraints );

        constraints = new GridBagConstraints( 0, 5, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 0 );
        panel.add( hexField.getView(), constraints );

        return panel;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    private void setOctetField( long num )
    {
        byte[] bytes = new byte[4];
        ByteBuffer buf = ByteBuffer.allocate( 8 );

        buf.putLong( num );
        buf.position( 4 );
        buf.get( bytes );

        String text = "";

        for( int i = 0; i < bytes.length; i++ )
        {
            text += bytes[i] & 0x0FF;
            if( i < bytes.length - 1 )
            {
                text += ".";
            }
        }

        octField.setText( text );
    }

    private void setDecField( long num )
    {
        decField.setText( "" + num );
    }

    private void setHexField( long num )
    {
        hexField.setText( String.format( "%X", num ) );
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new OctetConvFrame() );
    }

    private class DecValidator extends FieldValidator
    {
        @Override
        public boolean validate( String text )
        {
            hexListener.setEnabled( false );
            octListener.setEnabled( false );

            try
            {
                long i = Long.parseLong( text );

                setHexField( i );
                setOctetField( i );
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
                octListener.setEnabled( true );
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
            octListener.setEnabled( false );

            try
            {
                long i = NumberParsingUtils.parseHexLong( text );

                setDecField( i );
                setOctetField( i );
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
                octListener.setEnabled( true );
            }

            return true;
        }
    }

    private class OctValidator extends FieldValidator
    {
        @Override
        public boolean validate( String text )
        {
            decListener.setEnabled( false );
            hexListener.setEnabled( false );

            try
            {
                String[] nums = text.split( "\\." );

                if( nums.length == 4 )
                {
                    byte[] bytes = new byte[4];

                    for( int i = 0; i < bytes.length; i++ )
                    {
                        bytes[i] = ( byte )Integer.parseInt( nums[i] );
                    }

                    ByteBuffer buf = ByteBuffer.allocate( 8 );

                    buf.position( 4 );
                    buf.put( bytes );
                    buf.position( 0 );

                    long i = buf.getLong();

                    setHexField( i );
                    setDecField( i );
                }
                else
                {
                    return false;
                }
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
                hexListener.setEnabled( true );
            }

            return true;
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
        public final void validateText( String text ) throws FormatException
        {
            if( enabled )
            {
                validate( text );
            }
        }

        protected abstract boolean validate( String text )
            throws FormatException;
    }
}
