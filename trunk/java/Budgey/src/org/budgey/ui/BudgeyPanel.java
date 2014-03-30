package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.budgey.BudgeyIconConstants;
import org.budgey.data.*;
import org.budgey.model.BalanceCalculator;
import org.jutils.IconConstants;
import org.jutils.io.LogUtils;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyPanel extends JPanel
{
    /**  */
    private final JLabel accountField;

    /**  */
    private final JButton calenderAddButton;
    /**  */
    private final JButton transactionAddButton;
    /**  */
    private final JButton calenderButton;
    /**  */
    private final JButton configButton;

    /**  */
    private final CardPanel sliderPanel;
    /**  */
    private final AccountConfigPanel configView;
    /**  */
    private final SummaryPanel summaryPanel;
    /**  */
    private final TransactionListView transactionListView;
    /**  */
    private final TransactionView transactionView;

    /**  */
    private final BalanceCalculator calc;

    /**  */
    private Budget budget;
    /**  */
    private Account account;

    /***************************************************************************
     * 
     **************************************************************************/
    public BudgeyPanel()
    {
        super( new GridBagLayout() );

        ShowTransactionListListener doneListener = new ShowTransactionListListener();

        calc = new BalanceCalculator();

        transactionListView = new TransactionListView();
        transactionListView.addEditTransactionListener( new EditTransactionListener() );

        transactionView = new TransactionView();
        transactionView.addOkListener( new TransactionOkListener() );
        transactionView.addCancelListener( doneListener );

        configView = new AccountConfigPanel();
        configView.addNameChangedListeners( new NameChangedListener() );
        configView.addOkListener( doneListener );
        configView.addCancelListener( doneListener );

        calenderAddButton = new JButton(
            BudgeyIconConstants.getCalendarAddIcon() );
        transactionAddButton = new JButton(
            BudgeyIconConstants.getCoinsAddIcon() );
        calenderButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.CALENDAR_32 ) );
        configButton = new JButton( BudgeyIconConstants.getCogIcon() );

        accountField = new JLabel( "Account Name" );
        sliderPanel = new CardPanel();

        summaryPanel = new SummaryPanel();

        add( createMainPanel(), new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 4,
                4, 4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createToolBar()
    {
        JToolBar toolbar = new JToolBar();

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        calenderAddButton.setToolTipText( "Add Forecast" );
        calenderAddButton.setFocusable( false );
        // newButton.addActionListener( new NewButtonListener() );
        toolbar.add( calenderAddButton );

        transactionAddButton.setToolTipText( "Add Transaction" );
        transactionAddButton.setFocusable( false );
        transactionAddButton.addActionListener( new TransactionAddListener() );
        toolbar.add( transactionAddButton );

        toolbar.addSeparator();

        calenderButton.setToolTipText( "Set Date Interval" );
        calenderButton.setFocusable( false );
        // newButton.addActionListener( new NewButtonListener() );
        toolbar.add( calenderButton );

        configButton.setFocusable( false );
        configButton.setToolTipText( "Configure Account Properties" );
        configButton.addActionListener( new ConfigButtonListener() );
        toolbar.add( configButton );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createMainPanel()
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        GradientPanel titlePanel = new GradientPanel();

        titlePanel.setLayout( new GridBagLayout() );

        accountField.setForeground( Color.white );

        titlePanel.add( accountField, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 4, 4, 4 ), 0, 0 ) );

        sliderPanel.addCard( transactionListView.getPanel() );
        sliderPanel.addCard( transactionView.getPanel() );

        mainPanel.setBorder( new ShadowBorder() );

        mainPanel.add( titlePanel, new GridBagConstraints( 0, 0, 2, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        mainPanel.add( createToolBar(), new GridBagConstraints( 0, 1, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        mainPanel.add( createSummaryPanel(), new GridBagConstraints( 1, 1, 1,
            1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        mainPanel.add( sliderPanel.getView(), new GridBagConstraints( 0, 2, 2,
            1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createSummaryPanel()
    {

        summaryPanel.setBalance( new Money( 42964 ) );
        summaryPanel.setAvailable( new Money( -65323 ) );

        return summaryPanel;
    }

    /***************************************************************************
     * @param budget
     **************************************************************************/
    public void setBudget( Budget budget )
    {
        this.budget = budget;

        Account account = budget.getDefaultAccount();

        setAccount( account );

        if( account == null )
        {
            showAccountConfigScreen();
        }
        else
        {
            transactionListView.setTransactions( account.getCurrentMonthsTransactions() );
        }
    }

    /***************************************************************************
     * @param panel
     **************************************************************************/
    private void transitionTo( Component panel )
    {
        sliderPanel.addCard( panel );
        sliderPanel.showCard( panel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showAccountConfigScreen()
    {
        configView.setData( account );

        setToolbarEnabled( false );

        transitionTo( configView.getPanel() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTransactionScreen()
    {
        setToolbarEnabled( false );

        transitionTo( transactionView.getPanel() );

        transactionView.requestFocus();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTransactionListScreen()
    {
        setToolbarEnabled( true );

        transitionTo( transactionListView.getPanel() );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    private void setToolbarEnabled( boolean enabled )
    {
        calenderAddButton.setEnabled( enabled );
        calenderButton.setEnabled( enabled );
        transactionAddButton.setEnabled( enabled );
        configButton.setEnabled( enabled );
    }

    /***************************************************************************
     * @param account
     **************************************************************************/
    private void setAccount( Account account )
    {
        this.account = account;

        accountField.setText( account.getName() );
        summaryPanel.setBalance( calc.calculateBalance( account ) );
        summaryPanel.setAvailable( account.getAvailable() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Budget getBudget()
    {
        budget.setDefaultAccount( account );

        return budget;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TransactionOkListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Transaction trans = transactionView.getTransaction();
            account.addTransaction( trans );
            transactionListView.addTransaction( trans );
            summaryPanel.setBalance( calc.calculateBalance( account ) );
            summaryPanel.setAvailable( account.getAvailable() );
            showTransactionListScreen();
        }
    }

    private class EditTransactionListener implements
        ItemActionListener<Transaction>
    {
        @Override
        public void actionPerformed( ItemActionEvent<Transaction> event )
        {
            Transaction trans = event.getItem();

            transactionView.setTransaction( trans );

            LogUtils.printDebug( "Trans: " + trans.getSecondParty() );
            LogUtils.printDebug( "Amount: " + trans.getAmount() );

            showTransactionScreen();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ShowTransactionListListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showTransactionListScreen();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TransactionAddListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            transactionView.setTransaction( new Transaction() );
            showTransactionScreen();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ConfigButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showAccountConfigScreen();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NameChangedListener implements ItemActionListener<String>
    {
        @Override
        public void actionPerformed( ItemActionEvent<String> event )
        {
            accountField.setText( event.getItem() );
        }
    }
}
