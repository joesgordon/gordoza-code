package org.cojo.ui.tableModels;

import org.cojo.data.Project;
import org.cojo.data.SoftwareTask;
import org.jutils.ui.model.ITableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StfTableModel implements ITableItemsConfig<SoftwareTask>
{
    /**  */
    private static final String [] COLUMN_HEADING = { "#", "Title", "Lead",
        "Est Hours", "Act Hours" };
    /**  */
    private static final Class<?> [] COLUMN_CLASSES = { Integer.class,
        String.class, String.class, Integer.class, Integer.class };

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public StfTableModel()
    {
        this.project = new Project();
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
        return COLUMN_HEADING;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        return COLUMN_CLASSES;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Object getItemData( SoftwareTask item, int col )
    {
        switch( col )
        {
            case 0:
                return item.id;
            case 1:
                return item.title;
            case 2:
                return project.getUser( item.leadUserId ).name;
            case 3:
                return item.estimatedHours;
            case 4:
                return item.actualHours;
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setItemData( SoftwareTask item, int col, Object data )
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isCellEditable( SoftwareTask item, int col )
    {
        return false;
    }
}
