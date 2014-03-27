package org.duak;

import org.jutils.ui.app.FrameApplication;

public class DuakMain
{
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new DuakApp() );
    }
}
