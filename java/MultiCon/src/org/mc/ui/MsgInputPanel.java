package org.mc.ui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.jutils.ui.event.ItemActionListener;
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
    private final ScheduleMsgPanel scheduleField;
    /**  */
    private final AutoReplayPanel autoReplyField;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsgInputPanel()
    {
        this.adHocField = new MessageTextView();
        this.scheduleField = new ScheduleMsgPanel();
        this.autoReplyField = new AutoReplayPanel();

        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Ad Hoc", adHocField.getView() );
        tabs.addTab( "Scheduled", scheduleField.getView() );
        tabs.addTab( "Auto-Reply", autoReplyField.getView() );

        setEditable( false );

        return tabs;
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
