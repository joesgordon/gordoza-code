package org.jutils.ui;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jutils.Utils;
import org.jutils.io.RuntimeFormatException;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
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

        StandardFormView form = new StandardFormView();

        form.setHorizontalStretch( true );
        form.addField( "Message", messageField );
        form.addField( "Stack Trace", tracePane );

        titlePanel.setComponent( form.getView() );
    }

    public void setData( Throwable th )
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

    public static void showExceptionDialog( Component parent, String title,
        Throwable th )
    {
        showExceptionDialog( parent, null, title, th );
    }

    public static void showExceptionDialog( Component parent, String message,
        String title, Throwable th )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        ExceptionView view = new ExceptionView();
        int row = 0;
        GridBagConstraints constraints;

        if( message != null )
        {
            JTextField label = new JTextField( message, 20 );
            label.setEditable( false );
            label.setBorder( null );
            constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 8, 0 ), 0, 0 );
            panel.add( label, constraints );
        }

        view.setData( th );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( view.getView(), constraints );

        JOptionPane.showMessageDialog( parent, panel, title,
            JOptionPane.ERROR_MESSAGE );
    }

    public static void invokeLater( Throwable ex, String title, String message )
    {
        IApplication app = new DefaultApp( ex, title, message );

        SwingUtilities.invokeLater( new AppRunner( app ) );
    }

    public static void invokeAndWait( Throwable ex, String title, String message )
        throws InvocationTargetException, InterruptedException
    {
        IApplication app = new DefaultApp( ex, title, message );

        SwingUtilities.invokeAndWait( new AppRunner( app ) );
    }

    public static void main( String [] args )
    {
        RuntimeFormatException ex = new RuntimeFormatException(
            "Wrong, three tries for a quarter." );

        invokeLater( ex, "WRONG!", "It didn't work" );
    }

    private static class DefaultApp implements IApplication
    {
        private final Throwable ex;
        private final String title;
        private final String message;

        public DefaultApp( Throwable ex, String title, String message )
        {
            this.ex = ex;
            this.title = title;
            this.message = message;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            showExceptionDialog( null, title, message, ex );
        }
    }
}
