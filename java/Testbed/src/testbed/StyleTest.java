package testbed;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.*;

import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class StyleTest implements IView<JComponent>
{
    /**  */
    private final JComponent view;
    /**  */
    private final JScrollPane pane;
    /**  */
    private final JTextPane textPane;
    /**  */
    private final JToggleButton boldButton;
    /**  */
    private final DefaultStyledDocument styledDocument;
    /**  */
    private final Style boldStyle;
    /**  */
    private final Style ulStyle;

    /***************************************************************************
     *
     **************************************************************************/
    public StyleTest()
    {
        this.pane = new JScrollPane();
        this.textPane = new JTextPane();
        this.boldButton = new JToggleButton();
        this.styledDocument = new DefaultStyledDocument();
        this.boldStyle = styledDocument.addStyle( "bold", null );
        this.ulStyle = styledDocument.addStyle( "ul", null );

        JPanel panel = new JPanel();
        panel.setLayout( new BorderLayout() );

        boldButton.setText( "Bold It" );
        boldButton.addActionListener( ( e ) -> doBold() );

        textPane.setDocument( styledDocument );
        textPane.addCaretListener( ( e ) -> updateButton( e ) );
        pane.setViewportView( textPane );

        panel.add( pane, BorderLayout.CENTER );
        panel.add( boldButton, BorderLayout.SOUTH );
        addText();

        this.view = panel;
    }

    private void updateButton( CaretEvent e )
    {
        int selectionStart = e.getDot();
        int selectionEnd = e.getMark();
        int selectionMin = selectionStart < selectionEnd ? selectionStart
            : selectionEnd;

        Element run = styledDocument.getCharacterElement( selectionMin );
        MutableAttributeSet attr = ( MutableAttributeSet )run.getAttributes();

        boldButton.setSelected( attr.containsAttributes( boldStyle ) );
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     *
     **************************************************************************/
    private void doBold()
    {
        AttributeSet current = textPane.getCharacterAttributes();
        if( current == null )
        {
            // if no currently selected text, apply bold to anything
            // typed in the future
            // at this location.
            current = textPane.getInputAttributes();
        }
        MutableAttributeSet attr = new SimpleAttributeSet();
        boolean bold = false;
        if( current.getAttribute( StyleConstants.Bold ) != null )
        {
            Element run = styledDocument.getCharacterElement(
                textPane.getSelectionStart() );
            MutableAttributeSet curAttr = ( MutableAttributeSet )run.getAttributes();

            bold = curAttr.containsAttributes( boldStyle );
        }
        if( bold )
        {
            StyleConstants.setBold( attr, false );
        }
        else
        {
            StyleConstants.setBold( attr, true );
        }
        textPane.setCharacterAttributes( attr, false );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private void addText()
    {
        StyleConstants.setUnderline( ulStyle, true );

        StyleConstants.setBold( boldStyle, true );

        int start = 0;
        int end = 0;

        try
        {
            styledDocument.insertString( styledDocument.getLength(),
                "The quick brown ", null );

            start = styledDocument.getLength();
            styledDocument.insertString( styledDocument.getLength(), "duckling",
                boldStyle );
            end = styledDocument.getLength();

            styledDocument.insertString( styledDocument.getLength(),
                " swam over the lazy fish.", ulStyle );
        }
        catch( BadLocationException e )
        {
            e.printStackTrace();
        }

        textPane.setSelectionStart( start );
        textPane.setSelectionEnd( end );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                StandardFrameView view = new StandardFrameView();
                StyleTest st = new StyleTest();

                view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                view.setContent( st.getView() );
                view.setTitle( "StyleTest" );
                view.setSize( 400, 400 );

                return view.getView();
            }
        } );
    }
}
