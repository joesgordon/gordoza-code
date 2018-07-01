package org.jutils.appgallery;

import java.util.ArrayList;
import java.util.List;

import org.budgey.BudgeyTool;
import org.duak.DuakTool;
import org.eglsht.SheetTool;
import org.jutils.apps.filespy.FileSpyTool;
import org.jutils.apps.jexplorer.JExplorerTool;
import org.jutils.apps.jhex.JHexTool;
import org.jutils.chart.app.JChartTool;
import org.jutils.ui.IToolView;
import org.jutils.ui.app.FrameRunner;
import org.taskflow.TaskflowLibTool;

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
        apps.add( new JHexTool() );
        apps.add( new JExplorerTool() );
        apps.add( new ChatterboxTool() );
        apps.add( new BudgeyTool() );
        apps.add( new DuakTool() );
        apps.add( new SheetTool() );
        apps.add( new JChartTool() );
        apps.add( new TaskflowLibTool() );

        return apps;
    }
}
