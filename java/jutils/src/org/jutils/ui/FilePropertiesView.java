package org.jutils.ui;

import java.awt.Component;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jutils.io.IOUtils;
import org.jutils.io.StringPrintStream;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilePropertiesView implements IDataView<File>
{
    /**  */
    private final VerboseMessageView view;
    /**  */
    private final SimpleDateFormat fmt;

    /**  */
    private File file;

    /***************************************************************************
     * 
     **************************************************************************/
    public FilePropertiesView()
    {
        this.view = new VerboseMessageView();
        this.fmt = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss.SSS" );

        this.file = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public File getData()
    {
        return file;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( File file )
    {
        this.file = file;

        try( StringPrintStream msg = new StringPrintStream() )
        {
            msg.println( "  Can Execute: %s", file.canExecute() );
            msg.println( "     Can Read: %s", file.canRead() );
            msg.println( "    Can Write: %s", file.canWrite() );
            msg.println( "    Is Hidden: %s", file.isHidden() );
            msg.println( "       Exists: %s", file.exists() );
            msg.println( " Is Directory: %s", file.isDirectory() );
            msg.println( "      Is File: %s", file.isFile() );
            msg.println( "  File Length: %s",
                IOUtils.byteCount( file.length() ) );
            msg.println( "Last Modified: %s",
                fmt.format( new Date( file.lastModified() ) ) );

            msg.println();

            msg.println( "--- Volume Info ---" );
            msg.println( "   Free Space: %s",
                IOUtils.byteCount( file.getFreeSpace() ) );
            msg.println( "  Total Space: %s",
                IOUtils.byteCount( file.getTotalSpace() ) );
            msg.println( " Usable Space: %s",
                IOUtils.byteCount( file.getUsableSpace() ) );

            view.setMessages( file.getName(), msg.toString() );
        }
    }

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public void show( Component parent )
    {
        view.show( parent, "File Info", 350, 400 );
    }
}
