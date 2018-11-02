package org.jutils.apps.summer.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jutils.SwingUtils;
import org.jutils.apps.summer.SummerIcons;
import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.io.cksum.ChecksumType;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;
import org.jutils.ui.validation.IValidationField;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;
import org.jutils.ui.validation.ValidityUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SummerView implements IView<JFrame>
{
    /**  */
    private final JFrame frame;
    /**  */
    private final JTabbedPane tabField;
    /**  */
    private final CreateView createView;
    /**  */
    private final VerifyView verifyView;
    /**  */
    private final Action createAction;

    /***************************************************************************
     * 
     **************************************************************************/
    public SummerView()
    {
        this.tabField = new JTabbedPane();
        this.createView = new CreateView();
        this.verifyView = new VerifyView();
        this.createAction = new ActionAdapter( ( e ) -> runSummer(),
            "Create Checksums",
            SummerIcons.loader.getIcon( SummerIcons.SUMMER_016 ) );

        this.frame = createFrame();

        createView.addValidityChanged( new ValidityChanged( this, 0 ) );
        verifyView.addValidityChanged( new ValidityChanged( this, 1 ) );

        ChecksumResult input = new ChecksumResult();

        verifyView.setData( new ChecksumResult( input ) );

        input.type = ChecksumType.MD5;

        createView.setData( input );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frame.setTitle( "Check Summer" );
        frame.setIconImages( SummerIcons.getSummerImages() );
        frame.setSize( 650, 650 );

        frameView.setContent( createContentPanel() );
        frameView.setToolbar( createToolbar() );

        JMenu fileMenu = frameView.getFileMenu();
        JMenuItem item = new JMenuItem( createAction );

        fileMenu.add( item, 0 );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Container createContentPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        tabField.addTab( "Create", createView.getView() );
        tabField.addTab( "Verify", verifyView.getView() );

        tabField.addChangeListener( new TabListener( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( tabField, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        SwingUtils.addActionToToolbar( toolbar, createAction );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void runSummer()
    {
        int idx = tabField.getSelectedIndex();

        switch( idx )
        {
            case 0:
                createView.runCreate();
                break;

            case 1:
                verifyView.runVerify();
                break;

            default:
                break;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TabListener implements ChangeListener
    {
        private final SummerView view;

        public TabListener( SummerView view )
        {
            this.view = view;
        }

        @Override
        public void stateChanged( ChangeEvent e )
        {
            IValidationField field = null;

            if( view.tabField.getSelectedIndex() == 0 )
            {
                field = view.createView;
            }
            else
            {
                field = view.verifyView;
            }

            if( field.getValidity().isValid )
            {
                view.createAction.setEnabled( true );
                SwingUtils.setActionToolTip( view.createAction,
                    "Create Checksums" );
            }
            else
            {
                view.createAction.setEnabled( false );
                SwingUtils.setActionToolTip( view.createAction,
                    field.getValidity().reason );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValidityChanged implements IValidityChangedListener
    {
        private final SummerView view;
        private int index;

        public ValidityChanged( SummerView view, int index )
        {
            this.view = view;
            this.index = index;
        }

        @Override
        public void signalValidity( Validity v )
        {
            if( view.tabField.getSelectedIndex() == index )
            {
                ValidityUtils.setActionValidity( view.createAction, v,
                    "Create Checksums" );

                // LogUtils.printDebug( "Validity @ index[%d]: %s", index,
                // validity.toString() );
            }
        }
    }
}
