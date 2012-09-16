package org.cc.view.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.cc.cvs.CvsCloak;
import org.cc.data.OpenTask;
import org.cc.data.VersioningSystem;
import org.cc.edit.ui.CceFrame;
import org.cc.model.ICloak;
import org.cc.view.CvIconLoader;
import org.jutils.IconConstants;
import org.jutils.ui.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ClearViewFrame extends JFrame
{
    /**  */
    private JComboBox pathField;
    /**  */
    private ClearViewPanel cvPanel;
    /**  */
    private JPanel controlsPanel;
    /**  */
    private JPanel emptyPanel;
    /**  */
    private CardPanel viewPanel;
    /**  */
    private JButton assignButton;
    /**  */
    private JButton unassignButton;

    /**  */
    private ICloak cloak;

    /***************************************************************************
     * 
     **************************************************************************/
    public ClearViewFrame()
    {
        super( "ClearView" );

        cloak = new CvsCloak();

        // ---------------------------------------------------------------------
        // Setup content panel
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new BorderLayout() );

        contentPanel.add( createToolBar(), BorderLayout.NORTH );
        contentPanel.add( createCenterPanel( cloak ), BorderLayout.CENTER );
        contentPanel.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        // ---------------------------------------------------------------------
        // Setup frame.
        // ---------------------------------------------------------------------
        setContentPane( contentPanel );
        setJMenuBar( createMenuBar() );
    }

    private JComponent createCenterPanel( ICloak cloak )
    {
        controlsPanel = createControlsPanel( cloak );
        emptyPanel = createEmptyPanel();

        viewPanel = new CardPanel();
        viewPanel.addCard( emptyPanel );
        viewPanel.addCard( controlsPanel );

        return viewPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createEmptyPanel()
    {
        JPanel panel = new JPanel();

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createControlsPanel( ICloak cloak )
    {
        JPanel centerPanel = new JPanel( new GridBagLayout() );

        pathField = new JComboBox();
        pathField.setEditable( false );
        cvPanel = new ClearViewPanel( cloak );

        centerPanel.add( pathField, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 2, 4 ), 0, 0 ) );

        centerPanel.add( cvPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2,
                4, 4, 4 ), 0, 0 ) );

        return centerPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new UMenuBar();

        JMenu fileMenu = new JMenu( "File" );
        JMenuItem exitMenuItem = new JMenuItem( "Exit",
            IconConstants.getIcon( IconConstants.CLOSE_16 ) );

        exitMenuItem.addActionListener( new ExitListener( this ) );

        fileMenu.add( exitMenuItem );

        menubar.add( fileMenu );
        menubar.add( createViewMenu() );

        return menubar;
    }

    private JMenu createViewMenu()
    {
        JMenu viewMenu = new JMenu( "View" );
        JMenuItem menuItem;

        menuItem = new JMenuItem( "Task Info" );
        viewMenu.add( menuItem );

        menuItem = new JMenuItem( "Browse Repository" );
        viewMenu.add( menuItem );

        menuItem = new JMenuItem( "Data Editor" );
        menuItem.addActionListener( new EditorListener() );
        viewMenu.add( menuItem );

        return viewMenu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new UToolBar();

        JButton openButton = new JButton( CvIconLoader.getFileIcon() );
        openButton.addActionListener( new OpenListener() );
        openButton.setFocusable( false );
        openButton.setToolTipText( "Open an existing sandbox" );
        toolbar.add( openButton );

        JButton checkoutButton = new JButton( CvIconLoader.getCheckoutIcon() );
        checkoutButton.addActionListener( new CheckoutListener() );
        checkoutButton.setFocusable( false );
        checkoutButton.setToolTipText( "Checkout a new sandbox" );
        toolbar.add( checkoutButton );

        toolbar.addSeparator();

        assignButton = new JButton( CvIconLoader.getAssignIcon() );
        assignButton.setFocusable( false );
        assignButton.setToolTipText( "Assign a task" );
        assignButton.setEnabled( false );
        toolbar.add( assignButton );

        unassignButton = new JButton( CvIconLoader.getUnassignIcon() );
        unassignButton.setFocusable( false );
        unassignButton.setToolTipText( "Unassign a task" );
        unassignButton.setEnabled( false );
        toolbar.add( unassignButton );

        return toolbar;
    }

    public void setSandbox( File sb )
    {
        VersioningSystem vs = cloak.getVersioningSystem();
        OpenTask ot = vs.getOpenTaskBySandbox( sb );

        if( sb == null )
        {
            viewPanel.showCard( emptyPanel );
        }
        else
        {
            viewPanel.showCard( controlsPanel );

            assignButton.setEnabled( true );
            unassignButton.setEnabled( ot != null );

            cvPanel.setSandbox( sb );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            DirectoryChooser chooser = new DirectoryChooser(
                ClearViewFrame.this );
            chooser.setVisible( true );
            File[] files = chooser.getSelected();

            if( files != null && files.length > 0 )
            {
                setSandbox( files[0] );
            }
        }
    }

    private class CheckoutListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            ;
        }
    }

    private class EditorListener implements ActionListener
    {
        private CceFrame frame;

        public EditorListener()
        {
            frame = new CceFrame();
            frame.validate();
            frame.setData( cloak.getVersioningSystem() );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( !frame.isShowing() )
            {
                frame.setLocationRelativeTo( ClearViewFrame.this );
            }
            frame.setVisible( true );
        }
    }
}
