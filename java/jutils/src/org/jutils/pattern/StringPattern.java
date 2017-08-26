package org.jutils.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jutils.INamedItem;
import org.jutils.ValidationException;
import org.jutils.io.StringPrintStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StringPattern
{
    /**  */
    public StringPatternType type;
    /**  */
    public String patternText;
    /**  */
    public boolean isCaseSensitive;
    /**  */
    public String name;

    /***************************************************************************
     * 
     **************************************************************************/
    public StringPattern()
    {
        this.type = StringPatternType.CONTAINS;
        this.patternText = "";
        this.isCaseSensitive = false;
        this.name = "Phil";
    }

    /***************************************************************************
     * @param pattern
     **************************************************************************/
    public StringPattern( StringPattern pattern )
    {
        set( pattern );
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringPattern( String name )
    {
        this();

        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format( "%s: %s{%s}[%b]", name, type.name, patternText,
            isCaseSensitive );
    }

    /***************************************************************************
     * @return
     * @throws ValidationException
     **************************************************************************/
    public IMatcher createMatcher() throws ValidationException
    {
        if( patternText.isEmpty() )
        {
            return new YesMatcher( name );
        }

        switch( type )
        {
            case EXACT:
                return new ExactMatcher( this );

            case CONTAINS:
                return new ContainsMatcher( this );

            case WILDCARD:
                return new WildcardMatcher( this );

            case REGEX:
                return new RegexMatcher( this );

            default:
                break;
        }

        throw new IllegalStateException( "Unhandled type " + type.name );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IMatcher extends INamedItem
    {
        /**
         * @param str the string to be searched.
         * @return {@code true} if the pattern matches the input, {@code false}
         * otherwise.
         */
        public boolean matches( String str );

        /**
         * @param str the string to be searched.
         * @return results of search guaranteed to be non-null.
         */
        public Match find( String str );

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName();
    }

    /***************************************************************************
     * Relates if and where the pattern matched the input string.
     **************************************************************************/
    public final static class Match
    {
        /** Whether the pattern matched the input string. */
        public final boolean matches;
        /** The inclusive start index. */
        public final int start;
        /** The exclusive end index. */
        public final int end;

        public Match()
        {
            this( false, 0, 0 );
        }

        public Match( boolean matches, int start, int end )
        {
            this.matches = matches;
            this.start = start;
            this.end = end;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class YesMatcher implements IMatcher
    {
        private final String name;

        public YesMatcher( String name )
        {
            this.name = name;
        }

        @Override
        public boolean matches( String str )
        {
            return true;
        }

        @Override
        public Match find( String str )
        {
            return new Match( true, 0, str.length() );
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ExactMatcher implements IMatcher
    {
        private final boolean isCaseSensitive;
        private final String name;
        private final String pattern;

        public ExactMatcher( StringPattern strPattern )
        {
            this.isCaseSensitive = strPattern.isCaseSensitive;
            this.name = strPattern.name;
            this.pattern = strPattern.isCaseSensitive ? strPattern.patternText
                : strPattern.patternText.toUpperCase();
        }

        @Override
        public boolean matches( String str )
        {
            String text = str;

            if( !isCaseSensitive )
            {
                text = str.toUpperCase();
            }

            return text.equals( pattern );
        }

        @Override
        public Match find( String str )
        {
            if( matches( str ) )
            {
                return new Match( true, 0, str.length() );
            }

            return new Match();
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ContainsMatcher implements IMatcher
    {
        private final boolean isCaseSensitive;
        private final String name;
        private final String pattern;

        public ContainsMatcher( StringPattern strPattern )
        {
            this.isCaseSensitive = strPattern.isCaseSensitive;
            this.name = strPattern.name;
            this.pattern = strPattern.isCaseSensitive ? strPattern.patternText
                : strPattern.patternText.toUpperCase();
        }

        @Override
        public boolean matches( String str )
        {
            return prepare( str ).contains( pattern );
        }

        @Override
        public Match find( String str )
        {
            int idx = prepare( str ).indexOf( pattern );

            if( idx < 0 )
            {
                return new Match();
            }

            return new Match( true, idx, idx + pattern.length() );
        }

        private String prepare( String str )
        {
            return isCaseSensitive ? str : str.toUpperCase();
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class WildcardMatcher implements IMatcher
    {
        private final RegexMatcher matcher;

        public WildcardMatcher( StringPattern strPattern )
            throws ValidationException
        {
            this.matcher = buildMatcher( strPattern );
        }

        private static RegexMatcher buildMatcher( StringPattern strPattern )
            throws ValidationException
        {
            String patternText = strPattern.patternText;

            try( StringPrintStream stream = new StringPrintStream() )
            {
                int idx = -1;
                int from = 0;

                while( ( idx = patternText.indexOf( '*', from ) ) > -1 )
                {
                    if( idx > from )
                    {
                        String part = patternText.substring( from, idx );
                        stream.print( Pattern.quote( part ) );
                    }

                    stream.print( ".*" );

                    from = idx + 1;
                }

                if( from < patternText.length() - 1 )
                {
                    String part = patternText.substring( from );
                    stream.print( Pattern.quote( part ) );
                }

                String regex = stream.toString();

                // LogUtils.printDebug( "regex: " + regex );

                return new RegexMatcher( strPattern.name, regex,
                    strPattern.isCaseSensitive, true );
            }
        }

        @Override
        public boolean matches( String str )
        {
            return matcher.matches( str );
        }

        @Override
        public Match find( String str )
        {
            return matcher.find( str );
        }

        @Override
        public String getName()
        {
            return matcher.getName();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class RegexMatcher implements IMatcher
    {
        private final String name;
        private final Pattern pattern;

        public RegexMatcher( StringPattern strPattern )
            throws ValidationException
        {
            this( strPattern.name, strPattern.patternText,
                strPattern.isCaseSensitive );
        }

        public RegexMatcher( String name, String regex,
            boolean isCaseSensitive ) throws ValidationException
        {
            this( name, regex, isCaseSensitive, false );
        }

        public RegexMatcher( String name, String regex, boolean isCaseSensitive,
            boolean exact ) throws ValidationException
        {
            int flags = isCaseSensitive ? 0 : Pattern.CASE_INSENSITIVE;

            this.name = name;

            if( !exact )
            {
                if( !regex.startsWith( ".*" ) )
                {
                    regex = ".*" + regex;
                }

                if( !regex.endsWith( ".*" ) )
                {
                    regex = regex + ".*";
                }
            }

            try
            {
                if( !regex.startsWith( ".*" ) )
                {
                    regex = ".*" + regex;
                }

                if( !regex.endsWith( ".*" ) )
                {
                    regex += ".*";
                }

                this.pattern = Pattern.compile( regex, flags );
            }
            catch( IllegalArgumentException ex )
            {
                throw new ValidationException(
                    "Unable to build regular expression pattern \"" + regex +
                        "\"",
                    ex );
            }
        }

        @Override
        public boolean matches( String str )
        {
            Matcher matcher = pattern.matcher( str );

            return matcher.matches();
        }

        @Override
        public Match find( String str )
        {
            Matcher matcher = pattern.matcher( str );

            boolean matches = matcher.matches();
            int start = 0;
            int end = 0;

            if( matches )
            {
                start = matcher.start();
                end = matcher.end();
            }

            return new Match( matches, start, end );
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

    public void set( StringPattern pattern )
    {
        this.type = pattern.type;
        this.isCaseSensitive = pattern.isCaseSensitive;
        this.patternText = pattern.patternText;
        this.name = pattern.name;
    }
}
