package testbed.slider;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.apps.filespy.FileSpyMain;
import org.jutils.apps.filespy.ui.RegexPanel;
import org.jutils.apps.filespy.ui.SearchView;
import org.jutils.ui.*;

public class SliderTestFrame extends JFrame
{
    private SliderPanel sliderPanel;
    private JComboBox<Integer> comboBox;
    private JButton lastButton;
    private JButton nextButton;

    public SliderTestFrame()
    {
        super();

        super.setContentPane( createContentPane() );
        resetButtonStates();
    }

    private Container createContentPane()
    {
        JPanel contentPane = new JPanel( new BorderLayout() );
        StatusBarPanel statusBar = new StatusBarPanel();

        contentPane.add( createToolBar(), BorderLayout.NORTH );
        contentPane.add( createSliderPanel( statusBar ), BorderLayout.CENTER );
        contentPane.add( statusBar.getView(), BorderLayout.SOUTH );

        return contentPane;
    }

    private JToolBar createToolBar()
    {
        JGoodiesToolBar toolbar = new JGoodiesToolBar();

        lastButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.BACK_24 ) );
        lastButton.setFocusable( false );
        lastButton.addActionListener( new LastListener() );
        toolbar.add( lastButton );

        nextButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.FORWARD_24 ) );
        nextButton.setFocusable( false );
        nextButton.addActionListener( new NextListener() );
        toolbar.add( nextButton );

        JComponent comboPanel = new JPanel( new GridBagLayout() );
        comboBox = new JComboBox<Integer>();
        comboBox.addItem( new Integer( 0 ) );
        comboBox.addItem( new Integer( 1 ) );
        comboBox.addItem( new Integer( 2 ) );
        comboBox.addItem( new Integer( 3 ) );
        comboBox.setFocusable( false );
        Dimension size = comboBox.getPreferredSize();
        size.width = 75;
        comboBox.setPreferredSize( size );
        comboBox.addActionListener( new ComboListener() );
        comboPanel.add( comboBox,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        comboPanel.setOpaque( false );
        comboPanel.setMaximumSize( size );
        toolbar.addSeparator( new Dimension( 4, 0 ) );
        toolbar.add( comboPanel );

        return toolbar;
    }

    private Component createSliderPanel( StatusBarPanel statusBar )
    {
        sliderPanel = new SliderPanel();

        sliderPanel.addPanel( createPanel1() );
        sliderPanel.addPanel( createPanel2() );
        sliderPanel.addPanel( createPanel3( statusBar ) );
        sliderPanel.addPanel( createPanel4() );

        return sliderPanel;
    }

    private JPanel createPanel1()
    {
        JPanel panel = new GradientPanel();
        panel.setBackground( Color.red );
        return panel;
    }

    private JPanel createPanel2()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        DirectoryTree dirTree = new DirectoryTree();
        JScrollPane jsp = new JScrollPane( dirTree.getView() );
        panel.setBorder( BorderFactory.createRaisedBevelBorder() );

        panel.add( jsp, BorderLayout.CENTER );

        return panel;
    }

    private JPanel createPanel3( StatusBarPanel statusBar )
    {
        SearchView panel = new SearchView( statusBar,
            FileSpyMain.getOptions() );

        return panel.getView();
    }

    private JPanel createPanel4()
    {
        return new RegexPanel().getView();
    }

    private void resetButtonStates()
    {
        lastButton.setEnabled( sliderPanel.canMoveBackward() );
        nextButton.setEnabled( sliderPanel.canMoveForward() );
        comboBox.setSelectedIndex( sliderPanel.getCurrentPanel() );
    }

    private class ComboListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( comboBox.isPopupVisible() )
            {
                sliderPanel.setPanel( ( Integer )comboBox.getSelectedItem() );
                resetButtonStates();
            }
        }
    }

    private class LastListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            sliderPanel.last();
            resetButtonStates();
        }
    }

    private class NextListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            sliderPanel.next();
            resetButtonStates();
        }
    }
}
