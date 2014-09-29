package org.jutils.chart.app;

import javax.swing.JFrame;

import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartApp implements IFrameApp
{
    /**  */
    private final UserOptionsSerializer<UserData> userio;

    /***************************************************************************
     * @param userio
     **************************************************************************/
    public JChartApp( UserOptionsSerializer<UserData> userio )
    {
        this.userio = userio;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        JChartFrameView view = new JChartFrameView(
            JChartAppConstants.APP_NAME, userio );
        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.setSize( 700, 700 );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
