package utesting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.PropEditPanel;

public class PropEditMain extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();
        PropEditPanel panel = new PropEditPanel();

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
        refreshButton.addActionListener( new RefreshUiListener() );
        openButton.addActionListener( new OpenListener() );

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
    protected String getLookAndFeelName()
    {
        return UIManager.getCrossPlatformLookAndFeelClassName();
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    public static void main( String[] args )
    {
        Color stadBg = new Color( 0x808080 );

        UIManager.put( "Panel.background", stadBg );
        UIManager.put( "RadioButton.background", stadBg );
        UIManager.put( "ColorChooser.background", stadBg );
        UIManager.put( "TabbedPane.background", stadBg );
        UIManager.put( "Slider.background", stadBg );
        UIManager.put( "CheckBox.background", stadBg );
        UIManager.put( "Button.background", Color.black );
        UIManager.put( "Button.foreground", Color.white );
        UIManager.put( "RadioButtonMenuItem.background", stadBg );
        UIManager.put( "ToolBar.background", stadBg );
        UIManager.put( "OptionPane.background", stadBg );

        SwingUtilities.invokeLater( new PropEditMain() );
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
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JFrame frame = ( JFrame )SwingUtilities.getAncestorOfClass(
                JFrame.class, ( Component )e.getSource() );

            SwingUtilities.updateComponentTreeUI( frame );

            frame.validate();
        }
    }
}
