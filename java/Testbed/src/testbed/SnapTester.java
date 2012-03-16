package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.UToolBar;

public class SnapTester extends FrameRunner
{
    private USnapPanel snapPanel;

    private JPanel mainView;

    private JPanel snapView;

    public SnapTester()
    {
        mainView = createTestPanel( "I'm a shark", null );
        snapView = createTestPanel( "All your ____ are belong to us",
            Color.white );

        snapView.setBackground( Color.white );
    }

    public String promptForFolder( Component parent )
    {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

        if( fc.showOpenDialog( parent ) == JFileChooser.APPROVE_OPTION )
        {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    private JPanel createTestPanel( String text, Color bg )
    {
        JPanel panel = new JPanel();

        // panel.setBorder( BorderFactory.createLineBorder( Color.red ) );
        panel.setBackground( bg );

        panel.add( new JLabel( text ) );

        return panel;
    }

    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame( "SnapTest" );

        frame.setContentPane( createContentPanel() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        return frame;
    }

    private JPanel createContentPanel()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        snapPanel = new USnapPanel( mainView, snapView );

        panel.add( createToolBar(), BorderLayout.NORTH );
        panel.add( snapPanel, BorderLayout.CENTER );

        return panel;
    }

    private JToolBar createToolBar()
    {
        UToolBar toolbar = new UToolBar();
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
    protected boolean validate()
    {
        return true;
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new SnapTester() );
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

            System.out.println( "Selected dir " + promptForFolder( snapPanel ) );
        }

        protected abstract void doSelectedAction();

        protected abstract void doDeselectedAction();
    }

    @SuppressWarnings( "unused")
    private class MainToggleAction extends ToggleAction
    {
        public MainToggleAction( JToggleButton button )
        {
            super( button );
        }

        @Override
        protected void doDeselectedAction()
        {
            snapPanel.setMainView( null );
        }

        @Override
        protected void doSelectedAction()
        {
            snapPanel.setMainView( mainView );
        }
    }

    @SuppressWarnings( "unused")
    private class SnapToggleAction extends ToggleAction
    {
        private JToggleButton otherButton;

        public SnapToggleAction( JToggleButton button, JToggleButton otherButton )
        {
            super( button );
            this.otherButton = otherButton;
        }

        @Override
        protected void doDeselectedAction()
        {
            otherButton.setSelected( false );
            snapPanel.setSnapView( null );
            otherButton.setEnabled( false );
        }

        @Override
        protected void doSelectedAction()
        {
            snapPanel.setSnapView( snapView );
            otherButton.setEnabled( true );
        }
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
