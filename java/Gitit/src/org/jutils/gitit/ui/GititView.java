package org.jutils.gitit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jutils.SwingUtils;
import org.jutils.gitit.data.GititConfig;
import org.jutils.gitit.data.GititConfig.GititCommand;
import org.jutils.ui.ComponentView;
import org.jutils.ui.model.IDataView;

/***************************************************************************
 *
 **************************************************************************/
public class GititView implements IDataView<GititConfig>
{
    /**  */
    private final ComponentView view;

    /**  */
    private GititConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public GititView()
    {
        this.view = new ComponentView();

        setData( new GititConfig() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public GititConfig getData()
    {
        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( GititConfig config )
    {
        this.config = config;

        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        Color primColor = Color.white;
        Color altColor = new Color( 0x5B9BD5 );

        int c = 0;
        int r = 0;

        for( File dir : config.directories )
        {
            c = 0;

            Color rowColor = r % 2 == 0 ? primColor : altColor;

            JLabel dirLabel = new JLabel( dir.getName() );
            dirLabel.setFont( dirLabel.getFont().deriveFont( 14.0f ) );

            constraints = new GridBagConstraints( c, r, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( wrap( dirLabel, rowColor ), constraints );

            c++;

            for( GititCommand cmd : config.commands )
            {
                JButton cmdButton = new JButton( cmd.name );

                cmdButton.addActionListener( ( e ) -> runCommand( dir, cmd ) );
                cmdButton.setFont(
                    cmdButton.getFont().deriveFont( Font.BOLD ) );

                constraints = new GridBagConstraints( c, r, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 0, 0, 0, 0 ), 10, 8 );
                panel.add( wrap( cmdButton, rowColor ), constraints );

                c++;
            }

            r++;
        }

        constraints = new GridBagConstraints( c, r, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        view.setComponent( panel );
    }

    /**
     * @param dir
     * @param cmd
     */
    private void runCommand( File dir, GititCommand cmd )
    {
        ProcessBuilder pb = new ProcessBuilder( "cmd", "/C", cmd.command );

        pb.directory( dir );

        try
        {
            pb.start();
        }
        catch( IOException ex )
        {
            SwingUtils.showErrorMessage( getView(),
                "Unable to start " + cmd.command, "Error Starting Command" );
        }
    }

    /**
     * @param comp
     * @param c
     * @return
     */
    private static JPanel wrap( JComponent comp, Color c )
    {
        JPanel p = new JPanel( new BorderLayout() );

        p.setBorder( new EmptyBorder( 8, 8, 8, 8 ) );
        p.add( comp, BorderLayout.CENTER );

        p.setOpaque( true );
        p.setBackground( c );

        return p;
    }
}
