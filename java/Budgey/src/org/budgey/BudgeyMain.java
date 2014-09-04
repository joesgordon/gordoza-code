package org.budgey;

import java.io.File;

import javax.swing.JFrame;

import org.budgey.data.Budget;
import org.budgey.data.BudgeyOptions;
import org.budgey.ui.BudgeyFrame;
import org.jutils.io.IOUtils;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyMain implements IFrameApp
{
    /**  */
    public static final String OPTIONS_FILENAME = ".BudgeyOptions.cfg";
    /**  */
    public static final File OPTIONS_FILE = new File( IOUtils.USERS_DIR,
        OPTIONS_FILENAME );

    /**  */
    private BudgeyOptions options;

    /***************************************************************************
     * @param options
     **************************************************************************/
    public BudgeyMain( BudgeyOptions options )
    {
        this.options = options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        BudgeyFrame frame = new BudgeyFrame( options );

        frame.setSize( 640, 480 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setTitle( "Budgey" );
        frame.setBudget( new Budget() );

        return frame;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        BudgeyOptions options = BudgeyOptions.read( OPTIONS_FILE );

        FrameApplication.invokeLater( new BudgeyMain( options ) );
    }

    @Override
    public void finalizeGui()
    {
    }
}
