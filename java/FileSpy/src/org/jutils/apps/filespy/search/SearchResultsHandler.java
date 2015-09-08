package org.jutils.apps.filespy.search;

import java.util.List;

import javax.swing.SwingUtilities;

import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.apps.filespy.ui.SearchView;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.explorer.ExplorerItem;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchResultsHandler
{
    /**  */
    private final SearchView searchPanel;
    /**  */
    private final StatusBarPanel statusBar;

    /***************************************************************************
     * @param panel SearchPanel
     * @param statusBar
     **************************************************************************/
    public SearchResultsHandler( SearchView panel, StatusBarPanel statusBar )
    {
        this.searchPanel = panel;
        this.statusBar = statusBar;
    }

    /***************************************************************************
     * @param record SearchRecord
     **************************************************************************/
    public void addFile( SearchRecord record )
    {
        // LogUtils.printDebug( "Found record for file " +
        // record.getFile().getAbsolutePath() );
        SwingUtilities.invokeLater( new UiFileAdder( searchPanel, record ) );
    }

    /***************************************************************************
     * @param list List
     **************************************************************************/
    public void addFiles( List<? extends ExplorerItem> list )
    {
        SwingUtilities.invokeLater( new UiFilesAdder( searchPanel, list ) );
    }

    /***************************************************************************
     * @param messge
     **************************************************************************/
    public void addErrorMessage( String message )
    {
        SwingUtilities.invokeLater( new UiErrorHandler( message ) );
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void updateStatus( String message )
    {
        SwingUtilities.invokeLater( new StatusUpdater( message, statusBar ) );

    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class UiErrorHandler implements Runnable
    {
        private final String msg;

        public UiErrorHandler( String message )
        {
            msg = message;
        }

        @Override
        public void run()
        {
            System.err.println( msg );
            // TODO add error to UI.
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class UiFilesAdder implements Runnable
    {
        private final SearchView searchPanel;

        private final List<? extends ExplorerItem> list;

        public UiFilesAdder( SearchView panel,
            List<? extends ExplorerItem> list )
        {
            this.searchPanel = panel;
            this.list = list;
        }

        public void run()
        {
            searchPanel.addRecords( list );
            // LogUtils.printDebug( "Adding record: " +
            // record.getFile().toString()
            // );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class UiFileAdder implements Runnable
    {
        private final SearchView searchPanel;

        private final SearchRecord record;

        public UiFileAdder( SearchView panel, SearchRecord record )
        {
            searchPanel = panel;
            this.record = record;
        }

        public void run()
        {
            searchPanel.addRecord( record );
            // LogUtils.printDebug( "Adding record: " +
            // record.getFile().toString()
            // );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class StatusUpdater implements Runnable
    {
        /**  */
        private final StatusBarPanel statusBar;
        /**  */
        private final String msg;

        public StatusUpdater( String message, StatusBarPanel statusBar )
        {
            this.msg = message;
            this.statusBar = statusBar;
        }

        @Override
        public void run()
        {
            statusBar.setText( msg );
        }
    }
}
