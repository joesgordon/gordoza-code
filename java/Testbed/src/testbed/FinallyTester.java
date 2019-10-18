package testbed;

import java.util.ArrayList;
import java.util.List;

import org.jutils.io.IOUtils;

public class FinallyTester
{
    private static boolean done = false;

    public static void exceptionThrower()
    {
        throw new RuntimeException( "Shouldn't have called me" );
    }

    public static void exceptionCaller()
    {
        try
        {
            exceptionThrower();
        }
        finally
        {
            done = true;
        }
    }

    private static void printDone()
    {
        String str = done ? " " : " not ";
        System.err.println( "DONE?: I am" + str + "done." );
    }

    private static void testBytes( byte b1, byte b2 )
    {
        System.err.print( b1 );
        if( b1 == b2 )
        {
            System.err.print( " == " );
        }
        else
        {
            System.err.print( " != " );
        }
        System.err.println( b2 );
    }

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        test1();
    }

    private static void test1()
    {
        List<Integer> ints = new ArrayList<Integer>();
        List<?> stuff = ints;

        ints.add( 1 );

        String str = "Algy met a bear. The bear was bulgy. The bulge was Algy";

        String str2 = new String( str.getBytes(), IOUtils.get8BitEncoding() );

        if( !str.equals( str2 ) )
        {
            System.err.println( "They don't equal" );
        }

        @SuppressWarnings( "unchecked")
        List<Double> doubles = ( List<Double> )stuff;

        @SuppressWarnings( "unused")
        double d = doubles.get( 0 );
    }

    /**
     * @param args
     */
    @SuppressWarnings( "unused")
    private static void test2()
    {
        int i0 = 0xF6;
        byte b0 = ( byte )i0;
        byte b1 = ( ( Integer )0xF6 ).byteValue();
        byte b2 = ( byte )0xF6;

        testBytes( b0, b1 );
        testBytes( b1, b2 );

        try
        {
            printDone();
            exceptionCaller();
        }
        catch( Throwable th )
        {
            printDone();
            th.printStackTrace();
            printDone();
        }
        printDone();
    }

}
