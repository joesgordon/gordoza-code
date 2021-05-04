package org.budgey.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.budgey.data.Money;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SummaryPanel implements IView<Component>
{
    /**  */
    private final JPanel view;
    /**  */
    private final MoneyLabel balanceField;
    /**  */
    private final MoneyLabel availableField;

    /***************************************************************************
     * 
     **************************************************************************/
    public SummaryPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        JLabel balanceLabel = new JLabel( "Your account balance is " );
        balanceField = new MoneyLabel();

        JLabel availableLabel = new JLabel( "Your available funds are " );
        availableField = new MoneyLabel();

        view.add( balanceLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        view.add( balanceField.getView(),
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        view.add( availableLabel,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 4, 0, 0, 0 ), 0, 0 ) );
        view.add( availableField.getView(),
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param balance
     * @param available
     **************************************************************************/
    public void setData( Money balance, Money available )
    {
        balanceField.setData( balance );
        availableField.setData( available );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }
}
