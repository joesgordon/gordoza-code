package org.jutils.apps.filespy;

import java.util.List;

import javax.swing.SwingUtilities;

import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.explorer.ExplorerItem;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchHandler
{
    /**  */
    private final SearchPanel searchPanel;
    /**  */
    private final StatusBarPanel statusBar;

    /***************************************************************************
     * @param panel SearchPanel
     * @param statusBar
     **************************************************************************/
    public SearchHandler( SearchPanel panel, StatusBarPanel statusBar )
    {
        this.searchPanel = panel;
        this.statusBar = statusBar;
    }

    /***************************************************************************
     * @param record SearchRecord
     **************************************************************************/
    public void addFile( SearchRecord record )
    {
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
        private final SearchPanel searchPanel;

        private final List<? extends ExplorerItem> list;

        public UiFilesAdder( SearchPanel panel,
            List<? extends ExplorerItem> list )
        {
            this.searchPanel = panel;
            this.list = list;
        }

        public void run()
        {
            searchPanel.addRecords( list );
            // System.out.println( "Adding record: " +
            // record.getFile().toString()
            // );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class UiFileAdder implements Runnable
    {
        private final SearchPanel searchPanel;

        private final SearchRecord record;

        public UiFileAdder( SearchPanel panel, SearchRecord record )
        {
            searchPanel = panel;
            this.record = record;
        }

        public void run()
        {
            searchPanel.addRecord( record );
            // System.out.println( "Adding record: " +
            // record.getFile().toString()
            // );
        }
    }

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
