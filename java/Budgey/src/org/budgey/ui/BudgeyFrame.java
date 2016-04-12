package org.budgey.ui;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

import org.budgey.BudgeyIconConstants;
import org.budgey.BudgeyMain;
import org.budgey.data.Budget;
import org.budgey.data.BudgeyOptions;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.XStreamUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.IView;

import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BudgeyFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView frameView;
    /**  */
    private final IDataView<Budget> budgeyPanel;
    /**  */
    private final OptionsSerializer<BudgeyOptions> options;

    /***************************************************************************
     * 
     **************************************************************************/
    public BudgeyFrame()
    {
        super();

        this.frameView = new StandardFrameView();
        this.budgeyPanel = new BudgeyView();
        this.options = BudgeyMain.getOptions();

        frameView.setContent( ( Container )budgeyPanel.getView() );
        frameView.setToolbar( createToolBar() );

        JFrame frame = frameView.getView();

        frame.setSize( 640, 480 );
        frame.setTitle( "Budgey" );
        setBudget( new Budget() );

        frame.setIconImages( BudgeyIconConstants.getWalletIcons() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createNewAction() );
        SwingUtils.addActionToToolbar( toolbar, createOpenAction() );
        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

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
     * @return
     **************************************************************************/
    private Action createNewAction()
    {
        Icon icon = IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 );
        ActionListener listener = ( e ) -> setBudget( new Budget() );
        return new ActionAdapter( listener, "New Budget", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Icon icon = IconConstants.loader.getIcon(
            IconConstants.OPEN_FOLDER_16 );
        ActionListener listener = new FileChooserListener( getView(),
            "Open Budget", new OpenButtonListener(), false );
        return new ActionAdapter( listener, "Open Budget", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Icon icon = IconConstants.loader.getIcon( IconConstants.SAVE_16 );
        ActionListener listener = new FileChooserListener( getView(),
            "Save Budget", new SaveButtonListener(), true );
        return new ActionAdapter( listener, "Save Budget", icon );
    }

    /***************************************************************************
     * @param budget
     **************************************************************************/
    public void setBudget( Budget budget )
    {
        budgeyPanel.setData( budget );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenButtonListener implements IFileSelectionListener
    {
        @Override
        public File getDefaultFile()
        {
            return options.getOptions().lastBudgets.first();
        }

        @Override
        public void filesChosen( File [] files )
        {
            File file = files[0];

            options.getOptions().lastBudgets.push( file );

            try
            {
                Budget b = XStreamUtils.readObjectXStream( file );
                budgeyPanel.setData( b );
            }
            catch( XStreamException ex )
            {
                JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE );
            }
            catch( FileNotFoundException ex )
            {
                JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                    "File Not Found Error", JOptionPane.ERROR_MESSAGE );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                    "I/O Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveButtonListener implements IFileSelectionListener
    {
        @Override
        public File getDefaultFile()
        {
            return options.getOptions().lastBudgets.first();
        }

        @Override
        public void filesChosen( File [] files )
        {
            File file = files[0];

            options.getOptions().lastBudgets.push( file );

            try
            {
                XStreamUtils.writeObjectXStream( budgeyPanel.getData(), file );
            }
            catch( XStreamException ex )
            {
                JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE );
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( getView(), ex.getMessage(),
                    "I/O Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }
}
