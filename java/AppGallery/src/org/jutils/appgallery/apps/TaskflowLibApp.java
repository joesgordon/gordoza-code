package org.jutils.appgallery.apps;

import java.awt.Component;

import javax.swing.Icon;

import org.jutils.IconConstants;
import org.jutils.appgallery.ILibraryApp;
import org.taskflow.TaskflowApp;

public class TaskflowLibApp implements ILibraryApp
{
    @Override
    public Icon getIcon32()
    {
        return IconConstants.getIcon( IconConstants.CALENDAR_32 );
    }

    @Override
    public String getName()
    {
        return "Taskflow";
    }

    @Override
    public Component createApp()
    {
        TaskflowApp app = new TaskflowApp();

        return app.createFrame();
    }

}
