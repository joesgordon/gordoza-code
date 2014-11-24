package org.jutils;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class UtilsTest
{
    @Test
    public void testSplitEmpty()
    {
        String str = "";
        List<String> fields = Utils.split( str );

        Assert.assertEquals( true, fields.isEmpty() );
    }

    @Test
    public void testSplitHappyPath()
    {
        String str = "f0 f1 \t f2   f3\tf4";
        List<String> fields = Utils.split( str );

        Assert.assertEquals( false, fields.isEmpty() );

        for( int i = 0; i < 5; i++ )
        {
            Assert.assertEquals( "f" + i, fields.get( i ) );
        }
    }

    @Test
    public void testSplitPadLeft()
    {
        String str = " f1 \t f2   f3\tf4";
        List<String> fields = Utils.split( str );

        Assert.assertEquals( false, fields.isEmpty() );

        Assert.assertEquals( true, fields.get( 0 ).isEmpty() );

        for( int i = 1; i < 5; i++ )
        {
            Assert.assertEquals( "f" + i, fields.get( i ) );
        }
    }

    @Test
    public void testSplitPadRight()
    {
        String str = "f0 f1 \t f2   f3\tf4    ";
        List<String> fields = Utils.split( str );

        Assert.assertEquals( false, fields.isEmpty() );

        for( int i = 0; i < 5; i++ )
        {
            Assert.assertEquals( "f" + i, fields.get( i ) );
        }
    }
}
