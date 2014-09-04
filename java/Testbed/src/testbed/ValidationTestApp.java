package testbed;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.ui.ExitListener;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.fields.ValidationTextField;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.ITextValidator;

public class ValidationTestApp implements IFrameApp
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new ValidationTestApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        StandardFormView form = new StandardFormView();

        final ValidationTextField field1 = new ValidationTextField();
        final ValidationTextField field2 = new ValidationTextField();
        final ValidationTextField field3 = new ValidationTextField();
        final ValidationTextField field4 = new ValidationTextField();

        final JButton okButton = new JButton( "OK" );
        JButton cancelButton = new JButton( "Cancel" );
        Dimension btnSize = Utils.getMaxComponentSize( okButton, cancelButton );

        ITextValidator tvA = new IntegerValidator( 0, 25 );
        ITextValidator tvB = new IntegerValidator( 1, 24 );
        ITextValidator tvC = new ExactTextValidator( "e pluribus unum" );

        AggregateValidityChangedManager manager = new AggregateValidityChangedManager();

        manager.addValidityChanged( new ValidityChanged( okButton ) );

        manager.addField( field1 );
        manager.addField( field2 );
        manager.addField( field3 );
        manager.addField( field4 );

        ActionListener buttonListener = new ExitListener( frame );

        form.setHorizontalStretch( true );

        form.addField( "Number, (0-25)", field1.getView() );
        form.addField( "Number, (0-25)", field2.getView() );
        form.addField( "Number, [0-25]", field3.getView() );
        form.addField( "The phrase 'e pluribus unum'", field4.getView() );

        panel.setLayout( new GridBagLayout() );

        okButton.setPreferredSize( btnSize );
        okButton.addActionListener( buttonListener );
        okButton.setEnabled( false );
        cancelButton.setPreferredSize( btnSize );
        cancelButton.addActionListener( buttonListener );

        field1.setValidator( tvA );
        field2.setValidator( tvA );
        field3.setValidator( tvB );
        field4.setValidator( tvC );

        panel.add( form.getView(), new GridBagConstraints( 0, 0, 3, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 8, 8, 8 ), 0, 0 ) );

        panel.add( Box.createHorizontalStrut( 0 ), new GridBagConstraints( 0,
            1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
            GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        panel.add( okButton, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 0, 0,
                8, 8 ), 0, 0 ) );
        panel.add( cancelButton, new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 0,
                8, 8 ), 0, 0 ) );

        frame.setContentPane( panel );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.pack();
        frame.setMinimumSize( frame.getSize() );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        frame.getRootPane().setDefaultButton( okButton );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValidityChanged implements IValidityChangedListener
    {
        private final JButton button;

        public ValidityChanged( JButton button )
        {
            this.button = button;
        }

        @Override
        public void signalValid()
        {
            button.setToolTipText( null );
            button.setEnabled( true );
        }

        @Override
        public void signalInvalid( String reason )
        {
            button.setToolTipText( reason );
            button.setEnabled( false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ExactTextValidator implements ITextValidator
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class IntegerValidator implements ITextValidator
    {
        private final Integer min;
        private final Integer max;

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
}
