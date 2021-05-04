package org.cojo.ui.tableModels;

import java.time.LocalDateTime;

import org.cojo.data.Finding;
import org.cojo.data.Project;
import org.jutils.core.ui.model.DefaultTableItemsConfig;
import org.jutils.core.ui.model.ITableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FindingTableModel implements ITableItemsConfig<Finding>
{
    /**  */
    private final DefaultTableItemsConfig<Finding> config;

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public FindingTableModel()
    {
        this.config = new DefaultTableItemsConfig<>();
        this.project = new Project();

        config.addCol( "#", Integer.class, ( f ) -> f.id );
        config.addCol( "User", String.class,
            ( f ) -> project.getUser( f.userId ).getName() );
        config.addCol( "Date", LocalDateTime.class, ( f ) -> f.time );
        config.addCol( "Accepted", Boolean.class, ( f ) -> f.accepted );
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
    public Object getItemData( Finding finding, int col )
    {
        return config.getItemData( finding, col );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setItemData( Finding item, int col, Object data )
    {
        config.setItemData( item, col, data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isCellEditable( Finding item, int col )
    {
        return config.isCellEditable( item, col );
    }
}
