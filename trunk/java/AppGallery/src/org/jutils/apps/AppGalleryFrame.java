package org.jutils.apps;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.apps.filespy.FileSpyMain;
import org.jutils.apps.jexplorer.JExplorerMain;
import org.jutils.apps.jhex.JHexMain;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * This class defines the frame that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class AppGalleryFrame extends JFrame
{
    /***************************************************************************
     * Creates a new AppGallery frame.
     **************************************************************************/
    public AppGalleryFrame()
    {
        int appGap = 40;
        int compGap = 10;
        JPanel contentPanel = ( JPanel )this.getContentPane();
        JButton fileSpyButton = new JButton();
        JButton jhexButton = new JButton();
        JButton jexplorerButton = new JButton();

        contentPanel.setLayout( new GridBagLayout() );

        fileSpyButton.setText( "Launch FileSpy" );
        fileSpyButton.setIcon( IconConstants.getIcon( IconConstants.PAGEMAG_32 ) );
        fileSpyButton.setHorizontalAlignment( SwingConstants.LEFT );
        fileSpyButton.addActionListener( new FileSpyButtonListener() );

        jhexButton.setText( "Launch JHex" );
        jhexButton.setIcon( IconConstants.getIcon( IconConstants.BINARY_32 ) );
        jhexButton.setHorizontalAlignment( SwingConstants.LEFT );
        jhexButton.addActionListener( new JHexButtonListener() );

        jexplorerButton.setText( "Launch JExplorer" );
        jexplorerButton.setIcon( IconConstants.getIcon( IconConstants.OPEN_FOLDER_32 ) );
        jexplorerButton.setHorizontalAlignment( SwingConstants.LEFT );
        jexplorerButton.addActionListener( new JExplorerButtonListener() );

        contentPanel.add( fileSpyButton, new GridBagConstraints( 0, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2 * compGap, appGap, compGap, appGap ), 0, 10 ) );

        contentPanel.add( jhexButton, new GridBagConstraints( 0, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( compGap, appGap, compGap, appGap ), 0, 10 ) );

        contentPanel.add( jexplorerButton, new GridBagConstraints( 0, 2, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( compGap, appGap, 2 * compGap, appGap ), 0, 10 ) );

        this.setTitle( "JUtils Application Gallery" );
    }

    private static class FileSpyButtonListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            FrameRunner r = new FileSpyMain();
            r.run();
            r.getFrame().setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        }
    }

    private static class JExplorerButtonListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JExplorerMain r = new JExplorerMain();
            r.run();
            r.getFrame().setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        }
    }

    private static class JHexButtonListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            JHexMain r = new JHexMain();
            r.run();
            r.getFrame().setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        }
    }
}
