package org.jutils.appgallery.apps;

import java.awt.Component;

import javax.swing.Icon;

import org.jutils.appgallery.ILibraryApp;
import org.jutils.chart.ChartIcons;
import org.jutils.chart.app.JChartApp;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartApp implements ILibraryApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Icon getIcon32()
    {
        return ChartIcons.loader.getIcon( ChartIcons.CHART_032 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "JChart";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component createApp()
    {
        IFrameApp app = new JChartApp();

        return app.createFrame();
    }
}
