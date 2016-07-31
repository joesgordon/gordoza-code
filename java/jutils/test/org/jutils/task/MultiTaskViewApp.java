package org.jutils.task;

import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;

import org.jutils.Utils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskViewApp implements IApplication
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getLookAndFeelName()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void createAndShowUi()
    {
        MultiTaskView view = new MultiTaskView();

        String message;
        ITaskView taskView;

        message = "Set 7 of 120: 24 Parameters" + Utils.NEW_LINE;
        message += "A01, A02, A03, A04, A05, A06, A07, A08, A09, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24";
        taskView = view.addTaskView( message );
        taskView.signalPercent( 34 );

        message = "Set 6 of 120: 24 Parameters" + Utils.NEW_LINE;
        message += "GPS01, GPS02, GPS03, GPS04, GPS05, GPS06, GPS07, GPS08, GPS09, GPS10, GPS11, GPS12, GPS13, GPS14, GPS15, GPS16, GPS17, GPS18, GPS19, GPS20, GPS21";
        taskView = view.addTaskView( message );
        taskView.signalPercent( 10 );

        view.setTitle( "Sets 5 of 120 completed" );
        view.setPercent( 500 / 120 );

        OkDialogView okView = new OkDialogView( null, view.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_CANCEL );
        JDialog dialog = okView.getView();

        dialog.setTitle( "Decoding 2849 parameters" );
        dialog.setSize( 400, 400 );
        dialog.setLocationRelativeTo( null );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new MultiTaskViewApp() );
    }
}
