package org.jutils.chart.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.JPanel;

import org.jutils.ui.FontChooserDialog;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FontView implements IDataView<Font>
{
    /**  */
    private final JPanel view;

    /**  */
    private final JList<String> namesField;
    /**  */
    private final JList<Integer> sizesField;

    /**  */
    private Font f;

    /***************************************************************************
     * 
     **************************************************************************/
    public FontView()
    {
        this.namesField = new JList<>( FontChooserDialog.FONT_NAMES );
        this.sizesField = new JList<>( FontChooserDialog.FONT_SIZES );
        this.view = new JPanel();

        setData( new Font( Font.MONOSPACED, Font.PLAIN, 12 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Font getData()
    {
        return f;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Font data )
    {
        this.f = data;
    }
}
