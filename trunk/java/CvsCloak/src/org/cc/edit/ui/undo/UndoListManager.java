package org.cc.edit.ui.undo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.UndoableEdit;

import org.cc.edit.ui.undo.event.UndoChangedListener;

/*******************************************************************************
 * Manages <code>UndoableEdit</code>s. Provides a storage mechanism, methods to
 * initiate the un/re doing, and the ability to listen for changes in the
 * storage.
 ******************************************************************************/
public class UndoListManager
{
    /**
     * List of edits some of which may have been performed.
     */
    private List<UndoableEdit> edits;
    /**
     * Index that identifies the position of the next edit to be undone.
     * Normally points to <code>edits.size() - 1</code>.
     */
    private int nextUndoPosition;
    /** The limit to the number of edits saved. */
    private int limit;
    /** List of listeners to be notified when edits are added or performed. */
    private List<UndoChangedListener> changedListeners;

    /***************************************************************************
     * Constructs an empty manager with the default limit.
     **************************************************************************/
    public UndoListManager()
    {
        limit = 100;
        edits = new ArrayList<UndoableEdit>( limit );
        nextUndoPosition = -1;
        changedListeners = new ArrayList<UndoChangedListener>();
        // UndoManager m;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addChangedListener( UndoChangedListener l )
    {
        changedListeners.add( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeChangedListener( UndoChangedListener l )
    {
        changedListeners.remove( l );
    }

    protected void fireChangedListeners()
    {
        // TODO define function.
        ;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getLimit()
    {
        return limit;
    }

    public UndoableEdit getNextUndo()
    {
        // TODO define function
        return null;
    }

    /***************************************************************************
     * Returns a list of <code>UndoableEdit</code>s to be undone. This list is
     * in reverse order so that the next edit to be undone is at the beginning
     * of the list.
     * @return
     **************************************************************************/
    public List<UndoableEdit> getUndos()
    {
        List<UndoableEdit> undos = new ArrayList<UndoableEdit>();

        for( int i = nextUndoPosition; i > -1; i-- )
        {
            UndoableEdit e = edits.get( i );
            if( e.isSignificant() )
            {
                undos.add( e );
            }
        }

        return undos;
    }

    public UndoableEdit getNextRedo()
    {
        // TODO define function
        return null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<UndoableEdit> getRedos()
    {
        List<UndoableEdit> redos = new ArrayList<UndoableEdit>();

        for( int i = nextUndoPosition + 1; i < edits.size(); i++ )
        {
            UndoableEdit e = edits.get( i );
            if( e.isSignificant() )
            {
                redos.add( e );
            }
        }

        return redos;
    }

    /***************************************************************************
     * @param edit
     **************************************************************************/
    public void addEdit( UndoableEdit edit )
    {
        // TODO define function
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void discardAllEdits()
    {
        edits.clear();
        // TODO call listeners
    }

    public void undo()
    {
        // TODO define function.
        ;
    }

    public void undoTo( UndoableEdit edit )
    {
        // TODO define function.
        ;
    }

    public void redo()
    {
        // TODO define function.
        ;
    }

    public void redoTo( UndoableEdit edit )
    {
        // TODO define function.
        ;
    }
}
