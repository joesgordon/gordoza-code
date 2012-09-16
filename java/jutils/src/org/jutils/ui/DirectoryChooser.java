package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;

import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.looks.Options;

/*******************************************************************************
 *
 ******************************************************************************/
public class DirectoryChooser
{
    /**  */
    private final JDialog dialog;
    /**  */
    private final DirectoryTree tree;
    /**  */
    private final JLabel messageLabel;

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public DirectoryChooser( Frame owner )
    {
        this( owner, "Choose Directory" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     **************************************************************************/
    public DirectoryChooser( Frame owner, String title )
    {
        this( owner, title, "Please choose a folder or folders:" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param message String
     **************************************************************************/
    public DirectoryChooser( Frame owner, String title, String message )
    {
        this.dialog = new JDialog( owner, title, true );
        this.tree = new DirectoryTree();
        this.messageLabel = new JLabel();

        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        dialog.setContentPane( createContentPanel( message ) );

        dialog.setSize( 300, 300 );
        dialog.validate();
        dialog.setLocationRelativeTo( owner );
    }

    /***************************************************************************
     * @param message
     * @return
     **************************************************************************/
    private JPanel createContentPanel( String message )
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        JScrollPane scrollPane = new JScrollPane( tree );

        messageLabel.setText( message );

        scrollPane.setMinimumSize( new Dimension(
            messageLabel.getPreferredSize().width,
            messageLabel.getPreferredSize().width ) );
        scrollPane.setPreferredSize( scrollPane.getMinimumSize() );

        mainPanel.add( messageLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                8, 8, 8, 8 ), 0, 0 ) );
        mainPanel.add( scrollPane, new GridBagConstraints( 0, 1, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 ) );
        mainPanel.add( createButtonPanel(), new GridBagConstraints( 0, 2, 1, 1,
            0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 ) );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createButtonPanel()
    {
        ButtonBarBuilder2 builder = new ButtonBarBuilder2();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();

        okButton.setText( "OK" );
        okButton.addActionListener( new OkListener( this ) );
        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelListener( this ) );

        builder.addButton( okButton );
        builder.addRelatedGap();
        builder.addButton( cancelButton );

        return builder.getPanel();
    }

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setVisible( boolean visible )
    {
        if( visible && !dialog.isVisible() )
        {
            dialog.validate();
            dialog.setLocationRelativeTo( dialog.getParent() );
        }
        dialog.setVisible( visible );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                    UIManager.put( "TabbedPaneUI",
                        BasicTabbedPaneUI.class.getCanonicalName() );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                DirectoryChooser dialog = new DirectoryChooser( null );

                dialog.setSelectedPaths( new File( IOUtils.USERS_DIR,
                    "garbage_jfdkslfjsdl" ).getAbsolutePath() );

                dialog.setVisible( true );

                String paths = dialog.getSelectedPaths();
                LogUtils.printDebug( "DEBUG: Paths: " + paths );
            }
        } );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File[] getSelected()
    {
        return tree.getSelected();
    }

    /***************************************************************************
     * @param files File[]
     **************************************************************************/
    public void setSelected( File[] files )
    {
        tree.setSelected( files );
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        tree.setSelectedPaths( paths );
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        return tree.getSelectedPaths();
    }

    public void setSize( int width, int height )
    {
        dialog.setSize( width, height );
    }

    public void setTitle( String title )
    {
        dialog.setTitle( title );
    }

    public void setMessage( String message )
    {
        messageLabel.setText( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private final DirectoryChooser dialog;

        public CancelListener( DirectoryChooser dialog )
        {
            this.dialog = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            dialog.tree.clearSelection();
            dialog.dialog.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OkListener implements ActionListener
    {
        private final DirectoryChooser dialog;

        public OkListener( DirectoryChooser dialog )
        {
            this.dialog = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            dialog.dialog.dispose();
        }
    }
}
