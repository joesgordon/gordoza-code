package org.jutils.ui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/*******************************************************************************
 *
 ******************************************************************************/
public class USpinner extends JSpinner
{
    /***************************************************************************
	 * 
	 **************************************************************************/
    public USpinner()
    {
        super();
        init();
    }

    /***************************************************************************
     * @param model
     **************************************************************************/
    public USpinner( SpinnerModel model )
    {
        super( model );
        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void init()
    {
        this.addMouseWheelListener( new SpinnerMouseAdapter() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SpinnerMouseAdapter implements MouseWheelListener
    {
        public void mouseWheelMoved( MouseWheelEvent e )
        {
            SpinnerModel model = getModel();

            if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
            {
                if( e.getUnitsToScroll() > 0 )
                {
                    setValue( model.getPreviousValue() );
                }
                else if( e.getUnitsToScroll() < 0 )
                {
                    setValue( model.getNextValue() );
                }
            }
        }
    }
}
