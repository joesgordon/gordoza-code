package org.jutils.io;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.apps.summer.data.SumFile;

import com.thoughtworks.xstream.XStreamException;

public class XStreamUtilsTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_writeObjectXStream()
    {
        try
        {
            XStreamUtils.writeObjectXStream( new Double( 42.0 ) );
        }
        catch( XStreamException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_readObjectXStream()
    {
        try
        {
            Double expected = new Double( 42.0 );
            String xml = XStreamUtils.writeObjectXStream( expected );
            Double d = XStreamUtils.readObjectXStream( xml );

            Assert.assertEquals( expected, d );
        }
        catch( XStreamException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_readObjectXStream2()
    {
        try
        {
            ChecksumResult expected = new ChecksumResult();
            expected.commonDir = new File( "\\" );
            expected.files.add(
                new SumFile( new File( "C:\\Windows\\System32\\Calc.exe" ) ) );
            String xml = XStreamUtils.writeObjectXStream( expected );
            XStreamUtils.readObjectXStream( xml );
        }
        catch( XStreamException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
