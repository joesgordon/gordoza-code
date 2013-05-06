package org.eglsht;

import javax.swing.SwingUtilities;

public class EagleSheetMain
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new EagleSheetRunner() );
    }
}
