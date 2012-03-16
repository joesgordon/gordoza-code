package org.jutils.ui.event.updater;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

/*******************************************************************************
 * This class acts as a generic document undo listener. Any time an edit happens
 * that can be undone, the edit is added to an {@link UndoManager}. In addition,
 * an {@link IDataUpdater} can be specified in order to keep a text component's
 * underlying data in sync with edits.
 ******************************************************************************/
public class DocumentUpdater implements DocumentListener
{
    /**  */
    private IDataUpdater updater;

    /***************************************************************************
     * Constructor
     * @param mgr The manager that keeps track of edits
     * @param updatable Used to keep the underlying data in sync with edits (can
     * be <b>null</b>)
     **************************************************************************/
    public DocumentUpdater( IDataUpdater updatable )
    {
        updater = updatable;
        updater.getClass();
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
        updater.updateData();
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
        updater.updateData();
    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
        updater.updateData();
    }

}
