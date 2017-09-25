package org.mc.io;

import java.time.*;

import org.jutils.ui.fields.IDescriptor;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TimeDescriptor implements IDescriptor<LocalDateTime>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription( LocalDateTime time )
    {
        LocalDate date = time.toLocalDate();
        LocalTime t = time.toLocalTime();

        return date.toString() + " " + t.toString();
    }
}
