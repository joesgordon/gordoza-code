package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;

public class SnapTester implements IFrameApp
{
    private SnapPanel snapPanel;

    private JPanel mainView;

    private JPanel snapView;

    public SnapTester()
    {
        mainView = createTestPanel( "I'm a shark", null );
        snapView = createTestPanel( "All your ____ are belong to us",
            Color.white );

        snapView.setBackground( Color.white );
    }

    public static String promptForFolder( Component parent )
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

        if( fc.showOpenDialog( parent ) == JFileChooser.APPROVE_OPTION )
        {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    private static JPanel createTestPanel( String text, Color bg )
    {
        JPanel panel = new JPanel();

        // panel.setBorder( BorderFactory.createLineBorder( Color.red ) );
        panel.setBackground( bg );

        panel.add( new JLabel( text ) );

        return panel;
    }

    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();

        frameView.setTitle( "SnapTest" );
        frameView.setContent( createContentPanel() );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 500, 500 );

        return frameView.getView();
    }

    private JPanel createContentPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        snapPanel = new SnapPanel( mainView, snapView );

        panel.add( createToolBar(), BorderLayout.NORTH );
        panel.add( snapPanel.getView(), BorderLayout.CENTER );

        return panel;
    }

    private JToolBar createToolBar()
    {
        JGoodiesToolBar toolbar = new JGoodiesToolBar();
        // JToggleButton mainButton = new JToggleButton( "Add Main" );
        // JToggleButton snapButton = new JToggleButton( "Add Snap" );
        JToggleButton showButton = new JToggleButton( "Show Snap" );

        // mainButton.addActionListener( new MainToggleAction( mainButton ) );
        // snapButton.addActionListener( new SnapToggleAction( snapButton,
        // showButton ) );
        showButton.addActionListener( new ShowToggleAction( showButton ) );

        // mainButton.setFocusable( false );
        // snapButton.setFocusable( false );
        showButton.setFocusable( false );

        // showButton.setEnabled( false );

        // toolbar.add( mainButton );
        // toolbar.addSeparator();
        // toolbar.add( snapButton );
        // toolbar.addSeparator();
        toolbar.add( showButton );

        return toolbar;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new SnapTester() );
    }

    private abstract class ToggleAction implements ActionListener
    {
        private JToggleButton jtb;

        public ToggleAction( JToggleButton button )
        {
            jtb = button;
        }

        @Override
        public final void actionPerformed( ActionEvent e )
        {
            if( jtb.isSelected() )
            {
                doSelectedAction();
            }
            else
            {
                doDeselectedAction();
            }
        }

        protected abstract void doSelectedAction();

        protected abstract void doDeselectedAction();
    }

    private class ShowToggleAction extends ToggleAction
    {
        public ShowToggleAction( JToggleButton button )
        {
            super( button );
        }

        @Override
        protected void doDeselectedAction()
        {
            snapPanel.setSnapViewShowing( false );
        }

        @Override
        protected void doSelectedAction()
        {
            snapPanel.setSnapViewShowing( true );
        }
    }
}
