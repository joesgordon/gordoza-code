package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import com.jgoodies.looks.Options;

public class UFontChooser extends JDialog
{
    private int closedOption = JOptionPane.CLOSED_OPTION;

    private InputListPanel fontNameInputList;

    private InputListPanel fontSizeInputList;

    private MutableAttributeSet attributes;

    private JCheckBox boldCheckBox = new JCheckBox( "Bold" );

    private JCheckBox italicCheckBox = new JCheckBox( "Italic" );

    private JCheckBox underlineCheckBox = new JCheckBox( "Underline" );

    private JCheckBox strikethroughCheckBox = new JCheckBox( "Strikethrough" );

    private JCheckBox subscriptCheckBox = new JCheckBox( "Subscript" );

    private JCheckBox superscriptCheckBox = new JCheckBox( "Superscript" );

    private ColorComboBox colorComboBox;

    private FontLabel previewLabel;

    public final static String[] fontNames;

    public final static String[] fontSizes;

    static
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontNames = ge.getAvailableFontFamilyNames();
        fontSizes = new String[] { "8", "9", "10", "11", "12", "14", "16",
            "18", "20", "22", "24", "26", "28", "36", "48", "72" };
    }

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public UFontChooser( JFrame owner )
    {
        super( owner, "Font Chooser", true );

        // ---------------------------------------------------------------------
        // Setup font panel.
        // ---------------------------------------------------------------------
        JPanel fontPanel = new JPanel( new GridBagLayout() );
        fontPanel.setBorder( BorderFactory.createTitledBorder( "Font" ) );

        fontNameInputList = new InputListPanel( fontNames, "Name:" );
        fontNameInputList.setToolTipText( "Font name" );

        fontSizeInputList = new InputListPanel( fontSizes, "Size:" );
        fontSizeInputList.setToolTipText( "Font size" );

        fontSizeInputList.setPreferredSize( fontNameInputList.getPreferredSize() );
        fontSizeInputList.setMinimumSize( fontNameInputList.getPreferredSize() );

        fontNameInputList.setMinimumSize( fontNameInputList.getPreferredSize() );

        fontPanel.add( fontNameInputList, new GridBagConstraints( 0, 0, 1, 1,
            0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 4, 4, 2 ), 0, 0 ) );

        fontPanel.add( fontSizeInputList, new GridBagConstraints( 1, 0, 1, 1,
            0.5, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 2, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup color panel.
        // ---------------------------------------------------------------------
        JPanel colorPanel = new JPanel( new GridBagLayout() );
        JLabel colorLabel = new JLabel( "Color:" );

        colorComboBox = new ColorComboBox();
        colorComboBox.setToolTipText( "Font color" );

        colorPanel.add( colorLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 2 ), 0, 0 ) );

        colorPanel.add( colorComboBox, new GridBagConstraints( 1, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 2, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup effects panel.
        // ---------------------------------------------------------------------
        JPanel effectsPanel = new JPanel( new GridBagLayout() );
        effectsPanel.setBorder( BorderFactory.createTitledBorder( "Effects" ) );

        boldCheckBox.setToolTipText( "Bold font" );

        italicCheckBox.setToolTipText( "Italic font" );

        underlineCheckBox.setToolTipText( "Underline font" );

        strikethroughCheckBox.setToolTipText( "Strikethrough font" );

        subscriptCheckBox.setToolTipText( "Subscript font" );

        superscriptCheckBox.setToolTipText( "Superscript font" );

        effectsPanel.add( boldCheckBox, new GridBagConstraints( 0, 0, 1, 1,
            1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 4, 2, 2 ), 0, 0 ) );
        effectsPanel.add( italicCheckBox, new GridBagConstraints( 1, 0, 1, 1,
            1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 2, 2, 2 ), 0, 0 ) );
        effectsPanel.add( underlineCheckBox, new GridBagConstraints( 2, 0, 1,
            1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets( 0, 2, 2, 4 ), 0, 0 ) );

        effectsPanel.add( strikethroughCheckBox, new GridBagConstraints( 0, 1,
            1, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets( 2, 4, 2, 2 ), 0, 0 ) );
        effectsPanel.add( subscriptCheckBox, new GridBagConstraints( 1, 1, 1,
            1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        effectsPanel.add( superscriptCheckBox, new GridBagConstraints( 2, 1, 1,
            1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets( 2, 2, 2, 4 ), 0, 0 ) );

        effectsPanel.add( colorPanel, new GridBagConstraints( 0, 2, 3, 1, 1.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup preview panel.
        // ---------------------------------------------------------------------
        JPanel previewPanel = new JPanel( new GridBagLayout() );
        previewPanel.setBorder( BorderFactory.createTitledBorder( "Preview" ) );

        previewLabel = new FontLabel( "Preview Font" );

        previewPanel.add( previewLabel, new GridBagConstraints( 0, 0, 1, 1,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup button panel.
        // ---------------------------------------------------------------------
        JPanel buttonPanel = new JPanel( new GridBagLayout() );
        JButton btOK = new JButton( "OK" );
        JButton btCancel = new JButton( "Cancel" );
        ActionListener okActionListener = new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                closedOption = JOptionPane.OK_OPTION;
                dispose();
            }
        };
        ActionListener cancelActionListener = new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                closedOption = JOptionPane.CANCEL_OPTION;
                dispose();
            }
        };

        btOK.setToolTipText( "Save and exit" );
        btOK.addActionListener( okActionListener );
        getRootPane().setDefaultButton( btOK );

        btCancel.setToolTipText( "Exit without save" );
        btCancel.addActionListener( cancelActionListener );

        buttonPanel.add( btOK, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 4,
                4, 2 ), 18, 0 ) );
        buttonPanel.add( btCancel, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                4, 2, 4, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Setup content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = new JPanel( new GridBagLayout() );

        contentPanel.add( fontPanel, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( effectsPanel, new GridBagConstraints( 0, 1, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        contentPanel.add( previewPanel, new GridBagConstraints( 0, 2, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 2 ) );
        contentPanel.add( buttonPanel, new GridBagConstraints( 0, 3, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        setContentPane( contentPanel );

        ListSelectionListener listSelectListener = new ListSelectionListener()
        {
            public void valueChanged( ListSelectionEvent e )
            {
                updatePreview();
            }
        };
        fontNameInputList.addListSelectionListener( listSelectListener );
        fontSizeInputList.addListSelectionListener( listSelectListener );

        ActionListener actionListener = new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                updatePreview();
            }
        };
        boldCheckBox.addActionListener( actionListener );
        italicCheckBox.addActionListener( actionListener );
        underlineCheckBox.addActionListener( actionListener );
        strikethroughCheckBox.addActionListener( actionListener );
        subscriptCheckBox.addActionListener( actionListener );
        superscriptCheckBox.addActionListener( actionListener );
        colorComboBox.addActionListener( actionListener );

        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setFontFamily( a, boldCheckBox.getFont().getFamily() );
        StyleConstants.setFontSize( a, 12 );
        setAttributes( a );
    }

    public void setAttributes( AttributeSet a )
    {
        attributes = new SimpleAttributeSet( a );
        String name = StyleConstants.getFontFamily( a );
        int size = StyleConstants.getFontSize( a );

        fontNameInputList.setSelected( name );
        fontSizeInputList.setSelectedInt( size );

        boldCheckBox.setSelected( StyleConstants.isBold( a ) );
        italicCheckBox.setSelected( StyleConstants.isItalic( a ) );
        underlineCheckBox.setSelected( StyleConstants.isUnderline( a ) );
        strikethroughCheckBox.setSelected( StyleConstants.isStrikeThrough( a ) );
        subscriptCheckBox.setSelected( StyleConstants.isSubscript( a ) );
        superscriptCheckBox.setSelected( StyleConstants.isSuperscript( a ) );

        colorComboBox.setSelectedItem( StyleConstants.getForeground( a ) );

        updatePreview();
    }

    public AttributeSet getAttributes()
    {
        if( attributes == null )
        {
            return null;
        }

        String font = fontNameInputList.getSelected();
        int size = fontSizeInputList.getSelectedInt();
        boolean bold = boldCheckBox.isSelected();
        boolean italic = italicCheckBox.isSelected();
        boolean underline = underlineCheckBox.isSelected();
        boolean strikeThrough = strikethroughCheckBox.isSelected();
        boolean subscript = subscriptCheckBox.isSelected();
        boolean superscript = superscriptCheckBox.isSelected();
        Color fg = colorComboBox.getSelectedColor();

        StyleConstants.setFontFamily( attributes, font );
        StyleConstants.setFontSize( attributes, size );
        StyleConstants.setBold( attributes, bold );
        StyleConstants.setItalic( attributes, italic );
        StyleConstants.setUnderline( attributes, underline );
        StyleConstants.setStrikeThrough( attributes, strikeThrough );
        StyleConstants.setSubscript( attributes, subscript );
        StyleConstants.setSuperscript( attributes, superscript );
        StyleConstants.setForeground( attributes, fg );

        return attributes;
    }

    public int getOption()
    {
        return closedOption;
    }

    public void setVisible( boolean b )
    {
        if( b )
        {
            closedOption = JOptionPane.CLOSED_OPTION;
        }
        super.setVisible( b );
    }

    protected void updatePreview()
    {
        getAttributes();
        previewLabel.setAttributes( attributes );
    }

    public static void main( String argv[] )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }

                UFontChooser dlg = new UFontChooser( new JFrame() );
                dlg.addWindowListener( new WindowAdapter()
                {
                    public void windowClosing( WindowEvent e )
                    {
                        System.exit( 0 );
                    }
                } );
                dlg.pack();
                dlg.setLocationByPlatform( true );
                dlg.setVisible( true );
            }
        } );
    }
}

