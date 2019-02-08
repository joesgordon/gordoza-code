package org.jutils.ui.net;

import java.awt.Component;

import javax.swing.JEditorPane;

import org.jutils.SwingUtils;
import org.jutils.io.IStringWriter;
import org.jutils.net.NetMessage;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class MsgStringView implements IDataView<NetMessage>
{
    /** The field for the string representation of the message content. */
    private final JEditorPane editor;
    /**  */
    private final IStringWriter<NetMessage> writer;

    /**  */
    private NetMessage msg;

    /**
     * @param writer
     */
    public MsgStringView( IStringWriter<NetMessage> writer )
    {
        this.writer = writer;
        this.editor = new JEditorPane();

        editor.setEditable( false );
        editor.setFont( SwingUtils.getFixedFont( 12 ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getView()
    {
        return editor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetMessage getData()
    {
        return msg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData( NetMessage msg )
    {
        this.msg = msg;

        editor.setText( writer.toString( msg ) );
        editor.setCaretPosition( 0 );
    }
}
