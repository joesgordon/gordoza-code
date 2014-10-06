package org.jutils.chart.app;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.chart.ChartIcons;
import org.jutils.chart.ChartUtils;
import org.jutils.chart.model.ISeriesData;
import org.jutils.chart.model.Series;
import org.jutils.chart.ui.*;
import org.jutils.io.IOUtils;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.*;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final ChartView chartView;
    /**  */
    private final RecentFilesMenuView recentFiles;

    /**  */
    private final Action openAction;
    /**  */
    private final Action saveAction;

    /**  */
    private final UserOptionsSerializer<UserData> userio;

    /***************************************************************************
     * @param title
     * @param userio
     **************************************************************************/
    public JChartFrameView( String title, UserOptionsSerializer<UserData> userio )
    {
        this.userio = userio;

        this.frameView = new StandardFrameView();
        this.chartView = new ChartView();
        this.recentFiles = new RecentFilesMenuView();

        this.openAction = createOpenAction();
        this.saveAction = createSaveAction();

        createMenubar( frameView.getMenuBar(), frameView.getFileMenu() );
        frameView.setToolbar( createToolbar() );
        frameView.setContent( chartView.getView() );

        JFrame frame = frameView.getView();

        frame.setTitle( title );
        frame.setIconImages( ChartIcons.getChartImages() );

        chartView.addFileLoadedListener( new FileLoadedListener( this ) );

        recentFiles.setData( userio.getOptions().recentFiles.toList() );
        recentFiles.addSelectedListener( new FileSelected( this ) );

        Series s;
        ISeriesData data;

        // data = ChartUtils.createTestSeries();
        // s = new Series( data );
        // s.line.weight = 4;
        // s.isPrimaryDomain = true;
        // s.isPrimaryRange = true;
        // chartView.addSeries( s );

        data = ChartUtils.createLineSeries( 1000000, 1.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.line.weight = 4;
        s.isPrimaryDomain = false;
        s.isPrimaryRange = false;
        chartView.addSeries( s );

        data = ChartUtils.createLineSeries( 1000000, -1.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.marker.color = new Color( 0xFF9933 );
        s.highlight.color = new Color( 0xFF9933 );
        s.line.color = new Color( 0xCC6622 );
        chartView.addSeries( s, true );

        data = ChartUtils.createSinSeries( 1000000, 1.0, 4.0, 0.0, -5.0, 5.0 );
        s = new Series( data );
        s.marker.color = new Color( 0x339933 );
        s.highlight.color = new Color( 0x339933 );
        s.line.color = new Color( 0x227722 );
        s.line.weight = 4;
        chartView.addSeries( s, true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Action action;
        FileChooserListener fcListener;
        Icon icon;
        String name;

        name = "Open";
        icon = IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 );
        fcListener = new FileChooserListener( getView(), "Choose Data File",
            new OpenListener( this ), false );
        action = new ActionAdapter( fcListener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Action action;
        FileChooserListener fcListener;
        Icon icon;
        String name;

        name = "Save";
        icon = IconConstants.loader.getIcon( IconConstants.SAVE_16 );
        fcListener = new FileChooserListener( getView(), "Choose Image File",
            new SaveListener( this ), true );
        action = new ActionAdapter( fcListener, name, icon );

        return action;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, openAction );

        SwingUtils.addActionToToolbar( toolbar, saveAction );

        return toolbar;
    }

    /***************************************************************************
     * @param menuBar
     * @return
     **************************************************************************/
    private JMenuBar createMenubar( JMenuBar menubar, JMenu fileMenu )
    {
        menubar.add( createFileMenu( fileMenu ) );

        menubar.add( createViewMenu() );

        return menubar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createFileMenu( JMenu menu )
    {
        JMenuItem item;
        int i = 0;

        item = new JMenuItem( openAction );
        menu.add( item, i++ );

        item = new JMenuItem( saveAction );
        menu.add( item, i++ );

        menu.add( recentFiles.getView(), i++ );

        item = new JMenuItem( "Clear" );
        item.addActionListener( new ClearListener( this ) );
        menu.add( item, i++ );

        menu.add( new JSeparator(), i++ );

        return menu;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenu createViewMenu()
    {
        JMenu menu = new JMenu( "View" );
        JMenuItem item;

        item = new JMenuItem( "Data" );
        item.addActionListener( new DataDialogListener( this ) );
        menu.add( item );

        return menu;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DataDialogListener implements ActionListener
    {
        private final OkDialog dialog;
        private final DataView dataView;

        public DataDialogListener( JChartFrameView view )
        {
            this.dataView = new DataView();
            this.dialog = new OkDialog( dataView, view.getView() );

            JDialog d = dialog.getView();

            d.setSize( 300, 300 );
            d.validate();
            d.setLocationRelativeTo( view.getView() );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JDialog d = dialog.getView();

            if( !d.isVisible() )
            {
                d.setVisible( true );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ClearListener implements ActionListener
    {
        private final JChartFrameView view;

        public ClearListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.chartView.clear();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileLoadedListener implements ItemActionListener<File>
    {
        private final JChartFrameView view;

        public FileLoadedListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            view.userio.getOptions().recentFiles.push( event.getItem() );
            view.userio.write();
            view.recentFiles.setData( view.userio.getOptions().recentFiles.toList() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OpenListener implements IFileSelectionListener
    {
        private final JChartFrameView view;

        public OpenListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            return view.userio.getOptions().recentFiles.first();
        }

        @Override
        public void filesChosen( File [] files )
        {
            List<File> fileList = Arrays.asList( files );

            view.chartView.importData( fileList );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener implements IFileSelectionListener
    {
        private final JChartFrameView view;

        public SaveListener( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            File f = view.userio.getOptions().lastImageFile;

            if( f == null )
            {
                f = IOUtils.replaceExtension(
                    view.userio.getOptions().recentFiles.first(), "png" );
            }

            return f;
        }

        @Override
        public void filesChosen( File [] files )
        {
            File f = files[0];

            view.userio.getOptions().lastImageFile = f;
            view.userio.write();

            view.chartView.saveAsImage( f );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileSelected implements ItemActionListener<File>
    {
        private final JChartFrameView view;

        public FileSelected( JChartFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<File> event )
        {
            view.chartView.importData( event.getItem(), false );
        }
    }
}
