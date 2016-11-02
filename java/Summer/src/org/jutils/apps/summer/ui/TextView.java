package org.jutils.apps.summer.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextView implements IDataView<String>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextArea textField;
    /**  */
    private final FileChooserListener openListener;
    /**  */
    private final Action openAction;
    /**  */
    private final FileChooserListener saveListener;
    /**  */
    private final SaveListener saveFileListener;
    /**  */
    private final Action saveAction;

    /**  */
    private String text;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextView()
    {
        Icon icon;

        this.view = new JPanel();
        this.textField = new JTextArea();

        this.openListener = new FileChooserListener( view, "Open File",
            new OpenListener( this ), false );
        icon = IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 );
        this.openAction = new ActionAdapter( openListener, "Open File", icon );

        this.saveFileListener = new SaveListener( this );
        this.saveListener = new FileChooserListener( view, "Save File",
            saveFileListener, true );
        icon = IconConstants.loader.getIcon( IconConstants.SAVE_16 );
        this.saveAction = new ActionAdapter( saveListener, "Save File", icon );

        createContent();

        setData( "" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, openAction );
        SwingUtils.addActionToToolbar( toolbar, saveAction );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContent()
    {
        view.setLayout( new BorderLayout() );
        JScrollPane pane = new JScrollPane( textField );

        textField.setFont( SwingUtils.getFixedFont( 12 ) );

        pane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( pane, BorderLayout.CENTER );

        return view;
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
    public String getData()
    {
        return text;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( String text )
    {
        this.text = text;

        textField.setText( text );
    }

    /***************************************************************************
     * @param description
     * @param extension
     **************************************************************************/
    public void setExtension( String description, String extension )
    {
        openListener.removeAllExtensions();
        openListener.addExtension( description, extension );

        saveListener.removeAllExtensions();
        saveListener.addExtension( description, extension );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        this.textField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showSave()
    {
        showSave( null );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void showSave( File file )
    {
        saveFileListener.defaultFile = file;

        saveAction.actionPerformed( null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements IFileSelectionListener
    {
        private final TextView view;

        public OpenListener( TextView view )
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
            try( Scanner scnr = new Scanner( files[0] ) )
            {
                String text = scnr.useDelimiter( "\\A" ).next();

                view.setData( text );
            }
            catch( FileNotFoundException ex )
            {
                JOptionPane.showMessageDialog( view.view,
                    "Cannot open file for reading: " +
                        files[0].getAbsolutePath(),
                    "Cannot Open File", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener implements IFileSelectionListener
    {
        private final TextView view;

        public File defaultFile;

        public SaveListener( TextView view )
        {
            this.view = view;
            this.defaultFile = null;
        }

        @Override
        public File getDefaultFile()
        {
            return defaultFile;
        }

        @Override
        public void filesChosen( File [] files )
        {
            try( PrintStream stream = new PrintStream( files[0] ) )
            {
                stream.print( view.text );
            }
            catch( FileNotFoundException ex )
            {
                JOptionPane.showMessageDialog( view.view,
                    "Cannot open file for writing: " +
                        files[0].getAbsolutePath(),
                    "Cannot Open File", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
