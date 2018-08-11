package org.mc.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.net.IConnection;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.updater.CheckBoxUpdater;
import org.jutils.ui.fields.DoubleFormField;
import org.jutils.ui.model.IView;
import org.mc.MsgScheduleTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MessageInputPanel implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final MessageTextView adHocView;
    /**  */
    private final MessageTextView scheduleView;
    /**  */
    private final MessageTextView autoReplyView;

    /**  */
    private final JCheckBox scheduleField;
    /**  */
    private final DoubleFormField rateField;
    /**  */
    private final JCheckBox autoEnabledCheckbox;

    /**  */
    private IConnection connection;
    /**  */
    private MsgScheduleTask task;

    /***************************************************************************
     * 
     **************************************************************************/
    public MessageInputPanel()
    {
        this.scheduleField = new JCheckBox( "Schedule Messages" );
        this.rateField = new DoubleFormField( "Message Rate", "Hz", 4, null,
            1.0, 1000.0 );
        this.autoEnabledCheckbox = new JCheckBox( "Auto-Reply" );

        this.adHocView = new MessageTextView();
        this.scheduleView = new MessageTextView();
        this.autoReplyView = new MessageTextView();

        this.view = createView();

        this.connection = null;
        this.task = null;

        adHocView.setText( "Send Me" );
        scheduleView.setText( "Schedule Me" );
        autoReplyView.setText( "Reply with Me" );

        adHocView.addEnterListener( ( e ) -> sendAdHoc( e.getItem() ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        TitleView titlePanel = new TitleView( "Send/Receive Messages", panel );

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Ad Hoc", adHocView.getView() );
        tabs.addTab( "Scheduled", scheduleView.getView() );
        tabs.addTab( "Auto-Reply", autoReplyView.getView() );

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

        scheduleField.addActionListener(
            new CheckBoxUpdater( ( b ) -> scheduleMessages( b ) ) );

        rateField.setValue( 5.0 );
        rateField.getView().setMaximumSize(
            rateField.getView().getPreferredSize() );

        toolbar.add( scheduleField );

        toolbar.addSeparator();
        toolbar.add( rateField.getView() );

        toolbar.addSeparator();
        toolbar.add( autoEnabledCheckbox );

        return toolbar;
    }

    /***************************************************************************
     * @param scheduleMessages
     **************************************************************************/
    private void scheduleMessages( boolean scheduleMessages )
    {
        if( connection != null )
        {
            stopScheduled();

            if( scheduleMessages )
            {
                startScheduled();
            }
        }
    }

    private void startScheduled()
    {
        task = new MsgScheduleTask( rateField.getValue(),
            scheduleView.getData(), connection );
    }

    private void stopScheduled()
    {
        if( task != null )
        {
            task.stop();
            task = null;
            scheduleField.setSelected( false );
        }
    }

    /***************************************************************************
     * @param msgBytes
     **************************************************************************/
    private void sendAdHoc( byte[] msgBytes )
    {
        if( connection != null )
        {
            try
            {
                connection.sendMessage( msgBytes );
            }
            catch( IOException ex )
            {
                SwingUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Error sending message" );
            }
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
     * @param connection
     **************************************************************************/
    public void setConnection( IConnection connection )
    {
        closeConnection();
        this.connection = connection;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void closeConnection()
    {
        if( this.connection != null )
        {
            IConnection connection = this.connection;
            this.connection = null;

            try
            {
                connection.close();
            }
            catch( IOException ex )
            {
                SwingUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Error closing connection" );
            }
        }

        stopScheduled();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getMessageText()
    {
        return adHocView.getData();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setMessageText( byte[] text )
    {
        adHocView.setData( text );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        scheduleField.setEnabled( editable );
        rateField.setEditable( editable );
        autoEnabledCheckbox.setEnabled( editable );

        adHocView.setEditable( editable );
        scheduleView.setEditable( editable );
        autoReplyView.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        adHocView.selectAll();
    }
}
