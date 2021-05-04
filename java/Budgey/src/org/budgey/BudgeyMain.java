package org.budgey;

import java.io.File;

import org.budgey.data.BudgeyOptions;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.options.IOptionsCreator;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.io.xs.XsOptions;
import org.jutils.core.ui.app.FrameRunner;

/*******************************************************************************
 * Defines the main method for this application.
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
     * Starts the Budgey application with the provided arguments.
     * @param args empty, the file to open, or ignored.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new BudgeyApp() );
    }

    /***************************************************************************
     * Creates or returns the user options serializer for this application.
     * @return the user options serializer for this application.
     **************************************************************************/
    public static OptionsSerializer<BudgeyOptions> getOptions()
    {
        if( options == null )
        {
            options = XsOptions.getOptions( BudgeyOptions.class, OPTIONS_FILE,
                new BudgeyOptionsCreator() );
        }

        return options;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class BudgeyOptionsCreator
        implements IOptionsCreator<BudgeyOptions>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public BudgeyOptions createDefaultOptions()
        {
            return new BudgeyOptions();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BudgeyOptions initialize( BudgeyOptions options )
        {
            return new BudgeyOptions( options );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void warn( String message )
        {
        }
    }
}
