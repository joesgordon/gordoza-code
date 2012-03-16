package org.budgey.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.budgey.BudgeyIconConstants;
import org.budgey.data.Budget;
import org.budgey.data.BudgeyOptions;
import org.budgey.io.BudgetSerializer;
import org.jutils.IconConstants;
import org.jutils.io.FileOpener;
import org.jutils.io.FileOpener.LastDirectorySaver;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.UToolBar;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyFrame extends JFrame
{
    private BudgeyPanel budgeyPanel;
    private BudgeyOptions options;
    private FileOpener<Budget> opener;

    public BudgeyFrame( BudgeyOptions options )
    {
        super();

        this.options = options;
        this.opener = new FileOpener<Budget>( new BudgetSerializer(),
            new OptionsSaver(), "bgt" );

        setContentPane( createContentPane() );
        setIconImages( BudgeyIconConstants.getWalletIcons() );
    }

    private Container createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        budgeyPanel = new BudgeyPanel();

        panel.add( createToolBar(), BorderLayout.NORTH );
        panel.add( budgeyPanel, BorderLayout.CENTER );
        panel.add( new StatusBarPanel().getView(), BorderLayout.SOUTH );

        return panel;
    }

    private Component createToolBar()
    {
        UToolBar toolbar = new UToolBar();

        JButton newButton = new JButton(
            IconConstants.getIcon( IconConstants.NEW_FILE_16 ) );
        newButton.setFocusable( false );
        newButton.addActionListener( new NewButtonListener() );
        toolbar.add( newButton );

        JButton openButton = new JButton(
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        openButton.setFocusable( false );
        openButton.addActionListener( new OpenButtonListener() );
        toolbar.add( openButton );

        JButton saveButton = new JButton(
            IconConstants.getIcon( IconConstants.SAVE_16 ) );
        saveButton.setFocusable( false );
        saveButton.addActionListener( new SaveButtonListener() );
        toolbar.add( saveButton );

        toolbar.addSeparator();

        JButton accountAddButton = new JButton(
            BudgeyIconConstants.getBookAddIcon() );
        accountAddButton.setToolTipText( "Add Account" );
        accountAddButton.setFocusable( false );
        // newButton.addActionListener( new NewButtonListener() );
        toolbar.add( accountAddButton );

        JButton switchButton = new JButton( BudgeyIconConstants.getBookIcon() );
        switchButton.setFocusable( false );
        switchButton.setToolTipText( "View a Different Account" );
        // newButton.addActionListener( new NewButtonListener() );
        toolbar.add( switchButton );

        return toolbar;
    }

    /***************************************************************************
     * @param budget
     **************************************************************************/
    public void setBudget( Budget budget )
    {
        budgeyPanel.setBudget( budget );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showAccountConfigPanel()
    {
        budgeyPanel.showAccountConfigScreen();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OptionsSaver implements LastDirectorySaver
    {
        @Override
        public void saveLastOpenDir( File file )
        {
            options.lastOpenDir = file;
            options.write();
        }

        @Override
        public File getLastOpenDir()
        {
            return options.lastOpenDir;
        }

        @Override
        public void saveLastSaveDir( File file )
        {
            options.lastSaveDir = file;
            options.write();
        }

        @Override
        public File getLastSaveDir()
        {
            return options.lastSaveDir;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NewButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            budgeyPanel.setBudget( new Budget() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Budget b = opener.open( BudgeyFrame.this );

            if( b != null )
            {
                budgeyPanel.setBudget( b );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            opener.save( budgeyPanel.getBudget(), BudgeyFrame.this );
        }
    }
}
