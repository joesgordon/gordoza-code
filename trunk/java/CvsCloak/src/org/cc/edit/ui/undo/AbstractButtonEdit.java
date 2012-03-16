package org.cc.edit.ui.undo;

import javax.swing.*;
import javax.swing.undo.*;

import org.jutils.ui.event.updater.IDataUpdater;

/*******************************************************************************
 * This class represents an edit that takes place on a button that is
 * traditionally used to hold a state. E.g. A {@link JToggleButton} or
 * {@link JCheckBox}. A {@link IDataUpdater} can optionally be specified in
 * order to keep any underlying data in sync with the state of the button.
 ******************************************************************************/
public class AbstractButtonEdit implements UndoableEdit
{
    /**  */
    private AbstractButton button;
    /**  */
    private boolean oldValue;
    /**  */
    private boolean newValue;
    /**  */
    private IDataUpdater updater;

    /***************************************************************************
     * Constructor
     * @param button The button associated with this edit
     * @param oldValue The previous state
     * @param newValue The new state
     * @param updater Used to keep the underlying data in sync with edits (can
     * be <b>null</b>)
     **************************************************************************/
    public AbstractButtonEdit( AbstractButton button, boolean oldValue,
        boolean newValue, IDataUpdater updater )
    {
        this.button = button;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.updater = updater;

        // call initially, because a change just happened
        if( this.updater != null )
        {
            this.updater.updateData();
        }
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#undo()
     **************************************************************************/
    @Override
    public void undo() throws CannotUndoException
    {
        button.setSelected( oldValue );
        if( updater != null )
        {
            updater.updateData();
        }
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#canUndo()
     **************************************************************************/
    @Override
    public boolean canUndo()
    {
        return true;
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#redo()
     **************************************************************************/
    @Override
    public void redo() throws CannotRedoException
    {
        button.setSelected( newValue );
        if( updater != null )
        {
            updater.updateData();
        }
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#canRedo()
     **************************************************************************/
    @Override
    public boolean canRedo()
    {
        return true;
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#die()
     **************************************************************************/
    @Override
    public void die()
    {
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#addEdit(javax.swing.undo.UndoableEdit)
     **************************************************************************/
    @Override
    public boolean addEdit( UndoableEdit anEdit )
    {
        return false;
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#replaceEdit(javax.swing.undo.UndoableEdit)
     **************************************************************************/
    @Override
    public boolean replaceEdit( UndoableEdit anEdit )
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#isSignificant()
     */
    @Override
    public boolean isSignificant()
    {
        return true;
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#getPresentationName()
     **************************************************************************/
    @Override
    public String getPresentationName()
    {
        return "Button pressed";
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#getUndoPresentationName()
     **************************************************************************/
    @Override
    public String getUndoPresentationName()
    {
        return "Undo button press";
    }

    /**************************************************************************
     * (non-Javadoc)
     * @see javax.swing.undo.UndoableEdit#getRedoPresentationName()
     **************************************************************************/
    @Override
    public String getRedoPresentationName()
    {
        return "Redo button press";
    }
}
