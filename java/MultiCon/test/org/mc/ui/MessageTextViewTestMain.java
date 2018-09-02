package org.mc.ui;

import java.awt.Container;

import javax.swing.JFrame;

import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

public class MessageTextViewTestMain
{
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new MessageTextViewTestApp() );
    }

    private static class MessageTextViewTestApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frame = new StandardFrameView();

            frame.setContent( createContent() );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            frame.setSize( 800, 800 );

            return frame.getView();
        }

        private Container createContent()
        {
            MessageTextView view = new MessageTextView();

            return view.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
