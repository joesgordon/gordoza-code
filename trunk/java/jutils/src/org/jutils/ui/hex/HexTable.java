package org.jutils.ui.hex;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import org.jutils.PropConstants;
import org.jutils.ui.HighlightedLabel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexTable extends JTable
{
    /**  */
    private HexTableModel model;
    /**  */
    private ByteCellRenderer renderer;
    /**  */
    private ByteCellEditor editor;
    /**  */
    private int selectEnd;
    /**  */
    private int selectStart;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexTable()
    {
        editor = new ByteCellEditor();
        renderer = new ByteCellRenderer();
        model = new HexTableModel();

        setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
        setCellSelectionEnabled( true );
        setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        getTableHeader().setReorderingAllowed( false );
        setShowGrid( false );
        setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        // addKeyListener( new TableKeyListener() );

        ColHeaderRenderer colRenderer = new ColHeaderRenderer(
            getTableHeader().getDefaultRenderer() );

        getTableHeader().setDefaultRenderer( colRenderer );

        setModel( model );

        FontMetrics fm = getFontMetrics( getFont() );
        Font headerFont = UIManager.getFont( "TableHeader.font" );
        FontMetrics headerFM = getFontMetrics( headerFont );

        int w = Math.max( fm.stringWidth( "4444" ),
            headerFM.stringWidth( "4444" ) );

        for( int i = 0; i < getColumnCount(); i++ )
        {
            TableColumn column = getColumnModel().getColumn( i );
            column.setCellEditor( editor );

            if( i == getColumnCount() - 1 )
            {
                column.setPreferredWidth( w * 5 );
                column.setCellRenderer( new AsciiCellRenderer( this ) );
            }
            else
            {
                column.setPreferredWidth( w );
                column.setCellRenderer( renderer );
            }
            column.setResizable( false );
        }

        setPreferredScrollableViewportSize( new Dimension( w * 21,
            25 * getRowHeight() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( int row, int col )
    {
        if( getCellSelectionEnabled() )
        {
            if( col == 16 )
            {
                return false;
            }

            return true;
        }
        return false;
    }

    /***************************************************************************
     * @param buf
     **************************************************************************/
    public void setBuffer( IByteBuffer buf )
    {
        model.setBuffer( buf );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectStart()
    {
        return selectStart;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectEnd()
    {
        return selectEnd;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void changeSelection( int row, int col, boolean toggle,
        boolean extend )
    {
        if( isFocusable() )
        {
            col = calculateClosestValidColumn( row, col );

            selectEnd = calculateByteOffset( row, col );
            if( !extend )
            {
                selectStart = selectEnd;
            }

            repaint();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void clearSelection()
    {
        selectStart = -1;
        selectEnd = -1;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellSelected( int row, int col )
    {
        if( col == 16 )
        {
            return false;
        }

        int offset = row * 16 + col;
        int min = Math.min( selectStart, selectEnd );
        int max = Math.max( selectStart, selectEnd );

        return offset >= min && offset <= max;
    }

    /***************************************************************************
     * Calculate the offset into the data model's byte data for the specified
     * row and column.
     * @param row
     * @param col
     * @return
     **************************************************************************/
    private int calculateByteOffset( int row, int col )
    {
        if( row < 0 || row > getRowCount() || col < 0 || col > 16 )
        {
            return -1;
        }
        int offset = row * 16 + col;
        return ( offset >= 0 ) ? offset : -1;
    }

    /***************************************************************************
     * Calculates the closest valid column to the one specified by row and
     * column. This allows the user to obtain a valid selection when dragging
     * onto the non-data columns (byte location and string representation), as
     * well as any empty cells that may exist at the end of a stream.
     * @param row
     * @param col
     * @return
     **************************************************************************/
    private int calculateClosestValidColumn( int row, int col )
    {
        if( row == getRowCount() - 1 )
        {
            // last row
            int i = model.getBufferSize() % 16;
            return Math.min( i, col );
        }

        return Math.min( col, getColumnCount() - 1 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return model.getBuffer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class AutoSelector implements FocusListener
    {
        @Override
        public void focusGained( FocusEvent e )
        {
            ( ( JTextField )e.getSource() ).selectAll();
        }

        @Override
        public void focusLost( FocusEvent e )
        {
            ;
        }
    }

    /***************************************************************************
     * Filter that prevents the user from entering in text that does not parse
     * to a byte.
     **************************************************************************/
    private static class EditorDocumentFilter extends DocumentFilter
    {
        private Component comp;

        public EditorDocumentFilter( Component c )
        {
            comp = c;
        }

        private boolean isByte( String str )
        {
            try
            {
                int i = Integer.parseInt( str, 16 );
                if( i < 0 || i > 0xff )
                {
                    throw new NumberFormatException();
                }
            }
            catch( NumberFormatException nfe )
            {
                return false;
            }
            return true;
        }

        public void insertString( FilterBypass fb, int offs, String text,
            AttributeSet attr ) throws BadLocationException
        {
            Document doc = fb.getDocument();
            String str = doc.getText( 0, offs ) + text +
                doc.getText( offs, doc.getLength() - offs );

            if( isByte( str ) )
            {
                fb.insertString( offs, str, attr );
            }
            else
            {
                UIManager.getLookAndFeel().provideErrorFeedback( comp );
            }
        }

        public void replace( FilterBypass fb, int offs, int len, String text,
            AttributeSet attrs ) throws BadLocationException
        {
            Document doc = fb.getDocument();
            int endIndex = offs + len;
            String str = doc.getText( 0, offs ) + text +
                doc.getText( endIndex, doc.getLength() - endIndex );

            if( isByte( str ) )
            {
                fb.replace( offs, len, text, attrs );
            }
            else
            {
                UIManager.getLookAndFeel().provideErrorFeedback( comp );
            }
        }
    }

    private static class ByteCellEditor extends DefaultCellEditor
    {
        private JTextField field;

        public ByteCellEditor()
        {
            super( new JTextField() );

            field = ( JTextField )getComponent();

            field.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
            field.setHorizontalAlignment( SwingConstants.CENTER );
            field.setMargin( new Insets( 0, 0, 0, 0 ) );
            field.addFocusListener( new AutoSelector() );

            AbstractDocument doc = ( AbstractDocument )field.getDocument();
            doc.setDocumentFilter( new EditorDocumentFilter( field ) );
        }

        public boolean stopCellEditing()
        {
            // Prevent the user from entering empty string as a value.
            String value = getCellEditorValue().toString();

            if( value.length() == 0 )
            {
                UIManager.getLookAndFeel().provideErrorFeedback( null );
                return false;
            }

            return super.stopCellEditing();
        }
    }

    private static class AsciiCellRenderer implements TableCellRenderer
    {
        private HighlightedLabel label;

        private HexTable ht;

        public AsciiCellRenderer( HexTable table )
        {
            ht = table;
            label = new HighlightedLabel();

            label.setHorizontalAlignment( SwingConstants.CENTER );
            label.setHighlightColor( ByteCellRenderer.ALTERNATING_ROW_COLOR );
            label.setFont( loadFont() );
        }

        private Font loadFont()
        {
            Font f;

            f = new Font( "Monospaced", Font.PLAIN, 12 );

            return f;
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col )
        {
            int selectStart = ht.getSelectStart();
            int selectEnd = ht.getSelectEnd();

            label.setText( ( String )value );
            // label.setHorizontalAlignment( SwingConstants.LEFT );

            if( selectStart >= 0 )
            {
                int min = Math.min( selectStart, selectEnd );
                int max = Math.max( selectStart, selectEnd );
                if( min / 16 < row && max / 16 > row )
                {
                    label.setHighlight( 0, 16 );
                }
                else if( min / 16 == row )
                {
                    int off = min - ( row * 16 );
                    label.setHighlight( off, max - min + 1 );
                }
                else if( max / 16 == row )
                {
                    label.setHighlight( 0, max - ( row * 16 ) + 1 );
                }
                else
                {
                    label.clearHighlight();
                }
            }
            else
            {
                label.clearHighlight();
            }

            return label;
        }
    }

    private static class ByteCellRenderer extends DefaultTableCellRenderer
    {
        /**  */
        public static final Color ALTERNATING_ROW_COLOR = new Color( 210, 225,
            240 );

        /**  */
        private Color nullColor;

        public ByteCellRenderer()
        {
            super();

            nullColor = ( Color )UIManager.get( PropConstants.UI_PANEL_COLOR );

            setHorizontalAlignment( SwingConstants.CENTER );
        }

        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col )
        {
            super.getTableCellRendererComponent( table, value, isSelected,
                hasFocus, row, col );

            if( value == null )
            {
                setBackground( nullColor );
                setBorder( null );
            }
            else if( !isSelected )
            {
                if( row % 2 == 1 )
                {
                    setBackground( ALTERNATING_ROW_COLOR );
                }
                else
                {
                    setBackground( null );
                }
            }

            return this;
        }
    }

    private static class ColHeaderRenderer implements TableCellRenderer
    {
        private TableCellRenderer renderer;

        public ColHeaderRenderer( TableCellRenderer defaultRenderer )
        {
            renderer = defaultRenderer;
            Font f = new Font( "Monospaced", Font.PLAIN, 12 );

            DefaultTableCellRenderer r = ( DefaultTableCellRenderer )renderer;
            r.setHorizontalAlignment( SwingConstants.CENTER );
            r.setFont( f );
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int col )
        {
            Component c = renderer.getTableCellRendererComponent( table, value,
                isSelected, hasFocus, row, col );

            return c;
        }
    }
}
