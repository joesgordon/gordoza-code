package org.jutils.chart.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import org.jutils.*;
import org.jutils.chart.io.FilteredWriter;
import org.jutils.chart.model.Series;
import org.jutils.io.IOUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SeriesPropertiesView implements IDataView<Series>
{
    /**  */
    private final JPanel view;
    /**  */
    private final SeriesDataView dataView;
    /**  */
    private final SeriesView seriesView;

    /**  */
    private final Action saveAction;

    /**  */
    private Series series;

    /***************************************************************************
     * 
     **************************************************************************/
    public SeriesPropertiesView()
    {
        this.dataView = new SeriesDataView();
        this.seriesView = new SeriesView();

        this.saveAction = createSaveAction();

        this.view = createView();

        this.series = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( createPanels(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createPanels()
    {
        JTabbedPane tabs = new JTabbedPane();
        JScrollPane propPane = new JScrollPane( seriesView.getView() );

        tabs.addTab( "Properties", propPane );
        tabs.addTab( "Data", dataView.getView() );

        return tabs;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, saveAction );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Action action;
        ActionListener listener;
        Icon icon;

        listener = new FileChooserListener( view, "Choose File to Save",
            new SaveListener( this ), true );
        icon = IconConstants.loader.getIcon( IconConstants.SAVE_AS_16 );
        action = new ActionAdapter( listener, "Save", icon );

        return action;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Series getData()
    {
        return series;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Series series )
    {
        this.series = series;

        dataView.setData( series.data );
        seriesView.setData( series );
    }

    /***************************************************************************
     * @param pointIdx
     **************************************************************************/
    public void setSelected( int pointIdx )
    {
        dataView.setSelected( pointIdx );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SaveListener implements IFileSelectionListener
    {
        private SeriesPropertiesView view;

        public SaveListener( SeriesPropertiesView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            File file = view.series.getResourceFile();

            if( file != null )
            {
                String ext = IOUtils.getFileExtension( file );
                String name = IOUtils.removeFilenameExtension( file );

                file = new File( file.getParentFile(), name + "_filtered." +
                    ext );
            }

            return file;
        }

        @Override
        public void filesChosen( File [] files )
        {
            File toFile = files[0];
            File fromFile = view.series.getResourceFile();
            FilteredWriter fw = new FilteredWriter();

            try
            {
                if( fromFile == null )
                {
                    fw.write( toFile, view.series.data );
                }
                else
                {
                    fw.write( fromFile, toFile, view.series.data );
                }
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog(
                    view.getView(),
                    "Unable to save file: " + Utils.NEW_LINE +
                        toFile.getAbsolutePath() + Utils.NEW_LINE +
                        Utils.NEW_LINE + ex.getMessage(), "I/O Error",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
