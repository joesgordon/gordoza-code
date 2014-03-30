package org.jutils.apps.filespy.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.licensing.LicenseDialog;
import org.jutils.ui.*;
import org.jutils.ui.explorer.FileConfigurationDialog;
import org.jutils.ui.model.IView;

import com.jgoodies.looks.Options;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyFrameView implements IView<JFrame>
{
    /**  */
    private final SearchView spyPanel;
    /**  */
    private final JFrame frame;
    /**  */
    private final UserOptionsSerializer<FileSpyData> userio;

    /***************************************************************************
     *
     **************************************************************************/
    public FileSpyFrameView( UserOptionsSerializer<FileSpyData> userio )
    {
        StatusBarPanel statusBar = new StatusBarPanel();

        this.userio = userio;
        this.frame = new JFrame();
        this.spyPanel = new SearchView( statusBar, userio );

        JPanel contentPane = new JPanel( new BorderLayout() );

        spyPanel.setData( null );
        statusBar.setText( "" );

        contentPane.add( createToolBar(), BorderLayout.NORTH );
        contentPane.add( spyPanel.getView(), java.awt.BorderLayout.CENTER );
        contentPane.add( statusBar.getView(), BorderLayout.SOUTH );

        frame.setJMenuBar( createMenuBar() );
        frame.setSize( new Dimension( 725, 500 ) );
        frame.setTitle( "FileSpy" );

        frame.setIconImages( IconConstants.loader.getImages(
            IconConstants.PAGEMAG_16, IconConstants.PAGEMAG_32,
            IconConstants.PAGEMAG_64, IconConstants.PAGEMAG_128 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.setContentPane( contentPane );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JButton newButton = new JButton();
        newButton.setIcon( IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        newButton.setToolTipText( "Creates a new search tab." );
        newButton.addActionListener( new NewListener( spyPanel ) );
        newButton.setFocusable( false );

        JButton openButton = new JButton();
        openButton.setIcon( IconConstants.loader.getIcon( IconConstants.OPEN_FILE_16 ) );
        openButton.setToolTipText( "Opens a previously saved search tab." );
        openButton.addActionListener( new OpenListener() );
        openButton.setFocusable( false );

        JButton saveButton = new JButton();
        saveButton.setIcon( IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        saveButton.setToolTipText( "Saves search tab." );
        saveButton.addActionListener( new SaveListener() );
        saveButton.setFocusable( false );

        JToolBar toolbar = new JGoodiesToolBar();
        toolbar.add( newButton );
        toolbar.add( openButton );
        toolbar.add( saveButton );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JGoodiesMenuBar();
        menuBar.add( createFileMenu() );
        menuBar.add( createViewMenu() );
        menuBar.add( createToolsMenu() );
        menuBar.add( createHelpMenu() );

        return menuBar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createFileMenu()
    {
        JMenu fileMenu = new JMenu( "File" );

        JMenuItem newMenuItem = new JMenuItem( "New" );
        newMenuItem.setToolTipText( "Creates a new search tab." );
        newMenuItem.addActionListener( new NewListener( spyPanel ) );
        newMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );

        JMenuItem openMenuItem = new JMenuItem( "Open" );
        openMenuItem.setToolTipText( "Opens a previously saved search tab." );
        openMenuItem.addActionListener( new OpenListener() );
        openMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.OPEN_FILE_16 ) );

        JMenuItem saveMenuItem = new JMenuItem( "Save" );
        saveMenuItem.setToolTipText( "Saves search tab." );
        saveMenuItem.addActionListener( new SaveListener() );
        saveMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );

        JMenuItem exitMenuItem = new JMenuItem();
        exitMenuItem.setText( "Exit" );
        exitMenuItem.setToolTipText( "Exits the application." );
        exitMenuItem.addActionListener( new ExitListener( frame ) );
        exitMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.CLOSE_16 ) );

        fileMenu.add( newMenuItem );
        fileMenu.add( openMenuItem );
        fileMenu.add( saveMenuItem );
        fileMenu.add( exitMenuItem );

        return fileMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createViewMenu()
    {
        JMenu viewMenu = new JMenu( "View" );

        JMenuItem menuItem;

        menuItem = new JMenuItem( "Plastic" );
        menuItem.addActionListener( new SetLafListener( Options.PLASTIC_NAME ) );
        viewMenu.add( menuItem );

        menuItem = new JMenuItem( "Plastic 3D" );
        menuItem.addActionListener( new SetLafListener( Options.PLASTIC3D_NAME ) );
        viewMenu.add( menuItem );

        menuItem = new JMenuItem( "Plastic XP" );
        menuItem.addActionListener( new SetLafListener( Options.PLASTICXP_NAME ) );
        viewMenu.add( menuItem );

        viewMenu.addSeparator();

        menuItem = new JMenuItem( "Nimbus" );
        menuItem.addActionListener( new SetLafListener(
            "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" ) );
        viewMenu.add( menuItem );

        return viewMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createToolsMenu()
    {
        JMenu toolsMenu = new JMenu( "Tools" );

        JMenuItem regexMenuItem = new JMenuItem( "Test RegEx" );
        regexMenuItem.setToolTipText( "Allows you to test a regular "
            + "expression." );
        regexMenuItem.addActionListener( new RegexListener() );

        JMenuItem fileOptionsMenuItem = new JMenuItem( "File Options" );
        fileOptionsMenuItem.setIcon( IconConstants.loader.getIcon( IconConstants.EDIT_16 ) );
        fileOptionsMenuItem.addActionListener( new OptionsListener() );

        toolsMenu.add( regexMenuItem );
        toolsMenu.add( fileOptionsMenuItem );

        return toolsMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createHelpMenu()
    {
        JMenu helpMenu = new JMenu( "Help" );

        JMenuItem aboutMenuItem = new JMenuItem( "About" );
        aboutMenuItem.setToolTipText( "Presents infomation about this program." );
        aboutMenuItem.addActionListener( new AboutListener( frame ) );

        helpMenu.add( aboutMenuItem );

        return helpMenu;
    }

    /***************************************************************************
     * @param laf
     **************************************************************************/
    private void resetLaf( String laf )
    {
        try
        {
            UIManager.setLookAndFeel( laf );
            SwingUtilities.updateComponentTreeUI( frame );
            frame.validate();
        }
        catch( Exception ex )
        {
            JOptionPane.showMessageDialog(
                frame,
                "Unable to set the look and feel to " + laf + ". " +
                    ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void openSearchParams()
    {
        SearchParams params = null;
        JFileChooser chooser = new JFileChooser();
        int result = JFileChooser.ERROR_OPTION;
        File fileChosen = null;
        FileSpyData configData = userio.getOptions();

        chooser.setCurrentDirectory( configData.lastSavedLocation );
        chooser.setAcceptAllFileFilterUsed( false );
        chooser.setFileFilter( new FileSpySearchFilter() );
        chooser.setDialogTitle( "Open Search File" );

        result = chooser.showOpenDialog( frame );
        if( result == JFileChooser.APPROVE_OPTION )
        {
            userio.write();

            fileChosen = chooser.getSelectedFile();
            if( fileChosen != null && fileChosen.isFile() )
            {
                try
                {
                    params = ( SearchParams )SearchParams.read( fileChosen );
                    params.name = fileChosen.getName().substring(
                        0,
                        fileChosen.getName().length() -
                            FileSpySearchFilter.FILESPY_SEARCH_EXT.length() );
                    spyPanel.setData( params );
                }
                catch( IOException ex )
                {
                    JOptionPane.showMessageDialog( frame, ex.getMessage(),
                        "I/O ERROR", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void saveSearchParams()
    {
        SearchParams params = spyPanel.getData();
        JFileChooser chooser = new JFileChooser();
        int result = JFileChooser.ERROR_OPTION;
        File fileChosen = null;
        FileSpyData configData = userio.getOptions();

        chooser.setCurrentDirectory( configData.lastSavedLocation );
        chooser.setAcceptAllFileFilterUsed( false );
        chooser.setFileFilter( new FileSpySearchFilter() );
        chooser.setDialogTitle( "Save As" );

        result = chooser.showSaveDialog( frame );
        if( result == JFileChooser.APPROVE_OPTION )
        {
            userio.write();

            fileChosen = chooser.getSelectedFile();
            if( !fileChosen.getName().endsWith(
                FileSpySearchFilter.FILESPY_SEARCH_EXT ) )
            {
                fileChosen = new File( fileChosen.getParentFile(),
                    fileChosen.getName() +
                        FileSpySearchFilter.FILESPY_SEARCH_EXT );
            }

            try
            {
                params.write( fileChosen );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( frame, ex.getMessage(),
                    "I/O ERROR", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    private void showRegexHelper()
    {
        JDialog dialog = new JDialog( frame );
        dialog.setTitle( "Regex Friend" );
        dialog.setContentPane( new RegexPanel().getView() );
        dialog.setSize( new Dimension( 500, 500 ) );
        dialog.validate();
        dialog.setLocationRelativeTo( null );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showFileConfig()
    {
        FileConfigurationDialog.showDialog( frame );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NewListener implements ActionListener
    {
        private final SearchView fsPanel;

        public NewListener( SearchView fsPanel )
        {
            this.fsPanel = fsPanel;
        }

        public void actionPerformed( ActionEvent e )
        {
            fsPanel.setData( null );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            openSearchParams();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            saveSearchParams();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class RegexListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            showRegexHelper();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OptionsListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            showFileConfig();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AboutListener implements ActionListener
    {
        private final JFrame frame;

        public AboutListener( JFrame frame )
        {
            this.frame = frame;
        }

        public void actionPerformed( ActionEvent e )
        {
            LicenseDialog d = new LicenseDialog( frame );
            d.setLocationRelativeTo( frame );
            d.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SetLafListener implements ActionListener
    {
        private String laf;

        public SetLafListener( String lookAndFeel )
        {
            laf = lookAndFeel;
        }

        public void actionPerformed( ActionEvent e )
        {
            resetLaf( laf );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileSpySearchFilter extends
        javax.swing.filechooser.FileFilter
    {
        public static final String FILESPY_SEARCH_EXT = ".fss";

        public static final String FILESPY_SEARCH_DESC = "File Spy Search (*" +
            FILESPY_SEARCH_EXT + ")";

        public FileSpySearchFilter()
        {
            ;
        }

        public boolean accept( File file )
        {
            String name = file.getName();
            if( file.isDirectory() || name.endsWith( FILESPY_SEARCH_EXT ) )
            {
                return true;
            }
            return false;
        }

        public String getDescription()
        {
            return FILESPY_SEARCH_DESC;
        }
    }
}
