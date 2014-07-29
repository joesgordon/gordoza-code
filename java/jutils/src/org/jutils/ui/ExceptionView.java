package org.jutils.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.Utils;
import org.jutils.ui.model.IView;

public class ExceptionView implements IView<JComponent>
{
    private final TitleView titlePanel;
    private final JTextField messageField;
    private final JTextArea stacktraceField;

    public ExceptionView()
    {
        this.titlePanel = new TitleView();
        this.messageField = new JTextField( 20 );
        this.stacktraceField = new JTextArea();

        messageField.setEditable( false );
        stacktraceField.setEditable( false );

        stacktraceField.setLineWrap( false );

        JScrollPane tracePane = new JScrollPane( stacktraceField );
        tracePane.setBorder( new LineBorder( Color.gray ) );
        tracePane.setPreferredSize( new Dimension( 400, 200 ) );
        tracePane.setMinimumSize( new Dimension( 400, 200 ) );

        StandardFormView form = new StandardFormView();

        form.setHorizontalStretch( true );
        form.addField( "Message", messageField );
        form.addField( "Stack Trace", tracePane );

        titlePanel.setComponent( form.getView() );
    }

    public void setException( Throwable th )
    {
        titlePanel.setTitle( th.getClass().getName() );
        messageField.setText( th.getMessage() );
        stacktraceField.setText( Utils.printStackTrace( th ) );
    }

    @Override
    public JComponent getView()
    {
        return titlePanel.getView();
    }
}
