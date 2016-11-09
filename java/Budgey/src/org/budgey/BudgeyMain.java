package org.budgey;

import java.io.File;

import org.budgey.data.BudgeyOptions;
import org.jutils.io.IOUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyMain
{
    /**  */
    public static final String OPTIONS_FILENAME = ".BudgeyOptions.cfg";
    /**  */
    public static final File OPTIONS_FILE = IOUtils.getUsersFile(
        OPTIONS_FILENAME );

    /**  */
    private static OptionsSerializer<BudgeyOptions> options;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new BudgeyApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<BudgeyOptions> getOptions()
    {
        if( options == null )
        {
            options = new OptionsSerializer<>( new BudgeyOptionsCreator(),
                OPTIONS_FILE );
        }

        return options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class BudgeyOptionsCreator
        implements IOptionsCreator<BudgeyOptions>
    {
        @Override
        public BudgeyOptions createDefaultOptions()
        {
            return new BudgeyOptions();
        }

        @Override
        public BudgeyOptions initialize( BudgeyOptions options )
        {
            return new BudgeyOptions( options );
        }

        @Override
        public void warn( String message )
        {
        }
    }
}
