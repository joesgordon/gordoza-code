package org.jutils.chart.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.chart.model.TextLabel;
import org.jutils.ui.ColorButtonView;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.event.*;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.validation.UpdaterItemListener;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextLabelField implements IDataFormField<TextLabel>
{
    /**  */
    private final String name;
    /**  */
    private final JPanel view;

    /**  */
    private final JCheckBox visibleField;
    /**  */
    private final ValidationTextField textField;
    /**  */
    private final Action fontAction;
    /**  */
    private final ColorButtonView colorView;

    /**  */
    private TextLabel label;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public TextLabelField( String name )
    {
        this.name = name;

        this.visibleField = new JCheckBox();
        this.textField = new ValidationTextField();
        this.fontAction = createFontAction();
        this.colorView = new ColorButtonView( Color.red, 16, false );

        this.view = createView();

        setValue( new TextLabel() );

        ITextValidator itv;

        itv = new DataTextValidator<>( new StringLengthValidator( 0, null ),
            new ReflectiveUpdater<String>( this, "label.text" ) );

        textField.setValidator( itv );
        colorView.addUpdateListener( new UpdaterItemListener<>(
            new ReflectiveUpdater<Color>( this, "label.color" ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 0, 2 ), 0, 0 );
        panel.add( visibleField, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 2, 0, 2 ), 0, 0 );
        panel.add( textField.getView(), constraints );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                2, 0, 2 ), 0, 0 );
        panel.add( new JButton( fontAction ), constraints );

        constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                2, 0, 0 ), 0, 0 );
        panel.add( colorView.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createFontAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = null;
        icon = IconConstants.loader.getIcon( IconConstants.FONT_16 );
        listener = new FontListener( this );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFieldName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getField()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public TextLabel getValue()
    {
        return label;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( TextLabel value )
    {
        this.label = value;

        visibleField.setSelected( value.visible );
        textField.setText( value.text );
        textField.getView().setFont( value.font );
        colorView.setData( value.color );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<TextLabel> updater )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<TextLabel> getUpdater()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return textField;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FontListener implements ActionListener,
        ItemActionListener<Boolean>
    {
        private final TextLabelField field;
        private FontView fontView;

        public FontListener( TextLabelField field )
        {
            this.field = field;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            fontView = new FontView();
            OkDialogView okView = new OkDialogView( field.getField(),
                fontView.getView() );
            JDialog dialog = okView.getView();

            fontView.setData( field.label.font );
            okView.addOkListener( this );

            dialog.setTitle( "Choose Font" );
            dialog.pack();
            dialog.setLocationRelativeTo( field.getField() );
            dialog.setVisible( true );
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            if( event.getItem() )
            {
                field.label.font = fontView.getData();
                field.textField.getView().setFont( field.label.font );
                // field.textField.getView().invalidate();
                // field.getField().validate();
                // field.getField().repaint();
            }
        }
    }
}
