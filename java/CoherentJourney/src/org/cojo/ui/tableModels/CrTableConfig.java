package org.cojo.ui.tableModels;

import org.cojo.data.*;
import org.jutils.ui.model.DefaultTableItemsConfig;
import org.jutils.ui.model.ITableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrTableConfig implements ITableItemsConfig<ChangeRequest>
{
    /**  */
    private final DefaultTableItemsConfig<ChangeRequest> config;

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrTableConfig()
    {
        this.config = new DefaultTableItemsConfig<>();
        this.project = new Project();

        config.addCol( "#", Integer.class, ( cr ) -> cr.id );
        config.addCol( "Title", String.class, ( cr ) -> cr.title );
        config.addCol( "State", CrState.class, ( cr ) -> cr.state );
        config.addCol( "Author", String.class,
            ( cr ) -> project.getUser( cr.authorId ).name );
    }

    /***************************************************************************
     * @param project
     **************************************************************************/
    public void setProject( Project project )
    {
        this.project = project;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        return config.getColumnNames();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        return config.getColumnClasses();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Object getItemData( ChangeRequest cr, int col )
    {
        return config.getItemData( cr, col );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setItemData( ChangeRequest item, int col, Object data )
    {
        config.setItemData( item, col, data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isCellEditable( ChangeRequest item, int col )
    {
        return config.isCellEditable( item, col );
    }
}
