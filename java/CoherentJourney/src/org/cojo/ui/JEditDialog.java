package org.cojo.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IView;

import com.jgoodies.forms.builder.ButtonBarBuilder;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JEditDialog implements IView<JDialog>
{
    /**  */
    private final JDialog dialog;
    /**  */
    private final JPanel mainPanel;
    /**  */
    private final Container contentPane;

    /***************************************************************************
     * @param parent
     * @param contentPane
     **************************************************************************/
    public JEditDialog( Frame parent, Container contentPane )
    {
        this.dialog = new JDialog( parent, true );
        this.mainPanel = new JPanel( new GridBagLayout() );
        this.contentPane = contentPane;

        dialog.setContentPane( createMainPanel() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        mainPanel.add( contentPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        mainPanel.add( createButtonPanel(),
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 10, 0, 10, 0 ), 0, 0 ) );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createButtonPanel()
    {
        ButtonBarBuilder bbb = new ButtonBarBuilder();

        JButton okButton = new JButton( "Ok" );
        JButton cancelButton = new JButton( "Cancel" );

        okButton.addActionListener( ( e ) -> dialog.dispose() );

        cancelButton.addActionListener( ( e ) -> dialog.dispose() );

        bbb.addGlue();
        bbb.addButton( okButton );
        bbb.addRelatedGap();
        bbb.addButton( cancelButton );
        bbb.addGlue();

        return bbb.getPanel();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JDialog getView()
    {
        return dialog;
    }

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public void setSize( int width, int height )
    {
        dialog.setSize( width, height );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void validate()
    {
        dialog.validate();
    }

    /***************************************************************************
     * @param c
     **************************************************************************/
    public void setLocationRelativeTo( Component c )
    {
        dialog.setLocationRelativeTo( c );
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        dialog.setVisible( visible );
    }
}
