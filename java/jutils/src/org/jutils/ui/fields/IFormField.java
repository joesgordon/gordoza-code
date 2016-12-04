package org.jutils.ui.fields;

import javax.swing.JComponent;

import org.jutils.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IFormField extends INamedItem
{
    /**************************************************************************
     * @return
     **************************************************************************/
    public JComponent getField();
}
