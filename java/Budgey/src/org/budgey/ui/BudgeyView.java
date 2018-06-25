package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.budgey.BudgeyIcons;
import org.budgey.data.*;
import org.budgey.model.BalanceCalculator;
import org.jutils.IconConstants;
import org.jutils.io.LogUtils;
import org.jutils.ui.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyView implements IDataView<Budget>
{
    /**  */
    private final JPanel view;
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
    private Budget budget;
    /**  */
    private Account account;

    /***************************************************************************
     * 
     **************************************************************************/
    public BudgeyView()
    {
        this.view = new JPanel( new GridBagLayout() );

        ShowTransactionListListener doneListener = new ShowTransactionListListener();

        transactionListView = new TransactionListView();
        transactionListView.addEditTransactionListener(
            new EditTransactionListener() );

        transactionView = new TransactionView();
        transactionView.addOkListener( new TransactionOkListener() );
        transactionView.addCancelListener( doneListener );

        configView = new AccountConfigPanel();
        configView.addNameChangedListeners( new NameChangedListener() );
        configView.addOkListener( doneListener );
        configView.addCancelListener( doneListener );

        calenderAddButton = new JButton(
            BudgeyIcons.getCalendarAddIcon() );
        transactionAddButton = new JButton(
            BudgeyIcons.getCoinsAddIcon() );
        calenderButton = new JButton(
            IconConstants.getIcon( IconConstants.CALENDAR_32 ) );
        configButton = new JButton( BudgeyIcons.getCogIcon() );

        accountField = new JLabel( "Account Name" );
        sliderPanel = new CardPanel();

        summaryPanel = new SummaryPanel();

        view.add( createMainPanel(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
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

        titlePanel.add( accountField,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        sliderPanel.addCard( transactionListView.getView() );
        sliderPanel.addCard( transactionView.getView() );

        mainPanel.setBorder( new ShadowBorder() );

        mainPanel.add( titlePanel,
            new GridBagConstraints( 0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        mainPanel.add( createToolBar(),
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        mainPanel.add( createSummaryPanel(),
            new GridBagConstraints( 1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        mainPanel.add( sliderPanel.getView(),
            new GridBagConstraints( 0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createSummaryPanel()
    {
        summaryPanel.setData( new Money( 42964 ), new Money( -65323 ) );

        return summaryPanel.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Budget budget )
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
            transactionListView.setTransactions(
                account.getCurrentMonthsTransactions() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Budget getData()
    {
        budget.setDefaultAccount( account );

        return budget;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
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

        transitionTo( configView.getView() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTransactionScreen()
    {
        setToolbarEnabled( false );

        transitionTo( transactionView.getView() );

        transactionView.requestFocus();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showTransactionListScreen()
    {
        setToolbarEnabled( true );

        transitionTo( transactionListView.getView() );
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
        summaryPanel.setData( BalanceCalculator.calculateBalance( account ),
            account.getAvailable() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TransactionOkListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Transaction trans = transactionView.getData();
            account.addTransaction( trans );
            transactionListView.addTransaction( trans );
            summaryPanel.setData( BalanceCalculator.calculateBalance( account ),
                account.getAvailable() );
            showTransactionListScreen();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class EditTransactionListener
        implements ItemActionListener<Transaction>
    {
        @Override
        public void actionPerformed( ItemActionEvent<Transaction> event )
        {
            Transaction trans = event.getItem();

            transactionView.setData( trans );

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
            transactionView.setData( new Transaction() );
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
