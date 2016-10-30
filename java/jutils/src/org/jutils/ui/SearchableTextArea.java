package org.jutils.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.text.*;

import org.jutils.Utils;
import org.jutils.io.LogUtils;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchableTextArea extends JTextArea
{
    /**  */
    private GapContent content = new GapContent();

    /**  */
    private Document doc = new PlainDocument( content );

    /**  */
    private FindDialog findDialog = new FindDialog();

    /**  */
    private FindOptions lastOptions = null;

    /***************************************************************************
     *
     **************************************************************************/
    public SearchableTextArea()
    {
        this( null, null, 0, 0 );
        this.setDocument( doc );
    }

    /***************************************************************************
     * @param doc Document
     * @param text String
     * @param rows int
     * @param columns int
     **************************************************************************/
    public SearchableTextArea( Document doc, String text, int rows,
        int columns )
    {
        super( doc, text, rows, columns );
        this.addKeyListener( new SearchableTextArea_keyAdapter( this ) );
        findDialog.addFindListener(
            new SearchableTextArea_findAdapter( this ) );
        findDialog.setTitle( "Find" );
        findDialog.pack();
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void reFind()
    {
        if( lastOptions != null )
        {
            searchAndHighlight( lastOptions );
        }
    }

    /***************************************************************************
     * @param options FindOptions
     * @return boolean
     **************************************************************************/
    public boolean searchAndHighlight( FindOptions options )
    {
        boolean found = false;
        CharSequence seq = getSequence();
        int position = this.getCaretPosition();

        if( options.pattern == null )
        {
            buildOptions( options );
        }

        if( options.matcher == null )
        {
            options.matcher = options.pattern.matcher( seq );
        }
        if( options.matcher.find( position ) )
        {
            found = true;
            // LogUtils.printDebug( "m.start: " + m.start( 1 ) );
            // LogUtils.printDebug( "m.end: " + m.end( 1 ) );
        }
        else if( options.wrapAround )
        {
            options.matcher.reset();
            if( options.matcher.find() )
            {
                found = true;
            }
        }

        if( found )
        {
            select( options.matcher.start(), options.matcher.end() );
        }

        // LogUtils.printDebug( "****************************************" );

        // LogUtils.printDebug( "" );

        lastOptions = options;

        return found;
    }

    /***************************************************************************
     * @param options FindOptions
     * @return boolean
     **************************************************************************/
    private static boolean buildOptions( FindOptions options )
    {
        boolean found = false;
        int flags = Pattern.MULTILINE | Pattern.DOTALL;
        String expression = options.textToFind;

        flags |= options.matchCase ? flags : Pattern.CASE_INSENSITIVE;

        if( !options.useRegex )
        {
            expression = Utils.escapeRegexMetaChar( expression );
        }

        options.pattern = Pattern.compile( expression, flags );

        LogUtils.printDebug( "Building options..." );
        // LogUtils.printDebug( "expression: " + expression );
        // LogUtils.printDebug( "flags: " + Integer.toHexString( flags ) );
        // LogUtils.printDebug( "startPos: " + startPos );
        // LogUtils.printDebug( "endPos: " + content.length() );
        // LogUtils.printDebug( "content: " );
        // LogUtils.printDebug( sequence.toString() );

        return found;
    }

    /***************************************************************************
     * @return ContentSequence
     **************************************************************************/
    public ContentSequence getSequence()
    {
        return new ContentSequence( content );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void showFind()
    {
        String str = this.getSelectedText();
        if( str != null && lastOptions != null )
        {
            lastOptions.textToFind = str;
        }

        findDialog.setOptions( lastOptions );
        findDialog.setLocationRelativeTo( null );
        findDialog.setVisible( true );
        findDialog.requestFocus();
    }

    /***************************************************************************
     * @param options FindOptions
     **************************************************************************/
    public void setOptions( FindOptions options )
    {
        this.lastOptions = options;
    }
}

class SearchableTextArea_findAdapter implements FindListener
{
    SearchableTextArea textArea = null;

    public SearchableTextArea_findAdapter( SearchableTextArea ta )
    {
        textArea = ta;
    }

    public void findText( FindOptions findData )
    {
        textArea.searchAndHighlight( findData );
    }
}

class SearchableTextArea_keyAdapter implements KeyListener
{
    SearchableTextArea textArea = null;

    public SearchableTextArea_keyAdapter( SearchableTextArea ta )
    {
        textArea = ta;
    }

    public void keyPressed( KeyEvent e )
    {
        // LogUtils.printDebug( "keyPressed" );
    }

    public void keyReleased( KeyEvent e )
    {
        if( e.getKeyCode() == KeyEvent.VK_F3 )
        {
            // LogUtils.printDebug( "F3 Released" );
            textArea.reFind();
        }
    }

    public void keyTyped( KeyEvent e )
    {
        char keyTyped = e.getKeyChar();
        int modifiers = e.getModifiers();

        if( ( modifiers & KeyEvent.CTRL_MASK ) != 0 )
        {
            if( keyTyped == 'f' - 'a' + 1 )
            {
                // LogUtils.printDebug( "CTRL+F Typed" );
                textArea.showFind();
            }
        }
    }
}
