package org.jutils.chart.ui;

import java.awt.Component;

import javax.swing.JCheckBox;

import org.jutils.chart.model.Chart;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartPropertiesView implements IDataView<Chart>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final TextLabelField titleField;
    /**  */
    private final TextLabelField subtitleField;
    /**  */
    private final TextLabelField topBottomField;
    /**  */
    private final JCheckBox gridlinesVisibleField;
    /**  */
    private final JCheckBox antiAliasField;
    /**  */
    private final JCheckBox textAntiAliasField;

    /**  */
    private Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartPropertiesView()
    {
        this.titleField = new TextLabelField( "Title" );
        this.subtitleField = new TextLabelField( "Subtitle" );
        this.topBottomField = new TextLabelField( "Top/Bottom" );
        this.gridlinesVisibleField = new JCheckBox();
        this.antiAliasField = new JCheckBox();
        this.textAntiAliasField = new JCheckBox();
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
        form.addField( topBottomField );
        form.addField( "Gridlines Visible", gridlinesVisibleField );
        form.addField( "Anti-Alias", antiAliasField );
        form.addField( "Text Anti-Alias", textAntiAliasField );

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
    public Chart getData()
    {
        return chart;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Chart data )
    {
        this.chart = data;

        titleField.setValue( data.title );
        subtitleField.setValue( data.subtitle );
        topBottomField.setValue( data.topBottomLabel );
        gridlinesVisibleField.setSelected( data.options.gridlinesVisible );
        antiAliasField.setSelected( data.options.antialias );
        textAntiAliasField.setSelected( data.options.textAntiAlias );
    }
}
