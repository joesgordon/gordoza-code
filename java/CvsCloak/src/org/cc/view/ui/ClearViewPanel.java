package org.cc.view.ui;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import org.cc.data.OpenTask;
import org.cc.data.VersioningSystem;
import org.cc.model.ICloak;

public class ClearViewPanel extends JPanel
{
    /**  */
    private SandboxPanel vsPanel;
    /**  */
    private JLabel taskNameField;
    /**  */
    private JLabel authField;
    /**  */
    private ICloak cloak;

    public ClearViewPanel( ICloak cloak )
    {
        super( new GridBagLayout() );

        this.cloak = cloak;

        vsPanel = new SandboxPanel( cloak );

        add( createInfoPanel(), new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 2, 0 ), 0, 0 ) );
        add( vsPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2,
                0, 0, 0 ), 0, 0 ) );
    }

    private JPanel createInfoPanel()
    {
        JPanel infoPanel = new JPanel( new GridBagLayout() );
        JLabel taskNameLabel = new JLabel( "Task Name :" );
        taskNameField = new JLabel();
        JLabel authLabel = new JLabel( "Authorized :" );
        authField = new JLabel();

        infoPanel.add( taskNameLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(
                0, 0, 2, 2 ), 0, 0 ) );
        infoPanel.add( taskNameField, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                2, 2, 0, 0 ), 0, 0 ) );

        infoPanel.add( authLabel, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets( 0, 0,
                2, 2 ), 0, 0 ) );
        infoPanel.add( authField, new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 2, 2,
                0, 0 ), 0, 0 ) );

        infoPanel.add( Box.createHorizontalStrut( 0 ), new GridBagConstraints(
            15, 0, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
            GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return infoPanel;
    }

    public void setSandbox( File sb )
    {
        VersioningSystem vs = cloak.getVersioningSystem();
        OpenTask ot = vs.getOpenTaskBySandbox( sb );

        taskNameField.setText( ot == null ? "N/A" : ot.getName() );
        authField.setText( ot == null ? "N/A" : ot.isApproved() ? "yes" : "no" );

        vsPanel.setSandbox( sb );
    }
}
