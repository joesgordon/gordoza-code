package org.duak.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.duak.data.FileInfo;
import org.duak.model.Analyzer;
import org.duak.utils.HistoryList;
import org.jutils.IconConstants;
import org.jutils.concurrent.*;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.event.FileDropTarget.IFileDropEvent;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakFrame implements IView<JFrame>
{
    private StandardFrameView frameView;
    private JButton previousButton;
    private JButton nextButton;

    private final DuakPanel duakPanel;
    private final HistoryList<FileInfo> history;

    /***************************************************************************
     * 
     **************************************************************************/
    public DuakFrame()
    {
        this.frameView = new StandardFrameView();
        this.history = new HistoryList<FileInfo>();
        this.duakPanel = new DuakPanel();

        duakPanel.addFolderOpenedListener( new FolderOpenedListener() );

        JFrame frame = frameView.getView();

        frame.setTitle( "Disk Usage Analysis Kit" );

        frameView.setToolbar( createToolbar() );
        frameView.setContent( duakPanel );

        frame.setDropTarget( new FileDropTarget( new FileDropped( this ) ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    private void open( File file )
    {
        ProgressDialog dialog = new ProgressDialog( getView(),
            "Analysis Progress" );
        AnalysisThread analysisThread = new AnalysisThread( this, file, dialog );
        Stoppable stoppable = new Stoppable( analysisThread );
        Thread thread = new Thread( stoppable );

        dialog.setLocationRelativeTo( getView() );
        dialog.addCancelListener( new CancelListener( stoppable ) );
        thread.start();
        dialog.setVisible( true );
    }

    /***************************************************************************
     * @param results
     **************************************************************************/
    private void setResults( FileInfo results )
    {
        duakPanel.setResults( results );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        JButton button;

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        button.setFocusable( false );
        button.addActionListener( new OpenListener( this ) );
        toolbar.add( button );

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.BACK_16 ) );
        button.setFocusable( false );
        button.addActionListener( new BackListener() );
        button.setEnabled( false );
        toolbar.add( button );
        previousButton = button;

        button = new JButton(
            IconConstants.loader.getIcon( IconConstants.FORWARD_16 ) );
        button.setFocusable( false );
        button.addActionListener( new ForwardListener() );
        button.setEnabled( false );
        toolbar.add( button );
        nextButton = button;

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FolderOpenedListener implements ItemActionListener<FileInfo>
    {
        @Override
        public void actionPerformed( ItemActionEvent<FileInfo> event )
        {
            FileInfo result = event.getItem();
            history.add( result );
            setResults( result );
            refreshButtons();
        }
    }

    private void refreshButtons()
    {
        previousButton.setEnabled( history.hasPrevious() );
        nextButton.setEnabled( history.hasNext() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BackListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( history.hasPrevious() )
            {
                setResults( history.previous() );
                refreshButtons();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ForwardListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( history.hasNext() )
            {
                setResults( history.next() );
                refreshButtons();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements ActionListener
    {
        private final DuakFrame view;

        public OpenListener( DuakFrame view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFrame frame = view.getView();
            DirectoryChooser fd = new DirectoryChooser( frame );
            fd.setSelected( new File[] { new File( "/Files/JosephsFiles/" ) } );
            fd.setVisible( true );

            File [] selectedFiles = fd.getSelected();
            if( selectedFiles != null )
            {
                view.open( selectedFiles[0] );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private Stoppable stoppable;

        public CancelListener( Stoppable thread )
        {
            stoppable = thread;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            try
            {
                stoppable.stopAndWaitFor();
            }
            catch( InterruptedException ex )
            {
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AnalysisThread implements IStoppableTask
    {
        private final File dir;
        private final ProgressDialog dialog;
        private final DuakFrame view;

        public AnalysisThread( DuakFrame view, File directory,
            ProgressDialog progressDialog )
        {
            this.view = view;
            this.dir = directory;
            this.dialog = progressDialog;
        }

        @Override
        public void run( ITaskStopManager stopper )
        {
            Analyzer analyzer = new Analyzer();
            FileInfo results = analyzer.analyze( dir,
                dialog.getProgressReporter(), stopper );

            if( results != null )
            {
                SwingUtilities.invokeLater( new PanelUpdater( view, results,
                    dialog ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PanelUpdater implements Runnable
    {
        private FileInfo results;
        private ProgressDialog dialog;
        private final DuakFrame view;

        public PanelUpdater( DuakFrame view, FileInfo results,
            ProgressDialog dialog )
        {
            this.view = view;
            this.results = results;
            this.dialog = dialog;
        }

        @Override
        public void run()
        {
            dialog.dispose();
            view.history.clear();
            view.history.add( results );
            view.setResults( results );
            view.refreshButtons();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileDropped implements
        ItemActionListener<IFileDropEvent>
    {
        private final DuakFrame view;

        public FileDropped( DuakFrame view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<IFileDropEvent> event )
        {
            view.open( event.getItem().getFiles().get( 0 ) );
        }
    }
}
