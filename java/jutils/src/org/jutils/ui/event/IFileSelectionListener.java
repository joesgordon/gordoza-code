package org.jutils.ui.event;

import java.io.File;

/*******************************************************************************
 * Listener to be called when the a file or directory is selected by the user.
 ******************************************************************************/
public interface IFileSelectionListener
{
    /***************************************************************************
     * Returns the file to be selected by default when the dialog is displayed.
     * @return the file to be selected by default when the dialog is displayed.
     **************************************************************************/
    public File getDefaultFile();

    /***************************************************************************
     * Called when the user chooses a files/directories. Called only if the user
     * <b>can</b> select multiple files.
     * @param files the files/directories chosen.
     **************************************************************************/
    public void filesChosen( File [] files );
}
