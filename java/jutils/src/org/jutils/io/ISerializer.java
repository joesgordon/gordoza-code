package org.jutils.io;

/*******************************************************************************
 * @param <T>
 * @param <IN>
 * @param <OUT>
 ******************************************************************************/
public interface ISerializer<T, IN, OUT> extends IReader<T, IN>, IWriter<T, OUT>
{
}
