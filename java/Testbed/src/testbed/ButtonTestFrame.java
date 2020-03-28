package testbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.ui.GradientButtonUI;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;

/**
 * 
 */
public class ButtonTestFrame implements IFrameApp
{
    /**
     * @{@inheritDoc}
     */
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();

        frame.setContentPane( createContentPane() );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        return frame;
    }

    /**
     * @return
     */
    private static JComponent createToolbar()
    {
        JPanel panel = new JPanel();
        JButton showButton = new JButton( "Show Message Dialog" );
        JButton refreshButton = new JButton( "Refresh UI" );
        JButton openButton = new JButton( "Show Open Dialog" );

        showButton.addActionListener( ( e ) -> showMessage( e.getSource() ) );
        refreshButton.addActionListener( ( e ) -> refreshUi( e.getSource() ) );
        openButton.addActionListener(
            ( e ) -> showOpenDialog( e.getSource() ) );

        panel.add( showButton );
        panel.add( openButton );
        panel.add( refreshButton );

        return panel;
    }

    /**
     * @param source
     */
    private static void showMessage( Object source )
    {
        OptionUtils.showInfoMessage( ( Component )source, "Here is a message",
            "Here is a title" );
    }

    /**
     * @param source
     */
    private static void showOpenDialog( Object source )
    {
        JFileChooser jfc = new JFileChooser();

        jfc.showSaveDialog( ( Component )source );
    }

    /**
     * @param source
     */
    private static void refreshUi( Object source )
    {
        JFrame frame = SwingUtils.getComponentsJFrame( ( Component )source );

        SwingUtilities.updateComponentTreeUI( frame );

        frame.validate();
    }

    /**
     * @return
     */
    private static JComponent createContentPane()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( Box.createHorizontalStrut( 0 ), BorderLayout.CENTER );

        return panel;
    }

    @Override
    public void finalizeGui()
    {
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
        UIManager.put( "RadioButtonMenuItem.background",
            new Color( 0x808080 ) );
        UIManager.put( "ToolBar.background", new Color( 0x808080 ) );

        LogUtils.printDebug(
            "ButtonUI = " + UIManager.getDefaults().getString( "ButtonUI" ) );

        FrameRunner.invokeLater( new ButtonTestFrame(), true,
            UIManager.getCrossPlatformLookAndFeelClassName() );
    }
}
