package testbed.propEdit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.Utils;
import org.jutils.ui.PropEditPanel;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

public class PropEditUiMain implements IFrameApp
{
    private PropEditPanel panel;

    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();
        panel = new PropEditPanel();

        UIDefaults defaults = UIManager.getDefaults();

        panel.setProperties( defaults );

        frame.setContentPane( createContentPane( panel ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        return frame;
    }

    private JComponent createToolbar()
    {
        JPanel panel = new JPanel();
        JButton showButton = new JButton( "Show Message Dialog" );
        JButton refreshButton = new JButton( "Refresh UI" );
        JButton openButton = new JButton( "Show Open Dialog" );

        showButton.addActionListener( new ShowMsgListener() );
        refreshButton.addActionListener( new RefreshUiListener( this ) );
        openButton.addActionListener( new OpenListener() );

        Utils.setMaxComponentSize( showButton, refreshButton, openButton );

        panel.add( showButton );
        panel.add( openButton );
        panel.add( refreshButton );

        return panel;
    }

    private JComponent createContentPane( PropEditPanel propPanel )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane scrollPane = new JScrollPane( propPanel.getView() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( scrollPane, BorderLayout.CENTER );

        return panel;
    }

    @Override
    public void finalizeGui()
    {
    }

    public static void main( String[] args )
    {
        // Color stadBg = new Color( 0x808080 );
        //
        // UIManager.put( "Panel.background", stadBg );
        // UIManager.put( "RadioButton.background", stadBg );
        // UIManager.put( "ColorChooser.background", stadBg );
        // UIManager.put( "TabbedPane.background", stadBg );
        // UIManager.put( "Slider.background", stadBg );
        // UIManager.put( "CheckBox.background", stadBg );
        // UIManager.put( "Button.background", Color.black );
        // UIManager.put( "Button.foreground", Color.white );
        // UIManager.put( "RadioButtonMenuItem.background", stadBg );
        // UIManager.put( "ToolBar.background", stadBg );
        // UIManager.put( "OptionPane.background", stadBg );

        FrameApplication.invokeLater( new PropEditUiMain(), true );
    }

    private static class ShowMsgListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JOptionPane.showMessageDialog( ( Component )e.getSource(),
                "Here is a message" );
        }
    }

    private static class OpenListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFileChooser jfc = new JFileChooser();

            jfc.showSaveDialog( ( Component )e.getSource() );
        }
    }

    private static class RefreshUiListener implements ActionListener
    {
        private final PropEditUiMain pem;

        public RefreshUiListener( PropEditUiMain pem )
        {
            this.pem = pem;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            SwingUtilities.invokeLater( new Runnable()
            {
                @Override
                public void run()
                {
                    JFrame frame = ( JFrame )SwingUtilities.getAncestorOfClass(
                        JFrame.class, pem.panel.getView() );

                    SwingUtilities.updateComponentTreeUI( frame );

                    frame.invalidate();
                    frame.validate();
                    frame.repaint();

                    pem.panel.setProperties( UIManager.getDefaults() );
                }
            } );
        }
    }
}
