package org.jutils.apps.summer.ui;

import java.awt.*;
import java.io.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileSystemView;

import org.jutils.*;
import org.jutils.apps.summer.data.*;
import org.jutils.apps.summer.io.ChecksumFileSerializer;
import org.jutils.apps.summer.tasks.ChecksumVerificationTask;
import org.jutils.io.parsers.ExistenceType;
import org.jutils.task.TaskView;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.TitleView;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.fields.FileField;
import org.jutils.ui.model.*;
import org.jutils.ui.model.LabelTableCellRenderer.ITableCellLabelDecorator;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * 
 *******************************************************************************/
public class VerifyView implements IDataView<ChecksumResult>, IValidationField
{
    /**  */
    private final FileField commonDirField;
    /**  */
    private final ItemsTableModel<SumFile> tableModel;
    /**  */
    private final JTable table;
    /**  */
    private final JPanel view;

    /**  */
    private final ValidityListenerList validityListeners;

    /**  */
    private ChecksumResult input;

    /***************************************************************************
     * 
     **************************************************************************/
    public VerifyView()
    {
        this.commonDirField = new FileField( ExistenceType.DIRECTORY_ONLY );
        this.tableModel = new ItemsTableModel<>( new ChecksumsTableModel() );
        this.table = new JTable( tableModel );

        this.view = createView();

        this.validityListeners = new ValidityListenerList();

        setData( new ChecksumResult() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createForm(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 8, 8, 8 ), 0, 0 );
        panel.add( createTablePanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createForm()
    {
        StandardFormView form = new StandardFormView();

        commonDirField.addChangeListener(
            new CommonFileChangedListener( this ) );

        form.addField( "Common Directory", commonDirField.getView() );

        return form.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTablePanel()
    {
        TitleView view = new TitleView();
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane pane = new JScrollPane( table );

        table.getTableHeader().setReorderingAllowed( false );
        table.setDefaultRenderer( String.class,
            new LabelTableCellRenderer( new ChecksumRenderer( this ) ) );
        table.getSelectionModel().setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION );

        pane.getViewport().setBackground( table.getBackground() );
        pane.setDropTarget(
            new FileDropTarget( new OpenChecksumFileDropTarget( this ) ) );
        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( pane, BorderLayout.CENTER );

        view.setTitle( "Checksums" );
        view.setComponent( panel );

        return view.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();
        Action action;
        Icon icon;

        SwingUtils.setToolbarDefaults( toolbar );

        toolbar.setBorderPainted( true );
        toolbar.setBorder( new MatteBorder( 0, 0, 1, 0, Color.gray ) );

        FileChooserListener openListener = new FileChooserListener( view,
            "Choose Checksum File", new OpenChecksumFileListener( this ),
            false );

        openListener.addExtension( "MD5 Checksum File", "md5" );
        openListener.addExtension( "CRC Checksum File", "crc" );

        icon = IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 );
        action = new ActionAdapter( openListener, "Open Checksum File", icon );
        JButton button = SwingUtils.addActionToToolbar( toolbar, action );

        button.setDropTarget(
            new FileDropTarget( new OpenChecksumFileDropTarget( this ) ) );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void validate()
    {
        if( input == null )
        {
            validityListeners.signalValidity( "No input loaded" );
            return;
        }

        if( input.type == null )
        {
            validityListeners.signalValidity( "No file loaded" );
            return;
        }

        if( input.files.isEmpty() )
        {
            validityListeners.signalValidity( "No files in checksum file" );
            return;
        }

        for( SumFile sf : input.files )
        {
            if( !sf.file.isFile() )
            {
                validityListeners.signalValidity(
                    "File does not exist: " + sf.file.getAbsolutePath() );
                return;
            }
        }

        validityListeners.signalValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChecksumResult getData()
    {
        return input;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( ChecksumResult input )
    {
        this.input = input;

        commonDirField.setData( input.commonDir );
        tableModel.setItems( input.files );

        validate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        validityListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        validityListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return validityListeners.getValidity();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void runVerify()
    {
        ChecksumResult input = getData();
        ChecksumVerificationTask task = new ChecksumVerificationTask( input );

        if( input.type == null )
        {
            JOptionPane.showMessageDialog( view, "Checksum file not loaded",
                "File Not Loaded", JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( !getValidity().isValid )
        {
            JOptionPane.showMessageDialog( view, getValidity().reason,
                "Invalid Configuration", JOptionPane.ERROR_MESSAGE );
            return;
        }

        TaskView.startAndShow( view, task, "Verifing Checksums" );

        List<InvalidChecksum> invalidSums = task.invalidSums;

        if( invalidSums.isEmpty() )
        {
            JOptionPane.showMessageDialog( view, "All checksums were valid",
                "Checksums Valid", JOptionPane.INFORMATION_MESSAGE );
        }
        else
        {
            InvalidChecksumsView invalidView = new InvalidChecksumsView();

            invalidView.setData( invalidSums );

            JOptionPane.showMessageDialog( view, invalidView.getView(),
                "Invalid Checksums", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    private void readFile( File file )
    {
        ChecksumFileSerializer cfs = new ChecksumFileSerializer();

        try
        {
            ChecksumResult input = cfs.read( file );

            setData( input );

            for( SumFile sf : input.files )
            {
                if( sf.file.isFile() )
                {
                    sf.length = sf.file.length();
                }
            }
        }
        catch( ValidationException ex )
        {
            JOptionPane.showMessageDialog( view,
                "Error reading file " + file.getAbsolutePath() +
                    Utils.NEW_LINE + ex.getLocalizedMessage(),
                "Read Error", JOptionPane.ERROR_MESSAGE );
        }
        catch( FileNotFoundException ex )
        {
            JOptionPane.showMessageDialog( view,
                "Cannot open file " + file.getAbsolutePath(), "File Read Error",
                JOptionPane.ERROR_MESSAGE );
        }
        catch( IOException ex )
        {
            JOptionPane.showMessageDialog( view,
                "I/O error reading file:" + ex.getMessage() + Utils.NEW_LINE +
                    file.getAbsolutePath(),
                "I/O Error", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChecksumsTableModel
        implements ITableItemsConfig<SumFile>
    {
        private static final Class<?> [] CLASSES = { String.class,
            String.class };
        private static final String [] NAMES = { "Checksum", "File" };

        @Override
        public String [] getColumnNames()
        {
            return NAMES;
        }

        @Override
        public Class<?> [] getColumnClasses()
        {
            return CLASSES;
        }

        @Override
        public Object getItemData( SumFile checksum, int col )
        {
            switch( col )
            {
                case 0:
                    return checksum.checksum;

                case 1:
                    return checksum.path;

                default:
                    throw new IllegalArgumentException(
                        "Invalid column: " + col );
            }
        }

        @Override
        public void setItemData( SumFile item, int col, Object data )
        {
        }

        @Override
        public boolean isCellEditable( SumFile item, int col )
        {
            return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenChecksumFileListener
        implements IFileSelectionListener
    {
        private final VerifyView view;

        public OpenChecksumFileListener( VerifyView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            return null;
        }

        @Override
        public void filesChosen( File [] files )
        {
            File file = files[0];

            if( file == null || !file.isFile() )
            {
                return;
            }

            view.readFile( file );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenChecksumFileDropTarget
        implements ItemActionListener<IFileDropEvent>
    {
        private VerifyView view;

        public OpenChecksumFileDropTarget( VerifyView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            view.readFile( event.getItem().getFiles().get( 0 ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ChecksumRenderer implements ITableCellLabelDecorator
    {
        private static final FileSystemView FILE_SYSTEM = FileSystemView.getFileSystemView();

        private final VerifyView view;
        private final Font defaultFont;
        private final Font fixedFont;
        private final Color defaultBackground;

        public ChecksumRenderer( VerifyView view )
        {
            super();

            this.view = view;

            this.defaultFont = UIManager.getFont( "label.font" );
            this.fixedFont = new Font( Font.MONOSPACED, Font.PLAIN, 12 );
            this.defaultBackground = UIManager.getColor( "label.background" );
        }

        @Override
        public void decorate( JLabel label, JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col )
        {

            SumFile sum = view.tableModel.getItem( row );
            File commonPath = view.commonDirField.getData();

            Icon icon = null;

            if( col == 0 )
            {
                label.setFont( fixedFont );
            }
            else
            {
                icon = FILE_SYSTEM.getSystemIcon( sum.file );
                label.setFont( defaultFont );
            }

            label.setIcon( icon );

            if( !isSelected )
            {
                Color bg = defaultBackground;
                if( commonPath == null )
                {
                    bg = Color.red;
                }
                else
                {
                    if( !sum.file.isFile() )
                    {
                        bg = Color.red;
                    }
                }

                label.setBackground( bg );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CommonFileChangedListener
        implements ItemActionListener<File>
    {
        private final VerifyView view;

        public CommonFileChangedListener( VerifyView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            ChecksumResult input = view.getData();

            if( input != null )
            {
                for( SumFile sf : input.files )
                {
                    if( sf.file.isFile() )
                    {
                        sf.length = sf.file.length();
                    }
                }
            }

            view.input.setFiles( event.getItem() );
            view.table.repaint();
            view.validate();
        }
    }
}
