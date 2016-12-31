package org.jutils.ui.fields;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import org.jutils.*;
import org.jutils.io.IOUtils;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.io.parsers.FileParser;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileDropTarget.JTextFieldFilesListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.validation.*;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;
import org.jutils.utils.IGetter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileField implements IDataView<File>, IValidationField
{
    /**  */
    private final JPanel view;
    /**  */
    private final ValidationTextField field;
    /**  */
    private final JPopupMenu openMenu;
    /**  */
    private final ItemActionList<File> changeListeners;
    /**  */
    private final FileChooserListener fileListener;
    /**  */
    private final JMenuItem openPathMenuItem;

    /***************************************************************************
     * Creates a File view with an {@link ExistenceType} of FILE_ONLY, required,
     * opens files, and displays a browse button..
     **************************************************************************/
    public FileField()
    {
        this( ExistenceType.FILE_ONLY, true, false );
    }

    /***************************************************************************
     * Creates a File view with the provided {@link ExistenceType}, required,
     * and opens file if the existence is {@link ExistenceType#FILE_ONLY} that
     * displays a browse button.
     * @param existence type of existence to be checked: file/dir/either/none.
     **************************************************************************/
    public FileField( ExistenceType existence )
    {
        this( existence, true, existence != ExistenceType.FILE_ONLY );
    }

    /***************************************************************************
     * Creates a File view with the provided {@link ExistenceType} and required,
     * and saves files that displays a browse button.
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required )
    {
        this( existence, required, true );
    }

    /***************************************************************************
     * Creates a File view with the provided {@link ExistenceType}, required,
     * and save boolean that displays a browse button.
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required,
        boolean isSave )
    {
        this( existence, required, isSave, true );
    }

    /***************************************************************************
     * Creates a File view according to the parameters provided:
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.
     * @param isSave if the path is to be be save to (alt. read from).
     * @param showButton denotes whether the browse button should be shown.
     **************************************************************************/
    public FileField( ExistenceType existence, boolean required, boolean isSave,
        boolean showButton )
    {
        this.changeListeners = new ItemActionList<>();

        this.field = new ValidationTextField();
        this.fileListener = createFileListener( existence, isSave );
        this.openPathMenuItem = new JMenuItem();
        this.openMenu = createMenu();
        this.view = createView( existence, required, isSave, showButton );

        field.getView().setColumns( 20 );

        field.setText( "" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        Action a;

        a = createAction( ( e ) -> openPath(), "Open Path",
            IconConstants.OPEN_FILE_16 );
        openPathMenuItem.setAction( a );
        menu.add( openPathMenuItem );

        a = createAction( ( e ) -> openParent(), "Open Parent",
            IconConstants.OPEN_FOLDER_16 );
        menu.add( new JMenuItem( a ) );

        menu.addSeparator();

        IGetter<String> copyPath = () -> {
            return getData().getAbsolutePath();
        };
        IGetter<String> copyName = () -> {
            return getData().getName();
        };
        IGetter<String> copyParent = () -> {
            return getData().getParent();
        };
        IGetter<String> copyParentName = () -> {
            return getData().getParentFile().getName();
        };

        a = createAction( ( e ) -> copyPath( copyPath ), "Copy Path",
            IconConstants.EDIT_COPY_16 );
        menu.add( new JMenuItem( a ) );

        a = createAction( ( e ) -> copyPath( copyName ), "Copy Name",
            IconConstants.EDIT_COPY_16 );
        menu.add( new JMenuItem( a ) );

        menu.addSeparator();

        a = createAction( ( e ) -> copyPath( copyParent ), "Copy Parent Path",
            IconConstants.EDIT_COPY_16 );
        menu.add( new JMenuItem( a ) );

        a = createAction( ( e ) -> copyPath( copyParentName ),
            "Copy Parent Name", IconConstants.EDIT_COPY_16 );
        menu.add( new JMenuItem( a ) );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Action createAction( ActionListener l, String name,
        String iconStr )
    {
        Icon icon = IconConstants.getIcon( iconStr );

        return new ActionAdapter( l, name, icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void copyPath( IGetter<String> strGetter )
    {
        String str = strGetter.get();
        if( str != null )
        {
            Utils.setClipboardText( str );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void openPath()
    {
        File file = getData();
        if( file != null )
        {
            openPath( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void openParent()
    {
        File file = getData();
        if( file != null )
        {
            openPath( file.getParentFile() );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void openPath( File file )
    {
        if( !file.exists() )
        {
            String [] choices = new String[] { "Open Parent", "Cancel" };
            String choice = SwingUtils.showConfirmMessage( getView(),
                "File does not exist. Open existing parent?",
                "File does not exist", choices, choices[0] );

            if( choices[0].equals( choice ) )
            {
                File parent = IOUtils.getExistingDir( file.getAbsolutePath() );

                if( parent == null )
                {
                    JOptionPane.showMessageDialog( getView(),
                        "No parent exists for file:" + Utils.NEW_LINE +
                            file.getAbsolutePath(),
                        "Error Opening File", JOptionPane.ERROR_MESSAGE );
                    return;
                }

                file = parent;
            }
            else
            {
                return;
            }
        }

        try
        {
            Desktop.getDesktop().open( file );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( getView(),
                "Could not open file externally:" + Utils.NEW_LINE +
                    file.getAbsolutePath(),
                "Error Opening File", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param existence
     * @param isSave
     * @return
     **************************************************************************/
    private FileChooserListener createFileListener( ExistenceType existence,
        boolean isSave )
    {
        FileChooserListener fcl = null;

        if( existence != ExistenceType.DIRECTORY_ONLY )
        {
            IFileSelected ifs = ( f ) -> setData( f );
            fcl = new FileChooserListener( field.getView(), "Choose File",
                isSave, ifs, () -> getDefaultFile() );
        }

        return fcl;
    }

    /***************************************************************************
     * @param existence type of existence to be checked: file/dir/either/none.
     * @param required if the path can be empty or is required.s
     * @param isSave if the path is to be be save to (alt. read from).
     * @param showButton denotes whether the browse button should be shown.
     * @return
     **************************************************************************/
    private JPanel createView( ExistenceType existence, boolean required,
        boolean isSave, boolean showButton )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton button;
        ActionListener browseListener;

        if( existence == ExistenceType.DIRECTORY_ONLY )
        {
            IFileSelected ifs = ( f ) -> setData( f );
            browseListener = new DirectoryChooserListener( panel,
                "Choose Directory", ifs, () -> getDefaultFile() );
        }
        else
        {
            browseListener = fileListener;
        }

        ITextValidator validator = new DataTextValidator<File>(
            new FileParser( existence, required ), new FileUpdater( this ) );

        // LogUtils.printDebug( "Setting validator" );
        field.setValidator( validator );
        field.getView().setDropTarget( new FileDropTarget(
            new JTextFieldFilesListener( field.getView(), existence ) ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( field.getView(), constraints );

        if( showButton )
        {
            button = new JButton(
                IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
            button.addActionListener( browseListener );
            button.setToolTipText( "Browse (Right-click to open path)" );

            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 4, 0, 0 ), 0, 0 );
            panel.add( button, constraints );

            button.addMouseListener( new MenuListener( this ) );
        }

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getData()
    {
        File file;
        String path = field.getText();

        if( path.isEmpty() )
        {
            file = null;
        }
        else
        {
            file = new File( path ).getAbsoluteFile();
        }

        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( File file )
    {
        String text = "";

        if( file != null )
        {
            text = file.getAbsolutePath();
        }

        // LogUtils.printDebug( "Setting data to: \"" + text + "\"" );

        field.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @param b
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addChangeListener( ItemActionListener<File> l )
    {
        changeListeners.addListener( l );
    }

    /***************************************************************************
     * Adds the provided extension description and extensions to the file
     * dialog.
     * @param description the description of the file type denoted by the
     * extension list.
     * @param extensions the list of extensions of a file type.
     * @throws if this field was initialized with
     * {@link ExistenceType#DIRECTORY_ONLY}.
     * @see FileChooserListener#addExtension(String, String...)
     **************************************************************************/
    public void addExtension( String description, String... extensions )
        throws IllegalStateException
    {
        if( fileListener == null )
        {
            throw new IllegalStateException(
                "Cannot add extensions to a chooser that is directory only" );
        }

        fileListener.addExtension( description, extensions );
    }

    /***************************************************************************
     * Returns the last selected file for the listener.
     **************************************************************************/
    private File getDefaultFile()
    {
        File f = getData();

        while( f != null && !f.exists() )
        {
            f = f.getParentFile();
        }

        return f;
    }

    /***************************************************************************
     * Notifies the listeners that the file has been updated.
     **************************************************************************/
    private static class FileUpdater implements IUpdater<File>
    {
        private final FileField view;

        public FileUpdater( FileField view )
        {
            this.view = view;
        }

        @Override
        public void update( File file )
        {
            // LogUtils.printDebug( "File changed to " + file.getAbsolutePath()
            // );
            view.changeListeners.fireListeners( view, file );
        }
    }

    /***************************************************************************
     * Displays the context menu on the button on right-click.
     **************************************************************************/
    private static class MenuListener extends MouseAdapter
    {
        private final FileField field;
        private final FileSystemView fileSys;
        private final Icon fileIcon;
        private final Icon dirIcon;

        public MenuListener( FileField field )
        {
            this.field = field;
            this.fileSys = FileSystemView.getFileSystemView();
            this.fileIcon = field.openPathMenuItem.getIcon();
            this.dirIcon = IconConstants.getIcon(
                IconConstants.OPEN_FOLDER_16 );
        }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 )
            {
                Component c = e.getComponent();
                int x = c.getWidth() / 2; // e.getX();
                int y = c.getHeight() / 2; // e.getY();

                File file = field.getData();
                Icon icon = fileIcon;
                boolean enabled = true;

                if( file.isFile() )
                {
                    icon = fileSys.getSystemIcon( file );

                    if( icon == null )
                    {
                        icon = fileIcon;
                    }
                }
                else if( file.isDirectory() )
                {
                    icon = dirIcon;
                }
                else
                {
                    enabled = false;
                }

                field.openPathMenuItem.setIcon( icon );
                field.openPathMenuItem.setEnabled( enabled );

                field.openMenu.show( c, x, y );
            }
        }
    }
}
