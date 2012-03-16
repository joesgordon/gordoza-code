package nmrc.ui.tables;

import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemTable<T extends Object> extends
    ObjectTable<T, ItemTableModel<T>>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public ItemTable( ItemTableModel<T> model )
    {
        super( model );
    }
}
