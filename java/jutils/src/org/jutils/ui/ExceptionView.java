package org.jutils.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import org.jutils.Utils;
import org.jutils.ui.model.IView;

public class ExceptionView implements IView<JComponent>
{
    /**  */
    private final TitleView titlePanel;
    /**  */
    private final JTextField messageField;
    /**  */
    private final JTextArea stacktraceField;

    public ExceptionView()
    {
        this.messageField = new JTextField( 20 );
        this.stacktraceField = new JTextArea();
        this.titlePanel = new TitleView( "", createView() );

        messageField.setEditable( false );
        stacktraceField.setEditable( false );

        stacktraceField.setLineWrap( false );
    }

    private Component createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createForm(), BorderLayout.NORTH );
        panel.add( createTracePanel(), BorderLayout.CENTER );

        return panel;
    }

    private Component createTracePanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        JScrollPane tracePane = new JScrollPane( stacktraceField );
        // tracePane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        tracePane.setBorder( new MatteBorder( 1, 0, 0, 0, Color.gray ) );
        tracePane.setPreferredSize( new Dimension( 400, 200 ) );
        tracePane.setMinimumSize( new Dimension( 400, 200 ) );
        tracePane.getVerticalScrollBar().setUnitIncrement( 12 );

        // panel.add( new JSeparator(), BorderLayout.NORTH );
        panel.add( tracePane, BorderLayout.CENTER );

        return panel;
    }

    private Component createForm()
    {
        StandardFormView form = new StandardFormView();

        form.addField( "Message", messageField );

        return form.getView();
    }

    public void setException( Throwable th )
    {
        titlePanel.setTitle( th.getClass().getName() );
        messageField.setText( th.getMessage() );
        stacktraceField.setText( Utils.printStackTrace( th ) );
        stacktraceField.setCaretPosition( 0 );
    }

    @Override
    public JComponent getView()
    {
        return titlePanel.getView();
    }
}
