package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.ui.FrameRunner;
import org.jutils.ui.validation.*;

public class UTextValidatorTest extends FrameRunner
{

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new UTextValidatorTest() );
    }

    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        JLabel jl1 = new JLabel( "Number, (0-25):" );
        JLabel jl2 = new JLabel( "Number, (0-25):" );
        JLabel jl3 = new JLabel( "Number, [0-25]:" );
        JLabel jl4 = new JLabel( "The phrase 'e pluribus unum':" );

        final ValidationTextField uvtf1a = new ValidationTextField();
        final ValidationTextField uvtf2a = new ValidationTextField();
        final ValidationTextField uvtf3b = new ValidationTextField();
        final ValidationTextField uvtf4c = new ValidationTextField();

        final JButton okButton = new JButton( "OK" );
        JButton cancelButton = new JButton( "Cancel" );
        Dimension btnSize = Utils.getMaxComponentSize( new Component[] {
            okButton, cancelButton } );

        ITextValidator tvA = new IntegerValidator( 0, 25 );

        ITextValidator tvB = new IntegerValidator( 1, 24 );

        ITextValidator tvC = new ExactTextValidator( "e pluribus unum" );

        IValidityChangedListener vcl = new IValidityChangedListener()
        {
            @Override
            public void signalValid()
            {
                boolean newValidity = uvtf1a.isValid() && uvtf2a.isValid() &&
                    uvtf3b.isValid() && uvtf4c.isValid();
                okButton.setEnabled( newValidity );
            }

            @Override
            public void signalInvalid( String reason )
            {
                boolean newValidity = uvtf1a.isValid() && uvtf2a.isValid() &&
                    uvtf3b.isValid() && uvtf4c.isValid();
                okButton.setEnabled( newValidity );
            }
        };

        ActionListener buttonListener = new ExitListener( frame );

        panel.setLayout( new GridBagLayout() );

        okButton.setPreferredSize( btnSize );
        okButton.addActionListener( buttonListener );
        okButton.setEnabled( false );
        cancelButton.setPreferredSize( btnSize );
        cancelButton.addActionListener( buttonListener );

        uvtf1a.setValidator( tvA );
        uvtf1a.addValidityChanged( vcl );

        uvtf2a.setValidator( tvA );
        uvtf2a.addValidityChanged( vcl );

        uvtf3b.setValidator( tvB );
        uvtf3b.addValidityChanged( vcl );

        uvtf4c.setValidator( tvC );
        uvtf4c.addValidityChanged( vcl );

        panel.add( jl1, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        panel.add( uvtf1a.getView(), new GridBagConstraints( 1, 0, 4, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        panel.add( jl2, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        panel.add( uvtf2a.getView(), new GridBagConstraints( 1, 1, 4, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        panel.add( jl3, new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        panel.add( uvtf3b.getView(), new GridBagConstraints( 1, 2, 4, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        panel.add( jl4, new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        panel.add( uvtf4c.getView(), new GridBagConstraints( 1, 3, 4, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        panel.add( Box.createHorizontalStrut( 0 ), new GridBagConstraints( 1,
            4, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.NONE, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        panel.add( okButton, new GridBagConstraints( 2, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        panel.add( cancelButton, new GridBagConstraints( 3, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );

        frame.setContentPane( panel );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 200 );
        frame.validate();
        frame.setMinimumSize( frame.getSize() );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    public static class ExactTextValidator implements ITextValidator
    {
        private final String text;

        public ExactTextValidator( String text )
        {
            this.text = text;
        }

        @Override
        public void validateText( String text ) throws ValidationException
        {
            if( text.compareTo( this.text ) != 0 )
            {
                throw new ValidationException( text + " does not equal " +
                    this.text );
            }
        }
    }

    public static class IntegerValidator implements ITextValidator
    {
        private final Integer min;
        private final Integer max;

        public IntegerValidator()
        {
            this( null, null );
        }

        public IntegerValidator( Integer min, Integer max )
        {
            this.min = min;
            this.max = max;
        }

        public void validateText( String text ) throws ValidationException
        {
            int i = 0;
            try
            {
                i = Integer.parseInt( text );

                if( min != null && i < min )
                {
                    throw new ValidationException( "Value less than minimum: " +
                        i + " < " + min );
                }
                if( max != null && i > max )
                {
                    throw new ValidationException(
                        "Value greater than maximum: " + i + max );
                }
            }
            catch( NumberFormatException ex )
            {
                throw new ValidationException( ex.getMessage() );
            }
        }
    }

    private static class ExitListener implements ActionListener
    {
        private final JFrame frame;

        public ExitListener( JFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            frame.dispose();
        }
    }
}
