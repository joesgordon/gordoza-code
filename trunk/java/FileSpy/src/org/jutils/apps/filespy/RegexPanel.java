package org.jutils.apps.filespy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.*;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.ui.SearchableTextArea;
import org.jutils.ui.UFrame;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.looks.Options;

/*******************************************************************************
 *
 ******************************************************************************/
public class RegexPanel extends JPanel
{
    /**  */
    private GridBagLayout mainLayout = new GridBagLayout();

    /**  */
    private JPanel optionsPanel = new JPanel();

    /**  */
    private GridBagLayout optionsLayout = new GridBagLayout();

    /**  */
    private JCheckBox caseCheckBox = new JCheckBox();

    /**  */
    private JCheckBox multilineCheckBox = new JCheckBox();

    /**  */
    private JCheckBox commentsCheckBox = new JCheckBox();

    /**  */
    private JCheckBox globalCheckBox = new JCheckBox();

    /**  */
    private JPanel regexPanel = new JPanel();

    /**  */
    private GridBagLayout regexLayout = new GridBagLayout();

    /**  */
    private JScrollPane regexScrollPane = new JScrollPane();

    /**  */
    private SearchableTextArea regexTextArea = new SearchableTextArea();

    /**  */
    private JButton testButton = new JButton();

    /**  */
    private JButton closeButton = new JButton();

    /**  */
    private JPanel testPanel = new JPanel();

    /**  */
    private GridBagLayout testLayout = new GridBagLayout();

    /**  */
    private JScrollPane testScrollPane = new JScrollPane();

    /**  */
    private SearchableTextArea testTextArea = new SearchableTextArea();

    /**  */
    private JPanel resultPanel = new JPanel();

    /**  */
    private GridBagLayout resultLayout = new GridBagLayout();

    /**  */
    private JScrollPane resultScrollPane = new JScrollPane();

    /**  */
    private SearchableTextArea resultTextArea = new SearchableTextArea();

    /***************************************************************************
     *
     **************************************************************************/
    public RegexPanel()
    {
        try
        {
            jbInit();
        }
        catch( Exception exception )
        {
            exception.printStackTrace();
        }
    }

