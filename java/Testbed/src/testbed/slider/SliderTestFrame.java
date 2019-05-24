package testbed.slider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.jutils.IconConstants;
import org.jutils.apps.filespy.ui.RegexPanel;
import org.jutils.apps.filespy.ui.SearchParamsView;
import org.jutils.ui.DirectoryTree;
import org.jutils.ui.GradientPanel;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;

import testbed.SliderPanel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SliderTestFrame implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final SliderPanel sliderPanel;
    /**  */
    private final JComboBox<Integer> comboBox;
    /**  */
    private final JButton lastButton;
    /**  */
    private final JButton nextButton;

    /***************************************************************************
     * 
     **************************************************************************/
    public SliderTestFrame()
    {
        this.view = new StandardFrameView();
        this.sliderPanel = new SliderPanel();
        this.comboBox = new JComboBox<>();
        this.lastButton = new JButton();
        this.nextButton = new JButton();

        view.setToolbar( createToolBar() );
        view.setContent( createSliderPanel() );
        resetButtonStates();
    }

    private JToolBar createToolBar()
    {
        JGoodiesToolBar toolbar = new JGoodiesToolBar();

        lastButton.setIcon(
            IconConstants.getIcon( IconConstants.NAV_PREVIOUS_24 ) );
        lastButton.setFocusable( false );
        lastButton.addActionListener( new LastListener() );
        toolbar.add( lastButton );

        nextButton.setIcon(
            IconConstants.getIcon( IconConstants.NAV_NEXT_24 ) );
        nextButton.setFocusable( false );
        nextButton.addActionListener( new NextListener() );
        toolbar.add( nextButton );

        JComponent comboPanel = new JPanel( new GridBagLayout() );
        comboBox.addItem( Integer.valueOf( 0 ) );
        comboBox.addItem( Integer.valueOf( 1 ) );
        comboBox.addItem( Integer.valueOf( 2 ) );
        comboBox.addItem( Integer.valueOf( 3 ) );
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

    private JComponent createSliderPanel()
    {
        sliderPanel.addPanel( createPanel1() );
        sliderPanel.addPanel( createPanel2() );
        sliderPanel.addPanel( createPanel3() );
        sliderPanel.addPanel( createPanel4() );

        return sliderPanel;
    }

    private static JPanel createPanel1()
    {
        JPanel panel = new GradientPanel();
        panel.setBackground( Color.red );
        return panel;
    }

    private static JPanel createPanel2()
    {
        JPanel panel = new JPanel( new BorderLayout() );
        DirectoryTree dirTree = new DirectoryTree();
        JScrollPane jsp = new JScrollPane( dirTree.getView() );
        panel.setBorder( BorderFactory.createRaisedBevelBorder() );

        panel.add( jsp, BorderLayout.CENTER );

        return panel;
    }

    private static JPanel createPanel3()
    {
        SearchParamsView panel = new SearchParamsView();

        return panel.getView();
    }

    private static JPanel createPanel4()
    {
        return new RegexPanel().getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void resetButtonStates()
    {
        lastButton.setEnabled( sliderPanel.canMoveBackward() );
        nextButton.setEnabled( sliderPanel.canMoveForward() );
        comboBox.setSelectedIndex( sliderPanel.getCurrentPanel() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    private class LastListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            sliderPanel.last();
            resetButtonStates();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
