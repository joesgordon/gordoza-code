package org.cojo.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.jgoodies.forms.builder.ButtonBarBuilder;

public class JEditDialog extends JDialog
{
    private JPanel mainPanel;
    private Container contentPane;

    public JEditDialog( Frame parent, Container contentPane )
    {
        super( parent, true );

        super.setContentPane( createMainPanel() );

        setContentPane( contentPane );
    }

    @Override
    public void setContentPane( Container pane )
    {
        if( contentPane != null )
        {
            mainPanel.remove( contentPane );
        }

        if( pane != null )
        {
            mainPanel.add( pane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                    0, 0, 0, 0 ), 0, 0 ) );
        }

        contentPane = pane;
    }

    @Override
    public Container getContentPane()
    {
        return this.contentPane;
    }

    private JPanel createMainPanel()
    {
        mainPanel = new JPanel( new GridBagLayout() );

        mainPanel.add( createButtonPanel(), new GridBagConstraints( 0, 1, 1, 1,
            0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 10, 0, 10, 0 ), 0, 0 ) );

        return mainPanel;
    }

    private JPanel createButtonPanel()
    {
        ButtonBarBuilder bbb = new ButtonBarBuilder();

        JButton okButton = new JButton( "Ok" );
        JButton cancelButton = new JButton( "Cancel" );

        okButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                dispose();
            }
        } );

        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                dispose();
            }
        } );

        bbb.addGlue();
        bbb.addButton( okButton );
        bbb.addRelatedGap();
        bbb.addButton( cancelButton );
        bbb.addGlue();

        return bbb.getPanel();
    }
}
