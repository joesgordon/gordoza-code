package org.jutils.chart.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jutils.*;
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
    private final JTable table;
    /**  */
    private final DataCellRenderer cellRenderer;

    /**  */
    private final Action saveAction;

    /**  */
    private Series series;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataSeriesView()
    {
        this.tableModel = new SeriesTableModel();
        this.table = new JTable( tableModel );
        this.cellRenderer = new DataCellRenderer();

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
        JScrollPane scrollPane = new JScrollPane( table );

        table.setDefaultRenderer( Double.class, this.cellRenderer );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( scrollPane, BorderLayout.CENTER );

        return panel;
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

        saveAction.setEnabled( series.getResourceFile() != null );

        cellRenderer.setSeries( series );
        tableModel.setSeries( series );
    }

    /***************************************************************************
     * @param pointIdx
     **************************************************************************/
    public void setSelected( int pointIdx )
    {
        table.getSelectionModel().setSelectionInterval( pointIdx, pointIdx );
        Utils.scrollToVisible( table, pointIdx, 0 );
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

    /***************************************************************************
     * 
     **************************************************************************/
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

            if( !isSelected )
            {
                Color bg = defaultBackground;
                if( series.data.isHidden( row ) )
                {
                    bg = Color.LIGHT_GRAY;
                    setBackground( bg );
                }
                setBackground( bg );
            }

            return this;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
            File tofile = files[0];
            File fromFile = view.series.getResourceFile();

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
                JOptionPane.showMessageDialog( view.getView(),
                    "Unable to save file: " + ex.getMessage(), "I/O Error",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
