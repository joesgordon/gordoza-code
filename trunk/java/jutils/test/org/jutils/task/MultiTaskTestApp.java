package org.jutils.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;

public class MultiTaskTestApp implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setToolbar( createToolbar( frame ) );

        frame.setTitle( "Testing Multi Tasking" );
        frame.setIconImages( IconConstants.getPageMagImages() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 400, 400 );

        return frame;
    }

    private JToolBar createToolbar( JFrame frame )
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createGoAction( frame ) );

        return toolbar;
    }

    private Action createGoAction( JFrame frame )
    {
        Action action;
        ActionListener listener;
        Icon icon;
        String name;

        name = "Go";
        icon = IconConstants.loader.getIcon( IconConstants.FORWARD_16 );
        listener = new GoListener( frame );
        action = new ActionAdapter( listener, name, icon );

        return action;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String [] args )
    {
        FrameApplication.invokeLater( new MultiTaskTestApp() );
    }

    private static class GoListener implements ActionListener
    {
        private final JFrame frame;

        public GoListener( JFrame frame )
        {
            this.frame = frame;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            MultiTaskView.startAndShow( frame, new SampleTasker(),
                "Testing 1... 2... 3..", 4 );
        }
    }

    private static class SampleTasker implements ITasker
    {
        private final List<SampleTask> tasks;

        private int i;

        public SampleTasker()
        {
            this.tasks = new ArrayList<>();

            for( int i = 0; i < 10; i++ )
            {
                tasks.add( new SampleTask( ( i + 1 ) + " of " + 10 ) );
            }

            this.i = 0;
        }

        @Override
        public ITask nextTask()
        {
            ITask task = null;

            synchronized( tasks )
            {
                task = i < tasks.size() ? tasks.get( i ) : null;
                i++;
            }

            return task;
        }

        @Override
        public int getTaskCount()
        {
            return 10;
        }

        @Override
        public String getTaskAction()
        {
            return "Testing task";
        }
    }

    private static class SampleTask implements ITask
    {
        private final String name;
        private final long millis;

        public SampleTask( String name )
        {
            this.name = name;
            this.millis = 500 + new Random().nextInt( 500 );
        }

        @Override
        public void run( ITaskHandler handler )
        {
            for( int i = 0; i < 10 && handler.canContinue(); i++ )
            {
                int percent = i * 100 / 10;

                handler.signalPercent( percent );

                LogUtils.printDebug( "Percent : " + percent );

                try
                {
                    Thread.sleep( millis );
                }
                catch( InterruptedException e )
                {
                    break;
                }
            }

            handler.signalPercent( 100 );
        }

        @Override
        public String getName()
        {
            return name;
        }
    }
}
