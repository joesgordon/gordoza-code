package org.jutils.apps.filespy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.UClosableTabbedPane;
import org.jutils.ui.event.TabRemovedListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyPanel extends JPanel
{
    /**  */
    private final UClosableTabbedPane tabbedPane;
    /**  */
    private StatusBarPanel statusBar;
    /**  */
    private int searchNum = 0;
    /**  */
    private final List<SearchPanel> searchPanels;

    /***************************************************************************
     *
     **************************************************************************/
    public FileSpyPanel()
    {
        this.searchPanels = new ArrayList<SearchPanel>();
        this.searchNum = 0;
        this.tabbedPane = new UClosableTabbedPane();
        this.statusBar = null;

        tabbedPane.addTabRemovedListener( new SearchPanelRemovedListener(
            searchPanels ) );

        this.setLayout( new BorderLayout() );
        this.add( tabbedPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param statusBar StatusBarPanel
     **************************************************************************/
    public void setStatusBar( StatusBarPanel statusBar )
    {
        this.statusBar = statusBar;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void addNewSearch()
    {
        SearchParams params = new SearchParams();
        params.name = "Search " + searchNum;
        searchNum++;

        addSearch( params );
    }

    /***************************************************************************
     * @param params SearchParams
     **************************************************************************/
    public void addSearch( SearchParams params )
    {
        SearchPanel panel = new SearchPanel();

        searchPanels.add( panel );

        panel.setStatusBar( statusBar );
        tabbedPane.addTab( params.name, panel.getView() );
        tabbedPane.setSelectedComponent( panel.getView() );
    }

    /***************************************************************************
     * @return SearchParams
     **************************************************************************/
    public SearchParams getSelectedSearch()
    {
        int index = tabbedPane.getSelectedIndex();

        if( index > -1 )
        {
            return searchPanels.get( index ).getSearchParams();
        }

        return null;
    }

    private static class SearchPanelRemovedListener implements
        TabRemovedListener
    {
        private final List<SearchPanel> searchPanels;

        public SearchPanelRemovedListener( List<SearchPanel> searchPanels )
        {
            this.searchPanels = searchPanels;
        }

        public void tabRemoved( Component comp, int index )
        {
            // System.out.println( "Tab removed at " + index );

            searchPanels.remove( index ).clearPanel();
        }
    }
}
