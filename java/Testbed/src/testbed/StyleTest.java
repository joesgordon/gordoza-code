package testbed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;

public class StyleTest extends JFrame
{
    private JScrollPane pane = new JScrollPane();

    private JTextPane textPane = new JTextPane();

    private JToggleButton boldButton = new JToggleButton();

    private DefaultStyledDocument styledDocument = new DefaultStyledDocument();

    Style boldStyle = styledDocument.addStyle( "bold", null );

    Style ulStyle = styledDocument.addStyle( "ul", null );

    public StyleTest()
    {
        JPanel panel = ( JPanel )this.getContentPane();
        panel.setLayout( new BorderLayout() );

        boldButton.setText( "Bold It" );
        boldButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
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
        } );

        textPane.setDocument( styledDocument );
        textPane.addCaretListener( new CaretListener()
        {
            @Override
            public void caretUpdate( CaretEvent e )
            {
                int dot = e.getDot();
                int mark = e.getMark();
                int first = dot < mark ? dot : mark;

                Element run = styledDocument.getCharacterElement( first );
                MutableAttributeSet attr = ( MutableAttributeSet )run.getAttributes();

                boldButton.setSelected( attr.containsAttributes( boldStyle ) );
            }
        } );
        pane.setViewportView( textPane );

        panel.add( pane, BorderLayout.CENTER );
        panel.add( boldButton, BorderLayout.SOUTH );
        addText();

        this.setSize( 400, 400 );
    }

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

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                StyleTest st = new StyleTest();
                st.validate();
                st.setLocationRelativeTo( null );
                st.setVisible( true );
            }
        } );
    }

}
