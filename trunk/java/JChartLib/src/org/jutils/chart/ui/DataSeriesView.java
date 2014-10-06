package org.jutils.chart.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.io.DataLineReader;
import org.jutils.chart.model.Series;
import org.jutils.io.IOUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataSeriesView implements IDataView<Series>
{
    /**  */
    private final JPanel view;
    /**  */
    private final SeriesTableModel tableModel;
    /**  */
    private final DataCellRenderer cellRenderer;

    /**  */
    private Series series;
    private File file;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataSeriesView()
    {
        this.tableModel = new SeriesTableModel();
        this.cellRenderer = new DataCellRenderer();

        this.view = createView();

        this.file = null;
        this.series = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JTable table = new JTable( tableModel );
        JScrollPane scrollPane = new JScrollPane( table );

        table.setDefaultRenderer( Double.class, this.cellRenderer );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( scrollPane, BorderLayout.CENTER );

        return panel;
    }

    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();
        Action action;
        ActionListener listener;

        listener = new FileChooserListener( view, "Choose File to Save",
            new SaveListener( this ), true );
        action = new ActionAdapter( listener, "Save",
            IconConstants.loader.getIcon( IconConstants.SAVE_AS_16 ) );
        SwingUtils.addActionToToolbar( toolbar, action );

        return toolbar;
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

        cellRenderer.setSeries( series );
        tableModel.setSeries( series );
    }

    public void setFile( File file )
    {
        this.file = file;
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
    private static class SeriesTableModel extends AbstractTableModel
    {
        private Series series;

        public SeriesTableModel()
        {
            series = null;
        }

        @Override
        public int getRowCount()
        {
            return series.data.getCount();
        }

        @Override
        public int getColumnCount()
        {
            return 2;
        }

        public Class<?> getColumnClass( int columnIndex )
        {
            return Double.class;
        }

        public String getColumnName( int col )
        {
            switch( col )
            {
                case 0:
                    return "X";

                case 1:
                    return "Y";

                default:
                    throw new IllegalArgumentException(
                        "Column is out of bounds: " + col );
            }
        }

        @Override
        public Object getValueAt( int row, int col )
        {
            switch( col )
            {
                case 0:
                    return series.data.getX( row );

                case 1:
                    return series.data.getY( row );

                default:
                    throw new IllegalArgumentException(
                        "Column is out of bounds: " + col );
            }
        }

        public void setSeries( Series series )
        {
            this.series = series;
        }
    }

    private static class DataCellRenderer extends DefaultTableCellRenderer
    {
        private final Color defaultBackground;

        private Series series;

        public DataCellRenderer()
        {
            this.defaultBackground = super.getBackground();
        }

        public void setSeries( Series series )
        {
            this.series = series;
        }

        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col )
        {
            super.getTableCellRendererComponent( table, value, isSelected,
                hasFocus, row, col );

            Color bg = defaultBackground;

            if( !isSelected && series.data.isHidden( row ) )
            {
                bg = Color.LIGHT_GRAY;
                setBackground( bg );
            }

            setBackground( bg );

            return this;
        }
    }

    private static class SaveListener implements IFileSelectionListener
    {
        private DataSeriesView view;

        public SaveListener( DataSeriesView view )
        {
            this.view = view;
        }

        @Override
        public File getDefaultFile()
        {
            File file = view.file;

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
            File tofile = files[0];
            File fromFile = view.file;

            if( fromFile == null )
            {
                // TODO display error
                return;
            }

            try( FileReader fr = new FileReader( fromFile );
                 BufferedReader reader = new BufferedReader( fr ) )
            {
                try( PrintStream stream = new PrintStream( tofile ) )
                {
                    DataLineReader lineReader = new DataLineReader();
                    String line = null;
                    int idx = 0;
                    XYPoint point = null;

                    while( ( line = reader.readLine() ) != null )
                    {
                        point = lineReader.read( line );

                        if( point != null )
                        {
                            if( !view.series.data.isHidden( idx ) )
                            {
                                stream.println( line );
                            }

                            idx++;
                        }
                        else
                        {
                            stream.println( line );
                        }
                    }
                }
            }
            catch( IOException ex )
            {
                // TODO display error
            }
        }
    }
}
