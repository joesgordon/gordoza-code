package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TitleView implements IDataView<String>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel titleField;
    /**  */
    private final JPanel componentPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public TitleView()
    {
        GridBagConstraints constraints;

        componentPanel = new JPanel( new BorderLayout() );
        view = new JPanel( new GridBagLayout() );
        titleField = new JLabel( "Title" );
        JSeparator separator = new JSeparator();
        GradientPanel titlePanel = createTitlePanel();

        // componentPanel.setBorder( BorderFactory.createLineBorder( Color.red )
        // );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( titlePanel, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        view.add( separator, constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        view.add( Box.createHorizontalStrut( 0 ), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        view.add( componentPanel, constraints );

        setComponent( Box.createVerticalStrut( 20 ) );

        view.setBorder( BorderFactory.createLineBorder( Color.gray ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private GradientPanel createTitlePanel()
    {
        GridBagConstraints constraints;
        GradientPanel titlePanel;

        titlePanel = new GradientPanel( new GridBagLayout(), new Color( 58,
            110, 167 ) );

        Font bold = titleField.getFont().deriveFont( Font.BOLD );
        titleField.setFont( bold );
        titleField.setForeground( Color.white );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 4,
                4, 4 ), 0, 0 );
        titlePanel.add( titleField, constraints );
        return titlePanel;
    }

    /***************************************************************************
     * @param comp
     **************************************************************************/
    public void setComponent( Component comp )
    {
        componentPanel.removeAll();
        componentPanel.add( comp, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getData()
    {
        return titleField.getText();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( String data )
    {
        titleField.setText( data );
    }
}
