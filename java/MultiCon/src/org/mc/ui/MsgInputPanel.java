package org.mc.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsgInputPanel implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final MessageTextView adHocField;
    /**  */
    private final MessageTextView scheduleField;
    /**  */
    private final MessageTextView autoReplyField;

    /**  */
    private final JCheckBox scheduleCheckBox;
    /**  */
    private final JTextField msgCountTextField;
    /**  */
    private final JTextField msgDelayTextField;
    /**  */
    private final JCheckBox autoEnabledCheckbox;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsgInputPanel()
    {
        this.scheduleCheckBox = new JCheckBox( "Schedule Messages" );
        this.msgCountTextField = new JTextField( 4 );
        this.msgDelayTextField = new JTextField( 4 );
        this.autoEnabledCheckbox = new JCheckBox( "Auto-Reply" );

        this.adHocField = new MessageTextView();
        this.scheduleField = new MessageTextView();
        this.autoReplyField = new MessageTextView();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        TitleView titlePanel = new TitleView( "Send/Receive Messages", panel );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Ad Hoc", adHocField.getView() );
        tabs.addTab( "Scheduled", scheduleField.getView() );
        tabs.addTab( "Auto-Reply", autoReplyField.getView() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( tabs, BorderLayout.CENTER );

        return titlePanel.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        msgCountTextField.setMinimumSize(
            msgDelayTextField.getPreferredSize() );
        msgDelayTextField.setMinimumSize(
            msgDelayTextField.getPreferredSize() );

        scheduleCheckBox.addActionListener(
            new CheckBoxUpdater( ( b ) -> scheduleMessages( b ) ) );

        toolbar.add( scheduleCheckBox );

        toolbar.addSeparator();
        toolbar.add( new JLabel( "#" ) );
        toolbar.add( msgCountTextField );

        toolbar.addSeparator();
        toolbar.add( new JLabel( "Delay:" ) );
        toolbar.add( msgDelayTextField );
        toolbar.add( new JLabel( "ms" ) );

        toolbar.addSeparator();
        toolbar.add( autoEnabledCheckbox );

        return toolbar;
    }

    private void scheduleMessages( boolean enable )
    {
        if( enable )
        {
            // MsgScheduleTask task = new MsgScheduleTask();
        }
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
     * @param l
     **************************************************************************/
    public void addSendListener( ItemActionListener<String> l )
    {
        adHocField.addEnterListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getMessageText()
    {
        return adHocField.getData();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setMessageText( byte[] text )
    {
        adHocField.setData( text );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        scheduleCheckBox.setEnabled( editable );
        msgCountTextField.setEditable( editable );
        msgDelayTextField.setEditable( editable );
        autoEnabledCheckbox.setEnabled( editable );

        adHocField.setEditable( editable );
        scheduleField.setEditable( editable );
        autoReplyField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        adHocField.selectAll();
    }
}
