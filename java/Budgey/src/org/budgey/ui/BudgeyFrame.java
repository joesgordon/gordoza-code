package org.budgey.ui;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.budgey.BudgeyIcons;
import org.budgey.BudgeyMain;
import org.budgey.data.Budget;
import org.budgey.data.BudgeyOptions;
import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.io.xs.XsUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.model.IDataView;
import org.jutils.core.ui.model.IView;

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

        frame.setIconImages( BudgeyIcons.getAppImages() );
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
     * Creates the toolbar for this frame.
     * @return the toolbar for this frame.
     **************************************************************************/
    private JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createNewAction() );
        SwingUtils.addActionToToolbar( toolbar, createOpenAction() );
        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );

        toolbar.addSeparator();

        JButton accountAddButton = new JButton(
            BudgeyIcons.getIcon( BudgeyIcons.BOOK_ADD_32 ) );
        accountAddButton.setToolTipText( "Add Account" );
        accountAddButton.setFocusable( false );
        // newButton.addActionListener( new NewButtonListener() );
        toolbar.add( accountAddButton );

        JButton switchButton = new JButton(
            BudgeyIcons.getIcon( BudgeyIcons.BOOK_16 ) );
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
        Icon icon = IconConstants.getIcon( IconConstants.NEW_FILE_16 );
        ActionListener listener = ( e ) -> setBudget( new Budget() );
        return new ActionAdapter( listener, "New Budget", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createOpenAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
        IFileSelected ifs = ( f ) -> openFile( f );
        ILastFile ils = () -> options.getOptions().lastBudgets.first();
        ActionListener listener = new FileChooserListener( getView(),
            "Open Budget", false, ifs, ils );
        return new ActionAdapter( listener, "Open Budget", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createSaveAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
        IFileSelected ifs = ( f ) -> saveFile( f );
        ILastFile ils = () -> options.getOptions().lastBudgets.first();
        ActionListener listener = new FileChooserListener( getView(),
            "Save Budget", true, ifs, ils );
        return new ActionAdapter( listener, "Save Budget", icon );
    }

    /***************************************************************************
     * @param budget
     **************************************************************************/
    public void setBudget( Budget budget )
    {
        budgeyPanel.setData( budget );
    }

    private void openFile( File file )
    {
        options.getOptions().lastBudgets.push( file );

        try
        {
            Budget b = XsUtils.readObjectXStream( file );
            budgeyPanel.setData( b );
        }
        catch( ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "Data Error" );
        }
        catch( FileNotFoundException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "File Not Found Error" );
        }
        catch( IOException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "I/O Error" );
        }
    }

    /**
     * @param file
     */
    private void saveFile( File file )
    {
        options.getOptions().lastBudgets.push( file );

        try
        {
            XsUtils.writeObjectXStream( budgeyPanel.getData(), file );
        }
        catch( IOException | ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                "I/O Error" );
        }
    }
}
