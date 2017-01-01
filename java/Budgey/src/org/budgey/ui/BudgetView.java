package org.budgey.ui;

import java.awt.*;

import javax.swing.*;

import org.budgey.data.Budget;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgetView implements IDataView<Budget>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextField totalField;

    public BudgetView()
    {
        this.totalField = new JTextField();
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createTotalView(), BorderLayout.NORTH );
        panel.add( createAccountsView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createTotalView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel label = new JLabel( "Total:" );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 6, 6, 6, 0 ), 0, 0 );
        panel.add( label, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 6, 6, 6, 0 ), 0, 0 );
        panel.add( totalField, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Component createAccountsView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        ;

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Budget getData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Budget data )
    {
        // TODO Auto-generated method stub
    }
}
