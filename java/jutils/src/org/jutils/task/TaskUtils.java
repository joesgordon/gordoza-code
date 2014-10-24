package org.jutils.task;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JOptionPane;

import org.jutils.Utils;
import org.jutils.ui.ExtensiveErrorView;
import org.jutils.ui.MessageExceptionView;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class TaskUtils
{
    private TaskUtils()
    {
    }

    /***************************************************************************
     * @param error
     **************************************************************************/
    public static void displayError( Component comp, TaskError error )
    {
        Window parent = Utils.getComponentsWindow( comp );

        if( error.exception != null )
        {
            MessageExceptionView.showExceptionDialog( parent, error.message,
                error.name, error.exception );
            error.exception.printStackTrace();
        }
        else if( error.description != null )
        {
            ExtensiveErrorView view = new ExtensiveErrorView();

            view.setErrors( error.message, error.description );

            JOptionPane.showMessageDialog( parent, view.getView(), error.name,
                JOptionPane.ERROR_MESSAGE );
        }
        else
        {
            JOptionPane.showMessageDialog( parent, error.message, error.name,
                JOptionPane.ERROR_MESSAGE );
        }
    }
}