class FontLabel extends JPanel
{
    private JTextPane textPane;

    public FontLabel( String text )
    {
        super( new GridBagLayout() );
        textPane = new JTextPane();

        textPane.setEditable( false );
        textPane.setBorder( null );
        textPane.setOpaque( false );
        textPane.setText( text );
        textPane.setFocusable( false );

        setBackground( Color.white );
        setForeground( Color.black );
        setOpaque( true );
        setBorder( new LineBorder( Color.black ) );
        setMinimumSize( new Dimension( 120, 80 ) );
        setPreferredSize( new Dimension( 120, 80 ) );
        add( textPane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );

        centerText();
    }

    private void centerText()
    {
        MutableAttributeSet standard = new SimpleAttributeSet();
        StyleConstants.setAlignment( standard, StyleConstants.ALIGN_CENTER );
        textPane.getStyledDocument().setParagraphAttributes( 0,
            textPane.getStyledDocument().getLength(), standard, true );
    }

    public void setAttributes( MutableAttributeSet s )
    {
        StyleConstants.setAlignment( s, StyleConstants.ALIGN_CENTER );
        textPane.getStyledDocument().setParagraphAttributes( 0,
            textPane.getStyledDocument().getLength(), s, true );
    }
}

class ColorComboBox extends JComboBox
{
    public ColorComboBox()
    {
        int[] values = new int[] { 0, 128, 192, 255 };
        for( int r = 0; r < values.length; r++ )
        {
            for( int g = 0; g < values.length; g++ )
            {
                for( int b = 0; b < values.length; b++ )
                {
                    Color c = new Color( values[r], values[g], values[b] );
                    addItem( c );
                }
            }
        }
        setRenderer( new ColorComboRenderer() );
    }

    public Color getSelectedColor()
    {
        Object obj = getSelectedItem();

        return obj == null ? null : ( Color )obj;
    }
}

class ColorComboRenderer extends JLabel implements ListCellRenderer
{
    private ColorIcon icon;

    private static final int margin = 3;

    public ColorComboRenderer()
    {
        super();
        this.setBorder( BorderFactory.createEmptyBorder( margin, margin,
            margin, margin ) );
        icon = new ColorIcon( Color.black );
        this.setIcon( icon );
    }

    public Component getListCellRendererComponent( JList list, Object obj,
        int row, boolean sel, boolean hasFocus )
    {
        icon.setColor( ( Color )obj );
        return this;
    }

    public void paint( Graphics g )
    {
        icon.setIconWidth( this.getWidth() - 2 * margin );
        icon.setIconHeight( this.getHeight() - 2 * margin );
        super.paint( g );
    }
}
