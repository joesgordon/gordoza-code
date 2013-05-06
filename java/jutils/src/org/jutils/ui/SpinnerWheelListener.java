package org.jutils.ui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class SpinnerWheelListener implements MouseWheelListener
{
    private final JSpinner spinner;

    public SpinnerWheelListener( JSpinner spinner )
    {
        this.spinner = spinner;
    }

    public void mouseWheelMoved( MouseWheelEvent e )
    {
        SpinnerModel model = spinner.getModel();

        if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
        {
            Object value = null;

            if( e.getUnitsToScroll() > 0 )
            {
                value = model.getPreviousValue();
            }
            else if( e.getUnitsToScroll() < 0 )
            {
                value = model.getNextValue();
            }

            if( value != null )
            {
                spinner.setValue( value );
            }
        }
    }
}
