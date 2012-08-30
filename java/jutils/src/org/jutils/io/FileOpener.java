package org.jutils.io;

import java.awt.Component;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jutils.Utils;

public class FileOpener<T>
{
    private ISerializer<T, InputStream, OutputStream> serializer;
    private LastDirectorySaver saver;
    private String extension;

    public FileOpener( ISerializer<T, InputStream, OutputStream> serializer,
        LastDirectorySaver saver, String ext )
    {
        this.serializer = serializer;
        this.saver = saver;
        this.extension = ext;
    }

    public T open( Component comp )
    {
        JFileChooser chooser;
        int choice;
        T item = null;

        chooser = new JFileChooser( saver.getLastOpenDir() );
        chooser.setFileFilter( new ExtensionFilter( extension ) );
        choice = chooser.showOpenDialog( comp );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File file = chooser.getSelectedFile();
            FileInputStream stream;

            try
            {
                stream = new FileInputStream( file );
                item = serializer.read( stream );

                saver.saveLastOpenDir( file.getParentFile() );

                stream.close();
            }
            catch( IOException ex )
            {
                String msg = "Problem reading from file: " + Utils.NEW_LINE;
                msg += "    " + file.getAbsolutePath() + Utils.NEW_LINE;
                msg += "    " + ex.getMessage();

                JOptionPane.showMessageDialog( comp, msg, "Error reading file",
                    JOptionPane.ERROR_MESSAGE );
            }
        }

        return item;
    }

    public boolean save( T item, Component comp )
    {
        JFileChooser chooser;
        int choice;
        boolean saved = false;

        chooser = new JFileChooser( saver.getLastSaveDir() );
        chooser.setFileFilter( new ExtensionFilter( extension ) );
        choice = chooser.showSaveDialog( comp );

        if( choice == JFileChooser.APPROVE_OPTION )
        {
            File file = chooser.getSelectedFile();
            FileOutputStream stream;

            if( !file.getName().endsWith( extension ) )
            {
                file = new File( file.getParentFile(), file.getName() + "." +
                    extension );
            }

            try
            {
                stream = new FileOutputStream( file );
                serializer.write( item, stream );

                saver.saveLastSaveDir( file.getParentFile() );
                stream.close();
                saved = true;
            }
            catch( IOException ex )
            {
                String msg = "Problem writing file: " + Utils.NEW_LINE;
                msg += "    " + file.getAbsolutePath() + Utils.NEW_LINE;
                msg += "    " + ex.getMessage();

                JOptionPane.showMessageDialog( comp, msg, "Error writing file",
                    JOptionPane.ERROR_MESSAGE );
            }
        }

        return saved;
    }

    public static interface LastDirectorySaver
    {
        public void saveLastOpenDir( File file );

        public File getLastOpenDir();

        public void saveLastSaveDir( File file );

        public File getLastSaveDir();
    }
}
