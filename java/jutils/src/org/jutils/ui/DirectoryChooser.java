package org.jutils.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.*;

import org.jutils.*;
import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.fields.ValidationTextField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.ValidationView;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 *
 ******************************************************************************/
public class DirectoryChooser
{
    /**  */
    private final JDialog dialog;
    /**  */
    private final JLabel messageLabel;
    /**  */
    private final ValidationTextField dirsField;
    /**  */
    private final DirectoryTree tree;

    /**  */
    private File [] selected;

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public DirectoryChooser( Window owner )
    {
        this( owner, "Choose Directory" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     **************************************************************************/
    public DirectoryChooser( Window owner, String title )
    {
        this( owner, title, "Please choose a folder or folders:" );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param message String
     **************************************************************************/
    public DirectoryChooser( Window owner, String title, String message )
    {
        this( owner, title, message, null );
    }

    /***************************************************************************
     * @param owner
     * @param title
     * @param message
     * @param paths
     **************************************************************************/
    public DirectoryChooser( Window owner, String title, String message,
        String paths )
    {
        this.dialog = new JDialog( owner, title,
            ModalityType.APPLICATION_MODAL );
        this.tree = new DirectoryTree();
        this.messageLabel = new JLabel();
        this.dirsField = new ValidationTextField();

        this.selected = null;

        tree.setSelectedPaths( paths );

        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setContentPane( createContentPanel( message ) );
        dialog.setSize( 600, 500 );
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
        JScrollPane scrollPane = new JScrollPane( tree.getView() );
        GridBagConstraints constraints;

        messageLabel.setText( message );

        dirsField.setValidator( new DirsValidator() );
        dirsField.addValidityChanged( new DirFieldListener( this ) );

        tree.addSelectedListener( new DirsSelectedListener( this ) );

        scrollPane.setMinimumSize(
            new Dimension( messageLabel.getPreferredSize().width,
                messageLabel.getPreferredSize().width ) );
        scrollPane.setPreferredSize( scrollPane.getMinimumSize() );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        mainPanel.add( messageLabel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        mainPanel.add( scrollPane, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        mainPanel.add( new ValidationView( dirsField ).getView(), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 4, 8, 4 ), 0, 0 );
        mainPanel.add( createButtonPanel(), constraints );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton newButton;
        JButton okButton;
        JButton cancelButton;

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        newButton = new JButton();
        newButton.setText( "New Directory" );
        newButton.addActionListener( new NewDirectoryListener( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 4, 2, 2 ), 4, 0 );
        panel.add( newButton, constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        okButton = new JButton();
        okButton.setText( "OK" );
        okButton.addActionListener( new OkListener( this ) );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 4, 0 );
        panel.add( okButton, constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        cancelButton = new JButton();
        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelListener( this ) );

        constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 4 ), 4, 0 );
        panel.add( cancelButton, constraints );

        Dimension dim = SwingUtils.getMaxComponentSize( newButton, okButton,
            cancelButton );

        newButton.setMinimumSize( dim );
        newButton.setPreferredSize( dim );
        okButton.setMinimumSize( dim );
        okButton.setPreferredSize( dim );
        cancelButton.setMinimumSize( dim );
        cancelButton.setPreferredSize( dim );

        return panel;
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
     * @param iconImages
     **************************************************************************/
    public void setIconImages( List<Image> iconImages )
    {
        dialog.setIconImages( iconImages );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        IApplication app = new IApplication()
        {
            @Override
            public String getLookAndFeelName()
            {
                return null;
            }

            @Override
            public void createAndShowUi()
            {
                DirectoryChooser dialog = new DirectoryChooser( null );

                dialog.setSelectedPaths( new File( IOUtils.USERS_DIR,
                    "garbage_jfdkslfjsdl" ).getAbsolutePath() );

                dialog.setVisible( true );

                String paths = dialog.getSelectedPaths();
                LogUtils.printDebug( paths );
            }
        };

        SwingUtilities.invokeLater( new AppRunner( app ) );
    }

    /***************************************************************************
     * @return File[]
     **************************************************************************/
    public File [] getSelected()
    {
        return selected;
    }

    /***************************************************************************
     * @param paths String
     **************************************************************************/
    public void setSelectedPaths( String paths )
    {
        tree.setSelectedPaths( paths );
        dirsField.setText( tree.getSelectedPaths() );
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String getSelectedPaths()
    {
        return tree.getSelectedPaths();
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
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        dialog.setTitle( title );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void setMessage( String message )
    {
        messageLabel.setText( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private final DirectoryChooser chooser;

        public CancelListener( DirectoryChooser dialog )
        {
            this.chooser = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            chooser.dialog.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OkListener implements ActionListener
    {
        private final DirectoryChooser chooser;

        public OkListener( DirectoryChooser dialog )
        {
            this.chooser = dialog;
        }

        public void actionPerformed( ActionEvent e )
        {
            chooser.selected = chooser.tree.getSelected();
            chooser.dialog.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NewDirectoryListener implements ActionListener
    {
        private final DirectoryChooser chooser;

        public NewDirectoryListener( DirectoryChooser dialog )
        {
            this.chooser = dialog;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            File [] selectedFiles = chooser.tree.getSelected();

            if( selectedFiles != null && selectedFiles.length == 1 )
            {
                String name = JOptionPane.showInputDialog( chooser.dialog,
                    "Enter the new directory name:", "New Directory Name",
                    JOptionPane.QUESTION_MESSAGE );

                if( name != null )
                {
                    File parentDir = selectedFiles[0];
                    File newDir = new File( parentDir, name );

                    if( !newDir.exists() )
                    {
                        if( !newDir.mkdir() )
                        {
                            JOptionPane.showMessageDialog( chooser.dialog,
                                "Please check the permissions on the parent directory.",
                                "Cannot Create New Directory",
                                JOptionPane.ERROR_MESSAGE );
                        }
                        else
                        {
                            chooser.tree.refreshSelected();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( chooser.dialog,
                            "The directory or file, '" + name +
                                "', already exists.",
                            "Cannot Create New Directory",
                            JOptionPane.ERROR_MESSAGE );
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog( chooser.dialog,
                    "Please choose only one directory when creating a sub-directory.",
                    "Cannot Create New Directory", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DirFieldListener implements IValidityChangedListener
    {
        private final DirectoryChooser chooser;

        public DirFieldListener( DirectoryChooser chooser )
        {
            this.chooser = chooser;
        }

        @Override
        public void signalValid()
        {
            chooser.tree.setSelectedPaths( chooser.dirsField.getText() );
        }

        @Override
        public void signalInvalid( String reason )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DirsValidator implements ITextValidator
    {
        @Override
        public void validateText( String text ) throws ValidationException
        {
            if( text == null )
            {
                throw new ValidationException( "Null text" );
            }

            File [] dirs = IOUtils.getFilesFromString( text );

            if( dirs.length < 1 )
            {
                throw new ValidationException( "Empty paths string" );
            }

            for( File d : dirs )
            {
                IOUtils.validateDirInput( d, "Directories Chosen" );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DirsSelectedListener
        implements ItemActionListener<List<File>>
    {
        private final DirectoryChooser chooser;

        public DirsSelectedListener( DirectoryChooser chooser )
        {
            this.chooser = chooser;
        }

        @Override
        public void actionPerformed( ItemActionEvent<List<File>> event )
        {
            List<File> files = event.getItem();

            chooser.dirsField.setText(
                Utils.collectionToString( files, File.pathSeparator ) );
        }
    }
}
