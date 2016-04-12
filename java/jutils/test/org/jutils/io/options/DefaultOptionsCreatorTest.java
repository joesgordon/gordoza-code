package org.jutils.io.options;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import junit.framework.Assert;

/*******************************************************************************
 *
 ******************************************************************************/
public class DefaultOptionsCreatorTest
{
    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testNoConstructors()
    {
        testObject( ObjectNoConstructors.class );
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testDefaultConstructor()
    {
        testObject( ObjectWithDefaultConstructor.class );
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testCopyConstructor()
    {
        try
        {
            new DefaultOptionsCreator<>( ObjectWithCopyConstructor.class );
            Assert.fail(
                "An object with no default constructor should not be supported." );
        }
        catch( IllegalArgumentException ex )
        {
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    @Test
    public void testBothConstructors()
    {
        testObject( ObjectWithBothConstructors.class );
    }

    /***************************************************************************
     * @param obj
     * @param cls
     **************************************************************************/
    public <T extends IDataClass> void testObject( Class<T> cls )
    {
        File file;
        try
        {
            file = File.createTempFile( "test_file", ".xml" );
            // LogUtils.printDebug( "file %s", file.getAbsolutePath() );
        }
        catch( IOException ex )
        {
            Assert.fail( ex.getMessage() );
            return;
        }

        DefaultOptionsCreator<T> creator = new DefaultOptionsCreator<>( cls );
        OptionsSerializer<T> serializer = new OptionsSerializer<>( creator,
            file );

        T obj = creator.createDefaultOptions();

        obj.setData( 50.0 );
        serializer.write( obj );
        serializer = new OptionsSerializer<>( creator, file );
        obj = serializer.getOptions();

        Assert.assertEquals( 50.0, obj.getData(), 0.000001 );
    }

    /***************************************************************************
    *
    ***************************************************************************/
    private static interface IDataClass
    {
        public double getData();

        public void setData( double d );
    }

    /***************************************************************************
    *
    ***************************************************************************/
    private static class ObjectNoConstructors implements IDataClass
    {
        public double d;

        @Override
        public double getData()
        {
            return d;
        }

        @Override
        public void setData( double d )
        {
            this.d = d;
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    private static class ObjectWithDefaultConstructor implements IDataClass
    {
        public double d;

        @SuppressWarnings( "unused")
        public ObjectWithDefaultConstructor()
        {
            this.d = 100.0;
        }

        @Override
        public double getData()
        {
            return d;
        }

        @Override
        public void setData( double d )
        {
            this.d = d;
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    private static class ObjectWithCopyConstructor implements IDataClass
    {
        public double d;

        @SuppressWarnings( "unused")
        public ObjectWithCopyConstructor( ObjectWithCopyConstructor obj )
        {
            this.d = obj.d;
        }

        @Override
        public double getData()
        {
            return d;
        }

        @Override
        public void setData( double d )
        {
            this.d = d;
        }
    }

    /***************************************************************************
    *
    ***************************************************************************/
    private static class ObjectWithBothConstructors implements IDataClass
    {
        public double d;

        @SuppressWarnings( "unused")
        public ObjectWithBothConstructors()
        {
            this.d = 100.0;
        }

        @SuppressWarnings( "unused")
        public ObjectWithBothConstructors( ObjectWithBothConstructors obj )
        {
            this.d = obj.d;
        }

        @Override
        public double getData()
        {
            return d;
        }

        @Override
        public void setData( double d )
        {
            this.d = d;
        }
    }
}
