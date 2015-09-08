package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ExtensiveErrorView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel messageField;
    /**  */
    private final AltEditorPane errorsField;

    /***************************************************************************
     * 
     **************************************************************************/
    public ExtensiveErrorView()
    {
        this.messageField = new JLabel();
        this.errorsField = new AltEditorPane();

        view = createView();

        JTextField textField = new JTextField();

        textField.setEditable( false );
        errorsField.setBackground( textField.getBackground() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JScrollPane errorsPane = new JScrollPane( errorsField );
        GridBagConstraints constraints;

        Dimension dim = new Dimension( 600, 200 );
        errorsPane.setPreferredSize( dim );
        errorsPane.setMinimumSize( dim );
        errorsPane.setMaximumSize( dim );

        errorsField.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
        errorsField.setEditable( false );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( messageField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 4, 4, 4 ), 0, 0 );
        panel.add( errorsPane, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param message
     * @param errors
     **************************************************************************/
    public void setErrors( String message, String errors )
    {
        messageField.setText( message );
        errorsField.setText( errors );
    }

    /***************************************************************************
     * @param message
     * @param errors
     **************************************************************************/
    public static void invokeError( String title, String message,
        String errors )
    {
        IApplication app = new ExtErrorApp( title, message, errors );
        AppRunner.invokeLater( app );
    }

    /***************************************************************************
     * @param message
     * @param errors
     **************************************************************************/
    public static void invokeErrorAndWait( String title, String message,
        String errors )
    {
        IApplication app = new ExtErrorApp( title, message, errors );
        AppRunner.invokeAndWait( app );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ExtErrorApp implements IApplication
    {
        private final String title;
        private final String message;
        private final String errors;

        public ExtErrorApp( String title, String message, String errors )
        {
            this.title = title;
            this.message = message;
            this.errors = errors;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            ExtensiveErrorView view = new ExtensiveErrorView();
            OkDialogView dialogView = new OkDialogView( null, view.getView() );

            view.setErrors( message, errors );

            JDialog dialog = dialogView.getView();

            dialog.setTitle( title );
            dialog.setSize( 600, 600 );
            dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
            dialog.validate();
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
            dialog.toFront();
        }
    }
}
