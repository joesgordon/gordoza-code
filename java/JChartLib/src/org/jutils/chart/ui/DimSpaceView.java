package org.jutils.chart.ui;

import java.awt.Component;

import org.jutils.chart.model.DimSpace;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IDataView;

public class DimSpaceView implements IDataView<DimSpace>
{
    /**  */
    private final StandardFormView form;

    /**  */
    private final TextLabelField titleField;
    /**  */
    private final TextLabelField subtitleField;

    /**  */
    private DimSpace axis;

    /***************************************************************************
     * 
     **************************************************************************/
    public DimSpaceView()
    {
        this.titleField = new TextLabelField( "Title" );
        this.subtitleField = new TextLabelField( "Subtitle" );

        this.form = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private StandardFormView createView()
    {
        StandardFormView form = new StandardFormView();

        form.setHorizontalStretch( true );

        form.addField( titleField );
        form.addField( subtitleField );

        return form;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public DimSpace getData()
    {
        return axis;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( DimSpace data )
    {
        this.axis = data;

        // TODO Auto-generated method stub

        titleField.setValue( data.title );
        subtitleField.setValue( data.subtitle );
    }
}
