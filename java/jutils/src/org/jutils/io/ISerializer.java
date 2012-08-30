package org.jutils.io;


/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface ISerializer<T, IN, OUT> extends IReader<T, IN>,
    IWriter<T, OUT>
{
}
