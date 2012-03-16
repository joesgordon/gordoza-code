package nmrc.ui.tables;

import javax.swing.table.TableModel;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class DefaultObjectTable<T extends Object> extends
    ObjectTable<T, TableModel>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public DefaultObjectTable( TableModel model )
    {
        super( model );
    }
}
