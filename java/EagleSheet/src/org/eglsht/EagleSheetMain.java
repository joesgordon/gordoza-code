package org.eglsht;

import org.jutils.ui.app.FrameApplication;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EagleSheetMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameApplication.invokeLater( new EagleSheetApp() );
    }
}
