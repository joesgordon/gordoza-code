package org.duak.ui;

import java.awt.BorderLayout;
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
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DuakFrame extends JFrame
{
    JButton previousButton;
    JButton nextButton;

    private DuakPanel duakPanel;
    private HistoryList<FileInfo> history;

    /***************************************************************************
     * 
     **************************************************************************/
    public DuakFrame()
    {
        history = new HistoryList<FileInfo>();

        setTitle( "Disk Usage Analysis Kit" );

        setContentPane( createContentPanel() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createContentPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        duakPanel = new DuakPanel();

        duakPanel.addFolderOpenedListener( new FolderOpenedListener() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( duakPanel, BorderLayout.CENTER );
        panel.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        return panel;
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
        button.addActionListener( new OpenListener() );
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
     * @param results
     **************************************************************************/
    private void setResults( FileInfo results )
    {
        duakPanel.setResults( results );
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
    private class OpenListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFrame frame = DuakFrame.this;
            DirectoryChooser fd = new DirectoryChooser( frame );
            fd.setSelected( new File[] { new File( "/Files/JosephsFiles/" ) } );
            fd.setVisible( true );

            File[] selectedFiles = fd.getSelected();
            if( selectedFiles != null )
            {
                ProgressDialog dialog = new ProgressDialog( frame,
                    "Analysis Progress" );
                AnalysisThread analysisThread = new AnalysisThread(
                    selectedFiles[0], dialog );
                Stoppable stoppable = new Stoppable( analysisThread );
                Thread thread = new Thread( stoppable );

                dialog.setLocationRelativeTo( frame );
                dialog.addCancelListener( new CancelListener( stoppable ) );
                thread.start();
                dialog.setVisible( true );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class CancelListener implements ActionListener
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
    private class AnalysisThread implements IStoppableTask
    {
        private final File dir;
        private final ProgressDialog dialog;

        public AnalysisThread( File directory, ProgressDialog progressDialog )
        {
            dir = directory;
            dialog = progressDialog;
        }

        @Override
        public void run( ITaskStopManager stopper )
        {
            Analyzer analyzer = new Analyzer();
            FileInfo results = analyzer.analyze( dir,
                dialog.getProgressReporter(), stopper );

            if( results != null )
            {
                SwingUtilities.invokeLater( new PanelUpdater( results, dialog ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class PanelUpdater implements Runnable
    {
        private FileInfo results;
        private ProgressDialog dialog;

        public PanelUpdater( FileInfo results, ProgressDialog dialog )
        {
            this.results = results;
            this.dialog = dialog;
        }

        @Override
        public void run()
        {
            dialog.dispose();
            history.clear();
            history.add( results );
            setResults( results );
            refreshButtons();
        }
    }
}
