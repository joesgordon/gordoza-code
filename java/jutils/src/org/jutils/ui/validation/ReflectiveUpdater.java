package org.jutils.ui.validation;

import java.lang.reflect.Field;

import org.jutils.ui.event.updater.IUpdater;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ReflectiveUpdater<T> implements IUpdater<T>
{
    /**  */
    private final Object obj;
    /**  */
    private final String structureName;
    /**  */
    private final String dataName;

    /***************************************************************************
     * @param obj
     * @param structureName
     * @param dataName
     **************************************************************************/
    public ReflectiveUpdater( Object obj, String structureName, String dataName )
    {
        this.obj = obj;
        this.structureName = structureName;
        this.dataName = dataName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void update( T data )
    {
        try
        {
            Class<?> objClass = obj.getClass();
            Field structField = objClass.getDeclaredField( structureName );

            structField.setAccessible( true );

            Object struct = structField.get( obj );

            if( struct == null )
            {
                throw new IllegalStateException( "Object not initialized: " +
                    objClass.getSimpleName() + "." + structureName );
            }

            Class<?> structClass = struct.getClass();
            Field dataField = structClass.getField( dataName );
            dataField.set( struct, data );
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
