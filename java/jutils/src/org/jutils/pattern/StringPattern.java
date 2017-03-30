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
        this.type = StringPatternType.EXACT;
        this.patternText = "";
        this.isCaseSensitive = false;
        this.name = "Phil";
    }

    /***************************************************************************
     * @param pattern
     **************************************************************************/
    public StringPattern( StringPattern pattern )
    {
        this.type = pattern.type;
        this.isCaseSensitive = pattern.isCaseSensitive;
        this.patternText = pattern.patternText;
        this.name = pattern.name;
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
        public boolean matches( String str );

        @Override
        public String getName();
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
            String text = str;

            if( !isCaseSensitive )
            {
                text = str.toUpperCase();
            }

            return text.contains( pattern );
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
                    strPattern.isCaseSensitive );
            }
        }

        @Override
        public boolean matches( String str )
        {
            return matcher.matches( str );
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
            int flags = isCaseSensitive ? 0 : Pattern.CASE_INSENSITIVE;

            this.name = name;
            try
            {
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
        public String getName()
        {
            return name;
        }
    }
}
