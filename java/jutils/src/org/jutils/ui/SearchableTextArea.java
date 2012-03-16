package org.jutils.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.text.*;

import org.jutils.Utils;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchableTextArea extends UTextArea
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
    public SearchableTextArea( Document doc, String text, int rows, int columns )
    {
        super( doc, text, rows, columns );
        this.addKeyListener( new SearchableTextArea_keyAdapter( this ) );
        findDialog.addFindListener( new SearchableTextArea_findAdapter( this ) );
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
            // System.out.println( "m.start: " + m.start( 1 ) );
            // System.out.println( "m.end: " + m.end( 1 ) );
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
        // System.out.print( "****************************************" );
        // System.out.println( "****************************************" );

        // System.out.println( "" );

        lastOptions = options;

        return found;
    }

    /***************************************************************************
     * @param options FindOptions
     * @return boolean
     **************************************************************************/
    private boolean buildOptions( FindOptions options )
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

        System.out.println( "Building options..." );
        // System.out.println( "expression: " + expression );
        // System.out.println( "flags: " + Integer.toHexString( flags ) );
        // System.out.println( "startPos: " + startPos );
        // System.out.println( "endPos: " + content.length() );
        // System.out.println( "content: " );
        // System.out.println( sequence.toString() );

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
        // System.out.print( "keyPressed" );
    }

    public void keyReleased( KeyEvent e )
    {
        if( e.getKeyCode() == KeyEvent.VK_F3 )
        {
            // System.out.println( "F3 Released" );
            textArea.reFind();
        }
    }

    public void keyTyped( KeyEvent e )
    {
        char keyTyped = e.getKeyChar();
        int modifiers = e.getModifiers();

        if( ( modifiers & KeyEvent.CTRL_MASK ) > 0 )
        {
            if( keyTyped == 'f' - 'a' + 1 )
            {
                // System.out.println( "CTRL+F Typed" );
                textArea.showFind();
            }
        }
    }
}
