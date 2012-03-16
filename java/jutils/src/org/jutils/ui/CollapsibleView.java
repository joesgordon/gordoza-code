package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;

public class CollapsibleView
{
    private JPanel panel;
    private GradientPanel titlePanel;
    private JLabel titleField;
    private JSeparator separator;
    private JPanel componentPanel;

    public CollapsibleView()
    {
        GridBagConstraints constraints;

        componentPanel = new JPanel( new BorderLayout() );
        panel = new JPanel( new GridBagLayout() );
        titleField = new JLabel( "Title" );
        separator = new JSeparator();
        titlePanel = createTitlePanel();

        // componentPanel.setBorder( BorderFactory.createLineBorder( Color.red )
        // );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( titlePanel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( separator, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( componentPanel, constraints );

        setComponent( Box.createVerticalStrut( 20 ) );

        panel.setBorder( BorderFactory.createLineBorder( Color.gray ) );
    }

    private GradientPanel createTitlePanel()
    {
        GridBagConstraints constraints;
        GradientPanel titlePanel;
        JToggleButton collapseButton = new JToggleButton(
            IconConstants.getIcon( "collapse.png" ) );

        collapseButton.addActionListener( new CollapseButtonListener() );
        collapseButton.setSelectedIcon( IconConstants.getIcon( "expand.png" ) );

        collapseButton.setBorderPainted( false );
        collapseButton.setOpaque( false );

        titlePanel = new GradientPanel( new GridBagLayout(), new Color( 58,
            110, 167 ) );

        Font bold = titleField.getFont().deriveFont( Font.BOLD );
        titleField.setFont( bold );
        titleField.setForeground( Color.white );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 4 ), 0, 0 );
        titlePanel.add( collapseButton, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 4 ), 0, 0 );
        titlePanel.add( titleField, constraints );
        return titlePanel;
    }

    public void setComponent( Component comp )
    {
        componentPanel.removeAll();
        componentPanel.add( comp, BorderLayout.CENTER );
    }

    public JComponent getView()
    {
        return panel;
    }

    private class CollapseButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JToggleButton toggleButton = ( JToggleButton )e.getSource();

            if( toggleButton.isSelected() )
            {
                componentPanel.setVisible( false );
                separator.setVisible( false );
                // componentPanel.removeAll();
                componentPanel.revalidate();
            }
            else
            {
                componentPanel.setVisible( true );
                // setComponent( comp );
                separator.setVisible( true );
                componentPanel.revalidate();
            }
        }
    }
}
