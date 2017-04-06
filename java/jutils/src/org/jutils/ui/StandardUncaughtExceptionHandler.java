package org.jutils.ui;

import javax.swing.JFrame;

import org.jutils.SwingUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardUncaughtExceptionHandler
    implements Thread.UncaughtExceptionHandler
{
    /**  */
    private final JFrame frame;
    /**  */
    private final MessageExceptionView exView;

    /***************************************************************************
     * @param frame
     **************************************************************************/
    public StandardUncaughtExceptionHandler( JFrame frame )
    {
        this.frame = frame;
        this.exView = new MessageExceptionView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void uncaughtException( Thread thread, Throwable ex )
    {
        ex.printStackTrace();

        if( !exView.getView().isShowing() )
        {
            try
            {
                displayException( ex );
            }
            catch( Throwable th )
            {
                th.printStackTrace();
                System.exit( -2 );
            }
        }
    }

    /***************************************************************************
     * @param ex
     **************************************************************************/
    private void displayException( Throwable ex )
    {
        exView.setMessage( "The following error has occurred. You may " +
            "choose to ignore and continue or quit." );

        exView.setException( ex );

        String [] choices = new String[] { "Continue", "Quit" };
        String choice = SwingUtils.showConfirmMessage( frame, exView.getView(),
            "Unhandled Error", choices, choices[1], true );

        if( choice == null || choice.equals( choices[1] ) )
        {
            System.exit( 1 );
        }
    }
}