    /***************************************************************************
     * @throws Exception
     **************************************************************************/
    private void jbInit() throws Exception
    {
        ButtonStackBuilder builder;

        // ----------------------------------------------------------------------
        // Setup options panel
        // ----------------------------------------------------------------------
        optionsPanel.setLayout( optionsLayout );
        optionsPanel.setBorder( BorderFactory.createTitledBorder( "Options" ) );

        caseCheckBox.setText( "Case-Insensitive" );
        caseCheckBox.setToolTipText( "Enables case-insensitive matching." );
        multilineCheckBox.setText( "Multiline" );
        multilineCheckBox.setToolTipText( "^ and $ match just after or just "
            + "before, respectively, a line terminator or the end of the "
            + "input sequence." );
        commentsCheckBox.setText( "Comments" );
        commentsCheckBox.setSelected( true );
        commentsCheckBox.setToolTipText( "whitespace is ignored, and embedded "
            + "comments starting with # are ignored until the end of a line." );
        globalCheckBox.setText( "Global" );
        globalCheckBox.setToolTipText( ". matches any character, including a "
            + "line terminator." );
        testButton.addActionListener( new RegexPanel_testButton_actionAdapter(
            this ) );
        closeButton.addActionListener( new RegexPanel_closeButton_actionAdapter(
            this ) );

        optionsPanel.add( caseCheckBox, new GridBagConstraints( 0, 0, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( multilineCheckBox, new GridBagConstraints( 0, 1, 1,
            1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( commentsCheckBox, new GridBagConstraints( 0, 2, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( globalCheckBox, new GridBagConstraints( 0, 3, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        optionsPanel.add( Box.createVerticalStrut( 0 ), new GridBagConstraints(
            0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // ----------------------------------------------------------------------
        // Setup regex panel.
        // ----------------------------------------------------------------------
        builder = new ButtonStackBuilder();
        regexPanel.setLayout( regexLayout );
        // regexPanel.setBorder( BorderFactory.createTitledBorder( "Regex" ) );
        // regexPanel.

        regexTextArea.setText( "blah" );
        regexScrollPane.setViewportView( regexTextArea );

        Dimension dim = regexScrollPane.getPreferredSize();
        dim.height = 100;
        regexScrollPane.setMinimumSize( dim );
        regexScrollPane.setPreferredSize( dim );

        testButton.setText( "Test" );
        closeButton.setText( "Close" );

        builder.addFixed( testButton );
        builder.addRelatedGap();
        builder.addFixed( closeButton );

        regexPanel.add( new JLabel( "Regex" ), new GridBagConstraints( 0, 0, 2,
            1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 4, 2 ), 0, 0 ) );

        regexPanel.add( regexScrollPane, new GridBagConstraints( 0, 1, 1, 2,
            1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        regexPanel.add( builder.getPanel(), new GridBagConstraints( 1, 1, 1, 1,
            0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        regexPanel.add( optionsPanel, new GridBagConstraints( 1, 2, 1, 1, 0.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // ----------------------------------------------------------------------
        // Setup test panel
        // ----------------------------------------------------------------------
        testPanel.setLayout( testLayout );
        // testPanel.setBorder( BorderFactory.createTitledBorder( "Test Text" )
        // );

        testTextArea.setText( "Here is" + Utils.NEW_LINE + "some text with" +
            Utils.NEW_LINE + "the word blah in it" + Utils.NEW_LINE +
            "at some" + Utils.NEW_LINE + "point." );
        testScrollPane.setViewportView( testTextArea );

        testPanel.add( testScrollPane, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // ----------------------------------------------------------------------
        // Setup result panel
        // ----------------------------------------------------------------------
        resultPanel.setLayout( resultLayout );
        // resultPanel.setBorder( BorderFactory.createTitledBorder( "Result" )
        // );

        resultScrollPane.setViewportView( resultTextArea );

        resultPanel.add( resultScrollPane, new GridBagConstraints( 0, 0, 1, 1,
            1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        // ----------------------------------------------------------------------
        // Setup main panel
        // ----------------------------------------------------------------------
        this.setLayout( mainLayout );

        this.add( regexPanel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2,
                2, 2, 2 ), 0, 0 ) );

        this.add( new JLabel( "Test Text" ), new GridBagConstraints( 0, 1, 1,
            1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 4, 2 ), 0, 0 ) );

        this.add( testPanel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2,
                2, 2, 2 ), 0, 0 ) );

        this.add( new JLabel( "Result" ), new GridBagConstraints( 0, 3, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 4, 2 ), 0, 0 ) );

        this.add( resultPanel, new GridBagConstraints( 0, 4, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 2,
                2, 2, 2 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    public void testButton_actionPerformed( ActionEvent e )
    {
        boolean caseFlag = caseCheckBox.isSelected();
        boolean multilineFlag = multilineCheckBox.isSelected();
        boolean commentsFlag = commentsCheckBox.isSelected();
        boolean globalFlag = globalCheckBox.isSelected();
        int flags = 0;
        Pattern pattern = null;
        Matcher matcher = null;
        CharSequence seq = null;
        StringBuffer resultStr = new StringBuffer();

        flags |= caseFlag ? Pattern.CASE_INSENSITIVE : flags;
        flags |= multilineFlag ? Pattern.MULTILINE : flags;
        flags |= commentsFlag ? Pattern.COMMENTS : flags;
        flags |= globalFlag ? Pattern.DOTALL : flags;

        try
        {
            pattern = Pattern.compile( regexTextArea.getText(), flags );
            seq = testTextArea.getSequence();
            matcher = pattern.matcher( seq );

            if( matcher.find() )
            {
                int indexFirst = matcher.start();
                int indexLast = matcher.end();
                String text = seq.subSequence( indexFirst, indexLast ).toString();

                resultStr.append( "Matched Text: " + Utils.NEW_LINE );
                resultStr.append( text + Utils.NEW_LINE );
                resultStr.append( "Starts At: " + indexFirst + Utils.NEW_LINE );
                resultStr.append( "Ends At: " + indexLast + Utils.NEW_LINE );

                for( int i = 1; i <= matcher.groupCount(); i++ )
                {
                    resultStr.append( "Group " + i + ": '" );
                    resultStr.append( seq.subSequence( matcher.start( i ),
                        matcher.end( i ) ) + "'" + Utils.NEW_LINE );
                }

                resultTextArea.setText( text );
            }
            else
            {
                resultTextArea.setText( "No Result Found" );
            }
            resultTextArea.setText( resultStr.toString() );
        }
        catch( PatternSyntaxException ex )
        {
            JOptionPane.showMessageDialog( Utils.getComponentsWindow( this ),
                ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE );
            regexTextArea.requestFocus();
            regexTextArea.select( ex.getIndex() - 1, ex.getIndex() );
        }
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    public void closeButton_actionPerformed( ActionEvent e )
    {
        Utils.getComponentsWindow( this ).dispose();
    }

    public static void main( String[] args )
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
                JFrame frame = new UFrame();
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setTitle( "Regex Friend" );
                frame.setContentPane( new RegexPanel() );
                frame.setSize( new Dimension( 500, 500 ) );
                frame.validate();
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }
}

class RegexPanel_closeButton_actionAdapter implements ActionListener
{
    private RegexPanel adaptee;

    RegexPanel_closeButton_actionAdapter( RegexPanel adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.closeButton_actionPerformed( e );
    }
}

class RegexPanel_testButton_actionAdapter implements ActionListener
{
    private RegexPanel adaptee;

    RegexPanel_testButton_actionAdapter( RegexPanel adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.testButton_actionPerformed( e );
    }
}
