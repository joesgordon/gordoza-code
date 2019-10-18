package testbed.propEdit;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.jutils.OptionUtils;
import org.jutils.SwingUtils;
import org.jutils.ui.PropEditPanel;
import org.jutils.ui.app.FrameRunner;
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

        showButton.addActionListener( ( e ) -> showMessage( e.getSource() ) );
        refreshButton.addActionListener( ( e ) -> refreshUI() );
        openButton.addActionListener( ( e ) -> showOpen( e.getSource() ) );

        SwingUtils.setMaxComponentSize( showButton, refreshButton, openButton );

        panel.add( showButton );
        panel.add( openButton );
        panel.add( refreshButton );

        return panel;
    }

    private void refreshUI()
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                JFrame frame = ( JFrame )SwingUtilities.getAncestorOfClass(
                    JFrame.class, panel.getView() );

                SwingUtilities.updateComponentTreeUI( frame );

                frame.invalidate();
                frame.validate();
                frame.repaint();

                panel.setProperties( UIManager.getDefaults() );
            }
        } );
    }

    /**
     * @param source
     */
    private void showOpen( Object source )
    {
        JFileChooser jfc = new JFileChooser();

        jfc.showSaveDialog( ( Component )source );
    }

    /**
     * @param source
     */
    private void showMessage( Object source )
    {
        OptionUtils.showInfoMessage( ( Component )source, "Here is a message",
            "Here is a title" );
    }

    /**
     * @param propPanel
     * @return
     */
    private JComponent createContentPane( PropEditPanel propPanel )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        JScrollPane scrollPane = new JScrollPane( propPanel.getView() );

        panel.add( createToolbar(), BorderLayout.NORTH );
        panel.add( scrollPane, BorderLayout.CENTER );

        return panel;
    }

    /**
     * @{@inheritDoc}
     */
    @Override
    public void finalizeGui()
    {
    }

    /**
     * @param args
     */
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

        FrameRunner.invokeLater( new PropEditUiMain(), true );
    }
}
