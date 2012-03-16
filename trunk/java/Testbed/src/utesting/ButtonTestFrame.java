package utesting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.GradientButtonUI;

public class ButtonTestFrame extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();

        frame.setContentPane( createContentPane() );

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

    private JComponent createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( Box.createHorizontalStrut( 0 ), BorderLayout.CENTER );

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

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        UIManager.put( "ButtonUI", GradientButtonUI.class.getName() );
        UIManager.put( "Panel.background", new Color( 0x808080 ) );
        UIManager.put( "RadioButton.background", new Color( 0x808080 ) );
        UIManager.put( "ColorChooser.background", new Color( 0x808080 ) );
        UIManager.put( "TabbedPane.background", new Color( 0x808080 ) );
        UIManager.put( "Slider.background", new Color( 0x808080 ) );
        UIManager.put( "CheckBox.background", new Color( 0x808080 ) );
        UIManager.put( "Button.background", Color.black );
        UIManager.put( "Button.foreground", Color.white );
        UIManager.put( "RadioButtonMenuItem.background", new Color( 0x808080 ) );
        UIManager.put( "ToolBar.background", new Color( 0x808080 ) );

        System.out.println( "ButtonUI = " +
            UIManager.getDefaults().getString( "ButtonUI" ) );

        SwingUtilities.invokeLater( new ButtonTestFrame() );
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
