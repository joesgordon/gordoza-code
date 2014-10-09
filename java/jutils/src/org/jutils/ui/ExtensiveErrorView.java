package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

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
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 4, 4, 4 ), 0, 0 );
        panel.add( messageField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 0, 4,
                4, 4 ), 0, 0 );
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
}
