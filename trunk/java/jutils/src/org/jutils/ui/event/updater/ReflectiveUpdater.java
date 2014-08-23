package org.jutils.ui.event.updater;

import java.lang.reflect.Field;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ReflectiveUpdater<T> implements IUpdater<T>
{
    /**  */
    private final Object obj;
    /**  */
    private final String [] dataPath;

    /***************************************************************************
     * @param obj
     * @param structureName
     * @param dataName
     **************************************************************************/
    public ReflectiveUpdater( Object obj, String dataPath )
    {
        this.obj = obj;
        this.dataPath = dataPath.split( "\\." );

        if( this.dataPath.length == 0 )
        {
            throw new IllegalArgumentException(
                "No field names found in data path: " + dataPath );
        }
    }

    public ReflectiveUpdater( Object obj, String structureName, String dataName )
    {
        this( obj, structureName + "." + dataName );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void update( T data )
    {
        try
        {
            Object obj = this.obj;
            Class<?> clazz = null;
            Field field = null;

            // LogUtils.printDebug( "Data type: " +
            // data.getClass().getSimpleName() );

            for( int i = 0; i < dataPath.length; i++ )
            {
                String name = dataPath[i];
                clazz = obj.getClass();

                field = clazz.getDeclaredField( name );

                field.setAccessible( true );

                if( i < dataPath.length - 1 )
                {
                    obj = field.get( obj );

                    if( obj == null )
                    {
                        throw new IllegalStateException(
                            "Object not initialized: " + clazz.getSimpleName() +
                                "." + name );
                    }

                    // LogUtils.printDebug( clazz.getSimpleName() +
                    // " has a field " + name + " of type " +
                    // obj.getClass().getSimpleName() );
                }
            }

            field.set( obj, data );
        }
        catch( NoSuchFieldException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( SecurityException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( IllegalArgumentException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( IllegalAccessException ex )
        {
            throw new RuntimeException( ex );
        }
    }
}
