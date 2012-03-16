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
public class FolderDialog extends JDialog
{
    /**  */
    private DirTree tree;

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public FolderDialog( Frame owner )
    {
        this( owner, "Choose Directory" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     **************************************************************************/
    public FolderDialog( Frame owner, String title )
    {
        this( owner, title, "Please choose a folder or folders:" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param message String
     **************************************************************************/
    public FolderDialog( Frame owner, String title, String message )
    {
        super( owner, title, true );

        setDefaultCloseOperation( DISPOSE_ON_CLOSE );

        setContentPane( createContentPanel( message ) );

        setSize( 300, 300 );
        pack();
        setLocationRelativeTo( owner );
    }

    /**
     * @param message
     * @return
     */
    private JPanel createContentPanel( String message )
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        JLabel messageLabel = new JLabel( message );
        tree = new DirTree();
        JScrollPane scrollPane = new JScrollPane( tree );

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

    private JPanel createButtonPanel()
    {
        ButtonBarBuilder2 builder = new ButtonBarBuilder2();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();

        okButton.setText( "OK" );
        okButton.addActionListener( new OkListener() );
        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelListener() );

        builder.addButton( okButton );
        builder.addRelatedGap();
        builder.addButton( cancelButton );

        return builder.getPanel();
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

                FolderDialog dialog = new FolderDialog( null );
                dialog.validate();
                dialog.setLocationRelativeTo( null );

                dialog.setSelectedPaths( new File( IOUtils.USERS_DIR,
                    "garbage_jfdkslfjsdl" ).getAbsolutePath() );

                dialog.setVisible( true );

                String paths = dialog.getSelectedPaths();
                LogUtils.printDebug( "DEBUG: Paths: " + paths );
            }
        } );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Dimension getPreferredSize()
    {
        Dimension dim = super.getPreferredSize();

        dim.width = Math.max( dim.width, dim.height );
        dim.height = dim.width;

        return dim;
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

    /***************************************************************************
     * 
     **************************************************************************/
    private class CancelListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            tree.clearSelection();
            dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OkListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            dispose();
        }
    }
}
