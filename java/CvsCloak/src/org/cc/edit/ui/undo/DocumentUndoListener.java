package org.cc.edit.ui.undo;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import org.jutils.ui.event.updater.IDataUpdater;

/*******************************************************************************
 * This class acts as a generic document undo listener. Any time an edit happens
 * that can be undone, the edit is added to an {@link UndoManager}. In addition,
 * an {@link IDataUpdater} can be specified in order to keep a text component's
 * underlying data in sync with edits.
 ******************************************************************************/
public class DocumentUndoListener implements UndoableEditListener
{
    private UndoManager manager;
    private IDataUpdater updater;

    /***************************************************************************
     * Constructor
     * @param mgr The manager that keeps track of edits
     * @param updatable Used to keep the underlying data in sync with edits (can
     * be <b>null</b>)
     **************************************************************************/
    public DocumentUndoListener( UndoManager mgr, IDataUpdater updatable )
    {
        manager = mgr;
        updater = updatable;
    }

    /*
     * (non-Javadoc)
     * @see
     * javax.swing.event.UndoableEditListener#undoableEditHappened(javax.swing
     * .event.UndoableEditEvent)
     */
    @Override
    public void undoableEditHappened( UndoableEditEvent e )
    {
        manager.addEdit( e.getEdit() );
        if( updater != null )
        {
            updater.updateData();
        }
    }

}
