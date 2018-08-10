package org.mc.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;

import org.jutils.ui.model.IView;

/**
 *
 */
public class AutoReplayPanel implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JCheckBox autoEnabledCheckbox;
    /**  */
    private final MessageTextView msgField;

    /***************************************************************************
     * 
     **************************************************************************/
    public AutoReplayPanel()
    {
        this.autoEnabledCheckbox = new JCheckBox();
        this.msgField = new MessageTextView();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( msgField.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        autoEnabledCheckbox.setText( "Auto-Reply" );

        toolbar.add( autoEnabledCheckbox );

        return toolbar;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        autoEnabledCheckbox.setEnabled( editable );
        msgField.setEditable( editable );
    }
}
