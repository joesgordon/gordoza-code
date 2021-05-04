package org.jutils.appgallery;

import java.util.ArrayList;
import java.util.List;

import org.budgey.BudgeyTool;
import org.eglsht.SheetTool;
import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.duak.DuakTool;
import org.jutils.explorer.ExplorerTool;
import org.jutils.filespy.FileSpyTool;
import org.jutils.hexedit.HexeditTool;
import org.jutils.insomnia.InsomniaTool;
import org.jutils.mines.MinesTool;
import org.jutils.plot.app.PlotTool;
import org.jutils.summer.SummerTool;
import org.taskflow.TaskflowTool;

import chatterbox.ChatterboxTool;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class AppGalleryMain
{
    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new AppGalleryApp() );
    }

    /***************************************************************************
     * Defines the tools displayed by this application.
     * @return the list of tools in this application.
     **************************************************************************/
    public static List<IToolView> getTools()
    {
        List<IToolView> apps = new ArrayList<IToolView>();

        apps.add( new FileSpyTool() );
        apps.add( new HexeditTool() );
        apps.add( new ExplorerTool() );
        apps.add( new ChatterboxTool() );
        apps.add( new BudgeyTool() );
        apps.add( new DuakTool() );
        apps.add( new SheetTool() );
        apps.add( new PlotTool() );
        apps.add( new TaskflowTool() );
        apps.add( new InsomniaTool() );
        apps.add( new MinesTool() );
        apps.add( new SummerTool() );

        return apps;
    }
}
